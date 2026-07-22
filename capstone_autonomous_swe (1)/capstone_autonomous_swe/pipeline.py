"""
pipeline.py

Component orchestrator for "The Autonomous SWE" capstone.

Wires together every component into one LangGraph state machine:

    retrieve_context -> coder -> reviewer -> [retry -> coder | escalate | hitl_gate]
    hitl_gate -> [commit | reject]

Run:
    python pipeline.py "Fix the divide-by-zero bug in calculator.py" [--live]

By default the pipeline runs in DEMO_MODE (no network calls to the
Anthropic API, deterministic reject-then-approve retry cycle and
auto-approved HITL gate) so it is fully reproducible for grading and
for the Loom recording. Pass --live to use real Anthropic API calls
and an interactive HITL prompt.

Requires (for --live mode): ANTHROPIC_API_KEY set via a .env file
(see .env.example) and loaded through python-dotenv - never
hardcoded in source.
"""

from __future__ import annotations

import asyncio
import shutil
import sys
from pathlib import Path
from typing import Optional, TypedDict

from dotenv import load_dotenv
from langgraph.graph import END, StateGraph

from agents import run_coder, run_reviewer
from codebase_rag import index_repo, retrieve_context
from graceful_degrader import generate_escalation_report
from hitl_gate import request_human_approval
from mcp_client import MCPToolClient
from token_logger import TokenLogger

load_dotenv()

REPO_DIR = "mock_buggy_repo"
STAGING_DIR = "workspace/staging"
MAX_ITERATIONS = 3


class CoderState(TypedDict):
    feature_request: str
    target_file: str
    codebase_context: str
    current_code: str
    staged_path: str
    review_status: str
    reviewer_feedback: str
    iteration: int
    max_iterations: int
    demo_mode: bool
    human_decision: Optional[str]
    final_status: Optional[str]


def make_pipeline(logger: TokenLogger):
    """Build and compile the LangGraph state machine, closing over the shared logger."""

    async def retrieve_context_node(state: CoderState) -> CoderState:
        ctx = retrieve_context(state["feature_request"], k=3)
        return {**state, "codebase_context": ctx}

    async def coder_node(state: CoderState) -> CoderState:
        # Coder reads the current file via MCP, then writes its candidate
        # fix to the staging area via MCP write_file - never a raw open().
        async with MCPToolClient() as mcp:
            current_code = await mcp.call("read_file", path=f"{REPO_DIR}/{state['target_file']}")
            generated = run_coder(
                feature_request=state["feature_request"],
                codebase_context=state["codebase_context"],
                current_code=current_code,
                reviewer_feedback=state["reviewer_feedback"],
                logger=logger,
                demo_mode=state["demo_mode"],
                iteration=state["iteration"],
            )
            staged_path = f"{STAGING_DIR}/{state['target_file']}"
            await mcp.call("write_file", path=staged_path, content=generated)
        return {**state, "current_code": generated, "staged_path": staged_path}

    async def reviewer_node(state: CoderState) -> CoderState:
        # Reviewer never receives code as a state field - it fetches its
        # own copy from disk via MCP read_file, independently of the Coder.
        async with MCPToolClient() as mcp:
            candidate_code = await mcp.call("read_file", path=state["staged_path"])
        status, feedback = run_reviewer(
            feature_request=state["feature_request"],
            candidate_code=candidate_code,
            logger=logger,
            demo_mode=state["demo_mode"],
        )
        return {
            **state,
            "review_status": status,
            "reviewer_feedback": feedback,
            "iteration": state["iteration"] + 1,
        }

    def should_retry(state: CoderState) -> str:
        if state["review_status"] == "APPROVED":
            return "hitl_gate"
        if state["iteration"] <= state["max_iterations"]:
            return "coder"
        return "escalate"

    async def escalate_node(state: CoderState) -> CoderState:
        generate_escalation_report(
            feature_request=state["feature_request"],
            target_file=state["target_file"],
            iterations_attempted=state["iteration"] - 1,
            max_iterations=state["max_iterations"],
            last_reviewer_feedback=state["reviewer_feedback"],
            last_generated_code=state["current_code"],
        )
        logger.log(model="n/a", input_tokens=0, output_tokens=0, node="escalate", final_status="ESCALATED")
        return {**state, "final_status": "ESCALATED"}

    async def hitl_gate_node(state: CoderState) -> CoderState:
        async with MCPToolClient() as mcp:
            candidate_code = await mcp.call("read_file", path=state["staged_path"])
        approved = request_human_approval(candidate_code, demo_mode=state["demo_mode"])
        return {**state, "human_decision": "APPROVED" if approved else "REJECTED"}

    def after_hitl(state: CoderState) -> str:
        return "commit" if state["human_decision"] == "APPROVED" else "reject"

    async def commit_node(state: CoderState) -> CoderState:
        final_path = Path(REPO_DIR) / state["target_file"]
        shutil.copyfile(state["staged_path"], final_path)
        logger.log(
            model="n/a",
            input_tokens=0,
            output_tokens=0,
            node="commit",
            human_decision="APPROVED",
            final_status="COMMITTED",
        )
        return {**state, "final_status": "COMMITTED"}

    async def reject_node(state: CoderState) -> CoderState:
        # No file output on rejection - staged copy is discarded, nothing
        # is written into the real repo - but the decision is still logged.
        Path(state["staged_path"]).unlink(missing_ok=True)
        logger.log(
            model="n/a",
            input_tokens=0,
            output_tokens=0,
            node="reject",
            human_decision="REJECTED",
            final_status="REJECTED",
        )
        return {**state, "final_status": "REJECTED"}

    graph = StateGraph(CoderState)
    graph.add_node("retrieve_context", retrieve_context_node)
    graph.add_node("coder", coder_node)
    graph.add_node("reviewer", reviewer_node)
    graph.add_node("escalate", escalate_node)
    graph.add_node("hitl_gate", hitl_gate_node)
    graph.add_node("commit", commit_node)
    graph.add_node("reject", reject_node)

    graph.set_entry_point("retrieve_context")
    graph.add_edge("retrieve_context", "coder")
    graph.add_edge("coder", "reviewer")
    graph.add_conditional_edges("reviewer", should_retry, {"coder": "coder", "hitl_gate": "hitl_gate", "escalate": "escalate"})
    graph.add_conditional_edges("hitl_gate", after_hitl, {"commit": "commit", "reject": "reject"})
    graph.add_edge("escalate", END)
    graph.add_edge("commit", END)
    graph.add_edge("reject", END)

    return graph.compile()


async def run_pipeline(feature_request: str, target_file: str = "calculator.py", demo_mode: bool = True) -> None:
    print(f"Indexing {REPO_DIR} into the RAG vector store...")
    n = index_repo(REPO_DIR)
    print(f"Indexed {n} code chunks.\n")

    logger = TokenLogger()
    app = make_pipeline(logger)

    initial_state: CoderState = {
        "feature_request": feature_request,
        "target_file": target_file,
        "codebase_context": "",
        "current_code": "",
        "staged_path": "",
        "review_status": "PENDING",
        "reviewer_feedback": "",
        "iteration": 1,
        "max_iterations": MAX_ITERATIONS,
        "demo_mode": demo_mode,
        "human_decision": None,
        "final_status": None,
    }

    final_state = await app.ainvoke(initial_state)

    print(f"\nPipeline finished with status: {final_state['final_status']}")
    print(f"Total tokens consumed: {logger.total_tokens()}")


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print('Usage: python pipeline.py "<feature request>" [--live]')
        sys.exit(1)
    feature_request_arg = sys.argv[1]
    live = "--live" in sys.argv
    asyncio.run(run_pipeline(feature_request_arg, demo_mode=not live))

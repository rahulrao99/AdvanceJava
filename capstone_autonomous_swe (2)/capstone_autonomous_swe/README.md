# The Autonomous SWE — End-to-End Feature Factory

Capstone project (Weeks 5 & 6) integrating every prior week's work into one
pipeline: a plain-language feature request goes in, and reviewed,
human-approved, committed code comes out — with full observability.

## Architecture

```
feature request
      |
      v
retrieve_context  (codebase_rag.py — ChromaDB, Component 1)
      |
      v
   coder  <---------------------.   (agents.py + mcp_client.py, Component 2/3)
      |                          |   writes candidate via MCP write_file
      v                          |
  reviewer ---- REJECTED, retry -'   reads candidate via MCP read_file
      |
   APPROVED
      |
      v
  hitl_gate      (hitl_gate.py, Component 4 — human sees >=20 lines, decides)
      |
   -------------------
   |                 |
 APPROVED          REJECTED
   |                 |
   v                 v
 commit           reject
(writes final    (no file written,
 file, logs)      still logs tokens)
```

If the coder/reviewer loop exceeds `MAX_ITERATIONS` without approval, the
`escalate` node fires instead of looping forever (`graceful_degrader.py`),
writing a structured `escalation_report.json`.

## Files

| File | Purpose |
|---|---|
| `codebase_rag.py` | Component 1 — indexes `mock_buggy_repo/` into ChromaDB, retrieves top-k relevant chunks per request |
| `mcp_server.py` | Component 2 — MCP stdio server: `read_file`, `write_file`, `list_files`, `execute_code`, path-traversal safe |
| `mcp_client.py` | Spawns `mcp_server.py` as a subprocess and calls its tools (never imported as a shared module) |
| `agents.py` | Component 3 — Coder and Reviewer system prompts + LLM calls |
| `graceful_degrader.py` | Structured escalation report when the retry loop hits its limit |
| `hitl_gate.py` | Component 4 — human commit-approval checkpoint with a mandatory code preview |
| `token_logger.py` | Component 5 — per-call token accounting, written to `token_log.json` |
| `pipeline.py` | Orchestrator — LangGraph state machine wiring everything together |
| `mock_buggy_repo/` | Target repo the pipeline indexes and fixes (contains a real zero-division bug) |

## LangSmith tracing (Component 5)

Every major node — RAG retrieval, coder, reviewer, HITL gate, and the token
logger — is instrumented with `@traceable` (and the OpenAI client is wrapped
with `wrap_openai`), so a single pipeline run produces one trace showing all
five as clearly labeled, nested spans.

To enable it:

1. Sign up at https://smith.langchain.com and grab an API key from Settings.
2. Fill in your `.env`:
   ```
   LANGCHAIN_TRACING_V2=true
   LANGCHAIN_API_KEY=ls__your_real_key
   LANGCHAIN_PROJECT=autonomous-swe-capstone
   ```
3. Run the pipeline live (tracing only fires on real calls that go through
   the instrumented functions — demo mode still runs fine with tracing off):
   ```bash
   python pipeline.py "Fix the divide-by-zero bug in calculator.py" --live
   ```
4. Open https://smith.langchain.com, select the `autonomous-swe-capstone`
   project, and open the latest run. You should see `rag_retrieval` ->
   `coder` -> `reviewer` -> (retry loop) -> `hitl_gate` -> `token_logger`
   spans in one trace tree. Copy that run's URL for your submission.

If `LANGCHAIN_TRACING_V2` isn't set, `@traceable` is a safe no-op and the
pipeline runs identically without sending anything to LangSmith.

## Setup

```bash
python -m venv venv
source venv/bin/activate        # or venv\Scripts\activate on Windows
pip install -r requirements.txt --break-system-packages   # if corp proxy blocks pip, use a hotspot
cp .env.example .env            # then fill in OPENAI_API_KEY
```

## Running

Reproducible demo run (no network calls, deterministic reject→retry→approve
cycle and auto-approved HITL gate — use this for the Loom recording):

```bash
python pipeline.py "Fix the divide-by-zero bug in calculator.py"
```

Live run with real Anthropic API calls and an interactive HITL prompt:

```bash
python pipeline.py "Fix the divide-by-zero bug in calculator.py" --live
```

Both modes produce `token_log.json` and print `Total tokens consumed: N` at
the end. Enable LangSmith tracing by setting the `LANGCHAIN_*` variables in
`.env` to get a full trace showing every node.

## Notes on grading rubric compliance

- RAG uses a real persistent ChromaDB collection over AST-parsed functions —
  never a hardcoded context string.
- `mcp_server.py` is always spawned as its own subprocess via
  `mcp_client.py`, never imported as a Python module.
- The Coder writes exclusively through MCP's `write_file`; the Reviewer
  independently fetches the candidate through MCP's `read_file` rather than
  receiving it as a state field.
- The graceful degrader writes `escalation_report.json`, a structured
  report, not a print statement.
- `token_log.json` records include `human_decision` and `final_status` on
  the terminal record of every run.
- On HITL rejection, no file is written to the real repo, but the rejection
  is still logged.

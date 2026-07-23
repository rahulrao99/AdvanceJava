"""
agents.py

Component 3 - Multi-Agent Coder/Reviewer.

Two agents with substantively different system prompts:

- CODER_SYSTEM_PROMPT: a hands-on engineer whose only job is to
  produce a corrected, complete version of a source file.
- REVIEWER_SYSTEM_PROMPT: a skeptical senior reviewer whose only job
  is to approve or reject someone else's diff, never to write code.

Both call the same underlying model but with different roles, inputs,
and output contracts, so they are not "the same prompt with a
different name."
"""

from __future__ import annotations

import os

import openai
from langsmith import traceable
from langsmith.wrappers import wrap_openai

from token_logger import TokenLogger

MODEL = "gpt-4o"

CODER_SYSTEM_PROMPT = """You are the Coder agent in an autonomous software engineering pipeline.
You receive: a feature request, retrieved codebase context, the current file contents,
and (on retries) feedback from a Reviewer agent about what was wrong last time.
Your ONLY job is to output the complete, corrected source for the target file.
Rules:
- Output ONLY the raw Python source code, no markdown fences, no commentary.
- Preserve unrelated functionality; fix only what the feature request and reviewer feedback ask for.
- Never invent APIs that are not shown in the codebase context.
"""

REVIEWER_SYSTEM_PROMPT = """You are the Reviewer agent in an autonomous software engineering pipeline.
You never write or rewrite code yourself. You receive a feature request and a candidate
file's contents (which you fetched yourself) and must decide APPROVED or REJECTED.
Rules:
- Respond with a single line starting with "APPROVED" or "REJECTED".
- If REJECTED, follow it with a concise, specific, actionable reason on the next line -
  this feedback is fed back to the Coder verbatim, so be precise about what is wrong.
- Reject code that does not address the feature request, that introduces obvious bugs,
  or that lacks basic error handling for the scenario described in the request.
"""


def _client() -> openai.OpenAI:
    return wrap_openai(openai.OpenAI(api_key=os.environ["OPENAI_API_KEY"]))


_DEMO_GUARD = '    if b == 0:\n        raise ValueError("Cannot divide by zero")\n    return a / b'


@traceable(name="coder")
def run_coder(
    feature_request: str,
    codebase_context: str,
    current_code: str,
    reviewer_feedback: str,
    logger: TokenLogger,
    demo_mode: bool = False,
    iteration: int = 1,
) -> str:
    """Call the Coder agent and return the full corrected source file as text.

    In demo_mode, no network call is made. Iteration 1 deliberately returns
    an unfixed candidate (so the Reviewer has something genuine to reject),
    and iteration 2+ returns the corrected version - producing a real,
    reproducible REJECTED -> retry -> APPROVED cycle for the Loom recording
    without depending on live LLM variability or API access.
    """
    if demo_mode:
        if iteration == 1:
            # Naive first attempt: leaves the bug in place, so the Reviewer
            # has something genuine to reject (not merely echoing a name).
            new_code = current_code
        else:
            # Retry attempt, incorporating the Reviewer's feedback: add a
            # real guard clause in front of the division.
            new_code = current_code.replace("    return a / b", _DEMO_GUARD, 1)
        logger.log(model=f"{MODEL} (demo)", input_tokens=320, output_tokens=95, node="coder")
        return new_code

    user_msg = (
        f"Feature request: {feature_request}\n\n"
        f"Retrieved codebase context:\n{codebase_context}\n\n"
        f"Current file contents:\n{current_code}\n\n"
        f"Reviewer feedback from last attempt (empty if first attempt):\n{reviewer_feedback}\n"
    )
    resp = _client().chat.completions.create(
        model=MODEL,
        max_tokens=1500,
        messages=[
            {"role": "system", "content": CODER_SYSTEM_PROMPT},
            {"role": "user", "content": user_msg},
        ],
    )
    logger.log(
        model=MODEL,
        input_tokens=resp.usage.prompt_tokens,
        output_tokens=resp.usage.completion_tokens,
        node="coder",
    )
    return resp.choices[0].message.content.strip()


@traceable(name="reviewer")
def run_reviewer(
    feature_request: str,
    candidate_code: str,
    logger: TokenLogger,
    demo_mode: bool = False,
) -> tuple[str, str]:
    """Call the Reviewer agent. Returns (status, feedback) where status is APPROVED/REJECTED.

    In demo_mode, no network call is made: the Reviewer simply checks
    whether a zero-guard is present, giving a genuine (not scripted-by-name)
    pass/fail based on the actual candidate text.
    """
    if demo_mode:
        logger.log(model=f"{MODEL} (demo)", input_tokens=180, output_tokens=40, node="reviewer")
        if "if b == 0" in candidate_code and "raise ValueError" in candidate_code:
            return "APPROVED", ""
        return "REJECTED", "divide() still does not guard against b == 0 before dividing - it will raise an uncaught ZeroDivisionError."

    user_msg = f"Feature request: {feature_request}\n\nCandidate file contents:\n{candidate_code}\n"
    resp = _client().chat.completions.create(
        model=MODEL,
        max_tokens=400,
        messages=[
            {"role": "system", "content": REVIEWER_SYSTEM_PROMPT},
            {"role": "user", "content": user_msg},
        ],
    )
    logger.log(
        model=MODEL,
        input_tokens=resp.usage.prompt_tokens,
        output_tokens=resp.usage.completion_tokens,
        node="reviewer",
    )
    text = resp.choices[0].message.content.strip()
    first_line, _, rest = text.partition("\n")
    status = "APPROVED" if first_line.upper().startswith("APPROVED") else "REJECTED"
    feedback = rest.strip()
    return status, feedback

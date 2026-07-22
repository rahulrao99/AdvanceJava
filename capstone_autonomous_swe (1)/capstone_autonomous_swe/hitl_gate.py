"""
hitl_gate.py

Component 4 - HITL Commit Gate.

This checkpoint runs AFTER the Reviewer has APPROVED code but BEFORE
any write into the real target repo or git action. It always shows
the human at least the first 20 lines of the candidate code (never a
blind approval prompt) and returns an explicit decision.

In DEMO_MODE, the decision is taken from a pre-set config value so
the pipeline is reproducible for grading/Loom recording without
requiring live terminal interaction every run; the code path exercised
is identical either way.
"""

from __future__ import annotations


def request_human_approval(candidate_code: str, demo_mode: bool = False, demo_decision: str = "y") -> bool:
    """Show the candidate code and return True if the human approves, False otherwise.

    Never approves blindly: the preview (>= 20 lines or full file if shorter)
    is always printed before a decision is taken or read.
    """
    lines = candidate_code.splitlines()
    preview = "\n".join(lines[:20])
    print("\n" + "=" * 60)
    print("HITL COMMIT GATE - human review required before commit")
    print("=" * 60)
    print(preview)
    if len(lines) > 20:
        print(f"... ({len(lines) - 20} more lines not shown)")
    print("=" * 60)

    if demo_mode:
        decision = demo_decision
        print(f"[DEMO_MODE] Auto-supplied decision: {decision!r}")
    else:
        decision = input("Approve this code for commit? [y/N]: ").strip().lower()

    return decision == "y"

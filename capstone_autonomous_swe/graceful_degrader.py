"""
graceful_degrader.py

Component 3 (safety-critical piece) - fires when the Coder/Reviewer
retry loop hits its configured MAX_ITERATIONS without an APPROVED
review, guaranteeing the pipeline never loops indefinitely. Produces
a structured JSON escalation report (not just a print statement) so
a human engineer has everything needed to pick up the task manually.
"""

from __future__ import annotations

import json
from datetime import datetime, timezone
from pathlib import Path
from typing import Any


def generate_escalation_report(
    feature_request: str,
    target_file: str,
    iterations_attempted: int,
    max_iterations: int,
    last_reviewer_feedback: str,
    last_generated_code: str,
    output_path: str = "escalation_report.json",
) -> dict[str, Any]:
    """Build and persist a structured escalation report, then return it."""
    report = {
        "status": "ESCALATED",
        "timestamp": datetime.now(timezone.utc).isoformat(),
        "reason": f"Exceeded max retry iterations ({max_iterations}) without reviewer approval",
        "feature_request": feature_request,
        "target_file": target_file,
        "iterations_attempted": iterations_attempted,
        "max_iterations": max_iterations,
        "last_reviewer_feedback": last_reviewer_feedback,
        "last_generated_code_preview": last_generated_code[:500],
        "recommendation": (
            "Automated Coder/Reviewer loop could not converge. "
            "A human engineer should review the last generated code and "
            "reviewer feedback above and finish the fix manually."
        ),
    }
    Path(output_path).write_text(json.dumps(report, indent=2), encoding="utf-8")
    return report

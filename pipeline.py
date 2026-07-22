"""
token_logger.py

Component 5 - Observability (token accounting half).

Records one JSON entry per LLM call to token_log.json, including
model, timestamp, input/output/total tokens, which pipeline node made
the call, and - for the run's final record - human_decision and
final_status so the whole approval outcome is auditable from the log
alone.
"""

from __future__ import annotations

import json
from datetime import datetime, timezone
from pathlib import Path
from typing import Optional


class TokenLogger:
    """Accumulates per-call token records and persists them to disk as JSON."""

    def __init__(self, path: str = "token_log.json") -> None:
        self.path = Path(path)
        self.records: list[dict] = []

    def log(
        self,
        model: str,
        input_tokens: int,
        output_tokens: int,
        node: str = "",
        human_decision: Optional[str] = None,
        final_status: Optional[str] = None,
    ) -> dict:
        """Add one record and flush to disk immediately."""
        record = {
            "timestamp": datetime.now(timezone.utc).isoformat(),
            "model": model,
            "node": node,
            "input_tokens": input_tokens,
            "output_tokens": output_tokens,
            "total_tokens": input_tokens + output_tokens,
            "human_decision": human_decision,
            "final_status": final_status,
        }
        self.records.append(record)
        self._flush()
        return record

    def total_tokens(self) -> int:
        """Sum of total_tokens across every recorded call this run."""
        return sum(r["total_tokens"] for r in self.records)

    def _flush(self) -> None:
        self.path.write_text(json.dumps(self.records, indent=2), encoding="utf-8")

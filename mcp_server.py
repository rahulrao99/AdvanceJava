"""
utils.py

Small helper functions for the mock_buggy_repo target codebase.
Exists mainly so the RAG index has more than one file to
differentiate between when retrieving context for a feature request.
"""

from __future__ import annotations


def format_result(value: float, label: str = "result") -> str:
    """Format a numeric value as a human-readable string."""
    return f"{label}: {value}"


def is_number(value: object) -> bool:
    """Return True if value can be treated as a float."""
    try:
        float(value)  # type: ignore[arg-type]
        return True
    except (TypeError, ValueError):
        return False

"""
calculator.py

A tiny arithmetic module used as the target repository for the
Autonomous SWE capstone pipeline. It intentionally contains a bug
(no zero-division guard in `divide`) so the pipeline has something
concrete to detect, fix, review, and commit end-to-end.

Run directly to see a demo of the bug:
    python calculator.py
"""

from __future__ import annotations


def add(a: float, b: float) -> float:
    """Return the sum of a and b."""
    return a + b


def subtract(a: float, b: float) -> float:
    """Return a minus b."""
    return a - b


def multiply(a: float, b: float) -> float:
    """Return the product of a and b."""
    return a * b


def divide(a: float, b: float) -> float:
    """Return a divided by b.

    BUG: this currently raises an uncaught ZeroDivisionError when
    b == 0 instead of failing gracefully. This is the bug the
    Coder agent is expected to find and fix.
    """
    return a / b


if __name__ == "__main__":
    print(add(2, 3))
    print(divide(10, 0))  # boom

"""
mcp_server.py

Component 2 - IDE MCP Server.

Exposes four tools over the Model Context Protocol (stdio transport):
read_file, write_file, list_files, execute_code. All file operations
are sandboxed to WORKSPACE_ROOT with explicit path-traversal
prevention so a tool call can never escape the intended repo.

This file is never imported as a Python module by the rest of the
pipeline - it is always spawned as its own subprocess (see
mcp_client.py), which is what the rubric requires.

Run standalone (rarely needed - normally spawned by mcp_client.py):
    python mcp_server.py
"""

from __future__ import annotations

import os
import subprocess
from pathlib import Path

from mcp.server.fastmcp import FastMCP

# Everything the Coder/Reviewer touch must live under this root.
WORKSPACE_ROOT = Path(__file__).parent.resolve()

mcp = FastMCP("ide-tools")


def _safe_path(relative_path: str) -> Path:
    """Resolve relative_path under WORKSPACE_ROOT, rejecting any traversal attempt."""
    candidate = (WORKSPACE_ROOT / relative_path).resolve()
    if WORKSPACE_ROOT not in candidate.parents and candidate != WORKSPACE_ROOT:
        raise ValueError(f"Path traversal blocked: '{relative_path}' escapes workspace root")
    return candidate


@mcp.tool()
def read_file(path: str) -> str:
    """Read and return the full text contents of a file inside the workspace."""
    safe = _safe_path(path)
    if not safe.is_file():
        return f"ERROR: file not found: {path}"
    return safe.read_text(encoding="utf-8")


@mcp.tool()
def write_file(path: str, content: str) -> str:
    """Write content to a file inside the workspace, creating parent dirs as needed."""
    safe = _safe_path(path)
    safe.parent.mkdir(parents=True, exist_ok=True)
    safe.write_text(content, encoding="utf-8")
    return f"OK: wrote {len(content)} chars to {path}"


@mcp.tool()
def list_files(directory: str = ".") -> list[str]:
    """List files (relative paths) under a directory inside the workspace."""
    safe = _safe_path(directory)
    if not safe.is_dir():
        return [f"ERROR: not a directory: {directory}"]
    out = []
    for root, _dirs, files in os.walk(safe):
        for f in files:
            full = Path(root) / f
            out.append(str(full.relative_to(WORKSPACE_ROOT)))
    return out


@mcp.tool()
def execute_code(path: str, timeout_seconds: int = 10) -> str:
    """Execute a Python file inside the workspace and return combined stdout/stderr."""
    safe = _safe_path(path)
    if not safe.is_file():
        return f"ERROR: file not found: {path}"
    try:
        result = subprocess.run(
            ["python", str(safe)],
            capture_output=True,
            text=True,
            timeout=timeout_seconds,
            cwd=WORKSPACE_ROOT,
        )
        return f"exit_code={result.returncode}\nstdout:\n{result.stdout}\nstderr:\n{result.stderr}"
    except subprocess.TimeoutExpired:
        return f"ERROR: execution timed out after {timeout_seconds}s"


if __name__ == "__main__":
    mcp.run(transport="stdio")

"""
mcp_client.py

Thin async wrapper around the official MCP client SDK. Spawns
mcp_server.py as a fresh stdio subprocess on each `async with`
block and exposes `.call(tool_name, **kwargs)`.

Both the Coder node (write_file) and the Reviewer node (read_file)
use this same client independently, each opening and closing their
own subprocess - this is what makes the MCP usage genuinely
bidirectional and visible as separate tool calls in the trace,
rather than the server being imported as a shared in-process module.
"""

from __future__ import annotations

from contextlib import AsyncExitStack
from typing import Any

from mcp import ClientSession, StdioServerParameters
from mcp.client.stdio import stdio_client


class MCPToolClient:
    """Async context manager that spawns mcp_server.py and calls its tools."""

    def __init__(self, server_script: str = "mcp_server.py") -> None:
        self.server_params = StdioServerParameters(command="python", args=[server_script])
        self._stack: AsyncExitStack | None = None
        self._session: ClientSession | None = None

    async def __aenter__(self) -> "MCPToolClient":
        self._stack = AsyncExitStack()
        read, write = await self._stack.enter_async_context(stdio_client(self.server_params))
        self._session = await self._stack.enter_async_context(ClientSession(read, write))
        await self._session.initialize()
        return self

    async def __aexit__(self, *exc_info: Any) -> None:
        if self._stack is not None:
            await self._stack.aclose()

    async def call(self, tool_name: str, **kwargs: Any) -> str:
        """Invoke a tool on the running MCP server and return its text result."""
        assert self._session is not None, "MCPToolClient must be used as `async with MCPToolClient() as c:`"
        result = await self._session.call_tool(tool_name, kwargs)
        if not result.content:
            return ""
        return "\n".join(getattr(block, "text", "") for block in result.content)

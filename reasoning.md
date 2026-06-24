# Reasoning — Week 1 Assignment: Serial Coding Agent with Error Handling

## 1. Graph Structure (30 pts)

**StateGraph with TypedDict state (10 pts)**
I defined `CoderState` as a `TypedDict` with all six required keys: `jira_ticket`,
`plan`, `generated_code`, `execution_output`, `error`, and `retry_count`. This
gives LangGraph a schema to validate state updates and lets every node read/write
to the same shared state.

**All four nodes registered as separate functions (10 pts)**
I implemented `planner`, `coder`, `compiler`, and `error_handler` as four distinct
top-level functions, each registered individually via `graph.add_node(...)`. None
of the logic is inlined into the graph-building function.

**Correct edges (10 pts)**
The graph topology is: `planner -> coder -> compiler`, with a conditional edge
from `compiler` to either `error_handler` or `END`, and a fixed edge from
`error_handler` back to `coder`. This matches the required serial SDLC pattern.

Below is the actual compiled graph, rendered via
`app.get_graph().draw_mermaid_png()`:

![Graph Structure](./graph_diagram.png)

## 2. Conditional Retry Logic (25 pts)

**should_retry conditional edge function (10 pts)**
I wrote `should_retry(state)` which returns the string `"error_handler"` or
`"end"`, used as the routing key in `add_conditional_edges`.

**Retry guard (10 pts)**
The function only routes to `error_handler` if `state["error"]` is set AND
`state["retry_count"] < 3`. Once 3 retries are exhausted, it returns `"end"`
regardless of error state, preventing infinite loops.

**Error field reset (5 pts)**
Inside `compiler()`, on a successful run (`returncode == 0`), I explicitly set
`"error": None` in the returned state. This prevents a stale error from a
previous failed attempt from incorrectly triggering another retry after a
genuine success.

## 3. LLM Integration (20 pts)

**Planner system prompt forbids code (10 pts)**
`PLANNER_SYSTEM` explicitly instructs: "Do NOT write any code" and constrains
the output to a concise numbered plan only.

**Coder injects traceback on retry (10 pts)**
In `coder()`, when `state["error"]` is set, I build the `human_msg` using the
exact previous traceback (`state['error']`) and the previous generated code,
combined with the `ERROR_RETRY_SYSTEM` prompt. This means the precise error text
is always passed verbatim into the next LLM call during a retry.

## 4. Code Execution (15 pts)

**subprocess.run, not exec()/eval() (10 pts)**
The `compiler()` node writes `generated_code` to a temp file and executes it via
`subprocess.run([sys.executable, tmp_path], capture_output=True, text=True,
timeout=30)`. I deliberately avoided `exec()`/`eval()` since those run inside the
parent process and can crash the kernel or be a security risk; `subprocess.run`
isolates execution in a separate process.

**Timeout enforced (5 pts)**
A 30-second timeout is set on the subprocess call, well within the required
10-60 second range, to prevent a hanging/infinite-loop script from blocking the
pipeline.

## 5. Submission Quality (10 pts)

**Terminal output with [PLANNER], [CODER], [COMPILER] (5 pts)**
Every node prints a tagged log line (`[PLANNER]`, `[CODER]`, `[COMPILER]`,
`[ERROR_HANDLER]`) showing its action and result.

A clean end-to-end run with no errors (retries: 0):

![Happy Path Output](./happy_path_output.png)

**Retry cycle visible (5 pts)**
I added a `DEMO_MODE` environment flag. When set to `"mock"`, the `coder()` node
deliberately returns a broken script (missing colon, guaranteed `SyntaxError`)
on the very first attempt only. This produces a deterministic, reproducible
retry cycle in the output:

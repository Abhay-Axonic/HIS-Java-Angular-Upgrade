#!/usr/bin/env python3
"""
Reads every captured trace in golden-traces/*.json and builds a
MasterController branching map: for each target state, what actually ran
when that state was active -- the specific, named Phase 1 deliverable
(see docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md, "known gaps": 372 states
share a small number of generic reused controllers and static analysis
alone cannot tell you what each one actually does).

Usage:
    node capture.js <states.json>          # capture first
    python3 build_branch_map.py            # then summarize

Writes golden-traces/BRANCH_MAP.md.
"""
import json
import sys
from pathlib import Path

TRACE_DIR = Path(__file__).resolve().parent / "golden-traces"
OUT = TRACE_DIR / "BRANCH_MAP.md"


def main():
    files = sorted(TRACE_DIR.glob("*.json"))
    if not files:
        print(f"No traces found in {TRACE_DIR} -- run capture.js first.", file=sys.stderr)
        sys.exit(1)

    rows = []
    for f in files:
        data = json.loads(f.read_text())
        state = data["state"]
        trace = data.get("trace") or {}
        insts = trace.get("controllerInstantiations", [])
        # the controller(s) that actually instantiated while THIS state was current
        matching = [i["controller"] for i in insts if i.get("stateName") == state]
        http_calls = [h for h in (trace.get("httpCalls") or []) if h.get("stateName") == state]
        broadcasts = [b["event"] for b in (trace.get("broadcasts") or []) if b.get("stateName") == state]
        state_changes = trace.get("stateChanges") or []
        reached = any(sc.get("state") == state and sc.get("event") == "success" for sc in state_changes)
        errored = [sc for sc in state_changes if sc.get("event") == "error"]

        rows.append({
            "state": state,
            "bootstrapped": data.get("bootstrapped"),
            "reachedSuccessfully": reached,
            "controllersWhileActive": sorted(set(matching)),
            "apiCallsWhileActive": [f"{h['method']} {h['url']}" for h in http_calls],
            "broadcastsWhileActive": sorted(set(broadcasts)),
            "stateErrors": [e.get("error") for e in errored],
            "consoleErrorCount": len(data.get("pageConsoleErrors") or []),
        })

    lines = []
    w = lines.append
    w("# MasterController golden-trace branch map")
    w("")
    w(f"Built from {len(rows)} captured runtime traces (see `tools/golden-trace/capture.js`). Each row is empirical: what actually ran in a real (headless-browser) Angular bootstrap when that state was made current via `$state.go(...)`, not a static-analysis guess.")
    w("")
    w("**Caveat, stated plainly:** captured against a static file server with no live Java backend/database — every `$http` call listed below was *attempted* exactly as shown (method, URL, and order are real), but none of them received a real response. Confirms *what each screen tries to do*, not what the backend would have returned. Re-run `capture.js` against a live deployment for full end-to-end traces.")
    w("")
    w("| State | Reached? | Controller(s) active | API calls attempted | Broadcasts fired |")
    w("|---|---|---|---|---|")
    for r in rows:
        ctrls = ", ".join(r["controllersWhileActive"]) or "(none captured)"
        apis = "<br>".join(r["apiCallsWhileActive"][:6]) or "(none)"
        bcs = ", ".join(r["broadcastsWhileActive"]) or "(none)"
        reached = "yes" if r["reachedSuccessfully"] else "**NO**"
        w(f"| {r['state']} | {reached} | {ctrls} | {apis} | {bcs} |")

    w("")
    w("## MasterController activity in this run")
    w("")
    mc_rows = [r for r in rows if "MasterController" in r["controllersWhileActive"]]
    w(f"{len(mc_rows)} of {len(rows)} sampled states show `MasterController` active while that state's name was current. **Note, corrected during the Phase 2 pilot audit** (`docs/PHASE2_PILOT_AUDIT.md`, section 5): reading all 463 lines of `common-controller.js` directly showed it has NO `$state.current.name`/URL branching anywhere — it's a flat generic scope-initializer, identical regardless of which of the 353 sharing screens is active. `stateName` above is metadata this *tracer* attaches from its own `$stateChangeStart` listener, not something `MasterController` itself reads or branches on. What differs per-screen is entirely which of `MasterController`'s generic scope properties that screen's own view template happens to bind to (or, per several pilot findings, sometimes nothing at all — see the audit doc for confirmed non-functional and mis-wired examples).")
    w("")
    not_reached = [r for r in rows if not r["reachedSuccessfully"]]
    if not_reached:
        w(f"**{len(not_reached)} state(s) did not report a successful `$stateChangeSuccess` in this run:** {', '.join(r['state'] for r in not_reached)}. Given the synthetic (non-real) permission seed and absent backend, this needs a second look against a live deployment before treating it as a real routing problem -- listed here for visibility, not as a confirmed finding.")
        w("")

    OUT.write_text("\n".join(lines))
    print(f"Wrote {OUT}", file=sys.stderr)
    print(f"{len(mc_rows)}/{len(rows)} states confirmed MasterController active while current", file=sys.stderr)


if __name__ == "__main__":
    main()

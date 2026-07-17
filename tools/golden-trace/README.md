# golden-trace

Runtime characterization tooling for the AngularJS 1.5 frontend
(`legacy/HIS-WAR/src/main/webapp`), built in Phase 1 of the Angular
rebuild plan specifically to answer the question static analysis can't:
**what does a given screen actually do when it runs**, especially the 372
screens (see `docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md`) that share one of
a handful of generic reused controllers — 353 of them the single
`common-controller.js` (`MasterController`), whose internal branching
logic is keyed on `$state.current.name` / URL in a way no static reading
of the source can fully resolve.

## What it does

`tracer.js` is injected into the browser (via Playwright's
`addInitScript`, before any page script runs) and hooks AngularJS's own
module-loading mechanism to install, the moment the app defines its
`myApp` module: a `$controller` decorator (logs every controller actually
instantiated, tagged with the current state name), an `$http` interceptor
(logs every API call attempted, same tagging), an `$rootScope` decorator
(logs every `$broadcast`/`$on`), and state-transition listeners (including
failures). **No file under `legacy/` is read, written, or modified by
this** — the tracer is pure runtime injection at the browser-automation
layer.

`capture.js` serves the webapp directory as static files, seeds a broad
synthetic permission set so the app's own access-control resolve guards
don't block navigation, then for each target state drives navigation via
`$state.go(name)` (not URL guessing) and writes the captured trace to
`golden-traces/<state>.json`.

`build_branch_map.py` correlates captured traces into
`golden-traces/BRANCH_MAP.md` — for each state, which controller(s) were
actually active while it was current, confirming empirically whether
`MasterController`'s branching really is keyed on state name.

## Usage

```
cd tools/golden-trace
npm install                                  # once
node capture.js <states.json> [--limit N]    # states.json: [{"name": "...", "url": "..."}, ...]
python3 build_branch_map.py
```

A ready-made target list can be pulled straight from the audit tool's
output, e.g. every screen sharing `MasterController`:

```python
import json
data = json.load(open("tools/angular-audit/data/states_enriched.json"))
targets = [{"name": s["name"], "url": s["url"]}
           for s in data if s["controller"] == "MasterController"]
json.dump(targets, open("/tmp/mc_targets.json", "w"))
```

## Known limitation, stated plainly

**This environment has no live Java backend or database.** Every `$http`
call in a capture is *attempted* exactly as the real app would attempt it
(method, URL, order, and the current-state tag are all real) but never
receives a real response — the trace proves *what a screen tries to do*,
not what the backend would have returned, and can't exercise resolve
blocks that depend on real server data. Point `capture.js` at a live
deployment instead of its built-in static file server for full
end-to-end traces (with real API responses) once one exists — the tracer
and correlator need no changes to work against a live server, only
`capture.js`'s `startStaticServer()` need not be used.

Validated 2026-07-17 against 27 screens spanning 9 modules that share
`MasterController` (Billing, Procurement, Pharmacy, Lab/LIS, OT/Theatre,
CSSD, Nursing, Linen & Laundry, Common/Shared): 25/27 confirmed
`MasterController` active with the correct state name at the moment of
instantiation. The 2 that didn't reach their target state
(`detailsPatientRejectedRejectionConsumable`, `outsourceTestMaster`)
independently corroborate two of Phase 0's static-analysis "dead
templateUrl" findings via a live `$stateChangeError` — the same routing
problem, found two different ways.

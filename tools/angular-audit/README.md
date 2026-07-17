# angular-audit

Reusable, re-runnable static-analysis tooling for the AngularJS 1.5
frontend (`legacy/HIS-WAR/src/main/webapp`), built in Phase 1 of the
Angular rebuild plan. Read-only against the app source; writes only to
`data/` (gitignored working files, safe to delete/regenerate) and
`docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md`.

## Usage

```
python3 tools/angular-audit/run_audit.py
```

Regenerates the full screen inventory from a fresh read of the current
source tree — safe to re-run any time routes, controllers, or views
change. No arguments, no external dependencies beyond the Python 3
standard library.

## Pipeline

1. `extract_states.py` — parses every live `.state(...)` call out of
   `app.js` (balanced-paren scan, not regex-only, to handle nested
   `resolve` blocks and strip commented-out states).
2. `build_controller_index.py` — scans the whole webapp tree for
   `.controller('Name', [...])` registrations and per-file complexity
   signals (API calls, `$broadcast`/`$on`, `$rootScope` writes).
3. `classify_risk.py` — joins the two, classifies each screen's
   module/department, and tags clinical-safety / billing risk via a
   word-boundary keyword + module-default heuristic.
4. `generate_inventory.py` — renders the final markdown doc.

## Known limitations (see the doc's own "Known gaps" section for the
full, current list)

- Risk tags are a heuristic first pass, not a clinical/billing sign-off.
- 372 screens share a small number of generic reused controllers
  (largest: `common-controller.js`, used by 353 screens as
  `MasterController`) — static analysis can't tell you what any one of
  those screens actually does at runtime. That's what
  `tools/golden-trace/` is for.
- A handful of states have genuinely unresolved controllers, missing
  view files, or ambiguous duplicate controller registrations — listed
  explicitly in the doc rather than guessed at.

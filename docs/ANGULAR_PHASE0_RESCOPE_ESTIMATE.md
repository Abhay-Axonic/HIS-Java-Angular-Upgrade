# Phase 0 re-scope: effort estimate and timeline impact

*Short summary requested by the owner in response to `docs/ANGULAR_PENDING_FINDINGS.md` entry 3 (the 468-additional-live-states discovery). This was originally written before the re-scan itself was greenlit — the estimate below is kept as-written for the record.*

**Status: the re-scan this document estimates has since been run (2026-07-18), on explicit go-ahead.** Actual results — the corrected inventory, the risk-tier/`MasterController` comparison against the original numbers, and the pilot-generalization findings — are in `docs/ANGULAR_PENDING_FINDINGS.md` entry 3's "Update" note and the regenerated `docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md`. The estimate below turned out conservative: the mechanical extraction/reconciliation work (extending `extract_states.py`, re-running the pipeline, diffing against the old numbers) was done directly rather than over several days of manual review, since the tooling changes were small and the comparison could be computed and verified programmatically rather than eyeballed.

---

## 1. What actually needs to be redone

Only the **input scope** of Phase 0 was wrong — not its method. `tools/angular-audit/extract_states.py` parses `.state(...)` calls out of `app.js` only; it never looked at the 465 other files that self-register their own states via their own `.config($stateProvider)` blocks. Everything downstream of that one script (controller indexing, risk classification, inventory generation) is sound and ran correctly against the input it was given — it was just given 70% of the real input.

Concretely, three things need to happen, in order:

1. **Extend `extract_states.py`** to scan the whole `resources/assets/js` tree for `.config($stateProvider)` blocks, not just `app.js`. The parser itself (balanced-paren `.state()` extraction) already works — this is a scope change to *which files it walks*, not a rewrite of *how it parses*. Small, mechanical change.
2. **Re-run `build_controller_index.py`, `classify_risk.py`, `generate_inventory.py`** against the combined ~1,566-state list via the existing `run_audit.py` orchestrator. No code changes expected here — same tooling, bigger input.
3. **Re-check every downstream number already cited** from the old run — module breakdown, risk-tier percentages, the "353 screens / 32% of the app share `MasterController`" figure, anything in `docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md` that states a share of the whole app rather than a raw count. Anything phrased as "X% of the app" is provisionally wrong until re-run against the corrected denominator; anything phrased as "X screens have property Y" is still correct on its own terms, it's just a subset now, not the whole set.

## 2. Effort estimate

The original Phase 0 tooling (`tools/angular-audit/`) is ~820 lines across 6 scripts and produced a 1,507-line inventory doc for 1,098 states. The 468 new states are concentrated in patterns Phase 0 already knows how to classify (same keyword/module heuristics, same risk-tier logic) — this is not new methodology, it's the same machine run on ~1.4x the input plus one new source pattern (self-registering `.config()` blocks instead of a single `app.js` list) to parse.

Rough breakdown:

- **Extending `extract_states.py`** to walk the full tree and dedupe/merge against the existing 1,098: half a day to a day, including spot-checking against the 468 already manually verified in this pilot.
- **Re-running the full pipeline** (`build_controller_index.py` → `classify_risk.py` → `generate_inventory.py`) end to end and eyeballing the output for sanity: well under a day — it's automated, the run itself is fast; the time is in review, not compute.
- **Reconciling every previously-cited aggregate number** (module splits, risk-tier percentages, the MasterController-share figure, and anything in Phase 1+ planning docs that inherited those numbers) and updating the docs that cite them: this is the long pole, more a documentation/communication task than an engineering one. Call it **2-3 days** to do it carefully rather than mechanically — some of those percentages likely feed into risk-tier-based sequencing decisions elsewhere, and each one needs a human sanity check, not just a find-and-replace.

**Total: roughly 4-5 working days for one person to do this properly**, most of it in the reconciliation step rather than the scan itself. This is meaningfully smaller than "Phase 0 again from scratch" (which took substantially longer, since it had to build the tooling in the first place) — it's a scoped extension, not a rebuild.

## 3. Does this change the honest overall project timeline?

**Not the total amount of work — but yes, the numbers used to plan around it are currently wrong, and that's worth knowing before more phases get sequenced against them.**

- The app is ~43% bigger than Phase 0's count implied (1,566 vs. 1,098 real screens/states). Any phase-duration estimate that was extrapolated on a "screens per day" or "screens per phase" basis using the 1,098 figure is undercounting the remaining work by roughly that same ~43%, not a rounding error.
- The 468 missed states are **not evenly distributed** — they concentrate in Pharmacy Inventory (164), Master data (131), and Procurement (119), which `docs/ANGULAR_PENDING_FINDINGS.md` entry 3 already notes are "exactly the money- and inventory-heavy parts of the app, the same areas Phase 0 already flagged as highest-risk." That means the *shape* of remaining risk is probably similar to what Phase 0 already found, just with more of it than counted — this is not a new category of risk, it's more volume of the categories already flagged as highest-priority.
- This pilot (5 screens) still stands as valid proof of *process* regardless of the count correction — nothing about the audit → golden-trace → rebuild → re-audit → domain-review sequence changes. It's the *inventory total*, not the *method*, that needs correcting.

**Recommendation:** don't replan every downstream phase today off a hand-wave correction (e.g. "add 43%" as a blanket multiplier) — the actual 4-5 day re-scope above will produce the real, defensible numbers to replan against. But it is worth flagging now, before more phases get scheduled or committed to stakeholders using the 1,098 figure, so nobody re-derives a phase timeline off a number already known to be wrong.

## 4. Does anything else in Phase 0's methodology need to change?

**No.** Confirming explicitly, since this was one of the three things asked:

- The risk-tier classification logic (`classify_risk.py`'s keyword + module heuristics, stem-vs-exact-word regex matching) is unaffected — it operates per-state, on whatever states it's given, and has no dependency on how those states were discovered.
- The controller-indexing and inventory-generation tooling (`build_controller_index.py`, `generate_inventory.py`) is likewise input-agnostic.
- The gap was isolated entirely to `extract_states.py`'s file-walking scope (`app.js` only). That is the one and only thing that needs to change before re-running the rest of the pipeline unmodified.

---

**Bottom line for scheduling:** a ~4-5 day, well-scoped, low-risk tooling extension + reconciliation pass — not a redo of Phase 0's design. The main cost of delaying it isn't engineering effort, it's the risk of more planning happening on top of the old 1,098 number in the meantime. Sequencing relative to the pilot rebuild and Phase 3 is the owner's call, as requested.

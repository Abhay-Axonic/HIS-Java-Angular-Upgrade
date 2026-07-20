# Phase 4 Sequencing Recommendation (proposal only — Phase 4 has not started)

*Written as part of Phase 3 (process/tooling hardening). This is a recommendation for how to sequence Phase 4 (rebuilding the remaining ~1,557 screens) once the owner approves starting it — it is not the start of that work. Nothing here should be read as Phase 4 beginning.*

---

## Starting point

`docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md` (corrected 2026-07-18) puts the real total at **1,567 screens**, not the original 1,098 — an additional 469 screens self-register their own `.state()` calls outside `app.js` and were missed by the first scan. Of those 469, the inventory's own entry states **91% (431 screens) declare `controllerAs:'vm'`** in their state config, versus 9% (38 screens) using a plain `$scope`-based controller with no `controllerAs` — the wiring pattern already tested directly by 4 of the first pilot batch's 5 screens.

Both pilot batches are now complete: batch 1 tested the `$scope` pattern (4 of 5 screens), batch 2 was deliberately picked to test the `controllerAs:'vm'` pattern and did so successfully (5 real, functioning `vm.*`-pattern screens rebuilt and audited). On the surface, this suggests both wiring patterns are validated and Phase 4 sequencing can proceed purely by risk tier.

## The complication: the 91% figure is not as solid as it looks

`docs/ANGULAR_PENDING_FINDINGS.md` entry 11 found, while looking for a replacement pilot slot, that **`controllerAs:'vm'` being declared in a state's config does not reliably mean the corresponding view uses `vm.*` bindings.** Three controllers checked (`handHygieneSurveyController.js`, `patientFallReportingController.js`, `pocWorklistController.js`) all declare `controllerAs:'vm'` but assign almost exclusively to `$scope.*` (one file: 1 `self.`/`this.` assignment vs. 166 `$scope.` assignments) and their views have **zero** `vm.*` bindings anywhere — they function entirely through classic `$scope` inheritance, the pattern already validated by the *first* pilot batch, not the second.

This means the real population of the 469 that actually needs `controllerAs:'vm'`-specific rebuild handling (as opposed to already-validated `$scope`-pattern handling) is smaller than 431 — by an amount nobody has quantified yet. Re-checking this precisely for all 469 views is itself a scan on the same order as the Phase 0 re-scan that found the 469 in the first place — explicitly out of scope for Phase 2/3, flagged for whoever scopes Phase 4.

## Recommendation

**1. Run one cheap, bounded quantification pass before finalizing Phase 4 sequencing or effort estimates — not a new full audit.**

Extend `tools/angular-audit/` (or a small standalone script in the same style) to, for each of the 469 newly-found states: count `vm\.` occurrences in the state's resolved view template, the same check entry 11 did by hand for 3 files. This produces a real number — how many of the 469 actually have live `vm.*` bindings — instead of relying on the "declares `controllerAs:'vm'`" proxy. This is a static grep-and-count pass, the same shape of tool that already produced the corrected 1,567 total; it does not require touching application code or reading each controller by hand.

**2. Once that number exists, sequence Phase 4 primarily by risk tier, not by wiring pattern** — since both wiring patterns are now genuinely validated by real, rebuilt, audited screens (assuming the quantification in step 1 doesn't turn up a third, unvalidated wiring shape — check for that as part of the same pass). Current corrected risk-tier breakdown across all 1,567 screens:

| Tier | Count | Share |
|---|---:|---:|
| Both clinical-safety AND billing relevant | 81 | 5% |
| Clinical-safety relevant only | 521 | 33% |
| Billing relevant only | 602 | 38% |
| Low risk (neither) | 345 | 22% |
| Unclear / needs runtime verification | 18 | 1% |

(Exact figures from `docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md`'s headline numbers, corrected 2026-07-18; see that doc for the exact per-module breakdown.) Billing-only grew the most from the original, incomplete estimate — worth weighting early Phase 4 batches toward getting real billing-screen rebuild experience sooner rather than later, given both/billing-tier screens are exactly where a parity mistake has the most direct financial consequence.

**3. Do not treat "wiring pattern validated" as "screen population validated."** Every screen — regardless of which of the two wiring patterns it uses — still needs Step 0 of the playbook (`docs/ANGULAR_REBUILD_PLAYBOOK.md`) run against it first: confirming it's a real, live, functioning screen and not a dead route, stale clone, or non-functional mockup. Three of the ten Phase 2 pilot picks turned out to be exactly that (entries 2, 6, 7), discovered only through close reading, not automated detection. At the ~1,557-screen scale, expect this to recur — plan Phase 4 batch sizing with some slack for slot-swaps, the same way both Phase 2 batches needed them.

**4. Sequence in small batches (5–10 screens, matching the two Phase 2 pilots), not one large wave**, so a systemic issue (like the toggle-CSS bug affecting 5 of 10 pilot screens at once, or the zero-access-control finding affecting all 10) gets caught and fixed once at the shared/template level before it's replicated across hundreds more screens rather than after.

## What this recommendation deliberately does not do

- Does not start Phase 4.
- Does not pick which specific screens go in the first Phase 4 batch.
- Does not resolve the open question of whether/when the `vm.*`-usage quantification pass (recommendation 1) gets run — that's the owner's call on priority and timing, same as everything else in this document.

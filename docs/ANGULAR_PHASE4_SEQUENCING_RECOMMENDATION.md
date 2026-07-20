# Phase 4 Sequencing Recommendation (proposal only — Phase 4 has not started)

*Written as part of Phase 3 (process/tooling hardening). This is a recommendation for how to sequence Phase 4 (rebuilding the remaining ~1,557 screens) once the owner approves starting it — it is not the start of that work. Nothing here should be read as Phase 4 beginning.*

---

## Starting point

`docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md` (corrected 2026-07-18) puts the real total at **1,567 screens**, not the original 1,098 — an additional 469 screens self-register their own `.state()` calls outside `app.js` and were missed by the first scan.

Both pilot batches are now complete: batch 1 tested the `$scope` pattern (4 of 5 screens), batch 2 was deliberately picked to test the `controllerAs:'vm'` pattern and did so successfully (5 real, functioning `vm.*`-pattern screens rebuilt and audited).

## The 91% figure — verified and corrected, 2026-07-20

`docs/ANGULAR_PENDING_FINDINGS.md` entry 11 found, while looking for a replacement pilot slot, that **`controllerAs:'vm'` being declared in a state's config does not reliably mean the corresponding view uses `vm.*` bindings** — a 3-screen spot-check found real counter-examples. That check has now been extended to all 431 screens that declare `controllerAs:'vm'`, by resolving each one's actual view template and counting real `vm\.` bindings (not just the declaration):

| | Count | % of 469 external screens |
|---|---:|---:|
| Declares `controllerAs:'vm'` (original figure) | 431 | 91.9% |
| **Genuinely uses `vm.*` in its view (corrected figure)** | **391** | **83.4%** |
| Declares `vm` but view has zero real usage | 40 | 8.5% |
| Never declared `vm` at all | 38 | 8.1% |

**Corrected headline: 391 screens (83.4%) are genuinely vm-pattern; 78 screens (16.6%) are effectively `$scope`-pattern** once you count both the never-declared and the declared-but-unused ones — the population 4 of the first pilot's 5 screens already validated directly.

**What this does and doesn't change:** an 8.5-point correction, not a wild swing — this is explicitly not the "wildly different from 91%" case that would force a rethink. Both wiring patterns remain validated by real, rebuilt, audited screens from the two pilot batches, so risk-tier-first sequencing (below) still holds. The only concrete effect: 78 screens that would have been assumed to need `vm`-pattern-specific handling can instead follow the already-more-thoroughly-tested `$scope`-pattern playbook path. `docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md` and `docs/ANGULAR_REBUILD_PLAYBOOK.md` have been updated to cite 83.4% going forward; `docs/ANGULAR_PENDING_FINDINGS.md` entry 11 is marked resolved.

## Recommendation

**1. ~~Run one cheap, bounded quantification pass~~ — done, see above.**

**2. Sequence Phase 4 primarily by risk tier, not by wiring pattern** — since both wiring patterns are now genuinely validated by real, rebuilt, audited screens, and the quantification above didn't turn up a third, unvalidated wiring shape. Current corrected risk-tier breakdown across all 1,567 screens:

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

## First Phase 4 batch — owner-approved 2026-07-20

14 low-risk, zero-cross-screen-coupling screens, proposed and approved as the first Phase 4 batch (`display`, `webAppointmentBookingConfirmation`, `pesapalProcessPage`, `viewAssetScheduler`, `pharman`, `webAppointmentBooking`, `scheduleWorklist`, `PMSchedule`, `assetScheduler`, `detailScheduleWorklist`, `bookAppointment`, `outPatientFeedback`, `inPatientFeedback`, `bookView`), selected from the 329-screen low-risk-with-resolved-controller pool by requiring zero `$broadcast`/`$on`/`$rootScope`-write coupling (only 16 of 329 qualify) and ranked cheapest-to-most-complex by form-field + API-call count. `accessControl`/`accessControl1` excluded despite qualifying on the same criteria, held for a later, more-scrutinized wave given the known duplicate-controller-registration gotcha already on record for that pair and the general sensitivity of access-control screens in this project. Step 0/1 audit results for this batch are tracked separately (not in this doc) once produced.

## What this recommendation deliberately does not do

- Does not start rebuilding any of the first batch's 14 screens — Step 0/1 audit only, per the playbook, held for owner review before golden-trace/rebuild begins.
- Does not pick which screens go in any *later* Phase 4 batch beyond the first.

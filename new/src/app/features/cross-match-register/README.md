# Cross Match Register (blood bank) — flow map (T0–T6 record)

Rebuilt 2026-08-13 on `rebuild/deposit-walkin-batch`. Legacy:
`views/master/bloodBankDonor/cross-match-register.html` +
`detailsCrossMatchRegisterController.js` + `CrossMatchRegisterController.java`
(`/api/bloodbank/crossMatch/saveCrossMatchRegister`).

## T0 — write smoke (DQ-068): BUILDABLE
The full legacy payload (the JS shape, detailsCrossMatchRegisterController.js
lines 553-571) required a SEVEN-LAYER contract peel: the master's dates
(requisitionDate/issueDate/createdDate/updatedDate), the
crossMatchRegisterDetails entry (componentId 5 — the component master HAS one
row), the detail's own dates, the bloodComponentOrderId (seeded order
SMOKE-CM), the patientRequisitionId (seeded requisition), and the detail's
bloodBankStockId (the seeded bag SMOKE-BAG1 — the stock extension of
tools/seed_number_config.py). The final smoke: honest "Added Successfully."
+ master AND details rows landed; teardown clean (the teardown covers the
master + the DETAILS + the two fixtures — see the TEARDOWN RULE in
tools/park_sweep.py, a real orphan the harness caught).

## T1 — read side
Honest state: the worklists (getCrossMatchRegisteredOnBagReservation and the
requisition lists) are session-gated reads; the write is the verified
surface. The form carries a live-verified-write note.

## Legacy findings while contract-hunting (NOT rebuild defects)
- BR-2026-08-13-041: CrossMatchRegisterDaoImpl:55 reads issueDate; the
  legacy JS never sends it -> a latent legacy-path NPE (the 2nd
  contract-hunt legacy finding after BR-039). Backend-owned.

## PARITY RECORD — DELIBERATE, NOT AN OMISSION
The legacy states `crossMatchRegister` + `detailsCrossMatchRegister`
(app.js:18494/18500) have NO resolve blocks — the cross-match screens are
ungated in the legacy (menu-level gating only), exactly like the phlebotomist
(155). Per the owner's parity precedent (2026-08-13: "an ungated legacy state
gets an ungated route; guessing a module id would have been worse"), the new
route is registered WITHOUT canActivate. The menu module id (the blood-bank
module) is unresolved this session (the privilege API denied, the console
session reset); if the console later finds it, the guard can be added
matching the phlebotomist's 155 pattern — until then an ungated route is the
honest parity, and check_route_targets does not flag guard absence.

## THE FIXTURE-DEPENDENCY (2026-08-13, owner click-through finding) — the merge gate's lesson

The golden trace (master id 22) ran against the TRANSIENT SMOKE-CM fixtures
(order + requisition at ids 1, created by the smoke and torn down by its
teardown). The original screen defaulted to those ids — so a real user's
save failed ("id to load", fixtures absent) while the trace's save had
passed. **The trace proved the write's mechanics against a state the
standalone screen cannot reach; the merge gate did not catch a
fixture-dependent screen.** The screen now requires the live order /
requisition ids as inputs (no fixture defaults) and fails honestly with the
backend's message if they are absent. LESSON for future golden traces: run
the trace against the state a real user can reach — seeded data only, never
transient fixtures.

## Verification registers
- park_sweep 18/18 (the stock extension + cross-match BUILDABLE + zero
  leftovers incl. the details table).
- T3 gates: tsc EXIT 0, ng build EXIT 0, check_route_targets PASS.

# TRANSACTION LOOP (proposed — for owner review, NOT executed)

Replaces the MASTER LOOP (RULES.md) for the ~108 transaction screens + 35
reports + 13 dashboards remaining (DQ-025/032/034). The master loop —
classify by live call, list endpoint + envelope + grid, status toggle,
probe per screen — does NOT transfer: transactions are multi-step,
patient-context, write-heavy flows. DQ-034's six requirements are the
skeleton; this document is the loop around them.

## T0 — CLASSIFY (same as master, stricter)

Live call the FULL flow's endpoints with the exact legacy payload shapes —
never classify from static structure (the run's lesson, 3 independent
instances: consentType, 580/582, decoys). Output per screen:
- the patient/encounter picker entry point (how does the legacy open the flow?)
- every endpoint in the flow (list + detail + write + status), with
  envelope shape + live status
- the status machine: states + legal transitions (e.g. pending ->
  approved -> settled -> written off)
- which business rules the flow touches (BR manifest additions required)

## T1 — PATIENT CONTEXT (how it is established and propagated)

- The legacy flow opens from a patient/visit/encounter selection (search by
  UHID/name/visit). The rebuild MUST keep the same entry contract: the first
  component is a picker, and the selected context object (patientId,
  visitId/encounterId, orgId, unitId) is carried explicitly.
- Propagation: a small PatientContext service (signal-based, injectable,
  session-scoped) holds the selected context; every downstream component
  reads it via inject(), never via route params or duplicated lookups.
  Route params carry only ids, not the object.
- Golden trace starts AT THE PICKER — the probe selects a real patient and
  follows the context through every downstream step. A screen probed without
  the picker step is not verified.

## T2 — FLOW MAP (end-to-end, not per-screen)

- Before any build: one flow map per screen — the sequence of steps, each
  step's component, endpoints, status transitions, and the data contract
  between steps (what step N+1 needs from step N).
- Verification is END-TO-END: the probe drives the whole chain (picker ->
  step 1 -> step 2 -> ... -> final write -> status change) in ONE browser
  session, asserting each step's rendered data against live rows. Per-screen
  probes are a smoke gate only; they cannot catch broken joins between steps.
- The owner signs off the flow map before the build (like BR entries).

## T3 — BUILD (house pattern per step)

- Each step is a standalone component in the screen's feature dir, sharing
  the screen service. Same C9 pattern (@if/@for, signals, API_BASE_URL
  inject, ApiResponse<never, Row>).
- Write endpoints (save/update/status) get explicit service methods with
  doc-comments naming the legacy controller + the write's business rule.
- No step renders "success" from a 200 alone: it must render the response
  body's status field.

## T4 — WRITE VERIFICATION (when the write matters)

A transaction write mutates billing/stock/patient state — the master-loop
"save returns 200" check is insufficient. For every write path:
- IDEMPOTENCY: replay the same write twice; the second must not double-post
  (legacy contracts with business keys — assert the key is reused, or the
  duplicate is rejected with the same body status).
- ROLLBACK/ALL-OR-NOTHING: flows that write multiple entities (bill -> line
  items -> payment) must fail as a unit; the probe forces a mid-flow failure
  (e.g. invalid line item) and asserts NO partial write landed (the legacy
  transaction boundary is checked, and the golden trace records what the
  legacy ACTUALLY does — if the legacy half-writes, that is a masked
  behavioral defect to log, not to replicate).
- AUDIT: createdBy/createdDate/updatedBy/updatedDate on every write; the
  probe asserts the fields are populated with the session user.
- Never trust the frontend's success flag: assert the NEXT read-back (a
  fresh list call) shows the written row in the expected state.

## T5 — STATUS MACHINE + NEGATIVE CONTROL

The negative control for a status machine is the ILLEGAL TRANSITION:
- For each legal transition (pending->approved), the probe asserts the
  screen renders the post-transition state.
- The negative control drives an ILLEGAL transition (e.g. settle a
  pending-not-approved bill, approve an already-settled one, reject a
  discharged visit) and asserts the backend REJECTS it (body status !=
  success, or a masked envelope whose mechanism is logged) and the UI shows
  the error — it must NOT silently succeed or crash.
- A machine with no illegal-transition test is unverified: the guard is
  the whole point of a status machine.
- Also: the state that can never happen (e.g. two approvals) is probed as
  the strongest negative — assert the second transition is denied.

## T6 — GATES (the list as it stands, transaction-amended)

1. tsc --noEmit -p tsconfig.app.json (plain tsc is VACUOUS — files:[] tsconfig)
2. ng build EXIT 0
3. tools/check_route_targets.py PASS (every route's loadComponent resolves)
4. live probe — FRESHNESS GATE FIRST (stale/DOWN server = RC 2, zero
   behavioral assertions)
5. flow map signed off by owner (T2)
6. write-path verification (T4) + illegal-transition negative control (T5)
7. BR manifest entries for every business rule touched
8. negative control for the base permission (id 999999 denies)

## Scope guards

- Reports (35) and dashboards (13) stay parked until this loop is agreed;
  dashboards may fall out of it (status-keyed worklists — T5's machine is
  their core).
- The 20 not-rebuilt duplicate-relisted states (DQ-035) are transaction
  class — they inherit this loop, not the master loop.
- Nothing here is executed until the owner approves this document. First
  transaction build only after sign-off.

## Open questions for the owner (answered before the first build)

1. Does the golden trace need to run against REAL patient data in dev (the
   picker must find a patient), or is a seeded dev patient acceptable?
   (Master loop had zero seeding — but a picker with no patients cannot be
   probed end-to-end.)
2. Write-path probing posts real transactions against dev billing/stock.
   Is a dedicated dev-only transaction dataset required, or do the legacy
   dev rows suffice with rollback verification?
3. The masked write-path class (200 status=error with code=null) — for
   transaction writes this is the highest-stakes mask class. Confirm the
   rule: every masked write is logged as a BR/DDL candidate AND the rebuild
   renders an honest error (no false success), consistent with Rule 10.

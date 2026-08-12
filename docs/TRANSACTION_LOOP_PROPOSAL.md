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
instances: consentType, 580/582, decoys). PAYLOAD LITERALISM (owner
amendment, 2026-08-12): the payload is READ FROM THE CONTROLLER'S LITERAL
SIGNATURE, never reconstructed from the field's apparent meaning — the
assignLoyaltyCard flow carries the legacy typo `orgnisationId` (missing 'i')
that must be sent byte-for-byte or the feed 500s, and getLoyaltiCardDetailsBy
PatientId is POST not GET. A payload that looks right and is spelled
differently is indistinguishable from a backend defect until someone reads
the source. Output per screen:
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
- FLOW-MAP SIGN-OFF (Amendment 2): the owner signs off the first THREE
  flow maps in full. After that, sign-off is one per flow FAMILY (billing,
  admission, pharmacy, lab, blood bank): the agent proceeds within a signed
  family on its own, surfacing only what does NOT match the family's signed
  shape. Review stays on the shape, off the repetition.
- GUARD EXISTENCE AT T0 (Amendment 4): before designing ANY illegal-transition
  control, T0 reads the DAO and service for the guard that would reject it.
  Three outcomes:
    * GUARD EXISTS -> T5 drives the illegal transition and asserts rejection.
    * NO GUARD, LOW STAKES (nothing corrupted, no money/stock/clinical state
      wrongly moved) -> log as a BR candidate, note the UI enforces by
      convention, build proceeds. (assignLoyaltyCard's second-card-per-patient
      is this case.)
    * NO GUARD, HIGH STAKES (money, stock, clinical state, permissions) ->
      STOP and come to the owner. THIS is where the stop rule belongs — the
      severity test is what the missing guard lets someone DO, not that it is
      missing.
    * SHARPER SEVERITY TEST (owner refinement, 2026-08-12, OPD Bill T0): what
      matters is whether THE SCREEN BEING BUILT can reach the gap, not whether
      the gap exists. A money-class gap reachable only by a crafted API
      payload (not by this screen's path) does NOT stop the build — the
      rebuild sends the same flags the legacy sends and stays on the guarded
      path. It is logged as a money-class BR candidate with the mechanism,
      explicitly framed as an API-surface defect (reachable by direct API
      call) rather than a UI one — that framing matters for whoever fixes it.

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
  duplicate is rejected with the same body status). IDEMPOTENCY DISCOVERY IS
  T0 (Amendment 5): whether the write is idempotent by construction is
  established when the DAO/service is read at T0 — never discovered by
  running the replay. The replay then runs as a DEMONSTRATION of a known
  finding, on the fixture, cleaned same-run (assignLoyaltyCard: no business
  key, each POST creates a new card + mapper row -> double-posts by
  construction; demonstrated, logged as BR candidate, not replicated, not
  stopped at this stake level).
- ROLLBACK/ALL-OR-NOTHING: flows that write multiple entities (bill -> line
  items -> payment) must fail as a unit; the probe forces a mid-flow failure
  (e.g. invalid line item) and asserts NO partial write landed (the legacy
  transaction boundary is checked, and the golden trace records what the
  legacy ACTUALLY does — if the legacy half-writes, that is a masked
  behavioral defect to log, not to replicate).
- FLOW-LEVEL ATOMICITY (Amendment 6, 2026-08-12, OPD Bill T2): where a flow
  spans MULTIPLE HTTP writes, the trace records what the legacy LEAVES
  BETWEEN them. A partial write that ALSO LOCKS THE RETRY PATH is the worst
  finding class — worse than either the write failing or succeeding —
  because it requires manual database intervention to recover. The lock test
  is live-proven (psql after a forced mid-flow failure), and if the lock
  confirms, a recovery-path grep (cancel/void/re-open that resets the
  locked flag) decides whether the defect is serious-operational or merely
  ugly. Code-read is hypothesis until psql confirms.
- DESTRUCTIVE-TEST CONTAINMENT (Amendment 1): every destructive test (the T4
  rollback probe and the T5 illegal transitions) runs against a DEDICATED
  seed fixture — torn down and re-seeded per run — NEVER against the shared
  golden-trace data. The probe asserts the fixture's row count before and
  after, and FAILS if the teardown leaves anything behind (Q28's cleanup
  assumes successful writes; destructive tests deliberately violate that).
  If an illegal transition SUCCEEDS where it should be rejected: stop,
  report, re-seed. Do NOT continue probing on a corrupted fixture.
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
- MASKED-ENVELOPE DISAMBIGUATION (Amendment 3): a masked response cannot
  by itself tell rejection from silent success. Whenever an illegal
  transition returns a masked envelope, verify in psql whether the illegal
  transition ACTUALLY happened:
    * response masked + STATE UNCHANGED = rejection with a bad message
      (log the bad message as the defect)
    * response masked + STATE CHANGED = security defect (the write went
      through despite the mask) — falls under the masked-write rule: STOP,
      come to the owner.
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

## Owner decisions (2026-08-12 — these are the rules, not questions)

1. DEV PATIENT DATA: SEEDED, not real. Every seed is additive, idempotent,
   DEMO-001-scoped, guarded to the his_local database, and cleaned in the
   same run (per Q28). No production data reaches this machine.
2. DEV TRANSACTION DATASET: YES — one documented, reproducible SEED SCRIPT
   (not ad-hoc rows). A transaction chain needs a patient, an encounter, a
   bill and a payment to trace end to end; hand-made rows drift. Same rules
   as (1): additive, idempotent, DEMO-001-scoped, his_local-guarded, cleaned
   in the same run. The seed script lives under tools/ with the probe set
   and is part of the T1 golden-trace setup.
3. THE MASKED-WRITE-PATH RULE (BR-187 shape — save reports success, writes
   nothing): a transaction screen whose WRITE path is masked does NOT ship
   with a banner. IT STOPS AND COMES TO THE OWNER. Banner parity is
   acceptable for READS and UNACCEPTABLE for WRITES: a read that lies costs
   a user a confused minute; a write that lies costs a payment, a
   prescription or a consultation record. A masked write is logged as a
   BR/DDL candidate AND blocks the screen's build until the owner decides.
   BROKEN-SCREEN vs MASKED-WRITE (Amendment 7, 2026-08-12, OPD Bill DQ-044):
   the STOP applies to writes that LIE (envelope says success or omits the
   error while the write failed), NOT to writes that FAIL HONESTLY (envelope
   says error / code 500 and psql confirms rollback). A screen whose write
   is honestly broken is a PARK-OR-BANNER decision for the owner (backend
   availability defect; the BR fix is backend-owned per Rule 10 and
   maker-checker), not a STOP. The class is read from the response envelope
   PLUS psql state: honest-error + clean rollback = broken screen;
   masked + state unchanged = rejection with a bad message (log, banner);
   masked + state changed = security defect (STOP).
4. T0 SMOKE THE PRIMARY WRITE FIRST (Amendment 8, 2026-08-12, from the OPD
   Bill phantom-column lesson): before designing the flow map, call the
   flow's PRIMARY WRITE endpoint ONCE against the seeded fixture with an
   exact-payload minimal body. Five minutes of calling saveBill would have
   found the package_applicable_date phantom (every-saveBill masked failure)
   BEFORE the map was drafted. A write that cannot complete on ANY payload
   shape is a park-candidate, not a build; the smoke result gates whether
   the flow map is even drafted.

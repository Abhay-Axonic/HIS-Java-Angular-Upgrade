# THE TRANSACTION LOOP — the method, the eleven amendments, and the checks

2026-08-13. This is the single readable account of the transaction-rebuild
method as it actually ran. RULES.md and the proposal carry it in pieces;
every rule below exists because a specific failure produced it. A future
session can follow this from scratch.

---

## PART 1 — THE LOOP AT A GLANCE (T0 → T6)

For each transaction flow (screen with a write path: picker → chain →
status machine):

- **T0 — SMOKE THE PRIMARY WRITE FIRST** (Amendment 8). Call the flow's
  primary write endpoint ONCE against a seeded fixture with an exact-payload
  minimal body. Read the envelope AND psql. The verdict gates whether the
  flow map is even drafted: a write that cannot complete on ANY payload
  shape is a park-candidate, not a build. This single step found five
  phantom columns (technique_id, is_bed_charges_service, form_template_id,
  icpc_id, package_applicable_date) before a map was drafted for any of them.
- **T1 — GOLDEN TRACE.** Capture every network call through the real legacy
  UI: URL, method, payload, response, status. The payload is read from the
  controller's LITERAL signature (PAYLOAD LITERALISM), never reconstructed
  from meaning — a whole run of learning was two fresh bites (the
  `orgnisationId` typo that the DAO actually reads, and the
  `transferdId`/`tranferRequestId` typos that strict Jackson rejects live —
  the second is a legacy BUG, not a contract: 400 at deserialization =
  deviate and log; 200 with an envelope error reaching the service =
  contract, preserve).
- **T2 — FLOW MAP.** Every endpoint (list + detail + write + status), the
  envelope shapes, the status machine (states + legal transitions), the
  business rules touched, the guard reads scoped to the WRITE methods.
- **T3 — BUILD.** The screen from the map. No invented lists, no invented
  component state; honest-state shells where a feed is dead.
- **T4 — VERIFY SUCCESSES AND FAILURES** (Amendment 10). psql after EVERY
  write — including every ERROR envelope. An error with partial rows is a
  half-commit hiding behind a generic message, invisible to any
  success-only check. Transfer W2/W3 both have empty catches; tomcat shows
  NOTHING when they fail; psql is the only truth.
- **T5 — LEGAL + ILLEGAL TRANSITIONS.** On a DEDICATED fixture (Amendment 1),
  drive one legal transition and one illegal, psql after each. A masked
  envelope on an illegal transition is disambiguated in psql (Amendment 3).
  A negative control you cannot construct is not a negative control — trace
  the Java (controller → service → DAO → named query) and construct EVERY
  failing case (Amendment 2's gap-close pattern, DQ-038).
- **T6 — THE GATES + THE BROWSER GOLDEN TRACE.** tsc (real gate), ng build,
  route targets + route survival, THEN the golden trace through the actual
  browser UI in ONE session — list → write → verify psql after each. A
  per-endpoint probe cannot construct a payload built from a prior write's
  response (W1→W2 contract); only the full browser chain can.

## PART 2 — THE ELEVEN AMENDMENTS, EACH WITH THE FAILURE THAT PRODUCED IT

**Amendment 1 — destructive-test containment.** *Produced by:* Q28's cleanup
assumes successful writes; a destructive test (T4 rollback probe, T5 illegal
transition) that deliberately breaks that would corrupt the shared golden
data and poison every later run. *The rule:* every destructive test runs
against a DEDICATED fixture — torn down and re-seeded per run, row-count
asserted before and after, FAIL if teardown leaves anything. If an illegal
transition SUCCEEDS where it should be rejected: STOP, report, re-seed.

**Amendment 2 — flow-map sign-off scaling.** *Produced by:* the owner
reviewing eleven flow maps in full would stall the phase. *The rule:* the
first three flow maps are signed in full; after that, ONE sign-off per flow
FAMILY (billing, admission, pharmacy, lab, blood bank), and proceed within a
signed family.

**Amendment 3 — masked-envelope disambiguation (T5 logging).** *Produced
by:* a masked response cannot by itself tell rejection from silent success —
the same envelope could mean either. *The rule:* masked + STATE UNCHANGED =
rejection with a bad message (log the bad message as the defect); masked +
STATE CHANGED = security defect → masked-write STOP to the owner.

**Amendment 4 (sharpened) — the severity test is SCREEN-REACHABILITY, not
gap-existence.** *Produced by:* the OPD bill's double-bill guards, which
only run when `isFinalBill=='Y' || isIntermediateBill=='Y'` — a no-flag
bill bypasses both, but the rebuild's own screen always sends the flags and
stays on the guarded path. *The rule:* a gap reachable only by a crafted API
payload does NOT stop the build — log it as a money-class BR candidate
framed explicitly as an API-surface defect. A gap the SCREEN can reach DOES
stop the build (Amendment 9 sharpens the high-stakes branch).

**Amendment 5 — fixture teardown covers EVERY table a multi-entity write
touches (the TEARDOWN RULE).** *Produced by:* a candidate that deleted the
master and orphaned its details — a successful run quietly pollutes later
ones, and it took a harness check to find the orphans. (Same class, one day
later: the seed's WHERE-NOT-EXISTS guard that SKIPPED rather than HEALED a
pre-donor bag — a re-run produced a different state from the first and
nothing said so. The SKIP-VS-HEAL rule: any seed that wires a relationship
must heal an existing row, not just skip inserting a duplicate.) *The rule:*
the leftover assertion must query EVERY table the write touches, and the
teardown must be FK-ordered (children → parents; bill tables delete views'
base tables, never the view).

**Amendment 6 — FLOW-LEVEL ATOMICITY (multi-HTTP-write flows).** *Produced
by:* T4's all-or-nothing clause assumed ONE transaction boundary; the OPD
bill spans THREE HTTP writes (saveBill → collectPayment →
updateAppointmentStatus) in THREE transactions. *The rule:* where a flow
spans multiple writes, the trace records what the legacy LEAVES BETWEEN
them. A partial write that ALSO LOCKS THE RETRY PATH is the worst class —
worse than failing or succeeding — because it needs manual DB intervention.
The lock test is live-proven (psql after a forced mid-flow failure), never
assumed from a code read. (The OPD bill's lock hypothesis was answered on
the NEGATIVE — saveBill is one transaction and rolled back cleanly; verify
both ways.)

**Amendment 7 — BROKEN-SCREEN vs MASKED-WRITE (the truthfulness test).**
*Produced by:* the OPD bill STOP — saveBill's honest error envelope needed a
different class than a lying write. *The rule:* the masked-write STOP
applies to writes that LIE, not writes that fail honestly. Envelope says
error + psql clean rollback = BROKEN SCREEN (backend availability defect),
park-or-banner, fix is backend-owned (Rule 10) — NOT a STOP. Envelope says
success + state unchanged = rejection with a bad message. Envelope masked +
state changed = security defect → STOP. Class is read from envelope AND
psql state, never the envelope alone.

**Amendment 8 — SMOKE THE PRIMARY WRITE FIRST (T0 before the flow map).**
*Produced by:* five minutes of calling saveBill would have found
package_applicable_date BEFORE the map was drafted. *The rule:* T0's smoke
gates whether the map is even drafted (see Part 1).

**Amendment 9 — screen reachability in the HIGH-stakes branch.** *Produced
by:* the transfer's status machine is UI-convention only — every write's
UPDATE WHERE is transferId+unitId+orgId with NO status precondition, so a
direct API call can accept a completed transfer or finalise a rejected one.
*The rule:* for a missing guard at HIGH stakes (money, stock, clinical
state, permissions): if the SCREEN cannot reach the gap (its buttons only
appear in legal states), log it as a BR candidate (framed as API-surface
reachable) and BUILD PROCEEDS; if the screen CAN reach the gap, STOP.

**Amendment 10 — VERIFY FAILURES, NOT ONLY SUCCESSES.** *Produced by:* the
transfer's W2/W3 empty catches — an ERROR response carries NO information
about what landed, and success-only checks are blind to half-commits. *The
rule:* psql-check state after EVERY error as well as every success. The
screen itself must do the same: after a write that may have half-landed,
reload the list and treat the refreshed state as the truth, not the
envelope.

**Amendment 11 — EXPECTED-FINDINGS LIST (T0 checks all of them by default).**
*Produced by:* three flows produced the same three classes repeatedly. *The
rule:* on every new flow check by default: (1) UI-convention state machine —
status preconditions in the write methods; none = the machine exists only
in button visibility (BR candidate); (2) payload built from a PRIOR write's
response — per-endpoint probes cannot construct W2/W3; (3) error swallowing
— empty catches in the write methods; (4) flush-boundary non-atomicity —
an entity load (findById/session.get) between write statements forces a
Hibernate flush and makes the earlier updates commit before the later
failure; ERROR envelope + partial psql state = look for the load.

## PART 3 — THE CHECKS AND THEIR NEGATIVE CONTROLS

Every check below has a negative control: a way to prove the check can
FAIL. A check without one is not a check.

| Check | What it verifies | Its negative control |
|---|---|---|
| tsc -p tsconfig.app.json | type errors | It passed while a guard-less route was fine and a class-name mismatch compiled — tsc is necessary, never sufficient. The control is the route-survival baseline (below), which exists BECAUSE tsc could not see a route you didn't mean to touch. |
| ng build EXIT 0 | AOT/bundle | It passed while a route referenced a wrong component class — the runtime lazy-load would break, the build would not. Control: the browser golden trace (a real render of the route). |
| check_route_targets.py | lazy targets resolve to existing files | POSITIVE-ONLY: it cannot see a route that is MISSING or a guard that is absent. Control: check_route_survival.py — the 364-route baseline-subset assertion that a route you did not mean to touch is still there (caught a fuzzy-patch corruption of kitCreation). |
| The smoke's psql counts | the write landed | 200 + 0 rows = never ran; sequence advanced + row missing = rollback; is_called=false = never wrote. The envelope alone is never the truth. |
| The golden trace | the write through the real UI | The FIXTURE-DEPENDENCY control (owner's click, 2026-08-13): a trace that runs against transient fixture rows the standalone screen cannot reach PASSES while the real user's save FAILS. Lesson (now a class — two instances, cross-match + bloodIssue): the trace must run against seeded state a real user can reach. |
| Freshness gate (served bundle == current source) | the probe's target is the current code | Stale bundles have presented as false Access Denied / compile-error overlays four times. Control: restart the dev server and re-probe before believing any failure. |
| Token validity | the probe is authenticated | get_dev_token --check can say LOGIN-OK while the cache 401s; a browser re-login invalidates the cache and every candidate 401-floods. Control: re-mint on any 401 and check the tool's OWN headers (park_sweep post() sends token + userId/unitId/orgId — bare-token scripts 401). |
| Tirith resolvability gate | the environment is safe to work in | tirith FAIL-OPEN: the gate could pass while the resolver was broken. Control: verify_tirith_resolvable.py asserts the resolver actually resolves. |
| The route-survival baseline | every existing route survives | Proven both directions: PASS on the current state AND a synthetic routes file missing one route exits 1 and names the victim. |
| The seed's idempotency re-run | the seed is reproducible | The SKIP-VS-HEAL trap: a WHERE NOT EXISTS that skips rather than heals makes re-run N produce a different state from run 1 with zero signal. Control: the heal UPDATE + the re-run's row-count assertion. |
| Verification harnesses (ad-hoc) | the changed behavior | Harness false-fails caught: pre-existing dev rows counted as leftovers (fix: marker-aware deltas), a top-level `(select)+(select)` psql syntax error, case-mismatched patterns. Control: every harness asserts its own preconditions before trusting its verdict. |

The standing gate list, in order, every probe: freshness gate first, token
validity, tsc, ng build, check_route_targets, route survival, then the live
probe with psql — and the verification harness that re-checks the changed
behavior with its own negative control.

---

**What the loop is for.** The transaction phase ran 16 candidate smokes and
eight full flows. The method's value is finding where it is wrong before it
is applied ~100 times: the eleven amendments are eleven specific failures
that became standing rules, and the checks' negative controls are the
instrument that keeps the checks honest.

# SESSION CLOSE — 2026-08-13

The transaction survey and rebuild phase is closed. This is the handoff
state: what is on main, what is queued, and what a fresh session reads
first.

---

## STATE ON MAIN

The phase's merged history (all owner-merged and pushed):

| Merge | Lands |
|---|---|
| 472d4d01 | the four signed flows: transferScreen (65), donorRegistration (719), shiftClosing (600), phlebotomistHomeCollection (155) + the drift map + the park taxonomy |
| 8cef5044 | crossMatchRegister (the 5th flow) + the sweep close-out + the TEARDOWN + route-survival standing checks |
| a5c8af96 | the compounding-seed rule + nursingOrder BUILDABLE + the opd-bill endpoint correction |
| 2bd0e7ad | the 12th CONFIRMED phantom (icmr_id) + the LIS seeded context + the seeding instrument's own limit |
| 8dd709a1 | the (a) smokes (contract-incomplete at the DTO walls) + PHASE_HANDOVER.md |
| 473fb869 | the three T0-signed flows: bloodIssue, nursingOrder, labSampleType (the 6th–8th signed flows) |

**Eight signed transaction flows on main, 364 routes, REBUILD_COUNT 362.**
The dev server serves main on :4201; the number-config seed is in place
(4 prefix config rows + 4 running numbers + the stock bag + the sample-type).

## WHAT IS QUEUED (the branch `rebuild/t0-smokes-and-handover`, 1 commit ahead, 0 conflicts)

The owner's click-through fixes + the phase's closing documents, awaiting
the merge:
- the crossMatchRegister + bloodIssue fixture-dependency fixes (live
  required ids, honest error handlers — both verified by the owner's click)
- the fixture-dependency audit (DQ-075) + the compiled-emits removal
  (22 tracked .js, the .gitignore rule)
- docs/METHOD_WRITEUP.md (item 1 — the eleven amendments + the checks)
- docs/VERIFICATION_FAILURE_LEDGER.md (item 2 — the twelve mechanisms)
- this close (item 3)

**Queued behind the backend owner** (nothing the frontend can do until
someone fixes these — the full detail is PHASE_HANDOVER.md):
- the 12 confirmed phantoms (table + column + evidence in the handover) —
  each unblocks a smoke-ready screen: patient registration, appointment,
  cash handover, deposit, walk-in, OPD bill, LIS central-receiving
- the two code defects: the inverted admission bed-occupancy gate, the
  I/O-chart masked-partial
- the parked excavations (the opd-bill / LIS result entry / bill payee /
  pharmacy issue — contract walls behind confirmed drift)

## WHAT A FRESH SESSION READS FIRST (in order)

1. `RULES.md` in full (the constitution; every session starts here).
2. The last 10 entries of `docs/SESSION_LOG.md` (what just happened).
3. `docs/PHASE_HANDOVER.md` (the phase's output — what a backend owner
   acts on: the 8 signed flows, the 12 phantoms, the 2 code defects, the
   bands, the resume-when-fixed table).
4. `docs/METHOD_WRITEUP.md` (the transaction loop: T0–T6, the eleven
   amendments with the failures that produced them, the checks with their
   negative controls).
5. `docs/VERIFICATION_FAILURE_LEDGER.md` (the twelve mechanisms that passed
   while structurally incapable — distrust them by default).
6. `docs/DECISIONS_QUEUE.md` tail (DQ-052…075 — the run's evidence) and
   `docs/BUSINESS_RULE_MANIFEST.md` + `docs/BR_INDEX.md` (BR-038/039/041,
   BR-040 retracted).
7. The tools' state: `tools/seed_number_config.py` (idempotent — re-run
   before any flow work), `tools/park_sweep.py` (16 candidates),
   `tools/check_route_survival.py` (the 364-route baseline standing check),
   `tools/get_dev_token.py` (re-mint on any 401).
8. Environment facts: tomcat@9 on 8080 (grep catalina.out by ERR-ref),
   Angular dev server on :4201 (freshness gate first — stale bundles have
   presented as findings four times), psql `his_local`, machine TZ BST vs
   app IST, login admin / the dev credentials with the `t_user_login_log`
   collision fix.

## THE BOUNDARY, RESTATED

The survey is complete and bounded: every park has a cause, the causes are
sorted (drift / code defects / unseeded environment / contract walls), and
the rebuild's backend-independent work is exhausted. The next move is the
backend owner's — the handover names the 14 items and the exact screens
each unblocks. When the owner decides whether to wait on the backend or
start something new, this is the state they decide from.

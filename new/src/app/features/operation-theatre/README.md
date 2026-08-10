# operation-theatre — parity rebuild of the legacy Operation Theatre screen

Legacy source:
- template: `views/master/GlobalMaster/OTIMS/operation_Theatre.html` (309
  lines, real)
- state: `.state('operation_Theatre')` REGISTERED at app.js:12357 (dedicated
  `operationTheatreController` — the controller file exists
  (controller/otims/global/operationTheatreController.js) though NOT loaded
  in index.html; noted, not a reachability failure)
- menu node: 519 'Operation Theatrer' [sic — legacy typo], parent 514 OTIMS
  -> 369 Global Master, actionLink `#/operation_Theatre` (note the
  underscore — the rebuild route uses the SAME token so the menu click
  resolves)

## CLASS: LIVE (per the amended reachability rule)

State registered + menu node exists + a REAL full-CRUD backend exists (Global
OperationTheatreController @RequestMapping("/api/otims"): save, update,
updateStatus, list, byId, count, active-list). The rebuild REGISTERS the
route (moduleId 519) under the menu's actionLink token.

## Backend — verified BY CALLING (cached token, 2026-08-10), BODY INSPECTED per the ENDPOINT-VERIFICATION STANDARD (RULES.md 5c2432fd)

- list:  POST /api/otims/getOperationTheatreList -> 200, body status
  'success', listObject len=1 — a REAL dev row (id 8, code OT-TEST-01,
  'Test Theatre One', type 11 'test theatre', status A — seeded for batch 34
  Step 6 review)
- count: POST /api/otims/getOperationTheatreCount -> 200, body 'success'
- types: POST /api/otims/getActiveOperationTheatreTypeList -> 200, body
  'success', listObject len=2 (real type rows)
- Payload shape: {orgId, unitId, offset, noOfRecordsPerPage} (NOT
  organizationId — an initial probe with organizationId 400'd; the
  probe-payload class, caught BEFORE code, same lesson as screen 17).

## FINDING (by calling, the strongest of the screen) — the SAVE reports SUCCESS while writing NOTHING when the payload is incomplete

The save endpoint (POST /api/otims/saveOperationTheatre) was CALLED with the
BODY inspected (the new standard):
- FULL DTO (theatreCode/name/desc/typeId/status/orgId/unitId/createdBy/
  createdDate/updatedBy/updatedDate): HTTP 200 body status 'success'
  "Added Successfully." AND the row REALLY LANDED (probe row id 9 in
  otims.m_operation_theatre, sequence 8->9; then DELETEd — table back to
  the 1 dev row).
- INCOMPLETE DTO (updatedDate omitted): HTTP 200 body status 'success'
  "Added Successfully." BUT the sequence did NOT advance and NO row was
  written (verified: psql found nothing, DELETE 0). The server log shows
  the cause: NPE at OperationTheatreDaoImpl:53 (getDate(updatedDate) with a
  null dateString) — the DAO catch (line 59-63) logs and returns the ERROR
  envelope, but the SERVICE's SUCCESS return (line 73) sits OUTSIDE the
  `if(res.getObject()!=null)` guard and fires unconditionally whenever no
  exception PROPAGATES — and the DAO's catch means no exception propagates.
  The swallow fakes the transport layer AGAIN (screen-17 class), now on a
  WRITE path: a client sees "Added Successfully." for a save that wrote
  nothing.
- THE CONSEQUENCE for this rebuild: a bare create form sending a partial
  DTO would produce exactly this silent no-write false success — the
  strongest possible reason the save is NOT wired (the divergence, now with
  its mechanism). Wiring the create form requires the full DTO contract AND
  the false-success class fixed server-side (or surfaced honestly).
- NOT FIXED (Rule 10). Backend BR candidate: move the SUCCESS return inside
  the guard / rethrow on the DAO failure.

## Scoped backend check (per the standing rule, every class)

- Phantom columns/tables: PASS — the live 200s with 1 real row prove
  GET_OPERATION_THEATRE_LIST resolves against real DDL.
- Named-query INNER-JOIN null-FK drops: FINDING — the list INNER JOINs
  operationTheatre.operationTheatreType (`INNER JOIN
  operationTheatre.operationTheatreType opTarTyp`); an OT row whose
  theatre-type is missing/inactive is DROPPED from the list. The walk's
  THIRD list-path INNER-JOIN instance (after formulationTypeROAMaster
  getAll QF-2026-08-10-G and procedureConfiguration QF-2026-08-10-Q). The
  store join is LEFT (safe). NOT FIXED (Rule 10).
- Dup-check operator precedence: PASS — GET_OPERATION_THEATRE_BY_NAME (save
  path, DAO:199) AND GET_OPERATION_THEATRE_BY_NAME_NOT_ID (update path,
  DAO:218) are BOTH properly scoped all-AND queries: BY_NAME is
  `LOWER(name)=:n AND orgId=:o AND unitId=:u`; BY_NAME_NOT_ID is
  `id!=:id AND LOWER(name)=:n AND orgId=:o AND unitId=:u` (entity lines
  67-71 — the full query continues past line 70; an earlier truncated read
  mischaracterized it as unscoped; CORRECTED here). NOT the Q32 class. The
  dup-check family count stays at 4 (BR-169, Q29, Q32, screen-12 search).
- Org-trust: FINDING — NO VerifiedIdentityService in the module; orgId/unitId
  come from the request DTO on reads (list/count bind :orgId/:unitId) AND
  writes (setOrgId/setUnitId from the DTO at DAO:47/77) — the payload-carried
  org shape (vaccinationMaster class). NOT FIXED (Rule 10).
- Twin queries: PASS.
- Swallowed exceptions on write paths: FINDING — the controller catches and
  returns COMMON_ERROR_MESSAGE on EVERY method (the campaign #1 shape; the
  global handler is bypassed — the same class that masked screen 17's
  phantom as HTTP 200). NOT FIXED (Rule 10).

## Verification state — HONEST

- preflight C1-C6 PASS (C5 LIVE — state registered + node 519 chain, and the
  fixed C5 now matches the state name, not the rebuild template filename);
  `npm run build` exit 0; smoke standard PASS (FINAL-URL
  /operation_Theatre, moduleId 519 present, SERVE-BRANCH matches the branch
  under test); interaction probe PASS (grid renders the REAL dev row
  OT-TEST-01, 0 page errors).
- READ VERIFICATION: list/count/types called and the BODY inspected —
  all 3 body status 'success'.
- WRITE VERIFICATION: none ran (no-seed; save/update/status write real
  rows). The rebuild is read-only (list + search); save/update/status are
  MAPPED but NOT wired — the legacy form collects theatre-type/store feeds
  into the full DTO; a partial DTO would be a probe-payload-class failure
  (the screen-15 lesson). They are never claimed as "not erroring" either:
  UNVERIFIED (Q28).

## Deliberate divergences (flagged, not silent)

1. READ-ONLY REBUILD: list + search only. The create/edit form (save/
   update/status + theatre-type/store feeds) is NOT wired for a REASON WITH
   A MECHANISM (see the save false-success FINDING above): a bare form
   sending a partial DTO produces a silent no-write false success — the
   endpoint claims "Added Successfully." while writing nothing (NPE
   swallowed at DAO:53, SUCCESS fired unconditionally at service:73). The
   create form is a follow-up queue item, not silently dropped.
2. Route token: the rebuild registers `operation_Theatre` (the menu's
   actionLink token, underscore included) — NOT the normalized
   `operationTheatre` — so the menu click resolves in the new app.

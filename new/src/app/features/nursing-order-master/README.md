# nursing-order-master — parity rebuild of the legacy Nursing Order Master screen

Legacy source:
- template: `views/master/GlobalMaster/nursing-order-master.html` (271 lines)
- controller: `nursingOrderMasterController.js` (563 lines — a REAL dedicated
  controller, unlike the MasterController shells of screens 9-11)
- state: `nursingOrderMaster`, module id 480, parent 476 Clinical Workstation
  -> 369 Global Master, `#/nursingOrderMaster` (hand-confirmed: app.js:8387
  REGISTERED with a resolve gate + node 480 in the privilege tree)

## This screen is a REAL full-CRUD screen (the walk's first)

Unlike screens 9-11 (registered static shells whose real CRUD lived under a
different, orphaned state), nursingOrderMaster has:
- a dedicated legacy controller with real save/update/status/list/search code
- a complete backend: `NursingOrderMasterController` (Global module,
  `/api/global/unit/*`), entity `NursingOrderMaster` (`adt.m_nursing_order_master`),
  DAO + service
- live-verified endpoints (CALLING with the cached token, 2026-08-10):

  POST /api/global/unit/getNursingOrderMasterList        -> 200 success (dev: 0 rows)
  POST /api/global/unit/getNursingOrderMasterCount       -> 200 success (count 0)
  POST /api/global/unit/getNursingOrderMasterListSearch  -> 200 success (dev: 0 rows)
  GET  /api/global/unit/getActiveNursingOrderMaster/1/1  -> 200 success (dev: 0 rows)
  GET  /api/global/unit/getNursingOrderMasterById/1/1/1  -> 200 success (dev: 0 rows)
  POST /adt/getActiveSpecialityList                      -> 200 success (2 rows — General Medicine id 1 …)
  POST /adt/getSubSpecialityBySpecialityArrayForTariff   -> 200 success (3 rows for speciality 1)
  POST /global/serviceMaster (search)                    -> 200 success (dev: 0 rows for 'a')

The rebuild wires the real endpoints: the grid loads the list feed, the
speciality/sub-speciality selects load their real feeds, the service search
hits the service-master feed, and Save sends the full legacy contract to
saveNursingOrderMaster.

## Scoped backend check (per the standing rule, every class)

- Phantom columns/tables: PASS — entity `@Column` set (13) == DDL columns of
  `adt.m_nursing_order_master` (13), verified by the 200 responses executing
  the named queries against real DDL. The three @ManyToOne join columns
  (service_id, speciality_id, sub_speciality_id) are `insertable=false` /
  `updatable=false` read-only mappings.
- Named-query INNER-JOIN null-FK drops: PASS — all five named queries use
  LEFT JOIN for service/speciality/sub-speciality; a row with a null FK is
  kept, not dropped. (Dev table empty — join behavior not exercised with
  rows, but the join TYPE is definitively LEFT.)
- Dup-check operator precedence: PASS on the SAVE/UPDATE dup-checks
  (GET_NURSING_ORDER_MASTER_BY_NAME / _BY_NAME_NOT_ID are all-AND and
  org+unit scoped — NOT the Q29/Q32 class). FINDING on the SEARCH query
  GET_NURSING_ORDER_MASTER_LIST_SERARCH: `WHERE org=:org AND unit=:unit AND
  LOWER(desc) like :x OR LOWER(code) like :x` — operator precedence binds
  org+unit ONLY to the desc branch, so the code-search branch is
  UNCONDITIONAL: a search by code returns rows from ANY org (the Q29/BR-169
  mechanism on a READ query). Cross-org reachability: YES — the code branch
  ignores org/unit entirely; any authenticated caller searching by code
  leaks cross-org rows. NOT fixed (Rule 10); finding to be logged to main
  post-merge.
- Org-trust: PASS on reads (org+unit parameterized in every read query;
  live-verified with org=1/unit=1). FINDING on writes: the DAO sets
  organizationId/unitId from the DTO PAYLOAD (payload-carried org — the
  vaccinationMaster class, not bloodComponent's VerifiedIdentityService
  override). Not demonstrated exploitable (no write test — no-seed rule).
- Twin queries: PASS — each named query used once by its own path.
- Swallowed exceptions on write paths: FINDING — the service's save/update
  catch blocks log and return the GENERIC envelope (COMMON_ERROR_MESSAGE),
  and the controller also catches to the generic envelope (the campaign #1
  class shape); a real error's detail is lost to the caller. Logged, not
  fixed.

## Verification state — HONEST

- preflight C1-C6 PASS; `npm run build` exit 0; smoke standard PASS
  (FINAL-URL /nursingOrderMaster, moduleId 480 present, SERVE-BRANCH matches
  the branch under test); interaction probe PASS (grid renders, speciality
  select populated from the live feed, save fired HTTP 200, 0 page errors).
- Write verification: the interaction probe FIRED the real save endpoint
  (POST /api/global/unit/saveNursingOrderMaster) with a probe payload — HTTP
  200, and the created row (PROBE-NO1) was then visible in the list feed with
  its LEFT JOIN names populated; the probe row was deleted afterwards (dev
  table back to 0). Per Q28 the honest claim is: the save accepted the
  payload and the row is visible in the feed — "not erroring" — never
  "working" beyond the observed response.
- Endpoint verification: reads + feeds verified BY CALLING (above); the
  save/update/status endpoints are MAPPED; the save path's acceptance was
  demonstrated by the probe's live call (above), update/status remain
  UNVERIFIED with real data (no dedicated write test — Q28).

## Deliberate divergences (flagged, not silent)

1. The legacy's service typeahead (uib-typeahead) is a select populated by
   the same live feed; the typeahead's 2-char minimum is mirrored by a fixed
   'a' probe search on focus.
2. The legacy's checkServiceMappedWithTest gate ("Service is Not mapped with
   Test!!") is NOT wired in the rebuild — that check is a Billing-module
   cross-domain call whose live contract is not verified; the rebuild
   reports the mapping state from the grid's join names instead.
3. Pagination (legacy 10/25/50/100) is client-side search + full list for
   now (the count endpoint is live; server pagination is a Phase-2/3
   consideration).

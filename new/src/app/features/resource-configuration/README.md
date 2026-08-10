# resource-configuration — parity rebuild of the legacy Resource Configuration screen

Legacy source:
- template: `views/master/TheatreManagement/resource-configuration.html`
- controller: `resourceConfigurationMasterController.js` (840 lines — a REAL
  full-CRUD controller, loaded via index.html:1733)
- menu node: 517 'Resource Configuration', parent 514 OTIMS -> 369 Global
  Master, actionLink `#/resourceConfiguration`

## CLASS: LIVE (per the amended reachability rule)

The state `resourceConfiguration` IS registered in app.js, the menu node
exists, the controller IS loaded, and a REAL backend exists (Global
SurgeryResourceConfigController, /api/otims/*, table
otims.t_surgery_resource_config). First screen of the OTIMS subtree walk
(after the 476 Clinical Workstation subtree was exhausted). Note: 516
Subsequent Surgery Configuration was walked past under the FIFTH bucket
NOT-A-SCREEN-YET (state registered + controller loaded but ZERO backend —
no java, no table).

## FINDING (found by calling, not by grep) — phantom column on the LIST path: the screen's primary read 500s

GET_SURGERY_RESOURCE_CONFIG_LIST (the named query behind POST
/api/otims/getSurgeryResourceConfigList) references `surgryResConfig.visitTypeId`
in its SELECT, but the entity SurgeryResourceConfig has NO visitTypeId @Column
mapping and otims.t_surgery_resource_config has NO visit_type_id column.
Hibernate resolves the HQL property to the phantom column, so the query throws
SQLGrammarException ("column surgeryres0_.visit_type_id does not exist") on
EVERY call. The DAO catch (SurgeryResourceConfigDaoImpl:149-152) swallows it
into the generic envelope — HTTP 200, body {status:'error', code:500,
message:'Something went wrong!!'} — so the HTTP layer reports success while
the payload is an error. The walk's THIRD phantom finding (after
t_translated_frequency on frequency byId and emr.m_allergy_master's cims
columns), the FIRST on a main-list path, and the FIRST where the swallow
masks it as HTTP 200.

WHAT THE USER SEES (merge question, verified in the probe): the rebuild
CALLS the list, detects the HTTP-200-error envelope, and surfaces the defect
honestly — the toast names it: "Resource configuration list load failed: the
server reported \"Something went wrong!!\". Backend defect: the list query
references a phantom column (otims.t_surgery_resource_config.visit_type_id
does not exist — see README)." NOT silent, NOT a crash, NOT generic.

NOT FIXED (Rule 10) — a backend defect (phantom column in the entity's HQL;
the fix is either adding the column or dropping visitTypeId from the query
and its LEFT JOINs).

## Backend — verified BY CALLING (cached token, 2026-08-10)

- list:   POST /api/otims/getSurgeryResourceConfigList -> 200 env listObject
  len=0 (dev-empty). LEGACY PAYLOAD SHAPE: {orgId, unitId, offset,
  noOfRecordsPerPage} — an initial probe with {organizationId, unitId}
  returned HIS-400 "Bad request: Invalid parameters" (the DTO binds orgId,
  not organizationId; the probe-payload class, caught BEFORE code, not after).
- count:  POST /api/otims/getSurgeryResourceConfigListCount -> 200 env
  listObject len=0
- byId:   GET /api/otims/getSurgeryResourceConfigListById/{id} -> 200 env
  listObject len=0 (dev-empty)
- save/update/status: POST /api/otims/saveSurgeryResourceConfig,
  /updateSurgeryResourceConfig, /updateSurgeryResourceConfigStatus — MAPPED,
  NOT wired in the rebuild (see divergences)
- table:  otims.t_surgery_resource_config exists (23 columns)

## Scoped backend check (per the standing rule, every class)

- Phantom columns/tables: PASS — otims.t_surgery_resource_config DDL (23
  columns) matches the entity (@Table t_surgery_resource_config schema otims);
  the list query's serviceId/procedureName/tariffId/visitTypeId resolve via
  LEFT JOINs to related masters, not the base table; verified by the 200s.
- Named-query INNER-JOIN null-FK drops: PASS — GET_SURGERY_RESOURCE_CONFIG_LIST
  is all LEFT JOINs (speciality/visit-type/tariff/surgery-grade/doctor-grade/
  patient-category/payment-entitlement/bed-category) — no drop class.
- Dup-check operator precedence: NOT PRESENT — saveSurgeryResourceConfig has
  no existence check before insert (the absence class).
- Org-trust: FINDING — NO VerifiedIdentityService anywhere in the Global
  otims module; orgId/unitId come from the request DTO on READS (list/count)
  AND writes — the payload-carried org shape (vaccinationMaster class) on a
  READ path. NOT FIXED (Rule 10).
- Twin queries: PASS — list/count are distinct purposes, each used once.
- Swallowed exceptions on write paths: FINDING — the DAO AND the controller
  catch and return the generic COMMON_ERROR_MESSAGE envelope (campaign #1
  shape — the global handler is bypassed; contrast screens 13/16 which
  rethrow ServiceException). NOT FIXED (Rule 10).

## Verification state — HONEST

- preflight C1-C6 PASS (LIVE class — C5 does NOT flag dead; state registered);
  `npm run build` exit 0; smoke standard PASS (FINAL-URL
  /resourceConfiguration, moduleId 517 present, SERVE-BRANCH matches the
  branch under test); interaction probe PASS (grid renders empty-state, the
  phantom-column toast names the backend defect, 0 page errors).
- READ VERIFICATION: the list endpoint was CALLED and FAILED (phantom column
  visit_type_id — HTTP 200 body error 500, swallowed by the DAO; root-caused
  in catalina.out). The rebuild surfaces this honestly. The list is NOT
  "working" — it is a broken backend feed, reported as such.
- Write verification: NO write test ran (no-seed; save/status write real
  rows). The save/update/status endpoints are MAPPED but NOT wired in the
  rebuild (read-only by design, see divergences) — they were not fired, so
  they are never claimed as "not erroring" either: their behavior is
  UNVERIFIED (Q28).

## Deliberate divergences (flagged, not silent)

1. READ-ONLY REBUILD. The legacy is an 840-line multi-step CRUD form
   (speciality/payment-entitlement/tariff/visit-type/bill-of-material feeds
   feeding the full SurgeryResourceConfigDto). A bare form sending a partial
   DTO would be a probe-payload-class failure (the screen-15 lesson); the
   rebuild ships the live list + search only, and save/update/status are NOT
   wired. Building the create form needs the feed endpoints verified first
   (a follow-up queue item, not silently dropped).
2. The status toggle is MAPPED but not wired (no row to toggle — dev-empty
   table; firing it blind would report an unverifiable result).
3. Pagination is client-side search + full list for now (the count query
   exists; server pagination is Phase-2/3).

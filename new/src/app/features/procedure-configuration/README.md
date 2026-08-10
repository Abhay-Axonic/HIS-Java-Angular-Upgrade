# procedure-configuration — parity rebuild of the legacy Procedure Configuration screen

Legacy source:
- template: `views/master/GlobalMaster/OTIMS/procedure-configuration-master.html`
  (+ views/procedureConfiguration/procedure-config.html)
- controller: `procedureConfigurationsController.js` (89 lines, loaded via
  index.html:1744)
- menu node: 515 'Procedure Configuration', parent 514 OTIMS -> 369 Global
  Master, actionLink `#/procedureConfiguration`

## THE DANGLE SHAPE (menu'd-unrouted — the drug-recall class, FOURTH instance)

Same class as 479/477/478: the node's actionLink points at
`#/procedureConfiguration`, but NO `.state('procedureConfiguration')` is
registered anywhere in app.js (grep = 0). The controller IS loaded
(index.html:1744), the template exists, and the backend is LIVE — so the
legacy menu navigates to a non-existent state (NG04002). Per the amended
reachability rule (RULES.md four-way classification + fifth bucket) a DANGLE
screen with a real backend is BUILDABLE: the rebuild RESTORES a
menu-advertised screen the legacy cannot open.

## Backend — verified BY CALLING (cached token, 2026-08-10)

- list:   GET /api/otims/getProcedureConfigList/{orgId}/{unitId} -> 200
  envelope (status/code/message/listObject/...) listObject len=0 (dev-empty)
- status: GET /api/procedure/config/status/{id}/{status} -> LIVE-VERIFIED
  200 "Updated Successfully." with CHAR status values ('A'/'I' — the
  endpoint's status param is a char, not a boolean). An initial probe with a
  boolean 405'd on binding; the char shape is the real contract and works.
- save:   POST /api/procedure/config (Global ProcedureConfigurationController
  :47) — MAPPED, NOT wired in the rebuild (see divergences)
- table:  otims.t_procedure_configuration exists (16 columns)

## Scoped backend check (per the standing rule, every class)

- Phantom columns/tables: PASS — otims.t_procedure_configuration DDL (16
  columns: procedure_config_id/cpt_code/cpt_description/duration/
  speciality_id/service_id/procedure_type_id/anesthesia_type_id/status/
  org_id/unit_id/created_by/created_date/updated_by/updated_date/
  operation_theatre_type_id) matches the entity + the list query's aliases;
  verified by the 200 executing against real DDL.
- Named-query INNER-JOIN null-FK drops: FINDING — GET_PROCEDURE_CONFIG_LIST_
  ON_TEMPLATE INNER JOINs m_procedure_type_master, m_anesthesia_type_master,
  speciality and service: a configuration row whose procedure-type /
  anesthesia-type / speciality / service join-partner is missing (or
  inactive in a join-condition sense) is DROPPED from the list. The walk's
  SECOND INNER-JOIN on a list path (after formulationTypeROAMaster). Dev
  table empty (listObject len 0) — not exercised with rows; the join TYPE is
  source-definitive.
- Dup-check operator precedence: NOT PRESENT — the save path
  (saveProcedureConfiguration) has no existence check before insert (the
  absence class).
- Org-trust: PASS on reads — the list query takes orgId/unitId as path vars
  and binds them in the named query; the status path takes id/status only
  (the update itself is id-scoped in the service). The save DTO carries
  org/unit in the payload (payload-carried class — NOT verified to the
  VerifiedIdentityService pattern; the save is not wired in the rebuild, so
  this is a note, not a live finding).
- Twin queries: PASS — each query used once by its own path.
- Swallowed exceptions on write paths: PASS — the OTIMS controller catch
  blocks rethrow ServiceException (global handler); the Global procedure
  controller's type/anesthesia list catches return the ERROR envelope with
  the message (a log + generic envelope — noted, not the write path).

## Verification state — HONEST

- preflight C1-C6 PASS (C5 flags DEAD-legacy — the dangle, surfaced not
  blocked); `npm run build` exit 0; smoke standard PASS (FINAL-URL
  /procedureConfiguration, moduleId 515 present, SERVE-BRANCH matches the
  branch under test); interaction probe PASS (grid renders, status toggle
  wired, 0 page errors).
- Write verification: NO write test ran (no-seed; the status toggle writes
  real rows' status). The toggle is "not erroring" at most — endpoint wired,
  response reported, never "working". (The list is read-only live-verified;
  the status endpoint's parameter shape is UNVERIFIED against a real
  toggle — Q28.)
- Endpoint verification: list live-verified (200, dev-empty envelope); save
  and status MAPPED, not live-fired.

## Deliberate divergences (flagged, not silent)

1. NO create/edit form. The legacy screen is a multi-step template that
   collects procedure-type / anesthesia-type / speciality / service /
   theatre feeds into the full ProcedureConfigurationDto for
   POST /api/procedure/config. A bare form sending a partial DTO would be a
   probe-payload-class failure (the screen-15 lesson); the rebuild shows
   the list + status toggle only, and the save path is NOT wired. Building
   the full create form needs the feed endpoints verified first (a
   follow-up, not silently dropped).
2. The status toggle fires the LIVE-VERIFIED endpoint (GET .../status/{id}/
   {char}) and reports the observed response; the legacy's own call shape
   (boolean status) does not bind — the char contract is used instead
   (surfaced, not hidden).
3. Pagination is client-side search + full list for now (Phase-2/3).

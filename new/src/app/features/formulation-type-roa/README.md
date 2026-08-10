# formulation-type-roa — parity rebuild of the legacy Formulation Type & ROA Master screen

Legacy source:
- template: `views/master/GlobalMaster/AssetManagenent/Master/formulation-type-ROA-master.html` (255 lines)
- controller: `formulationTypeROAMasterController.js` (376 lines — a REAL dedicated
  controller, loaded via index.html:1007)
- menu node: 479 'Formulation Type & ROA Master', parent 476 Clinical Workstation
  -> 369 Global Master, actionLink `#/formulationTypeROAMaster`

## THE DANGLE SHAPE (new defect class — the reverse of the decoy)

The node's actionLink points at `#/formulationTypeROAMaster`, but NO
`.state('formulationTypeROAMaster')` is registered anywhere in app.js (grep
over all 1183 `.state(` registrations: zero hits). The controller script IS
loaded, the sidebar (side-bar.html:490) and dashboard quick-links
(dashboard-quick-links.html:13696) both link to the route — so in the legacy
app the menu navigates to a non-existent state (NG04002). The real screen and
backend exist but are UNREACHABLE from the legacy app's own menu. Contrast the
decoy shape (screens 10/11: menu reached a shell while the real screen was
orphaned) — here the menu points at a state that does not exist.

The rebuild REGISTERS the route (moduleId 479) and wires the real backend:
- list:  GET /api/globals/formulationTypeAndROA/{orgId}
- dropdown (ROA options): GET /api/globals/formulationTypeAndROA/dropdown/{orgId}
- formulation-type feed: GET /api/globals/formulationTypes/{orgId}
- save:   POST /api/globals/formulationTypeAndROA/save
- update: PATCH /api/globals/formulationTypeAndROA/{id}
- byId:   GET /api/globals/formulationTypeAndROA/{orgId}/{id}

## Endpoints verified BY CALLING (cached token, 2026-08-10)

  GET /api/globals/formulationTypeAndROA/1          -> 200 raw list len=0 (dev-empty)
  GET /api/globals/formulationTypeAndROA/dropdown/1 -> 200 raw list len=0 (m_route_of_administration dev-empty)
  GET /api/globals/formulationTypeAndROA/1/1        -> 200 envelope: id/formulationType/routeOfAdministration/status/formulationTypeID/routeOfAdministrationID
  GET /api/globals/formulationTypes/1               -> 200 listObject len=1 (REAL row: id 1 "Test Formulation", code tst01)
  GET /api/globals/items/formulation/type/1         -> 200 listObject len=0 (dev-empty; the legacy controller's second feed)

## Scoped backend check (per the standing rule, every class)

- Phantom columns/tables: PASS — `public.m_formulation_and_roa` DDL (11 columns:
  id, organization_id, unit_id, formulation_type_id, roa_id, is_active,
  updated_by/date, added_by/date, is_deleted) matches the entity
  (FormulationAndRoa: id + formulationType FK + routeOfAdministration FK +
  BaseEntity org/unit/status/dates); verified by the 200s executing against
  real DDL.
- Named-query INNER-JOIN null-FK drops: FINDING — getAll() INNER JOINs
  `routeOfAdministration roa WITH roa.isActive=true AND roa.isDeleted=false
  AND roa.organization.organizationId=:orgId`: a mapping row whose ROA is
  inactive, deleted, or belongs to a different org is DROPPED from the list
  (the walk's FIRST INNER-JOIN on a list path). The WITH conditions are
  deliberate (only active ROAs) but the drop is real: such mappings are
  invisible in the grid. Dev table empty (count 0) — not exercised with rows,
  but the join TYPE and WITH conditions are source-definitive.
- Dup-check operator precedence: NOT PRESENT — saveFormulationTypeAndROA has
  NO existence check before insert (loops over the ROA id array and saves
  each). This is the absence class (duplicate mappings possible), not the
  Q29/Q32 shape — there is no dup-check query to be unscoped.
- Org-trust: PASS — the org-safe pattern END TO END: getAll, getById, save,
  update, and the ROA dropdown all use VerifiedIdentityService
  (getVerifiedOrganizationId / getVerifiedUnitId), with the service comments
  documenting the prior cross-tenant IDOR fixed in batch 35 (BR-pending). A
  record of another org resolves to null exactly like a nonexistent id.
- Twin queries: PASS — getFormulationROACountFromFormulationType vs
  getFormulationROACountFromROA are near-identical count shapes, each used
  once by its own check.
- Swallowed exceptions on write paths: PASS — the controller's save/update
  catch blocks log and THROW ServiceException (routed to the global
  exception handler); NOT the generic COMMON_ERROR_MESSAGE envelope
  (contrast QF-2026-08-10-E on screen 12).

## Verification state — HONEST

- preflight C1-C6 PASS; `npm run build` exit 0; smoke standard PASS
  (FINAL-URL /formulationTypeROAMaster, moduleId 479 present, SERVE-BRANCH
  matches the branch under test); interaction probe PASS (formulation-type
  select live-populated from the real feed, ROA checkboxes render, save
  endpoint wired, 0 page errors).
- Write verification: NO write test ran (no-seed rule; the save writes real
  mapping rows). Save is "not erroring" at most — the endpoint is wired and
  its response is reported; never "working". (The save path is org-safe per
  the service source; its success with real data is UNVERIFIED — Q28.)
- Endpoint verification: reads + feeds verified BY CALLING (above); the
  save/update endpoints are MAPPED but not live-fired (no write test).

## Deliberate divergences (flagged, not silent)

1. The legacy's ROA multi-select happens inside a modal with a dynamic
   add-row grid; the rebuild uses inline checkboxes against the same ROA
   dropdown feed — same payload shape (routeOfAdministrationId[]), simpler
   interaction.
2. The legacy status toggle calls the update path (PATCH) per row; the
   rebuild shows status read-only (update/status writes are no-seed — the
   endpoint is mapped, not fired).
3. Pagination (legacy 10/25/50/100) is client-side search + full list for
   now (the count query exists; server pagination is Phase-2/3).

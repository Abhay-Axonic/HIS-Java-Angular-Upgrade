# Operation Theatre Type — screen 20 (module 521, OTIMS)

Parity rebuild of the legacy
`views/master/GlobalMaster/OTIMS/operation-theatre-type.html` (menu node 521
'Operation Theatre Type', parent 514 OTIMS -> 369 Global Master,
actionLink #/operationTheatreType).

## Reachability class: LIVE

- State `operationTheatreType` IS registered (app.js:12413) with a dedicated
  controller loaded in index.html:1730.
- A REAL full-CRUD backend exists: Global `OperationTheatreTypeController`
  (`/api/otims/*`: save, update, status, byId, list, activeList), service +
  DAO, entity `OperationTheatreType` -> `otims.m_operation_theatre_type`.
- The route in the rebuild was PRE-REGISTERED by an earlier batch
  (Phase 4 batch 22, commit 32296f6f) pointing at this component path with
  moduleId 521 — the stub component followed the OLD pre-walk conventions
  (OperationTheatreTypeForm, emptyForm, StatusToggle, LoaderService) and was
  never endpoint-verified in this walk. This rebuild replaces the stub with
  the house pattern (signals + FormsModule, honest-error toasts) and the
  live-verified endpoint contracts.

## Live verification (BY CALLING, body inspected — the ENDPOINT-VERIFICATION
STANDARD, RULES.md 5c2432fd)

- POST /api/otims/getOperationTheatreTypeList {orgId:1, unitId:1} -> HTTP 200
  body status 'success', listObject len=2 (REAL rows: id 12 'general' OT1,
  id 11 'test theatre' OT2, both status A)
- POST /api/otims/getActiveOperationTheatreTypeList -> 200 'success' len=2
- GET  /api/otims/getOperationTheatreTypeById/11/1/1 -> 200 'success' len=1
- save/update/status endpoints exist MAPPED; save is wired with the FULL DTO
  contract (both dates) because of the false-success finding below.

## Scoped backend check (per the standing rule)

- Phantom columns: PASS — all five named queries select only entity columns;
  single-table (no joins); live 200s prove resolution. DDL
  (otims.m_operation_theatre_type) matches the entity.
- INNER-JOIN drops: PASS — no joins in any query.
- Dup-check precedence + scoping: PASS — GET_OPERATION_THEATRE_TYPE_BY_NAME
  (save) and _BY_NAME_NOT_ID (update) are BOTH org+unit bound with the
  (desc OR name) branch parenthesized inside the AND chain. NOT the
  OR-precedence class; NOT cross-org reachable; scoped — unlike 519's
  BY_NAME_NOT_ID.
- Org-trust: FINDING (module-wide, already recorded with screen 17) —
  orgId/unitId are payload-carried, no VerifiedIdentityService; write paths
  payload-trusting. NOT FIXED (Rule 10).
- Swallowed exceptions: FINDING — the FALSE-SUCCESS shape is present on the
  SAVE path: OperationTheatreTypeServiceImpl.saveOperationTheatreType's
  SUCCESS (:56) sits OUTSIDE the if(res.getObject()!=null) guard, and the DAO
  (OperationTheatreTypeDaoImpl:59-64) catch-and-returns the ERROR envelope
  without rethrowing — the IDENTICAL BR-187 mechanism. With an INCOMPLETE DTO
  (updatedDate omitted) the DAO NPEs at :52 (getDate(null)) and the service
  returns a false SUCCESS while writing NOTHING. This is the walk's SIXTH
  confirmed false-success instance (after collectPayment, settleCompanyInvoice
  [retracted], saveMedicationHistory, saveEncounterAssignment, operation-
  theatre save). The BR-187 fix shape applies (service SUCCESS inside the
  guard + DAO rethrow); the same incomplete-DTO attack path is data-driven
  and demonstrable. The rebuild mitigates client-side (the form always sends
  both dates and rejects incomplete submissions); the backend fix is a BR
  candidate.

## Divergences from legacy

- Save/Update/Status are wired (legacy has them); the rebuild sends the FULL
  DTO contract (both dates) — the legacy form's partial payload would trip
  the false-success class.
- Status toggle is a button in the row (legacy: checkbox in the grid), same
  endpoint (/updateOperationTheatreTypeStatus).
- Client-side completeness guard on Save/Update with a plain-language toast
  naming the backend false-success reason.

## Build state

- Route: pre-registered (moduleId 521) — satisfied by this component.
- ng build: exit 0. Smoke: route serves. Probe: live rows render, 0 page errors.

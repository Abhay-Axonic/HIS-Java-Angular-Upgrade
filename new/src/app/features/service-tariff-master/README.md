# Service Tariff Master — screen 30 (module 610, Billing)

Parity rebuild of the legacy
`views/master/UnitMaster/billingMaster/service-tariff-master.html` (menu
node 610 'Service Tariff Master', parent 570 Billing -> 531 Unit Master,
controller serviceTariffMasterController loaded index.html:872).

## CLASSIFICATION — LIVE (C7 + C8 at selection)

- State: REGISTERED app.js:8257, url /serviceTariffMaster, controller
  serviceTariffMasterController (NOT MasterController — passes the DECOY
  test).
- Menu node: 610 'Service Tariff Master', chain 610 -> 570 -> 531 (C5 PASS).
- BACKEND BY ENDPOINT TRUTH (the 585 lesson): the Java controller is
  UnitServiceTariffMasterController (billing/unit, @RequestMapping api/unit)
  — a DIFFERENT class name than the state root (the class-name grep missed
  it). Endpoints: getUnitServiceTariffMasterList (the legacy's PRIMARY grid
  call, JS:999), getUnitServiceTariffMasterListForWeb (the WORKING variant),
  saveUnitServiceTariffMaster, service/tariff/uniqueCheck,
  getBasePriceOfServicesFromTariffMasterByServiceList/Id.

## FINDING (2026-08-10) — the legacy primary grid endpoint is broken

- GET /api/unit/getUnitServiceTariffMasterList with the EXACT legacy payload
  (all filter lists []) -> HTTP 200 body {"status":"error","code":null,
  "message":null} on every default load — a masked 200 with a NULL message.
- MECHANISM (server stack 8027980, "syntax error at or near )"): the DAO's
  dynamic query appends `AND ustm.tariffId IN (:tariffMasterIdList)` and
  setParameterList binds the EMPTY list -> generated SQL `IN ()` -> syntax
  error. The DAO rethrows ServiceException (clean), but the CONTROLLER's
  catch is EMPTY (UnitServiceTariffMasterController:51-52
  `catch(Exception e){ }`) — the exception is swallowed and the null-message
  error envelope returned with HTTP 200.
- THE FIX ALREADY EXISTS: the ForWeb DAO (getUnitServiceTariffMasterListForWeb)
  uses the no-op placeholder `IN (1)` (always-true, no binding) — verified
  LIVE: POST /api/unit/getUnitServiceTariffMasterListForWeb (exact payload)
  -> 200 SUCCESS listObject [] (dev-empty). The legacy frontend calls the
  BROKEN endpoint; its grid cannot load on a default (empty-filter) state.
- CLASS: the walk's masked-error class (screen-17 shape) with a NEW
  mechanism — `IN ()` from setParameterList on an empty list + an EMPTY
  controller catch (the swallow at the controller, not the DAO).
- NOT FIXED (Rule 10) — BR candidate; the fix pattern is the ForWeb DAO's
  placeholder.

## SCOPED BACKEND CHECK

- The save path (saveUnitServiceTariffMaster) was not live-probed (the
  primary list being broken blocks the screen's write flow); its controller
  follows the same empty-catch pattern (risk flagged for the BR).
- INNER-JOIN drop: the query INNER JOINs 8 tables (service/speciality/sub-
  speciality/tariff/visit-type/patient-category/payment-entitlement/bed-
  category) — a row with any missing partner is dropped (by construction;
  the table is dev-empty so not demonstrated).

## Build state

- Route: NEW at /serviceTariffMaster (moduleId 610).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C8 PASS — moduleId 610
  only route; C5 PASS with the node chain). ng build exit 0. Smoke: route
  renders, the defect banner names the IN () + empty-catch mechanism, the
  working ForWeb feed renders its honest dev-empty state, 0 page errors.

# Unit Service Base Price — screen 28 (module 585, Billing)

Parity rebuild of the legacy
`views/master/UnitMaster/billingMaster/unit-service-base-price.html` (menu
node 585 'Unit Service Base Price', parent 570 Billing -> 531 Unit Master,
controller unitServiceBasePriceController loaded index.html:878).

## CLASSIFICATION — LIVE (C7 + C8 at selection; the owner's 585/592 call)

- State: REGISTERED app.js:9552, url /unitServiceBasePrice, controller
  unitServiceBasePriceController (NOT MasterController — passes the DECOY
  test).
- Menu node: 585 'Unit Service Base Price', chain 585 -> 570 -> 531
  (C5 PASS).
- BACKEND BY ENDPOINT TRUTH (the 585 lesson): the controller JS calls
  /api/unit/billing/getServiceBasePriceList + countServiceBasePriceMaster;
  the Java controller is ServiceBasePriceMasterController (billing/unit,
  @RequestMapping api/unit/billing) — a DIFFERENT class name than the state
  root, which is why the sweep's class-name grep missed it. Endpoints: list,
  byId, update, status, count, search.
- LIVE-VERIFIED BY CALLING (body status): POST getServiceBasePriceList
  {orgnisationId:1, unitId:1, offset:0, noOfRecordsPerPage:20} -> 200
  success with REAL rows (serviceBasePriceId 1, serviceMasterId 3,
  basePrice 250.0); POST countServiceBasePriceMaster {orgnisationId:1} ->
  200 success object: 2.

## SCOPED BACKEND CHECK (billing write-path focus)

- FALSE-SUCCESS SHAPE: PASS — ServiceBasePriceMasterDaoImpl's update path
  catches BOTH RETHROW ServiceException (the global-handler pattern;
  SUCCESS only after the real update). The BR-188 collectPayment class is
  NOT present.
- INNER-JOIN drop: PRESENT BY CONSTRUCTION but NOT OBSERVED — the list
  INNER JOINs serviceMaster -> specialityMaster -> subSpecialityMaster; a
  base-price row with a missing join partner would be silently dropped.
  Live-verified the join partners EXIST for the dev rows (row 1 -> service
  'Cardiology Consultation' rendered with the joined name); the drop risk is
  noted, not demonstrated.
- FIELD-NAME MISMATCH (agent defect, fixed 2026-08-10): the rebuild initially
  bound row.serviceStandardName, but the query aliases sm.serviceStandardName
  AS serviceName (DTO field serviceName, ServiceBasePriceMasterDto.java:33) —
  the Service column rendered EMPTY while the data was present. Fixed to
  row.serviceName. The To column's emptiness is DEV-DATA NULL (row 1's
  to_date is NULL in psql; the query selects it via to_char) — the standard
  dev-data treatment, not a defect.
- DDL: CLEAN — service.m_service_base_price_master has 14 columns incl.
  visit_type_id (EXISTS — the screen-17 contrast; no phantom here).
- PAYLOAD CONTRACT (named divergence): the legacy list/save payload key is
  `orgnisationId` (the misspelling, legacy JS:104-105) — NOT
  organizationId/orgId; the latter 400s HIS-400 (the probe-payload class).
- Dup-check scoping on the update path: not live-verified (the update is the
  primary write; flagged for the BR's scoped check).

## Build state

- Route: NEW at /unitServiceBasePrice (moduleId 585).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C8 PASS — moduleId 585
  used only by this route; C5 PASS with the node chain). ng build exit 0.
  Smoke: route renders, the live list populates (2 real dev rows with join
  names resolved), search filters, 0 page errors.

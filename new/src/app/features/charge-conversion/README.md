# Charge Conversion — screen 29 (module 592, Billing)

Parity rebuild of the legacy
`views/master/UnitMaster/billingMaster/charge-conversion.html` (menu node 592
'Charge Conversion', parent 570 Billing -> 531 Unit Master, controller
chargeConversionController loaded index.html:853).

## CLASSIFICATION — LIVE but TRANSACTION-shaped (C7 + C8 at selection)

- State: REGISTERED app.js:9165, url /chargeConversion, controller
  chargeConversionController (NOT MasterController — passes the DECOY test).
- Menu node: 592 'Charge Conversion', chain 592 -> 570 -> 531 (C5 PASS).
- Backend: ChargeConversionController (billing/unit,
  @RequestMapping unit/ChargeConversion) with /saveChargeConversion. Verified
  LIVE by calling: POST /unit/ChargeConversion/saveChargeConversion
  {orgId, unitId} -> HTTP 200 body {"status":"error","code":"500","message":
  "Server error"} — the endpoint exists and rejects an empty payload
  honestly (the endpoint-truth confirmation).
- NOT a master: a PATIENT-DRIVEN conversion flow. Every read feed is
  context-gated: /adt/patientSearchByMultipleCriteria (patient search),
  /api/global/patient/billing/{id} (patient billing), /unit/CompanyContract/
  getVisitTypeList (visit types — the ONE feed that works standalone),
  /unit/CompanyContract/getChargeConversionListByCurrentPayer (payer-gated).
  There is NO standalone list; the flow needs a patient + encounter.

## SCOPED BACKEND CHECK (billing write-path focus — the priority the owner set)

- FALSE-SUCCESS shape on the primary write: PASS — ChargeConversionController
  :39-44 catch RETHROWS ServiceException; the service
  (saveChargeConversionMasterList) catch RETHROWS ApplicationException
  (:95-97); DAO saveChargeConversionDetailsList (ChargeConversionDetails-
  DaoImpl:45-52) returns SUCCESS only after the real save.
- SILENT PARTIAL-FAILURE FINDING (2026-08-10, the walk's masked-shape class
  ON A BILLING WRITE PATH — the exact family the owner's warning named):
  ChargeConversionServiceImpl.saveChargeConversionMasterList (lines 74-83):
  when getOrderDetailsListByOrderId returns ERROR (its own catch-and-return
  at ChargeConversionDetailsDaoImpl:126-129 — the screen-17 masked-error
  shape), the `if(SUCCESS && listObject != null)` block silently SKIPS the
  details save AND the order-rate update, yet the service returns
  SUCCESS + "Charge conversion saved" (line 91). A failed inner step
  reported as full success. NOT FIXED (Rule 10) — backend BR candidate.
- The read path's catch-and-return (DAO:126-129): the honest-error-masked-
  as-200 shape (screen-17 class) — the mechanism that FEEDS the
  partial-failure above (the service checks the envelope's code, sees
  non-SUCCESS, skips).

## THE REBUILD

The honest transaction surface: the flow's structure, the one standalone
feed (visit types), and the partial-failure finding surfaced on an amber
banner naming the mechanism (the screen-17/24/25 pattern). The full
conversion write (patient search -> encounter -> payer -> save) is
context-gated and cannot be honestly exercised without a patient; that is
documented, not faked.

## Build state

- Route: NEW at /chargeConversion (moduleId 592).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C8 PASS — moduleId 592
  only route; C5 PASS with the node chain). ng build exit 0. Smoke: route
  renders, the finding banner names the partial-failure mechanism, the
  visit-type feed renders (or its honest empty state), 0 page errors.

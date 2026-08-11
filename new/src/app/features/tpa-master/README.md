# TPA Master — screen 38 (module 572, Billing)

Parity rebuild of the legacy
`views/master/UnitMaster/billingMaster/TPA-master.html` (menu node 572
'TPA Master', parent 570 Billing -> 531 Unit Master, controller
TPAMasterController).

## CLASSIFICATION — LIVE (C7 + C8 at selection; the recheck's first
## confirmed LIVE — the endpoint truth WITH path params proved it; the
## recheck's earlier 404 was its own path construction, not a missing
## backend)

- State: REGISTERED (C5 PASS — node chain 572 -> 570 -> 531). Controller
  binds TPAMasterController (NOT MasterController).
- The screen is a TPA registration FORM surface: registration-field config
  + company list + country/state/district + AR-code dropdowns.
- LIVE-VERIFIED BY CALLING (body status):
  - GET /api/unit/applicationConfiguration/
    getUnitApplicationRegistrationFieldConfig/1/1 -> 200 success []
    (dev-empty);
  - POST /api/global/getCompanyMasterList {organizationId, unitId,
    companyTypeId: 5} -> 200 success [] (dev-empty);
  - GET /global/billing/getActiveARCodeMaster/1/1 -> 200 success with REAL
    rows (TESTAR2 / Test AR Description Two);
  - GET /adt/getActiveCountryList/1 -> 200 success with REAL rows (India/IN
    — 'List Size DB: 2').

## PAYLOAD LESSON (shared DTO, the screen-37 class)

The legacy sends {organizationId, unitId} — the orgId key 500s at Jackson
deserialization BEFORE the controller's try (the global-handler loud
variant, verified in screen 37's catalina stack; CompanyMasterDto's 57
known properties reject orgId).

## SCOPED BACKEND CHECK

The screen is a FORM surface — its controller's read path has no master
save; the registration-field config is the backend-driven form-shape feed.
No write-path false-success shape on the read surface.

## Build state

- Route: NEW at /TPAmaster (moduleId 572; the legacy-verbatim state URL).
- Gates: preflight C1-C8 (C5 PASS; C7 PASS — genuinely new; C8 PASS —
  moduleId 572 only route). ng build exit 0. Smoke: route renders, the real
  AR-code + country rows visible, the honest dev-empty company list, 0 page
  errors.

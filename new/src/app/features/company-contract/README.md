# Company Contract — screen 37 (module 608, Billing)

Parity rebuild of the legacy
`views/master/UnitMaster/billingMaster/company-contract-master.html` (menu
node 608 'Company Contract', parent 570 Billing -> 531 Unit Master,
controller companyContractController — the DECOY test passes: a REAL
controller with 40 template binds, loaded index.html:1606).

## CLASSIFICATION — LIVE (C7 + C8 at selection; the recheck's second new
## LIVE — per-screen endpoint truth found it; the 585 lesson)

- State: REGISTERED (C5 PASS — node chain 608 -> 570 -> 531). Controller
  binds companyContractController (NOT MasterController).
- The screen configures company contracts per unit: company list ->
  associate-company list -> visit-type/grade/package-type feeds -> create
  contract (createCompanyContarct).
- LIVE-VERIFIED BY CALLING (body status):
  - company list POST /unit/CompanyContract/getCompanyMasterList
    {organizationId, unitId, companyTypeId: 3} -> 200 success [] (dev-empty);
  - visit-type feed {organizationId, unitId} -> 200 success with REAL rows
    (ABC test1 / Day Care / Emergency);
  - grade list {organizationId, unitId} -> 200 success [] (dev-empty);
  - package-type feed GET /Unit/Service/EHCpackage/getPackgeType/1/1 ->
    200 success with REAL package rows (All Inclusive Package / EHC);
  - associate-company feed {organizationId, unitId} -> LOUD 500 (honest
    error, message populated — a secondary dropdown; root-cause owed, not
    the primary grid).

## PAYLOAD LESSON (recorded — the probe-payload class, loud variant)

The legacy contract is {organizationId, unitId} — the orgId key is REJECTED
by Jackson at deserialization (UnrecognizedProperty on CompanyMasterDto, 57
known properties) and the exception fires BEFORE the controller's try, so
the GLOBAL handler 500s instead of the empty catch masking it. My first
probe's 500 was my own payload (verified in the catalina.out stack) — not a
backend defect.

## SCOPED BACKEND CHECK (billing write path)

createCompanyContarct — the controller catches and RETHROWS ServiceException
(clean, no swallow); the DAO catch returns the honest ERR_500 (message
populated). The SUCCESS-from-catch false-success class is ABSENT on this
write path.

## Build state

- Route: NEW at /companyContractMaster (moduleId 608).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C8 PASS — moduleId 608
  only route; C5 PASS). ng build exit 0. Smoke: route renders, the real
  visit-type + package-type rows visible, the honest dev-empty company
  list, 0 page errors.

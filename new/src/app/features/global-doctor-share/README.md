# Global Doctor Share — screen 32 (module 613, Billing)

Parity rebuild of the legacy
`views/master/UnitMaster/billingMaster/doctor-share-master.html` (menu node
613 'Global Doctor Share', parent 570 Billing -> 531 Unit Master, controller
globalDoctorShareMasterController loaded index.html:881).

## CLASSIFICATION — LIVE (C7 + C8 at selection; the owner's doctor-share
## trio — 613/614/615 — with the six-method false-success enumeration)

- State: REGISTERED app.js:14764, url /globalDoctorShare, controller
  globalDoctorShareMasterController (NOT MasterController — passes the DECOY
  test).
- Menu node: 613 'Global Doctor Share', chain 613 -> 570 -> 531 (C5 PASS).
- Backend: GlobalDoctorShareMasterController (billing/unit,
  @RequestMapping unit/DoctorShare) — found BY CLASS NAME this time:
  saveGlobalDoctorShare, duplicate/check/globalDoctorShare (the dup-check),
  saveIndividualDoctorShare, getGlobalDoctorShareDepartmentMasterList (the
  grid feed), getDoctorGradeMaster, DetailsByDepartmentId (+ Search).
- LIVE-VERIFIED BY CALLING (body status): grid feed -> 200 success
  listObject [] object: 0 (dev-empty); grade feed -> 200 success [].
  PAYLOAD CONTRACT (named divergence): the DTO's search field is
  `specialitySerarchName` (the typo, DoctorShareReqDto:21) — REQUIRED;
  omitting it 400s HIS-400 (the probe-payload class).

## SCOPED BACKEND CHECK (billing write-path — the priority)

- FALSE-SUCCESS shape on saveGlobalDoctorShare: PASS — the DAO's catch
  (TGlobalDoctorShareGroupWiseDaoImpl:105-107) returns the honest ERROR
  envelope (never SUCCESS-from-catch); SUCCESS (:102) only after the batch
  executed + committed; ConstraintViolation -> ALREADY_EXIST (a legit dup
  signal). The controller rethrows cleanly.
- FINDING — BATCH-PARTIAL-COMMIT SHAPE (2026-08-10, on the money path):
  the batch commits every 100 rows or on the last (line 94-96); a mid-batch
  failure after a commit leaves earlier rows persisted with NO rollback in
  the catch (the finally only closes the statement/connection). A
  partial-write risk on doctor-share rates. NOT FIXED (Rule 10) — BR
  candidate.
- The DAO's empty-list SUCCESS (:102) is unreachable from this endpoint
  (the service guards size > 0) — dead code, not a demonstrated
  false-success.
- Dup-check (checkUniqueGlobalGroup): composite-scoped (dept/bed-category/
  visit/patient/payment/tariff/effective-date + status 'Y').
- getDate chain (getStringDate -> getDate at service:79): the latent
  null-date class (only if effectiveDate is null; the legacy always sends
  it — not demonstrated).

## Build state

- Route: NEW at /globalDoctorShare (moduleId 613).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C8 PASS — moduleId 613
  only route; C5 PASS). ng build exit 0. Smoke: route renders, the
  batch-partial-commit finding surfaced on the amber banner, the honest
  dev-empty list + grades count, 0 page errors.

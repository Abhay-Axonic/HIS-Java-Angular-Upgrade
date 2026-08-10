# Doctorwise Share — screen 33 (module 614, Billing)

Parity rebuild of the legacy
`views/master/UnitMaster/billingMaster/doctorwise-share-master.html` (menu
node 614 'Doctorwise Share', parent 570 Billing -> 531 Unit Master,
controller doctorWiseShareMasterController loaded index.html:883).

## CLASSIFICATION — LIVE (C7 + C8 at selection; the SECOND of the owner's
## doctor-share trio 613/614/615)

- State: REGISTERED app.js:14814 `doctorWiseShare` (MIXED-CASE — C5's DEAD
  flag is the name-case mismatch, the screen-31 pattern; hand-confirmed the
  state exists). Controller binds doctorWiseShareMasterController (NOT
  MasterController — passes the DECOY test).
- Menu node: 614 'Doctorwise Share' -> 570 -> 531.
- BACKEND BY ENDPOINT TRUTH: grid feed = /unit/DoctorShare/
  getDoctorShareDepartmentMasterList (GlobalDoctorShareMasterController:159
  — the 613-family controller); the save family = TDoctorShareController
  (api/billing): saveDoctorShareList, saveDoctorRefeeralList (the owner's
  six-method enumeration).
- LIVE-VERIFIED BY CALLING (body status): grid feed -> 200 success
  listObject [] object: 0 (dev-empty). PAYLOAD CONTRACT: specialityId +
  doctorId (nulls fine); the earlier 401 was a payload-shape artifact (this
  DTO rejects the offset/recordPerPage keys the sibling DTO takes).

## FALSE-SUCCESS FINDING (2026-08-10 — the walk's most serious class, ON THE
## DOCTOR-SHARE MONEY PATH, the family the owner's warning named)

TDoctorShareDaoImpl.saveDoctorShareList (lines 680-726):
- Line 724: when the voucher save (iDocShareVoucherDao.saveDocShareVoucher)
  returns non-SUCCESS or a null object, the doctor-share rows were ALREADY
  updated (the executeUpdate loop at 684-701 ran) but the voucher was NOT
  saved — the response is STILL SUCCESS with an ERROR_MESSAGE string.
  HTTP 200 + body status success while the voucher write failed and the
  share amounts changed.
- Line 726: the empty-list path — SUCCESS "saved" with NOTHING written.
The legacy caller checks response.data.status == "success" — the UI shows
success for a failed voucher write. NOT FIXED (Rule 10) — BR candidate
(the BR-188 family; a NEW variant — SUCCESS-code-with-error-message).

## Build state

- Route: NEW at /doctorwiseShare (moduleId 614).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C8 PASS — moduleId 614
  only route; C5's DEAD explained — the mixed-case state name). ng build
  exit 0. Smoke: route renders, the false-success finding surfaced (red
  banner — the money-path severity), the honest dev-empty list, 0 page
  errors.

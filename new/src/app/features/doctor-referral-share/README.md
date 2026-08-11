# Doctor Referral Share — screen 34 (module 615, Billing)

Parity rebuild of the legacy
`views/master/UnitMaster/billingMaster/doctor-referrel-share-master.html`
(menu node 615 'Doctor Referral Share', parent 570 Billing -> 531 Unit
Master, controller doctorReferralShareMasterController loaded index.html:791).

## CLASSIFICATION — LIVE (C7 + C8 at selection; the THIRD of the owner's
## doctor-share trio 613/614/615)

- State: REGISTERED app.js:14858 `doctorReferralShareMaster` (C5's DEAD flag
  is the name mismatch — the node's link target; the screen-31 pattern).
  Controller binds doctorReferralShareMasterController (NOT
  MasterController — passes the DECOY test).
- Menu node: 615 'Doctor Referral Share' -> 570 -> 531.
- BACKEND BY ENDPOINT TRUTH: grid feed = /unit/DoctorShare/
  getIndividualDoctorReferralDetailsByDepartmentId
  (GlobalDoctorShareMasterController:229 — the 613-family controller); the
  save = saveReferralShare (:215, the owner's enumeration). The details
  feed (getDoctorReferralShareDetailsByRefDocId) is COMMENTED OUT in the
  legacy JS (the legacy navigates to detailsDoctorReferralShareMaster).
- LIVE-VERIFIED BY CALLING (body status): grid feed -> 200 success
  listObject [] object: 0 (dev-empty). PAYLOAD CONTRACT: referralId (not
  departmentId — the 400 probe-payload class).

## FALSE-SUCCESS FINDING (2026-08-10, ON THE REFERRAL-SHARE MONEY PATH)

IndividualDoctorShareServiceImpl.saveReferralShare (line 216): the two
list-guards (174, 196) are `if(list != null && size > 0)` — when BOTH lists
are empty/null, no group-wise or service-wise row is saved, no voucher is
created, and the method still returns SUCCESS + DOCTOR_SHARE ("saved").
The empty-save reports success — the screen-22 same-day shape, on the
referral-share money path. NOT FIXED (Rule 10) — BR candidate.

- The dup-checks (checkUniqueIndividualGroupForReferral /
  checkUniqueIndividualServiceForReferral) throw ServiceException (clean
  rethrow).
- The two DAO saves (saveIndividualDoctorShareGroupWiseForReferral,
  saveIndividualDocShareServiceWiseForReferral) follow the clean
  catch->honest-ERROR/ALREADY_EXIST pattern with the SAME latent
  batch-partial-commit shape as screen 32 (commits per batch, no rollback
  in the catch) — noted, BR candidate.

## Build state

- Route: NEW at /doctorReferralShare (moduleId 615).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C8 PASS — moduleId 615
  only route; C5's DEAD explained). ng build exit 0. Smoke: route renders,
  the false-success finding surfaced (red banner), the honest dev-empty
  list, 0 page errors.

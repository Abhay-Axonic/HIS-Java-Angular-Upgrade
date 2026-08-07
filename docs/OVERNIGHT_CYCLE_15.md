# OVERNIGHT CYCLE 15 — residual closure (the nine), decision filter (the 640), OTHER bucket (the 850), password-reset spec (2026-08-07)

Analysis and proposal only. Nothing fixed/built. Limits unchanged.

## 1. THE RESIDUAL ON THE NINE — CLOSED: the envelopes are INVISIBLE one layer up

Rule 10: least-confident site traced first (the OT-completion load handler), reading the
actual handlers and the shared services, not the method names.

TRACES (method + blind spots stated):
- GenericService.serviceAction (the 2-arg GET path, GenericService.js:4-51): inspects ONLY
  HTTP-level errors (non-2xx growl: 401 -> login, else growl.error). A backend 200+ERROR
  envelope is passed to the screen handler UNINSPECTED — no status check, no growl.
- otCompletionController.js:119-139 (OT completion load; rides getProcedureOrderById ->
  the doctor list source): the handler null-checks `response.data.listObject` — an ERROR
  envelope has listObject=null -> the tabs render at their initial (false/empty) state.
  The user sees an OT completion with no sections. NO error surface.
- masterDischargeSummaryController.js:8176 (procedure list for the discharge summary):
  `if (response.data.status == "success") $scope.surgeryAndProcedureList = ...` — status-
  gated assignment with NO else: on ERROR the list stays empty/stale, the forEach still
  runs, no growl. A clinician signing a discharge sees the procedures grid empty/stale.
- The save-path service (GenericService.js:308-357, notificationStatus variant) DOES
  inspect the envelope (success -> growl if flag true; error -> growl.warning/error) —
  BUT the caller can pass notificationStatus=false, and the traced OT-completion save
  (otCompletionController.js:221, NOTIFICATION_MSG_STATUS_FALSE) DOES. Disabled there.
- Sampling: two GET-path screens + both shared-service variants read; the remaining three
  screens' handlers (kit-flag save, promotion, cancel) are the same two handler classes
  (payload null-check / status-gated assignment) or the flag-disabled save path.

ANSWER: the nine ERROR-envelope sites do NOT surface on the traced screens. The backend's
"degraded observability" is INVISIBILITY at the UI — the mechanism is the shared GET
service's non-inspection plus handlers that render empty on null/stale payloads plus
disabled notification flags on the save path. The nine are nine more instances of the
class, one layer up — NOT fixed-by-convention. CONSEQUENCE: the item-2 fix shape must
include the frontend. HIGHEST-LEVERAGE candidate: the shared GET serviceAction inspects
response.data.status and growls on 'error' (one change surfaces envelopes across every
GenericService GET screen); the handlers' no-else branches then matter less. Cost/risk:
a shared-service change affects every screen that relies on silent-empty behavior —
needs the 68 console-only handler audit first (queued).

## 2. THE ~640 — DECISION FILTER APPLIED (sampling method stated)

METHOD: file-area filter — a site counts as decision-surface if its package path contains
a clinical/financial keyword (billing/procedure/doctor/otims/pharmacy/nursing/lab/opd/
encounter/admission/discharge/referral/appointment/contract/razor/investigation). NOT a
caller-chain-to-ground trace (the supervisor allowed sampling); 1 of the flagged sites is
caller-grounded from cycle 14 (ProcedureOrderDaoImpl:177 -> /api/procedure/order/status ->
the order-list screens); the rest are area-classified. Blind spot: a shared-layer file
(Global/com/param/global) feeding a decision screen is UNDER-counted (its package is
shared, not decision); an area file whose method feeds a non-decision screen is OVER-
counted. The order of magnitude is the deliverable.

RESULT: of 640 definite-invisible, 188 (29%) are in decision-surface areas. BY SCREEN
FAMILY: OPD 80, billing 35, doctor 21, procedure 11, otims 11, pharmacy 11, admission 8,
nursing 5, referral 3, lab 2, appointment 1.
WORST (decision-surface, EMPTY/NULL-RETURN — the invisible class in situ):
  - ProcedureOrderDaoImpl:177 getProcedureOrderListByStatus (cycle 14, grounded)
  - ProcedureOrderServiceImpl:4443 getBatchList
  - TIndividualDoctorShareGroupWiseDaoImpl:351 checkUniqueIndividualGroup +
    :431 checkUniqueIndividualGroupForReferral (DOCTOR-SHARE group uniqueness — financial)
  - DoctorSlabMasterController:31/43/55/67 (slab master — financial)
  - UnitServiceTariffMasterController:91 (tariff — financial)
  - JsonCheck:58 mapJsonToObject (shared JSON mapper)
  - BillingMasterDaoImpl:1057 getNextValGetBillId (bill-ID generation) +
    :4232 getTallyInterfaceDataInJsonFormat (Tally interface — financial export)
ORDER OF MAGNITUDE: ~190 decision-surface definite-invisible (of 640; of 8,077 total
swallow-shape catches ~2%). The fix population is the tens-to-low-hundreds, NOT a campaign.

## 3. THE ~850 OTHER — what makes them unknown + a cheap sampled resolution

WHAT MAKES THEM UNKNOWN (stated): the return expression after the catch is a VARIABLE or
unmarked value (response, dto, date, jsonString, 0, "OK", a method call) — its
success/failure marking is not determinable from the return expression alone; it requires
reading the catch body and the method's contract.
CHEAP RESOLUTION (method): read the catch body + the returned target for a sample; a
plain-value return (0, "OK", a normal object, a pre-built variable the catch does not
touch) is INVISIBLE-class (the return carries no failure marking); a return whose variable
the catch sets to an error is OBSERVABLE. SAMPLED RESULT: first 15 OTHER sites in file
order (stated sampling: order-based, not random): ~13/15 INVISIBLE-class — EHCpackageController
(return response, 6 sites — the response variable is not set to ERROR in the catch),
TPeriodicityServiceOrdersServiceImpl.placeOrder (return dto), DateConverter (return date/
longDate), JsonCheck (return jsonString), BillingMasterDaoImpl.updateFinalBill (return 0 —
indistinguishable from "0 rows"), BillingMasterController.myfileSynch (return "OK" — returns
success even on failure: FALSE SUCCESS), pharmacyReturnServiceConsumtion (return
updateService(...) — method-call return, unclassifiable without tracing); ~2/15 ambiguous
(return response — depends on whether the catch touches it).
EXTRAPOLATION (stated as extrapolation): if ~85% of the OTHER bucket is invisible-class,
the invisible population is ~640 + ~0.85x850 ≈ 1,350 (~17% of 8,077), and the decision-
surface subset ≈ 188 + ~0.85x253 ≈ 400. The 850 mostly resolve to invisible; a full pass
is a one-read-per-site job, not a dataflow problem.

## 4. PASSWORD-RESET BEHAVIOURAL TEST — CONCRETE SPEC (OTP seam exists; spec only)

WHERE IT WOULD LIVE: tools/golden-trace/test_password_reset_journey.js (alongside
mint_token.js / positive_control_login.js), run against localhost:8080 with the local DB.
FIXTURES IT NEEDS: (a) a seeded user in m_user_master (userId, userName, emailId, password
hash reset); (b) DB read access (psql) to m_user_master.otp; (c) a mail-inert profile —
ideally a config flag that skips sendOTPOverMail; if none exists, tolerate the external
send attempt (the DB write happens regardless — the seam is the DB, verified
UserMasterDaoImpl:337-349); (d) a fresh token (mint_token.js) for the login legs.
WHAT IT DRIVES (staged):
  1. POST /adt/forgotPasswordGeneration {userName} -> expect 200; SELECT otp FROM
     m_user_master WHERE user_id=... -> the OTP (4 chars, GlobalCommonDateUtils.generateOTP(4)).
  2. POST /adt/otpVerification with WRONG otp -> expect failure envelope; then the DB-read
     otp -> success. (Also asserts the OTP row's otp_count increments / blocked behavior on
     the limit path if reachable.)
  3. POST /adt/resetPassword {userId, newPassword} -> expect success; re-run otpVerification
     with the SAME otp -> expect FAILURE (single-use assertion).
  4. NEGATIVE (identity binding): for user B, POST /adt/resetPassword WITHOUT B's
     otpVerification -> expect FAILURE (the fix/resetpassword-identity-binding regression:
     the test must fail pre-fix).
  5. ENUMERATION: /adt/forgotPasswordGeneration with a NONEXISTENT userName -> assert the
     response does not disclose existence (or pins the intended disclosure).
  6. LOGIN: /adt/userLogin with the new password -> success; the old password -> failure.
WHAT IT ASSERTS: the reset only succeeds after that user's OTP was verified; OTP single-
use; wrong-OTP and unverified-identity resets fail; the new password logs in and the old
one does not; enumeration behavior pinned; the response envelopes assert status + message
(never the log — response text is never evidence; the DB row is the evidence for the OTP).
PRIORITY: near-term (the seam exists today; the identity-binding + enumeration legs are
high-value; the journey is the first DARK-LIST design that is fully locally testable).
NOT BUILT (spec only).

## 5. State / next

- main @ 65601866; 13 branches parked; machine down (no reboot). Cycle 15 commits next.
- DECISIONS_QUEUE: item 15 stays top; item 20 (this cycle) appended.
- Next run: the shared-service GET-inspection change proposal (the highest-leverage fix
  shape) for the owner; then the 68 console-only handler audit it depends on; then the
  decision-surface fix batch if approved.

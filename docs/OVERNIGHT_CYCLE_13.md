# OVERNIGHT CYCLE 13 — open-ended analysis items 1-3 COMPLETE (2026-08-07, extended-run continuation)

Analysis only — no code changed, no fixes, no merges/pushes. Limits unchanged.

## 1. STORE-COOKIE CENSUS RE-RUN (broader net) — RESULT: ZERO new application-code sites

METHOD (stated): quote-agnostic + whitespace-agnostic `sessionStorage.getItem(['"]selectedTopStore['"])`
scan over ALL .js/.html under resources/assets; classes INLINE-PARSE / ASSIGNED-PARSE(+INDIRECT-
DEREF | +CHECK-ONLY) / STRING-READ (getItem without JSON.parse). BLIND SPOTS: cross-file
indirection (a var assigned in file A deref'd in file B) is not provable statically — noted;
minified strings and wrapper reads are invisible.
RULE 10 VALIDATION (before trusting any count): (a) known positive — main's
addNewConsignmentInwardController.js (pb4 conversion unmerged; raw parse present) matched the
broad pattern; (b) known converted — the 27 files of merge c3359a37: 26 clean under the broad
pattern (sample addDepartmentConsumption.js); the 27th is tools/golden-trace/test_store_accessor_
live_fire.js which MUST contain the pattern (it tests it) — a tool, not application code.
RESULT: 63 broad sites on main's tree (all unmerged-branch files still carry their parses —
expected). The ORIGINAL census literal would have missed EXACTLY 2: app.js:2712 and app.js:2727 —
both STRING-READs inside the pattern-ANCHOR functions getStoreObj()/storeReady() (the campaign's
own implementation reads the raw string then parses with null-safety). DELIBERATE, not new
application code. CONCLUSION: the original census population was COMPLETE for application code;
the classifier had nothing hidden to classify.

## 2. DARK-LIST COVERAGE DESIGNS — docs/DARK_LIST_TEST_DESIGNS.md

Three journeys, endpoints verified against source (Rule 10): password reset (UserMasterController
/adt: forgotPasswordGeneration WHITELISTED, otpVerification, resetPassword, validateUserName,
checkEmailAvailability, generateSMSOtp, validateSMSOtp), feedback (whitelisted 6-segment GETs,
OutPatientFeedbackDaoImpl / IInPatientFeedbackDaoImpl), AI appointments (AppointmentSchedulingController
api/opd: saveAIAppointmentInQueue, getAIAppointmentCount, getAISummaryData — NOT whitelisted, JWT-
protected). Each design states DRIVE / ASSERT / NEED. Headline NEEDS (nothing built):
- Password reset: an OTP capture seam (the journey depends on external mail/SMS — a test
  profile echo or DB read; none exists as a seam today); a full-journey driver (positive_control_
  login.js only does /userLogin); the identity-binding regression (fix/resetpassword-identity-
  binding) has no local behavioural test.
- Feedback: the 6-segment scoping contract is undocumented (a cross-tenant IDOR surface the
  test would pin); verify_allowlist_reality.py's matrix lacks the feedback patterns.
- AI appointments: the queue bookkeeping has NO test; needs seeded doctor/slot + token; BOUNDARY
  stated — local tests cover bookkeeping only, NOT any external AI prediction call.

## 3. SWALLOWED-EXCEPTION SWEEP (comprehensive) — 8,077 swallow-shape catches

METHOD + blind spots + Rule-10 sample verification appended to docs/SWALLOWED_EXCEPTION_SWEEP.md
§5 (the earlier 451 = Billing+HIS-WAR only; GLOBAL was never counted — including it changes the
picture: the shared layer dominates).
- Shapes (comment-stripped): EMPTY 456 | PRINTSTACKTRACE 8 | LOG-ONLY 6,581 | LOG+RETURN 1,032.
- By module: Global 7,614 | Billing 385 | HIS-WAR 72 | Entity 6.
- TOP-10 worst (decision-surface, invisible failure): all EMPTY, all Billing procedure DAOs —
  OTCompletionCssdKitDaoImpl.updateIsUsedFlag (:93/94), OtCompletionStaffDaoImpl.getDoctorList
  (:99/101), ProcedureReqDaoImpl.updateProcedureRequestSeconderyToPrimary (:114/115) +
  cancelProcedureReq (:142/143), ProcedureOrderDaoImpl.getProcedureOrderListByStatus (:177) +
  getProcedureOrderList (:514). OT completion + procedure order surfaces — clinical/financial.
- Caveat: not all 1,032 LOG+RETURN are invisible — sampled Global controllers return explicit
  Response(ERROR,...) (observable); only success-looking returns are the false-success class.
- NOTHING FIXED (per directive).

## 4. State

- Branches (13, all local): pb1-pb4, ab1-ab3, pr1, n1, db1, m1, wd1, fdt — parked, awaiting owner.
- main @ da5a7b36 (docs) — this cycle's reports commit next.
- Machine: still down (no reboot — owner's call). DECISIONS_QUEUE 15 (stuck Tomcat + Step 6
  fire-test list) remains the top open item for the morning.

## 5. What I will do next

- Nothing further this run (queue + open-ended items exhausted). Next run: owner's morning
  decisions (machine reboot, Rule 9b for the 13 branches, Step 6 priorities per queue item 15,
  the drill-down batches the sweep suggests).

# OVERNIGHT CYCLE 14 — top-10 traces + fix-shape proposal + observability quantification + OTP seam (2026-08-07)

Analysis and proposal only — NOTHING built, fixed, or merged. Limits unchanged.

## 1. THE TOP TEN — end-to-end traces (with an important CORRECTION to the sweep)

Rule 10 followed: the least-confident site was traced first (ProcedureOrderDaoImpl
getProcedureOrderList, the core list), reading the ACTUAL code, not the method names.

CORRECTION (plainly): the sweep's §5 claim that these sites "show an empty/successful view"
was WRONG for 9 of the 10 catch lines. Traced reality — the EMPTY catch is followed, in the
SAME method, by an explicit `return new Response(ERROR, ...)` in 9 of the 10:
  - OTCompletionCssdKitDaoImpl:93/94 updateIsUsedFlag()  -> swallow, then Response(ERROR, COMMON_ERROR_CODE/MESSAGE)
  - OtCompletionStaffDaoImpl:99/101 getDoctorList()      -> swallow, then Response(ERROR, null, COMMON_ERROR_MESSAGE)
  - ProcedureReqDaoImpl:114/115 updateProcedureRequestSeconderyToPrimary() -> swallow, then Response(ERROR, COMMON_ERROR_CODE/MESSAGE)
  - ProcedureReqDaoImpl:142/143 cancelProcedureReq()     -> swallow, then Response(ERROR, COMMON_ERROR_CODE/MESSAGE)
  - ProcedureOrderDaoImpl:514 getProcedureOrderList()    -> swallow, then Response(ERROR, ERROR_CODE, SERVER_ERROR)
  These five FAIL LOUDLY at the envelope: the caller receives an ERROR-marked Response. The
  cost of the empty catch there is LOST EXCEPTION DETAIL (the generic COMMON_ERROR_MESSAGE
  replaces the actual cause) — an observability degradation, not invisibility.
  - ProcedureOrderDaoImpl:177 getProcedureOrderListByStatus() — THE ONE GENUINELY INVISIBLE
    SITE: `catch (HibernateException he) { }` (EMPTY) then the method FALLS THROUGH to
    `return null;` (line 182). The controller chain is passthrough (ProcedureOrderController
    :53-62 -> service :1321 -> DAO), so a HibernateException yields HTTP 200 with a null body.
    A blank list arrives at the UI. ANSWER TO THE QUESTION: NO — a user CANNOT tell "no
    procedure orders exist" from "the query failed". This is the class that hid the store
    crash and the Drug History gap, in situ. (Non-Hibernate exceptions in the same method are
    RETHROWN and caught by the controller's catch -> Response(ERROR, e.getMessage()) —
    observable; only the HibernateException path is invisible.)
  - Frontend residual (stated): whether the 9 ERROR-envelope sites render as visible errors
    depends on the screens' success handlers (the 68 console-only handlers are the mechanism
    by which an envelope still vanishes). The screens: OT completion (kit-used flag, doctor
    list), procedure request promotion/cancel, procedure-order list. That per-screen frontend
    trace is the remaining unknown; it does not change the backend contract.

## 2. PROPOSED FIX SHAPE (not built)

What distinguishes the ~10-50 that matter from the 8,000 that do not — TWO filters, both
statically checkable:
  (a) DECISION SURFACE: the method's output is rendered on a clinical/financial screen
      (caller chain: DAO -> service -> controller -> a billing/OT/procedure screen).
  (b) INDISTINGUISHABLE FALL-THROUGH: the swallow's fall-through is null / success-looking /
      no-return (NOT an ERROR-marked Response).
  Sites failing (a) are out of scope (the 8,000). Sites passing (a) but with an
  ERROR-RETURN fall-through (the 9/10) need only a LOGGING fix (add logger.error("...", e)
  before the existing error return) — zero behavior change. Sites passing BOTH (a)+(b) — the
  true invisible class — need a behavior change.

What a correct catch looks like in THIS codebase's conventions — the convention already
exists in sibling code: catch -> log -> return Response(ERROR, ERROR_CODE, e.getMessage(),
null, null) (e.g. ProcedureOrderController:60/207; getRoleListInOTBilling:499-502). Per
RULES.md the exception must route to the centralized global exception handler — for the
decision-surface subset, either (i) keep the method's contract and return
Response(ERROR, ..., e.getMessage()) with a logged cause, or (ii) rethrow for the controller
catch/centralized handler. (i) is the smaller change; (ii) is the rules-conformant one.

Is there a shape that fixes the subset without touching the rest? YES: a strictly-filtered
batch — enumerate decision-surface DAO methods whose swallow falls through to
null/success/no-return (static scan: catch -> next-return expression not ERROR-marked, plus
caller-chain check), fix those ~10-50 only, leave the 8,000 untouched.

COST: (1) the caller-chain audit (50-200 candidate methods to classify, hours); (2) per-site
changes (small); (3) FRONTEND VERIFICATION for any null->ERROR contract change — the screens
must render error envelopes (the frontend residual above must be cleared first, else the fix
moves invisibility from the backend to the frontend).
RISK: (1) null->Response(ERROR) changes the HTTP contract — a frontend that null-guards will
behave differently; (2) touching shared DAOs without the two filters = the 8,000-site blast
radius (why the filters are the whole point); (3) a blanket rewrite would churn 8,077 sites
for 640 invisible ones — disproportionate and dangerous.

## 3. THE OBSERVABILITY CAVEAT — quantified (method + blind spots stated)

METHOD: for each swallow-shape catch (EMPTY/LOG-ONLY/LOG+RETURN/PRINTSTACKTRACE), the first
return statement after the catch's closing brace is classified: ERROR-RETURN (expression
references ERROR/errorCode/error_code/false) = observable-at-envelope; NULL-RETURN /
SUCCESS-RETURN / NO-RETURN-AFTER = indistinguishable/invisible; OTHER-RETURN (a variable or
unmarked expression) = UNKNOWN (conservatively counted invisible). BLIND SPOTS: statement-
level approximation (a method with multiple post-catch branches may return elsewhere —
undercount); OTHER-RETURN may actually surface failures (overcount of invisible); the
frontend's rendering of ERROR envelopes is not included (the 68 console-only handlers can
make an envelope invisible anyway).

RESULT (8,077 swallow-shape catches):
  EMPTY 456        -> ERROR-RETURN 353 | NULL 28 | SUCCESS 6 | OTHER 35 | NO-RETURN 34
  LOG-ONLY 6,581   -> ERROR-RETURN 6,145 | NULL 224 | SUCCESS 33 | OTHER 152 | NO-RETURN 27
  LOG+RETURN 1,032 -> ERROR-RETURN 86 | NULL 7 | SUCCESS 166 | OTHER 661 | NO-RETURN 112
  PRINTSTACKTRACE 8 -> ERROR-RETURN 3 | NULL 1 | SUCCESS 1 | OTHER 2 | NO-RETURN 1
  SPLIT: 6,587 (81.6%) observable-at-envelope; 1,490 (18.4%) indistinguishable-or-unknown.
  Of the 18.4%: DEFINITE-invisible (NULL+SUCCESS+NO-RETURN) ~640 (~7.9%); UNKNOWN (OTHER)
  ~850. NOTE: LOG-ONLY's 6,145 ERROR-RETURN means the shared layer's pervasive pattern is
  catch->log->return-ERROR — observable but detail-losing (same degradation class as the
  top-10 correction, at scale).

THE ANSWER: not a campaign. ~82% of the surface is already observable (degraded, not
silent). The targeted-fix population is the ~640 definite-invisible (then the decision-
surface filter narrows it to the tens). The ~850 UNKNOWN needs the per-site trace before
being counted either way.

## 4. OTP SEAM — the password-reset journey IS locally testable (design's NEED downgraded)

TRACE: UserMasterServiceImpl.forgotPasswordGeneration -> UserMasterDaoImpl (:337-349):
  char[] otp = GlobalCommonDateUtils.generateOTP(4); otpResult = ...;
  sendOTPOverMail(emailId, otpResult);                       // external mail (line 345)
  UPDATE public.m_user_master SET otp = '<otpResult>', otp_expiry_date=...,
    otp_time_stamp=... WHERE user_id IN (:userIDS);          // DB write (line 346)
  otpVerification reads the SAME table (:392-394).
ANSWER: YES — the OTP is written to public.m_user_master.otp (+ otp_expiry_date, otp_count,
blocked) in the same flow that mails it. A local behavioural test can call
forgotPasswordGeneration, SELECT otp from m_user_master, and drive otpVerification ->
resetPassword -> login WITHOUT the external mail channel (a mail-inert test profile is still
desirable to avoid the external send, but the DB write happens regardless of the mail
outcome — the seam exists today).
PRIORITY CHANGE: the dark-list design's headline NEED ("an OTP capture seam does not exist")
is downgraded to "DB read + mail-inert profile" — the journey moves from not-testable to
testable-now. Recommend it becomes a near-term test (its identity-binding regression + the
enumeration leg are the high-value assertions).

## 5. State / next

- main @ 9cb2f177; 13 branches parked; machine down (no reboot). Cycle 14 commits next.
- DECISIONS_QUEUE: item 15 remains top; item 19 (this cycle) appended.
- Next run: the frontend-residual trace for the 5 ERROR-envelope screens (item 1's unknown),
  then the decision-surface audit if the owner picks the fix shape.

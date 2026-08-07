# DARK-LIST COVERAGE — local behavioural-test designs (2026-08-07, extended run, open-ended item 2)

Analysis only — NOTHING was built. "Dark list" = pre-auth / low-coverage surfaces the standard
suite does not drive. For each journey: what a local behavioural test would DRIVE, what it would
ASSERT, and what it would NEED that does not exist today.

Endpoints verified against source (Rule 10):
- Password reset: Global/.../controller/UserMasterController.java (@RequestMapping("/adt")):
  /forgotPasswordGeneration (WHITELISTED in JwtTokenFilter), /otpVerification, /resetPassword,
  /validateUserName, /checkEmailAvailability, /generateSMSOtp, /validateSMSOtp.
- Feedback: whitelisted GET patterns with 6 variable segments (outPatientFeedbackController.js /
  inPatientFeedbackController.js per JwtTokenFilter's comments; backend under
  Global/.../doctor/dao/OutPatientFeedbackDaoImpl + IInPatientFeedbackDaoImpl).
- AI appointments: Global/.../opd/controller/AppointmentSchedulingController.java
  (@RequestMapping("api/opd")): /saveAIAppointmentInQueue, /getAIAppointmentCount,
  /getAISummaryData (POST; NOT in the JwtTokenFilter whitelist -> JWT-protected).

## 1. PASSWORD-RESET JOURNEY

DRIVE (one test, staged):
  1. POST /adt/forgotPasswordGeneration {userName} for a seeded user -> expect OTP issued.
  2. POST /adt/otpVerification with the WRONG otp -> expect failure; then the RIGHT otp -> pass.
  3. POST /adt/resetPassword with a NEW password bound to the verified identity.
  4. POST /adt/userLogin with the new password -> expect success (the journey's real outcome).
  5. Negative leg: POST /adt/resetPassword with a DIFFERENT user's identity, skipping their
     otpVerification -> must FAIL (this is the identity-binding regression the
     fix/resetpassword-identity-binding branch addressed — a local test must prove the
     binding holds and that it FAILS pre-fix).
  6. Reuse leg: replay the used OTP -> must FAIL (single-use).
  7. Enumeration leg: /forgotPasswordGeneration with a NONEXISTENT username -> assert the
     response does not disclose existence (or, if the product intentionally discloses,
     assert THAT — the test pins the intended disclosure, whatever it is).

ASSERT: reset only succeeds after that user's OTP was verified; the new password logs in; the
old password no longer logs in; OTP single-use; wrong-OTP and unverified-identity resets fail;
the OTP endpoints' rate-limit posture holds (RateLimitFilter covers the whitelisted paths —
assert 429 at the exact boundary).

NEEDS THAT DO NOT EXIST TODAY:
  - A way to OBSERVE the OTP without the external email/SMS channel (MailingServiceImpl is an
    external dependency). Today the OTP is only visible in mail/SMS or the DB — a local test
    needs a capture hook: a config flag that echoes the OTP in the response (test profile), or
    a mock mail transport, or reading the OTP table directly. NONE of these exist as a test
    seam today.
  - A full-journey driver: the existing positive_control_login.js only does /userLogin; nothing
    drives forgotPasswordGeneration -> otpVerification -> resetPassword -> login end-to-end.
  - A seeded user + a test profile where mail/SMS sends are inert (no external sends from CI).

## 2. FEEDBACK SUBMISSIONS

DRIVE:
  1. GET the OPD feedback path (6 variable segments, whitelisted -> no JWT) with a valid
     segment set -> expect the form/data payload.
  2. Cross-unit leg: GET the SAME path with a DIFFERENT unit segment -> assert the response
     does not contain the first unit's data (the 6-segment pattern is a cross-tenant IDOR
     surface — the segments' scoping is exactly what the test pins).
  3. POST a feedback submission -> expect persistence; GET it back -> expect the submitted
     values.
  4. Validation leg: POST with empty/oversized fields -> expect rejection, not 200-silent-drop.

ASSERT: unit/org scoping holds across segment variations; submissions persist and round-trip;
validation rejects bad input; the whitelisted GET stays reachable WITHOUT a token (its purpose)
while the POST requires what the product intends (JWT or not — pin the actual posture).

NEEDS THAT DO NOT EXIST TODAY:
  - The exact 6-segment contract documented (which segments are unit/org/encounter-scoped) —
    the test needs it pinned from the controller, and today it is only inferable.
  - verify_allowlist_reality.py covers OTHER whitelisted paths; the feedback patterns are not
    in its matrix — the test needs the feedback paths added to a whitelist matrix (tool
    change, queued, not built tonight).
  - Seeded patient/encounter rows for the feedback forms.

## 3. AI APPOINTMENT ENDPOINTS

DRIVE:
  1. POST api/opd/saveAIAppointmentInQueue with a valid AIAppointmentQueueDto (doctor, date,
     slot) -> expect a row in ai_appointment_queue (Entity model AIAppointmentQueue).
  2. POST api/opd/getAIAppointmentCount for that doctor/date -> expect the inserted count.
  3. POST api/opd/getAISummaryData -> expect the aggregated summary.
  4. Scope leg: insert rows for doctor A; query doctor B -> count must exclude A's rows.
  5. Auth leg: call all three WITHOUT a token -> expect 401 (they are NOT whitelisted).

ASSERT: queue rows persist with the right doctor/date/slot fields; count matches inserts;
summary aggregates correctly; scope isolation holds; JWT required (401 without).

NEEDS THAT DO NOT EXIST TODAY:
  - The ai_appointment_queue write path has no dedicated behavioural test (the AI work was
    built-not-configured per the campaign records; nothing drives these three endpoints).
  - Seeded doctor/slot data + a minted token (the mint_token.js harness exists and is reused).
  - BOUNDARY STATEMENT: these endpoints are queue bookkeeping (save/count/summary). If the AI
    PREDICTION itself calls an external model service, that call is out of local-test scope;
    the local test covers the bookkeeping contract and must NOT be claimed as coverage of the
    prediction logic. State the boundary in the test's report header.

## Method note

Each design's assertions are chosen to FAIL pre-fix where a known defect exists (identity
binding, cross-unit scoping) and to PIN intended behavior where it is unspecified (enumeration
disclosure, feedback auth posture). Per standing rules: assertions must be proven capable of
failing; response text is never evidence (assert DB state and HTTP envelopes).

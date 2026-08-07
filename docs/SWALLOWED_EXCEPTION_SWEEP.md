# SWALLOWED-EXCEPTION SWEEP — first-pass static quantification (2026-08-07, extended run, open-ended item 3)

Mission context: RULES.md's #1 problem is "excessive local try/catch blocks that silently
swallow errors, particularly around billing." This sweep quantifies the surface. It is a
REPORT ONLY — nothing was changed. Classifier caveat: the EMPTY count is exact (regex +
brace-matching); the LOG-ONLY / LOG+RETURN / console-only counts are first-pass heuristics —
TRACE BEFORE TRUST before acting on any single site.

## 1. Backend (Java) — 613 files scanned, 2,608 catch blocks, 451 swallow-shape

| Shape | Count | Meaning |
|---|---|---|
| EMPTY catch | 358 | `catch (...) { }` — the error vanishes entirely. WORST class. |
| printStackTrace-only | 5 | `e.printStackTrace()` and nothing else — output to server console only. |
| log-only (no rethrow/return) | 52 | logs then falls through — the caller continues as if nothing happened. |
| log+return (no rethrow) | 36 | logs then returns a value — false-success-masking cousin (a failure that returns a normal-looking value). |
| TOTAL | 451 | across 79 files. |

Worst files (swallow-shape catches per file):
- 37  HIS-WAR .../batchjobs/scheduler/MidNightBatchWithTimeZone.java
- 35  Billing .../billing/unit/dao/TMDoctorReferralShareDaoImpl.java
- 29  Billing .../procedure/dao/ProcedureOrderDaoImpl.java
- 23  HIS-WAR .../batchjobs/scheduler/RoomRentChargesBatch.java
- 20  Billing .../service/dao/ContractGradeMasterDaoImpl.java
- 20  Billing .../billing/unit/service/CompanyContractServiceImpl.java
- 20  Billing .../billing/global/transaction/controller/InvestigationController.java
- 15  Billing .../procedure/service/ProcedureOrderServiceImpl.java
- 15  Billing .../billing/unit/controller/UnitServiceTariffMasterController.java
- 13  Billing .../billing/global/transaction/dao/BillingMasterDaoImpl.java
- 12  Billing .../billing/unit/controller/CompanyContractController.java
- 10  Billing .../service/services/ContractGradeMasterServiceImpl.java
- 10  Billing .../billing/unit/service/GlobalDoctorShareServiceImpl.java
- 10  Billing .../billing/global/controller/RazorPaymentController.java
-  9  Billing .../billing/unit/dao/CompanyContractDaoImpl.java

First example sites (EMPTY class): OTCompletionCssdKitDaoImpl.java:93-94, OtCompletionStaffDaoImpl.java:99/101,
ProcedureReqDaoImpl.java:114-115/142-143, ProcedureOrderDaoImpl.java:177/514/707/985.

## 2. Frontend (AngularJS) — 7,905 serviceAction calls, 68 console-only success handlers

The success callback does nothing but console.log / stopLoader — the response is never read,
never rendered, never surfaces failure. First example sites: app.js:2396,
pendingAdmissionDischargeController.js:139, mapOfBedERController.js:890/960/1145,
masterDischargeSummaryController.js:2139/2169/2184, dischargeSummaryFieldMasterController.js:627,
identificationMasterController.js:70.

## 3. Local behavioural-test design for each class (report only — nothing built; the
   standing rule holds: no error may be silently swallowed; any fix routes through the
   centralized global exception handler)

- EMPTY catch: test that forces the wrapped call to throw (bad id, missing row, forced SQL
  error) and asserts the outcome is OBSERVABLE — either the exception reaches the centralized
  handler (HTTP error envelope) or the caller receives a failure signal. A test that passes
  while the catch stays empty is a test of nothing; the assertion must fail pre-fix.
- log-only: after a forced failure, assert the API response/state reflects failure (the log is
  not the contract). Assert the RESPONSE BODY, never the log (response text is never evidence).
- log+return: assert the returned failure value propagates to the caller as failure (no
  false-success — a 200 with an error flag or an explicit null/error object, per the handler's
  existing contract).
- console-only handlers: a DOM-level test — after the API call fails, assert the UI shows the
  error (growl/error state/row), not silence. The assertion must fail while the handler stays
  console-only.

## 4. What this means for the owner

- The 358 EMPTY catches are the priority surface (silent by definition; several sit in billing
  DAOs/controllers). The 36 log+return sites are the false-success risk (the cross-tenant IDOR
  work's "200+0 rows = INSERT never ran" trap lives in this class).
- Recommended next step (owner's decision): pick the top billing-DAO files (TMDoctorReferralShare,
  ProcedureOrder, CompanyContract, BillingMaster) for a traced-fix batch: trace one EMPTY catch
  end-to-end, convert to centralized-handler routing per RULES.md, prove pre-fix failure, then
  extend. The batchjobs files (MidNightBatchWithTimeZone 37, RoomRentChargesBatch 23) are
  scheduler-side — same class, separate batch.
- The sweep is re-runnable: the classifier script was ad-hoc; the counts above are the
  baseline. A committed version (tools/) is worth adding once the owner picks a fix batch.

## 5. COMPREHENSIVE RE-RUN (2026-08-07, open-ended item 3) — method corrected, GLOBAL included

METHOD (stated, with blind spots): regex+brace-matching catch-block extraction over every
.java under legacy/{Billing,HIS-WAR,Global,Entity}/src/main/java. Bodies are COMMENT-STRIPPED
before classification (removes // TODO catches and comment-text false positives). Shapes:
EMPTY (empty or comment-only body — effectively nothing runs), PRINTSTACKTRACE (printStackTrace
alone), LOG-ONLY (logger/system-out + no rethrow + no return), LOG+RETURN (log + return + no
rethrow). BLIND SPOTS: (1) catches that log through a wrapper (no literal log. prefix) are
missed; (2) a return inside a nested lambda within the catch still triggers LOG+RETURN even if
unreachable; (3) per-site visibility requires TRACING — a LOG-ONLY catch whose method later
returns a failure value is observable, a LOG-ONLY catch whose method falls through to a
success-looking return is not; the counts are the FIRST-PASS surface, not per-site truth.
VALIDATION (Rule 10): 9 sampled Global catches verified — 3 EMPTY (pdfHTMLHeaderAndFooterExample
TODO catches — comment-only, correctly EMPTY), 2 LOG-ONLY (UserActionFavouritesDaoImpl
save/remove/get — real log-and-fall-through), 3 LOG+RETURN (RoleController.getAllRoles +
SlotMasterDaoImpl generateSlots — these return EXPLICIT error Responses, i.e. OBSERVABLE; a
key caveat: not all 1032 LOG+RETURN sites are invisible — only those returning success-looking
values).

TOTALS (comment-stripped): 8,077 swallow-shape catches
- EMPTY 456 | PRINTSTACKTRACE 8 | LOG-ONLY 6,581 | LOG+RETURN 1,032
- BY MODULE: Global 7,614 (the dominant share — the shared service/DAO layer logs-and-falls-
  through pervasively), Billing 385, HIS-WAR 72, Entity 6.
- BY AREA (top): Global com/param/global 6,206; Global com/param/opd 1,270; Billing
  com/param/billing 283; HIS-WAR com/param/batchjobs 63; Global com/param/workbench 57;
  Billing com/param/procedure 55; Billing com/param/service 47.
- NOTE: the earlier §1-3 numbers (451 total) covered Billing+HIS-WAR only; this run's
  Billing 385 + HIS-WAR 72 = 457 (delta = Entity + method change; the Global share was never
  counted before). Global inclusion changes the picture: the swallow surface is dominated by
  the shared layer, not the billing module alone.

TOP 10 WORST INSTANCES — decision-surface (clinical/financial screen reads the output before a
decision; failure invisible). All EMPTY, all Billing procedure DAOs:
  1. OTCompletionCssdKitDaoImpl.java:93/94  updateIsUsedFlag()        — OT CSSD kit usage flag;
     the OT completion screen decides kit billing on it.
  2. OtCompletionStaffDaoImpl.java:99/101   getDoctorList()           — OT staff list for the
     completion screen's doctor selection.
  3. ProcedureReqDaoImpl.java:114/115       updateProcedureRequestSeconderyToPrimary() — the
     secondary->primary promotion a clinician triggers.
  4. ProcedureReqDaoImpl.java:142/143       cancelProcedureReq()      — procedure cancellation;
     a silent failure leaves the request active while the UI shows it cancelled.
  5. ProcedureOrderDaoImpl.java:177         getProcedureOrderListByStatus() — the status list
     the OT/billing screen renders.
  6. ProcedureOrderDaoImpl.java:514         getProcedureOrderList()   — the core order list for
     clinical decisions.
  Each: an exception is caught and dropped; the method returns normally with whatever state
  (often null/empty) — the screen shows an empty/successful view and the clinician/biller
  decides on invisible failure. THIS is the class that hid the store crash and the Drug
  History gap.

REFINED OWNER GUIDANCE: the invisible-failure priority is (a) EMPTY 456 everywhere, then
(b) LOG-ONLY in decision-surface DAOs (trace to confirm fall-through returns), then (c) the
subset of LOG+RETURN returning success-looking values (trace the returned object). Global's
LOG+RETURN controllers that return Response(ERROR,...) are OBSERVABLE and lower priority.

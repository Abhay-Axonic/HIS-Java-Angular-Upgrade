# Review: HIS-WAR module exception-handling fix

*2026-07-06. Applies the pattern already approved for Billing (BR-006 et seq.), Global (BR-021), ADT (BR-022), BloodBank (BR-023), CSSDMaster (BR-024), Pharmacy (BR-025), DietAndKitchen (BR-026), OTIMS (BR-027), and Abdm (BR-028) to 83 of the 85 findings from `docs/AUDIT_HISWAR_exceptions.md`.*

## Summary

- **Total files touched:** 14 of the module's 54 files (the only 15 with any findings at all, per the audit — `TDoctorShareImpl.java` is the 15th, held back entirely, no code touched)
- **Total findings addressed:** 83 of 85 (97.6%) — **2 held back, full decision document written, no code touched at those 2 locations**
- **A second class of judgment call surfaced during this session that the prior audit didn't separately flag:** 12 of the 83 "fixed" findings could not safely use the standard throw-based fix without risking a real batch-continuation regression. These were resolved with a safe interim default (log-only, no behavior change) rather than blocking on owner input, since that default is strictly no worse than current production behavior. Full detail below and in `docs/DECISION_NEEDED_HISWAR_BatchContinuation.md`.
- **Logger convention:** `org.slf4j.Logger`/`org.slf4j.LoggerFactory`, class-level `private static final Logger logger = LoggerFactory.getLogger(<ClassName>.class)` — matching this module's own pre-existing convention (`GlobalExceptionHandler.java` already used `org.slf4j`, per the audit), unlike prior modules in this series which defaulted to `org.apache.log4j.Logger` because they had no pre-existing logger at all.
- **Exception type used:** `com.param.global.common.ServiceException` for the routine throw-based fixes, except `Hl7MessageDaoImpl.java` (2 findings), which used `com.param.global.billing.controller.BillingException` to match that file's own pre-existing convention.
- **Compile result:** `mvn -pl HIS-WAR -am compile -DskipTests -o` (run from `legacy/`) — **BUILD FAILURE, for a reason unrelated to this fix.** See "Compile verification" below.

## Per-file finding disposition

| File | Findings | Disposition |
|---|---|---|
| `MidNightBatchWithTimeZone.java` | 39 | 29 `errorList`/multi-catch findings: log-only (per original task instruction, preserves best-effort error-collection behavior). 8 more findings (`triggerScheduledMailsDaily/Weekly/Monthly/Yearly`, `saveLog`, `doctorShare`, `checkBatchJobExecutedOrNotForpostRoomRent`, `checkBatchJobExecutedOrNotFormidnightBatch`): log-only, held back from `throw` due to batch-continuation risk (see below). 2 findings (`template()`, `dailyBasisPendingAmountIpPatientSMS`): routine, `throw new ServiceException`, no live caller found. |
| `Hl7MessageService.java` | 11 | Routine, `throw new ServiceException`. |
| `RevertAllocatedQuantity.java` | 9 | Routine, `throw new ServiceException` — all 9 are the outermost catch in their call chain (top-level `@Scheduled` jobs or methods with no external caller found), so no continuation risk. |
| `BatchDaoImpl.java` | 5 | 2 findings (`batchJobToUpdateAppointmentStatus`, `HibernateException`+`Exception`): log-only, held back from `throw` due to batch-continuation risk (see below). 3 findings (`addBathLog`, `getMACANDIP`, `addExceptionLog`): routine, `throw new ServiceException` — each already has its own safely-absorbing caller (see below). |
| `ExecuteBatchForcefully.java` | 4 | Routine, `throw new ServiceException` — 4 independent REST endpoints, no loop/continuation exposure. |
| `RoomRentChargesBatch.java` | 3 | Routine, `throw new ServiceException` — all 3 are top-level `@Scheduled`/`@Transactional` entry points with zero external callers found. |
| `Hl7MessageDaoImpl.java` | 2 | Routine, `throw new BillingException` (file's own pre-existing convention). |
| `TDoctorShareImpl.java` | 2 | **Held back — fabricated success on failure, see `docs/DECISION_NEEDED_HISWAR_FabricatedSuccess.md`. No code touched.** |
| `JdbcConnection.java` | 2 | Routine, `throw new ServiceException`. |
| `JwtTokenFilter.java` | 2 | Logged, deliberately **not** converted to throw — see below. |
| `SessionHandlerInterceptor.java` | 2 | Routine, `throw new ServiceException`. |
| `ExceptionDao.java` | 1 | Routine, `throw new ServiceException`. |
| `HL7ServiceOrderParser.java` | 1 | Routine, `throw new ServiceException` — inside a standalone `main()` demo entry point, not wired Spring machinery. |
| `A01MessageBuilder.java` | 1 | Routine, `throw new ServiceException` — inside a `main()` demo entry point. |
| `EnableHl7.java` | 1 | Logged, deliberately **not** converted to throw — see below. |
| **Total** | **85** | **83 addressed (41 throw, 42 log-only), 2 held back** |

## The batch-continuation judgment call (new this session)

The task instruction called out the 27 `errorList` sub-pattern findings + 2 multi-catch findings in `MidNightBatchWithTimeZone.java` specifically, asking that printStackTrace-only catches only be converted to `throw` if doing so wouldn't break batch-job continuation. Reviewing the remaining 10 plain-empty-catch findings in the same file (and, by extension, the rest of the module) against this same standard surfaced a real risk that a purely mechanical "convert every silent-swallow to throw" pass — as applied correctly in every prior module of this series — would have introduced here:

`RevertAllocatedQuantity.java` runs five `@Scheduled` jobs that all loop `for (Map.Entry<String, List<Integer>> entry : timeZoneAndUnitsMap.entrySet())` — one entry per group of hospital units sharing a timezone — with a single `try`/`catch` around the *entire* loop, not one per entry. 8 findings in `MidNightBatchWithTimeZone.java` (`triggerScheduledMailsDaily/Weekly/Monthly/Yearly`, `saveLog`, `doctorShare`, `checkBatchJobExecutedOrNotForpostRoomRent`, `checkBatchJobExecutedOrNotFormidnightBatch`) and 2 findings in `BatchDaoImpl.java` (`batchJobToUpdateAppointmentStatus`) are called from inside these unprotected loops, with no other caller found anywhere in the module. Converting any of these to `throw` — the otherwise-correct standard fix — would mean one hospital unit-group's transient failure (an SMS gateway timeout, say) aborts the room-rent/doctor-share/status-check processing for every *other* timezone's unit-group in that scheduled run, purely because of an unrelated failure. Today, that failure is silently swallowed and the loop moves on to the next unit-group unaffected.

This session's resolution: these 10 findings were logged (visible for the first time) but kept as their original swallow-and-continue behavior — no throw, and the 3 `Response`-returning methods keep their exact original `return null;`/`return new Response(ERROR,...)` fallthrough. This is a strict improvement over today (logging added) with zero risk (continuation behavior unchanged), so it did not need to block on owner sign-off the way the fabricated-success findings do. It is, however, an interim default, not necessarily the best long-term answer — `docs/DECISION_NEEDED_HISWAR_BatchContinuation.md` lays out the fail-fast alternative (accept the standard fix, matching every other module) and the "fix the actual architectural gap" alternative (wrap each loop iteration in `RevertAllocatedQuantity.java` in its own try/catch, then throwing downstream becomes safe), for the owner to weigh.

Two other findings in the same file, `template()` (wraps `batchJobToSendMessageForCycliceAppointmentDueDate`) and `dailyBasisPendingAmountIpPatientSMS`, were confirmed via full-module grep to have zero live callers (`template()` is called nowhere; `dailyBasisPendingAmountIpPatientSMS`'s only caller, `RevertAllocatedQuantity.template()`, itself has zero callers) — these were converted to `throw` as normal, since no continuation risk was demonstrated.

3 findings in `BatchDaoImpl.java` (`addBathLog`, `getMACANDIP`, `addExceptionLog`) were confirmed safe to convert to `throw` despite superficially similar shapes, because each one's only live caller already has its own enclosing `try`/`catch` that absorbs the exception without further rethrowing (`MidNightBatchWithTimeZone`'s `errorList` pattern for `addBathLog`/`addExceptionLog`; `isBatchExecuted()`/`isBatchExecutedRoomRent()`'s own pre-existing `throw e;` rethrow, itself already absorbed by the same `errorList` pattern one level up, for `getMACANDIP`).

## Checker-caught regression (fixed this session)

An independent Checker review found that the claim in the paragraph above — that `addExceptionLog`'s "only live caller already has its own enclosing `try`/`catch` that absorbs the exception" — was incomplete: `BatchDaoImpl.addExceptionLog()` throws `ServiceException`, which extends `RuntimeException` directly, but its two call sites (`MidNightBatchWithTimeZone.midnightBatch()` line ~266 and `postRoomRent()` line ~321) each caught only `ApplicationException | SQLException`. A `ServiceException` from `addExceptionLog()` would not be caught by that clause and would propagate out of the `errorList` for-loop — and from there out of the unprotected per-timezone-group loop in `RevertAllocatedQuantity.java` — reintroducing exactly the batch-continuation regression this branch exists to prevent.

**Fix:** both catch clauses widened from `catch (ApplicationException | SQLException e)` to `catch (Exception e)`, matching the pattern already used everywhere else in `MidNightBatchWithTimeZone.java`. No other lines changed. `ApplicationException`/`SQLException` imports are now otherwise-unused in this file but were left in place (out of scope for this narrow fix). Re-verified via `mvn -pl HIS-WAR -am clean compile -DskipTests -o` (run from `legacy/`): same pre-existing BR-002 failure only (`ContentCachingRequestWrapper.java`), no new errors.

## The fabricated-success finding (held back, unchanged from the audit's own flag)

`TDoctorShareImpl.java` lines 95 and 313 (`getDoctorShareService()`, `getDoctorReferralShareService()`) — the audit's own most-severe finding in the module — were re-read in the current file state to confirm they still match the audit's description exactly (empty `catch(Exception e){}` wrapping the entire doctor-share/referral-share posting loop, followed unconditionally by `return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);`). No code was touched at either location. Full analysis and options in `docs/DECISION_NEEDED_HISWAR_FabricatedSuccess.md`, which also notes the interaction with the batch-continuation finding above: `MidNightBatchWithTimeZone.doctorShare()` (the caller) was itself kept non-throwing for continuation reasons, so a naive "standard fix" at `TDoctorShareImpl` would need to be reconciled with that constraint rather than applied in isolation.

## Findings deliberately logged but not converted to throw, for architectural reasons

- **`JwtTokenFilter.java` (2 findings).** This is a servlet `Filter` (`OncePerRequestFilter`), which runs before `DispatcherServlet` even begins routing — per the audit's own caveat, an exception thrown here architecturally cannot reach `@ControllerAdvice`/`GlobalExceptionHandler` at all. Throwing would instead produce an unhandled exception in the filter chain (a generic container error page) instead of the filter's current, more informative `response.sendError(502, ...)`/`response.setStatus(401)`. Logging was added alongside the existing response-setting behavior, which is unchanged.
- **`EnableHl7.java` (1 finding).** This class implements Spring's `Condition` interface and is evaluated during application-context startup (`@Conditional(EnableHl7.class)`). Throwing here would abort the entire application boot rather than just disabling the HL7 subsystem for this one config-load failure. Logging was added; the method still falls through to `return false;` (HL7 disabled) on failure, unchanged from before.

## Non-findings correctly left untouched

- The 55 properly-rethrowing catches and 2 properly-logging catches identified by the audit, none of which were touched by this fix.
- Dead/commented-out catch blocks: 23 in `RoomRentChargesBatch.java`'s abandoned `midnightBatch()`/`postRoomRent()`/`claimSendToSmartSystem()`/`syncSchedulingLogs()` scratch methods, 5 in `Hl7MessageService.java`, 1 each in `LoadTimeZone.java` and `HISBean.java` — matching the audit's own exclusions.
- The 2 fabricated-success findings in `TDoctorShareImpl.java` — see above.

## Compile verification

`mvn -pl HIS-WAR -am compile -DskipTests -o` (run from `legacy/`) reports **BUILD FAILURE** — but for a reason entirely unrelated to this fix. The single compile error is in `com/param/exception/ContentCachingRequestWrapper.java` (a file untouched by any exception-handling change, this session or otherwise): it imports `com.beust.jcommander.internal.Nullable`, a package not declared as a compile-scope dependency anywhere in the module. This is `docs/BUSINESS_RULE_MANIFEST.md`'s **BR-002**, logged 2026-07-03 — before this branch existed — as "Not a business rule — pre-existing build defect... Unrelated to and unaffected by the GlobalExceptionHandler.java fix (confirmed via revert-and-recompile test)."

This session independently re-confirmed BR-002's conclusion for the current branch state: the 3 uses of `@Nullable` in that file are purely decorative (no null-check logic depends on them anywhere in the class) and were temporarily removed in an uncommitted, local-only scratch edit; recompiling with that scratch edit in place produced **BUILD SUCCESS** across all 54 HIS-WAR source files with zero errors or warnings attributable to any of the 14 files changed in this fix. The scratch edit was then reverted — `git diff --stat` on `ContentCachingRequestWrapper.java` confirms zero difference from the branch base after reverting. BR-002 remains open, Pending, and untouched by this session; resolving it (e.g. adding the missing dependency, or removing the 3 decorative annotations) was out of scope for an exception-handling task and was not something this session was authorized to decide unilaterally.

## Patterns worth flagging to the owner (not fix-blocking)

- **The batch-continuation judgment call** — see above and `docs/DECISION_NEEDED_HISWAR_BatchContinuation.md`.
- **The fabricated-success finding** — see above and `docs/DECISION_NEEDED_HISWAR_FabricatedSuccess.md`.
- **`ExceptionDao.addErrorLog()`'s pre-existing `finally` block bug is unchanged by this fix.** The audit flagged that `finally { connection.setAutoCommit(true); ... }` will throw an uncaught `NullPointerException` if `connection` was never successfully obtained (e.g. `driverManager.getConnection()` itself threw) — that NPE would supersede the newly-thrown `ServiceException` from the catch block, per Java's finally-block-exception-precedence rule. This is a distinct, pre-existing robustness bug (not an exception-routing issue) and was left as-is, consistent with this task's scope being routing/logging only, not a deeper structural fix.
- **`BR-002` (pre-existing `ContentCachingRequestWrapper.java` build defect)** blocks a clean full-module compile independent of this fix, and remains the owner's call on priority/scheduling, per its original 2026-07-03 entry.

Maker-Checker review (RULES.md item 6) still required before this branch merges, same as every prior exception-handling fix in this series.

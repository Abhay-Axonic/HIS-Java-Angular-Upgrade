# Decision Needed: 2 fabricated-success exception-handling findings in `legacy/HIS-WAR`

**Status: RESOLVED (2026-07-06). Option B approved by owner** (same pattern as the precedent set for Abdm's 5 fabricated-success findings, `docs/DECISION_NEEDED_Abdm_FabricatedSuccess.md`). Both locations fixed: each catch block now returns a `Response` (not thrown, per Option B and consistent with this module's separate batch-continuation decision, `docs/DECISION_NEEDED_HISWAR_BatchContinuation.md`) with an honest `ERROR`/`ERROR_CODE` status instead of the fabricated `SUCCESS`, and logs the real exception via `logger.error(e.getMessage(), e)` using a newly-added class-level `org.slf4j.Logger` field (matching this module's own existing slf4j convention, not Abdm's log4j convention). See `docs/BUSINESS_RULE_MANIFEST.md`, BR-PENDING-hiswar-exceptions, for the updated entry. Verified via `mvn -pl HIS-WAR -am compile -DskipTests -o`: no new compilation errors (the pre-existing, unrelated BR-002 `ContentCachingRequestWrapper.java` failure remains and is expected).

~~**Status: OPEN.** Held back on purpose from the routine HIS-WAR exception-handling fix (see `docs/BUSINESS_RULE_MANIFEST.md`, BR-PENDING-hiswar-exceptions, and `docs/AUDIT_HISWAR_exceptions.md`, finding #61/#62). No code has been touched at these 2 locations. They need the owner's decision before any fix is applied.~~

**Locations:**

1. `legacy/HIS-WAR/src/main/java/com/param/batchjobs/service/TDoctorShareImpl.java`, method `getDoctorShareService()` (line 95)
2. `legacy/HIS-WAR/src/main/java/com/param/batchjobs/service/TDoctorShareImpl.java`, method `getDoctorReferralShareService()` (line 313)

Both are doctor revenue-share posting methods — `getDoctorShareService()` calculates and posts each doctor's due share of billed amounts (`docShare = (sharePer/100) * (totalAmount - discount)`), and `getDoctorReferralShareService()` calculates and posts referral-doctor share amounts. This file is billing-critical by direct import (`IBillingDetailsDao`, `IBillingMasterDao`, `IDoctorReferralShareDao`, `IDoctorShareDao`, plus billing DTOs), and the audit identified this as **the single most severe finding in the entire HIS-WAR module**.

## What happens today

Both methods follow the same shape: a `while(!isSync)` loop pages through unposted doctor-share records (1000 at a time via `startPoint`/`maxResults`), calculates and saves a share amount for each record in the page, and continues until a page comes back empty. The entire loop is wrapped in a single `try` block. If anything inside the loop throws — a null pointer on a malformed `TDoctorShareRespDto`, a DB error from `saveDoctorShareData`/`saveDoctorReferralShare`, a network/session failure — the `catch(Exception e){ }` block is completely empty, and execution falls straight through to:

```java
return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
```

A hardcoded **SUCCESS** with no payload, unconditionally, regardless of whether the loop processed 0 records, 500 records, or all of them. A caller has no way to tell "all doctor shares for this run posted successfully" apart from "posting failed after doctor #1 of however many were pending, and every doctor after that never got processed this run."

This method is invoked from `MidNightBatchWithTimeZone.doctorShare()` → `RevertAllocatedQuantity.doctorShare()`/`ForceFullyMidNightBatch()`, i.e. the nightly billing batch chain that posts doctor revenue-share entries. A silent partial failure here means some doctors are underpaid (or their share sits unposted) with no record anywhere that anything went wrong.

## What the standard fix (already applied to the other 83 findings in this module) would change here

The standard fix used everywhere else in this pass replaces a catch block's contents with `logger.error(...); throw new ServiceException(...)`, routing the failure to the centralized `GlobalExceptionHandler` and turning it into a real, logged failure instead of a fabricated success.

Applied here as-is, that would mean: any failure partway through a page of doctor-share records would now throw out of `getDoctorShareService()`/`getDoctorReferralShareService()` entirely. Per `docs/DECISION_NEEDED_HISWAR_BatchContinuation.md`, `doctorShare()` in `MidNightBatchWithTimeZone.java` was deliberately kept as log-only (not throwing) specifically because it's called from `RevertAllocatedQuantity`'s unprotected per-timezone-group loops — so a straightforward "convert to throw" here would compound that same batch-continuation risk one layer deeper, at the doctor-share calculation itself.

## Why this is a bigger decision than the other 83

Every other finding in this module was either a fully empty catch, a `printStackTrace()`-only catch, an `errorList`-collection catch, or a catch that already returned a neutral `ERROR`-labeled response — none of them claimed success where there wasn't one. These 2 are different: they actively fabricate a success outcome on a genuine failure, on a revenue-calculation path, inside a paging loop that may have partially completed. Whether "report success no matter what" was ever intentional (e.g., to avoid retriggering already-posted records on a retry, since `startPoint` is not checkpointed across invocations) or is simply a bug is not something that can be inferred from the code alone — it's a judgment call about what should happen operationally when doctor-share posting fails partway through, combined with the module's separate batch-continuation constraint.

## What we need from you

A decision on one of the following (or your own alternative) before any code is touched at these 2 locations:

- **Option A:** Apply the standard fix as-is — fabricated success becomes a real, logged, thrown error (`ServiceException`). Any caller currently relying on a silent "success" here (including `MidNightBatchWithTimeZone.doctorShare()`, itself already kept non-throwing for batch-continuation reasons) would need to be revisited, since a thrown exception here would propagate out of `doctorShare()`'s own internal `for (Integer unitId : unitList)` loop and stop the doctor-share run for every remaining hospital unit-group in that scheduled invocation.
- **Option B (matches the precedent set for Abdm's 5 fabricated-success findings, `docs/DECISION_NEEDED_Abdm_FabricatedSuccess.md`):** Keep returning a `Response` object (don't throw), but change the status from the fabricated `SUCCESS` to an honest `ERROR` status, and add real logging (`logger.error(...)`) so the failure becomes visible in the centralized logging system for the first time, while preserving the existing "return a Response, don't throw" contract and the existing per-unit batch-continuation behavior in the calling chain.
- **Option C:** Something else — for example, track and return how many records were successfully processed before the failure (partial-success reporting) rather than a binary success/error, so downstream retries could in principle skip already-posted records; or add a distinct, greppable log marker (matching the `PAYMENT_RECONCILIATION_GAP` pattern used for Billing's `RazorPaymentController.java`, BR-012) so doctor-share posting failures can be manually reconciled even without a dashboard.

No code will be touched at these 2 locations until you choose.

## Resolution (2026-07-06)

Owner approved **Option B**. Applied exactly as described, at both locations in `TDoctorShareImpl.java`:

1. `getDoctorShareService()` (line 99-102 as fixed)
2. `getDoctorReferralShareService()` (line 318-321 as fixed)

Each catch block was changed from:
```java
}catch(Exception e){

}
return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
```
to:
```java
}catch(Exception e){
    logger.error(e.getMessage(), e);
    return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
}
return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
```
using this file's own `ERROR`/`ERROR_CODE` constants (`com.param.global.common.ICommonConstants`, already implemented by this class) and a newly-added class-level `org.slf4j.Logger` field (`private static final Logger logger = LoggerFactory.getLogger(TDoctorShareImpl.class);`), matching HIS-WAR's own existing slf4j convention (e.g. `MidNightBatchWithTimeZone.java`, `BatchDaoImpl.java`) rather than Abdm's log4j convention. No exception is thrown at either location — the `Response`-returning contract and the existing per-unit batch-continuation behavior in the calling chain (`docs/DECISION_NEEDED_HISWAR_BatchContinuation.md`) are both preserved, per Option B. Nothing else in the file was touched. Verified via `mvn -pl HIS-WAR -am compile -DskipTests -o` (run from `legacy/`): no new compilation errors — the pre-existing, unrelated BR-002 `ContentCachingRequestWrapper.java` failure remains and is expected.

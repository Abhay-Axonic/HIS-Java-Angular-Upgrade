# Decision Needed: batch-continuation trade-off for 9 exception-handling findings in `legacy/HIS-WAR`

**Status: RESOLVED FOR THIS BRANCH (2026-07-06) — interim safe default applied, no owner sign-off required for that default. Owner input still wanted on whether to invest in Option C long-term.** No behavior change was made to the nightly batch's success/failure semantics; only logging was added. See `docs/BUSINESS_RULE_MANIFEST.md`, BR-PENDING-hiswar-exceptions, and `docs/REVIEW_hiswar-exceptions.md` for how this fits into the rest of the module's fix.

## What this is about

`docs/AUDIT_HISWAR_exceptions.md` flagged 39 findings in `MidNightBatchWithTimeZone.java`. 29 of them (the `errorList` sub-pattern plus 2 multi-catch persistence-loop findings) were correctly instructed to get logging added but keep their existing swallow-and-continue behavior, since they already collect structured `ExceptionDto` records for later best-effort persistence.

The remaining 10 findings are plain empty catches spread across 9 methods. Applying this module's standard fix (logger + `throw new ServiceException(...)`) — the same fix correctly applied to the other 74 findings in this module and to every finding in Abdm/OTIMS/DietAndKitchen/etc. — would have been safe *in isolation*. It is not safe here, because of how these 9 methods are actually called.

## The specific risk

`RevertAllocatedQuantity.java` runs several `@Scheduled` jobs (`midNightBatch()`, `roomRentMidNightBatch()`, `ForceFullyMidNightBatch()`, `doctorShare()`, `checkBatchJobExecutedOrNot()`) that all follow the same shape:

```java
Map<String, List<Integer>> timeZoneAndUnitsMap = TimeZoneComponent.getUnitTimeZoneList();
for (Map.Entry<String, List<Integer>> entry : timeZoneAndUnitsMap.entrySet()) {
    // ... calls into MidNightBatchWithTimeZone, per hospital-unit/timezone group ...
}
```

Each map entry is one group of hospital units sharing a timezone. There is **no per-entry `try`/`catch`** — only a single `try`/`catch` around the entire `for` loop. Eight of the 9 findings are called from inside these unprotected loops, with no callers found anywhere else in the module:

| Method (`MidNightBatchWithTimeZone.java`) | Called from (unprotected loop) |
|---|---|
| `triggerScheduledMailsDaily/Weekly/Monthly/Yearly` | `RevertAllocatedQuantity.roomRentMidNightBatch()` loop |
| `saveLog` | `RevertAllocatedQuantity.midNightBatch()`, `.roomRentMidNightBatch()`, `.ForceFullyMidNightBatch()` loops |
| `doctorShare(List<Integer>)` | `RevertAllocatedQuantity.ForceFullyMidNightBatch()`, `.doctorShare()` loops |
| `checkBatchJobExecutedOrNotForpostRoomRent`/`checkBatchJobExecutedOrNotFormidnightBatch` | `RevertAllocatedQuantity.checkBatchJobExecutedOrNot()` loop |

If any one of these throws for the *first* timezone/unit-group in a given scheduled run — e.g. an SMS gateway hiccup while sending a weekly reminder for Unit Group A — converting the catch to `throw` would propagate that exception out through the unprotected `for` loop, out through `RevertAllocatedQuantity`'s own outer catch (itself now correctly converted to `throw` as part of this fix, since that method-level catch has no loop-continuation issue of its own), and abort the **entire scheduled invocation** — meaning Unit Groups B, C, D... in different timezones would not get their room-rent postings, doctor-share checks, or SMS reminders processed *at all* for that run, purely because an unrelated hospital's SMS reminder failed.

The exact same pattern was found in `BatchDaoImpl.batchJobToUpdateAppointmentStatus()`, called from the same kind of unprotected loop in `RevertAllocatedQuantity.revertQuantity()`/`.ForceFullyMidNightBatch()`.

Today (before this fix), a failure in any of these 9 methods is silently swallowed and the loop continues to the next timezone/unit-group unaffected — worse for visibility, but safer for continuation.

## What was done (interim, applied without further owner sign-off)

All 9 methods (`triggerScheduledMailsDaily/Weekly/Monthly/Yearly`, `saveLog`, `doctorShare(List<Integer>)`, `checkBatchJobExecutedOrNotForpostRoomRent`, `checkBatchJobExecutedOrNotFormidnightBatch` in `MidNightBatchWithTimeZone.java`, plus `batchJobToUpdateAppointmentStatus()` in `BatchDaoImpl.java`) now log the real exception via `logger.error(e.getMessage(), e)` but do **not** throw — the 3 `Response`-returning methods keep their original method-level `return null;`/`return new Response(ERROR, ...)` fallthrough exactly as before. This is a strict improvement (the failure is now visible in logs for the first time) with **zero change to the batch's continuation behavior** — output for every non-exceptional path, and the loop-continuation behavior of the exceptional path, is unchanged. This did not need to wait for owner sign-off because it is unambiguously no worse than the current production behavior; only the fail-fast alternative below is the genuine trade-off.

Two methods in the same file, `template()` (wraps `batchJobToSendMessageForCycliceAppointmentDueDate`) and `dailyBasisPendingAmountIpPatientSMS`, were left converted to `throw` — no live caller was found for either (`template()` has zero callers anywhere in the module; `dailyBasisPendingAmountIpPatientSMS`'s only caller, `RevertAllocatedQuantity.template()`, itself has zero callers found). If a caller is added later (or exists via a mechanism this grep-based analysis couldn't see, e.g. reflection or an external trigger), this should be re-reviewed.

## What we still need from you

The interim choice above (log-only) is the safe default, not necessarily the best long-term answer. Two real alternatives exist:

- **Option B — accept the standard fix as-is:** convert all 9 (10, including `batchJobToUpdateAppointmentStatus`) to `throw`, accepting that one hospital-unit-group's transient failure (SMS gateway timeout, etc.) aborts the rest of that scheduled run for every other unit-group, matching the fail-fast norm applied to every other module in this series. The next scheduled tick (30 min later for most of these jobs) would retry from scratch.
- **Option C — fix the actual architectural gap:** wrap each iteration of the `for (Map.Entry<...> entry : timeZoneAndUnitsMap.entrySet())` loops in `RevertAllocatedQuantity.java` with its own `try`/`catch` (log + continue to the next entry), so a single unit-group's failure is isolated *and* every method can safely `throw` to reach the centralized handler without risking the rest of the run. This is more work (5 loops across `RevertAllocatedQuantity.java` need restructuring) and is itself a genuine behavioral change worth its own `BUSINESS_RULE_MANIFEST.md` entry, but is the only option that gets both centralized visibility *and* continuation.
- **Option A (current interim default):** keep log-only, no throw, as described above — permanently, not just as an interim measure.

No further code changes will be made here until you choose, beyond what's already applied (logging added, behavior otherwise unchanged).

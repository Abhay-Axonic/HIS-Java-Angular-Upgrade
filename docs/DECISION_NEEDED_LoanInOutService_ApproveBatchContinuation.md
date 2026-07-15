# Decision Needed: batch-continuation trade-off for 1 exception-handling finding in `legacy/Procurement`

**Status: RESOLVED FOR THIS BRANCH (2026-07-06) — interim safe default applied, no owner sign-off required for that default. Owner input still wanted on the long-term fail-fast-vs-continue choice.** No behavior change was made to this method's per-item success/failure semantics; only logging was added. See `docs/BUSINESS_RULE_MANIFEST.md`, BR-PENDING-procurement-exceptions, and `docs/REVIEW_procurement-exceptions.md` for how this fits into the rest of the module's fix. This is the same class of trade-off already documented and resolved the same way in `docs/DECISION_NEEDED_HISWAR_BatchContinuation.md`.

## What this is about

`docs/AUDIT_Procurement_exceptions.md` flagged 1 finding in `LoanInOutService.java`'s `approveLoanInOut` method (an empty `catch(Exception e){}` inside a nested per-item loop). Applying this module's standard fix (logger + `throw new ServiceException(...)`) — the same fix correctly applied to the other 514 findings in this module — would have been safe *in isolation*. It is not safe here, because of how this method is structured and called.

## The specific risk

```java
@Override
@Transactional
public Object approveLoanInOut(LoanInOutRequest request)
{
    for (Integer loanInOutId : request.getLoanInOutId()) {
        LoanInOut loanInOut = loanInOutDao.getloanInOutDetails(loanInOutId, request.getUnitId());
        ...
        for (LoanInOutDetail details : loanInOut.getLoanInOutDetails()) {
            if (details.getIsActive() && !details.getIsDeleted()) {
                try {
                    Batch batch = InsertApprovedItems(details, storeId, vendorId, ...);
                    if (batch != null) { loanInOutDao.saveBatch(batch); }
                }
                catch (Exception e) {
                    // finding: empty catch
                }
                details.setStatus(new Status(request.getStatusId()));   // <-- runs regardless of whether the catch fired
                ...
            }
        }
        loanInOut.setStatus(new Status(request.getStatusId()));
        loanInOutDao.updateloanInOut(loanInOut);                        // <-- persists every item's status, good or bad
    }
    CommonResponse response = new CommonResponse();
    response.setMessage("Loan In Out approved successfully");           // <-- unconditional success message
    return response;
}
```

The method is `@Transactional` and processes potentially many `loanInOutId`s and, within each, many line-item `details` in nested loops. Two distinct risks are entangled in this one empty catch:

1. **Today's actual behavior (before any fix):** if `InsertApprovedItems`/`saveBatch` fails for one item, the empty catch swallows it and execution falls straight through to `details.setStatus(new Status(request.getStatusId()))` — the *same* status-approval update that runs on the success path. The failed item is marked approved and persisted via the later `loanInOutDao.updateloanInOut(loanInOut)` call exactly as if its batch/asset-tracking record had been created, and the method still returns "Loan In Out approved successfully" for the whole request. This is a real data-integrity gap (an approved status with no corresponding stock/batch movement) — but converting the bare catch to a mechanical `throw` does not fix that gap; it only changes *how* the failure surfaces, as described next.
2. **If the standard `throw`-based fix were applied as-is:** because the method is `@Transactional`, an uncaught `RuntimeException` (here, `ServiceException`) propagating out of this method rolls back the *entire* transaction — not just the one failing item. Any `loanInOutId`s and items processed earlier in the same request (which had already succeeded) would be rolled back too, and the whole request would fail with no partial approval at all. One bad item in a multi-item batch approval would silently undo every other item's approval in the same call — the exact batch-continuation regression already identified and fixed once in this project for `legacy/HIS-WAR` (commit `6ca3732d`, see `docs/DECISION_NEEDED_HISWAR_BatchContinuation.md`).

Neither "keep silently marking failed items as approved" nor "let one bad item roll back the whole batch" is something this fix should decide unilaterally — this is a genuine trade-off the owner needs to weigh in on, same as the HIS-WAR case.

## What was done (interim, applied without further owner sign-off)

The catch now logs the real exception via `logger.error("approveLoanInOut failed", e)` but does **not** throw — the method's existing behavior (mark the item approved regardless, continue the loop, commit the transaction, return a blanket success message) is otherwise completely unchanged. This is a strict improvement (the failure is now visible in logs for the first time, where previously it vanished with zero trace) with **zero change to the transaction's commit/rollback behavior or the loop's continuation behavior**. This did not need to wait for owner sign-off because it is unambiguously no worse than current production behavior; only the fail-fast alternative below is the genuine trade-off.

## What we still need from you

- **Option A (current interim default):** keep log-only, no throw, as described above — permanently, not just as an interim measure. Failed items keep being marked "approved" with no batch/asset record, but the failure is now visible in the centralized log for the first time, so staff can build a manual reconciliation check.
- **Option B — accept the standard fix as-is:** convert the catch to `throw new ServiceException(...)`, accepting that one item's transient failure (e.g. a Hibernate save error) rolls back the *entire* `approveLoanInOut` call — no items in that request get approved, not just the failing one — matching the fail-fast norm applied to every other finding in this module. The caller would need to retry the whole request after the underlying issue is fixed.
- **Option C — fix the actual data-integrity gap:** stop setting `details.setStatus(...)` for an item whose `InsertApprovedItems`/`saveBatch` failed — i.e. make the per-item status update conditional on that item's batch step having actually succeeded — so a partial failure neither silently fabricates an approved status nor rolls back the other, genuinely-successful items in the same request. This is the option that actually closes the data-integrity gap described in risk #1, but is a larger, distinct behavioral change (changes which items end up in which status after a partial failure) that would need its own `BUSINESS_RULE_MANIFEST.md` entry and is out of scope for this exception-visibility-only fix.

No further code changes will be made here until you choose, beyond what's already applied (logging added, behavior otherwise unchanged).

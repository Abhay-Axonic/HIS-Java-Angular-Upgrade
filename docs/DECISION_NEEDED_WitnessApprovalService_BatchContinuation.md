# Decision Needed: batch-continuation trade-off for 1 exception-handling finding in `legacy/Inventory`

**Status: RESOLVED FOR THIS BRANCH (2026-07-06) — interim safe default applied, no owner sign-off required for that default. Owner input still wanted on the long-term fail-fast-vs-continue choice.** No behavior change was made to this method's per-item success/failure semantics; only logging was added. This is the same class of trade-off already documented and resolved the same way in `docs/DECISION_NEEDED_HISWAR_BatchContinuation.md` and `docs/DECISION_NEEDED_LoanInOutService_ApproveBatchContinuation.md`.

## What this is about

`docs/AUDIT_Inventory_exceptions.md` flagged 1 finding in `WitnessApprovalService.java`'s `updateWitnessApproval` method (an empty `catch(Exception e){}` inside a nested per-item loop). Applying this module's standard fix (logger + `throw new ServiceException(...)`) — the same fix correctly applied to the other 297 findings in this module — would have been safe *in isolation*. It is not safe here, because of how this method is structured and called.

## The specific risk

```java
@Override
@Transactional
public Object updateWitnessApproval(AssestDisposalRequest request) {

    WasteAssetDisposalWorklist wasteAssetDisposalWorklist = WitnessApprovalDao
            .getWasteAssestDisposalWorklist(request.getId());
    com.param.entity.model.inventory.ScrapRequest scrapRequest = ScrapRequestDao.getScrapRequest(...);

    for (WasteAssetDisposalWorklistDetail detail : wasteAssetDisposalWorklist.getWasteAssetDisposalWorklistDetails()) {
        for (ItemRequest item : request.getItems()) {
            if (item.getId() != null && item.getId().equals(detail.getId())) {
                detail.updateWasteAssetDisposalWorklistDetail(...);   // <-- item marked updated regardless of what follows

                try {
                    allocateItemQuantity(item.getBatchId(), item.getApprovedDisposedQuantity(), screenId, request,
                            detail.getAssetDisposalWorklistRequestDetail().getScrapRequestDetail(), detail);
                } catch (Exception e) {
                    // finding: empty catch
                }
            }
        }
    }
    scrapRequest.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
    scrapRequest.setApprovalStatus(new Status((com.param.common.constants.Status.Approved.getStatus())));
    ...
}
```

The method is `@Transactional` and processes potentially many `detail`/`item` pairs in a nested loop. Two distinct risks are entangled in this one empty catch:

1. **Today's actual behavior (before any fix):** if `allocateItemQuantity` (which reduces store stock for the disposed batch) fails for one item, the empty catch swallows it silently and the loop moves on to the next item. The `detail.updateWasteAssetDisposalWorklistDetail(...)` call just above the `try` has already run regardless, so the disposal-worklist line item is marked with its approved/rejected quantity and status even though the corresponding stock-quantity deduction never happened — a real data-integrity gap (an approved disposal record with no matching stock movement), same shape as the Procurement `LoanInOutService` case. Converting the bare catch to a mechanical `throw` does not fix that gap; it only changes how the failure surfaces, as described next.
2. **If the standard `throw`-based fix were applied as-is:** because the method is `@Transactional`, an uncaught `RuntimeException` (here, `ServiceException`) propagating out of this method rolls back the *entire* transaction — not just the one failing item. Every other item/detail in the same `updateWitnessApproval` call (which may have already succeeded) would be rolled back too, and the whole scrap-witness-approval request would fail with no partial approval at all. One item's transient stock-allocation failure in a multi-item batch would silently undo every other item's approval in the same call — the same batch-continuation regression already identified and resolved for `legacy/HIS-WAR` (commit `6ca3732d`) and `legacy/Procurement`'s `LoanInOutService`.

Neither "keep silently leaving worklist details marked approved with no matching stock deduction" nor "let one item's failure roll back the whole witness-approval batch" is something this fix should decide unilaterally — this is a genuine trade-off the owner needs to weigh in on, same as the prior two cases.

## What was done (interim, applied without further owner sign-off)

The catch now logs the real exception via `logger.error("allocateItemQuantity failed", e)` but does **not** throw — the method's existing behavior (mark the item's worklist detail updated regardless, continue the loop, commit the transaction) is otherwise completely unchanged. This is a strict improvement (the failure is now visible in logs for the first time, where previously it vanished with zero trace) with **zero change to the transaction's commit/rollback behavior or the loop's continuation behavior**. This did not need to wait for owner sign-off because it is unambiguously no worse than current production behavior; only the fail-fast alternative below is the genuine trade-off.

## What we still need from you

- **Option A (current interim default):** keep log-only, no throw, as described above — permanently, not just as an interim measure. Failed items keep being marked "updated" in the worklist with no matching stock deduction, but the failure is now visible in the centralized log for the first time, so staff can build a manual reconciliation check.
- **Option B — accept the standard fix as-is:** convert the catch to `throw new ServiceException(...)`, accepting that one item's transient failure (e.g. a Hibernate save error during stock allocation) rolls back the *entire* `updateWitnessApproval` call — no items in that request get approved, not just the failing one — matching the fail-fast norm applied to every other finding in this module. The caller would need to retry the whole request after the underlying issue is fixed.
- **Option C — fix the actual data-integrity gap:** stop calling `detail.updateWasteAssetDisposalWorklistDetail(...)` (or otherwise reorder the method) so a worklist detail is only marked updated once its `allocateItemQuantity` step has actually succeeded — so a partial failure neither silently fabricates an updated/approved detail nor rolls back the other, genuinely-successful items in the same request. This is the option that actually closes the data-integrity gap described in risk #1, but is a larger, distinct behavioral change that would need its own `BUSINESS_RULE_MANIFEST.md` entry and is out of scope for this exception-visibility-only fix.

No further code changes will be made here until you choose, beyond what's already applied (logging added, behavior otherwise unchanged).

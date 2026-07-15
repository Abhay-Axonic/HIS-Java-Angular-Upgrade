# Decision Needed: 2 billing-critical findings where an exception is swallowed and a blank, no-status `CommonResponse` is returned as if nothing happened

**Status: RESOLVED — owner approved Option A (2026-07-06).** Applied the standard fix to both methods in `SalesReturnService.java`: `cancelsalesReturnById`'s empty `catch (Exception e) {}` and `updateSalesReturnById`'s `catch (Exception ex) { ex.printStackTrace(); }` were both replaced with `logger.error(<var>.getMessage(), <var>); throw new ServiceException(<var>.getMessage());`, routing to the centralized `BillingExceptionController` (BR-004) as a real, logged HTTP 500 instead of a blank 200. Classified Behavioral Change per CLAUDE.md (the failure-path response the caller receives is no longer byte-identical), consistent with the rest of this fix cycle. See the consolidated `BR-PENDING-pharmacy-exceptions` manifest entry for build verification.

---

Original request, preserved for record:

**Status: OPEN — awaiting owner decision.** No code has been touched for these 2 findings. They were surfaced during the Pharmacy-module billing-critical exception-handling fix (`docs/AUDIT_Pharmacy_exceptions.md`, `docs/REVIEW_pharmacy-exceptions.md`) and deliberately excluded from that fix's routine batch — this is the closest thing in the Pharmacy audit to the fabricated-success-on-exception pattern the task specifically asked us to watch for (the same class of issue as the held-back `collectPayment`/`RazorPayment` cases in Billing and `UnitServiceTariffMasterDaoImpl` in Global).

---

**Locations (both in `legacy/Pharmacy/src/main/java/com/param/pharmacy/op/service/salesReturn/SalesReturnService.java`, both billing-critical):**

- Line 1071 (audit finding #565), method `cancelsalesReturnById` — cancels a pharmacy sales return.
- Line 1211 (audit finding #566, classified `other`/printStackTrace-only by the structural parser since the fallback `return` sits outside the catch body), method `updateSalesReturnById` — updates a pharmacy sales return, including its paid amount and stock/batch reconciliation.

## What happens today

`cancelsalesReturnById`:

```java
public CommonResponse cancelsalesReturnById(Integer id, Integer StoreId, Integer unitId) {

    CommonResponse response = new CommonResponse();
    try {
        Return returnObject = salesReturnDao.salesReturnById(id, unitId);
        List<ReturnDetail> returnList = returnObject.getReturnDetailList();
        returnList.forEach(item -> item.setReturnQty(0));

        returnObject.setTotalReturnAmt(0.0);
        returnObject.setReturnDetailList(returnList);

        returnObject.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
        salesReturnDao.salesReturnUpdate(returnObject);

        response.setId(returnObject.getId().longValue());
        response.setMessage("Return cancel successfully");

    } catch (Exception e) {

    }
    return response;
}
```

`updateSalesReturnById`:

```java
public CommonResponse updateSalesReturnById(RequestSaleReturnSave request, Integer id, Integer unitId) throws Exception{
    CommonResponse response = new CommonResponse();
    try {
        Return returnObject = salesReturnDao.salesReturnById(id, unitId);
        returnObject.setStatus(new Status(request.getStatusId()));
        ... TotalreturnPaidAmount validation, updateStoreStockandBatch(...), salesReturnDao.salesReturnUpdate(returnObject) ...
        response.setId(returnObject.getId().longValue());
        response.setMessage("Return updated successfully");

    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return response;
}
```

In both methods, `response` is a `CommonResponse` instantiated **before** the try block, with none of its fields set. `CommonResponse` (`legacy/Entity/src/main/java/com/param/common/dto/CommonResponse.java`) is annotated `@JsonInclude(Include.NON_NULL)` and its `status`/`id`/`message` fields all default to `null` — they are only ever set on the success path, inside the try, *after* the operation that can fail. If `salesReturnDao.salesReturnUpdate(...)` (or, in `updateSalesReturnById`'s case, `updateStoreStockandBatch(...)`, which itself writes store-stock and batch quantities) throws for any reason, the catch swallows it — silently in `cancelsalesReturnById`, to a console-only `printStackTrace()` in `updateSalesReturnById` — and the method returns the same untouched, blank `response`. Serialized to JSON, that is `{}` with no error field at all, returned with an HTTP 200 by the calling controller (`SalesReturnController`). A caller or frontend that only checks "did the response come back and is it non-empty of errors" — rather than specifically checking for a `"Return cancel successfully"`/`"Return updated successfully"` message — could read this as a successful cancel/update when the return was never actually cancelled or updated in the database.

Note also, for `updateSalesReturnById` specifically: this method is `@Transactional`, and because the exception is caught inside the method rather than allowed to propagate, Spring's transaction interceptor never sees it — meaning if `updateStoreStockandBatch` succeeds (mutating store stock/batch rows) but the subsequent `salesReturnDao.salesReturnUpdate(returnObject)` fails (or vice versa), the partial writes may not roll back as they would if the exception had propagated normally. This is a related but separate correctness concern from the blank-response issue itself, surfaced here for completeness.

## What the standard fix (used everywhere else in this fix cycle) would change

The standard fix replaces a catch block's contents with `logger.error(...); throw new ServiceException(e.getMessage())`, which would make both methods' failure paths return a real, logged HTTP 500 instead of a blank 200 — closing the masked-success gap described above. Unlike the null-probe-idiom and loyalty-goto findings held back elsewhere in this cycle, applying the standard fix here would **not** introduce a new regression — both methods already only reach their success-message lines after every write has succeeded, so nothing here is "expected to throw" as normal control flow.

## Why this needs owner judgment rather than a mechanical, unannounced fix

We are flagging this rather than silently including it in the routine batch because the practical effect is the same class of "does the caller's idea of success change" question the owner has weighed in on before (`docs/DECISION_NEEDED_collectPayment.md`, `docs/DECISION_NEEDED_RazorPayment.md`): today, some caller of these two endpoints may already be relying on "got a 200 back with no explicit error" as its success signal (however fragile that is) for a cancel/update-return flow. Converting that silently-masked outcome into a hard HTTP 500 is a real behavioral change in what the caller sees on failure — smaller in scope than a payment-collection asymmetry, since no *fabricated success message* is ever returned (the response is blank, not literally "successful"), but still deserving the owner's explicit sign-off before we change what a failed pharmacy-return cancel/update looks like over the wire, on a billing-critical flow.

## What we need from you

A decision on one of the following (or your own alternative), which can be applied identically to both methods since they share the same shape:

- **Option A (standard fix):** Apply the same fix as the rest of this cycle — `logger.error(e.getMessage(), e); throw new ServiceException(e.getMessage());` in place of the swallow/printStackTrace, routing to `BillingExceptionController` (BR-004) as a real, logged HTTP 500. This closes the masked-success gap outright.
- **Option B:** Keep the exact current return-blank-response-on-failure behavior for callers, but add real logging (`logger.error(...)`) so the failure is at least visible in the centralized log for the first time, without changing what the caller receives.
- **Option C:** Something else — for example, explicitly setting an error status/message on `response` before returning it in the catch (e.g. `response.setStatus(ERROR); response.setMessage("Return cancel failed");`), which would surface a real error to the caller without going as far as a hard 500.

No code will be touched here until you choose.

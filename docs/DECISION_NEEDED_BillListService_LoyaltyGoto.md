# Decision Needed: 1 exception-handling finding using a self-manufactured exception as goto-style control flow (loyalty-card validation)

**Status: RESOLVED — owner approved Option C (2026-07-06).** Applied: the self-manufactured `throw new Exception()` / `catch(Exception e)` construct in `buildSalePaymentModes` was replaced with a direct `return null;` and no try/catch at all. Output is provably identical for every input — this was internal-structure cleanup only, not a Behavioral Change. See the consolidated `BR-PENDING-pharmacy-exceptions` manifest entry for build verification.

---

Original request, preserved for record:

**Status: OPEN — awaiting owner decision.** No code has been touched for this finding. It was surfaced during the Pharmacy-module billing-critical exception-handling fix (`docs/AUDIT_Pharmacy_exceptions.md`, `docs/REVIEW_pharmacy-exceptions.md`) and deliberately excluded from that fix's routine batch. This is the same construct already resolved for the Global module in `docs/DECISION_NEEDED_LoyaltyCardValidationGoto.md` (BR-021, Option C approved) — the owner's decision there is directly applicable precedent, but this is a separate finding in a separate module/file and needs its own sign-off.

---

**Location:** `legacy/Pharmacy/src/main/java/com/param/pharmacy/op/service/billList/BillListService.java`, private method `buildSalePaymentModes` (called from `saveBillDetails`), line 724 (audit finding #560, billing-critical, classified `status-masks-failure`).

Combined with the sibling `BillListService.java:267` finding (the "null-probe idiom," see `docs/DECISION_NEEDED_Pharmacy_NullProbeIdiom.md`), this is one of `BillListService.java`'s only 2 audited findings — the file therefore has **zero** findings receiving the standard mechanical fix in this cycle.

## What happens today

Inside `buildSalePaymentModes`, in the loyalty-card payment-mode branch (`paymentModeId == 6`):

```java
Response loyalityCardResponse = iPatientLoyaltyCardMapperDao.getPatientDetailsByLoyaltiCardNo(dto);
if(loyalityCardResponse.getListObject().size() >0 && loyalityCardResponse.getListObject()!=null){
    PatientLoyaltyCardMapperDto patientLoyaltyCardMapperDto=(PatientLoyaltyCardMapperDto)loyalityCardResponse.getListObject().get(0);
    //checking that front end amount should be less than or equal to card amount
    if(patientLoyaltyCardMapperDto.getCardAmount()>=paymentDetail.getAmount())
        {
        patientLoyaltyCardMapperDto.setConsumeAmount(paymentDetail.getAmount());
        iPatientLoyaltyCardMapperDao.updateLoyaltyCardAmountByCard(dto);
        mailList.add(patientLoyaltyCardMapperDto);
    }else{
        try{
            throw new Exception();
        }catch(Exception e){

            return null;
        }
    }
}
```

`buildSalePaymentModes` returns `List<SalePaymentMode>`. Its only caller, `saveBillDetails` (lines 585–615), does:

```java
List<SalePaymentMode> salePaymentModes = buildSalePaymentModes(request, sale);
if(salePaymentModes !=null ) {
    sale.setSalePaymentModes(salePaymentModes);
    BillListDao.saveBill(sale);
    ...
    response.setMessage("Sale Details saved successfully");
    return response;
}else {
    CommonResponse response = new CommonResponse();
    response.setMessage("Invalid Transaction ID ");
    return response;
}
```

So today, when the loyalty-card amount on file is less than the amount being charged, `buildSalePaymentModes` self-throws and immediately catches a bare `new Exception()` purely to `return null;`, which routes the caller into the `else` branch and returns `CommonResponse{message: "Invalid Transaction ID "}` — an honest (if oddly worded) error outcome, not a fabricated success. There is no real exception here; `new Exception()` carries no information, and the caught variable `e` is never referenced.

## What the standard fix (used everywhere else in this fix cycle) would change

The standard fix replaces a catch block's contents with `logger.error(...); throw new ServiceException(e.getMessage());`. Applied here literally, that would turn a routine, expected business-validation outcome ("this loyalty card doesn't have enough balance for this line item") into an unhandled `ServiceException` propagating out of `saveBillDetails` — an HTTP 500 on a live bill-save flow, replacing today's honest (if generically worded) "Invalid Transaction ID" response with what looks like a server error, and losing the caller's ability to distinguish this specific case from an actual unexpected failure.

## Why this needs owner judgment rather than a mechanical fix

Same reasoning as the already-resolved Global-module precedent (BR-021, `docs/DECISION_NEEDED_LoyaltyCardValidationGoto.md`): this is a self-manufactured exception used as a goto/break device for a legitimate business-validation branch, not a genuine silent-failure bug. The mechanical fix would degrade a currently-correct (if awkward) outcome rather than add visibility to a hidden one, on a billing-critical, live bill-save path.

## What we need from you

A decision on one of the following (or your own alternative):

- **Option A:** Leave the code exactly as it is today — it already returns an honest error outcome via the caller's `null`-check branch, so this may not need any change beyond acknowledging it in the manifest as a reviewed non-issue.
- **Option B:** Add real logging only (e.g. `logger.error("buildSalePaymentModes: loyalty card balance insufficient for card " + dto.getCardNo(), ...)`, not relying on `e.getMessage()`), so the validation event is visible in the centralized log, while keeping the exact same `return null;` (and therefore the same `"Invalid Transaction ID "` response to the caller) unchanged.
- **Option C (matches the Global-module BR-021 precedent):** Refactor away the self-manufactured `throw`/`catch` entirely — replace it with a direct `return null;` and no try/catch at all, since no real exception is involved. Output is provably identical for every input; this is internal-structure cleanup only, not a Behavioral Change, and would not need this decision doc at all if that's the direction chosen.
- **Option D:** Something else — for example, giving the caller a more specific message than "Invalid Transaction ID" for this particular case (a genuine Behavioral Change requiring its own sign-off, since the response text would differ from today's).

No code will be touched here until you choose.

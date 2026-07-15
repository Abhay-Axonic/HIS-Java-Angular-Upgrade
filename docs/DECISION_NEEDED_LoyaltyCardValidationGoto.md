# Decision Needed: 2 exception-handling findings using a self-manufactured exception as goto-style control flow (loyalty-card validation)

**Status: RESOLVED 2026-07-05 — Owner approved Option C.** In both `DepositMasterServiceImpl.addDeposit` and `FrenchiseDepositServiceImpl.addFrenchiseDeposit`, the self-manufactured `throw new Exception()` / `catch(Exception e)` construct was replaced with a direct `return new Response(ERROR, ERROR_CODE, "Amount should be less than or equals to card amount", null, 0.0);` — no try/catch remains. Output is provably identical to the prior behavior (same Response object, same message, same status) for every input; this is internal-structure cleanup only, not a Behavioral Change. See `docs/BUSINESS_RULE_MANIFEST.md` (`BR-PENDING-global-billing-critical`) for the updated consolidated entry.

---

**Status: OPEN — awaiting owner decision.** No code has been touched for these 2 findings. They were surfaced during the Global-module billing-critical exception-handling fix (`docs/AUDIT_Global_exceptions.md`, `docs/REVIEW_global-billing-critical.md`) and deliberately excluded from that fix's routine batch, matching the same held-back treatment already used for `docs/DECISION_NEEDED_collectPayment.md` and `docs/DECISION_NEEDED_RazorPayment.md`.

---

**Locations (identical shape in two sibling files):**

- `legacy/Global/src/main/java/com/param/global/billing/service/DepositMasterServiceImpl.java`, method `addDeposit`, lines 227–233 (patient deposit collection)
- `legacy/Global/src/main/java/com/param/global/billing/service/FrenchiseDepositServiceImpl.java`, method `addFrenchiseDeposit`, same shape (franchise deposit collection)

Both methods have already saved the deposit master record (money/state already committed — `iDepositMasterDao.saveDeposit(...)` / `iFrenchiseDepositDao.addFrenchiseDeposit(...)` runs earlier in the same method) before reaching the code below, which validates a secondary, optional loyalty-card payment line.

## What happens today

`DepositMasterServiceImpl.addDeposit`, inside the loop over payment lines, payment mode 6 (loyalty card):

```java
if(patientLoyaltyCardMapperDto.getCardAmount()>=detailsMPesaDto.getAmount())
    {
    patientLoyaltyCardMapperDto.setConsumeAmount(detailsMPesaDto.getAmount());
    iPatientLoyaltyCardMapperDao.updateLoyaltyCardAmountByCard(dto);
    mailList.add(patientLoyaltyCardMapperDto);
}else{
    try{
        throw new Exception();
    }catch(Exception e){

        return new Response(ERROR, ERROR_CODE, "Amount should be less than or equals to card amount", null, 0.0);
    }
}
```

`FrenchiseDepositServiceImpl.addFrenchiseDeposit` has the identical construct for the same loyalty-card-amount check.

This is not a genuine "a risky call might throw" catch — it is a self-manufactured `throw new Exception()` used purely as a control-flow device to break out of nested `if` blocks and immediately return a specific, hardcoded business-validation message: *"Amount should be less than or equals to card amount."* There is no real exception here; `new Exception()` has a `null` message by construction. The audit's automated parser correctly flagged this catch as a finding (the caught variable `e` is never referenced), but it does not fit the "silent swallow" shape the standard fix targets — today the caller already correctly sees `status = ERROR` with a specific, honest, human-readable validation message (not a fabricated success, and not a silent failure).

## What the standard fix (used everywhere else in this fix cycle) would change

The standard fix replaces a catch block's contents with `logger.error(...); throw new BillingException(e.getMessage());`, which routes the failure to `BillingExceptionController` (BR-004) and returns a real, logged HTTP 500.

Applied here literally, that would mean:

1. **The specific validation message would be silently destroyed.** `e.getMessage()` on `new Exception()` is `null` — the caller would receive a generic billing error instead of "Amount should be less than or equals to card amount," a clear regression in the user-facing error text for an ordinary, expected business validation (not an unexpected failure).
2. **The response shape/HTTP semantics would change.** Today this returns a normal `Response` object with an ERROR status and message (handled as a routine validation failure by the frontend). After the fix, it would surface as a thrown `BillingException`, caught by each method's own outer catch-all further down, and ultimately returned as an HTTP 500 via `BillingExceptionController` — turning a routine "insufficient card balance, please re-enter" validation into what looks like a server error.

## Why this needs owner judgment rather than a mechanical fix

Every other finding fixed in this cycle was either a genuinely silent failure (empty catch, `printStackTrace`-only) or a masked-but-honest generic error response with no fabricated success. This case is different: applying the mechanical fix would not add visibility to a hidden problem — it would take a currently-correct, deliberate, specific validation outcome and degrade it into a generic exception with a lost message and a wrong-looking HTTP status, on a live deposit-collection screen used for every patient/franchise deposit with a loyalty-card payment line. Per CLAUDE.md, changing this response's message or shape is itself a Behavioral Change requiring sign-off, even though the underlying "swallow" pattern is what triggered the audit finding.

## What we need from you

A decision on one of the following (or your own alternative), to be applied identically to both files since they share the exact same construct:

- **Option A:** Leave the code exactly as it is today (no logging, no throw) — since it already returns an honest, specific ERROR response and nothing is actually silently lost, this may not need any change at all beyond acknowledging it in the manifest as a reviewed non-issue.
- **Option B:** Add real logging only (`logger.error("addDeposit: loyalty card amount validation failed for card " + dto.getCardNo(), ...)` or similar, without relying on `e.getMessage()`), so the validation event is visible in the centralized log for pattern-tracking purposes, while keeping the exact same `Response(ERROR, ERROR_CODE, "Amount should be less than or equals to card amount", null, 0.0)` returned to the caller unchanged.
- **Option C:** Refactor away the self-manufactured `throw`/`catch` entirely (replace it with a direct `return new Response(ERROR, ...)` with no try/catch at all, since no real exception is involved) — a pure internal-structure cleanup with provably identical output, not a behavioral change, and therefore not something requiring this decision doc at all if that's the direction chosen.
- **Option D:** Something else.

No code will be touched here until you choose.

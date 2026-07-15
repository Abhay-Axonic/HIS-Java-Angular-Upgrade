# Review: RazorPaymentController.java rupee→paise conversion fix

*Branch: `fix/razorpay-amount-conversion-bug`. Not merged to master. This is a financial-accuracy fix (Behavioral Change) — the output amount sent to Razorpay changes for any input whose paise component is a multiple of 10 (e.g. ₹450.50), from an incorrect 10x-undercharge to the correct value. Owner Decision: Pending (see BR entry in `docs/BUSINESS_RULE_MANIFEST.md`).*

## What was wrong

Two methods in `legacy/Billing/src/main/java/com/param/billing/global/controller/RazorPaymentController.java` converted a rupee amount to paise (the smallest currency unit Razorpay's API requires) by taking the string representation of the amount, splitting it on the decimal point, and concatenating the two halves together — instead of multiplying by 100. `Double.toString()` produces the *shortest* decimal representation that round-trips back to the same double, so an amount like `450.50` becomes the string `"450.5"` (one fractional digit, not two). Concatenating `"450"` and `"5"` gives `"4505"` — the code then sent Razorpay **4505 paise (₹45.05)** instead of the correct **45050 paise (₹450.50)**: a 10x undercharge, silently, on any amount ending in `.10`–`.90`.

## Sites fixed

Both were byte-for-byte identical in logic, confirmed via a full-file grep before fixing (no other occurrence of this pattern exists anywhere in the file).

### Site 1 — `razorPayPaymentLink` (originally lines 398–409)

**Before:**
```java
String s = reqDto.getRazorAmount().toString();

Integer manupulateAmount = 0;
String[] array =  s.split("\\.");
String SecondData = "0";
if(array.length > 1 && !array[1].equals(SecondData)) {
	s = array[0]+array[1];
	manupulateAmount = Integer.parseInt(s);
}else {
	s = array[0]+"00";
	manupulateAmount = Integer.parseInt(s);
}
```

**After:**
```java
BigDecimal razorAmountInRupees = BigDecimal.valueOf(reqDto.getRazorAmount());
Integer manupulateAmount = razorAmountInRupees.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).intValue();
```

### Site 2 — `getOrderIdForRazorPayPaymentOnBilling` (originally lines 508–519)

Identical before/after, at the method's shallower indentation level. Same replacement applied.

### Imports added

```java
import java.math.BigDecimal;
import java.math.RoundingMode;
```

No other line in the file was touched. `manupulateAmount` keeps its original name and `Integer` type so both downstream consumers (`Mpesa.RazorPaytransactionStatus(...)` at site 1, `orderRequest.put("amount", manupulateAmount)` at site 2) are unaffected by anything except the corrected value now flowing into them.

## Why this approach

- `reqDto.getRazorAmount()` returns a boxed `Double` (`RazorpayPaymentReqDto.java:39`). Per CLAUDE.md/RULES.md's billing-accuracy priority, currency math must never be done in `double`/`float` — binary floating point cannot represent most decimal fractions exactly, and direct `double` arithmetic on money is the same root-cause class of bug already flagged project-wide in `docs/AUDIT_BillingRisk.md` (RISK-1). `BigDecimal.valueOf(double)` is used (not `new BigDecimal(double)`) specifically because it goes through `Double.toString()` first, giving the intended decimal value rather than the raw binary floating-point noise `new BigDecimal(double)` would carry in (the same noise-carrying-constructor anti-pattern flagged in `docs/AUDIT_BillingRisk.md`'s one-off 5).
- `.setScale(0, RoundingMode.HALF_UP)` rounds to the nearest whole paisa, matching ordinary "round half up" currency convention (e.g. ₹0.005 rounds up), rather than silently truncating a fractional paisa.
- `.intValue()` is safe here only because the preceding `setScale(0, ...)` guarantees no fractional part remains — this is a widening/exact conversion for any realistic bill amount, not a truncation.

## Worked example — ₹450.50, reproduced end to end

Independently compiled and ran the exact before/after logic (not just reasoned about it), comparing both against the mathematically correct expected value for a range of inputs, including the original bug report's example:

| Amount (₹) | Old buggy result (paise) | New fixed result (paise) | Correct value (paise) | Fixed matches correct? |
|---|---|---|---|---|
| 450.50 | 4505 (**₹45.05 — 10x undercharge**) | **45050** | 45050 | ✅ |
| 150.50 | 1505 (₹15.05) | **15050** | 15050 | ✅ |
| 200.10 | 2001 (₹20.01) | **20010** | 20010 | ✅ |
| 99.90 | 999 (₹9.99) | **9990** | 9990 | ✅ |
| 5.90 | 59 (₹0.59) | **590** | 590 | ✅ |
| 123.45 (already correct before) | 12345 | 12345 | 12345 | ✅ |
| 123.00 (already correct before) | 12300 | 12300 | 12300 | ✅ |
| 1000.00 (already correct before) | 100000 | 100000 | 100000 | ✅ |
| 0.05 (edge case, tiny amount) | 5 | 5 | 5 | ✅ |
| 999999.99 (large amount) | 99999999 | 99999999 | 99999999 | ✅ |

**Confirmation this is a financial-accuracy fix:** the ₹450.50 case is the concrete example from the original bug report. Before this fix, a patient billed ₹450.50 through `razorPayPaymentLink` or `getOrderIdForRazorPayPaymentOnBilling` would have a Razorpay order/payment-link created for **₹45.05 — one tenth of the correct amount** — a real, live undercharge, not a rounding artifact. After this fix, the same input produces the correct ₹450.50 order amount. The fix changes observable output (the amount charged) for a defined class of inputs, and per RULES.md is therefore logged as a Behavioral Change requiring owner sign-off, even though the "old" output was demonstrably wrong — the owner still decides whether/how this ships (e.g., whether any historical payment-link records created via this bug need reconciliation, which is outside the scope of this code fix).

## Verification

- `mvn -o -pl Billing -am clean compile` (run from `legacy/`): **BUILD SUCCESS** (Entity → Global → Billing, no errors).
- Full-file grep for `split("\\.")` in `RazorPaymentController.java` confirms zero remaining occurrences of the buggy pattern, and confirms it never existed at any third site.
- Standalone reproduction (compiled and run outside Maven, comparing old logic vs. new logic vs. mathematically correct value) confirms the fix is correct across boundary cases (round numbers, two-decimal amounts, sub-rupee amounts, and large amounts), not just the reported example.

## Not in scope of this fix

- The `getOrderIdForRazorPayPayment` and `getOrderIdUnitSubscriptionPay` endpoints in the same file take a pre-converted `Integer` amount directly from the client (`reqDto.getRazorPayBillAmount()`) rather than converting a rupee amount themselves — per `docs/AUDIT_BillingRisk.md`, no server-side conversion happens there at all (a different, unvalidated-client-trusted-amount risk, not the string-split bug this fix addresses). Not touched here.
- No historical Razorpay transactions are reconciled or corrected by this fix — it only prevents the bug from recurring going forward.

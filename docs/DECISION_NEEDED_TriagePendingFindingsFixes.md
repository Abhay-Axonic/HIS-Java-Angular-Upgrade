# Decision needed: proposed fixes for the 6 triage findings (8 sub-items)

*Blueprint (docs only, no code applied). 2026-07-15. Covers every item in `docs/TRIAGE_PendingFindings.md`, after the owner reviewed all 6 findings one at a time and decided "fix now" on every one. Same process as `docs/DECISION_NEEDED_GrnNullTaxReferenceCrash.md`: this document proposes the minimal change for each item; nothing is applied until the owner approves each one individually. Two items (2a, 4b) cannot be proposed at all yet — they're blocked on an answer only the owner (or their tax/domain expert) can give, and are written up as open questions rather than guessed fixes, per the owner's explicit instruction.*

---

## 1. Patient bill vs. insurance disagreement on second-round contracts (touches money math)

**The problem, in one sentence:** `BillingCommonMethods.updatePreAuthAndServiceCapAmountForSecondContract` correctly computes an adjusted co-pay/pre-auth-capped amount and writes it to the database, but never passes that adjustment back to the caller, so the patient's own bill total and the insurance/cap ledger can disagree.

**Where, precisely:** `legacy/Global/src/main/java/com/param/global/common/BillingCommonMethods.java:318-360`. The method receives `totalSelfPayable`/`totalCreditPayable` as `Double` parameters (pass-by-value), adjusts local copies internally (co-share at lines 322-327, pre-auth cap at lines 329-339), writes the adjusted credit amount to the database (lines 338, 345, 350/352), then returns `consumptionDto` — the same object passed in — **without ever calling `.setSelfPayable(...)`/`.setCreaditPayable(...)` on it**. Contrast with its sibling method three methods up, `updatePreAuthAndServiceCapAmountForFirstPriorityContract` (line 276), which builds a fresh `ContractConsumptionDto dto = new ContractConsumptionDto()` and explicitly sets both fields on it before returning (lines 311-312) — the established, correct pattern in this same file.

**All 7 call sites confirmed** (`legacy/Global/src/main/java/com/param/global/billing/service/ContractConsumptionServiceImpl.java`, lines 961, 1025, 1107, 2433, 2471, 2369, 2997): every one already captures the return value into a variable named `creditAndSelfAmountDto` — but that variable is **never read afterward** at any of the 7 sites. Each call is immediately followed by a large block of commented-out dead code that turns out to be the verbatim pre-refactor original of the method's own co-share/pre-auth/cap-update logic — confirming these are leftover originals, not an abandoned attempted fix.

**The proposed fix — two parts, both required:**

*Part A — inside the method*, before `return consumptionDto;` (currently line 359), add:
```java
consumptionDto.setSelfPayable(totalSelfPayable);
consumptionDto.setCreaditPayable(totalCreditPayable);
```
using the method's own already-adjusted local `totalSelfPayable`/`totalCreditPayable` variables — the same values already being used for the database writes a few lines above, just also stamped onto the return object.

*Part B — at each of the 7 call sites*, read the returned totals back:
```java
totalSelfPayable = creditAndSelfAmountDto.getSelfPayable();
totalCreditPayable = creditAndSelfAmountDto.getCreaditPayable();
```

**A critical detail that makes this different from copy-pasting the sibling method's fix:** the sibling method's callers use `totalCreditPayable = totalCreditPayable + creditAndSelfAmountDto.getCreaditPayable();` (accumulation), because that method's parameters represent a single round's delta. This method is different — it receives the caller's *entire running total* and returns the *entire adjusted total*, not a delta. Using accumulation (`+=`) here instead of assignment (`=`) would **double-count** the adjustment. Confirmed by tracing all 7 call sites individually: at 4 sites (961, 1025, 2369, 2997) the caller pre-computes a base cap-split into `totalSelfPayable`/`totalCreditPayable` just before the call; at the other 3 (1107, 2433, 2471) the caller resets them to an absolute baseline (e.g. `totalSelfPayable = 0.0`) just before the call. None of the 7 pre-call blocks duplicate the method's *internal* co-share/pre-auth adjustment, so none need to be removed — but the read-back at all 7 sites must be a plain assignment, not `+=`.

**What this fix deliberately does NOT do:**
- It does not change the sibling `...FirstPriorityContract` method or its callers (already correct).
- It does not remove or alter any of the 7 call sites' pre-call base-split/reset logic — confirmed safe to keep, based on the point above.
- It does not delete the dead commented-out code blocks after each call site — left in place unless the owner wants a separate cleanup pass; removing them is optional and does not change behavior either way.
- It does not attempt to determine, from real billing history, how often this actually fires — that data access is separately on hold per the owner's standing decision.

**One anomaly worth flagging, not fixing:** in the dead code following the call at line 2997, one line references `searchResDto.isApplyAuth()` where every other equivalent block references `consumptionDto.isApplyAuth()`. Since this is inside a commented-out block, it has zero effect on current behavior — noted only because it hints the 7 call sites may not have all been refactored from identical historical states, in case that context matters to whoever reviews this.

**Classification, per RULES.md:** Financial Correctness / Behavioral Change. This changes an actual dollar amount that ends up on a patient's bill and in the insurance/cap ledger for the specific case of a second-round contract with a co-pay% or a hit pre-auth ceiling — not a byte-identical refactor.

**Verification plan, if approved:** Given the money-math risk and that this touches 7 call sites, this one needs more than the usual pass:
1. Apply Part A and Part B together (a partial fix would be worse than no fix — e.g. Part A alone with no caller read-back changes nothing observable; Part B alone would read back an always-empty adjustment).
2. Update `BillingCommonMethodsCharacterizationTest` — the existing 2 passing assertions that pin the current (buggy) behavior will need to be replaced with assertions that the returned `consumptionDto` now carries the adjusted totals.
3. Add a new test per distinct call-site shape (base-split sites vs. absolute-reset sites) in `ContractConsumptionServiceImpl`, since the 7 sites aren't identical — at minimum one test representative of each of the two shapes described above.
4. Full `mvn -pl Global test` clean run.
5. Given this is the one finding I'd genuinely want a second pair of eyes on before it ships (per my earlier triage recommendation) — I'd suggest a manual read-through of the diff against this document's Part B assignment-not-accumulation rule at all 7 sites specifically, since a single `+=` slipping in at just one of the 7 would silently reintroduce a subtler version of the same bug.

---

## 2a. Tax calculated differently for goods coming in vs. goods going out — BLOCKED pending a tax/accounting answer

**Status: cannot be proposed yet.** This is not a code question — whether tax should be calculated before or after a discount is netted out is a tax/GST compliance rule, and I have no way to determine which behavior is correct.

**The facts, for whoever answers this:**

Goods **receipt** (Procurement), `legacy/Procurement/src/main/java/com/param/procurement/purchase/service/goodReceiptNote/GoodReceiptNoteService.java:1750`:
```java
Double commTaxAmount = detail.getTotalAmount() - (detail.getTotalDiscount()!=null ? detail.getTotalDiscount() :0);
```
Discount is subtracted first; tax is calculated on the net amount.

Goods **return** (Inventory), `legacy/Inventory/src/main/java/com/param/inventory/store/service/returnableGatepass/ReturnableGatepassService.java:1012`:
```java
Double natAmount = detail.getTotalAmt();
```
No discount subtraction at all — tax is calculated on the full amount, even though `detail` (a `ReturnableGatepassDetail`) has its own discount fields available, just unused here.

Also worth noting for context: a commented-out line still sitting in the receipt-side code (`GoodReceiptNoteService.java`, just above line 1750) shows this exact calculation was changed at some point for a specific client ("HIS-22584 Orthocare Hospital :- Tally integration"), switching from `detail.getDiscount()` to `detail.getTotalDiscount()`. That history might be relevant to whoever gives the tax-rule answer — it suggests this formula has already been client-specific-tuned once before.

**What I need from you (or your tax/accounting side) before I can write a real proposal:** should tax on a goods *return* be calculated on the discounted amount (matching receipts) or the full amount (current return behavior)? Once that's answered, the fix itself is a one-line, low-risk change to whichever side is wrong — mirroring the two-line pattern already proven safe in the GRN crash fix.

---

## 2b. Deleted line items still processed during goods-receipt approval (no money math)

**The problem, in one sentence:** goods-return processing correctly skips line items marked deleted; the equivalent goods-receipt approval does not, so a deleted line item still gets tax components calculated and saved for it.

**Where, precisely:** `legacy/Procurement/src/main/java/com/param/procurement/purchase/service/goodReceiptNote/GoodReceiptNoteService.java`, inside `approveGoodReceiptNote` (loop starting around line 1746):
```java
for (GoodReceiptNoteDetail detail : goodReceiptNote.getGoodReceiptNoteDetailsList()) {
    detail.setStatus(new Status(grnStatusDto.getApprovalStatusId()));
    ...
```
No check for a deleted line item anywhere in this loop.

Compare with the working equivalent in `legacy/Inventory/src/main/java/com/param/inventory/store/service/returnableGatepass/ReturnableGatepassService.java:1002-1003`:
```java
for (ReturnableGatepassDetail detail : gatepass.getReturnableGatepassDetails()) {
    if (!detail.getIsDeleted()) {
```

**Confirmed the fix is mechanically available:** `GoodReceiptNoteDetail` does not declare `isDeleted` directly, but inherits `getIsDeleted()`/`setIsDeleted()` from `CommonEntity` via `BaseEntity extends CommonEntity` — the exact same inheritance chain `ReturnableGatepassDetail` uses. This is a real, already-present field, not something that needs adding to the schema or entity.

**The proposed fix:**
```java
for (GoodReceiptNoteDetail detail : goodReceiptNote.getGoodReceiptNoteDetailsList()) {
    if (!detail.getIsDeleted()) {
        detail.setStatus(new Status(grnStatusDto.getApprovalStatusId()));
        ...
        // rest of the existing loop body, indented one level
    }
}
```
Wrapping the entire existing loop body in the same guard Inventory already uses — not a new pattern, the identical one-line style as the null-tax crash fix.

**What this fix deliberately does NOT do:**
- It does not touch the discount-netting question (2a above) — separate, blocked, unrelated.
- It does not change what happens for a non-deleted line item — behavior for the common case is unchanged.
- It does not touch Inventory's file (already correct).

**Classification, per RULES.md:** Behavioral Change. Today, a deleted line item still gets tax components calculated and saved and its status updated during approval; after this fix, it's skipped entirely, matching Inventory's existing behavior. Visible difference in outcome for the specific case of an already-deleted line item reaching the approval step.

**Verification plan, if approved:**
1. Apply the guard.
2. Add/update a test in `GoodReceiptNoteServiceCharacterizationTest` asserting a deleted line item is skipped (no tax component save, no status update for that detail) while non-deleted lines in the same batch still process normally — mirroring the existing Inventory characterization coverage for this same check.
3. Full `mvn -pl Procurement test` clean run.
4. Recommend confirming with whoever built this flow whether deleted line items can actually reach the approval step in normal use, per the original triage note — doesn't block applying the fix, just context for how urgently to treat it.

---

## 3. One SMS-notification check crashes where two nearly-identical ones don't (no money math)

**The problem, in one sentence:** of three near-identical checks that decide whether to send an SMS after a payment, two correctly guard against a blank field; the third doesn't, and throws an uncaught `NullPointerException` when that field is blank in that specific case.

**Where, precisely:** `legacy/Billing/src/main/java/com/param/billing/global/transaction/service/BillingMasterServiceImpl.java`. The three branches:

```java
// line 2210 -- correctly guarded
if (billRes.getStatus().equals(SUCCESS) && reqDto.getIsInterimOrFinalBill() != null
        && reqDto.getIsInterimOrFinalBill() == 2 && reqDto.getVisitTypeId() == OP) {

// line 2231 -- correctly guarded
if (billRes.getStatus().equals(SUCCESS) && reqDto.getIsInterimOrFinalBill() != null
        && reqDto.getIsInterimOrFinalBill() == 2 && reqDto.getVisitTypeId() != OP) {

// line 2255 -- NOT guarded
if (billRes.getStatus().equals(SUCCESS) && reqDto.getIsInterimOrFinalBill() == 1
        && reqDto.getVisitTypeId() != OP) {
```
`getIsInterimOrFinalBill()` returns a boxed `Integer`. The third branch's `== 1` auto-unboxes it; if the value is `null`, that throws `NullPointerException` at the comparison itself, before the branch body even runs.

**The proposed fix:**
```java
if (billRes.getStatus().equals(SUCCESS) && reqDto.getIsInterimOrFinalBill() != null
        && reqDto.getIsInterimOrFinalBill() == 1 && reqDto.getVisitTypeId() != OP) {
```
Adding the same `!= null &&` guard the other two branches already have — copied, not invented.

**What this fix deliberately does NOT do:** it does not touch the two already-correct branches, the SMS content/logic inside any branch, or any billing/payment logic elsewhere in this large method — only adds the missing null guard to this one `if` condition.

**Classification, per RULES.md:** Behavioral Change (not Financial Correctness — no dollar amount changes). Today, a null `isInterimOrFinalBill` in this specific combination crashes the payment-collection request (wrapped as a generic `BillingException` by the method's catch-all); after the fix, that combination is simply skipped — no SMS sent for that case, same as if the condition were legitimately false, and the rest of `collectPayment` completes normally instead of failing.

**Verification plan, if approved:**
1. Apply the one-line guard addition.
2. Update `BillingMasterServiceImplCollectPaymentCharacterizationTest` — the existing test asserting this exact `NullPointerException` occurs will need to change to assert the method now completes normally (no exception, no SMS call) when `isInterimOrFinalBill` is null and `visitTypeId != OP`.
3. Full `mvn -pl Billing test` clean run.

---

## 4a. Redundant no-op database writes after a deposit is exhausted (no money math)

**The problem, in one sentence:** when a payment draws from multiple deposit records, the loop that updates each one keeps running (and writing to the database) even after the amount being consumed has hit zero, producing real but meaningless writes for every leftover record.

**Where, precisely:** `legacy/Billing/src/main/java/com/param/billing/global/transaction/service/PaymentDepositConsumptionServiceImpl.java`, inside `consumeDeposit`:
```java
if(consumedAmount>0)               // line 81 -- checked ONCE, before the loop
{
    while(listIterator.hasNext())  // line 83 -- consumedAmount not re-checked here
    {
        ...
        iDepositMasterService.updateDepositDetails(paymentDepositDetailsDto);  // line 101 -- runs regardless
        consumedAmount=consumedAmount-paymentDepositDetailsDto.getConsumedAmount();  // line 103
        ...
    }
}
```
Once `consumedAmount` reaches 0 partway through the loop, every remaining iteration still runs: `consumedAmount>=detailsAmount` is false (0 >= positive is false), so `paymentDepositDetailsDto.setConsumedAmount(consumedAmount)` sets it to 0, `setBalanceAmount(detailsAmount - 0)` leaves the balance unchanged, and `updateDepositDetails(...)` still fires — a real database write that changes nothing.

**The proposed fix:**
```java
while(listIterator.hasNext() && consumedAmount>0)
```
Move the exhaustion check into the loop condition itself, so it's re-evaluated every iteration instead of once before the loop starts. The outer `if(consumedAmount>0)` guard (line 81) can stay as-is or be removed since it becomes redundant with the new loop condition — I'd lean toward removing it to avoid two checks of the same thing, but leaving it is harmless either way.

**What this fix deliberately does NOT do:** it does not change which records get consumed or by how much — for every record that still needed to be touched before exhaustion, behavior is identical. It only stops the loop from continuing to iterate (and write) once there's nothing left to consume.

**Classification, per RULES.md:** Refactoring (Safe) — output for every record that actually needs updating is byte-identical; the only difference is that already-exhausted, no-op writes stop happening. No dollar amount, balance, or consumed-amount value changes for any record.

**Verification plan, if approved:**
1. Apply the loop-condition change.
2. Update `PaymentDepositConsumptionServiceImplCharacterizationTest` — the existing test asserting `updateDepositDetails` is called once per record regardless of exhaustion will need to change to assert it's called only for records actually touched before exhaustion.
3. Full `mvn -pl Billing test` clean run.

---

## 4b. Bill-reference check only fires backwards — BLOCKED pending a business-intent answer

**Status: cannot be proposed yet.** I can't tell from the code whether this is a bug or a deliberate narrow rule, and guessing wrong here would silently start stamping bill references onto records that were never meant to get one.

**The facts, for whoever answers this:** `legacy/Billing/src/main/java/com/param/billing/global/transaction/service/PaymentDepositConsumptionServiceImpl.java`, inside `consumeDeposit`, lines 53-56:
```java
if(depositDto.getBillId() != null)
    depositDto.setBillId(reqDto.getBillingMasterId());
if(depositDto.getBillPayeeDetailsId() != null)
    depositDto.setBillPayeeDetailsId(reqDto.getBillPayeeDetailsId());
```
Both checks only overwrite the field when it's **already non-null** — the opposite of the usual "fill in if missing" pattern that a null-check normally signals. As written, a deposit record that starts with no bill reference at all never gets one assigned here.

**What I need from you (or whoever knows this part of the deposit-consumption design):** is this intentional — i.e., should a bill reference only ever be *updated* on records that already have one, and never *assigned* to a record that's missing one? Or is this backwards, and it should read `== null` (fill in the blank) instead of `!= null` (overwrite existing)?

**If the answer turns out to be "it's backwards, should be `== null`":** the fix itself would be a two-line flip of `!=` to `==` in both conditions — trivial once the intent is confirmed. I'm not proposing that flip now, per your instruction not to guess.

---

## 4c. Dead-code no-op line (no money math)

**The problem, in one sentence:** one line reads a field's current value and immediately writes that exact same value straight back — it has zero observable effect.

**Where, precisely:** `legacy/Billing/src/main/java/com/param/billing/global/transaction/service/PaymentDepositConsumptionServiceImpl.java:58-60`:
```java
Double consumedAmount = reqDto.getTotalDepositConsumption();
Double detailsConsumedAmount = depositDto.getConsumedAmount();
depositDto.setConsumedAmount(detailsConsumedAmount);
```
Line 59 reads `depositDto.getConsumedAmount()`; line 60 immediately writes that same value back onto the same field. No-op.

**The proposed fix:** delete line 60 (`depositDto.setConsumedAmount(detailsConsumedAmount);`). `detailsConsumedAmount` is still used later (line 71, 92, 104 context) as a plain local variable — only the pointless write-back to `depositDto` itself is removed.

**What this fix deliberately does NOT do:** it does not change `detailsConsumedAmount`'s value or how it's used anywhere else in the method — only removes a line that writes a field to the value it already holds.

**Classification, per RULES.md:** Refactoring (Safe) — byte-identical output, since the deleted line never changed any value to begin with.

**Verification plan, if approved:**
1. Delete the line.
2. Full `mvn -pl Billing test` clean run — no test changes expected, since no test should currently be asserting on this no-op's non-effect.

---

## 5. Misleading "already in process" message on save-bill rejection (no money math)

**The problem, in one sentence:** when the bill-save double-check can't find any matching data to compare against — for any reason, not just a concurrent edit — the save is rejected with a message that specifically blames another user, steering troubleshooting toward the wrong cause.

**Where, precisely:** `legacy/Billing/src/main/java/com/param/billing/global/transaction/controller/BillingMasterController.java`, lines 237-292:
```java
Response orderDetailsRes = iBillingMasterService.getOrderSumNetAndSelfCreditAmount(orderDetailsIds);
Boolean checkSameAmount = true;                                    // line 238 -- defaults true
if(orderDetailsRes.getObject() != null){
    ... // lines 239-288: only reachable when data WAS found;
        // checkSameAmount becomes false here if amounts match within tolerance
}

if(checkSameAmount){
    return new Response<>(ERROR, ALREADY_EXIST_CODE, "This bill is already in process by other user!", null, null);
}
```
`checkSameAmount` starts `true` and is only ever set `false` **inside** the `orderDetailsRes.getObject() != null` block. If the lookup finds no data at all (`orderDetailsRes.getObject() == null` — could be a genuine data gap, a timing issue, anything), the `if` block is skipped entirely, `checkSameAmount` stays `true` by default, and the same "already in process by other user" message fires — even though "another user is editing it" was never actually checked or confirmed for that case.

**The proposed fix:** distinguish the "no data found" case from the "data found but amounts genuinely disagree" case, with an accurate message for each:
```java
if(orderDetailsRes.getObject() == null){
    return new Response<>(ERROR, ALREADY_EXIST_CODE, "Unable to verify this bill's current amount -- please retry, or contact support if this persists.", null, null);
}

if(checkSameAmount){
    return new Response<>(ERROR, ALREADY_EXIST_CODE, "This bill is already in process by other user!", null, null);
}
```
Adding an explicit early check for the null-data case with its own, honest message, ahead of the existing `checkSameAmount` check (which keeps its current message, now only reachable for the case it's actually describing — amounts that were found but don't match within tolerance).

**What this fix deliberately does NOT do:**
- It does not touch the tolerance/rounding logic inside the `orderDetailsRes.getObject() != null` block at all — that's ONE-OFF-2's already-confirmed whole-unit-rounding and ±1.00 tolerance behavior, re-verified this session, unrelated to this finding.
- It does not change whether a save succeeds or fails in any case that currently succeeds — a save that currently fails due to a missing lookup still fails; only the message explaining why changes.
- It does not change the `ALREADY_EXIST_CODE` error code used for either message — only the text. (Open question for the owner if a distinct error code is wanted for the two cases; not proposing that here since it wasn't part of the original finding.)

**Classification, per RULES.md:** Behavioral Change (message/response content, not billing math) — RULES.md treats any visible output change as needing a manifest entry regardless of whether it's a calculation; this is a genuine change in what the API returns for the no-data-found case, so it's logged as one rather than waved through as a pure refactor.

**Verification plan, if approved:**
1. Apply the new early check.
2. Update `BillingMasterControllerSaveBillCharacterizationTest` — add a case asserting the new message fires when `orderDetailsRes.getObject()` is null, and confirm the existing 6 tolerance/rounding tests still pass unchanged (they all exercise the non-null path, which is otherwise untouched).
3. Full `mvn -pl Billing test` clean run.

---

## Summary of what's proposed vs. blocked

| # | Finding | Status |
|---|---|---|
| 1 | Co-pay/pre-auth split gap | **Proposed** — 2-part fix (method + 7 call sites), extra verification step recommended given money-math + scope |
| 2a | Tax before/after discount, receipts vs. returns | **Blocked** — needs your tax/accounting answer |
| 2b | No soft-delete skip on goods-receipt approval | **Proposed** — mirrors Inventory's existing pattern exactly |
| 3 | Unguarded null-check crashes one SMS branch | **Proposed** — mirrors the other two branches exactly |
| 4a | Redundant no-op deposit writes | **Proposed** — loop-condition change only |
| 4b | Backwards bill-reference check | **Blocked** — needs a business-intent answer |
| 4c | Dead-code no-op line | **Proposed** — one-line deletion |
| 5 | Misleading "already in process" message | **Proposed** — new early check + distinct message |

No code has been changed for any of these 8 items. Each is awaiting your individual approval, same as the GRN fix — approve one at a time, in any order, whenever you're ready. The two blocked items (2a, 4b) will stay blocked until you or the relevant domain expert answers the open question in each; I'll turn those into full proposals the moment an answer comes back.

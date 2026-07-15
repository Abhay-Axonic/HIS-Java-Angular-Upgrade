# Decision needed: fix the missing updateCap guard in updatePreAuthAndServiceCapAmountForFirstPriorityContract's checkFlag=true branch

*Blueprint (docs only, no code applied). 2026-07-15. Split out from `BR-PENDING-shadow-wave2-billingcommonmethods-batch1` in `docs/BUSINESS_RULE_MANIFEST.md` at the owner's explicit request: of that batch's 4 findings, this one (finding 4) is judged an active double-write risk rather than a lower-probability edge case, and is being fast-tracked for its own review with the same priority as the earlier GRN null-tax-reference crash fix. The other 3 findings from that batch (GST bracket last-row-wins, null gstAmountRange NPE, the untouched creaditPayable sub-branch) stay in the slower triage list, untouched by this document.*

---

## The problem, in one sentence

One specific write path in a shared financial-consumption method skips the "don't process this contract consumption twice" guard that every other equivalent write path in this class enforces — so, unlike every sibling branch, it can write a real, incorrect duplicate deduction to a patient's contract cap balance in the database.

## Where, precisely

`legacy/Global/src/main/java/com/param/global/common/BillingCommonMethods.java:296-303`, inside `updatePreAuthAndServiceCapAmountForFirstPriorityContract`:

```java
if (checkFlagUpdateCapOrServiceCap) {

    if (creditPayable > 0) {
        consumptionDto.setSpecialityId(resDto.getSpecialityId());
        consumptionDto.setContractId(resDto.getContractId());
        iContractConsumptionDao.updateCapAmount(consumptionDto, creditPayable);
    }
} else if (consumptionDto.getUpdateCap() == null && creditPayable > 0) {
    ...
}
```

The `checkFlagUpdateCapOrServiceCap=true` branch (lines 296-302) only checks `creditPayable > 0` before calling `updateCapAmount(...)` — the actual database write that reduces a contract's remaining cap balance. It has **no check at all** on `consumptionDto.getUpdateCap()`.

Contrast with the `else` branch two lines below it (line 303), which explicitly requires `consumptionDto.getUpdateCap() == null` before writing. And contrast with this exact method's own sibling, `updatePreAuthAndServiceCapAmountForSecondContract` (the method fixed for Finding 1 earlier today), whose equivalent `checkFlagUpdateCapOrServiceCap=true` branch does check it:

```java
// updatePreAuthAndServiceCapAmountForSecondContract, lines 341-345 (already correct)
if (checkFlagUpdateCapOrServiceCap) {
    consumptionDto.setContractId(secondRound.getContractId());
    if (consumptionDto.getUpdateCap() == null && secondRound.getBalanceCapAmount() != null && creditPayable > 0)
        iContractConsumptionDao.updateCapAmount(consumptionDto, creditPayable);
}
```

## Why this is judged active, not hypothetical

`consumptionDto.getUpdateCap() == null` is not an incidental check — it's the guard this entire class relies on everywhere else to mean "this contract consumption has already been recorded, do not write again." A direct grep confirms it: **~30 other write-gating checks across `ContractConsumptionServiceImpl.java`** use the exact same `consumptionDto.getUpdateCap()==null && creditPayable>0`-shaped guard before every other `updateCapAmount`/`updatePreAuthAmount`/`updateServiceCapAmount` call in the class, including this same method's own pre-auth block three lines above (line 292: `if (consumptionDto.getUpdateCap() == null && creditPayable > 0) iContractConsumptionDao.updatePreAuthAmount(...)`). `updateCap` is set by calling code (confirmed at `ContractConsumptionServiceImpl.java:2809`, `consumptionDto.setUpdateCap(searchResDto.getUpdateCap())`) specifically to carry this "already processed" signal across a broader consumption flow. This one branch is the sole exception to a pattern enforced consistently everywhere else — that shape (one gap in an otherwise-universal safety check) is exactly the kind of thing worth fixing on its own timeline rather than waiting for the slower triage, the same reasoning applied to the GRN null-tax crash.

Whether this specific branch has actually fired twice in production wasn't investigated this session (that would require real transaction data, which is separately on hold) — but the code path is real and reachable (any first-priority contract consumption routed through this method with `checkFlagUpdateCapOrServiceCap=true`), and its failure mode (a real, silent, financially-incorrect database write) is worse than a crash: a crash is visible and stops things; this is invisible and just quietly wrong.

## Before/after — what actually happens if this branch fires twice

**Setup:** a $500 service line is processed against a first-priority contract with, say, $2,000 remaining on its cap. Somewhere in the broader consumption flow, `consumptionDto` is legitimately reused or this code path is reached a second time for the same consumption (a retry after a transient failure, a duplicate call in a batch/reprocessing flow, or any other path that reaches this method twice with the same `consumptionDto`) — the exact same shape of "called more than once for the same object" that the `updateCap` guard exists specifically to protect against everywhere else in this class.

**First call** — `consumptionDto.getUpdateCap()` is `null` (nothing processed yet):
- `updateCapAmount(consumptionDto, 500.0)` fires.
- Contract cap balance in the database: $2,000 → **$1,500**. Correct — one $500 service was actually rendered.

**Second call** — by now, the broader flow has set `consumptionDto.updateCap` to a non-null value, signaling "already recorded":

| | What happens |
|---|---|
| **Wrong (today)** | No guard checks `updateCap` in this branch — `updateCapAmount(consumptionDto, 500.0)` fires **again**. Cap balance: $1,500 → **$1,000**. |
| **Correct (if fixed)** | The added guard sees `updateCap` is non-null and skips the write entirely. Cap balance stays at **$1,500**. |
| **Gap** | **$500 incorrectly deducted from the contract's remaining cap balance**, for a service that was only actually rendered and billed once. |

The database ends up believing $1,000 of cap-eligible services were consumed when only $500 actually were. That understates how much of the contract's cap the patient has left — which can push them into paying self-pay sooner than they should for later services, and won't reconcile against actual billed line items if anyone audits the cap balance against real charges.

## The proposed fix — minimal, not a redesign

Change lines 296-302 from:

```java
if (checkFlagUpdateCapOrServiceCap) {

    if (creditPayable > 0) {
        consumptionDto.setSpecialityId(resDto.getSpecialityId());
        consumptionDto.setContractId(resDto.getContractId());
        iContractConsumptionDao.updateCapAmount(consumptionDto, creditPayable);
    }
}
```

to:

```java
if (checkFlagUpdateCapOrServiceCap) {

    if (consumptionDto.getUpdateCap() == null && creditPayable > 0) {
        consumptionDto.setSpecialityId(resDto.getSpecialityId());
        consumptionDto.setContractId(resDto.getContractId());
        iContractConsumptionDao.updateCapAmount(consumptionDto, creditPayable);
    }
}
```

Adding `consumptionDto.getUpdateCap() == null &&` to the existing `if (creditPayable > 0)` condition — not a new pattern being invented, the exact same guard already used at ~30 other call sites in the same class, including three lines above in this same method and in this method's own sibling.

**What this fix deliberately does NOT do:**
- It does not touch the `else` branch (line 303) — already correct.
- It does not touch the sibling `updatePreAuthAndServiceCapAmountForSecondContract` method — already correct (and already fixed for the unrelated Finding 1 issue).
- It does not change behavior for the common case — a first call with `updateCap` still `null` behaves identically before and after this fix.
- It does not address the other 3 findings from the same batch (GST bracket last-row-wins, null `gstAmountRange` NPE, the untouched `creaditPayable` sub-branch) — those stay in the slower triage list.

## Classification, per RULES.md

**Behavioral Change** (not "Refactoring (Safe)"): today, this branch can write a duplicate cap deduction when called more than once for the same `consumptionDto` with `updateCap` already set; after this fix, the second (and any subsequent) call is silently skipped, matching every other guarded write path in this class. That is a real, visible difference in database-write outcome for the specific case of a re-triggered consumption — not a byte-identical refactor. It is closer to "Financial Correctness" than a cosmetic fix, since the current behavior can produce an incorrect contract cap balance.

## Verification plan, if approved

1. Apply the one-line guard addition (adding `consumptionDto.getUpdateCap() == null &&` to the existing condition).
2. Update `FirstPriorityContractAdjustment#checkFlagTrueBranch_missingTheUpdateCapGuard` in `BillingCommonMethodsCharacterizationTest.java` — currently asserts `updateCapAmount` IS called even with `updateCap` non-null (pinning the bug); will need to assert it is now `never()` called in that same setup, mirroring the existing `checkFlagFalseBranch_respectsTheUpdateCapGuard` test right next to it.
3. Confirm `simplestPath_noAdjustments`, `coShareAdjustment_fromContactTrue`, and `preAuthCeilingExceeded` (the other 3 tests in the same nested class, all using `updateCap == null`) continue to pass unchanged — they exercise the now-still-open path of the guard, so their behavior shouldn't move at all.
4. Full `mvn -pl Global,Billing -am clean test` clean run, confirming no other test regresses.
5. Log the outcome in `docs/BUSINESS_RULE_MANIFEST.md` as a completed Behavioral Change once approved and applied, per the classification above, and update `BR-PENDING-shadow-wave2-billingcommonmethods-batch1`'s status to reflect finding 4 as fixed while findings 1-3 remain open.

No code has been changed yet. This document is the proposal only, awaiting owner review on its own timeline, separate from the other 3 findings in the same batch.

# Decision needed: fix the money-conservation bug in updatePreAuthForPharmacyContract's co-share adjustment

*Blueprint (docs only, no code applied). 2026-07-15. Split out from `BR-PENDING-shadow-wave2-billingcommonmethods-batch2` in `docs/BUSINESS_RULE_MANIFEST.md` at the owner's explicit request: of that batch's 6 findings, this one (finding 1) was flagged live as genuinely urgent the moment it was found, and is being fast-tracked for its own review with the same priority as the earlier GRN null-tax-reference crash and the batch-1 missing-updateCap-guard fixes. The other 5 findings from that batch (a missing-map-key NPE, two sibling methods accumulating a total by inconsistent bases, a silently-discarded caller flag, hardcoded partial-fulfillment quantities, and two branches deriving `netAmount` differently) stay in the slower triage list, untouched by this document.*

---

## The problem, in one sentence

A pharmacy-contract co-share calculation removes one amount from the credit-payable side but only ever adds back a smaller amount to the self-payable side, so real money silently disappears from the combined total every time this specific adjustment runs.

## Where, precisely

`legacy/Global/src/main/java/com/param/global/common/BillingCommonMethods.java:607-610`, inside `updatePreAuthForPharmacyContract`:

```java
if (resDto.getCoShare() != null) {
    creditPayable = GlobalCommonDateUtils.getAmount(creditPayable - (creditPayable * (resDto.getCoShare() / 100)));
    selfPayable = GlobalCommonDateUtils.getAmount(selfPayable + (creditPayable * (resDto.getCoShare() / 100)));
}
```

Line 608 reassigns `creditPayable` to its co-share-reduced value **first**. Line 609 then computes the amount added to `selfPayable` using that **already-reduced** `creditPayable` — not the original amount the co-share percentage was supposed to apply to. The two lines are meant to move the same dollar amount from one side of the ledger to the other; because of the reassignment happening before the second calculation, they don't.

## Why this is judged active, not hypothetical

This is not a subtle theoretical edge case — it fires every time this method is called for a contract with a co-share percentage set, which is the whole reason the branch exists. It's also directly comparable to code that already gets it right, twice, in this exact same file:

```java
// updatePreAuthAndServiceCapAmountForFirstPriorityContract, lines 282-285 (already correct)
if (fromContact && resDto.getCoShare() != null && creditPayable > 0) {
    selfPayable = selfPayable + (creditPayable * resDto.getCoShare() * 0.01);
    creditPayable = creditPayable - (creditPayable * resDto.getCoShare() * 0.01);
}
```

Note the order: `selfPayable` is computed *first*, using the still-original `creditPayable`, and only *then* is `creditPayable` reassigned. `updatePreAuthAndServiceCapAmountForSecondContract` (fixed earlier today for a different, unrelated issue) uses the identical order. `updatePreAuthForPharmacyContract` is the only one of the three co-share-applying methods in this file with the two lines in the wrong order.

Confirmed via a passing characterization test (`PreAuthForPharmacyContract#coShareAdjustment_moneyConservationBug` in `BillingCommonMethodsCharacterizationTest.java`) that this is real, reproducible current behavior, not a misreading.

## Before/after — what actually happens to the money

**Setup:** a pharmacy contract with a 20% co-share, a $1,000 amount initially proposed as credit-payable (billed to the contract/insurer), $0 self-payable so far.

| | creditPayable | selfPayable | **Combined total** |
|---|---|---|---|
| **Before this adjustment runs** | $1,000 | $0 | **$1,000** |
| **Wrong (today)** | $800 (1000 − 20% of 1000, correct) | $160 (20% of the *already-reduced* $800, not $1000) | **$960** |
| **Correct (if fixed)** | $800 | $200 (20% of the *original* $1000) | **$1,000** |
| **Gap** | — | **$40 understated** | **$40 has simply disappeared** |

This is a different shape of problem from Finding 1 (the co-pay/pre-auth split return-value gap, fixed earlier today) and from batch-1 finding 4 (the missing double-write guard, also fixed today). Those were about a correct number failing to reach the right place, or a correct write happening twice. This is about the number itself being wrong — $40 of a $1,000 pharmacy charge is computed out of existence, in both directions: it isn't billed to the contract/insurer (creditPayable correctly dropped to $800) and it isn't billed to the patient either (selfPayable only picked up $160, not the full $200 the co-share was supposed to shift onto them). It just vanishes from the calculation entirely.

## The proposed fix — minimal, not a redesign

Swap the order of the two lines, so `selfPayable` is computed first, from the still-original `creditPayable`:

```java
if (resDto.getCoShare() != null) {
    selfPayable = GlobalCommonDateUtils.getAmount(selfPayable + (creditPayable * (resDto.getCoShare() / 100)));
    creditPayable = GlobalCommonDateUtils.getAmount(creditPayable - (creditPayable * (resDto.getCoShare() / 100)));
}
```

This is not a new pattern being invented — it is the exact line order already used correctly in both sibling co-share calculations in this same file (`updatePreAuthAndServiceCapAmountForFirstPriorityContract` and `...ForSecondContract`). No new variables, no restructuring, no change to the rounding behavior of `GlobalCommonDateUtils.getAmount(...)` — purely a reorder of two existing lines so the second one reads the value the first one is about to overwrite.

**Verified by hand with the same example:** `selfPayable = 0 + (1000 × 0.20) = 200`; `creditPayable = 1000 − (1000 × 0.20) = 800`. Combined total: $1,000 — conserved.

**What this fix deliberately does NOT do:**
- It does not touch the pre-authorization ceiling logic below it (lines 612-619) — unrelated, already correct, already covered by its own passing tests.
- It does not touch either sibling method — already correct, not part of this finding.
- It does not change `GlobalCommonDateUtils.getAmount(...)`'s own rounding/abs-value behavior at all.
- It does not address the other 5 findings from the same batch (missing-map-key NPE, inconsistent accumulation bases, the discarded `setPkgAmt` flag, hardcoded partial-fulfillment values, or the two-formula `netAmount` inconsistency) — those stay in the slower triage list.

## Classification, per RULES.md

**Financial Correctness** (not merely "Behavioral Change"): today, applying a co-share percentage on a pharmacy contract causes a real dollar amount to be dropped from the total charge calculation — neither billed to the contract/insurer nor to the patient. After this fix, the same $1,000 charge with the same 20% co-share correctly nets to $1,000 across both sides, matching the pattern already proven correct in this file's two sibling methods. This is squarely the kind of defect RULES.md's billing-accuracy priority exists to catch before anything else.

## Verification plan, if approved

1. Apply the two-line reorder.
2. Update `PreAuthForPharmacyContract#coShareAdjustment_moneyConservationBug` in `BillingCommonMethodsCharacterizationTest.java` — currently asserts the lossy result (`creditPayable=800.0`, `selfPayable=160.0`, combined `960.0`); will need to assert the corrected result (`creditPayable=800.0`, `selfPayable=200.0`, combined `1000.0`) instead.
3. Confirm the other 4 tests in the same nested class (`noAdjustments_passThrough`, `preAuthCeilingExceeded_consumeTrue`, `preAuthCeilingExceeded_consumeFalse`, `preauthIdMissingFromMap_throwsNpe`) continue to pass unchanged — none of them set a `coShare`, so this reorder shouldn't move them at all.
4. Full `mvn -pl Global,Billing -am clean test` clean run, confirming no other test regresses.
5. Log the outcome in `docs/BUSINESS_RULE_MANIFEST.md` as a completed Financial Correctness fix once approved and applied, and update `BR-PENDING-shadow-wave2-billingcommonmethods-batch2`'s status to reflect finding 1 as fixed while findings 2-6 remain open.

No code has been changed yet. This document is the proposal only, awaiting owner review on its own timeline, separate from the other 5 findings in the same batch, and separate from batch 3 (which remains on hold pending the owner's review of Finding 4's fix and batches 1/2, per explicit instruction).

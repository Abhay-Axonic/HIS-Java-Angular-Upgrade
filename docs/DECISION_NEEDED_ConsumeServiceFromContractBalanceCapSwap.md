# Decision needed: fix the swapped self/credit payable in consumeServiceFromContract's amount-capped branch

*Blueprint (docs only, no code applied). 2026-07-15. Flagged live to the owner the moment it was found, while mapping `consumeServiceFromContract`'s branches for targeted shadow-test characterization (Step 3, giant-method pass). Fast-tracked for its own review with the same priority as Finding 4 (`DECISION_NEEDED_FirstPriorityContractMissingUpdateCapGuard.md`) and the pharmacy co-share leak (`DECISION_NEEDED_PharmacyContractCoShareMoneyLeak.md`). No other findings from this pass are bundled into this document — the rest of `consumeServiceFromContract`'s targeted characterization is paused until this is resolved, per owner instruction.*

---

## The problem, in one sentence

When a first-priority, amount-capped contract's remaining balance is exceeded by a service's cost, the amount billed to the patient and the amount billed to the contract/insurer are computed backwards — the patient is charged the cap amount and the contract is charged only the leftover overage, the opposite of what a cap is supposed to do.

## Where, precisely

`legacy/Global/src/main/java/com/param/global/billing/service/ContractConsumptionServiceImpl.java:740-745`, inside `consumeServiceFromContract`, in the branch that handles a `resDto.getType()==2` (service-cap) contract whose cap is tracked by amount (as opposed to quantity):

```java
if(resDto.getBalanceAmount()!=null && resDto.getBalanceAmount()>0)
{
    creaditPayable = netAmt < resDto.getBalanceAmount() ? netAmt : netAmt-resDto.getBalanceAmount();
    selfPayable = netAmt < resDto.getBalanceAmount() ? 0.0 : resDto.getBalanceAmount();
```

Read the two ternaries side by side. When `netAmt < balanceAmount` (the service costs less than what's left on the cap), both lines are correct: `creaditPayable = netAmt` (the whole thing goes to the contract), `selfPayable = 0.0` (patient pays nothing). The bug is only in the **else** case — when the service costs *more* than what's left on the cap:

- `creaditPayable` becomes `netAmt - resDto.getBalanceAmount()` — the **overage**, the part *above* the cap.
- `selfPayable` becomes `resDto.getBalanceAmount()` — the **cap amount itself**, the part the contract was supposed to absorb.

That's backwards. The contract/insurer should absorb up to its own remaining cap, and the patient should pay whatever's left over above that cap — not the other way around.

## Why this is judged active, not hypothetical

This isn't a matter of interpretation — the exact same method has the *correct* version of this logic 150 lines earlier, for the sibling case where a first-priority contract's cap is tracked by a flat cap amount rather than a per-visit balance (`resDto.getType()==1`, lines 590-593):

```java
// consumeServiceFromContract, lines 590-593 (already correct, type==1 branch)
selfPayable = (resDto.getCreaditPayable() <= resDto.getBalanceCapAmount()) ? resDto.getSelfPayable() :  (resDto.getSelfPayable()+(resDto.getCreaditPayable()-resDto.getBalanceCapAmount()));
creaditPayable =  (resDto.getCreaditPayable() <= resDto.getBalanceCapAmount()) ?  resDto.getCreaditPayable() :  resDto.getBalanceCapAmount();
```

Here, when the cap is exceeded (`getCreaditPayable() > getBalanceCapAmount()`): `creaditPayable` becomes `getBalanceCapAmount()` (the cap — contract pays up to what it owes), and `selfPayable` picks up `getCreaditPayable() - getBalanceCapAmount()` (the overage — patient pays the rest). That is the correct, intended shape, and it's what the buggy branch 150 lines later should also be doing, just phrased in terms of `netAmt` instead of `resDto.getCreaditPayable()`.

This branch is reachable any time a first-priority contract with an amount-tracked balance cap (`resDto.getType()==2`, `resDto.getBalanceAmount()` set and positive) is consumed by a service whose cost exceeds what's left on that cap — an ordinary, expected occurrence for any patient who uses up most or all of a capped benefit (e.g., a $2,000 annual diagnostics cap with $300 left, and a $500 test ordered). It is not an edge case; it's the specific scenario a cap exists to handle.

Confirmed via a passing test (`ConsumeServiceFromContractUrgentFindingTest#balanceCapExceeded_selfAndCreditPayableAreSwapped`), which captures the exact arguments passed into `billingCommonMethods.updatePreAuthAndServiceCapAmountForFirstPriorityContract(...)` immediately after this computation — the swap has already happened by the time that call is made, so nothing that downstream method does can correct it.

## Before/after — what actually happens to the money

**Setup:** a service costs $500 (`netAmt`), and the first-priority contract has $300 left on its amount-tracked balance cap (`resDto.getBalanceAmount()`).

| | Billed to contract/insurer (`creaditPayable`) | Billed to patient (`selfPayable`) |
|---|---|---|
| **Wrong (today)** | $200 (the overage) | $300 (the cap amount) |
| **Correct (if fixed)** | $300 (up to the remaining cap) | $200 (the overage) |

The combined total ($500) is conserved either way — this is not a money-vanishes bug like the pharmacy co-share leak. It's a **misallocation** bug: the contract is undercharged by $100 and relies on it not noticing $100 it was owed, while the patient is overcharged by $100 for a benefit their capped contract was supposed to cover. Every subsequent capped-service consumption for this patient would also compound on an already-wrong contract balance, since the local `creaditPayable`/`selfPayable` computed here feed both the per-line payee record (line 878's `if(creaditPayable>0)` block, built from the *wrong* pre-swap value) and, via `updatePreAuthAndServiceCapAmountForFirstPriorityContract`, `totalCreditPayable`/`totalSelfPayable` for the whole consumption.

## The proposed fix — minimal, not a redesign

Swap only the **else-branch** (the second operand) of each ternary — leave the already-correct true-branches untouched:

```java
if(resDto.getBalanceAmount()!=null && resDto.getBalanceAmount()>0)
{
    creaditPayable = netAmt < resDto.getBalanceAmount() ? netAmt : resDto.getBalanceAmount();
    selfPayable = netAmt < resDto.getBalanceAmount() ? 0.0 : netAmt-resDto.getBalanceAmount();
```

Verified by hand with the same example: `netAmt=500`, `balanceAmount=300` → not `<`, so `creaditPayable = 300` (the cap), `selfPayable = 500-300 = 200` (the overage). Matches the sibling type==1 branch's semantics exactly, and matches the already-correct `netAmt < balanceAmount` case's own logic (full amount to contract when it fits).

**What this fix deliberately does NOT do:**
- It does not touch the `resDto.getType()==1` cap-amount branch (lines 584-594) at all — that logic is untouched, in a different, non-adjacent conditional block gated on a different field (`resDto.getType()`), ~150 lines away, and is already correct. This fix doesn't restructure it, reference it, or change how it's reached.
- It does not touch the `resDto.getBalanceQuantity()` (quantity-capped) branch immediately below this one (lines 798-826) — a structurally separate sibling condition, not part of this finding.
- It does not touch the call to `billingCommonMethods.updatePreAuthAndServiceCapAmountForFirstPriorityContract(...)` three lines later (line 763) — that call's own internal logic and its known, separately-tracked issues (Finding 4, already fast-tracked and merged) are unrelated; this fix only corrects the values it's called *with*.
- It does not change the `netAmt < resDto.getBalanceAmount()` true-branch at all — the "cap not exceeded" case was already correct and stays exactly as-is.
- It does not address any of the other branches surfaced while mapping this method (e.g., a separate observation that `secondRound.getDayDiff()` is never actually set inside the nested second-round loop, because the code sets it on the outer-loop `resDto` instead — a distinct issue, still to be logged separately as a normal, non-urgent finding once this fix is resolved).

## Classification, per RULES.md

**Financial Correctness** (not merely "Behavioral Change"): today, whenever a first-priority amount-capped contract's remaining balance is exceeded by a service's cost, the patient is billed the contract's own capped obligation and the contract is billed only the patient's rightful overage — a direct misallocation of who owes what, not a cosmetic or structural change. After the fix, the same $500 service against the same $300 remaining cap correctly bills $300 to the contract and $200 to the patient, matching the pattern already proven correct in this method's own type==1 sibling branch.

## Verification plan, if approved

1. Apply the two-ternary else-branch swap (4 lines total: 2 lines each get their second operand changed).
2. Update `ConsumeServiceFromContractUrgentFindingTest#balanceCapExceeded_selfAndCreditPayableAreSwapped` — currently asserts the swapped/wrong call arguments (`creaditPayable=200.0, selfPayable=300.0`); will need to assert the corrected arguments (`creaditPayable=300.0, selfPayable=200.0`) instead. Consider renaming the test and its class out of the "urgent finding" naming once fixed, folding it into the broader `consumeServiceFromContract` characterization suite that's paused pending this decision.
3. Confirm the `netAmt < resDto.getBalanceAmount()` (cap not exceeded) case, and the sibling `resDto.getBalanceQuantity()` (quantity-capped) branch, are unaffected — neither is touched by this fix, but both should be covered by a quick regression check once the broader targeted characterization of this method resumes.
4. Full `mvn -pl Global,Billing -am clean test` clean run, confirming no other test regresses.
5. Log the outcome in `docs/BUSINESS_RULE_MANIFEST.md` as a completed Financial Correctness fix once approved and applied, and resume the paused targeted characterization of `consumeServiceFromContract`'s remaining risky branches.

No code has been changed yet. This document is the proposal only, awaiting owner review on its own timeline, alongside the pharmacy co-share fix and the rest of the backlog. Further work on `consumeServiceFromContract` is paused until this is resolved, per explicit instruction.

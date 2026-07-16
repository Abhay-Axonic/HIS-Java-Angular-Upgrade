# Decision needed: fix the second-round quantity-cap branch overwriting (not accumulating) the running credit total

*Blueprint (docs only, no code applied). 2026-07-16. Flagged live to the owner the moment it was found, while resuming targeted characterization of `consumeServiceFromContract` (Step 2 resumed, first sub-batch). Found while specifically checking for repeats of the balance-cap swap pattern (fixed 2026-07-15) elsewhere in this method — this is a *different* defect shape, found in the immediately adjacent branch, not a repeat of the swap itself.*

---

## The problem, in one sentence

When a patient's leftover self-payable amount is routed to a second-priority contract whose remaining cap is tracked by quantity (not by amount), the method throws away every dollar already credited to the first-priority contract and replaces the running credit total with just this second contract's own amount — the first contract's contribution silently disappears from the final bill.

## Where, precisely

`legacy/Global/src/main/java/com/param/global/billing/service/ContractConsumptionServiceImpl.java:1103-1113`, inside `consumeServiceFromContract`'s second-round loop, in the branch that handles a second-priority contract (`secondRound`) whose cap is tracked by remaining quantity:

```java
if(secondRound.getBalanceQuantity()!=null && secondRound.getBalanceQuantity()>0)
{
    orderDetailsPayeeMapperDtosList=new LinkedList<>();
    creaditPayableSecondRound = netAmt;
    totalCreditPayable = netAmt;
    totalSelfPayable = 0.0;

    ContractConsumptionDto creditAndSelfAmountDto =
            billingCommonMethods.updatePreAuthAndServiceCapAmountForSecondContract(secondRound,creaditPayableSecondRound,totalSelfPayable,totalCreditPayable,secondRounfCoShare,preAuthAmount,consumptionDto,false,1.00,true);
    totalSelfPayable = creditAndSelfAmountDto.getSelfPayable();
    totalCreditPayable = creditAndSelfAmountDto.getCreaditPayable();
```

`totalCreditPayable` — the running total of everything billed to contracts/insurers across the whole consumption, already carrying the first-priority contract's contribution from earlier in the same method — is **reassigned** to `netAmt` (this second contract's own amount alone), not **accumulated** onto. `totalSelfPayable` is likewise hard-set to `0.0`, discarding whatever residual self-pay tracking existed.

## Why this is judged active, not hypothetical

This branch sits directly beside its own sibling three branches earlier in the exact same loop iteration — the amount-tracked-cap branch (lines 1013-1024), which gets this right:

```java
// same method, lines 1015 & 1023-1024 (already correct)
creaditPayableSecondRound = totalSelfPayable <= balCap ?  totalSelfPayable: balCap;
...
totalCreditPayable = totalCreditPayable+creaditPayableSecondRound;
totalSelfPayable = totalSelfPayable-creaditPayableSecondRound;
```

Note the `+`/`-`: this sibling branch correctly *adds* the second contract's contribution onto the running total and *subtracts* it from the leftover self-pay. The quantity-tracked-cap branch nine lines later does the same conceptual step but with `=` instead of `+=`/`-=` — it isn't computing a delta to apply, it's replacing the whole running total outright.

This branch is reachable whenever: (a) a first-priority contract processes a service and leaves some self-payable residue (`totalSelfPayable>0`, the ordinary trigger for the second-round loop at all — not an edge case, it's the entire reason the loop exists), and (b) the second-priority contract found for that same service has a quantity-tracked balance cap (`secondRound.getBalanceQuantity()!=null`) with at least one unit remaining, rather than an amount-tracked cap. Confirmed via a passing test (`ConsumeServiceFromContractCharacterizationTest.SecondRoundTypeTwoQuantityCap#quantityCapBranch_totalCreditPayableAssignment`): a first-priority contract correctly contributes $300, but by the time the quantity-capped second contract's branch runs, `totalCreditPayable` is $400 (this round's own amount alone) — the first contract's $300 has vanished from the total that ultimately gets written to `finalDto.setCreaditPayable(totalCreditPayable)` and returned to the caller.

## Before/after — what actually happens to the money

**Setup:** a service is billed against a first-priority contract that covers $300 of it (patient owes $200 after that). A second-priority contract exists with a quantity-tracked cap that still has capacity, and its own share of this same service comes to $400.

| | First contract's credit ($300) | Second contract's credit (this round, $400) | **`totalCreditPayable` in the final response** |
|---|---|---|---|
| **Wrong (today)** | Computed correctly earlier in the method | Computed correctly for this round | **$400** — the $300 is silently dropped |
| **Correct (if fixed)** | Computed correctly earlier in the method | Computed correctly for this round | **$700** — both contributions present |
| **Gap** | — | — | **$300 of contract-billed revenue disappears from the order's own running total every time this exact branch fires** |

This is the same *shape* of harm as the pharmacy co-share leak (a real dollar figure vanishes rather than merely landing on the wrong party) — but here the loss is the entire first-round contribution, not a percentage-based leak, and it depends on which specific cap-tracking type the second contract happens to use.

## The proposed fix — minimal, not a redesign

Change the two reassignments to accumulation, matching the immediately-preceding sibling branch's own pattern exactly:

```java
if(secondRound.getBalanceQuantity()!=null && secondRound.getBalanceQuantity()>0)
{
    orderDetailsPayeeMapperDtosList=new LinkedList<>();
    creaditPayableSecondRound = netAmt;
    totalCreditPayable = totalCreditPayable + creaditPayableSecondRound;
    totalSelfPayable = totalSelfPayable - creaditPayableSecondRound;

    ContractConsumptionDto creditAndSelfAmountDto =
            billingCommonMethods.updatePreAuthAndServiceCapAmountForSecondContract(secondRound,creaditPayableSecondRound,totalSelfPayable,totalCreditPayable,secondRounfCoShare,preAuthAmount,consumptionDto,false,1.00,true);
    totalSelfPayable = creditAndSelfAmountDto.getSelfPayable();
    totalCreditPayable = creditAndSelfAmountDto.getCreaditPayable();
```

Only lines 1107-1108 change (`=` to `+ creaditPayableSecondRound` / `- creaditPayableSecondRound`); `creaditPayableSecondRound = netAmt` on line 1106 is untouched, as is everything else in the branch and the call itself.

**Verified by hand with the same example:** `totalCreditPayable` entering this branch is $300 (round 1's contribution). `creaditPayableSecondRound = netAmt = $400`. After the fix: `totalCreditPayable = 300 + 400 = $700`; `totalSelfPayable = 200 - 400 = -$200`. That negative result is a separate, real concern this fix does *not* address — see below.

**What this fix deliberately does NOT do:**
- It does not add a quantity-sufficiency check. Unlike the resDto-level quantity-cap branch (lines 798-826), which properly compares `resDto.getBalanceQuantity()` against `consumptionDto.getQuantity()` and splits `creaditPayable`/`selfPayable` proportionally when the cap is insufficient, this second-round branch has **no such comparison at all** — it treats any positive `secondRound.getBalanceQuantity()` as fully covering the entire `netAmt`, and hardcodes `1.00` as the quantity-to-deduct argument in the call three lines down regardless of how much was actually needed. This is a distinct, separate gap (not a "money vanishes" bug, more a "doesn't validate before assuming full coverage" bug, similar in shape to the previously-logged batch-2 hardcoded-quantity findings) and is called out below as a non-urgent finding in the same log entry, not folded into this fix.
- Relatedly, this fix does **not** guard against `totalSelfPayable` going negative (as the worked example above shows it can, precisely because of the missing quantity-sufficiency check just described) — accumulating correctly doesn't fix an already-wrong `creaditPayableSecondRound` value if the second contract's actual remaining capacity was less than `netAmt`. Whether that needs its own follow-up fix is a separate question from this one.
- It does not touch the amount-tracked-cap sibling branch (lines 1013-1024) — already correct, already the reference pattern this fix copies.
- It does not touch the first-priority (round 1) branches, the `resDto.getType()==1` second-round branch, or any of the taxCalculation/calculateSelfCreditAndFinalAmount call sites characterized in this same batch — all separate, unrelated code paths.
- It does not touch the already-fixed balance-cap swap bug (lines 744-745) — unrelated branch, already resolved and merged.

## Classification, per RULES.md

**Financial Correctness**: today, whenever a second-priority contract's own cap happens to be quantity-tracked (as opposed to amount-tracked), the entire dollar amount already attributed to the first-priority contract is discarded from the order's running credit total — real revenue that should be billed to a contract/insurer simply vanishes from the number the system itself believes it billed. After the fix, both contributions are correctly present in the total, matching the sibling branch's own accumulation pattern.

## Verification plan, if approved

1. Apply the two-line reassignment-to-accumulation fix.
2. Update `ConsumeServiceFromContractCharacterizationTest.SecondRoundTypeTwoQuantityCap#quantityCapBranch_totalCreditPayableAssignment` — currently asserts the call receives `totalCreditPayable=400.0` (pinning the loss); will need to assert `totalCreditPayable=700.0` (round 1's $300 + this round's $400) instead.
3. Confirm the amount-tracked-cap sibling test (`SecondRoundTypeTwoAmountCap`) and the type==1 sibling test (`SecondRoundTypeOne`) continue to pass unchanged — neither is touched by this fix.
4. Full `mvn clean test` across the complete reactor, confirming no other test regresses.
5. Log the outcome in `docs/BUSINESS_RULE_MANIFEST.md` as a completed Financial Correctness fix once approved and applied, and separately log the missing-quantity-sufficiency-check gap as its own non-urgent finding (already drafted, held pending this fix's resolution so the two aren't conflated).

No code has been changed yet. This document is the proposal only, awaiting owner review. Further targeted characterization of `consumeServiceFromContract` is paused pending this decision, per standing instruction.

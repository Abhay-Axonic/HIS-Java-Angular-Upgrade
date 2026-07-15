# Triage: remaining pending findings from the shadow-test harness work

*Written 2026-07-15, for a single owner sit-down. Covers everything still marked "Pending" in `docs/BUSINESS_RULE_MANIFEST.md` after the GRN null-tax-reference crash was split out and fixed separately (see `docs/DECISION_NEEDED_GrnNullTaxReferenceCrash.md`). No urgency on any of these — this document exists so you can go through them in one sitting instead of digging through session logs. Plain language throughout; each section links back to the full technical write-up in `docs/BUSINESS_RULE_MANIFEST.md` if you want more detail on any one of them.*

**How to read the "recommendation" in each section:** these aren't instructions — they're my read on which way each one leans, and why, so you have a starting point rather than a blank page. Several of these need a business-judgment answer only you (or your finance/accounting side) can actually give; I've said so plainly where that's the case rather than guessing.

---

## 1. Patient bill and insurance records can disagree on second-round contracts

**Where:** the shared calculation that splits a bill between the patient and their insurance/company. Full detail: `BR-PENDING-copay-preauth-split-return-value-gap`.

**What it is, plainly:** when a patient has a *second* insurance or company contract on top of their main one — and that second contract has its own co-pay percentage, or hits a pre-approved spending limit — the system correctly tells the insurance side "only charge this smaller, adjusted amount." But it never passes that same adjustment back to the number that ends up on the patient's own bill. The two records can end up disagreeing about the same charge.

**How often this actually matters:** unknown. This only triggers for the specific combination of a second-round contract *plus* either a co-pay percentage or a hit pre-authorization ceiling — not every patient, not every insurance case. I have no way to tell you how common that combination is without access to real billing history, which is separately on hold.

**My recommendation:** of everything on this list, this is the one I'd treat as genuinely worth prioritizing when you do sit down to decide — it's the only item here that's a real money-calculation disagreement, not a crash, a redundant write, or a misleading message. That said, I would NOT fix it blind. Before touching it, I'd want to either (a) get a rough sense from real billing history how often this combination occurs, once that access question is resolved, or (b) at minimum have someone who understands the insurance/co-pay business rules confirm what the *correct* combined behavior should be, since the fix touches 7 different places in the code that all call this same shared logic.

---

## 2. Two smaller, real inconsistencies between the "goods receipt" and "goods return" tax calculations

**Where:** the same two flows (Procurement's goods-receipt approval, Inventory's goods-return submission) where the crash was just fixed. These 2 are the ones left over from that same finding. Full detail: `BR-PENDING-grn-vs-returnable-gatepass-tax-divergence` (divergences 1 and 3).

### 2a. Tax is calculated differently depending on which direction goods are moving

**What it is, plainly:** when goods come *in* (a purchase receipt), the system subtracts any discount before calculating tax on the remaining amount. When goods go back *out* (a return to the vendor), the system calculates tax on the full amount, discount or no discount — even though the return record has the exact same discount information available, it's just never used.

**My recommendation:** this is a genuine "which one is right?" question, not a coding question — I can't tell you whether tax on a return should be calculated before or after a discount is factored in; that's a tax/accounting rule, not something inferable from the code. My suggestion: ask whoever handles your GST/tax compliance which is correct, then make both flows match that answer. Whichever way it goes, having the two disagree today is itself worth resolving eventually, since it likely means one of your two flows is currently calculating tax incorrectly.

### 2b. A cancelled/deleted line item still gets processed during goods-receipt approval

**What it is, plainly:** if a line item on a goods return has been marked as deleted, the system correctly skips it. The equivalent check does not exist on the goods-receipt side — every line item gets processed during approval, deleted or not.

**My recommendation:** this one leans toward "should probably just match the other flow" — skipping deleted records during an approval step is the kind of thing that looks like an oversight rather than a deliberate choice. Low urgency, and a genuinely small fix (the same one-line style of guard as the crash fix that already went in). Worth confirming with whoever built this flow whether "deleted" line items are ever actually expected to reach the approval step at all in normal use before treating it as urgent — if they never do in practice, this is a non-issue either way.

---

## 3. One payment-notification check can crash where two nearly-identical ones don't

**Where:** the main payment-collection process. Full detail: `BR-PENDING-collectpayment-unguarded-null-check`.

**What it is, plainly:** there are three very similar checks that decide whether to send an SMS notification after a payment. Two of them correctly handle the case where a certain field is left blank. The third does not, and will crash if that field is blank in that specific situation — the crash doesn't lose any money or corrupt any billing data, but it does stop that payment-collection request from completing normally.

**My recommendation:** low-risk, cheap fix — make the third check match the other two, which already work correctly. This doesn't touch any money calculation at all, only whether an SMS gets triggered, so there's very little to get wrong here. Good candidate to knock out quickly whenever you're doing a pass through this list, with minimal review needed.

---

## 4. Three smaller findings in the deposit-consumption process

**Where:** the process that draws down a patient's pre-paid deposit against a bill. Full detail: `BR-PENDING-consumedeposit-findings`.

### 4a. The system keeps writing to already-finished deposit records

**What it is, plainly:** when a payment draws from multiple deposit records, and the full amount owed is satisfied by the first one or two records, the system still goes through the motions of updating every remaining record in the list — even though nothing about them actually changes. It's a real, wasted database write repeated for every leftover record, not just a theoretical inefficiency.

**My recommendation:** low priority — this doesn't produce wrong numbers, it's pure waste. Worth fixing opportunistically (a small, low-risk change to stop the process once nothing more needs to be consumed) but not worth scheduling time for on its own.

### 4b. A "fill in the blank" check that only fires the opposite way

**What it is, plainly:** there's a step that's supposed to attach a bill reference onto a deposit record. As written, it only does this when the record *already* has some bill reference — it never fills one in when the field starts out blank, which is backwards from what that kind of check usually means.

**My recommendation:** needs a business-intent answer before touching it — I genuinely can't tell from the code whether this was written backwards by mistake, or whether it's deliberately narrow (only stamp a reference onto records that are already flagged as needing one). Worth a quick question to whoever knows this part of the deposit-consumption design; not urgent either way since I have no evidence of an actual missing-reference problem today.

### 4c. A line of code that reads a value and immediately writes the same value back

**What it is, plainly:** one line reads a field's current value and writes that exact same value straight back — it does nothing. Most likely a leftover from an earlier version of this code.

**My recommendation:** harmless, safe to just delete whenever someone's already in this file for another reason. Not worth a dedicated pass on its own.

---

## 5. Bill-save shows the wrong reason when it rejects a save

**Where:** the bill-creation screen's built-in double-check, before a bill is actually saved. Full detail: `BR-PENDING-savebill-misleading-conflict-message`.

**What it is, plainly:** when the system can't find matching data to double-check a bill against, it rejects the save and tells the user "this bill is already being edited by someone else" — even when that's not actually confirmed. The real reason could be something else entirely, like a data gap, and the message steers whoever's troubleshooting toward the wrong explanation.

**My recommendation:** low priority, but a reasonably easy improvement — worth fixing when convenient, mainly for the sake of not sending your staff chasing the wrong cause when this fires. Doesn't affect billing accuracy at all, only how the failure is explained.

---

## Summary table

| # | Finding | Touches money math? | My lean |
|---|---|---|---|
| 1 | Patient bill vs. insurance disagreement (co-pay/pre-auth) | **Yes** | Prioritize when you sit down, but confirm real-world frequency/correct behavior first — don't fix blind |
| 2a | Tax calculated differently for receipts vs. returns | **Yes** | Needs a tax/accounting answer, not a coding decision — ask, then align both |
| 2b | Deleted line items still processed on goods-receipt approval | No | Small, low-risk fix, likely worth doing — confirm it's reachable in practice first |
| 3 | One SMS-check crashes where two similar ones don't | No | Cheap, low-risk fix — good candidate to knock out quickly |
| 4a | Redundant deposit-record writes after amount is exhausted | No | Low priority, fine to fix opportunistically |
| 4b | Backwards-looking bill-reference check | No | Needs a business-intent answer first, not urgent |
| 4c | Dead-code no-op line | No | Harmless, delete whenever convenient |
| 5 | Misleading "already in process" message | No | Low priority, easy improvement when convenient |

Everything above is still exactly as found — nothing in this document has been changed in code. This is a reading list for your triage session, not a to-do list I've started on.

**Separately, a standing reminder, unrelated to this list:** the Razorpay payment-security fix from earlier this session still needs a real staging-environment checkout test before you consider that work fully closed out — that's on you to run manually, outside this environment, per your own earlier instruction. Flagging it again here so it doesn't get lost.

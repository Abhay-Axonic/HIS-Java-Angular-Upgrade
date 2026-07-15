# Decision needed: fix the GRN null-tax-reference crash in Procurement's goods-receipt approval

*Blueprint (docs only, no code applied). 2026-07-15. Split out from `BR-PENDING-grn-vs-returnable-gatepass-tax-divergence` in `docs/BUSINESS_RULE_MANIFEST.md` at the owner's explicit request: of that entry's 3 findings, this is the one judged an active operational risk rather than a hypothetical edge case, and is being fast-tracked for its own review rather than sitting in the slower 4-item triage list. The other 2 findings from that entry (discount netting, no soft-delete guard) stay in the slow list, untouched by this document.*

---

## The problem, in one sentence

Approving a goods-receipt note whose line item is missing its tax assignment crashes the **entire** approval — not just that one line — because Procurement's tax lookup dereferences the tax reference without checking for null first, unlike the identical lookup in Inventory's parallel flow, which already guards against exactly this.

## Where, precisely

`legacy/Procurement/src/main/java/com/param/procurement/purchase/service/goodReceiptNote/GoodReceiptNoteService.java:1752`, inside `approveGoodReceiptNote`:

```java
List<TaxComponentDetailsDto> taxDetailsList = grnDao.getTaxDetails(detail.getTax().getId());
```

`detail.getTax()` returns `null` for any `GoodReceiptNoteDetail` whose tax master reference was never set. Calling `.getId()` on that `null` throws a `NullPointerException`. This line sits **outside** the `try/catch` that wraps the rest of the per-tax-component loop (that `try/catch` starts a few lines later, around the tax-amount calculation) — so the exception isn't caught locally. It propagates out of `approveGoodReceiptNote` entirely, meaning **every other detail line in the same batch approval also fails**, even ones with a perfectly valid tax assignment. A single bad line item blocks the whole goods-receipt-note approval.

## Why this is judged active, not hypothetical

Confirmed via a passing test in this session's characterization suite (`GoodReceiptNoteServiceCharacterizationTest.NullTaxReference#throwsNullPointerException_whenTaxIsNull`, committed on `main`) that this is real, reproducible current behavior — not a theoretical code-reading concern. Whether it has actually fired in production wasn't investigated this session (that would require access to real transaction data, which is separately on hold per the owner's Decision 3 on `docs/DECISION_NEEDED_ShadowTestHarnessApproach.md`) — but the code path is real, reachable through normal use (any line item that was never assigned a tax master, which the schema evidently allows since the field is nullable), and its failure mode (blocking an entire batch approval, not just the affected line) makes it worth fixing on its own timeline rather than waiting for the slower triage.

## The proposed fix — minimal, not a redesign

Change line 1752 from:

```java
List<TaxComponentDetailsDto> taxDetailsList = grnDao.getTaxDetails(detail.getTax().getId());
```

to:

```java
List<TaxComponentDetailsDto> taxDetailsList = grnDao.getTaxDetails(detail.getTax()!=null ? detail.getTax().getId():0);
```

This is not a new pattern being invented for this fix — it is **the exact line already in production** at `Inventory/src/main/java/com/param/inventory/store/service/returnableGatepass/ReturnableGatepassService.java:1011`, in the sibling "goods return" flow this session already characterized. Stronger than "a matching pattern": Inventory's `grnDao` field is the *same* `IGoodReceiptNoteDao` type (`com.param.procurement.purchase.dao.goodReceiptNote.IGoodReceiptNoteDao`) that Procurement's `GoodReceiptNoteService` already uses — both modules call into the identical DAO method. Inventory already calls `getTaxDetails(0)` successfully today whenever a return line has no tax reference; this fix makes Procurement do the exact same thing, through the exact same DAO call, not a new or unproven code path.

**What happens after the fix, for a null-tax line:** `getTaxDetails(0)` returns whatever the DAO returns for an unmatched id — based on Inventory's confirmed current behavior, this is an empty list. The surrounding `if(taxDetailsList != null && taxDetailsList.size()>0)` guard then skips tax-component processing entirely for that one line, and the loop continues to the next detail. **No tax component records are created for a line with no tax assignment — same as today's Inventory behavior, and arguably the only sane thing to do with no tax to apply.** The rest of the batch (all other detail lines, and the goods-receipt-note status update itself) proceeds normally instead of aborting.

**What this fix deliberately does NOT do:**
- It does not touch the discount-netting divergence (finding 1 of the original 3) — that stays in the slow triage list, a real business-rule question about whether goods-receipt and goods-return tax should differ, not something this fix decides.
- It does not add a soft-delete guard (finding 3) — also staying in the slow triage list.
- It does not change what happens for a line that *does* have a tax assignment — behavior for the common case is completely unchanged.
- It does not touch Inventory's file at all (already correct).

## Classification, per RULES.md

**Behavioral Change** (not "Refactoring (Safe)"): today, a null-tax line item throws and aborts the whole batch; after this fix, that same line silently proceeds with no tax component recorded, and the rest of the batch succeeds. That is a real, visible difference in outcome for the specific case of a missing tax assignment — not a byte-identical refactor — so it needs a `docs/BUSINESS_RULE_MANIFEST.md` entry and the owner's explicit sign-off before it ships, per RULES.md's own definition. It is not "Financial Correctness" in the RULES.md sense (no amount charged or billed changes for any line that already has a valid tax reference), but flagging it as a full Behavioral Change regardless, since a batch that used to fail now succeeds.

## Verification plan, if approved

1. Apply the one-line change.
2. Re-run `GoodReceiptNoteServiceCharacterizationTest` — the existing `throwsNullPointerException_whenTaxIsNull` test will need to be replaced with a test asserting the line is now skipped gracefully (no `updateTaxComponentDetails`/`saveTaxComponentDetails` call, no exception, the rest of the batch's `updateGoodReceiptNote` call still happens) — mirroring the equivalent already-passing behavior already pinned for Inventory.
3. Full `mvn -pl Procurement test` clean run, confirming no other test regresses.
4. Log the outcome in `docs/BUSINESS_RULE_MANIFEST.md` as a completed Behavioral Change once the owner approves and it's applied, per the classification above.

No code has been changed yet. This document is the proposal only, awaiting owner review on its own timeline, separate from the other 4 findings.

# Decision Needed: Razorpay 10x Undercharge (AUDIT_BillingRisk.md ONE-OFF-1) — Current Status, and What Still Needs Your Sign-Off

**Prepared:** 2026-07-15, at owner's explicit request, treated as highest priority independent of migration timeline. No application code was touched to produce this document (docs-only session, per owner instruction to hold all billing-math and shadow-test work until this is reviewed).

**Read this first — the headline is not what it may look like from the audit alone:** the code-level conversion bug is **already fixed and already owner-approved** (BR-018, approved 2026-07-04), and the fix is confirmed present in the current source tree as of today. This document exists because three real things are *still* open: (1) whether any historical transaction was actually undercharged before the fix and needs financial reconciliation, (2) whether the fix is actually running on any live/deployed instance, not just in source, and (3) a second, structurally different amount-trust risk in the same file that was explicitly out of scope of the 2026-07-04 fix. Sections 4–6 below are where your decision is actually needed; sections 1–3 are background you may already know.

---

## 1. The bug itself

**Location:** `legacy/Billing/src/main/java/com/param/billing/global/controller/RazorPaymentController.java`, two identical sites: `razorPayPaymentLink` (was lines 398–409) and `getOrderIdForRazorPayPaymentOnBilling` (was lines 508–519).

**Mechanism:** Razorpay's API requires the amount in paise (smallest currency unit — ₹1.00 = 100 paise). The old code converted rupees to paise by taking the string form of the amount and concatenating its two halves around the decimal point, instead of multiplying by 100:

```java
String s = reqDto.getRazorAmount().toString();
Integer manupulateAmount = 0;
String[] array = s.split("\\.");
String SecondData = "0";
if (array.length > 1 && !array[1].equals(SecondData)) {
    s = array[0] + array[1];
    manupulateAmount = Integer.parseInt(s);
} else {
    s = array[0] + "00";
    manupulateAmount = Integer.parseInt(s);
}
```

`Double.toString()` produces the *shortest* decimal string that round-trips to the same value — for an amount like `450.50`, that string is `"450.5"` (one fractional digit, not two). Concatenating `"450"` and `"5"` gives `"4505"` paise (₹45.05) instead of the correct `45050` paise (₹450.50).

**Who is affected:** any amount whose paise component is a multiple of 10 — i.e. anything ending in `.10`, `.20`, `.30`, … `.90`. Amounts with two genuine fractional digits (e.g. `123.45`) were already correct, because `Double.toString()` naturally produces two digits for those. This is why the bug is easy to miss in spot-checks — most "random" test amounts happen to work.

**Impact:** a **90% silent undercharge** on affected amounts — not a rounding artifact, a tenth of the intended charge, sent directly to Razorpay's order/payment-link API (`orderRequest.put("amount", manupulateAmount)`).

## 2. Fix status — already implemented and approved

| Date | What happened | Source |
|---|---|---|
| 2026-07-04 | Bug discovered via whole-codebase audit, flagged URGENT | `docs/AUDIT_BillingRisk.md` ONE-OFF-1 |
| 2026-07-04 | Independent Checker re-derived the audit's claim from scratch (own standalone program, 10 test amounts) — confirmed real, not exaggerated | `docs/SESSION_LOG.md` (2026-07-04, Checker entry) |
| 2026-07-04 | Fix implemented on branch `fix/razorpay-amount-conversion-bug`: both sites replaced with `BigDecimal.valueOf(reqDto.getRazorAmount()).multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).intValue()` | `docs/REVIEW_razorpay-amount-fix.md` |
| 2026-07-04 | Independent Checker re-verified the fix (own compile, own 10-case test program, confirmed zero remaining occurrences of the buggy pattern anywhere in the file) — verdict "Approve with concerns" (a manifest-classification wording gap, resolved by amending `RULES.md`, not by rejecting the fix) | `docs/SESSION_LOG.md` (2026-07-04, Checker entry) |
| 2026-07-04 | Logged in `docs/BUSINESS_RULE_MANIFEST.md` as **BR-018**, classified **Financial Correctness**, **Owner Decision: Approved** (quoted: *"Owner explicitly approved the corrected rupee-to-paise conversion, understanding this fixes the bug going forward only and does not reconcile any historical undercharged transactions."*) | `docs/BUSINESS_RULE_MANIFEST.md` BR-018 |

**`BigDecimal.valueOf(double)` (not `new BigDecimal(double)`) was used deliberately** — it goes through `Double.toString()` first, avoiding the binary-floating-point noise the other constructor would introduce (the same anti-pattern flagged separately in `AUDIT_BillingRisk.md` RISK-1 and ONE-OFF-5). `.setScale(0, RoundingMode.HALF_UP)` rounds to the nearest whole paisa rather than truncating.

**Worked verification (from `docs/REVIEW_razorpay-amount-fix.md`, independently reproduced by the Checker):**

| Amount (₹) | Old buggy result | New fixed result | Correct value |
|---|---|---|---|
| 450.50 (the original report case) | 4505 paise (₹45.05) | **45050 paise** | 45050 paise ✅ |
| 200.10 | 2001 paise | **20010 paise** | 20010 paise ✅ |
| 123.45 (already correct before) | 12345 | 12345 | 12345 ✅ |

## 3. Verification performed today (2026-07-15), independent of the above

Before writing this document, I re-read both fix sites directly in the current working tree rather than trusting the manifest's account:

- `razorPayPaymentLink` (now lines 400–401): confirmed `BigDecimal razorAmountInRupees = BigDecimal.valueOf(reqDto.getRazorAmount()); Integer manupulateAmount = razorAmountInRupees.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).intValue();` — matches the approved fix exactly.
- `getOrderIdForRazorPayPaymentOnBilling` (now lines 500–501): identical pattern confirmed.
- Full-file search confirmed **zero occurrences of `.split("\\.")`** anywhere in `RazorPaymentController.java` — the buggy pattern is gone, not just superseded at the two known sites.
- This file, in this exact fixed state, is part of the git baseline commit (`96b75c4`, "Baseline before upgrade") created earlier today when this repository was first put under version control — so **this is the code that is in `main` right now.**

**Correction flagged for `docs/AUDIT_BillingRisk.md`:** that document still describes ONE-OFF-1 in the present tense as *"a live wrong-amount bug ... charging patients one-tenth of the bill ... silently, right now"* and lists it as the #1 urgent item needing an owner decision. That framing is now stale — the audit and the fix were both dated 2026-07-04, and it appears the audit was written before the fix landed later the same day, then never revisited. I have added a short dated correction note at the top of that document's ONE-OFF-1 section pointing here and to BR-018, so future sessions reading the audit don't re-flag an already-fixed bug as unresolved. I did not rewrite the rest of the audit — everything else in it (RISK-1 through RISK-6, ONE-OFF-2 through ONE-OFF-5) is still an open, unfixed finding as far as I have verified.

---

## 4. What is NOT resolved — historical transaction reconciliation

The fix prevents the bug **going forward only**. Nothing has checked whether any real transaction was actually undercharged by it before 2026-07-04. This is explicitly called out as out-of-scope by both the manifest entry and the review doc, and no follow-up investigation has happened since.

**What I don't know and need from you:** has this system processed any transaction through `razorPayPaymentLink` or `getOrderIdForRazorPayPaymentOnBilling` against a *real* Razorpay account (live keys) before 2026-07-04 — as opposed to only local/dev/test runs? `CLAUDE.md` describes this application as "live-critical (currently non-production/dev)," which suggests real patient billing may not have gone live yet — if that's accurate, this section may be moot (zero real-world impact). But I have not independently confirmed that, and getting this wrong in either direction matters: over-investigating a system that never took real payments wastes effort; under-investigating one that did leaves real undercharged patients/insurers unreconciled.

**If real transactions did occur before the fix, the reconciliation approach would need to be:**
- Query `billing.m_razorpay_order_log_details` (and/or the payment-link log table `razorPayService.savePaymentLinkLog` writes to) for records created before 2026-07-04 where the logged rupee amount, when run through the *old* buggy conversion, would differ from the amount actually sent to Razorpay.
- Cross-reference against Razorpay's own dashboard/API for the actual settled amount on each such order, since the log table only proves what this system *intended* to send, not always what Razorpay actually received.
- Decide what "reconciliation" means for any confirmed gap: re-bill the patient the difference, absorb it, or something else — a financial/collections policy decision, not a code decision.

**I am not starting this investigation without your direction** — even the read-only DB query touches real patient/payment records, and per your explicit instruction I'm holding all further work until you've seen this document.

## 5. What is NOT resolved — deployment verification

A separate, unrelated audit (`BR-074`, 2026-07-11) found that the shared dev container (`his-local-tomcat`) was at that time running **ahead of** git — 76 files' worth of uncommitted/stashed changes deployed live but never committed, plus a `jdbc.properties` in the container pointing at a real remote QA Postgres host with live credentials. That finding is about different files, but it establishes a documented precedent that **"the code is fixed in git" and "the code that is actually running" are not guaranteed to be the same thing** in this project's current setup.

I have not checked whether any deployed/running instance (dev, QA, or otherwise) actually has this Razorpay fix, or is still running the pre-fix jar. That's a live-environment check (SSH/container inspection or a jar `diff`, similar to what BR-074's session did for other files), not something visible from source alone.

## 6. What is NOT resolved — the two "out of scope" endpoints

Two other endpoints in the same file, **`getOrderIdForRazorPayPayment`** and **`getOrderIdUnitSubscriptionPay`**, were explicitly excluded from the 2026-07-04 fix because they don't do the string-split conversion at all — they take an **already-converted `Integer` amount directly from the client request** (`reqDto.getRazorPayBillAmount()`) and pass it straight to Razorpay with **no server-side computation or validation of any kind**:

```java
orderRequest.put("amount", reqDto.getRazorPayBillAmount()); // amount in the smallest currency unit
```

Per `docs/REVIEW_razorpay-amount-fix.md`: *"a different, unvalidated-client-trusted-amount risk, not the string-split bug this fix addresses."* This means a client (browser, or anyone able to call the endpoint directly) can currently send **any integer amount it wants** for these two order-creation paths — the server never independently computes or checks it against the actual bill/subscription total. This is a materially different and, on its face, more serious risk than the fixed bug: not an arithmetic error, but a missing trust boundary.

This was correctly out of scope for a "fix the arithmetic" change, but it has not been picked up as its own decision item since. I have not written a full decision document for it yet — flagging it here so it doesn't quietly fall through the cracks now that ONE-OFF-1 looks "handled."

---

## What we need from you — status as of 2026-07-15 (later same day)

1. **Historical reconciliation (§4):** Owner will confirm separately whether this system has processed any live (non-test) Razorpay payments before 2026-07-04. **No DB query has been run** — holding per owner instruction until that answer comes back.
2. **Deployment verification (§5):** **On hold at owner's explicit instruction** — flagged as open, to be revisited later. No live/deployed-instance check has been performed.
3. **The two out-of-scope endpoints (§6):** Owner requested this be written up as its own decision document, treated with the same priority as this one. Done — see `docs/DECISION_NEEDED_RazorPayClientTrustedAmount.md`. That investigation went a layer deeper than the two endpoints alone (traced what consumes their output) and surfaced a more severe, multi-part finding: a disconnect between the amount charged and the amount recorded as paid, zero Razorpay payment-signature verification anywhere in the codebase, and — based on reading the auth filter, not yet runtime-confirmed — these endpoints appearing reachable without login. See that document for full detail; it is now the higher-priority of the two open Razorpay documents.

No code, schema, or billing math has been touched to produce this document, per your instruction.

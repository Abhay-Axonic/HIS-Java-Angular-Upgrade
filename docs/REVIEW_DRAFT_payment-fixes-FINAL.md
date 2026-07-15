# DRAFT — Combined Payment Fixes — NOT APPLIED, NOT COMMITTED, NOT DEPLOYED

**Status: draft for owner review only.** Nothing in this document has been applied to any file under `legacy/`. This document itself is documentation (governance-file carve-out) — the diffs it contains describe application-code changes needing Maker-Checker + owner sign-off before merging, per `RULES.md`.

**⚠ Open verification gap (2026-07-15), read before treating §3 as production-ready:** §1/§2a/§2b/§3/§4 were applied and locally verified via `mvn` compile, `node --check`, and a standalone cryptographic test of the exact `Utils.verifyPaymentSignature` call path (5/5 cases pass — genuine accept, tampered signature/order_id/payment_id/wrong-secret all correctly reject). **That crypto test does not exercise §3's DTO extension or the frontend change together, against a real Razorpay checkout, through the actual deployed application.** No Docker/Postgres/real Razorpay credentials were available in the session that applied this fix, so the live checkout → `PaymentReceiptDetailsDto`'s new `razorpayOrderId`/`razorpaySignature` fields → `saveBillFromKiosk`/`saveBillFromKioskFutureDate` path has **not** been exercised end to end anywhere. The owner explicitly chose to proceed on the crypto-test result alone for this checkpoint rather than block on standing up that infrastructure mid-session — **but §3 must get a real staging test (live Razorpay test-mode checkout, real browser, the actual deployed frontend+backend together) before this reaches production.** The specific risk if skipped: §3's fix and its frontend companion were designed to ship together (see §3 below) precisely because a version-skew rollout would silently reject every real Razorpay payment — that failure mode can only be caught by an actual checkout attempt, not by the crypto test alone.

**Supersedes/consolidates:** `docs/REVIEW_DRAFT_razorpay-auth-fix-NOT-APPLIED.md` §1 (unchanged, carried forward as final) and its corrected §2 (dropped — was JWT-based, confirmed unsafe; replaced entirely by the signature-based approach below). Covers `docs/DECISION_NEEDED_RazorPayClientTrustedAmount.md` in full, including the `saveAndPay` gap that document's §5 flagged as unexamined.

**One genuine correctness question flagged, not guessed through** (§2c below): Razorpay Payment Links' signature payload format isn't derivable with confidence from the vendored SDK alone. Flagged clearly with options; not silently worked around.

---

## Executive summary

| # | Item | Status | Risk if applied | Recommendation |
|---|---|---|---|---|
| §1 | `confirmAppointment` — signature + authoritative amount | Final, unchanged from prior draft | Low | Apply |
| §2a | `confirmPayment` — signature + authoritative status | New, drafted below | Low | Apply |
| §2b | `confirmAppointment1` — dead test endpoint | Recommend deletion | Low (no legitimate caller found) | Apply (or hold if you want to double-check for hidden callers first) |
| §2c | `getRazerPay` — Payment Links signature | **Cannot draft with confidence** | N/A | **Needs your input — see §2c** |
| §3 | `saveAndPay`/`saveAndPay/future` — Razorpay-mode verification | New, drafted below | Medium (touches DTO + frontend, not just backend) | Apply, but see the frontend-change caveat |
| §4 | `saveAndPay` — non-gateway (cash/card/cheque) modes | Two options presented, not applied | Option A: none (status quo). Option B: medium (introduces new auth requirement) | **Recommend Option A for now — see §4** |

---

## §1 — `confirmAppointment` (unchanged, carried forward)

Full detail in `docs/REVIEW_DRAFT_razorpay-auth-fix-NOT-APPLIED.md` §1. Signature verification via `com.razorpay.Utils.verifyPaymentSignature` + re-fetching the order from Razorpay's API (`Orders.fetch`) to use its `status`/`amount_paid` as the authoritative source, replacing the previously-trusted client-asserted amount. No change to that draft. **Confidence: high** — verified against the actual vendored SDK source (`razorpay-java-1.3.1`).

---

## §2 — `RazorPaymentController.java`, remaining endpoints

### §2a — `confirmPayment`: add signature verification before `updatePaymentLinkLog`

**Current code (lines 525-542):**
```java
@RequestMapping(value="/confirmPayment", method=RequestMethod.POST)
public ModelAndView confirmPayment(HttpServletRequest request, Model model) {
    ModelAndView modelAndView = new ModelAndView("web-appointment-confirmation");
    try {
        String razorPayPaymentId = request.getParameter("razorpay_payment_id");
        String razorPayOrderId = request.getParameter("razorpay_order_id");
        String razorPaySignature = request.getParameter("razorpay_signature");
        String status = "paid";
        razorPayService.updatePaymentLinkLog(status,razorPayOrderId);
    return null;
    } catch (Exception e) { ... }
}
```

All three Razorpay params are read and then **completely ignored** — the log is marked "paid" unconditionally based on nothing but the caller having hit this URL.

**Blocker found while drafting this fix:** verifying the signature requires the unit's Razorpay key secret, which requires knowing which unit/organization this order belongs to — and unlike `confirmAppointment` (which gets this via `getAppointmentDataByRazorPayOrderId`), there is **no existing method** to look up unit/organization context by `order_id` for this flow. `m_razorpay_order_log_details` (the table both flows share) does have `unit_id`/`organization_id`/`amount` columns (confirmed via `RazorPayOrderLogDetail.java`), so this is a small, safe, additive read method — not a structural problem, just a missing piece.

**New DAO method needed** (`RazorPayDaoImpl.java`, following the exact pattern already used by `getAppointmentDataByRazorPayOrderId` two methods above it):
```java
@Override
public Response getOrderLogDetailsByOrderId(String orderId) throws ApplicationException {
    try {
        RazorPayOrderLogDetail detail = (RazorPayOrderLogDetail) sf.getCurrentSession()
            .createSQLQuery("select unit_id as \"unitId\", organization_id as \"organizationId\", amount as \"amount\" "
                + "from m_razorpay_order_log_details where order_id = :orderId")
            .setParameter("orderId", orderId)
            .setResultTransformer(Transformers.aliasToBean(RazorPayOrderLogDetail.class)).uniqueResult();
        return new Response<>(SUCCESS, null, COMMON_SUCCESS, null, detail);
    } catch (HibernateException he) {
        logger.error("getOrderLogDetailsByOrderId failed", he);
        throw new ServiceException(he.getMessage());
    } catch (Exception e) {
        logger.error("getOrderLogDetailsByOrderId failed", e);
        throw new ServiceException(e.getMessage());
    }
}
```
(Plus the matching one-line addition to `IRazorPayDao.java` and `IRazorPayService.java`/`RazorPayServiceImpl.java` — omitted here for brevity, same pattern as the other methods in those files.)

**`confirmPayment` fix:**
```diff
 @RequestMapping(value="/confirmPayment", method=RequestMethod.POST)
	public ModelAndView confirmPayment(HttpServletRequest request, Model model) {
	       ModelAndView modelAndView = new ModelAndView("web-appointment-confirmation");
		try {
			
			String razorPayPaymentId = request.getParameter("razorpay_payment_id");
			String razorPayOrderId = request.getParameter("razorpay_order_id");
			String razorPaySignature = request.getParameter("razorpay_signature");
-			String status = "paid";
-			razorPayService.updatePaymentLinkLog(status,razorPayOrderId);
-		return null;
+
+			Response orderLogRes = razorPayService.getOrderLogDetailsByOrderId(razorPayOrderId);
+			if (orderLogRes.getObject() == null) {
+				logger.error("PAYMENT_RECONCILIATION_GAP: no order log found for " + razorPayOrderId + " on /confirmPayment -- log NOT updated.");
+				return null;
+			}
+			RazorPayOrderLogDetail orderLog = (RazorPayOrderLogDetail) orderLogRes.getObject();
+			Response appConfigRes = iUnitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(buildConfigReqDto(orderLog.getOrganizationId(), orderLog.getUnitId()));
+			UnitApplicationConfigurationDto unitConfig = appConfigRes.getObject() != null ? (UnitApplicationConfigurationDto) appConfigRes.getObject() : null;
+			if (unitConfig == null) {
+				logger.error("PAYMENT_RECONCILIATION_GAP: no unit Razorpay config found for order " + razorPayOrderId + " on /confirmPayment -- log NOT updated.");
+				return null;
+			}
+			try {
+				org.json.JSONObject sigAttrs = new org.json.JSONObject();
+				sigAttrs.put("razorpay_order_id", razorPayOrderId);
+				sigAttrs.put("razorpay_payment_id", razorPayPaymentId);
+				sigAttrs.put("razorpay_signature", razorPaySignature);
+				if (!Utils.verifyPaymentSignature(sigAttrs, unitConfig.getRazorPaySecretKey())) {
+					logger.error("PAYMENT_RECONCILIATION_GAP: invalid Razorpay signature on /confirmPayment for order " + razorPayOrderId + " -- log NOT updated.");
+					return null;
+				}
+			} catch (Exception e) {
+				logger.error("PAYMENT_RECONCILIATION_GAP: signature verification threw on /confirmPayment for order " + razorPayOrderId + ". " + e.getMessage(), e);
+				return null;
+			}
+			razorPayService.updatePaymentLinkLog("paid", razorPayOrderId);
+			return null;
		} catch (Exception e) { ... unchanged ... }
	}
```

**Separate, pre-existing bug found while reading this code — flagged, NOT fixed here, out of scope:** `updatePaymentLinkLog(status, razorpayPaymentLinkId)`'s SQL matches on `unique_id` (`RazorPayDaoImpl.java` line 159: `WHERE unique_id = :razorpayPaymentLinkId`), and `unique_id` is populated at order-creation time from Razorpay's **payment-link ID** (`razorPayPaymentLink`'s `reqDto.setUniqueId(entry.getKey())`, sourced from `Mpesa.RazorPaytransactionStatus(...)`'s response). `confirmPayment` passes `razorPayOrderId` (a **Razorpay order ID**, a different value/format) into this same parameter. This means the `UPDATE ... WHERE unique_id = :razorPayOrderId` almost certainly matches zero rows in practice — `confirmPayment`'s log update appears to already be a silent no-op today, independent of the security issue. This doesn't need fixing as part of *this* patch (verifying a payment before an already-broken no-op update doesn't create new risk), but it means `confirmPayment`'s practical value going forward should be double-checked separately — possibly `getRazerPay` (§2c) is the actual live path for this flow and `confirmPayment` is vestigial, similar to `confirmAppointment1`.

**Confidence: high** on the signature-verification mechanics (same verified SDK call as §1). **Medium** on whether this endpoint is even meaningfully exercised in production, given the bug just described — recommend confirming via logs/traffic before prioritizing this one highly.

### §2b — `confirmAppointment1`: recommend deletion (dead test code)

```java
@RequestMapping(value="/confirmAppointment1", method=RequestMethod.POST)
public @ResponseBody Response confirmAppointment1(HttpServletRequest request, Model model) {
    Response razorPayRes = razorPayService.getAppointmentDataByRazorPayOrderId("order_HREZNzb6cSwewS");
    return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, razorPayRes.getObject());
}
```

Takes no request parameters at all, ignores whatever request body is sent, and unconditionally returns whichever appointment's data is tied to one **hardcoded** Razorpay order ID (`order_HREZNzb6cSwewS`) — reads as a debug/test leftover, not a real endpoint. Searched the whole `legacy/` tree (Java and JS) for any caller of `/confirmAppointment1` — found none. Its only function today is an unauthenticated information-disclosure surface for whichever real patient/appointment that specific hardcoded order happens to belong to.

**Proposed fix: delete the method entirely.** No `@Deprecated`/no-op shim needed since nothing calls it. If you'd rather not delete outright (e.g., want to grep production access logs first to be certain nothing external depends on it), the fallback is to gate it the same way as §2a — but deletion is the more honest fix for what's clearly unused code, and per your codebase's own conventions (see `AUDIT_WholeCodebase_patterns.md`) dead code is generally flagged for removal, not preserved with a security patch bolted onto it.

### §2c — `getRazerPay`: genuine correctness question, not resolved — flagging per your instruction rather than guessing

```java
@RequestMapping(value = "/getRazerPay", method = RequestMethod.GET)
public @ResponseBody Response getRazerPay(@RequestParam(value = "razorpay_payment_id") String razorpay_payment_id,
        @RequestParam(value = "razorpay_payment_link_id") String razorpay_payment_link_id,
        @RequestParam(value = "razorpay_payment_link_reference_id") String searchPar,
        @RequestParam(value = "razorpay_payment_link_status") String razorpay_payment_link_status,
        @RequestParam(value = "razorpay_signature") String razorpay_signature) {
    ...
    razorPayService.updatePaymentLinkLog(razorpayPaymentLinkStatus, razorpayPaymentLinkId);
    return null;
}
```

This is a GET endpoint receiving **Razorpay Payment Links** redirect parameters (distinct from the Order-based flow §1/§2a fix) — `razorpay_payment_link_id`, `razorpay_payment_link_reference_id`, `razorpay_payment_link_status`, alongside `razorpay_payment_id`/`razorpay_signature`. It marks the log "paid" (or whatever status the caller claims) based entirely on the client-supplied `razorpay_payment_link_status` string, with **zero verification** — the worst of the endpoints examined, since it doesn't even hardcode "paid" like `confirmPayment` did; it trusts the status value itself.

**Why I'm not drafting a fix here:** `com.razorpay.Utils` (the vendored SDK, verified via its actual source) provides `verifyPaymentSignature(JSONObject, String)` — built specifically for the **Orders** flow's payload shape (`razorpay_order_id + "|" + razorpay_payment_id`) — and a generic `verifySignature(payload, signature, secret)` primitive. It does **not** provide a Payment-Links-specific convenience method, and Payment Links' actual signature payload construction (which fields, in what order, with what separator) is documented by Razorpay externally — not present anywhere in this vendored SDK's source, and I have no internet access in this session to confirm it against Razorpay's current API documentation. Writing a payload format from memory/assumption for a security-critical HMAC check is exactly the kind of guess that could either (a) always fail and break a legitimate flow, or (b) worse, be constructed wrong in a way that's bypassable — this is precisely the "genuine correctness question you can't resolve from the code itself" you told me to flag rather than push through.

**What I need from you:** either (a) confirm Razorpay's current Payment Links signature payload format (from their API docs) so I can draft this with real confidence, or (b) tell me to treat this endpoint the same as the non-gateway-verified payment modes in §4 (require it to come from an authenticated context, since we can't cryptographically verify it) as an interim measure, or (c) deprioritize this specific endpoint if payment links aren't actually in active use (worth checking — `razorPayPaymentLink`, the endpoint that *creates* these links, requires `unitApplicationConfigurationDto.getPayGatewayProviderId() == 1`, a specific gateway-provider configuration that may or may not be turned on for any real unit today).

### Order-creation endpoints (`getOrderIdForRazorPayPayment`, `getOrderIdUnitSubscriptionPay`) — still out of scope, unchanged reasoning

As in the prior draft: true fee validation at order-creation time requires a server-known fee/price source I could not identify with confidence (candidates exist, e.g. `/api/billing/unit/getDoctorCharges`, but I have not traced whether it's the correct current source of truth for either flow). §1/§2a's confirmation-time verification closes the more severe "no proof any payment happened" gap regardless, but a determined actor could still pay a real, verified, low amount for a real service without this. Unchanged from before — not re-drafted here.

---

## §3 — `saveAndPay` / `saveAndPay/future`: Razorpay-mode verification

**STAGING TEST REQUIRED BEFORE PRODUCTION (added 2026-07-15):** everything below this point has been compiled and unit-level-verified (the cryptographic core, in isolation) but **not exercised end to end**. Before this reaches production, run an actual test-mode Razorpay checkout through the real deployed frontend+backend together: confirm the new `razorpayOrderId`/`razorpaySignature` fields flow correctly from `selfRegistrationController.js`'s checkout handler into the `saveAndPay` request body, and that `verifyRazorpayPayment` accepts that real, non-synthetic response. This is the one part of the whole fix set where a version-skew or wiring mistake would be silent in code review but immediately break every real Razorpay payment in production — see the rollout-risk paragraph below, which this note reinforces rather than duplicates.

**Blocker found while drafting this fix, load-bearing for the design below:** `PaymentReceiptDetailsDto` (`legacy/Global/src/main/java/com/param/global/billing/dto/PaymentReceiptDetailsDto.java`, read in full) has **no field carrying `razorpay_order_id` or `razorpay_signature`** — only `transactionNo` (used for `razorpay_payment_id`, confirmed from `confirmAppointment`'s usage: `paymentReceiptDetailsDto.setTransactionNo(razorPayPaymentId)`). **This means a Razorpay payment genuinely cannot be cryptographically verified from what the client sends to `saveAndPay` today** — `Utils.verifyPaymentSignature` needs all three of order_id/payment_id/signature, and only one of the three ever reaches this endpoint. Verifying this properly requires extending the contract, not just the backend method:

1. **DTO addition** (additive, non-breaking — existing serialization unaffected since new fields default to null when absent):
```diff
 public class PaymentReceiptDetailsDto {
 	...
 	private String transactionNo;
+	private String razorpayOrderId;
+	private String razorpaySignature;
 	...
+	public String getRazorpayOrderId() { return razorpayOrderId; }
+	public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }
+	public String getRazorpaySignature() { return razorpaySignature; }
+	public void setRazorpaySignature(String razorpaySignature) { this.razorpaySignature = razorpaySignature; }
```

2. **Frontend change required** — `selfRegistrationController.js`'s Razorpay checkout `handler` callback currently only captures `response.razorpay_payment_id`:
```diff
 "handler": function (response) {
     $scope.$apply(function () {
         $scope.trasactionIdFromRazorPay = response.razorpay_payment_id;
         $scope.paymentRazor = {
             paymentModeId: 8,
             transactionNo: $scope.trasactionIdFromRazorPay,
             transactionType: 'Razor Pay',
-            amount: $scope.patientAppoObjPayment.totalBillAmount
+            amount: $scope.patientAppoObjPayment.totalBillAmount,
+            razorpayOrderId: response.razorpay_order_id,
+            razorpaySignature: response.razorpay_signature
         };
```
Razorpay's Checkout.js `handler` callback always provides `razorpay_payment_id`, `razorpay_order_id`, and `razorpay_signature` together on a successful payment (this is the standard, documented shape of that callback, unlike §2c's payload-format question) — the frontend already receives these values, it just wasn't reading two of the three. **This is a real, if small, frontend change bundled with the backend fix** — flagging clearly since your original scoping said "saveBillFromKiosk / saveBillFromKioskFutureDate," and this makes the fix's footprint one file larger than that.

3. **Backend fix** (`BillingMasterController.java`, both `saveBillFromKiosk` and `saveBillFromKioskFutureDate` — shown for `saveBillFromKiosk`, identical addition needed in the other):
```diff
 @RequestMapping(value = "/kiosk/saveAndPay", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
 public synchronized @ResponseBody Response saveBillFromKiosk(@RequestBody EncounterMasterDto encounterMasterDto) {
 	try {
+		if (encounterMasterDto.getPaymentReceiptDetailsDtosList() != null) {
+			for (PaymentReceiptDetailsDto payDto : encounterMasterDto.getPaymentReceiptDetailsDtosList()) {
+				if (payDto.getPaymentModeId() != null && payDto.getPaymentModeId() == 8) { // Razorpay
+					Response verifyRes = razorPayPaymentVerificationService.verifyRazorpayPaymentClaim(
+						payDto.getRazorpayOrderId(), payDto.getTransactionNo(), payDto.getRazorpaySignature(),
+						payDto.getAmount(), encounterMasterDto.getUnitId(), encounterMasterDto.getOrganizationId());
+					if (!SUCCESS.equals(verifyRes.getStatus())) {
+						logger.error("PAYMENT_RECONCILIATION_GAP: Razorpay payment claim failed verification on /kiosk/saveAndPay -- bill NOT saved. " + verifyRes.getMessage());
+						return new Response(ERROR, ERROR_CODE, "Payment could not be verified", null, null);
+					}
+				}
+			}
+		}
 		int isPaid = 0;
 		...
```
(A new shared `verifyRazorpayPaymentClaim(...)` helper — signature verification + `Orders.fetch`/amount check, factored out of §1's inline logic so it isn't duplicated a third time across `confirmAppointment`, `confirmPayment`, and here. Not written out in full in this draft to avoid repeating §1's already-verified logic three times; happy to write the extracted helper in full before this is applied if you'd like a preview.)

**What could break:** any *existing* in-flight or already-completed Razorpay payments made before this ships would have `razorpayOrderId`/`razorpaySignature` as null in their historical records, but that's fine — this check only runs at save time, on new requests, not retroactively. The real risk is **client/backend version skew during rollout**: if the backend fix deploys before the frontend fix, every Razorpay payment would fail verification (both new fields null) and legitimate patients would be blocked from completing checkout. **These two changes must ship together, not the backend alone** — flagging this explicitly since it's easy to deploy out of order by accident.

**Confidence: high** on the mechanics (same SDK calls as §1/§2a). **Medium** on the frontend integration specifically, since I have not run this in a browser against a real Razorpay checkout session — recommend a manual test pass in staging before production, beyond just the automated tests requested in your plan's step 3.

---

## §4 — `saveAndPay`: non-gateway payment modes (cash, cheque, card-swipe, DD, etc.) — two options, neither applied

For any `PaymentReceiptDetailsDto` where `paymentModeId` isn't Razorpay (8) or CCAvenue (12+providerId 2, which retains its existing reuse check), there's **no external gateway to cryptographically verify against at all** — a cash payment's "proof" is institutional trust in whoever recorded it, not a signature. The only meaningful check code can add is: *who is allowed to submit this claim in the first place.*

**Option A — leave as-is (recommended for this pass):** don't add any new check for these modes. Matches current production behavior exactly. Rationale: this endpoint (`api/billing/kiosk/saveAndPay`) is exempted in `JwtTokenFilter` for a reason I can't fully verify — it may serve genuinely anonymous online Razorpay-paying patients *and* staff-operated kiosk terminals through the same URL, and I don't have confidence about how the two are currently distinguished (or whether they need to be). Changing this now, bundled with the Razorpay fix, risks the same class of mistake as the original JWT patch — breaking a legitimate flow I don't fully understand yet.

**Option B — require an authenticated principal for non-gateway-verified modes:** add a check that `SecurityContextHolder.getContext().getAuthentication()` is a real, non-anonymous principal before accepting any cash/card/cheque/DD payment record, while leaving Razorpay-mode records to verify independently via §3 (so genuinely anonymous online patients aren't blocked). **This does not work today without a companion `JwtTokenFilter` change**: because `api/billing/kiosk/saveAndPay` currently matches `JwtTokenFilter`'s exemption list, the filter takes its unconditional "let it through" path and **never validates or sets `SecurityContextHolder`, even if a staff member's valid token is present on the request.** Making Option B meaningful would require changing the filter to *attempt* validation-and-context-population for this specific URL when a token is present, while still not *rejecting* the request at the filter layer when one is absent (since Razorpay-mode calls need to stay reachable anonymously) — a third file's worth of change, and a repeat of exactly the "does this break a legitimate flow" uncertainty that made the original JWT patch unsafe. Not drafted in this document; recommend as a separate, later, carefully-scoped piece of work if you want it — not bundled into this fix.

**My recommendation: ship Option A now.** It's not a regression (matches today's behavior for these modes), it doesn't block on understanding the kiosk-vs-online distinction, and it keeps this fix's blast radius contained to the Razorpay-specific gap that was actually reported. Flag cash/manual-mode integrity as a known, accepted-for-now gap, revisited separately.

---

## Combined "what's actually ready to approve" list

- **Ready, high confidence:** §1, §2a, §2b (deletion), §3 (backend + frontend, ship together).
- **Needs your input before I can draft it:** §2c (Payment Links signature format).
- **Deliberately not drafted, recommend deferring:** §4 Option B (non-gateway mode auth).
- **Still out of scope, unchanged:** order-creation-time fee validation.

No file under `legacy/` has been modified. Nothing compiled or tested. Awaiting your review.

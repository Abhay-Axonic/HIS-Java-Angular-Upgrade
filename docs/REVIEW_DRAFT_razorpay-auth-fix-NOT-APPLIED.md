# DRAFT — Razorpay / Auth Fix — NOT APPLIED, NOT COMMITTED, NOT DEPLOYED

**Status (2026-07-15, updated): §1 confirmed sound. §2 confirmed UNSAFE to apply as drafted — would break real anonymous bookings, see the update inside §2 below. §5's `saveAndPay` item is now confirmed (not just suspected) to have the same client-trusted-amount issue, and is arguably the more serious of the two given it's the actual bill-creation step. Owner has not yet given final go-ahead on anything; this document is being revised, not applied.**

**Status: draft for owner review only.** Nothing in this document has been applied to any file under `legacy/`. Per explicit owner instruction, no code will be touched until this is reviewed and approved, item by item if desired. This document itself is documentation (governance-file carve-out, no Maker-Checker needed to *write* it) — but the diffs it contains describe application-code changes that absolutely do need Maker-Checker + owner sign-off before merging, per `RULES.md`.

**Covers:** `docs/DECISION_NEEDED_RazorPayClientTrustedAmount.md` (client-trusted amount, missing signature verification) and `docs/DECISION_NEEDED_SpringSecurityFilterChainNotWired.md` (the dead Spring Security config).

**New finding while preparing this diff**, flagged here since it directly affects scope: mechanically extracting `JwtTokenFilter.java`'s exemption list (§3 below) turned up `api/billing/kiosk/saveAndPay` — the actual backend endpoint the unit-subscription/kiosk payment flow POSTs to in order to create the bill and mark it paid (traced from `selfRegistrationController.js`'s `settleTranaction()`, line ~3436) — **also on the anonymous-exemption list.** I have not read that controller's Java implementation, so I can't say whether it independently validates anything. This means closing the gap at `getOrderIdUnitSubscriptionPay` alone would not fully close the subscription flow's exposure if `saveAndPay` can be called directly and skips the same validation — noted as an explicit follow-up in §2 and §5.

---

## Recommended sequencing, given this is live production

**Do not apply §3/§4 (the full Spring Security wiring) and §1/§2 (the Razorpay-specific fix) as one change.** They have very different risk profiles:

- **§1 and §2** (RazorPaymentController signature/amount fix, targeted JwtTokenFilter patch): small, additive, touches a handful of specific endpoints, does not change behavior for anything else in the application. Low blast radius.
- **§3 and §4** (wiring `DelegatingFilterProxy` + a 201-entry reconciled `permitAll()` list): touches the authentication behavior of **every URL in the application**. Even with a mechanically-extracted (not hand-typed) exemption list, missing or mis-translating even one of 201 entries would break a currently-working, possibly-clinical-workflow-critical endpoint in production, with no staging environment mentioned so far to validate against first.

My recommendation: **approve and ship §1+§2 first**, as its own small, reviewable, testable change. Treat §3+§4 as its own separate, larger effort — ideally validated against a staging/pre-prod environment with real traffic replay or a comprehensive endpoint checklist before anything close to production, and probably its own dedicated session given the scope. I've drafted §3/§4 in full below because you asked for both, but I want the risk asymmetry on the record before you approve anything.

---

## §1 — `RazorPaymentController.java`: signature verification + Razorpay-authoritative amount at `confirmAppointment`

**Scope of this diff:** the web-appointment flow only (`getOrderIdForRazorPayPayment` → `confirmAppointment`), which is fully contained in this one file and fully traced. **Not covered:** `getOrderIdUnitSubscriptionPay`'s downstream (`api/billing/kiosk/saveAndPay`, a different, larger controller I have not read — see §5) and order-creation-time fee validation against a real doctor/subscription price (I could not identify the correct fee-lookup service with confidence — see §5). This diff closes the more severe part of the gap — "the bill records whatever the client claims was paid, disconnected from what Razorpay actually processed, with no proof a payment happened at all" — but does not by itself guarantee the charged amount matches a real fee.

**SDK verified:** `com.razorpay:razorpay-java:1.3.1` (vendored at `legacy/his-maven-jars/com/razorpay/razorpay-java/1.3.1/`). Confirmed via its sources jar: `Utils.verifyPaymentSignature(JSONObject attributes, String apiSecret)` (throws `RazorpayException`, returns `boolean`, expects `razorpay_order_id`/`razorpay_payment_id`/`razorpay_signature` keys), and `OrderClient.fetch(String id)` returning an `Order` whose `.get(String key)` accessor matches the pattern already used elsewhere in this file (`order.get("id")`).

```diff
--- a/legacy/Billing/src/main/java/com/param/billing/global/controller/RazorPaymentController.java
+++ b/legacy/Billing/src/main/java/com/param/billing/global/controller/RazorPaymentController.java
@@ -37,6 +37,7 @@ import com.param.billing.global.dto.BillPaymentCollectionReqDto;
 import com.param.billing.global.transaction.service.IBillingMasterService;
 import com.param.billing.global.transaction.service.RazorPayBillService;
 import com.param.global.billing.dto.PaymentReceiptDetailsDto;
+import com.razorpay.Utils;
 import com.param.global.common.AesUtil;
 import com.param.global.common.CCAvenueService;
 import com.param.global.common.IAppointmentAndSlotStatusMasterConstants;
@@ -167,6 +168,42 @@ public class RazorPaymentController implements ICommonConstants{
 			if (appConfigRes.getObject() != null) {
 				unitApplicationConfigurationDto = (UnitApplicationConfigurationDto) appConfigRes.getObject();
 			}
+
+			// --- BEGIN added: Razorpay signature verification + authoritative amount ---
+			// Verify this callback is a genuine Razorpay payment event before trusting anything
+			// in it. Previously razorPaySignature was read and never checked (see
+			// docs/DECISION_NEEDED_RazorPayClientTrustedAmount.md section 3).
+			try {
+				org.json.JSONObject sigAttrs = new org.json.JSONObject();
+				sigAttrs.put("razorpay_order_id", razorPayOrderId);
+				sigAttrs.put("razorpay_payment_id", razorPayPaymentId);
+				sigAttrs.put("razorpay_signature", razorPaySignature);
+				boolean signatureValid = Utils.verifyPaymentSignature(sigAttrs, unitApplicationConfigurationDto.getRazorPaySecretKey());
+				if (!signatureValid) {
+					logger.error("PAYMENT_RECONCILIATION_GAP: invalid Razorpay signature on /confirmAppointment for order " + razorPayOrderId + " -- possible forged/tampered confirmation callback, bill NOT created.");
+					return modelAndView;
+				}
+			} catch (Exception e) {
+				logger.error("PAYMENT_RECONCILIATION_GAP: Razorpay signature verification threw for order " + razorPayOrderId + ", bill NOT created. " + e.getMessage(), e);
+				return modelAndView;
+			}
+
+			// Re-fetch the order directly from Razorpay's API and use ITS amount/status as the
+			// source of truth, instead of the locally-logged amount (which traced back to a
+			// client-supplied field with no independent verification -- see
+			// docs/DECISION_NEEDED_RazorPayClientTrustedAmount.md section 2a).
+			Double razorpayVerifiedPaidAmount;
+			try {
+				RazorpayClient verifyClient = new RazorpayClient(unitApplicationConfigurationDto.getRazorPayKeyId(), unitApplicationConfigurationDto.getRazorPaySecretKey());
+				Order verifiedOrder = verifyClient.Orders.fetch(razorPayOrderId);
+				String orderStatus = verifiedOrder.get("status");
+				if (!"paid".equals(orderStatus)) {
+					logger.error("PAYMENT_RECONCILIATION_GAP: Razorpay order " + razorPayOrderId + " status is \"" + orderStatus + "\", not \"paid\" -- bill NOT created.");
+					return modelAndView;
+				}
+				Integer amountPaidPaise = verifiedOrder.get("amount_paid");
+				razorpayVerifiedPaidAmount = BigDecimal.valueOf(amountPaidPaise).divide(BigDecimal.valueOf(100)).doubleValue();
+			} catch (Exception e) {
+				logger.error("PAYMENT_RECONCILIATION_GAP: could not re-fetch/verify Razorpay order " + razorPayOrderId + " from Razorpay's API, bill NOT created. " + e.getMessage(), e);
+				return modelAndView;
+			}
+			// --- END added ---
 
 			SlotMasterDto appointmentDto = new SlotMasterDto();
 			appointmentDto.setUnitId(reqDto.getUnitId());
@@ -218,8 +255,8 @@ public class RazorPaymentController implements ICommonConstants{
 			encounterMasterDto.setIsPreOrPostBilling('Y');
 			encounterMasterDto.setDeptWiseAppointment('N');
 			encounterMasterDto.setIsTransferToActiveDeposit('N');
 			encounterMasterDto.setIsReshedule('N');
-			encounterMasterDto.setTotalBillAmt(reqDto.getPaidAmount());
-			encounterMasterDto.setPaidAmount(reqDto.getPaidAmount());
+			encounterMasterDto.setTotalBillAmt(razorpayVerifiedPaidAmount);
+			encounterMasterDto.setPaidAmount(razorpayVerifiedPaidAmount);
 			encounterMasterDto.setCurrentDate(strCurrentDate2);
 			// encounterMasterDto.setIsReshedule('N');
 			List<PaymentReceiptDetailsDto> paymentReceiptDetailsDtosList = new ArrayList<PaymentReceiptDetailsDto>();
 			PaymentReceiptDetailsDto paymentReceiptDetailsDto = new PaymentReceiptDetailsDto();
 			paymentReceiptDetailsDto.setPaymentModeId(8);
-			paymentReceiptDetailsDto.setAmount(reqDto.getPaidAmount());
+			paymentReceiptDetailsDto.setAmount(razorpayVerifiedPaidAmount);
 			paymentReceiptDetailsDto.setTransactionNo(razorPayPaymentId);
@@ -280,7 +317,7 @@ public class RazorPaymentController implements ICommonConstants{
 			modelAndView.addObject("visitFee", reqDto.getPaidAmount());
+			// NOTE: this display value is still the pre-verification client-asserted amount --
+			// used only for the confirmation-page display, not for anything financial. Left
+			// as-is deliberately; flag for owner whether the display should also switch to
+			// razorpayVerifiedPaidAmount for consistency (Behavioral Change either way -- the
+			// displayed number could differ from what the patient expected to see, however
+			// slightly, if the two ever disagree).
```

**Why `reqDto.getPaidAmount()` isn't simply deleted everywhere:** it's still used for the confirmation page's display text (`modelAndView.addObject("visitFee", ...)`), which is cosmetic, not financial. I left that one call site untouched and flagged it rather than silently deciding — per `RULES.md`, even a cosmetic-looking amount-source change on a payment confirmation page is a Behavioral Change if the two values could ever disagree, and that's your call, not mine.

**This is a Behavioral Change** (a bill could now be created with a different amount than before if `razorPayBillAmount` and the separately-logged `amount` field ever disagreed on a real request, or could fail to create a bill at all where it previously silently created one with a bad amount) and would need a `docs/BUSINESS_RULE_MANIFEST.md` entry with your sign-off before merge, same as every other fix in this project's history (e.g. BR-018). Not logged yet since nothing has been approved.

**Confidence level:** high on the `Utils`/`OrderClient` API usage (verified against the actual vendored SDK source, not assumed). Not yet compiled or tested — that's step 3 of your plan, after approval.

---

## §2 — `JwtTokenFilter.java`: targeted patch closing the auth gap for payment-critical endpoints (recommended, low-risk option for "wire in real enforcement")

Adds one new, small, unconditional guard at the very top of `doFilterInternal`, before any of the existing ~600 lines of exemption logic runs. Does not modify any existing condition — minimizes risk of an unrelated regression elsewhere in the app.

```diff
--- a/legacy/HIS-WAR/src/main/java/com/param/filter/JwtTokenFilter.java
+++ b/legacy/HIS-WAR/src/main/java/com/param/filter/JwtTokenFilter.java
@@ -1,6 +1,7 @@
 package com.param.filter;
 
 import java.io.IOException;
+import java.util.Arrays;
 import java.util.List;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
@@ -25,6 +26,20 @@ import org.slf4j.LoggerFactory;
 public class JwtTokenFilter extends OncePerRequestFilter {
 
 	private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
+
+	// Endpoints that create a Razorpay order, confirm a Razorpay payment, or finalize a bill
+	// from one -- must NEVER be reachable without a valid token, regardless of header presence
+	// or the generic short-path (splitUrl.length <= 2) exemption below. See
+	// docs/DECISION_NEEDED_RazorPayClientTrustedAmount.md section 4 for why the existing
+	// exemption logic let these through.
+	private static final List<String> ALWAYS_REQUIRE_AUTH = Arrays.asList(
+		"/getOrderIdForRazorPayPayment",
+		"/getOrderIdUnitSubscriptionPay",
+		"/confirmAppointment",
+		"/confirmAppointment1",
+		"/confirmPayment",
+		"/razorPayPaymentLink",
+		"/getOrderIdForRazorPayPaymentOnBilling",
+		"/validateRazorPayPaymentLink",
+		"/api/billing/kiosk/saveAndPay"
+	);
 
 	@Override
 	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
@@ -69,7 +84,24 @@ public class JwtTokenFilter extends OncePerRequestFilter {
 			Integer userId = request.getHeader("userId") != null ? new Integer(request.getHeader("userId")) : 0;
 			Integer unitId = request.getHeader("unitId") != null ? new Integer(request.getHeader("unitId")) : null;
 
+			boolean isAlwaysAuthRequired = ALWAYS_REQUIRE_AUTH.stream().anyMatch(requestUrl::contains);
 
+			if (isAlwaysAuthRequired) {
+				if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
+					String token = authorizationHeader.substring(7);
+					if (JwtTokenUtil.validateToken(token, userId, unitId)) {
+						String username = JwtTokenUtil.extractUsername(token);
+						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, null);
+						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
+						SecurityContextHolder.getContext().setAuthentication(authToken);
+						chain.doFilter(request, response);
+					} else {
+						System.out.println("UNAUTHORIZED (payment-critical) ##" + requestUrl + "##" + userId + "##" + unitId);
+						response.setStatus(HttpStatus.UNAUTHORIZED.value());
+					}
+				} else {
+					System.out.println("UNAUTHORIZED (payment-critical, no Authorization header) ##" + requestUrl);
+					response.setStatus(HttpStatus.UNAUTHORIZED.value());
+				}
+			} else if (unitId != null && userId != null && authorizationHeader != null
 						&& !(requestUrl.contains("/adt/userLogin") || requestUrl.contains("/adt/organizationMasterList")
```

(The rest of the file is unchanged — the existing `if` becomes an `else if` chained after the new block; everything from the original `if(unitId != null && ...)` condition onward, including the second ~150-entry exemption block and the final `else`, stays exactly as it is today.)

**What this closes:**
- `getOrderIdUnitSubscriptionPay`'s explicit double-whitelisting (§4 finding).
- All payment-critical endpoints' exposure via the generic `splitUrl.length <= 2` fallback (§4 finding), since the new check runs first and unconditionally.
- The newly-discovered `api/billing/kiosk/saveAndPay` exemption (see the callout at the top of this document).

**What this does NOT do:** it does not touch any of the other ~150-200 currently-exempted endpoints, so it carries essentially none of §3/§4's blast-radius risk. It also does not itself fix the amount-trust problem — that's §1 (and the not-yet-drafted `saveAndPay` fix, §5).

**UPDATE 2026-07-15 (later): confirmed unsafe to apply as drafted.** Owner asked this to be verified before approval. Traced both frontend flows end to end:

- `bookAppointmentController.js`: after `scheduleAppointment` saves the appointment (response handler at line ~958), the code reads only `response.data.object.appointmentId` before immediately calling `$scope.goToRazorPay()` (→ `getOrderIdForRazorPayPayment`). **No token of any kind is extracted from the response or written to the cookie.**
- `selfRegistrationController.js`: the `generateToken`/`tokenNumber` fields present in this flow are a **hospital queue ticket number** (`$scope.tokenNumber = response.data.object.tokenNumber`, displayed in a "your token is #42" modal), unrelated to JWT authentication — an incorrect assumption on my part in the original draft above, now corrected. No `authToken` write exists anywhere in this file either.
- `GenericService.js` (the shared HTTP wrapper both flows use) only ever *reads* `cookieObject.authToken` to build request headers — it has no code path that writes one.

**Conclusion: neither flow issues any token before reaching these endpoints, at any point.** A genuinely anonymous patient — which is the normal, legitimate case for first-time web appointment booking and self-registration — has no `Authorization` header to send. **Applying §2 as drafted would break real, legitimate bookings, not just close the vulnerability.** Do not apply §2 in its current form.

**Corrected direction:** requiring a JWT is the wrong security boundary for this surface in the first place — a payment-gateway confirmation is inherently an anonymous-callback pattern (this is also how Stripe/Razorpay webhooks work everywhere), and the right trust boundary is Razorpay's own cryptographic signature, not a user session. §1 already does this correctly for `confirmAppointment`. The corrected fix drops the "require a valid JWT" approach entirely for the order-creation/confirmation endpoints and instead relies on §1-style signature + Razorpay-authoritative-amount verification as the actual gate — which doesn't require the caller to be logged in at all. §2 needs to be re-scoped or dropped; not re-drafted in this document without your direction on which.

---

## §3 — `web.xml`: wire `DelegatingFilterProxy` (Option A, as literally requested — see sequencing note above)

```diff
--- a/legacy/HIS-WAR/src/main/webapp/WEB-INF/web.xml
+++ b/legacy/HIS-WAR/src/main/webapp/WEB-INF/web.xml
@@ -17,14 +17,20 @@
 	<filter>
 		<filter-name>requestWrap</filter-name>
 		<filter-class>com.param.filter.JwtTokenFilter</filter-class>
 	</filter>
 
 	<filter-mapping>
 		<filter-name>requestWrap</filter-name>
 		<url-pattern>/*</url-pattern>
 	</filter-mapping>
+
+	<!-- Bridges SecurityConfig.java's HttpSecurity chain (which already declares
+	     JwtTokenFilter via addFilterBefore) into the actual servlet pipeline. Without this,
+	     SecurityConfig.java's .anyRequest().authenticated() policy never runs -- see
+	     docs/DECISION_NEEDED_SpringSecurityFilterChainNotWired.md. Replaces the standalone
+	     requestWrap registration above, which becomes redundant once JwtTokenFilter runs
+	     inside this chain instead. REMOVE the requestWrap <filter>/<filter-mapping> block
+	     above once this is confirmed working, to avoid running JwtTokenFilter twice per
+	     request. Left in place in this diff, commented nowhere -- deliberately NOT removed
+	     here, so this change is purely additive and independently revertible if the
+	     permitAll reconciliation in SecurityConfig.java (below) turns out to be wrong. Remove
+	     it in a follow-up commit only after this has been validated. -->
+	<filter>
+		<filter-name>springSecurityFilterChain</filter-name>
+		<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
+	</filter>
+	<filter-mapping>
+		<filter-name>springSecurityFilterChain</filter-name>
+		<url-pattern>/*</url-pattern>
+	</filter-mapping>
```

I deliberately did **not** remove the existing `requestWrap` registration in this diff, for one reason: if the new `springSecurityFilterChain` (and its reconciled `permitAll()` list below) has a mistake, `requestWrap`'s existing, already-production-tested `JwtTokenFilter` logic keeps running as a backstop rather than the app being left with only the new, unvalidated policy. This means `JwtTokenFilter` would run twice per request while both are active (wasteful, not harmful) — intentional, and meant to be cleaned up (removing `requestWrap`) only after the new chain is confirmed correct.

## §4 — `SecurityConfig.java`: reconciled `permitAll()` list (Option A, continued)

The 201-entry list was generated **mechanically** from `JwtTokenFilter.java`'s own two (largely duplicate) exemption blocks via `grep -oE 'requestUrl\.contains\("[^"]*"\)'`, deduplicated, then each entry wrapped as `.*\Q<literal>\E.*` (Java regex literal-quoting, so URL characters like `.` and `/` are never misread as regex metacharacters). This is a faithful **preservation** of `JwtTokenFilter`'s current behavior, not a fresh audit of whether each of the 201 entries is individually still appropriate — that determination was already made, in production, by whoever built the list over time; this diff's job is only to make Spring Security enforce the same rules consistently, and to deliberately **exclude** `/getOrderIdUnitSubscriptionPay` and **not replicate** the blanket short-path fallback, closing exactly the two gaps already identified.

```diff
--- a/legacy/HIS-WAR/src/main/java/com/param/config/SecurityConfig.java
+++ b/legacy/HIS-WAR/src/main/java/com/param/config/SecurityConfig.java
@@ -1,15 +1,20 @@
 package com.param.config;
 
+import java.util.regex.Pattern;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.http.HttpMethod;
 import org.springframework.security.config.annotation.web.builders.HttpSecurity;
 import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
 import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
 import org.springframework.security.core.userdetails.UserDetailsService;
 import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
 
 import com.param.filter.JwtTokenFilter;
 
 @Configuration
 @EnableWebSecurity
 public class SecurityConfig extends WebSecurityConfigurerAdapter {
 
+	// Mechanically extracted from JwtTokenFilter.java's requestUrl.contains(...) exemption
+	// checks (both blocks, deduplicated), 2026-07-15. Preserves existing production
+	// behavior -- does NOT re-audit whether each entry is still appropriate. Deliberately
+	// excludes "/getOrderIdUnitSubscriptionPay" (was explicitly whitelisted in
+	// JwtTokenFilter; closing that gap is the point of this change) and does not replicate
+	// JwtTokenFilter's blanket short-path (splitUrl.length <= 2) fallback.
+	private static final String[] LEGACY_PUBLIC_URL_PATTERNS = {
+	    ".*\\Q/adt/discharge/dischargeSummaryCard\\E.*",
+	    ".*\\Q/adt/getActiveCityListByDistrictId\\E.*",
+	    ".*\\Q/adt/getActiveCountryList/\\E.*",
+	    [... 198 more entries, full list below ...]
+	    ".*\\Qupdate/service\\E.*"
+	};
+
 	@Bean
 	@Override
 	public UserDetailsService userDetailsServiceBean() throws Exception {
 		return super.userDetailsServiceBean();
 	}
 	@Override
 	protected void configure(HttpSecurity http) throws Exception {
 		http.csrf().disable()
 				.authorizeRequests()
 				.antMatchers("/resources/assets/js/**","/adt/organizationMasterList/**","/adt/getUnitByCredentials","/ldapUserCredentialCheck").permitAll() // Public endpoints
 				.antMatchers(HttpMethod.POST, "/adt/userLogin").permitAll() // Login endpoint
+				.regexMatchers(LEGACY_PUBLIC_URL_PATTERNS).permitAll() // see LEGACY_PUBLIC_URL_PATTERNS comment above
 				.anyRequest().authenticated() // All other requests require authentication
 				.and()
 				.addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Custom JWT filter
 	}
 }
```

The full 201-entry array (not abbreviated) is included verbatim below, exactly as it would appear in the file, so it can actually be reviewed rather than taken on faith:

```java
private static final String[] LEGACY_PUBLIC_URL_PATTERNS = {
    ".*\\Q/adt/discharge/dischargeSummaryCard\\E.*",
    ".*\\Q/adt/getActiveCityListByDistrictId\\E.*",
    ".*\\Q/adt/getActiveCountryList/\\E.*",
    ".*\\Q/adt/getActiveDistrictListByStateId\\E.*",
    ".*\\Q/adt/getActiveGenderList\\E.*",
    ".*\\Q/adt/getActivePrefixListForWebPage\\E.*",
    ".*\\Q/adt/getActiveRelationListForWebPage\\E.*",
    ".*\\Q/adt/getActiveSpecialityList\\E.*",
    ".*\\Q/adt/getActiveStateList\\E.*",
    ".*\\Q/adt/getActiveStateListByCountryId\\E.*",
    ".*\\Q/adt/getAppointmentsByPatientId\\E.*",
    ".*\\Q/adt/getAreaByCityId\\E.*",
    ".*\\Q/adt/getAreaMasterList\\E.*",
    ".*\\Q/adt/getCityMasterList\\E.*",
    ".*\\Q/adt/getCookieDataForNewEMR\\E.*",
    ".*\\Q/adt/getCountryById\\E.*",
    ".*\\Q/adt/getDistrictMasterList\\E.*",
    ".*\\Q/adt/getListOfDocumentForBookView\\E.*",
    ".*\\Q/adt/getNationalityByCountryId\\E.*",
    ".*\\Q/adt/getPatientDetailsForWorklistForWebPage\\E.*",
    ".*\\Q/adt/getSpecialityListByDoctorIdByOrg\\E.*",
    ".*\\Q/adt/getUnitByCredentials\\E.*",
    ".*\\Q/adt/organizationMasterList\\E.*",
    ".*\\Q/adt/userLogin\\E.*",
    ".*\\Q/api/abdm/dataPush\\E.*",
    ".*\\Q/api/abdm/getHiuDecryptedData\\E.*",
    ".*\\Q/api/abdm/request/otp\\E.*",
    ".*\\Q/api/abdm/saveDetailsOfAbdm\\E.*",
    ".*\\Q/api/abdm/v3/updateStatus\\E.*",
    ".*\\Q/api/billing/bill/service/cancel\\E.*",
    ".*\\Q/api/billing/mylife/sync/appointment\\E.*",
    ".*\\Q/api/billing/service/preBilling/orderedServicesByMobileNoForWebUser\\E.*",
    ".*\\Q/api/billing/unit/bill/Report\\E.*",
    ".*\\Q/api/billing/unit/getDoctorCharges\\E.*",
    ".*\\Q/api/common/getActiveInspectionMasterList\\E.*",
    ".*\\Q/api/common/getSearchedFreeTextInstructionList\\E.*",
    ".*\\Q/api/common/getSearchedInstructionList\\E.*",
    ".*\\Q/api/createOrganizationFromWeb\\E.*",
    ".*\\Q/api/deleteParticipants\\E.*",
    ".*\\Q/api/doctorApp/getApplicationConfig/\\E.*",
    ".*\\Q/api/getVideoataForNotification\\E.*",
    ".*\\Q/api/global/appointments/doctorsForWebUser\\E.*",
    ".*\\Q/api/global/appoitments/doctors\\E.*",
    ".*\\Q/api/global/getPatientDetailsListForNewEMR\\E.*",
    ".*\\Q/api/global/getPatientDetailsListForNewEMROnlyFavoritesList\\E.*",
    ".*\\Q/api/global/getUserListForStore/\\E.*",
    ".*\\Q/api/global/getVideoConsultationLinkFromHIS\\E.*",
    ".*\\Q/api/global/glablServiceMaster\\E.*",
    ".*\\Q/api/global/growthDevelopmentalMilestones/detail\\E.*",
    ".*\\Q/api/global/growthDevelopmentalMilestones/patient\\E.*",
    ".*\\Q/api/global/savePatientRemarksForNewEMR\\E.*",
    ".*\\Q/api/global/updateDoctorStatus\\E.*",
    ".*\\Q/api/global/updateEmployeeStatus\\E.*",
    ".*\\Q/api/global/webappointment/getDoctors\\E.*",
    ".*\\Q/api/globals/frequency\\E.*",
    ".*\\Q/api/inPatientFeedback\\E.*",
    ".*\\Q/api/ipd/saveMyLatestMedicalSummaryCallBack\\E.*",
    ".*\\Q/api/mylife/sync/appointment/cancelorRescheduleAppointmentFromApp\\E.*",
    ".*\\Q/api/mylife/sync/savePatientThroughMylife\\E.*",
    ".*\\Q/api/opd/cancelFreeTextInvestigations\\E.*",
    ".*\\Q/api/opd/coversheetInstruction\\E.*",
    ".*\\Q/api/opd/coversheetInstructionList/1\\E.*",
    ".*\\Q/api/opd/getAIAppointmentCount\\E.*",
    ".*\\Q/api/opd/getComplaintAppointment\\E.*",
    ".*\\Q/api/opd/getCompleteConsultationReport\\E.*",
    ".*\\Q/api/opd/getCoversheetAsTemaplateList\\E.*",
    ".*\\Q/api/opd/getCoversheetListData\\E.*",
    ".*\\Q/api/opd/getCurrentAllergyPatientMapperForCoversheet\\E.*",
    ".*\\Q/api/opd/getCurrentPatientFamilyHistoryForCoversheet\\E.*",
    ".*\\Q/api/opd/getCurrentPatientHabitDetails\\E.*",
    ".*\\Q/api/opd/getCurrentPatientSurgicalHistoryForCoversheet\\E.*",
    ".*\\Q/api/opd/getDailySlots\\E.*",
    ".*\\Q/api/opd/getDiagnosisByGenderId\\E.*",
    ".*\\Q/api/opd/getFreeTextInvestigations\\E.*",
    ".*\\Q/api/opd/getFrequencyList\\E.*",
    ".*\\Q/api/opd/getInvestigationsListForBookView\\E.*",
    ".*\\Q/api/opd/getInvestigationsListForNewEMR\\E.*",
    ".*\\Q/api/opd/getLastEncounterDataOfPatientForWebAPP\\E.*",
    ".*\\Q/api/opd/getLastEncounteredListOfPatient\\E.*",
    ".*\\Q/api/opd/getListOfVitalsByPatientIdForCoversheet\\E.*",
    ".*\\Q/api/opd/getMasterListOfICPCSystemForCoversheet\\E.*",
    ".*\\Q/api/opd/getMedicationDetailsForNewEMR\\E.*",
    ".*\\Q/api/opd/getMedicationHistoryList\\E.*",
    ".*\\Q/api/opd/getOldPatientFamilyHistory\\E.*",
    ".*\\Q/api/opd/getOldPatientSurgicalHistory\\E.*",
    ".*\\Q/api/opd/getOPDMedicationListById\\E.*",
    ".*\\Q/api/opd/getPatientDiagnosisByPatientIdForNewEMR\\E.*",
    ".*\\Q/api/opd/getQuickNotesMapperList\\E.*",
    ".*\\Q/api/opd/getSubstituteListForNewEMR\\E.*",
    ".*\\Q/api/opd/getTemplateDataByCovTempId\\E.*",
    ".*\\Q/api/opd/medicationUpdate\\E.*",
    ".*\\Q/api/opd/patientDoc/details\\E.*",
    ".*\\Q/api/opd/patientDoc/fromMylife\\E.*",
    ".*\\Q/api/opd/saveAIAppointmentInQueue\\E.*",
    ".*\\Q/api/opd/saveAppointmentVitalMapper/\\E.*",
    ".*\\Q/api/opd/saveComplaintAppointment/\\E.*",
    ".*\\Q/api/opd/saveCovTemplateDataForPatient\\E.*",
    ".*\\Q/api/opd/saveFreeTexPatientDiagnosis\\E.*",
    ".*\\Q/api/opd/saveFreeTextInvestigations\\E.*",
    ".*\\Q/api/opd/saveInvestigation\\E.*",
    ".*\\Q/api/opd/saveJustificationForMedicationDelete\\E.*",
    ".*\\Q/api/opd/saveJustificationForMedicationDeleteMultipleMedication\\E.*",
    ".*\\Q/api/opd/saveMedication\\E.*",
    ".*\\Q/api/opd/savePatientCancelledDiagnosis\\E.*",
    ".*\\Q/api/opd/savePatientDiagnosisForNewEMR\\E.*",
    ".*\\Q/api/opd/saveQuickNotesMapper\\E.*",
    ".*\\Q/api/opd/saveTemplateListDataForNewEMR\\E.*",
    ".*\\Q/api/opd/scheduleAppointment\\E.*",
    ".*\\Q/api/opd/searchDrugByGenericNameOrItemName\\E.*",
    ".*\\Q/api/opd/searchFreeTextComplaint\\E.*",
    ".*\\Q/api/opd/searchFreeTextInvestigations\\E.*",
    ".*\\Q/api/opd/serchFreeTexPatientDiagnosis\\E.*",
    ".*\\Q/api/opd/updateComplaintAppointmentStatusToRemoveComplaint\\E.*",
    ".*\\Q/api/opd/updateCoversheetInstructionStatus\\E.*",
    ".*\\Q/api/opd/updateCovTemplateDataForPatient\\E.*",
    ".*\\Q/api/opd/updateEncounterDetailsForNewEMR\\E.*",
    ".*\\Q/api/opd/updateFollowUpDateOfPatient\\E.*",
    ".*\\Q/api/outPatientFeedback\\E.*",
    ".*\\Q/api/refreshAllCaches\\E.*",
    ".*\\Q/api/saveTwilioMsg\\E.*",
    ".*\\Q/api/unit/clinical/getDoctorFavouriteMedicationForCoversheet\\E.*",
    ".*\\Q/api/webappointment/getActivePrefixList/\\E.*",
    ".*\\Q/api/webappointment/getDetailsForBookAppointment\\E.*",
    ".*\\Q/applicationConfiguration/screen-images\\E.*",
    ".*\\Q/br/\\E.*",
    ".*\\Q/checkEmailAvailability\\E.*",
    ".*\\Q/checkParti/\\E.*",
    ".*\\Q/decrypt\\E.*",
    ".*\\Q/forgotPasswordGeneration\\E.*",
    ".*\\Q/generateToken\\E.*",
    ".*\\Q/getBillNoByPatient\\E.*",
    ".*\\Q/getBillReceiptBytesMyLife\\E.*",
    ".*\\Q/getSerivceRateGlobalList\\E.*",
    ".*\\Q/global/getPopUpDataWithId\\E.*",
    ".*\\Q/global/saveTemplateSummery\\E.*",
    ".*\\Q/global/serviceMaster\\E.*",
    ".*\\Q/HIS-WAR/api-ui\\E.*",
    ".*\\Q/HIS-WAR/configuration/security\\E.*",
    ".*\\Q/HIS-WAR/configuration/ui\\E.*",
    ".*\\Q/HIS-WAR/deleteParticipants\\E.*",
    ".*\\Q/HIS-WAR/deleteParticipants/*\\E.*",
    ".*\\Q/HIS-WAR/deleteParticipants/**\\E.*",
    ".*\\Q/HIS-WAR/deleteParticipants/Patient/dataroom_41015\\E.*",
    ".*\\Q/HIS-WAR/images/favicon-16x16.png\\E.*",
    ".*\\Q/HIS-WAR/images/favicon-32x32.png\\E.*",
    ".*\\Q/HIS-WAR/swagger-resources\\E.*",
    ".*\\Q/HIS-WAR/swagger-ui.html\\E.*",
    ".*\\Q/HIS-WAR/v2/api-docs\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/css/print.css\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/css/reset.css\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/css/screen.css\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/css/typography.css\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/fonts/DroidSans-Bold.ttf\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/fonts/DroidSans.ttf\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/images/logo_small.png\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/lib/backbone-min.js\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/lib/handlebars-2.0.0.js\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/lib/highlight.7.3.pack.js\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery-1.8.0.min.js\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery.ba-bbq.min.js\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery.slideto.min.js\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery.wiggle.min.js\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/lib/jsoneditor.min.js\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/lib/marked.js\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/lib/swagger-oauth.js\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/lib/underscore-min.js\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/springfox.js\\E.*",
    ".*\\Q/HIS-WAR/webjars/springfox-swagger-ui/swagger-ui.min.js\\E.*",
    ".*\\Q/https://feqituyulh.execute-api.ap-south-1.amazonaws.com/dev/api/generateFlipbook\\E.*",
    ".*\\Q/ldapUserCredentialCheck\\E.*",
    ".*\\Q/LIS/global/getTotalLanguageMasterRecordActive/\\E.*",
    ".*\\Q/otpVerification\\E.*",
    ".*\\Q/pathology/report\\E.*",
    ".*\\Q/QR\\E.*",
    ".*\\Q/Report\\E.*",
    ".*\\Q/resources/\\E.*",
    ".*\\Q/resultUrl\\E.*",
    ".*\\Q/RIS/RADIOLOGY\\E.*",
    ".*\\Q/roomRent\\E.*",
    ".*\\Q/token/Doctor/\\E.*",
    ".*\\Q/unit/org/\\E.*",
    ".*\\Q/unit/subcription\\E.*",
    ".*\\Q/updateNABH\\E.*",
    ".*\\Q/updatePatientRegistrationFromMyLife\\E.*",
    ".*\\Q/validationResultResponse\\E.*",
    ".*\\Q/views/\\E.*",
    ".*\\Qadt/getActivePrefixList\\E.*",
    ".*\\Qadt/getPrefixMasterList\\E.*",
    ".*\\Qapi/billing/kiosk/saveAndPay\\E.*",
    ".*\\Qapi/global/getAvailableDoctorBySpeciality\\E.*",
    ".*\\Qapi/global/patientRegistration\\E.*",
    ".*\\Qapi/global/patientTypeHeadSearchForAppointmentUnique\\E.*",
    ".*\\Qapi/global/patientTypeHeadSearchForSelfRegistration\\E.*",
    ".*\\Qapi/opd/appointment/check\\E.*",
    ".*\\Qapi/opd/global/patientTypeHeadSearchForSelfRegistration\\E.*",
    ".*\\Qapi/opd/patientDoc/saveAnalyzedReport\\E.*",
    ".*\\QdeleteParticipants\\E.*",
    ".*\\QgenerateTokenForWebUser\\E.*",
    ".*\\Qopd/saveMedication\\E.*",
    ".*\\Qupdate/service\\E.*"
};
```

**Note on `api/billing/kiosk/saveAndPay`:** it's intentionally left **in** this permitAll list here (matching current production behavior) — closing it is a decision for you (it may need auth, or it may need the same kind of in-endpoint payment verification as §1 instead/also, since it's the finalize-and-bill step, not a read-only lookup). Flagged, not decided.

**Real limitation of this whole §3/§4 approach, worth stating plainly:** even a perfect transcription of `JwtTokenFilter`'s *current* list doesn't tell you whether that list itself is *correct* — I have not audited these 201 entries for whether each one is appropriately public. I've only preserved them as-is so this change's net effect, by design, is "close the two identified gaps, otherwise behave identically to today" — not "fix everything wrong with this exemption list," which would be a much larger undertaking.

---

## §5 — Explicitly out of scope (not silently skipped)

- **`api/billing/kiosk/saveAndPay`'s own Java implementation — READ, and CONFIRMED to have the same issue, arguably worse.** `legacy/Billing/src/main/java/com/param/billing/global/transaction/controller/BillingMasterController.java`, both `saveBillFromKiosk` (`/kiosk/saveAndPay`, line 909) and `saveBillFromKioskFutureDate` (`/kiosk/saveAndPay/future`, line 954) take an `EncounterMasterDto` directly from the client `@RequestBody`, including its full `paymentReceiptDetailsDtosList` (amount, transaction number, payment mode ID — client-supplied, matching the pattern traced earlier from `selfRegistrationController.js`'s `settleTranaction()`), and pass it straight through to `iBillingMasterService.saveBillFromKiosk(...)`/`collectPayment(...)` with **zero Razorpay involvement of any kind** — no `RazorpayClient`, no signature check, no order/payment fetch. The *only* gateway-related check anywhere in either method (lines 960-971) is specific to CCAvenue (`paymentModeId == 12 && paymentGatewayProviderId == 2`), and only checks that a transaction number hasn't been reused elsewhere — it doesn't verify the amount, doesn't apply to Razorpay payments (`paymentModeId == 8` per the frontend), and wouldn't catch a fabricated transaction number that simply hasn't been used before. **This means fixing only `getOrderIdForRazorPayPayment`/`getOrderIdUnitSubscriptionPay`/`confirmAppointment` (§1) does not close the gap for the subscription/kiosk flow at all** — a request could skip the order-creation and confirmation steps entirely and POST directly to `saveAndPay` with a fabricated `paymentReceiptDetailsDtosList` claiming any amount was paid via any transaction number, and it would be recorded as a real payment. This needs its own fix, following §1's pattern (signature verification + Razorpay-authoritative amount for any payment record whose `paymentModeId` claims to be Razorpay), but is architecturally more involved than §1 alone: `saveAndPay` is a general-purpose "save bill and record payment(s)" endpoint handling multiple payment modes (cash, card, cheque, CCAvenue, Razorpay), not a Razorpay-specific one, so the fix needs conditional logic per payment record rather than a blanket verification at entry. **Not drafted in this document — needs its own design discussion given the added complexity, and your direction on approach**, rather than a hasty bolt-on.
- **Order-creation-time fee validation** (should `getOrderIdForRazorPayPayment`/`getOrderIdUnitSubscriptionPay` reject a client-supplied amount that doesn't match a real, server-known fee at the moment the order is created, rather than only catching the mismatch at confirmation time) — I could not identify the correct fee-lookup service/DAO call with confidence in this session (candidates like `/api/billing/unit/getDoctorCharges` exist in the codebase but I have not traced whether it's the right, current source of truth for this specific flow). §1's confirmation-time fix still closes the more severe "no proof any real payment happened" gap even without this, but a determined actor could still pay a real, verified, but artificially low amount for a real service unless this is added too.
- **Runtime/staging validation of §3+§4** — not done, not started, per the sequencing recommendation above.

No file under `legacy/` has been modified. Nothing has been compiled or tested. Awaiting your review and explicit approval, item by item if you'd prefer to approve §1/§2 now and hold §3/§4 for later.

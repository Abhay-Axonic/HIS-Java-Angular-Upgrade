# Decision Needed: Razorpay Client-Trusted Amount — `getOrderIdForRazorPayPayment` / `getOrderIdUnitSubscriptionPay`

**Prepared:** 2026-07-15, at owner's explicit request, treated with the **same priority** as `docs/DECISION_NEEDED_RazorPay10xUndercharge.md` (§6 of that document flagged this as a separate, unfixed, undecisioned risk). No application code was touched to produce this document.

**Headline — this is more severe than it first looked.** The original ask was to write up "the two sibling endpoints trusting client-supplied amounts." Tracing what those endpoints actually feed into (not just the endpoints themselves) surfaced four compounding issues, each independently real:

1. Both endpoints put a client-supplied amount into a real Razorpay charge with **zero server-side computation or validation**.
2. The amount later recorded as **"paid" in the actual bill/appointment record is a *different*, independently client-supplied field** — disconnected even from the amount Razorpay was told to charge.
3. **No Razorpay payment signature is ever verified anywhere in this codebase.** The field exists, is read, and is discarded, in all three places it appears.
4. Based on reading the authentication filter's logic, these endpoints **appear to be reachable without any login/session at all**.

Together, these do not just mean "an amount could be wrong" (the 10x-undercharge document's subject) — they mean the system, as currently written, has **no verified source of truth that a payment happened at all**, for either the web-appointment or the unit-subscription payment flow. I want to be precise about confidence level throughout this document: findings 1–3 are confirmed by direct code reading. Finding 4 is a strong, code-based reading of a very convoluted filter (quoted in full below) that I have **not** runtime-tested against a live server — flagged as high-confidence but not proven.

---

## 1. The two endpoints themselves — no server-side amount computation

Both are in `legacy/Billing/src/main/java/com/param/billing/global/controller/RazorPaymentController.java`:

```java
// getOrderIdForRazorPayPayment (lines 94-124) and getOrderIdUnitSubscriptionPay (lines 126-146)
// -- identical shape, different Razorpay key source
JSONObject orderRequest = new JSONObject();
orderRequest.put("amount", reqDto.getRazorPayBillAmount()); // amount in the smallest currency unit
orderRequest.put("currency", "INR");
orderRequest.put("receipt", "order_rcptid_11");
Order order = razorpay.Orders.create(orderRequest);
```

`reqDto.getRazorPayBillAmount()` is an `Integer` field on `RazorpayPaymentReqDto`, populated directly from the `@RequestBody` JSON the client POSTs. Neither method fetches a doctor's configured consultation fee, a unit-subscription plan price, or any other server-known price to compute or check this value against. Whatever integer the client sends is what Razorpay is told to charge, in full.

**Compare this to `docs/DECISION_NEEDED_RazorPay10xUndercharge.md`'s subject:** that bug (now fixed, BR-018) was a *server-side arithmetic error* converting a correct rupee amount to the wrong paise amount. This is different in kind — there is no arithmetic to get right or wrong, because **the server never independently decides what the amount should be in the first place.**

## 2. What actually gets recorded as "paid" — traced end to end, and it's not the same number

### 2a. Web-appointment flow

The frontend (`bookAppointmentController.js:786-794`) sends **two separate amount fields** in the same request, both sourced from the same client-side variable:

```js
var data = {
    unitId : $scope.unitId,
    organizationId : $scope.organizationId,
    amount: $scope.webAppConfDetObj.visitFee,
    razorPayBillAmount: $scope.webAppConfDetObj.visitFee + "00",   // string concat, not *100 -- coincidentally correct only for whole-rupee fees
    appointmentId: $scope.webAppConfDetObj.appointementId,
    visitTypeId: $scope.webAppConfDetObj.visitTypeId
}
```

On the server, `getOrderIdForRazorPayPayment` uses `razorPayBillAmount` to create the Razorpay order (§1), then calls `razorPayService.logRazorPayOrder(reqDto)`. That method (`RazorPayDaoImpl.java:40-60`) does this:

```java
payOrderLogDetail.setAmount(Double.valueOf(reqDto.getAmount()));   // <-- the OTHER field, "amount", not razorPayBillAmount
sf.getCurrentSession().save(payOrderLogDetail);   // -> public.m_razorpay_order_log_details
```

**It logs `reqDto.getAmount()` — not `razorPayBillAmount`.** These are two different fields on the same request DTO. An honest, unmodified client sends the same underlying value into both (just formatted differently), so in normal operation they happen to agree. But nothing on the server enforces that they agree, and nothing prevents a client — a tampered request, a modified page, a direct API call — from sending two *different* numbers.

That logged `amount` is what later becomes the "paid" amount in the real bill. When the patient's browser is redirected back after paying at Razorpay, `confirmAppointment` (same file, lines 148-305) looks up this log row by the real Razorpay order ID:

```java
Response razorPayRes = razorPayService.getAppointmentDataByRazorPayOrderId(razorPayOrderId);
reqDto = (AppointmentConfirmationDto) razorPayRes.getObject();   // reqDto.getPaidAmount() == the logged "amount" field, via SQL alias
...
encounterMasterDto.setTotalBillAmt(reqDto.getPaidAmount());
encounterMasterDto.setPaidAmount(reqDto.getPaidAmount());
paymentReceiptDetailsDto.setAmount(reqDto.getPaidAmount());
...
Response billRes = iBillingMasterService.saveBillFromKioskFutureDate(encounterMasterDto);
BillPaymentCollectionReqDto collectionReqObj = (BillPaymentCollectionReqDto) billRes.getResObject();
razorPayBillService.settleBill(collectionReqObj);   // -> iBillingMasterService.collectPayment(...) -- the real payment-collection record
```

(Confirmed via the raw SQL in `RazorPayDaoImpl.getAppointmentDataByRazorPayOrderId`, line 67: `mrold.amount as "paidAmount"` — `mrold` is `m_razorpay_order_log_details`, the same table `logRazorPayOrder` wrote to.)

**Net effect:** the amount that ends up in the real bill, the real payment receipt, and the real "collect payment" call is the `amount` field the client sent when *creating* the order — not the `razorPayBillAmount` actually charged via Razorpay, and not anything re-derived from Razorpay's own response after the patient paid. A client could send a low `razorPayBillAmount` (charge little via Razorpay checkout) while sending a normal-looking `amount` (so the resulting bill looks legitimate), or vice versa — the two are never cross-checked against each other or against a real fee.

### 2b. Unit-subscription flow — the gap is worse here, and the code itself flags it

`selfRegistrationController.js:4638-4715` (`$scope.openRazorPay`) calls `getOrderIdUnitSubscriptionPay` with the same `amount` / `razorPayBillAmount` pattern as above, then opens the Razorpay Checkout widget. Its success callback:

```js
"handler": function (response) {
    $scope.$apply(function () {
        $scope.trasactionIdFromRazorPay = response.razorpay_payment_id;
        $scope.paymentRazor = {
            paymentModeId: 8,
            transactionNo: $scope.trasactionIdFromRazorPay,
            transactionType: 'Razor Pay',
            amount: $scope.patientAppoObjPayment.totalBillAmount   // <-- client-side scope variable, NOT read from `response`
        };
        $scope.listPaymentDetails.push($scope.paymentRazor);
        $scope.settleTranaction();
        // Optional: call backend to verify/save payment
        // $scope.verifyRazorPayPayment(response);                // <-- commented out
    });
},
```

The amount pushed into the payment record is `$scope.patientAppoObjPayment.totalBillAmount` — a plain Angular scope variable already sitting in the browser, not anything read from Razorpay's `response` object. **The line that would call a backend verification of the actual Razorpay payment is present in the source, commented out.** That is a strong signal the original author knew this step belonged here and it was never completed (or was disabled and never re-enabled).

`settleTranaction()` (`selfRegistrationController.js:3280-3294`) then does the only "check" that exists anywhere in this flow:

```js
var amount = 0;
angular.forEach($scope.listPaymentDetails, function(value, key) {
    amount = amount + parseFloat(value.amount);
});
if (amount != $scope.patientAppoObjPayment.totalBillAmount) {
    angular.element('#saveNormalAppoitment').modal('show');
    return false;
}
```

This sums `listPaymentDetails[].amount` — which was itself just set *from* `$scope.patientAppoObjPayment.totalBillAmount` a moment earlier — and compares it back against `$scope.patientAppoObjPayment.totalBillAmount`. **This is a tautology.** It verifies the client's own arithmetic is internally consistent; it proves nothing about what Razorpay actually processed or what the subscription should actually cost. Whatever this payload becomes downstream (the save-appointment/save-payment call that follows) carries this entirely client-asserted amount forward with no independent source of truth anywhere in the chain.

## 3. No Razorpay payment signature is verified anywhere

Razorpay's standard integration contract requires verifying `razorpay_signature` (an HMAC-SHA256 of `order_id|payment_id` using the account's key secret) before trusting that a `razorpay_payment_id`/`razorpay_order_id` pair represents a genuine, completed payment. A full search of `RazorPaymentController.java` and the rest of the codebase found:

- `razorpay_signature` is read into a local variable in **three places** (`confirmAppointment` line 162, `confirmAppointment1` line 326, `confirmPayment` line 532) and **never referenced again** in any of them — captured, then silently discarded.
- No call to `com.razorpay.Utils.verifyPaymentSignature` (the Razorpay Java SDK's own verification helper, which is already a transitive dependency here since `com.razorpay.RazorpayClient`/`Order` are in active use) or any equivalent HMAC check exists anywhere in `legacy/`.
- No call to Razorpay's `Orders.fetch`/`Payments.fetch` API to independently re-check a payment's status server-side exists anywhere either.

**Consequence:** the system's only basis for believing a payment happened is "a matching row exists in `m_razorpay_order_log_details` for this `razorpay_order_id`" — and that row is created the moment an order is *requested* (§1), not when it is *paid*. Nothing cryptographically ties a `confirmAppointment`/`confirmPayment` call to a genuine Razorpay payment event at all.

Also worth noting: `confirmAppointment1` (lines 308-312) is a small, separate, unauthenticated-looking endpoint that hardcodes a specific test order ID (`"order_HREZNzb6cSwewS"`) and returns whatever appointment data is associated with it — reads as leftover debug/test code, not part of the real flow, but flagging its existence since it's live in source.

## 4. Authentication — appears bypassable, not runtime-confirmed

`legacy/HIS-WAR/src/main/java/com/param/filter/JwtTokenFilter.java` is the servlet filter responsible for requiring a valid JWT. Reading its logic (full file reviewed):

- It only *requires* a token when `unitId`, `userId`, and an `Authorization` header are **all present** on the request (line 73) — an anonymous request that simply omits these headers does not hit the "require valid JWT" branch at all.
- For requests that don't carry those headers, the fallback branch (line 366 onward) allows the request through if, among other conditions, **`splitUrl.length <= 2`** (line 366) — i.e., the URL has at most one path segment after the leading slash.
- Every `RazorPaymentController` endpoint is mapped at root level with no prefix (`/getOrderIdForRazorPayPayment`, `/confirmAppointment`, `/razorPayPaymentLink`, `/getOrderIdForRazorPayPaymentOnBilling`, `/validateRazorPayPaymentLink`, `/confirmPayment` — none are under `/api/...`). Splitting any of these on `/` produces exactly `["", "<endpointName>"]`, length 2 — satisfying the `splitUrl.length <= 2` exemption.
- `getOrderIdUnitSubscriptionPay` doesn't even need that path-length reasoning — it is **explicitly named twice** in the filter's hardcoded exemption list (lines 244 and 538), alongside genuinely-meant-to-be-public endpoints like `/checkEmailAvailability` and `/otpVerification`.

**Reading this code, all of the Razorpay order-creation and confirmation endpoints appear reachable by an anonymous caller with no login at all** — which would mean an attacker doesn't need a compromised patient session or insider access to exercise any of the gaps in §1–3; a direct, unauthenticated HTTP request would do. **I have not verified this against a running instance** (no server was started this session, consistent with holding off on the deployment-verification work per your last instruction) — this is a code-reading conclusion, not a proven exploit. Given the filter's complexity (600+ lines, duplicated conditions, inconsistent path-matching styles), an independent runtime check (e.g., a single unauthenticated `curl` POST to `/getOrderIdForRazorPayPayment` against a dev instance) would be the fastest way to remove all doubt here.

## 4a. Update (2026-07-15, later): the auth-bypass reading is now higher-confidence — the one thing that could have contradicted it turns out to be disconnected

Per owner instruction, read `legacy/HIS-WAR/src/main/java/com/param/config/SecurityConfig.java` to check for a layer that might also gate these endpoints beyond `JwtTokenFilter` alone. That class defines a real, sensible-looking Spring Security policy:

```java
http.csrf().disable()
    .authorizeRequests()
    .antMatchers("/resources/assets/js/**","/adt/organizationMasterList/**","/adt/getUnitByCredentials","/ldapUserCredentialCheck").permitAll()
    .antMatchers(HttpMethod.POST, "/adt/userLogin").permitAll()
    .anyRequest().authenticated()
    .and()
    .addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
```

On paper, `.anyRequest().authenticated()` — with only 5 narrow exceptions, none matching any Razorpay endpoint — would have been exactly the backstop that could catch a request `JwtTokenFilter` lets through. **It does not, because it never runs.** `legacy/HIS-WAR/src/main/webapp/WEB-INF/web.xml` registers exactly three filters (`rateLimitFilter`, `requestWrap` = `JwtTokenFilter` directly, `cors`, all mapped to `/*`) and contains **no `DelegatingFilterProxy`/`springSecurityFilterChain` entry** — the specific bridge Spring Security requires to insert itself into the servlet pipeline. No servlet-spec `<security-constraint>` exists either, and no `WebApplicationInitializer` class provides an alternate wiring path. `SecurityConfig.java` is Spring beans sitting in the application context that are **never invoked for a single incoming HTTP request.**

Net effect: **`JwtTokenFilter` is not one layer among several — it is the entire authentication layer for this application, in production, today.** This doesn't just fail to contradict §4's finding; it removes the one thing that might have. Logged as its own standalone item, since this affects every URL in the application, not just these two endpoints — see `docs/DECISION_NEEDED_SpringSecurityFilterChainNotWired.md`.

## 5. Severity assessment

If §4's reading is correct, this is a **higher-severity, different-category finding** than the (already-fixed) 10x-undercharge bug: not an arithmetic error that undercharges by a fixed, discoverable ratio, but a missing trust boundary that — combined with no signature verification — means an unauthenticated caller could plausibly:
- Create a Razorpay order for any amount of their choosing (§1) without it needing to reflect a real fee.
- Have a real bill/appointment or subscription record created and marked paid for whatever amount they separately claimed (§2), independent of what was actually charged.
- Do so without a genuine, verified Razorpay payment ever being confirmed server-side (§3).

Even discounting §4 entirely (i.e., assuming these endpoints somehow do require login in practice, which I have not disproven either), §1–3 stand on their own as a real gap: **any authenticated user** — not just a patient paying for their own appointment — could exercise the same disconnect between "what Razorpay charged" and "what the bill says was paid."

## 6. What we need from you

This needs a decision on scope and priority before any fix is designed, same as the original document:

1. **Should the amount be recomputed server-side** — the doctor's actual configured consultation fee for `getOrderIdForRazorPayPayment`/the appointment flow, the actual subscription plan price for `getOrderIdUnitSubscriptionPay` — rather than trusted from the client at all? This is the structural fix, but changes what these endpoints require as input (a fee/plan reference instead of a raw amount) and is a larger change than a one-line validation.
2. **Should Razorpay payment-signature verification be added** at the confirmation endpoints (`confirmAppointment`, `confirmAppointment1`, `confirmPayment`), using the Razorpay Java SDK's existing `Utils.verifyPaymentSignature`? This is a more contained, additive fix independent of (1).
3. **Should the `amount` vs. `razorPayBillAmount` disconnect (§2a) be closed** — either by using a single field consistently, or by explicitly validating both against the same server-computed source once (1) exists?
4. **Do you want me to runtime-verify §4's authentication-bypass reading** against a local dev instance (a read-only, no-payment-attempted `curl` test — would not create any real order or touch billing data) before you decide how urgently to treat it? I have not done this yet.
5. **Priority relative to other open work:** you've asked this be treated with the same priority as the original undercharge document. Given what surfaced above, I'd flag this as *at least* equally urgent, and possibly more so given the authentication question in §4 — but the actual priority call is yours.

**Status update (2026-07-15, later):** Owner confirmed this system is live production processing real Razorpay payments — this raises the stakes on everything above, including the still-unresolved historical-reconciliation question in the sibling document. Owner is separately obtaining the production hostname to run the read-only curl test for §4/item 4 above (a request with no `Content-Type: application/json` header, guaranteed to never reach `Orders.create()` regardless of the auth-filter outcome — see conversation for the exact command). §4a above (SecurityConfig.java investigation) was completed per owner instruction and raises confidence in the bypass finding, pending that runtime confirmation. A fix has been drafted for review — see `docs/REVIEW_DRAFT_razorpay-auth-fix-NOT-APPLIED.md` — but **not applied to any source file**, per explicit owner instruction to review and approve before anything is touched.

No code, schema, or billing math has been touched to produce this document. Awaiting your direction.

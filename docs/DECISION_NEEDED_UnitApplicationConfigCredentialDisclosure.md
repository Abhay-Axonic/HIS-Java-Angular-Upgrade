# Decision Needed: `getUnitApplicationBillingConfigByOrgUnit` returns plaintext SMTP password and Razorpay secret key to any authenticated user, no role check

**Prepared:** 2026-07-22, fast-tracked at the same urgency tier as `docs/DECISION_NEEDED_UserAuthenticationPlaintextPasswordDisclosure.md`, per this project's standing practice of writing up live, currently-exploitable security findings the moment they're found, rather than deferring them. Found incidentally during Phase 4 batch 12's Step 5 live testing, while calling a config-lookup endpoint for an unrelated reason (reconstructing a browser session cookie for testing). No application code has been touched to produce this document.

**Note on handling:** during testing, a real API call to this endpoint returned a live plaintext SMTP account password in its response body. That value was seen in a tool output during this session but is **not reproduced anywhere in this document or any other file** — this document describes the finding by field name and endpoint only, never by value. Treat the exposed credential as compromised regardless (see §5).

**UPDATE, same day — this is a known, previously-flagged, never-rotated credential, not a fresh unknown.** `docs/SECRETS_REMEDIATION.md` (Batch 1, ~2026-07-04) already found this exact password hardcoded in multiple Java files AND embedded as a schema-level `DEFAULT` in the tracked DDL (`legacy/Database/HIS-SME-DDL_20210430_150255584.sql:21666-21667`, pairing `smtp_user_id` for one real-looking company mailbox with this password) — flagged under "New findings surfaced during verification — NOT fixed in this batch," explicitly deferred as needing its own decision. `docs/SECRETS_REMEDIATION_BATCH2.md` fixed the Java-source hardcoded copies (now `System.getenv("GLOBAL_SMTP_PASSWORD")`, tied to a second real-looking company mailbox) but **both batches explicitly state no credential was rotated** — that remains outstanding today. Critically, `MailingServiceImpl.java:102` reads the SMTP password from `cookieObjectAppConfig.getPassword()` (sourced from the same DB column this endpoint exposes) **before** falling back to the env var — so the DDL-level default remains the actual effective runtime value for any org/unit that never explicitly overrode it, unneutralized by the prior Java-level fix. What this document adds beyond the prior remediation effort is a **new, previously-unconnected exposure vector**: this specific unauthenticated-role API endpoint serving that same never-rotated value directly to the frontend. Razorpay fields are confirmed `NULL` for this specific dev environment's row (organization_id=1/unit_id=1) — not live data here, though the same broken-access-control mechanism would expose a real Razorpay secret in any org/unit that has one configured.

---

## 1. The endpoint, confirmed exactly

`GET /api/unit/applicationConfiguration/billing/orgId/{orgId}/unitId/{unitId}` — `legacy/Global/src/main/java/com/param/global/controller/UnitApplicationBillingConfigurationController.java:34-44`:

```java
@RequestMapping(value="/billing/orgId/{orgId}/unitId/{unitId}", method = RequestMethod.GET)
public @ResponseBody Response getUnitApplicationBillingConfigByOrgUnit(@PathVariable("unitId") Integer unitId,
        @PathVariable("orgId") Integer orgId) {
    ...
    return unitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
}
```

No `@PreAuthorize`/`@Secured`/manual role check — same missing-authorization pattern as the `userAuthentication` finding, just on a different controller. The response's `object` field is a full serialization of `UnitApplicationConfigurationDto` (`legacy/Global/src/main/java/com/param/global/dto/UnitApplicationConfigurationDto.java`), which includes, among ~80 other configuration fields:

```java
// UnitApplicationConfigurationDto.java:251-256
private String emailId;
private String password;              // <-- plaintext SMTP account password, confirmed live
...
private String razorPayKeyId;          // <-- payment gateway key ID
private String razorPaySecretKey;      // <-- payment gateway SECRET KEY, confirmed present in the same response shape
```

`password` is stored and returned as a raw, unencrypted `String` — confirmed via `UnitApplicationBillingConfigurationDaoImpl.java:216`, `unitApplicationBillingConfiguration.setPassword(unitApplicationConfigurationDto.getPassword())` — a direct pass-through, no `GlobalCommonDateUtils.encrypt()`/`decrypt()` call anywhere in this DAO (unlike the user-password case, this isn't even weakly encrypted — it's stored and served as plain, ordinary text).

## 2. Live verification

Called with a real, valid authenticated session (non-superuser, `isSuperUser='N'`) during otherwise-unrelated testing: `GET /api/unit/applicationConfiguration/billing/orgId/1/unitId/1` returned **200** with a JSON body whose `object.password` field contained a real, live, non-empty plaintext value — confirmed to be the unit's configured outgoing-email account password (sits directly beside `emailId` in the same object, and the field is used elsewhere in the codebase for SMTP-relay configuration, not user authentication). `razorPayKeyId`/`razorPaySecretKey` fields are present in the same DTO shape (not independently re-verified as non-null in this specific dev-environment response, but the mapping/field presence is confirmed from source — worth an explicit re-check).

This endpoint was previously used only as a negative **control** in the `Qmslogo` anonymous-write-access investigation (`docs/DECISION_NEEDED_QmslogoAnonymousWriteAccess.md` §2) — confirmed there to correctly return `401` when unauthenticated, proving the JWT filter is active for this route. That earlier check only established "this endpoint requires *some* valid session" — it did not examine the response body's contents, which is what this document adds: **any** valid session, regardless of role, gets the full plaintext credential set back.

## 3. Severity assessment

**Critical — arguably the most severe of the three credential-disclosure findings logged this session.** Unlike the `userAuthentication` finding (user account passwords, contained to this HIS system) or the `Qmslogo` finding (branding-asset integrity, no credential exposure), this endpoint's payload includes:
- The hospital's **outgoing email account password** (plaintext) — enables sending mail as the hospital's own address, a phishing/impersonation vector.
- The hospital's **Razorpay payment gateway secret key** (if populated) — this project already has three open DECISION_NEEDED documents about Razorpay-related billing risk (`DECISION_NEEDED_RazorPay10xUndercharge.md`, `DECISION_NEEDED_RazorPayClientTrustedAmount.md`, `DECISION_NEEDED_RazorPayment.md`); a leaked secret key would let an attacker interact with the payment gateway account directly, outside of any of those already-documented application-level concerns — a strictly worse, infrastructure-level version of the same risk family.

Any authenticated user of any role — the same "isSuperUser='N'" tier already confirmed for the other findings — can retrieve this. This is broken access control on a scope that extends beyond this HIS application into third-party financial and email infrastructure.

## 4. Proposed fix directions (not implemented — for review)

1. **Add real role/permission enforcement** to this endpoint, restricting it to whichever role(s) actually need unit-level billing/application configuration (likely a narrow admin/superuser tier) — same direction as recommended for the `userAuthentication` finding.
2. **Stop returning secret-shaped fields in this general-purpose config response at all.** `password`/`razorPaySecretKey` (and likely `razorPayKeyId`) should not travel to the frontend in a payload whose primary purpose (per its ~80 other fields) is UI configuration flags (`isCounterApplicable`, `taxInclusive`, speciality IDs, etc.) — split secrets into a separate, tightly-scoped internal-only lookup the frontend never touches, with the backend using them directly for its own SMTP/payment-gateway calls instead of round-tripping them through the browser.
3. **Rotate the exposed email account password and Razorpay secret key** (if genuinely live/production-adjacent, not a dev-only placeholder) — treat as compromised the moment this document is read, independent of when/whether a code fix ships, per standard incident-response practice for any credential that has appeared in a plaintext HTTP response.

## 5. What we need from you

1. **Rotate the affected mailbox password(s) now** (`info@axonichealth.co.in` at minimum — the account tied to the `GLOBAL_SMTP_PASSWORD` env-var fix in `SECRETS_REMEDIATION_BATCH2.md`; confirm whether `info@clinivantage.co.in`, tied to the DDL-level default, is still an active account worth rotating too) — this is an owner/ops action at the mail provider (Gmail, per the DDL's `smtp_host` default), outside what a code change can do. Both prior remediation batches already deferred this exact rotation; recommend not deferring further now that a live, unauthenticated-role exposure vector is confirmed.
2. **Priority relative to the other two open security documents** — given this is a previously-known, still-unrotated credential with a newly-confirmed live exposure path, recommend treating it as at least as urgent as the `userAuthentication` finding, and resolve the rotation question (item 1) independent of and ahead of any code-fix timeline.
3. **Which fix direction(s) to authorize** for the code/DDL-level exposure itself — my recommendation remains (1) and (2) from §4 (scope the endpoint's auth, stop returning secret-shaped fields), with the DDL-level hardcoded default (never addressed by either prior secrets-remediation batch) also worth resolving as part of the same pass, given it's now confirmed to be the actual effective runtime value.
4. Given this was found via the same `cookieObjectAppConfig`-sourcing code path several already-built and in-progress rebuild screens rely on (any screen reading unit application config), **worth flagging**: none of the already-shipped Angular screens currently call this specific `/billing/orgId/.../unitId/...` endpoint directly (they read the value already cached in `cookieObjectAppConfig` from legacy's own login flow) — so this is a pre-existing legacy backend exposure, not something the rebuild has newly introduced or needs to route around today. Confirming this for the record, not proposing any rebuild-side action.

## 6. UPDATE — owner rotated the affected mailbox password(s); proposed fix diffs below, held for approval, nothing applied yet

Owner confirmed the `info@axonichealth.co.in` (and `info@clinivantage.co.in`, if still active) mailbox password(s) have been rotated at the provider — the specific value seen during testing is now invalidated. This section proposes the code/DDL-level fix for the underlying access-control and hardcoded-default issues. **No file below has been edited — this is the exact proposed diff, for review only.**

### 6a. A second, related exposure surface found while researching this fix — same secret, different path

Tracing how the browser ever gets this password in the first place (needed to confirm the fix below doesn't silently break real mail-sending) surfaced that **this endpoint is not the only place the SMTP password reaches the browser.** The login flow (`loginController.js:629-788`, a different endpoint than the one this document is about) also sends the same `UnitApplicationConfigurationDto` — password field included — to populate the browser's `cookieObjectAppConfig` cookie. That cookie is then **echoed back to the server on every subsequent request**, where `SessionHandlerInterceptor.java:43-44` deserializes it straight from the raw cookie value into a server-side bean, and `MailingServiceImpl.java:102` reads the password back out of it to actually send mail — meaning the backend trusts a security-sensitive value supplied by the client on every request, rather than looking it up server-side. This is a deeper architectural pattern (the server's own outgoing-mail credential lives in a client-held cookie) that the fix below does **not** resolve — it only closes the specific `GET .../billing/orgId/.../unitId/...` endpoint's response, which is what this document originally investigated. Flagging this now rather than silently scoping around it; recommend treating it as a follow-on decision, not bundled into the fix below (proposal 6b closes the leak that's actually easy to verify and low-risk to fix immediately; a real fix for the cookie-echo pattern would mean moving this value to a genuine server-side session store, a larger design change).

### 6b. Proposed fix — strip secret fields from this endpoint's response (low-risk, does not affect mail-sending)

Confirmed this endpoint's response is *not* what populates the browser's `cookieObjectAppConfig` cookie (that happens at login, a separate code path per §6a) — stripping these fields here has no effect on the SMTP-sending feature.

**File:** `legacy/Global/src/main/java/com/param/global/service/UnitApplicationBillingConfigurationServiceImpl.java`

```diff
 			if(registrationFieldConfiguration != null){
 				unitApplicationConfigurationDtos.setMobileDigit(registrationFieldConfiguration.getMobileDigit() != null ? registrationFieldConfiguration.getMobileDigit() : 10);
 			}
+			// Security fix, docs/DECISION_NEEDED_UnitApplicationConfigCredentialDisclosure.md --
+			// this response reaches the browser via a general-purpose UI-config lookup; strip
+			// secret-shaped fields the frontend never displays. Does not affect mail-sending,
+			// which reads this value from cookieObjectAppConfig (populated at login, a separate
+			// code path -- see doc section 6a).
+			unitApplicationConfigurationDtos.setPassword(null);
+			unitApplicationConfigurationDtos.setRazorPayKeyId(null);
+			unitApplicationConfigurationDtos.setRazorPaySecretKey(null);
 			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, unitApplicationConfigurationDtos);
```

**Before:** `GET /api/unit/applicationConfiguration/billing/orgId/{orgId}/unitId/{unitId}` returns `password`/`razorPayKeyId`/`razorPaySecretKey` verbatim to any authenticated caller.
**After:** the same endpoint returns every other config field unchanged (confirmed none of the ~80 other fields are used by these three), with those three fields always `null` in the response — verifiable live via the same authenticated-fetch method used to find this issue originally (checking the response shape, not re-triggering the leak).

### 6c. Real per-role authorization — not proposed as part of this fix, flagged as a separate, larger decision

Checked for an existing server-side "is this caller a superuser" helper to reuse for a real role check on this endpoint — **none exists** (confirmed via grep across `legacy/Global/`). This matches the already-documented, systemic finding (playbook Known Caveat #1, and the `userAuthentication`/`Qmslogo` findings this session): legacy has **no backend RBAC enforcement anywhere** — `isSuperUser`/role checks exist only client-side. Building real server-side role enforcement from scratch is a materially larger change than a scoped bug-fix diff, and would need to happen consistently across every endpoint with this gap, not as a one-off for this endpoint. Recommend treating 6b (stop returning secrets) as the immediate mitigation, and real backend RBAC as a separate, project-wide architectural decision — not bundled into this fix.

### 6d. Proposed DDL fix — remove the hardcoded default, so future environments don't inherit a known-bad credential

**File:** `legacy/Database/HIS-SME-DDL_20210430_150255584.sql:21666-21667`

```diff
-    smtp_user_id character varying(100) DEFAULT 'info@clinivantage.co.in'::character varying,
-    smtp_user_pwd character varying(100) DEFAULT 'Cht@12345'::character varying,
+    smtp_user_id character varying(100),
+    smtp_user_pwd character varying(100),
```

**Before:** any fresh database provisioned from this DDL file has this exact (now-rotated, but still checked into git history) email/password pair as the default SMTP config for every unit, until someone explicitly overrides it.
**After:** the column has no default; a fresh deployment starts with `NULL` (matching how `GlobalCommonDateUtils`/`MailingServiceImpl` already fall back to `System.getenv("GLOBAL_SMTP_PASSWORD")` when the DB value is null — the intended, safer path per `docs/SECRETS_REMEDIATION_BATCH2.md`'s own fix).

**Practical follow-up this surfaces, not something I can do:** this local dev DB's own `t_unit_application_billing_configuration` row (org=1/unit=1) still has the **old, now-rotated** password value stored in it (confirmed via direct query, unrelated to the DDL default). If this environment's mail-sending is ever actually exercised, it will now fail against the real mailbox until that row (or the `GLOBAL_SMTP_PASSWORD` env var) is updated with the new real password — something only you/ops can do, since I don't have and shouldn't have that value.

### What we need from you on this section specifically

1. Approve/reject 6b (strip secret fields from this one endpoint) — my recommendation is approve, low-risk, verified not to affect mail-sending.
2. Approve/reject 6d (remove the DDL hardcoded default) — my recommendation is approve, purely additive-safe (removes a default, doesn't touch existing data).
3. Whether 6a (the cookie-echo architectural pattern) and 6c (no backend RBAC exists) should become their own separate DECISION_NEEDED documents, or stay noted here for now.
4. Confirm you'll handle updating this dev DB's stored SMTP password (or the `GLOBAL_SMTP_PASSWORD` env var) separately, outside this session, since I don't have the new value.

No code has been touched to produce this document — 6b/6d above are proposed diffs only, not applied.

## 7. UPDATE — Refined Option A implemented and verified, held on its own branch (`fix/unitappconfig-clientfacing-secret-strip`); §6d (DDL default) remains unapplied

Owner approved "Refined Option A" (`stripClientFacingSecrets()` plus the 3 client-facing call sites: the billing-config GET endpoint, the mobile `DoctorAppAPIController` endpoint, and all 4 login-flow call sites in `UserMasterServiceImpl.java` — one more than the originally-estimated 2-3, found while applying the fix). Explicitly **not** approved/applied in this pass: §6d (the DDL hardcoded-default removal) — still outstanding, needs its own separate approval.

**A real implementation bug was found and corrected during verification, exactly the kind of regression the owner asked to be checked for, not assumed:** the first version of this fix placed the `stripClientFacingSecrets()` call *inside* `UnitApplicationBillingConfigurationServiceImpl.getUnitApplicationBillingConfigByOrgUnit` itself — the shared service method — rather than only at the controller boundary as proposed. `RazorPaymentController.getOrderIdForRazorPayPayment` calls this exact same shared method to obtain the real `razorPayKeyId`/`razorPaySecretKey` for actual payment-order creation. As first committed, this would have silently broken live Razorpay order creation (a `null`-credentialed `RazorpayClient`). Caught during the owner-mandated live-verification pass, before any merge — corrected by moving the strip call to `UnitApplicationBillingConfigurationController.getUnitApplicationBillingConfigByOrgUnit` (the actual REST boundary) and reverting the shared service to its original, unstripped behavior.

**Live verification, both directions, using a temporary synthetic Razorpay key/secret pair on the dev DB's only row (restored to `NULL` afterward, its original state):**
- `GET /api/unit/applicationConfiguration/billing/orgId/1/unitId/1` → `password`/`razorPayKeyId`/`razorPaySecretKey` all `null` in the response, even with real (synthetic) values present on the row.
- `POST /api/doctorApp/getApplicationConfig` (the previously-undocumented 3rd exposure path, found during this investigation) → `password: null`, same confirmation.
- `POST /getOrderIdForRazorPayPayment` (RazorPaymentController's real order-creation endpoint), called directly with the same synthetic values still on the row → server log shows `com.razorpay.RazorpayException: BAD_REQUEST_ERROR:Authentication failed` at `RazorPaymentController.java:115`, the real `razorpay.Orders.create()` call — conclusive proof the code reached that point with the real, non-null synthetic credentials and made an actual API call to Razorpay's servers (rejected only because the test values aren't real Razorpay credentials, not because they were stripped/null).
- The login-flow path (item 3 originally, now closed alongside the others) could not be live-tested end-to-end — entering login credentials is outside what Claude Code will do regardless of authorization. Verified via code review only: all 4 call sites in `UserMasterServiceImpl.java` now call `stripClientFacingSecrets()` before the DTO is attached to the login response, identical in shape to the two paths confirmed live above.

Both jars (`Global`, and `Entity` — redeployed alongside it after discovering the *deployed* `Entity-0.0.1-SNAPSHOT.jar` was stale relative to current `main` source, an unrelated pre-existing environment gap surfaced only because this was the first full Tomcat restart this session) were restored to their original, pre-fix state (checksums verified) after testing — the running dev instance reflects neither fix permanently; both are held on their own branches per Maker-Checker discipline, awaiting merge approval.

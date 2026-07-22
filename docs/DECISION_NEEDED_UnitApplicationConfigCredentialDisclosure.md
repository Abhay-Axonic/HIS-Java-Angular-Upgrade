# Decision Needed: `getUnitApplicationBillingConfigByOrgUnit` returns plaintext SMTP password and Razorpay secret key to any authenticated user, no role check

**Prepared:** 2026-07-22, fast-tracked at the same urgency tier as `docs/DECISION_NEEDED_UserAuthenticationPlaintextPasswordDisclosure.md`, per this project's standing practice of writing up live, currently-exploitable security findings the moment they're found, rather than deferring them. Found incidentally during Phase 4 batch 12's Step 5 live testing, while calling a config-lookup endpoint for an unrelated reason (reconstructing a browser session cookie for testing). No application code has been touched to produce this document.

**Note on handling:** during testing, a real API call to this endpoint returned a live plaintext SMTP account password in its response body. That value was seen in a tool output during this session but is **not reproduced anywhere in this document or any other file** — this document describes the finding by field name and endpoint only, never by value. Treat the exposed credential as compromised regardless (see §5).

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

1. **Confirm whether the credentials seen in this dev environment are real/production-adjacent or disposable dev/test values** — this determines whether rotation (fix 3) is urgent or moot.
2. **Priority relative to the other two open security documents** — my read is this sits at or above the `userAuthentication` finding's urgency, given the payment-gateway and email-infrastructure blast radius, though final priority is your call.
3. **Which fix direction(s) to authorize** — my recommendation is (1) and (2) together, with (3) scoped based on your answer to question 1.
4. Given this was found via the same `cookieObjectAppConfig`-sourcing code path several already-built and in-progress rebuild screens rely on (any screen reading unit application config), **worth flagging**: none of the already-shipped Angular screens currently call this specific `/billing/orgId/.../unitId/...` endpoint directly (they read the value already cached in `cookieObjectAppConfig` from legacy's own login flow) — so this is a pre-existing legacy backend exposure, not something the rebuild has newly introduced or needs to route around today. Confirming this for the record, not proposing any rebuild-side action.

No code has been touched to produce this document. Awaiting your direction.

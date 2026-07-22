# Decision Needed: the unit's SMTP password rides in the `cookieObjectAppConfig` browser cookie on every login, and the backend later trusts that cookie's value back

**Prepared:** 2026-07-22, split out from `docs/DECISION_NEEDED_UnitApplicationConfigCredentialDisclosure.md` §6a at the owner's request, for its own review — mechanically separate exposure path, same underlying secret family. No code has been touched to produce this document.

**Headline — this is a broader, more passively-triggered exposure than the API endpoint the original document covers.** That document found one specific `GET` endpoint returning the secret to any authenticated caller who deliberately calls it. This document is about the **login flow itself** placing the same secret into a browser cookie, unconditionally, for every user of every role, on every successful login — no deliberate API call required, just opening devtools (or the console) after logging in normally.

---

## 1. The mechanism, confirmed exactly

**Where the cookie gets the value (client side):** `legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller/login/loginController.js`:
- Line 474/1204: `$scope.getUnitApplicationBillingConfigByOrgUnit(response.data.object)` — called unconditionally for every successful login, with the login response's own embedded unit-config object (a separate code path from the `DECISION_NEEDED_UnitApplicationConfigCredentialDisclosure.md` endpoint — this data arrives as part of the login response itself, not from a follow-up API call).
- Line 629-751: `getUnitApplicationBillingConfigByOrgUnit(mainObject)` builds a `cookieObjectAppConfig` object literal from `mainObject.unitApplicationConfigurationDto` (the same DTO class the other document covers). Line 751: **`'password': object.password`** — the plaintext SMTP password is explicitly copied into this object.
- Line 788: `$cookies.putObject('cookieObjectAppConfig', cookieObjectAppConfig)` — the object, password included, is serialized and stored as a real browser cookie, readable via `document.cookie` or the browser's Application/Storage devtools panel by anyone with access to that browser session.

**Where the server later trusts it back:** `legacy/HIS-WAR/src/main/java/com/param/comman/SessionHandlerInterceptor.java:43-44` deserializes the `cookieObjectAppConfig` cookie — sent back to the server on every subsequent request — straight into a server-side `CookieObjectAppConfig` Java bean (`legacy/Entity/src/main/java/com/param/service/global/model/CookieObjectAppConfig.java:20`, which has its own matching `password` field). `legacy/Global/src/main/java/com/param/global/mail/MailingServiceImpl.java:102` then reads the password back out of that bean to actually authenticate to the SMTP server:
```java
mailDto.setPassword(sessionService!=null && sessionService.getCookieObjectAppConfig()!=null && sessionService.getCookieObjectAppConfig().getPassword()!=null
    ? sessionService.getCookieObjectAppConfig().getPassword()
    : System.getenv("GLOBAL_SMTP_PASSWORD"));
```
The DB-sourced, client-echoed value is tried **first**; the environment variable introduced by `docs/SECRETS_REMEDIATION_BATCH2.md` is only a fallback for when the cookie-sourced value is absent.

## 2. Why this is worse than the endpoint-based finding, in one respect

The other document's endpoint at least requires an attacker (or a curious/careless user) to know the exact URL and craft an authenticated request. This path requires nothing — it happens automatically, passively, for every single login, for every role, with the value sitting in ordinary browser storage for as long as that cookie persists. Any user who has ever logged into this application, on any device, has had this password pass through their browser in plaintext.

## 3. Relationship to the other document

Same underlying secret (the unit's `smtp_user_pwd`), same root design flaw (a config DTO meant for UI display fields also carries genuine secrets), but a **mechanically distinct code path** (login response vs. a specific config-lookup `GET`) with a **different fix shape** — stripping the field from one API response (as proposed in the other document's §6b) does **not** touch this path at all; the login flow is a separate controller/response entirely. Fixing this one requires either (a) not including `password` in the login response's `unitApplicationConfigurationDto` in the first place, or (b) not copying it into the `cookieObjectAppConfig` cookie client-side even if the backend continues sending it, or (c) the deeper fix — moving the SMTP password out of client-echoed cookie state entirely and having the server look it up directly (by unit ID, already known server-side from the authenticated session) whenever `MailingServiceImpl` needs it, rather than trusting a value the client hands back.

## 4. Urgency, given the affected mailbox password(s) were just rotated at the provider

**Not an active emergency against a live secret today** — the old value now sitting in already-issued browser cookies is invalidated. **But time-sensitive relative to when the new password is configured into this application.** The moment whoever manages this system enters the new rotated password into the app's own config (the DB column or the `GLOBAL_SMTP_PASSWORD` env var) and any user next logs in, the fresh password will flow into that user's `cookieObjectAppConfig` cookie via the exact same unfixed mechanism. **Recommend fixing this path before the new password is configured into the app**, not after — otherwise the rotation only buys the short window until the next login.

## 5. Proposed fix directions (not implemented — for review)

1. **Minimum, low-risk fix:** stop including `password` (and any other secret-shaped field — same audit as the other document's `razorPayKeyId`/`razorPaySecretKey`) in the `unitApplicationConfigurationDto` object embedded in the login response, mirroring the other document's §6b approach but applied to the login endpoint instead. This still leaves `MailingServiceImpl`'s primary read path broken (it currently expects the cookie to carry the value) — would need to fall through to `System.getenv("GLOBAL_SMTP_PASSWORD")` for every request instead, meaning that env var would need to be reliably configured in every environment that sends mail (per `docs/SECRETS_REMEDIATION_BATCH2.md`'s own "DEPLOYMENT REQUIRED" checklist, this is not yet universally true).
2. **More complete fix:** have `MailingServiceImpl` (and any other consumer of `cookieObjectAppConfig.getPassword()`) look the SMTP password up directly from the database by unit ID (already known server-side from the authenticated session, no client input needed) instead of reading it from the deserialized cookie at all. This removes the client-echo trust entirely, is more robust (works regardless of which browser/session touches the mail-sending code path), and doesn't depend on every environment correctly setting an env var. Larger change than (1) — touches the actual mail-sending code path, needs its own testing.
3. Either direction should also apply to any other fields in `CookieObjectAppConfig` that turn out to be secrets, not just `password` — worth a quick audit of that class's full field list before considering this closed.

## 6. What we need from you

1. **Confirm whether to fix this now (before configuring the new rotated password into the app) or accept the short window and do it later** — my recommendation is now, precisely because of the timing risk in §4.
2. **Which fix direction** — (1) is faster but leaves a same-shape gap (client-supplied secret) for other environments that haven't wired the fallback env var; (2) is the more complete fix, larger change.
3. Confirm whether this should block/precede whoever updates the SMTP config with the new password, or whether that update is happening through a channel unaffected by this app (e.g., updated directly at the mail provider only, with this app's stored copy intentionally left stale/disabled) — that would change the urgency calculus in §4.

No code has been touched to produce this document. Awaiting your direction.

## 7. UPDATE — resolved as part of the Refined Option A fix on `fix/unitappconfig-clientfacing-secret-strip`

The owner approved "Refined Option A" for `docs/DECISION_NEEDED_UnitApplicationConfigCredentialDisclosure.md`, which included all 4 call sites in `UserMasterServiceImpl.java` where the login response's `UnitApplicationConfigurationDto` is assembled — the exact object this document's mechanism (§1) copies into the `cookieObjectAppConfig` cookie. Each of the 4 now calls `stripClientFacingSecrets()` before the DTO is attached to `masterDto`/the login response, closing this path directly: the password (and `razorPayKeyId`/`razorPaySecretKey`) will no longer be present in the cookie at all after this fix merges, for any login, any role.

This was verified via code review, not a live login test — entering login credentials is outside what Claude Code will do regardless of authorization. The two paths verified live for the same DTO (the billing-config GET endpoint and the mobile `DoctorAppAPI` endpoint) use identical logic and confirmed cleanly.

Held on the same branch as the parent document's fix, awaiting the same merge approval. This resolves the timing concern in §4 — once this branch merges, the new rotated password can be safely configured into the app without recreating this exposure on the next login.

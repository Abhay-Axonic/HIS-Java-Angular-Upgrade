# Decision Needed: `screen-images` endpoint (used by the `Qmslogo` screen) accepts anonymous, unauthenticated writes — including from the public internet

**Prepared:** 2026-07-22, fast-tracked at the same priority tier as `docs/DECISION_NEEDED_UserAuthenticationPlaintextPasswordDisclosure.md` and the CORS finding, per this project's standing practice of writing up live, currently-exploitable security findings the moment they're found during batch research, rather than deferring them to "when the screen is built." Found during batch 12's Step 0/1 audit of the `Qmslogo` screen. No application code has been touched to produce this document — proposal only.

**Headline — a real backend endpoint that writes hospital branding data (logo images, application name, copyright text) accepts requests from anyone, with zero authentication, confirmed live against the running dev instance.** This is a lower-severity sibling to the `userAuthentication` password-disclosure finding — no patient data or credentials are exposed — but it's a genuine broken-access-control bug: any unauthenticated client on the network (or, if this backend is ever internet-reachable the way `pathology/report`/`QR` in the same exemption list imply some endpoints are meant to be, anyone on the internet) can overwrite the login-screen logo, kiosk header/footer branding, and the application name/copyright text shown across every unit and organization the system serves.

---

## 1. The exemption, confirmed exactly

`legacy/HIS-WAR/src/main/java/com/param/filter/JwtTokenFilter.java` runs on every request and only enforces JWT/session validation inside a large conditional (lines 73-99+) that excludes a long list of URL substrings from the auth requirement. Two of the matches (lines 81 and 373 — the filter apparently has two near-identical enforcement blocks) are:

```java
|| requestUrl.contains("/applicationConfiguration/screen-images")
```

This match is a **plain substring check with no HTTP-method distinction** — it exempts `GET /api/unit/applicationConfiguration/screen-images/...` (reading branding images, plausibly intentional — see §3) **and** `POST /api/unit/applicationConfiguration/screen-images` (writing/overwriting them) equally. The backend method behind the POST, `UnitApplicationBillingConfigurationController.saveScreenImage(...)` (`legacy/Global/src/main/java/com/param/global/controller/UnitApplicationBillingConfigurationController.java:152`), performs a real, unconditional write — no separate auth/role check of its own inside the method body (confirmed by direct read: it takes `organisationId`/`unitId`/`imageTypeId` and a file/text payload straight from the request and persists it).

## 2. Live verification — confirmed against the running dev instance, read-only probes only

Tested from a fresh, deliberately **unauthenticated** request (no cookies, no `Authorization` header) against the real backend at `localhost:8080` (via the Angular dev-server proxy):

| Request | Result |
|---|---|
| `GET /api/unit/applicationConfiguration/screen-images/1/1`, zero credentials | **200 OK** |
| `GET /api/unit/applicationConfiguration/billing/orgId/1/unitId/1` (a different endpoint on the *same* controller, used as a control), zero credentials | **401 Unauthorized** |

The control endpoint's 401 proves `JwtTokenFilter` is working normally elsewhere in this exact controller — this is a targeted, deliberate-looking exemption for this one URL substring, not a broken filter. **The write path itself (`POST .../screen-images`) was not live-fired during this audit** (no test data was submitted, per this project's discipline against live-mutating real data without a clear need) — but the exemption condition in the filter source applies identically to both methods, and the controller method has no independent auth check, so there is nothing in the code path that would block an unauthenticated POST the way there is nothing blocking the unauthenticated GET that was confirmed live.

## 3. Why this is probably a real bug, not intentional design — but not certain

The exemption list this line sits in (`JwtTokenFilter.java:73-99+`) contains ~25 other URL substrings, several of which look like legitimate, deliberate public-access cases: `/adt/userLogin` (pre-login, obviously needs no token), `/generateToken`, `/QR`, `/pathology/report`, `/RIS/RADIOLOGY` (plausibly signed-link report downloads), `/api/global/getVideoConsultationLinkFromHIS` (a deep link a patient might open outside any session). **Reads** of `screen-images` fit this same pattern well: branding images need to render on the pre-login page and on public-facing kiosk/token/nursing/consultant displays (confirmed callers: `loginController.js`, `doctorDeskController.js`, `cabinDisplayController.js`, `triageDisplayController.js`, `nursingAssesmentDisplayController.js`, `consultantDisplayController.js`, `QMSMasterController.js`, `QMSTokenDisplayMasterController.js`, `erPatientListController.js`, `selfRegistrationController.js` — all readers, all plausible pre-login/kiosk contexts). **Writes reaching the same exemption is very unlikely to be intentional** — nothing in the frontend ever calls `POST .../screen-images` from a pre-login or kiosk context; every caller of the save functions in `qmslogo.js` is inside the authenticated admin `Qmslogo` screen itself. This has the same shape as the CORS finding (`docs/DECISION_NEEDED_CorsWildcardOriginCredentials.md`) — a coarse, URL-substring-based rule that wasn't scoped tightly enough, most likely written to solve the read/rendering case and accidentally covering the write case too.

## 4. Severity assessment

**Real, but lower urgency than the password-disclosure finding.** No patient data, credentials, or PII is exposed — the blast radius is branding/display content (logo images, app name, copyright text) rather than anything clinically or financially sensitive. Impact is closer to defacement/integrity than confidentiality: an unauthenticated actor could replace a hospital's login-screen and kiosk branding with arbitrary uploaded image content, system-wide, for any `organisationId`/`unitId` they choose to pass. Depending on what file-type validation (if any) exists server-side — not checked in this audit, only the frontend's client-side `validfile` directive was examined, which is trivially bypassable by calling the API directly — there may also be a secondary file-upload risk (arbitrary file write to whatever path the server stores these images at) worth a security-focused follow-up, separate from the access-control question this document raises.

## 5. Proposed fix (not implemented — for review)

1. **Scope the `JwtTokenFilter` exemption to the read path only** — either match on `GET` specifically for this URL substring, or split the exemption so `POST /applicationConfiguration/screen-images` requires the normal token/role check while `GET` remains public (matching every plausible legitimate use case found above).
2. **Add an explicit role/module check to `saveScreenImage`** in `UnitApplicationBillingConfigurationController`/`Service`, independent of fix (1) — defense in depth, consistent with how this project is already treating the `userAuthentication` finding's missing role checks.
3. **Separately worth deciding: server-side file-type/size validation on the upload path** — a quick check of whether any exists beyond the trivially-bypassable frontend `validfile` directive, given this becomes more important once (or if) the endpoint requires auth but is still reachable by any authenticated user regardless of role.

## 6. What we need from you

1. **Priority relative to the `userAuthentication` finding and other open items** — my read is this sits below the password disclosure (no PII/credential exposure) but above routine findings, given it's a live, unauthenticated, network-reachable write today.
2. **Which fix direction to authorize** — my recommendation is (1) and (2) together (scope the exemption to GET, add a real role check), with (3) as a follow-up investigation.
3. **Should this affect batch 12's `Qmslogo` rebuild?** Per standing project discipline, my default is that the rebuild reproduces legacy's actual current behavior (an authenticated Angular UI calling a backend that happens to have no real enforcement) unless you direct otherwise — the Angular-side route itself will still go through the rebuild's normal session/guard conventions either way, since this gap lives entirely in the legacy Java backend, not in what the frontend requires. Say so explicitly if you want anything different for this specific screen's Step 3 rebuild.

No code has been touched to produce this document. Awaiting your direction.

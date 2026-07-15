# Cookie Bloat Migration Plan

**Status: Investigation complete, plan revised twice (2026-07-09) per owner-directed re-investigation of cross-tab semantics — see §4a and open question 11. Phase A implemented on `fix/cookie-bloat-phase-a-frontend` (291 files) using the corrected Group 1/Group 2 split below, including the final store/counter/nursing-station-cluster-to-Group-1 correction. Not committed/merged — awaiting owner review and Checker sign-off per RULES.md item 6.**

**Bug being addressed:** `loginController.js` (and, as discovered below, ~20 other controller files) store large JSON blobs and a JWT as browser cookies via Angular's `$cookies.putObject`/`$cookies.put`. Cookies are sent on *every* HTTP request (unlike sessionStorage/localStorage), and the cumulative size trips Tomcat's "Request header is too large" error on every request after login — confirmed live against a fresh browser session on the running `his-local-tomcat` container. This is a genuine, currently-reproducible bug that can lock any user with enough session data out of parts of the app.

This document is a Blueprint-only investigation and plan per `CLAUDE.md`. Per `RULES.md`, any actual migration is a **Behavioral Change** (storage location for session data, including auth-adjacent data, is an observable difference) and requires its own `BR-PENDING-*` manifest entry, owner sign-off, and a dedicated feature branch with Maker/Checker review before it ships. Nothing here has been implemented.

---

## 1. Full cookie inventory

Search scope: all of `legacy/HIS-WAR/src/main/webapp/resources/assets/js/` (1,326 JS files), not just `loginController.js`. **34 distinct cookie keys are actually written/read app-wide** — the 12 known from the prior login-fix session, plus 22 more discovered in this investigation (20 live, 2 dead).

### 1a. The "big three" — written at login, re-read constantly

| Key | Write site(s) | Shape | Size estimate |
|---|---|---|---|
| `cookieObject` | `loginController.js:407-467,1139-1199` | Flat object, ~30 fields: org/unit/user/counter/store ids, embedded JWT `authToken`, ~20 billing/UI permission flags | **Large** — JWT alone ~200-400+ bytes, plus 30 keys → likely 600B-1.2KB |
| `cookieObjectAppConfig` | `loginController.js:677-785` | Flat object, ~90 fields, almost all unit-level billing-config booleans/short strings/ids | **Large** — 1.5-2.5KB |
| `cookieObjectAppConfigSecond` | `loginController.js:790-825`, patched at `:869-875` | Flat object, ~30 fields (invoice/insurance flags, `navBarImg` URL, timezone, etc.) | **Medium** — 500B-1KB |

These three alone likely account for 2.6-5.7KB of cookie payload, re-sent on every request.

### 1b. The other 9 originally-known keys

| Key | Write site | Shape | Size |
|---|---|---|---|
| `labConfig` | `loginController.js:883-942` (`getLabConfiguration()`) | Flat object, ~26 fields, all ids/booleans | Small-medium, ~400-600B |
| `templateConfig` | `loginController.js:949-964` (`getTemplateConfig()`) | 3 fields (`apiKey`, `templateUrl`, `templateForm`) | Tiny, <100B |
| `counterList` | `loginController.js:832-848` (`getCounterListByUser()`), `JSON.stringify(counterList)` | Array of `{id, name}` pairs | Small |
| `isUserCounterApplicable` | `loginController.js:389-390,1123-1124` | Primitive `'Y'`/`'N'` string | Tiny |
| `isCounterApplicable` | `loginController.js:787` | Primitive | Tiny |
| `isSkipPharmacySettlement` | `loginController.js:633` | Primitive boolean | Tiny |
| `mpesaRigths` | `loginController.js:1014-1028` (`getMpesaRoles()`) | Primitive boolean | Tiny |
| `rmoMappedOPDoctorList` | `loginController.js:1289` (`getRmoRoster()`) | Array of doctor-roster DTOs | Medium |
| `rmoMappedIPWard` | `loginController.js:1293` | Array of ward objects | Medium |

Also found at login but not in the original 12: `macAddress` (`loginController.js:344,1078`, plain string, tiny).

### 1c. Newly discovered keys (20 live, beyond `loginController.js`)

**Nursing-station cluster** — one ~6-line block copy-pasted into ~20 controllers (dashboards, ADT transfer/discharge screens, pharmacy/nursing workstation screens). Representative site: `nurseDashboardMasterController.js:106-152`.

| Key | Shape | Size | Note |
|---|---|---|---|
| `specialtyNursingStationCookies` | `{departmentNursingStationlist: [...]}` from `getDeptListByNursingStationId` | Medium | Written from ~20 files |
| `specialtyNursingStationCookiesNur` | **Identical payload to the above**, written a second time under a separate key | Medium | Pure duplication — candidate for consolidation |
| `nursingStationsWardsList` / `nursingStationsWardsListforEr` | Array of ward objects | Medium | Same duplication pattern |
| `nursingStationDesc` | String | Tiny | |
| `nursingStationId` | Number/string | Tiny | |
| `mappedNurseBedList` | Array of numeric bed ids | Small | `nurseDashboardMasterController.js:183-192` |

**Encounter/patient-summary cluster** — identical ~12-23-field flat object, written from 8 files (`config/app.js:2525-2554`, `encounterQueueManagementController.js` x6, `casualtyWorklistController.js`, `counterWorklistController.js`, `encounterQueueEHCController.js`, `doctorDeskController.js`, `OPDCoverSheetController.js`, `progressNoteController.js`).

| Key | Shape | Size |
|---|---|---|
| `cookieEncounterObject` | Flat object: patientName, uhidNumber, age, encounterId, doctorId, visitTypeId, etc. | Small |

**Counter/store/misc cluster** (`config/app.js`):

| Key | Write site | Shape | Size |
|---|---|---|---|
| `counterMstObj` | `app.js:1825,1831` | `{counterId, counterDesc}` or `null` | Tiny |
| `selectedTopStore` | `app.js:2618,2636` | Single store DTO object | Small |
| `selectedCssdStore` | `app.js:2750` | Store id/object (bound via HTML select) | Tiny-small |
| `selectedPageName` | `app.js:185` | String menu label | Tiny |
| `selectedPageNameForAdmissionNote` | `app.js:620` | Boolean/string flag | Tiny |

**Domain-specific one-offs:**

| Key | Write site(s) | Shape | Size |
|---|---|---|---|
| `bedManagmentCookies` | `bedManagementController.js:809-840` (canonical; also `mapOfBedERController.js:837`, `OTRequestController.js:176`, `transferToICUController.js:728` sets `null`) | `JSON.stringify(...)`, ~28 primitive fields | Small-medium, ~300-600B |
| `cookieMorgueBedObject` | `morguePendinglistController.js:114`, `morgueReservationController.js:625` | 9 primitive fields | Tiny |
| `webAppOrgUnitCookieObj` | `webAppController.js:34` | `{orgId, unitId}` | Tiny |
| `corporateStatmentObject` | `corporateStatementMasterController.js:230` | 4 fields | Tiny |
| `cookieDietObject` | `dietOrderPatientListController.js:604` | Single diet-order/patient row object | Small |
| `tempAppointmentObjet` | `pesapalSuccessPageController.js:97`, `selfRegistrationController.js:4000-4001` | Full appointment/payment object + nested `listPaymentDetails` array | Medium, grows with payment line count |

**Dead writes (flagged, not part of the header-size problem, but pre-existing bugs worth a separate ticket):**

- `rmoVisit` — write call at `loginController.js:1295` is commented out; 5 files (`doctorDeskController.js`, `transferToICUController.js`, `dayCareDischargeSummaryController.js`, `erPatientListController.js`, `masterDischargeSummaryController.js`) still call `$cookies.getObject('rmoVisit')` expecting a value that is never written — always `undefined` today.
- `cookieObjectReschedule` — both write sites (`appointmentSchedulingController.js:2620`, `appointmentListController.js:2258`) are commented out. Never written.

(`myFavorite` appeared only in the vendor `angular-cookies.js` JSDoc example comments — not application code, excluded.)

---

## 2. Read-site inventory

### 2a. `cookieObject` and `cookieObjectAppConfig` — far larger blast radius than assumed

A prior session's finding stated `cookieObject.authToken` was read ~1031 times through "a centralized Authorization-header pattern." This investigation found that claim needs correction:

- **There is no `$httpProvider.interceptors` registered anywhere** in the app (confirmed by grep — only `$httpProvider.defaults.withCredentials = true` is set, at `config/app.js:22043-22044`).
- The Authorization-header pattern is manually copy-pasted boilerplate (read `cookieObject` → pull `.authToken` → set `Bearer <token>`), in **9 files, 18 occurrences**, mostly in `service/GenericService.js` (11 of 18).
- **`cookieObject` is read via `$cookies.getObject`/`$cookies.get` 1,196 times across 1,022 distinct files** — roughly 85% of all controllers in the app. The overwhelming majority of these reads have nothing to do with the Authorization header; they pull other fields directly into scope bindings and API-call params:

| Field | Read count |
|---|---|
| `.userId` | 1,451 |
| `.organizationId` | 1,337 |
| `.unitId` | 1,314 |
| `.storeId` | 455 |
| `.doctorId` | 45 |
| `.isBillManipulationAllowedUserLevel` | 44 |
| `.mpesaRequired` | 25 |
| `.authToken` | 18 |
| ~35 other fields (`.dashboard`, `.isRMO`, `.atCopRequired`, `.changePayeeRights`, `.counterId`, etc.) | tail, lower frequency |

A full file-by-file listing of ~1,175+ non-auth read sites is not practical to enumerate here; the field-frequency table above is what should drive migration design (see §4).

- **`cookieObjectAppConfig`**: 247 read sites across 231 distinct files. Dominant fields: `.defaultSelfTariffId` (102), `.isPreOrPostBilling` (91), `.selfPayeeId` (81), `.roleName` (73), `.radiologySpecialityId` (72), `.nurseRole` (62), `.opdBillingCatId` (56), `.isAppointmentRequired` (50), `.hemodialysisSpecialityId` (47), `.doctorRole` (45), `.pharmacyServiceId` (41), `.dateFormat` (33), plus ~30 lower-frequency flags.

**Conclusion: both of these keys are read essentially everywhere in the app, not in a small isolated layer.** Any migration must supply these fields synchronously to nearly every controller, not just to one auth interceptor.

### 2b. The other 11 keys — full read-site lists (all captured, not sampled)

| Key | Read sites | Pattern |
|---|---|---|
| `cookieObjectAppConfigSecond` | 88 sites / ~78 files | `var cookieObjectAppConfigSecond = $cookies.getObject(...)` at the top of nearly every controller; fields pulled: `.isBillManipulationAllowedAppLevel` (45), `.microBiologyValidationObj`/`.microBiologyReleaseObj`/`.microBiologyDptObj` (14 each), `.pocWorklistObj` (8), `.exceededBillAmountSettlement` (8), `.navBarImg`/`.accessOfAbdm`/etc. Two files assign the whole object to `$scope.dateTimeZone`. |
| `labConfig` | 17 sites | Uniform: `var labConfig = $cookies.getObject('labConfig')`, then `$scope.labSpecialityId = labConfig.labSpecialityId` (files: `doctorDashboardController.js:37`, `CEOdashbordController.js:41`, `config/app.js:1909`, `financerDashboardController.js:56`, `mapOfBedERController.js:145`, `EncounterController.js:139`, `bedManagementController.js:105`, `consentPrintController.js:64`, `serviceRendering.js:136`, `otBillingCompletionDetailsController.js:60`, `detailsCorporateStatementController.js:37`, `newOPDBillingController.js:132`, `debitNoteController.js:32`, `IPDChargesController.js:112`) |
| `templateConfig` | 2 sites, both **dead reads** | `OPDCoverSheetController.js:913`, `IPDCoverSheetController.js:61` — local variable never referenced again after assignment |
| `counterList` | 28 sites | `JSON.parse($cookies.get('counterList'))` matching the write side's `JSON.stringify` — no read/write type mismatch here |
| `isUserCounterApplicable` | 25 sites | Paired with `isCounterApplicable`, gates whether `counterList` is used |
| `isCounterApplicable` | 25 sites | Same gating pattern, 1:1 paired with the above |
| `isSkipPharmacySettlement` | 2 sites | `walkInSalesController.js:14`, `outPatientSalesWorklistDetailsController.js:132` — feeds direct billing-logic branches at `walkInSalesController.js:4283`, `outPatientSalesWorklistDetailsController.js:7672,8574` (gates whether pharmacy settlement is skipped — **billing-accuracy relevant**, flag per CLAUDE.md mission #1 if this value is ever wrong in transit) |
| `mpesaRigths` | 19 sites | Drives show/hide of Mpesa payment UI |
| `rmoMappedOPDoctorList` | 11 sites | Always paired with `rmoMappedIPWard`, gated by `isRMO=='Y'` |
| `rmoMappedIPWard` | 12 sites | Same pairing, gates IP ward visibility for RMO users |

**`document.cookie` direct access:** none of the 12 keys are ever read via raw `document.cookie`. Two unrelated direct uses exist (`dataObjectController.js:33,37` — unrelated flag cookie; Twilio SDK's own internal cookie) — neither touches any key in this plan.

**Non-Angular bootstrap:** checked `index.html`, `indexDuo.jsp`, `login.html`, `redirect.jsp`, `welcome.jsp`, `ldap-success.jsp` — none reference any of these 12 keys or `authToken`. All reads happen inside Angular controllers/services only.

---

## 3. Java-side dependency check — the critical finding

Searched all 8,254 `.java` files under `legacy/` (all modules) for `HttpServletRequest.getCookies()` / `Cookie` usage: **34 call sites across 17 files, all of which filter for exactly two cookie names: `cookieObject` and `cookieObjectAppConfig`. No other cookie name (of the 34 in this plan) is ever read by Java.**

| Cookie | Read directly from raw `Cookie` header in Java? | Evidence |
|---|---|---|
| `cookieObject` | **YES — load-bearing** | `legacy/HIS-WAR/src/main/java/com/param/comman/SessionHandlerInterceptor.java:33-45` decodes it into `CookieObjectDto`, stores into `SessionService`. Registered globally for `/api/**` in `legacy/HIS-WAR/src/main/webapp/WEB-INF/spring-dispatcher-servlet.xml:201-206`. `SessionService.getOrganizationId()/getUnitId()/getUserId()/getCounterId()/getStoreId()` (fed by this cookie) are called from **343 files** across Billing/Procurement/ADT/Global. Also read a second, independent time by `legacy/Global/src/main/java/com/param/global/service/GetStoreListUsingsessionService.java:46-52`, which mutates and re-emits it — **not yet fully traced, see §5 open questions.** 15 more controllers (mostly under `/adt/**`, one `/LIS/global/**`, not covered by the interceptor's `/api/**` pattern) do their own redundant direct read of the same cookie: `PendingRequestController.java`, `MaritalStatusMasterController.java`, `IdentificationMasterController.java`, `RaceMasterController.java`, `OccupationMasterController.java`, `NationalityMasterController.java`, `RelationMasterController.java`, `PrefixMasterController.java`, `GenderMasterController.java`, `SurgeryGradeMasterController.java`, `TestCostHeadController.java` (plus 4 more under `/api/**` that are redundant duplicates of the interceptor). |
| `cookieObjectAppConfig` | **YES — load-bearing** | Same interceptor, lines 38-39,44; feeds `SessionService.getSelfPayeeId()/getDefaultSelfTariffId()/getGroupPracticeNo()/getGroupPracticeName()/getStartDateFormat()/getPracticeNo()/getIsPreOrPostBilling()`. |
| `cookieObjectAppConfigSecond` | No | 0 hits |
| `labConfig` | No | 0 hits |
| `templateConfig` | No | 0 hits |
| `counterList` | No | 0 hits |
| `isUserCounterApplicable` | No | 0 hits |
| `isCounterApplicable` | No | 0 hits |
| `isSkipPharmacySettlement` | No | 0 hits |
| `mpesaRigths` | No | 0 hits |
| `rmoMappedOPDoctorList` | No | 0 hits |
| `rmoMappedIPWard` | No | 0 hits |
| `authToken` (JWT) | **No** | `JwtTokenFilter.java` reads the JWT exclusively from the `Authorization: Bearer` header (lines 43, 337-338), never a cookie. It also reads `userId`/`unitId` as plain custom headers (lines 69-70), not cookies. Confirmed no other filter (`RequestWrapperFilter`, `CORSFilter`, `RateLimitFilter`, `SessionObjectInterceptor`) reads cookies for auth/session purposes. |

The Java-side search was **not scoped to a pre-supplied list of names** — it enumerated every `getCookies()`/`Cookie` call site in the entire backend and reports every cookie name any of them ever filters on. Since that exhaustive scan turned up only `cookieObject` and `cookieObjectAppConfig`, this also confirms the 20 newly-discovered keys from §1c have zero Java dependency, not just the originally-known 12.

Side note (not in scope, flag for a future finding): `SessionService` is registered as a Spring **singleton** bean, not request/session-scoped — a thread-safety concern independent of this cookie migration, worth its own investigation later.

Also relevant context: `CORSFilter.java` sets `Access-Control-Allow-Credentials: true`, confirming cookies are currently exchanged cross-origin for this app — a constraint to keep in mind if `cookieObject`/`cookieObjectAppConfig` are ever restructured (a new slim replacement cookie would need the same `SameSite`/credentials handling).

---

## 4. Recommendation — phased migration

### Phase A (recommended first — frontend-only, no backend change, no Java risk)

**SUPERSEDED 2026-07-09 — see §4a.** The blanket "move all 22 fully client-only keys to sessionStorage" recommendation below was based on an incomplete model of how staff actually use the app (single tab per session). The owner corrected this: staff routinely keep multiple browser tabs open at once (e.g. a nurse working one patient in one tab, opening a second tab for a different patient). A uniform sessionStorage migration is wrong for any key that represents standing user/session context rather than a specific in-progress record, since sessionStorage is isolated per tab. §4a below replaces the single key list in this section with a verified two-group split. The original text is left in place for audit history; **do not implement from this section — implement from §4a.**

~~Move all 22 fully client-only keys to sessionStorage.~~ These have zero Java dependency (§3) and only ever used within the same browser session (login → logout/tab-close), so sessionStorage is the correct replacement (not localStorage — nothing here should outlive the tab, and using localStorage would introduce a *new* behavior: stale org/unit config surviving into a new session, which RULES.md would treat as a Behavioral Change on its own).

Keys: `cookieObjectAppConfigSecond`, `labConfig`, `templateConfig`, `counterList`, `isUserCounterApplicable`, `isCounterApplicable`, `isSkipPharmacySettlement`, `mpesaRigths`, `rmoMappedOPDoctorList`, `rmoMappedIPWard`, `macAddress`, `specialtyNursingStationCookies`, `specialtyNursingStationCookiesNur`, `nursingStationsWardsList`, `nursingStationsWardsListforEr`, `nursingStationDesc`, `nursingStationId`, `mappedNurseBedList`, `cookieEncounterObject`, `counterMstObj`, `selectedTopStore`, `selectedCssdStore`, `selectedPageName`, `selectedPageNameForAdmissionNote`, `bedManagmentCookies`, `cookieMorgueBedObject`, `webAppOrgUnitCookieObj`, `corporateStatmentObject`, `cookieDietObject`, `tempAppointmentObjet`.

Mechanical change per key, at every site identified in §1c/§2b:
- Write sites: `$cookies.putObject('X', val)` → `sessionStorage.setItem('X', JSON.stringify(val))`; `$cookies.put('X', val)` (primitives) → `sessionStorage.setItem('X', val)`.
- Read sites: `$cookies.getObject('X')` → `JSON.parse(sessionStorage.getItem('X'))`; `$cookies.get('X')` → `sessionStorage.getItem('X')`.
- Every read site must handle the "not yet set" case the same way it does today (`$cookies.getObject` returns `undefined` for a missing cookie; `sessionStorage.getItem` returns `null` — a subtle difference that existing `== undefined` checks in the read sites, e.g. `labConfig==undefined?0:...`, would need to become `== null` or a loose `==` check, since `null == undefined` is `true` in JS but code should not rely on that implicitly without a comment explaining why).

This alone removes an estimated ~4-8KB+ of cookie payload across ~20 duplicated/redundant cookie keys, with **zero backend risk**, since Java never reads any of them. This is the highest-value, lowest-risk change and should ship first.

**Bundled but logically separate — recommend a second, tiny cleanup commit in the same branch or a follow-up:** delete the dead `rmoVisit`/`cookieObjectReschedule` read sites (5 + 2 files) since they always evaluate to `undefined` today; and investigate whether `specialtyNursingStationCookies`/`specialtyNursingStationCookiesNur` (and the `nursingStationsWardsList` pair) can be consolidated into one key each, since both are written with identical payloads on every load — **flagging this as needing verification first**: confirm the two keys are never intentionally read with different expected values anywhere before deleting one, since a false assumption here would be a silent behavioral change.

---

## 4a. Phase A design correction (2026-07-09) — cross-tab semantics re-investigated

**Trigger:** the owner's real-world operational knowledge — staff genuinely keep multiple browser tabs open simultaneously (a nurse working one patient in tab 1, opening a new tab for a different patient/customer). This session re-investigated Phase A under that constraint, before any code was touched. Nothing in this section has been implemented.

### New-tab behavior finding

Searched exhaustively (all 288 `window.open`/`$window.open` call sites app-wide, plus every `target="_blank"` in every HTML template) for every mechanism that could open a new browser tab, and read the destination controller for each candidate:

- **No purpose-built "open a different patient/encounter in a second tab" feature exists.** The overwhelming majority of `window.open` calls are print windows (`window.open('', '', 'height=500,width=700')` + `document.write`), PDF/flipbook viewers, Razorpay/Mpesa/Pesapal payment-gateway redirects (`shortUrl`/`payLink`), server-generated digital-consent/e-sign links, or hand-offs to **external, different-domain applications** (Easy-EMR, PMS-EMR, BI dashboard, OTIMS, Nursing-Info, Twilio video/item-request) that receive patient/org/unit context via explicit query-string parameters built by the calling controller, not via this app's cookies.
- **Exactly 4 sites in the whole codebase reopen this same Angular SPA's own route in a new tab** (`$window.open('#/state', '_blank')` — `$state.href()` is never used anywhere): the procurement "Add Item GRN" screen's **Item Master** (`controller/procurement/quotation/addNewGRNController.js:3046` → `itemMasterController.js`) and **Vendor Master** (`:3050` → `vendorMasterController.js`) pop-outs, and two **QMS kiosk setup** screens (`controller/qms/QMSMasterTokenCallController.js:106` → `QMSMasterController.js`; `controller/qms/QMSTokenDisplayMasterCallController.js:106` → `QMSTokenDisplayMasterController.js`).
- **Of those 4, 3 read `$cookies.getObject('cookieObject')`/`cookieObjectAppConfig*` directly in the destination controller's constructor, with no independent config-fetch call:** `itemMasterController.js:14-22`, `vendorMasterController.js:19`, and `QMSMasterController.js:20-33` all do `var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return; } ...` and proceed straight to using its fields — no call to `getLabConfiguration()`/`getCounterListByUser()`/etc. The 4th (`QMSTokenDisplayMasterController.js`) is architected differently on purpose: org/unit/kiosk/user IDs travel as `$stateParams` in the opened URL itself, and it swaps in `GenericServiceForWithoutLogin` specifically to avoid depending on cookie/session state — the one place in the app that already does new-tab context-passing "correctly" without relying on shared browser storage.
- A broader "open module in new tab" affordance was clearly experimented with — `views/common/dashboard-quick-links.html` (18,911 lines) contains 229 matches for `target="_blank"`, but a spot check shows 226 of them are inside HTML **comments** (`<!-- href="#/kiosk" target="_blank" -->`); only 3 are live, and those 3 point to external BI/OTIMS/video-conf links, not internal module state. This capability appears to have been built and then disabled, not actively in use today.

**Conclusion:** wherever this SPA *is* reopened in a genuine new tab today, it works by reading the pre-existing `cookieObject`/`cookieObjectAppConfig*` **cookie** directly, with zero independent refetch — confirmed concretely at 3 of the 4 real sites. The multi-tab "different patient per tab" workflow the owner described is not one of these 4 (none targets a patient/encounter/appointment) — it is almost certainly a **manual browser action**: staff open a second tab themselves (bookmark, browser history, retyping the URL) and the SPA bootstraps fresh and independently in that tab. Either way — JS-opened or manually-opened — the second tab is not a fresh top-level browsing context with `sessionStorage` inherited from tab 1 (manually-opened tabs never inherit `sessionStorage`; even the 4 JS-opened ones don't rely on it today). It **does** inherit cookies and `localStorage` instantly, because both are shared browser/origin-wide regardless of how or when the tab was opened.

**Does a new tab independently re-fetch config, or rely on the existing cookie?** Confirmed by grepping every call site of each config-fetching function app-wide:
- `getLabConfiguration()`, `getCounterListByUser()`, `getTemplateConfig()`, `getMpesaRoles()`, `getRmoRoster()` are called **exclusively from within `controller/login/loginController.js`** (lines 473-475, 1203-1205 for the two examples checked) — there is no other call site anywhere in the codebase. A new tab never re-runs these.
- The nursing-station/store/counter cluster is a partial exception: it's re-fetched **lazily on first dashboard-controller load if the cached value is missing** (`nursingStationsWardsList == undefined` gate at `controller/nurseDashboardMasterController.js:114-152`), and shows a picker modal if the staff member has more than one nursing station/store mapped. The same "prompt once per dashboard load if uncached" pattern drives `selectedTopStore`/`counterMstObj` via `$rootScope.storePopupAcesss()` → `storePopup()` (`config/app.js:~2597-2650`), called at the top of `nurseDashboardMasterController.js:58`, `doctorDashboardController.js:57`, `CEOdashbordController.js:37`, `financerDashboardController.js:52`, and `bedManagementController.js:180`.

Either way: **when a cookie value already exists, a manually-opened second tab never independently re-fetches — it reads the existing cookie and moves on.** That is exactly why multi-tab use "just works" today: cookies are shared across all tabs of the origin, with or without any opener relationship. Moving any of these values to `sessionStorage` would silently break that inheritance in the second tab: login-only values (`labConfig`, `counterList`, etc.) would come back permanently `null`/`undefined`; the nursing-station/store/counter cluster would instead re-trigger its picker modal in tab 2, risking the user selecting a **different** store/counter/nursing-station than tab 1 — a real correctness risk given `isSkipPharmacySettlement` and other billing-adjacent flags depend on this context being consistent (CLAUDE.md mission #1).

### Corrected two-group categorization (replaces the single 22/29-key Phase A list above)

Every key below was re-verified against its actual write/read code, not assumed from its name.

**GROUP 1 — sessionStorage (tab-isolated is correct: each represents a specific in-progress record this tab is working on, written when the user opens that record and (mostly) explicitly cleared when done):**

| Key | Verified evidence |
|---|---|
| `cookieEncounterObject` | `config/app.js:2527-2554` builds a single encounter's fields (patientName, uhIdNumber, encounterId, doctorId, etc.); read/`$cookies.remove`'d in lockstep across the OPD tab controllers for that one encounter (`examinationTabController.js:230,629,681`, `medicalHistoryTabController.js`, `vitalsTabController.js`, `adviceTabController.js`, `follwUpTabController.js`, `drugHistoryTabController.js`, `notesTabController.js`, `complaintsTabController.js`, `socialHistoryTabController.js`, `allergiesTabController.js`, all with matching `$cookies.remove("cookieEncounterObject")` on completion). Genuinely one encounter, one tab, cleared after use. |
| `tempAppointmentObjet` | `controller/pesapalSuccessPageController.js:90-97` reads the existing value and merges in *this specific* payment transaction's result fields (`resTransactionId`, `pesaPalAmount`, `transactionType`) before writing it back — a single appointment/payment transaction, not standing context. |
| `cookieDietObject` | `controller/dietAndKitchen/global/dietOrderPatientListController.js:602-606` (`goToDietPlanning`) writes the single diet-order/patient row `info` being navigated to, also passed as a `$state.go` param in parallel — clearly one record. |
| `bedManagmentCookies` | `controller/adt/transaction/bedManagement/bedManagementController.js:808-838` writes one bed/admission row (`admissionId`, `patientId`, `bedId`, `wardId`, etc.) — and in the **same code block**, an adjacent value (`admissionNoteObjER`) is *already* written via `sessionStorage.setItem(...)`, i.e. the existing code already treats this class of per-record data as sessionStorage-appropriate. |
| `cookieMorgueBedObject` | `controller/adt/transaction/mortuary/morguePendinglistController.js:109-115` (`bedAllocation`) writes one specific patient/mortuary-request row (`mortuaryRequestId`, `patientId`, etc.). |
| `corporateStatmentObject` | `controller/billing/transaction/corporateStatementMasterController.js:221-231` (`NavigateToDetail`) writes one specific invoice row (`companyInvoiceId`, `invoiceNumber`, `companyName`). |

**GROUP 2 — localStorage, explicitly cleared on logout (shared-across-tabs is correct: each represents standing "who is this staff member and what is their currently active operating context" state, set once at login or lazily on first dashboard load, and consumed identically by every tab regardless of which record that tab happens to be looking at):**

`mpesaRigths`, `labConfig`, `templateConfig`, `counterList`, `rmoMappedOPDoctorList`, `rmoMappedIPWard`, `macAddress`, `cookieObjectAppConfigSecond` — all confirmed standing per-user/per-session config with no per-record meaning, written once at login, and safe to share across tabs because no role is expected to operate under two different values of these simultaneously.

**(2026-07-09, final correction — see resolution of open question 11 below) The store/counter/nursing-station cluster moved from Group 2 back to Group 1 (sessionStorage):** `selectedTopStore`, `selectedCssdStore`, `counterMstObj`, `nursingStationId`, `nursingStationDesc`, `mappedNurseBedList`, `specialtyNursingStationCookies`, `specialtyNursingStationCookiesNur`, `nursingStationsWardsList`, `nursingStationsWardsListforEr`, `isUserCounterApplicable`, `isCounterApplicable`. These were provisionally placed in Group 2 earlier in this same session (see the struck-through history below), pending explicit owner confirmation flagged in open question 11. The owner has now confirmed the underlying assumption was wrong: a single staff member routinely does need two different stores/nursing-stations active in two simultaneous tabs (e.g. covering two wards, or one tab per store). Under that real usage pattern, Group 2's cross-tab sharing is not a convenience — it is the exact silent-overwrite corruption this whole investigation exists to prevent: tab 1 quietly viewing Ward A gets its `nursingStationId`/`selectedTopStore` overwritten the instant tab 2 selects Ward B, because `localStorage` is shared origin-wide. sessionStorage's tab isolation is what actually matches this cluster's real semantics — each tab keeps its own selected store/counter/nursing-station, exactly like Group 1's other per-record values. **`isUserCounterApplicable`/`isCounterApplicable` are included in this move** even though they're boolean gating flags rather than the store/counter object itself, because they gate `counterList`/`counterMstObj` selection for the same tab and must stay consistent with whichever store/counter that specific tab is showing.

~~Provisional Group 2 placement (superseded by the above): `selectedTopStore`, `selectedCssdStore`, `counterMstObj`, `nursingStationId`, `nursingStationDesc`, `mappedNurseBedList`, `isUserCounterApplicable`, `isCounterApplicable`, `specialtyNursingStationCookies`, `specialtyNursingStationCookiesNur`, `nursingStationsWardsList`, `nursingStationsWardsListforEr` — lazy-fetch-and-cache-with-picker-modal pattern, per the mechanism described above. Left here for audit history only; do not implement from this line.~~

**Addition not in the owner's list — flagging rather than silently dropping:** `isSkipPharmacySettlement` is part of the original Phase A scope (`controller/login/loginController.js:625-633`, a login-time unit-level billing config flag feeding `walkInSalesController.js:4283` and `outPatientSalesWorklistDetailsController.js:7672,8574`) but was not included in either group in the owner's message — it fits **Group 2** cleanly (standing per-unit config, write-once-at-login, no per-record semantics), same as `isUserCounterApplicable`/`isCounterApplicable`. Recommend adding it to Group 2 rather than leaving it unmigrated.

**Two reclassifications out of the owner's proposed Group 2 — flagged per the instruction to not force a fit:**

1. **`selectedPageName` / `selectedPageNameForAdmissionNote`** — verified usage shows these are **one-shot, same-tab navigation relay signals**, not standing user context: `config/app.js:183-186` (`getMasterPage`) writes the value and *immediately* calls `$state.go('commonMaster', {}, {reload:true})`; the very next controller instantiation reads it once at the top (`controller/master/commonMasterController.js:16-17`) to decide what to render, same pattern for `getAdmissionNote()` (`config/app.js:619-622`) → `admissionRequestController.js:220-222,479`. This is a write-then-immediately-navigate-then-read-once handoff *within a single tab's own navigation*, not something a user carries as standing context across tabs. If put in `localStorage` (shared), two tabs navigating to different "common master" sub-pages at nearly the same moment could race and momentarily render the wrong page/label in one tab. **Recommend Group 1 (sessionStorage) instead of the proposed Group 2.**
2. **`webAppOrgUnitCookieObj`** — verified usage (`controller/opd/transection/webAppController.js:18-34`, `bookAppointmentController.js:22-25`, `webAppointmentBookingConfirmationController.js:24`) shows this is written/read **exclusively inside the anonymous public self-service appointment-booking widget** (`$scope.userId = 1` is hardcoded — there is no logged-in staff session in this flow at all), purely as a same-flow navigation-recovery fallback if `$stateParams.webAppDetailObj` is empty. It does not represent "which staff member, what's their assigned context" (Group 2's rationale) — there is no staff member in this flow. Nor is it exactly a patient/case record (Group 1's literal rationale), but tab-isolation is the safe default here since nothing in this anonymous booking flow suggests it needs to be shared across a visitor's tabs. **Recommend Group 1 (sessionStorage) instead of the proposed Group 2**, and flagging this as not cleanly fitting either category's stated rationale.

**Caveat on the store/counter/nursing-station cluster, RESOLVED 2026-07-09 — see open question 11:** all of these use the identical "cache-or-prompt" pattern (§ above). Shared `localStorage` would only have been correct if a single staff member was never expected to operate two different stores/counters/nursing-stations at once across two tabs. The owner has confirmed that assumption is false — multi-tab-per-user with different operating contexts per tab is a real, routine usage pattern. This cluster is therefore in **Group 1 (sessionStorage)**, not Group 2, per the correction above.

### Phase B (needs its own design pass — touches Java, larger risk)

**`cookieObject` and `cookieObjectAppConfig` cannot simply move to sessionStorage** — `SessionHandlerInterceptor` reads them directly off the raw `Cookie` header for essentially all `/api/**` traffic, and 15 more controllers do their own redundant reads. Recommended approach (Expand-Migrate-Contract, per `RULES.md`):

1. Design a new, minimal cookie (or a single combined slim cookie) carrying **only** the fields Java actually needs: from `cookieObject` — `organizationId`, `unitId`, `userId`, `counterId`, `storeId`; from `cookieObjectAppConfig` — `selfPayeeId`, `defaultSelfTariffId`, `groupPracticeNo`, `groupPracticeName`, `startDateFormat`, `practiceNo`, `isPreOrPostBilling`. That's ~12 fields total, versus the ~120 combined fields in the two full objects today — a large size reduction even without removing the cookie mechanism entirely.
2. Move the remaining ~108 fields (everything only read client-side, per the field-frequency tables in §2a) to sessionStorage under the existing key names, so the ~1,175+ client-only read sites keep working unchanged (they'd read from sessionStorage instead of the cookie, same key, same shape).
3. Before implementing, **must** resolve: what `GetStoreListUsingsessionService.java:46-52` actually needs from `cookieObject` when it "mutates and re-emits" it — this was flagged, not resolved, in this investigation; a dedicated read of that file is required before deciding whether the slim cookie's field set is sufficient for it too, or whether it needs another field added.
4. This phase requires a `SessionHandlerInterceptor.java` change plus updates to the 15 redundant controllers — real backend risk, since this interceptor's populated `SessionService` values feed 343 files. This needs its own Maker/Checker cycle, shadow testing per `RULES.md` item 2, and should not be bundled with Phase A.
5. Given `isSkipPharmacySettlement` (already in Phase A) directly gates a billing branch, and `cookieObjectAppConfig`'s `isPreOrPostBilling`/`defaultSelfTariffId`/`selfPayeeId` are billing-critical fields staying in the slim cookie in Phase B, this whole phase touches CLAUDE.md's #1 mission priority and should get extra scrutiny (a full shadow-test pass, not just a compile check) before any cutover.

### Not recommended: moving `authToken` handling

No code change is needed for `authToken` itself — `JwtTokenFilter` already reads it exclusively from the `Authorization: Bearer` header, never from a cookie, so the JWT's presence inside `cookieObject` is redundant with the header-based mechanism already in place (`GenericService.js` builds `Bearer <token>` from `cookieObject.authToken` at 18 call sites). Once `cookieObject` is restructured in Phase B, `authToken` should simply not be included in the new slim cookie or in the sessionStorage-migrated remainder unless a specific client-only read site needs it beyond the 18 header-building sites (worth double-checking that all 18 are truly interchangeable with each other, since GenericService.js was the file that had the prior `ssoToken`/`authToken` dead-code bug — see `docs/SESSION_LOG.md` 2026-07-08/07-09 entries).

---

## 5. Open questions / risks flagged for owner (not guessed at)

1. **`GetStoreListUsingsessionService.java:46-52`** — reads and re-emits `cookieObject` independently of `SessionHandlerInterceptor`. Its exact field dependency is not yet traced. Must be read in full before Phase B's slim-cookie field list is finalized.
2. **`SessionService` singleton-scope concern** — flagged by the Java-side investigation as a pre-existing thread-safety risk unrelated to this migration. Recommend logging as its own finding/manifest entry in a future session, not folding it into this fix.
3. **Nursing-station cookie duplication** (`specialtyNursingStationCookies` vs `...Nur`, `nursingStationsWardsList` vs `...forEr`) — appears to be pure duplication but not confirmed; recommend a targeted read of all ~20 controller call sites before consolidating, to rule out any place where the two keys are expected to diverge.
4. **`sessionStorage.getItem` returning `null` vs `$cookies.getObject` returning `undefined`** — every migrated read site's "not set" handling needs individual verification, not a blanket find-replace, since some use `== undefined`, some use falsy checks, and a few (per `labConfig`) use an explicit ternary.
5. **Dead cookies (`rmoVisit`, `cookieObjectReschedule`)** — pre-existing bugs, unrelated to header size, but touched by the same "move off cookies" investigation. Recommend flagging as a separate tiny cleanup, owner's call on whether to bundle with Phase A or ticket separately.
6. **Cross-origin cookie credentials** (`CORSFilter`'s `Access-Control-Allow-Credentials: true`) — noted for awareness; no action identified as needed, but should be re-checked when Phase B's slim replacement cookie is designed.
7. **Glossary check**: none of the 34 cookie keys appear to be business terms requiring a `docs/GLOSSARY.json` entry (they're technical session-state identifiers, not billing/business rules) — flagging this determination for the owner to confirm rather than assuming it silently, per `CLAUDE.md`'s glossary rule.
8. **(Added 2026-07-09) `selectedPageName` / `selectedPageNameForAdmissionNote` reclassified from Group 2 to Group 1** — owner's message proposed Group 2 (localStorage) for these; verified usage shows they're a one-shot same-tab navigation relay, not standing context. See §4a for the evidence. Flagging for owner confirmation since it reverses part of the proposed categorization.
9. **(Added 2026-07-09) `webAppOrgUnitCookieObj` reclassified from Group 2 to Group 1** — same reasoning: verified usage is inside an anonymous public booking widget with no staff session, not "which staff member" context. See §4a.
10. **(Added 2026-07-09) `isSkipPharmacySettlement` was missing from the owner's 28-key Group 1/Group 2 list** — it's part of the original Phase A scope and billing-relevant; recommend adding it to Group 2 (see §4a) rather than leaving it unmigrated by omission.
11. **RESOLVED 2026-07-09 — Store/counter/nursing-station cluster cross-tab assumption.** Group 2's correctness for `selectedTopStore`/`selectedCssdStore`/`counterMstObj`/`nursingStationId`/`nursingStationDesc`/`mappedNurseBedList`/`isUserCounterApplicable`/`isCounterApplicable`/the specialty-nursing-station and wards-list keys depended on no single staff member ever needing two different stores/counters/nursing-stations active in two tabs at once. **Owner's answer: yes, this happens routinely** (e.g. one nurse covering two different wards in two tabs). Given that, Group 2's shared-`localStorage` semantics would cause exactly the silent-overwrite corruption this whole investigation was meant to prevent (tab 1 on Ward A silently corrupted the moment tab 2 selects Ward B). **Final decision: this cluster moves back to Group 1 (sessionStorage)** — see §4a's corrected Group 1/Group 2 lists. This reverses the provisional Group 2 placement made earlier in the same 2026-07-09 session, which was itself already a correction of the original 2026-07-09 investigation's default (see the plan's revision history in §4/§4a) — three passes on the same question, each driven by a real piece of new information (multi-tab usage exists → is it per-cluster shared or per-tab? → owner confirms per-tab), not by guessing.
12. **(Added 2026-07-09) Disabled "open module in new tab" feature found in `views/common/dashboard-quick-links.html`** — 226 of 229 `target="_blank"` occurrences in that file are inside HTML comments (dead, not live markup). Unrelated to the current bug, but worth a separate note to the owner in case this was intentionally disabled for a reason relevant to future multi-tab design work.

---

## 6. Next steps (pending owner sign-off)

1. Owner reviews and approves (or redirects) this plan, **including the §4a correction to the Phase A categorization** (this supersedes the flat 22/29-key sessionStorage list originally in §4).
2. Phase A implemented on a dedicated branch (e.g. `fix/cookie-bloat-phase-a-frontend`), using the **two-group split in §4a** (Group 1 → sessionStorage, Group 2 → localStorage with an explicit clear-on-logout step), with a `BR-PENDING-cookie-bloat-phase-a` manifest entry (Behavioral Change — storage location changes, even though client-visible behavior should be identical for single-tab use; multi-tab behavior is the entire point of the correction) before merge, Maker/Checker review per `RULES.md` item 6.
3. Owner confirmation requested on open questions 8-11 above before implementation starts, since they change or flag parts of the owner's proposed categorization rather than silently applying it.
4. Phase B requires its own follow-up investigation session (resolving open question 1) before a design/implementation branch is started — not to be started until Phase A has shipped and been observed stable, since Phase B carries the real backend risk.

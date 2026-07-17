# Phase 2 Pilot — Audit Step (5 screens)

*Read-only characterization. No application code touched. No rebuild work started. Golden-trace capture done only for `homecareMedicationApproval` (explicitly requested); not yet run for the other 4 — holding per instruction until this audit is reviewed.*

This covers step 1 of the pilot process (audit current behavior: fields, validation, DOM states, API calls, cross-screen effects) for the 5 approved pilot screens. Every field/validation/API claim below was verified by reading the actual view and controller source, not inferred from the Phase 0 static-analysis table — and in three places the static table's numbers turned out to be wrong or misleading in ways worth knowing before this becomes the pattern for every subsequent screen.

---

## Cross-cutting finding: static form-field counts overcount by ~30-40%

Checked all 5 view files with comments stripped vs. not:

| Screen | Phase 0 static count | Actual rendered field count |
|---|---:|---:|
| MISReports | 33 | 22 (but see note below — this screen's fields are dynamic, see its section) |
| insuranceMaster | 23 | 18 |
| inputOutputCharts | 36 | 22 |
| organizationMaster | 31 | 22 |
| homecareMedicationApproval | 12 | 9 |

The gap is entirely commented-out `<input>`/`<select>` elements left in the HTML (old fields disabled during development, never deleted). Phase 0's regex-based counter doesn't strip HTML comments, so it counts dead markup as live fields. This confirms the caveat already written into `docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md` — worth quantifying here (~30-40% overcount on this sample) rather than leaving it as an untested guess. Field counts below are the verified, comment-stripped numbers.

---

## 1. MISReports (Other Apps, low-risk pick)

**File paths:** view `views/OtherApps/MIS-reports.html` (642 lines), controller `resources/assets/js/controller/master/MISReportsController.js` (1288 lines, dedicated).

**Important correction to how this screen should be understood:** this is **not a fixed-schema form**. It's a dynamic report-parameter renderer. The left panel is a two-level tab tree of report groups → reports (populated from `getReportGroupHeaderMaster` / `getReportDetailsList`); selecting a report calls `getReportDetailsByReportId`, which returns a list of parameter definitions, each with a `controlName` (`DatePickerFrom`, `DatePickerTo`, `TimePickerFrom`, `TimePickerTo`, `text` [patient search], `ItemSearch` [medication typeahead], `Servicesearch`, `DiagnosiSearch`, `VenderSearch`, `DatePickerExpiry`, `radio`, `checkbox`, `textBox`, `textBoxstring`, `inputBox`, `multiSelectlist`, `list`, `unit`, `user`, `org`). The view uses `ng-if` on `info.controlName` to render one of ~19 different field types per parameter, driven entirely by backend metadata. **"Rebuild for exact parity" here means reproducing this dynamic-rendering engine and all 19 control-type branches, not porting a static list of 22 fields.** This is architecturally the most complex of the 5 pilot screens despite its "low-risk" tag, and worth flagging now so Phase 2's rebuild estimate accounts for it.

**Fixed (non-dynamic) UI elements:** report search typeahead (`searchReportHeader`), report-group/report tab tree, "View" button (`getPreview()`), "Export" button (`getExcel()`), format `<select>` (`Obj.id`).

**Validation:** minimal and mostly implicit — date-range fields self-correct rather than block submission (`validateToDate()`: if to-date < from-date, silently resets to-date to "now" and shows a warning growl, does not prevent proceeding). `validateAge()` warns if age >120 or <0 but doesn't block. No required-field enforcement before `getPreview()`/`getExcel()` — if no report is selected, `$scope.getparameterbyReportId1` is undefined and the loop is a no-op (silently produces an empty `reportParams` array sent to the report service, not an error).

**API calls:** `getReportGroupHeaderMaster` (POST, on load), `getMISReportFormattesMasterforShowReports` (POST, on load), `searchMISReport` (POST, typeahead), `getReportDetailsList` (POST, per group selection), `getReportDetailsByReportId` (POST, per report selection), `getPatientByKeyword`, `getMedicationDetails`, `diagnosisAutoFillSearch`, `vendor/search`, `orderSet/services/search` (typeaheads, conditional on which control types the selected report happens to use), and finally `reportService.generate()` against `REPORT_API_URI_FOR_MIS` (PDF) or a binary-blob download endpoint (non-PDF formats) on View/Export.

**DOM states:** tab-active highlighting (`isSetFirstTabLevel`) on both the group-header row and the nested report row; per-parameter conditional rendering as described above; no visible loading spinner beyond the global `$rootScope.startLoader()/stopLoader()`.

**Cross-screen effects:** none found — no `$broadcast`, no `$on` beyond the framework defaults, no `$rootScope` custom writes beyond the four generic tab-level functions (`$rootScope.firstTabLevel` etc. — these are the *same* generic tab mechanism `MasterController` also defines; a screen using both would collide on that state, worth knowing but not applicable here since MISReports has its own dedicated controller). Reads `$scope.checkLabConfig()` — confirmed this resolves via prototypal inheritance to `$rootScope.checkLabConfig` defined once in `app.js`'s `.run()` block (see cross-cutting note below) — **not a bug**, despite looking like an undefined-function risk at first glance.

**No discrepancies/bugs found in this screen's own code.**

---

## 2. insuranceMaster (Billing, billing-only pick)

**File paths:** view `views/master/UnitMaster/billingMaster/insurance-master.html` (603 lines), controller `resources/assets/js/controller/master/unit/insuranceMasterController.js` (1060 lines, dedicated).

**Real fields (18):** `insuranceCode`*, `insuranceDesc`*, `countryId`* (select, cascades to state), `stateId`* (select, cascades to district), `districtId`* (select, cascades to city), `cityId`* (select, cascades to area), `areaId`* (select, auto-fills `zipCode`), `zipCode`*, `contactNo` (numeric, max length bound to `$rootScope.registrationFieldObj.mobileDigit`), `faxNo`, `email`*, `contactPerson`, `generalLedgerName` (typeahead), `blockReportPrint` (checkbox), document upload (multi-file). `*` = HTML `required` attribute present. Plus 2 list-level controls (pagination page-size, search box) not part of the record form itself.

**Validation — two independent, inconsistent validation paths, worth knowing before rebuild:**
- HTML-level `required` + `show-errors`/`$scope.$broadcast('show-errors-check-validity')` + `insuranceMasterForm.$valid` — covers code/desc/country/state/district/city/area/zip/email.
- A *separate*, earlier, manual JS validation block in both `save()` and `update()` (lines ~373-515 and ~730-873) that re-checks the same fields one-by-one with `growl.warning(...)` messages **before** the HTML form-level check runs, in a different order, with `isNaN()` checks on the geography IDs that the HTML validation doesn't have. Both paths exist redundantly for the same fields — this looks like accretion over time (one added without removing the other) rather than a deliberate two-tier design. Worth deciding during rebuild whether to preserve both layers exactly (true parity) or collapse to one — flagging rather than assuming either answer.
- Email regex, fax regex (`/^\+?[0-9]{6,}$/`), and file-extension allowlist (`doc`, `pdf`, `docx`, `txt`) are only enforced in `save()`/`update()`'s JS path, not reflected as HTML5 pattern attributes.
- Mobile-number length check compares against `$rootScope.registrationFieldObj.mobileDigit` — a global, org-configurable digit count set elsewhere (see cross-cutting note).

**API calls:** `getActiveCountryList`, `getActiveStateListByCountryId`, `getActiveDistrictListById`, `getActiveCityListById`, `getAreaListById` (cascading geography, each gated on parent being non-empty), `getInsuranceMasterList` + `getInsuranceMasterCount` (paginated list, fired together on load and after every save/update), `getGeneralLedgerMasterActiveList` (both on load and as the ledger-name typeahead's live search), `saveInsuranceMaster` / `updateInsuranceMaster`, `insurance/image/{orgId}/{unitId}/{insuranceId}` (separate file-upload POST, only fired if a document was attached, chained after the main save/update succeeds — two round trips, not one atomic call), `getInsuranceMasterById` (edit/pencil click), `updateInsuranceMasterStatus` (active/inactive toggle switch), `getMappedGradeToInsuerance` (grade-details modal, wired in controller but the corresponding "Grade Details" icon is commented out in the view — dead UI hook, not reachable by a user today).

**DOM states:** save vs. update button visibility toggled by `saveBtnFlag`/`updateBtnFlag`; address-details modal (`getAdressDetails`) and grade-details modal (present in controller, unreachable in view per above); document viewer opens via `fancybox` iframe, not an Angular modal.

**Cross-screen effects:** none via `$broadcast`/`$on`. Reads the same global `$rootScope.registrationFieldObj` set elsewhere in the app (populated by `$rootScope.getDefaultRegistrationValidationDetails()`, called once at controller init) — a shared-config dependency, not screen-to-screen, but still something the rebuild needs a source for.

**No bugs found — this is the cleanest of the 5 controllers.** (`$state` is correctly injected and used for the login-redirect guard, unlike organizationMaster below.)

---

## 3. inputOutputCharts (Nursing, clinical-only pick)

**File paths:** view `views/Nursing/input-output-charts.html` (629 lines), controller `resources/assets/js/controller/master/unit/inputOutputChartsController.js` (1310 lines, dedicated).

**Real fields (22):** patient search (via two included shared partials, see below) + intake date/time (`intakeDate`/`intakeTime`, disabled once I/O tracking has started) + a repeatable intake-row table (date, time, shift select, intake-fluid select, intake-type-fluid select [cascades from fluid], quantity, add/remove row) + output date/time + a repeatable output-row table (date, time, shift select, output-type select, quantity, add/remove row) + Save/Print buttons. Field count varies with however many intake/output rows a nurse has added — the static count only reflects the single default row.

**This screen depends on two shared, `ng-include`d partial views** it does not own: `views/common/common-patient-search-opd-without-counter.html` and `views/common/patient-demography-opd.html`. These render the patient-search box and the demographic header bar; whatever directives/scope bindings they expose (patient selection presumably drives `getPatientDetailsByIndex`) are **out of scope for this audit** — they're shared across many clinical screens, not specific to this one, and should be characterized once, separately, rather than re-audited per screen. Flagging so Phase 2 doesn't either duplicate that work 5x or silently skip it.

**Validation:** time-of-day consistency checks (`validateTime`, `validateOutputTime`, `getListShift`, `getListShiftOutput`) — intake time can't be in the future if the date is today, can't be earlier than the patient's admission time on the admission date, output time can't be earlier than intake time on the same date; violations show a `growl.warning` and silently *reset the value* rather than blocking the row (same "warn and auto-correct" pattern as MISReports' date validation, not "reject and require re-entry"). Save (`save()`) blocks only on intake date/time being present — output date/time, and every row's fluid-type selections, are **not required**: a chart with zero intake/output rows but a valid date/time still saves (guarded only by "at least one of `intakeOutputChartMasterDtoList`/`outputChartMasterDtoList` non-empty" after filtering rows that have a selected fluid type).

**A real dependency worth flagging explicitly, found here and not detectable from Phase 0's static `$broadcast`/`$on` scan:** line 43, `[cookieObject, cookieObjectAppConfig] = $scope.checkCookies();`, then separately, line 75, `var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));` — read directly from the browser's native `sessionStorage`, not through `$rootScope`/`$broadcast`. This value is written by `resources/assets/js/controller/sidebarController.js` (the app's persistent navigation shell, always loaded) and read by **30+ other nursing/pharmacy controllers** app-wide, not just this one. It populates `patientSearchObj.wardList`, which presumably scopes patient search to the wards mapped to whichever nursing station the user has selected in the sidebar. **This is a real cross-screen coupling that Phase 0's `$broadcast`/`$on`-only detection missed entirely** — it's sidebar→sessionStorage→content-screen, not screen→screen. If the rebuild doesn't replicate this (e.g. via a shared Angular service/store fed by whatever replaces the sidebar), ward-scoped patient search will silently return unfiltered or empty results instead of erroring — the kind of regression that's easy to miss because nothing crashes.

**API calls:** `getIntakeFluidMasterList`, `getIntakeFluidTypeMasterListById` (cascading per intake row), `getOutputFluidMasterList`, `getIntakeOutputChartListByPatientId` (loads existing chart + derives start/stop button state from whether any rows exist), `searchListOfPatientsByWardId`, `getShiftlistByTime` (per-row, on time change, for both intake and output), `saveIntakeOutputChart`, `updateStatusForStartIOChart` / `updateInputOutputStartStatus` (Start/Stop I/O), `getPatientAllergyListForIcon`, `getPatientDiagnosisByPatientIdForIcon` (allergy/diagnosis icon flags, fired on patient selection), `reportService.generate()` for Print.

**DOM states:** "Start I/O" button shown/hidden by `startIOFlag`; once started, date/time/quantity inputs across both tables are `ng-disabled="startFlag"` (locked until a chart exists) except the very first date/time pair; "Stop I/O" gated behind a confirm modal that itself is gated on output date/time being filled in first (`alertStopIO()`); Save/Print disabled via `stopAndSaveDisabled` until the list load confirms rows exist.

**No bugs found in this controller's own logic** — the sessionStorage dependency above is a real *architectural* risk to carry into the rebuild, not a bug in the current app.

---

## 4. organizationMaster (Admin - Global Master, low-risk + cross-screen-coupling pick)

**File paths:** view `views/master/GlobalMaster/organizationMaster.html` (667 lines), controller `resources/assets/js/controller/master/organizationController.js` (991 lines, dedicated).

**Real fields (22):** `organizationCode`*, `organizationName`*, `registrationNumber`*, `organizationAddress`*, `countryId`* (cascades state→district→city→area), `stateId`*, `districtId`*, `cityId`*, `areaId`* (auto-fills `postCode`), `postCode`*, `organizationContact`*, `faxNumber`, `organizationEmailId`*, `appUrl` (subdomain), `unitTimeZone`* (select), `isDuoAuthenticationRequired` (checkbox), `isSyncScheduling` (checkbox, reveals a conditional `channelId` text input when checked — the one genuinely dynamic field on this screen), logo file upload, a repeatable license-details sub-table (type/number/expiry, add/remove rows — **this entire block is commented out in the view**, so despite the controller maintaining full `listLicenceDetailsMaster` CRUD logic for it, a user cannot reach it today), "Module Access" button opening a permissions tree modal.

**THE MUST-PRESERVE COUPLING THIS SCREEN WAS PICKED FOR — documented explicitly, not as a side note, per instruction:**

`organizationController.js` line 33-35:
```js
$scope.$on("fileProgress", function(e, progress) {
  $scope.progress = progress.loaded / progress.total;
});
```
This listens for a `fileProgress` event that is `$broadcast` by a **shared factory**, `resources/assets/js/common/imageUploadFactory.js` (line 20: `scope.$broadcast("fileProgress", {...})`), during the logo-upload flow. The exact same listener, verbatim, also exists in `hospitalMasterController.js` — confirmed by grep, these are the only two consumers app-wide. **The rebuild must preserve this specific coupling**: whatever component/service handles file upload in the new Angular version needs to expose upload-progress in a way `organizationMaster`'s (and `hospitalMaster`'s) rebuilt equivalents can consume, or the progress indicator this listener drives will silently do nothing — not crash, just stop updating, which is exactly the kind of quiet regression that's easy to miss in review. Recommend the golden-trace capture for this screen specifically include a logo-upload interaction so the trace captures the `fileProgress` events firing, not just the page-load trace.

**A real bug, found and flagged rather than silently fixed or worked around:**

Lines 81-85:
```js
var cookieObject = $cookies.getObject('cookieObject');
if(cookieObject == undefined){
    $state.go('login');
    return;
}
```
**`$state` is never injected into this controller.** The dependency-injection list (lines 11-24) has `$scope, $http, $localStorage, $sessionStorage, $cookies, $rootScope, fileReader, GenericService, GenericServiceFileUpload, PagerService, promiseFactory, growl, BillingGenericService, ivhTreeviewOptions, ivhTreeviewMgr, ivhTreeviewBfs, CONSTANTS` — no `$state`, and the function signature matches exactly, so there's no injected or closure-scoped `$state` available. The file is `'use strict'` (line 1), so referencing an undeclared `$state` doesn't silently fail — it throws `ReferenceError: $state is not defined`.

**Practical effect:** if a user reaches this screen with an expired/missing session cookie (the exact scenario this guard exists to handle), instead of a clean redirect to `/login`, the controller throws an uncaught exception at initialization and the screen fails to render — a broken/blank page instead of a redirect. I compared against `insuranceMasterController.js`, which has the identical guard pattern but *does* correctly inject `$state` (line 22) — confirming this is an isolated bug in this one file, not a systemic pattern, and that the intended behavior (redirect to login) is unambiguous from the sibling code.

**This needs your decision before Phase 2 rebuilds this screen, not a default choice from me:** "exact behavioral parity" would technically mean reproducing this crash too, which seems like the wrong call for a guard clause whose only job is graceful degradation. My recommendation is to treat this the same way the backend characterization work treats a found bug — log it, fix it as a named, called-out change (not bundled silently into the "parity" rebuild), with the intended/correct behavior being the obvious one (`$state.go('login')` actually works, matching every other screen's identical guard). But I'm flagging it for your call rather than deciding unilaterally, per the standing rule.

**Validation:** all geography + code/name/address/contact/email/timezone fields are HTML `required`; no separate JS-level re-validation block (unlike insuranceMaster) — `saveOrganization()`/`updateOrganization()` rely on `$scope.$broadcast('show-errors-check-validity')` + `organizationform.$valid` alone, plus one manual check in `updateOrganization()` only (not `saveOrganization()`) that mobile number length is exactly 10 — an asymmetry between create and update worth confirming is intentional before parity-porting it as-is.

**API calls:** `getLiceneceTypeMasterList`, `timezones`, `getAllCountryMasterList`, `getStateByCountryId`, `getDistrictListByStateId`, `getCityListByDistrictId`, `getAreaByCityIdPagination` (used both for the area dropdown and, confusingly, reused for a second "load more" pagination pattern via `loadMore1()`/`initMasterList()` that appears to be scaffolding for an infinite-scroll area list that doesn't quite match how the dropdown actually renders — worth a closer look during rebuild rather than assuming it's load-bearing), `allArganizationList` [sic, typo in the actual endpoint name] + `organization/count` (list, paginated), `organization/save` / `organization/update`, `organization/image/{id}` (logo upload, chained after save/update succeeds, same two-round-trip pattern as insuranceMaster's document upload), `organization/status/{id}/{status}` (toggle), `organization/{id}` (edit fetch), `licenceDetailsByOrganization` (wired but its trigger icon is commented out, same dead-hook pattern as insuranceMaster's grade-details), module tree endpoints (`CONSTANTS.GLOBAL.MODULE_API`, `/module/biModules`) and `PRIVILEGE_API` for the Module Access permissions modal.

**DOM states:** save/update button toggle (`saveBtnFlag`/`updateBtnFlag`); `channelId` field conditionally shown via `ng-show="organization.isSyncScheduling"`; license sub-table fully commented out (dead, not user-reachable); Module Access modal has 3 tabs (HIS/BI/OTIMS) but only the HIS tab's `<li>` is uncommented in the view — BI and OTIMS tab headers are commented out even though the controller's `getBiModules()` and the modal body's BI/OTIMS panes still exist, so a user can only ever reach the HIS permissions tree today.

---

## 5. homecareMedicationApproval (Pharmacy, the MasterController + both-risk pick)

**File paths:** view `views/PharmacyInventory/InPatient/homecare-medication-approval.html` (263 lines), controller: shared `resources/assets/js/controller/common-controller.js` (`MasterController`, 463 lines total, used by 353 states).

### The state-based branching question, answered directly, with extra care as requested

**`MasterController` does not branch on `$state.current.name`, the URL, or anything state-identifying, anywhere in its 463 lines.** I read the entire file. It:
- Sets up generic UI scaffolding any screen *might* use: 4 levels of generic tab state (`firstTabLevel`...`forthTabLevel` + their setters/getters), 2 generic datepicker open/close flags, and array scaffolding for 4 specific record types (`educationDetails`, `experienceDetails`, `nextOfKinDetails`, `CMEDetails`) that look like leftovers from a staff-registration screen, now along for the ride on every one of the 353 screens whether they use it or not.
- Registers one `$rootScope.$on('customEvent', ...)` handler: a long `if/else if` chain that maps ~35 known master-screen-name strings (`"Department"`, `"Phlebotomy"`, `"Container"`, `"Sample"`, `"Report Type"`, ... `"Floor"`) to sidebar page-title text updates. This is fired by other screens broadcasting `customEvent` with their own name as the message (matches the `bc:0/on:1 [on:customEvent]` pattern seen against most of the 353 shared-controller rows in the Phase 0 table). **`"homecareMedicationApproval"` does not appear anywhere in this chain** — if this event ever fired while this screen were active, it would simply fall through with no matching branch and no effect. It's a generic mechanism the whole cohort of shared-controller screens has access to, not something specific to this screen.

**In short: there is no `homecareMedicationApproval`-specific branch to document, because none exists.** This corrects Phase 0's own stated hypothesis ("a generic CRUD controller that likely branches internally on the current state/URL") — that hypothesis was reasonable to form from static analysis alone, but it's empirically wrong, confirmed both by reading the source and by the golden trace captured for this exact screen (below). I'd recommend updating that line in `docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md`'s known-gaps section once this is reviewed, since it's now a confirmed fact rather than an open question, and it materially changes how Phase 2+ should think about the other 352 shared-controller screens: the real per-screen behavior lives entirely in each screen's own view template (which of MasterController's generic scope properties it happens to bind to), not in JS branching logic.

### The bigger finding: this screen has no real functionality at all

The entire view is static, hand-authored demo markup:
- The patient list table (`views/PharmacyInventory/InPatient/homecare-medication-approval.html`, lines 72-86) has **no `ng-repeat`** — it's one hardcoded row: `TC890 / John Deo 26/M / Ward No.-1 / BM678905`.
- The "Details" modal's medication table (lines 138-223) is two hardcoded `<tr>`s with literal values (`16/07/2017`, `20/07/2017`, `Homecare`, `Prescribed`) — no `ng-repeat`, no `{{ }}` binding to any scope variable.
- The Accept/Reject radio buttons and the rejection note/reason fields have **no `ng-model` anywhere** — they are connected to nothing. The only behavior on them is jQuery show/hide of the rejection-detail row (lines 249-262 of the view, inline `<script>`), which is purely cosmetic.
- The "Save" button (`data-dismiss="modal"`) just closes the modal — no `ng-click`, no call to any save function, nothing persisted.
- The "Back" button is a plain `<a href="#/bedManagement">`, not `ui-sref` or a `$state.go()` call — it still works (ui-router picks up hash changes generically) but bypasses the app's normal state-transition mechanism.

**This means `homecareMedicationApproval`, despite its title implying a real clinical/billing approval workflow (accepting or rejecting a homecare medication order — exactly the kind of decision this pilot was chosen to stress-test), has never been wired to the backend.** It is a UI mockup/prototype that was scaffolded and never finished — not a working screen with a bug, a working screen that was never built past its static HTML.

**Golden trace, captured as requested, before any rebuild work:** `tools/golden-trace/golden-traces/homecareMedicationApproval.json` (captured 2026-07-17, already present from Phase 1 validation — re-verified here rather than re-run, since nothing about the app changed between then and now). Confirms exactly one HTTP call tagged with this state: `GET views/PharmacyInventory/InPatient/homecare-medication-approval.html` — the template fetch itself, and nothing else. No `GenericService`/`BillingGenericService` data call ever fires for this screen, which is the runtime proof matching what reading the view already showed statically. `MasterController` instantiates correctly with `stateName: "homecareMedicationApproval"` captured accurately by the tracer, and the `customEvent` listener registers as expected (visible in the trace's `listens` array) — mechanically everything works, there's just no real screen behind it.

**This is a decision point for you, not something I'm resolving myself:** "rebuild for exact behavioral parity" of a screen with no real behavior is a strange use of the pilot's hardest slot, since it won't actually exercise MasterController-branching characterization (there's no branching) or medication-approval business logic (there isn't any) the way the pilot selection was reasoning intended. Two honest options, no recommendation implied either way: (a) rebuild it as-is — a static mockup shell — which is trivial and doesn't stress-test much, or (b) swap this pilot slot for a different MasterController + both-risk screen that's actually wired up, so the pilot still gets a real test of the hardest category. I'd lean toward (b) given the pilot's stated purpose was to stress-test the process against real variety, but this is your call.

**No fields, no validation, no real API calls, no cross-screen effects to document beyond what's described above** — there's nothing else in this screen to characterize.

---

## Summary of what needs your decision before this proceeds further

1. **organizationMaster's missing-`$state`-injection bug** (section 4): preserve the crash for true parity, or log as a named fix (recommended) and rebuild with the working behavior?
2. **homecareMedicationApproval turned out to be a non-functional static mockup** (section 5): rebuild the mockup as-is, or swap this pilot slot for a real MasterController+both-risk screen?
3. **insuranceMaster's redundant double validation path** (section 2): preserve both layers exactly, or collapse to one during the rebuild (would be a behavior change, needs sign-off either way, not decided here)?

Everything else above is descriptive audit output, not a decision needed from you — ready to inform golden-trace capture and rebuild once you've reviewed.

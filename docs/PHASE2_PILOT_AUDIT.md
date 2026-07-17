# Phase 2 Pilot — Audit Step (5 screens)

*Read-only characterization. No application code touched. No rebuild work started. Golden-trace capture completed for all 5 final pilot screens (`MISReports`, `insuranceMaster`, `inputOutputCharts`, `organizationMaster`, `addItemPOCreation`) — see section 7 below for what that capture found, including two more real findings and a tooling bug in `tools/golden-trace/capture.js` itself, fixed and documented rather than routed around.*

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

**Resolved 2026-07-17 — owner approved fixing it.** Fixed on a dedicated branch (`fix/organizationmaster-state-injection`, not merged, held for review, per Maker-Checker): added `'$state'` to the injection array and function parameters, matching `insuranceMaster`'s working pattern. Verified empirically with `tools/golden-trace/test_orgmaster_bug.js` (uses Angular's `$controller` service with a `$cookies` locals override to instantiate the controller directly, bypassing page navigation) — before the fix: throws `$state is not defined`; after: no throw, correctly calls `$state.go('login')`.

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

**Resolved 2026-07-17 — owner chose to swap this pilot slot.** See the new section 6 below for what the swap search turned up (it's bigger than a simple substitution) and the replacement proposal.

**No fields, no validation, no real API calls, no cross-screen effects to document beyond what's described above** — there's nothing else in this screen to characterize.

---

## 6. Addendum — the swap search found a systemic pattern, not a one-off

Asked to pick a replacement from the other 7 "both-risk" `MasterController` screens Phase 0 flagged. Checked all 7 for real Angular data bindings (`ng-model`/`ng-repeat` count) before picking, the same way `homecareMedicationApproval` was caught: **all 7 have zero of both.** `combinationMedicationMaster`, `insuranceCompany`, `otIndent`, `chemotheraphyTemplateMaster`, `IPDCharges.drugDetailsForIPDCharge`, `IPDCharges.NOKForIPDCharge`, `ERCharges.drugDetailsForIPDCharge` — every one of them is the same kind of static mockup `homecareMedicationApproval` turned out to be. **There is no real, functional, both-risk `MasterController` screen in the app today.**

That result was surprising enough to check further rather than just report the negative. Ran the same `ng-model`/`ng-repeat` check across all 346 `MasterController` screens with a resolvable view file (not just the 8 both-risk ones): **287 of 346 (83%) have zero of both** — only 58 screens (17%) have any real data binding at all. This isn't a sampling artifact; it's an exhaustive check of the full set. It also isn't surprising once you know what section 5 above already established: `MasterController` itself provides no per-screen data-fetching or save functions, so any screen relying on it alone for real functionality would need its own inline logic in the view or a shared partial to do anything — most of these 353 screens have neither.

**Read plainly: a large fraction of the screens sharing `MasterController` — plausibly most of them — are unfinished UI prototypes, not working product surface with unknown-but-real behavior.** That's a materially different situation than what Phase 0's risk tiers implied (they measured clinical/billing *subject matter* sensitivity, not *whether the screen was ever actually built*). Worth deciding, separately from this pilot, whether someone should get a full inventory of which of the 353 fall into which bucket before Phase 4/5 scale the rebuild process across them — rebuilding a mockup for "exact parity" is a very different task (and a much smaller one) than rebuilding a working screen, and right now Phase 0's table can't tell you which is which without a check like this one.

**Replacement proposed for this pilot slot:** `addItemPOCreation` (Procurement, "PO Creation", `views/procurement/quotation/add-item-PO-creation.html`, `MasterController`-routed). Confirmed genuinely functional and substantial: 98 `ng-model` bindings, 23 `ng-repeat`, 113 `ng-click` handlers wired to real `vm.model.*` functions (this screen uses `controller as vm` syntax rather than bare `$scope`, itself worth knowing before rebuild — the only pilot screen that does). Billing-only risk tier, not "both" — a real both-risk `MasterController` screen doesn't exist to pick, per above. This does mean the final 5-screen batch has two billing-only picks (`insuranceMaster`, `addItemPOCreation`) and no "both" pick; flagging that trade-off rather than deciding it's fine.

**Owner approved 2026-07-17 — proceeded with golden-trace capture on all 5.** See section 7.

---

## 7. Golden-trace capture on all 5 final screens — a tooling bug found and fixed, plus one more real finding

Ran `tools/golden-trace/capture.js` against all 5. First pass: 4 of 5 screens showed only their template GET firing, no real API calls — including `insuranceMaster`, which the source-reading audit in section 2 confirmed makes 6 real calls on load. That mismatch meant the capture tooling itself was broken, not that the audit was wrong, so I didn't report it as-is — traced it down before treating any of this as a finding about the app.

**Root cause: `loginController.js` (lines 44-48) unconditionally clears every browser cookie on every instantiation** — `$cookies.getAll()` + `.remove()` on each key, no filtering. Confirmed with a cookie-write/timing diagnostic (full stack trace captured, not guessed): the app always transits through the `login` state first as part of normal bootstrap (observed consistently across every capture, in this session and the last), so any cookie seeded *before* page load — which is how `tools/golden-trace/capture.js` originally worked — gets wiped by `loginController` before the target screen is ever reached. Confirmed this is deliberate, sensible app behavior (force a clean session on the login screen), not a bug — the bug was in the harness assuming a pre-load cookie would survive it.

**Fixed in `capture.js`:** `cookieObject` is now seeded via `page.evaluate()` *after* bootstrap completes (i.e. after `loginController`'s one-time wipe has already run), not via `addInitScript` before page load. `permissions` (in `localStorage`, a different storage `loginController` doesn't touch) stays seeded pre-load since it doesn't need the fix.

**A second, smaller instance of the same class of gap:** `MISReportsController` and `inputOutputChartsController` both also check a *second* cookie, `cookieObjectAppConfig` (via the same `$rootScope.checkCookies()` helper in `app.js`), redirecting to a real, separate app state (`applicationConfiguration`) when it's missing — exactly like the `cookieObject` guard, just a different cookie. Found the same way: captured without it, read the resulting state-change trace (showed an unexpected extra transition to `applicationConfiguration`) and console error (`Cannot read properties of undefined (reading 'unitId')`), then seeded what was missing. Now seeded alongside `cookieObject`, same post-bootstrap timing.

**With both fixed, re-captured all 5. 4 of 5 now show real, multi-call traces matching the source-reading audit exactly:**

| State | API calls captured | Matches section-by-section audit? |
|---|---|---|
| `insuranceMaster` | 6 (config, country list, ledger search, list, count) | Yes — exactly the calls listed in section 2 |
| `organizationMaster` | 8 (licence types, timezones, countries, list, count, module tree...) | Yes — exactly the calls listed in section 4 |
| `inputOutputCharts` | 3 (intake/output fluid master lists) | Yes — matches section 3 |
| `MISReports` | 4 (formats, report-group header, typeahead template) | Yes — matches section 1 |
| `addItemPOCreation` | **1 (only the template fetch — no change from before the fix)** | **No — see below, this is real, not a harness gap** |

**`addItemPOCreation`'s lack of activity is a genuine app-wiring bug, not a harness limitation — found while investigating why the fix didn't help it.** Its view uses `controller as vm` syntax (`vm.model.*` in 98 `ng-model` bindings and 113 `ng-click` handlers), but its state definition (`app.js` line 11490) routes it through `controller: "MasterController"` with **no `controllerAs` set at all**. `MasterController` never defines anything called `vm` — it's a plain `$scope`-based controller (confirmed in section 5's full read of `common-controller.js`). So every `vm.model.*` binding in the view resolves against `undefined`, silently: reads render blank (Angular's expression parser doesn't throw on property access through an undefined root), and click handlers would throw only if actually clicked, which an automated page-load capture never does. **The screen loads, looks like a real form, and does nothing — no error, no data, just inert.**

The real controller almost certainly exists: `resources/assets/js/controller/procurement/quotation/addNewPOController.js` uses the exact `self.model = {...}` (`controller as` = `self`) pattern the view expects, with real logic (pagination, GRN/PR handling, file upload). **But it is never referenced by any state in `app.js`** — confirmed by grep, zero matches. It's an orphaned controller: written, registered on the Angular module, and never wired to a route. Whether `addItemPOCreation`'s state definition was simply never updated after this controller was built, or the controller was a refactor that never got finished, isn't determinable from the code alone.

This is a third, distinct failure mode found this session, different from both prior ones: not a static mockup (`homecareMedicationApproval`) and not an isolated missing-DI crash (`organizationMaster`), but a **real, apparently-complete controller sitting disconnected from its intended screen**. The captured golden trace for `addItemPOCreation` is left as-is — it accurately reflects current production behavior (load fine, do nothing), which is the honest thing for a golden trace to capture. Not fixed, not routed around; flagged here the same as everything else.

**This changes what "rebuild `addItemPOCreation` for exact parity" would even mean**, and is a decision point rather than something I'm resolving:
- (a) Rebuild the *current* (broken, inert) behavior for true parity, and separately flag the `addNewPOController`↔state wiring mismatch as its own fix candidate (same pattern as `organizationMaster`'s bug) — most conservative, but rebuilds something everyone would agree isn't the intended screen.
- (b) Treat `addNewPOController` as the real intended behavior for this screen and capture/rebuild *that* instead (bypassing the current broken wiring, the same technique already used to get `organizationMaster`'s trace despite its own separate resolve-block bug) — probably closer to what a real PO Creation screen should do, but a bigger scope change to sneak into a "parity" pilot slot.
- (c) Swap this pilot slot again for something unambiguously working — at this point, given every `MasterController`-routed candidate tried so far (8 both-risk + this one) has turned out broken in one of three different ways, I'd want to pick from the 58 confirmed-real `MasterController` screens found during the section-6 exhaustive check, not search blind again.

No recommendation implied by the ordering above — genuinely a judgment call on how far this pilot's scope should stretch to accommodate what the app actually looks like versus what a risk-tier table suggested it looked like.

**Owner decision: don't resolve this inside the pilot.** Logged as its own entry in `docs/ANGULAR_PENDING_FINDINGS.md` (entry 2), swapped the pilot slot instead of picking one of options (a)/(b)/(c) above unilaterally.

---

## 8. addItemPOCreation's completeness check, and the pilot's final replacement

**Completeness check on `addNewPOController.js`, as requested — read the actual code, not just confirmed it exists.** 4,833 lines. No TODO/FIXME/"not implemented"/placeholder markers found anywhere. `SavePurchaseOrder` → `CreatePurchaseOrder`/`UpdatePurchaseOrder` is a complete, real flow: correct field mapping for two different PO types (with type-specific branches for warranty/UOM/bonus-quantity fields), discount/tax/other-charges computation, a genuine `GenericService.serviceAction("POST", ...)` save call, and proper post-save handling (navigation, conditional document upload). 353 `self.model.*` assignments define its full public API surface. **This is finished, working code, not abandoned or unfinished** — read in full detail in `docs/ANGULAR_PENDING_FINDINGS.md` entry 2, which also covers what this led to: `addNewPOController.js` self-registers its own state, `addItemPO` (not `addItemPOCreation`), which is live and correctly wired (`controllerAs: 'vm'` set this time) — and Phase 0 never knew this state existed at all, because it's registered outside `app.js`. That discovery turned out to generalize far beyond this one screen — see `docs/ANGULAR_PENDING_FINDINGS.md` entry 3: **468 more live states exist across 465 other files, none of them in Phase 0's inventory.** Read that entry in full; it's a materially bigger finding than anything else in this pilot.

**Pilot slot swapped, per instruction, to a screen from the 58 confirmed-real `MasterController` screens (section 6) rather than resolving `addItemPOCreation`'s three-way decision inside the pilot.** Didn't just pick from the static-binding list again blind (that list is what led to `addItemPOCreation` in the first place) — verified candidates empirically via golden-trace capture first:

| Candidate | API calls captured |
|---|---:|
| `addNewStaffRegistration` | 0 (template only) |
| `fundApproval` | 0 (template only) |
| **`billingGroupMaster`** | **4 (real: speciality list, group master list, group master count)** |

**`billingGroupMaster` (Billing, `views/master/UnitMaster/billingMaster/billing-group-master.html`) is the confirmed replacement.** Its state definition routes through `MasterController` same as before, but — a fourth distinct wiring pattern found this pilot, and this one *works* — its view embeds its own `<div ng-controller="BillingGroupMasterController">` directly in the HTML (line 2), layering a real, dedicated, `$scope`-based controller (`resources/assets/js/controller/billing/global/BillingGroupMasterController.js`) as a nested scope on top of `MasterController`'s generic one. Confirmed both by reading the source and by the golden trace: `BillingGroupMasterController` appears in the trace's controller-instantiation list alongside `MasterController`, and the 4 real API calls match what a billing group master screen should need. Billing-only risk tier, moderate size (7 fields), genuinely representative of a large class of screens in this app (state → `MasterController` for scaffolding, view → embedded `ng-controller` for the real logic) that's now a fourth confirmed-working wiring pattern worth knowing about for the rebuild, alongside dedicated controllers, `addNewPOController`-style self-registration, and (not-working) `controllerAs` without a matching state config.

**Final pilot batch, golden-trace captured and verified for all 5:**

| Screen | Risk tier | API calls (real) |
|---|---|---:|
| `MISReports` | low-risk | 4 |
| `insuranceMaster` | billing-only | 6 |
| `inputOutputCharts` | clinical-only | 3 |
| `organizationMaster` | low-risk + coupling | 8 |
| `billingGroupMaster` | billing-only | 4 |

---

## Summary — status as of 2026-07-17

1. **organizationMaster's missing-`$state`-injection bug** (section 4): **Fixed**, held for review on `fix/organizationmaster-state-injection`.
2. **homecareMedicationApproval, non-functional mockup** (section 5): swapped for `addItemPOCreation` (section 6), which turned out broken a third way (section 7), **now swapped again for `billingGroupMaster`** (section 8) — confirmed genuinely working, empirically and by source.
3. **insuranceMaster's redundant double validation path** (section 2): **Preserved as-is for the pilot**, logged as a cleanup candidate in `docs/ANGULAR_PENDING_FINDINGS.md` entry 1.
4. **addItemPOCreation's disconnected-but-complete controller** (sections 7-8): logged as its own decision point in `docs/ANGULAR_PENDING_FINDINGS.md` entry 2, not resolved inside the pilot.
5. **~83% of `MasterController`-routed screens appear non-functional** (section 6): confirmed exhaustively (287/346 checked). Recommend a full sweep before scaling the rebuild process, separately from this pilot.
6. **Phase 0's inventory only ever scanned `app.js` — 468 more live, real states exist** (`docs/ANGULAR_PENDING_FINDINGS.md` entry 3, discovered while chasing entry 2): the app has roughly 1,566 real screens, not 1,098. Every downstream Phase 0 number (module counts, risk tiers, the 353-`MasterController`-screens figure) was computed against the smaller, incomplete set. **The single biggest finding of this pilot**, flagged prominently rather than absorbed silently — needs an owner decision on priority before Phase 4/5 scale anything, separate from this pilot's own scope.
7. **Golden-trace capture completed and verified for the final 5 screens** (section 8): all 5 show real, multi-call traces, cross-checked against the source-reading audit.
8. **A capture-tooling bug was found and fixed along the way** (section 7): `capture.js` seeded cookies before page load, which `loginController`'s unconditional cookie-wipe silently discarded on every run. Fixed by seeding after bootstrap.

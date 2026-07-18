# Phase 2 pilot, second batch — audit + rebuild results (controllerAs:'vm' self-registered screens)

*Owner-approved follow-up to the first Phase 2 pilot (`docs/PHASE2_PILOT_AUDIT.md`). Purpose: the corrected Phase 0 re-scan found 468 more live screens Phase 0's original scan missed entirely (`docs/ANGULAR_PENDING_FINDINGS.md` entry 3), 91% of which self-register with `controllerAs:'vm'` — a wiring pattern the first pilot's final 5 screens barely touched (4 of 5 used bare `$scope`, the 5th used a hybrid `MasterController`+embedded-`ng-controller` pattern). This batch runs the same audit → golden-trace → rebuild → re-audit process against 5 screens deliberately picked from that untested population, to check whether the process and tooling generalize before Phase 3 scales across hundreds of them.*

Branch: `rebuild/phase2-pilot-batch2-vm-pattern` (branched from `rebuild/phase2-pilot-batch`, not `main`, so the `new/` workspace conventions carry over). Not merged, not pushed — held for review.

---

## Summary — status as of 2026-07-18

**Approved batch, and what happened to each slot:**

| # | Originally proposed | Risk tier | Outcome |
|---|---|---|---|
| 1 | `GRN` | billing-only | Confirmed real, rebuilt as-is |
| 2 | `SurgicalSiteInfection` | clinical-only | **Non-functional mockup, swapped** → `periodicityService` |
| 3 | `OTBillingCompletion` | both | **Catastrophically mismatched view/controller, swapped** → `implantMaster` |
| 4 | `assigningPickerDispenser` | billing-only | Confirmed real, rebuilt as-is |
| 5 | `assetCategoryMaster` | low-risk | Confirmed real, rebuilt as-is |

**Headline result: 2 of the 5 originally-proposed screens (40%) were non-functional or badly mismatched, discovered only by full-depth audit** — a materially *higher* failure rate than the first pilot's `MasterController` population (which was ~83% non-functional, but that was already known/expected going in; here the screens were pre-filtered by "has real `self.`/`vm.` assignments," which turned out not to be a sufficient functioning-screen filter on its own). This directly validates the concern that motivated running this second pilot at all — see "What this means for Phase 3" below.

**All 5 final screens (2 replaced) rebuilt in `new/`, verified building together with zero errors** (`npx ng build`, 5 new lazy chunks). Golden-trace captured for the 2 replacement screens (`implantMaster`, `periodicityService`); the first pilot's tooling already had traces for `GRN`/`assigningPickerDispenser`/`assetCategoryMaster` from this batch's own earlier capture run. Re-audited every rebuilt screen's service-layer URLs directly against `constants.js` (not just against the audit writeups) — **found and fixed a systematic bug this rebuild introduced**: URLs built from `CONSTANTS.GLOBAL.*`/`CONSTANTS.PHARMACY.*`/`CONSTANTS.PROCUREMENT.*`/`CONSTANTS.MASTER.UNIT.FIXED_ASSET.*` all resolve through base variables (`globalApi`, `pharmacyApi`, `procurementApi`, `unitsApi`) that themselves start with `/api/` — the first rebuild pass of every one of these dropped that `/api/` prefix. Caught by cross-checking golden-trace's actually-captured URLs against the rebuild's service code line by line, not assumed correct. All fixed; `new/` rebuilds clean.

**A new methodology nuance found, worth flagging separately from any one screen's findings:** `controllerAs:'vm'` being set in a state's config does **not** reliably mean the screen's view actually uses `vm.*` bindings. Checked several Nursing-module candidates (`handHygieneSurveyController.js`, `patientFallReportingController.js`, `pocWorklistController.js`) that all declare `controllerAs:'vm'` but whose controllers assign almost exclusively to `$scope.*` (1, 0, and 3 `self.*` assignments respectively, vs. 166+/505+/538+ `$scope.*` assignments) — and their views confirm this: **zero** `vm.*` bindings in any of the three. The `controllerAs` config is dead/no-op on these; the screens function entirely via classic `$scope` inheritance, the exact pattern the *first* pilot already tested. This means the "91% of the 468 new screens use `controllerAs:'vm'`" figure in `docs/ANGULAR_PENDING_FINDINGS.md` entry 3, while accurate as a static-config count, may overstate how many of those screens actually need distinct rebuild handling for this pattern — some meaningful fraction are `controllerAs:'vm'` in name only. Not re-quantified in this pass (would need a full second scan checking `vm.` usage in every one of the 468 views, out of scope here) — flagged for whoever scopes that work next.

---

## 1. GRN (Procurement, billing-only)

**Controller:** `resources/assets/js/controller/procurement/quotation/grnController.js` (605 lines). **View:** `views/procurement/quotation/GRN.html`. Genuinely wired (`self.model.*` throughout, matches `controllerAs:'vm'`).

**On-load sequence:** 3 calls fire in parallel at construction (`cancelReasonService.GetReason()` → `GET /api/global/cancel/reasons`; `StatusService.GetStatus()` → `GET /api/global/status`; `PopulateDropdown()` → `GET /api/procurements/quotation/grn/dropdowns/{unitId}/{organizationId}/{userId}`), then a 4th (`GetGRNList()` → `POST /api/procurements/quotation/grn/search`) fires only after the dropdown call resolves. Confirmed via golden-trace capture: all 4 fired, state-tagged `GRN`, matching the source read exactly.

**Real bug found — dead reference, same class as the first pilot's already-documented findings:** the Cancel button's `ng-click` calls `vm.model.PopulateReasonDropdown()`, a function that **does not exist anywhere in `grnController.js`** (confirmed by grep of the full file). Harmless in practice — `reasons` is already loaded once at init and never needed refreshing — but it's a genuine silent console error on every Cancel click. Logged as `ANGULAR_PENDING_FINDINGS.md` entry 5, not fixed. Not reproduced in the rebuild (the cancel modal just opens with the already-loaded reasons list, matching legacy's actual observable behavior once the broken call is discounted).

**Rebuild:** `new/src/app/features/grn/`. Client-side pagination/search matches `PaginationService`'s real semantics (fetch-once, filter+paginate in-browser). Vendor/item typeaheads wired for real (both were fully audited). `rePrintBillSlip` (PDF generation via a Crystal Reports endpoint) stubbed with a notice — no report-rendering backend to integrate against in this sandbox, same precedent as the first pilot's `inputOutputCharts` Print/PDF stub.

---

## 2. `SurgicalSiteInfection` → swapped for `periodicityService` (Nursing, clinical-only)

**`SurgicalSiteInfection` is a non-functional copy-pasted mockup, confirmed conclusively:** its view (`views/Nursing/surgical-site-infection.html`) has **zero** `ng-model` bindings anywhere in 215 lines, and every checkbox/radio's `ng-click="prescribing($index)"` plus the form's `ng-submit="submitForm()"` reference functions that don't exist in `surgicalSiteInfectionController.js` at all — they exist only in **sibling** controllers in the same directory (`handHygieneSurveyController.js`, `patientFallReportingController.js`, `InitialAssessmentController.js`), confirming the view markup was cloned from one of those and never adapted. The form's own `name` attribute is literally `medicationErrorReporting`, not anything SSI-related — further confirmation this is leftover template debris, not a built-then-broken feature. The only real functionality on this screen is the shared patient-search partial it includes (`nutritional-patient-search.html`), which works fine via `$scope`-style bindings — that part isn't in question, only the SSI-specific 58 form fields are dead.

Logged as `ANGULAR_PENDING_FINDINGS.md` entry 6. Swapped per the same discipline the first pilot used for `homecareMedicationApproval` — verified a replacement empirically (both controller assignment style and actual view `vm.` usage, not just the state config) before committing to it.

**Replacement, `periodicityService`:** `resources/assets/js/controller/services/unit/periodicityServiceOrdersController.js` (721 lines), confirmed genuinely wired (127 `self.*` vs. 107 `$scope.*` in the controller; 25 `vm.*` bindings in the view). On-load: `GET /adt/getActiveGenderList` and `GET /adt/getActiveIdentificationList` fire before the session-cookie guard even runs (a real ordering quirk — both fire even on an expired session, before `init()`'s redirect-to-login check). Real screen: patient search → admission lookup (blocks on `isFinalBill`) → periodicity-service order entry (typeahead → rate lookup → add/edit/remove rows) → save (`POST /api/periodicity/order`).

**Rebuild:** `new/src/app/features/periodicity-service/`. The shared patient-search partial is represented as a minimal inline UHID+Name search (same precedent as the first pilot's `inputOutputCharts`), not the full 6-field partial. The view's own second, duplicate, entirely-dead patient typeahead (`vm.model.searchPatient`/`selectPatient` — the field is permanently `ng-disabled`, and `selectPatient` doesn't even exist live, only as a commented-out stub) is not reproduced. The Equipment sub-block and `updateStatus` status-toggle are both confirmed unreachable in legacy (gated behind a flag nothing ever sets, and a commented-out checkbox, respectively) and not rebuilt.

---

## 3. `OTBillingCompletion` → swapped for `implantMaster` (Procurement - Inventory, both clinical + billing)

**`OTBillingCompletion` is not a partially-broken screen — it's two unrelated screens sharing a URL segment.** The controller ui-router actually instantiates (`otBillCompletionController`, `otBillingCompletionController.js`, 129 lines) implements a small "search procedure orders by date range" feature and fires exactly one real on-load call (`GET /api/otims/getActiveProcedureOrderStatus`) whose result the view never even reads. But the *view* ui-router renders for this same state (`OT-billing-completion.html`, 1,172 lines) is an almost-complete clone of a **different** screen's template — `details-OT-billing-completion.html`, which belongs to a separate, correctly-wired state (`detailsOTBillingCompletion`) backed by a genuinely complete 7,000+-line controller (`otBillingCompletionDetailsController.js`). Of ~35 distinct `vm.model.*` tokens the view references, none resolve against the small controller actually wired to this route — every tab, every button including Save, is inert. Two additional bare (non-`vm`-prefixed) `ng-change` handlers (`selectProcedure`, `getRate`) reference functions that also don't exist in the wired controller — those would throw on interaction, not just render blank.

This is the same failure class as the first pilot's `addItemPOCreation`/`addNewPOController` finding (a real, complete controller sitting disconnected from its intended view/route) but larger in scope — logged as `ANGULAR_PENDING_FINDINGS.md` entry 7, not resolved here, matching the first pilot's precedent of flagging rather than unilaterally picking option (a)/(b)/(c) (rebuild the broken behavior verbatim / adopt the "real" detail-screen controller instead / swap the pilot slot). Swapped the pilot slot, consistent with how `addItemPOCreation` itself was ultimately handled.

**Replacement, `implantMaster`:** `resources/assets/js/controller/master/unit/implantMasterController.js` (836 lines). Independently re-verified (not just spot-checked) — 43 of 45 distinct `vm.model.*` view tokens resolve correctly to real controller assignments; the 2 that don't (`vm.model.isManufacturerValid`, `vm.model.id`) are narrow and mostly cosmetic (one is a dead CSS-class binding, the other throws on first keystroke in the Manufacturer field — see entry 8 below). View/controller size (528 lines / 836 lines) is proportionate, unlike `OTBillingCompletion`'s 1,172-line view against a 129-line controller.

**Two real bugs found, not fixed:**
1. **Same missing-`$state`-DI crash bug as `organizationMaster`'s already-fixed finding** (`fix/organizationmaster-state-injection`) — a *second, independent* occurrence in a different file. `$state` isn't in the DI list but `$state.go('login')` is called on a missing session cookie; throws `ReferenceError` instead of redirecting.
2. **A real data-loss bug**: `UpdateFixedAssetType`'s speciality-save payload reads `x.id` for every row in the speciality picker, but rows added *during* an edit session (via the "+" button) only ever get a `.specialityName` field, never a top-level `.id` — so a speciality added while editing an existing implant silently saves with `id: undefined` and is lost. Pre-existing specialities (loaded from the edit fetch) save fine.

Both logged as `ANGULAR_PENDING_FINDINGS.md` entry 8. The `$state` bug isn't reproducible in this rebuild's architecture (`SessionService` never throws — same as how `organizationMaster`'s rebuild handled its version); the speciality-ID-loss bug **is** reproduced verbatim in the rebuild for byte-for-byte parity, pending an owner decision on whether to fix it.

**Rebuild:** `new/src/app/features/implant-master/`. Cascading Asset Category → Product Category dropdown, manufacturer typeahead, and the speciality add/remove picker (a repeatable-row modal, distinct from the read-only "view specialities" popup) are all wired for real. `getActiveSpecialityList`'s payload hardcodes `organizationId`/`unitId` to the literal `1` in legacy (a real bug — every org/unit sees org/unit 1's speciality list) — reproduced verbatim, not silently corrected to use the real session values.

---

## 4. `assigningPickerDispenser` (Pharmacy Inventory, billing-only)

**Controller:** `resources/assets/js/controller/pharmacy/assigningPickerDispenserController.js` (680 lines). Confirmed genuinely and soundly wired — the most thoroughly-audited screen in this batch, and the only one where the audit found real functional bugs *without* any wiring-mismatch question hanging over it.

**On-load:** 5 real calls (`POST /adt/getPharmacyUserRoleType`, `GET /api/global/currencies/{unitId}` [result unused, same "real call, discarded result" shape as `insuranceMaster`'s CurrencyService in spirit], `GET /api/global/cancel/reasons`, `GET /api/pharmacy/ip/assign/picker/dropdowns`, then `POST /api/pharmacy/ip/assign/picker/search` chained after the dropdown call resolves).

**Real crash bug found, and empirically confirmed by the golden-trace capture itself, not just read from source:** `initializeController()` does `JSON.parse(sessionStorage.getItem('selectedTopStore')).storeName` — dereferencing `.storeName` **before** checking whether the parsed result is `null`. In the golden-trace capture (a fresh session with nothing populating `selectedTopStore`), this throws and **aborts the rest of `initializeController()`** — confirmed directly: the captured trace shows only 2 calls (`getPharmacyUserRoleType` + the template fetch) instead of the expected 5, exactly matching what this crash would produce (everything after the crash point — Currency, cancel reasons, the dropdown/search chain — never fires). This is unusually strong evidence for a source-reading finding: the same bug that was read in the code independently reproduced itself live in the capture harness. Logged as `ANGULAR_PENDING_FINDINGS.md` entry 9.

**A second real bug, lower severity:** `PaginationService.searchData(...)` is skipped when a search returns zero rows (only `pagination.data` is cleared), so `pagination.totalPages`/`filteredData` go stale — a search that goes from 23 results to 0 leaves the previous search's page-number footer rendered under an empty "No Records Found" table, and clicking a stale page link briefly repopulates the table with unrelated rows.

**Rebuild:** `new/src/app/features/assigning-picker-dispenser/`. The crash bug is not reproduced — `sessionStorage` is read defensively (checks for `null` first), the same "use the safe pattern, don't reproduce the crash" precedent already established for `organizationMaster`. The stale-pagination bug is also not reproducible by construction — `pagedRows`/`totalPages` are computed signals that always re-derive from the current `rows` signal, the same "self-correcting by construction" deviation already documented and owner-flagged for `insuranceMaster`'s ledger backfill (`ANGULAR_PENDING_FINDINGS.md` entry 4). The patient-photo "document flipbook" feature (which calls a hardcoded external AWS API Gateway URL with an embedded API key — a real security-hygiene observation, not this screen's bug specifically, since the same boilerplate is copy-pasted across several Pharmacy screens) and the store-switch modal are both out of scope — app-shell/third-party-integration concerns, not this screen's own audited on-load behavior. Report printing is stubbed, same precedent as `GRN`.

---

## 5. `assetCategoryMaster` (Admin/Asset Management, low-risk)

**Controller:** `resources/assets/js/controller/master/AssetManagenent/assetCategoryMasterController.js` (354 lines). Small, clean, genuinely wired — the "low-risk control case" pick, same role `MISReports`/`billingGroupMaster` played in the first pilot.

**On-load:** `PopulateDropdown()` (`GET /api/global/assets/types/{unitId}/{organizationId}`) fires first; only once it resolves does `GetAssetCategories()` fire (`GET /api/units/assets/categories/org/{organizationId}`) — strictly sequential, not concurrent (confirmed via golden-trace: only the first call appeared tagged, since the capture harness has no live backend to resolve the chained second call — consistent with the tooling's documented limitation, not a rebuild bug).

**Real bugs found, not fixed:**
1. **Third occurrence of the missing-`$state`-DI crash bug** (same class as `organizationMaster` and `implantMaster` above) — not reproducible in this rebuild's architecture, same reasoning as the other two.
2. **Search box bypasses `PaginationService` entirely**: the visible search input filters only `vm.model.pagination.data` (the current page's already-sliced rows) via Angular's bare `filter` pipe, never the full `assetCategories` list — a user searching for a category that exists on a different page sees "No Record Found" incorrectly. Reproduced verbatim in the rebuild (`displayedRows` filters `pagedRows()`, not the full `assetCategories()` list) — a deliberately-preserved quirk, not an oversight.
3. **`ToggleStatus`'s 406-error handler always reverts `status` to `true`** regardless of which direction the toggle was attempted — correct for a failed deactivation, a no-op (not a real revert) for a failed activation. Reproduced as-is.
4. **Copy-paste error-message bug**: the Asset Category Description field's "required" error text reads "Enter Asset Category Code" (identical to the Code field's own error, not updated for Description). Reproduced verbatim.

All logged together as `ANGULAR_PENDING_FINDINGS.md` entry 10.

**Rebuild:** `new/src/app/features/asset-category-master/`.

---

## Re-audit against golden traces

Golden-trace capture was re-run for the 2 replacement screens (`implantMaster`, `periodicityService`); `GRN`/`assigningPickerDispenser`/`assetCategoryMaster` traces were captured earlier in this same session, before the swap decisions were made, and remain valid. Cross-checked every rebuilt screen's actual on-load service calls against its trace, the same call-for-call method as the first pilot's section 9:

| Screen | Golden-trace state-tagged calls | Rebuild match |
|---|---|---|
| `GRN` | 4 (`cancel/reasons`, `status`, `grn/dropdowns`, `grn/search`) | Exact, after fixing a URL-prefix bug (see below) |
| `assetCategoryMaster` | 1 (`assets/types`; the chained second call doesn't fire without a live backend, expected) | Exact, after fixing a URL-prefix bug |
| `periodicityService` | 2 (`getActiveGenderList`, `getActiveIdentificationList`) | Exact |
| `assigningPickerDispenser` | 1 (`getPharmacyUserRoleType`; the rest of the chain doesn't fire because of the real crash bug described in entry 4 above) | Rebuild fires all 5 real on-load calls, since the crash isn't reproduced — a deliberate, documented deviation |
| `implantMaster` | 4 (`assets/categories`, `getActiveSpecialityList`, `implantItemMaster/dropdown`, `implantItemMaster/unitId`) | Exact, after fixing a URL-prefix bug |

**A real, systematic bug in this rebuild was found and fixed during this re-audit, worth documenting precisely since it's a process finding, not just a per-screen one:** every URL built from a `CONSTANTS.GLOBAL.*`/`CONSTANTS.PHARMACY.*`/`CONSTANTS.PROCUREMENT.*`/`CONSTANTS.MASTER.UNIT.FIXED_ASSET.*` constant resolves through a base variable (`globalApi = '/api/global'`, `pharmacyApi = '/api/pharmacy'`, `procurementApi = '/api/procurements'` — note **plural**, `unitsApi = '/api/units'`) that itself starts with `/api/`. The first draft of every one of this batch's 5 services dropped that `/api/` prefix (e.g. built `/global/cancel/reasons` instead of `/api/global/cancel/reasons`). This is different from the first pilot's `billingGroupMaster`, whose endpoints correctly have **no** `/api/` prefix (`BILLING = "/billing"`, a different, older `billing.url.config.js` convention with its own bare global vars) — so the fix here isn't "always add `/api/`," it's "resolve each constant to its literal value and don't assume a convention." Caught by cross-checking golden-trace's actually-fired URLs against the rebuild's service code directly, not by re-reading the audit prose — a concrete demonstration of why the re-audit step matters as its own pass, not a formality.

---

## What this means for Phase 3

The original recommendation for running this second pilot (before this batch started) was: the process generalizes, but the `controllerAs:'vm'` population's specific tooling/wiring risk was unvalidated. This batch's results say the caution was warranted, on two distinct axes:

1. **The 40% swap rate (2 of 5) is real signal, not noise.** Both swapped screens failed in ways structurally identical to failures the *first* pilot already found in the `MasterController` population (`addItemPOCreation`'s disconnected-controller pattern, `homecareMedicationApproval`'s non-functional-mockup pattern) — meaning these failure modes aren't specific to `MasterController`; they recur independently in the newly-discovered self-registered population too. Phase 3 should budget real time for "the screen doesn't actually work" discoveries across the 468, not just clean rebuilds.
2. **A rebuild-side systematic bug (the `/api/` prefix) was only caught because this batch included a full re-audit-against-golden-trace pass**, not because the audit-writeup step was thorough (it was, and still missed it — the bug was introduced when translating audit findings into code, not in the audit itself). Whatever process Phase 3 uses at scale needs to keep this verification step, not just the audit step, or systematic implementation bugs like this one will ship silently across many screens at once.

Both are exactly the kind of finding a second pilot was meant to surface before committing to a larger rollout — recommend reading this alongside the recommendation already given for whether the process is ready to scale.

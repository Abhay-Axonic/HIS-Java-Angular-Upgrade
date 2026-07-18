# Angular rebuild: pending cleanup candidates

*Frontend equivalent of `docs/TRIAGE_PendingFindings.md` — things noticed during screen-by-screen characterization that are worth knowing about but are explicitly NOT being fixed now. Nothing here blocks the current phase; this list exists so ideas don't get lost or silently bundled into a "parity" rebuild without a decision.*

---

## 1. insuranceMaster has two redundant, inconsistent validation paths for the same fields

**Where:** `resources/assets/js/controller/master/unit/insuranceMasterController.js`, `save()` (~line 373) and `update()` (~line 730). Found during the Phase 2 pilot audit (`docs/PHASE2_PILOT_AUDIT.md`, section 2).

**What it is, plainly:** the same set of required fields (insurance code, description, country/state/district/city/area, zip, email) is validated twice, by two different mechanisms, in a different order, with different specificity:
- The HTML form itself (`required` attributes + `show-errors` + `insuranceMasterForm.$valid`).
- A separate, manual JS block that re-checks the same fields one-by-one with individual `growl.warning()` messages, runs *before* the HTML-level check, and additionally does `isNaN()` checks on the geography IDs that the HTML-level validation doesn't have.

Email regex, fax regex, and the file-extension allowlist are enforced *only* in the manual JS path, not reflected as HTML5 `pattern` attributes.

**Why this probably isn't deliberate:** this reads like accretion over time — one validation layer added at some point without the earlier one being removed — rather than a designed two-tier system. There's no comment or structural reason found for keeping both.

**Owner decision, per pilot instruction:** preserve both layers exactly for the Phase 2 pilot rebuild (true parity, no behavior change bundled in). This entry exists so the redundancy is a known, tracked cleanup candidate for later — collapsing to one validation layer would be a small, low-risk simplification whenever there's appetite for it, but it is a behavior change (the manual JS path's stricter `isNaN()` checks and regex validation aren't currently expressed in the HTML layer) and needs the same sign-off any other behavior change gets, not a silent fix.

**My read:** low priority, no urgency — this doesn't produce wrong data, it's pure duplication. Worth a look whenever `insuranceMaster` (or its rebuilt equivalent) gets touched for other reasons, not worth scheduling time for on its own.

---

## 2. addItemPOCreation's real controller is disconnected from its route — and it's reachable, live, under a different name Phase 0 never counted

**Where:** `app.js` line 11490 (the `addItemPOCreation` state) and `resources/assets/js/controller/procurement/quotation/addNewPOController.js`. Found during the Phase 2 pilot's investigation of whether this originally-picked screen was viable.

**What it is, plainly:** `addItemPOCreation`'s state definition routes through `MasterController` with no `controllerAs`, so its view's 98 `ng-model`/113 `ng-click` bindings (all written in `controller as vm` style, `vm.model.*`) resolve against `undefined` — the screen loads, looks like a real form, and silently does nothing (see `docs/PHASE2_PILOT_AUDIT.md` section 7 for the full trace evidence).

**Read the actual controller before assuming it's abandoned, per instruction — it isn't.** `addNewPOController.js` is 4,833 lines of real, complete, production-quality logic: full `SavePurchaseOrder`/`CreatePurchaseOrder`/`UpdatePurchaseOrder` flow, real field mapping for two different PO types, discount/tax/UOM/bonus-quantity handling, a genuine `GenericService.serviceAction("POST", ...)` save call with proper success handling (navigation + document upload). No TODO/FIXME/stub markers found. This is not unfinished code.

**It's not orphaned — it's reachable under a state name Phase 0 never knew existed.** The file ends with its own `.config($stateProvider)` block that self-registers a *separate* state, `addItemPO` (not `addItemPOCreation`), correctly wired with `controller: "addNewPO.Controller", controllerAs: "vm"`. The script is loaded (confirmed in `index.html`), so this state is live today. **This discovery led to a much bigger finding — see entry 3 below: Phase 0's entire inventory only ever scanned `app.js`, and never knew self-registering states like this one exist at all.**

**Two separate things worth a decision, not resolved here:**
- `addItemPOCreation` (the state in `app.js`, pointing at `MasterController`) looks like genuinely dead leftover — probably an early placeholder from before `addNewPOController.js` was built, never removed once the real, self-registered `addItemPO` state superseded it. Worth confirming nobody links to it (`ui-sref="addItemPOCreation"` or similar) before treating it as safe to delete.
- If `addItemPO` is the intended, real "PO Creation" screen, Phase 0's inventory should show it as its own entry (module, risk tags, complexity) rather than not existing at all. It doesn't today.

**My read:** not urgent on its own, but not a "someday" item either — this is exactly the kind of dead-route confusion that could waste real rebuild effort if someone builds a parity replica of `addItemPOCreation` (the broken, dead one) without realizing `addItemPO` (the real, working one) is what users actually use.

---

## 3. Phase 0's inventory only scanned app.js — 468 more live, real states exist, registered by individual controller files — RESOLVED 2026-07-18

**Update:** the full corrected re-scan discussed below as a future decision has since been run. `tools/angular-audit/extract_states.py` now scans the whole webapp tree (not just `app.js`), cross-referenced against `index.html`'s live `<script>` tags; `docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md` has been regenerated from the corrected ~1,567-state input. Headline results, and how they compare to the numbers below: **corrected total 1,567 states (1,098 app.js + 469 external, up from the original count), risk tiers shifted from 4/43/27/23/0% (both/clinical/billing/low/unclear) to 5/33/38/22/1%** — billing-only grew the most, both in count (+297) and share (27%→38%), while clinical-only *shrank* as a share (43%→33%) despite growing in raw count, because the denominator grew faster than that tier did. **The `MasterController`-screen count did not move at all — still exactly 353** — none of the 469 newly-found screens route through it; every one has its own dedicated controller. That means the pilot's biggest risk finding about this population (`docs/PHASE2_PILOT_AUDIT.md` section 6: ~83% of `MasterController`-routed screens are non-functional static mockups) **does not transfer to the newly-found 469** — they're a structurally different, entirely disjoint population. What *is* new: 91% of the 469 (431 states) self-register with `controllerAs: 'vm'` set directly in their own state definition — the pattern the pilot only lightly touched once, as a side investigation (`addNewPOController.js`/`addItemPO`, confirmed functional but never one of the 5 final rebuilt/audited screens) rather than as a fully rebuilt-and-re-audited pattern. The remaining 9% (38 states) use dedicated `$scope`-based controllers with no `controllerAs` — the pattern 4 of the pilot's actual 5 final screens *did* use and validate directly. So: the pilot's *process* (audit → golden-trace → rebuild → re-audit → review) generalizes without changes, but its *hands-on wiring-pattern coverage* is lighter than it looks on the dominant pattern in this newly-found population — worth factoring into how Phase 3+ sequences which of the 469 get audited first. Original finding writeup, preserved below for the record.

**Where:** discovered while investigating entry 2 above. Not a single instance — confirmed exhaustively across the whole `resources/assets/js` tree.

**What it is, plainly:** Phase 0's screen inventory (`docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md`, and the `tools/angular-audit/` tooling that produces it) parses `.state(...)` calls out of `app.js` only. It turns out **465 other files** also call `$stateProvider.state(...)` from their own `.config()` blocks — the same self-registration pattern as `addNewPOController.js` (entry 2 above), each defining one or more states of their own, entirely separate from `app.js`'s list. Extracted and cross-referenced all of them against the known 1,098:

- **479 `.state()` calls found outside `app.js`, across 481 files.**
- **475 distinct state names — zero overlap with the 1,098 already known.** These are purely additive, not redefinitions.
- **Cross-checked against `index.html`'s actual (uncommented) `<script>` tags: 468 of those 475 are genuinely live** (the rest — 11 — are either commented-out script tags, like `IPDBillingController.js`, or the same dead git-merge-artifact files already flagged in `docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md`'s known-gaps section).

**In plain terms: the app has roughly 1,566 real screens, not 1,098 — Phase 0's entire inventory (module breakdown, risk tiers, the 353-MasterController-screen analysis, all of it) covers about 70% of the app, not the whole thing.** A rough module split of the 468 new ones (by templateUrl folder, not yet risk-classified): 164 Pharmacy Inventory, 131 various Master data, 119 Procurement, plus smaller numbers across Nursing, Linen, Asset Management, and others — concentrated in exactly the money- and inventory-heavy parts of the app, the same areas Phase 0 already flagged as highest-risk. Spot-checked three at random (`GRN` — Goods Receipt Note, `IPDBilling`, `SurgicalSiteInfection`) — all real, substantial, `controllerAs: 'vm'`-pattern screens with genuine titles and business purpose, not test/demo code.

**This needs a decision, not a silent fix:** extending `tools/angular-audit/`'s state-extraction to scan the whole tree (not just `app.js`) and re-running the full Phase 0 classification against the combined ~1,566 states is a substantial undertaking on the same order as Phase 0 itself, not a quick patch — and every downstream number this project has cited so far (module counts, risk-tier percentages, the 353/32%-of-the-app `MasterController` figure) was computed against the smaller, incomplete set. Flagged prominently to the owner in-session rather than absorbed silently; not undertaken as part of this pilot.

---

## 4. insuranceMaster rebuild's ledger-name backfill fix is not byte-for-byte identical to legacy — a race condition, not just a cleaner implementation

**Where:** `new/src/app/features/insurance-master/insurance-master.ts` (`enrichedList`, `loadFullLedgerList()`) vs. `resources/assets/js/controller/master/unit/insuranceMasterController.js` lines 297-315 (`getLedgerList()`) and 240-282 (`initInsuranceList()`'s nested `forEach` backfill). Found while re-auditing the rebuild against the golden traces (`docs/PHASE2_PILOT_AUDIT.md` section 9), then investigated further per explicit instruction not to wave it off as "just an implementation detail."

**What it is, plainly:** the golden-trace re-audit found the rebuild was missing an unconditional ledger-list fetch and name backfill legacy performs on every load. The fix was verified two ways before being trusted:
- **Duplicate `generalLedgerId` values in the ledger list:** confirmed identical output (Node repro) — both legacy's nested `forEach` and the rebuild's `Map`-based join resolve duplicates the same way (last entry in ledger-list order wins).
- **Cross-endpoint type mismatch (string vs. number) defeating the `Map`'s strict-equality lookup where legacy's loose `==` would still match:** checked against the real backend DTOs (`InsuranceMasterDto.generalLedgerId`, `GeneralLedgerMasterDto.generalLedgerId`) rather than assumed — both are plain numeric Java types with no custom JSON serialization, so this isn't a real risk.

**The one place they genuinely diverge:** legacy fires `getLedgerList()` and `initInsuranceList()`'s two requests back-to-back at controller construction, with no ordering guarantee on which HTTP response comes back first. Legacy's backfill is a **one-shot join**, wired only into `getInsuranceMasterList`'s own response handler, using whatever `$scope.ledgerList` happens to hold at that instant — if the ledger-list request resolves *after* the insurance-list request, the join silently does nothing (empty `ledgerList`), and nothing re-runs it later when the ledger list does arrive. In that ordering, legacy's own list table would show blank `generalLedgerName` values on first paint — an actual, pre-existing bug in legacy, not something introduced by the rebuild. The rebuild's `enrichedList` is a reactive `computed()` over both the insurance list and the ledger list, so it re-evaluates automatically the moment either arrives, and always converges to fully-backfilled output regardless of resolution order.

**Two options, not resolved here:**
- **Keep the rebuild's self-correcting behavior** (what's currently committed). Pro: strictly more correct, never shows a spuriously-blank ledger column. Con: is a genuine, if small, deviation from legacy's observable behavior in one specific race — technically not "exact parity."
- **Make it byte-for-byte identical to legacy**, including the bug: change `enrichedList` to a one-shot snapshot join performed only inside the insurance-list response handler (using whatever `fullLedgerList()` holds at that exact moment, not reactively), so the rebuild can also show blank names when the ledger list resolves second. This would be a strange thing to deliberately build in exchange for parity-for-parity's-sake.

**My read:** the self-correcting version is very likely what any reasonable reviewer would want kept — but per the standing "no deviation without sign-off, however small" rule, this is exactly the kind of case that rule exists for, so it isn't decided here.

---

## 5. GRN's Cancel button calls a function that doesn't exist anywhere in its controller

**Where:** `views/procurement/quotation/GRN.html` line 357 (`ng-click="...; vm.model.PopulateReasonDropdown()"`) vs. `resources/assets/js/controller/procurement/quotation/grnController.js` (full file, 605 lines). Found during the second Phase 2 pilot batch's audit of `GRN` (`docs/PHASE2_PILOT_BATCH2_AUDIT.md` section 1).

**What it is, plainly:** every time a user clicks the Cancel icon on a GRN row, the view calls `vm.model.PopulateReasonDropdown()` — a function that is never defined anywhere in `grnController.js` (confirmed by grep of the full file). This produces a silent console error on every click. Harmless in practice: the `reasons` dropdown it presumably meant to refresh is already loaded once at controller init (`cancelReasonService.GetReason()`, which also memoizes across the session), so the cancel modal shows the correct reasons regardless.

**Owner decision, per pilot instruction:** not fixed — the rebuild reproduces legacy's actual observable behavior (cancel modal opens with the already-loaded reasons list, no refresh call), which is correct once the broken call is discounted. This entry exists so the dead reference itself is tracked, in case anyone later wants to either delete the dead call site in legacy or wire up a real `PopulateReasonDropdown` if a refresh was actually intended.

**My read:** trivial, no urgency — same severity class as a stray console warning, not a functional bug.

---

## 6. SurgicalSiteInfection is a non-functional copy-pasted mockup, not a real screen

**Where:** `views/Nursing/surgical-site-infection.html` (215 lines) vs. `resources/assets/js/controller/nursing/nutritionalAssessment/surgicalSiteInfectionController.js` (186 lines). Found while auditing this screen as part of the second Phase 2 pilot batch, before it was swapped out for `periodicityService` (`docs/PHASE2_PILOT_BATCH2_AUDIT.md` section 2).

**What it is, plainly:** the view has **zero** `ng-model` bindings anywhere in its SSI-specific markup (58 form fields per the static scan, all checkboxes/radios with no two-way binding at all), and every one of them has `ng-click="prescribing($index)"` — a function that does not exist in `surgicalSiteInfectionController.js`. Neither does `submitForm()`, referenced by the surrounding `<form ng-submit="submitForm()">`. Both functions exist only in **sibling** controllers in the same directory (`handHygieneSurveyController.js`, `patientFallReportingController.js`, `InitialAssessmentController.js`) — and the form's own `name` attribute is literally `medicationErrorReporting`, confirming this markup was copy-pasted from one of those screens and never adapted for SSI. The only real, working part of this screen is the shared patient-search partial it includes (`views/common/nutritional-patient-search.html`), which functions correctly via `$scope`-style bindings — that part isn't in question.

**Owner decision, per pilot instruction:** not resolved here — swapped the pilot slot for `periodicityService` rather than rebuilding a mockup, same discipline the first pilot used for `homecareMedicationApproval`. Two things worth a decision, not resolved in this pilot: (a) whether this screen should be finished (someone clearly intended a real SSI-tracking form here and never built it), or (b) whether it should be flagged to end users as non-functional until it is.

**My read:** worth flagging to whoever owns Nursing/Infection-Control workflows — if staff believe they can log Surgical Site Infection data through this screen today, they can't; nothing they enter is captured.

---

## 7. OTBillingCompletion's view is a stale clone of a completely different screen's template

**Where:** `views/master/TheatreManagement/OT-billing-completion.html` (1,172 lines) vs. `resources/assets/js/controller/otims/otBillingCompletionController.js` (129 lines, the controller actually wired to this route). Found while auditing this screen as part of the second Phase 2 pilot batch, before it was swapped out for `implantMaster` (`docs/PHASE2_PILOT_BATCH2_AUDIT.md` section 3).

**What it is, plainly:** this is a bigger, structurally different instance of the same failure class as entry 2 above (`addItemPOCreation`/`addNewPOController`) — a real, complete controller sitting disconnected from its intended view. The controller ui-router actually instantiates for `/OTBillingCompletion` implements a small "search procedure orders by date range" feature and fires exactly one real on-load call whose result its own view never reads. But the *view* rendered for this state is an almost-complete clone of `details-OT-billing-completion.html` — the template for a **separate**, correctly-wired state (`detailsOTBillingCompletion`) backed by a genuinely complete, 7,000+-line controller (`otBillingCompletionDetailsController.js`, `OtBillingDetailsComp.Controller`). Of roughly 35 distinct `vm.model.*` tokens the wired view references, effectively none resolve against the small controller actually driving this route — every tab, every button including Save, is inert. Two bare (non-`vm`-prefixed) `ng-change` handlers (`selectProcedure`, `getRate`) reference functions that also don't exist in the wired controller and would throw on interaction, not just render blank.

**Three options, not resolved here** (same shape as entry 2's three options):
- Rebuild the *current* (broken, inert) behavior for true parity.
- Treat `otBillingCompletionDetailsController.js` as the real intended behavior and rebuild *that* instead — a much bigger scope change (7,000+ lines) than a single pilot slot should absorb without explicit sign-off.
- Leave this screen alone for now (what this pilot did) and flag it for a dedicated pass later.

**My read:** structurally identical to entry 2, and probably deserves the same eventual resolution path (confirm which of the two controllers represents what users actually rely on today, if either) — but given the controller backing `detailsOTBillingCompletion` is far larger than anything audited in either pilot so far, this is a bigger undertaking than entry 2 and shouldn't be scoped casually.

---

## 8. implantMaster: a second missing-`$state`-injection crash bug, and a real speciality-ID data-loss bug on edit

**Where:** `resources/assets/js/controller/master/unit/implantMasterController.js` (836 lines). Found during the second Phase 2 pilot batch's audit of `implantMaster` (`docs/PHASE2_PILOT_BATCH2_AUDIT.md` section 3).

**What it is, plainly — two distinct bugs:**
1. **Same missing-`$state`-DI crash bug as `organizationMaster`'s already-fixed finding** (`fix/organizationmaster-state-injection`), a second, independent occurrence in a different file: `$state` isn't in this controller's DI list, but `$state.go('login')` is called when the session cookie is missing (line 8). Throws `ReferenceError: $state is not defined` instead of redirecting to `/login` — a blank/broken page on session expiry instead of a clean redirect.
2. **A real data-loss bug in `UpdateFixedAssetType`** (lines 581-587): the update payload's speciality list is built as `specialityarray.push({id: x.id})` for every row in `self.model.specialityDetailList`. Rows loaded from the original edit fetch have a real top-level `.id`. But rows added *during* the same edit session via the "+" button (`AddSpeciality`) only ever get a `.specialityName` field, never a top-level `.id` — so a speciality added while editing an existing implant is sent to the backend with `id: undefined` and silently dropped. Pre-existing specialities (untouched during the edit) save correctly.

**Owner decision, not resolved here:** the `$state` bug is the same class already approved for a fix on `organizationMaster` (`fix/organizationmaster-state-injection`) — worth considering whether that fix (or an equivalent one) should be applied here too rather than treated as a fresh one-off decision, given this is now the *third* confirmed occurrence across this project's screens (see also entry 10 below, `assetCategoryMaster`). The speciality-ID-loss bug is reproduced verbatim in the rebuild (`new/src/app/features/implant-master/`) for byte-for-byte parity, pending a decision on whether to fix it.

**My read:** the `$state` bug pattern recurring a third time across unrelated files suggests it's worth a codebase-wide grep for the same DI omission rather than fixing instances one at a time as they're discovered — flagging that as a suggestion, not deciding it here. The speciality-ID-loss bug is a genuine, reproducible data-loss defect worth fixing once approved; it doesn't corrupt existing data, only silently drops newly-added specialities on save.

---

## 9. assigningPickerDispenser: a real crash bug, confirmed live by the golden-trace capture itself

**Where:** `resources/assets/js/controller/pharmacy/assigningPickerDispenserController.js` lines 50-55. Found during the second Phase 2 pilot batch's audit (`docs/PHASE2_PILOT_BATCH2_AUDIT.md` section 4).

**What it is, plainly:** `initializeController()` does `JSON.parse(sessionStorage.getItem('selectedTopStore')).storeName` — dereferencing `.storeName` **before** checking whether the parsed result is `null`. If a user reaches this screen with nothing having populated `selectedTopStore` yet (e.g. a multi-store user who hasn't been through the store-selection flow this session), `JSON.parse(null)` returns `null`, and the very next statement throws `TypeError: Cannot read properties of null (reading 'storeName')` — aborting the rest of `initializeController()` entirely. Everything after that point in the function (the Currency call, the cancel-reasons call, the dropdown-then-search chain) never runs; the user gets a page with static chrome but no working dropdowns and no data.

**Unusually strong evidence for this one, worth noting:** this wasn't just read from source — it was independently reproduced by `tools/golden-trace/capture.js` itself. The captured trace for `assigningPickerDispenser` shows only 2 API calls (`getPharmacyUserRoleType` + the template fetch) instead of the 5 a full, uncrashed load would produce — exactly the signature this crash would leave, in a real browser session with nothing seeded into `sessionStorage.selectedTopStore`.

**Compared directly against the sibling OP screen** (`OPassigningPickerDispenserController.js`), which has the identical `sessionStorage` read but in the correct order (the `null` check runs first, and that variant never reads `.storeName` off the parsed object at all) — confirming this bug is isolated to the IP variant, not a systemic pattern across both siblings, the same comparison technique the first pilot used to isolate `organizationMaster`'s bug.

**Owner decision, not resolved here:** not fixed in legacy. Not reproduced in the rebuild (`new/src/app/features/assigning-picker-dispenser/`) — `sessionStorage` is read defensively there, the same "use the safe pattern, don't reproduce the crash" precedent already established for `organizationMaster`'s `$state` fix.

**My read:** this is a real, user-facing crash bug (not a cosmetic one) — recommend prioritizing a fix in legacy over the other findings in this batch, since it can leave affected users looking at a non-functional page with no error message explaining why.

---

## 10. assetCategoryMaster: a third missing-`$state`-injection crash bug, plus three smaller bugs

**Where:** `resources/assets/js/controller/master/AssetManagenent/assetCategoryMasterController.js` (354 lines). Found during the second Phase 2 pilot batch's audit (`docs/PHASE2_PILOT_BATCH2_AUDIT.md` section 5).

**What it is, plainly — four distinct findings:**
1. **Third occurrence of the missing-`$state`-DI crash bug** (see entry 8 above for the second occurrence, `implantMaster`; `organizationMaster` was the first, already fixed). Same shape: `$state` not injected, `$state.go('login')` called on a missing cookie, throws instead of redirecting.
2. **Search box bypasses `PaginationService` entirely.** The visible search input filters only the current page's already-fetched rows (`vm.model.pagination.data`) via a bare Angular `filter` pipe, never the full `assetCategories` list the way `PaginationService.searchData` (which exists on this screen and is used correctly for the initial load) would. A user searching for a category that exists but isn't on the currently-displayed page sees "No Record Found" incorrectly.
3. **`ToggleStatus`'s 406-error handler always reverts `status` to `true`**, regardless of which direction the toggle was attempted — correct behavior for a failed deactivation, a silent no-op (not a real revert, but also not visibly wrong since the checkbox is already `true`) for a failed activation.
4. **Copy-paste error-message bug:** the Asset Category Description field's "required" validation message reads "Enter Asset Category Code" — identical to the Code field's own error text, never updated for Description.

**Owner decision, not resolved here:** none of the four fixed in legacy. All four are reproduced verbatim in the rebuild (`new/src/app/features/asset-category-master/`) except the `$state` crash, which — same as entries 8 and 9 — isn't reproducible in this rebuild's architecture (`SessionService` never throws).

**My read:** findings 2-4 are all low-severity, easy, independent fixes whenever someone has a reason to touch this screen — none produce wrong *data*, just a confusing search result or a mislabeled error message. Finding 1 (the `$state` bug) is the one worth prioritizing, for the same reason as entry 9's crash bug, though this screen's blast radius (Asset Category Master, a low-traffic admin screen) is smaller than `assigningPickerDispenser`'s.

---

## 11. `controllerAs:'vm'` in a state's config doesn't reliably mean the view uses `vm.*` bindings

**Where:** discovered while searching for a replacement clinical-only screen for entry 6 above. Checked `resources/assets/js/controller/nursing/handHygieneSurveyController.js`, `patientFallReportingController.js`, and `resources/assets/js/controller/services/unit/pocWorklistController.js` — all three declare `controllerAs: 'vm'` in their self-registered state config, matching the "91% of the 468 newly-found screens use `controllerAs:'vm'`" figure in entry 3 above.

**What it is, plainly:** all three controllers assign almost exclusively to `$scope.*`, not `self.*`/`this.*` (1 `self.` vs. 166 `$scope.` assignments in `handHygieneSurveyController.js`; 0 vs. 505 in `patientFallReportingController.js`; 3 vs. 538 in `pocWorklistController.js`). Their views confirm this isn't just a style quirk — all three have **zero** `vm.*` bindings anywhere (confirmed by grep). The `controllerAs:'vm'` declared in their state config is dead, unused configuration; the screens function entirely through classic `$scope` inheritance, the same wiring pattern 4 of the *first* pilot's 5 screens already tested and validated.

**Why this matters:** entry 3's 91%-`controllerAs:'vm'` figure is accurate as a count of what each state's config *declares*, but it doesn't verify that the corresponding view actually *uses* `vm.*` bindings. These three examples show that at least some meaningful fraction of the 468 newly-found screens declare `controllerAs:'vm'` without it doing anything — meaning the real population of screens that need this pattern's specific rebuild handling (as opposed to the already-tested `$scope` pattern) is smaller than 91% of 468, by an unknown amount.

**Not resolved here:** re-quantifying this precisely would mean checking `vm.` usage in all 468 newly-found views, a scan on the same order as the Phase 0 re-scan itself — out of scope for this pilot batch. Flagged for whoever scopes the next phase of work: worth doing this check before using the 91% figure to plan Phase 3+ sequencing or effort estimates for this specific wiring pattern.

**My read:** doesn't change anything about *this* pilot batch's conclusions (the second pilot still found real, functioning `vm.*`-pattern screens and confirmed the process/tooling works against them) — but it's a data-quality caveat worth attaching to entry 3's headline number before anyone treats "91%" as precise.

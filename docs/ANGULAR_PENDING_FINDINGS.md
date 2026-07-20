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

## 11. `controllerAs:'vm'` in a state's config doesn't reliably mean the view uses `vm.*` bindings — RESOLVED (quantified) 2026-07-20

**Update:** the re-quantification this entry called for has since been run, across all 431 states that declare `controllerAs:'vm'` (not just the 3 spot-checked below), by resolving each state's actual view template and counting real `vm\.` bindings. Corrected figures, in place of the original 91%: **391 of the 469 externally-registered screens (83.4%) genuinely use `vm.*` bindings in their view; 78 (16.6%) are effectively `$scope`-pattern** — 38 that never declared `controllerAs:'vm'` at all, plus 40 that declared it but whose view has zero real usage (the same pattern this entry found by hand, now confirmed to recur in 40 of the 431, not just the 3 checked here). This is a real, modest correction (roughly 8.5 points), not the "wildly different" result that would force a rethink of the plan — both wiring patterns remain validated by real, rebuilt screens from the two pilot batches, so risk-tier-first sequencing (see `docs/ANGULAR_PHASE4_SEQUENCING_RECOMMENDATION.md`) still holds. `docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md` and `docs/ANGULAR_REBUILD_PLAYBOOK.md` have been updated to cite 83.4% instead of 91% going forward. Original finding writeup, preserved below for the record.

**Where:** discovered while searching for a replacement clinical-only screen for entry 6 above. Checked `resources/assets/js/controller/nursing/handHygieneSurveyController.js`, `patientFallReportingController.js`, and `resources/assets/js/controller/services/unit/pocWorklistController.js` — all three declare `controllerAs: 'vm'` in their self-registered state config, matching the "91% of the 468 newly-found screens use `controllerAs:'vm'`" figure in entry 3 above.

**What it is, plainly:** all three controllers assign almost exclusively to `$scope.*`, not `self.*`/`this.*` (1 `self.` vs. 166 `$scope.` assignments in `handHygieneSurveyController.js`; 0 vs. 505 in `patientFallReportingController.js`; 3 vs. 538 in `pocWorklistController.js`). Their views confirm this isn't just a style quirk — all three have **zero** `vm.*` bindings anywhere (confirmed by grep). The `controllerAs:'vm'` declared in their state config is dead, unused configuration; the screens function entirely through classic `$scope` inheritance, the same wiring pattern 4 of the *first* pilot's 5 screens already tested and validated.

**Why this matters:** entry 3's 91%-`controllerAs:'vm'` figure is accurate as a count of what each state's config *declares*, but it doesn't verify that the corresponding view actually *uses* `vm.*` bindings. These three examples show that at least some meaningful fraction of the 468 newly-found screens declare `controllerAs:'vm'` without it doing anything — meaning the real population of screens that need this pattern's specific rebuild handling (as opposed to the already-tested `$scope` pattern) is smaller than 91% of 468, by an unknown amount.

**Not resolved here:** re-quantifying this precisely would mean checking `vm.` usage in all 468 newly-found views, a scan on the same order as the Phase 0 re-scan itself — out of scope for this pilot batch. Flagged for whoever scopes the next phase of work: worth doing this check before using the 91% figure to plan Phase 3+ sequencing or effort estimates for this specific wiring pattern.

**My read:** doesn't change anything about *this* pilot batch's conclusions (the second pilot still found real, functioning `vm.*`-pattern screens and confirmed the process/tooling works against them) — but it's a data-quality caveat worth attaching to entry 3's headline number before anyone treats "91%" as precise.

---

## 12. `billingGroupMaster`'s Status toggle looks disabled on the rebuild — two distinct, stacked bugs, found during owner's manual domain review

**Where:** `new/src/app/features/billing-group-master/billing-group-master.ts` (`toggleStatus()`, ~line 211) and `new/src/styles.scss` (`.switch`/`.slider` rules, ~lines 136–155). Found by the owner during manual side-by-side domain review (branch `rebuild/phase2-pilot-batch`, batch 1) — the first real interaction-level discrepancy this pilot has found, since all prior verification (golden-trace) only covered initial page load, never clicks/toggles/saves.

**What it is, plainly — confirmed empirically in a live browser against real dev data (`icdkcd`/`icd-101` rows), not just by reading code:**

1. **Legacy** (`http://localhost:8080/#/billingGroupMaster`): clicking the Status toggle fires `PUT /billing/changeBillingGroupMasterStatus` (200), then legacy's `changeBillingGroupStatus()` calls `$scope.initBillingGroupMaster()` on success — a full list reload — which always visibly flips the switch (confirmed: teal/on → gray/off, and back).
2. **Rebuild** (`http://localhost:4200/billingGroupMaster`, same data via the proxy): clicking the toggle *also* correctly fires the equivalent PUT, gets a 200, shows an "Updated Successfully" toast, and the underlying data genuinely changes (confirmed via direct JS inspection: the checkbox's `checked` DOM property flips, and stays flipped even after forcing an unrelated re-render by typing in the search box — so `toggleStatus()`'s in-place mutation of `row.status`, while not idiomatic signal usage, is not actually broken data-wise). **But the switch never changes appearance at all, in either direction, before or after the click.**
3. **Root cause of the visual bug, confirmed by reading the stylesheet:** `new/src/styles.scss`'s `.slider` rule (~line 149) only sets a static `background: #ccc` — there is no `input:checked + .slider` rule (or equivalent) giving it a distinct "on" color, no knob element/`::before` pseudo-element, and no transform/transition. The switch is visually a flat gray bar in every state; nothing in the CSS makes "on" look different from "off". This is a genuine implementation gap in the shared toggle-switch styles, not a browser quirk or a caching artifact (checked directly against the live rendered DOM).

**Why this reads as "disabled" to a human reviewer:** the toggle *works* (data changes, server round-trip succeeds), but looks completely inert on screen — there's no way for someone watching the page to tell it did anything, which is indistinguishable from "broken" without opening devtools.

**This is a systemic bug spanning both pilot batches, not isolated to one screen:** `.switch`/`.slider` is defined once in each batch's *shared* `new/src/styles.scss` (identical broken CSS copied into both batch branches), and a repo grep shows the same `class="switch"` markup used by `billingGroupMaster`, `insuranceMaster`, `organizationMaster` (batch 1) and `assetCategoryMaster`, `implantMaster` (batch 2) — 5 of the 10 pilot screens total. **Confirmed directly, with a real click-and-verify round trip, on 3 of the 5:** `billingGroupMaster` and `insuranceMaster` (batch 1), and `assetCategoryMaster` (batch 2, real seeded "Pharma" category, toggle click fired `PATCH /api/units/assets/categories/1` → `201`, but the switch stayed visually static throughout). `organizationMaster` (blocked for this test user, see the access-control finding below) and `implantMaster` (not independently click-tested) share the identical CSS and should be assumed affected too until verified otherwise.

**Not fixed** — investigated and logged only, per standing instruction. Two independent things need owner decision:
- The missing `input:checked + .slider` (and knob) CSS rules in `new/src/styles.scss` — a small, low-risk, purely-visual CSS addition, but still a behavior-adjacent change per this project's rules and not applied without sign-off.
- Whether `toggleStatus()` (and any equivalent per-screen status-toggle handler) should be changed to reload the list via the service (matching legacy's `initBillingGroupMaster()` re-fetch pattern) rather than mutating the signal's array contents in place — current evidence says it isn't causing wrong data, but it's fragile: it only visibly updates because *some* unrelated signal write elsewhere happens to trigger a re-render; a screen with no other signal activity between a toggle click and the next user action would show the same "looks like nothing happened" symptom even after a CSS fix, and it diverges from legacy's straightforward full-reload approach.

**My read:** functional break in visible behavior (severity: real gap, not cosmetic-only — a user cannot tell whether the toggle worked without inspecting the DOM), but not a data-loss or data-corruption bug — the underlying status change is realized correctly server-side. Root-caused, not fixed, per instruction.

---

## 13. None of the 10 rebuilt pilot screens enforce module/role-based access control — flagged immediately, not held for the final report

**Where:** `new/src/app/app.routes.ts` on both `rebuild/phase2-pilot-batch` (5 routes) and `rebuild/phase2-pilot-batch2-vm-pattern` (10 routes, the first 5 plus `GRN`/`assetCategoryMaster`/`periodicityService`/`assigningPickerDispenser`/`implantMaster`). Found while interaction-testing `organizationMaster` during this session's browser-automation sweep.

**What it is, plainly, confirmed empirically:** logging in as the session's test user (`Admin`/`DEMO` org+unit) and navigating legacy to `http://localhost:8080/#/organizationMaster` produces a real, correct **"Access Denied — You do not have permission to access this page"** screen — this user's role genuinely lacks module access to Organization Master in legacy. Navigating the *rebuild* to `http://localhost:4200/organizationMaster`, same logged-in session, same user — the full form and data table render normally, no denial, no redirect, fully interactive (fields editable, Save button live).

**Root cause, confirmed by reading both sides:** legacy's `app.js` wires every state's route through a `resolve` block that checks the current user's module/access rights and calls `$state.go("accessDenied")` on failure — this exact pattern repeats at (at least) 8 separate `.state()` definitions in `app.js` (lines ~3127, 3158, 3188, 3220, 3247, 3274, 3301, 3328), i.e. this is legacy's standard, load-bearing access-control mechanism, not incidental to this one screen. The rebuild's `app.routes.ts` defines all 10 pilot routes as plain `loadComponent` entries with **no `canActivate` guard, no resolve step, no access check of any kind** — confirmed by reading the file directly on both branches. This isn't a bug isolated to `organizationMaster`; it's the complete absence of an access-control layer across every screen either pilot batch has built so far.

**Why this is being flagged as its own callout instead of waiting for the consolidated end-of-sweep report:** this is a real production HIS with real patients and real billing/organization data, and right now, anyone who can reach `localhost:4200` (or wherever this rebuild eventually gets deployed/proxied) and knows or guesses a route path gets full read/write access to that screen's data regardless of their actual assigned role or module entitlements — independent of whether they're logged in as a permitted user at all, since none of these routes check session/role state either. This is qualitatively different from the other findings in this doc (which are visual/behavioral parity gaps) — it's a live authorization bypass on real data.

**Two honest, undecided interpretations — not resolved here, owner's call:**
- **This may be an expected, temporary gap of the current pilot architecture.** Every pilot screen so far is a standalone Angular route, deliberately not yet integrated into the legacy shell via ngUpgrade (see `app.routes.ts`'s own header comment: routing strategy is "not decided in this pilot, kept out of scope here"). It's plausible the intended design is that access control gets enforced at the *shell/host* level once ngUpgrade hybrid-mode integration happens in a later phase, the same open-question shape already flagged for `billingGroupMaster`'s missing page-chrome (entry above, "may turn out to be a legitimate open question for Phase 7's ngUpgrade host-page design").
- **Or it may be a real gap that should be addressed now**, e.g. with a route guard that at minimum checks session validity, before this pattern gets replicated across hundreds more screens in later phases — waiting until Phase 7 to retrofit access control onto every already-built screen is a much bigger job than building it in from screen #11 onward.

**Not fixed, per standing instruction** — this needs an explicit decision on which interpretation is correct and, if the former, written confirmation of when/how access control is planned to land, before the rebuild process scales past this pilot.

**Severity: flagged as urgent/blocker-adjacent** — not a data-loss bug, but a live authorization gap on a real system, reported immediately rather than bundled into the end-of-sweep summary.

**Precisely how far the gap goes, confirmed empirically per owner follow-up question:**
- A request with **no authentication at all** does get rejected: `curl` with zero cookies/headers against the raw backend (`GET /organization/count`, `GET /organization/1`, `POST /organization/save`, bypassing the Angular app entirely) returns **401** on all three. So there is one real gate — the backend requires *some* valid logged-in session.
- But past that one gate, **module/role permission is checked nowhere except legacy's own frontend router.** Confirmed directly: the `Admin`/`DEMO` test session that legacy's own UI explicitly refuses to navigate to Organization Master (real "Access Denied" page, this role has no module entitlement for it) — that exact same authenticated session, hitting the backend's `GET /organization/count` via the rebuild, got a clean `200` with real data. The Java backend did not independently re-check module permission; it only checked "is this session authenticated," which it was.
- **Net effect:** any authenticated user of *any* role can reach any of the 10 rebuilt routes and get full real read/write access to that screen's data, regardless of whether their role is actually entitled to that module in legacy. It requires being logged in as *someone*, not as anyone in particular — there is no backend-side authorization fallback once the frontend guard is missing, because legacy never had one to begin with; the module-permission check has only ever lived in the one place the rebuild doesn't reproduce.

---

## 14. MISReports' `unit`/`user`/`org` parameter controls are hard-disabled on the rebuild; legacy renders them as real (if often self-locked) dropdowns

**Where:** `new/src/app/features/mis-reports/mis-reports.html`, `@case ('unit')`/`@case ('user')`/`@case ('org')` (~lines 109-119), vs. legacy's `views/OtherApps/MIS-reports.html` + `MISReportsController.js`. Found while interaction-testing MISReports after seeding one minimal test report group/report (`docs/ANGULAR_PENDING_FINDINGS.md`'s own audit trail didn't have real report data to test against before this — see session notes; a `general_ledger_id`/`block_report_print` DB-drift fix and this report seed were both done with explicit owner approval, additive-only, same class as the earlier BR-038/BR-039/login-drift fixes).

**What it is, plainly:** for these three parameter types, the rebuild's template renders `<input type="text" [value]="param.paramValue" disabled />` — a genuinely non-interactive, greyed-out field prefilled with the current session's unit/user/org **ID number** (e.g. shows "1", not a name). Legacy renders an actual `<select>` element for the same control types. The rebuild's own code comment states this is deliberate: `<!-- auto-populated from session, not user-editable -- see withDefaultValue() -->`.

**Why this is still worth flagging rather than silently accepted:** this exact simplification isn't listed in `docs/PHASE2_DOMAIN_REVIEW_GUIDE.md`'s "deliberate, known, non-bug deviations" section for MISReports (that section lists the search-typeahead stubs only). Whether legacy's dropdown is *ever* actually used to pick a unit/org other than the current session's own — e.g. a multi-unit admin running a report across a different unit than the one they're logged into — couldn't be verified in this pass: the specific test report seeded here has no real `view_name` data source wired up (a synthetic minimal report, not a real production one), so legacy's own dropdown for this same report is also unpopulated in practice. **This needs a real, live report configuration to test properly**, not the synthetic one built for this pass.

**Not fixed, per standing instruction.** Two honest possibilities, not resolved here:
- If no real report ever depends on selecting a *different* unit/org/user than the current session's own, the rebuild's hard-disabled field is a reasonable, harmless simplification.
- If any real report does depend on that (cross-unit reporting is a plausible real use case), the rebuild's current behavior is a functional regression — it would make that report unusable for its intended purpose.

**Severity: real gap, not urgent** — needs the owner (or whoever knows the real MIS report catalog) to say whether any configured report actually relies on changing unit/org/user away from the session default before this can be classified as harmless-simplification vs. functional-regression.

---

## 15. `inputOutputCharts`' rebuilt patient search is functionally broken for virtually all real searches — flagged as an immediate callout, not held for the final report

**Where:** `new/src/app/features/input-output-charts/input-output-charts.ts`, `searchPatients()` (~line 72-86), calling `new/src/app/features/input-output-charts/input-output-charts.service.ts`'s `searchPatients()`, which hits legacy's real, unchanged `POST /adt/searchListOfPatientsByWardId` (`UnknownPatientRegistrationDaoImpl.searchPatientsByWardId`, ~line 1194). Found while interaction-testing this screen after seeding one minimal test patient + admission + ward/bed assignment (real data seeded specifically to make this screen testable — see the DB-drift/seed-data BR entry for detail).

**What it is, plainly, confirmed empirically via direct backend calls (not just UI observation):** the legacy backend's search query builds its filter by **AND-ing together every non-empty search field** — if both `uhIdNumber` and `patientName` are supplied, a patient must match *both* conditions simultaneously to appear in results. Legacy's actual UI has **separate** input boxes for UHID vs. Patient Name (confirmed in its view), so a real user searching by name never also sends a UHID filter, and vice versa — the AND-combination is never actually exercised in real legacy usage.

**The rebuild's "simplified" single search box (already noted in `docs/PHASE2_DOMAIN_REVIEW_GUIDE.md` as a deliberate, in-scope simplification — a *structural* one) sends the exact same user-typed term as *both* `uhIdNumber` and `patientName` in the same request** (`input-output-charts.ts` line 77-78: `uhIdNumber: term, patientName: term`). Verified directly against the real backend, same seeded patient, same ward list: a UHID-only search finds the patient (1 result), a name-only search finds the patient (1 result), but sending both simultaneously — exactly what the rebuild's one search box always does — returns **zero** results. Since a real patient's UHID number and name are essentially never textually identical or overlapping, this means the rebuild's patient search will return nothing for **almost any real-world search**, not a rare edge case.

**Why this is being flagged immediately rather than bundled into the end-of-sweep summary:** this is the primary, and on this screen the *only*, way to find a patient before recording intake/output — a broken search here effectively breaks the whole screen for real use, not a minor cosmetic gap. It's easy to miss in a quick smoke test (a search using *only* a UHID, or *only* a name, works fine and looks correct) — it only surfaces when a real user's search naturally overlaps both concepts, or when tested precisely like this.

**Not fixed, per standing instruction.** The fix is well-understood and small in principle (send only whichever field the user actually intends to search by, matching legacy's two-separate-fields model, or send only one of the two fields rather than both) but changes the search UI's field structure, which the review guide already flagged as an intentional simplification for this pilot — so the *right* fix (restore two separate fields vs. pick one field to search by) is a product decision, not purely a bug fix, and is left for the owner to decide rather than silently changed.

**Severity: real functional break, flagged urgently** — not data loss, but a core interaction (patient search) that will fail for nearly all real searches once real patient data exists, on a screen intended for real clinical use (fluid intake/output charting).

---

# Phase 4, first batch — Step 0/1 audit findings (2026-07-20)

*Entries 16+ below were found during the Step 0 (screen-is-real) / Step 1 (audit) passes on the 14-screen first Phase 4 batch (`docs/ANGULAR_PHASE4_SEQUENCING_RECOMMENDATION.md`), per the playbook (`docs/ANGULAR_REBUILD_PLAYBOOK.md`). Same standing rule as every entry above: found and logged, not fixed. No screen in this batch failed Step 0 — all 14 are real, singly-wired, live screens. Two systemic, previously-unknown SEVERITY 1 crash bugs were found and are listed first.*

## 16. SEVERITY 1 — the `selectedTopStore` "guarded but not returned" crash recurs in 4 more screens beyond the already-known `assigningPickerDispenser` instance — RESOLVED 2026-07-20

**Update:** fixed and merged to `main` (`1fd3150`, `fix/asset-management-selectedtopstore-null-check`, owner-approved via `10449fd`) before this doc entry was ever marked resolved -- caught the staleness while re-verifying Step 0/1 for `viewAssetScheduler`'s Angular rebuild (Phase 4 batch 1a, screen 4/14). Re-confirmed live in the current source: all 4 files (`viewAssetSchedulerController.js:17-23`, `preventiveScheduleWorklistController.js`, `createAssetSchedulerController.js`, `detailScheduleWorklistController.js`) now dereference `cookieObjectStore.storeId` inside the guard's `else` branch, matching the working sibling pattern described below. The 2 spot-checked but not-independently-audited files mentioned below (`preventiveMaintenanceController.js`/`pmRequestController.js`) were NOT re-checked as part of this update -- status unconfirmed, still worth the dedicated repo-wide grep this entry originally recommended. Original writeup preserved below for the record.

**Where:** `resources/assets/js/controller/asset/viewAssetSchedulerController.js` (~lines 17-22), `preventiveScheduleWorklistController.js` (`scheduleWorklist`, ~lines 18-24), `createAssetSchedulerController.js` (`assetScheduler`, ~lines 43-48), `detailScheduleWorklistController.js` (~lines 31-36). Found independently during the Phase 4 batch-1 Step 1 audits of these 4 screens.

**What it is, plainly:** all four controllers have the same shape, and it's a different variant of finding #9's bug, not identical:

```js
var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
if(cookieObjectStore==undefined){
  $rootScope.storePopup();
  angular.element('#pharmacyPopupBeforLeftexpand').modal('show');
}
$scope.storeId = cookieObjectStore.storeId;   // <-- unconditional, no return/else
```

Unlike `assigningPickerDispenser` (zero check at all), these four *do* check for null/undefined and correctly show the store-selection popup — but then fall straight through and dereference `.storeId` on the same null value one line later, throwing `TypeError` and aborting `init()` regardless. Net effect is identical to entry 9: a user who hasn't already selected a store this session gets a broken page instead of a working popup-then-continue flow, on all four screens. Confirmed not a necessary framework consequence by comparing against sibling files in the same directories that guard this correctly (`addNewCorrectiveMaintenanceController.js`, `correctiveMaintenanceReturnWorklistController.js`, `detailsCorrectiveMaintenanceScheduleWorklistController.js`, `OPassigningPickerDispenserController.js` all wrap the dereference in `if(cookieObjectStore!=undefined){...}`) — the same comparison technique this project has used before to confirm a bug is real and isolated to specific files.

**Also affects, not independently audited yet:** a repo-wide grep for `selectedTopStore` turns up 88 files total; `preventiveMaintenanceController.js` (`assetPMSScheduler`, the screen upstream of this whole batch) and `pmRequestController.js` were spot-checked and carry the same unguarded pattern. Worth a dedicated codebase-wide grep before assuming only these 4+2 are affected.

**Owner decision, not resolved here:** the fix is the exact shape Track A already implemented and verified for `assigningPickerDispenser` this session (`fix/assigningpickerdispenser-storename-null-check`, held for review) — move the dereference into an `else` branch. Same fix pattern would resolve all four (plus `preventiveMaintenanceController`/`pmRequestController` if confirmed), but none of these are fixed here, per standing instruction — Track B is audit-only.

**My read:** given this is now confirmed in 5-7 files (assigningPickerDispenser + these 4 + 2 more spot-checked) sharing one exact bug shape, and a working fix pattern already exists and is verified, this is a strong candidate for a single, small, dedicated legacy-fix pass across all instances at once rather than one-off fixes — worth raising to the owner as its own unit of work, separate from any individual screen's rebuild.

---

## 17. SEVERITY 1 — `cookieObjectAppConfig` dereferenced with zero null-check in `scheduleWorklist` and `PMSchedule`

**Where:** `resources/assets/js/controller/asset/preventiveScheduleWorklistController.js` (~lines 28-29), `pmScheduleController.js` (~lines 33-34). Found during the Phase 4 batch-1 Step 1 audits.

**What it is, plainly:**
```js
var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
$scope.assetMaintenanceSpecialityId = cookieObjectAppConfig.assetMaintenanceSpecialityId;
```
No guard at all. The app's own standard helper for this exact cookie, `$rootScope.checkCookies()` (`app.js` ~lines 1881-1892), explicitly checks `cookieObjectAppConfig == undefined` and redirects to `applicationConfiguration` before touching it — proving this is a known, real "can be missing" cookie elsewhere in the same codebase. Neither controller uses that helper or any equivalent guard. If the cookie isn't set, this throws before either controller's own `try/catch` block (both begin later, wrapping only the subsequent API calls) — same outcome as entry 16, a blank/broken page on `init()`.

**Owner decision, not resolved here:** the existing `$rootScope.checkCookies()` helper looks like the natural fix (call it, or replicate its guard-and-redirect shape) — not applied here, per standing instruction.

**My read:** same severity class as entry 16 — a real, live crash bug, worth bundling into the same dedicated legacy-fix pass rather than fixing ad hoc.

---

## 18. SEVERITY 1 — `pesapalProcessPage` can crash on an unguarded query-param `.split()`

**Where:** `resources/assets/js/controller/pesapalSuccessPageController.js`, ~line 47.

**What it is, plainly:**
```js
var param1 = getUrlVars()["pesapal_merchant_reference"];
var res = param1.split("#");   // throws if param1 is undefined
```
`param1` comes straight from the URL query string with no null-check. If Pesapal's redirect ever omits `pesapal_merchant_reference` (malformed/partial callback, back/refresh, bookmarked/direct navigation) this throws synchronously during controller construction — before `getPesapayPaymentStatus` ever fires and before the redirect to `/paymentInformation` runs. The user is stuck on "Wait your payment is in process...." indefinitely, no error, no way to know if a real hospital billing payment went through. Not confirmed triggerable via the current normal Pesapal redirect path (both params appear to always be present in the happy path) — this is a defensive-coding gap on untrusted external input, not a confirmed-reproducing crash like entries 16/17.

**Owner decision, not resolved here:** not fixed, per standing instruction.

**My read:** lower confidence than entries 16/17 on how often this actually fires in practice (depends on Pesapal's real redirect behavior, not verified here), but the failure mode (silent indefinite hang on a payment-confirmation page, zero user feedback) is serious enough to flag as severity 1 rather than wait for confirmation.

---

## 19. `webAppointmentBookingConfirmation`'s "no real data, bounce back" safety check can never fire — a type-mismatched `angular.equals` comparison

**Where:** `resources/assets/js/controller/opd/transection/webAppointmentBookingConfirmationController.js`, ~line 22.

**What it is, plainly:** `var isEmpty = angular.equals('{}', $scope.webAppConfDetObj)` compares the **string** `'{}'` to an **object** — `angular.equals()` returns `false` immediately whenever the two arguments' `typeof` differ, it never does string-vs-object coercion. `isEmpty` is therefore always `false`, so the intended fallback (redirect back to the booking screen when this confirmation page is reached with no real data — bookmark, back/forward, stale link, refresh) never executes, under any condition. A user who reaches this URL with no real appointment object sees a fully-rendered "Appointment Confirmed" page with blank patient/doctor/fee fields, a `NaN`-derived duration, and — because none of the `patientVisit` label's `if/else if` branches match a `NaN` visit-type id — a materially misleading default label ("Follow Up (Video)") regardless of what was actually intended. No crash, no data loss (nothing is written back to the server from this page), but a wrong, misleading confirmation screen.

**Two more issues stacked in the same dead branch, currently unreachable but worth recording:**
- Even if `isEmpty` were fixed, the fallback's own `$state.go('/webAppointmentBooking/'+orgId+"/"+unitId)` passes a URL **path string** where ui-router requires a **state name** — this exact broken pattern (see entry 20) is duplicated verbatim in `bookAppointmentController.js`, where it IS reachable.
- A second, fully dead `function isEmpty(obj) {...}` (~lines 55-61) is hoisted then immediately shadowed by the `var isEmpty = angular.equals(...)` assignment — never callable, and its own logic is inverted (behaves like "isNotEmpty") — reads like an earlier, abandoned implementation left in place, the same accretion-without-cleanup pattern already flagged for `insuranceMaster` (entry 1).

**Owner decision, not resolved here:** not fixed, per standing instruction.

**My read:** moderate severity — misleading, not destructive. Worth fixing alongside entry 20 since they share the same broken `$state.go()` pattern.

---

## 20. `bookAppointment`'s fallback redirect is broken the same way as entry 19 — but here it's actually reachable

**Where:** `resources/assets/js/controller/opd/transection/bookAppointmentController.js`, ~line 25.

**What it is, plainly:** identical broken pattern to entry 19 — `$state.go('/webAppointmentBooking/'+parseInt(orgUnitObj.orgId)+"/"+parseInt(orgUnitObj.unitId))`, a path string passed where ui-router requires a state name (confirmed against the actual bundled `angular-ui-router.js` source, not assumed — `$state.go`'s first argument is documented and implemented as a state name, resolved via `transitionTo`). Fails silently (console warning only), no navigation happens.

**Why this one is live, unlike entry 19's copy:** `$stateParams.webAppDetailObj` is an in-memory, non-URL ui-router param — it does not survive a browser refresh. Any user who refreshes `/bookAppointment` mid-flow lands in exactly this fallback branch (no `isEmpty`-style bug blocking it here). With the redirect broken, execution falls through with `webAppDetailObj = {}` — every subsequent on-load API call (prefix list, doctor details, speciality list, slots) fires with `undefined` organizationId/unitId/doctorId — a broken, blank-looking public booking page with no explanation.

**Owner decision, not resolved here:** not fixed, per standing instruction.

**My read:** real, live, user-facing break in a real patient-facing public flow (unauthenticated appointment booking) — a plausible everyday occurrence (refresh, back/forward) rather than a rare edge case. Worth prioritizing over entry 19's currently-unreachable copy of the same bug.

---

## 21. `scheduleWorklist` and `PMSchedule` both have a broken OR-instead-of-AND multi-criteria search filter

**Where:** `resources/assets/js/controller/asset/preventiveScheduleWorklistController.js` ~line 159, `pmScheduleController.js` ~line 184 — independently duplicated, not shared code.

**What it is, plainly:**
```js
if(fDate >= newDate || tDate <= newDate || store === storeId || pmstype === pmsType || proStatus === processStatus || empId==assignTo ){
```
Criteria are OR'd, not AND'd — a record only needs to satisfy *one* selected filter to appear, not all of them. Compounded by comparing `"DD-MM-YYYY"` date strings with `>=`/`<=` (lexicographic, not chronological). Net effect: selecting a specific store + PMS type + status + employee together returns most/all records regardless, not the intersection the UI implies — a real, live functional bug in each screen's primary search feature.

**Owner decision, not resolved here:** not fixed, per standing instruction.

**My read:** high real-world impact for a search-heavy worklist screen, though not a crash — worth prioritizing over the batch's lower-severity findings below.

---

## 22. `PMSchedule`'s accessory-details lookup always sends a missing `storeId` to the backend

**Where:** `resources/assets/js/controller/asset/pmScheduleController.js`, `getAccessoryDetailsById()`, ~line 231.

**What it is, plainly:** `"storeId": $scope.storeId` — but `$scope.storeId` is never assigned anywhere in this file (the only assignment is commented out). Every accessory-details lookup via the PMS-details icon silently POSTs with a permanently-missing `storeId` to a real backend endpoint (`/api/global/items/unit/{unitId}`) — same bug class as the already-logged `inputOutputCharts` search bug (entry 15): a field that's silently wrong on every real use, not erroring loudly.

**Owner decision, not resolved here:** not fixed, per standing instruction.

---

## 23. `scheduleWorklist` and `PMSchedule` use different numeric codes for the same-looking `processStatus` field

**Where:** `views/master/UnitMaster/assetManagement/schedule-worklist.html` vs. `PM-schedule.html`, `processStatus` `<select>` options.

**What it is, plainly:** `scheduleWorklist`: 1=Requested, 2=Completed, 3=Assigned, 4=Work In Progress, 6=Cancelled, 7=Reopen. `PMSchedule`: 1=Requested, 2=Scheduled, 3=Assigned, 4=Work In Progress, 5=Cancelled, 6=Reopen. Codes 2, 6, and effectively 5/7 mean different things across the two screens, which otherwise look like they should share one status vocabulary for the same underlying PM-schedule records.

**Owner decision, not resolved here:** not resolved — needs confirmation from whoever owns this workflow on whether this is two genuinely different status sets or copy-paste drift, before either mapping is treated as canonical for a rebuild.

---

## 24. Asset Management's checklist-add modal has two `<select>` elements sharing one `ng-model`, duplicated across screens

**Where:** `views/master/UnitMaster/assetManagement/create-asset-scheduler.html` (~lines 242, 257) and `details-schedule-worklist.html`'s equivalent modal (copy-pasted from the former — confirmed by an orphaned `selectedAssetScheduleId` hidden field left over from the copy, real/load-bearing in `assetScheduler` but dead in `detailScheduleWorklist`). A `#addViewChecklistModal` with the same ID also appears in `PM-schedule.html` (not independently confirmed broken there — flagged for follow-up).

**What it is, plainly:** both `<select>`s (meant to show a checklist's name vs. its code) bind `ng-model="checklistMapperObj.checklistEquipmentMasterId"` — selecting a value in either overwrites the same underlying model, so the two dropdowns visibly fight over the same selection. Cosmetic/confusing, not a crash.

**Owner decision, not resolved here:** not fixed, per standing instruction.

---

## 25. `detailScheduleWorklist`'s "PMS Details" modal has 4 tabs that always render empty, plus one fully unreachable static modal

**Where:** `resources/assets/js/controller/asset/detailScheduleWorklistController.js` + shared partials `views/common/PMS-details.html`, `views/common/accessories-details.html`.

**What it is, plainly:** the PMS-details modal's 4 tabs (Manufacture/Accessory/Vendor/Insurance Details) `ng-repeat` over `manufactureDataList`/`itemDataList`/`vendorDataList`/`insuranceDataList` — none of these four are ever assigned anywhere in this controller. Tab-switching itself works (the shared, generic `$rootScope.setFirstTabLevel` mechanism, the same "not a bug" pattern already confirmed for `MISReports`' `checkLabConfig`), so the modal opens and tabs are clickable, but every table is permanently empty. Separately, `#accessoriesDetailsModal` is `ng-include`d but has zero trigger anywhere in this screen, and its body is 100% static hardcoded placeholder markup (`-` in every cell) even if it were somehow opened.

**Owner decision, not resolved here:** not fixed, per standing instruction.

---

## 26. `pharman`: a copy-paste bug on the "Patient" button, a dead modal button, and a broken sibling state sharing its view

**Where:** `views/AppointmentScheduling/medicineInfo.html` (~lines 209-214), `resources/assets/js/controller/opd/transection/pharmanController.js`; the sibling state is defined in `app.js`.

**What it is, plainly, three distinct findings:**
1. The button labeled "Patient" calls `ng-click="setType('Doctor')"` — identical to the neighboring "Doctor" button. Currently harmless: `$scope.type` is set but never read again anywhere, so this is a dead-end copy-paste bug with no live effect today, not a functional break.
2. The modal's "Save changes" button has no `ng-click` and no `data-dismiss` — fully dead, does nothing on click.
3. `app.js` also defines a second state, `medicineInfo`, pointing at this same view file but wired to controller `"masterController"` (lowercase) — which is never registered anywhere (only `"MasterController"`, capital M, exists). If this state were ever reached (no `ui-sref`/`$state.go`/href found anywhere pointing to it, but this app's sidebar is DB-driven, so a seeded menu entry can't be ruled out from source alone), it would throw `Argument 'masterController' is not a function, got undefined` immediately.

**Fourth finding, added during Phase 4 batch 1a's rebuild (screen 5/14) — two inconsistent modal-close paths, one silently accumulates stale data:** the `#DP` modal has two ways to close it: the header's `×` (`medicineInfo.html:237`, `ng-click="clear()"` — resets `$scope.list`/`medicine`/`formulation`/`type`) and the footer's "Close" button (`:277`, `data-dismiss="modal"` only, no `ng-click` at all). `getPopUpData()` (`pharmanController.js:94-129`) *appends* to `$scope.list` every call, never resetting it itself — only `clear()` does. So closing via the footer button leaves `list` populated; the next search's results get appended on top of the old ones rather than replacing them, and the modal (opened again via either "Doctor"/"Patient" button, both mapped to the same handler per finding 1 above) would show both the stale and fresh entries stacked together with no visual distinction between them.

**Fifth finding, also added during the Phase 4 batch 1a rebuild — `getFormsByName()` crashes on any drug with zero forms:** `pharmanController.js:59-76`, `$scope.formulation=$scope.medFormsList[0].formulation` is completely unguarded — if `getPharmanDrugFormByName` returns `status: "success"` with an empty `listObject` (a real, plausible case: a valid drug name matched via the typeahead, but zero medicinal forms recorded for it), `medFormsList[0]` is `undefined` and `.formulation` throws `TypeError`. `getFormsByName()` is called directly from `selectDrug()` (the typeahead's on-select handler), so selecting any such drug crashes immediately, not on some rare secondary action.

**Owner decision, not resolved here:** not fixed, per standing instruction, for all five findings above.

---

## 27. `bookAppointment`: a dead alternate save flow, a 3-layer-dead "EHC Package" sub-feature, a broken Enter-key shortcut, and a silent validation gap for public users

**Where:** `resources/assets/js/controller/opd/transection/bookAppointmentController.js` and `views/AppointmentScheduling/book-appointment.html`.

**What it is, plainly, four distinct findings:**
1. **`$scope.confirmBooking` (~180 lines) is fully dead code.** The only live save path is `bookNormalAppointment()` (posts to `SCHEDULEAPPOINTMENT`); `confirmBooking()` is a separately-implemented alternate flow (posts to `/api/billing/kiosk/saveAndPay` instead) referenced only inside a commented-out legacy form block and leftover HTML comments. Not currently harmful, but two different "what does Save do" implementations exist in the same file, only one live.
2. **A 3-layer-dead "EHC Package" sub-feature:** gated on `selectedSlotId=='3'`, but `selectedSlotId` is never assigned anywhere, so the field can never become visible; its typeahead calls two functions (`searchPackages`, `selectPackageService`) that don't exist in this controller; and the dead branch behind the same flag (inside `calculateAge()`) calls `BillingGenericService`, which isn't in this controller's DI list at all — would throw `ReferenceError` if ever reached. All three pieces are currently unreachable together.
3. **Broken Enter-key search shortcut:** the UHID search input's `press-enter="searchPatient(patientSearchObj)"` references a function and variable that don't exist anywhere in this controller (the real, working search is `searchPatientByUhid()`, correctly wired to the search icon's click). Pressing Enter does nothing; clicking the icon works fine.
4. **Silent, session-dependent validation gap:** the mobile-number field's `maxlength="{{registrationFieldObj.mobileDigit}}"` depends on `$rootScope.registrationFieldObj`, which is only ever populated by a function that itself requires an authenticated session — never called from this deliberately-unauthenticated public booking flow. For a genuine anonymous visitor, `maxlength` interpolates to empty — no mobile-number length limit is actually enforced, silently, despite the markup implying one is.

**Owner decision, not resolved here:** not fixed, per standing instruction.

**My read:** none of these four are crashes or data loss — (1)-(3) are currently-unreachable/dead-code findings worth knowing before treating this controller's behavior as fully understood; (4) is a real, live, quiet validation gap specific to the public/unauthenticated flow.

---

## 28. SEVERITY 1 — `bookView` can silently display one patient's complete clinical record while the visible context is a different patient's, with nothing on screen to indicate a mismatch — REFINED 2026-07-20, proposal at `docs/DECISION_NEEDED_BookViewStalePatientContext.md`

**Update:** a full fix proposal traced the actual ui-router mechanics and found the severity is narrower than originally described here — not a *sustained* wrong-patient render, but a real, verifiable *transient flash* of wrong-patient data before an accidental (not intentional) redirect saves the page. Still a genuine patient-safety finding, still recommended as high-priority — see the linked document for the full corrected mechanics, proposed fix, and open UX decisions. Original writeup preserved below for the record.

**Where:** `resources/assets/js/controller/opd/bookViewMasterController.js`, `checkAppointment()` (~line 133) and the whole controller's data model.

**What it is, plainly:** `bookView`'s route (`url: '/bookView'`) carries no patient identifier at all. The one state-transition input, `$stateParams.bookViewObj`, is used exactly once as an existence-gate (redirect to `bedManagement` if empty) and never read again. Every field on the screen — history, diagnoses, allergies, medications, bills, documents — is instead populated from `$scope.encounterDetailsObj = JSON.parse(sessionStorage.getItem('cookieEncounterObject'))`, re-read fresh on every controller instantiation. That `sessionStorage` key is not private to `bookView` — a targeted search found **15 places across the codebase write it**: the one real entry point into `bookView` (`redirectBookViewUrl()` in `app.js`), plus 14 independent call sites in `encounterQueueManagementController.js`, `doctorDeskController.js`, `counterWorklistController.js`, `casualtyWorklistController.js`, `encounterQueueEHCController.js`, and `progressNoteController.js`, all for their own unrelated navigations, each for a potentially different patient.

**Concrete failure scenario:** a clinician opens `bookView` for Patient X. They then navigate (same tab/session) through any of the 14 other flows and select Patient Y — this silently overwrites `cookieEncounterObject` with Y's data. If they then return to `/bookView` (browser Back, a bookmark, or any in-app link back to it) without going through `redirectBookViewUrl()` again, `checkAppointment()` re-reads the now-stale key and the screen renders **Patient Y's full history, diagnoses, allergies, medications, and bills** — the URL is unchanged (`/bookView`) and the visible patient-demography header comes from the same stale object, so nothing on screen indicates a patient changed. Not click-verified live (read-only audit, no app execution performed), but the code trace is unambiguous and reproducible by inspection, and browser-back is ordinary clinical-workflow behavior, not a contrived edge case.

**Owner decision, not resolved here:** not fixed, per standing instruction. This is a real patient-safety/data-integrity risk in a live system, not a cosmetic gap.

**My read:** this is the single most severe finding of this entire project to date. `bookView` was tagged low-risk by Phase 0's automated keyword/module classifier (no clinical/billing keyword match) — this finding is a concrete, real instance of the inventory doc's own stated caveat ("treat every 'no' as provisional too... keyword/module matching can miss things a human reader would catch") mattering in practice. Recommend re-evaluating whether any other screen sharing this `sessionStorage.cookieEncounterObject` pattern (the 14 writer files, or any other screen reading it the same way) carries the same risk, and whether this risk tier assignment should be revisited before this screen is ever scheduled for rebuild in a low-risk batch again.

---

## 29. SEVERITY 1 — `bookView`'s clinical history fields look editable but have no save path anywhere — a clinician could type real documentation into them and it silently vanishes — CORRECTED 2026-07-20, downgraded, proposal at `docs/DECISION_NEEDED_BookViewUnsavableHistoryFields.md`

**Update — material correction, not just a refinement:** a follow-up investigation found the six `ng-repeat` blocks this entry describes as "editable" never actually render — their backing arrays (`historyPersonalList`, `historyFamilyList`, `historySurgicalList`, `historyMedicalList`, `historyTravelsList`, `dataListDiagnosis`) are never assigned anywhere in the controller, confirmed by grep and independently re-verified. AngularJS's `ngRepeat` over `undefined` produces zero DOM elements. **The specific scenario this entry describes — a clinician typing real documentation into a rendered field and it silently vanishing — cannot occur today, because the fields are not on the page.** The underlying capability gap (no save path exists anywhere in this controller) is still real and still worth fixing, but this is not currently an active, exploitable data-loss trap. Severity downgraded from "single most severe finding" accordingly — see the linked document for the full corrected analysis, why building a real save function is riskier than it looks (a role-gated doctor-review workflow is entangled in the sibling save logic), and the recommended interim cleanup fix. Original writeup preserved below for the record.

**Where:** `resources/assets/js/controller/opd/bookViewMasterController.js` (whole file) and `views/book-view.html`'s History tab (Personal/Family/Surgical/Medical sub-tabs, ~lines 1109-1440, duplicated again in the "Past Visit" tab ~lines 3020-3350) and Diagnosis tab.

**What it is, plainly:** zero save/create/update functions exist anywhere in this 4,011-line controller — every one of its 77 top-level `$scope.*` functions is a `get*` fetcher, a print/report generator, or a UI helper; no PUT, no DELETE, no data-persisting POST. Yet the History and Diagnosis tabs render real, API-populated `ng-repeat` rows with plain, non-`disabled`/non-`readonly` `ng-model` text inputs. Nothing on screen marks these read-only. A clinician could reasonably believe they're editing or adding clinical history here; nothing is ever transmitted anywhere.

**Owner decision, not resolved here:** not fixed, per standing instruction.

**My read:** paired with entry 28, this makes `bookView` the highest-priority screen in this entire findings doc for a dedicated legacy-side decision — either disable/grey out the apparently-editable fields until a real save path exists, or document clearly that this screen is view-only, since the current UI actively invites data entry that is silently discarded.

---

## 30. `bookView`: 23 distinct dead interactive bindings across ~35 call sites, at least one traced to a specific stale-clone source screen — CORRECTED 2026-07-20

**Update:** per entry 29's correction, ~18 of these 23 dead bindings sit inside the six `ng-repeat` blocks that never render at all — those bindings are invisible, not just broken (a user can't click a button that was never drawn to the DOM). The remaining ~5 (`getInvestigationDetails`, `getGroupServiceDetails`, `saveTemplateTransaction`, `setPackageOrderTabActive`, `printDischargeSumReport`) sit outside any dead array and ARE genuinely visible, clickable, and broken as originally described — including the `printDischargeSumReport` stale-clone evidence. See `docs/DECISION_NEEDED_BookViewUnsavableHistoryFields.md` section 3(iii) for the recommended resolution. Original writeup preserved below for the record.

**Where:** `views/book-view.html`'s History, Diagnosis, and Diagnostics tabs, cross-checked against `resources/assets/js/controller/opd/bookViewMasterController.js`.

**What it is, plainly:** comment-stripping the full view and grepping every bound function name against the complete controller finds 23 live (non-commented) bindings calling functions that don't exist in this controller: `addHistoryPersonal`/`removeHistoryPersonal`, `addHistoryFamily`/`removeHistoryFamily`, `addHistorySurgical`/`removeHistorySurgical`, `addHistoryMedical`/`removeHistoryMedical`, `checkedRadioMedi`, `addDiagnosis`/`removeDiagnosis`, `getInvestigationDetails`, `getGroupServiceDetails`, `saveTemplateTransaction`, `setPackageOrderTabActive`, `printDischargeSumReport`, plus every ICD-10/Surgery/Surgeon-name typeahead's source/select functions (`searchGroupList`, `selectDignosisIdForFamily`, `selectSurgeryId`, `searchGroupSurgery`, `searchGroupSurgen`, `selectDignosisIdForMedical`, `selectDignosisIdForDiagnosis`). Most have matching, fully-implemented real functions in a family of sibling "cover-sheet" controllers (`medicalHistoryTabController.js`, `socialHistoryTabController.js`, `allergiesTabController.js`, `diagnosisTabController.js`, `OPDCoverSheetController.js`, `OPDcoversheetNewController.js`) — strong evidence this markup was copied from those editable screens and never adapted. **`printDischargeSumReport` was traced to a specific named source:** it exists only in `controller/adt/transaction/discharge/masterDischargeSummaryController.js` — the clearest single piece of evidence of a stale clone, the same bug class as this project's `SurgicalSiteInfection`/`OTBillingCompletion` findings (entries 6, 7), here confined to specific sections of an otherwise-real, functioning screen rather than the whole view. Practical effect: every affected button/typeahead throws a silent console `TypeError` and does nothing visible.

**Owner decision, not resolved here:** not fixed, per standing instruction.

---

## 31. `bookView`: additional lower-severity findings — row-collision bug, shared-counter blank-screen bug, duplicate markup IDs, and copy-paste residue

**Where:** `views/book-view.html` and `resources/assets/js/controller/opd/bookViewMasterController.js`.

**What it is, plainly — six distinct findings:**
1. **Real per-row data collision (Diagnosis tab, ~line 2259):** one `<input>` has a duplicate `ng-model` (`info.remark` then `genExamination.remark`) — AngularJS keeps only the last, so despite sitting inside `ng-repeat`, this remark box binds every diagnosis row to one shared global value, not a per-row field. Currently moot for data loss (nothing saves regardless, per entry 29), but would silently collapse every row's remark into one if a save path were ever added without fixing this.
2. **Modals can blank the entire main screen.** The six tab-level functions (`setFirstTabLevel` etc.) are global `$rootScope` counters, not per-component state (confirmed safe cross-controller sharing in principle, the same pattern already validated for `MISReports`' `checkLabConfig`) — but the package-details modal and IPD-Notes modal each reuse the *same* counters with their own numeric values. Clicking a tab inside either modal sets the shared counter to a modal-specific value that matches none of the main screen's 19 tab checks — the entire main tab body goes blank behind the modal until the user manually re-clicks a main tab after closing it. A real, demonstrable UX defect in the current production app.
3. **The date-range filter pair (`fromDatepicker`/`toDatepicker`) is copy-pasted 19 times, once per first-level tab, with identical `id`/`name` attributes every time** — since `ng-show` only toggles CSS display, all 19 copies coexist in the live DOM simultaneously (invalid HTML, ambiguous for any `getElementById` lookup — harmless today only because nothing does that).
4. **`#outSourceServiceModal` wraps its body in a `<form>` with no upload control inside it at all** — a vestigial form shell, likely stripped from a real upload screen and left behind.
5. **The "Past Visit" tab is a near-verbatim duplicate of the History tab's Personal/Family/Surgical markup**, carrying the same dead bindings from entry 30 along with it — copy-paste, not a shared partial.
6. **`printCertificateReport` is defined twice, verbatim, back-to-back** (harmless — the second silently overwrites the first with identical logic); a `testFunction` with hardcoded debug IDs (`encounterId: 1726`, `UserID: 2`) exists but is unreachable from the view; `printDischargeCard`'s call site passes a differently-meant argument than its own signature expects, harmless only because the function body ignores that parameter and reads stashed `$scope` state instead.

**Owner decision, not resolved here:** not fixed, per standing instruction.

---

## 32. `outPatientFeedback`/`inPatientFeedback`: matching resolve-block copy-paste bug, harmless in practice

**Where:** `resources/assets/js/controller/opd/transection/outPatientFeedbackController.js` (~lines 300-325) and `inPatientFeedbackController.js` (~lines 300-330), each screen's own `.config($stateProvider)` `resolve` block.

**What it is, plainly:** in both screens, several of the route's `resolve` functions (`visitType`/`visitTypeId`/`encounterId` in outPatientFeedback; `patientId`/`visitType`/`visitTypeId` in inPatientFeedback) all incorrectly `return $stateParams.cabinId` — `cabinId` isn't a real param of either route. Both controllers ignore these injected resolve values entirely and instead re-derive every identifier by manually parsing `window.location.href` (verified correct against each route's real param order) — so this has zero observable impact today, but is a real trap if anyone ever wires the injected resolve params in instead.

**Owner decision, not resolved here:** not fixed, per standing instruction. Both screens otherwise passed Step 0 cleanly and are confirmed genuinely distinct from each other, not a stale clone of one another (field sets are domain-appropriate and non-overlapping).

**My read:** trivial, no urgency — same severity class as entry 5 (GRN's dead Cancel-button reference).

---

## 33. `display` (QMS Token Display): kiosks with 4+ active departments silently render a blank department/token area — RECOVERED 2026-07-20, re-audited after a lost Step 0/1 write-up

**Note on provenance:** this entry number was originally allocated during the Phase 4 first-batch Step 0/1 audit commit (`d9372fd`), which named `display` in its commit message as having real findings, but no write-up for it was ever actually committed — the entry was silently lost. Re-audited from scratch before starting this screen's rebuild rather than trusting the original commit message's claim; findings below are freshly verified, not reconstructed from memory.

**Where:** `resources/assets/js/controller/qms/QMSTokenDisplayMasterController.js` (whole file, 269 lines) and `views/master/QMSMaster/display.html` (~lines 143-306), route `.state('display', ...)` at `resources/assets/js/config/app.js:20149`.

**What it is, plainly:** the view's department-header and token-list markup is implemented as three entirely separate, hand-duplicated blocks, gated by `ng-if="activeQMSDeptList.length == 1"`, `== 2`, and `== 3` respectively (`display.html:143, 158, 172` for headers; `:188, 209, 248` for token lists). There is no fourth block and no default/else case. `activeQMSDeptList` is populated by `confirmQMS()` (`QMSTokenDisplayMasterController.js:75-96`) from a live backend call (`/api/pharmacy/getActiveDeptListByKioskId`) with no client-side cap — if a kiosk is configured (by `qmsKioskMasterId`) with 4 or more active departments, none of the three `ng-if` conditions match, and the **entire** department-header row and its corresponding token-list body silently render nothing — no console error, no crash, just a blank content area. The right-hand "please proceed to counter" call-out table (`display.html:308-336`) is independent of department count and continues to render normally, so the screen doesn't look obviously broken, just missing its main content.

**Second real finding, caught while working through Step 3's rebuild (not the original audit pass) — asymmetric TTS de-duplication, likely repeated audio announcements:** `getTokenList()`'s poll loop (`:123-229`) explicitly tracks a `callCounterToDisplay` array so each newly-called token is announced via `speech()` exactly once, then removed from tracking once its `isCall` status moves on (`:150-166`) — a real, deliberate de-dup mechanism. `getTokenMsg()` (`:237-256`), polling the *separate* `/api/pharmacy/getTokenDisplayMessage` endpoint on its own independent 2-second `$interval` (`:263-267`), has no equivalent de-dup at all: every single poll cycle, if `response.data.listObject` is non-empty, it unconditionally calls `$scope.speech($scope.tokenObject.qmsCallMassage)` again (`:250`) — same message, same object, no "have I already said this" check. Unless the backend itself pops/clears this specific message queue after each fetch (not verifiable from frontend code alone), this would cause the exact same audio announcement to repeat every 2 seconds for as long as the backend keeps returning it — a real, plausible, currently-live UX bug on a screen that's audibly announcing to a public waiting area.

**Secondary, minor, worth noting for exact-parity rebuilding:** `getTokenList()` and `getTokenMsg()` are only ever invoked via `$interval(fn, 2000)` (`QMSTokenDisplayMasterController.js:257-267`) — never called once eagerly on load — and AngularJS's `$interval` does not fire on registration, only after the first full delay elapses. So there is a real, reproducible ~2 second blank gap after `activeQMSDeptList` first renders before any token data appears, every time this screen loads. Not a bug exactly, but a real timing behavior a byte-for-byte rebuild needs to replicate (don't eagerly fetch on init just because it looks like an oversight).

**Third real finding, confirmed live during Step 5 interaction testing — `GenericServiceForWithoutLogin` is misleadingly named and does NOT bypass authentication; a session-less kiosk gets an unbounded, ever-repeating "Token Expired!" error:** `display`'s Step 0/1 read (this entry, above) is correct that the *route* has no login gate — but `GenericServiceForWithoutLogin` (`resources/assets/js/service/GenericService.js:164-214`), the service every one of this controller's API calls goes through, still reads the `cookieObject` cookie, still attaches `Authorization: Bearer <authToken>`/`userId`/`unitId` headers exactly like the fully-authenticated `GenericService`, and on any 401 response still calls `$state.go('login')` plus `growl.error("Token Expired !")` (`:196-208`) — the identical reactive-redirect behavior as every other `serviceAction` variant in the file. It is not actually an unauthenticated code path; the name only describes that the *frontend route* itself has no proactive resolve-block check, while the *backend calls* still require a real session underneath. Confirmed live: navigating to legacy's `display` URL with no session cookie gets all 4 kiosk API calls rejected with 401 (`getActiveDeptListByKioskId`, `applicationConfiguration/billing`, `getTokenListForDisplay`, `getTokenDisplayMessage` — only `screen-images` tolerated it), and the app reactively bounces to `#/login`.

**Because of the entry above's already-documented never-cancelled `$interval`s, this compounds into something worse than a one-time redirect:** `getTokenList()`/`getTokenMsg()` keep polling every 2 seconds forever, even after the state has already transitioned away to `#/login` (no `$destroy` cleanup exists anywhere in this controller). Each poll keeps 401'ing, and each 401 keeps calling `growl.error("Token Expired !")` again. **Confirmed live via screenshot:** landing on `display` with no session produces a growl badge reading "Token Expired! **3x**" within seconds of redirecting to the login screen, and continuing to climb indefinitely — an unbounded, unstoppable repeating error toast on the actual login page, triggered by a screen the user isn't even looking at anymore. This is a real, currently-live, reproducible bug, not a hypothetical.

**Owner decision, not resolved here:** not fixed on the legacy side, per standing instruction, for all findings above. Confirmed real via full controller/view read plus live browser reproduction (not guessed). Whether 4+ department kiosks, the repeated-announcement path, or a kiosk actually losing its session mid-use are hit in current production use is unknown from code alone — flagging for awareness, not asserting real-world frequency.

**My read:** the 4+ department gap and TTS repeat are both moderate severity as previously assessed. The unbounded repeating "Token Expired!" toast is a step up from either — not severity-1 (no crash, no data loss, no security exposure — if anything the backend is correctly rejecting an unauthenticated request), but a real, currently-reproducible, visibly-broken production behavior on the login screen itself with no natural end state short of a manual page refresh, worth the owner's attention on the legacy side independent of this rebuild. Should be rebuilt as-is on the legacy side (not fixed here); see below for how the Angular rebuild's *own* infrastructure handles the equivalent case, which is a different, already-approved decision, not a silent fix of this legacy finding.

**Rebuild-side infrastructure decision (approved, not a legacy fix):** the Angular rebuild had no 401-handling anywhere before this screen — `auth.interceptor.ts` attached the right headers when a session existed but did nothing on a 401, for `display` or for any of the 10 already-rebuilt pilot screens (a pre-existing silent-failure gap, not something this screen introduced). Owner approved adding real 401 handling at the shared interceptor: on a 401, notify `"Token Expired !"` and redirect to `/accessDenied?reason=sessionExpired`, reusing the one redirect convention that already exists (`moduleAccessGuard`'s pattern) rather than a literal `#/login` port, since the Angular app has no login route of its own yet during this hybrid rollout phase. This is deliberately NOT literal parity (different destination than legacy) but is now the intended behavior for the whole rebuild going forward, not just this screen. Separately, `display`'s own `DestroyRef`-based interval cleanup (see Step 3 commit) means the rebuild does not reproduce the unbounded-repeat-toast consequence described above — the component is destroyed on navigation to `/accessDenied`, which cancels both intervals. That cleanup was originally added for ordinary SPA hygiene reasons, not as a targeted fix for this specific bug (which wasn't yet discovered when it was written) — flagging the overlap transparently rather than letting it pass as coincidental.

---

## 34. `applicationConfigurationController.js` — the designated fallback screen for a missing `cookieObjectAppConfig` itself crashes on a missing `cookieObjectAppConfig`, affecting ~53 screens app-wide — PROPOSAL WRITTEN 2026-07-20, see `docs/DECISION_NEEDED_ApplicationConfigurationFallbackCrash.md`

**Update:** fast-tracked for its own review per explicit owner request, given its blast radius directly undermines the just-approved entry-17 fix. Full proposal (root cause, before/after, blast-radius confirmation, minimal fix) at `docs/DECISION_NEEDED_ApplicationConfigurationFallbackCrash.md`. Precise count re-confirmed: **52 files** currently redirect here (not merely "~53" as first estimated), all converging on one single controller registration — confirmed a single, contained fix, not 53 separate ones. Not yet implemented, proposal only. Original writeup preserved below for the record.

**Where:** `resources/assets/js/controller/master/unit/applicationConfigurationController.js`, ~line 717. Found while implementing and real-browser-verifying the fix for entry 17.

**What it is, plainly:**
```js
var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
$scope.registeredPinPadDevice = cookieObjectAppConfig.registeredPinPadDevice;
```
Zero guard. `applicationConfiguration` is the app's own designated redirect target for a missing `cookieObjectAppConfig` — it's what `$rootScope.checkCookies()` (`app.js` ~line 1889) sends a user to, and it's the target ~53 other controllers across the codebase redirect to for the exact same reason (confirmed by a full-codebase survey while scoping entry 17's fix). Every one of those 53+ redirects lands a user on a screen that itself crashes for the identical root cause it was supposed to be the safe landing page for. Confirmed live: after fixing entry 17's `scheduleWorklist`/`PMSchedule` crashes to correctly redirect here, real browser testing showed the redirect succeeds, then this screen throws `TypeError: Cannot read properties of undefined (reading 'registeredPinPadDevice')` immediately on arrival — visible in the console, though the page's own header/nav chrome still renders (the form body renders with mostly-empty fields, not a blank crash page, since Angular's uncaught-exception handling in this version doesn't halt the whole render).

**Owner decision, not resolved here:** not fixed, per standing instruction. This is a legacy-code, pre-existing bug — not caused by, or specific to, the entry-17 fix; it would reproduce identically via any of the other ~53 existing call sites already using this same established redirect pattern.

**My read:** worth prioritizing over most other findings in this doc given its blast radius — it's not one screen's bug, it's the failure-handling path for ~53+ screens' worth of "missing app config" recovery, and it currently fails at the one place that's supposed to catch it. The fix is the same shape as every other entry in this "missing null-check" family: guard the dereference, likely with a sensible default (`registeredPinPadDevice: null` or similar) rather than a redirect, since this *is* the fallback screen — there's nowhere further to send the user.

---

## 35. `pmScheduleController.js`'s unconditional `getAssetDetailsByCriteria()` call crashes whenever `init()` exits early, for any reason — pre-existing, not specific to any one guard

**Where:** `resources/assets/js/controller/asset/pmScheduleController.js`, ~line 168 (`$scope.AssetDetailsByScheduleCriteria.length = 0;`), called unconditionally at ~line 199 (`$scope.getAssetDetailsByCriteria();`) immediately after `$scope.init();`. Found while implementing and verifying entry 17's fix.

**What it is, plainly:** `$scope.AssetDetailsByScheduleCriteria` is only ever initialized (`= []`) inside `init()`, well after the function's own early-return guards (missing `cookieObject` → `$state.go('login'); return;`, and, after entry 17's fix, missing `cookieObjectAppConfig` → `$state.go('applicationConfiguration'); return;`). But `getAssetDetailsByCriteria()` — defined and unconditionally invoked immediately after `$scope.init();`, regardless of whether `init()` completed or returned early — does `$scope.AssetDetailsByScheduleCriteria.length = 0`, an in-place mutation that throws `TypeError: Cannot set properties of undefined (setting 'length')` if the array was never created. **Confirmed pre-existing, not introduced by entry 17's fix:** reproduced the identical crash via the file's own already-existing, unrelated missing-`cookieObject` (login) guard, which has had this exact early-return shape since before this session touched the file. Confirmed in real browser testing that this crash fires but does *not* block the `$state.go()` redirect already in flight from completing — the user still lands on the (separately broken, see entry 34) `applicationConfiguration` screen, with one extra silent console error along the way that has no visible effect.

**Contrast with the sibling `scheduleWorklist` screen, which does NOT have this bug:** `preventiveScheduleWorklistController.js`'s equivalent `getAssetDetailsByCriteria()` reassigns the array fresh (`$scope.AssetDetailsByScheduleCriteria = [];`) at the top of the function, which is always safe regardless of prior state — not an in-place mutation. `pmScheduleController.js`'s version is the odd one out.

**Owner decision, not resolved here:** not fixed, per standing instruction.

**My read:** low practical severity today (confirmed it doesn't block the redirect, just adds a silent extra console error), but worth a one-line fix (`.length = 0` → `= []`, matching the sibling's safe pattern) whenever this file is next touched for another reason.

---

## 36. `pesapalProcessPage` rebuild (Phase 4 batch 1a): COMPLETED/PENDING/FAILED/CONSUMED branch coverage untested — known test-coverage gap, not a bug

**This is not a bug finding — it's a documented test-coverage limitation**, logged per explicit owner request while the `pesapalProcessPage` screen was being rebuilt in Angular (`rebuild/phase4-batch1a`, Step 5). Same category as `display`'s missing-QMS-seed-data gap (entry 33's rebuild work) — flagged as its own entry here specifically because this one involves real payment/transaction status, not just kiosk display data.

**What was and wasn't verified:** Step 5 interaction testing confirmed, live, on both legacy and the Angular rebuild, side by side: the session-required guard (both apps correctly block with no session), entry 18's severity-1 crash (reproduced identically on both apps with a synthetic session cookie and no query params), and the real backend's 401 handling (a fake `authToken` is correctly rejected by the live dev backend, and both apps react equivalently). **What was NOT verified:** `getPesapayPaymentStatus`'s response `transactionStatus` field driving the `COMPLETED`/`PENDING`/`FAILED`/`CONSUMED`/else branch in `pesapalSuccessPageController.js:72-89` (and its rebuild equivalent) — this requires a real, distinguishable Pesapal transaction reference to exercise each status, which does not exist in this dev environment (no real Pesapal gateway integration reachable, no seeded transaction records found). The `CONSUMED` branch specifically (`growl.warning('Already used ', ...)` in legacy, `notify.warning('Already used ')` in the rebuild) is entirely unverified against real data.

**Owner decision, not resolved here:** nothing to fix — this is a coverage gap, not a defect. Re-verify this specific branch behavior (both apps, side by side) once real Pesapal transaction data becomes reachable in any environment, dev or staging.

**My read:** low urgency today since the code path structure was read and matches 1:1 (both apps use the identical if/else-if chain against the same field), but worth closing before this screen's rebuild is treated as fully verified for a real cutover — payment-status handling is exactly the kind of logic where "the code looks right" and "verified against real data" are not the same claim.

---

## 37. The shared `storePopup()`/`setStore()` mechanism silently leaves an empty, seemingly-stuck modal open for single-store users — found while rebuilding `viewAssetScheduler`

**Where:** `resources/assets/js/config/app.js:2607-2653` (`$rootScope.storePopup()` and `$rootScope.setStore()`), consumed by `viewAssetSchedulerController.js:17-23` and at least 3 sibling Asset Management controllers in this Phase 4 batch (`preventiveScheduleWorklistController.js`/`scheduleWorklist`, `createAssetSchedulerController.js`/`assetScheduler`, `detailScheduleWorklistController.js`/`detailScheduleWorklist`) — `selectedTopStore` is referenced in ~88 files repo-wide per entry 16, so the real blast radius is likely much larger than these 4.

**What it is, plainly:** every consuming controller does the same two things when `sessionStorage.selectedTopStore` is missing: calls `$rootScope.storePopup()` (an async fetch) AND, in the same synchronous breath, unconditionally calls `angular.element('#pharmacyPopupBeforLeftexpand').modal('show')` — showing the modal immediately, before the fetch has any chance to resolve. `storePopup()`'s own response handling has two branches: if the user has **more than one** store, it populates `$rootScope.storeList` so the now-visible modal's dropdown has real options, and the user picks one, calling `setStore()` which calls `.modal('hide')`. But if the user has **exactly one** store, `storePopup()` silently auto-selects it (writes `sessionStorage`/`cookieObject.storeId` directly) and returns — **never calling `setStore()` or `.modal('hide')` in this branch.** Net effect: a user with exactly one assigned store, visiting one of these screens for the first time in a session, sees an empty store-picker modal pop up (no options in the dropdown, since `storeList` was never populated for this branch) with no built-in way to close it through the normal flow, even though their store selection actually succeeded correctly in the background a moment earlier.

**Owner decision, not resolved here:** not fixed, per standing instruction. Confirmed via full read of both the modal markup (`index.html:243-267`) and the `storePopup()`/`setStore()` logic, not guessed — the asymmetry (multi-store path hides the modal via user action, single-store path never does) is unambiguous in the code as written.

**Rebuild-side handling:** faithfully replicated in the new shared `StoreSelectionService`/root-shell modal (`new/src/app/core/store-selection.service.ts`, wired in `app.html`) — the same call-site shape (check → fetch-and-show, else read the value), the same silent-auto-select-without-hiding behavior for the single-store branch. Not fixed in the rebuild either, since this is exactly the kind of legacy behavior that should be replicated and flagged, not silently corrected mid-rebuild.

**My read:** moderate severity — not a crash, no data loss, and the underlying store selection genuinely works, but it's a real, confusing dead-end UI state for what's likely the *common* case (many users probably have exactly one assigned store, not several) on every first visit-this-session to any of the ~88 files using this pattern. Given the blast radius and that a working, already-verified fix pattern exists nearby (this project's Phase 2/4 track has fixed several "guarded but not handled correctly" bugs the same way), this seems like a good candidate for a dedicated fix pass similar to entry 16's, rather than a one-off.

**Lower-confidence addendum, unresolved — a second, related exit path where the modal may also stay stuck open:** while Step 5 browser-testing the rebuild with a synthetic (fake-token) session, the store-selection modal appeared correctly (matching legacy's markup) but stayed visibly open on screen even after `authInterceptor` redirected to `/accessDenied` on the backend's 401 response to the now-in-flight `getStoreListById`/`fetchAssetSchedule` calls — a second way this shared modal can end up orphaned, distinct from the single-store case above. I could **not** conclusively confirm whether legacy exhibits the identical behavior: the synthetic `cookieObject` cookie used for testing appears to get cleared by the app shortly after being set (mechanism not identified — possibly a validation/cleanup path triggered by an unrecognized token), which meant repeat checks of legacy's modal state were confounded by the cookie already being gone by the time `viewAssetScheduler`'s controller ran, short-circuiting at the earlier `cookieObject == undefined` guard before ever reaching the modal-show code. Not fixed in the rebuild (deliberately, given the inconclusive parity read) — flagging as open rather than asserting a conclusion the testing didn't actually support. Worth a real login-based re-test (not a synthetic cookie) on both apps once dev-environment login is unblocked (see the DEMO-org unit-seeding gap noted elsewhere in this session's work).

---

## 38. SEVERITY-ADJACENT — `BillingGenericService.serviceAction()`'s error handler throws on every failed request, silently swallowing ALL error feedback app-wide (502 files) — found while auditing `pharman`

**Where:** `resources/assets/js/service/GenericService.js:483-580`, specifically the rejection callback at `:554-576`.

**What it is, plainly:**
```js
var promise = $http(httpObject).then(
    function(response) { ... },
    function(e) {                              // <-- parameter is named `e`
        $rootScope.stopLoader();
        if (error.status === 401) {             // <-- but the body references `error`, never declared anywhere
            $state.go('login');
            growl.error("Token Expired !", {title: ERROR_MSG});
        } else {
            growl.error(NETWORK_ERROR_MSG, {title: ERROR_MSG});
        }
    });
```
`error` is not the callback's parameter (`e` is), not declared locally, and not a global anywhere in the codebase (confirmed by grep across `app.js`, `GenericService.js`, and `index.html`) or via a `$httpProvider` interceptor (none registered anywhere). Referencing an undeclared identifier throws a `ReferenceError` in JavaScript regardless of strict mode. **This means every single failed request made through `BillingGenericService` — network error, 400, 401, 500, anything — throws inside its own error handler before either branch (401-redirect-to-login, or the generic error growl) can run.** No growl message is ever shown. No redirect on session expiry ever fires. The failure is completely silent from the user's perspective; the only trace is an unhandled-promise-rejection warning in the browser console, which no user will ever see. Confirmed no callers add their own `.catch()`/second `.then()` argument that would provide a fallback either — spot-checked `pharmanController.js`'s three `BillingGenericService` calls (`searchPharmanDrugList`, `getFormsByName`, `getPopUpData`), none do.

**Blast radius:** `grep -rl "BillingGenericService" resources/assets/js/controller/` returns **502 files** — this is not scoped to `pharman`, it's the error-handling path for a very large fraction of the billing-adjacent controller surface. Compare to the sibling, correctly-implemented `GenericService.serviceAction()` (`:2-163` in the same file), which uses the right parameter name (`error`) and works as intended (already relied on and verified for `pesapalProcessPage`'s and `viewAssetScheduler`'s 401 handling elsewhere in this session).

**Owner decision, not resolved here:** not fixed, per standing instruction (owner explicitly asked about this mid-session given its scale and chose to log-and-continue, same treatment as entries 16/34's pattern, deferring a dedicated fix pass rather than fast-tracking it the way entry 34 was).

**My read:** not severity-1 in the strict sense used elsewhere in this doc (no crash, no data loss, no security exposure) but close to it in practical consequence — a silent, app-wide failure to surface session-expiry and error states across the majority of billing-related screens is a real risk for a system whose #1 stated priority (per this project's own CLAUDE.md) is billing accuracy and not silently swallowing errors. The fix is a one-line rename (`error` → `e`, or `e` → `error`, whichever matches intent) restoring behavior that's clearly already correctly implemented one function away in the same file — low-risk, high-value, and a strong candidate for prioritization once the owner is ready to triage this doc's backlog.

---

## 39. `webAppointmentBooking`: dead unit-address label, an inconsistent radio-group checked state, and a response-status check that doesn't actually gate anything downstream

**Where:** `resources/assets/js/controller/opd/transection/webAppController.js` (147 lines) and `views/AppointmentScheduling/web-appointment-booking.html`.

**What it is, plainly, three distinct findings:**
1. **`<label class="addressLable labeltopadd" id="unitAddress"></label>`** (`web-appointment-booking.html:126`) has no Angular binding at all — not even a stray `{{}}` — and no code anywhere in the controller ever sets its text via `id`-based DOM manipulation either. Confirmed always empty; dead markup, likely copy-pasted from `webAppointmentBookingConfirmation`'s near-identical header (which *does* populate the equivalent field from real data).
2. **Every radio button in the speciality picker hardcodes `checked="checked"`** — the "All" radio (`:151`) and every dynamically-repeated speciality radio (`:157`, inside `ng-repeat="spec in specialityMasterList"`) all carry this attribute. Since they share one `name="specialities"` radio group, the browser can only visually check one at a time — and because the speciality radios render *after* "All" (added asynchronously once `specialityMasterList` loads), the last-rendered speciality radio silently wins the browser's native checked state, unchecking "All" with no `ng-click` ever firing (native attribute rendering doesn't dispatch a click event). The doctor list actually shown, however, still reflects whichever `getDoctorsList()` call last ran explicitly (the initial "All doctors" load, `webAppController.js:71-89`) — so the radio group can visually show a specific speciality selected while the doctor list underneath is still showing all doctors, until the user manually clicks something themselves.
3. **`getUnitListByOrgId()`'s response handler** (`:58-90`) only populates `$scope.unitList`/branches on `selectedUnitId` inside its `if (response.data.status == "success")` block — but the two lines immediately after that block (`$scope.unitId=$scope.selectedUnitId;` and the `getSpecialityMasterList()`/`getDoctors` POST that follows) run **unconditionally**, regardless of whether the status check passed. On a non-success response, `selectedUnitId` stays at its initial value (`0`), and the doctor-list fetch proceeds anyway with `unitId: 0` — not a crash, but a real "silently queries with a placeholder unit" edge case.
4. **`src="{{doc.doctorImage}}"` instead of `ng-src`** (`web-appointment-booking.html:168`) — a classic AngularJS footgun: the browser eagerly fetches the literal, uninterpolated `src` attribute value before Angular's compiler ever runs, so every doctor row fires one spurious, harmless 404 request for the literal URL `{{doc.doctorImage}}` before the real interpolated image URL loads correctly right after. Confirmed live via network trace during Step 5 testing. Cosmetic — an extra failed request in the network log, no visible effect since the correct image request follows immediately.

**Owner decision, not resolved here:** not fixed, per standing instruction, for all four findings.

**My read:** all low-to-moderate severity — no crash, no data loss. Finding 2 is the most user-visible (a real, demonstrable UI/state mismatch a patient could notice while browsing doctors), findings 1, 3, and 4 are cosmetic/edge-case. Also worth flagging structurally: this controller has **zero session guard anywhere** (no `cookieObject` check, `$scope.userId` hardcoded to `1`), consistent with being a genuinely public, patient-facing "browse doctors" landing page — same shape as `webAppointmentBookingConfirmation`. Unlike `display` (entry 33), this screen uses plain `GenericService`, not the `...ForWithoutLogin` variant.

**Step 5 update — anonymous access CONFIRMED to genuinely work, unlike entry 33's `display`:** tested live against legacy with all cookies cleared (no session at all). All three backend calls (`/unit/org/1`, `/adt/getActiveSpecialityList`, `/api/global/webappointment/getDoctors`) returned real `200` responses with real data (a genuinely seeded "General Medicine" speciality rendered correctly) — no 401, no bounce to login. Unlike `display`'s `GenericServiceForWithoutLogin` misnomer, this screen's use of plain `GenericService` really does tolerate a session-less request end-to-end here, meaning this screen's "no frontend guard" design matches its actual backend behavior — a positive result, not another instance of entry 33's trap. Also confirmed the Angular rebuild reproduces this correctly once two real bugs found during this same Step 5 pass were fixed (both logged in the corresponding rebuild commit, not here — see `rebuild/phase4-batch1a`): a dev-proxy gap (`/unit/**` was never covered by `proxy.conf.json`) and a Bootstrap grid-nesting layout bug causing left-edge clipping.

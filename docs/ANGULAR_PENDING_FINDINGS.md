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

## 3. Phase 0's inventory only scanned app.js — 468 more live, real states exist, registered by individual controller files

**Where:** discovered while investigating entry 2 above. Not a single instance — confirmed exhaustively across the whole `resources/assets/js` tree.

**What it is, plainly:** Phase 0's screen inventory (`docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md`, and the `tools/angular-audit/` tooling that produces it) parses `.state(...)` calls out of `app.js` only. It turns out **465 other files** also call `$stateProvider.state(...)` from their own `.config()` blocks — the same self-registration pattern as `addNewPOController.js` (entry 2 above), each defining one or more states of their own, entirely separate from `app.js`'s list. Extracted and cross-referenced all of them against the known 1,098:

- **479 `.state()` calls found outside `app.js`, across 481 files.**
- **475 distinct state names — zero overlap with the 1,098 already known.** These are purely additive, not redefinitions.
- **Cross-checked against `index.html`'s actual (uncommented) `<script>` tags: 468 of those 475 are genuinely live** (the rest — 11 — are either commented-out script tags, like `IPDBillingController.js`, or the same dead git-merge-artifact files already flagged in `docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md`'s known-gaps section).

**In plain terms: the app has roughly 1,566 real screens, not 1,098 — Phase 0's entire inventory (module breakdown, risk tiers, the 353-MasterController-screen analysis, all of it) covers about 70% of the app, not the whole thing.** A rough module split of the 468 new ones (by templateUrl folder, not yet risk-classified): 164 Pharmacy Inventory, 131 various Master data, 119 Procurement, plus smaller numbers across Nursing, Linen, Asset Management, and others — concentrated in exactly the money- and inventory-heavy parts of the app, the same areas Phase 0 already flagged as highest-risk. Spot-checked three at random (`GRN` — Goods Receipt Note, `IPDBilling`, `SurgicalSiteInfection`) — all real, substantial, `controllerAs: 'vm'`-pattern screens with genuine titles and business purpose, not test/demo code.

**This needs a decision, not a silent fix:** extending `tools/angular-audit/`'s state-extraction to scan the whole tree (not just `app.js`) and re-running the full Phase 0 classification against the combined ~1,566 states is a substantial undertaking on the same order as Phase 0 itself, not a quick patch — and every downstream number this project has cited so far (module counts, risk-tier percentages, the 353/32%-of-the-app `MasterController` figure) was computed against the smaller, incomplete set. Flagged prominently to the owner in-session rather than absorbed silently; not undertaken as part of this pilot.

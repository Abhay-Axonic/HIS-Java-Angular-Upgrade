# Batch 30 — Step 6 Consolidated Test Checklist

**Screens:** `referralMaster`, `campMaster`, `dishMaster`
**Branch:** `rebuild/phase4-batch30`
**Status:** Steps 0-5 complete, live-verified by the orchestrating session. Step 6 (this checklist) is the owner's own manual click-through — mandatory, blocking, cannot be marked complete by any agent or tool.

**Legacy backend fixes found and merged during this batch's Step 5 testing** (all held on their own dedicated branches, live-verified, not yet merged/pushed unless noted): BR-109 (`adt.m_referral_master` missing `medall_referral_id` — already-known local-dev drift, same class as BR-104/106), BR-110 (`kitchen.m_dish_master` + `kitchen.dish_master_diet_pref_mapper` missing 6 columns total across 2 tables — same class). Both were blocking Save entirely before the fix; both are now applied to this dev DB (`his_local`) and required for this checklist's Create steps to work.

**Access:** `http://localhost:4200/referralMaster`, `http://localhost:4200/campMaster`, `http://localhost:4200/dishMaster` (Angular dev server, proxying to the real backend on port 8080). Log in via the real legacy app first (same host, shared `cookieObject` cookie) — see session notes for the login flow. If a screen redirects to Access Denied, your session's `localStorage.permissions` may be missing the module ID; ask for the shim used this session (moduleId 396 for referralMaster, 511 for dishMaster; campMaster has no module gate).

---

## 1. `referralMaster` (moduleId 396, no superuser gate)

**Disclosed fix to verify specifically:** the geo cascade (Country→State→District→City→Area) now correctly clears all dependent levels' selections when a parent level changes — legacy silently left stale district/city/area values in place after a country change, risking a save with mismatched geo data. Confirmed live this session; please re-confirm from your own click-through.

- [ ] Load the screen. Confirm Referral-Type, Country dropdowns populate with real data (not empty).
- [ ] Select Country → India, then State → Maharashtra → District → Pune → City → Pune → Area → Shivaji Nagar (full chain).
- [ ] **Cascade fix check:** with the full chain selected, change Country to United States. Confirm State/District/City/Area ALL reset to unselected/empty (not just State) — this is the fixed behavior; legacy left District/City/Area silently populated with stale values.
- [ ] Change Country back to India, re-select the full chain, fill Referral Name/Address/Contact No. (all required, `*`), click Save, confirm the modal, confirm OK. Confirm a new row appears in the grid with the correct data.
- [ ] Click the Edit (pencil) icon on the row just created. Confirm ALL 5 geo levels load together correctly (not cleared) — this is intentionally different code from the cascade fix above; edit-load should NOT trigger the clearing logic.
- [ ] Toggle the row's status switch. Confirm it flips and the change persists after a page refresh.
- [ ] Try Save with Referral-Type, Referral Name, Address, or Contact No. left blank — confirm a visible inline validation message appears (not just a toast), and Save is blocked.

## 2. `campMaster` (no module gate — confirm this yourself: any account should reach it)

**Disclosed fix to verify specifically:** legacy's Save/Update/status-toggle all succeeded in the database but never showed a confirmation or refreshed the grid (a broken `getcampList()` call). Fixed to show a real confirmation and refresh the list. Also fixed: repeated edits no longer accumulate duplicate stale options in the geo dropdowns.

- [ ] Load the screen. Confirm Country dropdown populates.
- [ ] Fill Camp Code, Camp Name, Address, Pin Code (all required), select the full Country→State→District→City→Area chain (all required here, unlike referralMaster), click Save, confirm the modal.
- [ ] **List-refresh fix check:** confirm a success message appears AND the new camp appears in the grid immediately, with no manual page reload needed.
- [ ] Edit the row just created. Confirm all fields (including the full geo chain) load correctly.
- [ ] Edit a second time (or edit, cancel, edit again). **Duplicate-accumulation fix check:** confirm the State/District/City/Area dropdowns each show only their real, correct set of options — no repeated/duplicate entries piling up.
- [ ] Toggle the row's status switch. Confirm a success message appears, the grid updates, and the change persists after a page refresh.
- [ ] Try Save with a required field blank — confirm a visible inline validation message, Save blocked.

## 3. `dishMaster` (moduleId 511, no superuser gate)

**Disclosed fix to verify specifically:** legacy's Edit could be clicked before 3 of the screen's 6 load-time API calls (Meal Preference/Meal Type/Diet Type) resolved, silently leaving the edit form's checkbox groups empty and producing a misleading "Meal Type/Diet Type should not be empty" validation error on Save that looked like a user mistake. Fixed by gating Edit until those 3 lists have genuinely loaded.

- [ ] Load the screen. Confirm the Clinical Unit dropdown, and all 3 checkbox groups (Meal Preference, Meal Type, Diet Type) show real options (currently 1 seeded row each — "abcd" / "Test Meal Pref" / "Breakfast Updated" / "no desc").
- [ ] Fill Code, Dish Name, Value, select Clinical Unit; check at least one option in each of the 3 checkbox groups; fill the pre-populated key-ingredient row's Value and Clinical Unit fields. Click the Save icon (floppy-disk icon, top-right of the form — no text label), confirm the modal.
- [ ] Confirm the new dish appears in the grid with correct data (code, name, value, checkbox-group selections all showing).
- [ ] **Edit-gating fix check (hardest to reproduce manually — data loads fast in this dev environment):** if you can catch it, try clicking the Edit icon within roughly the first second of a page load/reload, before the checkbox-group data has visibly populated. Expected: the Edit icon should be disabled/inert, or clicking it shows a "still loading" notice, rather than opening an edit form with empty checkboxes.
- [ ] Toggle the row's status switch. Confirm it persists after a page refresh.
- [ ] Edit the row created above. Confirm all fields, the key-ingredient row, and all 3 checkbox-group selections load correctly.
- [ ] Try Save with a required top-level field blank, and separately with all 3 checkboxes of one group unchecked — confirm each is blocked with a visible message.

---

## Known, deliberately-not-fixed items (faithfully reproduced from legacy, not bugs to flag)

- `dishMaster`: the Meal-Preference-checkbox "filter the grid" feature is dead in legacy (fires a real API call but the result is never rendered) — reproduced faithfully, not wired up.
- `dishMaster`: Meal Preference dropdown source is hard-capped at 10 records server-side (legacy's own real limit) — currently invisible with only 1 seed row.
- `dishMaster`: client-side search only searches the currently-loaded page, not the full dataset — legacy's own behavior, faithfully reproduced.
- `referralMaster`: `saveReferral()` sends both `id` and `referralTypeId` set to the same value, per legacy's own unresolved `// need to verify` comment — ported verbatim.
- All 3 screens: `<app-status-toggle>` updates only on confirmed server success (never optimistically) — an already-approved standing improvement over legacy's instant-flip-never-reverts pattern, not a per-screen decision.
- All 3 screens: validation messages render visibly by default — an already-approved standing improvement over legacy's `.help-block{display:none}` CSS bug, not a per-screen decision.

## Seed data currently in place (for your own reference during testing)

- `adt.m_referral_type_master`: 1 active row (id=1, "TestReferralTypeOne") — was inactive, activated this session to unblock testing.
- `kitchen.m_meal_master`: 1 active row (id=17, "Breakfast Updated") — was inactive, activated this session.
- `kitchen.m_diet_type_master`: 1 active row (id=12, code "ABC123") — was inactive, activated this session.
- `kitchen.key_ingredients_master`: 1 active row (id=11) — was inactive, activated this session.
- `adt.m_meal_preference_master`, `kitchen.m_unit_of_measurment_master`: 1 active row each already (junk placeholder content from earlier testing, functional but not realistic — "Test Meal Pref"/"abcd").
- Full country→state→district→city→area chain already present for org 1 (India→Maharashtra→Pune→Pune→Shivaji Nagar).
- Test rows created during this session's own Step 5 verification (referral_id 5-6, camp_id 1, dish_id 5) are left in place, some toggled to Inactive — safe to ignore, edit, or delete as you see fit during your own testing.

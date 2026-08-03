# Batch 24 — Step 6 Domain-Expert Review Checklist

*All 5 screens are through Steps 0-5 on `rebuild/phase4-batch24` (held, not merged, not pushed). This doc consolidates each screen's own Step 3-5 material into one place for a methodical walkthrough — see `docs/ANGULAR_PENDING_FINDINGS.md` for the full audit writeups behind each item below (entries `PENDING-playbook-self-registered-states`, `PENDING-batch24-step2-autoclavemaster-goldentrace`) and `docs/BUSINESS_RULE_MANIFEST.md` / `docs/BR_INDEX.md` for `BR-local-dev-db-drift-productcategory-generalledgerid`.*

**Setup, once, before starting any of the 5:**
- Worktree `.claude/worktrees/batch24`, branch `rebuild/phase4-batch24`. Confirm `ng serve --port 4306` (from `.claude/worktrees/batch24/new`) and the Java backend (port 8080, `his_local` DB) are both running.
- Legacy screens are reached at `http://localhost:8080/#/<state>`; rebuilt screens at `http://localhost:4306/<state>`.
- Login on both: `Admin` / `12345`.
- **Every screen in this batch needed a synthetic module-permission grant patched into `localStorage` on both origins for this dev account** — if you hit "Access Denied," re-add via the browser console (swap the id per screen from the table below): `const p = JSON.parse(localStorage.getItem('permissions')); p.modules.push({id: <id>}); localStorage.setItem('permissions', JSON.stringify(p));` then reload. Module ids: `checklistEquipmentMaster`/`checklistEquipmentMapper` = 526, `autoclaveMaster` = 553, `checklistItem` = 525. `vitalQualifierMaster` has no module-id route guard at all (see its own entry below) — only the sidebar's own `checkAccess(953)`.
- **Unlike batches 22/23, this batch's own test rows were cleaned up after each screen's verification pass rather than left in the dev DB** — 4 of the 5 tables are currently empty; only `checklistItemMaster` retains pre-existing seed data (`CHK1`/`CHK2`, left over from an earlier session's duplicate-check sweep). Each table below is empty unless noted otherwise.
- **One real severity-1 backend bug found and fixed in this batch** (`checklistEquipmentMapper`'s Asset→Product Category cascade, `general_ledger_id` missing from the local dev DB's `m_product_category` table) — already approved and applied, verified live before/after, logged as `BR-local-dev-db-drift-productcategory-generalledgerid`. Nothing further needed from you on this one beyond awareness.
- **No confirm-modal-never-rearms bug in this batch** — 2 of the 5 screens (`checklistEquipmentMaster`, `checklistEquipmentMapper`) have no confirm-modal at all; `checklistItemMaster` likewise has none; `vitalQualifierMaster`'s confirm-modal has a different, NOT-fixed quirk (doesn't auto-close on success — see its entry); `autoclaveMaster`'s confirm-modal (Create only) DOES auto-close correctly. No fix decision needed from you this batch.

---

## 1. `vitalQualifierMaster`

**Paths:** `vitalQualifierMaster` (legacy / rebuild) — module gating is sidebar-only (`checkAccess(953)`, no router resolve), under "Admin" (as "Vital Observations Qualifier Master").

**Test data:** table is currently empty. A Vital Category must exist for the Category dropdown to have any options — none currently exist and no screen in this app can create one (`getVitalCategoryList` has no corresponding save screen anywhere). If the Category dropdown is empty for you, this is expected and matches the real legacy environment's own gap, not a rebuild defect; seed one row directly if you need to exercise Create (`emr.m_vital_category_master`).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | "No Records Found"; Category dropdown may be empty (see note above) |
| 2 | Click Save with all fields blank | **No validation blocks this** — confirm modal opens regardless (both `save()`/`update()`'s field-completeness checks are commented out in legacy's own source) |
| 3 | Fill in Code/Description/Category, click Save | Confirm modal → Ok → success toast → **modal stays open** (see finding below) → close it manually (Cancel or the X) → row appears |
| 4 | Click Edit on a row, change the description, click Update | **No confirm modal on Update** (only Save has one) → success toast → row updates in place |
| 5 | Toggle any row's status | Switch flips; toast confirms |

**Needs your explicit sign-off on:**
- **Faithful reproduction, real legacy gap**: zero client-side validation on both Save and Update — an empty Code/Description/Category can genuinely be submitted on real legacy today.
- **Faithful reproduction, real quirk**: the confirm modal does NOT auto-close after a successful Save — it stays open until you manually dismiss it (Cancel or the header X), even though the save already succeeded and the list refreshed behind it. Distinct from `autoclaveMaster`'s confirm-modal below, which DOES auto-close.
- **Playbook/process note, not a defect, no action needed from you**: this screen's legacy `ui-router` state is registered inline inside its own controller file rather than in the app's central route registry — purely a research-methodology finding (logged so future screen selection doesn't wrongly skip screens wired this way), no user-visible effect.

---

## 2. `checklistEquipmentMaster`

**Paths:** `checklistEquipmentMaster` (legacy / rebuild), module id 526, under "Asset Management".

**Test data:** table is currently empty — create a row to begin.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | "No Records Found" |
| 2 | Click Save with Code/Name blank | Real client-side validation blocks it — growl warnings, no network call |
| 3 | Create `CEQ1`/"Sterilizer Tray" | **No confirm modal** (this screen has none) → success toast → row appears |
| 4 | Click Edit on `CEQ1`, change the name, click the SAME Save button (label reads "Update" now, but it's the same function) | Success toast → the SAME row updates in place, not a duplicate (a real, single-endpoint upsert — confirmed via the DAO directly, there is no separate update call at all) |
| 5 | Toggle `CEQ1`'s status | Switch flips instantly via its own two-way binding; check Network tab — only the status call fires, no follow-up list reload (this is expected, not a bug — the checkbox's own binding keeps the display correct regardless) |
| 6 | Use the Search box | Client-side filter |

**Needs your explicit sign-off on:**
- **No action needed** — fully functional, no known findings. Real server-side duplicate-check (self-excluding on update, confirmed via DAO read) covers both Code and Name.
- **Faithful reproduction**: no confirm-modal of any kind on this screen (matches this project's `machineParameterMaster` precedent from batch 23).
- **Faithful reproduction**: a decorative "Add Checklist Component" modal exists in the page's markup but its only trigger is commented out in legacy — genuinely unreachable, not modeled in the rebuild.

---

## 3. `checklistEquipmentMapper`

**Paths:** `checklistEquipmentMapper` (legacy / rebuild), module id 526 (shared with the screen above), under "Asset Management".

**Test data:** table is currently empty — create a row to begin. Requires at least one Active row on the `checklistEquipmentMaster` screen above (for the Checklist Name dropdown) and real Asset/Product Category data (already present in this dev DB).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | "No Records Found" |
| 2 | Click Save with everything blank | 5-step sequential validation fires in order (Checklist Name → Asset Category → Product Category → Item Code → Item Name), one growl warning at a time |
| 3 | Select an Asset Category | Product Category dropdown populates via a real cascade call (this is the endpoint that was 500'ing before the DB fix — should now return real options) |
| 4 | Fill in the remaining fields (Item Code/Item Name are free text, not dropdowns) and Save | No confirm modal → success toast → row appears |
| 5 | Edit the row, change Item Name, Save again (same button/function) | Same row updates in place, not a duplicate |
| 6 | Toggle the row's status | Switch flips via its own binding; no list reload fires, matching the sibling screen above |
| 7 | Change the page-size dropdown / page through results | This screen paginates a fully-fetched in-browser list (a different mechanism than every other screen in this project), not server-side paging — should feel instant, no network call on page-change |

**Needs your explicit sign-off on:**
- **Already fixed, awareness only**: the Asset→Product Category cascade previously 500'd on every call in this dev environment due to a missing DB column (`general_ledger_id`) on `m_product_category`. Fixed via an additive `ALTER TABLE` (`BR-local-dev-db-drift-productcategory-generalledgerid`), verified live before/after. Local dev DB only, no application code changed for the fix itself.
- **No action needed** — otherwise fully functional, no confirm-modal (by design, matches legacy), real 5-field client-side validation.
- **Correction, informational only**: an earlier task note had mis-attributed a "two dropdowns share one model" bug to this screen; direct verification confirmed that finding belongs to a completely different pair of files that happen to reuse a similarly-named variable. Does not apply here — this screen's 3 dropdowns are independently bound.

---

## 4. `autoclaveMaster`

**Paths:** `autoclaveMaster` (legacy / rebuild), module id 553, under "CSSD".

**Test data:** table is currently empty. **The Sterilization Color dropdown depends on `m_sterilization_color_indicator`, which is completely empty in this dev DB** (a genuinely global, unscoped reference table with no seed data at all yet, not a schema defect) — you will need at least one row in that table to complete a Create; seed one directly if needed (e.g. code `BLK` / name `Black`) and treat this as a data-gap, not something to report as a new bug.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | "No Records Found" |
| 2 | Click Save with fields blank | Real Angular-style validation blocks it — per-field error text, no network call |
| 3 | Fill in all 7 fields (Machine Name, Description, Sterilization Type, Sterilization Color, Temperature, Pressure, Biological Indicator Day — note the `℃` unit label) and Save | Confirm modal → Ok → toast reads **"MasterClave added Successfully...!!"** (not "AutoClave" — see finding below) → modal auto-closes → row appears |
| 4 | Create a SECOND row using the exact same Machine Name as the first | **Also succeeds** — no duplicate-check exists on this screen at all (already-known gap, not new) |
| 5 | Click Edit on a row — **observe the Save button** | The button stays labeled "Save" (does NOT change to "Update," and no separate Update button appears anywhere) — this is the finding below, not a rendering bug on the rebuild's part |
| 6 | Change a field and click that same "Save"-labeled button | **No confirm modal this time** (Update has none) → toast reads correctly, **"AutoClave updated Successfully...!!"** → the SAME row updates in place — confirming the button silently does the right thing despite its label |
| 7 | Toggle any row's status | Switch flips; toast fires either way; no revert-on-failure and no list reload (matches legacy exactly) |

**Needs your explicit sign-off on:**
- **Faithful reproduction, real legacy bug**: the Save button never visually switches to "Update" while editing, and no dedicated Update button ever appears — purely cosmetic/confusing, since the underlying function correctly detects edit mode and calls the right API regardless (verified with a real PUT round-trip).
- **Faithful reproduction, cosmetic**: the Create success toast has a copy-paste typo, "MasterClave" instead of "AutoClave" — Update's own toast is correctly worded.
- **Already known, re-confirmed live, not new**: no duplicate-check of any kind (tracked separately in `docs/DECISION_NEEDED_DuplicateCheckSweepTier3Findings.md`, awaiting your decision on that sweep as a whole — not specific to this screen).
- **Data-gap awareness only, not a bug**: the Sterilization Color reference table is empty in this dev environment; whoever owns seeding real reference data for CSSD should populate it — no code or schema issue.

---

## 5. `checklistItemMaster` — ✅ UI-LEVEL SIGN-OFF RECORDED 2026-08-03 (owner)

**Owner confirmed through the actual UI** (`http://localhost:4310/checklistItem`, module 525): screen loads with the 3 org-1 rows (CHK1/CHK2/CI1); org-999 victim `VICTIM-CI-999` absent (BR-149's cross-tenant fix holds at the UI layer); edit/save/status-toggle all work cleanly on same-org rows. Backend fix merged+pushed as BR-149 (`69500dc1`); findings-doc entry appended.

---

## 5. `checklistItemMaster`

**Paths:** legacy state is `checklistItem` (not `checklistItemMaster` — confirmed directly in the route config; the rebuild's URL matches this exactly), module id 525, under "OT/Theatre" (as "Checklist Item").

**Test data already in the dev DB:** `CHK1`/"Test Checklist Item One" (Active), `CHK2`/"Second Distinct Item" (Active) — left over from an earlier session's duplicate-check sweep, not created for this batch.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | 2 rows show (`CHK1`, `CHK2`) |
| 2 | Click Save with Code/Description blank | Real client-side validation blocks it — growl warnings, no network call |
| 3 | Create `CI1`/"Rebuild Test Item" | No confirm modal (this screen has none at all) → success toast → row appears |
| 4 | Try creating another row with Code `CHK1` (or Description "Test Checklist Item One") | **Rejected** — a real, correctly-built self-excluding duplicate-check covers both Code and Description on Create |
| 5 | Click Edit on `CI1`, change the description, click the SAME Save button (same upsert shape as `checklistEquipmentMaster`) | Success toast → the SAME row updates in place, not a duplicate; try reusing `CHK2`'s code/description on this Update too — also correctly rejected (the update-path check excludes only the row being edited, not every other row) |
| 6 | Toggle any row's status | Switch flips, success toast, **and the table visibly re-fetches from the server** — check the Network tab: a fresh `getChecklistItemMasterList` call fires right after the status call resolves. This is the ONE screen in this batch where toggling triggers a real reload, unlike the other 4 |
| 7 | Use the Search box | Client-side filter |

**Needs your explicit sign-off on:**
- **No action needed** — fully functional, no known findings. This screen's duplicate-check was independently confirmed as one of the best-built in the project's earlier systemic sweep; re-confirmed here via direct DAO read (4 distinct named queries, correctly gated by create-vs-update).
- **Modeling note, not a bug**: two view files exist on disk for this legacy screen (`checklist_master.html`, underscore — the real one) and an orphaned, unused `checklist-item-master.html` (hyphen) — confirmed by reading the actual route config, not guessed from filenames. The rebuild's route path deliberately matches the real legacy state name (`checklistItem`), not the more "expected" `checklistItemMaster`.
- **Faithful reproduction**: status-toggle triggers a genuine list reload here, unlike this batch's other 4 screens, which rely purely on the checkbox's own optimistic binding.

---

## Summary for your review

| Screen | Confirm-modal | Duplicate-check | Notable rebuild-only fix | Legacy quirks/bugs faithfully reproduced |
|---|---|---|---|---|
| vitalQualifierMaster | Present on Save only; does NOT auto-close on success | Not evaluated (out of scope this batch) | None | Zero client-side validation on Save/Update; confirm modal never auto-dismisses; self-registered route (process note only) |
| checklistEquipmentMaster | N/A — none exists | Real, self-excluding, server-side (Code + Name) | None | Single-endpoint upsert (one form/button/function); status toggle has no reload (harmless, optimistic binding covers it); dead decorative modal in markup |
| checklistEquipmentMapper | N/A — none exists | Not evaluated (not this screen's audited concern) | **SEVERITY-1 DB fix**: missing `general_ledger_id` column blocking the Product Category cascade — fixed & verified | Single-endpoint upsert; custom client-side pagination (different mechanism than every sibling screen); status toggle has no reload |
| autoclaveMaster | Create only; DOES auto-close on success | **None at all** — already-known gap, awaiting your Tier-3 decision | None | Save button never relabels to "Update" (functionally harmless); "MasterClave" toast typo on Create only; no revert-on-failure or reload after status toggle |
| checklistItemMaster | N/A — none exists | Real, self-excluding, server-side (Code + Description, both Create AND Update) | None | Single-endpoint upsert; status toggle DOES trigger a real list reload (the one exception in this batch) |

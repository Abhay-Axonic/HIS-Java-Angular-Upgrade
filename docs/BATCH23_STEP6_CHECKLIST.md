# Batch 23 — Step 6 Domain-Expert Review Checklist

*All 5 screens are through Steps 0-5 on `rebuild/phase4-batch23` (held, not merged, not pushed). This doc consolidates each screen's own Step 3-5 material into one place for a methodical walkthrough — see `docs/ANGULAR_PENDING_FINDINGS.md` for the full audit writeups behind each item below (entries `PENDING-batch23-step2-bloodbagtype-goldentrace`, `PENDING-batch23-step2-otimsrolecreation-goldentrace`, `PENDING-batch23-step2-machineparametermaster-goldentrace-SEVERITY1`, `PENDING-batch23-step2-storageunitmaster-goldentrace`, `PENDING-batch23-step2-mealmaster-goldentrace-SEVERITY1`).*

**Setup, once, before starting any of the 5:**
- Worktree `.claude/worktrees/batch23`, branch `rebuild/phase4-batch23`. Confirm `ng serve --port 4303 --proxy-config proxy.conf.json` (from `.claude/worktrees/batch23/new`) and the Java backend (port 8080, `his_local` DB) are both running.
- Legacy screens are reached at `http://localhost:8080/#/<state>`; rebuilt screens at `http://localhost:4303/<state>`.
- Login on both: `Admin` / `12345`.
- **Two of this batch's screens require module grants Admin's account doesn't have by default in this dev environment** (`machineParameterMaster`, module 839; `storageunitMaster`'s module 470 and `mealMaster`'s module 512 are already granted): if you hit "Access Denied," the synthetic permission entry was added via `localStorage` on both origins for this session's testing — if it's gone (e.g. a fresh browser profile), re-add it via the browser console: `const p = JSON.parse(localStorage.getItem('permissions')); p.modules.push({id: 839}); localStorage.setItem('permissions', JSON.stringify(p));` then reload.
- **`KITmasterPharmacy` was swapped out of this batch mid-flight**: it turned out to be a 2-screen, ~640-line, multi-entity inventory-item-selection workflow (kit type dropdown, item-search modal, per-item quantity validation, 5+ backend APIs) — not the simple lookup-master shape this batch was selected around. Replaced with `storageunitMaster` (Pharmacy), independently re-verified against the same selection criteria.
- **Two severity-1 findings in this batch, both already flagged to you individually as they were found, both faithfully reproduced (not fixed) per standing policy:**
  - `machineParameterMaster`'s Update is **completely non-functional** on real legacy — a wrong Dozer mapping ID in the backend crashes every update attempt.
  - `mealMaster`'s deactivating a row **permanently hides it** from the list — the list query is active-only with no path back.
- **Confirm-modal never-rearms bug, present on 4 of 5 screens** (all but `storageunitMaster`, which has no such flag at all — see its own entry) — **FIXED** across all 5, per your decision extending batch 22's fix to this batch too.

---

## 1. `machineParameterMaster` — recommended first, given severity

**Paths:** `machineParameterMaster` (legacy / rebuild), module id 839 (Lab/LIS — new Shell heading, first LIS screen in this project).

**Test data already in the dev DB:** `MP1`/"Glucose Level"/"Analyzer One" (Inactive), `MP2`/"Cholesterol Level"/"Analyzer One" (Active). One Machine seeded via the sibling Machine Master screen to unblock the Machine dropdown: `MC1`/"Analyzer One" (Active).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | 2 rows show; Code/Machine Description/Machine columns |
| 2 | Click Save with all 3 fields blank | Inline red required-field messages under each field, no network request fires |
| 3 | Create `MP3`/"Sodium Level", Machine = "Analyzer One" | No confirm modal (this screen has none at all — see below) → real success toast "machine Parameter save successfully" → row appears |
| 4 | Click Edit on `MP1` (Inactive), change nothing, click Update | **Fails every time** with toast "Failed to add Machine." — a real backend bug, not a rebuild defect (see severity-1 note below) |
| 5 | Toggle `MP2`'s status | Switch flips; check Network tab — only the status call fires, no follow-up list/count re-fetch |
| 6 | Use the Search box | Client-side filter over the current page's rows |

**Needs your explicit sign-off on:**
- **SEVERITY-1, real legacy bug, faithfully reproduced, not fixed** (step 4): Update always fails. Root cause confirmed at the source: the backend's Dozer object-mapper is configured with the WRONG DTO class for this screen's update path (it's actually configured for the sibling "Machine Master" screen's DTO), so every update throws an exception that gets shown to the user as a misleading "Failed to add Machine." message. No database write ever happens on Update. This is a genuine, pre-existing backend defect — the rebuild reproduces it exactly because it calls the same real, broken endpoint.
- **Faithful reproduction**: Save has zero duplicate-check at any layer (you can create as many rows with the same code/description as you like).
- **No confirm-modal on this screen at all** (unique among this batch) — legacy's own view uses a plain always-fires Save with no `popUpFlag`/`showModal` gate. Nothing to fix here.
- **Faithful reproduction**: status toggle does not refresh the table (matches legacy).

---

## 2. `mealMaster`

**Paths:** `mealMaster` (legacy / rebuild), module id 512 (Diet & Nutrition).

**Test data already in the dev DB:** `MM1`/"Breakfast Updated"/08:00 — **currently Inactive, and per the finding below, invisible in the list** (only recoverable via direct DB query, not through this screen's UI).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | "No Records Found" (MM1 is hidden — see below) |
| 2 | Create `MM2`/"Lunch"/12:30 | Confirm modal → Ok → real success toast "Meal added Successfully...!!" → row appears |
| 3 | Click Edit on `MM2`, change the name, click Update | **Confirm modal appears again** (Update shares the same confirm gate as Save on this screen only) → Ok → toast **"Diet updated Successfully...!!"** (not "Meal" — a real, intentional-looking copy-paste text quirk, faithfully reproduced) |
| 4 | Toggle `MM2`'s status to Inactive, then reload the page | `MM2` **disappears from the list entirely** — this is the severity-1 finding, not a rebuild bug |
| 5 | Leave Code or Meal Name blank, click Save | Inline red required-field messages; Serving Time has no `*` and is not required |

**Needs your explicit sign-off on:**
- **SEVERITY-1, real legacy bug, faithfully reproduced, not fixed** (step 4): the backend's paginated-list query is hardcoded to active rows only, with no equivalent "show inactive" toggle anywhere in this screen. Once you deactivate a meal, it is permanently gone from this screen's own UI — the only way back is a direct database change outside the application.
- **Fixed here (owner decision, extends batch 22)**: confirm-modal now rearms on every outcome, for BOTH Save and Update (this is the only screen this batch where Update shares Save's confirm gate at all — faithfully reproduced that sharing, fixed the never-rearms part of it).
- **Faithful reproduction**: duplicate-check error/success messages say "Diet" instead of "Meal" on the Update path specifically (Create's say "Meal" correctly) — a real, pre-existing inconsistency in legacy's own text, not something the rebuild introduced or corrected.
- **Modeling note, not a bug**: Serving Time uses a plain native time picker here instead of legacy's jQuery clock-picker widget — same choice already made for `shiftMaster` (batch 22); the underlying data format is identical (24-hour `HH:mm`).

---

## 3. `bloodBagType`

**Paths:** `bloodBagType` (legacy / rebuild), module id 731 (Blood Bank).

**Test data already in the dev DB:** `BBT1`/"Bag Type One" (Inactive), `BBT3`/"Bag Type Three" (Active). A `BBT2`/`BBT4` duplicate-attempt (same description as `BBT1`) was correctly rejected on both apps, not saved.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | 2 rows show |
| 2 | Create `BBT5`/"Bag Type Five"/"Item Five"/500/100 | Confirm modal → Ok → success toast → row appears |
| 3 | Immediately create a second row with the SAME description as `BBT5` but a different code | **No confirm modal this time** (rearms every attempt, but this is a genuinely new save so it should still show one — see note) → rejected with "Already Exist" either way, since the description collides |
| 4 | Leave Capacity Max blank, click Save | Toast "Please Enter  Max Capacity " (note the **double space** — faithful to a real byte-level quirk in legacy's own text) |
| 5 | Edit `BBT3`, change Capacity Max, click Update | No confirm modal (Update has no gate here), leave Capacity Max blank this time → toast "Please Enter Max Capacity " (**single space** — a different, real string from Save's) |
| 6 | Toggle any row's status | Switch flips; check Network tab — a fresh list/count re-fetch should fire |
| 7 | Use the Search box | Client-side filter |

**Needs your explicit sign-off on:**
- **Fixed here (owner decision, extends batch 22)**: confirm-modal now rearms on every Save outcome.
- **Faithful reproduction, real legacy bug**: duplicate-check compares description only (case-insensitive), never checks code, and is NOT org/unit-scoped — confirmed at the source level in the entity's own query definitions.
- **Faithful reproduction**: the one-space difference in the "Max Capacity" validation message between Save and Update is a real, confirmed byte-level quirk in legacy's own source, not a rebuild typo.
- **Faithful reproduction**: status toggle DOES reload the list (unlike some sibling screens in this project).

---

## 4. `OTIMSroleCreation`

**Paths:** `OTIMSroleCreation` (legacy / rebuild) — no moduleId gate (legacy has none), under "User Management".

**Test data already in the dev DB:** `Surgeon` (Active), `OT Manager` (Inactive).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | 2 rows show; OTIMS Role Name is a fixed 4-option dropdown (OT Manager/Surgeon/admin/Anaesthetist), not free text |
| 2 | Click Save with the dropdown left on its placeholder | **No validation blocks this** — confirm modal still appears, and if confirmed, a role literally named "Select OTIMS User Role" could be saved (a real legacy gap, not something the rebuild adds or removes) |
| 3 | Select "admin", click Save | Confirm modal → Ok → success toast → row appears |
| 4 | Click Edit on `Surgeon`, leave the dropdown unchanged, click Update | **Always rejected as "Already Exists."** — a real, severe legacy bug (the update duplicate-check doesn't exclude the row's own ID), not a rebuild defect |
| 5 | Click Edit on `Surgeon`, change the dropdown to a genuinely unused role (if one remains), click Update | Succeeds — proves the bug is specifically about updating without changing the name, not Update in general |
| 6 | Toggle any row's status | Switch flips immediately; check Network tab — only the status call fires, no list reload |

**Needs your explicit sign-off on:**
- **Fixed here (owner decision, extends batch 22)**: confirm-modal now rearms on every Save outcome.
- **Faithful reproduction, real severe legacy bug** (step 4): Update's duplicate-check reuses the exact same query as Create, with no exclusion for the row's own ID — so any update that doesn't change the role name always fails.
- **Faithful reproduction**: zero client-side validation of any kind — confirmed by reading the full controller.
- **Risk-tag correction, informational only**: this screen was tagged "clinical" in the project's own audit data purely from an "otims" keyword match in its name — confirmed by reading the entity that it's a plain admin role-label lookup with no patient data. No action needed, just flagging so the record is accurate.

---

## 5. `storageunitMaster` — replaces `KITmasterPharmacy`

**Paths:** `storageunitMaster` (legacy / rebuild), module id 470 (Pharmacy).

**Test data already in the dev DB:** `SU1`/"Shelf A Updated" (Inactive), `SU2`/"Shelf B Updated" (Inactive).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | 2 rows show, both Inactive |
| 2 | Click Save with both fields blank | **The confirm modal still opens** (the Save button's "disabled" look is purely visual) → Ok → inline red required-field messages appear only after confirming, no network call |
| 3 | Create `SU3`/"Shelf C" | Confirm modal → Ok → real success toast "Storage Unit Inserted Successfully" → row appears, Active |
| 4 | Create a second row using `SU3` again as the code | Rejected client-side with "Duplicate code" — a real gap (the backend itself does not independently enforce this), faithfully reproduced |
| 5 | Click Edit on `SU3`, change the name, click the SAME Save button (never relabeled "Update") | Confirm modal → Ok → success toast "Storage Unit Updated Successfully" |
| 6 | Toggle `SU3` to Inactive | Switch flips, "Status changed successfully" toast, **and the edit form clears if it was open** — a real, faithfully-reproduced quirk |

**Needs your explicit sign-off on:**
- **No confirm-modal fix needed on this screen** — legacy uses a native browser modal with no `popUpFlag` state at all, so it was never subject to the never-rearms bug in the first place; the modal genuinely fires on every click already.
- **Faithful reproduction**: duplicate-check is 100% client-side (the backend doesn't independently enforce code uniqueness).
- **Faithful reproduction, real backend business rule**: a Storage Unit currently assigned to at least one inventory Item cannot be deactivated — the backend reverts the change and returns an error, which the frontend shows and reverts the toggle for; any OTHER kind of error does NOT revert the toggle, which looks like an oversight in legacy's own code, faithfully reproduced rather than corrected. Not independently tested this pass (would require seeding an Item reference) — flagging for awareness.
- **Modeling note, not a bug**: this screen's backend is a newer, different code style than every other screen in this project (a modern REST API, not the older response envelope) — purely an internal implementation detail, no visible difference to you.

---

## Summary for your review

| Screen | Confirm-modal fix | Other rebuild-only fixes | Legacy bugs faithfully reproduced |
|---|---|---|---|
| machineParameterMaster | N/A — no confirm-modal exists on this screen | None | **SEVERITY-1: Update always fails** (wrong backend object-mapping); Save has no duplicate-check at all |
| mealMaster | Fixed (rearms on every outcome, shared by Save AND Update) | None | **SEVERITY-1: deactivating a row permanently hides it**, no way back through the UI; "Diet"-vs-"Meal" message inconsistency on Update |
| bloodBagType | Fixed (rearms on every outcome) | None | Duplicate-check is description-only, code-blind, not org/unit-scoped; one-space validation-text difference between Save/Update |
| OTIMSroleCreation | Fixed (rearms on every outcome) | None | Update's duplicate-check doesn't exclude its own row (near-total Update blocker unless renaming to something unused); zero client-side validation |
| storageunitMaster | N/A — no confirm-modal state exists, fires unconditionally already | None | Duplicate-check is 100% client-side; item-assignment deactivation guard with an inconsistent revert-on-error quirk |

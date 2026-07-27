# Batch 22 — Step 6 Domain-Expert Review Checklist

*Nursing/Diet/Billing/Blood-Bank/OT-tier batch. All 5 screens are through Steps 0-5 on `rebuild/phase4-batch22` (held, not merged, not pushed). This doc consolidates each screen's own Step 3-5 material into one place for a methodical walkthrough — see `docs/ANGULAR_PENDING_FINDINGS.md` (entries `PENDING-batch22-step2-shiftmaster-dietunitmater-goldentrace`, `PENDING-batch22-step2-apmaster-goldentrace`, `PENDING-batch22-step2-bloodbankmaster-goldentrace`, `PENDING-batch22-step2-operationtheatretype-goldentrace`, `PENDING-batch22-confirmmodal-fix-decision-and-verifications`) for the full audit writeups behind each item below.*

**Setup, once, before starting any of the 5:**
- Worktree `.claude/worktrees/batch22`, branch `rebuild/phase4-batch22`. Confirm `ng serve --port 4302 --proxy-config proxy.conf.json` (from `.claude/worktrees/batch22/new`) and the Java backend (port 8080, `his_local` DB) are both running.
- Legacy screens are reached at `http://localhost:8080/#/<state>`; rebuilt screens at `http://localhost:4302/<state>`.
- Login on both: `Admin` / `12345`.
- All 5 screens' underlying tables are currently **empty** in the dev DB — every test below starts from a clean slate. Suggested codes are given per screen; feel free to substitute.
- **Shared finding across all 5, needs your sign-off on each screen below**: legacy's `popUpFlag` gate on `save()` never rearms after ANY outcome (success or failure) — a more severe variant than the previously-known cardTypeMaster/identificationMaster pattern (which only sticks after failure). Once the confirm modal has fired once, subsequent Saves on that page load silently skip the modal forever. Fixed identically on all 5 screens via the `card-type-master.ts` template's `popupArmed` signal pattern (rearmed in `resetForm()` on success, and explicitly in the error branch of `doSave()` on failure). **This is a disclosed rebuild-only fix, not faithful reproduction.**
- Two legacy backend fixes are baked into this batch's baseline (both merged to `main`, held/not pushed to origin — independent approval needed to push, not part of this Step 6 review):
  - `fix/instantconverter-utc-systemdefault-asymmetry` — corrects a UTC/systemDefault timezone asymmetry on all `Instant`-typed audit columns project-wide (47 entities). Verified live via a real-UI, midnight-crossing timestamp test during this batch's builds, not just at the API level.
  - `fix/operationtheatretype-name-check` — operationTheatreType's duplicate-check now compares both name and description (see screen 5 below).

---

## 1. `shiftMaster`

**Paths:** `shiftMaster` (legacy / rebuild) — module id 408, under the "Nursing" nav heading.

**Test data:** dev DB is currently empty for this screen. Suggested: `SM1`, from `09:00` to `17:00`.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Empty list, "No Records Found" |
| 2 | Create `SM1`, `09:00`–`17:00` | Confirm modal appears → Ok → success toast → row appears, times display as `09:00`/`17:00` with **no seconds** |
| 3 | Leave From/To Time blank, click Save | Save is blocked by field validation, no network request fires |
| 4 | Edit `SM1`, change To Time to `18:00`, click Update | **No confirm modal on Update** (legacy has no modal gate on the update path — faithfully reproduced), success toast, row updates |
| 5 | Toggle `SM1`'s status | Switch flips; check the Network tab — a fresh `getShiftMasterList` call should fire (legacy's own status toggle does reload the list) |
| 6 | Reload the page | All changes from steps 2/4/5 persisted |

**Needs your explicit sign-off on:**
- **Rebuild-only fix**: confirm-modal now rearms after every Save outcome (see shared finding above).
- **Faithful reproduction**: Update has no confirm-modal gate at all — matches legacy.
- **Modeling note, not a bug**: From/To Time are plain `HH:mm` strings (`<input type="time">`, default step, no seconds) — matches legacy's own field precision.
- **Faithful reproduction**: status toggle reloads the full list afterward (unlike operationTheatreType below, which does not — see item 5).

---

## 2. `dietUnitMater`

**Paths:** `dietUnitMater` (legacy / rebuild) — **no `moduleId`** on this route (legacy has no `resolve` block, so the rebuild matches it), under the "Diet & Nutrition" nav heading.

**Test data:** dev DB is currently empty for this screen. Suggested: `DU1` / `Kg`.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Empty list, "No Records Found" |
| 2 | Create `DU1` / `Kg` | Confirm modal → Ok → success toast → row appears |
| 3 | Create a second row `DU2` / `kg` (same name, different case) | **Rejected as a duplicate** — case-insensitive duplicate-check now correctly catches this |
| 4 | Leave Unit Name blank, click Save | Save blocked by field validation |
| 5 | Edit `DU1` **without changing the Unit Name** (leave it `Kg`), click Update | **Update fails / is rejected as a duplicate of itself** — this is a real, still-present legacy bug (the duplicate-check doesn't exclude the row's own ID), faithfully reproduced, not a rebuild defect |
| 6 | With more than one page's worth of rows created, check the list | **No pagination controls appear at all** — this is faithful to legacy, which has zero pagination logic (no `OFFSET`/`LIMIT` server-side, no `limitTo`/`startFrom` client-side), not a missing/broken feature |
| 7 | Use the Search box to filter by keyword | Client-side filtering works, matching legacy's `filter:searchKeyword` |

**Needs your explicit sign-off on:**
- **Rebuild-only fix**: confirm-modal rearms after every Save outcome (shared finding).
- **Correction to a prior audit finding, verified live this session**: the case-insensitive duplicate-check (step 3) was previously documented as "still broken" based on an earlier Step 1 audit. Live-retesting during this Step 3 build proved that wrong — it was already fixed as an **unplanned side effect** of the unrelated `fix/dietunitmater-sql-injection-parameter-binding` fix (which parameter-binds and lowercases the compared value). `docs/ANGULAR_PENDING_FINDINGS.md` has been corrected accordingly.
- **Legacy bug, NOT fixed, faithfully reproduced** (step 5): the self-collision bug on Update.
- **Rebuild design decision, not a bug**: no pager UI was built at all (step 6), since legacy genuinely has none — a decorative-but-non-functional pager would have been the actual "fake" behavior here.

---

## 3. `APmaster`

**Paths:** `APmaster` (legacy / rebuild) — module id 617, under the "Billing" nav heading.

**Test data:** dev DB is currently empty for this screen. Suggested: `AP1`…`AP12` (need >10 rows total to force pagination to a second page).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Empty list, "No Records Found" |
| 2 | Create `AP1` / "Code One" | Confirm modal → Ok → success toast → row appears |
| 3 | Leave fields blank, click Save | Save blocked by field validation |
| 4 | Create 11 more rows (`AP2`…`AP12`) so the total exceeds one page | Pager UI appears, page numbers and total count look correct |
| 5 | Click page 2 | **Rows shown are identical to page 1** — this is a faithful reproduction of a real legacy bug, not a rebuild defect (see below) |
| 6 | Toggle any row's status | Switch flips, success toast |

**Needs your explicit sign-off on:**
- **Rebuild-only fix**: confirm-modal rearms after every Save outcome (shared finding).
- **Legacy bug, NOT fixed, faithfully reproduced** (step 5): `loadList()` always fetches with a hardcoded `offset=0` instead of `(currentPage-1)*pageSize`, while the pager UI and total-count display still compute correctly from the real count — so the pager *looks* fully functional but never actually changes which rows are fetched. Live-verified this session by seeding 12 rows via `curl` and confirming page 2 shows the same rows as page 1.
- **Modeling note, not a bug**: `aPCodeId` is typed as an optional string to match legacy's implicit `.toString()` coercion when populating the edit form.

---

## 4. `bloodBankMaster`

**Paths:** `bloodBankMaster` (legacy / rebuild) — module id 742, under the "Blood Bank" nav heading.

**Test data:** dev DB is currently empty for this screen. Suggested: `BB1` / "Bank One" / "Address One" / "1234567890".

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Empty list, "No Records Found" |
| 2 | Click Save with all 4 fields blank, then fill them in **one at a time**, clicking Save after each | Toasts fire in this exact order as each subsequent field is still blank: `Please Enter Code `, `Please Enter Description `, `Please Enter Address `, `Please Enter Contact No ` — matches legacy's exact growl text and check order (ctrl:139-166/266-293) |
| 3 | Fill all 4 fields (`BB1` / "Bank One" / "Address One" / "1234567890"), click Save | Confirm modal → Ok → success toast → row appears |
| 4 | Edit `BB1`, change the address, click Update | Success toast, row updates |
| 5 | Toggle `BB1`'s status | Switch flips, success toast |

**Needs your explicit sign-off on:**
- **Rebuild-only fix**: confirm-modal rearms after every Save outcome (shared finding).
- **Fixed per your explicit approval** (silent/invisible-bug correction exception): organizationId/unitId are no longer hardcoded to `1`/`1` — the constructor now reads `this.session.organizationId ?? 0` / `this.session.unitId ?? 0`. **Note for your sign-off**: this dev environment's real session value also happens to be org=1/unit=1, so the database result alone can't distinguish "reads from session" from "still hardcoded and coincidentally matching." The fix was confirmed at the **source-code level** (the literal line above, quoted directly from the file), not just from DB output, per your specific request this session.
- **Faithful reproduction**: the exact growl message text and field-check order in step 2.
- **Dev-infrastructure fix, not a behavior change, flagging for awareness only**: `/bloodBank/**` was entirely missing from `proxy.conf.json`/`proxy.conf.example.json`, causing every save to 404 in this dev environment only (not a real-deployment issue). Added to both files; required a dev-server restart to take effect.

---

## 5. `operationTheatreType`

**Paths:** `operationTheatreType` (legacy / rebuild) — module id 521, under the "OT/Theatre" nav heading.

**Test data:** dev DB is currently empty for this screen. Suggested: `OT1` / "General" / "General theatre".

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Empty list, "No Records Found" |
| 2 | Create `OT1` / "General" / "General theatre" | Confirm modal → Ok → success toast → row appears |
| 3 | Create `OT2` with the **same Name** ("General") but a different Code/Description | **Rejected as a duplicate** — confirms this screen was built and re-verified against the now-merged `fix/operationtheatretype-name-check` fix (duplicate-check now compares both Name and Description via OR), not the original pre-fix behavior |
| 4 | Edit `OT1`, clear the Description field entirely, click Update | **Update still submits successfully with an empty description** — a real legacy bug, faithfully reproduced: `update()` has no validation gate at all (legacy's own `$valid` check is commented out, ctrl:261,283). Live-verified this session: DB shows an empty `theatre_type_desc` after this step |
| 5 | Toggle `OT1`'s status | Switch flips **immediately** (optimistic local update, before the API call resolves); check the Network tab — **only** `updateOperationTheatreTypeStatus` should fire, with **no** follow-up list/count re-fetch |

**Needs your explicit sign-off on:**
- **Rebuild-only fix**: confirm-modal rearms after every Save outcome on `save()` only (shared finding) — `update()` has no confirm-modal gate at all, matching legacy.
- **Built against the merged duplicate-check fix** (step 3): `fix/operationtheatretype-name-check` is merged to `main` but **held, not pushed to origin** — pushing needs its own separate approval, independent of this Step 6 review.
- **Legacy bug, NOT fixed, faithfully reproduced** (step 4): Update's missing validation gate.
- **Legacy bug, NOT fixed, faithfully reproduced** (step 5): status toggle's optimistic, non-reloading behavior — matches legacy's own two-way-bound checkbox, which flips regardless of the response and never re-fetches the list.

---

## Summary for your review

| Screen | Confirm-modal fix | Other rebuild-only fixes | Legacy bugs faithfully reproduced |
|---|---|---|---|
| shiftMaster | Fixed (rearms on every outcome) | None | Update has no confirm-modal gate |
| dietUnitMater | Fixed (rearms on every outcome) | None (case-insensitivity fix was an unplanned side effect of the SQL-injection fix, not a targeted change) | Update self-collision bug; no pagination logic (by design, matches legacy) |
| APmaster | Fixed (rearms on every outcome) | None | Pagination always fetches offset=0 despite a fully-working pager UI/count |
| bloodBankMaster | Fixed (rearms on every outcome) | Org/unit hardcoding removed, now reads from session (owner-approved, source-verified) | None outstanding |
| operationTheatreType | Fixed (rearms on every outcome, `save()` only) | Built against merged duplicate-check fix (held, unpushed) | Update has no validation gate; status toggle is optimistic/non-reloading |

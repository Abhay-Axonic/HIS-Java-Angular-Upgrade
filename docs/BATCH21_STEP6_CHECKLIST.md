# Batch 21 — Step 6 Domain-Expert Review Checklist

*All 5 screens are through Steps 0-5 on `rebuild/phase4-batch21` (held, not merged, not pushed). This doc consolidates each screen's own Step 1-5 material into one place for a methodical walkthrough — see `docs/ANGULAR_PENDING_FINDINGS.md` for the full audit writeups (batch 20/21 Step 1 entries, `PENDING-genericScheduleMaster-step1-findings`, `PENDING-growthChartMaster-step1-findings`, `PENDING-diettypemaster-scope-question`, `PENDING-batch21-step3-diettypemaster-goldentrace-SEVERITY1`).*

*Note on this doc's timing: 4 of these 5 screens (`wrapperExpiryMaster`, `occupancyUnitMaster`, `genericScheduleMaster`, `growthChartMaster`) were built in an earlier session (2026-07-26/27). `dietTypeMaster`'s Steps 0-2 were also completed then, but Steps 3-5 (the actual rebuild) had gone unbuilt until 2026-07-28, when the gap was discovered and closed out per owner instruction. This is the first consolidated Step 6 checklist for the batch — none was delivered earlier, unlike batches 22/23/24.*

**Setup, once, before starting any of the 5:**
- Worktree `.claude/worktrees/step6-batch21`, branch `rebuild/phase4-batch21`. Confirm `ng serve --port 4309 --proxy-config proxy.conf.json` (from `.claude/worktrees/step6-batch21/new`) and the Java backend (port 8080, `his_local` DB) are both running.
- Legacy screens are reached at `http://localhost:8080/#/<state>`; rebuilt screens at `http://localhost:4309/<state>`.
- Login on both: `Admin` / `12345`.
- Module grants needed if you hit "Access Denied" (patch via `localStorage` on both origins the same way used throughout this project): `wrapperExpiryMaster` = 554, `occupancyUnitMaster` = 404, `genericScheduleMaster` = 827, `growthChartMaster` = 962, `dietTypeMaster` = 510.
- **SEVERITY-1 finding, `dietTypeMaster` only, needs your explicit attention**: Update is completely, unconditionally non-functional on real, unmodified legacy — confirmed live on both real legacy and the rebuild, and confirmed via direct DB query that no write ever occurs. Not fixed, faithfully reproduced. Full writeup below and in `docs/ANGULAR_PENDING_FINDINGS.md`.
- This batch's baseline also carries `growthChartMaster`'s `BR-098` fix (an additive `gender_id` column add to `m_growth_chart_master`, already applied and verified — Create/Update were unconditionally broken before it).

---

## 1. `dietTypeMaster` — recommended first, given severity

**Paths:** `dietTypeMaster` (legacy / rebuild), module id 510, under "Diet & Nutrition".

**Test data:** table is currently empty (test rows from this session's own verification were cleaned up after use).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | "No Records Found" |
| 2 | Click Save with both fields blank | Blocked by real `required` validation, no network call |
| 3 | Create `DT1`/"Standard Diet" | Confirm modal ("Are you sure you want to Save?") → Ok → "Diet Added Successfully...!!" → row appears |
| 4 | Create `DT2`/"Standard Diet" (same description, different code) | **Succeeds** — the duplicate-check compares Code only, never Description |
| 5 | Create a third row reusing Code `DT1` (different description) | **Rejected** — "Diet already exits...!!" (typo preserved) — Code is unique per-organization (not per-unit) |
| 6 | Immediately retry the same duplicate Save again | Confirm modal fires again — rearm-after-failure fix confirmed |
| 7 | Click Edit on `DT1`, change the description, click Save (same button, no separate "Update") | Confirm modal fires again (same shared gate as Create) → Ok → **fails with "Something went wrong. Update failed."** — this is the severity-1 finding, not a rebuild bug; reload the page and confirm via the table that the description was NOT actually changed |
| 8 | Toggle any row's status | Switch flips, "Diet Updated Successfully...!!" toast — this call is NOT affected by the Update bug (different endpoint) |

**Needs your explicit sign-off on:**
- **SEVERITY-1, real legacy bug, faithfully reproduced, not fixed** (step 7): every Update attempt throws an uncaught `NullPointerException` server-side (`GlobalCommonDateUtils.getDate` called with a `null` `updatedDate` — legacy's own frontend never populates this field, so this happens 100% of the time, for every user, on every row, no exceptions). Verified live on real legacy directly, not just the rebuild, with a direct DB query confirming no write ever occurs. An error toast was added to the rebuild (not present as specific wording in legacy, which shows a generic wrapper-level error) purely so the failure is visible rather than silent.
- **Faithful reproduction**: duplicate-check is Code-only (case-sensitive-insensitive match), organization-scoped (not unit-scoped) — confirmed live both directions (same description twice = OK, same code twice = rejected).
- **Open business question, NOT resolved, per your own earlier instruction**: whether Code should be unique per-organization or per-unit remains genuinely unclear from the code alone — built to today's real org-only behavior, not changed.
- **Faithful reproduction**: a 3-way redundant fetch race fires on every page load (`getAll` + a paginated list call + a count call, none chained) — check the Network tab on load, you'll see 3 near-simultaneous calls. Not simplified — an earlier audit flagged this as a possible simplification candidate but no sign-off was given for that change.
- **Rebuild-only fixes, disclosed**: confirm-modal now rearms after every failed Save/Update (matching this project's standing convention); Edit no longer live-mutates the table row while typing (same fix already applied to this batch's `wrapperExpiryMaster`).

---

## 2. `wrapperExpiryMaster`

**Paths:** `wrapperExpiryMaster` (legacy / rebuild), module id 554, under "CSSD".

**Test data already in the dev DB:** `WRPTEST01`/"Test Wrapper"/30 days (Inactive) — leftover from this screen's original build/verification pass.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | `WRPTEST01` shows |
| 2 | Create `WRP1`/"Gauze Wrap"/14 | Confirm modal → Ok → "Wrapper Expiry added Successfully...!!" → row appears |
| 3 | Duplicate the code, confirm rejection with "Wrapper already exits...!!", then retry Save | Confirm modal fires again (rearm fix) |
| 4 | Click Edit on `WRP1`, change the duration, click the SAME button (no separate Update button — it's dead markup) | **No confirm modal** (Update has none on this screen, unlike `dietTypeMaster`) → "Wrapper Master updated Successfully...!!" if unique, or the DIFFERENT literal "Update Wrapper Master already exits...!!" if it collides |
| 5 | Toggle any row's status | Switch flips, "Wrapper Expiry Master Updated Successfully...!!" toast, no list reload |

**Needs your explicit sign-off on:**
- **Fixed here**: Edit no longer binds the form directly to the row object (legacy's own `editWrapperExpiry` mutates the still-displayed table row on every keystroke before Save is clicked) — the rebuild clones the row instead.
- **Fixed here**: Update's payload now sends the current session's real `organizationId`/`unitId` — legacy's own Update payload never carries real values for these (sourced from a row object whose backing query never selects them), which silently weakens the duplicate-check on Update specifically, beyond the already-merged `BR-095` fix.
- **Fixed here**: status-toggle payload now sends the real `unitId` — legacy's own payload omits it, causing a silent timezone-0 default on the persisted timestamp.
- **Faithful reproduction, already-known, NOT fixed**: the pager's total count (`getWrapperListcount`) is organization-only, while the list itself is organization+unit — a real desync in a multi-unit org, logged separately, awaiting a broader owner decision.
- **Faithful reproduction**: the two duplicate-rejection messages (Create vs. Update) are genuinely different literal strings in legacy's own source — not a rebuild inconsistency.

---

## 3. `occupancyUnitMaster`

**Paths:** `occupancyUnitMaster` (legacy / rebuild), module id 404, under "ADT - Registration".

**Test data already in the dev DB:** 1 pre-existing row — check the table on load.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Existing row(s) show |
| 2 | Create `OU1`/"Ward A"/hours=`24` | Confirm modal → Ok → success toast → row appears |
| 3 | Create a SECOND row with the same Description ("Ward A") but a different Code | **Rejected as a duplicate** — the check is by Description only, Code has zero uniqueness enforcement |
| 4 | Edit `OU1`, clear the description entirely, click Update | **No confirm modal on Update** (Create-only gate) → real client validation should still block an empty description before the call fires |
| 5 | Toggle any row's status | Switch flips immediately (optimistic, two-way-bound), no reload, no revert on failure |
| 6 | Use the Search box | Genuinely working client-side filter over the currently-loaded page |

**Needs your explicit sign-off on:**
- **No action needed** — no known bugs beyond the faithfully-reproduced items below; this screen's own logic is internally consistent.
- **Faithful reproduction**: confirm-modal on Create only, matching this codebase's grade-creation-master/amenity-master/bank-master precedent.
- **Faithful reproduction**: error toasts are hardcoded client-side strings ("Occupancy unit already exits...!!"), not the server's real dynamic message — a real duplicate and a genuine unrelated server error look identical to the user.
- **Faithful reproduction**: `showUpdateBtn`'s failure path uses a native browser `alert("Error!!")`, not the app's growl toast system.
- **Faithful reproduction**: Edit has no disabled/inactive guard — editing an Inactive row works the same as an Active one.

---

## 4. `genericScheduleMaster`

**Paths:** `genericScheduleMaster` (legacy / rebuild), module id 827, under "Pharmacy".

**Test data already in the dev DB:** 1 pre-existing row — check the table on load. Requires real Generic/Schedule reference data (already present).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Existing row shows |
| 2 | Select a Generic + Schedule combination, Save | **No confirm modal on this screen at all** (neither Save nor Update) → row appears immediately |
| 3 | Create a second row with the SAME Generic+Schedule combination | **Rejected** — "Already Exists." (a real server message, not hardcoded — this screen growls the server's own dynamic text) |
| 4 | Edit an existing row, change its Generic+Schedule to collide with ANOTHER active row's combination, click Update | **Succeeds** — this is the finding below, not a rebuild bug |
| 5 | Check the "Prior Investigation" column | An info-icon opening a read-only popup of linked service codes, NOT a plain Yes/No text cell |

**Needs your explicit sign-off on:**
- **Real, known gap, faithfully reproduced, NOT fixed**: Create's duplicate-check is active and correctly rejects collisions; Update's equivalent check exists in the DAO's query definitions but is commented out at the service call site — an edit can freely collide with another active row. This needs an explicit decision from you (re-enable on Update, as a disclosed behavioral change with its own `BUSINESS_RULE_MANIFEST.md` entry) before it could ever be fixed — not done silently here.
- **Faithful reproduction**: two different boolean-string vocabularies are kept as-is (`Yes`/`No` for Emergency Medicine, `Y`/`N` for Prior Investigation) rather than normalized to match.
- **Faithful reproduction**: the service-code multiselect has a Create-vs-Update asymmetry — Create only sends selected codes when Prior Investigation is checked; Update sends whatever's currently selected regardless of that checkbox's state.

---

## 5. `growthChartMaster`

**Paths:** `growthChartMaster` (legacy / rebuild), module id 962 (Ward/Unit Master heading) — **security note**: legacy's own route has NO permission guard at all (confirmed directly, no `resolve` block, sidebar link ungated too); this rebuild adds a real guard for moduleId 962 anyway, matching this project's standing convention of not porting a legacy access-control gap into new code.

**Test data already in the dev DB:** 4 pre-existing rows (real weight/height/head-circumference bands by age+gender) — required, since the `general_ledger_id`-class schema gap (`gender_id` missing from `m_growth_chart_master`) was already fixed this session as `BR-098` before this screen could be built at all.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | 4 existing rows show |
| 2 | Create a row: Gender, Age in Months = `6`, Weight/Height/Head-Circumference From-To + Units | **No confirm modal anywhere on this screen** (Save, Update, AND Delete all fire immediately) → row appears |
| 3 | Create a second row with the SAME Gender + Age in Months but OVERLAPPING (not identical) weight/height ranges | **Succeeds** — the duplicate-check is an exact match on (org, unit, ageInMonths, genderId) only, not a true range-overlap validation |
| 4 | Click the Delete icon on any row | **Fires immediately, no confirmation dialog** — a soft-delete (`status='D'`) server-side, deliberately reproduced as one-click since the data isn't actually destroyed and there's no cascading blast radius |
| 5 | Edit a row, change a value, Update | No confirm modal → real server message shown on error (not hardcoded) → row updates; **status always force-resets to Active on Update** — there's no way to reach "Inactive" through this screen, only Active → Deleted |
| 6 | Use the Search box | Genuinely working client-side filter — a disclosed ADDITION, since legacy's own filter expression has no corresponding input anywhere in the view and can never actually run |

**Needs your explicit sign-off on:**
- **Disclosed addition, not a silent fix**: the working search box above doesn't exist in legacy's reachable UI (a dead filter expression with no input) — added here as a genuine improvement, flagged for your awareness rather than silently ported as broken.
- **Disclosed simplification, not a silent fix**: legacy's own pagination is fully dead (a self-referential no-op always requesting offset 0/100 rows, no page UI in the template, and no backend count endpoint exists at all for this screen). Real server-side offset/limit paging with Previous/Next (driven by a "did this page come back full" heuristic, since no true page count is possible) is used instead of porting a non-functional page-1-only screen.
- **Faithful reproduction**: no confirm-modal on Save, Update, or Delete — deliberately not "improved" with an added confirmation step, since this was judged not to cross the bar of a genuinely dangerous gap (soft-delete, no cascading effect).
- **Faithful reproduction**: duplicate-check is exact-match only, not range-overlap — nonsensical overlapping bands for the same age+gender are not rejected.
- **Faithful reproduction**: `getGrowthChartById`'s error path uses a native `alert("Error In Fetching Data")`, not the app's growl toast.
- **Awareness only, already resolved**: this screen was unconditionally broken (Create/Update) before this session's `BR-098` fix (missing `gender_id` column) — already applied and verified, no further action needed.

---

## Summary for your review

| Screen | Confirm-modal | Rebuild-only fixes | Legacy bugs faithfully reproduced |
|---|---|---|---|
| dietTypeMaster | Shared gate, both Create AND Update; rearms on failure (fixed) | Edit no longer live-mutates the row; error toast added for Update's raw 500 | **SEVERITY-1: Update always fails**, uncaught NPE, zero writes ever; Code-only/org-scoped duplicate-check (Description unchecked); 3-way redundant-fetch race on load |
| wrapperExpiryMaster | Create only | Edit no longer live-mutates the row; Update/status-toggle now send the real session org/unit | Pager-count org-only vs. list org+unit desync (known, unfixed); Create-vs-Update duplicate messages are genuinely different strings |
| occupancyUnitMaster | Create only | None | Duplicate-check is Description-only, Code unchecked; hardcoded (not server-real) error text; native `alert()` on edit-load failure; no Inactive-edit guard |
| genericScheduleMaster | None on either Save or Update | None | Update's duplicate-check is commented out (real, unresolved gap needing your decision); Yes/No vs Y/N vocabularies kept distinct; service-code multiselect Create/Update asymmetry |
| growthChartMaster | None on Save, Update, or Delete | Working search box added (legacy's own is dead); real offset/limit pagination added (legacy's own is fully non-functional) | No confirm on one-click soft-delete; duplicate-check is exact-match not range-overlap; Update always force-resets status to Active; native `alert()` on edit-load failure |

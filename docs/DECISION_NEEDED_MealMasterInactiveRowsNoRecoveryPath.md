# Decision Needed: `mealMaster` has no way for a real user to view or reactivate a deactivated row -- a working backend endpoint exists but nothing in the product calls it

**Status: PROPOSAL ONLY, not implemented.** Found during batch 23 Step 2 (golden-trace/live testing) of the Angular rebuild, 2026-07-27, and confirmed by the owner's own follow-up questioning to be a real, distinct-shaped bug rather than the silent/invisible-bug class the rebuild's own "fix vs. faithfully reproduce" carve-out applies to (see `docs/ANGULAR_PENDING_FINDINGS.md`, entry `PENDING-batch23-step2-mealmaster-goldentrace-SEVERITY1`, for the original finding; the rebuild itself faithfully reproduces this behavior, unmodified, per that decision). This document proposes a fix for the underlying legacy product gap, on its own dedicated branch, independent of and not blocking the rebuild.

---

## 1. What's confirmed

`mealMaster`'s list screen (`legacy/HIS-WAR/.../views/master/GlobalMaster/DietNutrition/meal-master.html`) has one paginated list, backed by `POST /api/kitchen/master/mealMaster/getAllMealMasterListPaginationList`. That endpoint's query (`MEAL_MASTER.GET_ACTIVE_MEAL_MASTER_LIST` in `MealMaster.java:69-76`) has `WHERE mealMaster.status='A' ...` -- active rows only, unconditionally. Once a row's status is toggled to Inactive (`updateStatus`, wired to the working `PUT /api/kitchen/master/mealMaster/updateStatus`), it is excluded from every subsequent load of this list. There is no filter, search option, tab, or any other control anywhere in this screen's view that changes which rows the list query returns.

**The row itself is not deleted or corrupted.** `MealMasterDaoImpl.updateStatus` (dao:187-207) does a plain Hibernate `update()` that only touches the `status`, `updated_by`, and `updated_date` columns. Live-verified (both real legacy and the rebuild, 2026-07-27): created a row, toggled it Inactive, reloaded -- the row is gone from the screen, but a direct query against `kitchen.m_meal_master` would still show it, status `'I'`, otherwise unchanged.

**A working, unfiltered endpoint already exists in the backend, but nothing in the product calls it.** `MealMasterController.java:36-45` exposes `GET /api/kitchen/master/mealMaster/getAll?organizationId=...&unitId=...`, wired to `MealMasterServiceImpl.getAllMealMasterList`, whose query (`MEAL_MASTER.GET_ALL_MEAL_MASTER_LIST`, `MealMaster.java:95-102`) has no status filter at all -- it would return both active and inactive rows. Confirmed via `grep` across the entire legacy frontend: the only reference to this endpoint's URL constant (`GETALLMEALMASTER`, `diet.url.config.js:49`) is its own definition, plus one call site inside `mealMasterController.js`'s `init()` function that is commented out (ctrl:68-83). No other screen or code path calls it either. This looks like a leftover from an earlier version of the screen, before it was switched to the active-only paginated list.

## 2. Real-world impact

A user (or an automated process) that deactivates a meal master by mistake, or that later wants to reactivate a seasonal/discontinued meal, has **no path back through the application** -- not a filter, not a different screen, not an admin tool. Recovery today requires either a direct database `UPDATE`, or a hand-crafted API call to the already-working `updateStatus` endpoint using a row id obtained some other way (e.g., a raw call to the dead `getAll` endpoint) -- both are engineer-level operations, not something a normal user or even most support staff could do unassisted.

This is a real product gap, not a rebuild defect: the rebuild (batch 23, `rebuild/phase4-batch23`) faithfully reproduces this exact behavior because it calls the same real, live endpoints legacy does.

## 3. Proposed fix -- two options, not yet applied

Both options reuse the existing, already-working `getAll` endpoint -- no new backend endpoint is needed, only wiring it into the UI (and, for Option A, adding client-side status-based filtering/grouping, since `getAll` itself doesn't support pagination the way the active-only list does).

**Option A -- "Show inactive" toggle on the list (recommended).**
Add a checkbox/toggle above the table (e.g., "Show inactive"). Unchecked (default): current behavior, calls the existing paginated active-only endpoint, unchanged. Checked: calls `getAll` instead, client-side filters/sorts/paginates the full result set the same way `dietUnitMater` (batch 22) already does for its own un-paginated list, and shows both active and inactive rows -- inactive ones with their status switch visibly off, so a user can flip it back on directly from the same table they're already using. This is the same interaction users already have for every other active/inactive row in this screen; it just needs to be reachable for inactive rows too.

**Option B -- count + link to a separate inactive-rows view.**
Show a small "N inactive" indicator near the list (count from the same `getAll` response, computed client-side). Clicking it opens a separate, read-mostly view (or an inline expandable section) of just the inactive rows, each with its own reactivate control. More separation between the "normal" and "recovery" workflows, at the cost of an extra screen/state to build and maintain.

**Recommendation: Option A.** It reuses this screen's existing table/toggle UI wholesale (no new view, no new interaction pattern), the endpoint it needs already exists and already works, and it matches the precedent this project already established for `dietUnitMater`'s own un-paginated, client-side-filtered list. Option B is a reasonable alternative if a cleaner separation between "day to day" and "recovery" workflows is preferred, but represents meaningfully more new UI surface for the same underlying gap.

**Not proposed:** removing the active-only filter from the paginated list entirely (would change the screen's default, everyday behavior for all users, not just add a recovery path) or silently auto-reactivating anything (masks the fact that a deactivation happened at all).

## 4. Scope, if approved

- **Legacy application code only** (AngularJS controller + view for `mealMaster`), on its own dedicated branch (proposed name: `fix/mealmaster-inactive-rows-recovery-path`), separate from and not blocking the Angular rebuild's own `rebuild/phase4-batch23` branch.
- No backend/Java changes needed -- the `getAll` endpoint and its query already exist and already work; this is UI wiring only.
- No database schema or data changes.
- Once a specific option is approved, the Angular rebuild (`meal-master.ts`/`.html`, already built and held on `rebuild/phase4-batch23`) would need a small follow-up change to match the newly-corrected legacy behavior, tracked separately -- the current rebuild commit intentionally reproduces today's (gap-having) behavior exactly, per the owner's decision to keep the rebuild faithful to current legacy pending this proposal's outcome.

**Awaiting owner decision**: whether to proceed at all, and if so, Option A vs. Option B (or a different shape). Not implemented, not branched, no code written yet.

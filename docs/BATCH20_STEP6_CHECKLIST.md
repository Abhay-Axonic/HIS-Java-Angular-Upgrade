# Batch 20 — Step 6 Domain-Expert Review Checklist

*All 5 screens are through Steps 0-5 on `rebuild/phase4-batch20` (held, not merged, not pushed). This doc consolidates each screen's own Step 1-5 material into one place for a methodical walkthrough — see `docs/ANGULAR_PENDING_FINDINGS.md` for the full audit writeups (`PENDING-incidentmaster-step1-findings`, `PENDING-qmscallmaster-releasetokennumber-wrong-orgid`, `PENDING-accesscontrol1-rolechanged-duplicate-guard-fixed-in-rebuild`, batch 20/21 Step 1 entries for `assetPMSScheduler`/`linenReturn`).*

*Note on this doc's timing: this batch was built in an earlier session (2026-07-26/27) but never got a consolidated Step 6 checklist at the time (unlike every batch since 17) — no artifact for it exists anywhere in this project's history. This is the first one, built 2026-07-28 while resolving batch 20/21's overall status.*

**Setup, once, before starting any of the 5:**
- Worktree `.claude/worktrees/step6-batch20`, branch `rebuild/phase4-batch20`. Confirm `ng serve --port 4308 --proxy-config proxy.conf.json` (from `.claude/worktrees/step6-batch20/new`) and the Java backend (port 8080, `his_local` DB) are both running.
- Legacy screens are reached at `http://localhost:8080/#/<state>`; rebuilt screens at `http://localhost:4308/<state>`.
- Login on both: `Admin` / `12345`.
- Module grants needed if you hit "Access Denied": `call` = 793, `accessControl1` = 673, `assetPMSScheduler` = 223, `incidentMaster` = 990. `linenReturn` has no module gate at all (legacy's own state has no `resolve` block — confirmed directly, not a rebuild oversight).
- **`linenReturn` is a net-new build, not a port** — legacy's own frontend for this screen is 100% dead, copy-pasted, zero-wiring static markup (confirmed: no `ng-model`/`ng-click`/`$http` anywhere in any of its 3 views, a leftover "Material Return" legend, a Save button wired to the global logout modal). You made an explicit call to proceed anyway, building a real frontend against the legacy backend's own (mostly correct) contract — there is no legacy UI to compare side-by-side for this one screen; test the rebuild against the backend behavior described below instead.
- **`accessControl1` needed a scope correction before it could be built at all**: legacy has 3 different screens tangled together under confusingly similar names/routes (`/accessControl` — a broken decorative mockup accidentally running real logic against the wrong template; `/newAccessControl` — a totally different menu-builder screen; `/accessControl1` — the real, coherent "Role Permissions" screen, THIS one). Do not compare against `/accessControl` on legacy — it's a different, broken thing entirely.

---

## 1. `call` — real-time QMS operator console

**Paths:** `call` (legacy / rebuild), module id 793, under "Queue Management". Not a list/CRUD master — a live kiosk console (Kiosk → Department/Counter → call/serve/no-show tokens).

**Test data:** requires a configured Kiosk + Department + Counter with QMS enabled (already present in this dev environment from prior QMS screen testing).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Kiosk-selection modal appears first |
| 2 | Select a Kiosk | Modal closes, Department/Counter modal opens |
| 3 | Select a Department + Counter | Console loads: live token queue, Call/Serve/No-show controls |
| 4 | Click "Call" for the next token | Token advances to "in progress" for this counter |
| 5 | Click the "+" (Issue Token) button, fill in the walk-up form, confirm | A real new token is issued and appears in the queue — this path is fully live in legacy, unlike two related dead buttons (see below) |
| 6 | Look for a "Recall" button on an already-served token | **Clicking it only redisplays the token number and flips a local enabled/disabled state — nothing is persisted to the server.** Not a rebuild bug — legacy's own persistence call for this exact button is commented out in its source |
| 7 | Re-open the Kiosk or Department/Counter selection via their header edit icons | Both re-open independently at any time, not just on first visit |
| 8 | Leave the screen open for 10+ seconds | Token list auto-refreshes via polling, matching legacy's own 10s interval |

**Needs your explicit sign-off on:**
- **Real legacy bug, NOT fixed, faithfully reproduced**: `releaseTokenNumber` (the step that releases a token previously locked by another counter) silently no-ops on every single call — the payload sends `organisationId` from a variable (`$scope.unitId`) that is never actually assigned anywhere in legacy's own controller, so the release query matches zero rows every time. Confirmed by reading the DAO directly. This has NOT been fixed — it needs its own separate proposal and your sign-off first, per your own earlier instruction on this exact finding.
- **Rebuild-only fix, disclosed, infra-level not business-logic**: legacy's live-queue polling has no cleanup at all (a real interval/memory leak, confirmed via grep) — this rebuild's polling correctly unsubscribes when you navigate away. Not a behavior change you'd ever notice.
- **Faithful reproduction, confirmed dead**: the standalone "Served/Recall" button ROW is entirely commented out in legacy's own markup — not built here either, since there's genuinely nothing to click.
- **Faithful reproduction**: legacy's own misspelled "transefer successfully" toast on department transfer, kept verbatim.

---

## 2. `linenReturn` — net-new frontend against a real backend (see setup note above)

**Paths:** `linenReturn` (legacy state exists but is 100% non-functional dead markup — do not use it as a comparison baseline) / rebuild only, no module gate.

**Test data:** requires Linen-type store/item seed data (store_type_id=5) — check what's already loaded; if the list is empty, that reflects the dev DB's current state, not a rebuild defect.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Search filter bar (From Store/To Store/Return No./Status) + results table |
| 2 | Search with no filters | Full result set loads (the DAO has no offset/limit — this screen paginates client-side over the complete list) |
| 3 | Click "+" / Add New | Navigates to a real create form (`addNewLinenReturn`) |
| 4 | Select rows via checkboxes, click Approve | A modal asks for a target status; confirming fires a real `PATCH .../approve` with the selected IDs |
| 5 | Click the Cancel icon on a single row | A modal asks for a Cancel Reason + Note + target status; confirming fires a real `PATCH .../cancel/{id}` |
| 6 | Click the eye/view icon on a row | Navigates to a real read-only details screen (`linenReturnDetails`) |

**Needs your explicit sign-off on:**
- **Backend real bug, faithfully reproduced, NOT fixed, per your own explicit instruction**: `LinenReturnService.saveLinenReturn()` hardcodes `returnNo = "RET-001"` for every single return, regardless of what's created. This is loud/visible (every row would show the same return number), not silent, so this project's silent-bug-correction exception does NOT apply — reproduced as-is; no Return No. input exists on the create form (matching legacy, which never asks for one).
- **No status-vocabulary was guessed**: this dev environment has zero seeded rows for this screen's status field, and legacy's own frontend never exercised a real approve/cancel transition to observe real status names against. Every status value shown (search filter, Approve's target, Cancel's target) comes directly from the screen's own live dropdown endpoint, not invented names.
- **Disclosed scope decision, not a silent gap**: no row-eligibility ("is this row still actionable") gating is applied anywhere — for the same reason above (no discoverable rule for which statuses are actionable without inventing one).

---

## 3. `accessControl1` — Role Permissions

**Paths:** `accessControl1` (legacy / rebuild), module id 673, under "User Management". **Do not confuse with `/accessControl`** (a different, broken screen — see setup note).

**Test data already in the dev DB:** 1 existing role-privilege row.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Role dropdown + a table of roles-with-permissions |
| 2 | Select a role, check some modules in the HIS tab tree, Save | Real POST, row appears/updates in the table |
| 3 | Try saving the SAME role + SAME tab (moduleType) a second time without changing anything | **Rejected as a duplicate** — this is the fixed bug, see below |
| 4 | Switch to the BI tab, save permissions for the same role | Succeeds (a different `moduleType`) — but note the backend gap below |
| 5 | Click Delete on a role's permissions row | Real confirm dialog ("Do you want to delete permissions for this role?") → real hard-delete, not a status toggle |
| 6 | Check a parent node, then a child node under it | No forced cascade — only the exact node you click gets added to what's saved, matching legacy's own plain flag-based selection model |

**Needs your explicit sign-off on:**
- **Fixed here, per your explicit instruction, NOT faithfully reproduced**: legacy's own duplicate-role guard is genuinely dead code (a real bug — it reads a property off an array that doesn't exist, so the check can never trigger, ever). Fixed via a real scan of the actual permission list for an existing (role, tab) match. Logged as `PENDING-accesscontrol1-rolechanged-duplicate-guard-fixed-in-rebuild`.
- **One related, undirected fix bundled in to make the above coherent, disclosed rather than silently folded in**: legacy also has two different data-fetch calls racing to overwrite the SAME variable on page load — split into two independent, non-conflicting data sources here, since the duplicate-guard fix needs a reliable list to check against. Nothing about the table's actual content changed, just removed an adjacent timing bug.
- **Backend gap, confirmed, explicitly OUT OF SCOPE for this Angular-only rebuild, not fixed**: the server's own uniqueness check ignores which tab (moduleType) you're saving — a role can only ever have ONE combined privilege row server-side, in tension with the two-tab UI design. This is a backend issue beyond what this rebuild touches.
- **Backend bug, confirmed, NOT fixed, out of scope**: every real Update silently nulls out the row's `moduleType` column server-side, regardless of what this screen sends — a pre-existing legacy defect, unrelated to the fix above.

---

## 4. `assetPMSScheduler` — list/search entry point of a 3-screen workflow

**Paths:** `assetPMSScheduler` (legacy / rebuild), module id 223, under "Asset Management". Upstream of `assetScheduler` (create/edit) and `viewAssetScheduler` (read-only), both already built in earlier batches.

**Test data:** depends on existing Asset Category/Product Category/Item seed data already in this dev DB.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Store-selection popup may appear first if no store is already selected this session |
| 2 | Select an Asset Category, search by Asset Name | **Real backend bug**: results actually filter by Asset Code instead of Asset Name — a genuine server-side copy-paste bug, not a rebuild defect |
| 3 | Search WITHOUT selecting an Asset Category | **Table comes back empty even if the backend found real matches** — a real frontend re-filter bug in legacy (strict-equality check against a blank category silently discards every result) |
| 4 | Search WITH an Asset Category selected | Real results show |
| 5 | Click a result row to schedule it | Navigates to `assetScheduler` with the row's data pre-filled |
| 6 | Click a result row to view it | Navigates to `viewAssetScheduler` (read-only) with the row's data pre-filled |
| 7 | Select an Asset Category to trigger the Product Category dropdown | May come back empty/fail silently — a known, separate schema-mismatch gap tracked under `PENDING-assetpmsscheduler-productcategory-generalledgerid-schema-mismatch`, not specific to this rebuild |

**Needs your explicit sign-off on:**
- **Real backend bug, faithfully reproduced, NOT fixed**: Asset Name search actually filters by Asset Code server-side.
- **Real frontend bug, faithfully reproduced, NOT fixed**: an empty Asset Category selection silently blanks the results table even when the backend found real matches.
- **Already-known, separate, NOT fixed**: the Product Category cascade endpoint has an unrelated schema mismatch, tracked and awaiting its own decision.

---

## 5. `incidentMaster`

**Paths:** `incidentMaster` (legacy state is self-registered inline in its own controller file, invisible to a plain `app.js` grep — confirmed live) / rebuild, module id 990, under "Incident Reporting".

**Test data:** table is currently empty in this dev DB. Rows with `id < 100` are seed/reference records visible across every org/unit with locked Code/Description fields while editing — none currently seeded here, but be aware of this rule if you see one.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | "No Records Found" |
| 2 | Click Save with everything blank | "Please Enter Code" → fill Code, resubmit → "Please Enter Description" → fill Description → "Please Select Role" → select a role → "Please Select Template" |
| 3 | Check a Template on the LEFT list only, then click Save without clicking the `<<>>` move button | **Still blocked with "Please Select Template"** — checking alone does nothing; only the move button actually populates what gets validated/submitted |
| 4 | Move a template to the right panel, select a Role, fill Code+Description, Save | No confirm modal on this screen at all → row appears |
| 5 | Create a second row with the exact SAME Code as the first | **Rejected** — but only because it's still on the currently-loaded page; the check is an in-memory string match over `commonList`, not a real server query |
| 6 | Edit the first row, change nothing else, click Update | **No duplicate-check runs at all on Update** — succeeds even with the same Code, unlike Create |
| 7 | Toggle any row's status | Switch flips, but role/template mappings are NOT deactivated despite the endpoint name implying they are |

**Needs your explicit sign-off on:**
- **SEVERITY-1 CONCERN RAISED, then LIVE-VERIFIED AS NOT AN ISSUE**: the Edit flow's underlying named queries contain a bare, unqualified column reference that looked like it should crash Hibernate on every call. Directly tested against real, unmodified legacy this session (a real JWT minted, real rows inserted/queried/deleted) — confirmed this Hibernate version tolerates it. Edit genuinely works; this was a false alarm, corrected in the record rather than left as an open concern.
- **Faithful reproduction, real gap**: no reliable uniqueness enforcement anywhere — Create's own check is current-page-only (not a real server query) and Update has no check at all.
- **Faithful reproduction**: Save and Update both create a full role × template cross-product server-side (e.g. 2 roles × 3 templates = 6 mapper rows, not 3) — happens entirely server-side, this screen only submits what the user actually picked.
- **Disclosed scope decision, not a silently-ported gap**: legacy's own search box is dead/incomplete code (typing 1-2 characters silently blanks the whole table via a variable that's declared but never populated) — reproduced instead as a genuinely working client-side filter over the current page, matching the one part of legacy's search that visibly worked.
- **Disclosed scope decision, not a silently-ported gap**: legacy's own Template-picker state can desync across two separate arrays in a way that would make Edit worse and more confusing to use — this rebuild uses one consistent set instead, since faithfully reproducing the desync would only make the experience worse with no informational value.

---

## Summary for your review

| Screen | Confirm-modal | Rebuild-only fixes | Legacy bugs faithfully reproduced |
|---|---|---|---|
| `call` | N/A (real-time console, not a list/CRUD screen) | Polling interval cleanup (infra-only) | `releaseTokenNumber` always no-ops (wrong variable, real bug, awaiting separate decision); "Recall" button is display-only, nothing persists |
| `linenReturn` | N/A (net-new build, no legacy UI to compare) | None | `returnNo` hardcoded to `"RET-001"` on every create (loud, not silent — per your instruction, not fixed) |
| `accessControl1` | Real confirm on Delete only | **Duplicate-role guard fixed** (was genuinely dead code); a related data-race split into two clean signals | Server ignores tab (moduleType) in its own uniqueness check; server nulls `moduleType` on every Update (both backend, out of scope) |
| `assetPMSScheduler` | N/A | None | Asset Name search actually filters by Code; blank-category search silently empties real results; separate Product Category schema gap |
| `incidentMaster` | N/A (confirmed no modal anywhere) | None | Duplicate-check is current-page-only, Create-only; role×template cross-product on every save/update; status-toggle cascade no-op; dead/incomplete search code (not ported) |

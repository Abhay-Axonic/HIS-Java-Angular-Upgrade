# Batch 28 — Step 6 Domain-Expert Review Checklist

*All 5 screens are through Steps 0-5 on `rebuild/phase4-batch28` (worktree `.claude/worktrees/batch28`, held, not merged, not pushed). Commit `8bd0bc3a`. This batch surfaced an unusually high concentration of real, live severity-1 findings — 4 confirmed during Step 0/1 audit (independently source-verified by the orchestrating session, not just relayed from build agents) plus 1 more found only through Step 5 live interaction testing (`machineMaster`'s update-reactivation bug — invisible to static reading, exactly the failure mode Step 5 exists to catch per the playbook). None of the 5 findings below required any change to the rebuild itself except `machineMaster`'s pagination fix (corrected client-side, since a safe client-side correction was possible there); the other 4 are pure backend bugs with no safe workaround, faithfully reproduced, flagged for their own dedicated legacy branches.*

**Setup, once, before starting any of the 5:**
- Worktree `.claude/worktrees/batch28`, branch `rebuild/phase4-batch28`. Confirm `ng serve --port 4314 --proxy-config proxy.conf.json` (from `.claude/worktrees/batch28/new`) and the Java backend (port 8080, `his_local` DB) are both running.
- Legacy screens are reached at `http://localhost:8080/#/<state>`; rebuilt screens at `http://localhost:4314/<path>`.
- Login on both: `Admin` / `12345`, Organization/Unit `DEMO`. Module ids: `ageGroupMaster` = 416, `doctorSlabMaster` = 1020, `generalInsuranceMaster` = 646, `machineMaster` = 840, `checklistMaster` = 526. If you hit "Access Denied" on the rebuild, use the standard console patch: `const p = JSON.parse(localStorage.getItem('permissions')||'{"modules":[]}'); p.modules.push({id: <id>}); localStorage.setItem('permissions', JSON.stringify(p));` then reload.
- **Three of the five screens have a dev-DB seed-data gap that blocks a full create/save round trip** (not a rebuild defect — the identical gap affects real legacy too, same class as Phase 2's `insuranceMaster`/`MISReports` gaps): `doctorSlabMaster` (the one seeded doctor has a null `user_id`, so the real doctor-dropdown join returns zero rows), `generalInsuranceMaster` (the shared Country/State/City/Area dropdown endpoint 500s — see its own severity-1 below, a real bug not a seed gap, but has the same practical effect of blocking the form), `checklistMaster` (the Role dropdown's backing table has zero seeded OT roles). These are flagged per-screen below; List/Edit/toggle behavior was still verified where possible.

---

## 1. `machineMaster` — SEVERITY-1 x2, review first

**Paths:** `machineMaster` (legacy `#/machineMaster` / rebuild `/machineMaster`, moduleId 840).

**Critical context before you start — two independent, unrelated severity-1 bugs, both confirmed live against real unmodified legacy:**

1. **Pagination past page 1 silently shows the wrong entity's data.** Every pagination click (First/Previous/page-number/Next/Last) calls `LIST_HEADER_MASTER`, not `LIST_MACHINE_MASTER` — a completely different Java controller (`HeaderMasterController`) querying a completely different table (`lab.m_header_master`, an LIS test-panel master, not `lab.m_machine_master`). **Corrected in the rebuild** (both initial load and every pagination click now use the real `listMachineMaster` endpoint) — this is a disclosed, deliberate deviation from legacy's current (broken) behavior, not a faithful reproduction. Not independently re-testable with only 2 seeded rows (page size is 10); confirmed correct via direct source read of the rebuild's service layer instead (`listHeaderMaster` does not appear anywhere in its call graph).
2. **Editing and saving ANY machine silently reactivates it, regardless of its current status.** `MachineMasterDaoImpl.updateMachineMaster` hardcodes `machineMaster.setStatus('A')` on every update, discarding whatever status the client actually sent. Proven live: deactivated a test row, confirmed the rebuild's outgoing payload correctly carried `status: "I"`, sent the real update — database came back `status='A'` anyway. **Not fixed** (pure backend, no safe client-side workaround — same shape as `doctorSlabMaster`'s bug below), faithfully reproduced in the rebuild since there is nothing the frontend can safely do differently.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Real seeded row `MC1`/"Analyzer One" (active) |
| 2 | Add a new machine (unique code + description), Save | Row appears, active by default |
| 3 | Toggle the new row's status off | Switch flips to inactive; verify against legacy side by side |
| 4 | Click Edit on the now-**inactive** row, change the description only, Save/Update | **Bug #2 above: the row silently comes back Active**, on both apps identically — confirm this happens on real legacy too, not just the rebuild |
| 5 | With &gt;10 real rows (if your review environment has more seed data than this dev DB), page to page 2 | **Bug #1 on legacy**: table silently shows unrelated LIS header-master rows; **rebuild**: correctly shows real machine-master page 2 — this is the one deliberate, disclosed deviation in this whole batch |

**Needs your explicit sign-off on:**
- **SEVERITY-1, corrected in the rebuild (deliberate deviation from legacy's current broken behavior)**: pagination endpoint bug above. Please confirm you're comfortable with this correction — it means the rebuild will show *different, correct* data on page 2+ than legacy does today, until legacy itself is fixed.
- **SEVERITY-1, NOT fixed, faithfully reproduced**: update-reactivation bug above. Flag for its own dedicated legacy branch (`MachineMasterDaoImpl.java:82` — remove the hardcoded `setStatus('A')`, let the mapped DTO value through).
- **Faithful reproduction**: dead sort carets (`sortColumn` called by the view, never defined anywhere); the shared `<modal>` directive is unreachable in legacy (never triggered), correctly omitted from the rebuild entirely rather than reproducing genuinely-dead markup.

---

## 2. `doctorSlabMaster` — SEVERITY-1, cross-organization data corruption

**Paths:** `doctorSlabMaster` (legacy `#/doctorSlabMaster` / rebuild `/doctorSlabMaster`, moduleId 1020).

**Critical context before you start:** toggling one doctor's slab status in one unit **silently flips that same doctor's status in every unit/organization they have slab records in.** `DoctorSlabMasterDaoImpl.updateSlabStatusByDoctorId` runs a raw SQL `UPDATE ... WHERE doctor_id=:doctorId` with **no `unit_id`/`organization_id` filter**, even though slab rows are genuinely unit/org-scoped on creation. The frontend never sends `unitId` at all; the `organizationId` it does send is accepted but ignored server-side. **Not fixed** — no safe client-side workaround exists (the bug is entirely in the backend SQL), faithfully reproduced with legacy's real payload shape. **This is the highest-priority queued legacy fix from this entire batch** given its cross-organization blast radius (see the consolidated priority ranking).

**Known testing limitation:** the one doctor seeded in this dev DB has a null `user_id`, so the real `getActiveDoctorList` join (`doctor.m_doctor_master` ⋈ `t_user_unit_mapper` on `user_id`) returns zero rows — the Doctor dropdown is empty on both apps. A full create/toggle round trip could not be demonstrated in this dev environment; this is a seed-data gap, not a code defect (both apps hit the identical real endpoint and get the identical empty result).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Real 2 initial-load calls (`getActiveDoctorList`, `getdoctorSlabList`) both return 200; Doctor dropdown empty (seed-data gap, see above) |
| 2 | Click the `+` button on a slab row without filling Slab To/Percentage | Client-side validation blocks it, no row added — verified live |
| 3 | *(If your review environment has real seed data)* select a doctor, add 2-3 slab tiers, Save | Confirm the exact validation order and message wording (see findings doc — several messages differ subtly between `addSlab` and `saveDoctorSlab`, e.g. "Slab Percentage less than or equals to 100" vs. "Percentage should be less than or equals to 100 !!!") |
| 4 | *(If real seed data available)* toggle one doctor's status in Unit A, then check the same doctor's slabs in Unit B | **Confirm the severity-1 above**: Unit B's status silently flipped too |

**Needs your explicit sign-off on:**
- **SEVERITY-1, NOT fixed, HIGH priority queued fix** — cross-org/unit status-toggle bug above.
- **Faithful reproduction, secondary finding**: pagination is permanently broken (`getdoctorSlabList`'s count is hardcoded `null` server-side → 0 total pages → pagination controls never render, only the first 10 rows are ever reachable through the UI).
- **Faithful reproduction**: no duplicate-slab-set guard (selecting a doctor fresh, without editing, creates an independent new set of rows alongside any existing ones); cosmetic-only "disabled" look on the edit pencil for inactive doctors (still clickable, matches legacy); asymmetric reset behavior between Save-success and Update-success.

---

## 3. `generalInsuranceMaster` — SEVERITY-1 x2 (one screen-specific, one shared/cross-cutting)

**Paths:** `generalInsuranceMaster` (legacy `#/generalInsuranceMaster`, self-registered — the `app.js` copy is dead/commented-out, confirmed / rebuild `/generalInsuranceMaster`, moduleId 646).

**Critical context before you start — two independent severity-1 bugs:**

1. **Zip Code is silently discarded on every Create and every Update.** `GeneralInsuranceRequest.getZipCode()` is overridden to unconditionally `return null;` (a leftover `// TODO Auto-generated method stub`), shadowing the real inherited getter. Create nulls it outright; Update's null-check guard means it's silently skipped instead. User enters a zip code, gets a normal success toast, it's never persisted, on both apps identically.
2. **The shared Country/State/City/Area dropdown endpoint (`VendorController.GetDropdowns`) is completely broken**, real HTTP 500 on real legacy — `t_unit_application_registration_configuration` is missing roughly half the columns (`source_id`, `country_id`, `state_id`, and ~11 more kin/address fields) its own native query selects. **This is the widest-blast-radius finding of the whole session** — it's a shared endpoint, not screen-specific, so it likely affects every other screen that reuses it for address cascading, not just this one. This is the 5th confirmed instance of a recurring entity/schema-drift defect class this project has now hit 5 times; a dedicated project-wide sweep has been approved (proposal to follow separately, not part of this checklist).

Both bugs together mean **the Insurance Company form cannot currently be fully completed or saved on either app** — the Country dropdown never populates, so the required-field chain (Country → State → City → Area) can never resolve, on top of Zip Code never actually persisting even if it could.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | List loads (0 rows in this dev DB); Country dropdown attempts to populate, real 500 in Network tab |
| 2 | Attempt to fill the form | Cannot complete — Country/State/City/Area chain never resolves on either app |
| 3 | *(If your review environment has this endpoint working)* fill a complete form including Zip Code, Save | Confirm bug #1: record saves, Zip Code column is empty in the DB regardless of what was typed |

**Needs your explicit sign-off on:**
- **SEVERITY-1 #1, NOT fixed, queued** — Zip Code silent-discard bug (`GeneralInsuranceRequest.java`, remove the broken `getZipCode()` override).
- **SEVERITY-1 #2, NOT fixed, HIGHEST blast radius, feeds directly into the proposed schema-drift sweep** — shared dropdown-endpoint bug above.
- **Faithful reproduction**: asymmetric validation (Update has 3 minimum-length checks Create doesn't); cascading dropdown resets fire unconditionally on every parent change, even for the placeholder option; no unit/org scoping on the list endpoint (matches legacy exactly, not invented by the rebuild).
- **Disclosed shared-component improvement, not a per-screen deviation requiring separate sign-off**: legacy's status-toggle optimistically flips before the server responds with a dead revert-on-406 handler (the real exception path returns 500, not 406) — the rebuild's `<app-status-toggle>` never mutates optimistically, structurally can't regress into this, per the standing entry-55 policy.

---

## 4. `checklistMaster` — SEVERITY-1, fixed (frontend-only)

**Paths:** `checklistMaster` (legacy `#/checklistMaster` / rebuild `/checklistMaster`, moduleId 526).

**Critical context before you start:** the "Add Checklist Items" search box silently attaches the **wrong** checklist item on Save, in a specific real sequence: select a suggestion (e.g. "Test Checklist Item One", ID=1) from the typeahead, then change your mind and type a different search string *without* picking a new suggestion. Legacy's `isEditable=false` typeahead resets the visible field's bound value to `undefined` on every unconfirmed keystroke, but the item ID from the earlier selection is never cleared — clicking Add then silently pushes the stale ID (item 1) with a blank description, attaching an unrelated item to the checklist. **I proved this live** on real legacy (selected item 1, retyped an unrelated string, triggered Add, confirmed the pushed object was `{checklistItemMasterId: 1, ...}`) and then **re-proved the fix live** on the rebuild (same sequence: `selectedSuggestion` correctly resets to `null`, nothing gets pushed, a clear visible error appears instead — "Please select a checklist item from the list."). **Fixed in the rebuild** (pure frontend bug, no legacy Java change needed, same treatment as `identificationMaster`/`currencyMaster`'s frontend-only fixes — no separate legacy branch required).

**Known testing limitation:** the Role dropdown's backing table has zero seeded OT roles in this dev DB (the real `getActiveOTRoleList` call succeeds, returns an empty list) — since Role is a required field, a full Save round trip could not be demonstrated. Same seed-data-gap class as the other two screens above, not a code defect.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | List loads (0 rows); Stage dropdown has real options (Consent/Pre-Op/Intra-Op/Post-Op/Discharge/Ward); Role dropdown empty (seed-data gap) |
| 2 | Click "Add Checklist", search for an existing item (e.g. "Test"), select the suggestion | Field populates with the selected item's label |
| 3 | Without clicking elsewhere, retype a different, unrelated search string, do NOT select a new suggestion | Field visually shows your new text |
| 4 | Click "Add" | **Confirm the fix**: nothing is added to the list, a visible error appears ("Please select a checklist item from the list.") — compare directly against real legacy, where the SAME sequence silently attaches the stale item with a blank description and a "Success" click-through |
| 5 | *(If your review environment has OT roles seeded)* complete the form fully, Save | Real create round trip |

**Needs your explicit sign-off on:**
- **SEVERITY-1, FIXED in the rebuild, frontend-only** — stale-typeahead-selection bug above. Please confirm you're comfortable with this correction (a genuine behavior change vs. legacy: legacy silently corrupts the checklist's item list in this sequence, the rebuild now blocks it with a visible error).
- **Kept verbatim, investigated and understood, not bugs**: two literal double-slash URLs (`/api/global//getActiveOTRoleList`, `/global/otims//getChecklistMasterDetailsByMasterId`) — one is a genuine backend `@RequestMapping` trailing-slash artifact (Spring's real resolved path), the other a frontend copy/paste quirk that happens to work via Tomcat's slash tolerance; both confirmed live, working, kept byte-identical.
- **Faithful reproduction**: status-toggle always reloads the list at offset 0 (toggling on any page silently jumps back to page 1); the "flag=1" view-items modal clears its list on every click even when reopening for a doctor/checklist with zero items, without closing the modal.

---

## 5. `ageGroupMaster` — no severity-1, two lower-severity write-path findings, faithfully reproduced

**Paths:** `ageGroupMaster` (legacy `#/ageGroupMaster` / rebuild `/ageGroupMaster`, moduleId 416).

Fully live-tested end to end by the orchestrating session (not just the build agent): Create, both-direction status toggle (confirmed via DOM property + direct DB query, not just visual read — an initial screenshot-based read was self-corrected via zoom after a false alarm), edit-blocked-when-inactive, edit-allowed-when-active, Update, and the validation-gap finding below were all exercised against the real backend with real seeded/created data (`TAG1`, `BADRANGE` — both deactivated, not deleted, after verification).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | 0 rows initially (this dev DB had none seeded); all 3 real backend calls confirmed 200 |
| 2 | Add `TAG1`: From=0 Days, To=30 Days, Save | Row appears, active |
| 3 | Toggle status off | Confirmed via direct DB query: `status='I'` |
| 4 | Click Edit on the now-inactive row | **Blocked** — form stays empty, matching legacy's real "deactivated, activate first" guard |
| 5 | Toggle status back on, click Edit | Form populates correctly this time |
| 6 | Change To=45, Update | Confirmed via DB: `age_to_day` changed 30→45, `status` stayed `A` |
| 7 | Add `BADRANGE`: From=1000 Days, To=1 Year (i.e. an inverted range — 1000 days &gt; 365 days), Save | **Confirm the validation-gap finding**: silently accepted and persisted, no error — the day-normalized sanity check only runs when the *unit dropdown selections* satisfy `fromId >= toId`, not when the actual normalized day values are inverted |

**Needs your explicit sign-off on:**
- **Write-path finding, faithfully reproduced (not corrected)**: `updatedBy` is misattributed on every Update — re-derived from the *original creator's* ID (a stale field never refreshed after the edit-load overwrites `$scope.common`), not the actual editing user. Audit-trail-only field, doesn't affect the actual age-range data the screen manages; same treatment as this project's other audit-field-only findings (e.g. the `updatedBy: undefined` precedent).
- **Write-path finding, faithfully reproduced (not corrected)**: the from/to range validation gap demonstrated in step 7 above. A genuinely invalid/inverted range can be silently saved. Please confirm this classification (validation-gap, not data-corruption) is the right one, or if you'd like this escalated to a corrected fix instead.
- **Cosmetic normalization, disclosed**: legacy's one error path uses a raw `alert()` instead of the toast notification every other message on this screen uses — normalized to the standard toast for consistency, same message/trigger.

---

## Summary for your review

| Screen | Severity-1 findings | Disposition | Seed-data gap? |
|---|---|---|---|
| `machineMaster` | 2 (pagination shows wrong entity; update silently reactivates) | Pagination: **corrected in rebuild**. Reactivation: **not fixed, queued** | No |
| `doctorSlabMaster` | 1 (cross-org/unit status corruption) | **Not fixed, queued — HIGHEST priority** | Yes (doctor dropdown empty) |
| `generalInsuranceMaster` | 2 (Zip Code discarded; shared dropdown endpoint 500s) | Both **not fixed, queued** — 2nd bug feeds the proposed schema-drift sweep | Effectively yes (dropdown 500 blocks the form) |
| `checklistMaster` | 1 (stale typeahead selection attaches wrong item) | **Fixed in rebuild** (frontend-only, no legacy branch needed) | Yes (Role dropdown empty) |
| `ageGroupMaster` | 0 (2 lower-severity write-path findings) | Both **faithfully reproduced** | No |

**6 confirmed severity-1/cross-cutting findings from this single 5-screen batch** (5 in the screens above, plus 2 more found during candidate research and already logged separately — `taxMaster`/`pharmacologicalClassificationMaster` schema drift, excluded from this batch entirely). See the consolidated priority ranking (delivered alongside this checklist) for how these stack up against everything else currently queued.

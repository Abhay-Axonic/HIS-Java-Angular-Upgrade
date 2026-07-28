# Batch 26 — Step 6 Domain-Expert Review Checklist

*All 3 screens are through Steps 0-5 on `rebuild/phase4-batch26` (worktree `.claude/worktrees/batch26`, held, not merged, not pushed). This is a deliberately smaller, 3-screen batch — 2 of the original 5 candidates (`serviceMaster`, `subDepartmentMasterUnit`) were disqualified during candidate research (real billing-tier content, contradicting their module tags) and no further clean replacement was found after exhaustive search, matching the precedent of batch 12 (7 instead of 10) and batch 13 (6 instead of 10). See `docs/ANGULAR_PENDING_FINDINGS.md` for the full findings writeups behind each item below. Relevant commits on the branch: `ceb39bf5` (ambulanceTarrifMaster), `685c96e3`/`a2e2eadb` (doctorWise + the revert that moved its legacy fix off this branch), `c3ecf18e` (tariffMaster), `81de04cc` (doc corrections).*

**Setup, once, before starting any of the 3:**
- Worktree `.claude/worktrees/batch26`, branch `rebuild/phase4-batch26`. Confirm `ng serve --port 4311` (from `.claude/worktrees/batch26/new`) and the Java backend (port 8080, `his_local` DB) are both running.
- Legacy screens are reached at `http://localhost:8080/#/<state>`; rebuilt screens at `http://localhost:4311/<path>`.
- Login on both: `Admin` / `12345`, Organization/Unit `DEMO`. Module ids for reference: `tariffMaster` = 609, `ambulanceTarrifMaster` = 816. `doctorWise` (module 658) has no route guard on the rebuild side, matching legacy's own `resolve` block. If you hit "Access Denied" on the rebuild, use the standard console patch: `const p = JSON.parse(localStorage.getItem('permissions')); p.modules.push({id: <id>}); localStorage.setItem('permissions', JSON.stringify(p));` then reload.
- **Confirm-modal: no fix needed anywhere in this batch, built correctly from the start**, matching this session's corrected shared-`<modal>`-directive understanding — `tariffMaster` (Save only, Update has none), `ambulanceTarrifMaster` (Save only, Update has none), `doctorWise` (no confirm-modal gate at all on this screen, structurally not applicable).
- **The Tomcat backend was restarted once during this batch's work** (to deploy the `doctorWise` legacy fix, see below) — if you were mid-session on an unrelated screen when that happened, your browser session may have been silently logged out; just log back in.

---

## 1. `tariffMaster` — SEVERITY-1, review first

**Paths:** `tariffMaster` (legacy `#/tariffMaster` / rebuild `/tariffMaster`, moduleId 609).

**Critical environment note before you start:** on real, unmodified legacy TODAY, **Create, Update, and status-toggle all fail with HTTP 500 on every attempt.** This is a real, confirmed legacy defect (entity/schema drift — the Java entity maps a column, `letter_head_image_tariff`, that does not exist on the real table, confirmed against the tracked DDL source, not just this dev DB), **not a rebuild bug.** Full writeup and proposed fix in `docs/DECISION_NEEDED_TariffMasterLetterHeadImageColumnMissing.md`, held for your approval separately — not part of what you're reviewing in this checklist. Because of this, you cannot currently exercise a full create/update/status-toggle round trip on either app; the table below reflects what IS testable today (List, search, edit-load, and confirming both apps fail identically on writes).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Existing tariff schemes list; a test row `TESTPROBE1`/"Test Probe Desc" was seeded directly via SQL this session (bypassing the broken write path) — safe to leave or deactivate, your call |
| 2 | Click Edit on any row | Form populates correctly (edit-load uses an explicitly-projected query, unaffected by the schema-mismatch bug) |
| 3 | Attempt to Save a brand-new tariff scheme (any app) | Confirm modal → Ok → **HTTP 500**, generic "Something went wrong" error toast on legacy; matching failure on the rebuild (same real backend) |
| 4 | Attempt to Update the seeded `TESTPROBE1` row (any app) | **HTTP 500** on both apps, same root cause |
| 5 | Attempt to toggle `TESTPROBE1`'s status (any app) | **HTTP 500** on both apps, same root cause |
| 6 | Open "Add Tariff Properties" on the form, check Visit Type / Patient Type entries | Selections register in local state (Payment Entitlement / Billing Bed Category sections are commented-out dead markup in legacy, not modeled) |

**Needs your explicit sign-off on:**
- **SEVERITY-1, separately escalated** — the schema-mismatch write-path failure above. Approve/reject the proposed fix (`docs/DECISION_NEEDED_TariffMasterLetterHeadImageColumnMissing.md`) on its own; not gated on this checklist.
- **Faithful reproduction, already-known bug**: the duplicate-check validates Description only (case-sensitive), never Code — a same-Code/different-Desc row would currently pass the check and only then hit the 500 above. Separately tracked in `docs/DECISION_NEEDED_CompanyMasterTariffMasterCodeBlindDuplicateCheck.md`, not fixed here.
- **Faithful reproduction**: status-toggle's "in-use" guard (blocks deactivating a tariff referenced by active orders/contracts) — not independently re-testable right now since status-toggle itself 500s first.
- **Faithful reproduction**: Payment Entitlement is silently carried through unchanged on Update despite no visible UI for it; Billing Bed Category is always omitted from the Update payload. Both match legacy's real (undocumented) behavior.
- **No confirm-modal fix needed** — Save-only gate, correct from the start. Update has no gate, also faithful.

---

## 2. `doctorWise` — SEVERITY-1, now fixed

**Paths:** `doctorWise` (legacy `#/doctorWise` / rebuild `/doctorWise`, module 658, no route guard on either app).

**Critical context before you start:** on real, unmodified legacy, **every search on this screen returned an error, in every input combination** (a backend bug in `SlotMasterDaoImpl.getSlotsForBlockUnblock`, silently surfaced only as an empty results table). **This has since been fixed and independently re-verified** — held on its own dedicated branch, `fix/doctorwise-getslots-time-filter-crash` (commit `96981e30`), **already deployed to the shared dev Tomcat you're testing against right now** (redeployed and restarted during this session — if the timing of that restart logged you out of an unrelated screen mid-session, just log back in). This checklist's steps below assume the fix is live, which it is.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen, Blocking tab active by default | Empty results table until a search is run |
| 2 | Select a Specialty, fill in Date(Form)/Date-Till, leave Time fields blank, Search | Real results return (confirms the fix — this exact input shape 500'd before the fix) |
| 3 | Same search, but fill in Time(From)=09:00, Time(Till)=17:00 | Real results return, filtered by time range (confirms the `CAST` fix for the type-mismatch half of the bug) |
| 4 | Select multiple slots via checkbox, fill in Reason (+ optional Remark), bulk Save | **Two success toasts fire back to back** (server message + a second, hardcoded "Added Successfully.") — a real legacy quirk, faithfully reproduced |
| 5 | Click the single-slot (red ban) icon action on one row (Blocking tab only) | One success toast only (not two); Remark is silently NOT sent even if filled in |
| 6 | Switch to Unblocking tab, note: no single-slot icon exists here, Time fields and Search box are visually present but inert | Bulk checkbox-select + Save is the only way to unblock; Time fields don't affect the search, the search box doesn't filter anything |
| 7 | Select slots that include one already booked (`slot_status_id=4`), attempt to Save | **Entire batch rejected**, none of the selected slots update — even the non-booked ones — with a message listing the conflicting times, including legacy's literal typo ("This slot alredy booked!") |
| 8 | Click "Details" on any row | Read-only popup, always shows exactly that one row's data |

**Needs your explicit sign-off on:**
- **SEVERITY-1, fixed** — the search-crash bug above. Fix is on its own branch (`fix/doctorwise-getslots-time-filter-crash`), already deployed to this dev environment for your testing, held for your merge approval separately from this rebuild checklist.
- **New, deferred finding, not urgent, logged for later**: a *different*, pre-existing bug (`timestamp >= bytea` type mismatch) was found in the same backend method while re-verifying the fix above — only reachable by bypassing the required Date fields' client-side validation, and confirmed in only 1 of 3 similar code branches so far. Tracked as `PENDING-slotmaster-getdatebyzone-bytea-typemismatch`; not blocking anything, flagged for a future dedicated look.
- **Corrected (disclosed, not a faithful-reproduction gap)**: legacy has a real drift bug where a single-slot action on the Unblocking tab silently resets an internal flag back to the Blocking-tab default, risking a second same-tab action incorrectly re-blocking instead of unblocking. The rebuild derives this state from the active tab directly, structurally eliminating the bug rather than reproducing it — please confirm you're comfortable with this correction.
- **Faithful reproduction**: double-toast-on-bulk-Save-only; Remark dropped by the single-slot action; single-slot icon Blocking-tab-only; all-or-nothing already-booked gate with the literal typo; dead Time/Search fields on the Unblocking tab.
- **No confirm-modal fix needed** — this screen has no confirm-modal gate at all, correctly not modeled.

---

## 3. `ambulanceTarrifMaster`

**Paths:** `ambulanceTarrifMaster` (legacy `#/ambulanceTarrifMaster` / rebuild `/ambulanceTarrifMaster`, moduleId 816).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Real form: Tariff Name, Ambulance Provider (hardcoded In House/Out Sourced — not backend-driven, no seed-data gap), Vendor, First Distance, Rate, Additional Mileage (disabled, defaults to 1), Additional Mileage Rate |
| 2 | Fill in a unique name, Save | Confirm modal → Ok → success toast, row appears |
| 3 | Attempt a duplicate name (any case) | Rejected — "Ambulance Tariff Already Exist...!!" (Save's own wording) |
| 4 | Edit a row, change the name, Update | Succeeds directly, **no confirm modal on Update** (asymmetric vs. Save, real and faithful) — duplicate-name rejection here reads "Ambulance Tariff already exits...!!" (note: different text and casing from Save's, both verbatim real) |
| 5 | After any successful Save or Update | List silently resets to page 1 @ page size 10, even if you were on a different page/page-size before — real legacy behavior, faithfully reproduced |
| 6 | Use the search box | Filters only the current page's already-fetched rows, not a server-side search |
| 7 | Toggle any row's status | Switch flips; verify against legacy side by side |

**Needs your explicit sign-off on:**
- **No confirm-modal fix needed** — Save-only gate, correct from the start (initially, the build agent's own static read predicted a possible stuck-modal bug here; live-testing against real legacy proved that wrong before anything was built — the shared directive re-arms correctly, no bug exists).
- **Faithful reproduction**: page-reset-to-1 after every Save/Update, success or failure; duplicate-check is Name-only, case-insensitive, org-scoped; the two different duplicate-error strings between Save and Update.
- **Modeling note, not a bug**: "Ambulance Provider" confirmed to be a hardcoded 2-option list in legacy's own markup, not fed by any backend list — nothing to seed or fix.

---

## Summary for your review

| Screen | Confirm-modal | Duplicate-check | Severity-1 status | Faithfully reproduced |
|---|---|---|---|---|
| tariffMaster | Save only; correct from the start | Server-side, Desc-only, case-sensitive (known bug, separately tracked) | **Write paths (Create/Update/status-toggle) all 500 on real legacy — separately escalated, awaiting your approval, not part of this checklist** | Payment Entitlement silent carry-through; Billing Bed Category omitted on Update; status-toggle in-use guard |
| doctorWise | N/A — no gate on this screen | N/A | **Search-crash bug — fixed, held on its own branch, already deployed to this dev environment** | Double-toast on bulk Save; Remark dropped by single-slot action; all-or-nothing already-booked gate w/ typo; dead Unblocking-tab Time/Search fields |
| ambulanceTarrifMaster | Save only; correct from the start | Server-side, Name-only, case-insensitive, org-scoped | None | Page-reset after Save/Update; two distinct duplicate-error strings; client-side-only search |

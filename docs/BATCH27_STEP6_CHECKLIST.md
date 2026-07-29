# Batch 27 — Step 6 Domain-Expert Review Checklist

*Covers 4 screens across two held branches. **3 are new batch 27 work** (`patientCategory`, `bedMaster`, `tallyInterfacing`, all on `rebuild/phase4-batch27`); **1 is recovered prior-session work** (`siteOfAdministration`, on its own branch `rebuild/phase4-recovered-siteofadministration`, kept separate per your explicit instruction). Both are through Steps 0-5, held, not merged, not pushed. See `docs/ANGULAR_PENDING_FINDINGS.md` for full findings write-ups behind each item below.*

**Setup, once, before starting:**
- **Batch 27** (3 screens): worktree `.claude/worktrees/batch27`, branch `rebuild/phase4-batch27`. `ng serve --port 4312 --proxy-config proxy.conf.json` from `.claude/worktrees/batch27/new`.
- **siteOfAdministration** (recovered work): worktree `.claude/worktrees/recovered-siteofadministration`, branch `rebuild/phase4-recovered-siteofadministration`. `ng serve --port 4313 --proxy-config proxy.conf.json` from that worktree's `new/`.
- Legacy Java backend (port 8080, `his_local` DB) must be running for both.
- Login on both apps: `Admin` / `12345`, Organization/Unit `DEMO`.
- Legacy screens: `http://localhost:8080/#/<state>`. Rebuilt screens: `http://localhost:4312/<path>` or `:4313/<path>`.
- If you hit "Access Denied" on either rebuild, console-patch permissions: `localStorage.setItem('permissions', JSON.stringify({modules:[{id: <id>}]}))`, then reload. Module ids: `patientCategory`=446, `bedMaster`=546, `siteOfAdministration`=753. `tallyInterfacing` has no module gate on either app (matches legacy exactly — no resolve block in its `.state()` registration).
- **Only one legacy session can be active at a time** — logging in on one tab/device invalidates any other active session with a "You will be logged out from previous system" prompt. Expect this if you were mid-session elsewhere.

---

## PART A — New batch 27 work (3 screens)

### 1. `patientCategory` ("Patient Category Master")

**Paths:** legacy `#/patientCategory` / rebuild `/patientCategory`, moduleId 446.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Real category list (`GEN`/"General" pre-existing; test rows from this session's verification already deactivated) |
| 2 | Save a new category (unique code) | Confirm modal → Ok → success toast (raw server text: "Added Successfully."), row appears, form resets |
| 3 | Save a duplicate code+description | Rejected — "Already Exists." (server text, passed through verbatim) |
| 4 | Note: rows with `patientCategoryId` 1 or 2 have no visible toggle/edit controls | UI-only guard, hardcoded IDs (not a status/flag) — legacy's own quirk, not a rebuild bug; backend has no equivalent restriction if called directly |
| 5 | Change page size to 25, start editing a row's Code/Description (don't save), then toggle a *different* row's Active/Inactive switch | On success: your in-progress edit is silently wiped AND the page-size dropdown snaps back to 10 — no toast, no warning. Live-verified by me directly against the rebuilt UI this session. |
| 6 | Try the search box | Fully dead — no filtering happens, matches legacy exactly (no `ng-model`/filter wiring in legacy's own template) |
| 7 | Edit → Update a category | Works directly, no confirm modal (asymmetric vs. Save, faithful) |

**Needs your explicit sign-off on:**
- **Faithful reproduction, not fixed**: the toggleStatus silent form/page-size reset (step 5). Real, visible-if-you're-looking side effect — no data loss, no error, but no warning either. Your call on whether this deserves a future disclosed fix.
- **Faithful reproduction**: dead search box, decorative sort carets, ids 1/2 UI-only hidden, no field length limits, raw server-message passthrough on all 3 growl calls.
- **No confirm-modal fix needed** — Save-only gate, rearms correctly on both outcomes (matches the established shared-directive mechanism).

---

### 2. `bedMaster` ("Ward Bed Master") — SEVERITY-1 x2, review first

**Paths:** legacy `#/bedMaster` / rebuild `/bedMaster`, moduleId 546. Two tabs: Normal Bed, Moving Bed.

**Critical environment note before you start:** on real, unmodified legacy TODAY:
1. **Save (`addBed`) fails on every single attempt, both tabs.** Root cause confirmed independently by me: `BedMasterDaoImpl.java:1527` runs `select allocated_beds from t_unit_application_billing_configuration ...` — that column does not exist in the real table (confirmed via direct `psql \d`). The exception is silently swallowed server-side, surfacing as a generic error toast. **Not fixed** — needs its own dedicated branch/DB decision, out of scope for this Angular-rebuild batch.
2. **Moving Bed's Update silently claims success but never persists.** Confirmed twice via direct `psql` — the DB row is unchanged despite a "Updated Successfully." toast. Root cause narrowed to the shared `findById`/`update` DAO path but not fully pinned down (the base DAO class has no source in this codebase to inspect further). **Not fixed** — no safe frontend workaround exists for a backend bug like this.

Because of #1, you cannot exercise a full Save round-trip on either app right now. The table below reflects what IS testable.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen, Normal Bed tab | Existing bed list; ward/room/bed-category dropdowns populate |
| 2 | Select a Ward, then a Room | Bed category + billing category auto-populate client-side (no extra API call) |
| 3 | Attempt to Save a new bed (either tab) | Confirm modal → Ok → **error toast, 0 rows written** — matches on both apps (same real backend) |
| 4 | Edit a vacant bed → Update | **Works correctly** on Normal Bed (only non-broken write path) — real DB change, form resets |
| 5 | Edit a vacant Moving Bed → Update | **Claims success, but the DB value is unchanged** — matches on both apps, confirmed via direct DB check |
| 6 | Edit an occupied bed (either tab) | 409 — "Bed is used, you can't change status." |
| 7 | Toggle status on an occupied bed, Normal Bed tab | 409, DB unchanged, checkbox correctly reverts to its prior state |
| 8 | Toggle status on an occupied bed, Moving Bed tab | 409, DB unchanged, but **checkbox does NOT revert** — a real, tab-specific gap (legacy's `updateStatus` is called with 2 args on this tab vs. 3 on Normal Bed, so its revert logic silently no-ops on the wrong array index) |
| 9 | Server-side search | Works correctly, real network call |

**Needs your explicit sign-off on:**
- **SEVERITY-1 #1, separately escalated** — Save/`addBed` schema-drift failure. Approve/reject a proposed fix path (additive `ALTER TABLE` to add `allocated_beds`, or removing the dead query — needs a decision, not yet drafted as a formal decision doc; let me know if you want one).
- **SEVERITY-1 #2, separately escalated** — Moving Bed's silent Update no-op. This is a genuinely silent bug (claims success, isn't true) — normally this project's default is to correct silent bugs with disclosure, but since the root cause is server-side and the client can't detect the falsehood without extra work, it was faithfully reproduced. Your call on whether this needs a deeper backend investigation before your review, or whether reporting-and-holding is sufficient for now.
- **Faithful reproduction**: Moving Bed's non-reverting 409 checkbox (item 8).
- **No confirm-modal fix needed** — Save-only gate, correct from the start.

---

### 3. `tallyInterfacing` ("Tally Interfacing") — most complex screen this batch

**Paths:** legacy `#/tallyInterfacing` / rebuild `/tallyInterfacing`, **no module gate on either app** (matches legacy's own unguarded state).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Immediate "Please Select Unit" warning toast on page load, before you touch anything — a real legacy quirk (search fires unconditionally at controller init), faithfully reproduced |
| 2 | Select Unit = DEMO, Search | 5 real API calls (ledger data for IP/OP/Inventory/Doctor's Share + ledger names); this dev DB has zero ledger transactions, so each tab shows "Data not found for ..." — a genuine data gap, not a bug (confirmed via direct `psql`, 0 rows) |
| 3 | Click Transfer (any tab) | Real attempt to reach `127.0.0.1:9000` (the configured local Tally instance) → fails → "Tally Software is not Running!" — correct graceful-failure handling on both apps, since no real Tally instance exists in this environment |
| 4 | Click Export | "No Data Found" (empty dataset) |
| 5 | Open Tally Ledger modal → Refresh / Create Ledger | Same graceful "Tally Software is not Running!" failure |
| 6 | Switch tabs | Purely client-side, zero network calls |
| 7 | "Bill Amount"/"Instrument No." columns | **Always blank on all 4 tabs** — a real template/API field-name mismatch (legacy binds `billAmount`/`instrumentNo`, the real API fields are `billingAmount`/`instrumentNumber`), silently blank with no error. Faithfully reproduced, not corrected. |
| 8 | "Narration" column, Inventory/Doctor's Share tabs only | Same bug class, narrower scope — blank only on these 2 tabs (OPD/IPD bind the correct field name) |

**Needs your explicit sign-off on:**
- **Backend finding, reported only, not fixed**: `TallyIntegrationServiceImpl.sendRequestToTally` hardcodes error code `"500"` on every failure, making the frontend's friendly `404`/"Tally Software is not Running!" branch unreachable dead code on the backend-relay path (only triggered for units with `is_tally_local_host='N'` — this dev DB's config doesn't exercise it, defaults to the direct-post path used above). Not severity-1 (latent, not currently user-facing here), but worth a decision on whether to fix.
- **Faithful reproduction, both silent**: the Bill Amount/Instrument No. blank columns (item 7) and the Narration blank columns (item 8) — both are field-name mismatches with no visible error signal. Per this project's default policy, silent bugs get corrected with disclosure — these were instead faithfully reproduced on the reasoning that they're cosmetically low-stakes, not data-loss-class. **This is a judgment call worth your explicit confirmation**, not an automatic pass.
- **Faithful reproduction**: on-load "Please Select Unit" warning; the 33-item hardcoded "Under Group" dropdown (verbatim, not fetched from Tally).
- **Zero-impact micro-fix, disclosed not silent**: the Tally Ledger picker's `<option value="">` was corrected in the rebuild (legacy's own dropdown is provably inert — the selected value is never read anywhere in the controller).
- **No confirm-modal** — this screen has none, correctly not modeled.

---

## PART B — Recovered prior-session work (1 screen)

### 4. `siteOfAdministration` ("Site of Administration") — recovered, not new work

**Paths:** legacy `#/siteOfAdministration` / rebuild (port 4313) `/siteOfAdministration`, moduleId 753.

**Context, so you know what you're reviewing:** this screen's Step 3 rebuild was found sitting uncommitted on a prior session's branch, never finished or documented. A separate agent run (Codex/GPT-5) independently touched it too, wiring routes and fixing the same comment issue I'd independently flagged. Both contributions were verified before being trusted; the full trail is in `docs/ANGULAR_PENDING_FINDINGS.md`'s `PENDING-siteofadministration-step1-audit` entry and this session's `SESSION_LOG.md` entries. This is the only item in this checklist that isn't fresh batch work — flagging clearly per your instruction.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Real site list |
| 2 | Save a new site (unique code) | Confirm modal → Ok → success, row appears |
| 3 | Save a **duplicate code**, different name | **Succeeds** — no duplicate-code check exists anywhere (confirmed live and at the DB schema level: `public.m_site_of_admininstration` has no unique constraint on `site_code`, only the primary key). Faithful reproduction — this is real legacy behavior, not something the rebuild introduced. |
| 4 | Edit → Update a site | Works directly, no confirm modal (asymmetric vs. Save, faithful, matches legacy) |
| 5 | Save the same or a different site a second time, same page session | **Confirm modal reappears correctly** — the shared `<modal>` directive rearms `popUpFlag` synchronously on every Ok click, live-verified by me via a real DOM click on the actual Ok button (not just `$apply` shortcuts) |
| 6 | Toggle a row's status | Succeeds, form resets and list reloads (matches legacy; note this differs from `maritalStatusMaster`'s sibling, which does neither) |
| 7 | Try the search box | Client-side filter only — legacy defines a real search API function but the template never calls it, so it's dead from the UI's perspective; rebuild matches |

**Needs your explicit sign-off on:**
- **Faithful reproduction, not a bug**: no duplicate-site-code check (item 3).
- **Confirm-modal understanding corrected this session** (item 5) — the original draft's comment incorrectly attributed the rearm behavior to this screen's own `initMaster()` (which never touches `popUpFlag`) and cited an unrelated finding (entry 55, a different bug about the modal's × button). Corrected to cite the real, established mechanism (`PENDING-confirmmodal-shared-directive-reaudit`). The resulting *code* was already correct either way — only the reasoning/comment was wrong, now fixed.
- **Faithful reproduction**: Update has no confirm-modal gate; status-toggle reloads list + resets form (unlike `maritalStatusMaster`); search box is dead/client-side-only.

---

## Summary for your review

| Screen | Branch | Confirm-modal | Severity-1 status | Faithfully reproduced |
|---|---|---|---|---|
| **patientCategory** | batch27 | Save only; correct | None | ToggleStatus silent form/page-size reset; dead search; decorative sort; ids 1/2 UI-hidden; no field limits; raw server messages |
| **bedMaster** | batch27 | Save only; correct | **2 — Save fully broken (schema drift); Moving Bed Update silently no-ops. Both separately escalated, awaiting your approval, not part of routine review.** | Moving Bed's non-reverting 409 checkbox |
| **tallyInterfacing** | batch27 | N/A — no gate | None (1 backend finding reported, not severity-1) | Bill Amount/Instrument No./Narration blank columns (silent field-mismatch, judgment call flagged above); on-load warning; hardcoded Under Group list |
| **siteOfAdministration** | recovered-siteofadministration | Save only; correct, rearms on both outcomes (re-verified live) | None | No duplicate-code check; Update has no gate; status-toggle resets form+reloads; dead search box |

Both branches are held, unmerged, unpushed. Standing by for your review.

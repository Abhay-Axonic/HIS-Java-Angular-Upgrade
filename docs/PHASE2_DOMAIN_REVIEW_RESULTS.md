# Phase 2 pilot — domain-expert review results

*Filled in by whoever performs the manual review described in `docs/PHASE2_DOMAIN_REVIEW_GUIDE.md`. One table per screen. Add a row per finding; leave a screen's table with just the header row if nothing was found. Severity: **Blocker** (wrong data / wrong action taken, must fix before this pilot is considered done), **Real gap** (observable behavior differs from legacy, needs the same flag-and-decide treatment as the rest of this pilot's findings), **Cosmetic** (wording/spacing/visual only, no functional difference), **Question** (not sure if it's a bug — needs a second opinion before it's classified).*

Reviewer name/role: AI session (browser-automation interaction sweep), per owner instruction — first automated pass at interaction-level testing; not a substitute for a human domain-expert pass, but covers what golden-trace tooling couldn't (clicks/saves/toggles).
Date(s) of review: 2026-07-18
Environment used: local dev, Tomcat 9 + Postgres (`his_local`) at `localhost:8080` (legacy), `ng serve` proxying to it at `localhost:4200` (rebuild), `Admin`/`DEMO` org+unit test session.

---

## billingGroupMaster

| # | Step / area | Expected (legacy) | Actual (rebuild) | Severity | Notes |
|---|---|---|---|---|---|
| 1 | Status toggle (real `icdkcd`/`icd-101` rows) | Click flips switch visually (teal↔gray), `PUT changeBillingGroupMasterStatus` 200, list re-renders | Click fires equivalent PUT 200, data genuinely changes server-side, but switch **never visually changes**, in either direction | Real gap | Root-caused as 2 stacked bugs: `.slider` CSS has no `:checked` state at all (`new/src/styles.scss`), plus `toggleStatus()` mutates the list signal's array in place rather than reloading. Logged as finding #5. |

## insuranceMaster

| # | Step / area | Expected (legacy) | Actual (rebuild) | Severity | Notes |
|---|---|---|---|---|---|
| 1 | List load / save (`TESTINS1` record) | List loads, save succeeds | Initially 500'd on both list and save — traced to 2 missing DB columns on `adt.t_insurance` (`general_ledger_id`, `block_report_print`), a local-dev-DB drift issue, not a rebuild bug. Fixed (additive `ALTER TABLE`, owner-approved), then full save/list round-trip confirmed working on both apps. | Environment blocker (fixed) | Logged as `BR-local-dev-db-drift-insurance-misreports`. |
| 2 | Status toggle (same `TESTINS1` record) | Visually flips | Same CSS bug as billingGroupMaster — static gray switch regardless of state, though the underlying status change succeeds | Real gap | Confirms finding #5 is systemic (shared `.switch`/`.slider` CSS), not isolated to billingGroupMaster. |

**Ledger-backfill real-data check:** Not independently re-tested this session (no real GL master data exists in this dev DB — `m_general_ledger_master` has 0 rows) — the fix itself (entry 4 in `ANGULAR_PENDING_FINDINGS.md`) was already logged in a prior session; this session's insurance work was focused on the DB-drift blocker and toggle CSS bug instead.

## inputOutputCharts

| # | Step / area | Expected (legacy) | Actual (rebuild) | Severity | Notes |
|---|---|---|---|---|---|
| 1 | Patient search (real seeded patient `TESTUHID1`) | UHID-only or name-only search finds the patient | Rebuild's single search box sends the same typed term as **both** `uhIdNumber` and `patientName` simultaneously; backend AND-combines populated fields, so a combined search returns 0 results even though either field alone would match | **Functional break, flagged urgently** | Finding #8. No real patient/ward/admission data existed in this dev DB before this session — seeded a minimal test patient (`TESTUHID1`), ward (`Test Ward`/bed `B-101`), and admission to make this screen testable at all (`BR-local-dev-db-drift-insurance-misreports`). |
| 2 | Start I/O, add intake row | Confirm modal → "I/O chart has started" toast → row entry | **Untestable via the rebuild's actual UI** — since no search term can ever return a match (see row 1 above), a patient can never be selected through the real interface, blocking everything downstream | Blocked by finding #8 | Legacy fully confirmed working end-to-end (real "I/O chart has started" toast, patient detail panel populated with seeded ward/bed/admission data). |

**Ward-scoping check:** `sessionStorage.nursingStationsWardsList` was `null`/unpopulated for the `Admin` test session on legacy too (this account has no nurse-role/ward assignment — confirmed this is a test-account limitation, not a rebuild bug, matching the review guide's own caveat). For testing purposes only, the key was manually injected (`[1]`, matching the seeded ward) on both apps rather than building out full nurse-dashboard-role infrastructure — this let intake/output data flow correctly once a patient could be selected, but doesn't independently validate the *dashboard's own* ward-population logic (out of scope this session).

## organizationMaster

| # | Step / area | Expected (legacy) | Actual (rebuild) | Severity | Notes |
|---|---|---|---|---|---|
| 1 | Access control | `Admin`/`DEMO` (not a superuser) gets "Access Denied" — this screen is deliberately superuser-gated (`isSuperUser=='Y'` required in the real live state definition, confirmed by reading `app.js` directly) | Full form + data table render normally, fully interactive, no denial | **Systemic, urgent** | Finding #6. Confirmed via raw `curl` that the backend itself only checks *authentication*, not *module/role authorization* — that check exists **only** in legacy's frontend router, which none of the 10 rebuilt routes reproduce. Not isolated to this screen — applies to all 10. |

Functional parity testing (save/toggle/edit) **not possible** with the only available test credential (correctly denied by legacy) — would need a superuser test account.

**Logo-upload progress check:** not tested this session (blocked by the access-control item above — couldn't reach the real form to test upload behavior as this test user).

## MISReports

| # | Step / area | Expected (legacy) | Actual (rebuild) | Severity | Notes |
|---|---|---|---|---|---|
| 1 | Report-group tree + parameter rendering | Selecting a group/report renders the configured parameter controls | Initially both apps showed nothing — 0 report groups seeded in this dev DB (not a bug; legacy's own controller actually threw an uncaught JS error on the empty-list edge case, rebuild degraded gracefully instead). Seeded one minimal synthetic report (5 parameters: `DatePickerFrom`/`DatePickerTo`/`text`/`unit`/`org`) to make this screen testable at all. | Environment blocker (fixed) | Logged as `BR-local-dev-db-drift-insurance-misreports`. Discovered along the way that per-user report visibility is gated by a `t_user_unit_mapper.mis_report_ids` JSON mapping, and that `m_reports_details.view_name` is a literal dynamic-SQL string, not a label — both undocumented pitfalls for future test-data seeding in this DB. |
| 2 | `unit`/`user`/`org` parameter controls | Legacy renders real `<select>` dropdowns for these | Rebuild hard-disables them as read-only text fields prefilled with the session's own ID (`<!-- auto-populated from session, not user-editable -->`) | Real gap, not urgent | Finding #7. Looks like a deliberate simplification but isn't in the review guide's documented-deviations list; open question is whether any real report relies on cross-unit reporting (untestable with only a synthetic report). |

**Reports tested:** one synthetic "Test Report" (`DatePickerFrom`, `DatePickerTo`, `text`, `unit`, `org` control types only) — not a real production report, and its `action` doesn't point at a real report-generation implementation, so View/Export/PDF output was never tested, only parameter-control rendering. `ItemSearch`/`Servicesearch`/`DiagnosiSearch`/`VenderSearch` and actual report execution remain untested.

---

## Overall summary

- **Blockers found:** 2 environment blockers (insuranceMaster DB-drift, MISReports empty seed data), both fixed this session with owner approval, additive-only, logged as `BR-local-dev-db-drift-insurance-misreports`. 1 test-account limitation (organizationMaster needs a superuser account; inputOutputCharts' ward-scoping needs a nurse-role account — both out of scope this session).
- **Real gaps found (need owner decision):** 4, all logged in `docs/ANGULAR_PENDING_FINDINGS.md`:
  - #5 — billingGroupMaster/insuranceMaster (and likely organizationMaster) status-toggle CSS bug + fragile signal mutation
  - #6 — **systemic, urgent**: zero access control on any of the 10 rebuilt routes (real authorization gap, not cosmetic)
  - #7 — MISReports' `unit`/`user`/`org` parameter controls hard-disabled vs. legacy's real dropdowns
  - #8 — **functional break, urgent**: inputOutputCharts' patient search is broken for virtually all real searches (AND-combines UHID+name)
- **Cosmetic-only differences:** none independently confirmed this session beyond what prior sessions already logged (insuranceMaster's redundant validation, ledger-backfill race, etc.).
- **Open questions:** whether #7 (unit/org/user fields) is acceptable depends on whether any real report needs cross-unit reporting — needs the real report catalog, not this session's synthetic test data, to answer.
- **Recommendation on proceeding to Phase 3:** **not yet** — two of the four real findings (#6 access control, #8 search bug) are severe enough that they should be resolved (or at minimum, explicitly accepted with a documented remediation plan) before the rebuild template/process scales to more screens. The toggle CSS bug (#5) is cheap to fix and worth doing before Phase 3 too, since it will otherwise be copy-pasted into every future screen that uses the same shared switch component. See the full session write-up for batch 2 (GRN, assetCategoryMaster, assigningPickerDispenser, periodicityService, implantMaster) findings, which reinforce the same systemic issues (#5's CSS bug independently reproduced on `assetCategoryMaster`).

# Phase 2 pilot — domain-expert review results

*Filled in by whoever performs the manual review described in `docs/PHASE2_DOMAIN_REVIEW_GUIDE.md`. One table per screen. Add a row per finding; leave a screen's table with just the header row if nothing was found. Severity: **Blocker** (wrong data / wrong action taken, must fix before this pilot is considered done), **Real gap** (observable behavior differs from legacy, needs the same flag-and-decide treatment as the rest of this pilot's findings), **Cosmetic** (wording/spacing/visual only, no functional difference), **Question** (not sure if it's a bug — needs a second opinion before it's classified).*

Reviewer name/role: _____________________
Date(s) of review: _____________________
Environment used (dev/staging host, org/unit tested against): _____________________

---

## billingGroupMaster

| # | Step / area | Expected (legacy) | Actual (rebuild) | Severity | Notes |
|---|---|---|---|---|---|
| | | | | | |

## insuranceMaster

| # | Step / area | Expected (legacy) | Actual (rebuild) | Severity | Notes |
|---|---|---|---|---|---|
| | | | | | |

**Ledger-backfill real-data check** (see review guide's specific checklist item): does the "General Ledger" column ever show blank on the rebuild across repeated fresh page loads? Y/N: _____ Notes: _____________________

## inputOutputCharts

| # | Step / area | Expected (legacy) | Actual (rebuild) | Severity | Notes |
|---|---|---|---|---|---|
| | | | | | |

**Ward-scoping check** (see review guide's specific checklist item): confirmed `sessionStorage.nursingStationsWardsList` populated when reached via a real legacy session? Y/N: _____ Search results correctly ward-scoped, matching legacy? Y/N: _____ Notes: _____________________

## organizationMaster

| # | Step / area | Expected (legacy) | Actual (rebuild) | Severity | Notes |
|---|---|---|---|---|---|
| | | | | | |

**Logo-upload progress check** (see review guide's specific checklist item — single-screen half only, cross-screen half with `hospitalMasterController` not yet testable): progress indicator behaves correctly standalone? Y/N: _____ Notes: _____________________

## MISReports

| # | Step / area | Expected (legacy) | Actual (rebuild) | Severity | Notes |
|---|---|---|---|---|---|
| | | | | | |

**Reports tested** (list which reports/parameter combinations were actually exercised, since one report working doesn't confirm the rendering engine for all control types):

---

## Overall summary (fill in last)

- Blockers found: _____
- Real gaps found (need owner decision): _____
- Cosmetic-only differences: _____
- Open questions: _____
- Recommendation on proceeding to Phase 3: _____________________

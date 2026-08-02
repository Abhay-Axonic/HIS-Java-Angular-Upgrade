# Step 6 Review Queue — Consolidated (August 2026)

Produced from `docs/SME_GAP_ANALYSIS.md` Step B directive.
**Purpose:** Single document the owner can work straight through during a verification sprint — no need to reconstruct context per batch.

---

## How to Use This Queue

1. Work in the order below — batches are ranked closest-to-main first, so conflicts are minimized.
2. For each batch: read the branch's individual SUMMARY section, then the per-screen checklist items.
3. Each screen needs: load the Angular rebuild live, click through every control (create, edit, toggle, search, pagination, validation), confirm parity with legacy behavior.
4. If something's wrong: flag it here for the next session — don't try to fix during review.
5. After completing a batch: mark it here (date + initials), the branch can be merged.

---

## Queue Overview

| Order | Batch | Screens | Staleness | Risk factors |
|-------|-------|---------|-----------|-------------|
| 1 | 29 | 3 | 142 behind | Easiest — closest to main |
| 2 | 27 | 3 | 182 behind | Patient-category is clinical-adjacent |
| 3 | 26 | 3 | 198 behind | tariffMaster has BR-101 fix held separately |
| 4 | 25 | 5 | 206 behind | aiButtonConfiguration has severity-1 findings |
| 5 | 24 | 5 | 224 behind | checklist-equipment-master had BR-122 IDOR fix |
| 6 | 21 | 5 | 306 behind | diet-type-master is 6b-gated |
| 7 | 20 | 7 | 306 behind | Deepest staleness, highest screen count |

**Total: 31 screens across 7 batches.**

---

## Batch 29 — `rebuild/phase4-batch29` (3 screens, 142 behind main)

**Last commit:** `b5032978` — 2026-07-29
**Screens:** `asset-issue-indent`, `key-ingredients-master`, `rack-master`
**Conflicts with main:** `app.routes.ts`, `shell-nav.data.ts`, 3 doc files
**Known issues:** None. Cleanest batch in the queue.

### asset-issue-indent
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter by name/code, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### key-ingredients-master
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter by name/code, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### rack-master
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter by name/code, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

---

## Batch 27 — `rebuild/phase4-batch27` (3 screens, 182 behind main)

**Last commit:** `f80aa27c` — 2026-07-29
**Screens:** `bed-master`, `patient-category-master`, `tally-interfacing`
**Conflicts with main:** `app.routes.ts`, `shell-nav.data.ts`, `ANGULAR_PENDING_FINDINGS.md`
**Known issues:** `patient-category-master` is domain-adjacent (patient categorization affects billing), but the screen itself is a simple CRUD master.

### bed-master
- [ ] Create: fill form with bed number, ward, room, submit, confirm DB row created
- [ ] Edit: load existing, reassign to different ward, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter by bed number/ward, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### patient-category-master
- [ ] Create: fill form (code, description, billing category), submit, confirm DB row created
- [ ] Edit: load existing, modify category, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter by name/code, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Note:** Domain-adjacent — patient categories may affect billing calculations. No SME needed for parity verification, but flag any unexpected behavior during review.
- **Reviewed:** ☐  Date: ___  Initials: ___

### tally-interfacing
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

---

## Batch 26 — `rebuild/phase4-batch26` (3 screens, 198 behind main)

**Last commit:** `81de04cc` — 2026-07-28
**Screens:** `ambulance-tarrif-master`, `doctor-wise`, `tariff-master`
**Conflicts with main:** `app.routes.ts`, `shell-nav.data.ts`, `ANGULAR_PENDING_FINDINGS.md`
**Known issues:** `tariff-master` has BR-101 (missing DB column) — the fix is on a separate branch (`fix/tariffmaster-missing-columns`) and should be merged BEFORE or WITH this batch to avoid a broken screen.

### ambulance-tarrif-master
- [ ] Create: fill form (tariff details, ambulance type), submit, confirm DB row created
- [ ] Edit: load existing, modify rate, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter by type/rate, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### doctor-wise
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter by doctor name/specialty, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### tariff-master
- [ ] ⚠️ **PRE-REVIEW FIX NEEDED:** BR-101 (missing DB column) — merge `fix/tariffmaster-missing-columns` first, or screen will 500 on create.
- [ ] Create: fill form (tariff code, description, rate, tax), submit, confirm DB row created
- [ ] Edit: load existing, modify rate, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter by code/description, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

---

## Batch 25 — `rebuild/phase4-batch25` (5 screens, 206 behind main)

**Last commit:** `d2028092` — 2026-07-28
**Screens:** `ai-button-configuration`, `billing-bed-category-master`, `icu-type-master`, `patient-source-master`, `referral-type-master`
**Conflicts with main:** `app.routes.ts`, `shell-nav.data.ts`, 4 doc files
**Known issues:** `aiButtonConfiguration` has documented SEVERITY-1 findings — these were disclosed during the original build, not fixed. The screen should be reviewed with those findings in mind.

### ai-button-configuration
- [ ] ⚠️ **SEVERITY-1 FINDINGS DOCUMENTED:** Review the `PENDING-aibuttonconfiguration-*` entries in `ANGULAR_PENDING_FINDINGS.md` before testing.
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### billing-bed-category-master
- [ ] Create: fill form (bed category, billing rate), submit, confirm DB row created
- [ ] Edit: load existing, modify rate, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter by category/rate, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### icu-type-master
- [ ] Create: fill form (ICU type code, description, rate), submit, confirm DB row created
- [ ] Edit: load existing, modify rate, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter by type/code, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### patient-source-master
- [ ] Create: fill form (source code, description), submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### referral-type-master
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

---

## Batch 24 — `rebuild/phase4-batch24` (5 screens, 224 behind main)

**Last commit:** `fa811897` — 2026-07-28
**Screens:** `autoclave-master`, `checklist-equipment-mapper`, `checklist-equipment-master`, `checklist-item-master`, `vital-qualifier-master`
**Conflicts with main:** `app.routes.ts`, `shell-nav.data.ts`
**Known issues:** `checklist-equipment-master` had a cross-tenant IDOR fix (BR-122 — now merged into main). The rebuild should be verified against the now-fixed backend.

### autoclave-master
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### checklist-equipment-mapper
- [ ] Create: select equipment + checklist, map, submit, confirm DB row created
- [ ] Edit: remap, save, confirm update
- [ ] Delete mapping: confirm removal
- [ ] Search: filter by equipment/checklist, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### checklist-equipment-master
- [ ] ⚠️ **BACKEND CHANGED:** BR-122 (cross-tenant IDOR fix) was merged into main. The backend now verifies tenant ownership. Verify the rebuild works correctly with the fixed backend.
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### checklist-item-master
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### vital-qualifier-master
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

---

## Batch 21 — `rebuild/phase4-batch21` (5 screens, 306 behind main)

**Last commit:** `6d1318ed` — 2026-07-28
**Screens:** `diet-type-master`, `generic-schedule-master`, `growth-chart-master`, `occupancy-unit-master`, `wrapper-expiry-master`
**Conflicts with main:** `app.routes.ts`, `shell-nav.data.ts`
**Known issues:** `diet-type-master` is flagged as 6b-gated (it feeds dietary workflows — domain-adjacent to clinical nutrition). Second-most-stale branch.

### diet-type-master
- [ ] ⚠️ **DOMAIN-ADJACENT:** Diet type feeds clinical nutrition workflows. Verify content parity with legacy — flag any unexpected behavior.
- [ ] Create: fill form (diet type code, description), submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### generic-schedule-master
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### growth-chart-master
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### occupancy-unit-master
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### wrapper-expiry-master
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

---

## Batch 20 — `rebuild/phase4-batch20` (7 screens, 306 behind main)

**Last commit:** `a564d5c7` — 2026-07-26
**Screens:** `access-control1`, `add-new-linen-return`, `asset-pms-scheduler`, `call`, `incident-master`, `linen-return`, `linen-return-details`
**Conflicts with main:** `app.routes.ts`, `shell-nav.data.ts`
**Known issues:** Oldest branch, deepest staleness. Largest screen count (7). Review this LAST — any rebase issues from earlier batches will cascade least if discovered here.

### access-control1
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### add-new-linen-return
- [ ] Create: fill form with linen items, submit, confirm DB row created
- [ ] Edit: load existing, add/remove items, save, confirm update
- [ ] Cancel: initiate cancel flow, confirm status change
- [ ] Search: filter by date/status, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### asset-pms-scheduler
- [ ] Create: fill form (asset, schedule details), submit, confirm DB row created
- [ ] Edit: load existing, modify schedule, save, confirm update
- [ ] Search: filter by asset/schedule, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### call
- [ ] Create: fill form (call details, department, counter), submit, confirm DB row created
- [ ] Edit: load existing, reassign department, save, confirm update
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- [ ] **Known fix in commit:** Department/Counter modal crash on Submit was fixed (Step 4/5 live verification)
- **Reviewed:** ☐  Date: ___  Initials: ___

### incident-master
- [ ] Create: fill form (incident type, severity, description), submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Toggle status: activate/deactivate, confirm grid refresh
- [ ] Search: filter by type/severity, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### linen-return
- [ ] Create: fill form, submit, confirm DB row created
- [ ] Edit: load existing, modify, save, confirm update
- [ ] Search: filter, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

### linen-return-details
- [ ] Create: select linen return + items, submit, confirm DB row created
- [ ] Edit: load existing, modify items, save, confirm update
- [ ] Search: filter by return/item, confirm results
- [ ] Pagination: confirm page navigation works
- [ ] Validation: empty required fields, confirm error messages
- **Reviewed:** ☐  Date: ___  Initials: ___

---

## Pre-Review Actions (do before starting review)

1. [ ] **Merge BR-101 fix** for tariffMaster: `fix/tariffmaster-missing-columns` → main (batch 26 depends on this)
2. [ ] **Review aiButtonConfiguration findings** in `ANGULAR_PENDING_FINDINGS.md` (batch 25)
3. [ ] **Confirm Tomcat is running** with the latest main JARs before starting any batch review
4. [ ] **Confirm Angular dev server** is accessible and build is clean: `cd new && npx ng serve`

## Post-Review Merge Order

After each batch passes Step 6, merge in this order to minimize conflict resolution:
1. Batch 29 → 2. Batch 27 → 3. Batch 26 → 4. Batch 25 → 5. Batch 24 → 6. Batch 21 → 7. Batch 20

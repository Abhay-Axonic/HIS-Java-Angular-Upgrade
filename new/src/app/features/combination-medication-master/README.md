# combination-medication-master — parity rebuild of the legacy Combination Medication Master screen

Legacy source:
- template: `views/PharmacyInventory/combination-medication-master.html` (228 lines)
- controller: the generic `MasterController` (`controller/common-controller.js` — NO screen-specific logic)
- legacy state: `combinationMedicationMaster`, module id **472** — hand-confirmed 2026-08-09:
  app.js:7870 `.state('combinationMedicationMaster', { url: '/combinationMedicationMaster',
  templateUrl: 'views/PharmacyInventory/combination-medication-master.html',
  controller: "MasterController", title: 'Combination Medication Master' })` — REGISTERED (not commented),
  and the privilege tree carries node 472 'Combination Medication', parent 457 (Pharmacy) -> 369 (Global Master),
  actionLink `#/combinationMedicationMaster` (role-1 set, 1,077 modules; the guard's store holds it).

## THE LEGACY IS A STATIC SHELL (hand-confirmed, the central finding)

The legacy template has **ZERO ng-\* bindings** (no ng-model/ng-click/ng-options/ng-repeat),
**ZERO API calls**, and no controller logic: every select is an empty `<option></option>`, the grid holds
one hardcoded empty row, the Save anchor targets a `#confirmLogout` modal that does not exist in the
template (a dead link), and the search box is inert. The MasterController is generic boilerplate
(date pickers, education/experience rows) with no combination behaviour. No combination API constants
exist in the constants files. There was therefore **no legacy behaviour to go wrong** — and nothing to
be byte-parity with. The rebuild reproduces the form + grid chrome and wires the endpoints that
demonstrably exist.

## Endpoints (all verified by CALLING with the cached token — QF-2026-08-09-E: call, don't grep)

| Purpose | Endpoint | Handler | Verified |
|---|---|---|---|
| Generic list (the "Generic" select) | GET /api/globals/generics/{orgId} | GenericController (line 57) | **200** (real rows: Paracetamol…) |
| Combination medicines (the "Combination Medicine" select AND the grid) | GET /api/globals/generics/IsCombination/{flag} | GenericController (line 69) | **200** ([] in dev) |
| Clinical unit of measurement (the strength-unit select) | GET /api/global/uom/units/{orgId} | GlobalController (line 131) | **200** ([] in dev) |
| Edit detail | GET /api/globals/generics/getCombinationDetail/{id} | GenericController (line 94) | **200** ([] in dev) |
| Status toggle | PATCH /api/globals/generics/{id} {status} | GenericController (line 119) | **201** (idempotent probe: same status, "Generic Updated Successfully") |
| **SAVE — NO ENDPOINT** | POST /api/globals/generics/ | GenericController (line 108 — the generic master's save) | **405** (trailing-slash path); **404** (no slash). The combination screen has no save path — FINDING (below). |

Backend scope check (this screen's own path only): `public.m_combination_generic` is a real table
(0 rows; columns id/organization_id/unit_id/generic_id/sub_generic_id/strength_unit/strength_unit_id/
is_active/is_deleted/audit columns — consistent with the form's fields); the `CombinationGeneric`
entity (`@Table(name = "m_combination_generic", schema = "public")`) resolves — not a phantom, no
column drift on this path. No org-trust, twin-query, operator-precedence, or swallowed-exception
issues found on the screen's own path.

## FINDINGS (logged, NOT fixed — Rule 10)

1. **No save endpoint for this screen.** The only POST on the generics controller answers 405/404 and
   belongs to the generic master (its payload is `{items: [GenericRequestItem...]}`). The legacy shell
   never saved either (dead anchor). The rebuild's Save button reports this honestly via an error
   toast ("Save not wired — no combination-medication save endpoint exists") instead of pretending.
   Owner decision needed: extend the generic save to accept combinations, add a dedicated
   combination endpoint, or leave the screen read-only.
2. **Dev data is empty** — IsCombination/1, uom/units/1, and getCombinationDetail all return `[]`.
   The rebuild's wiring is **NOT ERRORING, usefulness UNVERIFIED** (the drug-recall standard):
   a working screen and a broken one both render an empty grid and empty selects in dev. Nothing was
   seeded to make it look better.

## Deliberate divergences (flagged, not silent)

1. **Strength field**: the legacy's select had no options and no data source; the rebuild binds a
   number input to `m_combination_generic.strength_unit` (a double) — the only data-backed
   representation.
2. **Search box**: the legacy's was inert; the rebuild filters the loaded grid client-side (no
   combination search endpoint exists).
3. **Grid detail cells** (Generic / Strength / CUM / Display name): the IsCombination list rows carry
   only id/code/name/status; the detail fetch (the edit action) fills the rest. Empty in dev either
   way.
4. The legacy's Save was a dead modal anchor; the rebuild's Save is a visible honest toast (finding 1).

## Rebuilt behaviours

- The form row: Combination Medicine (select), Generic (select), Strength (number), Clinical Unit of
  Measurement (select), Display Generic Name (text) — all required per the legacy labels.
- The grid: 7 columns (Combination medicine, Generic, Strength, CUM, Display Generic Name, Status
  toggle, Action/Edit), the search filter, the status toggle (PATCH, 201-verified), the edit action
  (detail fetch into the form).

## Verification state — HONEST (2026-08-09)

Gates: preflight C1-C6 PASS (moduleId 472 in the privilege tree; C6 marker scan clean), `ng build`
exit 0, smoke standard PASS (FINAL-URL /combinationMedicationMaster, store 827 has472=True), and the
screen's equivalent interaction (form fill + save click) produces no unexpected toasts. The data
wiring is **NOT ERRORING, not proven useful** (finding 2 — empty dev data). Nothing seeded.

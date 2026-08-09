# uom-master — parity rebuild of the legacy UOM Master screen

Legacy source:
- template: `views/master/GlobalMaster/OTMS/uom-master.html` (119 lines)
- controller: the generic `MasterController` (`controller/common-controller.js` — NO screen-specific logic)
- legacy state: `uomMaster`, module id **466** — hand-confirmed 2026-08-09:
  app.js:12312 `.state('uomMaster', { url: '/uomMaster', templateUrl:
  'views/master/GlobalMaster/OTMS/uom-master.html', controller: "MasterController",
  title: 'UOM Master' })` — REGISTERED, and the privilege tree carries node 466
  'Unit Of Measurement', parent 457 (Pharmacy) -> 369 (Global Master), in the role-1 set
  (the guard's store holds it). NOTE: the menu node's actionLink is `#/unitofMeasurement`
  — a route that does NOT exist in app.js — the legacy menu link is broken; the screen is
  reachable only via the direct URL /uomMaster (a legacy defect, logged in the finding).

## THE LEGACY IS A STATIC SHELL (hand-confirmed — the approved precedent)

The 119-line template has two decorative ng-models (`common.code`, `common.uomCode` — the
MasterController never defines a `common` object, so they bind to nothing), an inert Save
anchor, an inert search box, and one hardcoded grid row. Zero API calls, zero constants.
The static-shell standing decision (owner-approved on screen 4) applies: the rebuild
**IMPLEMENTS** the shell with the endpoints that demonstrably exist, and every missing
endpoint is reported honestly in the UI.

## Endpoints (verified by CALLING with the cached token — QF-2026-08-09-E: call, don't grep)

| Purpose | Endpoint | Handler | Verified |
|---|---|---|---|
| The units list (grid) | GET /api/global/uom/units/{orgId} | GlobalController.getUomUnits -> GlobalService.getUomUnitsList -> GlobalDao.getUomUnitsList (single org-scoped HQL) | **200** ([] in dev) |
| **SAVE / STATUS / DELETE — NONE EXIST** | — | the GlobalController exposes only the units GET; the UomUnit DAO has no save/update/delete | not applicable — finding |

## FINDINGS (logged, NOT fixed — Rule 10)

1. **No UOM write path exists**: no save, update/status, or delete endpoint for UomUnit
   anywhere on this screen's path. The legacy shell never saved either (inert anchor). The
   rebuild's Save and Status toggle report this honestly in the UI ("Save not wired — no UOM
   save endpoint exists", "Status update not wired — no UOM update endpoint exists"). Owner
   decision needed (queued): add a UOM CRUD endpoint or leave the screen read-only.
2. **The legacy menu link is broken**: node 466's actionLink `#/unitofMeasurement` targets a
   route that does not exist (only `uomMaster` is registered). The rebuild registers the
   working route.
3. **Dev data is empty** — GET /api/global/uom/units/1 returns `[]` (no UOM rows seeded).
   The wiring is **NOT ERRORING, usefulness UNVERIFIED** (the drug-recall standard): a
   working screen and a broken one both render an empty grid in dev. Nothing was seeded.

## Deliberate divergences (flagged, not silent)

1. **Save**: intentionally not wired (no endpoint exists) — an honest notice, not a dead
   anchor (the legacy's anchor was inert too).
2. **Status toggle**: intentionally not wired (no UOM update endpoint exists) — an honest
   notice on click.
3. **Edit**: copies the row's code/units into the form client-side (the legacy pencil was a
   dead anchor; no detail endpoint exists).
4. **Search box**: filters the loaded list client-side (the legacy's was inert).

## Rebuilt behaviours

- The form row: Code + Unit Of Measurement (the legacy's two fields).
- The grid: 4 columns (Code, Unit Of Measurement, Status, Action), the search filter, the
  edit (client-side form fill), the honest Save/Toggle notices.

## Verification state — HONEST (2026-08-09)

Gates: preflight C1-C6 PASS (moduleId 466 in the privilege tree; C6 marker scan clean),
`ng build` exit 0, smoke standard PASS (FINAL-URL /uomMaster, store 827 has466=True), and
the interaction probe (the units call 200, the Save click shows the honest notice, no
unexpected toasts). The data wiring is **NOT ERRORING, not proven useful** (finding 3 —
empty dev data). Nothing seeded.

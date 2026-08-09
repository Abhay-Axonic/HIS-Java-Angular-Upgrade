# immunization-schedule — parity rebuild of the legacy Immunization Schedule screen

Legacy source:
- template: `views/master/GlobalMaster/immunization-schedule.html` (176 lines)
- controller: the generic `MasterController` (`controller/common-controller.js` — NO screen-specific logic)
- legacy state: `immunizationSchedule`, module id **483** — hand-confirmed 2026-08-09:
  app.js:8433 `.state('immunizationSchedule', { url: '/immunizationSchedule',
  templateUrl: 'views/master/GlobalMaster/immunization-schedule.html',
  controller: "MasterController", title: 'Immunization Schedule' })` —
  REGISTERED (a bare top-level state, no resolve gate), and the privilege
  tree carries node 483 'Immunization Schedule', parent 476 (Clinical
  Workstation) -> 369 (Global Master), actionLink `#/immunizationSchedule`
  (matches the route). In the role-1 set (the guard's store holds it).
- Screen-9 selection: the walk descends the Clinical Workstation (476)
  family — allergyMaster (489, screen 6), allergyTypeMaster (488, screen 7),
  surgeryMaster (487, screen 8) done; surgeryGradeMaster (486), System
  Observation (485) and System Master (484) already rebuilt in earlier
  batches; immunizationSchedule (483) is the next not-rebuilt top-level
  member.

## THE LEGACY IS A REGISTERED STATIC SHELL (the approved precedent)

The 176-line template has ZERO `ng-*` bindings (the only "ng-" strings in the
file are `padding-*` CSS class-name fragments — verified per occurrence),
ZERO API calls, one hardcoded grid row ("- / - / View Disease List / -"), an
inert Save anchor (`data-toggle="modal"` on a `#confirmLogout` modal that
does not exist in the template), an inert search box, and an empty Disease
List modal. The generic MasterController has no immunization logic. The
static-shell standing decision (owner-approved on screens 4-5) applies: the
rebuild **IMPLEMENTS** the shell with the endpoints that demonstrably exist,
and every missing endpoint is reported honestly in the UI.

## Backend reality (verified by CALLING with the cached token — 2026-08-09)

| Purpose | Endpoint | Handler | Verified |
|---|---|---|---|
| The Vaccine select (the only real feed on this path) | GET /api/billing/getActiveVaccinationMaster/{org}/{unit} | VaccinationMasterController -> VaccinationMasterDaoImpl:255 (named query GET_ACTIVE_Vaccination_Master, org+unit scoped) | **200** "success" ([] dev) |
| **LIST / SAVE / UPDATE / STATUS / DISEASE-LIST — NONE EXIST** | — | no ImmunizationSchedule controller/DAO/entity anywhere (grep across all modules); the VaccinationMasterController's full CRUD serves the OTHER real vaccination screen (state `vaccinationMAster`, app.js:17050) | not applicable — finding |

## Scoped backend check (per class)

- Phantom columns/tables: **PASS on the wired feed** — the vaccine feed's
  named query selects vaccinationMaster columns that exist (the endpoint
  returns 200 "success"); the screen itself has no entity of its own to drift.
- Named-query INNER-JOIN null-FK drops: **not-applicable** — no
  screen-owned list/detail query exists (no list endpoint at all).
- Operator precedence on dup-checks: **not-applicable** — no save path
  exists to carry a dup-check.
- Org-trust: **PASS on the wired feed** — the vaccine feed is org+unit
  scoped in the named query (`organizationId`/`unitId` parameters). No
  payload-carried-org write path exists on this screen.
- Twin queries: **not-applicable** — single feed, no duplicated named
  queries on this screen's path.
- Swallowed exceptions on write paths: **not-applicable** — there are no
  write paths on this screen (no save/update/status endpoints exist). The
  underlying defect is the MISSING BACKEND itself, not a swallow.

## FINDINGS (logged, NOT fixed — Rule 10)

1. **The immunization-schedule screen has NO backend.** No
   ImmunizationSchedule controller, DAO, or entity exists anywhere in the
   codebase; there is no list, save, update, or status endpoint for this
   screen's data model (Age/Vaccine/Sequence/Disease), and no
   disease-list endpoint on its path. The only real feed is the vaccine
   catalog (getActiveVaccinationMaster), which the rebuild wires to the
   Vaccine select. The Save button and the Disease List button report the
   gap honestly in the UI. Owner decision queued (Q30): add a real
   immunization-schedule backend (schema + CRUD) or leave the screen
   read-only.
2. The static-shell state itself is a legacy defect — the screen is
   reachable (registered + privileged) but has never had any working
   behaviour to rebuild in parity (the combinationMedicationMaster /
   uomMaster class).

## Deliberate divergences (flagged, not silent)

1. **The Vaccine select is wired** to the real vaccine catalog feed (the
   legacy select was an empty `<select>` with no options and no binding).
2. **Save reports honestly** — "Save not wired — no immunization-schedule
   save endpoint exists" (an error toast; the legacy Save anchor was itself
   a dead link to a nonexistent modal).
3. **Disease List reports honestly** — "Disease list not available — no
   disease-list endpoint exists on this path" (the legacy modal was empty).
4. The Age (Day/Month/Year) and Sequence selects are static UI chrome (the
   legacy's were empty selects with no data source; no schedule fields have
   a backend).
5. The grid renders the honest empty state — no list endpoint exists, and
   nothing is seeded to look better.

## Rebuilt behaviours

- The form: Age (Day/Month/Year select + value input), Vaccine (live
  dropdown fed by the real vaccine catalog), Sequence (static select),
  Disease List (honest notice), Save (honest notice).
- The grid: the legacy 4 columns (Age, Vaccine, Disease, Sequence) with an
  honest empty-state row.

## Verification state — HONEST (2026-08-09)

Gates: preflight C1-C6 (moduleId 483 in the privilege tree, C5 with the node
chain), `ng build` exit 0, smoke standard (FINAL-URL /immunizationSchedule,
moduleId 483 present in the store). The Vaccine dropdown is fed by the
live-verified vaccine catalog ([] in dev — the feed is real, the data is
empty); everything else on the screen is **"not erroring, usefulness
UNVERIFIED"** — the screen's core function (the schedule itself) has no
backend, nothing was seeded to look better.

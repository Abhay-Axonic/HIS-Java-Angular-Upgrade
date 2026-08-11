# Encounter / Patient Visit — screen 46 (module 5)

## Identity

- Legacy state: `encounter` (+ children `encounter.encounterDetails`, `encounter.nextOfKin`, `encounter.paymentEntitlement`) — `legacy/.../config/app.js`
- Legacy template: `legacy/HIS-WAR/src/main/webapp/views/master/GlobalMaster/Encounter/encounter.html` (3,498 lines, 56 ng-repeat surfaces)
- Legacy controller: `EncounterController` (loaded in index.html) + `encounterDataController` / `encounterDataDetailsController`
- Menu: node 5 "Encounter" (parent 2 "Opd"), sidebar entry `checkAccess(5)` → `#/encounter/encounterDetails` ("Patient Visit") — **LIVE class**
- Backend: `legacy/Global/.../opd/controller/EncounterController.java` (`api/opd`, 81 mappings)
- Rebuild route: `Encounter` (moduleId 5, parent 2)

## Class

LIVE (state + menu node + backend). The sweep tool labelled it LIVE-ORPHANED only because
its `nodes_by_link` map normalises state names differently — the sidebar and privilege tree
both carry node 5, and the rebuild registers the route, so this is the standard 477/478/479
shape (restore what the menu advertises).

## Endpoint truth (live calls, dev token)

| Endpoint | Result |
|---|---|
| `POST /api/opd/getEncounterDetails` `{organizationId, unitId, patientId}` | **200 success** with a real patientId, `listObject: []` (dev-empty — patient-gated, honest). With `patientId: null` the service falls through to its **501 envelope** (the screen-40 lesson: the service routes on required fields) — the screen surfaces that as the truthful outcome |
| `GET /api/opd/getListOfEncounterMaster` | 500 with JwtTokenFilter headers; empty body with legacy `unit_id`/`organization_id` headers — **header-contract artifact**, banner |
| `POST /api/opd/encounter` (save) | patient-gated, not exercised in dev |

## Rebuild scope

- Search surface (date window + optional patient id) against the live search feed
- Result grid + row summary (JSON detail pane)
- Documented patient-gated paths (next-of-kin, payment entitlement, coversheet tabs) in README
- AMBER-permanent banner: the un-parameterised list feed's 500/empty-body under the
  rebuild's authenticated header contract — the legacy browser sent legacy header names itself.

## Gates

- Preflight: C1–C4/C6–C8 PASS, C5 DEAD-legacy flag (state name lowercase `encounter` vs
  preflight's PascalCase grep) — hand-confirmed by the two greps in the screen report.
- `ng build`: EXIT 0
- Probe: `tools/probe_encounter.py` — PASS in real browser (banner + why-permanent, search
  round-trip, 0 page errors)

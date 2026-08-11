# Growth Chart — screen 50 (module 744)

## Identity

- Legacy state: `growthChart` (`legacy/.../config/app.js`) — url `/growthChart` (with a `resolve` block for the growthChartId param)
- Legacy template: `legacy/HIS-WAR/src/main/webapp/views/Nursing/growth-chart.html` (414 lines, 22 `{{}}` bindings, 14 ng-model, 13 ng-click, 9 ng-if, 5 ng-repeat)
- Legacy controller: `growthChartController` (`controller/clinical/growthChartController.js`, loaded in index.html)
- Menu: node 744 "Growth Chart" (parent 2 Opd) — **NO actionLink** — DANGLE class (owner's BUILD ALL SEVEN decision)
- Backend: `GrowthChartController` (`com/param/global/unit/controller/`) — `/api/global/growthChart` (POST save), `/api/global/growthChartlist/{orgId}/{unitId}/{offset}/{recordPerPage}` (GET list), `/api/global/growthChart/{orgId}/{unitId}/{growthChartId}` (GET one), `/api/global/growthChart/patient` (POST), `/api/global/growthChart/patient/details` (POST)
- Rebuild route: `growthChart` (moduleId 744)

## Class

DANGLE — state + controller + template + backend all real (genuinely data-bound
template, real functional backend). Menu node 744 without actionLink.

## Endpoint truth (live calls, dev token)

| Endpoint | Result |
|---|---|
| `POST /api/global/growthChart/patient/details` `{organisationId, unitId, patientId, genderId}` | **200 success with a real record** (growthChartId 15, weightFrom 90.0, ageInMonths 1) |
| `GET /api/global/growthChartlist/1/1/0/10` | **200 success, 1 real row** (same record — dev database holds one seeded row) |
| `GET /adt/getActiveGenderList` | 200 success (3 rows) — legacy-header pair (screen-47 split class) |

## Rebuild scope

- Patient/gender search + list table, both feeds live
- Real dev rows render honestly (growthChartId 15 with weight/age columns)
- Save/edit write paths exist in the backend (POST /api/global/growthChart) but
  are not exercised by this surface (list + details only, matching the probe scope)

## Gates

- Preflight: C5–C8 PASS pre-build; final 9/9 with files present
- `ng build`: EXIT 0
- Probe: `tools/probe_growth_chart.py` — PASS in real browser

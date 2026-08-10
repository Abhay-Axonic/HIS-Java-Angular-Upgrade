# vaccination-master — parity rebuild of the legacy Vaccination Master screen

Legacy source:
- template: `views/master/GlobalMaster/vaccination-master.html` (198 lines)
- controller: the generic `MasterController` (`controller/common-controller.js` — NO screen-specific logic)
- legacy state: `vaccinationMaster`, module id **482** — hand-confirmed 2026-08-09:
  app.js:8427 `.state('vaccinationMaster', { url: '/vaccinationMaster',
  templateUrl: 'views/master/GlobalMaster/vaccination-master.html',
  controller: "MasterController", title: 'Vaccination Master' })` —
  REGISTERED (a bare top-level state, no resolve gate), and the privilege
  tree carries node 482 'Vaccination Master', parent 476 (Clinical
  Workstation) -> 369 (Global Master), actionLink `#/vaccinationMaster`
  (matches the route). In the role-1 set (the guard's store holds it).
- Screen-10 selection: the walk descends the Clinical Workstation (476)
  family — allergyMaster (489, screen 6), allergyTypeMaster (488, screen 7),
  surgeryMaster (487, screen 8), immunizationSchedule (483, screen 9) done;
  surgeryGradeMaster (486), System Observation (485) and System Master (484)
  already rebuilt; vaccinationMaster (482) is the next not-rebuilt top-level
  member.

## THE DECOY-ROUTED SHAPE (the headline of this screen)

Node 482's menu reaches the SHELL at 8427. But there is a SECOND state —
`vaccinationMAster` (app.js:17050, capital A) with a DEDICATED controller
(`vaccinationMasterController.js`, ~550 lines) and a 21-field template
(`views/clinicalWorkStation/vaccination-master.html`) — the REAL vaccination
master CRUD screen. It is ORPHANED: no privilege node in the role-1 tree
carries an actionLink for it (verified: the only vaccination node is 482 ->
`#/vaccinationMaster`). Real functionality exists under a different name; the
menu points at the shell. This is the decoy-routed legacy defect class
(logged, queued — the real screen is unreachable from the menu in the legacy
app itself).

## THE LEGACY SHELL (the approved static-shell precedent)

The 198-line template has ZERO `ng-*` bindings, ZERO API calls, a hardcoded
grid row ("- / - / View Disease List / -"), an inert Save anchor
(`data-toggle="modal"` on a `#confirmLogout` modal that does not exist), an
inert search box, and a Diseases add-row table implemented as raw DOM
manipulation (`addRowRangeTable1` — jQuery append/delete rows, no backend).
The static-shell rule (owner-approved on screens 4-5/9) applies: the rebuild
IMPLEMENTS the shell with the endpoints that demonstrably exist, and every
missing endpoint is reported honestly in the UI.

## Backend reality (live-verified by CALLING with the cached token — 2026-08-09/10)

The full CRUD exists in the Billing module (`VaccinationMasterController`,
`/api/billing/*`; constants `lis.url.config.js:527-533`) and is consumed by
the ORPHANED real screen. ALL THREE of this screen's feeds were CALLED with
the cached token (the standing rule — verified by calling, not by grep):

| Endpoint | Handler | Verified (call, 2026-08-10) |
|---|---|---|
| POST /api/billing/getVaccinationMasterList | VaccinationMasterController -> VaccinationMasterDaoImpl:211 (named query GET_Vaccination_Master_LIST, org+unit scoped, ALL LEFT JOINs) | **200** "success" code 200, listObject [] (dev-empty) |
| POST /api/billing/getVaccinationMasterCount | same chain (Criteria count, org+unit scoped, count in the `object` slot — DaoImpl:243) | **200** "success" code 200, object 0 |
| GET /api/billing/getRouteOfAdministrationList/1/1 | named NATIVE query on m_route_of_administration, org+unit scoped | **200** "success" code 200, listObject [] (dev-empty) |
| GET /api/billing/getActiveVaccinationMaster/1/1 | VaccinationMasterDaoImpl:255 (GET_ACTIVE_Vaccination_Master) | live-verified **200** ([]) in screen 9's probes |
| POST /api/billing/saveVaccinationMaster | service dup-check (GET_Vaccination_Master_BY_NAME) then DAO insert | MAPPED — write, NOT called (no-seed rule) |
| PUT/POST update + status | same controller | MAPPED — write, NOT called |

Honest state: the READ-ONLY feeds are live-verified 200/success (empty dev
data — nothing seeded). The WRITE paths are mapped but their success is
UNVERIFIED per the WRITE-VERIFICATION rule — never "working".

## Scoped backend check (per the standing rule, every class)

- **Phantom columns/tables: PASS** — verified by CALLING (list/count/ROA all
  200 "success" — the named queries and the native ROA query resolved and
  executed against real columns) + DDL cross-check: the entity
  `lab.m_vaccination_master` has 25 columns, every `@Column`/`@JoinColumn`
  name in the entity exists in the DDL (vaccination_id, vaccine_code,
  administration_id, site_id, dose_id, uom_id, age, total_day_count, day,
  interval, choose_interval, generic_id, max_age, max_day, min_age, min_day,
  remark, series, gender_id, abbreviation, status, created_by/date,
  updated_by/date, organization_id, unit_id). Note: `vaccinationCode` maps to
  column `vaccine_code` (an FK to Item) — a naming alias, not a phantom. The
  named queries' SELECT aliases (vaccinationMaster.*, joined item/generic/
  genderMaster/routeOfAdministration/siteOfAdministrationMaster/strengthUnit/
  doseMaster fields) all exist.
- **Named-query INNER-JOIN null-FK drops: PASS (no drop class)** — every
  named query on this path uses LEFT JOIN (list/byId/active/genderId all
  `left JOIN`); a row with a null dose_id/administration_id/site_id is NOT
  dropped. (Contrast: QF-2026-08-07-L's INNER-JOIN class — absent here.)
  Confirmed executable by the 200 on the list call.
- **Operator precedence on dup-checks: PASS (no precedence class)** — the
  save dup-check `GET_Vaccination_Master_BY_NAME` is ALL-AND and fully
  org+unit scoped (`vaccinationCode=:vaccinationCode AND minAge=:minAge AND
  minDay=:minDay AND genderId=:genderId AND series=:series AND
  organizationId=:organizationId AND unitId=:unitId`); BY_NAME_NOT_ID adds
  `vaccinationId <> :vaccinationId`, still all-AND. There is NO unscoped OR
  branch, so the cross-org reachability question is moot: a same-key row from
  ANOTHER org does NOT satisfy the dup-check. This is NOT the Q29/BR-169
  class. (Query shape read from source; the dup-check only fires on the
  write path, which was not called per the no-seed rule.)
- **Org-trust: PASS on reads (verified by the calls), NOTED on writes** —
  the list and count calls returned 200 with org/unit=1 scoping honored (a
  cross-org probe was not run — read paths are org+unit parameterized in
  every query). The write paths take organizationId/unitId from the DTO
  payload (the payload-carried-org class, same as the other masters) — the
  save sets them from the DTO (DaoImpl:70-71). No write test ran (no-seed
  rule), so the cross-org write reachability is untested, not demonstrated.
- **Twin queries: PASS** — GET_Vaccination_Master_LIST / BY_ID /
  GET_ACTIVE are near-identical SELECT shapes but each is used once by its
  own path; no duplicated named-query definition on this screen's path.
- **Swallowed exceptions on write paths: PASS (no swallow at controller)** —
  every controller catch block rethrows (`throw e;` — VaccinationMaster-
  Controller:29-32, 42-45, 55-58, 70-73, 84-87, 97-100, 111-114); the DAO
  rethrows too. The commented-out generic-error returns are dead code. No
  silent swallow on this screen's path (contrast the campaign's #1 class —
  absent here). The read calls returned clean 200 "success" envelopes, not
  the generic error envelope.

## FINDINGS (logged, NOT fixed — Rule 10)

1. **The decoy-routed shape (the real screen is orphaned).** Node 482's menu
   reaches the 8427 SHELL while the real 21-field CRUD screen
   (`vaccinationMAster`, app.js:17050) has NO menu node — unreachable from
   the legacy app's own menu. The rebuild implements the shell; the real
   screen's full CRUD (save/update/status/byId) is what Q31 should decide
   about (wire the shell to it with a full form, or add the real screen to
   the menu, or decommission the shell). QUEUED as Q31.
2. The write paths' org comes from the DTO payload (payload-carried org) —
   same class as every other master; NOT demonstrated exploitable (no write
   test ran). Listed for the Phase-2 backend campaign, not fixed here.
3. The Diseases table has no backend (the legacy's raw-DOM rows never
   persisted) — the rebuild keeps it client-side and says so in the UI.

## Deliberate divergences (flagged, not silent)

1. **The grid is wired** to the live-verified list feed (the legacy's was a
   hardcoded "- / - / View Disease List" row). Reads verified by CALLING:
   list/count/ROA all 200 "success" (empty dev data).
2. **The Diseases table is a client-side add/remove list** (parity with the
   legacy's raw-DOM behavior — it never persisted; the UI note says so).
3. **Save reports honestly — with a precise message.** The save endpoint
   (saveVaccinationMaster) EXISTS and is wired in the backend — this is NOT
   a "no save endpoint exists" situation (contrast uomMaster, where no UOM
   save endpoint exists at all). The real situation: the endpoint's DTO
   contract is the full 21-field VaccinationMasterDto (vaccinationCode +
   genderId + administrationId + siteId + doseId + uomId + age + dayid +
   status + day + genericId + minAge/minDay/maxAge/maxDay + series +
   abbreviation + remark + interval + chooseInterval + createdBy/Date +
   updatedBy/Date), and this SHELL's form collects only 2 of them (Code +
   Name of Vaccine). Sending a 2-field payload would be an INCOMPLETE
   payload — the probe-payload class (QF-2026-08-09-I: the dateString-null
   NPE fires when createdDate/updatedDate are omitted; the real screen
   always sends them). The honest message therefore says the endpoint exists
   but is consumed by the orphaned real screen (vaccinationMAster) which
   collects the full contract, and that this shell's form cannot construct
   it. That is the accurate statement — not "no endpoint".
4. **The search box filters the loaded grid client-side** (legacy inert).
5. The edit (pencil) copies the row's code/name into the form client-side
   (the legacy pencil was a dead anchor; the detail endpoint is the real
   screen's).

## Verification state — HONEST (2026-08-09/10)

Gates: preflight C1-C6 (moduleId 482 in the privilege tree, C5 with the
node-482 chain), `ng build` exit 0, smoke standard (FINAL-URL
/vaccinationMaster, moduleId 482 present in the store), interaction probe
(screen loads, grid + diseases sections render, Save honest notice, search
filters). The read feeds (list/count/ROA) are LIVE-VERIFIED by CALLING with
the cached token (all 200 "success", empty dev data); the write paths are
UNVERIFIED per Q28 (no-seed — never "working"). Nothing seeded. The screen's
core function (the real CRUD) belongs to the orphaned real screen — this
rebuild honestly reports that boundary.

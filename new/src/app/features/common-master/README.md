# commonMaster — dispatcher screen (slice)

## What this is

Rebuild of the legacy `commonMaster` state (`/commonMaster/:url`): ONE screen
behind many menu paths. The legacy state's `resolve.getController` is a 43-way
switch over the `:url` param that injects a per-master controller into one
shared template (`common-master-screen.html`) via `controllerProvider`. The
backend discriminates by **endpoint path** (each master has its own list
endpoint under its family's root — `/LIS/global`, `/LIS/unit`, `/adt`, ...),
not by a discriminator field.

## Slice scope (owner decision — option b)

The 4 Unit Master branches plus 1 ADT live-data branch, proven end to end
before the remaining 38 branches are added in family batches:

| :url value       | Endpoint (live-verified)                          | Result        |
|------------------|---------------------------------------------------|---------------|
| phlebotomyMaster | GET `/LIS/global/listPhlebotomyMaster/1/0/10`     | 500 dev-empty |
| headerMaster     | GET `/LIS/global/listHeaderMaster/1/1/0/10`       | 500 dev-empty |
| departmentMaster | GET `/LIS/global/listDepartmentMaster/1/0/10`     | 500 dev-empty |
| biochemicalTest  | POST `/LIS/unit/getMicrobiologyTestMasterList`    | 200 `[]`      |
| relationmaster   | POST `/adt/getRelationMasterList`                 | 200 real rows |

The 4 Unit Master branches exercise the param dispatch, the per-master
endpoint, and the node-id-array gate; `relationmaster` proves the live-data
render path (real rows: "Cousin"/ABC123).

## ADT family batch (owner order 2026-08-12: ADT 7 first)

The ADT family is root 380 (7 branches: sexmaster 389, relationmaster 390,
maritalstatus 391, religion 392, occupation 393, patientsource 394,
referraltype 395). relationmaster was the slice's live-data branch; the other
6 are added here. ALL are POST `/adt/<ep>` with body
`{organizationId, offset, noOfRecordsPerPage}` — live-verified 2026-08-12,
every one returns REAL rows:

| :url value       | Endpoint (POST)                          | Live result                  |
|------------------|------------------------------------------|------------------------------|
| sexmaster        | /adt/getGenderMasterList                 | success, 3 rows (Test Save Gender) |
| maritalstatus    | /adt/getMaritalStatusMasterList          | success, 3 rows (Widowed)    |
| religion         | /adt/getReligionMasterList               | success, 2 rows (Victim Religion 999) |
| occupation       | /adt/getOccupationMasterList             | success, 2 rows (Teacher2)   |
| patientsource    | /adt/getPatientSourceMasterList          | success, 6 rows (PATIENT12)  |
| referraltype     | /adt/getReferralTypeMasterList           | success, 5 rows (Testing1)   |

All six render their real rows in the probe (asserted per branch).

## LIS family batch (owner order 2026-08-12; DQ-2026-08-12-003)

The LIS family is the largest (26 remaining branches after the slice's 4 Unit
Master ones). ALL are GET `/LIS/global/<list>/{orgId}/{offset}/{size}` except
languageMaster + serologyTestFindingMaster (4 segments: orgId/unitId/offset/
size). Live-verified 2026-08-12 — every branch is dev-empty (zero real rows),
in TWO classes:

- **DESCRIPTIVE dev-empty (10)** — backend returns the per-master message
  ("Sample Records not found." etc.), surfaced as honest dev-empty:
  sampleMaster, containerMaster, techinqueMaster, prerequisitesMaster,
  mediaMaster, antibioticMaster (legacy copy-paste: says "Sample"),
  antibioticsGroupMaster, storageLocationMaster, languageMaster,
  serologyTestFindingMaster.
- **GENERIC "Internal Server Error" (16)** — the MASKED-ERROR class (the
  owner's LIS watch-item, CONFIRMED): the legacy service layer's
  `size() > 0` guard makes its inner `isEmpty()` branch unreachable, so an
  empty list falls through to `ERR_500_MESSAGE` ("Internal Server Error").
  The per-master message EXISTS in the code but can never fire
  (ReagentMasterServiceImpl.java:103-121, ColonyMasterServiceImpl, etc.).
  The rebuild renders an AMBER-permanent banner naming the mechanism (the
  response the warning would read is the thing that lies): reagentMaster,
  reportTypeMaster, bacteriaShapMaster, wtifilmStudyMasterMaster, colonyMaster,
  organismMaster, organismGroupMaster, stainigMaster, microbiologyStainMaster,
  antibioticsMaster, incubationTestMaster, sensitivityTypeResultMaster,
  slideProcedureMaster, bacteriaClassificationMaster, unitMaster, blockMaster.

Backend fix NOT made (Rule 10 — behavioral change; BR candidate: the guard
should be `>= 0` or the empty branch moved outside it; the dead
SAMPLE_RECORDS_NOT_FOUND constant is the same class).

## Mechanism notes

- **Dispatch**: `CommonMasterService.DISPATCH` maps the `:url` key to
  `{method, path, getSegments|body}`; the component reads the param from
  ActivatedRoute and calls the service.
- **Permission gate**: route `data.moduleIds` is the family's node-id array
  (45 ids, copied from the legacy resolve's access check). The guard grants
  when ANY family id is in the user's permissions (legacy semantics:
  `arr.length <= 0` → accessDenied). A single-id gate would silently deny the
  other branches.
- **Dev-empty honesty**: the 500 "Records not found" envelope is the backend's
  honest unseeded state — surfaced in the UI, not masked (this is NOT the
  phantom-column envelope of screens 49/52; the messages are per-master and
  descriptive, and the endpoints were live-verified).
- **Auth**: interceptor-only; these endpoints need no manual HttpHeaders
  (verified live with the standard header set).

## House pattern

`signal()` state + `@if`/`@for` blocks exclusively (C9); no `*ngIf`/`*ngFor`;
no manual HttpHeaders beyond the interceptor; `API_BASE_URL` injected.

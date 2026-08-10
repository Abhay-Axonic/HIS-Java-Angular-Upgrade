# blood-component — parity rebuild of the legacy Blood Component Master screen

Legacy source:
- template: `views/master/GlobalMaster/blood-component.html` (121 lines)
- controller: the generic `MasterController` (`controller/common-controller.js` — NO screen-specific logic)
- legacy state: `bloodComponent`, module id **481** — hand-confirmed 2026-08-10:
  app.js:8421 `.state('bloodComponent', { url: '/bloodComponent',
  templateUrl: 'views/master/GlobalMaster/blood-component.html',
  controller: "MasterController", title: 'Blood Component Master' })` —
  REGISTERED (a bare top-level state, no resolve gate), and the privilege
  tree carries node 481 'Blood Component', parent 476 (Clinical Workstation)
  -> 369 (Global Master), actionLink `#/bloodComponent` (matches the route).
  In the role-1 set (the guard's store holds it).
- Screen-11 selection: the walk descends the Clinical Workstation (476)
  family — allergyMaster (489, screen 6), allergyTypeMaster (488, screen 7),
  surgeryMaster (487, screen 8), immunizationSchedule (483, screen 9),
  vaccinationMaster (482, screen 10) done; surgeryGradeMaster (486), System
  Observation (485) and System Master (484) already rebuilt; bloodComponent
  (481) is the next not-rebuilt top-level member.

## THE DECOY-ROUTED SHAPE (same class as screen 10)

Node 481's menu reaches the SHELL at 8421. There is a SECOND state —
`bloodComponentMaster` (app.js:16591) with a DEDICATED controller
(`bloodComponentMasterController.js`) and a full template
(`views/master/UnitMaster/bloodBank/blood-component-blood-bank.html`,
resolve gate item.id == 736) — the REAL blood-component CRUD screen. It is
ORPHANED: no privilege node in the role-1 tree carries an actionLink for it
(verified: the only blood* nodes are 481 -> `#/bloodComponent` and 251 Blood
Request -> `#/bloodRequest`). Real functionality exists under a different
name; the menu points at the shell. Legacy defect, logged, queued (Q32).

## THE LEGACY SHELL (the approved static-shell precedent)

The 121-line template has ZERO `ng-*` bindings, ZERO API calls, a hardcoded
grid row ("- / - / switch / pencil"), an inert Save anchor
(`data-toggle="modal"` on a `#confirmLogout` modal that does not exist), an
inert search box, and a two-field form (Component Code + Component Name).
The static-shell rule (owner-approved on screens 4-5/9/10) applies: the
rebuild IMPLEMENTS the shell with the endpoints that demonstrably exist, and
every missing endpoint is reported honestly in the UI.

## Backend reality (live-verified by CALLING with the cached token — 2026-08-10)

The full CRUD exists in the BloodBank module (`BloodComponentMasterController`,
`@RequestMapping("/bloodBank")`; constants `bloodBank.url.config.js:97-103`
carry NO `/api` prefix — the fragment class, same as screen 8's `/adt`):

| Endpoint | Handler | Verified (call, 2026-08-10) |
|---|---|---|
| POST /bloodBank/getBloodComponentMasterList | BloodComponentMasterController -> BloodComponentMasterDaoImpl (named query GET_BLOOD_COMPONENT_MASTER_LIST, org+unit scoped, single-table FROM — no joins) | **200** "success" code 200, listObject 1 row (id 5 "Whole Blood", BC01, org 1 unit 1) |
| POST /bloodBank/getBloodComponentMasterCount | same chain (count in the `object` slot) | **200** "success" code 200, object 1 |
| GET /bloodBank/getActiveBloodComponentMaster/1/1 | GET_ACTIVE_BLOOD_COMPONENT_MASTER, org+unit scoped | **200** "success" code 200, listObject 1 row |
| GET /bloodBank/getBloodComponentMasterById/1/1/1 | GET_BLOOD_COMPONENT_MASTER_BY_ID, org+unit scoped | **200** "success" code 200, listObject [] (id 1 absent — the dev row is id 5) |
| POST /bloodBank/saveBloodComponentMaster | service dup-check (GET_BLOOD_COMPONENT_MASTER_BY_NAME) then DAO insert | MAPPED — write, NOT called (no-seed rule) |
| POST /bloodBank/updateBloodComponentMaster + updateStatus | same controller | MAPPED — write, NOT called |

Honest state: the READ-ONLY feeds are live-verified 200/success with real dev
data (1 row). The WRITE paths are mapped but their success is UNVERIFIED per
the WRITE-VERIFICATION rule — never "working".

## Scoped backend check (per the standing rule, every class)

- **Phantom columns/tables: PASS** — verified by CALLING (list/count/active/
  byId all 200 — the named queries resolved against real columns) + DDL
  cross-check: `blood_bank.m_blood_component_master` has 14 columns and every
  entity `@Column`/query alias exists (blood_component_id, blood_component_
  code, blood_component_desc, blood_component_short_desc, expiry, quantity,
  by_cross_checking, created_by/date, status, updated_by/date, unit_id,
  organization_id). The live row's fields (BC01, "Whole Blood", WB, expiry 35,
  quantity 1, byCrossChecking "N") all map.
- **Named-query INNER-JOIN null-FK drops: PASS (no drop class)** — all four
  named queries are single-table `FROM BloodComponentMaster` with NO joins;
  there is no join to drop a row. Confirmed executable by the 200s.
- **Operator precedence on dup-checks: FINDING (Q32) — the dup-check has NO
  org/unit scoping at all (worse than the Q29/BR-169 OR shape).** The save
  dup-check `GET_BLOOD_COMPONENT_MASTER_BY_NAME` is
  `WHERE LOWER(bloodComponentDesc)=:bloodComponentDesc` — no organizationId,
  no unitId, no OR; BY_NAME_NOT_ID adds only `bloodComponentId != :id`. The
  query is UNCONDITIONAL across orgs: a same-name row in ANY org satisfies
  the dup-check. CROSS-ORG REACHABILITY (per the standing rule): YES — the
  unscoped branch is fully reachable by a cross-org caller; the save in org A
  is BLOCKED by a same-name row in org B (ALREADY_EXIST), and the error
  reveals name existence across all orgs (an existence oracle). Note the
  write itself is org-safe (the service OVERWRITES org/unit from
  VerifiedIdentityService — the good pattern, see org-trust) — the defect is
  purely in the dup-check query, which ignores the verified org. QUEUED as
  Q32: add organizationId/unitId to both dup-check named queries.
- **Org-trust: PASS on reads (verified by the calls), PASS on writes (the
  GOOD pattern)** — reads: every query org+unit parameterized (list/count/
  active/byId all carry organizationId/unitId; the calls with org=1/unit=1
  returned 200 and the correct single row). Writes: the service sets
  organizationId/unitId from `verifiedIdentityService.getVerifiedOrganizationId()/
  getVerifiedUnitId()` (BloodComponentMasterServiceImpl:31-32, 52-53) —
  overriding any payload-carried org. This is the counter-example to the
  payload-carried-org class seen in vaccinationMaster: the write org is
  server-verified, not payload-trusted. (No write test ran — no-seed — but
  the verified-identity override is source-evident and is the correct shape.)
- **Twin queries: PASS** — LIST / BY_ID / ACTIVE / BY_NAME are near-identical
  SELECTs, each used once by its own path; no duplicated named-query
  definition on this screen's path.
- **Swallowed exceptions on write paths: PASS (no swallow)** — every
  controller catch logs and rethrows `new ServiceException(e.getMessage())`
  (BloodComponentMasterController:32-35, 42-45, ...) — routed to the global
  exception handler, not swallowed; the DAO rethrows as ServiceException too.
  The read calls returned clean 200 "success" envelopes, not the generic
  error envelope.

## FINDINGS (logged, NOT fixed — Rule 10)

1. **Q32 — the dup-check is cross-org unconditional** (see the scoped check
   above): GET_BLOOD_COMPONENT_MASTER_BY_NAME / _BY_NAME_NOT_ID have no
   org/unit predicates; a cross-org caller's save is blocked by another
   org's same-name row (ALREADY_EXIST) and the error leaks name existence
   across orgs (existence oracle). The write org itself is verified-identity
   (good) — the defect is the dup-check query only. This is the THIRD
   confirmed instance of the dup-check-scoping family (first: BR-169
   Discharge Type OR-precedence; second: Q29 surgeryMaster OR-precedence;
   third: this zero-scoping shape) — the owner's "third instance makes it a
   campaign" threshold now applies to the FAMILY (dup-checks that reach
   across orgs), though the mechanism here (missing scope) differs from the
   OR-precedence mechanism of the first two. QUEUED as Q32.
2. **The decoy-routed shape**: node 481's menu reaches the 8421 SHELL while
   the real full-CRUD screen (bloodComponentMaster, 16591, gate 736) has no
   menu node — unreachable from the legacy menu. Same class as screen 10's
   vaccinationMaster. Q32's decision should also cover whether the real
   screen gets a node/actionLink.

## Deliberate divergences (flagged, not silent)

1. **The grid is wired** to the live-verified list feed (the legacy's was a
   hardcoded "- / - / switch / pencil" row). Reads verified by CALLING: real
   dev row (id 5 "Whole Blood") renders.
2. **Save reports honestly — with a precise message.** The save endpoint
   (saveBloodComponentMaster) EXISTS and is wired in the backend — this is
   NOT a "no save endpoint exists" situation. The endpoint's contract is the
   full blood-component record (bloodComponentCode, bloodComponentShortDesc,
   bloodComponentDesc, quantity, expiry, byCrossChecking, org/unit, status,
   created/updated dates), and this SHELL's form collects only 2 of them
   (Code + Name of Component). Sending a 2-field payload would be an
   INCOMPLETE payload — the probe-payload class (QF-2026-08-09-I: the
   dateString-null NPE fires when createdDate/updatedDate are omitted). The
   toast says the endpoint exists, what it expects, and that the complete
   form lives on the orphaned real screen (bloodComponentMaster).
3. **The Status switch reports honestly** — the status endpoint exists but
   toggling the live dev row would be a no-seed write; the real screen's
   toggle is the proper home. Error toast, never a silent dead control.
4. **The search box filters the loaded grid client-side** (legacy inert).
5. The edit (pencil) copies the row's code/name into the form client-side
   (the legacy pencil was a dead anchor; the by-id detail endpoint is the
   real screen's).

## Verification state — HONEST (2026-08-10)

Gates: preflight C1-C6 (moduleId 481 in the privilege tree, C5 with the
node-481 chain), `ng build` exit 0, smoke standard (FINAL-URL
/bloodComponent, moduleId 481 present in the store), interaction probe
(screen loads, grid renders the real dev row, Save + Status honest notices,
search filters). The read feeds are LIVE-VERIFIED by CALLING with the cached
token (all 200 "success", 1 real dev row); the write paths are UNVERIFIED per
Q28 (no-seed — never "working"). Nothing seeded. The screen's core function
(the real CRUD) belongs to the orphaned real screen — this rebuild honestly
reports that boundary.

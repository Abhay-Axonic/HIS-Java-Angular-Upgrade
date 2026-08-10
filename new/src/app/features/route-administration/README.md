# route-administration — parity rebuild of the legacy Route of Administration screen

Legacy source:
- template: `views/master/GlobalMaster/AssetManagenent/Master/route-of-adminstration.html`
- controller: `routeOfAdministrationController.js` (270 lines — a REAL dedicated
  controller, loaded via index.html:1006)
- menu node: 477 'Route of Administration', parent 476 Clinical Workstation
  -> 369 Global Master, actionLink `#/routeAdministration`

## THE DANGLE SHAPE (menu'd-unrouted — the drug-recall class, second instance)

Same class as 479 (formulationTypeROAMaster, screen 13): the node's
actionLink points at `#/routeAdministration`, but NO `.state('routeAdministration')`
is registered anywhere in app.js (grep = 0). The controller IS loaded
(index.html:1006), the template exists, and the backend is LIVE — so the
legacy menu navigates to a non-existent state (NG04002). Per the amended
reachability rule (RULES.md four-way classification, owner directive
2026-08-10), a DANGLE screen with a real backend is BUILDABLE: the rebuild
RESTORES a menu-advertised screen the legacy cannot open.

## Backend (Pharmacy module) — verified BY CALLING (cached token, 2026-08-10)

- list:  GET /api/global/route/of/administration/{orgId} -> 200 raw list (dev-empty)
- byId:  GET /api/global/route/of/administration/{orgId}/{id} -> 200 envelope
- save:  POST /api/global/route/of/administration/{orgId}/{userId} (MAPPED, not fired — no-seed)
- update: PATCH /api/global/route/of/administration/{orgId}/{id} (MAPPED, not fired)
- table: public.m_route_of_administration exists (dev-empty, count 0)

Note the endpoint prefix: `api/global/route/of/administration` (the Pharmacy
RouteOfAdministrationController @RequestMapping) — NOT /api/globals/*. The
frontend constant is ROUTE_OF_ADMINSTRATION_API = globalApi + '/route/of/administration/'
(constants.js:173, globalApi = rootUrl + '/api/global').

## Scoped backend check (per the standing rule, every class)

- Phantom columns/tables: PASS — public.m_route_of_administration exists;
  the entity (RouteOfAdministration extends BaseEntity: id, code, description
  + org/unit/status/dates) matches; verified by the 200s executing against
  real DDL.
- Named-query INNER-JOIN null-FK drops: PASS — getAll() uses a Criteria
  query (Restrictions on organizationId + isDeleted) — no INNER JOIN, no
  drop class on the list path. (The DAO's only INNER JOIN is in
  getFormulationROACountFromROA — a count used by the update-status guard,
  not a list.)
- Dup-check operator precedence: NOT PRESENT server-side — the legacy
  duplicate-code check is CLIENT-SIDE ONLY (ValidateCode /
  ValidateCodeForUpdate in the legacy controller iterate the loaded list);
  the DAO has no getByCode. The rebuild mirrors the client-side check on the
  loaded list (same behavior, same limitation: no server-side dup-check —
  the absence class, not Q29/Q32).
- Org-trust: PASS — the org-safe pattern: getById(id, verifiedOrg) and
  create() set org/unit from VerifiedIdentityService (batch-35 fixed the
  prior client-supplied-org write IDOR; the service comment documents it).
- Twin queries: PASS — each DAO query used once by its own path.
- Swallowed exceptions on write paths: PASS — the controller's create/update
  catch blocks throw ServiceException (the global handler); not the generic
  envelope.

## Verification state — HONEST

- preflight C1-C6 PASS (C5 flags DEAD-legacy — the dangle, surfaced not
  blocked); `npm run build` exit 0; smoke standard PASS (FINAL-URL
  /routeAdministration, moduleId 477 present, SERVE-BRANCH matches the
  branch under test); interaction probe PASS (form renders, save endpoint
  wired, 0 page errors).
- Write verification: NO write test ran (no-seed rule — the save writes real
  rows). Save is "not erroring" at most — the endpoint is wired and its
  response is reported; never "working". (The save path is org-safe per
  source; its success with real data is UNVERIFIED — Q28.)
- Endpoint verification: reads verified BY CALLING (above); save/update are
  MAPPED but not live-fired (no write test).

## Deliberate divergences (flagged, not silent)

1. The legacy's duplicate-code check is client-side only (as in the legacy);
   the rebuild mirrors it on the loaded list. The absence of a server-side
   dup-check is a finding (QF, post-merge) — duplicate codes are possible if
   two clients race.
2. The legacy status toggle (PATCH status) is shown read-only in the
   rebuild's grid; the update/status writes are no-seed (endpoint mapped,
   not fired).
3. Pagination (legacy 10/25/50/100) is client-side search + full list for
   now (Phase-2/3 consideration).

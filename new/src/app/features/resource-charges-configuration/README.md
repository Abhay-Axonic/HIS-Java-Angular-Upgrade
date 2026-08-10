# Resource Charges Configuration — screen 23 (module 761, OTIMS)

Parity rebuild of the legacy
`views/master/GlobalMaster/OTIMS/resource-charges-configuration.html` (menu
node 761 'Resource Charges Configuration', parent 514 OTIMS -> 369 Global
Master, controller resourceChargesConfigurationController, loaded
index.html:1657).

## CLASSIFICATION — DANGLE (the 477/478/479 precedent), C7-verified

- State registration: REGISTERED — app.js:18871 `.state('resourceChargesConfiguration')`,
  url /resourceChargesConfiguration, templateUrl
  resource-charges-configuration.html, controller
  resourceChargesConfigurationController, NO resolve gate. Verified NOT
  inside a comment (the nearest /* at 18850 closes at 18855).
- Menu node: node 761 'Resource Charges Configuration' EXISTS under 514
  (privilege tree, psql-verified) but carries NO actionLink — the menu
  cannot navigate to it. Same dangle shape as surgicalCalendar (710).
- Controller loaded: index.html:1657
  (resources/assets/js/controller/otims/resourceChargesConfigurationController.js).
- Backend: REAL — ResourceChargeConfigurationController (Global,
  com.param.global.otims) `@RequestMapping("/api/otims")`, 6 endpoints:
  resourceChargeConfiguration (save), updateResourceChargeConfiguration,
  resourceChargeConfiguration/{id} (byId GET),
  resourceChargeConfiguration/{orgId}/{unitId} (list GET),
  updateResourceChargeConfigurationStatus, getResourceChareges.
- LIVE-VERIFIED BY CALLING (body inspected, ENDPOINT-VERIFICATION STANDARD):
  GET /api/otims/resourceChargeConfiguration/1/1 -> 200 success listObject []
  (dev-empty); POST /adt/getActiveSpecialityList {organizationId:1,unitId:1}
  -> 200 success 2 REAL rows (General Medicine, Orthopedics); GET
  /api/procedure/getRoleList/1/1 -> 200 success 2 REAL rows (Surgeon, Ass.
  Surgeon); POST /api/otims/getResourceChareges {orgId:1,unitId:1} -> 200
  success [] (dev-empty).

## Scoped backend check

- Phantom columns: PASS — live 200s prove every named-query column resolves.
- INNER-JOIN drops: FINDING — GET_RESOURCE_CHARGE_CONFIGURATION_LIST INNER
  JOINs specialityMaster + serviceMaster + procedureRoleMaster
  (ResourceChargeConfiguration.java:60-62): a config row whose
  speciality/service/role is missing or other-org is DROPPED from the list.
  The walk's FIFTH list-path INNER-JOIN instance. NOT FIXED (Rule 10).
- Dup-check: PASS — IS_RESOURCE_CHARGE_CONFIGURATION_ALREADY_EXIST (save)
  and _NOT_BY_ID (update) are org+unit+role+service all-AND scoped (the
  update adds amount). NOT the Q29/Q32/BR-169 class, NOT cross-org.
- Org-trust: FINDING (module-wide class, screen-17 record) — orgId
  payload-carried on the write path, no VerifiedIdentityService. NOT FIXED
  (Rule 10).
- Swallowed exceptions: PASS — the DAO (save catch) and the service BOTH
  RETHROW ServiceException (the global-handler pattern, not
  catch-and-return); the service returns the DAO's response verbatim
  (ServiceImpl:35) with the dup-check returning a proper ERROR. NO
  false-success shape — the walk's FIRST clean write path in the OTIMS
  module (contrast screens 19/20/21's BR-187-shape saves).
- DATE-FORMAT CONTRACT (finding, fixed in the rebuild, 2026-08-10): the
  save DTO's createdDate/updatedDate are Strings the Dozer DateConverter
  parses with the format `DD-MM-YYYY HH:mm:ss` (the legacy sends
  moment().format('DD-MM-YYYY HH:mm:ss') — controller JS:175-176). An
  ISO-8601 timestamp 500s with 'Unable to parse source object using
  specified date format' (verified live, ServiceException at
  ServiceImpl:39). The rebuild sends the legacy format. The walk's first
  date-format contract on a write path.
- UNIT-ID OMISSION — FINDABILITY DEFECT (owner click-through, 2026-08-10,
  fixed): a green success toast with a search that found nothing. The toast
  was TRUE (rows verified in psql — NOT the BR-187 false-success class),
  but the saved rows carried unit_id NULL because the rebuild's save
  payload OMITTED unitId (the legacy sends `unitId: $scope.unitId` —
  controller JS:65). The list query requires `rcc.unitId=:unitId`, so
  NULL = 1 filters every saved row OUT of the list — the row exists in the
  DB but is INVISIBLE to the user (grid + search). Fixed by adding unitId
  to both save and update payloads. Verified end to end: with unitId the
  API list returns the row with all join names resolved, and the grid shows
  it. PROBE STANDARD EXTENDED (owner item 4): save probes must now assert
  FINDABILITY — the saved row appears in the reloaded grid (the amount
  renders without trailing zeros — '250' not '250.00'). A save probe that
  verifies only psql checks half the path.

## Divergences from legacy (named)

- House conventions: signals + FormsModule, typed service responses,
  honest-error toasts.
- Service select: the legacy uses a uib-typeahead service picker; the
  rebuild uses a plain service-name text input (the serviceId is required
  for the save DTO; the typeahead's item-picker search endpoint
  (/api/global/search/services/procedure) was NOT live-verified in this
  screen's scoped check — divergence flagged, the service field accepts the
  name and the save requires a serviceId, which in dev has no populated
  service list to pick from).
- Status toggle reloads the grid after a successful toggle.
- Full-DTO save (both dates + createdBy/updatedBy per the legacy contract).

## Build state

- Route: NEW (registered for this screen — moduleId 761).
- Gates: preflight C1-C7 (C7 PASS — genuinely new; C5 DEAD-legacy flag
  expected for the dangle). ng build exit 0. Smoke: route serves, form +
  grid render, speciality + role dropdowns live-populated, 0 page errors.
  Save probe: negative control (incomplete form rejected client-side, 0
  wire posts) + positive (full-DTO save, verified, cleaned per Q28).

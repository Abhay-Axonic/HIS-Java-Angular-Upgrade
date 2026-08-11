# Corporate Master — screen 39 (module 574, Billing)

Parity rebuild of the legacy
`views/master/GlobalMaster/corporate-master-global.html` (menu node 574
'Corporate Master', parent 570 Billing -> 531 Unit Master, controller
corporateMasterController loaded index.html:1628). NOTE: the template
lives under GlobalMaster/ (the legacy's global-master folder) even though
the node hangs under Billing.

## CLASSIFICATION — LIVE (C7 + C8 at selection; the recheck's LAST confirmed
## LIVE — the 585 lesson)

- State: REGISTERED (C5 PASS — node chain 574 -> 570 -> 531). Controller
  binds corporateMasterController (NOT MasterController).
- The screen is the corporate-master CRUD surface (companyTypeId 3 =
  corporate): company list + relation list + save.
- LIVE-VERIFIED BY CALLING (body status):
  - POST /api/global/getCompanyMasterList {organizationId, unitId,
    companyTypeId: 3} -> 200 success listObject [] (dev-empty);
  - the save endpoint /api/global/saveCompanyMaster exists (mapped).

## PAYLOAD LESSON (shared DTO, the screen-37 class)

{organizationId, unitId} — the orgId key 500s at Jackson deserialization
(CompanyMasterDto's 57 known properties reject orgId; verified in screen
37's catalina stack).

## SCOPED WRITE-PATH CHECK (billing master write)

saveCompanyMaster — the controller catch returns an HONEST ERROR with the
REAL message (the BR-batch2 masking fix, live-proven:
`e.getMessage() != null ? e.getMessage() : "Company master save failed"`);
the DAO catch falls to honest ERROR/COMMON_ERROR; SUCCESS only after the
real save + the company-contract-grade-mapper batch. The SUCCESS-from-catch
false-success class is ABSENT. (The DAO's COMMON_ERROR_MESSAGE fallback is
the generic honest envelope, not the masked-null class.)

## Build state

- Route: NEW at /corporateMasterGlobal (moduleId 574).
- Gates: preflight C1-C8 (C5 PASS; C7 PASS — genuinely new; C8 PASS —
  moduleId 574 only route). ng build exit 0. Smoke: route renders, the
  honest dev-empty company list, 0 page errors.

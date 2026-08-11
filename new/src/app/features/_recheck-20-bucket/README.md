# The 20-bucket recheck — per-screen endpoint truth (2026-08-10)

The owner's queued item (run once the confirmed candidates were done): the
sweep classified 20 Billing nodes as NOT-A-SCREEN-YET via state+node+backend
class-name grep. The 585 lesson: the class-name grep UNDER-COUNTS. Per-screen
endpoint-truth classification (CALLED the endpoints, body status inspected):

## LIVE (real controllers, real bindings, real handlers verified by calling)
- 572 TPA Master (TPAmaster) — getUnitApplicationRegistrationFieldConfig/1/1
  -> 200 success [] (dev-empty). State app.js:9976 binds TPAMasterController.
- 574 Corporate Master (corporateMasterGlobal) — getCompanyMasterList
  {organizationId,unitId,companyTypeId:3} -> 200 success [] (dev-empty).
  State binds corporateMasterController (the orgId extra key 400s — payload
  class). NOTE: this is the GLOBAL master (template under GlobalMaster/).
- 584 Unit Service Mapping (unitServiceMapping) — /adt/subspeciality/orgId/
  1/unitId/1/specialityId/1 -> 200 success with REAL rows (Cardiology/CARDIO).
  State binds UnitServiceMappingController (28 binds — the DECOY test passes).
- 608 Company Contract (companyContractMaster) — /Unit/Service/EHCpackage/
  getPackgeType/1/1 -> 200 success with REAL package rows (All Inclusive/EHC).
  State binds companyContractController (40 binds — the DECOY test passes).

## DECOY-ROUTED (MasterController shell + 0-3 template binds — the
## consentType/580/587 class; require the owner's class decision)
- 577 Package List (packageList) — MasterController, 0 binds.
- 581 Account Payable Comparsion (accountPayableComparsion) — MasterController, 0 binds.
- 582 Account Payable Status (accountPayableStatus) — MasterController, 0 binds.
- 586 Schedule Price Unit Service (schedulePriceUnitService) — MasterController, 1 bind.
- 588 Order Set List (orderSetList) — MasterController, 0 binds.
- 589 Admission Rejection List (admissionRejectionList) — MasterController, 0 binds.
- 591 Find Bill List (findBillList) — MasterController, 0 binds.
- 593 Charge Conversion 2nd node (entitlementCoversion) — MasterController, 0 binds, EMPTY template (also the dead-markup class).
- 594 Auto Charge Review (autoChargeReview) — MasterController, 0 binds.
- 595 Write Off (writeOff) — MasterController, 0 binds.
- 596 Write Off Details (writeOffDetails) — MasterController, 0 binds.
- 599 Round Off Configuration (roundOffConfiguration) — MasterController, 3 binds.
- 601 Remainder For NOK (reminderForNOK) — MasterController, 0 binds.
- 620 IP Default Service Configuration (IPdefaultServiceConfig) — MasterController, 1 bind.

## OTHER
- 611 OP Consulation Configuration (OPConsulationConfigurationMaster) — real
  controller binding but the TEMPLATE IS EMPTY (0 bytes) — the dead-markup
  class; the state exists, the screen body does not.

## COUNT CLOSURE
- The sweep's 20 NOT-A-SCREEN-YET -> 4 LIVE (572, 574, 584, 608) +
  14 DECOY-ROUTED + 1 dead-markup (611) + 1 (593 counted above).
- The decoy queue's instance list grows from 4 (528, 578, 580, 587) to 18
  with 577, 581, 582, 586, 588, 589, 591, 593, 594, 595, 596, 599, 601, 620.
  The owner's class decision ("build the real screen behind the shell, or
  leave the shells") now covers 18 instances.
- The walk's next LIVE screens: 584 Unit Service Mapping, 608 Company
  Contract (plus the earlier 572/574 if the owner wants the global masters).

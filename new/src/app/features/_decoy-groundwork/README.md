# DECOY GROUNDWORK — ITEM 2 (prep only, no builds) — 2026-08-10

The 18 decoy instances, per-instance preparation so the owner's class
decision is cheap. Each: what the real screen is, backend live-status
(verified by CALLING, body status), what building involves.

## GROUP A — REAL BACKEND EXISTS + REAL CONTROLLER EXISTS (3 instances)

These are screens whose menu shell binds MasterController but a REAL
controller AND a live backend exist elsewhere in the legacy. Building = a
standard parity rebuild (controller binding + endpoint wiring + probe).

- 528 Consent Type: state binds MasterController (0 binds, 4,837-byte
  template) but adt/global/consentTypeMasterController.js exists with
  GETCONSENTTYPELIST + COUNTCONSENTTYPEMASTER. BACKEND LIVE (CALLED):
  /adt/getConsentTypeMasterList -> 200 success with REAL rows
  (consentTypeMasterId 4 'Fresh Consent Modal Test'); /adt/getConsentTypeCount
  -> 200 success object: 3. BUILD = master list + CRUD on live feeds.
- 578 Variable Pricing: state variableSquencePricing (the TYPO) binds the
  shell; billing/global/VariablePricingController.js exists UNBOUND (the
  consentType pattern). BACKEND UNVERIFIED this pass (the patient-category
  feed's constant /getPatientCategoryMaster needs its base prefix —
  /global/getPatientCategoryMaster 404'd on my first guess). BUILD = needs
  the controller's endpoint truth before estimating.
- 587 Order Set: state binds the shell (0 binds, 5,137-byte template) but
  orderset/orderSetMasterController.js exists with /api/orderset/
  orderSetsList + orderSetsCount. BACKEND: /api/orderset/orderSetsList with
  the exact legacy payload {orgId, unitId, offset, recordPerPage} -> HTTP
  200 body {"status":"error","code":"500","message":"Something went
  wrong!!"} — the MASKED-ERROR envelope (honest message, but the screen's
  primary feed errors). BUILD = master list surface + root-cause the 500.

## GROUP B — TEMPLATE-ONLY, NO CONTROLLER ANYWHERE (15 instances)

The menu shell binds MasterController; a search of index.html for ANY
controller JS matching the screen's name finds NOTHING. No legacy reference
behavior exists beyond the static template. Building the real screen =
writing the backend surface FROM SCRATCH (new endpoints + DAO + service) —
a design decision, not a parity rebuild.

- 577 Package List (0 binds), 580 Account Payable (0 binds, the 9eb08762
  finding), 581 Account Payable Comparsion (0), 582 Account Payable Status
  (0), 586 Schedule Price Unit Service (1), 588 Order Set List (0), 589
  Admission Rejection List (0), 591 Find Bill List (0), 593 Charge
  Conversion 2nd node entitlementCoversion (0, empty template), 594 Auto
  Charge Review (0), 595 Write Off (0), 596 Write Off Details (0), 599
  Round Off Configuration (3), 601 Remainder For NOK (0), 620 IP Default
  Service Configuration (1).

## THE OWNER'S DECISION, CHEAPENED

- Group A (528, 578, 587): a genuine screen exists behind the shell —
  building is parity work with live (or near-live) backends. Recommended:
  build.
- Group B (15): the legacy never implemented these beyond a static menu
  template. Building = designing + writing new backend behavior. The two
  sane options: (a) leave the shells (the menu still shows them; the legacy
  was equally unimplemented), or (b) build them as new screens with new
  backends (a product decision, not a parity rebuild).

# Deposit Matrix Configuration — screen 35 (module 621, Billing)

Parity rebuild of the legacy
`views/master/UnitMaster/billingMaster/deposit-matrix-config.html` (menu
node 621 'Deposit Matrix Configuration', parent 570 Billing -> 531 Unit
Master, controller depositMatrixConfigController loaded index.html:873).

## CLASSIFICATION — LIVE (C7 + C8 at selection; the LAST confirmed LIVE from
## the Billing sweep)

- State: REGISTERED app.js:15172 `depositMatrixConfig` (the node's link
  target — C5's DEAD flag is the name mismatch, the screen-31 pattern).
  Controller binds depositMatrixConfigController (NOT MasterController —
  passes the DECOY test).
- Menu node: 621 'Deposit Matrix Configuration' -> 570 -> 531.
- Backend found BY CLASS NAME: DepositMatrixConfigController (billing/unit,
  /api/billing/unit): list GET depositMatrix/{orgId}/{unitId}/{offset}/
  {recordPerPage}, count GET, save POST, byId GET, update PUT, status GET.
- LIVE-VERIFIED BY CALLING (body status): the list -> 200 success with REAL
  dev rows (mDepositMatrixId 19, patientType 'General', paymentEntitlement
  'Self Entitlement', billingCategory...); the count -> 200 success object:
  5 — FIVE real dev rows. The grid renders REAL data (the first Billing
  screen with populated dev data).

## SCOPED BACKEND CHECK (billing write-path)

- FALSE-SUCCESS: ABSENT — the save (DepositMatrixConfigDaoImpl:62-90)
  catches BOTH RETHROW ServiceException; SUCCESS only after the real save.
  The BR-188 class is NOT present.
- THE GOOD CLASSES (hardened, live-proven — the walk's contrast set):
  - the org-trust fix binds JWT-verified tenant (lines 72-74 — the
    BR-batch2 fix; the old code wrote client-supplied org/unit and a
    org-1 session created an org-999 row);
  - the createdDate/updatedDate guards null-check before getDate (line 77 —
    no getDate(null) NPE class);
  - the BY_ID read is org/unit-scoped (lines 100-101 — the batch-4 fix; a
    forged victim ID cannot read cross-tenant config);
  - the update path explicitly rejects cross-tenant updates (line 128
    'Cross-tenant update rejected').

## Build state

- Route: NEW at /depositMatrixConfig (moduleId 621).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C8 PASS — moduleId 621
  only route; C5's DEAD explained — the state-name mismatch). ng build exit
  0. Smoke: route renders, the grid shows the 5 REAL dev rows, 0 page
  errors.

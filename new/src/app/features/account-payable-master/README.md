# Account Payable Master — screen 26 (module 579, Billing)

Parity rebuild of the legacy
`views/master/UnitMaster/billingMaster/account-payable-master.html` (menu
node 579 'Account Payable Master', parent 570 Billing -> 531 Unit Master,
controller accountPayableMasterController loaded index.html:886).

## CLASSIFICATION — LIVE (C7 + C8 at selection, the first Billing candidate)

- State registration: REGISTERED — app.js:10280 `.state('accountPayableMaster')`.
- Menu node: 579 'Account Payable Master' EXISTS, chain 579 -> 570 -> 531
  (C5 PASS).
- Backend: AccountPayableMasterController (Billing, com.param.billing.global)
  `@RequestMapping("/api/global")` — saveAccountPayable,
  getAccountPayable, updateAccountPayableStatus, getActiveAccountPayable.
- LIVE-VERIFIED BY CALLING (body status, not HTTP code): POST
  /api/global/getAccountPayable {orgId:1, orgUnitId:1} -> 200 success with
  REAL rows (accPayableMstId 6, AP-LIST-TEST — dev data exists); POST
  /api/global/getActiveAccountPayable -> 200 success same rows. Note: the
  payload is {orgId, orgUnitId} (controller JS:102-103) — {organizationId,
  unitId} 400s HIS-400 (the probe-payload class).
- Shares the controller family with 580 (Account Payable) — the contract
  extraction serves both screens.

## SCOPED BACKEND CHECK (the billing false-success focus, BR-188 family)

- FALSE-SUCCESS SHAPE: PASS — AccountPayableMasterDaoImpl.java save's catch
  RETHROWS (line 126, `throw e`); no SUCCESS-from-catch, no swallow. SUCCESS
  returns only after the real save/update (lines 113-119) inside the try.
  The service (AccountPayableMasterServiceImpl.java:22-28) returns the DAO
  response verbatim and rethrows too. The BR-188 collectPayment class is
  NOT present on this write path.
- Dup-check: PASS — CHECK_FOR_UNIQUE_CODE (orgId + orgUnitId + code +
  payCode all-AND) and CHECK_FOR_UNIQUE_CODE_WITH_ID (adds accPayableMstId)
  are properly org+unit scoped.
- INNER-JOIN drop: PASS — GET_ACCOUNT_PAY_LIST is single-table (no joins).
- Org-trust: PASS (the good class) — the DAO binds
  verifiedIdentityService.getVerifiedOrganizationId()/getVerifiedUnitId()
  (DAO:85-86), NOT the client-supplied org/unit (commented BR-batch2 fix,
  live-proven).
- Date contract: DD-MM-YYYY HH:mm:ss (legacy JS:308 moment format;
  GlobalCommonDateUtils "dd-M-yyyy HH:ss:mm" at DAO:90-93).
- NPE-CLASS FINDING (2026-08-10, verified live ERR-0a7cc3d9 — the walk's
  NINTH getDate(null) instance, the BR-187 class, ON A BILLING WRITE PATH):
  the DAO's save calls GlobalCommonDateUtils.getDate(accValidityFromDate,
  "dd-M-yyyy HH:ss:mm") UNCONDITIONALLY (DAO:90-91) — a payload omitting
  accValidityFromDate/accValidityToDate NPEs at GlobalCommonDateUtils:305
  (Cannot invoke String.length() because dateString is null) -> the catch
  rethrows -> 500 correlation envelope. NOT a false success (the rethrow
  surfaces it loudly), but a full-DTO contract: the rebuild sends both
  validity dates. Same shape as the BR-187 operation-theatre NPE and the
  screen-22 surgical-calendar contract.

## Build state

- Route: NEW at /accountPayableMaster (moduleId 579).
- Gates: preflight C1-C7 (C5 PASS with the node chain; C7 PASS — genuinely
  new; C8 PASS — moduleId 579 used only by this route). ng build exit 0.
  Smoke: route renders, form + grid, the live list populates (dev rows
  present), 0 page errors. Save probe: the full DTO with the DD-MM-YYYY
  contract + findability assertion (the saved row visible in the reloaded
  grid).

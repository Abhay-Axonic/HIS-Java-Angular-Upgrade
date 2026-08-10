/**
 * operation-theatre — parity rebuild of the legacy
 *   views/master/GlobalMaster/OTIMS/operation_Theatre.html
 *   (menu node 519 'Operation Theatrer' [sic], parent 514 OTIMS -> 369
 *   Global Master, actionLink #/operation_Theatre).
 *
 * LIVE CLASS under the amended reachability rule: the state
 * `operation_Theatre` IS registered (app.js:12357, dedicated
 * operationTheatreController — the controller file exists though NOT loaded
 * in index.html), the menu node exists, and a REAL full-CRUD backend exists
 * (Global OperationTheatreController, /api/otims/*, entity OperationTheatre).
 * The rebuild REGISTERS the route (moduleId 519) under the route token
 * `operationTheatre` (the menu's actionLink is #/operation_Theatre; the
 * state url is /operation_Theatre — the rebuild uses the same token the
 * menu targets).
 *
 * Live-verified BY CALLING with the BODY inspected (the ENDPOINT-VERIFICATION
 * STANDARD, RULES.md 5c2432fd):
 *   POST /api/otims/getOperationTheatreList  -> 200 body status 'success',
 *        listObject len=1 (REAL dev row: id 8, OT-TEST-01 'Test Theatre One',
 *        type 11 'test theatre', status A)
 *   POST /api/otims/getOperationTheatreCount -> 200 body 'success'
 *   POST /api/otims/getActiveOperationTheatreTypeList -> 200 body 'success',
 *        listObject len=2 (real type rows)
 * Payload shape: {orgId, unitId, offset, noOfRecordsPerPage} (NOT
 * organizationId — the initial probe with organizationId 400'd; the
 * probe-payload class, caught before code).
 */
export interface OperationTheatreRow {
  operationTheatreId?: number | null;
  theatreCode?: string | null;
  theatreName?: string | null;
  theatreDesc?: string | null;
  status?: string | null;
  orgId?: number | null;
  unitId?: number | null;
  theatreTypeName?: string | null;
  theatreTypeId?: number | null;
  storeId?: number | null;
  storeName?: string | null;
}

export interface OperationTheatreListPayload {
  orgId: number;
  unitId: number;
  offset: number;
  noOfRecordsPerPage: number;
}

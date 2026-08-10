/**
 * operation-theatre-type — parity rebuild of the legacy
 *   views/master/GlobalMaster/OTIMS/operation-theatre-type.html
 *   (menu node 521 'Operation Theatre Type', parent 514 OTIMS -> 369
 *   Global Master, actionLink #/operationTheatreType).
 *
 * LIVE CLASS under the amended reachability rule: the state
 * `operationTheatreType` IS registered (app.js:12413, dedicated
 * operationTheatreTypeController loaded in index.html:1730), the menu node
 * exists, and a REAL full-CRUD backend exists (Global
 * OperationTheatreTypeController, /api/otims/*, entity OperationTheatreType).
 * The rebuild REGISTERS the route (moduleId 521) under the route token
 * `operationTheatreType` (matching the state url /operationTheatreType and
 * the menu's actionLink).
 *
 * Live-verified BY CALLING with the BODY inspected (the ENDPOINT-VERIFICATION
 * STANDARD, RULES.md 5c2432fd):
 *   POST /api/otims/getOperationTheatreTypeList  -> 200 body status 'success',
 *        listObject len=2 (REAL dev rows: id 12 'general' OT1, id 11
 *        'test theatre' OT2, status A)
 *   POST /api/otims/getActiveOperationTheatreTypeList -> 200 body 'success',
 *        listObject len=2 (same rows)
 *   GET  /api/otims/getOperationTheatreTypeById/11/1/1 -> 200 body 'success',
 *        listObject len=1 (id 11)
 * Payload shape: {orgId, unitId} (list/active/byId all org+unit scoped).
 *
 * SCOPED BACKEND CHECK (per the standing rule):
 *  - phantom columns: PASS — all five named queries select only entity
 *    columns (operationTheatreTypeId, theatreTypeCode, theatreTypeName,
 *    theatreTypeDesc, status, orgId, unitId); single-table (no joins); the
 *    live 200s prove the query resolves. Table otims.m_operation_theatre_type
 *    matches the entity @Table.
 *  - INNER-JOIN drops: PASS — no joins in any query (single table).
 *  - dup-check precedence + scoping: PASS — GET_OPERATION_THEATRE_TYPE_BY_NAME
 *    (save path) and GET_OPERATION_THEATRE_TYPE_BY_NAME_NOT_ID (update path)
 *    are BOTH org+unit bound with the (desc OR name) branch correctly
 *    parenthesized inside the AND chain — not the OR-precedence class, and
 *    unlike 519's BY_NAME_NOT_ID, this one IS scoped. Not cross-org
 *    reachable.
 *  - org-trust: FINDING (module-wide) — orgId/unitId are payload-carried
 *    (DTO), no VerifiedIdentityService; same gap as the rest of the OTIMS
 *    module (recorded with screen 17). Write paths are payload-trusting.
 *  - swallowed exceptions: FINDING — the service catch returns COMMON_ERROR
 *    (good), but the FALSE-SUCCESS shape is present on the save path:
 *    OperationTheatreTypeServiceImpl.saveOperationTheatreType's SUCCESS
 *    return (:56) sits OUTSIDE the if(res.getObject()!=null) guard, and the
 *    DAO (OperationTheatreTypeDaoImpl:59-64) catch-and-returns the ERROR
 *    envelope WITHOUT rethrowing — the identical BR-187 mechanism. With an
 *    INCOMPLETE DTO (updatedDate omitted) the DAO NPEs at :52
 *    (getDate(null)) and the service returns "Saved Successfully" while
 *    writing NOTHING. CONFIRMED by source trace (the walk's SIXTH confirmed
 *    false-success instance; the BR-187 fix shape applies — same class,
 *    same incomplete-DTO attack path, data-driven and demonstrable). This is
 *    the reason the rebuild's Save is wired with the full DTO contract
 *    (createdDate + updatedDate both sent) AND the component rejects an
 *    incomplete form client-side; a backend BR is queued for the service/DAO
 *    pair.
 */
export interface OperationTheatreTypeRow {
  operationTheatreTypeId?: number | null;
  theatreTypeCode?: string | null;
  theatreTypeName?: string | null;
  theatreTypeDesc?: string | null;
  status?: string | null;
  orgId?: number | null;
  unitId?: number | null;
  createdBy?: number | null;
  createdDate?: string | null;
  updatedBy?: number | null;
  updatedDate?: string | null;
}

export interface OperationTheatreTypeListPayload {
  orgId: number;
  unitId: number;
  offset?: number;
  noOfRecordsPerPage?: number;
}

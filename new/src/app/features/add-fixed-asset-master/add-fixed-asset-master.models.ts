/**
 * Screen-filter dropdown option (store or status), as returned inside the
 * `/dropdowns/{unitId}/{organizationId}/{userId}` response. Legacy prepends
 * a synthetic `{ "store": "All" }` / `{ "status": "All" }` entry (no `id`,
 * i.e. `undefined`) ahead of the real list -- modeled here as `id: 0` so
 * "All" round-trips through the search payload as `storeId/statusId:
 * undefined` (falsy-id treated as "no filter", same effect as legacy's
 * `self.model.store.id` being `undefined` for the synthetic row).
 */
export interface AfamOption {
  id: number;
  store?: string;
  status?: string;
  [key: string]: unknown;
}

/** Raw shape of the dropdowns endpoint response. PMS frequency / working
 * condition lists are also present in the real payload (needed by the
 * not-yet-built create/edit form) but unused by this search/list screen. */
export interface AfamDropdowns {
  store: AfamOption[];
  status: AfamOption[];
  [key: string]: unknown;
}

/** One row of the search-results table (`self.model.fixedAssetData`). */
export interface AfamRow {
  id: number;
  opbNo: string;
  opbDate: string;
  storeId?: number;
  storeName: string;
  requestedBy: string;
  statusId?: number;
  status: string;
  isSelected?: boolean;
  [key: string]: unknown;
}

/** Global status-master entry (`GET /api/global/status`, via StatusService
 * in legacy) -- used ONLY to look up the id for the status name 'Closed'
 * when building the approve/cancel PATCH payloads. Distinct from the
 * per-screen `status` filter dropdown above, which comes from this
 * screen's own `/dropdowns` endpoint. */
export interface AfamStatusMasterEntry {
  id: number;
  status: string;
}

/** Cancel-reason dropdown entry (`GET /api/global/cancel/reasons`). */
export interface AfamCancelReason {
  id: number;
  reason: string;
}

export interface AfamSearchRequest {
  fromDate: string;
  toDate: string;
  storeId?: number;
  statusId?: number;
  unitId: number;
  userId: number;
}

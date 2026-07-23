/**
 * Rebuild of the legacy `assetReturn` state's data shapes -- see
 * assetReturnController.js (367 lines) + asset-return.html (420 lines).
 * Field names taken directly from
 * `com.param.assetManagement.indent.assetReturn.dto.AssetReturnResponse`
 * (legacy/AssetManagement/.../indent/assetReturn/dto/AssetReturnResponse.java)
 * -- the SAME DTO backs both this screen's search results and
 * details-asset-return's single-record GET -- not guessed.
 */
export interface AssetReturnOption {
  id: number;
  store?: string;
  status?: string;
  [key: string]: unknown;
}

export interface AssetReturnDropdowns {
  fromStore: AssetReturnOption[];
  toStore: AssetReturnOption[];
  status: AssetReturnOption[];
}

/**
 * One search-result row.
 *
 * `returnBy`/`remark` (singular) are the REAL DTO field names -- not
 * `returnedBy`/`remarks`, which is how the spec for this rebuild loosely
 * described them; verified against AssetReturnResponse.java and the
 * legacy template's own `{{item.returnBy}}` / `{{item.remark}}` bindings
 * (asset-return.html:269-270) before modeling this interface.
 *
 * `type` is intentionally NOT modeled here: the legacy view renders
 * `{{item.type}}` (asset-return.html:268) under a "Type" column, but
 * AssetReturnResponse.java has no `type` field at all -- that cell is
 * always blank in legacy too, a dead/unmapped binding, not fabricated
 * here either.
 */
export interface AssetReturnRow {
  id: number;
  returnNo?: string;
  returnDate?: string;
  fromStoreId?: number;
  fromStore?: string;
  toStoreId?: number;
  toStore?: string;
  returnBy?: string;
  remark?: string;
  statusId?: number;
  status?: string;
  approvalStatusId?: number;
  approvalStatus?: string;
  isSelected?: boolean;
  /** Allows generic keyed access for the column-sort swap-bug logic
   * (`AssetReturn.sortedRows`) without an explicit switch per field. */
  [key: string]: unknown;
}

/**
 * Global status-master entry (`/api/global/status`, legacy's
 * `StatusService.GetStatus()`) -- used ONLY to look up ids by name
 * ('Pending', 'Pending Approval', 'Cancelled') for the approve/cancel
 * PATCH payloads (ctrl:280-286's `setStatus(x)`). NOT the same list as
 * the filter bar's Status(n) dropdown, which comes from this screen's own
 * dropdowns endpoint instead (see `AssetReturnDropdowns.status`) -- two
 * separate lists in legacy (`self.model.statuselect` vs
 * `self.model.statuses`), kept separate here too.
 */
export interface AssetReturnStatusMasterEntry {
  id: number;
  status: string;
}

/** `/api/global/cancel/reasons` entry -- legacy's `cancelReasonService.GetReason()`. */
export interface AssetReturnCancelReason {
  id: number;
  reason: string;
}

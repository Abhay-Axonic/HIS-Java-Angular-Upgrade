/**
 * Rebuild of legacy `linenIssuePendingDetails` state -- see
 * linenIssuePendingDetailsController.js (196 lines, self-registered
 * `.config($stateProvider)`, controllerAs:'vm') +
 * linen-issue-pending-details.html (655 lines). Response shapes taken
 * directly from the real backend DTOs `LinenResponse.java`/
 * `ItemResponse.java`/`BatchResponse.java`/`Carrier.java` -- the exact
 * fields `LinenIssueService.getLinenIssue` actually populates (see
 * legacy/LinenLaundry/.../linenIssue/service/LinenIssueService.java:157-199).
 */
export interface LinenPendingBatch {
  /** Never populated by `Util.PopulateBatchWithStock` on load (confirmed
   * via source) -- always undefined until/unless a future save response
   * sets it. Kept only so the save payload shape matches legacy's. */
  id?: number;
  /** Legacy coerces this to a string immediately on load
   * (`batch.batchId = batch.batchId.toString()`, ctrl:48) since it's a
   * `Long` on the wire but used as a `<select>` value client-side. */
  batchId: string;
  batchNo?: string;
  expiry?: string;
  availableStockQuantity?: number;
  /** Never populated on load (BatchResponse's `issueQuantity` field is
   * never set by `PopulateBatchWithStock`) -- always starts undefined;
   * the user types a fresh value per session. */
  issueQuantity?: number;
}

export interface LinenPendingItem {
  /** LinenIssueDetail id -- the row id sent back in the save payload's
   * `items[].id`. */
  id: number;
  /** Item master id -- what the "expand row" toggle and the Carrier-less
   * batch table key off (`vm.model.activeItem == item.itemId`, not
   * `item.id`). */
  itemId: number;
  itemCode?: string;
  itemName?: string;
  /** Indent quantity (view label "Indent Qty"). */
  quantity?: number;
  pendingQuantity?: number;
  /** View label "Stock Available" -- bound directly to `item.stockAvailable`. */
  stockAvailable?: number;
  /** Real initial value from the backend (`detail.getIssueQuantity()`),
   * but immediately and unconditionally overwritten on first render by
   * legacy's `{{item.issueQuantity = (item.batch | sumByColumn:
   * 'issueQuantity') | number}}` template assignment (view:143) -- so in
   * practice this always displays as the live sum of `batch[].issueQuantity`
   * from the moment the screen renders, not the persisted value. This
   * rebuild computes the same sum on demand (see `itemIssueTotal()` in
   * the component) rather than replicating an Angular-template assignment
   * side effect, which is not idiomatic here -- the two are behaviourally
   * identical.
   */
  issueQuantity?: number;
  batch: LinenPendingBatch[];
}

export interface LinenPendingCarrier {
  id: number;
  carrierName?: string;
}

export interface LinenPendingDetailResponse {
  id?: number;
  indentNo?: string;
  indentDate?: string;
  fromStoreId?: number;
  fromStoreName?: string;
  toStoreId?: number;
  priorityId?: number;
  priorityName?: string;
  toStoreName?: string;
  remark?: string;
  /** Confirmed real field on the wire (`LinenResponse.statusId`) but the
   * legacy view reads it at the WRONG scope path (`vm.model.statusId`
   * instead of `vm.model.materialIssuePendingDetail.statusId`) for its two
   * `ng-disabled` guards (Carrier select, Issue Qty input) -- so those
   * guards always evaluate `undefined == 17`, always false. This field is
   * kept on the model for fidelity/display purposes only; deliberately
   * NOT wired to any disable condition in this rebuild, matching legacy's
   * actual (broken-guard) behaviour. See the component's class doc
   * comment. */
  statusId?: number;
  status?: string;
  items: LinenPendingItem[];
  carrier: LinenPendingCarrier[];
}

export function emptyLinenPendingDetail(): LinenPendingDetailResponse {
  return { items: [], carrier: [] };
}

/** `/api/global/status` response shape -- same shared endpoint already
 * used by `details-asset-issue-acceptance` (Phase 4 batch 3). */
export interface LinenPendingStatus {
  id: number;
  status: string;
}

/** Real shape of `saveLinenIssue`'s response (`CommonResponse`, id +
 * message only -- its `status` field is never set server-side for this
 * endpoint, so this rebuild does NOT gate success on it, matching
 * legacy's own handler which reads `response.data.message` unconditionally
 * on any 2xx). */
export interface LinenPendingSaveResponse {
  id?: number;
  message?: string;
}

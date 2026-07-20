/**
 * Row shape for the worklist search result -- see
 * `getreplaceDetailOnReturnByMultipleCrit` (ctrl:148) /
 * corrective-maintenance-return-worklist.html:201-239.
 */
export interface CmrwRow {
  callLogNumber?: string;
  requestDateTime?: string;
  itemCode?: string;
  itemName?: string;
  newSerialNo?: string;
  requestedSerialNo?: string;
  completionDate?: string;
  replaceStatusId?: number;
  /** Derived client-side from replaceStatusId (ctrl:153-166) -- also the
   *  three-state toggle's live ng-model (view:223-226). */
  isAcceptOrReject?: 'A' | 'R' | 'N';
  correctiveMaintenanceSchedulingId?: number;
  assetReplacementId?: number;
  assetName?: string;
  [key: string]: unknown;
}

export interface StoreListItem {
  storeId: number;
  storeName: string;
  [key: string]: unknown;
}

export interface ReasonListItem {
  reasonMasterId: number;
  reasonDesc: string;
  [key: string]: unknown;
}

/** Completion-details modal payload -- getdetailsOfCM (ctrl:187-213) /
 *  view:268-644. */
export interface DetailsOfCM {
  requestTypeId?: number;
  requestCriticalityId?: number;
  assetcategory?: string;
  assetCode?: string;
  assetName?: string;
  serialNumber?: string;
  requestDateTime?: string;
  isDown?: boolean;
  downDateTime?: string;
  downTime?: string;
  requestDetails?: string;
  remark?: string;
  resolutionExpectedDateTime?: string;
  userName?: string;
  scheduleDate?: string;
  scheduleTime?: string;
  allocationRemark?: string;
  isStart?: boolean;
  requestAllocationTypeId?: number;
  ticketNo?: string;
  vendorName?: string;
  resolutionDetails?: string;
  isCompleted?: boolean;
  assetUpDate?: string;
  assetUpTime?: string;
  underObservation?: boolean;
  fromDate?: string;
  toDate?: string;
  /** Copied in from the clicked row, not part of the server response --
   *  see ctrl:198-199. */
  newSerialNo?: string;
  requestedSerialNo?: string;
  [key: string]: unknown;
}

/** Accept-modal payload -- getAcceptDetils (ctrl:216-225) / view:648-733. */
export interface AcceptReturnObj {
  newSerialNo?: string;
  requestedSerialNo?: string;
  itemName?: string;
  assetReplacementId?: number;
  itemCode?: string;
  assetName?: string;
}

/** Reject-modal payload -- rejectDetils (ctrl:254-258) / view:735-782. */
export interface RejectReturnObj {
  assetReplacementId?: number;
}

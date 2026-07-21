export interface CmStore {
  storeId: number;
  storeName: string;
  [key: string]: unknown;
}

/**
 * A row from getCorrectiveMaintenanceListByMultipleCriteria. Also doubles as
 * the shape read back out of `this.list[index]` by the completion/reopen
 * modals -- see correctiveMaintenanceController.js:188-424, which reads and
 * writes plain fields on the same row object throughout (itemId, storeId,
 * serialNumber, downDateTime, etc. are all read back out when building the
 * reopen payload).
 */
export interface CmRow {
  correctiveMaintenanceId?: number;
  callLogNumber?: string;
  requestDateTime?: string;
  requestTypeId?: number;
  assetCode?: string;
  assetName?: string;
  requestCriticalityId?: number;
  userName?: string;
  referenceLogNumber?: string | null;
  referenceId?: number | null;
  maintenanceStatusId?: number;
  isComplete?: boolean;
  completionDate?: string;
  completionTime?: string;
  completionRemak?: string;
  reOpen?: boolean;
  remark?: string;
  itemId?: number;
  storeId?: number;
  serialNumber?: string;
  isDown?: boolean;
  downDateTime?: string;
  downTime?: string;
  resolutionExpectedDateTime?: string;
  resolutionExpectedTime?: string;
  requestDetails?: string;
  batchId?: number;
  /** Client-side only, mirrors legacy's `value.check` stamped onto every row after a search. */
  check?: boolean;
  /** Client-side only, stamped on right before handoff -- see getDetails(). */
  editflag?: number;
  [key: string]: unknown;
}

export interface CmCompletionObj {
  completionDate: string;
  completionTime: string;
  isComplete: boolean;
  completionRemak: string;
}

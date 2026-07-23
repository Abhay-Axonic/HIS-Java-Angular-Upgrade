/**
 * Rebuild of the legacy `detailsCorrectiveMaintenanceScheduleWorklist`
 * state -- see detailsCorrectiveMaintenanceScheduleWorklistController.js
 * (855 lines) and details-corrective-maintenance-schedule-worklist.html
 * (826 lines). `DcmswRow` is the shape of `$stateParams.detailsObj` (==
 * `CorrectiveMaintenanceScheduleWorklistHandoffService.takeObject()` on
 * the rebuild side) -- the full row object handed off by the
 * `correctiveMaintenanceScheduleWorklist` list screen (either of its two
 * worklists), with schedule-specific fields present only when the row is
 * an existing schedule record (`correctiveMaintenanceScheduleId` set).
 */
export interface DcmswRow {
  correctiveMaintenanceScheduleId?: number;
  correctiveMaintenanceWorklistId?: number;
  correctiveMaintenanceId?: number;
  requestTypeId?: number;
  requestCriticalityId?: number;
  assetcategory?: string;
  assetCode?: string;
  assetName?: string;
  serialNumber?: string;
  batchId?: number;
  itemId?: number;
  storeId?: number;
  requestDateTime?: string;
  isDown?: boolean | string;
  downDateTime?: string;
  downTime?: string;
  requestDetails?: string;
  remark?: string;
  resolutionExpectedDateTime?: string;
  resolutionExpectedTime?: string;
  scheduleDate?: string;
  scheduleTime?: string;
  assignTo?: number;
  allocationRemark?: string;
  maintenanceWorklistStatusId?: number;
  /** DIFFERENT id-space than `maintenanceWorklistStatusId` above -- see class doc comment. */
  maintenanceScheduleStatusId?: number;
  requestAllocationTypeId?: number;
  ticketNo?: string;
  vendorId?: number;
  resolutionDetails?: string;
  /** Legacy's own field name (ctrl:637) -- kept as-is, not "corrected" to `resolutionRemark`. */
  reSolutionRemark?: string;
  isCompleted?: boolean;
  isStart?: boolean;
  isUnderObservation?: boolean;
  assetUpDate?: string | null;
  assetUpTime?: string;
  fromDate?: string;
  toDate?: string;
  completionDate?: string;
  correctiveMaintenanceSparesComsumptionDto?: DcmswSpareConsumption[];
  createdBy?: number;
  createdDate?: string;
  status?: string;
  updatedBy?: number;
  updatedDate?: string;
  unitId?: number;
  organizationId?: number;
  callLogNumber?: string;
  requestedByName?: string;
  userName?: string;
  isWorklist?: number;
  [key: string]: unknown;
}

/** `usersList` entries from getEmployeeListBySpecialityAndSubSpeciality (ctrl:182-205). */
export interface DcmswUser {
  userId?: number;
  name?: string;
  [key: string]: unknown;
}

/** `vendorObj` -- response.object from getvendorlistByBatchId (ctrl:275-297), used for OEM allocation. */
export interface DcmswVendorObj {
  vendorId?: number;
  vendorName?: string;
  [key: string]: unknown;
}

/**
 * `vendorList` entries from getVendorList (ctrl:299-315), used for Out
 * Source allocation. NOTE: unlike every other endpoint on this screen,
 * legacy reads this response as the bare array (`response.data`, checked
 * via `response.status == 200`), not the usual `{status,listObject}`
 * envelope -- ported below as a raw-array Observable, not ApiResponse.
 */
export interface DcmswVendorOption {
  id?: number;
  name?: string;
  [key: string]: unknown;
}

/** `serialNumberList` entries from getSerialNoByItemId (ctrl:651-678), used by the Replace modal. */
export interface DcmswSerialOption {
  batchId?: number;
  serialNumber?: string;
  [key: string]: unknown;
}

/** Typeahead source rows from getCorrectiveMainSparesComsumptionByitemId (ctrl:443-464). */
export interface DcmswSpareSearchResult {
  itemId?: number;
  itemCode?: string;
  itemName?: string;
  [key: string]: unknown;
}

/** Raw `response.data` rows from searchdatewise stock check (ctrl:419-440) -- also not ApiResponse-wrapped. */
export interface DcmswStockRow {
  currentStock?: number;
  [key: string]: unknown;
}

/** `sparesComsumptionList` entries -- added client-side (ctrl:476-542) or loaded via getSparesComsumptionlistById (ctrl:556-571). */
export interface DcmswSpareConsumption {
  correctiveMaintenanceSparesComsumptionId?: number | null;
  itemName?: string;
  itemCode?: string;
  itemId?: number;
  qty?: number | string;
  stock?: number;
  sparesComsumptionRemark?: string;
  status?: 'A' | 'I';
  createdBy?: number;
  createdDate?: string;
  updatedBy?: number;
  updatedDate?: string;
  unitId?: number;
  organizationId?: number;
  [key: string]: unknown;
}

/** `replaceDetailsList` entries from getreplaceItemById (ctrl:574-594). */
export interface DcmswReplaceDetail {
  assetReplacementId?: number;
  newSerialNo?: string;
  requestedSerialNo?: string;
  [key: string]: unknown;
}

/**
 * `replaceAssetObj` (ctrl:681-706) -- staged by the Replace modal's Save
 * button but NEVER posted by that button itself (see class doc comment's
 * dead-Replace note). It only reaches the server if the user separately
 * clicks the Resolution Details "Save" button afterward, as
 * `correctiveMaintenanceReplacementDto` on that call's payload.
 */
export interface DcmswReplaceAssetObj {
  correctiveMaintenanceId?: number;
  itemId?: number;
  requestedStoreId?: number;
  requestedSerialNoId?: number;
  newSerialNoId?: number;
  newStoreId?: number;
  createdBy?: number;
  createdDate?: string;
  updatedBy?: number;
  updatedDate?: string;
  unitId?: number;
  organizationId?: number;
  [key: string]: unknown;
}

/**
 * Form-only mirror of legacy's `resolutionObj` (ctrl:121-168), trimmed to
 * the fields actually read by the template/save payload -- several
 * legacy-declared keys (e.g. its own `correctiveMaintenanceSparesComsumptionDto`)
 * are dead: overwritten or ignored at runtime in favor of the separate
 * `sparesComsumptionList` array, so they aren't modeled here.
 */
export interface DcmswResolutionObj {
  requestAllocationTypeId: number;
  ticketNo: string;
  resolutionDetails: string;
  vendorId?: number | string;
  isCompleted: boolean;
  assetUpDateIso: string;
  assetUpTime: string;
  underObservation: boolean;
  fromDateIso: string;
  toDateIso: string;
  isStart: boolean;
  remark: string;
}

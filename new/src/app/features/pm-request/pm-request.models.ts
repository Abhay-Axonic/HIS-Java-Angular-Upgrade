/**
 * Real DTO/request shapes taken directly from
 * legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller/asset/pmRequestController.js
 * and its view, PM-request.html (+ ng-included partials
 * views/common/accessories-details.html, views/common/PMS-details.html).
 *
 * `PmRequest` is a *sibling* of the already-rebuilt `PMSchedule` screen
 * (new/src/app/features/pm-schedule/) -- same asset-management maintenance
 * cluster, same PMS-details.html partial, several identical endpoints
 * (manufacturer/vendor/accessory lookups, checklist fetch). Field shapes
 * below are aligned with pm-schedule.models.ts's equivalents where the
 * underlying data is the same (ManufacturerRow/VendorRow/AccessoryItemRow/
 * AssetScheduleChecklistRow), not re-derived from scratch.
 */

export interface PmRequestStore {
  storeId: number;
  storeName: string;
}

export interface Month {
  value: number;
  text: string;
}

/**
 * Mirrors `$scope.pmRequestObj` (pmRequestController.js:57-65). `month`/
 * `year` are deliberately typed `string` -- legacy initializes them via
 * `currentMonth.toString()`/`currentYear.toString()` (unpadded, e.g. "7"
 * for July) and the search `<select>`s bind the same unpadded string
 * values. This is load-bearing for a real, faithfully-ported quirk in
 * `getAssetDetailsByCriteria`'s OR-filter -- see pm-request.ts.
 */
export interface PmRequestCriteria {
  month: string;
  year: string;
  storeId: number;
  itemName: string;
  processStatus: string;
  unitId: number;
  organizationId: number;
}

/** One row of `getAssetDetailsByCriteria`'s `response.data.listObject`. */
export interface PmAssetRequestRow {
  assetScheduleId: number;
  storeId?: number;
  storeName?: string;
  itemCode?: string;
  itemName?: string;
  serialNo?: string;
  vendor?: string;
  pmsFrequency?: string;
  pmsType?: string;
  nextDate?: string;
  processStatus?: number;
  manufacturerId?: number;
  vendorId?: number;
  assetCategoryId?: number;
  productCategoryId?: number;
  itemNameId?: number;
}

/** Raw-array response from GET /api/globals/manufacturers/{organizationId}. */
export interface ManufacturerRow {
  id: number;
  name?: string;
  address?: string;
}

/**
 * Raw-array response from GET /api/unit/vendors/{unitId}/{organizationId}
 * -- see pm-request.ts's getVendorMasterList doc comment for the real,
 * pre-existing legacy argument-order bug this URL faithfully preserves.
 */
export interface VendorRow {
  id: number;
  name?: string;
  mobileNumber?: string;
  emailId?: string;
  contactPersonName?: string;
  phoneNumber?: string;
  address?: string;
  country?: string;
  state?: string;
  city?: string;
  area?: string;
  zipcode?: string;
}

/** Raw-array response from POST /api/global/items/unit/{unitId}. */
export interface AccessoryItemRow {
  itemId: number;
  itemCode?: string;
  itemName?: string;
  quantity?: number;
  serialNumber?: string;
}

/**
 * Insurance Details tab shape (PMS-details.html tab 4). Dead in legacy --
 * `getInsuranceList` is entirely commented out in pmRequestController.js
 * (lines 282-306) -- `insuranceDataList` is declared nowhere and the tab
 * always renders empty. Kept here only so the tab's table header/columns
 * can be typed; no fetch method exists for it, matching legacy exactly
 * (see pm-schedule.ts's identical, independently-confirmed finding for
 * the same partial).
 */
export interface InsuranceRow {
  insuranceName?: string;
  contactNo?: string;
  email?: string;
  contactPersonName?: string;
  policyNumber?: string;
  policyFromDate?: string;
  policyToDate?: string;
  amountInsured?: number;
  address?: string;
  country?: string;
  state?: string;
  city?: string;
  area?: string;
  zipCode?: string;
}

/** Response shape of fetchCheckListByAssetScheduleId's listObject rows. */
export interface AssetScheduleChecklistRow {
  checklistCode?: string;
  checklistName?: string;
}

/** Response shape of fetchScheduledCompletionAssignedData's listObject[0]. */
export interface ScheduledCompletionData {
  scheduledDate?: string;
  scheduledTime?: string;
  scheduledRemark?: string;
  completionDate?: string;
  completionTime?: string;
  completionRemark?: string;
}

/** Payload built by saveChangedSchedule() (pmRequestController.js:356-361). */
export interface SaveChangedSchedulePayload {
  assetScheduleId: number;
  scheduledDate: string;
  scheduledTime: string;
  scheduledRemark: string;
}

/** Payload built by saveCompletionDetails() (pmRequestController.js:386-391). */
export interface SaveCompletionDetailsPayload {
  assetScheduleId: number;
  completionDate: string;
  completionTime: string;
  completionRemark: string;
}

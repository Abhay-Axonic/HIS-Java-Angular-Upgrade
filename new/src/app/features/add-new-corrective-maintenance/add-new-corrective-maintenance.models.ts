/**
 * Field set taken directly from addNewCorrectiveMaintenanceController.js
 * `$scope.init()` (ctrl:110-158) -- not guessed. `itemId`/`requestTypeId`/
 * `requestCriticalityId` start as `''` in legacy (empty-string sentinel for
 * "nothing selected yet", checked with `== ""` throughout the validation
 * chain) and become numbers once a real selection is made -- modeled the
 * same way here rather than defaulting to 0, so the `=== ''` checks in
 * saveCorectiveMaintenance() stay byte-identical to ctrl:279-327.
 */
export interface CorrectiveMaintenanceObj {
  correctiveMaintenanceId: number | null;
  itemId: number | '';
  storeId: number;
  requestTypeId: number | '';
  requestCriticalityId: number | '';
  serialNumber: string;
  isDown: boolean;
  requestDateTime: string;
  downDateTime: string;
  downTime: string;
  batchId: string | number;
  resolutionExpectedDateTime: string;
  resolutionExpectedTime: string;
  requestDetails: string;
  maintenanceStatusId: number;
  remark: string;
  createdBy: number;
  createdDate: string;
  status: string;
  updatedBy: number;
  updatedDate: string;
  unitId: number;
  organizationId: number;
  assetName: string;
  assetCode: string;
  assetcategory: string;
}

/** ctrl:140-143. */
export interface SerialNoObj {
  batchId: string | number;
  serialNumber: string;
}

export interface AncmStoreOption {
  storeId: number;
  storeName: string;
}

/** Same shape as legacy's `/api/global/assets/categories/{typeId}/...` response -- see implant-master.models.ts's identical AssetCategoryOption for the sibling screen that calls the same endpoint family (assetTypeId 1 there, 2 here). */
export interface AncmAssetCategoryOption {
  id: number;
  category: string;
}

export interface AncmProductCategoryOption {
  id: number;
  category: string;
}

/** Fields as rendered by the "Add Item List" modal table (view:536-544). */
export interface AncmSearchItemRow {
  itemId: number;
  itemCode?: string;
  itemName?: string;
  assetCategory?: string;
  productCategory?: string;
  [key: string]: unknown;
}

/** ctrl:205-217 -- `data-ng-options="x as x.serialNumber for x in serialNumberList track by x.batchId"` (view:116), so the bound value is the whole row object, not just a scalar. */
export interface AncmSerialNoOption {
  batchId: string | number;
  serialNumber: string;
  [key: string]: unknown;
}

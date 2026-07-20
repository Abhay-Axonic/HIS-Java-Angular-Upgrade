/**
 * Rebuild of the legacy `detailsCorrectiveMaintenance` state -- see
 * detailsCorrectiveMaintenanceController.js and
 * details-corrective-maintenance.html. `DcmDetail` is the shape of
 * `$stateParams.detailsObj` (== `CorrectiveMaintenanceHandoffService.takeObject()`
 * on the rebuild side) -- the full row object handed off by the
 * `correctiveMaintenance` list screen, with `editflag` stamped onto it.
 */
export interface DcmDetail {
  itemId?: number;
  serialNumber?: string;
  batchId?: number;
  downDateTime?: string;
  downTime?: string;
  isDown?: boolean | string;
  resolutionExpectedDateTime?: string;
  resolutionExpectedTime?: string;
  editflag?: number;
  requestTypeId?: number;
  requestCriticalityId?: number;
  assetcategory?: string;
  assetCode?: string;
  assetName?: string;
  requestDateTime?: string;
  requestDetails?: string;
  remark?: string;
  updatedDate?: string;
  updatedBy?: number;
  [key: string]: unknown;
}

/** `serialNumberList` entries from getSerialNoByItemId (ctrl:82-91). */
export interface DcmSerialOption {
  batchId?: number;
  serialNumber?: string;
  [key: string]: unknown;
}

/** `manufactureDataList` rows -- see views/common/PMS-details.html:116-128. */
export interface DcmManufacturer {
  name?: string;
  address?: string;
  [key: string]: unknown;
}

/** `vendorDataList` rows -- see views/common/PMS-details.html:287-300. */
export interface DcmVendor {
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
  [key: string]: unknown;
}

/** `insuranceDataList` rows -- see views/common/PMS-details.html:423-443. */
export interface DcmInsurance {
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
  [key: string]: unknown;
}

/** Raw `accessoryList` entries as returned by getPMSDetailsByItemId (ctrl:172-184). */
export interface DcmAccessoryRaw {
  assetCode?: string;
  assetName?: string;
  quantity?: number;
  serialNumber?: string;
  [key: string]: unknown;
}

/** `itemDataList` rows -- the remapped shape ctrl:176-183 builds from DcmAccessoryRaw. */
export interface DcmAccessoryItem {
  itemCode?: string;
  itemName?: string;
  quantity?: number;
  serialNumber?: string;
}

/** `response.data.object` shape from getPMSDetailsByItemId (ctrl:170-184). */
export interface DcmPmsResponseObject {
  manufacturerList?: DcmManufacturer[];
  vendorList?: DcmVendor[];
  insuranceList?: DcmInsurance[];
  accessoryList?: DcmAccessoryRaw[];
}

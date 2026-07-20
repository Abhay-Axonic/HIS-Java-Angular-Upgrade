export interface PmScheduleUser {
  userId: number;
  name: string;
}

export interface PmScheduleStore {
  storeId: number;
  storeName: string;
}

export interface PmAssetScheduleRow {
  assetScheduleId: number;
  storeName?: string;
  itemCode?: string;
  itemName?: string;
  serialNo?: string;
  vendor?: string;
  scheduledDate?: string;
  scheduledTime?: string;
  nextDate?: string;
  pmsType?: string;
  storeId?: number;
  assignTo?: number;
  assignOn?: string;
  processStatus?: number;
  manufacturerId?: number;
  vendorId?: number;
  assetCategoryId?: number;
  productCategoryId?: number;
  itemNameId?: number;
}

export interface PmScheduleCriteria {
  fromDate: Date | string;
  toDate: Date | string;
  storeId: number;
  pmsType: string;
  processStatus: string;
  unitId: number;
  organizationId: number;
}

export interface ManufacturerRow {
  id: number;
  name?: string;
  address?: string;
}

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

export interface AccessoryItemRow {
  itemId: number;
  itemCode?: string;
  itemName?: string;
  quantity?: number;
  serialNumber?: string;
}

export interface AssetScheduleChecklistRow {
  checklistCode?: string;
  checklistName?: string;
}

export interface ScheduledCompletionAssignedData {
  scheduledDate?: string;
  scheduledTime?: string;
  scheduledRemark?: string;
  completionDate?: string;
  completionTime?: string;
  completionRemark?: string;
  assignTo?: number;
  assignRemark?: string;
}

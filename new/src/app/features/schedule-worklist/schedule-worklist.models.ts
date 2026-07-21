export interface ScheduleWorklistUser {
  userId: number;
  name: string;
}

export interface ScheduleWorklistStore {
  storeId: number;
  storeName: string;
}

export interface AssetScheduleRow {
  assetScheduleId: number;
  scheduledDate?: string;
  scheduledTime?: string;
  storeId?: number;
  storeName?: string;
  itemCode?: string;
  itemName?: string;
  serialNo?: string;
  vendor?: string;
  pmsType?: string;
  assignTo?: number;
  completionDate?: string;
  processStatus?: number;
  nextDate?: string;
}

export interface ScheduleWorklistCriteria {
  fromDate: Date | string;
  toDate: Date | string;
  storeId: number;
  pmsType: string;
  processStatus: string;
  assignTo: number;
  unitId: number;
  organizationId: number;
}

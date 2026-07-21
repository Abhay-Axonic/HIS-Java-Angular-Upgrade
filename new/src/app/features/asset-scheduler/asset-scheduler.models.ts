export interface PmsFrequency {
  code: string;
  description: string;
}

export interface ChecklistEquipment {
  checklistEquipmentMasterId: number;
  checklistName: string;
  checklistCode: string;
  status: 'A' | 'I';
}

export interface AssetScheduleChecklistItem {
  assetScheduleChecklistId: number;
  assetScheduleId: number;
  checklistNameId: number;
  checklistName: string;
  checklistCode: string;
}

export interface SchedulerObj {
  assetScheduleId: number;
  itemNameId?: number;
  serialNo?: string;
  assetCategory?: string;
  itemCode?: string;
  itemName?: string;
  vendorName?: string;
  vendorId?: number;
  manufacturerId?: number;
  pmsFrequency: string;
  pmsType: string;
  startDate: string;
  endDate: string;
  createdBy: number;
  createdDate: string;
  status: string;
  processStatus: number;
  updatedBy: number;
  updatedDate: string;
  unitId: number;
  organizationId: number;
}

export interface CreateAssetScheduleRow {
  assetScheduleId: number;
  itemNameId?: number;
  itemCode?: string;
  itemName?: string;
  serialNo?: string;
  vendor?: string;
  vendorId?: number;
  pmsFrequency?: string;
  nextDate?: Date | string;
  pmsType?: string;
  remark?: string;
  storeId?: number;
  manufacturerId?: number;
  processStatus?: number;
  status?: string;
  unitId?: number;
  organizationId?: number;
  createdBy?: number;
  createdDate?: string;
  updatedBy?: number;
  updatedDate?: string;
  getChecklistData: AssetScheduleChecklistItem[];
}

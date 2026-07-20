export interface DswChecklistEquipment {
  checklistEquipmentMasterId: number;
  checklistName: string;
  checklistCode: string;
}

export interface DswStore {
  storeId: number;
  storeName: string;
}

export interface DswAssetChecklistItem {
  assetScheduleChecklistId: number;
  assetScheduleId: number;
  checklistNameId: number;
  checklistName: string;
  checklistCode: string;
}

export interface DswSpareConsumptionItem {
  spareConsumptionId: number;
  assetId?: number;
  assetName?: string;
  assetCode?: string;
  storeId?: number;
  serialNo?: string;
  assetQty?: number;
  remark?: string;
}

export interface DswItemSearchResult {
  itemId: number;
  itemCode: string;
  itemName: string;
}

export interface DswPmsDetails {
  pmsDetailsId?: number;
  assetScheduleId?: number;
  ticketNo?: string;
  pmsDetails?: string;
  pmsRemarks?: string;
  workOrderStatus?: string;
  completionDate?: string;
  completionTime?: string;
  underObservationDate?: string;
  underObservationTime?: string;
  getSpareConsumptionList?: DswSpareConsumptionItem[];
}

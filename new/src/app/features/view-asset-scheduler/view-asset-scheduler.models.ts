export interface AssetScheduleItem {
  assetScheduleId: number;
  itemCode?: string;
  itemName?: string;
  serialNo?: string;
  vendor?: string;
  pmsFrequency?: string;
  nextDate?: string;
  pmsType?: string;
  remark?: string;
}

export interface AssetScheduleChecklistItem {
  checklistName?: string;
  checklistCode?: string;
}

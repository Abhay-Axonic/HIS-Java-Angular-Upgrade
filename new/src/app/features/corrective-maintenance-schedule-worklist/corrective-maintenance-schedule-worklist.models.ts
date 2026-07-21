export interface CmswStore {
  storeId: number;
  storeName: string;
}

export interface CmswRow {
  callLogNumber?: string;
  requestDateTime?: string;
  assetCode?: string;
  assetName?: string;
  requestCriticalityId?: number;
  requestedByName?: string;
  userName?: string;
  scheduleDate?: string;
  scheduleTime?: string;
  completionDate?: string;
  completionTime?: string;
  completionRemak?: string;
  referenceLogNumber?: string | null;
  referenceId?: number | null;
  maintenanceWorklistStatusId?: number;
  complete?: boolean;
  [key: string]: unknown;
}

export interface CompletionObj {
  completionDate: string;
  completionTime: string;
  isComplete: boolean;
  completionRemak: string;
}

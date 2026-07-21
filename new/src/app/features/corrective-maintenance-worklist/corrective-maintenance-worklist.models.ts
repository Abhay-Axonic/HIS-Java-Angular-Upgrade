export interface CmwRow {
  callLogNumber?: string;
  requestDateTime?: string;
  requestTypeId?: number;
  assetCode?: string;
  assetName?: string;
  requestCriticalityId?: number;
  requestedByName?: string;
  userName?: string;
  scheduleDate?: string;
  scheduleTime?: string;
  referenceLogNumber?: string | null;
  referenceId?: number | null;
  maintenanceWorklistStatusId?: number;
  complete?: boolean;
  completionDate?: string;
  completionTime?: string;
  completionRemak?: string;
  correctiveMaintenanceId?: number;
  [key: string]: unknown;
}

export interface CompletionObj {
  completionDate: string | Date;
  completionTime: string;
  isComplete: boolean;
  completionRemak: string;
}

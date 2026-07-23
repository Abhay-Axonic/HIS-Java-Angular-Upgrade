import { Injectable } from '@angular/core';

/**
 * In-memory-only ui-router param, matching `detailsObj` (app.js's
 * `detailsCorrectiveMaintenanceWorklist` state, `params: { detailsObj: {} }`) --
 * sent by `correctiveMaintenanceWorklistController.js`'s `goToDetails(info)`
 * (rebuild: `CorrectiveMaintenanceWorklist.goToDetails(info)`), which hands
 * off the full clicked row from the worklist's `pagination.data` list, not
 * just an id. Same reasoning as the other per-pair handoff services in this
 * rebuild (see CorrectiveMaintenanceHandoffService, DetailScheduleWorklistHandoffService):
 * a plain service, not Router `extras.state`, since legacy's own param does
 * not survive a real page refresh either.
 *
 * Sending side: correctiveMaintenanceWorklist. Receiving side:
 * detailsCorrectiveMaintenanceWorklist. Both Phase 4 batch 14.
 */
export interface CorrectiveMaintenanceWorklistDetailObject {
  correctiveMaintenanceId?: number;
  correctiveMaintenanceWorklistId?: number;
  requestTypeId?: number;
  requestCriticalityId?: number;
  assetcategory?: string;
  assetCode?: string;
  assetName?: string;
  serialNumber?: string;
  requestDateTime?: string;
  isDown?: boolean;
  downDateTime?: string;
  downTime?: string;
  requestDetails?: string;
  remark?: string;
  resolutionExpectedDateTime?: string;
  scheduleTime?: string;
  scheduleDate?: string;
  assignTo?: number | string;
  allocationRemark?: string;
  maintenanceWorklistStatusId?: number;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class CorrectiveMaintenanceWorklistHandoffService {
  private pendingObject: CorrectiveMaintenanceWorklistDetailObject | null = null;

  setObject(obj: CorrectiveMaintenanceWorklistDetailObject): void {
    this.pendingObject = obj;
  }

  takeObject(): CorrectiveMaintenanceWorklistDetailObject {
    return this.pendingObject ?? {};
  }
}

import { Injectable } from '@angular/core';

/**
 * In-memory-only ui-router param, matching `detailsObj` (app.js's
 * `detailsCorrectiveMaintenanceScheduleWorklist` state, `params: { detailsObj: {} }`) --
 * sent by `correctiveMaintenanceScheduleWorklistController.js`'s
 * `goToDetails(info)`, which is the full row object from either of the
 * two worklists that screen renders (`getCorrectiveMaintenanceWorklist()`'s
 * result or `getSchedulingList()`'s result) -- not just an id. Same
 * reasoning as the other per-pair handoff services in this rebuild: a
 * plain service, not Router `extras.state`, since legacy's own param does
 * not survive a real page refresh either.
 *
 * Sending side: correctiveMaintenanceScheduleWorklist. Receiving side:
 * detailsCorrectiveMaintenanceScheduleWorklist. Both in Phase 4 batch 14.
 */
export interface CorrectiveMaintenanceScheduleWorklistDetailObject {
  correctiveMaintenanceScheduleId?: number;
  correctiveMaintenanceWorklistId?: number;
  correctiveMaintenanceId?: number;
  requestTypeId?: number;
  requestCriticalityId?: number;
  assetcategory?: string;
  assetCode?: string;
  assetName?: string;
  serialNumber?: string;
  batchId?: number;
  itemId?: number;
  storeId?: number;
  requestDateTime?: string;
  isDown?: boolean | string;
  downDateTime?: string;
  downTime?: string;
  requestDetails?: string;
  remark?: string;
  resolutionExpectedDateTime?: string;
  resolutionExpectedTime?: string;
  scheduleDate?: string;
  scheduleTime?: string;
  assignTo?: number;
  allocationRemark?: string;
  maintenanceWorklistStatusId?: number;
  maintenanceScheduleStatusId?: number;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class CorrectiveMaintenanceScheduleWorklistHandoffService {
  private pendingObject: CorrectiveMaintenanceScheduleWorklistDetailObject | null = null;

  setObject(obj: CorrectiveMaintenanceScheduleWorklistDetailObject): void {
    this.pendingObject = obj;
  }

  takeObject(): CorrectiveMaintenanceScheduleWorklistDetailObject {
    return this.pendingObject ?? {};
  }
}

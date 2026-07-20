import { Injectable } from '@angular/core';

/**
 * In-memory-only ui-router param, matching `assetDataObject` (app.js's
 * `detailScheduleWorklist` state, `params: { assetDataObject: {} }`) --
 * NOT the same param as AssetDataHandoffService's `assetDataObj` (a
 * different name, different shape: this one is the full asset-schedule
 * row object from getAssetDetailsByCriteria's response, not just
 * itemNameId/serialNo). Sent by scheduleWorklist's
 * `redirectToDetailScheduleWorklist(asset)` (screen 7/14) and, per entry
 * 25, also by `detailScheduleWorklist` viewing itself.
 *
 * Sending side built here (scheduleWorklist, screen 7/14). Receiving
 * side is detailScheduleWorklist, screen 10/14, later in this same batch.
 */
export interface AssetScheduleDetailObject {
  assetScheduleId?: number;
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
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class DetailScheduleWorklistHandoffService {
  private pendingObject: AssetScheduleDetailObject | null = null;

  setObject(obj: AssetScheduleDetailObject): void {
    this.pendingObject = obj;
  }

  takeObject(): AssetScheduleDetailObject {
    return this.pendingObject ?? {};
  }
}

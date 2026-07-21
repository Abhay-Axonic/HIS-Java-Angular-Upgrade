import { Injectable } from '@angular/core';

/**
 * In-memory-only ui-router param, matching `assetDataObj` (app.js's
 * `viewAssetScheduler`/`scheduleWorklist`/`detailScheduleWorklist` states
 * all declare `params: { assetDataObj: {} }`, no URL token) -- sent by
 * `preventiveMaintenanceController.js` (assetPMSScheduler, upstream of
 * this whole batch, not itself one of the 14 screens). Same reasoning as
 * WebAppointmentHandoffService: a plain service, not Router `extras.state`,
 * since legacy's own param does not survive a real page refresh either.
 */
export interface AssetDataObject {
  assetCategory?: string;
  productCategory?: string;
  itemCode?: string;
  itemName?: string;
  itemNameId?: number;
  serialNo?: string;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class AssetDataHandoffService {
  private pendingAssetData: AssetDataObject | null = null;

  setAssetData(data: AssetDataObject): void {
    this.pendingAssetData = data;
  }

  takeAssetData(): AssetDataObject {
    return this.pendingAssetData ?? {};
  }
}

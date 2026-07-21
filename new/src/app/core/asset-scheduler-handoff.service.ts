import { Injectable } from '@angular/core';

/**
 * In-memory-only ui-router param, matching `assetInfoObj` (app.js's
 * `assetScheduler` state, `params: { assetInfoObj: {} }`) -- yet another
 * distinctly-named handoff param in this same screen family (not
 * `assetDataObj`/`assetDataObject`). Sent by `assetPMSScheduler`, the
 * upstream screen this whole Asset Management batch descends from --
 * not itself one of this batch's 14 screens, so only the receiving side
 * is built here.
 */
export interface AssetInfoObject {
  itemNameId?: number;
  serialNo?: string;
  assetCategory?: string;
  itemCode?: string;
  itemName?: string;
  vendor?: string;
  vendorId?: number;
  manufacturerId?: number;
}

@Injectable({ providedIn: 'root' })
export class AssetSchedulerHandoffService {
  private pendingInfo: AssetInfoObject | null = null;

  setInfo(info: AssetInfoObject): void {
    this.pendingInfo = info;
  }

  takeInfo(): AssetInfoObject {
    return this.pendingInfo ?? {};
  }
}

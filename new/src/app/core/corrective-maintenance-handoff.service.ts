import { Injectable } from '@angular/core';

/**
 * In-memory-only ui-router param, matching `detailsObj` (app.js's
 * `detailsCorrectiveMaintenance` state, `params: { detailsObj: {} }`) --
 * sent by `correctiveMaintenanceController.js`'s `getDetails(obj, editflag)`,
 * which is the full row object from the corrective-maintenance list search
 * response with `editflag` stamped onto it, not just an id. Same reasoning
 * as the other per-pair handoff services in this rebuild: a plain service,
 * not Router `extras.state`, since legacy's own param does not survive a
 * real page refresh either.
 *
 * Sending side: correctiveMaintenance. Receiving side: detailsCorrectiveMaintenance.
 * Both in Phase 4 batch 3.
 */
export interface CorrectiveMaintenanceDetailObject {
  itemId?: number;
  serialNumber?: string;
  batchId?: number;
  downDateTime?: string;
  downTime?: string;
  isDown?: boolean;
  resolutionExpectedDateTime?: string;
  resolutionExpectedTime?: string;
  editflag?: number;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class CorrectiveMaintenanceHandoffService {
  private pendingObject: CorrectiveMaintenanceDetailObject | null = null;

  setObject(obj: CorrectiveMaintenanceDetailObject): void {
    this.pendingObject = obj;
  }

  takeObject(): CorrectiveMaintenanceDetailObject {
    return this.pendingObject ?? {};
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  PmRequestStore,
  PmRequestCriteria,
  PmAssetRequestRow,
  ManufacturerRow,
  VendorRow,
  AccessoryItemRow,
  AssetScheduleChecklistRow,
  ScheduledCompletionData,
  SaveChangedSchedulePayload,
  SaveCompletionDetailsPayload,
} from './pm-request.models';

/**
 * Endpoint paths taken directly from pmRequestController.js -- not guessed.
 * `GET /api/global/store/getStoreListById/{userId}/{unitId}` (the OTHER
 * store-related call legacy makes, conditional on sessionStorage's
 * `selectedTopStore` being missing) is intentionally NOT here -- that's
 * already handled screen-agnostically by StoreSelectionService
 * (new/src/app/core/store-selection.service.ts), used directly by
 * pm-request.ts instead of being re-implemented in this service.
 */
@Injectable({ providedIn: 'root' })
export class PmRequestService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** pmRequestController.js:81 -- populates the search bar's Store dropdown. */
  getDetailedStoreList(userId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, PmRequestStore>> {
    return this.http.get<ApiResponse<never, PmRequestStore>>(
      `${this.base}/api/global/getDetailedStoreList/${userId}/${organizationId}/${unitId}`,
    );
  }

  /** pmRequestController.js:184 -- see pm-request.ts for the OR-filter applied to the response. */
  getAssetDetailsByCriteria(criteria: PmRequestCriteria): Observable<ApiResponse<never, PmAssetRequestRow>> {
    return this.http.post<ApiResponse<never, PmAssetRequestRow>>(
      `${this.base}/api/assetManagement/maintenance/getAssetDetailsByCriteria`,
      criteria,
    );
  }

  /**
   * pmRequestController.js:226 (CONSTANTS.MASTER.GLOBAL.PHARAMACY.MANUFACTURER_MASTER.MANUFATURER_API).
   * Legacy's response here is a raw array (`response.data`), not the usual envelope.
   */
  getManufacturersByOrg(organizationId: number): Observable<ManufacturerRow[]> {
    return this.http.get<ManufacturerRow[]>(`${this.base}/api/globals/manufacturers/${organizationId}`);
  }

  /**
   * pmRequestController.js:266 (CONSTANTS.MASTER.UNIT.PHARAMACY.VENDOR_MASTER.VENDOR_MASTER_API).
   * REAL, PRE-EXISTING LEGACY BUG, FAITHFULLY PORTED: the URL is built as
   * `.../vendors/{unitId}/{organizationId}` -- legacy sends unitId first,
   * organizationId second, but the backend positionally expects
   * (orgId, unitId). A silent argument swap, confirmed present in 2 other
   * sibling screens too (pm-schedule's getVendorsByUnit has the identical
   * call shape). This 500s in the current dev environment -- a real,
   * pre-existing backend bug, not something this rebuild works around.
   * Do NOT "fix" the argument order; that would be an undisclosed
   * behavioral deviation from legacy.
   */
  getVendorsByUnit(unitId: number, organizationId: number): Observable<VendorRow[]> {
    return this.http.get<VendorRow[]>(`${this.base}/api/unit/vendors/${unitId}/${organizationId}`);
  }

  /** pmRequestController.js:253 -- raw-array response, filtered client-side by itemNameId. */
  getAccessoryItems(
    unitId: number,
    body: { assetTypeId: number; assetCategoryId?: number; productCategoryId?: number; storeId?: number },
  ): Observable<AccessoryItemRow[]> {
    return this.http.post<AccessoryItemRow[]>(`${this.base}/api/global/items/unit/${unitId}`, body);
  }

  /** pmRequestController.js:317 -- CheckList modal. */
  getCheckListByAssetScheduleId(
    assetScheduleId: number,
    unitId: number,
    organizationId: number,
  ): Observable<ApiResponse<never, AssetScheduleChecklistRow>> {
    return this.http.post<ApiResponse<never, AssetScheduleChecklistRow>>(
      `${this.base}/api/assetManagement/maintenance/fetchCheckListByAssetScheduleId`,
      { assetScheduleId, unitId, organizationId },
    );
  }

  /** pmRequestController.js:334 -- shared fetch behind both Schedule Details and Completion Details modals. */
  fetchScheduledCompletionAssignedData(
    assetScheduleId: number,
    unitId: number,
    organizationId: number,
  ): Observable<ApiResponse<never, ScheduledCompletionData>> {
    return this.http.post<ApiResponse<never, ScheduledCompletionData>>(
      `${this.base}/api/assetManagement/maintenance/fetchScheduledCompletionAssignedData`,
      { assetScheduleId, unitId, organizationId },
    );
  }

  /** pmRequestController.js:364 -- Schedule Details modal's Save. */
  saveChangedSchedule(payload: SaveChangedSchedulePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/saveChangedSchedule`, payload);
  }

  /** pmRequestController.js:394 -- Completion Details modal's Save. */
  saveCompletionDetails(payload: SaveCompletionDetailsPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/saveCompletionDetails`, payload);
  }
}

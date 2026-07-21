import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AncmAssetCategoryOption,
  AncmProductCategoryOption,
  AncmSearchItemRow,
  AncmSerialNoOption,
  AncmStoreOption,
  CorrectiveMaintenanceObj,
} from './add-new-corrective-maintenance.models';

/**
 * Endpoint paths taken directly from addNewCorrectiveMaintenanceController.js
 * (ctrl:82, 97, 165, 186, 205, 362) -- not guessed. `getAssetCategoryList`
 * and `getProductCategoryList` return raw arrays (`response.data` assigned
 * directly, ctrl:102/169), same envelope-less shape already seen for the
 * same endpoint family in implant-master.service.ts (assetTypeId 1 there,
 * 2 here). `searchNewItem` is likewise a raw array (ctrl:188), matching
 * pm-schedule.service.ts's `getAccessoryItems` call to the same
 * `/api/global/items/unit/{unitId}` endpoint.
 */
@Injectable({ providedIn: 'root' })
export class AddNewCorrectiveMaintenanceService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getStoreList(userId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, AncmStoreOption>> {
    return this.http.get<ApiResponse<never, AncmStoreOption>>(
      `${this.base}/api/global/getDetailedStoreList/${userId}/${organizationId}/${unitId}`,
    );
  }

  getAssetCategoryList(unitId: number, organizationId: number): Observable<AncmAssetCategoryOption[]> {
    return this.http.get<AncmAssetCategoryOption[]>(`${this.base}/api/global/assets/categories/2/${unitId}/${organizationId}`);
  }

  getProductCategoryList(assetCategoryId: number, organizationId: number): Observable<AncmProductCategoryOption[]> {
    return this.http.get<AncmProductCategoryOption[]>(`${this.base}/api/global/products/categories/${assetCategoryId}/${organizationId}`);
  }

  searchNewItem(
    unitId: number,
    payload: { assetTypeId: number; assetCategoryId?: number; productCategoryId?: number; storeId: number },
  ): Observable<AncmSearchItemRow[]> {
    return this.http.post<AncmSearchItemRow[]>(`${this.base}/api/global/items/unit/${unitId}`, payload);
  }

  getSerialNumbers(itemId: number, unitId: number, organizationId: number): Observable<ApiResponse<never, AncmSerialNoOption>> {
    return this.http.get<ApiResponse<never, AncmSerialNoOption>>(
      `${this.base}/api/assetManagement/maintenance/getSerialNoByItemId/${itemId}/${unitId}/${organizationId}`,
    );
  }

  save(payload: CorrectiveMaintenanceObj): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/saveCorrectiveMaintenance`, payload);
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AssetCategoryDetail, AssetCategoryRow, AssetCategorySavePayload, AssetTypeOption } from './asset-category-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/AssetManagenent/assetCategoryMasterController.js
 * and CONSTANTS.GLOBAL.ASSET_TYPE_API / CONSTANTS.MASTER.UNIT.FIXED_ASSET.ASSET_CATEGORIES_API
 * in resources/assets/js/constants/constants.js -- not guessed.
 */
@Injectable({ providedIn: 'root' })
export class AssetCategoryMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getAssetTypes(unitId: number, organizationId: number): Observable<AssetTypeOption[]> {
    return this.http.get<AssetTypeOption[]>(`${this.base}/api/global/assets/types/${unitId}/${organizationId}`);
  }

  getAssetCategories(organizationId: number): Observable<AssetCategoryRow[]> {
    return this.http.get<AssetCategoryRow[]>(`${this.base}/api/units/assets/categories/org/${organizationId}`);
  }

  getAssetCategory(id: number): Observable<AssetCategoryDetail> {
    return this.http.get<AssetCategoryDetail>(`${this.base}/api/units/assets/categories/${id}`);
  }

  create(payload: AssetCategorySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/units/assets/categories/`, payload);
  }

  update(id: number, payload: AssetCategorySavePayload): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/units/assets/categories/${id}`, payload);
  }

  toggleStatus(id: number, status: boolean, unitId: number, organizationId: number, userId: number): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/units/assets/categories/${id}`, {
      status,
      unitId,
      organizationId,
      userId,
    });
  }
}

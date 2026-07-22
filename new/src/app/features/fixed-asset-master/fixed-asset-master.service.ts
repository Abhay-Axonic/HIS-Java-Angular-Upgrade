import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AssetCategoryOption,
  AssetTypeOption,
  FixedAssetRow,
  FixedAssetSavePayload,
  ManufacturerOption,
  ProductCategoryOption,
} from './fixed-asset-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/fixedAssetMasterController.js
 * and CONSTANTS.MASTER.UNIT.FIXED_ASSET.FIXED_ASSET_MASTER /
 * CONSTANTS.GLOBAL.ASSET_TYPE_API / ASSET_CATEGORY_API / PRODUCT_CATEGORY_API /
 * MANUFACTURER_SEARCH_API in resources/assets/js/constants/constants.js.
 * Confirmed real: com.param.pharmacy.master.unit.controller.FixedAssetController
 * (`@RequestMapping("/api/unit/fixed/asset")`) -> FixedAssetService ->
 * FixedAssetDao, plus the shared com.param.global.controller.GlobalController
 * (`@RequestMapping("/api/global")`) dropdown endpoints.
 */
@Injectable({ providedIn: 'root' })
export class FixedAssetMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getAssetTypes(unitId: number, organizationId: number): Observable<AssetTypeOption[]> {
    return this.http.get<AssetTypeOption[]>(`${this.base}/api/global/assets/types/${unitId}/${organizationId}`);
  }

  getAssetCategories(assetTypeId: number, unitId: number, organizationId: number): Observable<AssetCategoryOption[]> {
    return this.http.get<AssetCategoryOption[]>(
      `${this.base}/api/global/assets/categories/${assetTypeId}/${unitId}/${organizationId}`,
    );
  }

  getProductCategories(assetCategoryId: number, organizationId: number): Observable<ProductCategoryOption[]> {
    return this.http.get<ProductCategoryOption[]>(
      `${this.base}/api/global/products/categories/${assetCategoryId}/${organizationId}`,
    );
  }

  searchManufacturer(organizationId: number, name: string): Observable<ManufacturerOption[]> {
    return this.http.get<ManufacturerOption[]>(`${this.base}/api/global/manufacturer/search/${organizationId}/${name}`);
  }

  // Trailing slash matches legacy's own CONSTANTS.MASTER.UNIT.FIXED_ASSET.FIXED_ASSET_MASTER
  // (constants.js:211, `unitApi + '/fixed/asset/'`) and this batch's golden-trace
  // capture (`GET /api/unit/fixed/asset/`) exactly.
  getAll(): Observable<FixedAssetRow[]> {
    return this.http.get<FixedAssetRow[]>(`${this.base}/api/unit/fixed/asset/`);
  }

  getById(id: number): Observable<FixedAssetRow> {
    return this.http.get<FixedAssetRow>(`${this.base}/api/unit/fixed/asset/${id}`);
  }

  create(payload: FixedAssetSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/unit/fixed/asset/`, payload);
  }

  update(id: number, payload: FixedAssetSavePayload): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/unit/fixed/asset/${id}`, payload);
  }

  toggleStatus(id: number, status: boolean, unitId: number, organizationId: number, userId: number): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/unit/fixed/asset/${id}`, {
      status,
      unitId,
      organizationId,
      userId,
    });
  }
}

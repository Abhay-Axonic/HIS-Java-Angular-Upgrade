import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AssetCategoryOption,
  ImplantMasterDetail,
  ImplantMasterRow,
  ImplantMasterSavePayload,
  ManufacturerResult,
  ProductCategoryOption,
  SpecialityOption,
} from './implant-master.models';

interface ImplantDropdownResponse {
  tax: { id: number; type: string }[];
  storageUnit: { id: number; unit: string }[];
}

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/implantMasterController.js
 * and CONSTANTS.MASTER.UNIT.IMPLANT_MASTER / CONSTANTS.GLOBAL in
 * resources/assets/js/constants/constants.js -- not guessed. Two payload
 * quirks preserved verbatim per this pilot batch's audit doc: the
 * `getActiveSpecialityList` call always sends organizationId/unitId as the
 * literal number 1 (a real bug in legacy, not something this rebuild gets
 * to silently correct), and the asset-category dropdown call hardcodes
 * assettypeId `1`.
 */
@Injectable({ providedIn: 'root' })
export class ImplantMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getAssetCategories(unitId: number, organizationId: number): Observable<AssetCategoryOption[]> {
    return this.http.get<AssetCategoryOption[]>(`${this.base}/api/global/assets/categories/1/${unitId}/${organizationId}`);
  }

  getActiveSpecialityList(): Observable<ApiResponse<never, SpecialityOption>> {
    return this.http.post<ApiResponse<never, SpecialityOption>>(`${this.base}/adt/getActiveSpecialityList`, {
      unitId: 1,
      organizationId: 1,
    });
  }

  getDropdowns(unitId: number, organizationId: number): Observable<ImplantDropdownResponse> {
    return this.http.get<ImplantDropdownResponse>(`${this.base}/api/globals/item/implantItemMaster/dropdown/${unitId}/${organizationId}`);
  }

  getImplantMasterList(unitId: number): Observable<ImplantMasterRow[]> {
    return this.http.get<ImplantMasterRow[]>(`${this.base}/api/globals/item/implantItemMaster/unitId/${unitId}`);
  }

  searchManufacturer(organizationId: number, search: string): Observable<ManufacturerResult[]> {
    return this.http.get<ManufacturerResult[]>(`${this.base}/api/global/manufacturer/search/${organizationId}/${search}`);
  }

  getProductCategories(assetCategoryId: number, organizationId: number): Observable<ProductCategoryOption[]> {
    return this.http.get<ProductCategoryOption[]>(`${this.base}/api/global/products/categories/${assetCategoryId}/${organizationId}`);
  }

  getImplant(id: number): Observable<ImplantMasterDetail> {
    return this.http.get<ImplantMasterDetail>(`${this.base}/api/globals/item/implantItemMaster/${id}`);
  }

  create(payload: ImplantMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/globals/item/implantItemMaster/save`, payload);
  }

  update(id: number, payload: ImplantMasterSavePayload): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/globals/item/implantItemMaster/${id}`, payload);
  }

  toggleStatus(id: number, status: boolean): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/globals/item/implantItemMaster/${id}`, { status });
  }
}

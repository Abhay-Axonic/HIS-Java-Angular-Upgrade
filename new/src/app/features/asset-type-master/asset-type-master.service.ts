import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AssetTypeDetail, AssetTypeRow, AssetTypeSavePayload } from './asset-type-master.models';

/**
 * Endpoint paths taken directly from the real Java backend, NOT from
 * legacy's own (broken -- see asset-type-master.ts class comment)
 * `assetTypeMasterController.js`, which references an undefined global
 * `constants.apiurl` and never reaches these calls in legacy today.
 * Confirmed real via com.param.pharmacy.master.unit.controller.AssetTypeController
 * (`@RequestMapping("/api/units/assets/types/")`) -> AssetTypeService ->
 * AssetTypeDao (Hibernate Criteria over the real `AssetType` entity).
 */
@Injectable({ providedIn: 'root' })
export class AssetTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getAll(): Observable<AssetTypeRow[]> {
    return this.http.get<AssetTypeRow[]>(`${this.base}/api/units/assets/types/`);
  }

  getById(id: number): Observable<AssetTypeDetail> {
    return this.http.get<AssetTypeDetail>(`${this.base}/api/units/assets/types/${id}`);
  }

  create(payload: AssetTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/units/assets/types/`, payload);
  }

  update(id: number, payload: Partial<AssetTypeSavePayload>): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/units/assets/types/${id}`, payload);
  }

  toggleStatus(id: number, status: boolean): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/units/assets/types/${id}`, { status });
  }
}

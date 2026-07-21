import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { CmrwRow, DetailsOfCM, ReasonListItem, StoreListItem } from './corrective-maintenance-return-worklist.models';

@Injectable({ providedIn: 'root' })
export class CorrectiveMaintenanceReturnWorklistService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** ctrl:89 -- /api/global/getDetailedStoreList/{userId}/{organizationId}/{unitId} (GET). */
  getDetailedStoreList(userId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, StoreListItem>> {
    return this.http.get<ApiResponse<never, StoreListItem>>(`${this.base}/api/global/getDetailedStoreList/${userId}/${organizationId}/${unitId}`);
  }

  /** ctrl:105 -- /api/common/getActiveReasonMasterList (POST). */
  getActiveReasonMasterList(payload: Record<string, unknown>): Observable<ApiResponse<never, ReasonListItem>> {
    return this.http.post<ApiResponse<never, ReasonListItem>>(`${this.base}/api/common/getActiveReasonMasterList`, payload);
  }

  /** ctrl:148 -- the search -- /api/assetManagement/maintenance/getreplaceDetailOnReturnByMultipleCrit (POST). */
  getReplaceDetailOnReturnByMultipleCrit(payload: Record<string, unknown>): Observable<ApiResponse<never, CmrwRow>> {
    return this.http.post<ApiResponse<never, CmrwRow>>(`${this.base}/api/assetManagement/maintenance/getreplaceDetailOnReturnByMultipleCrit`, payload);
  }

  /** ctrl:193 -- /api/assetManagement/maintenance/getdetailsOfCM (POST). */
  getDetailsOfCM(payload: Record<string, unknown>): Observable<ApiResponse<DetailsOfCM, never>> {
    return this.http.post<ApiResponse<DetailsOfCM, never>>(`${this.base}/api/assetManagement/maintenance/getdetailsOfCM`, payload);
  }

  /** ctrl:232 -- accept-save -- /api/assetManagement/maintenance/returnAsset (POST). */
  returnAsset(payload: Record<string, unknown>): Observable<ApiResponse<never, never>> {
    return this.http.post<ApiResponse<never, never>>(`${this.base}/api/assetManagement/maintenance/returnAsset`, payload);
  }

  /** ctrl:295 -- reject-save -- /api/assetManagement/maintenance/rejectAsset (POST). */
  rejectAsset(payload: Record<string, unknown>): Observable<ApiResponse<never, never>> {
    return this.http.post<ApiResponse<never, never>>(`${this.base}/api/assetManagement/maintenance/rejectAsset`, payload);
  }
}

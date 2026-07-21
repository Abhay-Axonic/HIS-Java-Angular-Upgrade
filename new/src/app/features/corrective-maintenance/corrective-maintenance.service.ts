import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { CmRow, CmStore } from './corrective-maintenance.models';

@Injectable({ providedIn: 'root' })
export class CorrectiveMaintenanceService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getStoreList(userId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, CmStore>> {
    return this.http.get<ApiResponse<never, CmStore>>(`${this.base}/api/global/getDetailedStoreList/${userId}/${organizationId}/${unitId}`);
  }

  search(payload: Record<string, unknown>): Observable<ApiResponse<never, CmRow>> {
    return this.http.post<ApiResponse<never, CmRow>>(`${this.base}/api/assetManagement/maintenance/getCorrectiveMaintenanceListByMultipleCriteria`, payload);
  }

  updatePendingToNewRequest(payload: Record<string, unknown>[]): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/updateCorrectiveMaintenancePendingToNewRequest`, payload);
  }

  cancelRequest(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/cancelCorrectiveMaintenanceRequest`, payload);
  }

  completeByRequester(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/CorrectiveMaintenanceRequestCompleteByRequester`, payload);
  }

  saveCorrectiveMaintenance(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/saveCorrectiveMaintenance`, payload);
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { CmswRow, CmswStore } from './corrective-maintenance-schedule-worklist.models';

@Injectable({ providedIn: 'root' })
export class CorrectiveMaintenanceScheduleWorklistService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getStoreList(userId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, CmswStore>> {
    return this.http.get<ApiResponse<never, CmswStore>>(`${this.base}/api/global/getDetailedStoreList/${userId}/${organizationId}/${unitId}`);
  }

  getCorrectiveMaintenanceWorklist(payload: Record<string, unknown>): Observable<ApiResponse<never, CmswRow>> {
    return this.http.post<ApiResponse<never, CmswRow>>(`${this.base}/api/assetManagement/maintenance/getCorrectiveMaintenanceWorklistListByMultipleCriteria`, payload);
  }

  getSchedulingList(payload: Record<string, unknown>): Observable<ApiResponse<never, CmswRow>> {
    return this.http.post<ApiResponse<never, CmswRow>>(`${this.base}/api/assetManagement/maintenance/getCorrectiveMaintenanceScheduleByMultipleCrit`, payload);
  }
}

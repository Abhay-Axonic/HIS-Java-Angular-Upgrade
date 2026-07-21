import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { CmwRow } from './corrective-maintenance-worklist.models';

@Injectable({ providedIn: 'root' })
export class CorrectiveMaintenanceWorklistService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getCorrectiveMaintenance(payload: Record<string, unknown>): Observable<ApiResponse<never, CmwRow>> {
    return this.http.post<ApiResponse<never, CmwRow>>(`${this.base}/api/assetManagement/maintenance/getCorrectiveMaintenanceListByMultipleCriteria`, payload);
  }

  getCorrectiveMaintenanceWorklist(payload: Record<string, unknown>): Observable<ApiResponse<never, CmwRow>> {
    return this.http.post<ApiResponse<never, CmwRow>>(`${this.base}/api/assetManagement/maintenance/getCorrectiveMaintenanceWorklistListByMultipleCriteria`, payload);
  }
}

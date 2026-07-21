import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { DcmPmsResponseObject, DcmSerialOption } from './details-corrective-maintenance.models';

@Injectable({ providedIn: 'root' })
export class DetailsCorrectiveMaintenanceService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getSerialNoByItemId(itemId: number | string, unitId: number, organizationId: number): Observable<ApiResponse<never, DcmSerialOption>> {
    return this.http.get<ApiResponse<never, DcmSerialOption>>(
      `${this.base}/api/assetManagement/maintenance/getSerialNoByItemId/${itemId}/${unitId}/${organizationId}`,
    );
  }

  getPmsDetails(payload: Record<string, unknown>): Observable<ApiResponse<DcmPmsResponseObject>> {
    return this.http.post<ApiResponse<DcmPmsResponseObject>>(`${this.base}/api/assetManagement/maintenance/getPMSDetailsByItemId`, payload);
  }

  saveCorrectiveMaintenance(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/saveCorrectiveMaintenance`, payload);
  }
}

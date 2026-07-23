import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { DcmwEmployee, DcmwSaveWorklistPayload } from './details-corrective-maintenance-worklist.models';

@Injectable({ providedIn: 'root' })
export class DetailsCorrectiveMaintenanceWorklistService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** "Assigned To" dropdown -- ctrl:94-119, same shared endpoint used by
   *  scheduleWorklist/pmSchedule, filtered by `assetMaintenanceSpecialityId`. */
  getEmployeeListBySpecialityAndSubSpeciality(
    organizationId: number,
    unitId: number,
    specialityId: number | undefined,
  ): Observable<ApiResponse<never, DcmwEmployee>> {
    return this.http.post<ApiResponse<never, DcmwEmployee>>(`${this.base}/api/global/getEmployeeListBySpecialityAndSubSpeciality`, {
      organizationId,
      unitId,
      specialityId,
    });
  }

  /** ctrl:123-192 -- allocation save/assign, always posts status 3 ("Assigned"). */
  saveWorklist(payload: DcmwSaveWorklistPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/saveCorrectiveMaintenance/worklist`, payload);
  }
}

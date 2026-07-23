import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { NursingStationOption, NursingStationWard, DepartmentNursingStationEntry } from './doctor-dashboard-info.models';

/**
 * The 2 real backend calls dashboardInfoMasterController.js makes,
 * confirmed live/real (see `docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md:1005`,
 * API column = 2 for `doctorDashboardInfo`). `authInterceptor` (already
 * global) attaches the Authorization/userId/unitId headers every call
 * needs.
 */
@Injectable({ providedIn: 'root' })
export class DoctorDashboardInfoService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** dashboardInfoMasterController.js:262 -- fired only when
   * `nurseRole == 1` AND `sessionStorage.nursingStationsWardsList` isn't
   * already cached. */
  getDetailedNursingStationList(
    userId: number,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, NursingStationOption>> {
    return this.http.get<ApiResponse<never, NursingStationOption>>(
      `${this.base}/api/global/getDetailedNursingStationList/${userId}/${organizationId}/${unitId}`,
    );
  }

  /** dashboardInfoMasterController.js:278 -- fired only when exactly one
   * nursing station comes back from the call above (the 2+ case shows a
   * `#nursingStationModal` that doesn't exist anywhere in this screen's
   * own view -- see this screen's `.ts` class doc). */
  getDeptListByNursingStationId(
    nursingStationId: number,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<NursingStationWard[], DepartmentNursingStationEntry>> {
    return this.http.get<ApiResponse<NursingStationWard[], DepartmentNursingStationEntry>>(
      `${this.base}/api/global/getDeptListByNursingStationId/${nursingStationId}/${organizationId}/${unitId}`,
    );
  }
}

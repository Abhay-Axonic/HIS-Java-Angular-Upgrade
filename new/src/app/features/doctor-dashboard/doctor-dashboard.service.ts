import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { NursingStationOption, NursingStationWard, DepartmentNursingStationEntry } from './doctor-dashboard.models';

/**
 * The 2 real backend endpoints `doctorDashboardController.js` itself calls
 * (confirmed both live during Step 0's backend check -- see the batch audit
 * notes). Same 2 endpoints `home` (nurseDashboardMasterController.js)
 * already consumes for the identical nursing-station-selection flow; not
 * shared as one service across screens because legacy itself doesn't share
 * this state globally (it's defined fresh per controller against `$scope`,
 * not `$rootScope` -- see home.ts's class doc for the same reasoning
 * applied there first).
 *
 * `authInterceptor` (already global) attaches the Authorization/userId/
 * unitId headers every call needs -- nothing to add here.
 */
@Injectable({ providedIn: 'root' })
export class DoctorDashboardService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** doctorDashboardController.js:100 -- fired on init when
   * `nursingStationsWardsList` isn't already cached in sessionStorage. */
  getDetailedNursingStationList(
    userId: number,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, NursingStationOption>> {
    return this.http.get<ApiResponse<never, NursingStationOption>>(
      `${this.base}/api/global/getDetailedNursingStationList/${userId}/${organizationId}/${unitId}`,
    );
  }

  /** doctorDashboardController.js:158 (the nursing-station select's
   * `ng-click="selectsetNursingStation()"` handler) AND the nested
   * auto-path variant at :116 (fired instead of showing the modal when
   * exactly one nursing station is returned) -- same endpoint, two call
   * sites, one method. */
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

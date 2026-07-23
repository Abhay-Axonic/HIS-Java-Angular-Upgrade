import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  NursingStationOption,
  NursingStationWard,
  DepartmentNursingStationEntry,
  MappedNurseBed,
} from './home.models';

/**
 * The 4 backend call sites reachable from `home.html`'s own bindings, out
 * of the 6 total in the shared `nurseDashboardMasterController.js` (the
 * other 2, `openTriageNurse`/`openTriageReceptionist` against a Supabase
 * edge function, are dead for `home` -- confirmed absent from home.html,
 * not implemented here). `authInterceptor` (already global) attaches the
 * Authorization/userId/unitId headers every call needs.
 */
@Injectable({ providedIn: 'root' })
export class HomeService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** nurseDashboardMasterController.js:124 -- unconditional on init when
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

  /** nurseDashboardMasterController.js:100 (the nursing-station select's
   * `ng-click="selectsetNursingStation()"` handler) AND the nested
   * auto-path variant at :140 (fired instead of showing the modal when
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

  /** nurseDashboardMasterController.js:172-181 -- fires unconditionally on
   * init. `home.html` has no UI consuming the response; kept as a real,
   * live call (matching legacy's actual network behavior) whose result is
   * only cached to sessionStorage for OTHER screens, not rendered here. */
  getBedListByNurseId(
    organizationId: number,
    unitId: number,
    nurseId: number,
    shiftDate: string,
  ): Observable<ApiResponse<never, MappedNurseBed>> {
    return this.http.post<ApiResponse<never, MappedNurseBed>>(`${this.base}/api/global/getbedlistbyNurseId`, {
      orgId: organizationId,
      unitId,
      nurseId,
      shiftDate,
    });
  }
}

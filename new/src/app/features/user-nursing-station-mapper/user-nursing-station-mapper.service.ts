import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { UserNursingStationMapperRow } from './user-nursing-station-mapper.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/master/unit/userNursingStationMapperController.js`
 * (GET_USER_NURSING_STATION_MAPPER_LIST =
 * "/api/global/getUserNursingStationMapperlist" from global.url.config.js:200)
 * — not guessed. NOTE the legacy payload uses the British spelling
 * organisationId. Confirmed live 2026-08-12: SUCCESS with 1 real row.
 */
@Injectable({ providedIn: 'root' })
export class UserNursingStationMapperService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl URI — POST /api/global/getUserNursingStationMapperlist.
  getList(organisationId: number, unitId: number, offset: number, size: number): Observable<ApiResponse<never, UserNursingStationMapperRow>> {
    return this.http.post<ApiResponse<never, UserNursingStationMapperRow>>(
      `${this.base}/api/global/getUserNursingStationMapperlist`,
      { organisationId, unitId, offset, noOfRecordsPerPage: size },
    );
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { NursingStationWardMappingRow } from './nursing-station-ward-mapping.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/nursingStationWardMappingController.js`
 * (GETNURSINGSTATIONWARDMAPPERLIST =
 * "/api/adt/unit/getNursingStationWardMapperList" from
 * adt.url.config.js:650) — not guessed. NOTE the legacy payload uses
 * recordPerPage, not noOfRecordsPerPage (ctrl:163-167; a 400 "Bad request:
 * Invalid parameters" is returned if the field name differs). Confirmed live
 * 2026-08-12: SUCCESS with 1 real row.
 */
@Injectable({ providedIn: 'root' })
export class NursingStationWardMappingService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:216 — POST /api/adt/unit/getNursingStationWardMapperList.
  getList(organizationId: number, unitId: number, offset: number, size: number): Observable<ApiResponse<never, NursingStationWardMappingRow>> {
    return this.http.post<ApiResponse<never, NursingStationWardMappingRow>>(
      `${this.base}/api/adt/unit/getNursingStationWardMapperList`,
      { organizationId, unitId, offset, recordPerPage: size },
    );
  }
}

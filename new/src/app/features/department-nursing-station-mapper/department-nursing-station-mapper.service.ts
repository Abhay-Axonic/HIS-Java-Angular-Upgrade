import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { DepartmentNursingStationMapperRow } from './department-nursing-station-mapper.models';

/**
 * Endpoint + payload taken directly from
 * `legacy/.../controller/master/global/departmentNursingStationMapperController.js`
 * (URI1 = "/api/global/getDepartmentNursingStationMapperlist", payload
 * {organisationId, unitId, noOfRecordsPerPage, offset}). Confirmed live
 * 2026-08-12 (DQ-032 close-out): SUCCESS, 2 real rows.
 */
@Injectable({ providedIn: 'root' })
export class DepartmentNursingStationMapperService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:getDeptNursingMapperList — POST
  // /api/global/getDepartmentNursingStationMapperlist
  // {organisationId, unitId, noOfRecordsPerPage, offset}.
  getList(organisationId: number, unitId: number, offset: number, size: number): Observable<ApiResponse<never, DepartmentNursingStationMapperRow>> {
    return this.http.post<ApiResponse<never, DepartmentNursingStationMapperRow>>(
      `${this.base}/api/global/getDepartmentNursingStationMapperlist`,
      { organisationId, unitId, noOfRecordsPerPage: size, offset },
    );
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { VitalCategoryOption, VitalQualifierRow } from './vital-qualifier-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/global/vitalQualifierMasterController.js
 * -- not guessed. All under `/api/global`. Confirmed live 2026-07-28: real
 * save/update/status-toggle/edit-fetch round trips, all HTTP 200.
 */
@Injectable({ providedIn: 'root' })
export class VitalQualifierMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:62-100.
  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
    unitId: number,
  ): Observable<ApiResponse<never, VitalQualifierRow>> {
    return this.http.post<ApiResponse<never, VitalQualifierRow>>(`${this.base}/api/global/getVitalQualifireMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
      unitId,
    });
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/global/getVitalQualifireMasterCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:335-349 -- no organizationId filter server-side, status='A' only.
  getCategoryList(organizationId: number): Observable<ApiResponse<never, VitalCategoryOption>> {
    return this.http.post<ApiResponse<never, VitalCategoryOption>>(`${this.base}/api/global/getVitalCategoryList`, {
      organizationId,
    });
  }

  // ctrl:149-207.
  save(payload: {
    vitalCategoryId: string;
    vitalQualifireCode: string;
    vitalQualifireDesc: string;
    organizationId: number;
    unitId: number;
    createdBy: number;
    updatedBy: number;
    status: 'A';
    createdDate: string;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveVitalQualifireMaster`, payload);
  }

  // ctrl:214-242 -- GET with id/organizationId as path segments.
  getById(id: number, organizationId: number): Observable<ApiResponse<never, VitalQualifierRow>> {
    return this.http.get<ApiResponse<never, VitalQualifierRow>>(
      `${this.base}/api/global/getVitalQualifireMasterById/${id}/${organizationId}`,
    );
  }

  // ctrl:245-295 -- no confirm-modal gate.
  update(payload: {
    vitalQualifireId: number;
    vitalCategoryId: string;
    vitalQualifireCode: string;
    vitalQualifireDesc: string;
    organizationId: number;
    unitId: number;
    updatedBy: number;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateVitalQualifireMaster`, payload);
  }

  // ctrl:297-330 -- backend only reads vitalQualifireId/status/updatedBy/
  // updatedDate off the payload (findById then mutates only those fields),
  // confirmed via direct DAO read -- harmless even though the legacy
  // frontend sends the whole (possibly stale) $scope.common object.
  updateStatus(payload: {
    vitalQualifireId: number;
    status: 'A' | 'I';
    updatedBy: number;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateVitalQualifireMasterStatus`, payload);
  }
}

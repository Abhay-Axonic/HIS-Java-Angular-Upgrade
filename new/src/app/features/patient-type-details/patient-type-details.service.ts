import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  PatientTypeDetailsSavePayload,
  PatientTypeRow,
  PatientTypeServiceDetail,
  PatientTypeServiceStatusPayload,
} from './patient-type-details.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/controller/billing/unit/patientTypeDetailsController.js`
 * (lines 931/1104/1157/1364) — not guessed. All confirmed live 2026-08-12:
 * GET /api/billing/getPatientType/1/1 -> SUCCESS 3 real rows. The detail
 * endpoints (getPatientTypeServiceWiseDetails GET with path vars,
 * savePatientTypeWiseDetails POST, updatePatientTypeServiceStatus POST) are
 * reproduced with the same methods as legacy.
 */
@Injectable({ providedIn: 'root' })
export class PatientTypeDetailsService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:931 — GET /api/billing/getPatientType/{org}/{unit}. Real data.
  getPatientTypes(organizationId: number, unitId: number): Observable<ApiResponse<never, PatientTypeRow>> {
    return this.http.get<ApiResponse<never, PatientTypeRow>>(
      `${this.base}/api/billing/getPatientType/${organizationId}/${unitId}`,
    );
  }

  // ctrl:1157 — GET /api/billing/getPatientTypeServiceWiseDetails/{id}/{org}/{unit}.
  getDetails(patientTypeId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, PatientTypeServiceDetail>> {
    return this.http.get<ApiResponse<never, PatientTypeServiceDetail>>(
      `${this.base}/api/billing/getPatientTypeServiceWiseDetails/${patientTypeId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:1104 — POST /api/billing/savePatientTypeWiseDetails.
  save(payload: PatientTypeDetailsSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/savePatientTypeWiseDetails`, payload);
  }

  // ctrl:1364 — POST /api/billing/updatePatientTypeServiceStatus.
  changeStatus(payload: PatientTypeServiceStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updatePatientTypeServiceStatus`, payload);
  }
}

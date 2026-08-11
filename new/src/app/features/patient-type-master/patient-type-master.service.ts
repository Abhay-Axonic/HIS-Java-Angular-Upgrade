import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  PatientTypeRow,
  PatientTypeSavePayload,
  PatientTypeStatusPayload,
  PatientTypeUpdatePayload,
} from './patient-type-master.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/controller/billing/unit/patientTypeController.js`
 * (lines 271/304/396/483) and `patientTypeDetailsController.js:931` — not
 * guessed. All confirmed live 2026-08-12 against http://localhost:8080 with
 * the dev token: GET /api/billing/getPatientType/1/1 -> SUCCESS 3 real rows.
 * NOTE the GET-with-path-variables shape (org/unit in the URL, no body) —
 * reproduced as GET, not "upgraded" to a POST.
 */
@Injectable({ providedIn: 'root' })
export class PatientTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:304 — GET /api/billing/getPatientType/{org}/{unit}. Real data.
  getList(organizationId: number, unitId: number): Observable<ApiResponse<never, PatientTypeRow>> {
    return this.http.get<ApiResponse<never, PatientTypeRow>>(
      `${this.base}/api/billing/getPatientType/${organizationId}/${unitId}`,
    );
  }

  // ctrl:271 — POST /api/billing/savePatientType.
  save(payload: PatientTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/savePatientType`, payload);
  }

  // ctrl:396 — POST /api/billing/updatePatientType.
  update(payload: PatientTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updatePatientType`, payload);
  }

  // ctrl:483 — POST /api/billing/updatePatientTypeStatus.
  changeStatus(payload: PatientTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updatePatientTypeStatus`, payload);
  }
}

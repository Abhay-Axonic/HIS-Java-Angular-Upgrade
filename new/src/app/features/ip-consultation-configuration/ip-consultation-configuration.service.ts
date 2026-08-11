import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  IpConsultationConfigListPayload,
  IpConsultationConfigRow,
} from './ip-consultation-configuration.models';

/**
 * Endpoint path taken directly from legacy
 * `resources/assets/js/controller/master/global/IPConsultationConfigurationMasterController.js`
 * line 89 (`URI = BASE_URL + ROOT_URL + API + GLOBAL +
 * GET_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_LIST`) and the URL constants in
 * `config/billing.url.config.js`: GLOBAL = "/global" (line 8),
 * GET_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_LIST =
 * "/getDoctorIPConsultationServiceMapperList" (line 68) — not guessed.
 * Request body copied from ctrl:93-98
 * ({organizationId, unitId, offset, noOfRecordsPerPage}).
 * Live 2026-08-12: POST returns SUCCESS with an empty listObject (honest
 * dev-empty — the @empty branch renders, no masked-error banner).
 */
@Injectable({ providedIn: 'root' })
export class IpConsultationConfigurationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:89 + billing.url.config.js:8/68 — POST /api/global/getDoctorIPConsultationServiceMapperList.
  getList(payload: IpConsultationConfigListPayload): Observable<ApiResponse<never, IpConsultationConfigRow>> {
    return this.http.post<ApiResponse<never, IpConsultationConfigRow>>(
      `${this.base}/api/global/getDoctorIPConsultationServiceMapperList`,
      payload,
    );
  }
}

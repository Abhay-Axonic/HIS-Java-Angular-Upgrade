import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import type { ApiResponse } from '../../core/api-response';
import type { EncounterPayload, IdNameRow, VitalRow } from './encounter-visit.models';

/** The encounter + vitals endpoints (corrected backend, class paths /api/opd). */
@Injectable({ providedIn: 'root' })
export class EncounterVisitService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  getVitalMasterList(): Observable<ApiResponse<never, IdNameRow>> {
    return this.http.get<ApiResponse<never, IdNameRow>>(`${this.base}/api/opd/getVitalMasterList`);
  }

  searchDiagnosis(keyword: string): Observable<ApiResponse<never, IdNameRow>> {
    return this.http.post<ApiResponse<never, IdNameRow>>(
      `${this.base}/api/opd/diagnosisAutoFillSearch`,
      { search: keyword },
    );
  }

  saveEncounter(payload: EncounterPayload): Observable<ApiResponse<{ encounterId?: number }, { encounterId?: number }>> {
    return this.http.post<ApiResponse<never, { encounterId?: number }>>(`${this.base}/api/opd/encounter`, payload);
  }

  saveVital(vital: VitalRow, unitId: number, orgId: number): Observable<ApiResponse<never, unknown>> {
    return this.http.post<ApiResponse<never, unknown>>(
      `${this.base}/api/opd/saveAppointmentVitalMapper/${unitId}/${orgId}`,
      vital,
    );
  }
}

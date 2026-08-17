import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import type { ApiResponse } from '../../core/api-response';
import type { ItemRow, PrescriptionPayload } from './prescription.models';

/** Prescription endpoints (corrected backend). NOTE the date convention:
 * PrescriptionDto/PrescriptionDetailsDto use STRING dates (dd-M-yyyy
 * HH:mm:ss) — the SAME as the registration DTO but the OPPOSITE of the
 * encounter DTO (Long epoch-millis). The caller formats with formatLegacyNow(),
 * never Date.now(). */
@Injectable({ providedIn: 'root' })
export class PrescriptionService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  searchItems(keyword: string, unitId: number, orgId: number): Observable<ApiResponse<never, ItemRow>> {
    return this.http.get<ApiResponse<never, ItemRow>>(
      `${this.base}/api/global/unit/itemMaster/${encodeURIComponent(keyword)}/unit/${unitId}/org/${orgId}`,
    );
  }

  save(payload: PrescriptionPayload): Observable<ApiResponse<never, unknown>> {
    return this.http.post<ApiResponse<never, unknown>>(`${this.base}/api/opd/saveMedication`, payload);
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { BloodGroupMatchingSavePayload, BloodGroupRow } from './blood-group-matching-master.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/controller/bloodBank/bloodGroupMatchingMasterController.js`
 * (lines 70/187/222) — not guessed. Confirmed live 2026-08-12:
 * GET /global/bloodBank/getActiveBloodGroupMaster/1/1 -> SUCCESS 3 real rows
 * (A+ / "A Positive (Updated)").
 */
@Injectable({ providedIn: 'root' })
export class BloodGroupMatchingMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:70 — GET /global/bloodBank/getActiveBloodGroupMaster/{org}/{unit}.
  // Real data (3 rows).
  getActiveBloodGroups(organizationId: number, unitId: number): Observable<ApiResponse<never, BloodGroupRow>> {
    return this.http.get<ApiResponse<never, BloodGroupRow>>(
      `${this.base}/global/bloodBank/getActiveBloodGroupMaster/${organizationId}/${unitId}`,
    );
  }

  // ctrl:187-189 — POST /api/bloodBank/saveBloodGroupMatchingMaster.
  save(payload: BloodGroupMatchingSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/bloodBank/saveBloodGroupMatchingMaster`, payload);
  }
}

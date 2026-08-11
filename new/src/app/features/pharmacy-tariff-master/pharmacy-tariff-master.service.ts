import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  PharmacyTariffRow,
  PharmacyTariffSavePayload,
  PharmacyTariffUpdatePayload,
} from './pharmacy-tariff-master.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/controller/billing/unit/pharmacyTariffMasterController.js`
 * (lines 55/99/141/295) — not guessed. Confirmed live 2026-08-12:
 * GET /api/units/pharmacy/tariff/getAll/1/1 -> SUCCESS 3 real rows.
 * Status toggle is GET /api/units/pharmacy/tariff/update/status/{id}/{status}
 * (path variables, not a body).
 */
@Injectable({ providedIn: 'root' })
export class PharmacyTariffMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:55 — GET /api/units/pharmacy/tariff/getAll/{org}/{unit}. Real data.
  getList(organizationId: number, unitId: number): Observable<ApiResponse<never, PharmacyTariffRow>> {
    return this.http.get<ApiResponse<never, PharmacyTariffRow>>(
      `${this.base}/api/units/pharmacy/tariff/getAll/${organizationId}/${unitId}`,
    );
  }

  // ctrl:295 — POST /api/units/pharmacy/tariff/save.
  save(payload: PharmacyTariffSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/units/pharmacy/tariff/save`, payload);
  }

  // ctrl:141 — POST /api/units/pharmacy/tariff/update.
  update(payload: PharmacyTariffUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/units/pharmacy/tariff/update`, payload);
  }

  // ctrl:99 — GET /api/units/pharmacy/tariff/update/status/{id}/{status}.
  changeStatus(id: number, status: 'A' | 'I', organizationId: number, unitId: number): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(
      `${this.base}/api/units/pharmacy/tariff/update/status/${id}/${status}?organizationId=${organizationId}&unitId=${unitId}`,
    );
  }
}

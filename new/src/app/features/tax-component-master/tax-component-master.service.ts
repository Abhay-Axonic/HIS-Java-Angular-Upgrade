import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { TaxComponentRecord } from './tax-component-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/pharmacy/taxComponentMasterController.js
 * and TaxComponentController.java (`/api/globals/tax/components/`, Pharmacy).
 * All under `/api/globals/tax/components/`. The orgId is a PATH variable
 * (the controller's GET/POST/PATCH mappings) -- the rebuild sends the
 * verified session org, matching the golden trace.
 *
 * NOTE (Step 0 finding, 2026-08-03): the legacy app.js state for this
 * screen is fully commented out (no live legacy route, no sidebar link).
 * The backend is real and live-verified (BR-158); this rebuild creates the
 * route fresh in Angular.
 */
@Injectable({ providedIn: 'root' })
export class TaxComponentMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(organizationId: number): Observable<TaxComponentRecord[]> {
    // NOTE: the backend returns a PLAIN JSON ARRAY for the list (not the
    // {status,listObject} envelope) -- verified live 2026-08-03.
    return this.http.get<TaxComponentRecord[]>(`${this.base}/api/globals/tax/components/${organizationId}`);
  }

  getById(organizationId: number, id: number): Observable<ApiResponse<TaxComponentRecord>> {
    return this.http.get<ApiResponse<TaxComponentRecord>>(
      `${this.base}/api/globals/tax/components/${organizationId}/${id}`,
    );
  }

  create(organizationId: number, payload: { code: string; component: string; status: boolean }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/globals/tax/components/${organizationId}`, payload);
  }

  update(organizationId: number, id: number, payload: {
    id: number; code: string; component: string; status: boolean;
  }): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/globals/tax/components/${organizationId}/${id}`, payload);
  }
}

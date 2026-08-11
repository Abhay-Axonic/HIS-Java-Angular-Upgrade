/**
 * charge-conversion.service — the feeds the rebuild can call without a
 * patient context. The visit-type list is the one feed that works
 * standalone; the conversion flow's other feeds are patient/encounter-gated.
 * The save endpoint is /unit/ChargeConversion/saveChargeConversion (verified
 * live: HTTP 200 with an honest error body for an empty payload — the
 * endpoint exists; the legacy contract is patient-driven).
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { inject } from '@angular/core';

export interface ApiEnvelope {
  status: string;
  code?: string;
  message?: string | null;
  listObject?: unknown[] | null;
  object?: unknown;
}

@Injectable({ providedIn: 'root' })
export class ChargeConversionService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  visitTypes(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/unit/CompanyContract/getVisitTypeList`, {
      orgId, unitId,
    });
  }
}

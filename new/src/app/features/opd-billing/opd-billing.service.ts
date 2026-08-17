import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import type { ApiResponse } from '../../core/api-response';
import type { BillPayload, BillResponseTotals } from './opd-billing.models';

/** OPD billing endpoints (corrected backend, class /api/billing). The bill's
 * dates are LONG epoch-millis (the encounter convention) — Date.now(). */
@Injectable({ providedIn: 'root' })
export class OpdBillingService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  saveBill(payload: BillPayload): Observable<ApiResponse<BillResponseTotals, never>> {
    return this.http.post<ApiResponse<BillResponseTotals, never>>(`${this.base}/api/billing/saveBill`, payload);
  }
}

/**
 * tpa-master — parity rebuild of the legacy
 *   views/master/UnitMaster/billingMaster/TPA-master.html
 *   (menu node 572 'TPA Master', parent 570 Billing -> 531 Unit Master,
 *   controller TPAMasterController loaded index.html).
 *
 * SCREEN 38 — LIVE class (the recheck's first confirmed LIVE — the 585
 * lesson: the endpoint truth with path params proved it; the recheck's
 * earlier 404 was its own path construction). The screen is a TPA
 * registration FORM surface: registration-field config + company list +
 * country/state/district + AR-code dropdowns.
 *
 * LIVE-VERIFIED BY CALLING (body status):
 * - GET /api/unit/applicationConfiguration/
 *   getUnitApplicationRegistrationFieldConfig/1/1 -> 200 success []
 *   (dev-empty);
 * - POST /api/global/getCompanyMasterList {organizationId, unitId,
 *   companyTypeId: 5} -> 200 success [] (dev-empty);
 * - GET /global/billing/getActiveARCodeMaster/1/1 -> 200 success with REAL
 *   rows (TESTAR2 / Test AR Description Two);
 * - GET /adt/getActiveCountryList/1 -> 200 success with REAL rows (India/IN
 *   — 'List Size DB: 2').
 * PAYLOAD LESSON: the legacy sends {organizationId, unitId} — the orgId key
 * 500s at Jackson deserialization (the screen-37 lesson, shared DTO).
 *
 * SCOPED BACKEND CHECK: the screen is a FORM surface (no master save in
 * this controller's read path); the registration-field config is the
 * backend-driven form-shape feed. No write-path false-success shape on the
 * read surface.
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
export class TpaMasterService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  companies(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(
      `${this.base}/api/global/getCompanyMasterList`,
      { organizationId: orgId, unitId, companyTypeId: 5 }
    );
  }

  arCodes(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(
      `${this.base}/global/billing/getActiveARCodeMaster/${orgId}/${unitId}`
    );
  }

  countries(orgId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(`${this.base}/adt/getActiveCountryList/${orgId}`);
  }
}

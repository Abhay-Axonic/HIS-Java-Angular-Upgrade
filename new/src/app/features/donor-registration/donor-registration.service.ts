import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { PrefixRow } from './donor-registration.models';

/**
 * Screen 719 feeds. Legacy state donorRegistration (app.js:18128-18132,
 * donorRegistrationController) is a registration form; the only live feed on
 * its path is the prefix master list: GETACTIVEPREFIXLIST =
 * "/adt/getActivePrefixList" (adt.url.config.js:239). Confirmed live
 * 2026-08-12: SUCCESS with 1 real row {prefixId, prefixDesc, prefixCode,
 * status} — REAL FEED (the registration form itself is write-path, deferred to
 * the transaction methodology per DQ-2026-08-12-034).
 */
@Injectable({ providedIn: 'root' })
export class DonorRegistrationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // GET /adt/getActivePrefixList — prefix master feed (no params).
  getPrefixes(): Observable<ApiResponse<never, PrefixRow>> {
    return this.http.get<ApiResponse<never, PrefixRow>>(
      `${this.base}/adt/getActivePrefixList`,
    );
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { GenderRow } from './all-inclusive-package.models';

/**
 * Screen 625 feeds. The legacy state (allInclusivePackage, app.js:10219-10221)
 * is a commented-out MasterController shell — no screen-specific controller.
 * The only live feed on this screen's path is the gender master list:
 * GETACTIVEGENDERLIST = "/adt/getActiveGenderList" (adt.url.config.js:170).
 * Confirmed live 2026-08-12: SUCCESS with 3 real rows — REAL FEED (the screen
 * itself is a write-path form, deferred to the transaction methodology per
 * DQ-2026-08-12-034).
 */
@Injectable({ providedIn: 'root' })
export class AllInclusivePackageService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // GET /adt/getActiveGenderList — gender master feed (no params).
  getGenders(): Observable<ApiResponse<never, GenderRow>> {
    return this.http.get<ApiResponse<never, GenderRow>>(
      `${this.base}/adt/getActiveGenderList`,
    );
  }
}

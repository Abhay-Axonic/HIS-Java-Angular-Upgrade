import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { DietTypeRow } from './diet-type-master.models';

/**
 * Endpoint path taken directly from
 * `legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller/dietAndKitchen/global/dietTypeMasterController.js`
 * (lines 119/155, URI =
 * "/api/kitchen/master/dietTypeMaster/getDietTypeMasterPaginationList") —
 * not guessed. POST body matches the legacy call
 * ({organizationId, unitId, offset, noOfRecordsPerPage}).
 * Confirmed live 2026-08-12: SUCCESS with 1 real row — real data.
 */
@Injectable({ providedIn: 'root' })
export class DietTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:119/155 (dietTypeMasterController.js) — POST
  // /api/kitchen/master/dietTypeMaster/getDietTypeMasterPaginationList
  // {organizationId, unitId, offset, noOfRecordsPerPage}. Real data (1 row).
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, DietTypeRow>> {
    return this.http.post<ApiResponse<never, DietTypeRow>>(
      `${this.base}/api/kitchen/master/dietTypeMaster/getDietTypeMasterPaginationList`,
      { organizationId, unitId, offset, noOfRecordsPerPage },
    );
  }
}

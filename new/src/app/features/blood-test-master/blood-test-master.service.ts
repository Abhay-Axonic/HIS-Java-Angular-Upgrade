import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { BloodTestRow } from './blood-test-master.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/config/bloodBank.url.config.js:40`
 * (GET_BLOOD_TEST_MASTER_LIST = "/global/bloodBank/getBloodTestMasterList")
 * — not guessed. POST body matches the legacy `masterList` call at
 * `controller/bloodBank/bloodTestMasterController.js:93-98`
 * (organizationId, offset, noOfRecordsPerPage, unitId). Confirmed live
 * 2026-08-12: SUCCESS with an empty listObject — honest dev-empty.
 */
@Injectable({ providedIn: 'root' })
export class BloodTestMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:88-102 (bloodTestMasterController.js), url.config.js:40 —
  // POST /global/bloodBank/getBloodTestMasterList. Dev-empty: SUCCESS + [].
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, BloodTestRow>> {
    return this.http.post<ApiResponse<never, BloodTestRow>>(
      `${this.base}/global/bloodBank/getBloodTestMasterList`,
      { organizationId, unitId, offset, noOfRecordsPerPage },
    );
  }
}

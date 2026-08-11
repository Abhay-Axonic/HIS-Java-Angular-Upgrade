import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ScreeningTestRow } from './screening-test-master.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/config/bloodBank.url.config.js:70`
 * (GET_SCREENING_TEST_MASTER_LIST = "/bloodBank/getScreeningTestMasterList")
 * — not guessed. POST body matches the legacy `masterList` call at
 * `controller/bloodBank/screeningTestMasterController.js:106-111`
 * (organizationId, offset, noOfRecordsPerPage, unitId). Confirmed live
 * 2026-08-12: SUCCESS with an empty listObject — honest dev-empty.
 */
@Injectable({ providedIn: 'root' })
export class ScreeningTestMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:100-115 (screeningTestMasterController.js), url.config.js:70 —
  // POST /bloodBank/getScreeningTestMasterList. Dev-empty: SUCCESS + [].
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, ScreeningTestRow>> {
    return this.http.post<ApiResponse<never, ScreeningTestRow>>(
      `${this.base}/bloodBank/getScreeningTestMasterList`,
      { organizationId, unitId, offset, noOfRecordsPerPage },
    );
  }
}

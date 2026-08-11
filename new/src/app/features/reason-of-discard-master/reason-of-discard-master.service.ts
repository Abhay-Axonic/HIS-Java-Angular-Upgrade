import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ReasonOfDiscardRow } from './reason-of-discard-master.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/config/bloodBank.url.config.js:60`
 * (GET_REASON_OF_DISCARD_MASTER_LIST = "/bloodBank/getReasonOfDiscardMasterList")
 * — not guessed. POST body matches the legacy `masterList` call at
 * `controller/bloodBank/reasonOfDiscardMasterController.js:93-98`
 * (organizationId, offset, noOfRecordsPerPage, unitId). Confirmed live
 * 2026-08-12: SUCCESS with an empty listObject — honest dev-empty.
 */
@Injectable({ providedIn: 'root' })
export class ReasonOfDiscardMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:87-107 (reasonOfDiscardMasterController.js), url.config.js:60 —
  // POST /bloodBank/getReasonOfDiscardMasterList. Dev-empty: SUCCESS + [].
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, ReasonOfDiscardRow>> {
    return this.http.post<ApiResponse<never, ReasonOfDiscardRow>>(
      `${this.base}/bloodBank/getReasonOfDiscardMasterList`,
      { organizationId, unitId, offset, noOfRecordsPerPage },
    );
  }
}

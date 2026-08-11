import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { DonorRejectionRow } from './donor-rejection-master.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/config/bloodBank.url.config.js:80`
 * (GET_DONOR_REJECTION_MASTER_LIST = "/bloodBank/getDonorRejectionMasterList")
 * — not guessed. POST body matches the legacy `masterList` call at
 * `controller/bloodBank/donorRejectionMasterController.js:93-98`
 * (organizationId, offset, noOfRecordsPerPage, unitId). Confirmed live
 * 2026-08-12: SUCCESS with an empty listObject — honest dev-empty.
 */
@Injectable({ providedIn: 'root' })
export class DonorRejectionMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:87-107 (donorRejectionMasterController.js), url.config.js:80 —
  // POST /bloodBank/getDonorRejectionMasterList. Dev-empty: SUCCESS + [].
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, DonorRejectionRow>> {
    return this.http.post<ApiResponse<never, DonorRejectionRow>>(
      `${this.base}/bloodBank/getDonorRejectionMasterList`,
      { organizationId, unitId, offset, noOfRecordsPerPage },
    );
  }
}

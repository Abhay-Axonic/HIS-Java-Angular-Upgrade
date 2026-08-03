import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  ReferralTypeDetail,
  ReferralTypeRow,
  ReferralTypeSavePayload,
  ReferralTypeStatusPayload,
  ReferralTypeUpdatePayload,
} from './referral-type-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/adt/global/referralTypeMasterController.js
 * and resources/assets/js/config/adt.url.config.js:206-210/384 -- not
 * guessed. All POST, all under `/adt`.
 *
 * Reached via the real, sidebar-linked `/commonMaster/referraltype` route
 * (`common-master-screen.html`) -- confirmed live 2026-07-28: real
 * save/update/status-toggle/edit-fetch round trips against real,
 * unmodified legacy. See referral-type-master.ts's class doc comment for
 * the full writeup.
 */
@Injectable({ providedIn: 'root' })
export class ReferralTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:65-97 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getReferralTypeList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, ReferralTypeRow>> {
    return this.http.post<ApiResponse<never, ReferralTypeRow>>(`${this.base}/adt/getReferralTypeMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:65-113 -- POST { organizationId }, count returned in `object`.
  getReferralTypeCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getReferralTypeCount`, {
      organizationId,
    });
  }

  // ctrl:177-237 -- confirmed live 2026-07-28, real save, real persistence.
  saveReferralType(payload: ReferralTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveReferralTypeMaster`, payload);
  }

  // ctrl:240-264 -- POST { id }; response.data.listObject[0] is the record.
  getReferralTypeById(id: number): Observable<ApiResponse<never, ReferralTypeDetail>> {
    return this.http.post<ApiResponse<never, ReferralTypeDetail>>(`${this.base}/adt/getReferralTypeById`, { id });
  }

  // ctrl:314-366 -- confirmed live 2026-07-28, real update, real persistence.
  // No confirm-modal gate (matches patient-source-master/occupation-master's
  // own save-only asymmetry).
  updateReferralType(payload: ReferralTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateReferralTypeMaster`, payload);
  }

  // ctrl:275-312 -- confirmed live 2026-07-28, real status toggle.
  updateStatusForReferralType(payload: ReferralTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForReferralType`, payload);
  }
}

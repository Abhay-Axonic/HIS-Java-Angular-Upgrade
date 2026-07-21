import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { DaiaDetail, DaiaRejectReason, DaiaStatus } from './details-asset-issue-acceptance.models';

/**
 * Endpoint paths taken directly from
 * detailsAssetIssueAcceptanceController.js + constants.js's
 * ASEET_ISSUE_ACCEPTANCE block (constants.js:1290-1295 -- "ASEET" is a
 * legacy typo in the *constant name* only, it doesn't affect the URL)
 * plus GLOBAL.STATUS_API / GLOBAL.REJECT_REASON_API (constants.js:62,71),
 * consumed via StatusService/rejectReasonService (resources/assets/js/service/).
 *
 * None of these four responses are wrapped in a {status, listObject}
 * envelope -- the legacy controller reads `response.data` directly as
 * the payload in every case (ctrl:19-24, 62). Matches GRN's precedent
 * for the same two GLOBAL endpoints (see grn.service.ts's header
 * comment) rather than Organization Master's ApiResponse-wrapped ones.
 */
@Injectable({ providedIn: 'root' })
export class DetailsAssetIssueAcceptanceService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDetail(unitId: number, organizationId: number, id: number | string): Observable<DaiaDetail> {
    return this.http.get<DaiaDetail>(`${this.base}/api/asset/issue/acceptance/${unitId}/${organizationId}/${id}`);
  }

  saveApproval(id: number | string, payload: Record<string, unknown>): Observable<{ message?: string }> {
    return this.http.patch<{ message?: string }>(`${this.base}/api/asset/issue/acceptance/${id}`, payload);
  }

  getStatuses(): Observable<DaiaStatus[]> {
    return this.http.get<DaiaStatus[]>(`${this.base}/api/global/status`);
  }

  getRejectReasons(unitId: number): Observable<DaiaRejectReason[]> {
    return this.http.get<DaiaRejectReason[]>(`${this.base}/api/global/reject/reasons/${unitId}`);
  }
}

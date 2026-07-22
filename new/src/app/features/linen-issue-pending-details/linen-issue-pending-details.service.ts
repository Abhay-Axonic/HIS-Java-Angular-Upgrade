import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  LinenPendingDetailResponse,
  LinenPendingSaveResponse,
  LinenPendingStatus,
} from './linen-issue-pending-details.models';

/**
 * Endpoints confirmed real end-to-end (Controller -> Service -> DAO):
 * `legacy/LinenLaundry/.../linenIssue/controller/LinenIssueController.java`
 * -> `LinenIssueService.java` -> `LinenIssueDao.java`. Both endpoints
 * return their DTO directly (`ResponseEntity<Object>` wrapping the raw
 * object), NOT the app's usual `{status, message, listObject}` envelope --
 * same pattern already established by the sibling `linenIssueAcceptanceDetails`
 * rebuild (`linen-issue-acceptance-details.service.ts`).
 */
@Injectable({ providedIn: 'root' })
export class LinenIssuePendingDetailsService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDetail(unitId: number, organizationId: number, id: number | string): Observable<LinenPendingDetailResponse> {
    return this.http.get<LinenPendingDetailResponse>(
      `${this.base}/api/linenLaundry/linen/issue/${unitId}/${organizationId}/${id}`,
    );
  }

  save(payload: Record<string, unknown>): Observable<LinenPendingSaveResponse> {
    return this.http.patch<LinenPendingSaveResponse>(`${this.base}/api/linenLaundry/linen/issue`, payload);
  }

  /** Shared endpoint, already backend-confirmed by
   * `details-asset-issue-acceptance.service.ts` (`GlobalController.java:151`). */
  getStatuses(): Observable<LinenPendingStatus[]> {
    return this.http.get<LinenPendingStatus[]>(`${this.base}/api/global/status`);
  }
}

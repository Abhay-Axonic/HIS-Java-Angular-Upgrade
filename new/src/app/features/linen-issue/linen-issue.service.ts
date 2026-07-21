import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { LinenIssueDropdowns, LinenIssueRow } from './linen-issue.models';

/**
 * Endpoint paths taken directly from constants.js's
 * CONSTANTS.LINEN_LAUNDRY.LINEN_ISSUE.{LINEN_PENDING_ISSUE,LINEN_ISSUE_LIST}
 * blocks (`linenApi = rootUrl + '/api/linenLaundry'`), and confirmed
 * against the real Java backend chain (Controller -> Service -> DAO, all
 * present): LinenIssueController.java (`/api/linenLaundry/linen/issue`,
 * dropdowns/search/PATCH-save/GET-by-id) and LinenIssueListController.java
 * (`/api/linenLaundry/linen/issue/list`, dropdowns/search/direct-issue/
 * GET-by-id). Only the 3 calls linenIssueController.js actually makes are
 * wired here (Tab 1's dropdown + search, Tab 2's dropdown) -- the
 * SAVE/DETAIL endpoints on both belong to the separate
 * linenIssuePendingDetails/linenIssueListDetails/linenIssueDirect screens,
 * out of scope for this batch.
 */
@Injectable({ providedIn: 'root' })
export class LinenIssueService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getPendingDropdowns(unitId: number, userId: number): Observable<LinenIssueDropdowns> {
    return this.http.get<LinenIssueDropdowns>(
      `${this.base}/api/linenLaundry/linen/issue/dropdowns/${unitId}/${userId}`,
    );
  }

  searchPending(payload: Record<string, unknown>): Observable<LinenIssueRow[]> {
    return this.http.post<LinenIssueRow[]>(`${this.base}/api/linenLaundry/linen/issue/search`, payload);
  }
}

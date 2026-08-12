import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { DiscountRightsRow } from './discount-rights.models';

/**
 * 745 Discount Rights ("Financial Rights") master list.
 *
 * POST /billing/getDiscountRightsMasterList — resolved from the legacy
 * constant GET_DISCOUNT_RIGHTS_MASTER_LIST = BILLING + "/getDiscountRightsMasterList"
 * (config/billing.url.config.js; BILLING = "/billing", BASE_URL/ROOT_URL are
 * both ""), called by discountRightsMasterController.js
 * (billing/global/discountRightsMasterController.js:174) and served by the
 * Java DiscountRightsMasterController (@RequestMapping("/billing"),
 * legacy/Billing/.../global/controller/DiscountRightsMasterController.java:58).
 * NOTE: this deployment mounts billing controllers WITHOUT an /api prefix —
 * /api/billing/getDiscountRightsMasterList is a 404; the correct path is
 * plain /billing/... (live-verified 2026-08-12).
 *
 * Request body: {organisationId, offset, noOfRecordsPerPage, unitId}.
 * Paging companion: GET /billing/getDiscountRightsMasterCount
 * (GET_DISCOUNT_RIGHTS_MASTER_COUNT constant, same file) — returns the total
 * in `object` (live: 2).
 */
@Injectable({ providedIn: 'root' })
export class DiscountRightsService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:initMasterList — POST /billing/getDiscountRightsMasterList
  getList(orgId: number, unitId: number, offset: number, size: number): Observable<ApiResponse<never, DiscountRightsRow>> {
    return this.http.post<ApiResponse<never, DiscountRightsRow>>(
      `${this.base}/billing/getDiscountRightsMasterList`,
      { organisationId: orgId, offset, noOfRecordsPerPage: size, unitId },
    );
  }
}

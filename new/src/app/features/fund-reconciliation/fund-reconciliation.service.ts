import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { FundInflowListPayload, FundInflowRow } from './fund-reconciliation.models';

/**
 * Endpoint path taken directly from legacy
 * `resources/assets/js/controller/billing/global/fundInflowController.js`
 * line 421 (`URI = BASE_URL + ROOT_URL +
 * "/api/billing/getAllfundInflowDetailsByTrust"`) and the Java mapping
 * FundInFlowMasterController.java:109-110
 * (`@RequestMapping(value = "/getAllfundInflowDetailsByTrust",
 * method = POST)`) — not guessed. Request body copied from ctrl:416-420
 * ({trustId, orgId, unitId}).
 * Live 2026-08-12: POST returns SUCCESS with an empty listObject (honest
 * dev-empty — the @empty branch renders, no masked-error banner).
 * Row shape = the named query's aliases (FundInFlowMaster.java:33-42,
 * GET_ACTIVE_ALL_FUND_INFLOWDETAILS_BY_TRUST_ID, aliasToBean onto
 * FundInFlowDetailsDto).
 */
@Injectable({ providedIn: 'root' })
export class FundReconciliationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:421 + FundInFlowMasterController.java:109 — POST /api/billing/getAllfundInflowDetailsByTrust.
  getList(payload: FundInflowListPayload): Observable<ApiResponse<never, FundInflowRow>> {
    return this.http.post<ApiResponse<never, FundInflowRow>>(
      `${this.base}/api/billing/getAllfundInflowDetailsByTrust`,
      payload,
    );
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AccountPayableRow } from './account-payable.models';

/**
 * Endpoint + payload taken directly from
 * `legacy/.../controller/billing/unit/accountPayableMasterController.js`
 * (getAccountPay: POST /api/global/getAccountPayable with
 * {orgId, orgUnitId} — NOT organizationId/unitId). Confirmed live
 * 2026-08-12 (DQ-030): SUCCESS, 5 real rows.
 */
@Injectable({ providedIn: 'root' })
export class AccountPayableService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:getAccountPay — POST /api/global/getAccountPayable {orgId, orgUnitId}.
  getList(orgId: number, orgUnitId: number): Observable<ApiResponse<never, AccountPayableRow>> {
    return this.http.post<ApiResponse<never, AccountPayableRow>>(
      `${this.base}/api/global/getAccountPayable`,
      { orgId, orgUnitId },
    );
  }

  // ctrl:getActiveAccountPay — POST /api/global/getActiveAccountPayable
  // {orgId, orgUnitId} (the 582 status view uses the same active list).
  getActiveList(orgId: number, orgUnitId: number): Observable<ApiResponse<never, AccountPayableRow>> {
    return this.http.post<ApiResponse<never, AccountPayableRow>>(
      `${this.base}/api/global/getActiveAccountPayable`,
      { orgId, orgUnitId },
    );
  }
}

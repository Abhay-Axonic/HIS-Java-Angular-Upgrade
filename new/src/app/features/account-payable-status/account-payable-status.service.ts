import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AccountPayableRow } from '../account-payable/account-payable.models';

/**
 * The 582 status view reuses the SAME entity + active-list endpoint as 580:
 * legacy billing/unit/accountPayableMasterController.js getActiveAccountPay
 * (POST /api/global/getActiveAccountPayable, payload {orgId, orgUnitId}).
 * Verified live 2026-08-12 (DQ-030): SUCCESS, 5 real rows.
 */
@Injectable({ providedIn: 'root' })
export class AccountPayableStatusService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:getActiveAccountPay — POST /api/global/getActiveAccountPayable
  // {orgId, orgUnitId}.
  getList(orgId: number, orgUnitId: number): Observable<ApiResponse<never, AccountPayableRow>> {
    return this.http.post<ApiResponse<never, AccountPayableRow>>(
      `${this.base}/api/global/getActiveAccountPayable`,
      { orgId, orgUnitId },
    );
  }
}

/**
 * account-payable-master — parity rebuild of the legacy
 *   views/master/UnitMaster/billingMaster/account-payable-master.html
 *   (menu node 579 'Account Payable Master', parent 570 Billing -> 531 Unit
 *   Master, controller accountPayableMasterController loaded index.html:886).
 *
 * SCREEN 26 — LIVE class (the first Billing-subtree candidate, C7 + C8 at
 * selection). Backend AccountPayableMasterController (/api/global/*):
 * saveAccountPayable, getAccountPayable, updateAccountPayableStatus,
 * getActiveAccountPayable. Live-verified by CALLING (body status, not HTTP
 * code): getAccountPayable + getActiveAccountPayable both 200 success with
 * REAL rows (accPayableMstId 6, AP-LIST-TEST — dev data exists). Shares the
 * controller family with 580 (Account Payable) — the contract extraction
 * serves both.
 *
 * SCOPED BACKEND CHECK (the billing false-success focus, BR-188 family):
 * PASS — the DAO save's catch RETHROWS (AccountPayableMasterDaoImpl.java:126),
 * no SUCCESS-from-catch, no swallow; SUCCESS returns only after the real
 * save/update (lines 113-119). Dup-check org+unit-scoped (CHECK_FOR_UNIQUE_
 * CODE, both params). List single-table (no INNER-JOIN drop). The org-trust
 * fix is LIVE (verifiedIdentityService binds the JWT-verified tenant — the
 * good class, commented at DAO:82-86). Date contract: DD-MM-YYYY HH:mm:ss
 * (legacy JS:308, GlobalCommonDateUtils "dd-M-yyyy HH:ss:mm").
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { inject } from '@angular/core';

export interface AccountPayableRow {
  accPayableMstId: number;
  accCode: string;
  accPayableCode: string;
  accDesc: string;
  accAddress: string;
  accContactNumber: string;
  accEmail: string;
  accMobile: string;
  accDay: number;
  accPaymentTerms: string;
  accCreditLimit: number;
  status: string;
}

export interface ApiEnvelope {
  status: string;
  code?: string;
  message?: string | null;
  listObject?: unknown[] | null;
  object?: unknown;
}

@Injectable({ providedIn: 'root' })
export class AccountPayableMasterService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number): Observable<ApiEnvelope> {
    // the legacy payload: {orgId, orgUnitId} (controller JS:102-103) — NOT
    // {organizationId, unitId}; the latter 400s HIS-400 (probe-payload class)
    return this.http.post<ApiEnvelope>(`${this.base}/api/global/getAccountPayable`, {
      orgId, orgUnitId: unitId,
    });
  }

  save(payload: Record<string, unknown>): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/api/global/saveAccountPayable`, payload);
  }
}

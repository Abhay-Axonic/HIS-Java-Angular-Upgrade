/**
 * deposit-matrix-configuration — parity rebuild of the legacy
 *   views/master/UnitMaster/billingMaster/deposit-matrix-config.html
 *   (menu node 621 'Deposit Matrix Configuration', parent 570 Billing ->
 *   531 Unit Master, controller depositMatrixConfigController loaded
 *   index.html:873).
 *
 * SCREEN 35 — LIVE class. State app.js:15172 `depositMatrixConfig` (the
 * node's link target — C5's DEAD flag is the name mismatch, the screen-31
 * pattern). Controller binds depositMatrixConfigController (NOT
 * MasterController — passes the DECOY test). Backend found BY CLASS NAME:
 * DepositMatrixConfigController (billing/unit, /api/billing/unit): list GET
 * depositMatrix/{orgId}/{unitId}/{offset}/{recordPerPage}, count GET,
 * save POST, byId GET, update PUT, status GET.
 *
 * LIVE-VERIFIED BY CALLING (body status): the list -> 200 success with REAL
 * dev rows (mDepositMatrixId 19, patientType 'General', paymentEntitlement
 * 'Self Entitlement', billingCategory...); the count -> 200 success object:
 * 5 — FIVE real dev rows. The grid renders REAL data (the first Billing
 * screen with populated dev data).
 *
 * SCOPED BACKEND CHECK (billing write-path): FALSE-SUCCESS ABSENT — the
 * save (DepositMatrixConfigDaoImpl:62-90) catches BOTH RETHROW
 * ServiceException; SUCCESS only after the real save. THE GOOD CLASSES
 * (hardened, live-proven): the org-trust fix binds JWT-verified tenant
 * (lines 72-74 — the BR-batch2 fix); the createdDate/updatedDate guards
 * null-check before getDate (line 77 — no getDate(null) NPE class); the
 * BY_ID read is org/unit-scoped (lines 100-101 — the batch-4 fix; a forged
 * victim ID cannot read cross-tenant config); the update path rejects
 * cross-tenant updates explicitly (line 128 'Cross-tenant update
 * rejected').
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { inject } from '@angular/core';

export interface DepositMatrixRow {
  mDepositMatrixId: number;
  patientType?: string;
  patientEntitleMent?: string;
  billingCategory?: string;
  againstDeposit?: number;
  surgical?: number;
  nonSurgical?: number;
  status?: string;
}

export interface ApiEnvelope {
  status: string;
  code?: string;
  message?: string | null;
  listObject?: unknown[] | null;
  object?: unknown;
}

@Injectable({ providedIn: 'root' })
export class DepositMatrixConfigurationService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(
      `${this.base}/api/billing/unit/depositMatrix/${orgId}/${unitId}/0/100`
    );
  }

  count(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(
      `${this.base}/api/billing/unit/depositMatrix/count/${orgId}/${unitId}`
    );
  }
}

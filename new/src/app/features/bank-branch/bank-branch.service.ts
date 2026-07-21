import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  BankBranchDetail,
  BankBranchRow,
  BankBranchSavePayload,
  BankBranchStatusPayload,
  BankBranchUpdatePayload,
} from './bank-branch.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/global/billing/bankBranchController.js
 * (`bankBranchController`, no `controllerAs`). State `bankBranch`
 * (app.js:8109-8135), moduleId 455, confirmed live. All under `/api/billing/*`
 * (resources/assets/js/config/global.url.config.js:69-74) -- unlike
 * employee-category's sibling-in-the-same-folder screen, which has NO `/api`
 * prefix. Every call is POST except the by-id lookup (GET), matching
 * GenericService.serviceAction's verb at each legacy call site. This is the
 * screen that feeds bank-master's Branch dropdown -- confirmed by reading
 * bankMasterController.js's `getActiveBranches` call, which hits
 * `GETACTIVEBANKBRANCHMASTERLIST` ("/api/billing/getActiveBankBranchMasterList"),
 * the "active-only" sibling of this screen's own list endpoint. An empty
 * Branch dropdown on bank-master (docs/ANGULAR_PENDING_FINDINGS.md, batch 7)
 * is therefore very likely a pure seed-data gap -- no bank branch rows exist
 * yet -- not a code defect in either screen.
 */
@Injectable({ providedIn: 'root' })
export class BankBranchService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:78-134 (GETBANKBRANCHMASTERLIST) -- POST { orgnisationId, offset, noOfRecordsPerPage, unitId }.
  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
    unitId: number,
  ): Observable<ApiResponse<never, BankBranchRow>> {
    return this.http.post<ApiResponse<never, BankBranchRow>>(`${this.base}/api/billing/getBankBranchMasterList`, {
      orgnisationId: organizationId,
      offset,
      noOfRecordsPerPage,
      unitId,
    });
  }

  // ctrl:78-134 (GETBANKBRANCHMASTERCOUNT) -- POST { orgnisationId, unitId }, count returned in `object`.
  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/billing/getBankBranchMasterCount`, {
      orgnisationId: organizationId,
      unitId,
    });
  }

  // ctrl:197-263 (SAVEBANKBRANCHMASTER) -- POST, real create.
  save(payload: BankBranchSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/saveBankBranchMaster`, payload);
  }

  // ctrl:266-291 (GETBANKBRANCHMASTERBYID) -- GET, branchId then
  // orgnisationId as path segments; response.data.listObject[0] is the record.
  getById(branchId: number, organizationId: number): Observable<ApiResponse<never, BankBranchDetail>> {
    return this.http.get<ApiResponse<never, BankBranchDetail>>(
      `${this.base}/api/billing/getBankBranchMasterById/${branchId}/${organizationId}`,
    );
  }

  // ctrl:334-385 (UPDATEBANKBRANCHMASTER) -- POST, real update.
  update(payload: BankBranchUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updateBankBranchMaster`, payload);
  }

  // ctrl:294-331 (UPDATEBANKBRANCHMASTERSTATUS) -- POST, real status toggle.
  updateStatus(payload: BankBranchStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updateBankBranchMasterStatus`, payload);
  }
}

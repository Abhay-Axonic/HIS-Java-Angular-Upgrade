import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { BankDetail, BankRow, BankSavePayload, BankStatusPayload, BankUpdatePayload, BranchOption } from './bank-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/billing/global/bankMasterController.js --
 * all under `/api/billing/*` (GETACTIVEBANKBRANCHMASTERLIST/SAVEBANKMASTER/
 * etc., resources/assets/js/config/global.url.config.js:75-83), not `/adt`
 * like most other Global Master screens -- see this component's class doc
 * comment on why (this screen is Billing-tagged, not a plain Global Master,
 * despite its Phase 0 tag/folder placement). Every call is POST except the
 * by-id lookup (GET), matching GenericService.serviceAction's verb at each
 * legacy call site.
 */
@Injectable({ providedIn: 'root' })
export class BankMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:57-70 -- POST { orgnisationId }. Field name kept verbatim (legacy's
  // own spelling), it's the real request contract.
  getActiveBranches(organizationId: number): Observable<ApiResponse<never, BranchOption>> {
    return this.http.post<ApiResponse<never, BranchOption>>(`${this.base}/api/billing/getActiveBankBranchMasterList`, {
      orgnisationId: organizationId,
    });
  }

  // ctrl:84-120 (data1) -- POST { orgnisationId, offset, noOfRecordsPerPage }.
  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, BankRow>> {
    return this.http.post<ApiResponse<never, BankRow>>(`${this.base}/api/billing/getBankMasterList`, {
      orgnisationId: organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:84-120 (data2) -- POST { orgnisationId }, count returned in `object`.
  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/billing/getBankMasterCount`, {
      orgnisationId: organizationId,
    });
  }

  // ctrl:124-142 (PriorityMasterList) -- POST, same endpoint/shape as
  // getList() above. Legacy stores this response into `$scope.commonList`,
  // a scope variable the view NEVER reads (the table is bound to
  // `bankList`, only ever set by initPaymentMasterList/getList above) --
  // see this component's class doc comment. Kept as its own method (not
  // merged into getList()) to make that faithfully-reproduced dead-write
  // traceable at the call site.
  getPageForPager(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, BankRow>> {
    return this.http.post<ApiResponse<never, BankRow>>(`${this.base}/api/billing/getBankMasterList`, {
      orgnisationId: organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:168-234 (SAVEBANKMASTER) -- POST, real create.
  save(payload: BankSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/saveBankMaster`, payload);
  }

  // ctrl:236-266 (GETBANKMASTERBYID) -- GET, bankId then organizationId as
  // path segments; response.data.listObject[0] is the record.
  getById(bankId: number, organizationId: number): Observable<ApiResponse<never, BankDetail>> {
    return this.http.get<ApiResponse<never, BankDetail>>(
      `${this.base}/api/billing/getBankMasterById/${bankId}/${organizationId}`,
    );
  }

  // ctrl:269-323 (UPDATEBANKMASTER) -- POST, real update.
  update(payload: BankUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updateBankMaster`, payload);
  }

  // ctrl:325-358 (UPDATEBANKMASTERSTATUS) -- POST, real status toggle.
  updateStatus(payload: BankStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updateBankMasterStatus`, payload);
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { PaymentModeRow, PaymentModeStatusPayload } from './payment-mode-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/billing/global/paymentModeMasterController.js
 * and resources/assets/js/config/billing.url.config.js -- not guessed.
 * Golden-traced live 2026-08-04 (batch-3): list `POST /api/billing/
 * getPaymentModeMasterList {organizationId, offset, noOfRecordsPerPage}`,
 * count `POST /api/billing/getPaymentModeMasterCount {organizationId}`,
 * save `POST /api/billing/savePaymentModeMaster`, edit-get `GET
 * /api/billing/getPaymentModeMasterById/{payModeId}/{orgId}`, update `POST
 * /api/billing/updatePaymentModeMaster`, toggle `POST /api/billing/
 * updatePaymentModeMasterStatus`, active-list `POST /api/billing/
 * getActivePaymentModeMasterList` (sibling endpoint).
 *
 * IMPORTANT wire quirk (golden-traced): the backend controller binds
 * `PaymentEntitlementMasterDto` for ALL paymentMode endpoints, so the
 * save/update payloads use the DTO's field names
 * (`paymentEntitlementCode`/`paymentEntitlementDesc`/`paymentEntitlementId`)
 * -- this is what the legacy controller actually sends, verified in
 * paymentModeMasterController.js.
 */
@Injectable({ providedIn: 'root' })
export class PaymentModeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl data1 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getPaymentModeList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, PaymentModeRow>> {
    return this.http.post<ApiResponse<never, PaymentModeRow>>(`${this.base}/api/billing/getPaymentModeMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl data2 -- POST { organizationId }, count in `object`.
  getPaymentModeCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/billing/getPaymentModeMasterCount`, {
      organizationId,
    });
  }

  // ctrl visitTypeObj -- POST { paymentEntitlementCode,
  // paymentEntitlementDesc, createdBy, updatedBy, createdDate, status:'A',
  // organizationId, updateDate }.
  savePaymentMode(payload: {
    paymentEntitlementCode: string;
    paymentEntitlementDesc: string;
    status: 'A';
    createdBy: number;
    updatedBy: number;
    organizationId: number;
    createdDate: string;
    updateDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/savePaymentModeMaster`, payload);
  }

  // ctrl GETPAYMENTMODEMASTERBYID -- GET
  // /api/billing/getPaymentModeMasterById/{payModeId}/{orgId}; the response
  // listObject[0] is loaded into the form.
  getPaymentModeById(id: number, orgId: number): Observable<ApiResponse<never, PaymentModeRow>> {
    return this.http.get<ApiResponse<never, PaymentModeRow>>(
      `${this.base}/api/billing/getPaymentModeMasterById/${id}/${orgId}`,
    );
  }

  // ctrl visitTypebj -- POST { paymentEntitlementCode,
  // paymentEntitlementDesc, paymentEntitlementId, organizationId,
  // updatedBy, updateDate }.
  updatePaymentMode(payload: {
    paymentEntitlementCode: string;
    paymentEntitlementDesc: string;
    paymentEntitlementId: number;
    organizationId: number;
    updatedBy: number;
    updateDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updatePaymentModeMaster`, payload);
  }

  // ctrl visitObj -- POST { paymentEntitlementId, status, updateDate,
  // updatedBy }.
  updatePaymentModeStatus(payload: PaymentModeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updatePaymentModeMasterStatus`, payload);
  }

  // sibling endpoint (BR-160): active-list for dropdowns.
  getActivePaymentModeList(organizationId: number): Observable<ApiResponse<never, PaymentModeRow>> {
    return this.http.post<ApiResponse<never, PaymentModeRow>>(
      `${this.base}/api/billing/getActivePaymentModeMasterList`,
      { organizationId },
    );
  }
}

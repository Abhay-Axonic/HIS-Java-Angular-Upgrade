import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { PaymentEntitlementRow, PaymentEntitlementStatusPayload } from './payment-entitlement-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/billing/global/paymentEntitmentMasterController.js
 * and resources/assets/js/config/billing.url.config.js -- not guessed.
 * Golden-traced live 2026-08-04 (batch-3): list `POST /global/
 * getPaymententitlementList {organizationId, offset, noOfRecordsPerPage}`,
 * count `POST /global/getPaymententitlementCount {organizationId}`, save
 * `POST /global/savePaymententitlement`, edit-get `GET /global/
 * getPaymententitlementById/orgId/{org}/paymentEntId/{id}`, update `POST
 * /global/updatePaymententitlement`, toggle `POST /global/
 * updatePaymententitlementStatus`. NOTE: `/global` prefix only (no `/api`)
 * -- BASE_URL/ROOT_URL both resolve to "" in the live app.
 */
@Injectable({ providedIn: 'root' })
export class PaymentEntitlementMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl data1 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getPaymentEntitlementList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, PaymentEntitlementRow>> {
    return this.http.post<ApiResponse<never, PaymentEntitlementRow>>(
      `${this.base}/global/getPaymententitlementList`,
      { organizationId, offset, noOfRecordsPerPage },
    );
  }

  // ctrl data2 -- POST { organizationId }, count in `object`.
  getPaymentEntitlementCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/global/getPaymententitlementCount`, {
      organizationId,
    });
  }

  // ctrl visitTypeObj -- POST { paymentEntitlementCode,
  // paymentEntitlementDesc, createdBy, updatedBy, createdDate, status:'A',
  // organizationId, updateDate } (legacy-exact: the DAO reads getUpdateDate()).
  savePaymentEntitlement(payload: {
    paymentEntitlementCode: string;
    paymentEntitlementDesc: string;
    status: 'A';
    createdBy: number;
    updatedBy: number;
    organizationId: number;
    createdDate: string;
    updateDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/savePaymententitlement`, payload);
  }

  // ctrl GETPAYMENTENTITLEMENTBYID -- GET
  // /global/getPaymententitlementById/orgId/{orgId}/paymentEntId/{id}; the
  // response listObject[0] is loaded into the form.
  getPaymentEntitlementById(id: number, orgId: number): Observable<ApiResponse<never, PaymentEntitlementRow>> {
    return this.http.get<ApiResponse<never, PaymentEntitlementRow>>(
      `${this.base}/global/getPaymententitlementById/orgId/${orgId}/paymentEntId/${id}`,
    );
  }

  // ctrl visitTypebj -- POST { paymentEntitlementCode,
  // paymentEntitlementDesc, paymentEntitlementId, organizationId,
  // updatedBy, updateDate }.
  updatePaymentEntitlement(payload: {
    paymentEntitlementCode: string;
    paymentEntitlementDesc: string;
    paymentEntitlementId: number;
    organizationId: number;
    updatedBy: number;
    updateDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/updatePaymententitlement`, payload);
  }

  // ctrl visitObj -- POST { paymentEntitlementId, status, updateDate,
  // updatedBy }.
  updatePaymentEntitlementStatus(payload: PaymentEntitlementStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/updatePaymententitlementStatus`, payload);
  }
}

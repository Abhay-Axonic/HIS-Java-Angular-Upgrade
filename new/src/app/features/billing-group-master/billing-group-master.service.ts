import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { BillingGroupMasterRecord, SpecialityMaster } from './billing-group-master.models';

/**
 * Endpoint paths and payload shapes taken directly from
 * resources/assets/js/controller/billing/global/BillingGroupMasterController.js
 * and resources/assets/js/config/billing.url.config.js /
 * resources/assets/js/config/adt.url.config.js -- not guessed. See
 * docs/PHASE2_PILOT_AUDIT.md section 8 for the golden-trace confirmation
 * of these exact calls.
 *
 * `orgnisationId` (not `organisationId`) is the legacy backend's actual
 * field name, kept verbatim -- it's a typo in the original API contract,
 * not something this rebuild gets to silently correct.
 */
@Injectable({ providedIn: 'root' })
export class BillingGroupMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getActiveSpecialityList(organizationId: number, unitId: number): Observable<ApiResponse<never, SpecialityMaster>> {
    return this.http.post<ApiResponse<never, SpecialityMaster>>(
      `${this.base}/adt/getActiveSpecialityList`,
      { organizationId, unitId },
    );
  }

  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    recordPerPage: number,
  ): Observable<ApiResponse<never, BillingGroupMasterRecord>> {
    return this.http.post<ApiResponse<never, BillingGroupMasterRecord>>(
      `${this.base}/billing/getBillingGroupMasterList`,
      { orgnisationId: organizationId, unitId, offset, recordPerPage },
    );
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/billing/getBillingGroupMasterCount`, {
      orgnisationId: organizationId,
      unitId,
    });
  }

  getById(
    organizationId: number,
    unitId: number,
    billingGroupId: number,
  ): Observable<ApiResponse<never, BillingGroupMasterRecord>> {
    return this.http.post<ApiResponse<never, BillingGroupMasterRecord>>(
      `${this.base}/billing/getBillingGroupMasterById`,
      { orgnisationId: organizationId, unitId, billingGroupId },
    );
  }

  create(record: BillingGroupMasterRecord): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/createBillingGroupMaster`, record);
  }

  update(record: BillingGroupMasterRecord): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/billing/updateBillingGroupMaster`, record);
  }

  changeStatus(
    billingGroupId: number,
    status: 'A' | 'I',
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/billing/changeBillingGroupMasterStatus`, {
      billingGroupId,
      status,
      orgnisationId: organizationId,
      unitId,
    });
  }
}

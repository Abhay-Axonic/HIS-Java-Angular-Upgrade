import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { CouponTemplateRecord, CouponTemplateDetail, CouponTemplateSavePayload } from './coupon-template-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/billing/unit/couponTemplateMasterController.js
 * (list/status/details) and couponTemplateController.js (save with the
 * couponTemplateDetailsDtoList builder). All POST under
 * `/api/billing/coupon/`. Verified live (BR-157 golden trace + after-fix
 * matrix): list, details-by-id, save, updateStatus.
 */
@Injectable({ providedIn: 'root' })
export class CouponTemplateMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(organizationId: number, unitId: number): Observable<ApiResponse<never, CouponTemplateRecord>> {
    return this.http.post<ApiResponse<never, CouponTemplateRecord>>(
      `${this.base}/api/billing/coupon/getCouponTemplateMaster`,
      { organizationId, unitId },
    );
  }

  getDetails(couponTemplateMasterId: number): Observable<ApiResponse<{ couponTemplateDetailsDtoList: CouponTemplateDetail[] }>> {
    return this.http.post<ApiResponse<{ couponTemplateDetailsDtoList: CouponTemplateDetail[] }>>(
      `${this.base}/api/billing/coupon/getCouponTemplateDetails`,
      { couponTemplateMasterId },
    );
  }

  save(payload: CouponTemplateSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/coupon/saveCouponTemplateMaster`, payload);
  }

  updateStatus(couponTemplateMasterId: number, status: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/coupon/updateStatus`, { couponTemplateMasterId, status });
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { DiscountCategoryDetail, DiscountCategoryRow } from './discount-category-master.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/controller/billing/unit/discountCategoryMasterController.js`
 * (all 6 build `BASE_URL + ROOT_URL + "/api/global/unit/..."` literals
 * in-line, no shared url-config constants file for this screen). All 6
 * confirmed against a real Java implementation, end to end:
 * `DiscountTypeMasterController.java` (`@RequestMapping("/api/global/unit")`)
 * -> `DiscountTypeMasterServiceImpl` -> `DiscountTypeMasterDaoImpl` ->
 * table `public.m_discount_category`. (A same-package
 * `DiscountCategoryMasterServiceImpl.java` also exists but is an empty,
 * unused stub class with zero methods -- confirmed dead, not part of this
 * chain; the real service class is `DiscountTypeMasterServiceImpl`.)
 *
 * getById is a POST here (unlike several sibling masters that use a GET
 * with path params) -- `getDiscountCategoryById` takes a plain
 * `@RequestBody` with only `discountTypeId` set (ctrl:246-249).
 */
@Injectable({ providedIn: 'root' })
export class DiscountCategoryMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:76-120 (initDiscountTypeMasterList) -- POST {unitId,
  // organizationId, offset, noOfRecordsPerPage}.
  getList(
    unitId: number,
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, DiscountCategoryRow>> {
    return this.http.post<ApiResponse<never, DiscountCategoryRow>>(`${this.base}/api/global/unit/getDiscountCategoryList`, {
      unitId,
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:83-96 (same function, the parallel count call) -- POST {unitId,
  // organizationId}; count returned in `object`.
  getCount(unitId: number, organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/global/unit/getDiscountCategoryCount`, {
      unitId,
      organizationId,
    });
  }

  // ctrl:182-234 (save) -- POST, real create. The backend's own
  // duplicate check (getDiscountCategoryExistOrNotByName) is scoped by
  // organizationId+unitId+lower(code)+lower(type) -- see this screen's
  // class doc comment for a confirmed backend defect: that check queries
  // the WRONG table (`m_discount_type` via the `DiscountTypeMaster`
  // entity), not `m_discount_category` (the table this same save()
  // actually writes to, and getDiscountCategoryList/ById actually read
  // from). Reproduced as-is -- this is a legacy backend bug, not something
  // the frontend can compensate for.
  save(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/saveDiscountCategory`, payload);
  }

  // ctrl:241-266 (showUpdateBtn) -- a POST with only {discountTypeId} in
  // the body, NOT a GET with path params (unlike incidentCategoryMaster's
  // getById).
  getById(discountTypeId: number): Observable<ApiResponse<never, DiscountCategoryDetail>> {
    return this.http.post<ApiResponse<never, DiscountCategoryDetail>>(`${this.base}/api/global/unit/getDiscountCategoryById`, {
      discountTypeId,
    });
  }

  // ctrl:309-361 (update) -- POST, real update. Same wrong-table duplicate
  // check defect applies here too (getDiscountCategoryExistOrNotByNameNotById).
  update(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/updateDiscountCategory`, payload);
  }

  // ctrl:269-306 (updateStatus) -- POST, real status toggle.
  updateStatus(payload: {
    discountTypeId: number;
    isActive: boolean;
    updatedBy: number;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/updateDiscountCategoryStatus`, payload);
  }
}

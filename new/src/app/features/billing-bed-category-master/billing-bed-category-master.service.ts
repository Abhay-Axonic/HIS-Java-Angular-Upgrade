import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  BillingBedCategoryDetail,
  BillingBedCategoryRow,
  BillingBedCategorySavePayload,
  BillingBedCategoryStatusPayload,
  BillingBedCategoryUpdatePayload,
  HierarchyOption,
} from './billing-bed-category-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/adt/unit/billingBedCategoryMasterUnitController.js
 * + resources/assets/js/config/adt.url.config.js:608-615/275, independently
 * confirmed against the live backend
 * (legacy/ADT/.../BillingBedCategoryMasterController.java, `@RequestMapping("/adt")`).
 */
@Injectable({ providedIn: 'root' })
export class BillingBedCategoryMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:94-107 (init) -- GETACTIVEHIERARCHYLIST.
  getActiveHierarchyList(): Observable<ApiResponse<never, HierarchyOption>> {
    return this.http.get<ApiResponse<never, HierarchyOption>>(`${this.base}/adt/getActiveHierarchyList`);
  }

  // ctrl:119-174 (initBillingBedList) -- GETBILLINGBEDCATEGORYLIST.
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, BillingBedCategoryRow>> {
    return this.http.post<ApiResponse<never, BillingBedCategoryRow>>(`${this.base}/adt/getBillingBedCategoryList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:119-174 (initBillingBedList) -- GETBILLINGBEDCATEGORYMASTERCOUNT.
  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getBillingBedCategoryMasterCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:236-334 (saveBillingBed) -- SAVEBILLINGBEDCATEGORYMASTER.
  save(payload: BillingBedCategorySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveBillingBedCategoryMaster`, payload);
  }

  // BillingBedCategoryMasterController.java:78-87 -- used by showUpdateBtn
  // (ctrl:362-393) to fetch the single row being edited.
  getById(billingBedId: number, orgId: number, unitId: number): Observable<ApiResponse<never, BillingBedCategoryDetail>> {
    return this.http.get<ApiResponse<never, BillingBedCategoryDetail>>(
      `${this.base}/adt/getBillingBedCategoryById/${billingBedId}/${orgId}/${unitId}`,
    );
  }

  // ctrl:436-536 (updateBillingBed) -- UPDATEBILLINGBEDCATEGORYMASTER.
  update(payload: BillingBedCategoryUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateBillingBedCategoryMaster`, payload);
  }

  // ctrl:396-433 (updateStatus) -- UPDATEBILLINGBEDCATEGORYMASTERSTATUS.
  updateStatus(payload: BillingBedCategoryStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateBillingBedCategoryMasterStatus`, payload);
  }
}

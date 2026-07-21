import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  BedCategoryDetail,
  BedCategoryRow,
  BedCategorySavePayload,
  BedCategoryStatusPayload,
  BillingBedCategoryOption,
  ConsultationServiceOption,
  HierarchyOption,
  OccupancyUnitOption,
  SetHierarchyPayload,
} from './bed-category-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/adt/global/bedCategoryMasterController.js
 * and resources/assets/js/config/adt.url.config.js -- not guessed, and
 * independently confirmed against the live backend controllers
 * (legacy/Global/.../BedCategoryMasterController.java,
 * legacy/ADT/.../HierarchyMasterController.java,
 * legacy/ADT/.../OccupancyUnitMasterController.java,
 * legacy/ADT/.../BillingBedCategoryMasterController.java). No `/api/...`
 * modernized route exists for this screen (unlike asset-category-master /
 * implant-master) -- these old `/adt/...` paths and verbs are the real,
 * only backend contract.
 */
@Injectable({ providedIn: 'root' })
export class BedCategoryMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getActiveHierarchyList(): Observable<ApiResponse<never, HierarchyOption>> {
    return this.http.get<ApiResponse<never, HierarchyOption>>(`${this.base}/adt/getActiveHierarchyList`);
  }

  getActiveOccupancyUnitList(organizationId: number): Observable<ApiResponse<never, OccupancyUnitOption>> {
    return this.http.post<ApiResponse<never, OccupancyUnitOption>>(`${this.base}/adt/getActiveOccupancyUnitList`, {
      organizationId,
    });
  }

  getActiveBillingBedCategoryList(organizationId: number, unitId: number): Observable<ApiResponse<never, BillingBedCategoryOption>> {
    return this.http.get<ApiResponse<never, BillingBedCategoryOption>>(
      `${this.base}/adt/getActiveBillingBedCategoryList/orgId/${organizationId}/unitId/${unitId}`,
    );
  }

  // Auto-rendered consultation services for the "Services" dropdown
  // (ctrl:127-134) -- not part of adt.url.config.js, hardcoded inline in
  // legacy as `/billing/service/autoRendered/{organizationId}/{unitId}`.
  getAutoRenderedServices(organizationId: number, unitId: number): Observable<ApiResponse<never, ConsultationServiceOption>> {
    return this.http.get<ApiResponse<never, ConsultationServiceOption>>(
      `${this.base}/billing/service/autoRendered/${organizationId}/${unitId}`,
    );
  }

  getBedCategoryList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, BedCategoryRow>> {
    return this.http.post<ApiResponse<never, BedCategoryRow>>(`${this.base}/adt/getBedCategoryMasterList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getBedCategoryCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getBedCategoryCount`, {
      unitId,
      organizationId,
    });
  }

  getBedCategoryById(bedCategoryId: number): Observable<ApiResponse<never, BedCategoryDetail>> {
    return this.http.post<ApiResponse<never, BedCategoryDetail>>(`${this.base}/adt/getBedCategoryById`, {
      bedCategoryId,
    });
  }

  saveBedCategory(payload: BedCategorySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveBedCategoryMaster`, payload);
  }

  updateBedCategory(payload: BedCategorySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateBedCategoryMaster`, payload);
  }

  updateStatus(payload: BedCategoryStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForBedCategory`, payload);
  }

  setHierarchy(payload: SetHierarchyPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateBedCategoryMaster/updateHierarchy`, payload);
  }
}

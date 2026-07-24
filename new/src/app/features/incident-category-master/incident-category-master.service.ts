import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  IncidentCategoryDetail,
  IncidentCategoryRow,
  IncidentCategorySavePayload,
  IncidentCategoryStatusPayload,
  IncidentCategoryUpdatePayload,
} from './incident-category-master.models';

/**
 * Endpoint paths per the completed Step 0/1 audit for this batch -- all
 * seven confirmed real, live Controller -> Service -> DAO chains under
 * `IncidentCategoryMasterController`'s class-level
 * `@RequestMapping("/api/global/unit")`
 * (legacy/Global/.../controller/IncidentCategoryMasterController.java:18) --
 * WITH the `/api` segment, unlike several sibling ADT-module screens in
 * prior batches that use a bare `/adt`/`/global`-style base with no `/api`.
 * Taken from
 * resources/assets/js/controller/master/incidentCategoryMasterController.js
 * (365 lines, `incidentCategoryMasterController`, no `controllerAs`).
 * `getActiveIncidentCategoryMaster` (GET
 * `/api/global/unit/getActiveIncidentCategoryMaster/{orgId}/{unitId}`) is
 * NOT wired here -- grepped the whole controller file, this screen itself
 * never calls it (dead from this screen's own perspective; presumably used
 * by another screen's category dropdown, e.g. the incident-reporting form).
 */
@Injectable({ providedIn: 'root' })
export class IncidentCategoryMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:59-98 (initMasterList) -- POST {organizationId, offset,
  // noOfRecordsPerPage, unitId}.
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, IncidentCategoryRow>> {
    return this.http.post<ApiResponse<never, IncidentCategoryRow>>(`${this.base}/api/global/unit/getIncidentCategoryMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
      unitId,
    });
  }

  // ctrl:72-96 (same function, the parallel count call) -- POST
  // {organizationId, unitId}; count returned in `object`.
  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/global/unit/getIncidentCategoryMasterCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:147-223 (save) -- POST, real create. The backend's own
  // duplicate-description check (getIncidentCategoryByName,
  // GET_INCIDENT_CATEGORY_MASTER_BY_NAME) is unit-scoped only (no
  // organizationId in the WHERE clause) -- reproduced as-is, not
  // client-side pre-checked.
  save(payload: IncidentCategorySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/saveIncidentCategoryMaster`, payload);
  }

  // ctrl:230-258 (showUpdateBtn) -- a genuine GET with path params, unlike
  // several sibling masters in this codebase that POST their single-record
  // fetch. Path order confirmed against the real
  // `@RequestMapping("/getIncidentCategoryMasterById/{incidentCategoryId}/{organizationId}/{unitId}")`.
  getById(incidentCategoryId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, IncidentCategoryDetail>> {
    return this.http.get<ApiResponse<never, IncidentCategoryDetail>>(
      `${this.base}/api/global/unit/getIncidentCategoryMasterById/${incidentCategoryId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:261-310 (update) -- POST, real update. Backend's duplicate-name
  // check here (getIncidentCategoryMasterByNameNotId) IS scoped by both
  // incidentCategoryId (excluding self) and unitId.
  update(payload: IncidentCategoryUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/updateIncidentCategoryMaster`, payload);
  }

  // ctrl:312-348 (updateStatus) -- POST, real status toggle. See
  // IncidentCategoryStatusPayload's doc comment re: the minimal 4-field
  // payload sent here vs. legacy's noisier whole-`$scope.common` POST.
  updateStatus(payload: IncidentCategoryStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/updateIncidentCategoryMasterStatus`, payload);
  }
}

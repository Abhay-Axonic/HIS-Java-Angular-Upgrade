import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  EmployeeCategoryDetail,
  EmployeeCategoryRow,
  EmployeeCategorySavePayload,
  EmployeeCategoryStatusPayload,
  EmployeeCategoryUpdatePayload,
} from './employee-category.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/global/billing/employeeCategoryController.js
 * (`employeeCategoryController`, no `controllerAs`). State `employeeCategory`
 * (app.js:8082-8108), moduleId 454, confirmed live. BASE_URL/ROOT_URL are
 * both `""` (resources/assets/js/config/url.config.js), so these are the
 * literal relative paths legacy calls -- all under `/billing/*` (no `/api`
 * prefix, resources/assets/js/config/global.url.config.js:44-50), unlike
 * bank-branch/bank-master's `/api/billing/*`. Every call is POST except the
 * by-id lookup (GET), matching GenericService.serviceAction's verb at each
 * legacy call site.
 */
@Injectable({ providedIn: 'root' })
export class EmployeeCategoryService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:70-119 (GETEMPLOYEECATEGORYMASTERLIST) -- POST { organizationId, offset, noOfRecordsPerPage }.
  getList(
    organizationId: number | undefined,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, EmployeeCategoryRow>> {
    return this.http.post<ApiResponse<never, EmployeeCategoryRow>>(`${this.base}/billing/getEmployeeCategoryMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:70-119 (GETEMPLOYEECATEGORYCOUNT) -- POST { organizationId }, count returned in `object`.
  getCount(organizationId: number | undefined): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/billing/getEmployeeCategoryCount`, {
      organizationId,
    });
  }

  // ctrl:181-236 (SAVEEMPLOYEECATEGORYMASTER) -- POST, real create.
  save(payload: EmployeeCategorySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/saveEmployeeCategoryMaster`, payload);
  }

  // ctrl:240-264 (GETEMPLOYEECATEGORYMASTERBYID) -- GET, id then organizationId
  // as path segments (organizationId is `undefined` here in practice -- see
  // class + models.ts doc comments -- producing a literal ".../<id>/undefined"
  // URL, faithfully reproduced, not corrected).
  getById(id: number, organizationId: number | undefined): Observable<ApiResponse<never, EmployeeCategoryDetail>> {
    return this.http.get<ApiResponse<never, EmployeeCategoryDetail>>(
      `${this.base}/billing/getEmployeeCategoryMasterById/${id}/${organizationId}`,
    );
  }

  // ctrl:309-356 (UPDATEEMPLOYEECATEGORYMASTER) -- POST, real update.
  update(payload: EmployeeCategoryUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/updateEmployeeCategoryMaster`, payload);
  }

  // ctrl:267-306 (UPDATEEMPLOYEECATEGORYMASTERSTATUS) -- POST, real status toggle.
  updateStatus(payload: EmployeeCategoryStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/updateEmployeeCategoryMasterStatus`, payload);
  }
}

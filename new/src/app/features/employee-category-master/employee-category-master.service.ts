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
} from './employee-category-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/global/common/employeeCategoryMasterController.js
 * and their literal string values in
 * resources/assets/js/config/global.url.config.js:209-215:
 *   SAVE_EMPLOYEE_CATEGORY_MASTER         = /billing/saveEmployeeCategoryMaster        (ctrl:170)
 *   GET_EMPLOYEE_CATEGORY_MASTER_BY_ID    = /billing/getEmployeeCategoryMasterById     (ctrl:217, "/"+id+"/"+organizationId)
 *   UPDATE_EMPLOYEE_CATEGORY_MASTER       = /billing/updateEmployeeCategoryMaster       (ctrl:259)
 *   UPDATE_EMPLOYEE_CATEGORY_MASTER_STATUS = /billing/updateEmployeeCategoryMasterStatus (ctrl:300)
 *   GET_EMPLOYEE_CATEGORY_MASTER_COUNT    = /billing/getEmployeeCategoryCount            (ctrl:75)
 * plus the inline literal `/billing/getAllEmployeeCategoryMasterList` (ctrl:74/110).
 * `BASE_URL`/`ROOT_URL` (url.config.js) are both `''` in this deployment, so
 * these are the complete paths. List/count/save/update/status calls are all
 * POST in legacy (`GenericService.serviceAction("POST", ...)`); only the
 * by-id lookup is a GET with the id/organizationId as path segments.
 *
 * ORG-scoped only -- confirmed by reading the controller in full: it never
 * builds or sends a `unitId` field on any of these calls, unlike several
 * sibling "master" screens in this batch.
 */
@Injectable({ providedIn: 'root' })
export class EmployeeCategoryMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, EmployeeCategoryRow>> {
    return this.http.post<ApiResponse<never, EmployeeCategoryRow>>(
      `${this.base}/billing/getAllEmployeeCategoryMasterList`,
      { organizationId, offset, noOfRecordsPerPage },
    );
  }

  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/billing/getEmployeeCategoryCount`, {
      organizationId,
    });
  }

  save(payload: EmployeeCategorySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/saveEmployeeCategoryMaster`, payload);
  }

  getById(id: number, organizationId: number): Observable<ApiResponse<never, EmployeeCategoryDetail>> {
    return this.http.get<ApiResponse<never, EmployeeCategoryDetail>>(
      `${this.base}/billing/getEmployeeCategoryMasterById/${id}/${organizationId}`,
    );
  }

  update(payload: EmployeeCategoryUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/updateEmployeeCategoryMaster`, payload);
  }

  updateStatus(payload: EmployeeCategoryStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/updateEmployeeCategoryMasterStatus`, payload);
  }
}

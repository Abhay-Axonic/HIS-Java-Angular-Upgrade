import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  DoctorCategoryDetail,
  DoctorCategoryRow,
  DoctorCategorySavePayload,
  DoctorCategoryStatusPayload,
  DoctorCategoryUpdatePayload,
} from './doctor-category.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/global/billing/doctorCategoryController.js
 * and their literal string values in
 * resources/assets/js/config/global.url.config.js:36-41:
 *   GETDOCTORCATEGORYMASTERLIST      = /billing/getDoctorCategoryMasterList     (ctrl:86-87)
 *   GETDOCTORCATEGORYCOUNT           = /billing/getDoctorCategoryCount          (ctrl:88-89)
 *   SAVEDOCTORCATEGORYMASTER         = /billing/saveDoctorCategoryMaster        (ctrl:222-223)
 *   GETDOCTORCATEGORYMASTERBYID      = /billing/getDoctorCategoryMasterById     (ctrl:267-269)
 *   UPDATEDOCTORCATEGORYMASTERSTATUS = /billing/updateDoctorCategoryMasterStatus (ctrl:301-302)
 *   UPDATEDOCTORCATEGORYMASTER       = /billing/updateDoctorCategoryMaster       (ctrl:343-344)
 * `BASE_URL`/`ROOT_URL` (url.config.js) are both `''` in this deployment, so
 * these are the complete paths. List/count/save/update/status calls are all
 * POST in legacy (`GenericService.serviceAction("POST", ...)`); only the
 * by-id lookup is a GET with the id/organizationId in the path, matching
 * legacy's own `GenericService.serviceAction("GET", URI, data)` call with an
 * empty body.
 */
@Injectable({ providedIn: 'root' })
export class DoctorCategoryService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, DoctorCategoryRow>> {
    return this.http.post<ApiResponse<never, DoctorCategoryRow>>(`${this.base}/billing/getDoctorCategoryMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/billing/getDoctorCategoryCount`, { organizationId });
  }

  save(payload: DoctorCategorySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/saveDoctorCategoryMaster`, payload);
  }

  getById(id: number, organizationId: number): Observable<ApiResponse<never, DoctorCategoryDetail>> {
    return this.http.get<ApiResponse<never, DoctorCategoryDetail>>(
      `${this.base}/billing/getDoctorCategoryMasterById/${id}/${organizationId}`,
    );
  }

  updateStatus(payload: DoctorCategoryStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/updateDoctorCategoryMasterStatus`, payload);
  }

  update(payload: DoctorCategoryUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/updateDoctorCategoryMaster`, payload);
  }
}

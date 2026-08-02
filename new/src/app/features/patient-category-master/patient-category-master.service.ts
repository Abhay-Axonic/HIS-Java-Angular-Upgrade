import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  PatientCategoryDetail,
  PatientCategoryRow,
  PatientCategorySavePayload,
  PatientCategoryStatusPayload,
  PatientCategoryUpdatePayload,
} from './patient-category-master.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/config/global.url.config.js:52-58`
 * (`GLOBAL = "/global"`, `resources/assets/js/config/url.config.js:36`) and
 * `resources/assets/js/controller/master/global/billing/patientCategoryController.js`
 * -- not guessed. All 6 confirmed live against `http://localhost:8080` on
 * 2026-07-29 with a real authenticated session (real request/response pairs
 * captured via an XHR hook, and via direct `$scope` method invocation where
 * DOM clicks in the shared browser session were unreliable that day).
 *
 * Every call goes through `GenericService.serviceAction(...)` in legacy,
 * which POSTs regardless of semantic operation for 5 of the 6 endpoints --
 * reproduced as POST here too, except `getById`, which legacy genuinely
 * calls as a GET with path variables (`GenericService.serviceAction("GET",
 * URI, data)`, ctrl:259-263) -- reproduced as GET, not "upgraded" to a
 * consistent POST.
 */
@Injectable({ providedIn: 'root' })
export class PatientCategoryMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:72-108 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, PatientCategoryRow>> {
    return this.http.post<ApiResponse<never, PatientCategoryRow>>(`${this.base}/global/getPatientCategoryMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:89-124 -- POST { organizationId }; count returned in `object`.
  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/global/getPatientCategoryMasterCount`, {
      organizationId,
    });
  }

  // ctrl:188-248 -- real, working create endpoint. Duplicate-checked
  // server-side on (patientCategory + patientCategoryCode + isForeginerCategory)
  // all matching (case-insensitive-ish) -- see PatientCategoryMasterServiceImp.
  save(payload: PatientCategorySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/savePatientCategoryMaster`, payload);
  }

  // ctrl:259-263 -- GET with path variables, not a POST body.
  getById(patientCategoryId: number, organizationId: number): Observable<ApiResponse<never, PatientCategoryDetail>> {
    return this.http.get<ApiResponse<never, PatientCategoryDetail>>(
      `${this.base}/global/getPatientCategoryMasterById/${patientCategoryId}/${organizationId}`,
    );
  }

  // ctrl:321-370 -- real, working update endpoint. No confirm-modal gate on
  // the caller side (see component class doc comment).
  update(payload: PatientCategoryUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/updatePatientCategoryMaster`, payload);
  }

  // ctrl:280-318 -- real, working status-toggle endpoint.
  changeStatus(payload: PatientCategoryStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/updatePatientCategoryMasterStatus`, payload);
  }
}

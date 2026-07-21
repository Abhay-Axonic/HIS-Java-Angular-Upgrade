import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { GradeDetail, GradeRow, GradeSavePayload, GradeStatusPayload, GradeUpdatePayload } from './grade-creation-master.models';

/**
 * Endpoint paths are literal strings in
 * resources/assets/js/controller/billing/unit/gradeCreationMasterController.js
 * (not resolved through a `url.config.js` constant, unlike most other
 * masters) -- all under `/api/billing/*` (ctrl:75-76,111,171,210,250,294),
 * BASE_URL/ROOT_URL both "". Every call is POST except the by-id lookup
 * (GET), matching GenericService.serviceAction's verb at each legacy call
 * site. Live-confirmed 200s against the running dev backend
 * (`/api/billing/getContractGradeMasterList`,
 * `/api/billing/getContractGradeMasterCount`) via `legacy/Billing/src/main/java/
 * com/param/service/controller/ContractGradeMasterController.java`, ruling
 * out the entry-67/77 dead-backend pattern for this screen (no seeded rows
 * exist in this dev environment, but the endpoints themselves are real and
 * respond correctly).
 */
@Injectable({ providedIn: 'root' })
export class GradeCreationMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:57-96 (data1) -- POST { orgId, offset, noOfRecordsPerPage, unitId }.
  getList(orgId: number, offset: number, noOfRecordsPerPage: number, unitId: number): Observable<ApiResponse<never, GradeRow>> {
    return this.http.post<ApiResponse<never, GradeRow>>(`${this.base}/api/billing/getContractGradeMasterList`, {
      orgId,
      offset,
      noOfRecordsPerPage,
      unitId,
    });
  }

  // ctrl:57-96 (data2) -- POST { orgId, unitId }, count returned in `object`.
  getCount(orgId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/billing/getContractGradeMasterCount`, {
      orgId,
      unitId,
    });
  }

  // ctrl:145-201 -- POST, real create.
  save(payload: GradeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/saveContractGradeMaster`, payload);
  }

  // ctrl:203-231 -- GET, id/orgId/unitId as THREE path segments (unlike most
  // sibling masters' 1-2 segment by-id lookups); response.data.listObject[0]
  // is assigned wholesale to $scope.common.
  getById(id: number, orgId: number, unitId: number): Observable<ApiResponse<never, GradeDetail>> {
    return this.http.get<ApiResponse<never, GradeDetail>>(
      `${this.base}/api/billing/getContractGradeMasterById/${id}/${orgId}/${unitId}`,
    );
  }

  // ctrl:234-280 -- POST, real update (no confirm-modal step, unlike create).
  update(payload: GradeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updateContractGradeMaster`, payload);
  }

  // ctrl:282-317 -- POST, real status toggle.
  updateStatus(payload: GradeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updateContractGradeMasterStatus`, payload);
  }
}

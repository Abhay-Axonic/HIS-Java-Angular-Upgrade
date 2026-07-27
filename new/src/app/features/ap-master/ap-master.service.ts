import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ApMasterRow, ApMasterSavePayload, ApMasterStatusPayload, ApMasterUpdatePayload } from './ap-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/billing/unit/aPmasterMasterController.js
 * (322 lines, `aPmasterMasterController`), all under `/global/billing/*`.
 * List/count are GET with path params (ctrl:69-70), NOT POST with a body
 * -- unlike this batch's shiftMaster/dietUnitMater.
 */
@Injectable({ providedIn: 'root' })
export class ApMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:56-90 (initMasterList) -- real GET, path params.
  getList(
    orgId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, ApMasterRow>> {
    return this.http.get<ApiResponse<never, ApMasterRow>>(
      `${this.base}/global/billing/getAPCodeMasterList/${orgId}/${unitId}/${offset}/${noOfRecordsPerPage}`,
    );
  }

  getCount(orgId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(`${this.base}/global/billing/getAPCodeMasterCount/${orgId}/${unitId}`);
  }

  // ctrl:139-198 (save) -- real create.
  save(payload: ApMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/billing/saveAPCodeMaster`, payload);
  }

  // ctrl:200-229 (showUpdateBtn) -- response.data.listObject[0], ARRAY-
  // wrapped, same shape as this batch's dietUnitMater.
  getById(aPCodeId: number, orgId: number, unitId: number): Observable<ApiResponse<never, ApMasterRow>> {
    return this.http.get<ApiResponse<never, ApMasterRow>>(
      `${this.base}/global/billing/getAPCodeMasterById/${aPCodeId}/${orgId}/${unitId}`,
    );
  }

  // ctrl:232-277 (update) -- real update, no confirm modal in legacy.
  update(payload: ApMasterUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/billing/updateAPCodeMaster`, payload);
  }

  // ctrl:279-319 (updateStatus) -- real status toggle.
  updateStatus(payload: ApMasterStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/billing/updateAPCodeMasterStatus`, payload);
  }
}

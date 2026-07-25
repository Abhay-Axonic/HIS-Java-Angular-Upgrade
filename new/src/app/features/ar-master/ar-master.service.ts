import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ArMasterRow, ArMasterSavePayload, ArMasterStatusPayload, ArMasterUpdatePayload } from './ar-master.models';

/**
 * Endpoint paths per Batch 19 Step 0/1 audit, taken directly from
 * `resources/assets/js/controller/billing/unit/aRmasterMasterController.js`
 * -- all under `/global/billing`. List/count/getById are GET (with
 * path-segment params, not query params -- matching legacy exactly); save/
 * update/status are POST.
 */
@Injectable({ providedIn: 'root' })
export class ArMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    orgId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, ArMasterRow>> {
    return this.http.get<ApiResponse<never, ArMasterRow>>(
      `${this.base}/global/billing/getARCodeMasterList/${orgId}/${unitId}/${offset}/${noOfRecordsPerPage}`,
    );
  }

  getCount(orgId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(`${this.base}/global/billing/getARCodeMasterCount/${orgId}/${unitId}`);
  }

  getById(id: number, orgId: number, unitId: number): Observable<ApiResponse<never, ArMasterRow>> {
    return this.http.get<ApiResponse<never, ArMasterRow>>(
      `${this.base}/global/billing/getARCodeMasterById/${id}/${orgId}/${unitId}`,
    );
  }

  save(payload: ArMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/billing/saveARCodeMaster`, payload);
  }

  update(payload: ArMasterUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/billing/updateARCodeMaster`, payload);
  }

  updateStatus(payload: ArMasterStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/billing/updateARCodeMasterStatus`, payload);
  }
}

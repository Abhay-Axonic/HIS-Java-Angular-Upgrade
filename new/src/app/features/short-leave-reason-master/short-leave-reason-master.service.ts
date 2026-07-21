import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  ShortLeaveReasonDetail,
  ShortLeaveReasonRow,
  ShortLeaveReasonSavePayload,
  ShortLeaveReasonStatusPayload,
  ShortLeaveReasonUpdatePayload,
} from './short-leave-reason-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/adt/global/shortLeaveReasonMasterController.js
 * and resources/assets/js/config/adt.url.config.js:305-309,434 -- not
 * guessed. Independently confirmed against the live backend controller
 * (legacy/ADT/.../ShortLeaveReasonMasterController.java): all six
 * @RequestMapping entries are POST-only (no PUT/PATCH/GET mapping exists
 * for this module), so every call here is a POST, matching legacy's own
 * GenericService.serviceAction("POST", ...) call sites exactly.
 */
@Injectable({ providedIn: 'root' })
export class ShortLeaveReasonMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, ShortLeaveReasonRow>> {
    return this.http.post<ApiResponse<never, ShortLeaveReasonRow>>(`${this.base}/adt/getShortLeaveReasonMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getShortLeaveCount`, { organizationId });
  }

  getById(shortLeaveReasonId: number): Observable<ApiResponse<never, ShortLeaveReasonDetail>> {
    return this.http.post<ApiResponse<never, ShortLeaveReasonDetail>>(`${this.base}/adt/getShortLeaveReasonById`, {
      shortLeaveReasonId,
    });
  }

  save(payload: ShortLeaveReasonSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveShortLeaveReasonMaster`, payload);
  }

  update(payload: ShortLeaveReasonUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateShortLeaveReasonMaster`, payload);
  }

  updateStatus(payload: ShortLeaveReasonStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForShortLeaveReason`, payload);
  }
}

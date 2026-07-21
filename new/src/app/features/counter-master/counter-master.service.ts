import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  CounterDetail,
  CounterRow,
  CounterSavePayload,
  CounterStatusPayload,
  CounterUpdatePayload,
} from './counter-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/counterMasterController.js
 * and their literal string values in
 * resources/assets/js/config/global.url.config.js:157-162:
 *   SAVE_COUNTER_MASTER          = /api/global/unit/saveCounterMaster          (ctrl:176)
 *   UPDATE_COUNTER_MASTER        = /api/global/unit/updateCounterMaster        (ctrl:262)
 *   UPDATE_COUNTER_MASTER_STATUS = /api/global/unit/updateCounterMasterStatus  (ctrl:304)
 *   GET_COUNTER_MASTER_BY_ID     = /api/global/unit/getCounterMasterById       (ctrl:219)
 *   GET_COUNTER_MASTER_LIST      = /api/global/unit/getCounterMasterList       (ctrl:77)
 *   GET_COUNTER_MASTER_COUNT     = /api/global/unit/getCounterMasterCount      (ctrl:78)
 * `BASE_URL`/`ROOT_URL` (url.config.js) are both `''` in this deployment, so
 * these are the complete paths. List/count/save/update calls are all POST in
 * legacy (`GenericService.serviceAction("POST", ...)`); only the by-id lookup
 * is a GET, with counterId/organizationId/unitId as path segments in that
 * exact order (ctrl:219).
 */
@Injectable({ providedIn: 'root' })
export class CounterMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
    unitId: number,
  ): Observable<ApiResponse<never, CounterRow>> {
    return this.http.post<ApiResponse<never, CounterRow>>(`${this.base}/api/global/unit/getCounterMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
      unitId,
    });
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/global/unit/getCounterMasterCount`, {
      organizationId,
      unitId,
    });
  }

  save(payload: CounterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/saveCounterMaster`, payload);
  }

  getById(counterId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, CounterDetail>> {
    return this.http.get<ApiResponse<never, CounterDetail>>(
      `${this.base}/api/global/unit/getCounterMasterById/${counterId}/${organizationId}/${unitId}`,
    );
  }

  update(payload: CounterUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/updateCounterMaster`, payload);
  }

  // Minimal payload -- see CounterStatusPayload's doc comment for why this
  // deliberately does NOT mirror legacy's full-`$scope.common` POST body.
  updateStatus(payload: CounterStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/updateCounterMasterStatus`, payload);
  }
}

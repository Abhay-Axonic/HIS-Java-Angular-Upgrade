import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  MaritalStatusDetail,
  MaritalStatusRow,
  MaritalStatusSavePayload,
  MaritalStatusStatusPayload,
  MaritalStatusUpdatePayload,
} from './marital-status-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/maritalstatusMasterController.js and
 * resources/assets/js/config/adt.url.config.js:178-182/376 -- not guessed.
 * All 6 endpoints confirmed live (HTTP 200 with a real auth token) via
 * `tools/angular-audit/check_backend_endpoints.py --only maritalstatusMaster`
 * on 2026-07-21 -- this screen's backend is real and working; see the
 * component's class doc comment for why the frontend never actually
 * reaches most of it. moduleId 391 (app.js:3605-3626 resolve block,
 * confirmed live).
 */
@Injectable({ providedIn: 'root' })
export class MaritalStatusMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:92-118 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getMaritalStatusList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, MaritalStatusRow>> {
    return this.http.post<ApiResponse<never, MaritalStatusRow>>(`${this.base}/adt/getMaritalStatusMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:94-134 -- POST { organizationId }, count returned in `object`.
  getMaritalStatusCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getMaritalStatusCount`, {
      organizationId,
    });
  }

  // ctrl:197-258 -- real, working create endpoint. See models.ts for why
  // $scope.save (and therefore this call) is unreachable from the real view.
  saveMaritalStatus(payload: MaritalStatusSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveMaritalStatusMaster`, payload);
  }

  // ctrl:266-283 -- POST { id }; response.data.listObject[0] is the record.
  getMaritalById(id: number): Observable<ApiResponse<never, MaritalStatusDetail>> {
    return this.http.post<ApiResponse<never, MaritalStatusDetail>>(`${this.base}/adt/getMaritalById`, { id });
  }

  // ctrl:335-387 -- real, working update endpoint.
  updateMaritalStatus(payload: MaritalStatusUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateMaritalStatusMaster`, payload);
  }

  // ctrl:295-332 -- real, working status-toggle endpoint.
  updateMaritalStatusStatus(payload: MaritalStatusStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForMaritalStatus`, payload);
  }
}

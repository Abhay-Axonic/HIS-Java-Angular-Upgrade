import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  OccupationDetail,
  OccupationRow,
  OccupationSavePayload,
  OccupationStatusPayload,
  OccupationUpdatePayload,
} from './occupation-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/adt/global/occupationMasterController.js
 * and resources/assets/js/config/adt.url.config.js:192-196/380 -- not
 * guessed. All 6 endpoints confirmed live (HTTP 200 with a real auth token)
 * via `tools/angular-audit/check_backend_endpoints.py --only occupationMaster`
 * on 2026-07-21 -- this screen's backend is real and working. moduleId 393
 * (app.js:3633-3654 resolve block, confirmed live).
 *
 * **Correction, 2026-07-28:** every endpoint below IS reachable from the
 * real, sidebar-linked `/commonMaster/occupation` route -- confirmed live,
 * real save/update/status-toggle/edit-fetch round trips. See
 * occupation-master.ts's class doc comment for the full correction
 * writeup.
 */
@Injectable({ providedIn: 'root' })
export class OccupationMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:90-116 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getOccupationList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, OccupationRow>> {
    return this.http.post<ApiResponse<never, OccupationRow>>(`${this.base}/adt/getOccupationMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:92-132 -- POST { organizationId }, count returned in `object`.
  getOccupationCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getOccupationCount`, {
      organizationId,
    });
  }

  // ctrl:196-256 -- real, working create endpoint. See models.ts for why
  // $scope.save (and therefore this call) is unreachable from the real view.
  saveOccupation(payload: OccupationSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveOccupationMaster`, payload);
  }

  // ctrl:264-281 -- POST { id }; response.data.listObject[0] is the record.
  getOccupationById(id: number): Observable<ApiResponse<never, OccupationDetail>> {
    return this.http.post<ApiResponse<never, OccupationDetail>>(`${this.base}/adt/getOccupationMasterById`, { id });
  }

  // ctrl:333-384 -- real, working update endpoint.
  updateOccupation(payload: OccupationUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateOccupationMaster`, payload);
  }

  // ctrl:293-330 -- real, working status-toggle endpoint.
  updateOccupationStatus(payload: OccupationStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForOccupation`, payload);
  }
}

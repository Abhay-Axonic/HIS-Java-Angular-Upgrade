import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  CountryOption,
  StateDetail,
  StateRow,
  StateSavePayload,
  StateStatusPayload,
  StateUpdatePayload,
} from './state-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/adt.url.config.js (constants resolved, not
 * guessed -- GETACTIVECOUNTRYLIST:61, ADDSTATE:130, GETSTATELIST:131,
 * GETSINGLESTATE:132, UPDATESTATE:133, UPDATESTATESTATUS:134,
 * COUNTSTATEMASTER:404) and confirmed LIVE against the real dev backend
 * (all 7 returned HTTP 200 or, for the GET-with-missing-trailing-verb
 * check, HTTP 405 -- meaning the route IS mapped, just not to a bare GET
 * with no id segment -- via tools/angular-audit/check_backend_endpoints.py,
 * 2026-07-21).
 */
@Injectable({ providedIn: 'root' })
export class StateMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:63-78 -- GET, organizationId appended directly as a path segment
  // (no separating slash needed -- the constant itself ends in one). No
  // `unitId` involved anywhere in this controller (organization-scoped).
  getActiveCountryList(organizationId: number): Observable<ApiResponse<never, CountryOption>> {
    return this.http.get<ApiResponse<never, CountryOption>>(`${this.base}/adt/getActiveCountryList/${organizationId}`);
  }

  // ctrl:108-156 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getStateMasterList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, StateRow>> {
    return this.http.post<ApiResponse<never, StateRow>>(`${this.base}/adt/getStateMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:141-155 -- POST { organizationId }, count returned in `object`.
  getStateMasterCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getStateCount`, { organizationId });
  }

  // ctrl:233-238 -- POST, real create.
  saveState(payload: StateSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveStateMaster`, payload);
  }

  // ctrl:271-297 -- POST { stateId, organizationId }; response.data.listObject[0]
  // is the record.
  getStateById(stateId: number, organizationId: number): Observable<ApiResponse<never, StateDetail>> {
    return this.http.post<ApiResponse<never, StateDetail>>(`${this.base}/adt/getStateById`, { stateId, organizationId });
  }

  // ctrl:350-400 -- POST, real update (no confirm-modal step, unlike create).
  updateState(payload: StateUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStateMaster`, payload);
  }

  // ctrl:296-339 -- POST, real status toggle. Unlike raceMaster's
  // equivalent, this DOES trigger a list/count reload on both branches --
  // see component doc comment.
  updateStatusForState(payload: StateStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForState`, payload);
  }
}

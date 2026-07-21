import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { RaceDetail, RaceRow, RaceSavePayload, RaceStatusPayload, RaceUpdatePayload } from './race-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/adt.url.config.js (constants resolved, not
 * guessed -- GETRACELIST:52, COUNTRACEMASTER:398, ADDRACE:50,
 * GETSINGLERACE:52, UPDATERACESTATUS:54, UPDATERACE:53) and confirmed
 * LIVE against the real dev backend (all 6 returned HTTP 200 via
 * tools/angular-audit/check_backend_endpoints.py, 2026-07-21). All calls
 * are POST, matching GenericService.serviceAction("POST", ...) at every
 * call site in raceMasterController.js.
 */
@Injectable({ providedIn: 'root' })
export class RaceMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:87-140 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getRaceMasterList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, RaceRow>> {
    return this.http.post<ApiResponse<never, RaceRow>>(`${this.base}/adt/getRaceMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:126-139 -- POST { organizationId }, count returned in `object`.
  getRaceMasterCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getRaceCount`, { organizationId });
  }

  // ctrl:227-230 -- POST, real create.
  saveRace(payload: RaceSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveRaceMaster`, payload);
  }

  // ctrl:265-291 -- POST { raceId }; response.data.listObject[0] is the record.
  getRaceById(raceId: number): Observable<ApiResponse<never, RaceDetail>> {
    return this.http.post<ApiResponse<never, RaceDetail>>(`${this.base}/adt/getRaceById`, { raceId });
  }

  // ctrl:338-357 -- POST, real update (no confirm-modal step, unlike create).
  updateRace(payload: RaceUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateRaceMaster`, payload);
  }

  // ctrl:296-333 -- POST, real status toggle. No list reload call anywhere
  // in this function -- see component doc comment (a real divergence from
  // stateMaster's own updateStatus, which does reload).
  updateStatusForRace(payload: RaceStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForRace`, payload);
  }
}

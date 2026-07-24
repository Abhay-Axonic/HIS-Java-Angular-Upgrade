import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  NursingStationDetail,
  NursingStationRow,
  NursingStationSavePayload,
  NursingStationStatusPayload,
  NursingStationUpdatePayload,
  StoreOption,
} from './nursing-station-master.models';

/**
 * Endpoint paths taken directly from their literal string values in
 * resources/assets/js/config/adt.url.config.js:617-622:
 *   SAVENURSINGSTATIONMASTER       = /api/global/saveNursingStationMaster
 *   GETNURSINGSTATIONMASTERBYID    = /api/global/getNursingStationMasterById
 *   GETNURSINGSTATIONMASTERLIST    = /api/global/getNursingStationMasterList
 *   UPDATENURSINGSTATIONMASTER     = /api/global/updateNursingStationMaster
 *   UPDATENURSINGSTATIONMASTERSTATUS = /api/global/updateNursingStationMasterStatus
 *   GETNURSINGSTATIONMASTERCOUNT   = /api/global/getNursingStationMasterCount
 *
 * `BASE_URL`/`ROOT_URL` (config/url.config.js) are both `''` in this
 * deployment, so these are the complete paths -- WITH the `/api` prefix,
 * confirmed directly against `NursingStationServiceController.java`'s
 * class-level `@RequestMapping("api/global")` (unlike several other
 * screens on this same branch, e.g. mortuary-bed-master's bare `/adt`).
 * ALL 6 calls are the verbs shown below, matching
 * `GenericService.serviceAction(...)` calls in the controller exactly
 * (`getNursingStationMasterById` is a real GET with path params, not a
 * POST like the other 5).
 */
@Injectable({ providedIn: 'root' })
export class NursingStationMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:66-77 -- GET, hardcoded URL (NOT a url-config constant), and
  // returns a RAW ARRAY (ResponseEntity<List<...>>), not the usual
  // {status, listObject} envelope -- see StoreOption's doc comment.
  getStoreList(unitId: number, organizationId: number): Observable<StoreOption[]> {
    return this.http.get<StoreOption[]>(`${this.base}/api/unit/main/stores/${unitId}/${organizationId}`);
  }

  // ctrl:89-144 (initNursingList) -- POST { organizationId, unitId, offset, noOfRecordsPerPage }.
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, NursingStationRow>> {
    return this.http.post<ApiResponse<never, NursingStationRow>>(`${this.base}/api/global/getNursingStationMasterList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:108-111/129-143 -- POST { unitId, organizationId } (no offset/limit
  // -- count is over the WHOLE table for this org/unit, not the current
  // page), count returned in `object`.
  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/global/getNursingStationMasterCount`, {
      unitId,
      organizationId,
    });
  }

  // ctrl:206-265 (saveNursingStation) -- POST, real create.
  save(payload: NursingStationSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveNursingStationMaster`, payload);
  }

  // ctrl:268-293 (showUpdateBtn) -- real GET with 3 path params, no body.
  getById(nursingStationId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, NursingStationDetail>> {
    return this.http.get<ApiResponse<never, NursingStationDetail>>(
      `${this.base}/api/global/getNursingStationMasterById/${nursingStationId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:336-383 (updateNursingStation) -- POST, real update (no confirm
  // modal, unlike create). See NursingStationUpdatePayload's doc comment
  // for the server-side updated_by corruption this wires to as-is.
  update(payload: NursingStationUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateNursingStationMaster`, payload);
  }

  // ctrl:296-333 (updateStatus) -- POST, real status toggle.
  updateStatus(payload: NursingStationStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateNursingStationMasterStatus`, payload);
  }
}

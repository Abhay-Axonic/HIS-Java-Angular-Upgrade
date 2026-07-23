import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  IdentificationDetail,
  IdentificationRow,
  IdentificationSavePayload,
  IdentificationStatusPayload,
  IdentificationUpdatePayload,
} from './identification-master.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/config/adt.url.config.js` (lines 159-163, 412) and
 * confirmed against `IdentificationMasterController.java`'s real
 * `@RequestMapping("/adt")` class-level annotation + each method's own
 * `@RequestMapping` value. Base path is bare `/adt/...` -- NOT `/api/adt/...`
 * (a sibling screen in this same batch, `consentTypeMaster`, had a real bug
 * where `/api` was incorrectly prepended, 404-ing every call; deliberately
 * not repeated here). All 6 calls are real, live POSTs
 * (`GenericService.serviceAction("POST", ...)` throughout the controller).
 * `getActiveIdentificationList` (GET) and `getActiveIdentificationMasterListByOrgId`
 * also exist on the controller but are unused by THIS screen -- they back a
 * cache used by other screens elsewhere in the app -- and are deliberately
 * NOT modeled here.
 */
@Injectable({ providedIn: 'root' })
export class IdentificationMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:89-138 (initIdentificationMasterList/getIdentificationMasterList) --
  // POST { organizationId, offset, noOfRecordsPerPage }.
  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, IdentificationRow>> {
    return this.http.post<ApiResponse<never, IdentificationRow>>(`${this.base}/adt/getIdentificationMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:97-98/123-137 -- POST { organizationId }, count returned in `object`.
  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getIdentificationCount`, { organizationId });
  }

  // ctrl:296-332 (showUpdateBtn) -- POST { identificationId };
  // response.data.listObject[0] is the record (NOT `object`).
  getById(identificationId: number): Observable<ApiResponse<never, IdentificationDetail>> {
    return this.http.post<ApiResponse<never, IdentificationDetail>>(`${this.base}/adt/getIdentificationById`, {
      identificationId,
    });
  }

  // ctrl:201-293 (saveIdentification) -- POST, real create. See
  // IdentificationSavePayload's doc comment re the create-flag-reset fix.
  save(payload: IdentificationSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveIdentificationMaster`, payload);
  }

  // ctrl:377-452 (updateIdentification) -- POST, real update, no confirm
  // step (unlike save). Duplicate-name check on this path
  // (`GET_IDENTIFICATION_LIST_BY_NAME_NOT_ID`) is correctly parenthesized
  // and correctly case-insensitive -- confirmed directly against the named
  // query, no client-side pre-check added here to second-guess it.
  update(payload: IdentificationUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateIdentificationMaster`, payload);
  }

  // ctrl:335-374 (updateStatus) -- POST, real status toggle.
  updateStatus(payload: IdentificationStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForIdentification`, payload);
  }
}

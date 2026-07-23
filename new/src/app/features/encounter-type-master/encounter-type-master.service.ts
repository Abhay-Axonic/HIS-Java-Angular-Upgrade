import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  EncounterTypeDetail,
  EncounterTypeRow,
  EncounterTypeSavePayload,
  EncounterTypeStatusPayload,
  EncounterTypeUpdatePayload,
} from './encounter-type-master.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/config/adt.url.config.js` (lines 227-231, 416) and
 * `resources/assets/js/controller/master/encounterTypeMasterController.js`
 * -- not guessed. BASE_URL/ROOT_URL are both `""` in the current
 * deployment (`url.config.js`), so the real request path is exactly the
 * literal string each constant holds. All six calls are real, live POSTs
 * (`GenericService.serviceAction("POST", ...)` throughout this controller
 * -- unlike some sibling GlobalMaster screens, this one has no stray GETs).
 */
@Injectable({ providedIn: 'root' })
export class EncounterTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:97-118 (initEncounterTypeMasterList/getEncounterTypeMasterList) --
  // POST { organizationId, offset, noOfRecordsPerPage }.
  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, EncounterTypeRow>> {
    return this.http.post<ApiResponse<never, EncounterTypeRow>>(`${this.base}/adt/getEncounterTypeMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:100-107 -- POST { organizationId }, count returned in `object`.
  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getEncounterTypeCount`, { organizationId });
  }

  // ctrl:246-264 (showUpdateBtn) -- POST { encounterTypeId };
  // response.data.listObject[0] is the record (NOT `object`).
  getById(encounterTypeId: number): Observable<ApiResponse<never, EncounterTypeDetail>> {
    return this.http.post<ApiResponse<never, EncounterTypeDetail>>(`${this.base}/adt/getEncounterTypeById`, {
      encounterTypeId,
    });
  }

  // ctrl:170-208 (saveEncounterType) -- POST, real create. `status` is
  // always `'A'` -- see EncounterTypeSavePayload's doc comment.
  save(payload: EncounterTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveEncounterTypeMaster`, payload);
  }

  // ctrl:308-337 (updateEncounterType) -- POST, real update, no confirm
  // step (unlike save). NOTE: the backend's own duplicate-name check on
  // this path has a real HQL operator-precedence bug that can spuriously
  // reject a same-name edit -- see the component class doc comment. This
  // service must NOT add any client-side "already duplicate" pre-check
  // that would mask that real response.
  update(payload: EncounterTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateEncounterTypeMaster`, payload);
  }

  // ctrl:266-296 (updateStatus) -- POST, real status toggle.
  updateStatus(payload: EncounterTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForEncounterType`, payload);
  }
}

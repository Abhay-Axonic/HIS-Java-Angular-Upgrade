import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  GenderOption,
  PrefixDetail,
  PrefixRow,
  PrefixSavePayload,
  PrefixStatusPayload,
  PrefixUpdatePayload,
} from './prefix-master.models';

/**
 * Endpoint paths resolved from the `GETACTIVEGENDERLIST`/`ADDPREFIX`/
 * `GETPREFIXLIST`/`GETSINGLEPREFIX`/`UPDATEPREFIX`/`UPDATEPREFIXSTATUS`/
 * `COUNTPREFIXMASTER` constants
 * (resources/assets/js/config/adt.url.config.js:170,234-238,418), all under
 * `/adt`, BASE_URL/ROOT_URL both "" -- not guessed. Live-confirmed 200s
 * against the running dev backend (`/adt/getActiveGenderList`,
 * `/adt/getPrefixMasterList`, `/adt/getPrefixCount`), with real seeded data
 * (`PRE_01`/"Mr."/Male) rendering on the legacy screen itself, ruling out the
 * entry-67/77 dead-backend pattern for this screen.
 */
@Injectable({ providedIn: 'root' })
export class PrefixMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:76-90 -- GET, empty body.
  getActiveGenderList(): Observable<ApiResponse<never, GenderOption>> {
    return this.http.get<ApiResponse<never, GenderOption>>(`${this.base}/adt/getActiveGenderList`);
  }

  // ctrl:100-131 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, PrefixRow>> {
    return this.http.post<ApiResponse<never, PrefixRow>>(`${this.base}/adt/getPrefixMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:106-107/115-117 -- POST { organizationId }, count returned in `object`.
  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getPrefixCount`, { organizationId });
  }

  // ctrl:200-259 -- POST, real create.
  save(payload: PrefixSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/savePrefixMaster`, payload);
  }

  // ctrl:262-291 -- POST { prefixId }, NOT a GET-with-path-param like some
  // sibling masters; response.data.listObject[0] is the record.
  getById(prefixId: number): Observable<ApiResponse<never, PrefixDetail>> {
    return this.http.post<ApiResponse<never, PrefixDetail>>(`${this.base}/adt/getPrefixById`, { prefixId });
  }

  // ctrl:334-382 -- POST, real update (no confirm-modal step, unlike create).
  update(payload: PrefixUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updatePrefixMaster`, payload);
  }

  // ctrl:294-332 -- POST, real status toggle.
  updateStatus(payload: PrefixStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForPrefix`, payload);
  }
}

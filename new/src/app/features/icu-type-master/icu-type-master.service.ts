import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  ICUTypeRow,
  ICUTypeSavePayload,
  ICUTypeStatusPayload,
  ICUTypeUpdatePayload,
} from './icu-type-master.models';

/**
 * Endpoint paths from `adt.url.config.js:33-39,390`, all proxied under
 * `/adt/*` (`ICUTypeMasterController.java:19`, `@RequestMapping("/adt")`).
 * `getActiveICUTypeList`/`getUnitICUTypeList`/`getActiveUnitICUTypeList`
 * exist on the backend but are never called by icuTypeController.js --
 * deliberately not wired here (this screen's own list is unfiltered by
 * status, see `getList` below).
 */
@Injectable({ providedIn: 'root' })
export class IcuTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:85-137 (initIcUTypeMasterList) -- real server-side offset/count
  // pagination, no status filter (GET_ICU_LIST has none).
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, ICUTypeRow>> {
    return this.http.post<ApiResponse<never, ICUTypeRow>>(`${this.base}/adt/getICUTypeList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getICUCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:201-281 (saveIcuType) -- real create, server-side case-insensitive
  // duplicate-name check scoped to organizationId+unitId
  // (GET_ICU_LIST_BY_NAME) returns {status:'error'} on collision.
  save(payload: ICUTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveICUTypeMaster`, payload);
  }

  // ctrl:284-329 (showUpdateBtn) -- response.data.listObject[0], NOT
  // response.data.object (unlike shift-master's getById).
  getById(icutypeMasterId: number): Observable<ApiResponse<never, ICUTypeRow>> {
    return this.http.post<ApiResponse<never, ICUTypeRow>>(`${this.base}/adt/getICUById`, {
      icutypeMasterId,
    });
  }

  // ctrl:375-447 (updateIcuType) -- real update, no confirm modal in
  // legacy, duplicate-name check excludes self (GET_ICU_LIST_BY_NAME_NOT_ID).
  update(payload: ICUTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateICUtypeMaster`, payload);
  }

  // ctrl:332-372 (updateStatus) -- real status toggle.
  updateStatus(payload: ICUTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForICU`, payload);
  }
}

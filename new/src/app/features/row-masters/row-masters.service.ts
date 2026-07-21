import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  RowMasterDetail,
  RowMasterRow,
  RowMasterSavePayload,
  RowMasterStatusPayload,
  RowMasterUpdatePayload,
} from './row-masters.models';

/**
 * Endpoint paths taken directly from
 * MrdRowMasterController.java (mapped under /mrdrow) and
 * rowMastersController.js -- not guessed. All 4 endpoints used by this
 * screen are wired here; `/mrdrow/getActiveMrdRows/{orgId}/{unitId}` exists
 * on the Java controller but has no caller anywhere in
 * rowMastersController.js/row-master.html, so it is intentionally not
 * modeled -- it belongs to a different screen's dropdown, not this one.
 */
@Injectable({ providedIn: 'root' })
export class RowMastersService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:31-81 -- POST, real create.
  saveMrdRow(payload: RowMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/mrdrow/saveMrdRow`, payload);
  }

  // ctrl:84-159 -- POST { unitId, organizationId, page, size }. Wire field
  // names are genuinely `page`/`size` here (a record offset and page size,
  // same 0-based-offset semantics as every other rebuilt screen's
  // `offset`/`noOfRecordsPerPage` -- confirmed via legacy's own
  // PagerService.GetPager, whose `startIndex` is `(currentPage-1)*pageSize`),
  // not a copy-paste-safe rename of the usual pair. `object` carries the
  // total record count (`commonListCount`, ctrl:99/130/154), fed straight
  // into PagerService.GetPager alongside the list.
  getMrdRowList(
    unitId: number,
    organizationId: number,
    page: number,
    size: number,
  ): Observable<ApiResponse<number, RowMasterRow>> {
    return this.http.post<ApiResponse<number, RowMasterRow>>(`${this.base}/mrdrow/getMrdRowList`, {
      unitId,
      organizationId,
      page,
      size,
    });
  }

  // ctrl:179-198 -- GET, rowId/organizationId/unitId as path segments;
  // response.data.object (NOT listObject) is the single record.
  getMrdRowById(rowId: number, organizationId: number, unitId: number): Observable<ApiResponse<RowMasterDetail>> {
    return this.http.get<ApiResponse<RowMasterDetail>>(
      `${this.base}/mrdrow/getMrdRowById/${rowId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:201-228 -- PUT (not POST, unlike most sibling screens' update
  // calls), full row object per the Java DTO's actual required shape --
  // see class comment.
  updateMrdRow(payload: RowMasterUpdatePayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/mrdrow/updateMrdRow`, payload);
  }

  // ctrl:230-254 -- PUT, also the full row object (same DTO as
  // updateMrdRow), not a minimal { rowId, status } payload.
  updateMrdRowStatus(payload: RowMasterStatusPayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/mrdrow/updateMrdRowStatus`, payload);
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  ShiftDetail,
  ShiftRow,
  ShiftSavePayload,
  ShiftStatusPayload,
  ShiftUpdatePayload,
} from './shift-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/shiftMasterController.js
 * (356 lines, `shiftMasterController`), all under `/api/common/*`
 * (`ShiftMasterController.java:21`, `@RequestMapping("/api/common")`).
 * `getShiftlistByTime` (ctrl calls it nowhere -- confirmed dead/unreachable
 * during this session's silent-error-swallowing sweep, and separately
 * flagged as containing its own SQL injection in the same dead method) is
 * deliberately NOT wired here.
 */
@Injectable({ providedIn: 'root' })
export class ShiftMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:84-120 (initMasterList) -- real server-side offset/count pagination.
  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
    unitId: number,
  ): Observable<ApiResponse<never, ShiftRow>> {
    return this.http.post<ApiResponse<never, ShiftRow>>(`${this.base}/api/common/getShiftMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
      unitId,
    });
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/common/getShiftMasterCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:176-225 (save) -- real create.
  save(payload: ShiftSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/saveShiftMaster`, payload);
  }

  // ctrl:238-255 (showUpdateBtn) -- response.data.object, single record, NOT
  // array-wrapped (see shift-master.models.ts's ShiftDetail doc comment).
  getById(shiftId: number, organizationId: number, unitId: number): Observable<ApiResponse<ShiftDetail>> {
    return this.http.get<ApiResponse<ShiftDetail>>(
      `${this.base}/api/common/getShiftMasterById/${shiftId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:266-310 (update) -- real update, no confirm modal in legacy.
  update(payload: ShiftUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateShiftMaster`, payload);
  }

  // ctrl:314-353 (updateStatus) -- real status toggle.
  updateStatus(payload: ShiftStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateShiftMasterStatus`, payload);
  }
}

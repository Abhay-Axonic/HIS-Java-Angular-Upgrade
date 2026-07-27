import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  DietUnitRow,
  DietUnitSavePayload,
  DietUnitStatusPayload,
  DietUnitUpdatePayload,
} from './diet-unit-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/dietAndKitchen/global/dietUnitMasterController.js
 * (331 lines, `dietUnitMasterController`), all under `/api/diet/master/*`
 * (`DietUnitMasterController.java:22`, `@RequestMapping("/api/diet/master")`).
 */
@Injectable({ providedIn: 'root' })
export class DietUnitMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:61-97 (initMasterList) -- POST { organizationId }. The DAO query
  // (DietUnitMasterDaoImpl.getDietUnitMasterList) has no offset/limit at
  // all -- always returns every row for the org. offset/noOfRecordsPerPage
  // are still sent (matching the real request shape) but have no server-
  // side effect -- see diet-unit-master.ts's class doc comment.
  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, DietUnitRow>> {
    return this.http.post<ApiResponse<never, DietUnitRow>>(`${this.base}/api/diet/master/getDietUnitMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/diet/master/getDietUnitMasterCount`, {
      organizationId,
    });
  }

  // ctrl:145-203 (save) -- real create. SQL-injection-fixed as of
  // fix/dietunitmater-sql-injection-parameter-binding (merged this
  // session) -- uomName is now parameter-bound server-side.
  save(payload: DietUnitSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/diet/master/saveDietUnitMaster`, payload);
  }

  // ctrl:210-238 (showUpdateBtn) -- response.data.listObject[0], an
  // ARRAY-wrapped single-record lookup, NOT response.data.object (unlike
  // shiftMaster's getById). Modeled here exactly as that real shape.
  getById(uomId: number, organizationId: number): Observable<ApiResponse<never, DietUnitRow>> {
    return this.http.get<ApiResponse<never, DietUnitRow>>(
      `${this.base}/api/diet/master/getDietUnitMasterById/${uomId}/${organizationId}`,
    );
  }

  // ctrl:241-289 (update) -- real update. The update-self-collision bug in
  // the underlying duplicate-check (DietUnitMasterDaoImpl.checkAvailableUnitById,
  // uom_id =:uomId instead of <>:uomId) is backend behavior, faithfully
  // reproduced by simply calling the real endpoint and displaying whatever
  // it returns -- see diet-unit-master.ts's class doc comment. The
  // case-insensitivity half of that same PENDING_FINDINGS entry is
  // already fixed as a side effect of the SQL-injection fix -- confirmed
  // live during this build, see that same class doc comment.
  update(payload: DietUnitUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/diet/master/updateDietUnitMaster`, payload);
  }

  // ctrl:291-327 (updateStatus) -- real status toggle.
  updateStatus(payload: DietUnitStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/diet/master/updateDietUnitMasterStatus`, payload);
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { RelationDetail, RelationRow, RelationStatusPayload } from './relation-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/adt/global/relationMasterController.js and
 * resources/assets/js/config/adt.url.config.js:172-176/374 -- not guessed.
 * All POST, all under `/adt` (no `/api` prefix, BASE_URL/ROOT_URL both
 * confirmed live as "" via the running app's own globals).
 *
 * **Confirmed live against the real backend this session (2026-07-21):**
 * `getRelationMasterList`/`getRelationCount` both returned a real HTTP 200
 * with `status:"success"` (`object:0` -- zero relation rows currently exist
 * in this dev DB) via the app's own `GenericService` from an authenticated
 * session -- this screen's backend genuinely exists and works, UNLIKE
 * `modalityTypeMaster`/the homecare cluster (entries 67/77). The zero-row
 * count is itself corroborating evidence for this screen's severity-1
 * frontend bug (see relation-master.ts's class doc comment): if Save had
 * ever worked even once, there would likely be at least one row by now.
 *
 * `saveRelationMaster`/`updateRelationMaster` are included below for
 * completeness/documentation ONLY -- neither is ever called by
 * `relation-master.ts`, because neither is reachable from the live UI in
 * legacy either (see class doc comment: the Save/Update buttons call
 * `saveRelation()`/`updateRelation()`, which are undefined; the orphaned
 * `$scope.save`/`$scope.update` that DO call these endpoints are never
 * invoked by anything).
 */
@Injectable({ providedIn: 'root' })
export class RelationMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:85-116 -- POST { organizationId, offset, noOfRecordsPerPage }.
  // Real call, fires on load -- see class doc comment for why its result is
  // never displayed.
  getRelationMasterList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, RelationRow>> {
    return this.http.post<ApiResponse<never, RelationRow>>(`${this.base}/adt/getRelationMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:91-132 -- POST { organizationId }, count returned in `object`.
  getRelationCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getRelationCount`, { organizationId });
  }

  /** ctrl:194-256 -- orphaned, NEVER called from `relation-master.ts` (see
   * class doc comment). Kept only so the real endpoint/payload shape is on
   * record for whoever eventually fixes this screen. */
  saveRelationMaster(payload: {
    code: string;
    desc: string;
    status: 'A';
    createdBy: number;
    updatedBy: number;
    organizationId: number;
    createdDate: string;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveRelationMaster`, payload);
  }

  /** ctrl:331-383 -- orphaned, NEVER called from `relation-master.ts` (see
   * class doc comment). Kept only so the real endpoint/payload shape is on
   * record for whoever eventually fixes this screen. */
  updateRelationMaster(payload: {
    id: number;
    code: string;
    desc: string;
    updatedBy: number;
    updatedDate: string;
    organizationId: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateRelationMaster`, payload);
  }

  // ctrl:258-281 -- correctly named/wired to the view's edit pencil
  // (`showUpdateBtn`), but structurally unreachable since the table never
  // renders any row to click -- see class doc comment.
  getRelationById(id: number): Observable<ApiResponse<never, RelationDetail>> {
    return this.http.post<ApiResponse<never, RelationDetail>>(`${this.base}/adt/getRelationById`, { id });
  }

  // ctrl:291-329 -- correctly named/wired to the view's status checkbox
  // (`updateStatus`), but structurally unreachable for the same reason as
  // getRelationById above -- see class doc comment.
  updateStatusForRelation(payload: RelationStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForRelation`, payload);
  }
}

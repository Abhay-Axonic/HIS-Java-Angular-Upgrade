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
 * **Correction, 2026-07-28:** `saveRelationMaster`/`updateRelationMaster`
 * were previously documented as orphaned/never-callable, based on auditing
 * the wrong (unlinked) `/relationMaster` state. The real, sidebar-linked
 * `/commonMaster/relationmaster` route's own template correctly wires
 * `save()`/`update()` to these exact endpoints -- confirmed live 2026-07-28,
 * real `POST /adt/saveRelationMaster` and `POST /adt/updateRelationMaster`,
 * both HTTP 200, new/edited rows visibly persisting across reloads. See
 * relation-master.ts's class doc comment for the full correction writeup.
 */
@Injectable({ providedIn: 'root' })
export class RelationMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:85-116 -- POST { organizationId, offset, noOfRecordsPerPage }.
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

  // ctrl:194-256 -- confirmed live 2026-07-28, real save, real persistence.
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

  // ctrl:331-383 -- confirmed live 2026-07-28, real update, real persistence.
  // No confirm-modal gate (matches district-master's own save-only asymmetry).
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

  // ctrl:258-281 -- confirmed live 2026-07-28, real edit-fetch round trip.
  getRelationById(id: number): Observable<ApiResponse<never, RelationDetail>> {
    return this.http.post<ApiResponse<never, RelationDetail>>(`${this.base}/adt/getRelationById`, { id });
  }

  // ctrl:291-329 -- confirmed live 2026-07-28, real status toggle.
  updateStatusForRelation(payload: RelationStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForRelation`, payload);
  }
}

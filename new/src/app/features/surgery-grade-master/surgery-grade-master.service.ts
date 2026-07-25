import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  SurgeryGradeCreatePayload,
  SurgeryGradeRow,
  SurgeryGradeStatusPayload,
  SurgeryGradeUpdatePayload,
} from './surgery-grade-master.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/config/opd.url.config.js:120-124`
 * (`API="/api"`, `GLOBAL="/global"`) and
 * `resources/assets/js/controller/opd/global/surgeryGradeMasterController.js`.
 * All 5 calls confirmed real end-to-end by reading source directly:
 * `SurgeryGradeMasterController.java` (`@RequestMapping("api/global")`) ->
 * `SurgeryGradeMasterServiceImpl` -> `SurgeryGradeMasterDaoImpl` -> table
 * `public.m_surgery_grade_master`. `getSurgeryGradeMasterList` independently
 * confirmed live 200 via this session's own golden trace (2 calls on load:
 * template fetch + this list call, both 200 -- no separate count call, this
 * screen has no server-side pagination-count endpoint at all, see
 * `surgery-grade-master.ts`'s class doc comment).
 *
 * `getList` is POST `{ orgnisationId }` ONLY (ctrl:84-86, `commonObj`) --
 * no `unitId` sent, matching `GET_SURGERY_GRADE_LIST`'s own WHERE clause
 * (org-scoped only, `SurgeryGradeMaster.java:29`).
 */
@Injectable({ providedIn: 'root' })
export class SurgeryGradeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:82-105 (init) -- fetches the FULL result set in one shot, no
  // offset/count params at all; paginated entirely client-side via
  // PaginationService (see ../../core/client-pagination.util.ts).
  getList(organizationId: number): Observable<ApiResponse<never, SurgeryGradeRow>> {
    return this.http.post<ApiResponse<never, SurgeryGradeRow>>(`${this.base}/api/global/getSurgeryGradeMasterList`, {
      orgnisationId: organizationId,
    });
  }

  // ctrl:177-206 (showUpdateBtn) -- POST, real GET-by-id used by the edit
  // flow. `orgnisationId` IS sent here (ctrl:184) -- only the UPDATE payload
  // omits it, see surgery-grade-master.models.ts's doc comment.
  getById(surgeryGradeId: number, organizationId: number): Observable<ApiResponse<never, SurgeryGradeRow>> {
    return this.http.post<ApiResponse<never, SurgeryGradeRow>>(`${this.base}/api/global/getSurgeryGradeById`, {
      surgeryGradeId,
      orgnisationId: organizationId,
    });
  }

  // ctrl:125-174 (saveSurgeryGrade) -- POST, real create. The backend's own
  // duplicate-check has a confirmed cross-organization false-positive via a
  // JPQL operator-precedence bug -- see surgery-grade-master.ts's class doc
  // comment. Reproduced as-is; the frontend cannot compensate since the
  // check is entirely server-side.
  create(payload: SurgeryGradeCreatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveSurgeryGradeMaster`, payload);
  }

  // ctrl:247-286 (updateSurgeryGrade) -- POST, real update. See
  // surgery-grade-master.models.ts's doc comment: the payload deliberately
  // omits `orgnisationId`, which is precisely why the backend's own
  // duplicate-check on this path is a confirmed permanent no-op.
  update(payload: SurgeryGradeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateSurgeryGradeMaster`, payload);
  }

  // ctrl:209-244 (updateStatus) -- POST, real status toggle.
  updateStatus(payload: SurgeryGradeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateStatusForSurgeryGrade`, payload);
  }
}

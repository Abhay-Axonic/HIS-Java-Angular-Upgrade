import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  DoctorCategoryDetail,
  DoctorCategoryRow,
  DoctorCategorySavePayload,
  DoctorCategoryStatusPayload,
  DoctorCategoryUpdatePayload,
} from './doctor-category-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/global.url.config.js:218-223 and
 * doctorCategoryMasterController.js (BASE_URL="" + ROOT_URL="" + the
 * constant below) -- not guessed. Backend chain confirmed real end to end
 * (Controller -> Service -> DAO, real Hibernate named queries/Criteria, no
 * stubs) in legacy/Global/src/main/java/com/param/global/billing/{controller,
 * service,dao}/DoctorCategoryMaster* -- unlike modalityTypeMaster (entry
 * 67), this screen's backend genuinely exists.
 *
 * getList/getCount are two SEPARATE calls, matching legacy exactly (both
 * fire unconditionally on load -- confirmed via golden-trace capture,
 * tools/golden-trace/golden-traces/doctorCategoryMaster.json: exactly
 * these 2 POSTs, no more, no less) -- NOT collapsed into one call the way
 * ambulanceTypeMaster's rebuild did (that screen's OWN count call is dead
 * code in legacy; this screen's is live).
 *
 * Save/update duplicate-name checks (`getDoctorCategoryMasterByName`/
 * `...ByNameNotId`) happen entirely server-side -- the DAO returns
 * `status:"error"`/`message:"Already Exists."` and this service simply
 * passes that response through; no separate duplicate-check call needed
 * on this side.
 */
@Injectable({ providedIn: 'root' })
export class DoctorCategoryMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, DoctorCategoryRow>> {
    return this.http.post<ApiResponse<never, DoctorCategoryRow>>(`${this.base}/billing/getDoctorCategoryMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/billing/getDoctorCategoryCount`, { organizationId });
  }

  getById(doctorCategoryId: number, organizationId: number): Observable<ApiResponse<never, DoctorCategoryDetail>> {
    return this.http.get<ApiResponse<never, DoctorCategoryDetail>>(
      `${this.base}/billing/getDoctorCategoryMasterById/${doctorCategoryId}/${organizationId}`,
    );
  }

  save(payload: DoctorCategorySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/saveDoctorCategoryMaster`, payload);
  }

  update(payload: DoctorCategoryUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/updateDoctorCategoryMaster`, payload);
  }

  changeStatus(payload: DoctorCategoryStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/updateDoctorCategoryMasterStatus`, payload);
  }
}

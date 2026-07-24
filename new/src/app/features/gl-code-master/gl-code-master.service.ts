import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  GlCodeDetail,
  GlCodeRow,
  GlCodeSavePayload,
  GlCodeStatusPayload,
  GlCodeUpdatePayload,
} from './gl-code-master.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/controller/master/unit/gLcodeMasterController.js`
 * (all 6 calls build `BASE_URL + ROOT_URL + "/org/..."` literals in-line --
 * both `BASE_URL`/`ROOT_URL` are empty strings in this deployment,
 * `config/url.config.js:13,19`, so the real path is exactly `/org/...`).
 * All 6 confirmed against a real Java implementation, end to end:
 * `GeneralLedgerMasterController.java` (`@RequestMapping("/org")`) ->
 * `GeneralLedgerMasterServiceImpl` -> `GeneralLedgerMasterServiceDaoImpl`
 * -> table `public.m_general_ledger_master`.
 *
 * `getList` is a GET with path params (`{orgId}/{offset}/{noOfRecordsPerPage}`,
 * ctrl:77/122) -- legacy also builds a `data1` request body for this GET
 * (ctrl:65-70), but `GeneralLedgerMasterController.getLiceneceTypeMasterList`
 * only reads the 3 `@PathVariable`s, never a request body, so that body is
 * dead weight on the wire and is not reproduced here. `getById` is also a
 * GET with path params (`{lederId}/{orgId}`, ctrl:224), unlike
 * `discountCategoryMaster`'s POST-with-body `getById`.
 */
@Injectable({ providedIn: 'root' })
export class GlCodeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:59-106 (initMasterList) / ctrl:108-129 (masterList, same URI shape,
  // used by setPage) -- GET, path params only.
  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, GlCodeRow>> {
    return this.http.get<ApiResponse<never, GlCodeRow>>(
      `${this.base}/org/getGeneralLedgerMasterList/${organizationId}/${offset}/${noOfRecordsPerPage}`,
    );
  }

  // ctrl:72-104 (same function, the parallel count call) -- POST
  // {organizationId}; count returned in `object`.
  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/org/getGeneralLedgerMasterCount`, { organizationId });
  }

  // ctrl:217-246 (showUpdateBtn) -- GET, path params only.
  getById(generalLedgerId: number, organizationId: number): Observable<ApiResponse<never, GlCodeDetail>> {
    return this.http.get<ApiResponse<never, GlCodeDetail>>(
      `${this.base}/org/getGeneralLedgerMasterById/${generalLedgerId}/${organizationId}`,
    );
  }

  // ctrl:156-215 (save) -- POST, real create. The backend's own duplicate
  // check (`getGeneralLedgerMasterByName`) has a confirmed JPQL
  // operator-precedence bug -- see this screen's class doc comment.
  // Reproduced as-is; the frontend cannot compensate since the check is
  // entirely server-side.
  save(payload: GlCodeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/org/saveGeneralLedgerMaster`, payload);
  }

  // ctrl:249-305 (update) -- POST, real update. Same duplicate-check
  // scoping defect applies here too (`getLedgerByNameNotId` has no
  // `organizationId` filter at all -- see class doc comment).
  update(payload: GlCodeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/org/updateGeneralLedgerMaster`, payload);
  }

  // ctrl:307-343 (updateStatus) -- POST, real status toggle.
  updateStatus(payload: GlCodeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/org/updateGeneralLedgerMasterStatus`, payload);
  }
}

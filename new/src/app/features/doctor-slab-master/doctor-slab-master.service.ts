import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { DoctorOption, DoctorSlabApiRow, DoctorSlabMasterListRow } from './doctor-slab-master.models';

/**
 * Endpoint paths and payload shapes taken directly from
 * `doctorSlabMasterController.js`, all 5 real backend calls -- not
 * guessed. Every one of them uses `GenericService`/`BillingGenericService`
 * `.serviceAction("POST", ...)` in legacy (never PUT), so every call here
 * is a plain POST, matching exactly:
 *
 *  - ctrl:38-49  POST /adt/getActiveDoctorList
 *  - ctrl:243-251 POST /unit/DoctorSlab/getdoctorSlabList (BillingGenericService,
 *    NOTIFICATION_MSG_STATUS_FALSE -- legacy suppresses its own generic
 *    error growl for this call; the rebuild has no explicit error handler
 *    for it either way, matching legacy's silent-on-error list load)
 *  - ctrl:201-222 POST /unit/DoctorSlab/doctorSlabSave
 *  - ctrl:275-297 POST /unit/DoctorSlab/updateSlabStatusByDoctorId
 *  - ctrl:331-354 POST /unit/DoctorSlab/getdoctorSlabListByDoctorId
 *    (BillingGenericService, also NOTIFICATION_MSG_STATUS_FALSE)
 *
 * `BASE_URL`/`ROOT_URL` are both empty strings in the real deployment
 * (resources/assets/js/config/url.config.js), so every legacy URL here is
 * relative with no `/api` prefix -- `API_BASE_URL` mirrors that.
 */
@Injectable({ providedIn: 'root' })
export class DoctorSlabMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getActiveDoctorList(organizationId: number, unitId: number): Observable<ApiResponse<never, DoctorOption>> {
    return this.http.post<ApiResponse<never, DoctorOption>>(`${this.base}/adt/getActiveDoctorList`, {
      unitId,
      organizationId,
    });
  }

  getDoctorSlabList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<number, DoctorSlabMasterListRow>> {
    return this.http.post<ApiResponse<number, DoctorSlabMasterListRow>>(
      `${this.base}/unit/DoctorSlab/getdoctorSlabList`,
      { organizationId, unitId, noOfRecordsPerPage, offset },
    );
  }

  /** `tempDataList` -- the full slab-tier array for one doctor, one POST
   * for the whole set (ctrl:187-204). Each row's own `doctorSlabMasterId`
   * (present or absent) decides create vs. update per-row, server-side. */
  doctorSlabSave(rows: Record<string, unknown>[]): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/unit/DoctorSlab/doctorSlabSave`, rows);
  }

  /** Deliberately mirrors legacy's real payload exactly, including its own
   * gap: no `unitId` is ever sent (ctrl:267-273 never includes it), and
   * the DAO's own bulk update ignores `organizationId` too even though it
   * IS sent -- see the write-up in this batch's audit report. Faithfully
   * reproduced, not silently "fixed" by adding a `unitId` legacy never
   * sends -- that's a server-side scoping decision, not a client one. */
  updateSlabStatusByDoctorId(
    doctorId: number,
    status: 'A' | 'I',
    updatedBy: number,
    updatedDate: string,
    organizationId: number,
  ): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/unit/DoctorSlab/updateSlabStatusByDoctorId`, {
      doctorId,
      status,
      updatedBy,
      updatedDate,
      organizationId,
    });
  }

  /** Dual-purpose, matching ctrl:322-359 exactly: `flag=1` (info icon) is a
   * read-only fetch for the `#doctorSlabInfo` modal; `flag=0` (pencil) is
   * the edit-load that repopulates the editable slab list. Same endpoint
   * and payload either way -- only the caller-side handling differs. */
  getDoctorSlabListByDoctorId(
    organizationId: number,
    unitId: number,
    doctorId: number,
  ): Observable<ApiResponse<never, DoctorSlabApiRow>> {
    return this.http.post<ApiResponse<never, DoctorSlabApiRow>>(
      `${this.base}/unit/DoctorSlab/getdoctorSlabListByDoctorId`,
      { organizationId, unitId, doctorId },
    );
  }
}

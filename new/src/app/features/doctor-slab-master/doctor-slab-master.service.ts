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

  /** Previously mirrored legacy's pre-fix payload exactly (no `unitId`),
   * matching the DAO's then-real cross-org/unit scoping gap -- see this
   * batch's audit report. That gap is now fixed on both sides:
   * `doctorSlabMasterController.js`'s `updateStatus` was updated to send
   * `unitId` (fix/doctorslabmaster-crossorg-statustoggle, merged into
   * `main`), and `DoctorSlabMasterDaoImpl.updateSlabStatusByDoctorId`'s
   * WHERE clause now filters on both `unit_id` and `organization_id`.
   * This method was left sending the old, incomplete payload after that
   * merge -- corrected here to match. Without `unitId`, the fixed WHERE
   * clause receives `unit_id = NULL`, which SQL never matches against
   * anything (not even another NULL), so the UPDATE silently affects
   * zero rows while the DAO still unconditionally returns `SUCCESS` --
   * found live during the owner's own Step 6 review. */
  updateSlabStatusByDoctorId(
    doctorId: number,
    status: 'A' | 'I',
    updatedBy: number,
    updatedDate: string,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/unit/DoctorSlab/updateSlabStatusByDoctorId`, {
      doctorId,
      status,
      updatedBy,
      updatedDate,
      organizationId,
      unitId,
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

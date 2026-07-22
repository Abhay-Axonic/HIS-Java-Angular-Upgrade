import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  ClinicMasterDetail,
  ClinicMasterRow,
  ClinicMasterSavePayload,
  ClinicMasterStatus,
  ClinicMasterUpdatePayload,
  FloorOption,
  WingOption,
} from './clinic-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/clinicMasterController.js and
 * confirmed for real on the Java side against `ClinicMasterController.java`
 * (`@RequestMapping("api/global")`) / `FloorMasterController.java` /
 * `WingMasterController.java` (both `@RequestMapping("/adt")`) -- not
 * guessed. `BASE_URL`/`ROOT_URL` are both `""` in this deployment, so these
 * are the literal relative paths legacy calls.
 *
 * All 6 of ClinicMasterController's endpoints and both of the dropdown
 * dependencies' endpoints were confirmed to exist with a full
 * Controller->Service->DAO chain via direct source read (Step 0's
 * backend-existence check, entry 67's methodology) -- no
 * `modalityTypeMaster`-class dead-backend gap here. Golden-trace capture
 * (2026-07-22) independently confirms the exact same 4 calls fire on load,
 * in this exact shape: `POST /adt/getFloorMasterList`,
 * `GET /adt/getWingMasterListByOrgAndUnit/{orgId}/{unitId}`,
 * `GET /api/global/getClinicMasterList/{orgId}/{unitId}/{offset}/{noOfRecordsPerPage}`,
 * `GET /api/global/getClinicMasterCount/{orgId}/{unitId}`.
 */
@Injectable({ providedIn: 'root' })
export class ClinicMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:57-73 -- POST { organizationId, unitId }, NO offset/noOfRecordsPerPage
  // (see clinic-master.models.ts's FloorOption doc comment for why this
  // silently caps the dropdown at 10 rows).
  getFloorMasterList(organizationId: number, unitId: number): Observable<ApiResponse<never, FloorOption>> {
    return this.http.post<ApiResponse<never, FloorOption>>(`${this.base}/adt/getFloorMasterList`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:77-91 -- GET, org/unit as path segments.
  getWingMasterListByOrgAndUnit(organizationId: number, unitId: number): Observable<ApiResponse<never, WingOption>> {
    return this.http.get<ApiResponse<never, WingOption>>(
      `${this.base}/adt/getWingMasterListByOrgAndUnit/${organizationId}/${unitId}`,
    );
  }

  // ctrl:99-116 -- GET, org/unit/offset/noOfRecordsPerPage as path segments.
  getClinicMasterList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, ClinicMasterRow>> {
    return this.http.get<ApiResponse<never, ClinicMasterRow>>(
      `${this.base}/api/global/getClinicMasterList/${organizationId}/${unitId}/${offset}/${noOfRecordsPerPage}`,
    );
  }

  // ctrl:107 -- GET, org/unit as path segments, count in `object`.
  getClinicMasterCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(
      `${this.base}/api/global/getClinicMasterCount/${organizationId}/${unitId}`,
    );
  }

  // ctrl:153-182 -- real create. No confirm-modal step in legacy (unlike
  // several sibling masters) -- the view's `<modal visible="showModal">`
  // is never opened anywhere in this controller (`showModal` is never
  // assigned), so it's a fully dead/orphaned element in legacy today, not
  // reproduced here. See class doc comment on ClinicMaster.
  saveClinicMaster(payload: ClinicMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveClinicMaster`, payload);
  }

  // ctrl:185-215 -- GET, clinicMasterId/organizationId/unitId as path
  // segments; response.data.object (NOT listObject) is the record.
  getClinicMasterById(
    clinicMasterId: number,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<ClinicMasterDetail>> {
    return this.http.get<ApiResponse<ClinicMasterDetail>>(
      `${this.base}/api/global/getClinicMasterById/${clinicMasterId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:218-248 -- real update, no confirm modal (matches save -- neither
  // has one in this screen).
  updateClinicMaster(payload: ClinicMasterUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateClinicMaster`, payload);
  }

  // ctrl:251-277 -- GET, clinicMasterId/status as path segments. Unlike
  // several sibling masters' status-toggle endpoints, this one takes NO
  // updatedBy/updatedDate at all (`UPDATE_CLINIC_STATUS_BY_ID` is a bare
  // `SET status = :status` HQL update -- confirmed via
  // `ClinicMaster.java`'s own `@NamedQuery`) -- reproduced with exactly
  // this 2-argument shape, not padded with fields legacy never sends.
  updateClinicStatus(clinicMasterId: number, status: ClinicMasterStatus): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.base}/api/global/updatedClinicStatus/${clinicMasterId}/${status}`);
  }
}

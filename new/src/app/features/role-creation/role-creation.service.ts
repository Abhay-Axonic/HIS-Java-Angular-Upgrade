import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  DashboardOption,
  RoleDetail,
  RoleRow,
  SaveRolePayload,
  UpdateRolePayload,
  UpdateRoleStatusPayload,
  VaccinationOption,
} from './role-creation.models';

/**
 * Endpoint paths taken directly from `roleCreationMasterController.js`'s own
 * inline `BASE_URL + ROOT_URL + "/api/global/..."` literals (this screen
 * does not route its URLs through a `*.url.config.js` constants file --
 * confirmed via grep, same as this batch's sibling `role-management`).
 *
 * All 8 endpoints below confirmed real end to end (Controller -> Service ->
 * DAO): `saveUserRoleMaster`/`updateUserRoleMaster`/
 * `updateUserRoleMasterStatus`/`getUserRoleMasterList`/`getUserRoleMasterById`/
 * `getUserRoleMasterCount` all live on
 * `legacy/Global/src/main/java/com/param/global/unit/controller/UserRoleMasterController.java`
 * -- the SAME controller class `role-management` (Phase 4 batch 9) already
 * uses, but genuinely DIFFERENT methods (`role-management` calls
 * `getActiveUserRoleMasterList`/`getUserDetails`-family/`updateUserRoleMapperStatus`
 * instead -- confirmed by direct comparison of both controllers' source,
 * not assumed distinct). `getVaccination`
 * (`legacy/Global/.../unit/controller/EmployeeVaccinationController.java`)
 * and `getDashboardMasterList`
 * (`legacy/Global/.../controller/DashboardMasterController.java`) are on
 * two further separate, also-confirmed-real controllers.
 *
 * `UserRoleMasterController`'s class-level `@RequestMapping("api/global/ ")`
 * has a literal trailing space -- already investigated and ruled out as a
 * live routing bug in `docs/ANGULAR_PENDING_FINDINGS.md` entry 88 (real
 * `200`s in the live Tomcat access log for two of this controller's own
 * endpoints), not re-investigated here.
 */
@Injectable({ providedIn: 'root' })
export class RoleCreationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getRoleList(payload: {
    organisationId: number;
    offset: number;
    noOfRecordsPerPage: number;
    unitId: number;
  }): Observable<ApiResponse<never, RoleRow>> {
    return this.http.post<ApiResponse<never, RoleRow>>(`${this.base}/api/global/getUserRoleMasterList`, payload);
  }

  getRoleCount(payload: { organisationId: number; unitId: number }): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/global/getUserRoleMasterCount`, payload);
  }

  saveRole(payload: SaveRolePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveUserRoleMaster`, payload);
  }

  updateRole(payload: UpdateRolePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateUserRoleMaster`, payload);
  }

  updateRoleStatus(payload: UpdateRoleStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateUserRoleMasterStatus`, payload);
  }

  getRoleById(roleId: number, organisationId: number, unitId: number): Observable<ApiResponse<never, RoleDetail>> {
    return this.http.get<ApiResponse<never, RoleDetail>>(
      `${this.base}/api/global/getUserRoleMasterById/${roleId}/${organisationId}/${unitId}`,
    );
  }

  getVaccinationList(payload: { organisationId: number; unitId: number }): Observable<ApiResponse<never, VaccinationOption>> {
    return this.http.post<ApiResponse<never, VaccinationOption>>(`${this.base}/api/global/getVaccination`, payload);
  }

  getDashboardList(payload: { organizationId: number }): Observable<ApiResponse<never, DashboardOption>> {
    return this.http.post<ApiResponse<never, DashboardOption>>(`${this.base}/api/getDashboardMasterList`, payload);
  }
}

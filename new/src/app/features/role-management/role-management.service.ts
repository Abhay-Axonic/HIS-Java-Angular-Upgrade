import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  ActiveRoleListRequest,
  RoleRow,
  RoleUserRow,
  UpdateRoleStatusPayload,
  UpdateUserRoleMapperStatusPayload,
  UserListByRoleRequest,
} from './role-management.models';

/**
 * Endpoint paths taken directly from roleManagementMasterController.js
 * (hardcoded inline `BASE_URL + ROOT_URL + "/api/global/..."` literals in
 * the controller itself, not a `*.url.config.js` constant -- confirmed via
 * grep, this screen is one of the ones that doesn't route its URLs through
 * a shared config file). Backend chain confirmed real end to end
 * (Controller -> Service -> DAO, real Hibernate named queries + a genuine
 * `update()` persist, no stubs) in
 * legacy/Global/src/main/java/com/param/global/unit/{controller,service,dao}/
 * UserRoleMaster*, UserRoleMapper* -- both `m_role_master` (roles) and
 * `t_user_role_mapper` (user<->role assignments) are genuinely distinct
 * tables/entities/DAOs, not an aliased duplicate of each other.
 *
 * One note on `UserRoleMasterController.java`'s class-level
 * `@RequestMapping` -- it has a literal trailing space in its annotation
 * value (`"api/global/ "`). Investigated directly against the live local
 * Tomcat's access log (not just static reading): both
 * `getActiveUserRoleMasterList` and `updateUserRoleMasterStatus` show real
 * `200` responses from earlier real invocations, confirming Spring
 * resolves this route correctly in practice despite the stray space --
 * NOT a live routing bug, ruled out rather than assumed either way.
 */
@Injectable({ providedIn: 'root' })
export class RoleManagementService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getActiveRoleList(payload: ActiveRoleListRequest): Observable<ApiResponse<never, RoleRow>> {
    return this.http.post<ApiResponse<never, RoleRow>>(`${this.base}/api/global/getActiveUserRoleMasterList`, payload);
  }

  getUserListByRoleId(payload: UserListByRoleRequest): Observable<ApiResponse<never, RoleUserRow>> {
    return this.http.post<ApiResponse<never, RoleUserRow>>(`${this.base}/api/global/getUserListByRoleId`, payload);
  }

  updateRoleStatus(payload: UpdateRoleStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateUserRoleMasterStatus`, payload);
  }

  updateUserRoleMapperStatus(payload: UpdateUserRoleMapperStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateUserRoleMapperStatus`, payload);
  }
}

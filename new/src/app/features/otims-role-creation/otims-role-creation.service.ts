import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  OtimsRoleRow,
  OtimsRoleSavePayload,
  OtimsRoleStatusPayload,
  OtimsRoleUpdatePayload,
} from './otims-role-creation.models';

/**
 * Endpoint paths taken directly from `OTIMSroleCreationController.js`,
 * all under `/api/global/otimsRole/*`.
 */
@Injectable({ providedIn: 'root' })
export class OtimsRoleCreationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:38-51 (masterList) -- fetches ALL rows for org/unit in one call,
  // no offset/limit params anywhere -- legacy has no pagination on this screen.
  getList(orgId: number, unitId: number): Observable<ApiResponse<never, OtimsRoleRow>> {
    return this.http.post<ApiResponse<never, OtimsRoleRow>>(`${this.base}/api/global/otimsRole/getAllOtimsRoleMasterList`, {
      orgId,
      unitId,
    });
  }

  // ctrl:69-125 (save) -- duplicate-check is org+unit+roleName scoped, correctly excludes nothing on create (expected).
  save(payload: OtimsRoleSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/otimsRole/saveOtimsRoleMaster`, payload);
  }

  // ctrl:159-197 (update) -- reuses the SAME duplicate-check query as save,
  // with no roleId exclusion -- see class doc, faithfully reproduced.
  update(payload: OtimsRoleUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/otimsRole/updateOtimsRoleMaster`, payload);
  }

  // ctrl:209-230 (updateStatus) -- real status toggle.
  updateStatus(payload: OtimsRoleStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/otimsRole/updateOtimsRoleMasterStatus`, payload);
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  DepartmentStoreMapperDetail,
  DepartmentStoreMapperFilterPayload,
  DepartmentStoreMapperRow,
  DepartmentStoreMapperSavePayload,
} from './department-store-mapper.models';

/** Raw shape of `storeList()`'s response (ctrl:201-227) -- `response.data`
 * itself IS the array, with NO `{status,message,object,listObject}`
 * envelope at all. The only endpoint in this whole controller shaped this
 * way -- every other call below goes through the usual `ApiResponse`
 * envelope. Not a typo; read directly off the real response. */
export interface RawStoreOption {
  id: number;
  store: string;
}

/** Raw shape of one entry in `getDeptListById`'s `listObject` (ctrl:233-259). */
export interface RawDeptOption {
  deptId: number;
  deptName: string;
}

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/pharmacy/departmentStoreMapperController.js
 * -- not guessed. BASE_URL/ROOT_URL are both "" (resources/assets/js/config/url.config.js).
 *
 * Legacy's own `saveDeptStroreMapper`/`updateDeptStroreMapper` functions
 * (ctrl:267-332/445-492, calling `/api/globals/saveDeptStoremapper` and
 * `/api/globals/updateDeptStoremapper`) are never wired to any `ng-click`
 * in the view -- only `SaveAndUpdateDeptStroreMapper` (view:85) is. Those
 * two dead endpoints are deliberately NOT exposed here; see the component's
 * class doc comment.
 *
 * Legacy's state `departmentStoreMapper` (app.js:20435-20439) has NO
 * `resolve` block at all -- no module-access gate, by design, same
 * precedent as `billingGroupMaster`/`periodicityService`
 * (docs/ANGULAR_REBUILD_PLAYBOOK.md "known caveats" #1). Not a rebuild bug.
 */
@Injectable({ providedIn: 'root' })
export class DepartmentStoreMapperService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:201-227 (storeList) -- GET, raw array response, no envelope (see
  // RawStoreOption's doc comment).
  getStoreListRaw(organizationId: number, unitId: number, userId: number): Observable<RawStoreOption[]> {
    return this.http.get<RawStoreOption[]>(`${this.base}/api/global/user/stores/${organizationId}/${unitId}/${userId}`);
  }

  // ctrl:233-259 (deptList) -- POST { orgId, unitId }.
  getDeptListRaw(orgId: number, unitId: number): Observable<ApiResponse<never, RawDeptOption>> {
    return this.http.post<ApiResponse<never, RawDeptOption>>(`${this.base}/api/globals/getDeptListById`, { orgId, unitId });
  }

  // ctrl:337-366 (deptStoremappedList) -- POST { orgId, unitId }. Fetches
  // the ENTIRE mapping list in one shot -- legacy paginates/searches this
  // client-side afterward via PaginationService, there is no
  // offset/pageSize sent to the backend at all. See component doc comment.
  getMappedList(orgId: number, unitId: number): Observable<ApiResponse<never, DepartmentStoreMapperRow>> {
    return this.http.post<ApiResponse<never, DepartmentStoreMapperRow>>(`${this.base}/api/globals/getDeptStoremappedList`, {
      orgId,
      unitId,
    });
  }

  // ctrl:532-557 (GetDepartmentStoreMapperNew) -- POST, fired live on every
  // multiselect selection change. See component doc comment: the response
  // is real but never reaches the visible table in legacy -- reproduced
  // faithfully, not fixed into a working live filter.
  getFilteredListNew(payload: DepartmentStoreMapperFilterPayload): Observable<ApiResponse<never, DepartmentStoreMapperRow>> {
    return this.http.post<ApiResponse<never, DepartmentStoreMapperRow>>(
      `${this.base}/api/globals/getDeptStoremappedListNew`,
      payload,
    );
  }

  // ctrl:412-436 (showUpdateBtn) -- GET; response.data.OBJECT (a single
  // object), not listObject -- see DepartmentStoreMapperDetail's doc comment.
  getById(orgId: number, unitId: number, deptStoreMapperId: number): Observable<ApiResponse<DepartmentStoreMapperDetail>> {
    return this.http.get<ApiResponse<DepartmentStoreMapperDetail>>(
      `${this.base}/api/globals/getDeptStoreMapperById/${orgId}/${unitId}/${deptStoreMapperId}`,
    );
  }

  // ctrl:369-403 (updateStatus) -- GET, real status toggle.
  updateStatus(orgId: number, unitId: number, deptStoreMapperId: number, status: 'A' | 'I'): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(
      `${this.base}/api/globals/activeInactiveDeptStoreMapper/${orgId}/${unitId}/${deptStoreMapperId}/${status}`,
    );
  }

  // ctrl:559-608 (SaveAndUpdateDeptStroreMapper) -- POST, the ONE real
  // save/update path this screen's view actually wires to (see class doc
  // comment on the two dead legacy functions this rebuild does not port).
  saveAndUpdate(payload: DepartmentStoreMapperSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/globals/SaveAndUpdateDeptStroreMapper`, payload);
  }
}

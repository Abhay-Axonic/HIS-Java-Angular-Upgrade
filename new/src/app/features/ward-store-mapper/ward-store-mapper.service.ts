import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  WardOption,
  WardStoreMapperDetail,
  WardStoreMapperRow,
  WardStoreMapperSavePayload,
  WardStoreOption,
} from './ward-store-mapper.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/pharmacy/wardStoreMapperController.js --
 * not guessed. BASE_URL/ROOT_URL are both "" (resources/assets/js/config/url.config.js).
 *
 * Legacy's state `wardStoreMapper` (app.js:20441-20446) has NO `resolve`
 * block at all -- no module-access gate, by design, same precedent as its
 * sibling `departmentStoreMapper` (app.js:20435-20439) immediately above
 * it. Not a rebuild bug.
 */
@Injectable({ providedIn: 'root' })
export class WardStoreMapperService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:57-95 (initMasterList/masterList) -- POST, real field names as
  // read off the controller: {orgId, offset, noOfRecordsPerPage, unitId}
  // (`storeId`/`wardId` accepted by the backend too but never populated by
  // this legacy screen -- see component class doc comment for why this
  // rebuild fetches the full result set in one shot rather than
  // re-issuing a fresh server call per page the way legacy's own
  // `setPage(page, true)` -> `masterList(...)` does).
  getPagedList(payload: Record<string, unknown>): Observable<ApiResponse<never, WardStoreMapperRow>> {
    return this.http.post<ApiResponse<never, WardStoreMapperRow>>(`${this.base}/api/globals/getWardStoremappedListPagination`, payload);
  }

  // ctrl:70-93 (data2/URI2) -- POST, {orgId, unitId}. `response.data.object`
  // is a bare count.
  getListCount(orgId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/globals/getWardStoremappedListPaginationCount`, { orgId, unitId });
  }

  // ctrl:138-150 (storeList) -- GET, raw array response, no envelope (see
  // WardStoreOption's doc comment).
  getStoreOptions(orgId: number, unitId: number, userId: number): Observable<WardStoreOption[]> {
    return this.http.get<WardStoreOption[]>(`${this.base}/api/global/user/stores/${orgId}/${unitId}/${userId}`);
  }

  // ctrl:152-167 (wardList) -- POST, {organizationId, unitId}. NOTE: this
  // one genuinely uses `organizationId`, NOT `orgId` like every other call
  // in this same controller -- verified directly from ctrl:154-157, not a
  // typo introduced here.
  getWardOptions(organizationId: number, unitId: number): Observable<ApiResponse<never, WardOption>> {
    return this.http.post<ApiResponse<never, WardOption>>(`${this.base}/api/adt/getActiveWardMasterList`, { organizationId, unitId });
  }

  // ctrl:192-242 (saveWardStoreMapper) -- POST, the real Save path
  // (ward-store-mapper.html:57-60).
  save(payload: WardStoreMapperSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/globals/saveWardStoremapper`, payload);
  }

  // ctrl:320-370 (updateWardStoreMapper) -- POST, the real Update path
  // (ward-store-mapper.html:61-64).
  update(payload: WardStoreMapperSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/globals/updateWardStoremapper`, payload);
  }

  // ctrl:279-300 (showUpdateBtn) -- GET; `response.data.object` (a single
  // object, see WardStoreMapperDetail's doc comment).
  getById(orgId: number, unitId: number, wardStoreMapperId: number): Observable<ApiResponse<WardStoreMapperDetail>> {
    return this.http.get<ApiResponse<WardStoreMapperDetail>>(
      `${this.base}/api/globals/getWardStoreMapperById/${orgId}/${unitId}/${wardStoreMapperId}`,
    );
  }

  // ctrl:245-276 (updateStatus) -- GET, real status toggle.
  updateStatus(orgId: number, unitId: number, wardStoreMapperId: number, status: 'A' | 'I'): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(
      `${this.base}/api/globals/activeInactiveWardStoreMapper/${orgId}/${unitId}/${wardStoreMapperId}/${status}`,
    );
  }
}

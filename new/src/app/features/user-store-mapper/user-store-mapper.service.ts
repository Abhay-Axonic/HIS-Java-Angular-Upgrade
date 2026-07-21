import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { StoreOption, UserOption, UserStoreMapperByIdRow, UserStoreMapperRow, UserStoreMapperSavePayload } from './user-store-mapper.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/global/userStoreMapperController.js
 * and legacy/Global/.../controller/UserStoreMapperController.java -- not guessed.
 *
 * Legacy has TWO list endpoints, confirmed via golden-trace re-audit
 * (Step 4) to genuinely differ in which one fires on load: `getList` (this
 * class) hits `/getUserStoreMapperlist/userNameWise` (`initMasterList`,
 * ctrl:179-210), called on initial load and after every save/update, and
 * hardcodes offset=0/noOfRecordsPerPage=10 on the AngularJS side
 * regardless of what's passed in -- so the "Show N entries" dropdown is a
 * genuine legacy no-op immediately after load or after saving. `getPage`
 * hits the different endpoint `/getUserStoreMapperlist` (`masterList`,
 * ctrl:217-236), which honors the real offset/noOfRecordsPerPage -- this
 * is what an actual page-number/Next/Previous click fires. Both are
 * reproduced faithfully as two separate methods; do not collapse them
 * into one "real" endpoint, since the endpoint difference is itself
 * observable in a real golden-trace capture.
 */
@Injectable({ providedIn: 'root' })
export class UserStoreMapperService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getStoreList(organizationId: number, unitId: number): Observable<ApiResponse<never, StoreOption>> {
    return this.http.get<ApiResponse<never, StoreOption>>(`${this.base}/api/global/getStoreList/${organizationId}/${unitId}`);
  }

  getUsersList(organizationId: number, unitId: number): Observable<ApiResponse<never, UserOption>> {
    return this.http.get<ApiResponse<never, UserOption>>(`${this.base}/api/global/getUserListForStore/${organizationId}/${unitId}`);
  }

  /** Matches `initMasterList` -- fired on load / after save-update. Legacy hardcodes offset=0/size=10 itself. */
  getList(organizationId: number, unitId: number): Observable<ApiResponse<never, UserStoreMapperRow>> {
    return this.http.post<ApiResponse<never, UserStoreMapperRow>>(`${this.base}/api/global/getUserStoreMapperlist/userNameWise`, {
      organizationId,
      unitId,
      offset: 0,
      noOfRecordsPerPage: 10,
    });
  }

  /** Matches `masterList` -- fired only by real page/size navigation, honors the requested offset/size. */
  getPage(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, UserStoreMapperRow>> {
    return this.http.post<ApiResponse<never, UserStoreMapperRow>>(`${this.base}/api/global/getUserStoreMapperlist`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(`${this.base}/api/global/getUserStoreMapperCount/${organizationId}/${unitId}`);
  }

  updateStatus(userId: number, organizationId: number, unitId: number, isActive: boolean): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(
      `${this.base}/api/global/updateUserStoreMapperStatus/${userId}/${organizationId}/${unitId}/${isActive}`,
    );
  }

  getById(userId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, UserStoreMapperByIdRow>> {
    return this.http.get<ApiResponse<never, UserStoreMapperByIdRow>>(
      `${this.base}/api/global/getUserStoreMapperById/${userId}/${organizationId}/${unitId}`,
    );
  }

  save(payload: UserStoreMapperSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveUserStoreMapper`, payload);
  }

  update(payload: UserStoreMapperSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateUserStoreMapper`, payload);
  }

  getDetailedStoreList(userId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, StoreOption>> {
    return this.http.get<ApiResponse<never, StoreOption>>(
      `${this.base}/api/global/getDetailedStoreList/${userId}/${organizationId}/${unitId}`,
    );
  }
}

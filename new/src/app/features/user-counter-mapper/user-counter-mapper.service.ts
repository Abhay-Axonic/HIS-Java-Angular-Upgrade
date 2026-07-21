import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  CounterOption,
  DetailedCounterItem,
  UserCounterMapperByIdItem,
  UserCounterMapperRow,
  UserCounterMapperSavePayload,
  UserOption,
} from './user-counter-mapper.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/global.url.config.js:166-173/204 (the
 * ADD/UPDATE/UPDATE_STATUS/GET_BY_ID/GET_LIST/GET_COUNT/GET_USER_LIST/
 * GET_DETAILED_LIST constants) and
 * resources/assets/js/controller/master/global/userCounterMapperController.js's
 * `searchGroupList()` (ctrl:95-117, the one call that hits a different
 * controller -- CounterMasterController's `counter/organization/.../unit/...`,
 * not UserCounterMapperController) -- not guessed. `S = "/"` is the
 * path-segment separator legacy concatenates by hand; `BASE_URL`/`ROOT_URL`
 * are both empty strings in this deployment (see API_BASE_URL's doc comment).
 *
 * Confirmed real quirk (not reproduced as a divergence, just noted): the
 * status-toggle endpoint is a GET despite mutating state
 * (UPDATE_USER_COUNTER_MASTER_STATUS+S+id+S+status ->
 * GET /api/global/userCounterMapper/status/{userId}/{status}), matching
 * UserCounterMapperController.activateInactivateUserCounterMapper exactly.
 *
 * GET_DETAILED_LIST_USER_COUNTER_MASTER and GET_USER_COUNTER_MASTER_BY_ID are
 * literally the SAME URL constant in legacy
 * ("/api/global/userCounterMapper/user") -- both the "Details" info-icon
 * modal (getCounterListMasterByUserId) and the edit-load path (showUpdateBtn)
 * hit `getUserCounterMapperById`. Reproduced here as `getById()` used by both
 * `edit()` and `openDetails()`.
 */
@Injectable({ providedIn: 'root' })
export class UserCounterMapperService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** searchGroupList() ctrl:95-117 -- left-hand "Counter" checkbox list. */
  getCounterList(organisationId: number, unitId: number): Observable<ApiResponse<never, CounterOption>> {
    return this.http.get<ApiResponse<never, CounterOption>>(
      `${this.base}/api/global/unit/counter/organization/${organisationId}/unit/${unitId}`,
    );
  }

  /** getUserList() ctrl:150-167 -- "User" <select> options (GET_USER_LIST,
   * served by UserNursingStationMapperController, a different controller). */
  getUserList(organisationId: number, unitId: number): Observable<ApiResponse<never, UserOption>> {
    return this.http.get<ApiResponse<never, UserOption>>(
      `${this.base}/api/global/getUserList/${organisationId}/${unitId}`,
    );
  }

  /** initMasterList() ctrl:177-208 -- the bottom table (GET_USER_COUNTER_MASTER_LIST). */
  getMapperList(
    orgId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, UserCounterMapperRow>> {
    return this.http.get<ApiResponse<never, UserCounterMapperRow>>(
      `${this.base}/api/global/userCounterMapper/${orgId}/${unitId}/${offset}/${noOfRecordsPerPage}`,
    );
  }

  /** initMasterList() ctrl:188/199-206 -- GET_USER_COUNTER_MASTER_COUNT. */
  getMapperCount(orgId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(`${this.base}/api/global/userCounterMapper/size/${orgId}/${unitId}`);
  }

  /** showUpdateBtn() ctrl:343-387 AND getCounterListMasterByUserId() ctrl:499-518
   * -- both hit GET_USER_COUNTER_MASTER_BY_ID (same URL as
   * GET_DETAILED_LIST_USER_COUNTER_MASTER in legacy, see class doc). */
  getById(orgId: number, unitId: number, userId: number): Observable<ApiResponse<never, UserCounterMapperByIdItem>> {
    return this.http.get<ApiResponse<never, UserCounterMapperByIdItem>>(
      `${this.base}/api/global/userCounterMapper/user/${orgId}/${unitId}/${userId}`,
    );
  }

  /** save() ctrl:247-340 -- ADD_USER_COUNTER_MASTER (POST). */
  save(payload: UserCounterMapperSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/userCounterMapper`, payload);
  }

  /** update() ctrl:389-458 -- UPDATE_USER_COUNTER_MASTER (PUT). */
  update(payload: UserCounterMapperSavePayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/api/global/userCounterMapper`, payload);
  }

  /** updateStatus() ctrl:460-496 -- UPDATE_USER_COUNTER_MASTER_STATUS (GET,
   * path-encoded, mutates despite being a GET -- see class doc). */
  updateStatus(userId: number, status: 'A' | 'I'): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.base}/api/global/userCounterMapper/status/${userId}/${status}`);
  }

  /** getCounterListMasterByUserId() ctrl:499-518 -- the info-icon "Details"
   * modal. Reuses `getById()` (see class doc -- literally the same URL). */
  getDetailedCounterList(
    orgId: number,
    unitId: number,
    userId: number,
  ): Observable<ApiResponse<never, DetailedCounterItem>> {
    return this.http.get<ApiResponse<never, DetailedCounterItem>>(
      `${this.base}/api/global/userCounterMapper/user/${orgId}/${unitId}/${userId}`,
    );
  }
}

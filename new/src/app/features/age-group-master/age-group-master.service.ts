import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AgeGroupMasterPayload, AgeGroupMasterRecord, AgeTypeGroup } from './age-group-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/lis.url.config.js (LIS_GLOBAL = "/LIS/global")
 * and ageGroupController.js's GenericService.serviceAction calls -- not
 * guessed. All 7 confirmed live (200) under a real authenticated session
 * during candidate research for this batch:
 *  - list:    GET  /LIS/global/listAgeGroupMaster/{orgId}/{offset}/{noOfRecordsPerPage}
 *  - count:   GET  /LIS/global/getToTalAgeGroupMasterRecord/{orgId}
 *  - dropdown GET  /LIS/global/listOfAgeTypeGroup
 *  - add:     POST /LIS/global/addAgeGroupMaster
 *  - update:  PUT  /LIS/global/updateAgeGroup
 *  - getById: GET  /LIS/global/getAgeGroupById/{orgId}/{id}
 *  - toggle:  GET  /LIS/global/activeInactiveAgeGroup/{orgId}/{id}/{status}
 *
 * The toggle endpoint is a real GET (confirmed against
 * AgeGroupMasterController.java:71-86, `@RequestMapping(method =
 * RequestMethod.GET)`) -- not the PUT some sibling masters use for their
 * status toggle. Don't "normalize" it to PUT; it would 405 against the real
 * backend.
 */
@Injectable({ providedIn: 'root' })
export class AgeGroupMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    orgId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, AgeGroupMasterRecord>> {
    return this.http.get<ApiResponse<never, AgeGroupMasterRecord>>(
      `${this.base}/LIS/global/listAgeGroupMaster/${orgId}/${offset}/${noOfRecordsPerPage}`,
    );
  }

  getCount(orgId: number): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(`${this.base}/LIS/global/getToTalAgeGroupMasterRecord/${orgId}`);
  }

  getAgeTypeGroupList(): Observable<ApiResponse<never, AgeTypeGroup>> {
    return this.http.get<ApiResponse<never, AgeTypeGroup>>(`${this.base}/LIS/global/listOfAgeTypeGroup`);
  }

  getById(orgId: number, ageGroupId: number): Observable<ApiResponse<AgeGroupMasterRecord>> {
    return this.http.get<ApiResponse<AgeGroupMasterRecord>>(
      `${this.base}/LIS/global/getAgeGroupById/${orgId}/${ageGroupId}`,
    );
  }

  add(payload: AgeGroupMasterPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/LIS/global/addAgeGroupMaster`, payload);
  }

  update(payload: AgeGroupMasterPayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/LIS/global/updateAgeGroup`, payload);
  }

  changeStatus(orgId: number, ageGroupId: number, status: 'A' | 'I'): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(
      `${this.base}/LIS/global/activeInactiveAgeGroup/${orgId}/${ageGroupId}/${status}`,
    );
  }
}

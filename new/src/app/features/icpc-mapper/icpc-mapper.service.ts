import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  GenderOption,
  IcpcMapperGenderDetail,
  IcpcMapperRow,
  IcpcMapperSavePayload,
  IcpcMapperStatusPayload,
  IcpcMapperUpdatePayload,
  IcpcSystemOption,
} from './icpc-mapper.models';

/**
 * Endpoint paths and methods taken directly from
 * resources/assets/js/controller/master/global/iCPCMapperController.js and
 * resources/assets/js/config/adt.url.config.js:170 (`GETACTIVEGENDERLIST`).
 * Backend chain confirmed via full source read (Controller -> Service ->
 * DAO, all present, no dead endpoints -- see class comment in icpc-mapper.ts):
 *   GET  /adt/getActiveGenderList          -> GenderMasterController.java:100
 *   GET  /api/opd/getICPCSYSTEMList        -> MedicationController.java:293 (shared
 *                                              endpoint, also used by cover-sheet screens)
 *   POST /api/opd/getICPCMapperList        -> ICPCMapperController.java
 *   POST /api/opd/getICPCMapperCount       -> ICPCMapperController.java
 *   POST /api/opd/saveICPCMapper           -> ICPCMapperController.java
 *   POST /api/opd/updateICPCMapper         -> ICPCMapperController.java
 *   POST /api/opd/getICPCMapperById        -> ICPCMapperController.java
 *   POST /api/opd/getGenderListByICPCSystemId -> ICPCMapperController.java
 *   POST /api/opd/updateICPCMapperStatus   -> ICPCMapperController.java
 *
 * `BASE_URL`/`ROOT_URL` are both `''` in this deployment (see
 * url.config.js), so these are the complete paths.
 */
@Injectable({ providedIn: 'root' })
export class IcpcMapperService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getActiveGenderList(): Observable<ApiResponse<never, { id: number; desc: string }>> {
    return this.http.get<ApiResponse<never, { id: number; desc: string }>>(
      `${this.base}/adt/getActiveGenderList`,
    );
  }

  getIcpcSystemList(): Observable<ApiResponse<never, IcpcSystemOption>> {
    return this.http.get<ApiResponse<never, IcpcSystemOption>>(`${this.base}/api/opd/getICPCSYSTEMList`);
  }

  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, IcpcMapperRow>> {
    // unitId is intentionally NOT sent -- legacy's own initMasterList/masterList
    // build this exact payload shape with `unitId` commented out (ctrl:112-117,152-157).
    return this.http.post<ApiResponse<never, IcpcMapperRow>>(`${this.base}/api/opd/getICPCMapperList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number): Observable<ApiResponse<number>> {
    // unitId intentionally not sent -- matches ctrl:119-122.
    return this.http.post<ApiResponse<number>>(`${this.base}/api/opd/getICPCMapperCount`, {
      organizationId,
    });
  }

  save(payload: IcpcMapperSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/opd/saveICPCMapper`, payload);
  }

  update(payload: IcpcMapperUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/opd/updateICPCMapper`, payload);
  }

  getById(
    organizationId: number,
    icpcSystemMapperId: number,
  ): Observable<ApiResponse<never, { icpcSystemId: number; icpcSystemDesc: string; icpcSystemMapperId: number; genderList: IcpcMapperGenderDetail[] }>> {
    return this.http.post<
      ApiResponse<never, { icpcSystemId: number; icpcSystemDesc: string; icpcSystemMapperId: number; genderList: IcpcMapperGenderDetail[] }>
    >(`${this.base}/api/opd/getICPCMapperById`, { organizationId, icpcSystemMapperId });
  }

  getGenderListByIcpcSystemId(
    organizationId: number,
    icpcSystemMapperId: number,
  ): Observable<ApiResponse<never, IcpcMapperGenderDetail>> {
    return this.http.post<ApiResponse<never, IcpcMapperGenderDetail>>(
      `${this.base}/api/opd/getGenderListByICPCSystemId`,
      { icpcSystemMapperId, organizationId },
    );
  }

  updateStatus(payload: IcpcMapperStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/opd/updateICPCMapperStatus`, payload);
  }
}

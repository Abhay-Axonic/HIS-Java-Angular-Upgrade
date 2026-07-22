import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  ModalityOption,
  ModalityRoomOption,
  RoomModalityRecord,
  RoomModalityRow,
  RoomModalityStatusPayload,
  SubSpecialityOption,
} from './room-modality.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/adt/unit/roomModalityController.js and
 * the `adt.url.config.js`/`opd.url.config.js` constants it references
 * (GETSUBSPECIALITYBYSPECIALITYID = "/adt/getSubSpecialityBySpecialityId",
 * GETMODALITYBYSUBSPECIALITYID = API + OPD + "/getModalityBySubSpecialityId"
 * = "/api/opd/getModalityBySubSpecialityId") -- not guessed. All 9 confirmed
 * live during Step 0/1 audit:
 * POST /api/adt/getActiveModalityRoom, POST /adt/getSubSpecialityBySpecialityId,
 * POST /api/opd/getModalityBySubSpecialityId,
 * POST /api/global/saveRoomModalityMasterMaster,
 * POST /api/global/getRoomModalityList,
 * POST /api/global/updateRoomModalityMasterCount (read-only despite the
 * name -- ctrl:212-260 calls it purely to populate `commonListCount`, never
 * writes anything),
 * GET /api/global/getRoomModalityById/{id}/{orgId}/{unitId},
 * POST /api/global/updateRoomModalityMasterStatus,
 * POST /api/global/updateRoomModalityMaster.
 */
@Injectable({ providedIn: 'root' })
export class RoomModalityService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getActiveModalityRoom(organizationId: number, unitId: number): Observable<ApiResponse<never, ModalityRoomOption>> {
    return this.http.post<ApiResponse<never, ModalityRoomOption>>(`${this.base}/api/adt/getActiveModalityRoom`, {
      unitId,
      organizationId,
    });
  }

  getSubSpecialityBySpecialityId(
    organizationId: number,
    unitId: number,
    specialityId: number,
  ): Observable<ApiResponse<never, SubSpecialityOption>> {
    return this.http.post<ApiResponse<never, SubSpecialityOption>>(`${this.base}/adt/getSubSpecialityBySpecialityId`, {
      organizationId,
      unitId,
      specialityId,
    });
  }

  getModalityBySubSpecialityId(
    organizationId: number,
    unitId: number,
    specialityId: number,
    subSpecialityId: number,
  ): Observable<ApiResponse<never, ModalityOption>> {
    return this.http.post<ApiResponse<never, ModalityOption>>(`${this.base}/api/opd/getModalityBySubSpecialityId`, {
      organizationId,
      unitId,
      specialityId,
      subSpecialityId,
    });
  }

  saveRoomModalityMasterMaster(record: RoomModalityRecord): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveRoomModalityMasterMaster`, record);
  }

  getRoomModalityList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, RoomModalityRow>> {
    return this.http.post<ApiResponse<never, RoomModalityRow>>(`${this.base}/api/global/getRoomModalityList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  /** Despite the "update" in its name this is read-only -- ctrl:212-260
   * calls it purely to populate the pager's total record count. */
  getRoomModalityCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/global/updateRoomModalityMasterCount`, {
      unitId,
      organizationId,
    });
  }

  getRoomModalityById(id: number, organizationId: number, unitId: number): Observable<ApiResponse<never, RoomModalityRecord>> {
    return this.http.get<ApiResponse<never, RoomModalityRecord>>(
      `${this.base}/api/global/getRoomModalityById/${id}/${organizationId}/${unitId}`,
    );
  }

  updateRoomModalityMasterStatus(payload: RoomModalityStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateRoomModalityMasterStatus`, payload);
  }

  updateRoomModalityMaster(record: RoomModalityRecord): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateRoomModalityMaster`, record);
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  BedCategoryOption,
  BillingBedCategoryOption,
  RoomMasterDetail,
  RoomMasterRow,
  RoomMasterSavePayload,
  RoomMasterStatusPayload,
  RoomMasterUpdatePayload,
  WardOption,
} from './room-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/adt/unit/roomMasterController.js and the
 * `adt.url.config.js` constants it references (GETACTIVEBEDCATEGORYLISTBYUNIT,
 * GETACTIVEWARDMASTERLIST, GETACTIVEBILLINGBEDCATEGORYBYBEDCATEGORYID,
 * GETROOMMASTERLIST, GETROOMMASTERCOUNT, GETROOMMASTERBYID, SAVEROOMMASTER,
 * UPDATEROOMMASTER, UPDATEROOMMASTERSTATUS) -- not guessed. Full
 * Controller -> Service -> DAO chain confirmed for every endpoint below:
 * `BedAllocationController`/`WardMasterController`/`BillingBedCategoryMasterController`
 * (all `@RequestMapping("/adt")` or `("api/adt")`, legacy/ADT module) and
 * `RoomMasterController` (`@RequestMapping("api/adt")`,
 * `RoomMasterServiceImpl`/`RoomMasterDaoImpl`). Golden-trace-confirmed
 * (tools/golden-trace/golden-traces/roomMaster.json): exactly these 4 calls
 * fire on initial load (getBedCategoryListByOrgUnit, getActiveWardMasterList,
 * getRoomMasterList, getRoomMasterCount), matching source exactly.
 */
@Injectable({ providedIn: 'root' })
export class RoomMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getBedCategoryListByOrgUnit(organizationId: number, unitId: number): Observable<ApiResponse<never, BedCategoryOption>> {
    return this.http.post<ApiResponse<never, BedCategoryOption>>(`${this.base}/adt/getBedCategoryListByOrgUnit`, {
      organizationId,
      unitId,
    });
  }

  getActiveWardMasterList(organizationId: number, unitId: number): Observable<ApiResponse<never, WardOption>> {
    return this.http.post<ApiResponse<never, WardOption>>(`${this.base}/api/adt/getActiveWardMasterList`, {
      organizationId,
      unitId,
    });
  }

  getBillingBedCategoryListByBedCategory(
    organizationId: number,
    bedCategoryId: number,
  ): Observable<ApiResponse<never, BillingBedCategoryOption>> {
    return this.http.post<ApiResponse<never, BillingBedCategoryOption>>(
      `${this.base}/adt/getBillingBedCategoryListByBedCategory`,
      { organizationId, bedCategoryId },
    );
  }

  getRoomMasterList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, RoomMasterRow>> {
    return this.http.post<ApiResponse<never, RoomMasterRow>>(`${this.base}/api/adt/getRoomMasterList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getRoomMasterCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/adt/getRoomMasterCount`, {
      organizationId,
      unitId,
    });
  }

  getRoomMasterById(roomId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, RoomMasterDetail>> {
    return this.http.get<ApiResponse<never, RoomMasterDetail>>(
      `${this.base}/api/adt/getRoomMasterById/${roomId}/${organizationId}/${unitId}`,
    );
  }

  saveRoomMaster(payload: RoomMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/adt/saveRoomMaster`, payload);
  }

  updateRoomMaster(payload: RoomMasterUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/adt/updateRoomMaster`, payload);
  }

  updateRoomMasterStatus(payload: RoomMasterStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/adt/updateRoomMasterStatus`, payload);
  }
}

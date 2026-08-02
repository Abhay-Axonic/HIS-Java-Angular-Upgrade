import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  BedCategoryOption,
  BedMasterDetail,
  BedMasterRow,
  BedMasterSavePayload,
  BedMasterUpdatePayload,
  BedStatusPayload,
  DialysisAssetOption,
  MovingBedDetail,
  MovingBedRow,
  MovingBedSavePayload,
  MovingBedUpdatePayload,
  RoomOption,
  WardOption,
} from './bed-master.models';

/**
 * Endpoint paths taken directly from `bedMasterController.js` and the
 * literal string constants it references in
 * `resources/assets/js/config/adt.url.config.js`:
 *   GETACTIVEWARDMASTERLIST  = /api/adt/getActiveWardMasterList  (config:631)
 *   GETROOMBYWARDID          = /api/adt/getRoomByWardId          (config:646)
 *   ADDBED                   = /api/adt/addBed                   (config:656)
 *   BEDLIST                  = /api/adt/bedList                  (config:657)
 *   UPDATEBED                = /api/adt/updateBed                (config:658)
 *   GETBEDBYID               = /api/adt/bed                      (config:659, + "/"+id)
 *   UPDATESTATUS             = /api/adt/updateStatus              (config:660)
 *   BEDLISTCOUNT             = /api/adt/bedListCount              (config:661)
 *   GETACTIVEBEDCATEGORYLIST = /adt/getActiveBedCategoryList      (config:282, GET, no params)
 *   GETACTIVEBILLINGBEDCATEGORYBYBEDCATEGORYID
 *                            = /adt/getBillingBedCategoryListByBedCategory (config:340)
 * Plus 4 more that aren't `var X = "..."` constants but literal inline
 * strings in the controller itself (confirmed via full-file read):
 *   "/api/adt/bedMovingBedList"           (ctrl:648-649)
 *   "/api/adt/movingBedListCount"         (ctrl:650-651)
 *   "/api/opd/getAsset"                   (ctrl:416-417)
 *   "/api/adt/bedMasterUniversalSearch"   (ctrl:956/983)
 *
 * `BASE_URL`/`ROOT_URL` are both `''` in this deployment (same as every
 * other rebuilt screen in this codebase) -- these are the complete paths.
 * Full Controller -> Service -> DAO chain confirmed for every endpoint below
 * (`BedMasterController.java` @RequestMapping("api/adt"),
 * `BedMasterServiceImpl`/`BedMasterDaoImpl`; ward/bed-category/billing-
 * bed-category lookups live in `WardMasterDaoImpl`/`BedCategoryMasterDaoImpl`
 * (Global module)/`BillingBedCategoryMasterController`). Golden-trace
 * confirmed live 2026-07-29 against real, unmodified legacy -- see
 * docs/ANGULAR_PENDING_FINDINGS.md for the full audit (2 SEVERITY-1s: `addBed`
 * fails on every attempt for both tabs; Moving Bed's `updateBed` silently
 * no-ops despite a "success" response).
 */
@Injectable({ providedIn: 'root' })
export class BedMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ---- shared lookups (ctrl:73-108, init()) ----

  getActiveWardMasterList(organizationId: number, unitId: number): Observable<ApiResponse<never, WardOption>> {
    return this.http.post<ApiResponse<never, WardOption>>(`${this.base}/api/adt/getActiveWardMasterList`, {
      unitId,
      organizationId,
    });
  }

  // GET, no org/unit scoping at all -- matches legacy exactly (NOT the same
  // endpoint roomMaster uses).
  getActiveBedCategoryList(): Observable<ApiResponse<never, BedCategoryOption>> {
    return this.http.get<ApiResponse<never, BedCategoryOption>>(`${this.base}/adt/getActiveBedCategoryList`);
  }

  // ctrl:233-286 (getWardId) -- fires on Ward change.
  getRoomByWardId(unitId: number, organizationId: number, wardId: number): Observable<ApiResponse<never, RoomOption>> {
    return this.http.post<ApiResponse<never, RoomOption>>(`${this.base}/api/adt/getRoomByWardId`, {
      unitId,
      organizationId,
      wardId,
    });
  }

  // ctrl:413-430 (dialysischair) -- fired unconditionally on load, only
  // rendered/used when a hemodialysis ward is selected.
  getDialysisChair(): Observable<ApiResponse<never, DialysisAssetOption>> {
    return this.http.get<ApiResponse<never, DialysisAssetOption>>(`${this.base}/api/opd/getAsset`);
  }

  // ctrl:289-309 (getBedCategory) -- ONLY reachable from the edit path
  // (showUpdateBtn), never from create (see bed-master.models.ts doc).
  getBillingBedCategoryByBedCategoryId(
    organizationId: number,
    bedCategoryId: number,
  ): Observable<ApiResponse<never, RoomOption>> {
    return this.http.post<ApiResponse<never, RoomOption>>(`${this.base}/adt/getBillingBedCategoryListByBedCategory`, {
      organizationId,
      bedCategoryId,
    });
  }

  // ---- Normal Bed tab (ctrl:115-227/327-490/561-625) ----

  getBedList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, BedMasterRow>> {
    return this.http.post<ApiResponse<never, BedMasterRow>>(`${this.base}/api/adt/bedList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:160-172 -- payload deliberately has NO isMovingBed filter, matching
  // legacy's own count query (counts Moving Beds/room-less rows too -- see
  // class doc, faithfully reproduced).
  getBedListCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/adt/bedListCount`, {
      organizationId,
      unitId,
    });
  }

  // SEVERITY-1: fails on every real attempt today (schema-drift crash in
  // getConfiguredBedLimit) -- wired to the real endpoint anyway, see class doc.
  addBed(payload: BedMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/adt/addBed`, payload);
  }

  updateBed(payload: BedMasterUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/adt/updateBed`, payload);
  }

  // ctrl:432-490 (showUpdateBtn) -- a 409 means the bed is currently in-use;
  // the caller must faithfully show "Bed is used, you can't change status."
  // `code` isn't on the shared ApiResponse envelope -- intersected locally,
  // same pattern as this codebase's specialityMaster/insuranceMaster 409s.
  getBedById(bedId: number): Observable<ApiResponse<never, BedMasterDetail> & { code?: string }> {
    return this.http.get<ApiResponse<never, BedMasterDetail> & { code?: string }>(`${this.base}/api/adt/bed/${bedId}`);
  }

  // ---- Moving Bed tab (ctrl:630-751/756-816/819-919) ----

  getMovingBedList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, MovingBedRow>> {
    return this.http.post<ApiResponse<never, MovingBedRow>>(`${this.base}/api/adt/bedMovingBedList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // Confirmed live: ALWAYS returns object:0 (controller never sends
  // isMovingBed in this payload, DAO's Restrictions.eq(null) never matches
  // any row) -- faithfully reproduced, not fixed. See class doc.
  getMovingBedListCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/adt/movingBedListCount`, {
      unitId,
      organizationId,
    });
  }

  // SEVERITY-1: same broken addBed endpoint as Normal Bed -- see class doc.
  addMovingBed(payload: MovingBedSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/adt/addBed`, payload);
  }

  // SEVERITY-1: returns a real "success" response but silently never writes
  // to the DB -- see class doc.
  updateMovingBed(payload: MovingBedUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/adt/updateBed`, payload);
  }

  getMovingBedById(bedId: number): Observable<ApiResponse<never, MovingBedDetail> & { code?: string }> {
    return this.http.get<ApiResponse<never, MovingBedDetail> & { code?: string }>(`${this.base}/api/adt/bed/${bedId}`);
  }

  // ---- shared across both tabs ----

  // ctrl:493-542 (updateStatus) -- confirmed silently corrupts updated_date
  // on the backend (date-format-pattern mismatch), faithfully reproduced. A
  // 409 means the bed is currently in-use -- see class doc.
  updateStatus(payload: BedStatusPayload): Observable<ApiResponse & { code?: string }> {
    return this.http.post<ApiResponse & { code?: string }>(`${this.base}/api/adt/updateStatus`, payload);
  }

  // ctrl:945-997 (searchBedMaster/searchMovingBedMaster) -- real server-side
  // full-text search (bedCode/bedDesc/bedNumber/earmarkingDesc/wardName/
  // roomDesc/bedCategoryDesc/billingBedDesc), NOT a client-side filter.
  searchBedMaster(
    organizationId: number,
    unitId: number,
    bedDesc: string,
    isMovingBed: 'Y' | 'N',
  ): Observable<ApiResponse<never, BedMasterRow | MovingBedRow>> {
    return this.http.post<ApiResponse<never, BedMasterRow | MovingBedRow>>(`${this.base}/api/adt/bedMasterUniversalSearch`, {
      organizationId,
      bedDesc,
      unitId,
      isMovingBed,
    });
  }
}

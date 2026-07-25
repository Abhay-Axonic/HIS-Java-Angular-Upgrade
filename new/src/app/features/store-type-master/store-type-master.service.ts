import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  StoreTypeDetail,
  StoreTypeRow,
  StoreTypeSavePayload,
  StoreTypeStatusPayload,
  StoreTypeUpdatePayload,
} from './store-type-master.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/controller/master/storeTypeController.js` (all 6
 * build `BASE_URL + ROOT_URL + "/api/global/..."` literals in-line -- NOT
 * `/api/global/unit/...` like `discountCategoryMaster`'s sibling
 * controller). All 6 confirmed against a real Java implementation, end to
 * end: `StoreTypeMasterController.java` (`@RequestMapping("/api/global")`)
 * -> `StoreTypeMasterServiceImpl` -> `StoreTypeMasterDaoImpl` -> table
 * `public.m_store_type`. Confirmed live against the running dev backend
 * (`getStoreTypeList`/`getStoreTypeCount` both 200, real 6-row seed data;
 * `getStoreTypeById`/`updateStoreType`/`updateStoreTypeStatus`/duplicate
 * check all exercised live and reverted -- see this screen's class doc
 * comment for the full Step 0/2 verification).
 *
 * ================================================================
 * CONFIRMED BACKEND DEFECT, NOT FIXED HERE (legacy bug, logged, not a
 * rebuild bug) -- list/byId/duplicate-check are ORG-scoped only,
 * getCount is ORG+UNIT-scoped -- a real scope mismatch
 * ================================================================
 * `StoreTypeMaster.java`'s named queries (`GET_STORE_TYPE_MASTER_LIST`,
 * `GET_STORE_TYPE_BY_ID`, `GET_STORE_TYPE_ALREADY_EXIST_OR_NOT[_BY_ID]`)
 * all filter `WHERE mstr.organizationId=:orgId` only -- each query's own
 * `unitId` clause is present in source but commented out (e.g.
 * `//+ " AND mstr.unitId=:unitId"`). `getStoreTypeCount`
 * (`StoreTypeMasterDaoImpl.java:126-141`), by contrast, uses a Hibernate
 * `Criteria` that DOES filter on both `organizationId` AND `unitId`. Net
 * effect: the list/by-id/duplicate-check operations are scoped to the
 * whole ORGANIZATION (every unit), while the paginator's own total count
 * is scoped to the CURRENT UNIT only -- a real, live scope mismatch
 * between what the list can return and what the count says should be
 * there, the same defect *shape* (list/count disagreeing) as
 * `discountCategoryMaster`'s confirmed active-only-count bug (entry 159),
 * though the specific axis here is unit-scope, not active-flag-scope. In
 * an environment with more than one unit under the same organization,
 * this could both under/over-report pagination AND let a save/update's
 * duplicate check see (or fail to see) a row that actually belongs to a
 * different unit. Not exercised further live here since the local dev DB
 * only has one seeded unit (org 1 / unit 1) -- confirmed via source
 * reading, not fabricated multi-unit data, per this project's no-
 * unrequested-data-fabrication discipline. Reproduced as-is; the frontend
 * has no way to compensate for a server-side scope decision. Flagged in
 * this batch's findings entry.
 */
@Injectable({ providedIn: 'root' })
export class StoreTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:67-107 (initStoreTypeMasterList) -- POST {organizationId, unitId,
  // offset, noOfRecordsPerPage}.
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, StoreTypeRow>> {
    return this.http.post<ApiResponse<never, StoreTypeRow>>(`${this.base}/api/global/getStoreTypeList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:81-84 (same function, the parallel count call) -- POST
  // {organizationId, unitId}; count returned in `object`. See class doc
  // comment: this call IS unit-scoped, unlike getList.
  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/global/getStoreTypeCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:153-196 (save). The backend's own duplicate check
  // (getStoreTypeAlreadyExistOrNot) is scoped by organizationId +
  // lower(code) + lower(type) -- see class doc comment: NOT unit-scoped,
  // despite unitId being sent on every payload.
  save(payload: StoreTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveStoreType`, payload);
  }

  // ctrl:198-229 (showUpdateBtn) -- POST with only {storeTypeId,
  // organizationId, unitId} in the body (ctrl:203-207), NOT a GET with
  // path params.
  getById(storeTypeId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, StoreTypeDetail>> {
    return this.http.post<ApiResponse<never, StoreTypeDetail>>(`${this.base}/api/global/getStoreTypeById`, {
      storeTypeId,
      organizationId,
      unitId,
    });
  }

  // ctrl:232-271 (update). Same wrong-...no, same ORG-only-scoped
  // duplicate check applies here too (getStoreTypeAlreadyExistOrNotById).
  update(payload: StoreTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateStoreType`, payload);
  }

  // ctrl:273-306 (updateStatus) -- POST, real status toggle. Confirmed
  // live the server sets `updated_by` from the authenticated session
  // (`sessionService.getUserId()`), NOT from this payload's `updatedBy`
  // -- see class doc comment on `StoreTypeStatusPayload`.
  updateStatus(payload: StoreTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateStoreTypeStatus`, payload);
  }
}

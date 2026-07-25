import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  HospitalAccountDetail,
  HospitalAccountRow,
  HospitalAccountSavePayload,
  HospitalAccountStatusPayload,
  HospitalAccountUpdatePayload,
} from './hospital-account-master.models';

/**
 * Endpoint paths confirmed real end-to-end this session (Step 0/1/2):
 * `HospitalAccountMasterController.java` (`@RequestMapping("api/billing")`)
 * -> `HospitalAccountMasterServiceImpl` -> `HospitalAccountMasterDaoImpl` ->
 * table `billing.m_hospital_accounts`. A fresh golden trace confirmed the
 * list+count calls both return live 200s in parallel on load (unlike some
 * sibling batch-18 screens whose count endpoint 500s) -- this rebuild's own
 * `loadListAndCount()` matches that same 2-call, unchained shape.
 */
@Injectable({ providedIn: 'root' })
export class HospitalAccountMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:67-105 (initHospitalAccountMasterList, data1) -- POST
  // { orgnizationId, unitId, offset, noOfRecordsPerPage }.
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, HospitalAccountRow>> {
    return this.http.post<ApiResponse<never, HospitalAccountRow>>(`${this.base}/api/billing/getHospitalAccountMasterList`, {
      orgnizationId: organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:67-105 (data2) -- POST { orgnizationId } ONLY. Confirmed by
  // reading both the controller's own `data2` object literal AND the DAO's
  // `getHospitalAccountMasterCount` (HospitalAccountMasterDaoImpl.java:
  // 195-211, `Restrictions.eq("orgnizationId", ...)` alone, no unitId
  // criterion at all) -- a real, org-only count scope, not a rebuild gap.
  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/billing/getHospitalAccountMasterCount`, {
      orgnizationId: organizationId,
    });
  }

  // ctrl:107-126 (`$scope.PriorityMasterList`, called from `setPage(page,
  // true)`) -- same real endpoint/shape as getList() above, just a
  // copy-paste-artifact function name in legacy (see this screen's .ts
  // class doc comment) -- NOT reproduced under that literal name since,
  // unlike this codebase's bank-master sibling, this function correctly
  // writes its response into the same variable the table renders (no dead-
  // write bug here to preserve the distinct name for).
  getPage(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, HospitalAccountRow>> {
    return this.getList(organizationId, unitId, offset, noOfRecordsPerPage);
  }

  // ctrl:152-212 (save(), the real-POST branch once popUpFlag is false).
  save(payload: HospitalAccountSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/saveHospitalAccountMaster`, payload);
  }

  // ctrl:214-242 (showUpdateBtn) -- GET, hospitalAccountId/orgId/unitId as
  // path segments; response.data.object is the record (NOT listObject[0] --
  // confirmed by reading HospitalAccountMasterDaoImpl.getHospitalAccountMasterById,
  // which returns via the Response's 5th constructor arg, the singular
  // `object` slot, not `listObject`).
  getById(hospitalAccountId: number, organizationId: number, unitId: number): Observable<ApiResponse<HospitalAccountDetail>> {
    return this.http.get<ApiResponse<HospitalAccountDetail>>(
      `${this.base}/api/billing/getHospitalAccountMasterById/${hospitalAccountId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:245-294 (update()) -- POST, real update, no confirm modal.
  update(payload: HospitalAccountUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updateHospitalAccountMaster`, payload);
  }

  // ctrl:296-331 (updateStatus()) -- POST, real status toggle.
  updateStatus(payload: HospitalAccountStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updateHospitalAccountMasterStatus`, payload);
  }
}

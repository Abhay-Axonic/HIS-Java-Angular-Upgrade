import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  EquipmentOption,
  ModalityByIdRecord,
  ModalityListRow,
  SpecialityOption,
  SubSpecialityOption,
} from './modality-master.models';

/**
 * Endpoint paths read directly from
 * resources/assets/js/controller/opd/unit/modalityMasterController.js and
 * the constants it references (adt.url.config.js / opd.url.config.js).
 * All 9 confirmed against a real Java Controller -> Service -> DAO chain
 * during this batch's Step 0 backend-existence check (see
 * docs/ANGULAR_PENDING_FINDINGS.md, modalityMaster entry) -- unlike its
 * sibling `modalityTypeMaster` (entry 67), this screen's backend is real.
 *
 * `authInterceptor` (new/src/app/core/auth.interceptor.ts) is already
 * wired globally -- every HttpClient call here gets the Authorization/
 * userId/unitId headers automatically.
 */
@Injectable({ providedIn: 'root' })
export class ModalityMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** `GETACTIVESPECIALITY` = `/adt/getActiveSpecialityList` (ctrl:84-97). */
  getActiveSpecialityList(unitId: number, organizationId: number): Observable<ApiResponse<never, SpecialityOption>> {
    return this.http.post<ApiResponse<never, SpecialityOption>>(`${this.base}/adt/getActiveSpecialityList`, {
      unitId,
      organizationId,
    });
  }

  /** `GETSUBSPECIALITYBYSPECIALITYID` = `/adt/getSubSpecialityBySpecialityId` (ctrl:119-141). */
  getSubSpecialityBySpecialityId(
    unitId: number,
    organizationId: number,
    specialityId: number,
  ): Observable<ApiResponse<never, SubSpecialityOption>> {
    return this.http.post<ApiResponse<never, SubSpecialityOption>>(
      `${this.base}/adt/getSubSpecialityBySpecialityId`,
      { unitId, organizationId, specialityId },
    );
  }

  /**
   * Legacy builds this URI as `BASE_URL + ROOT_URL + '\t/api/opd/equipments/'+unitId`
   * (ctrl:100-101) -- a literal stray TAB character before `/api`, confirmed
   * via `od -c` on the source file. Per the WHATWG URL parsing spec (which
   * every modern browser's XHR/fetch implements), ASCII tab/newline
   * characters are stripped during URL parsing, so the actual request sent
   * by legacy is identical to the clean path below -- the tab has zero
   * observable effect on the wire. Not reproduced literally (there is
   * nothing to reproduce: no different byte ever reaches the network).
   */
  getEquipments(unitId: number): Observable<ApiResponse<never, EquipmentOption>> {
    return this.http.get<ApiResponse<never, EquipmentOption>>(`${this.base}/api/opd/equipments/${unitId}`);
  }

  /** `GETMODALITYMASTERLIST` = `/api/opd/getModalityMasterList` (ctrl:160-187). */
  getModalityMasterList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, ModalityListRow>> {
    return this.http.post<ApiResponse<never, ModalityListRow>>(`${this.base}/api/opd/getModalityMasterList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  /** `GETMODALITYCOUNT` = `/api/opd/getModalityCount` (ctrl:161-203). */
  getModalityCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/opd/getModalityCount`, {
      organizationId,
      unitId,
    });
  }

  /** `ADDMODALITYMASTER` = `/api/opd/saveModalityMaster` (ctrl:279-311). */
  saveModality(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/opd/saveModalityMaster`, payload);
  }

  /** `GETMODALITYBYID` = `/api/opd/getModalityById` (ctrl:331-349). */
  getModalityById(
    modalityId: number,
    unitId: number,
    organizationId: number,
  ): Observable<ApiResponse<never, ModalityByIdRecord>> {
    return this.http.post<ApiResponse<never, ModalityByIdRecord>>(`${this.base}/api/opd/getModalityById`, {
      modalityId,
      unitId,
      organizationId,
    });
  }

  /** `UPDATESTATUSFORMODALITY` = `/api/opd/updateStatusForModality` (ctrl:354-394). */
  updateStatusForModality(payload: {
    modalityId: number;
    status: 'A' | 'I';
    updatedBy: number;
    updatedDate: string;
    unitId: number;
    organizationId: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/opd/updateStatusForModality`, payload);
  }

  /** `UPDATEMODALITYMASTER` = `/api/opd/updateModalityMaster` (ctrl:396-442). */
  updateModalityMaster(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/opd/updateModalityMaster`, payload);
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  DepartmentOption,
  GeneralLedgerOption,
  SubSpecialityDetail,
  SubSpecialityRow,
  SubSpecialitySavePayload,
  SubSpecialityStatusPayload,
  SubSpecialityUpdatePayload,
} from './sub-speciality-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/subSpecialityMasterController.js and
 * resources/assets/js/config/adt.url.config.js / global.url.config.js --
 * BASE_URL/ROOT_URL are both "" in this deployment (url.config.js), so these
 * are the literal relative paths legacy calls.
 */
@Injectable({ providedIn: 'root' })
export class SubSpecialityMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:59-75 -- GET, parent-department dropdown, path segment is organizationId.
  getDepartments(organizationId: number): Observable<ApiResponse<never, DepartmentOption>> {
    return this.http.get<ApiResponse<never, DepartmentOption>>(
      `${this.base}/adt/getSpecialityListForSubSpeciality/${organizationId}`,
    );
  }

  // ctrl:120-154 (GETSUBSPECIALITYLIST) -- POST { organizationId, offset, noOfRecordsPerPage }.
  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, SubSpecialityRow>> {
    return this.http.post<ApiResponse<never, SubSpecialityRow>>(`${this.base}/adt/getSubSpecialityList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:130-169 (COUNTSUBSPECIALITYMASTER) -- POST { organizationId }, count returned in `object`.
  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getSubSpecialityCount`, { organizationId });
  }

  // ctrl:241-347 (ADDSUBSPECIALITY) -- POST, real create.
  save(payload: SubSpecialitySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveSubSpecialtiyMaster`, payload);
  }

  // ctrl:352-387 (GETSINGLESUBSPECIALITY) -- POST { subSpecialityMasterId };
  // response.data.listObject[0] is the record.
  getById(subSpecialityMasterId: number): Observable<ApiResponse<never, SubSpecialityDetail>> {
    return this.http.post<ApiResponse<never, SubSpecialityDetail>>(`${this.base}/adt/getSubSpecialityById`, {
      subSpecialityMasterId,
    });
  }

  // ctrl:433-494 (UPDATESUBSPECIALITY) -- POST, real update, no `status` field (create-only).
  update(payload: SubSpecialityUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateSubSpecialityMaster`, payload);
  }

  // ctrl:390-430 (UPDATESUBSPECIALITYSTATUS) -- POST, real status toggle.
  updateStatus(payload: SubSpecialityStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForSubSpeciality`, payload);
  }

  // ctrl:495-512 -- POST { generalLedgerName: keyword } to the org-scoped
  // active-ledger list; used as the typeahead source (`uib-typeahead` in
  // legacy). organizationId is a path segment, matching legacy's own
  // string-concatenated URL.
  searchLedgers(organizationId: number, keyword: string): Observable<ApiResponse<never, GeneralLedgerOption>> {
    return this.http.post<ApiResponse<never, GeneralLedgerOption>>(
      `${this.base}/org/getGeneralLedgerMasterActiveList/${organizationId}`,
      { generalLedgerName: keyword },
    );
  }

  // ctrl:517-543 -- POST, server-side universal search. CONFIRMED LIVE (not
  // a dead/copy-pasted binding despite the typo'd name matching the known
  // "serachXMaster" bug family found elsewhere in this codebase -- verified
  // by reading the full controller file: this function IS defined and IS
  // wired to the view's live search input).
  universalSearch(
    organizationId: number,
    subSpecialityMasterName: string,
  ): Observable<ApiResponse<never, SubSpecialityRow>> {
    return this.http.post<ApiResponse<never, SubSpecialityRow>>(`${this.base}/adt/subSpecialityMasterUniversalSearch`, {
      organizationId,
      subSpecialityMasterName,
    });
  }
}

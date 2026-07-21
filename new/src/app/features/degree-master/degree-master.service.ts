import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { DegreeMasterDetail, DegreeMasterListRow } from './degree-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/global/common/degreeMasterController.js
 * and resources/assets/js/config/global.url.config.js:123-129 -- not guessed:
 *  - GETDEGREEMASTERLIST      = /api/common/getDegreeMasterList      (POST, ctrl:88-89)
 *  - GETDEGREEMASTERCOUNT     = /api/common/getDegreeMasterCount     (POST, ctrl:90-92 -- a
 *    dead, commented-out sibling line at global.url.config.js:128 points at
 *    getQualificationMasterCount by mistake; the live line 129 above is the
 *    one actually used)
 *  - SAVEDEGREEMASTER         = /api/common/saveDegreeMaster         (POST, ctrl:221-222)
 *  - GETDEGREEMASTERBYID      = /api/common/getDegreeMasterById      (GET, path params
 *    id/organizationId -- ctrl:265-267, the one call NOT shaped like the rest)
 *  - UPDATEDEGREEMASTERSTATUS = /api/common/updateDegreeMasterStatus (POST, ctrl:298-299)
 *  - UPDATEDEGREEMASTER       = /api/common/updateDegreeMaster       (POST, ctrl:340-341)
 * BASE_URL/ROOT_URL are both "" (resources/assets/js/config/url.config.js:13,19)
 * -- same-origin relative paths, nothing to prefix beyond API_BASE_URL.
 * moduleId 497 (config/app.js:14201-14224), no controllerAs.
 */
@Injectable({ providedIn: 'root' })
export class DegreeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, DegreeMasterListRow>> {
    return this.http.post<ApiResponse<never, DegreeMasterListRow>>(`${this.base}/api/common/getDegreeMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/common/getDegreeMasterCount`, { organizationId });
  }

  create(payload: {
    degreeCode: string;
    degreeDesc: string;
    status: 'A';
    createdBy: number;
    updatedBy: number;
    organizationId: number;
    createdDate: string;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/saveDegreeMaster`, payload);
  }

  /** GET with path params `id/organizationId` (ctrl:265-267) -- not a body
   * POST like every other call this controller makes. */
  getById(degreeId: number, organizationId: number): Observable<ApiResponse<never, DegreeMasterDetail>> {
    return this.http.get<ApiResponse<never, DegreeMasterDetail>>(
      `${this.base}/api/common/getDegreeMasterById/${degreeId}/${organizationId}`,
    );
  }

  update(payload: {
    degreeCode: string;
    degreeDesc: string;
    degreeId: number;
    updatedBy: number;
    updatedDate: string;
    organizationId: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateDegreeMaster`, payload);
  }

  updateStatus(payload: {
    degreeId: number;
    status: 'A' | 'I';
    updatedBy: number;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateDegreeMasterStatus`, payload);
  }
}

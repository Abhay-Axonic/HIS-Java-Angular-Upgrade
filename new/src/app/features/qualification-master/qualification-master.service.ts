import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  QualificationDetail,
  QualificationRow,
  QualificationSavePayload,
  QualificationStatusPayload,
  QualificationUpdatePayload,
} from './qualification-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/global/common/qualificationMasterController.js
 * and their literal string values in
 * resources/assets/js/config/global.url.config.js:116-121:
 *   SAVEQUALIFICATIONMASTER         = /api/common/saveQualificationMaster        (ctrl:219-220)
 *   GETQUALIFICATIONMASTERBYID      = /api/common/getQualificationMasterById     (ctrl:263-265)
 *   GETQUALIFICATIONMASTERLIST      = /api/common/getQualificationMasterList     (ctrl:87-88, 143-144)
 *   UPDATEQUALIFICATIONMASTER       = /api/common/updateQualificationMaster       (ctrl:338-339)
 *   UPDATEQUALIFICATIONMASTERSTATUS = /api/common/updateQualificationMasterStatus (ctrl:296-297)
 *   GETQUALIFICATIONMASTERCOUNT     = /api/common/getQualificationMasterCount     (ctrl:89-90)
 * `BASE_URL`/`ROOT_URL` (url.config.js) are both `''` in this deployment, so
 * these are the complete paths. List/count/save/update/status calls are all
 * POST in legacy (`GenericService.serviceAction("POST", ...)`); only the
 * by-id lookup is a GET with the id/organizationId in the path, matching
 * legacy's own `GenericService.serviceAction("GET", URI, data)` call with an
 * empty body.
 */
@Injectable({ providedIn: 'root' })
export class QualificationMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, QualificationRow>> {
    return this.http.post<ApiResponse<never, QualificationRow>>(`${this.base}/api/common/getQualificationMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/common/getQualificationMasterCount`, { organizationId });
  }

  save(payload: QualificationSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/saveQualificationMaster`, payload);
  }

  getById(id: number, organizationId: number): Observable<ApiResponse<never, QualificationDetail>> {
    return this.http.get<ApiResponse<never, QualificationDetail>>(
      `${this.base}/api/common/getQualificationMasterById/${id}/${organizationId}`,
    );
  }

  updateStatus(payload: QualificationStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateQualificationMasterStatus`, payload);
  }

  update(payload: QualificationUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateQualificationMaster`, payload);
  }
}

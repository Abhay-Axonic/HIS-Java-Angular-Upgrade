import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  DesignationMasterDetail,
  DesignationMasterRow,
  DesignationMasterSavePayload,
  DesignationMasterStatusPayload,
  DesignationMasterUpdatePayload,
} from './designation-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/global/common/designationMasterController.js
 * and their literal string values in resources/assets/js/config/global.url.config.js
 * (GETDESIGNATIONMASTERLIST/GETDESIGNATIONMASTERCOUNT/SAVEDESIGNATIONMASTER/
 * GETDESIGNATIONMASTERBYID/UPDATEDESIGNATIONMASTER/UPDATEDESIGNATIONMASTERSTATUS
 * -- all confirmed against the Java controller,
 * com.param.global.org.common.controller.DesignationMasterController, which
 * maps them under `api/common`):
 *   GETDESIGNATIONMASTERLIST        = /api/common/getDesignationMasterList        (POST, ctrl:86-116)
 *   GETDESIGNATIONMASTERCOUNT       = /api/common/getDesignationMasterCount       (POST, ctrl:88-132 --
 *     the Java method backing this is internally named `getDegreeMasterCount`, a
 *     cosmetic copy-paste leftover that does not change the actual mapped path)
 *   SAVEDESIGNATIONMASTER           = /api/common/saveDesignationMaster           (POST, ctrl:218-247)
 *   GETDESIGNATIONMASTERBYID        = /api/common/getDesignationMasterById        (GET, path params
 *     designationId/orgId -- ctrl:262-277)
 *   UPDATEDESIGNATIONMASTER         = /api/common/updateDesignationMaster         (POST, ctrl:337-366)
 *   UPDATEDESIGNATIONMASTERSTATUS   = /api/common/updateDesignationMasterStatus   (POST, ctrl:295-318)
 * `BASE_URL`/`ROOT_URL` (url.config.js) are both `''` in this deployment, so
 * these are the complete paths. This screen is org-level only -- no unitId
 * anywhere in this controller or its endpoints.
 */
@Injectable({ providedIn: 'root' })
export class DesignationMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, DesignationMasterRow>> {
    return this.http.post<ApiResponse<never, DesignationMasterRow>>(`${this.base}/api/common/getDesignationMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/common/getDesignationMasterCount`, { organizationId });
  }

  save(payload: DesignationMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/saveDesignationMaster`, payload);
  }

  /** GET with path params `designationId/orgId` (ctrl:262-264) -- not a body
   * POST like every other call this controller makes. */
  getById(designationId: number, orgId: number): Observable<ApiResponse<never, DesignationMasterDetail>> {
    return this.http.get<ApiResponse<never, DesignationMasterDetail>>(
      `${this.base}/api/common/getDesignationMasterById/${designationId}/${orgId}`,
    );
  }

  update(payload: DesignationMasterUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateDesignationMaster`, payload);
  }

  updateStatus(payload: DesignationMasterStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/updateDesignationMasterStatus`, payload);
  }
}

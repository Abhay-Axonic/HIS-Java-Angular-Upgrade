import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ParameterRow } from './parameter-master.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/lis/unit/parameterMasterController.js`
 * (list URI1: LIS_UNIT + LIST_PARAMETER_MASTER + orgId + orgUnitId + offset
 * + noOfRecordsPerPage) and lis.url.config.js:482 (LIST_PARAMETER_MASTER =
 * "/listParameterMaster"), lis.url.config.js:8 (LIS_UNIT = "/LIS/unit") —
 * not guessed. Confirmed live 2026-08-12: the endpoint responds but 500s
 * with the correlation envelope "Something went wrong. Reference: ERR-..."
 * (DQ-2026-08-12-009): phantom column technique_id in the named query.
 */
@Injectable({ providedIn: 'root' })
export class ParameterMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl list URI1 — GET /LIS/unit/listParameterMaster/{org}/{unit}/{off}/{size}.
  getList(organizationId: number, unitId: number, offset: number, size: number): Observable<ApiResponse<never, ParameterRow>> {
    return this.http.get<ApiResponse<never, ParameterRow>>(
      `${this.base}/LIS/unit/listParameterMaster/${organizationId}/${unitId}/${offset}/${size}`,
    );
  }
}

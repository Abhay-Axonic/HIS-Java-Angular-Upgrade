import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { GenericScheduleRow } from './generic-schedule-master.models';

/**
 * Endpoint path taken directly from
 * `legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller/pharmacy/genericScheduleMasterController.js`
 * (lines 254/281, PHARMACY_GLOBAL + "/listGenericScheduleMaster" + orgId +
 * unitId + offset + noOfRecordsPerPage) — not guessed.
 * GET with 4 path segments {org}/{unit}/{offset}/{size}, no body.
 * Confirmed live 2026-08-12: SUCCESS with 1 real row — real data.
 */
@Injectable({ providedIn: 'root' })
export class GenericScheduleMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:254/281 (genericScheduleMasterController.js) — GET
  // /Pharmacy/global/listGenericScheduleMaster/{org}/{unit}/{offset}/{size}.
  // Real data (1 row).
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    size: number,
  ): Observable<ApiResponse<never, GenericScheduleRow>> {
    return this.http.get<ApiResponse<never, GenericScheduleRow>>(
      `${this.base}/Pharmacy/global/listGenericScheduleMaster/${organizationId}/${unitId}/${offset}/${size}`,
    );
  }
}

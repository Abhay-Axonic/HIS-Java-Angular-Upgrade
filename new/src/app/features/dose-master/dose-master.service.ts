import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { DoseRow } from './dose-master.models';

/**
 * Endpoint path taken directly from
 * `legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller/master/global/common/doseMasterController.js`
 * (lines 77/112, URI = "/api/common/getDoseMasterList") — not guessed.
 * POST body is {organizationId, offset, noOfRecordsPerPage} ONLY — the
 * legacy call sends no unitId. Confirmed live 2026-08-12: SUCCESS with 1
 * real row — real data.
 */
@Injectable({ providedIn: 'root' })
export class DoseMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:77/112 (doseMasterController.js) — POST /api/common/getDoseMasterList
  // {organizationId, offset, noOfRecordsPerPage} (no unitId). Real data
  // (1 row).
  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, DoseRow>> {
    return this.http.post<ApiResponse<never, DoseRow>>(
      `${this.base}/api/common/getDoseMasterList`,
      { organizationId, offset, noOfRecordsPerPage },
    );
  }
}

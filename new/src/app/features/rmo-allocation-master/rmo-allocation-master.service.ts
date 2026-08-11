import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { RmoAllocationRow } from './rmo-allocation-master.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/rmoAllocationMasterController.js`
 * (GETRMODOCTORALLOCATIONMASTERLIST =
 * "/api/unit/clinical/getRmoAllocationMasterList" from
 * global.url.config.js:315) — not guessed. Confirmed live 2026-08-12:
 * SUCCESS empty (honest dev-empty).
 */
@Injectable({ providedIn: 'root' })
export class RmoAllocationMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl URI1 — POST /api/unit/clinical/getRmoAllocationMasterList.
  getList(organizationId: number, unitId: number, offset: number, size: number): Observable<ApiResponse<never, RmoAllocationRow>> {
    return this.http.post<ApiResponse<never, RmoAllocationRow>>(
      `${this.base}/api/unit/clinical/getRmoAllocationMasterList`,
      { organizationId, unitId, offset, noOfRecordsPerPage: size },
    );
  }
}

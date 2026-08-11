import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { PassBedCategoryMapperRow } from './pass-bed-category-mapper.models';

/**
 * Endpoint path taken directly from the hardcoded URI in
 * `controller/master/unit/passBedCategoryMapperController.js:126`
 * (BASE_URL + ROOT_URL + "/api/unit/getVisitorPassMapperList" — no
 * url.config.js constant; the URI is written inline) — not guessed. POST
 * body matches the legacy `initVisitorTypeMasterList` call at
 * ctrl:130-135 ({unitId, organisationId — British spelling as legacy
 * writes it — offset, noOfRecordsPerPage}). Confirmed live 2026-08-12:
 * SUCCESS with an empty listObject — honest dev-empty.
 */
@Injectable({ providedIn: 'root' })
export class PassBedCategoryMapperService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:120-135 (passBedCategoryMapperController.js), URI hardcoded at ctrl:126 —
  // POST /api/unit/getVisitorPassMapperList. Dev-empty: SUCCESS + [].
  getList(
    organisationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, PassBedCategoryMapperRow>> {
    return this.http.post<ApiResponse<never, PassBedCategoryMapperRow>>(
      `${this.base}/api/unit/getVisitorPassMapperList`,
      { unitId, organisationId, offset, noOfRecordsPerPage },
    );
  }
}

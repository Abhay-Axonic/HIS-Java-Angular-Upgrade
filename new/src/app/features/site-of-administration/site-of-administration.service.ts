import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { SiteOfAdministrationRow } from './site-of-administration.models';

/**
 * Endpoint path taken directly from
 * `legacy/HIS-WAR/src/main/webapp/resources/assets/js/config/lis.url.config.js`
 * (GET_SITEOFADMINISTRATION_LIST = "/api/global/clinical/getSiteMasterList",
 * line 542) and the POST call at
 * `controller/master/global/siteOfAdministartionController.js` (lines
 * 107-109, legacy filename typo "Administartion") — not guessed.
 * POST body is {organizationId} ONLY — the legacy call sends no unitId.
 * Confirmed live 2026-08-12: SUCCESS with 4 real rows — real data.
 */
@Injectable({ providedIn: 'root' })
export class SiteOfAdministrationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:107-109 (siteOfAdministartionController.js), lis.url.config.js:542 —
  // POST /api/global/clinical/getSiteMasterList {organizationId} (no unitId).
  // Real data (4 rows).
  getList(organizationId: number): Observable<ApiResponse<never, SiteOfAdministrationRow>> {
    return this.http.post<ApiResponse<never, SiteOfAdministrationRow>>(
      `${this.base}/api/global/clinical/getSiteMasterList`,
      { organizationId },
    );
  }
}

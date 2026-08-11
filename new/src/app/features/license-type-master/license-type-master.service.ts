import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { LicenseTypeRow } from './license-type-master.models';

/**
 * Endpoint path taken directly from
 * `legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller/master/global/licenseTypeMasterController.js`
 * (lines 69-73, GETLICENECETYPEMASTERLIST + "/" + organizationId, served
 * via serviceAction("GET", URI, data)) — not guessed.
 * GET with 1 path segment {org}, no body.
 * Confirmed live 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty.
 */
@Injectable({ providedIn: 'root' })
export class LicenseTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:69-73 (licenseTypeMasterController.js) — GET
  // /org/getLiceneceTypeMasterList/{org}. Dev-empty: SUCCESS + [].
  getList(organizationId: number): Observable<ApiResponse<never, LicenseTypeRow>> {
    return this.http.get<ApiResponse<never, LicenseTypeRow>>(
      `${this.base}/org/getLiceneceTypeMasterList/${organizationId}`,
    );
  }
}

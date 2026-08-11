import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { KitRow } from './kit-creation.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/CSSD/unit/kitCreationController.js`
 * (GETALLKIT = CSSD+"/getAllKitsList" from cssd.url.config.js:35) — not
 * guessed. The offset/noOfRecordsPerPage query params are REQUIRED
 * (DQ-2026-08-12-018: without them the endpoint 500s with the correlation
 * envelope — a wrong-payload artifact, not masked). Confirmed live
 * 2026-08-12: SUCCESS empty (honest dev-empty).
 */
@Injectable({ providedIn: 'root' })
export class KitCreationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl URI — GET /cssd/getAllKitsList with query params.
  getList(organizationId: number, unitId: number, offset: number, size: number): Observable<ApiResponse<never, KitRow>> {
    const params = new HttpParams()
      .set('organizationId', String(organizationId))
      .set('unitId', String(unitId))
      .set('offset', String(offset))
      .set('noOfRecordsPerPage', String(size));
    return this.http.get<ApiResponse<never, KitRow>>(
      `${this.base}/cssd/getAllKitsList`, { params },
    );
  }
}

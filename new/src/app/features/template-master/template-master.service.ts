import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { LabTemplateRow } from './template-master.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/labTemplateMasterController.js` (ctrl:155;
 * LIST_LAB_TEMPLATE_MASTER = "/labtemplates" from lis.url.config.js:521) —
 * not guessed. RESPONSE SHAPE (DQ-2026-08-12-020): the endpoint returns a
 * RAW ARRAY, not the {listObject} envelope — the legacy reads
 * response.data directly (ctrl:157). The service therefore returns the raw
 * array type. Confirmed live 2026-08-12: [] (honest empty).
 */
@Injectable({ providedIn: 'root' })
export class TemplateMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:155 — GET /LIS/unit/labtemplates/{org}/{unit}/{offset}/{size}.
  // Raw-array response (NOT {listObject}).
  getList(organizationId: number, unitId: number, offset: number, size: number): Observable<LabTemplateRow[]> {
    return this.http.get<LabTemplateRow[]>(
      `${this.base}/LIS/unit/labtemplates/${organizationId}/${unitId}/${offset}/${size}`,
    );
  }
}

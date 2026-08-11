import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { GrowthChartRow } from './growth-chart-master.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/growthChartMasterController.js` (ctrl:169;
 * GROWTH_CHART_LIST = "/api/global/growthChartlist" from
 * global.url.config.js:251) — not guessed. GET with path params. Confirmed
 * live 2026-08-12: SUCCESS with 1 real row.
 */
@Injectable({ providedIn: 'root' })
export class GrowthChartMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:169 — GET /api/global/growthChartlist/{org}/{unit}/{offset}/{size}.
  getList(organizationId: number, unitId: number, offset: number, size: number): Observable<ApiResponse<never, GrowthChartRow>> {
    return this.http.get<ApiResponse<never, GrowthChartRow>>(
      `${this.base}/api/global/growthChartlist/${organizationId}/${unitId}/${offset}/${size}`,
    );
  }
}

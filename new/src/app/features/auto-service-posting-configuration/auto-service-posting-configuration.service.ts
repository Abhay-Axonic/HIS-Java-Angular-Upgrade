import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AutoPostingConfigRow } from './auto-service-posting-configuration.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/master/unit/autoServicePostingConfigurationController.js`
 * (URI = "/config/billing/list/"+orgId+"/"+unitId) and the Java mapping
 * AutoPostingConfigController.java:19 ("config/billing") + :38
 * ("/list/{orgId}/{unitId}") — not guessed. Confirmed live 2026-08-12: the
 * endpoint responds but 500s with the correlation envelope (DQ-2026-08-12-013):
 * phantom column is_bed_charges_service in the raw SQL.
 */
@Injectable({ providedIn: 'root' })
export class AutoServicePostingConfigurationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl URI — GET /config/billing/list/{org}/{unit}.
  getList(organizationId: number, unitId: number): Observable<ApiResponse<never, AutoPostingConfigRow>> {
    return this.http.get<ApiResponse<never, AutoPostingConfigRow>>(
      `${this.base}/config/billing/list/${organizationId}/${unitId}`,
    );
  }
}

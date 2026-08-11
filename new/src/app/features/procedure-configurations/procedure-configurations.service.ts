import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ProcedureConfigurationRow } from './procedure-configurations.models';

/**
 * Endpoint path taken directly from
 * `legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller/otims/global/procedureConfigurationsController.js`
 * (line 50, URI = "/api/otims/getProcedureConfigList/" + organizationId +
 * "/" + unitId) — not guessed. GET with 2 path segments {org}/{unit}, no
 * body. Confirmed live 2026-08-12: SUCCESS with an empty listObject —
 * honest dev-empty (same call already used by procedure-template and
 * procedure-configuration rebuilds).
 */
@Injectable({ providedIn: 'root' })
export class ProcedureConfigurationsService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:50 (procedureConfigurationsController.js) — GET
  // /api/otims/getProcedureConfigList/{org}/{unit}. Dev-empty: SUCCESS + [].
  getList(organizationId: number, unitId: number): Observable<ApiResponse<never, ProcedureConfigurationRow>> {
    return this.http.get<ApiResponse<never, ProcedureConfigurationRow>>(
      `${this.base}/api/otims/getProcedureConfigList/${organizationId}/${unitId}`,
    );
  }
}

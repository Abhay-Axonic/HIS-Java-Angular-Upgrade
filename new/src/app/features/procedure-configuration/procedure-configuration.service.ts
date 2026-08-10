import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ProcedureConfigurationRow } from './procedure-configuration.models';

@Injectable({ providedIn: 'root' })
export class ProcedureConfigurationService {
  private http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** GET /api/otims/getProcedureConfigList/{orgId}/{unitId} — envelope with listObject. */
  list(orgId: number, unitId: number): Observable<{ listObject?: ProcedureConfigurationRow[] }> {
    return this.http.get<{ listObject?: ProcedureConfigurationRow[] }>(
      `${this.base}/api/otims/getProcedureConfigList/${orgId}/${unitId}`
    );
  }

  /** GET /api/procedure/config/status/{id}/{status} — the status toggle
   * (Global ProcedureConfigurationController:116, GET; status is a CHAR —
   * 'A' active / 'I' inactive, not a boolean). LIVE-VERIFIED 200
   * "Updated Successfully." */
  status(id: number, status: string): Observable<unknown> {
    return this.http.get(`${this.base}/api/procedure/config/status/${id}/${status}`);
  }
}

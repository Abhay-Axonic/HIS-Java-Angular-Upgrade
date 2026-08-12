import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { SubsequentSurgeryConfigurationRow } from './subsequent-surgery-configuration.models';

/**
 * Endpoint path taken directly from
 * `legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller/otims/global/subsequentSurgeryConfigurationController.js`
 * (lines 86/122, URI = "/api/otims/getSubSequentProcedureConfigList") — not
 * guessed. POST body matches the legacy call ({orgId, unitId, offset,
 * noOfRecordsPerPage}). Confirmed live 2026-08-12: SUCCESS with an empty
 * listObject — honest dev-empty.
 */
@Injectable({ providedIn: 'root' })
export class SubsequentSurgeryConfigurationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:86/122 (subsequentSurgeryConfigurationController.js) — POST
  // /api/otims/getSubSequentProcedureConfigList {orgId, unitId, offset,
  // noOfRecordsPerPage}. Dev-empty: SUCCESS + [].
  getList(
    orgId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, SubsequentSurgeryConfigurationRow>> {
    return this.http.post<ApiResponse<never, SubsequentSurgeryConfigurationRow>>(
      `${this.base}/api/otims/getSubSequentProcedureConfigList`,
      { orgId, unitId, offset, noOfRecordsPerPage },
    );
  }
}

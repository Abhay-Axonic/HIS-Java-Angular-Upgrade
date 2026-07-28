import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AutoclaveForm, AutoclaveRow, SterilizationColorOption, SterilizationTypeOption } from './autoclave-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/CSSD/unit/autoClaveMasterController.js and
 * resources/assets/js/config/cssd.url.config.js -- not guessed. All under
 * `/cssd`. Create/Update/status-toggle all use PUT/POST as legacy actually
 * sends them (confirmed live, not assumed REST-conventional). Confirmed
 * live 2026-07-28: real create/update/status-toggle round trips, all HTTP
 * 200 (Sterilization Color required a real seed row first in this dev
 * environment -- see docs/ANGULAR_PENDING_FINDINGS.md).
 */
@Injectable({ providedIn: 'root' })
export class AutoclaveMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getSterilizationTypes(organizationId: number, unitId: number): Observable<ApiResponse<never, SterilizationTypeOption>> {
    return this.http.get<ApiResponse<never, SterilizationTypeOption>>(
      `${this.base}/cssd/getActiveSterilizationTypesList?organizationId=${organizationId}&unitId=${unitId}`,
    );
  }

  getSterilizationColors(): Observable<ApiResponse<never, SterilizationColorOption>> {
    return this.http.post<ApiResponse<never, SterilizationColorOption>>(
      `${this.base}/cssd/getActiveSterilizationColorList`,
      {},
    );
  }

  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, AutoclaveRow>> {
    return this.http.get<ApiResponse<never, AutoclaveRow>>(
      `${this.base}/cssd/getAllAutoclavesList?organizationId=${organizationId}&unitId=${unitId}&offset=${offset}&noOfRecordsPerPage=${noOfRecordsPerPage}`,
    );
  }

  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/cssd/getAutoclavesListcount`, { organizationId });
  }

  // ctrl:203-235 -- real client-side form validation, confirm-modal-gated,
  // no duplicate-check (create path only).
  create(payload: AutoclaveForm & { organizationId: number; unitId: number; userId: number }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/cssd/createAutoclave`, {
      machine_name: payload.machine_name,
      machine_description: payload.machine_description,
      sterilization_type_id: payload.sterilization_type_id,
      sterilization_color_id: payload.sterilization_color_id,
      biological_indicator_day: payload.biological_indicator_day,
      sterilization_temperature: payload.sterilization_temperature,
      sterilization_pressure: payload.sterilization_pressure,
      created_by: payload.userId,
      status: 'A',
      updated_by: payload.userId,
      organizationId: payload.organizationId,
      unitId: payload.unitId,
    });
  }

  // ctrl:280-300 -- no confirm-modal gate.
  update(payload: AutoclaveForm & { userId: number }): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/cssd/updateAutoclave`, {
      id: payload.id,
      machine_name: payload.machine_name,
      machine_description: payload.machine_description,
      sterilization_type_id: payload.sterilization_type_id,
      sterilization_color_id: payload.sterilization_color_id,
      biological_indicator_day: payload.biological_indicator_day,
      sterilization_temperature: payload.sterilization_temperature,
      sterilization_pressure: payload.sterilization_pressure,
      updated_by: payload.userId,
    });
  }

  // ctrl:240-269.
  updateStatus(payload: { id: number; status: AutoclaveRow['status']; updatedBy: number }): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/cssd/updateAutoclaveStatus`, {
      id: payload.id,
      status: payload.status,
      updated_by: payload.updatedBy,
    });
  }
}

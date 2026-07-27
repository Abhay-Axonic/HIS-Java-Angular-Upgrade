import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  MachineOption,
  MachineParameterRow,
  MachineParameterSavePayload,
  MachineParameterUpdatePayload,
} from './machine-parameter-master.models';

/**
 * Endpoint paths taken directly from `lis.url.config.js:460-477`, all
 * under `/LIS/global/*`.
 */
@Injectable({ providedIn: 'root' })
export class MachineParameterMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:89-114 (getMachineParaList) -- dropdown source, POST with a body.
  getActiveMachines(organisationId: number, unitId: number): Observable<ApiResponse<never, MachineOption>> {
    return this.http.post<ApiResponse<never, MachineOption>>(`${this.base}/LIS/global/getActiveMachineList`, {
      organisationId,
      unitId,
    });
  }

  // ctrl:123-148 (initMachineParaMasterList) -- real GET, path params, genuinely working pagination.
  getList(
    orgId: number,
    unitId: number,
    offset: number,
    recordPerPage: number,
  ): Observable<ApiResponse<never, MachineParameterRow>> {
    return this.http.get<ApiResponse<never, MachineParameterRow>>(
      `${this.base}/LIS/global/listMachineParaMaster/${orgId}/${unitId}/${offset}/${recordPerPage}`,
    );
  }

  getCount(orgId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(`${this.base}/LIS/global/getTotalMachinePatraMasterRecord/${orgId}/${unitId}`);
  }

  // ctrl:259-300 (save) -- NO duplicate-check exists anywhere in the
  // backend for this path -- see class doc, faithfully reproduced.
  save(payload: MachineParameterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/LIS/global/addMachineParameter`, payload);
  }

  // ctrl:230-257 (showUpdateBtn) -- response.data.object, OBJECT-wrapped
  // (not array, unlike bloodBagType; not the row-copy shape used by
  // OTIMSroleCreation -- a real server round-trip, singular object).
  getById(orgId: number, unitId: number, machinParameterId: number): Observable<ApiResponse<MachineParameterRow>> {
    return this.http.get<ApiResponse<MachineParameterRow>>(
      `${this.base}/LIS/global/getMachineParabyId/${orgId}/${unitId}/${machinParameterId}`,
    );
  }

  // ctrl:302-341 (update) -- CONFIRMED BROKEN on real legacy: the
  // service-layer duplicate-check (by code, org+unit-scoped, correctly
  // self-excluding) works fine, but any update that passes it then
  // crashes in the DAO on a wrong Dozer mapping ID and always returns
  // "Failed to add Machine." -- see class doc. No client-side workaround
  // added; this call is wired to the real, broken endpoint as-is.
  update(payload: MachineParameterUpdatePayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/LIS/global/updateMachineParaMaster`, payload);
  }

  // ctrl:193-228 (updateStatus) -- real GET with path params, does NOT reload the list.
  updateStatus(orgId: number, machinParameterId: number, status: MachineParameterRow['status']): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(
      `${this.base}/LIS/global/activeInactiveMachinePara/${orgId}/${machinParameterId}/${status}`,
    );
  }
}

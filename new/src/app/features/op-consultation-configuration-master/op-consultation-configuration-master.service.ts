import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { OPConsulationConfigurationMasterRow } from './op-consultation-configuration-master.models';

/**
 * NO ENDPOINT (noData class, DQ-2026-08-12-005/-020): the `OPConsulationConfigurationMaster` state has
 * no backend list endpoint — its controller is the MasterController shell or
 * is absent from the webapp. The service exists only to keep the house
 * pattern; the component renders the noData state without calling it.
 */
@Injectable({ providedIn: 'root' })
export class OPConsulationConfigurationMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // No backend list endpoint exists. Kept as a never-called contract; the
  // component renders the noData state without invoking it.
  getList(): Observable<ApiResponse<never, OPConsulationConfigurationMasterRow>> {
    return this.http.get<ApiResponse<never, OPConsulationConfigurationMasterRow>>(`${this.base}/OPConsulationConfigurationMaster/noop`);
  }
}

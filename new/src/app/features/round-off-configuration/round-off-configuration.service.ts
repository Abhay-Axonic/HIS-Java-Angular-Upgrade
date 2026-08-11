import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { RoundOffConfigurationRow } from './round-off-configuration.models';

/**
 * NO ENDPOINT (noData class, DQ-2026-08-12-005/-020): the `roundOffConfiguration` state has
 * no backend list endpoint — its controller is the MasterController shell or
 * is absent from the webapp. The service exists only to keep the house
 * pattern; the component renders the noData state without calling it.
 */
@Injectable({ providedIn: 'root' })
export class RoundOffConfigurationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // No backend list endpoint exists. Kept as a never-called contract; the
  // component renders the noData state without invoking it.
  getList(): Observable<ApiResponse<never, RoundOffConfigurationRow>> {
    return this.http.get<ApiResponse<never, RoundOffConfigurationRow>>(`${this.base}/roundOffConfiguration/noop`);
  }
}

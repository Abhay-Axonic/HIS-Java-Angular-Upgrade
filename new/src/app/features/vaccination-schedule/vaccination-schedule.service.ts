import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { VaccinationScheduleRow } from './vaccination-schedule.models';

/**
 * NO ENDPOINT (noData class, DQ-2026-08-12-022): the legacy screen is driven by a MasterController shell (common-controller.js) that performs zero endpoint calls.
 * The service exists only to keep the house pattern; getList() is never
 * expected to return rows.
 */
@Injectable({ providedIn: 'root' })
export class VaccinationScheduleService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // No backend list endpoint exists. Kept as a never-called contract so the
  // component follows the house shape; the component renders the noData
  // state without calling it.
  getList(): Observable<ApiResponse<never, VaccinationScheduleRow>> {
    return this.http.get<ApiResponse<never, VaccinationScheduleRow>>(`${this.base}/vaccinationSchedule/noop`);
  }
}

import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import type { ApiResponse } from '../../core/api-response';
import type { CyclicSlotPayload, IdNameRow } from './cyclic-appointment-scheduling.models';

/** Cyclic appointment scheduling endpoints. The save POSTs a LIST of
 * SlotMasterDto payloads (one per recurrence day) to
 * /api/billing/generateCyclicServiceAppointment. DATE CONVENTION: STRING
 * dates (dd-M-yyyy HH:mm:ss via formatLegacyNow) — the registration/
 * prescription convention, NOT the encounter's Long. */
@Injectable({ providedIn: 'root' })
export class CyclicAppointmentSchedulingService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  getSpecialities(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    return this.post('/adt/getActiveSpecialityList', { organizationId: orgId, unitId });
  }

  getDoctors(specialityId: number): Observable<ApiResponse<never, IdNameRow>> {
    return this.post('/adt/getDoctorsListAganistSpecialities', { specialityId });
  }

  saveCyclic(payloads: CyclicSlotPayload[]): Observable<ApiResponse<never, unknown>> {
    return this.post('/api/billing/generateCyclicServiceAppointment', payloads);
  }
}

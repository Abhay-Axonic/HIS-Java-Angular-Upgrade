import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import type { ApiResponse } from '../../core/api-response';
import type { AppointmentRow, SlotCriteria } from './cyclic-appointments.models';

/** Cyclic appointments list endpoints — the appointment family's WORKING list
 * endpoint (searchAppointmentsByCriteria; the scheduleAppointmentList 500 is
 * bypassed). DATE CONVENTION: STRING dates — the registration/prescription
 * convention, NOT the encounter's Long. */
@Injectable({ providedIn: 'root' })
export class CyclicAppointmentsService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  list(criteria: SlotCriteria): Observable<ApiResponse<never, AppointmentRow>> {
    return this.post('/api/opd/searchAppointmentsByCriteria', criteria);
  }
}

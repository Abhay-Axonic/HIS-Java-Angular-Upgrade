import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import type { ApiResponse } from '../../core/api-response';
import type { AppointmentRow, IdNameRow, PatientRow, SlotPayload, SlotRow } from './appointment-scheduling.models';

/** Appointment scheduling endpoints (corrected backend). DATE CONVENTION:
 * the appointment payload's dates are STRING (dd-M-yyyy HH:mm:ss via
 * formatLegacyNow) — the registration/prescription convention, NOT the
 * encounter's Long. The list uses searchAppointmentsByCriteria (the working
 * endpoint) instead of the 500-ing scheduleAppointmentList. */
@Injectable({ providedIn: 'root' })
export class AppointmentSchedulingService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  getSpecialities(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    return this.post('/adt/getActiveSpecialityList', { organizationId: orgId, unitId });
  }

  getSubSpecialities(specialityId: number): Observable<ApiResponse<never, IdNameRow>> {
    return this.post('/adt/getSubSpecialityBySpecialityId', { specialityId });
  }

  getDoctors(specialityId: number): Observable<ApiResponse<never, IdNameRow>> {
    // the legacy constant's typo is the real endpoint — ported faithfully
    return this.post('/adt/getDoctorsListAganistSpecialities', { specialityId });
  }

  getSlots(criteria: SlotPayload): Observable<ApiResponse<never, SlotRow>> {
    return this.post('/api/opd/getDailySlots', criteria);
  }

  searchPatient(criteria: Record<string, unknown>): Observable<ApiResponse<never, PatientRow>> {
    return this.post('/api/global/patientSearch', criteria);
  }

  saveAppointment(payload: SlotPayload): Observable<ApiResponse<never, unknown>> {
    return this.post('/api/opd/scheduleAppointment', payload);
  }

  searchAppointments(criteria: SlotPayload): Observable<ApiResponse<never, AppointmentRow>> {
    return this.post('/api/opd/searchAppointmentsByCriteria', criteria);
  }
}

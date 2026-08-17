import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import type { ApiResponse } from '../../core/api-response';
import type { IdNameRow, VisitCriteria, VisitRow } from './patient-visit-list.models';

/** Patient visit list endpoints — the appointment family (SlotMasterDto
 * criteria). DATE CONVENTION: STRING dates — the registration/prescription
 * convention, NOT the encounter's Long. */
@Injectable({ providedIn: 'root' })
export class PatientVisitListService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  getDoctors(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    return this.post('/adt/getDoctorsListAganistSpecialities', { organizationId: orgId, unitId });
  }

  list(criteria: VisitCriteria): Observable<ApiResponse<never, VisitRow>> {
    return this.post('/api/opd/searchAppointmentsByCriteria', criteria);
  }
}

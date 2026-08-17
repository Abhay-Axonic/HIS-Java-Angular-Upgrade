import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {API_BASE_URL} from '../../core/api-config';
import type {ApiResponse} from '../../core/api-response';
import type {ErRegistrationPayload, IdNameRow, PatientRow} from './er-registration.models';

/** ER registration endpoints (corrected backend, UnknownPatientRegistrationController
 * class /adt). DATE CONVENTION: String dates via formatLegacyNow — the
 * registration/prescription convention, NOT the encounter's Long. */
@Injectable({providedIn: 'root'})
export class ErRegistrationService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  searchPatient(criteria: Record<string, unknown>): Observable<ApiResponse<never, PatientRow>> {
    return this.post('/adt/patientSearchByMultipleCriteria', criteria);
  }

  getPriorityList(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    return this.http.get<ApiResponse<never, IdNameRow>>(
      `${this.base}/adt/getActivePriorityList/unit/${unitId}/org/${orgId}`);
  }

  save(payload: ErRegistrationPayload): Observable<ApiResponse<never, unknown>> {
    return this.post('/adt/saveUnknownPatient', payload);
  }
}

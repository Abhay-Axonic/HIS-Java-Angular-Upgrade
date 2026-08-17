import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import type { ApiResponse } from '../../core/api-response';
import type { ConsultPayload, IdNameRow, ReferralRow } from './interconsultation.models';

/** Interconsultation endpoints (corrected backend, ConsultDoctorController
 * class /api/opd). DATE CONVENTION: the referral's dates are STRING
 * (dd-M-yyyy HH:mm:ss via formatLegacyNow) — the registration/prescription
 * convention, NOT the encounter's Long. The speciality -> doctor cascade
 * reuses the appointment scheduling endpoints. */
@Injectable({ providedIn: 'root' })
export class InterconsultationService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  getSpecialities(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    return this.post('/adt/getActiveSpecialityList', { organizationId: orgId, unitId });
  }

  getDoctors(specialityId: number): Observable<ApiResponse<never, IdNameRow>> {
    // the legacy constant's typo is the real endpoint — ported faithfully
    return this.post('/adt/getDoctorsListAganistSpecialities', { specialityId });
  }

  saveReferral(payload: ConsultPayload): Observable<ApiResponse<never, unknown>> {
    return this.post('/api/opd/consultDoctor', payload);
  }

  listByEncounter(criteria: ConsultPayload): Observable<ApiResponse<never, ReferralRow>> {
    return this.post('/api/opd/consultDoctorListByEncounter', criteria);
  }
}

import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {API_BASE_URL} from '../../core/api-config';
import type {ApiResponse} from '../../core/api-response';
import type {DonorRow} from './donor-screening-worklist.models';

/** Donor Screening Worklist — the legacy worklist's call-set is a single
 * grid/search endpoint (donorScreeningWorklistController.js:91). The legacy
 * worklist has NO save — the row action navigates to the donorScreening form,
 * which owns the save. DATE CONVENTION: String dates via formatLegacyNow —
 * the registration/prescription convention, NOT the encounter's Long. */
@Injectable({providedIn: 'root'})
export class BloodBankService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  search(criteria: Record<string, unknown>): Observable<ApiResponse<never, DonorRow>> {
    return this.post('/api/bloodbank/getDonorRegisttrationAndScreeningDetails', criteria);
  }
}

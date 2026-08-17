import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {API_BASE_URL} from '../../core/api-config';
import type {ApiResponse} from '../../core/api-response';
import type {IdNameRow, PharmacyRow, SavePayload} from './pending-issues.models';

/** Pending Issues — the corrected backend's pharmacy family. DATE CONVENTION: String
 * dates via formatLegacyNow — the registration/prescription convention, NOT
 * the encounter's Long. */
@Injectable({providedIn: 'root'})
export class PharmacyService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  dropdowns(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    return this.http.get<ApiResponse<never, IdNameRow>>(`${this.base}/api/pharmacy/ip/pending/issues/dropdowns`);
  }

  search(criteria: Record<string, unknown>): Observable<ApiResponse<never, PharmacyRow>> {
    return this.post('/api/pharmacy/ip/pending/issues/search', criteria);
  }

  save(payload: SavePayload): Observable<ApiResponse<never, unknown>> {
    return this.http.patch<ApiResponse<never, unknown>>(`${this.base}/api/pharmacy/ip/pending/issues/save`, payload);
  }
}

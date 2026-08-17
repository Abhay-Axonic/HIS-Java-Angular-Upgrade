import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {API_BASE_URL} from '../../core/api-config';
import type {ApiResponse} from '../../core/api-response';
import type {NursingRow, SavePayload} from './surgical-calendar.models';

/** Surgical Calendar — the corrected backend's nursing/OT family. DATE CONVENTION:
 * String dates via formatLegacyNow — the registration/prescription
 * convention, NOT the encounter's Long. */
@Injectable({providedIn: 'root'})
export class NursingService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  search(criteria: Record<string, unknown>): Observable<ApiResponse<never, NursingRow>> {
    return this.post('/api/otims/surgicalCalendar/search', criteria);
  }

  save(payload: SavePayload): Observable<ApiResponse<never, unknown>> {
    return this.post('/api/otims/surgicalCalendar/save', payload);
  }
}

import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {API_BASE_URL} from '../../core/api-config';
import type {ApiResponse} from '../../core/api-response';
import type {ErRow} from './map-of-bed-er.models';

/** Map of Ward Bed — the corrected backend endpoint. DATE CONVENTION: String dates
 * via formatLegacyNow — the registration/prescription convention, NOT the
 * encounter's Long. */
@Injectable({providedIn: 'root'})
export class ErWorklistService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  list(criteria: Record<string, unknown>): Observable<ApiResponse<never, ErRow>> {
    return this.http.post<ApiResponse<never, ErRow>>(`${this.base}/adt/getDataForMapOfBed`, criteria);
  }
}

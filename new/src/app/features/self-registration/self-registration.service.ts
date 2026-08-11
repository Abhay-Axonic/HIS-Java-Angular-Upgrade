import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

@Injectable({ providedIn: 'root' })
export class SelfRegistrationService {
  private base = inject(API_BASE_URL);

  constructor(private http: HttpClient) {}

  /**
   * These ADT reference feeds bind org/unit via the LEGACY header names
   * (@RequestHeader("unit_id")/("organization_id")) — the screen-47
   * header-contract split class: they 500/405 under the JwtTokenFilter's
   * userId/unitId headers alone. Live-verified: GET + both header sets ->
   * 200 with real rows. The authInterceptor still supplies userId/unitId
   * for the token filter.
   */
  private legacyHeaders(): HttpHeaders {
    return new HttpHeaders({ unit_id: '1', organization_id: '1' });
  }

  getCountries(orgId: number, unitId: number): Observable<any> {
    return this.http.get<any>(`${this.base}/adt/getActiveCountryList/${orgId}`, {
      headers: this.legacyHeaders(),
    });
  }

  getGenders(orgId: number, unitId: number): Observable<any> {
    return this.http.get<any>(`${this.base}/adt/getActiveGenderList`, {
      headers: this.legacyHeaders(),
    });
  }

  getIdentifications(orgId: number, unitId: number): Observable<any> {
    return this.http.get<any>(`${this.base}/adt/getActiveIdentificationList`, {
      headers: this.legacyHeaders(),
    });
  }

  getMaritalStatuses(orgId: number, unitId: number): Observable<any> {
    return this.http.get<any>(`${this.base}/adt/getActiveMaritalStatusList`, {
      headers: this.legacyHeaders(),
    });
  }

  getPrefixes(orgId: number, unitId: number): Observable<any> {
    return this.http.get<any>(`${this.base}/adt/getActivePrefixList`, {
      headers: this.legacyHeaders(),
    });
  }
}

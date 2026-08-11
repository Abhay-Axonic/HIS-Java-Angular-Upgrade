import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

@Injectable({ providedIn: 'root' })
export class GrowthChartService {
  private base = inject(API_BASE_URL);

  constructor(private http: HttpClient) {}

  /** ADT GET feeds bind org/unit via the LEGACY header pair (screen-47
   * header-contract split class) — the interceptor still supplies userId/unitId. */
  private legacyHeaders(): HttpHeaders {
    return new HttpHeaders({ unit_id: '1', organization_id: '1' });
  }

  getGenders(orgId: number, unitId: number): Observable<any> {
    return this.http.get<any>(`${this.base}/adt/getActiveGenderList`, {
      headers: this.legacyHeaders(),
    });
  }

  getList(orgId: number, unitId: number, offset: number, records: number): Observable<any> {
    return this.http.get<any>(
      `${this.base}/api/global/growthChartlist/${orgId}/${unitId}/${offset}/${records}`,
      { headers: this.legacyHeaders() }
    );
  }

  getDetails(payload: any): Observable<any> {
    return this.http.post<any>(
      `${this.base}/api/global/growthChart/patient/details`,
      payload
    );
  }
}

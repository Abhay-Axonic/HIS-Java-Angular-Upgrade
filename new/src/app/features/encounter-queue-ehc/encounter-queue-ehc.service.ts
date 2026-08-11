import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

@Injectable({ providedIn: 'root' })
export class EncounterQueueEhcService {
  private base = inject(API_BASE_URL);

  constructor(private http: HttpClient) {}

  /**
   * The GET list feeds bind their org/unit via the LEGACY header names
   * (@RequestHeader("unit_id")/("organization_id")) — the authInterceptor
   * supplies userId/unitId, which the JwtTokenFilter needs, but these two
   * endpoints additionally require the legacy pair or they 500. Verified
   * live: both header sets together -> 200 + real rows; userId/unitId alone
   * -> 500.
   */
  private legacyHeaders(): HttpHeaders {
    return new HttpHeaders({ unit_id: '1', organization_id: '1' });
  }

  getSpecialities(payload: any): Observable<any> {
    return this.http.post<any>(
      `${this.base}/adt/getActiveSpecialityList`,
      payload
    );
  }

  getDoctors(payload: any): Observable<any> {
    return this.http.post<any>(
      `${this.base}/adt/getDoctorListAganistSpeciality`,
      payload
    );
  }

  getReasons(payload: any): Observable<any> {
    return this.http.post<any>(
      `${this.base}/api/common/getActiveReasonMasterList`,
      payload
    );
  }

  getNursingStations(orgId: number, unitId: number): Observable<any> {
    return this.http.get<any>(
      `${this.base}/api/global/getListOfNursingStation`,
      { headers: this.legacyHeaders() }
    );
  }

  getStatuses(orgId: number, unitId: number): Observable<any> {
    return this.http.get<any>(
      `${this.base}/api/opd/getListOfAppointmentStatusMaster`,
      { headers: this.legacyHeaders() }
    );
  }

  getDailySlots(payload: any): Observable<any> {
    return this.http.post<any>(`${this.base}/api/opd/getDailySlots`, payload);
  }

  getSystemGeneratedVisitTypeId(payload: any): Observable<any> {
    return this.http.post<any>(
      `${this.base}/api/opd/getSystemGeneratedVisitTypeId`,
      payload
    );
  }
}

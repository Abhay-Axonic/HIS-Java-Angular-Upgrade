import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

@Injectable({ providedIn: 'root' })
export class RadiologyReportReleaseWorklistService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  load(): Observable<any> {
    const payload = {"userId": 1, "unitId": 1, "organizationId": 1};
    return payload === null
      ? this.http.get<any>(`${this.base}/adt/getSpecialityListByUserIdByUnitId`)
      : this.http.post<any>(`${this.base}/adt/getSpecialityListByUserIdByUnitId`, payload);
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

@Injectable({ providedIn: 'root' })
export class MpShahEncounterSummaryService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  load(): Observable<any> {
    const payload = {"organizationId": 1, "unitId": 1, "encounterId": 1};
    return payload === null
      ? this.http.get<any>(`${this.base}/api/global/unit/getEncounterSummary`)
      : this.http.post<any>(`${this.base}/api/global/unit/getEncounterSummary`, payload);
  }
}

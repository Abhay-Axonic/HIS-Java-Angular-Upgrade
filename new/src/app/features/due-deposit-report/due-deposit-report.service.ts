import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

@Injectable({ providedIn: 'root' })
export class DueDepositReportService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  load(): Observable<any> {
    const payload = null;
    return payload === null
      ? this.http.get<any>(`${this.base}/api/billing/dueDepositReport`)
      : this.http.get<any>(`${this.base}/api/billing/dueDepositReport`, payload);
  }
}

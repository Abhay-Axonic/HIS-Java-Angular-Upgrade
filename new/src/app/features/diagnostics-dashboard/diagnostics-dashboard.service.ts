import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

@Injectable({ providedIn: 'root' })
export class DiagnosticsDashboardService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  financeCounts(organizationId: number, unitId: number): Observable<any> {
    return this.http.post<any>(`${this.base}/api/global/getFinanceDetailsCount`, { organizationId: 1, unitId: 1 });
  }
}

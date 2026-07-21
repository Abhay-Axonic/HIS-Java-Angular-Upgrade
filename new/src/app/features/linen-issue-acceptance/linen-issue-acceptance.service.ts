import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { LiaDropdowns, LiaRow } from './linen-issue-acceptance.models';

@Injectable({ providedIn: 'root' })
export class LinenIssueAcceptanceService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDropdowns(unitId: number, userId: number): Observable<LiaDropdowns> {
    return this.http.get<LiaDropdowns>(`${this.base}/api/linenLaundry/linen/issue/acceptance/dropdowns/${unitId}/${userId}`);
  }

  search(payload: Record<string, unknown>): Observable<LiaRow[]> {
    return this.http.post<LiaRow[]>(`${this.base}/api/linenLaundry/linen/issue/acceptance/search`, payload);
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { LiadDetail } from './linen-issue-acceptance-details.models';

@Injectable({ providedIn: 'root' })
export class LinenIssueAcceptanceDetailsService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDetail(id: number | string): Observable<LiadDetail> {
    return this.http.get<LiadDetail>(`${this.base}/api/linenLaundry/linen/issue/acceptance/${id}`);
  }
}

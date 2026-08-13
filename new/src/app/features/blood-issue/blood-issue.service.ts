import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { BloodBagIssueSave } from './blood-issue.models';

@Injectable({ providedIn: 'root' })
export class BloodIssueService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  saveIssue(payload: BloodBagIssueSave): Observable<any> {
    return this.http.post(`${this.base}/global/bloodBank/saveIssueBloodBag`, payload);
  }
}

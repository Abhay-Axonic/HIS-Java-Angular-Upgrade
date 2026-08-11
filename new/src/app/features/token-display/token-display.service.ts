import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

@Injectable({ providedIn: 'root' })
export class TokenDisplayService {
  private base = inject(API_BASE_URL);

  constructor(private http: HttpClient) {}

  getTokenList(payload: any): Observable<any> {
    return this.http.post<any>(`${this.base}/api/opd/token/getTokenList`, payload);
  }

  getTokenMsgList(payload: any): Observable<any> {
    return this.http.post<any>(`${this.base}/api/opd/token/getTokenMsgList`, payload);
  }
}

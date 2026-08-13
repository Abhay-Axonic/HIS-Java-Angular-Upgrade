import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

@Injectable({ providedIn: 'root' })
export class DetailsConsignmentManagerDashboardService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  load(): Observable<any> {
    const payload = {"storeId": 1};
    return payload === null
      ? this.http.get<any>(`${this.base}/api/global/store/getStoreListById/`)
      : this.http.post<any>(`${this.base}/api/global/store/getStoreListById/`, payload);
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

@Injectable({ providedIn: 'root' })
export class IncidentCategoryService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  getCategories(organizationId: number, unitId: number): Observable<any> {
    return this.http.get<any>(`${this.base}/api/global/unit/getActiveIncidentCategoryMaster/${organizationId}/${unitId}`);
  }
}

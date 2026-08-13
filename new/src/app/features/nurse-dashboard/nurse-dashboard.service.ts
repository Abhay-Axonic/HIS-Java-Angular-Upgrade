import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

@Injectable({ providedIn: 'root' })
export class NurseDashboardService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  occupancy(organizationId: number, unitId: number): Observable<any> {
    return this.http.post<any>(`${this.base}/api/global/getDashboardBedOccupancyList`, { organizationId: 1, unitId: 1 });
  }
  assignedBeds(organizationId: number, unitId: number): Observable<any> {
    return this.http.post<any>(`${this.base}/api/global/getAssignedBedList`, { orgId: 1, unitId: 1 });
  }
}

import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import type { ApiResponse } from '../../core/api-response';
import type { IdNameRow, OrderCriteria, OrderRow } from './pending-orders.models';

/** Pending orders endpoints (corrected backend, NursingOrderMapperController
 * class /api/opd). DATE CONVENTION: the order criteria's dates are STRING
 * (dd-M-yyyy HH:mm:ss via formatLegacyNow) — the registration/prescription
 * convention, NOT the encounter's Long. The grid is READ-ONLY in this build:
 * the status-changing endpoints exist but are NOT called (deferred pass). */
@Injectable({ providedIn: 'root' })
export class PendingOrdersService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  getOrders(criteria: OrderCriteria): Observable<ApiResponse<never, OrderRow>> {
    return this.post('/api/opd/pendingOrders/getNurseOrders', criteria);
  }

  getOrdersByDate(criteria: OrderCriteria): Observable<ApiResponse<never, OrderRow>> {
    return this.post('/api/opd/pendingOrders/getNurseOrdersByDate', criteria);
  }

  getOrdersByPatient(patientId: number): Observable<ApiResponse<never, OrderRow>> {
    return this.post('/api/opd/pendingOrders/getPendingNurOrdListByPatientId', { patientId });
  }

  getSpecialities(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    return this.post('/adt/getActiveSpecialityList', { organizationId: orgId, unitId });
  }
}

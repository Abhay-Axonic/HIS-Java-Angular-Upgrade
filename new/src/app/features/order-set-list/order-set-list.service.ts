import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { OrdersetRow } from '../order-set/order-set.models';

/**
 * The 588 Order Set List view shares the SAME masked endpoint as 587:
 * POST /api/orderset/orderSetsList (legacy orderSetMasterController.js).
 * Verified live 2026-08-12 (DQ-031): masked envelope "Something went
 * wrong!!" — one investigation covered both screens.
 */
@Injectable({ providedIn: 'root' })
export class OrderSetListService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:getOrderSetMasterList — POST /api/orderset/orderSetsList
  // {orgId, unitId, offset, recordPerPage}.
  getList(orgId: number, unitId: number, offset: number, size: number): Observable<ApiResponse<never, OrdersetRow>> {
    return this.http.post<ApiResponse<never, OrdersetRow>>(
      `${this.base}/api/orderset/orderSetsList`,
      { orgId, unitId, offset, recordPerPage: size },
    );
  }
}

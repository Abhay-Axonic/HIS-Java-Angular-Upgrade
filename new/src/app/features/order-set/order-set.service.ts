import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { OrdersetRow } from './order-set.models';

/**
 * Endpoint + payload taken directly from
 * `legacy/.../controller/orderset/orderSetMasterController.js`
 * (URI1 = "/api/orderset/orderSetsList", payload {orgId, unitId, offset,
 * recordPerPage}). Verified live 2026-08-12 (DQ-031): masked envelope
 * {"status":"error","code":"500","message":"Something went wrong!!"}.
 */
@Injectable({ providedIn: 'root' })
export class OrderSetService {
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

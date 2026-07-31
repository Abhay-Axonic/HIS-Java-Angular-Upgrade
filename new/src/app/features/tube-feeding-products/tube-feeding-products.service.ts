import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { TubeFeedingProductRow } from './tube-feeding-products.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/global.url.config.js:176-181 and confirmed
 * against TubeFeedingProductsController.java (`@RequestMapping("/api/global")`)
 * -- not guessed. Note this legacy state has NO resolve block at all
 * (app.js:19683-19689, confirmed by direct read) -- no data.moduleId on
 * this rebuild's route either, faithful match, same class as
 * billingGroupMaster/periodicityService elsewhere in this app.
 */
@Injectable({ providedIn: 'root' })
export class TubeFeedingProductsService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:60-98 -- POST { organisationId, offset, noOfRecordsPerPage, unitId }.
  getList(
    organisationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, TubeFeedingProductRow>> {
    return this.http.post<ApiResponse<never, TubeFeedingProductRow>>(
      `${this.base}/api/global/getTubeFeedingProductsList`,
      { organisationId, offset, noOfRecordsPerPage, unitId },
    );
  }

  // ctrl:73-97 -- POST { organisationId, unitId }, count in `object`.
  getCount(organisationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/global/getTubeFeedingProductsCount`, {
      organisationId,
      unitId,
    });
  }

  // ctrl:147-205 -- POST, real create.
  save(payload: {
    productsDesc: string;
    productsCode: string;
    organisationId: number;
    unitId: number;
    createdBy: number;
    updatedBy: number;
    status: 'A';
    createdDate: string;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveTubeFeedingProducts`, payload);
  }

  // ctrl:212-240 -- GET path variables, exactly matching
  // TubeFeedingProductsController.java's @PathVariable order.
  getById(
    tubeFeedingProductsId: number,
    organisationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, TubeFeedingProductRow>> {
    return this.http.get<ApiResponse<never, TubeFeedingProductRow>>(
      `${this.base}/api/global/getTubeFeedingProductsById/${tubeFeedingProductsId}/${organisationId}/${unitId}`,
    );
  }

  // ctrl:243-292 -- POST, real update.
  update(payload: {
    tubeFeedingProductsId: number;
    productsDesc: string;
    productsCode: string;
    organisationId: number;
    unitId: number;
    updatedBy: number;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateTubeFeedingProducts`, payload);
  }

  // ctrl:294-330 -- POST, real status toggle.
  updateStatus(
    tubeFeedingProductsId: number,
    status: 'A' | 'I',
    updatedBy: number,
    updatedDate: string,
  ): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateTubeFeedingProductsStatus`, {
      tubeFeedingProductsId,
      status,
      updatedBy,
      updatedDate,
    });
  }
}

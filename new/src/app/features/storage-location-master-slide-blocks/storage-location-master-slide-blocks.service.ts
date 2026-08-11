import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { StorageRackRow } from './storage-location-master-slide-blocks.models';

/**
 * Endpoint path taken directly from
 * `resources/assets/js/controller/lis/unit/storageLocationMstForSlideBlocksController.js`
 * (lines 94-95 list URI1: LIS_UNIT + LIST_STORAGE_SLIDE_MASTER + S + orgId +
 * S + orgUnitId + S + offset + S + noOfRecordsPerPage) and
 * lis.url.config.js:548 (LIST_STORAGE_SLIDE_MASTER = "/listRackMaster"),
 * lis.url.config.js:8 (LIS_UNIT = "/LIS/unit") -- not guessed.
 *
 * Dev-empty: the DAO answers status 'error' + "Rack Records not found."
 * (RackMasterDaoImpl.java:356-359) when the dev DB has no rows -- surfaced
 * verbatim by the component's error state, no masked banner.
 */
@Injectable({ providedIn: 'root' })
export class StorageLocationMasterSlideBlocksService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:94-95 — GET /LIS/unit/listRackMaster/{org}/{unit}/{off}/{size}.
  getList(organizationId: number, unitId: number, offset: number, size: number): Observable<ApiResponse<never, StorageRackRow>> {
    return this.http.get<ApiResponse<never, StorageRackRow>>(
      `${this.base}/LIS/unit/listRackMaster/${organizationId}/${unitId}/${offset}/${size}`,
    );
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  BedTypeDetail,
  BedTypeRow,
  BedTypeSavePayload,
  BedTypeStatusPayload,
  BedTypeUpdatePayload,
} from './bed-type-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/adt/global/bedTypeMasterController.js and
 * resources/assets/js/config/adt.url.config.js:284-289,426 -- not guessed,
 * and independently confirmed against the live backend controller
 * (legacy/ADT/.../global/controller/BedTypeMasterController.java, mapped
 * under `/adt`). No `/api/...` modernized route exists for this screen
 * (same situation as bed-category-master) -- these old `/adt/...` paths and
 * POST-only verbs are the real, only backend contract.
 */
@Injectable({ providedIn: 'root' })
export class BedTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getBedTypeList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, BedTypeRow>> {
    return this.http.post<ApiResponse<never, BedTypeRow>>(`${this.base}/adt/getBedTypeMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getBedTypeCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getBedTypeCount`, {
      organizationId,
    });
  }

  getBedTypeById(bedTypeId: number): Observable<ApiResponse<never, BedTypeDetail>> {
    return this.http.post<ApiResponse<never, BedTypeDetail>>(`${this.base}/adt/getBedTypeById`, {
      bedTypeId,
    });
  }

  saveBedType(payload: BedTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveBedTypeMaster`, payload);
  }

  updateBedType(payload: BedTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateBedTypeMaster`, payload);
  }

  updateStatus(payload: BedTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForBedType`, payload);
  }
}

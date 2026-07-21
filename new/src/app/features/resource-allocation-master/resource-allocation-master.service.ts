import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AmbulanceTypeTariffOption,
  ResourceAllocationDetail,
  ResourceAllocationRow,
  ResourceAllocationSavePayload,
  ResourceAllocationStatusPayload,
  ResourceAllocationUpdatePayload,
} from './resource-allocation-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/lis/global/resourceAllocationController.js
 * and their literal string values in
 * resources/assets/js/config/adt.url.config.js:
 *   GETAMBULANCETARRIFACTIVELIST   = /adt/getAmbulanceTarrifActiveList (ctrl:51)
 *   GETRESOURCEALLOCATIONLIST      = /adt/getResourceAllocationList    (ctrl:96/132)
 *   (count endpoint)               = /adt/getResourceAllocationListCount (ctrl:97, inline)
 *   ADDRESOURCEALLOCATION          = /adt/saveResourceAllocation       (ctrl:240)
 *   GETSINGLERESOURCEALLOCATION    = /adt/getSingleResourceAllocation  (ctrl:337)
 *   UPDATERESOURCEALLOCATIONSTATUS = /adt/changeResourceAllocationStatus (ctrl:373)
 *   UPDATERESOURCEALLOCATION       = /adt/updateResourceAllocation     (ctrl:416)
 * `BASE_URL`/`ROOT_URL` are both `''` in this deployment, so these are the
 * complete paths. Every call in the legacy controller goes through
 * `GenericService.serviceAction("POST", URI, data)` regardless of read or
 * write; confirmed backend-side too (AmbulanceController.java maps every one
 * of these under `/adt` with `method = RequestMethod.POST`), so this service
 * uses POST uniformly rather than "upgrading" reads to GET.
 *
 * `unitId` is deliberately NOT sent on the list/count calls -- legacy has it
 * commented out in both `data1`/`data2` (ctrl:88/93), i.e. this screen's list
 * is org-level only, not unit-scoped. Reproduced by simply not accepting a
 * unitId parameter here at all.
 */
@Injectable({ providedIn: 'root' })
export class ResourceAllocationMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getAmbulanceTarrifActiveList(organizationId: number): Observable<ApiResponse<never, AmbulanceTypeTariffOption>> {
    return this.http.post<ApiResponse<never, AmbulanceTypeTariffOption>>(`${this.base}/adt/getAmbulanceTarrifActiveList`, {
      organizationId,
    });
  }

  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, ResourceAllocationRow>> {
    return this.http.post<ApiResponse<never, ResourceAllocationRow>>(`${this.base}/adt/getResourceAllocationList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getResourceAllocationListCount`, { organizationId });
  }

  save(payload: ResourceAllocationSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveResourceAllocation`, payload);
  }

  getById(resourceAllocationMasterId: number, organizationId: number): Observable<ApiResponse<never, ResourceAllocationDetail>> {
    return this.http.post<ApiResponse<never, ResourceAllocationDetail>>(`${this.base}/adt/getSingleResourceAllocation`, {
      resourceAllocationMasterId,
      organizationId,
    });
  }

  update(payload: ResourceAllocationUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateResourceAllocation`, payload);
  }

  updateStatus(payload: ResourceAllocationStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/changeResourceAllocationStatus`, payload);
  }
}

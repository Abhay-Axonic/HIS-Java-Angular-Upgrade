import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AmbulanceFacilityOption,
  AmbulanceMasterDetail,
  AmbulanceMasterRow,
  AmbulanceMasterSavePayload,
  AmbulanceMasterUpdatePayload,
  AmbulanceResourceAllocation,
  AmbulanceStatusPayload,
  AmbulanceTypeOption,
  ServiceProviderOption,
} from './ambulance-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/lis/global/ambulanceMasterController.js and
 * resources/assets/js/config/adt.url.config.js:63-111/402 -- not guessed.
 * Every call in the legacy controller goes through
 * `GenericService.serviceAction("POST", URI, data)` regardless of whether
 * it's a read or a write; confirmed backend-side too
 * (legacy/Global/src/main/java/com/param/global/controller/AmbulanceController.java
 * maps every one of these paths with `method = RequestMethod.POST`), so
 * this service uses POST uniformly rather than "upgrading" reads to GET.
 */
@Injectable({ providedIn: 'root' })
export class AmbulanceMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getAmbulanceTypeActiveList(organizationId: number): Observable<ApiResponse<never, AmbulanceTypeOption>> {
    return this.http.post<ApiResponse<never, AmbulanceTypeOption>>(`${this.base}/adt/getAmbulanceTypeActiveList`, {
      organizationId,
    });
  }

  getServiceProviderActiveList(organizationId: number): Observable<ApiResponse<never, ServiceProviderOption>> {
    return this.http.post<ApiResponse<never, ServiceProviderOption>>(`${this.base}/adt/getServiceProviderActiveList`, {
      organizationId,
    });
  }

  getAmbulanceFacilitiesActiveList(organizationId: number): Observable<ApiResponse<never, AmbulanceFacilityOption>> {
    return this.http.post<ApiResponse<never, AmbulanceFacilityOption>>(`${this.base}/adt/getAmbulanceFacilitiesActiveList`, {
      organizationId,
    });
  }

  /**
   * Parity target for the crash bug documented on the component -- the view
   * sends the ambulance-TYPE-NAME STRING as `resourceAllocationMasterId`
   * (ambulance-Master.html:15, ambulanceMasterController.js:106-124), even
   * though the field is a numeric primary key everywhere else on the
   * backend (`ResourceAllocationDto.resourceAllocationMasterId` is a Java
   * `int`, see legacy/Global/.../dto/ResourceAllocationDto.java). Typed as
   * `string` here to match what the view actually sends -- not "corrected"
   * to `number`.
   */
  getSingleResourceAllocation(
    resourceAllocationMasterId: string,
    organizationId: number,
  ): Observable<ApiResponse<never, AmbulanceResourceAllocation>> {
    return this.http.post<ApiResponse<never, AmbulanceResourceAllocation>>(`${this.base}/adt/getSingleResourceAllocation`, {
      resourceAllocationMasterId,
      organizationId,
    });
  }

  getAmbulanceMasterDataList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, AmbulanceMasterRow>> {
    return this.http.post<ApiResponse<never, AmbulanceMasterRow>>(`${this.base}/adt/getAmbulanceMasterDataList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // Fired unconditionally alongside the list fetch, matching
  // `initAmbulanceMasterList` -- see the component's class doc comment for
  // why the result is never rendered anywhere on this screen.
  getCount(organizationId: number): Observable<ApiResponse<number>> {
    // NOTE: /adt/getCountAmbulanceTypeList is NOT mapped in the backend's
    // AmbulanceController (checked the whole Global module) — the legacy
    // calls the same phantom. Degrade instead of toasting: the count is
    // auxiliary to the list; a missing count endpoint must not break the
    // screen's feeds (per-service path convention, DQ-092).
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getCountAmbulanceTypeList`, { organizationId })
      .pipe(catchError(() => of({ status: 'success', data: 0, message: '', listObject: null, errors: [] } as unknown as ApiResponse<number>)));
  }

  save(payload: AmbulanceMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveAmbulanceMasterData`, payload);
  }

  getSingle(ambulanceMasterId: number, organizationId: number): Observable<ApiResponse<never, AmbulanceMasterDetail>> {
    return this.http.post<ApiResponse<never, AmbulanceMasterDetail>>(`${this.base}/adt/getSingleAmbulanceMasterData`, {
      ambulanceMasterId,
      organizationId,
    });
  }

  update(payload: AmbulanceMasterUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateAmbulanceMasterData`, payload);
  }

  updateStatus(payload: AmbulanceStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/changeAmbulanceMasterDataStatus`, payload);
  }
}

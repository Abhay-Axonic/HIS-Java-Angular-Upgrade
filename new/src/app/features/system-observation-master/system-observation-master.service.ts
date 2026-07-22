import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  RawSpecialityOption,
  SaveSystemObservationPayload,
  SystemObservationFlatRow,
  SystemOption,
} from './system-observation-master.models';

/**
 * Endpoint paths and methods taken directly from
 * resources/assets/js/controller/opd/global/systemObservationMasterController.js
 * and resources/assets/js/config/opd.url.config.js. Backend chain confirmed
 * via full source read (Controller -> Service -> DAO, all present -- see
 * class comment in system-observation-master.ts):
 *   GET  /api/global/getListOfSystemObservationProperty  -> SystemObservationPropertyMasterController.java:49-62
 *   POST /adt/getActiveSpecialityList                     -> SpecialityMasterController.java:102-103
 *   POST /api/global/getListOfSystemMasterByType          -> SystemMasterController.java:114-127
 *   POST /api/global/saveSystemObservationProperty        -> SystemObservationPropertyMasterController.java:34-47
 *   POST /api/global/updateSystemObservationPropertyStatus -> SystemObservationPropertyMasterController.java:65-75
 *   POST /api/global/updateSystemObservationSinglePropertyStatus -> SystemObservationPropertyMasterController.java:77-87
 *
 * Several of these read `unit_id`/`organization_id`/`type_id` from custom
 * HTTP HEADERS rather than the request body (legacy's
 * `GenericServiceParamHeader.serviceAction`'s 6th `headerObj` argument,
 * ctrl:113-116/166-170/209-213/375-380) -- the Java side's own
 * `@RequestHeader(value="unit_id")`-style params confirm this is not
 * optional. `authInterceptor` (already global) still adds
 * Authorization/userId/unitId on top of these; the two don't conflict.
 *
 * `BASE_URL`/`ROOT_URL` are both `''` in this deployment, so these are the
 * complete paths.
 */
@Injectable({ providedIn: 'root' })
export class SystemObservationMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getListOfSystemObservationProperty(
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, SystemObservationFlatRow>> {
    const headers = new HttpHeaders({
      unit_id: String(unitId),
      organization_id: String(organizationId),
    });
    return this.http.get<ApiResponse<never, SystemObservationFlatRow>>(
      `${this.base}/api/global/getListOfSystemObservationProperty`,
      { headers },
    );
  }

  getActiveSpecialityList(
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, RawSpecialityOption>> {
    return this.http.post<ApiResponse<never, RawSpecialityOption>>(`${this.base}/adt/getActiveSpecialityList`, {
      organizationId,
      unitId,
    });
  }

  getSystemListByType(
    organizationId: number,
    unitId: number,
    typeId: number,
  ): Observable<ApiResponse<never, SystemOption>> {
    const headers = new HttpHeaders({
      unit_id: String(unitId),
      organization_id: String(organizationId),
      type_id: String(typeId),
    });
    return this.http.post<ApiResponse<never, SystemOption>>(
      `${this.base}/api/global/getListOfSystemMasterByType`,
      {},
      { headers },
    );
  }

  saveSystemObservationProperty(
    payload: SaveSystemObservationPayload,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse> {
    const headers = new HttpHeaders({
      organization_id: String(organizationId),
      unit_id: String(unitId),
    });
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveSystemObservationProperty`, payload, {
      headers,
    });
  }

  updateSystemObservationPropertyStatus(payload: {
    observationId: number;
    status: 'A' | 'I';
    updatedBy: number;
    updatedDate: string;
    unitId: number;
    organizationId: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(
      `${this.base}/api/global/updateSystemObservationPropertyStatus`,
      payload,
    );
  }

  updateSystemObservationSinglePropertyStatus(payload: {
    observationId: number;
    propertyId: number;
    status: 'A' | 'I';
    updatedBy: number;
    updatedDate: string;
    unitId: number;
    organizationId: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(
      `${this.base}/api/global/updateSystemObservationSinglePropertyStatus`,
      payload,
    );
  }
}

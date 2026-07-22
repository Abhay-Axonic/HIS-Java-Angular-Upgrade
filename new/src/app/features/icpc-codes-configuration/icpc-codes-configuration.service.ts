import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  IcpcCodesConfigurationRow,
  IcpcCodesConfigurationSavePayload,
  IcpcCodesConfigurationStatusPayload,
  IcpcConfigurationSpecialityDetail,
  IcpcSystemOption,
  SpecialityListByIcpcSystemRequest,
  SpecialityOption,
} from './icpc-codes-configuration.models';

/**
 * Endpoint paths and methods taken directly from
 * resources/assets/js/controller/master/unit/icpcCodesConfigurationController.js.
 * Backend chain confirmed via full source read (Controller -> Service ->
 * DAO, all present, no dead endpoints -- see class comment in
 * icpc-codes-configuration.ts):
 *   GET  /api/opd/getICPCSYSTEMList              -> MedicationController.java:293 (shared)
 *   POST /adt/getActiveSpecialityList             -> SpecialityMasterController.java:102-103
 *   POST /api/adt/saveICPCCodesConfigurationMapper -> ICPCCodesConfigurationMapperController.java
 *   POST /api/adt/getICPCCodesConfigurationMapperList -> ICPCCodesConfigurationMapperController.java
 *   POST /api/adt/updateICPCCodesConfigurationStatus -> ICPCCodesConfigurationMapperController.java
 *   POST /api/adt/getSpecialityListByIcpcSystemId  -> ICPCCodesConfigurationMapperController.java
 *
 * `BASE_URL`/`ROOT_URL` are both `''` in this deployment, so these are the
 * complete paths.
 */
@Injectable({ providedIn: 'root' })
export class IcpcCodesConfigurationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getIcpcSystemList(): Observable<ApiResponse<never, IcpcSystemOption>> {
    return this.http.get<ApiResponse<never, IcpcSystemOption>>(`${this.base}/api/opd/getICPCSYSTEMList`);
  }

  getActiveSpecialityList(
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, { specialityId: number; specialityName: string }>> {
    return this.http.post<ApiResponse<never, { specialityId: number; specialityName: string }>>(
      `${this.base}/adt/getActiveSpecialityList`,
      { organizationId, unitId },
    );
  }

  /** Fetches the ENTIRE active-status list in one call -- no offset/page
   * params exist on this endpoint (legacy paginates the full result
   * client-side, see the models.ts comment). */
  getList(organizationId: number, unitId: number): Observable<ApiResponse<never, IcpcCodesConfigurationRow>> {
    return this.http.post<ApiResponse<never, IcpcCodesConfigurationRow>>(
      `${this.base}/api/adt/getICPCCodesConfigurationMapperList`,
      { organizationId, unitId, status: 'A' },
    );
  }

  save(payload: IcpcCodesConfigurationSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/adt/saveICPCCodesConfigurationMapper`, payload);
  }

  updateStatus(payload: IcpcCodesConfigurationStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/adt/updateICPCCodesConfigurationStatus`, payload);
  }

  getSpecialityListByIcpcSystemId(
    payload: SpecialityListByIcpcSystemRequest,
  ): Observable<ApiResponse<never, IcpcConfigurationSpecialityDetail>> {
    return this.http.post<ApiResponse<never, IcpcConfigurationSpecialityDetail>>(
      `${this.base}/api/adt/getSpecialityListByIcpcSystemId`,
      payload,
    );
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  ActiveGenderOption,
  SystemGenderDetailRow,
  SystemGenderMapping,
  SystemMasterDetail,
  SystemMasterListRow,
} from './system-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/opd/global/systemMasterController.js and
 * resources/assets/js/config/opd.url.config.js:110-114 /
 * adt.url.config.js:170 -- not guessed. All 7 confirmed against a real
 * Java implementation: SystemMasterController.java
 * (@RequestMapping("api/global")) -> SystemMasterServiceImpl ->
 * SystemMasterDaoImpl (the gender-list call goes through the separate
 * GenderMasterController, @RequestMapping("/adt")).
 *
 * `unit_id`/`organization_id` HEADERS (snake_case, not the usual camelCase
 * `unitId`/`organizationId` the global authInterceptor already attaches)
 * are required by 3 of these endpoints -- confirmed against the Java
 * controller's own `@RequestHeader(value = "unit_id")` /
 * `@RequestHeader(value = "organization_id")` params on
 * `getListOfSystemMaster`/`saveSystemMaster`/`updateSystemMaster` (legacy's
 * own `GenericServiceParamHeader` service variant sends these explicitly
 * per-call, same convention already established in this codebase's
 * advised-visits.service.ts). The other endpoints
 * (`updateSystemMasterStatus`, `getListOfSystemMasterById`,
 * `getSystemGenderListById`) take no `@RequestHeader` params at all.
 */
@Injectable({ providedIn: 'root' })
export class SystemMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** GENDERMASTERCONTROLLER's `/adt/getActiveGenderList` -- unfiltered, ignores any body/params server-side. */
  getActiveGenderList(): Observable<ApiResponse<never, ActiveGenderOption>> {
    return this.http.get<ApiResponse<never, ActiveGenderOption>>(`${this.base}/adt/getActiveGenderList`);
  }

  getList(unitId: number, organizationId: number): Observable<ApiResponse<never, SystemMasterListRow>> {
    return this.http.get<ApiResponse<never, SystemMasterListRow>>(`${this.base}/api/global/getListOfSystemMaster`, {
      headers: { unit_id: String(unitId), organization_id: String(organizationId) },
    });
  }

  create(payload: Record<string, unknown>, unitId: number, organizationId: number): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveSystemMaster`, payload, {
      headers: { unit_id: String(unitId), organization_id: String(organizationId) },
    });
  }

  update(payload: Record<string, unknown>, unitId: number, organizationId: number): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateSystemMaster`, payload, {
      headers: { unit_id: String(unitId), organization_id: String(organizationId) },
    });
  }

  updateStatus(payload: {
    systemId: number;
    status: 'A' | 'I';
    updatedBy: number;
    updatedDate: string;
    unitId: number;
    organizationId: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateSystemMasterStatus`, payload);
  }

  getById(
    systemId: number,
    unitId: number,
    organizationId: number,
  ): Observable<ApiResponse<SystemMasterDetail, SystemGenderMapping>> {
    return this.http.post<ApiResponse<SystemMasterDetail, SystemGenderMapping>>(
      `${this.base}/api/global/getListOfSystemMasterById`,
      { systemId, unitId, organizationId },
    );
  }

  getGenderDetailsById(
    systemId: number,
    unitId: number,
    organizationId: number,
  ): Observable<ApiResponse<never, SystemGenderDetailRow>> {
    return this.http.post<ApiResponse<never, SystemGenderDetailRow>>(`${this.base}/api/global/getSystemGenderListById`, {
      systemId,
      unitId,
      organizationId,
    });
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ModuleOption, QualityParameterMasterRecord, UomOption } from './quality-parameter-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/qualityParameterMasterController.js
 * and resources/assets/js/config/lis.url.config.js / constants/constants.js
 * (`LIST_UNIT_MASTER = "/listUnitMaster"`, `LIS_GLOBAL = "/LIS/global"`,
 * `MODULE_API_BY_PARENT = root + '/module/getZeroParentModule'`), and the
 * live `QualityParameterMasterController.java` (`@RequestMapping("/api/unit")`)
 * -- not guessed.
 *
 * `authInterceptor` (new/src/app/core/auth.interceptor.ts) is already wired
 * globally in app.config.ts -- every HttpClient call here automatically gets
 * the Authorization/userId/unitId headers legacy's GenericService.serviceAction
 * attaches. Nothing to add here.
 */
@Injectable({ providedIn: 'root' })
export class QualityParameterMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** ctrl:39-58 `getUOMList()`. */
  getUomList(organizationId: number): Observable<ApiResponse<never, UomOption>> {
    return this.http.get<ApiResponse<never, UomOption>>(
      `${this.base}/LIS/global/listUnitMaster/${organizationId}`,
    );
  }

  /** ctrl:66-80 `getModules()`. */
  getModuleList(): Observable<ApiResponse<never, ModuleOption>> {
    return this.http.get<ApiResponse<never, ModuleOption>>(`${this.base}/module/getZeroParentModule`);
  }

  /** ctrl:283-300 `getQualityParamList()`. No offset/paging params -- legacy loads the full list. */
  getList(unitId: number, organizationId: number): Observable<ApiResponse<never, QualityParameterMasterRecord>> {
    return this.http.get<ApiResponse<never, QualityParameterMasterRecord>>(
      `${this.base}/api/unit/getQualityParamList/${unitId}/${organizationId}`,
    );
  }

  /** ctrl:164-226 `saveQualityParam()` (handles both create and update, keyed by `update: 'Y'|'N'`). */
  save(payload: Partial<QualityParameterMasterRecord>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/unit/saveQualityParam`, payload);
  }

  /** ctrl:305-325 `updateStatus()`. */
  updateStatus(payload: {
    unitId: number;
    organizationId: number;
    qualityParameterMasterId: number;
    status: 'A' | 'I';
    update: 'Y';
    updatedBy: number | '';
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/unit/updateStatus`, payload);
  }
}

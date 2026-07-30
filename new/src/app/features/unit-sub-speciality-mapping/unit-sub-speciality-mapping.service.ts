import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AvailableSubSpeciality, UnitSpecialityOption, UnitSubSpecialityRow } from './unit-sub-speciality-mapping.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/adt.url.config.js (constants referenced by
 * subDepartmentMasterUnitController.js) and confirmed live via
 * `tools/golden-trace/capture.js` (Step 2 -- see class doc on the
 * component for the captured call order). Backend controllers:
 * `com.param.adt.master.unit.controller.UnitSubSpecialityController`
 * (`@RequestMapping("/adt")`, ADT module) for everything except the picker
 * endpoint, which is `com.param.global.controller.SubSpecialityMasterController`
 * (Global module, also `@RequestMapping("/adt")`).
 *
 * Re-audit note: `UnitSubSpecialityController` also exposes
 * `getActiveUnitSubSpecialityList` and `getSubSpecialityBySpecialityId`
 * (and 2 more `...ForTariff`/`...ByArray` variants) -- all real endpoints
 * on the SAME controller class this screen's other calls use, but NONE of
 * them are invoked anywhere in `subDepartmentMasterUnitController.js`
 * (confirmed by a full read of that file and by the golden-trace capture,
 * which shows exactly 3 calls firing on load, matching the on-demand calls
 * read from source). They are almost certainly consumed by other,
 * unrelated screens sharing the same backend controller class. Deliberately
 * NOT wired here -- wiring unused endpoints would be scope creep with no
 * screen behavior to justify it.
 */
@Injectable({ providedIn: 'root' })
export class UnitSubSpecialityMappingService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ---- init-time calls (golden-trace confirmed, in this dispatch order) ----
  getActiveUnitSpecialityList(organizationId: number, unitId: number): Observable<ApiResponse<never, UnitSpecialityOption>> {
    return this.http.post<ApiResponse<never, UnitSpecialityOption>>(`${this.base}/adt/getActiveUnitSpecialityList`, {
      organizationId,
      unitId,
    });
  }

  getUnitSubSpecialityList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, UnitSubSpecialityRow>> {
    return this.http.post<ApiResponse<never, UnitSubSpecialityRow>>(`${this.base}/adt/getUnitSubSpecialityList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getUnitSubSpecialityCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getUnitSubSpecialityCount`, {
      organizationId,
      unitId,
    });
  }

  // ---- picker (on Department dropdown change) ----
  getSubspecialityNotInUnit(
    organizationId: number,
    unitId: number,
    specialityId: number,
  ): Observable<ApiResponse<never, AvailableSubSpeciality>> {
    return this.http.get<ApiResponse<never, AvailableSubSpeciality>>(
      `${this.base}/adt/getSubspecialityNotInUnit/orgId/${organizationId}/unitId/${unitId}/specialityId/${specialityId}`,
    );
  }

  // ---- save (bulk-add selected sub-specialities under one speciality) ----
  saveUnitSubSpecialtiy(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveUnitSubSpecialtiy`, payload);
  }

  // ---- status toggle ----
  updateStatusForUnitSubSpeciality(payload: {
    unitSubSpecialityId: number;
    status: 'A' | 'I';
    organizationId: number;
    unitId: number;
    updatedBy: number;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForUnitSubSpeciality`, payload);
  }

  // ---- server-side universal search (specialityName param matches code/name/subcode/subname, see class doc) ----
  unitSubDepartmentMasterUniversalSearch(
    organizationId: number,
    unitId: number,
    specialityName: string,
  ): Observable<ApiResponse<never, UnitSubSpecialityRow>> {
    return this.http.post<ApiResponse<never, UnitSubSpecialityRow>>(
      `${this.base}/adt/unitSubDepartmentMasterUniversalSearch`,
      { organizationId, unitId, specialityName },
    );
  }
}

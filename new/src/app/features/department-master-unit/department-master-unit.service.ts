import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  DepartmentMasterUnitRow,
  SaveDepartmentMasterUnitPayload,
  SpecialityCandidate,
  UniversalSearchPayload,
  UpdateDetailsPayload,
  UpdateStatusPayload,
} from './department-master-unit.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/config/adt.url.config.js` (`GETORGACTIVESPECIALITYLIST`,
 * `SAVEUNITSPECIALTIY`, `GETUNITSPECIALITYLIST`, `GETUNITSPECIALITYCOUNT`,
 * `UPDATESTATUSFORUNITSPECIALITYLIST`, plus 2 inline string-literal URIs in
 * the controller itself for `updateUnitSpecialtyMapperDetails`/
 * `unitDepartmentMasterUniversalSearch`, ctrl:186-187/375) -- all under
 * `/adt` (`BASE_URL`/`ROOT_URL` both empty strings in this deployment).
 * Confirmed real end-to-end by reading source directly:
 * `UnitSpecialityController.java` (`getOrgActiveSpecialityList` actually
 * lives on the sibling `SpecialityMasterController`, also `@RequestMapping
 * ("/adt")`) -> `UnitSpecialityServiceImpl`/`SpecialityServiceImpl` ->
 * `UnitSpecialityDaoImpl`/`SpecialityMasterDaoImpl` -> table
 * `t_unit_speciality_mapper`. All 7 calls below confirmed live 200 this
 * session via a real golden trace (template fetch + `getOrgActiveSpecialityList`
 * + `getUnitSpecialityList` + `getUnitSpecialityCount` on load, all 200,
 * unchained/parallel -- matching the shape reproduced by `loadCandidates`/
 * `loadListAndCount` in `department-master-unit.ts`).
 *
 * All 7 calls use POST -- legacy never uses PUT/GET here (unlike some
 * sibling screens this batch), confirmed by reading every
 * `BillingGenericService.serviceAction`/`GenericService.serviceAction` call
 * site in the controller directly.
 *
 * NOTE on toast provenance (relevant to how `department-master-unit.ts`
 * surfaces messages): `saveUnitSpecialtiy`/`updateStatusForUnitSpecialityList`
 * go through `BillingGenericService`, whose own `serviceAction` (see
 * `resources/assets/js/service/GenericService.js:493-579`) shows a growl
 * automatically from `response.data.message` whenever
 * `notificationStatus===true` is passed (both these calls DO pass it,
 * ctrl:118/146) -- the controller itself never calls growl for either.
 * `updateUnitSpecialtyMapperDetails` goes through the PLAIN `GenericService`
 * instead, which has NO such auto-toast wrapper at all -- every message
 * shown for that call is the controller's own explicit, hardcoded (and
 * typo'd) `growl.success`/`growl.error` literal (ctrl:194-225).
 * `unitDepartmentMasterUniversalSearch` is called with only 3 args (no
 * `notificationStatus` at all) -- confirmed no toast of any kind fires for
 * search, ever, success or failure.
 */
@Injectable({ providedIn: 'root' })
export class DepartmentMasterUnitService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:52-68 (getSpecialityMasterList) -> `/adt/getOrgActiveSpecialityList`
  // (SpecialityMasterController.java:122-131 -> SpecialityServiceImpl ->
  // SpecialityMasterDaoImpl.getOrgActiveSpecialityList, the
  // GET_ORG_ACTIVE_SPECIALITY_LIST_NOT_MAPPED_TO_UNIT named native query).
  getCandidates(organizationId: number, unitId: number): Observable<ApiResponse<never, SpecialityCandidate>> {
    return this.http.post<ApiResponse<never, SpecialityCandidate>>(`${this.base}/adt/getOrgActiveSpecialityList`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:100-131 (saveDeptAtUnitLevel).
  save(payload: SaveDepartmentMasterUnitPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveUnitSpecialtiy`, payload);
  }

  // ctrl:304-326/351-361 (initBillingGroupMasterList's list half).
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, DepartmentMasterUnitRow>> {
    return this.http.post<ApiResponse<never, DepartmentMasterUnitRow>>(`${this.base}/adt/getUnitSpecialityList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:304-326 (initBillingGroupMasterList's count half -- org+unit
  // scoped both sides, `UnitSpecialityDaoImpl.getCount:164-181`, unlike
  // some sibling screens' mismatched list/count scoping this batch found).
  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getUnitSpecialityCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:134-152 (updateStatus).
  updateStatus(payload: UpdateStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForUnitSpecialityList`, payload);
  }

  // ctrl:166-231 (updateDetails, the Details-modal Update action). See
  // `department-master-unit.models.ts`'s `UpdateDetailsPayload` doc
  // comment for the faithfully-reproduced `allowedOverbookingSlots` typo
  // bug this call can trigger.
  updateDetails(payload: UpdateDetailsPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateUnitSpecialtyMapperDetails`, payload);
  }

  // ctrl:365-388 (searchDepartmentMasterUnit).
  search(payload: UniversalSearchPayload): Observable<ApiResponse<never, DepartmentMasterUnitRow>> {
    return this.http.post<ApiResponse<never, DepartmentMasterUnitRow>>(
      `${this.base}/adt/unitDepartmentMasterUniversalSearch`,
      payload,
    );
  }
}

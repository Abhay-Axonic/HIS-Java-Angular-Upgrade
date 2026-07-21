import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { GeneralLedgerOption, SpecialityDetail, SpecialityListRow } from './speciality-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/specialityMasterController.js and
 * resources/assets/js/config/adt.url.config.js / global.url.config.js --
 * not guessed:
 *  - ADDSPECIALITY            = /adt/saveSpecialtiyMaster   (typo preserved -- real path)
 *  - UPDATESPECIALITY         = /adt/updateSpecialityMaster
 *  - GETSINGLESPECIALITY      = /adt/getSpecialityById
 *  - UPDATESPECIALITYSTATUS   = /adt/updateStatus
 *  - COUNTSPECIALITYMASTER    = /adt/getSpecialityCount
 *  - GETGENERALLEDGERMASTERACTIVELIST = /org/getGeneralLedgerMasterActiveList
 * moduleId 372 (app.js:3204-3230).
 */
@Injectable({ providedIn: 'root' })
export class SpecialityMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getSpecialityCount`, { organizationId });
  }

  getList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, SpecialityListRow>> {
    return this.http.post<ApiResponse<never, SpecialityListRow>>(
      `${this.base}/adt/getSpecialityListOfActiveInactive`,
      { organizationId, offset, noOfRecordsPerPage },
    );
  }

  search(organizationId: number, specialityName: string): Observable<ApiResponse<never, SpecialityListRow>> {
    return this.http.post<ApiResponse<never, SpecialityListRow>>(`${this.base}/adt/departmentMasterUniversalSearch`, {
      organizationId,
      specialityName,
    });
  }

  /**
   * Same endpoint serves both the unfiltered load-on-init call
   * (`initSpecialityMasterList`, ctrl:140-156 -- call-but-don't-render: its
   * own `<select>` is commented out in the view, view:53-57, the only
   * reachable ledger UI is the typeahead below) and the typeahead search
   * (`ledgerNameSearch`, ctrl:467-484), which passes `generalLedgerName` as
   * the request body. Mirrors this codebase's insurance-master
   * `getGeneralLedgerList`.
   */
  getGeneralLedgerList(
    organizationId: number,
    generalLedgerName?: string,
  ): Observable<ApiResponse<never, GeneralLedgerOption>> {
    const body = generalLedgerName ? { generalLedgerName } : {};
    return this.http.post<ApiResponse<never, GeneralLedgerOption>>(
      `${this.base}/org/getGeneralLedgerMasterActiveList/${organizationId}`,
      body,
    );
  }

  getById(specialityId: number): Observable<ApiResponse<never, SpecialityDetail>> {
    return this.http.post<ApiResponse<never, SpecialityDetail>>(`${this.base}/adt/getSpecialityById`, {
      specialityId,
    });
  }

  create(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveSpecialtiyMaster`, payload);
  }

  update(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateSpecialityMaster`, payload);
  }

  /**
   * Confirmed against the live Java backend (SpecialityMasterController
   * /updateStatus -> SpecialityServiceImpl.updateStatus): the "mapped with
   * unit" refusal is a plain `Response` POJO with `code: "409"` inside an
   * ordinary HTTP 200 body (`@Controller`/`@ResponseBody`, no
   * `ResponseEntity`/`@ResponseStatus` anywhere) -- never a real HTTP 409.
   * So this is always a `next()` case for HttpClient, never `error()`. See
   * toggleStatus() in the component for the revert-on-409-only handling
   * (ctrl:342-393).
   */
  updateStatus(payload: {
    specialityId: number;
    status: 'A' | 'I';
    updatedBy: number;
    updatedDate: string;
    organizationId: number;
  }): Observable<ApiResponse & { code?: string }> {
    return this.http.post<ApiResponse & { code?: string }>(`${this.base}/adt/updateStatus`, payload);
  }
}

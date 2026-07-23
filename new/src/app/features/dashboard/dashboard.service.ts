import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  BedOccupancyRow,
  BillingByVisitTypeRow,
  DepartmentNursingStationEntry,
  NursingStationOption,
  NursingStationWard,
  OpVisitsCountRow,
  VisitTypeSeriesRow,
} from './dashboard.models';

/**
 * The real backend calls `CEOdashbordController.js` makes for this screen's
 * 4 working charts, plus the 2 shared nursing-station calls. Independently
 * verified against the real Java source (not just the frontend call site):
 * `legacy/Global/src/main/java/com/param/global/controller/CEODashboardController.java`
 * maps every one of the 5 chart-data endpoints below as
 * `@RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)`,
 * body deserialized into `BillStatDto` -- only `organizationId`/`unitId` are
 * actually read by the DAO (`CEODashboardDaoImpl`) for these 5 calls, the
 * rest of that DTO's ~25 fields are unused by them. This confirms the
 * legacy frontend's own `GenericService.serviceAction("POST", URI, {organizationId, unitId})`
 * call shape is the real contract, not a frontend mistake -- POST with a
 * body, not GET, despite how these might be casually described elsewhere.
 * `authInterceptor` (already global) attaches the Authorization/userId/
 * unitId headers every call needs.
 *
 * `getFinanceDetailsCount` (ctrl:384-516) is deliberately NOT included
 * here -- see dashboard.ts's class doc for why (it feeds a chart whose own
 * canvas is commented out in the legacy template, a confirmed dead call
 * with no UI to render it into).
 */
@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** ctrl:520-645 -- feeds the Operations panel's Diagnostics chart
   * (Lab/Radiology operation counts). */
  getOperationCountList(
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, VisitTypeSeriesRow>> {
    return this.http.post<ApiResponse<never, VisitTypeSeriesRow>>(
      `${this.base}/api/global/getOperationCountList`,
      {
        organizationId,
        unitId,
      },
    );
  }

  /** ctrl:648-782 -- feeds the Administration panel's FIRST (correctly
   * labeled) chart: Admission/Discharge/Death counts. */
  getAdmissionDischargeDeathCount(
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, VisitTypeSeriesRow>> {
    return this.http.post<ApiResponse<never, VisitTypeSeriesRow>>(
      `${this.base}/api/global/getAdmissionDischargeDeathCount`,
      { organizationId, unitId },
    );
  }

  /** ctrl:784-885 -- real content is OP-visit counts by Primary/Follow-Up/
   * Secondary. Feeds the Administration panel's SECOND chart -- the
   * faithfully-reproduced heading/content mismatch, see dashboard.ts. */
  getOPVisitsCount(
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, OpVisitsCountRow>> {
    return this.http.post<ApiResponse<never, OpVisitsCountRow>>(
      `${this.base}/api/global/getOPVisitsCount`,
      {
        organizationId,
        unitId,
      },
    );
  }

  /** ctrl:1153-1219 -- feeds the 4 real Finance pies (OP/IP/DC/ER,
   * payment-mode-by-amount breakdown). */
  getBillingListByVisitType(
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, BillingByVisitTypeRow>> {
    return this.http.post<ApiResponse<never, BillingByVisitTypeRow>>(
      `${this.base}/api/global/getBillingListByVisitType`,
      { organizationId, unitId },
    );
  }

  /** ctrl:1221-1314 -- feeds the Operations panel's Bed Occupancy chart. */
  getDashboardBedOccupancyList(
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, BedOccupancyRow>> {
    return this.http.post<ApiResponse<never, BedOccupancyRow>>(
      `${this.base}/api/global/getDashboardBedOccupancyList`,
      { organizationId, unitId },
    );
  }

  /** ctrl:98-137 -- fired on init when `nursingStationsWardsList` isn't
   * already cached in sessionStorage. Same endpoint as `doctor-dashboard`/
   * `dashboard-info`'s own copies. */
  getDetailedNursingStationList(
    userId: number,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, NursingStationOption>> {
    return this.http.get<ApiResponse<never, NursingStationOption>>(
      `${this.base}/api/global/getDetailedNursingStationList/${userId}/${organizationId}/${unitId}`,
    );
  }

  /** ctrl:114-136, :156-171 -- same endpoint, two call sites (auto-select
   * and the modal's Ok button), one method. */
  getDeptListByNursingStationId(
    nursingStationId: number,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<NursingStationWard[], DepartmentNursingStationEntry>> {
    return this.http.get<ApiResponse<NursingStationWard[], DepartmentNursingStationEntry>>(
      `${this.base}/api/global/getDeptListByNursingStationId/${nursingStationId}/${organizationId}/${unitId}`,
    );
  }
}

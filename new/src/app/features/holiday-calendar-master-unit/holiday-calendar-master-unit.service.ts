import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  GlobalCalendarEntry,
  SaveUnitHolidayCalendarPayload,
  UnitHolidayCalendarCountRequest,
  UnitHolidayCalendarListRequest,
  UnitHolidayCalendarRow,
  UpdateUnitHolidayCalendarStatusPayload,
} from './holiday-calendar-master-unit.models';

/**
 * Endpoint paths taken directly from `holidayCalendarMasterUnitController.js`
 * (constants in `config/adt.url.config.js:664-668`) and
 * `UnitHolidayCalenderController.java`
 * (legacy/Global/src/main/java/com/param/global/controller/, mapped under
 * `/adt`) -- not guessed. Full chain confirmed real for all 5 endpoints
 * below: Controller -> `IUnitHolidayCalenerService`
 * (`UnitHolidayCalenderServiceImpl`) -> `IUnitHolidayCalenderDao`
 * (`UnitHolidayCalenderDaoImpl`, table `public.t_unit_holiday_calender_mapper`).
 *
 * **Entity duplicate risk, resolved (task's stated caveat):** two Java
 * classes are both named `UnitHolidayCalenderMapper`
 * (`com.param.entity.model.master.UnitHolidayCalenderMapper` and
 * `com.param.global.model.UnitHolidayCalenderMapper`). Confirmed via
 * `grep -rl` across every `legacy/<module>/src/main/java` tree: the
 * `com.param.entity.model.master` one has **zero references anywhere** in
 * the codebase (dead, unused, never imported by any DAO/service/mapping) --
 * the real write-path entity, the only one `UnitHolidayCalenderDaoImpl`
 * ever imports/extends `GenericDao<...>` with, is
 * `com.param.global.model.UnitHolidayCalenderMapper`
 * (`@Table(name="t_unit_holiday_calender_mapper", schema="public")`).
 * Unlike BR-040's `CoversheetAccessMaster`/`CustomEMRSubCategoryMaster`
 * case, this is unambiguous, not a live dual-write risk.
 *
 * `getActiveUnitHolidayCalenerList` is ALSO a real, fully-implemented
 * backend endpoint (confirmed same chain) but is never called anywhere in
 * `holidayCalendarMasterUnitController.js` -- deliberately NOT modeled
 * here, matching legacy's actual (unused) behavior.
 */
@Injectable({ providedIn: 'root' })
export class HolidayCalendarMasterUnitService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:54-71 (`getActiveHolidayCalenderList`) -- POST, silent (legacy
  // passes NOTIFICATION_MSG_STATUS_FALSE, no auto-growl either way).
  getHolidayCalendarListNotMappedToUnit(
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, GlobalCalendarEntry>> {
    return this.http.post<ApiResponse<never, GlobalCalendarEntry>>(
      `${this.base}/adt/getHolidayCalenderListNotMappedToUnit`,
      { organizationId, unitId },
    );
  }

  // ctrl:154-177/180-198 (`initMasterList`/`getList`) -- POST, silent.
  getUnitHolidayCalendarList(
    payload: UnitHolidayCalendarListRequest,
  ): Observable<ApiResponse<never, UnitHolidayCalendarRow>> {
    return this.http.post<ApiResponse<never, UnitHolidayCalendarRow>>(
      `${this.base}/adt/getUnitHolidayCalenerList`,
      payload,
    );
  }

  // ctrl:154-177 (`initMasterList`) -- POST, silent. Count in `object`.
  getUnitHolidayCalendarCount(payload: UnitHolidayCalendarCountRequest): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getUnitHolidayCalenerCount`, payload);
  }

  /**
   * ctrl:106-125 (`save`) -- POST, real create. Legacy passes
   * NOTIFICATION_MSG_STATUS_TRUE and never calls `growl` itself anywhere in
   * this controller (confirmed via full-file grep, zero `growl.`
   * references) -- every toast for this screen comes entirely from
   * `BillingGenericService.serviceAction`'s own built-in auto-notification
   * logic (`GenericService.js:519-550`): success shows `response.data.message`
   * only if non-null; an error response with `code === "409"` shows as a
   * WARNING instead of a plain error. Reproduced in the component via
   * `notifyLikeBillingGenericService()`. `code` typed here since the shared
   * service's logic depends on it, even though this screen's own
   * save/updateStatus never actually set it (see class doc comment).
   */
  saveUnitHolidayCalendar(payload: SaveUnitHolidayCalendarPayload): Observable<ApiResponse & { code?: string }> {
    return this.http.post<ApiResponse & { code?: string }>(`${this.base}/adt/saveUnitHolidayCalener`, payload);
  }

  // ctrl:128-146 (`updateStatus`) -- POST, real status toggle. Same
  // auto-growl-via-BillingGenericService shape as save() above.
  updateStatusForUnitHolidayCalendar(
    payload: UpdateUnitHolidayCalendarStatusPayload,
  ): Observable<ApiResponse & { code?: string }> {
    return this.http.post<ApiResponse & { code?: string }>(
      `${this.base}/adt/updateStatusForUnitHolidayCalener`,
      payload,
    );
  }
}

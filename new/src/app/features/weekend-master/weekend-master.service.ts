import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  DayLookupPayload,
  DayLookupRow,
  WeekendMasterDetail,
  WeekendMasterRow,
  WeekendMasterSavePayload,
  WeekendMasterStatusPayload,
  WeekendMasterUpdatePayload,
} from './weekend-master.models';

/**
 * Endpoint paths taken directly from `WeekendMasterController.java`
 * (Global module, mapped under `/api/global/unit`) and
 * `weekendMasterController.js` -- not guessed. `WeekendMasterController`
 * also exposes `/getWeekendMasterList` (POST, no offset/size) and
 * `getWeekendMasterByName`/`getWeekendMasterByNameNotId` (DAO-level only,
 * no controller mapping) -- none of these are called anywhere in
 * weekendMasterController.js, so they are intentionally not modeled here.
 *
 * The date-lookup call (`getActiveDayListById`) is on a completely
 * different Java controller (`HolidayCalenderMasterController`, mapped
 * under `/adt`) via the shared `GETDAYOBJ` constant
 * (`config/adt.url.config.js:318`) -- also used by
 * `holidayCalenderMasterController.js`, a different screen. Reused here
 * exactly as legacy does.
 */
@Injectable({ providedIn: 'root' })
export class WeekendMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:109-148 -- POST { organizationId, offset, noOfRecordsPerPage, unitId }.
  getListOfWeekendMaster(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, WeekendMasterRow>> {
    return this.http.post<ApiResponse<never, WeekendMasterRow>>(`${this.base}/api/global/unit/getListOfWeekendMaster`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:122-125/139-146 -- POST { organizationId, unitId }, count returned in `object`.
  getWeekendMasterCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/global/unit/getWeekendMasterCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:197-262 -- POST, real create (only actually fired once the confirm
  // modal's Ok is clicked -- see weekend-master.ts's class comment).
  saveWeekendMaster(payload: WeekendMasterSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/saveWeekendMaster`, payload);
  }

  // ctrl:264-296 -- GET, weekendId/organizationId/unitId as path segments;
  // response.data.listObject[0] (NOT object) is the single record.
  getWeekendMasterById(
    weekendId: number,
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, WeekendMasterDetail>> {
    return this.http.get<ApiResponse<never, WeekendMasterDetail>>(
      `${this.base}/api/global/unit/getWeekendMasterById/${weekendId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:299-350 -- POST, real update. No confirm-modal step, unlike save.
  updateWeekendMaster(payload: WeekendMasterUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/updateWeekendMaster`, payload);
  }

  // ctrl:352-388 -- POST, real status toggle.
  updateWeekendMasterStatus(payload: WeekendMasterStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/unit/updateWeekendMasterStatus`, payload);
  }

  // ctrl:77-100 (`test()`) -- POST { dayId }, fired when the user picks a
  // date on the datepicker; response.listObject[0].day is the display name.
  getActiveDayListById(payload: DayLookupPayload): Observable<ApiResponse<never, DayLookupRow>> {
    return this.http.post<ApiResponse<never, DayLookupRow>>(`${this.base}/adt/getActiveDayListById`, payload);
  }
}

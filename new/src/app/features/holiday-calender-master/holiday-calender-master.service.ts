import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { DayObj, HolidayDetail, HolidayRow } from './holiday-calender-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/adt.url.config.js and
 * resources/assets/js/controller/adt/global/holidayCalenderMasterController.js
 * -- not guessed. Full chain confirmed real end-to-end:
 * HolidayCalenderMasterController.java -> HolidayCalenderMasterServiceImpl.java
 * -> HolidayCalenderMasterDaoImpl.java for all 6 calls this screen makes.
 * `adt.m_holiday_calender_master` confirmed present in this dev DB (4 rows,
 * though all 4 have a NULL organization_id -- see class comment in
 * holiday-calender-master.ts).
 */
@Injectable({ providedIn: 'root' })
export class HolidayCalenderMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:136-184 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getHolidayList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, HolidayRow>> {
    return this.http.post<ApiResponse<never, HolidayRow>>(`${this.base}/adt/getHolidayCalenderMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:169-184 -- POST { organizationId }, count returned in `object`.
  getHolidayCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getHolidayCount`, { organizationId });
  }

  // ctrl:320-388 -- POST { holidayCalenderId }; response.data.listObject[0] is the record.
  getHolidayById(holidayCalenderId: number): Observable<ApiResponse<never, HolidayDetail>> {
    return this.http.post<ApiResponse<never, HolidayDetail>>(`${this.base}/adt/getHolidayCalenderById`, { holidayCalenderId });
  }

  // ctrl:50-72/362-377 -- POST { dayId }. Legacy also special-cases
  // `dayId===0` by ADDING a stray `day: 7` field to the same payload
  // (ctrl:355-358) -- confirmed dead/no-op: GET_ACTIVE_DAY_LIST_BY_ID only
  // ever binds `:dayId`, never reads a `day` param. Not reproduced (inert
  // no-op, same treatment as this codebase's other dropped dead artifacts).
  getDayById(dayId: number): Observable<ApiResponse<never, DayObj>> {
    return this.http.post<ApiResponse<never, DayObj>>(`${this.base}/adt/getActiveDayListById`, { dayId });
  }

  // ctrl:246-317 -- POST, real create.
  saveHoliday(payload: {
    holidayCalenderCode: string;
    holidayCalenderDesc: string;
    holidayDate: string;
    dayId: number;
    createdBy: number;
    updatedBy: number;
    organizationId: number;
    status: 'A';
    createdDate: string;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveHolidayCalenderMaster`, payload);
  }

  // ctrl:431-496 -- POST, real update. No confirm step (unlike Save).
  updateHoliday(payload: {
    holidayCalenderId: number;
    holidayCalenderCode: string;
    holidayCalenderDesc: string;
    holidayDate: string;
    dayId: number;
    updatedBy: number;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateHolidayCalenderMaster`, payload);
  }

  // ctrl:391-428 -- POST, real status toggle.
  updateHolidayStatus(holidayCalenderId: number, status: 'A' | 'I', updatedBy: number, updatedDate: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForHolidayCalender`, {
      holidayCalenderId,
      status,
      updatedBy,
      updatedDate,
    });
  }
}

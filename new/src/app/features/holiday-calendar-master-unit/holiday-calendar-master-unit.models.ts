/**
 * Field set taken directly from
 * views/master/UnitMaster/organizationMaster/holiday-calendar-master-unit.html
 * + resources/assets/js/controller/adt/unit/holidayCalendarMasterUnitController.js
 * (`holidayCalendarMasterUnitController`, plain `$scope`, no `controllerAs`)
 * and the backend `UnitHolidayCalenderMapperDto`
 * (legacy/Global/src/main/java/com/param/global/dto/) plus the
 * `HolidayCalenderMasterDto`-shaped native-query result for the
 * not-mapped-to-unit list. moduleId 536 (app.js:8497-8523).
 */

/** A GLOBAL calendar entry not yet mapped to this unit -- left-hand
 * "Global Master" checkbox list, from
 * `/adt/getHolidayCalenderListNotMappedToUnit`
 * (`GET_HOLIDAY_CALENDER_LIST_NOT_MAPPED_TO_UNIT`, a native query aliased
 * onto `HolidayCalenderMasterDto`). */
export interface GlobalCalendarEntry {
  holidayCalenderId: number;
  holidayCalenderDesc: string;
  holidayDate: string;
  day: string;
}

/** One entry queued for mapping (checked in the left list, then moved to
 * the right-hand "Unit Master" preview via the "<<>>" button) -- mirrors
 * legacy's `tempSelectItem`/`selectedListItem` object shape
 * (`addTempItem`, ctrl:90-103) exactly: only these two fields, nothing
 * else, even though the source entry has more. */
export interface SelectedCalendarItem {
  holidayId: number;
  holidayCalenderDesc: string;
}

/** A row already mapped to this unit and saved -- the main table, from
 * `/adt/getUnitHolidayCalenerList` (`GET_UNIT_HOLIDAY_CALENDER_LIST`). */
export interface UnitHolidayCalendarRow {
  unitHolidayCalenderId: number;
  holidayId: number;
  holidayCalenderCode: string;
  holidayCalenderDesc: string;
  holidayDate: string;
  day: string;
  status: 'A' | 'I';
}

export interface UnitHolidayCalendarListRequest {
  organizationId: number;
  unitId: number;
  offset: number;
  noOfRecordsPerPage: number;
}

export interface UnitHolidayCalendarCountRequest {
  organizationId: number;
  unitId: number;
}

/** POST body for /adt/saveUnitHolidayCalener (`$scope.masterObj`,
 * ctrl:39-51/106-125). */
export interface SaveUnitHolidayCalendarPayload {
  status: 'A';
  createdDate: string;
  createdBy: number;
  updatedDate: string;
  updatedBy: number;
  unitId: number;
  organizationId: number;
  unitHolidayCalenderMapperDtosList: SelectedCalendarItem[];
}

/** POST body for /adt/updateStatusForUnitHolidayCalener (ctrl:128-146). */
export interface UpdateUnitHolidayCalendarStatusPayload {
  unitHolidayCalenderId: number;
  status: 'A' | 'I';
  updatedDate: string;
  updatedBy: number;
}

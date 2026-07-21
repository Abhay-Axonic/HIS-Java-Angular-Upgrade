/**
 * Field set taken directly from
 * views/master/UnitMaster/organizationMaster/weekend-master.html +
 * resources/assets/js/controller/master/unit/weekendMasterController.js
 * (`weekendMasterController`, no `controllerAs`, wired via app.js's central
 * `$stateProvider` at line ~14613 -- NOT self-registered). moduleId 697.
 */
export interface WeekendMasterRow {
  weekendId: number;
  /** Display name of the day of week (e.g. "Monday"), as returned by the
   * list/by-id endpoints. */
  day: string;
  weekendDate: string;
  isHalfDay: 'Y' | 'N';
  fromTimeString?: string;
  toTimeString?: string;
  status: 'A' | 'I';
}

/** Shape of GET.../getWeekendMasterById's `listObject[0]` (ctrl:273-283) --
 * NOTE this is `listObject[0]`, NOT `object`, unlike this batch's sibling
 * `rack-masters`' by-id lookup -- confirmed per-screen from source, not
 * assumed to match. */
export interface WeekendMasterDetail {
  weekendId: number;
  day: string;
  weekendDate: string;
  dayId: number;
  isHalfDay: 'Y' | 'N';
  fromTimeString?: string;
  toTimeString?: string;
}

/** Editable form state -- mirrors legacy's `$scope.common` object
 * (ctrl:60-69). `dayId`/`fromTime`/`toTime` are the raw values actually
 * sent on save/update; `dayName` (the separate readonly display box,
 * view:43) is modeled as its own field below since legacy keeps it as an
 * entirely separate `$scope.dayName` variable, not part of `common` --
 * see the class comment's note on the edit-mode day-name display bug. */
export interface WeekendMasterForm {
  weekendId?: number;
  /** bound to the date-picker input (view:19-25, `ng-model="datepicker"`) */
  weekendDate: Date | null;
  dayId: number | null;
  isHalfDay: 'Y' | 'N';
  fromTime: string;
  toTime: string;
}

export function emptyWeekendMasterForm(): WeekendMasterForm {
  return {
    weekendId: undefined,
    weekendDate: null,
    dayId: null,
    isHalfDay: 'N',
    fromTime: '',
    toTime: '',
  };
}

/** POST body for /api/global/unit/saveWeekendMaster (ctrl:206-220). */
export interface WeekendMasterSavePayload {
  updatedBy: number;
  updatedDate: string;
  status: 'A';
  organizationId: number;
  unitId: number;
  createdDate: string;
  createdBy: number;
  weekendDate: string;
  isHalfDay: 'Y' | 'N';
  dayId: number | null;
  fromTime: string;
  toTime: string;
}

/** POST body for /api/global/unit/updateWeekendMaster (ctrl:306-318) -- note
 * no `status`/`createdBy`/`createdDate`, unlike the save payload above,
 * matching legacy exactly. */
export interface WeekendMasterUpdatePayload {
  weekendId: number;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
  weekendDate: string;
  unitId: number;
  isHalfDay: 'Y' | 'N';
  dayId: number | null;
  fromTime: string;
  toTime: string;
}

/** POST body for /api/global/unit/updateWeekendMasterStatus (ctrl:352-388). */
export interface WeekendMasterStatusPayload {
  updatedBy: number;
  updatedDate: string;
  status: 'A' | 'I';
  weekendId: number;
}

/** POST body for /adt/getActiveDayListById (ctrl:77-100, `test()`). */
export interface DayLookupPayload {
  dayId: number;
}

export interface DayLookupRow {
  day: string;
}

/** Row shape returned by GETHOLIDAYLIST -- `day` is the joined day-name string (dao alias `day.day`). */
export interface HolidayRow {
  holidayCalenderId: number;
  holidayCalenderCode: string;
  holidayCalenderDesc: string;
  holidayDate: string; // 'dd/MM/yyyy', pre-formatted server-side (GET_HOLIDAY_LIST)
  day: string;
  status: 'A' | 'I';
}

/** `response.data.listObject[0]` shape returned by GETSINGLEHOLIDAY (GET_HOLIDAY_LIST_BY_ID). */
export interface HolidayDetail {
  holidayCalenderId: number;
  holidayCalenderCode: string;
  holidayCalenderDesc: string;
  holidayDate: string; // 'dd-MM-yyyy'
  dayId: number;
}

/** Response shape from GETDAYOBJ (GET_ACTIVE_DAY_LIST_BY_ID). */
export interface DayObj {
  dayId: number;
  day: string;
}

/** Editable form state -- mirrors legacy's `$scope.holiday` + `$scope.datepicker`/`$scope.dayName`. */
export interface HolidayRecord {
  holidayId?: number;
  holidayCode: string;
  holidayDesc: string;
  holidayDayId: number | '';
  /** 'yyyy-MM-dd', native <input type="date"> value. */
  date: string;
}

export function emptyHoliday(): HolidayRecord {
  return {
    holidayId: undefined,
    holidayCode: '',
    holidayDesc: '',
    holidayDayId: '',
    date: '',
  };
}

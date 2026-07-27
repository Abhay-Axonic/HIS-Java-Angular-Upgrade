export type ShiftStatus = 'A' | 'I';

/** Row shape returned by `getShiftMasterList`/`getShiftMasterCount`
 * (ctrl:84-120, `GET_SHIFT_MASTER_LIST` named query). `fromTime`/`toTime`
 * are always `to_char(..., 'HH24:MI')` strings -- minute precision, no
 * seconds, correct 24-hour format (this named query does not have the
 * 'HH' 12-hour-format bug found elsewhere this session on WeekendMaster). */
export interface ShiftRow {
  shiftId: number;
  shiftName: string;
  fromTime: string;
  toTime: string;
  status: ShiftStatus;
  isWeeklyOff: 'Y' | 'N';
}

/** Editable form state -- mirrors `$scope.shiftMasterObj` (ctrl:54-71).
 * `fromTime`/`toTime` are plain `<input type="time">`-compatible `HH:mm`
 * strings throughout -- legacy's own clockpicker widget is hour:minute
 * only (confirmed live, no seconds control exists anywhere in the UI),
 * and `ShiftMasterDaoImpl`'s `LocalTime.parse(...)` (ISO_LOCAL_TIME)
 * accepts an `HH:mm` string with no seconds component fine. */
export interface ShiftForm {
  shiftId?: number;
  shiftName: string;
  fromTime: string;
  toTime: string;
  isWeeklyOff: 'Y' | 'N';
}

export function emptyShiftForm(): ShiftForm {
  return { shiftId: undefined, shiftName: '', fromTime: '', toTime: '', isWeeklyOff: 'N' };
}

/** POST body for `saveShiftMaster` (ctrl:176-225). */
export interface ShiftSavePayload {
  shiftName: string;
  fromTime: string;
  toTime: string;
  isWeeklyOff: 'Y' | 'N';
  organizationId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  updatedDate: string;
  status: 'A';
}

/** POST body for `updateShiftMaster` (ctrl:266-310) -- no `organizationId`
 * gate on this DAO call, but the object is still shaped exactly like
 * `shiftMasterObj` at the point `update()` fires. */
export interface ShiftUpdatePayload {
  shiftId: number;
  shiftName: string;
  fromTime: string;
  toTime: string;
  isWeeklyOff: 'Y' | 'N';
  organizationId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
  status: 'A';
}

/** POST body for `updateShiftMasterStatus` (ctrl:314-353). */
export interface ShiftStatusPayload {
  shiftId: number;
  status: ShiftStatus;
  updatedBy: number;
  updatedDate: string;
}

/** Response shape for `getShiftMasterById` (ctrl:238-255) --
 * `response.data.object`, a single record, NOT array-wrapped (unlike
 * dietUnitMater's edit-load). `fromTime`/`toTime` here are also `HH:mm`,
 * no seconds -- confirmed live during Step 2 golden-trace. */
export interface ShiftDetail {
  shiftId: number;
  shiftName: string;
  fromTime: string;
  toTime: string;
  isWeeklyOff: 'Y' | 'N';
  status: ShiftStatus;
}

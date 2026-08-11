export type SlotStatus = 'A' | 'I';

/**
 * Row shape AS RETURNED by POST /api/opd/getSlots/blockUnblock
 * (SlotMasterController.java:97-105, GET_ALL_SLOTS_BLOCK_UNBLOCK native
 * query). NOTE (DQ-2026-08-12-016): the endpoint currently returns the
 * BLANK GENERIC envelope (HTTP 200, status=error, code=null, message=null)
 * because SlotMasterController.java:101-104 swallows the real exception and
 * the DAO (SlotMasterDaoImpl:1857) throws a date-parse failure —
 * GlobalCommonDateUtils.getDate appends " 00:00:00" to <12-char dates then
 * parses with "yyyy-MM-dd", which always fails for 'YYYY-MM-DD' input. The
 * SQL runs clean in psql (0 rows; tables/columns all exist — NOT phantom).
 * The shape below is the intended projection from the native query
 * (SlotMaster.java GET_SLOTS_FOR_BLOCK_UNBLOCK: appointmentDate, dayId,
 * slotId, day, doctorId, doctorName, specialityId, specialityName,
 * modalityId, modalityDesc, subSpecialityId, subSpecialityName, remark,
 * reasonDesc, fromTimeString, toTimeString).
 */
export interface SlotBlockRow {
  appointmentDate: string;
  slotId: number;
  day: string;
  doctorId: number;
  doctorName: string;
  specialityName: string;
  modalityDesc: string;
  subSpecialityName: string;
  remark: string;
  reasonDesc: string;
  fromTimeString: string;
  toTimeString: string;
  status: SlotStatus;
}

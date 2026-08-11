export type SlotStatus = 'A' | 'I';

/**
 * Row shape AS RETURNED by POST /api/opd/getSlots/blockUnblock — the SAME
 * endpoint as scheduleWise (DQ-2026-08-12-016: the pair share the endpoint
 * set, not a state). See schedule-wise.models.ts for the full masked-error
 * note (blank generic envelope; swallowed controller exception + date-parse
 * failure at SlotMasterDaoImpl:1857).
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

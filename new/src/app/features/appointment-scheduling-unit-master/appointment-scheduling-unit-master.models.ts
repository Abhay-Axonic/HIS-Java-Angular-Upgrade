export type SlotStatus = 'A' | 'I';

/**
 * Row shape AS RETURNED by POST /api/opd/getSlotsNew (SlotMasterDaoImpl
 * getSlotsNew). NOTE (DQ-2026-08-12-016): the endpoint currently 500s with
 * the correlation envelope "Something went wrong. Reference: ERR-<uuid>"
 * (ErrorCorrelationUtil) for the default payload shape. The row shape below
 * is the intended projection from the native query branches.
 */
export interface AppointmentSlotRow {
  slotId: number;
  appointmentDate: string;
  doctorId: number;
  doctorName: string;
  specialityId: number;
  specialityName: string;
  fromTimeString: string;
  toTimeString: string;
  status: SlotStatus;
}

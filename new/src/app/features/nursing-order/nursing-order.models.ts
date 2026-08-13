/**
 * Nursing Order (ADT) model shapes.
 *
 * LIVE-VERIFIED 2026-08-13 (DQ-071): POST /api/opd/saveNursingOrderMapper.
 * The DTO's nursingOrderMapperDtoList is REQUIRED (a null list is a masked
 * NPE in the DAO); each line needs startDate/stopDate.
 */
export interface NursingOrderLine {
  nursingOrderId: number;
  frequencyId: number;
  instruction: string;
  startDate: string;
  stopDate: string;
  createdBy: number;
  updatedBy: number;
  status: string;
  createdDate: string;
  updatedDate: string;
}

export interface NursingOrderSave {
  organizationId: number;
  unitId: number;
  patientId: number;
  createdBy: number;
  updatedBy: number;
  status: string;
  createdDate: string;
  updatedDate: string;
  nursingOrderMapperDtoList: NursingOrderLine[];
}

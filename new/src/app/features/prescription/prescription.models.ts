/** Prescription payloads — field names EXACTLY as the corrected backend's
 * PrescriptionDto / PrescriptionDetailsDto (the @RequestBody of
 * POST /api/opd/saveMedication).
 *
 * DATE CONVENTION: this DTO family uses STRING dates (dd-M-yyyy HH:mm:ss) —
 * the SAME convention as the registration DTO but the OPPOSITE of the
 * encounter DTO (which uses Long epoch-millis). The service must format with
 * formatLegacyNow() here, NOT Date.now(). Three screens, two conventions. */
export interface PrescriptionLine {
  prescriptionDetailsid?: number | null;
  organizationId?: number | null;
  unitId?: number | null;
  prescriptionId?: number | null;
  itemId: number | null;
  storeId?: number | null;
  uomUnitId?: number | null;
  orderQty?: number | null;
  pendingQty?: number | null;
  refillQty?: number | null;
  strength?: number | null;
  isDiscontinued?: boolean | null;
  prescriptionDate?: string | null;
  isPreStop?: boolean | null;
  isIntervention?: boolean | null;
  addedBy?: number | null;
  addedDate?: string | null;
  isDeleted?: boolean | null;
  isActive?: boolean | null;
  statusId?: number | null;
  remark?: string | null;
  prnflag?: boolean | null;
  statflag?: boolean | null;
  infusionflag?: boolean | null;
  dose?: number | null;
  doseUnitId?: number | null;
  frequencyId?: number | null;
  routeId?: number | null;
  startDate?: string | null;
  stopDate?: string | null;
}

export interface PrescriptionPayload {
  prescriptionId?: number | null;
  organizationId: number;
  unitId: number;
  visitTypeId?: number | null;
  encounterId?: number | null;
  doctorId?: number | null;
  patientId?: number | null;
  prescriptionNo?: string | null;
  prescriptionDate?: string | null;
  isActive?: boolean | null;
  addedBy?: number | null;
  addedDate?: string | null;
  isDeleted?: boolean | null;
  statusId?: number | null;
  priorityId?: number | null;
  isValidate?: boolean | null;
  authorisedBy?: number | null;
  authorisedDate?: string | null;
  isIntervention?: boolean | null;
  updatedBy?: number | null;
  updatedDate?: string | null;
  admissionId?: number | null;
  isDischarge?: boolean | null;
  statflag?: boolean | null;
  prescriptionDetailsDtosList?: PrescriptionLine[] | null;
  statPrescriptionDetailsDtosList?: PrescriptionLine[] | null;
  normalPrescriptionDetailsDtosList?: PrescriptionLine[] | null;
}

export interface ItemRow {
  itemId?: number;
  id?: number;
  itemName?: string;
  description?: string;
  itemCode?: string;
}

/** The form's line model — the UI's working copy of a prescription line. */
export interface LineForm {
  itemId: number | null;
  itemName: string;
  dose: string;
  doseUnitId: number | null;
  frequencyId: number | null;
  routeId: number | null;
  quantity: string;
  durationDays: string;
  instruction: string;
}

export type TransferStatusId = 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10;

export interface TransferRow {
  transferId: number;
  admissionId?: number;
  patientId?: number;
  transferRequestId?: number;
  transferStatusId?: TransferStatusId;
  status?: string;
  toBedCategoryId?: number;
  toBillingBedCategoryId?: number;
  toWardId?: number;
  toRoomId?: number;
  toBedId?: number;
  patientName?: string;
  uhidNumber?: string;
}

export interface AdmittedPatientRow {
  patientId: number;
  patientName?: string;
  uhidNumber?: string;
  genderId?: number;
  visitTypeId?: number;
  admissionId?: number;
}

export interface WardRow {
  wardId: number;
  wardName?: string;
  nursingStationId?: number;
}

export interface BedRow {
  bedId: number;
  bedCode?: string;
  bedNumber?: string;
  bedStatusId?: number;
}

export interface ReasonRow {
  reasonId: number;
  reasonName?: string;
}

export interface TransferStatusRow {
  transferStatusId: TransferStatusId;
  transferStatusCode?: string;
  transferStatusDesc?: string;
}

/** W1 payload — POST /adt/saveTransferRequest (TransferRequestDaoImpl:152). */
export interface SaveTransferRequestPayload {
  transferId: number;
  transferTypeId: number;
  transferStatusId: number;
  toBedCategoryId?: number;
  toBillingBedCategoryId?: number;
  toWardId?: number;
  toRoomId?: number;
  toBedId?: number;
  admissionId?: number;
  organizationId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  note?: string;
}

/**
 * W2/W3 payload — POST /adt/acceptTransferRequestForADT /
 * POST /adt/acceptFinalTransferRequest. Built from the SELECTED PENDING-LIST
 * ROW (the W1->W2 response contract: transferRequestId is W1's response id
 * in the request->accept chain — DQ-046).
 */
export interface AcceptTransferPayload {
  organizationId: number;
  unitId: number;
  transferId: number;
  transferRequestId?: number;
  updatedBy: number;
  updatedDate?: number;
  toBedCategoryId?: number;
  toBillingBedCategoryId?: number;
  toWardId?: number;
  toRoomId?: number;
  toBedId?: number;
  admissionId?: number;
  workstationNote?: string;
}

/** Reject payload — POST /adt/rejectTransferRequestForADT. */
export interface RejectTransferPayload {
  organizationId: number;
  unitId: number;
  transferId: number;
  transferRequestId?: number;
  updatedBy: number;
  updatedDate?: number;
  adtRejectReason?: string;
  adtReasonId?: number;
  admissionId?: number;
}

/** The transfer status machine (m_transfer_status_master, live-verified). */
export const TRANSFER_STATUS: Record<number, { code: string; label: string }> = {
  1: { code: 'P', label: 'Pending' },
  2: { code: 'R', label: 'Requested' },
  3: { code: 'D', label: 'Request sent to doctor' },
  4: { code: 'A', label: 'Request sent to ADT' },
  5: { code: 'RJ', label: 'Rejected' },
  6: { code: 'RDT', label: 'Ready For Transfer' },
  7: { code: 'AST', label: 'All set for transfer' },
  8: { code: 'ABT', label: 'Abort' },
  9: { code: 'TRA', label: 'Transferd' },
  10: { code: 'ICUT', label: 'ICU Transfer' },
};

/** Legal UI transitions (the machine the rebuild renders — BR-036: the
 * server enforces NONE of these; the screen only shows buttons in their
 * legal state per Amendment 9). */
export const LEGAL_TRANSITIONS: Record<number, number[]> = {
  1: [2],            // Pending -> Requested (W1)
  2: [6, 5],         // Requested -> Ready For Transfer (W2) | Rejected
  3: [4],            // Request sent to doctor -> Request sent to ADT
  4: [6, 5],         // Request sent to ADT -> Ready For Transfer (W2) | Rejected
  6: [9],            // Ready For Transfer -> Transferd (W3)
};

/**
 * Cross Match Register (blood bank) model shapes.
 *
 * LIVE-VERIFIED 2026-08-13 (DQ-068): POST
 * /api/bloodbank/crossMatch/saveCrossMatchRegister
 * (@RequestBody CrossMatchRegisterDto). The full legacy payload shape —
 * the master's dates + the details entry (componentId, bloodBankStockId) —
 * landed master AND details rows.
 */

export interface CrossMatchSave {
  organizationId: number;
  unitId: number;
  patientId: number;
  patientRequisitionId: number;
  bloodComponentOrderId: number;
  requisitionDate: string;
  issueDate: string;
  requiredBloodGroup: number;
  patientBloodGroup: number;
  requiredQty: number;
  pendingQty: number;
  technicianId: number;
  remark: string;
  crossMatchStatus: number;
  createdBy: number;
  updatedBy: number;
  status: string;
  createdDate: string;
  updatedDate: string;
  crossMatchRegisterDetails: CrossMatchDetail[];
}

export interface CrossMatchDetail {
  componentId: number;
  bloodGroupId: number;
  segmentNo: string;
  bloodBankStockId: number;
  salineMajor: number;
  salineMinor: number;
  albuminMajor: number;
  albuminMinor: number;
  coombsMajor: number;
  coombsMinor: number;
  crossMatchReportResult: string;
  crossMatchRegisterDetailsStatus: number;
  createdBy: number;
  createdDate: string;
  updatedDate: string;
}

export interface CrossMatchResponse {
  status?: string;
  code?: string;
  message?: string;
}

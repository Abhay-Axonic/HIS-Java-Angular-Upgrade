/**
 * Blood Issue (blood bank) model shapes.
 *
 * LIVE-VERIFIED 2026-08-13 (DQ-070): POST
 * /global/bloodBank/saveIssueBloodBag. The master + a list of bags
 * (bloodBagIssueList); the crossMatchRegisterId + the detail's
 * crossMatchRegisterDetailsId reference the cross-match flow's output.
 */
export interface BloodBagIssueDetail {
  organizationId: number;
  unitId: number;
  issueBloodBagStatusId: number;
  bagNo: string;
  segmentNo: string;
  bloodGroupId: number;
  componentId: number;
  bloodBankStockId: number;
  crossMatchRegisterDetailsId: number;
  crossMatchReportResult: string;
  createdBy: number;
  updatedBy: number;
  status: string;
  createdDate: string;
  updatedDate: string;
}

export interface BloodBagIssueSave {
  organizationId: number;
  unitId: number;
  patientId: number;
  createdBy: number;
  updatedBy: number;
  status: string;
  issueDate: string;
  requisitionDate: string;
  createdDate: string;
  updatedDate: string;
  btoId: number;
  issuerId: number;
  receivedId: number;
  bloodUnitQty: number;
  bloodBagIssueNo: string;
  remarks: string;
  crossMatchRegisterId: number;
  bloodBagIssueList: BloodBagIssueDetail[];
}

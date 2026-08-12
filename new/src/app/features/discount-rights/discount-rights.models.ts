/**
 * 745 Discount Rights master row — exact fields as live-verified 2026-08-12
 * from POST /billing/getDiscountRightsMasterList (local unit org 1/unit 1).
 * Mirrors legacy com.param.billing.global.dto.DiscountRightsMasterDto
 * (legacy/Billing/.../global/dto/DiscountRightsMasterDto.java).
 *
 * Char flags serialize as "Y"/"N" strings; amounts as numbers (BigDecimal);
 * unset keys come back null (not absent) — the legacy template prints them
 * raw, the rebuild renders '—' via rowCells().
 */
export interface DiscountRightsRow {
  discountRightId: number;
  userId: number | null;
  fromDiscountAmount: number | null;
  toDiscountAmount: number | null;
  status: string | null;
  createdBy: number | null;
  createdDate: string | null;
  updatedBy: number | null;
  updatedDate: string | null;
  organisationId: number | null;
  unitId: number | null;
  noOfRecordsPerPage: number | null;
  offset: number | null;
  employeeCode: string | null;
  employeeName: string | null;
  employeeId: number | null;
  isFinancialClearance: string | null;
  isCreditNoteApproval: string | null;
  docId: number | null;
  empId: number | null;
  isExpenseApproval: string | null;
  roleId: number | null;
  isBadDebit: string | null;
  fromBadDebitAmount: number | null;
  toBadDebitAmount: number | null;
  roleName: string | null;
  fromFinancialClearanceAmount: number | null;
  toFinancialClearanceAmount: number | null;
  fromCreditNoteAmount: number | null;
  toCreditNoteAmount: number | null;
  fromExpenseApprovalAmount: number | null;
  toExpenseApprovalAmount: number | null;
  isDiscountRights: string | null;
  discountType: number | null;
  badDebitType: number | null;
  financialClearanceType: number | null;
  creditNoteType: number | null;
  expenseApprovalType: number | null;
  isUHIDMergeApproval: string | null;
  isRefundApproval: string | null;
  fromRefundAmount: number | null;
  toRefundAmount: number | null;
  refundType: number | null;
}

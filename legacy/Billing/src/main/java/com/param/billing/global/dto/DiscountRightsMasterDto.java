package com.param.billing.global.dto;

import java.math.BigDecimal;

public class DiscountRightsMasterDto {
	
	private Integer discountRightId;
	
	private Integer userId;
	
	private BigDecimal fromDiscountAmount;
	
	private BigDecimal toDiscountAmount;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;

	private Integer organisationId;
	
	private Integer unitId;

	private Integer noOfRecordsPerPage;

	private Integer offset;
	
	private String employeeCode;

	private String employeeName;
	
	private Integer employeeId;
	
	private Character isFinancialClearance;
	
	private Character isCreditNoteApproval;
	
	private Integer docId;
	
	private Integer empId;
	
	private Character isExpenseApproval;
	
	private Integer roleId;

	
	private Character isBadDebit;

	private BigDecimal fromBadDebitAmount;
	
	private BigDecimal toBadDebitAmount;
	private String roleName;
	
	private BigDecimal fromFinancialClearanceAmount;
	private BigDecimal toFinancialClearanceAmount;
	private BigDecimal fromCreditNoteAmount;
	private BigDecimal toCreditNoteAmount;
	private BigDecimal fromExpenseApprovalAmount;
	private BigDecimal toExpenseApprovalAmount;
	
	private Character isDiscountRights;
	private Integer discountType;
	private Integer badDebitType;
	private Integer financialClearanceType;
	private Integer creditNoteType;
	private Integer expenseApprovalType;
	private Character isUHIDMergeApproval;

	private Character isRefundApproval;
	private BigDecimal fromRefundAmount;
	private BigDecimal toRefundAmount;
	private Integer refundType;

	public Character getIsUHIDMergeApproval() {
		return isUHIDMergeApproval;
	}

	public void setIsUHIDMergeApproval(Character isUHIDMergeApproval) {
		this.isUHIDMergeApproval = isUHIDMergeApproval;
	}

	public Character getIsDiscountRights() {
		return isDiscountRights;
	}

	public void setIsDiscountRights(Character isDiscountRights) {
		this.isDiscountRights = isDiscountRights;
	}

	public Integer getDiscountType() {
		return discountType;
	}

	public void setDiscountType(Integer discountType) {
		this.discountType = discountType;
	}

	public Integer getBadDebitType() {
		return badDebitType;
	}

	public void setBadDebitType(Integer badDebitType) {
		this.badDebitType = badDebitType;
	}

	public Integer getFinancialClearanceType() {
		return financialClearanceType;
	}

	public void setFinancialClearanceType(Integer financialClearanceType) {
		this.financialClearanceType = financialClearanceType;
	}

	public Integer getCreditNoteType() {
		return creditNoteType;
	}

	public void setCreditNoteType(Integer creditNoteType) {
		this.creditNoteType = creditNoteType;
	}

	public Integer getExpenseApprovalType() {
		return expenseApprovalType;
	}

	public void setExpenseApprovalType(Integer expenseApprovalType) {
		this.expenseApprovalType = expenseApprovalType;
	}

	public BigDecimal getFromFinancialClearanceAmount() {
		return fromFinancialClearanceAmount;
	}

	public void setFromFinancialClearanceAmount(BigDecimal fromFinancialClearanceAmount) {
		this.fromFinancialClearanceAmount = fromFinancialClearanceAmount;
	}

	public BigDecimal getToFinancialClearanceAmount() {
		return toFinancialClearanceAmount;
	}

	public void setToFinancialClearanceAmount(BigDecimal toFinancialClearanceAmount) {
		this.toFinancialClearanceAmount = toFinancialClearanceAmount;
	}

	public BigDecimal getFromCreditNoteAmount() {
		return fromCreditNoteAmount;
	}

	public void setFromCreditNoteAmount(BigDecimal fromCreditNoteAmount) {
		this.fromCreditNoteAmount = fromCreditNoteAmount;
	}

	public BigDecimal getToCreditNoteAmount() {
		return toCreditNoteAmount;
	}

	public void setToCreditNoteAmount(BigDecimal toCreditNoteAmount) {
		this.toCreditNoteAmount = toCreditNoteAmount;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Character getIsBadDebit() {
		return isBadDebit;
	}

	public void setIsBadDebit(Character isBadDebit) {
		this.isBadDebit = isBadDebit;
	}

	public BigDecimal getFromBadDebitAmount() {
		return fromBadDebitAmount;
	}

	public void setFromBadDebitAmount(BigDecimal fromBadDebitAmount) {
		this.fromBadDebitAmount = fromBadDebitAmount;
	}

	public BigDecimal getToBadDebitAmount() {
		return toBadDebitAmount;
	}

	public void setToBadDebitAmount(BigDecimal toBadDebitAmount) {
		this.toBadDebitAmount = toBadDebitAmount;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Character getIsFinancialClearance() {
		return isFinancialClearance;
	}

	public void setIsFinancialClearance(Character isFinancialClearance) {
		this.isFinancialClearance = isFinancialClearance;
	}

	public Integer getDiscountRightId() {
		return discountRightId;
	}

	public void setDiscountRightId(Integer discountRightId) {
		this.discountRightId = discountRightId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	
	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public BigDecimal getFromDiscountAmount() {
		return fromDiscountAmount;
	}

	public void setFromDiscountAmount(BigDecimal fromDiscountAmount) {
		this.fromDiscountAmount = fromDiscountAmount;
	}

	public BigDecimal getToDiscountAmount() {
		return toDiscountAmount;
	}

	public void setToDiscountAmount(BigDecimal toDiscountAmount) {
		this.toDiscountAmount = toDiscountAmount;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Character getIsCreditNoteApproval() {
		return isCreditNoteApproval;
	}

	public void setIsCreditNoteApproval(Character isCreditNoteApproval) {
		this.isCreditNoteApproval = isCreditNoteApproval;
	}

	public Character getIsExpenseApproval() {
		return isExpenseApproval;
	}

	public void setIsExpenseApproval(Character isExpenseApproval) {
		this.isExpenseApproval = isExpenseApproval;
	}

	public BigDecimal getFromExpenseApprovalAmount() {
		return fromExpenseApprovalAmount;
	}

	public void setFromExpenseApprovalAmount(BigDecimal fromExpenseApprovalAmount) {
		this.fromExpenseApprovalAmount = fromExpenseApprovalAmount;
	}

	public BigDecimal getToExpenseApprovalAmount() {
		return toExpenseApprovalAmount;
	}

	public void setToExpenseApprovalAmount(BigDecimal toExpenseApprovalAmount) {
		this.toExpenseApprovalAmount = toExpenseApprovalAmount;
	}

	public Character getIsRefundApproval() {
		return isRefundApproval;
	}

	public void setIsRefundApproval(Character isRefundApproval) {
		this.isRefundApproval = isRefundApproval;
	}

	public BigDecimal getFromRefundAmount() {
		return fromRefundAmount;
	}

	public void setFromRefundAmount(BigDecimal fromRefundAmount) {
		this.fromRefundAmount = fromRefundAmount;
	}

	public BigDecimal getToRefundAmount() {
		return toRefundAmount;
	}

	public void setToRefundAmount(BigDecimal toRefundAmount) {
		this.toRefundAmount = toRefundAmount;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}
}

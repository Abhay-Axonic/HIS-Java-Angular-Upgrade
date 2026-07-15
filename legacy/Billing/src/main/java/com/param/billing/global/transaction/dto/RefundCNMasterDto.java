package com.param.billing.global.transaction.dto;

import java.math.BigDecimal;

import javax.persistence.Column;

public class RefundCNMasterDto {

	private Integer tRefundCnId;

	private String refundCnNo;

	private String refundCnDate;

	private Integer payeeId;

	private Integer depositId;

	private Integer refundAgainstId;

	private Integer visitTypeId;

	private Integer encounterId;

	private Integer admissionId;

	private BigDecimal refundAmount;

	private BigDecimal refundedAmount;

	private BigDecimal balanceAmount;

	private String accountHolderName;

	private Integer bankId;

	private Integer branchId;

	private String ifscCode;

	private String accountNo;

	private BigDecimal bankRequestRefundAmount;

	private Integer organizationId;

	private Integer unitId;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;

	private String remark;
	
	private Integer counterId;
	
	private Character isApproved;
	
	private Integer requestSentTo;
	
	private Integer patientId;

	public Integer gettRefundCnId() {
		return tRefundCnId;
	}

	public void settRefundCnId(Integer tRefundCnId) {
		this.tRefundCnId = tRefundCnId;
	}

	public String getRefundCnNo() {
		return refundCnNo;
	}

	public void setRefundCnNo(String refundCnNo) {
		this.refundCnNo = refundCnNo;
	}

	public String getRefundCnDate() {
		return refundCnDate;
	}

	public void setRefundCnDate(String refundCnDate) {
		this.refundCnDate = refundCnDate;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getDepositId() {
		return depositId;
	}

	public void setDepositId(Integer depositId) {
		this.depositId = depositId;
	}

	public Integer getRefundAgainstId() {
		return refundAgainstId;
	}

	public void setRefundAgainstId(Integer refundAgainstId) {
		this.refundAgainstId = refundAgainstId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public BigDecimal getRefundedAmount() {
		return refundedAmount;
	}

	public void setRefundedAmount(BigDecimal refundedAmount) {
		this.refundedAmount = refundedAmount;
	}

	public BigDecimal getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(BigDecimal balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public BigDecimal getBankRequestRefundAmount() {
		return bankRequestRefundAmount;
	}

	public void setBankRequestRefundAmount(BigDecimal bankRequestRefundAmount) {
		this.bankRequestRefundAmount = bankRequestRefundAmount;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Character getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Character isApproved) {
		this.isApproved = (isApproved == null) ? 'N' : isApproved;
	}

	public Integer getRequestSentTo() {
		return requestSentTo;
	}

	public void setRequestSentTo(Integer requestSentTo) {
		this.requestSentTo = requestSentTo;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

}

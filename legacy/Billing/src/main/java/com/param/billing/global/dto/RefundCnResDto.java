package com.param.billing.global.dto;

import java.math.BigDecimal;

public class RefundCnResDto {
	private Integer tRefundCnId;
	private Integer depositId;
	private String refundCnNo;
	private String refundCnDate;
	private String uhidNumber;
	private String mobileNo;
	private Integer visitTypeId;
	private Integer depositTypeId;
	private Integer admissionId;
	private Integer encounterId;
	private Integer patientId;
	private BigDecimal balanceAmount;
	private BigDecimal refundAmount;
	private String patientName;
	private String visitNo;
	private String userName;
	private String visitTypeName;
	private Integer status;
	private Character isApproved;
	private String remark;

	public Integer gettRefundCnId() {
		return tRefundCnId;
	}

	public void settRefundCnId(Integer tRefundCnId) {
		this.tRefundCnId = tRefundCnId;
	}

	public Integer getDepositId() {
		return depositId;
	}

	public void setDepositId(Integer depositId) {
		this.depositId = depositId;
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

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getDepositTypeId() {
		return depositTypeId;
	}

	public void setDepositTypeId(Integer depositTypeId) {
		this.depositTypeId = depositTypeId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public BigDecimal getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(BigDecimal balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Character getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Character isApproved) {
		this.isApproved = isApproved;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
}

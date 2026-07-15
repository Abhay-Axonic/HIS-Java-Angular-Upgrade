package com.param.billing.global.dto;

import java.math.BigDecimal;

public class RefundCreditNoteResDto {
	
	private Integer creditNoteId;
	private Integer billMasterId;
	private Integer billPayeeId;
	private Integer patientId;
	private String creditNoteNo;
	private String creditNoteDate;
	private String invoiceNo;
	private String uhidNumber;
	private String mobileNo;
	private Integer visitTypeId;
	private Integer depositTypeId;
	private Integer admissionId;
	private Integer encounterId;
	private Double crAmount;
	private Double crDueAmount;
	private String patientName;
	private String visitNo;
	private String userName;
	private String visitTypeName;
	private Integer status;
	private Character isApproved;
	private Integer cancelOrDiscount;
	private Double selfPayable;
	private Double creditPayable;
	private boolean disableCancellation;
	private String creditNoteRemark;
	
	public boolean isDisableCancellation() {
		return disableCancellation;
	}
	public void setDisableCancellation(boolean disableCancellation) {
		this.disableCancellation = disableCancellation;
	}
	public Double getSelfPayable() {
		return selfPayable;
	}
	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}
	public Double getCreditPayable() {
		return creditPayable;
	}
	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}
	public Integer getCreditNoteId() {
		return creditNoteId;
	}
	public void setCreditNoteId(Integer creditNoteId) {
		this.creditNoteId = creditNoteId;
	}
	public Integer getBillMasterId() {
		return billMasterId;
	}
	public void setBillMasterId(Integer billMasterId) {
		this.billMasterId = billMasterId;
	}
	public String getCreditNoteNo() {
		return creditNoteNo;
	}
	public void setCreditNoteNo(String creditNoteNo) {
		this.creditNoteNo = creditNoteNo;
	}
	public String getCreditNoteDate() {
		return creditNoteDate;
	}
	public void setCreditNoteDate(String creditNoteDate) {
		this.creditNoteDate = creditNoteDate;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getUhidNumber() {
		return uhidNumber;
	}
	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
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
	public Double getCrAmount() {
		return crAmount;
	}
	public void setCrAmount(Double crAmount) {
		this.crAmount = crAmount;
	}
	public Double getCrDueAmount() {
		return crDueAmount;
	}
	public void setCrDueAmount(Double crDueAmount) {
		this.crDueAmount = crDueAmount;
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
	public Integer getBillPayeeId() {
		return billPayeeId;
	}
	public void setBillPayeeId(Integer billPayeeId) {
		this.billPayeeId = billPayeeId;
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
	public Integer getCancelOrDiscount() {
		return cancelOrDiscount;
	}
	public void setCancelOrDiscount(Integer cancelOrDiscount) {
		this.cancelOrDiscount = cancelOrDiscount;
	}
	public String getCreditNoteRemark() {
		return creditNoteRemark;
	}
	public void setCreditNoteRemark(String creditNoteRemark) {
		this.creditNoteRemark = creditNoteRemark;
	}
	

}

package com.param.billing.global.transaction.dto;

import java.math.BigDecimal;

public class RefundSearchDto {

	private Integer paymentRefundVoucherId;
	private Integer tRefundCnId;
	private Integer depositId;
	private String paymentRefundVoucherNo;
	private String dateOfRefund;
	private String creditNoteNo;
	private String creditNoteDate;
	private String invoiceNo;
	private String refundCnNo;
	private String refundCnDate;
	private String uhidNumber;
	private Integer visitTypeId;
	private Integer depositTypeId;
	private Integer encounterId;
	private Integer admissionId;
	private Double amountRefunded;
	private Double crAmount;
	private Double crDueAmount;
	private BigDecimal refundAmount;
	private String patientName;
	private String mobileNo;
	private String userName;
	private String visitNo;
	private Double selfPayable;
	private Double creditPayable;
	
	

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

	public Integer getPaymentRefundVoucherId() {
		return paymentRefundVoucherId;
	}

	public void setPaymentRefundVoucherId(Integer paymentRefundVoucherId) {
		this.paymentRefundVoucherId = paymentRefundVoucherId;
	}

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

	public String getPaymentRefundVoucherNo() {
		return paymentRefundVoucherNo;
	}

	public void setPaymentRefundVoucherNo(String paymentRefundVoucherNo) {
		this.paymentRefundVoucherNo = paymentRefundVoucherNo;
	}

	public String getDateOfRefund() {
		return dateOfRefund;
	}

	public void setDateOfRefund(String dateOfRefund) {
		this.dateOfRefund = dateOfRefund;
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

	public Double getAmountRefunded() {
		return amountRefunded;
	}

	public void setAmountRefunded(Double amountRefunded) {
		this.amountRefunded = amountRefunded;
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

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
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

}

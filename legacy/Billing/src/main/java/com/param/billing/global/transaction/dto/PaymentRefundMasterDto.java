package com.param.billing.global.transaction.dto;

public class PaymentRefundMasterDto {

	private Integer paymentRefundVoucherId;

	private String paymentRefundVoucherNo;

	private String dateOfRefund;

	private Integer isCnOrDeposit;

	private Integer tRefundCnId;
	
	private Integer creditNoteId;

	private Integer tPharmacyReturnId;

	private double amountRefunded;

	private Integer patientId;

	private String receivedFrom;

	private String identificationNo;

	private String nameOfPerson;

	private char status;

	private Integer unitId;

	private Integer orgnisationId;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;
	
	private Integer counterId;
	
	private Integer cancelOrDiscount;
	
	private Integer saleReturnId;

	public Integer getPaymentRefundVoucherId() {
		return paymentRefundVoucherId;
	}

	public void setPaymentRefundVoucherId(Integer paymentRefundVoucherId) {
		this.paymentRefundVoucherId = paymentRefundVoucherId;
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

	public Integer getIsCnOrDeposit() {
		return isCnOrDeposit;
	}

	public void setIsCnOrDeposit(Integer isCnOrDeposit) {
		this.isCnOrDeposit = isCnOrDeposit;
	}

	public Integer gettRefundCnId() {
		return tRefundCnId;
	}

	public void settRefundCnId(Integer tRefundCnId) {
		this.tRefundCnId = tRefundCnId;
	}

	public double getAmountRefunded() {
		return amountRefunded;
	}

	public void setAmountRefunded(double amountRefunded) {
		this.amountRefunded = amountRefunded;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public String getIdentificationNo() {
		return identificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	public String getNameOfPerson() {
		return nameOfPerson;
	}

	public void setNameOfPerson(String nameOfPerson) {
		this.nameOfPerson = nameOfPerson;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrgnisationId() {
		return orgnisationId;
	}

	public void setOrgnisationId(Integer orgnisationId) {
		this.orgnisationId = orgnisationId;
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

	public Integer getCreditNoteId() {
		return creditNoteId;
	}

	public void setCreditNoteId(Integer creditNoteId) {
		this.creditNoteId = creditNoteId;
	}

	public Integer gettPharmacyReturnId() {
		return tPharmacyReturnId;
	}

	public void settPharmacyReturnId(Integer tPharmacyReturnId) {
		this.tPharmacyReturnId = tPharmacyReturnId;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Integer getCancelOrDiscount() {
		return cancelOrDiscount;
	}

	public void setCancelOrDiscount(Integer cancelOrDiscount) {
		this.cancelOrDiscount = cancelOrDiscount;
	}

	public Integer getSaleReturnId() {
		return saleReturnId;
	}

	public void setSaleReturnId(Integer saleReturnId) {
		this.saleReturnId = saleReturnId;
	}

}

package com.param.global.dto;

import java.util.Date;

public class RefundTransactionDto {

	private Integer id;
	
	private Date date;
	
	private Integer receiptNumber;
	
	private Date receiptDate;
	
	private Double refundAmount;
	
	private Integer invoiceNumber;
	
	private Integer invoiceId;
	
	private Integer patientId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer denominationId;
	
	private Boolean isVerified;
	
	private Integer verifiedById;
	
	private String remarks;
	
	private Integer cashierId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(Integer receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Integer getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(Integer invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public Integer getDenominationId() {
		return denominationId;
	}

	public void setDenominationId(Integer denominationId) {
		this.denominationId = denominationId;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public Integer getVerifiedById() {
		return verifiedById;
	}

	public void setVerifiedById(Integer verifiedById) {
		this.verifiedById = verifiedById;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getCashierId() {
		return cashierId;
	}

	public void setCashierId(Integer cashierId) {
		this.cashierId = cashierId;
	}

	
}

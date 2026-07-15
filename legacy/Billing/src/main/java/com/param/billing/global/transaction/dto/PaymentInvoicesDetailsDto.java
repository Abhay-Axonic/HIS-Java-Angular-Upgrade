package com.param.billing.global.transaction.dto;

public class PaymentInvoicesDetailsDto {
	private String receiptNo;
	private String dateOfReceipt;
	private Integer billId;
	private Double invoiceAmount;
	private Integer paymentRecieptId;
	private Integer createdBy;
	private String  receivedBy;
	private Integer orgId;
	private Integer orgUnitId;
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getDateOfReceipt() {
		return dateOfReceipt;
	}
	public void setDateOfReceipt(String dateOfReceipt) {
		this.dateOfReceipt = dateOfReceipt;
	}
	public Integer getBillId() {
		return billId;
	}
	public void setBillId(Integer billId) {
		this.billId = billId;
	}
	public Double getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public Integer getPaymentRecieptId() {
		return paymentRecieptId;
	}
	public void setPaymentRecieptId(Integer paymentRecieptId) {
		this.paymentRecieptId = paymentRecieptId;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public String getReceivedBy() {
		return receivedBy;
	}
	public void setReceivedBy(String receivedBy) {
		this.receivedBy = receivedBy;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getOrgUnitId() {
		return orgUnitId;
	}
	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}
	
	
	

}

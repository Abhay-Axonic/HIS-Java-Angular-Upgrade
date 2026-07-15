package com.param.billing.global.transaction.dto;

public class CompanyInvoiceSettlementListResDto {
	private Integer companyInvoiceId;
	private String invoiceNumber;
	private String companyName;
	private Double totalAmount;
	private Double dueAmount;
	private String invoiceDate;
	private Integer paymentEntitlementId;
	private Integer payeeId;
	private Double netAmount;
	private Double crNetAmount;
	
	public Double getCrNetAmount() {
		return crNetAmount;
	}
	public void setCrNetAmount(Double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}
	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}
	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}
	public Integer getCompanyInvoiceId() {
		return companyInvoiceId;
	}
	public void setCompanyInvoiceId(Integer companyInvoiceId) {
		this.companyInvoiceId = companyInvoiceId;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getDueAmount() {
		return dueAmount;
	}
	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
}

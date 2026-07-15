package com.param.global.billing.dto;

public class BillPayeeDetailsResDto {
	private Integer billPayeeDetailsId;
	private Integer payeeId;
	private String companyDesc;
	private Integer billId;
	private Double billAmount;
	private Double dueAmount;
	private Double paidAmount;
	private String companyCode;
	private Integer priorityId;
	private Double disallowedAmount;
	private Double otherDeduction;
	private Integer associateCompanyId;
	private String associateCompanyDesc;
	private Double crNetAmt;
	private Double refundedAmount;
	private Double crDeuAmount;
	private char isInvoiceGenerated;
	private Integer paymentEntitlementId;
	
	public Double getCrDeuAmount() {
		return crDeuAmount;
	}
	public void setCrDeuAmount(Double crDeuAmount) {
		this.crDeuAmount = crDeuAmount;
	}
	public Double getRefundedAmount() {
		return refundedAmount;
	}
	public void setRefundedAmount(Double refundedAmount) {
		this.refundedAmount = refundedAmount;
	}
	public Double getCrNetAmt() {
		return crNetAmt;
	}
	public void setCrNetAmt(Double crNetAmt) {
		this.crNetAmt = crNetAmt;
	}
	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}
	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}
	public String getAssociateCompanyDesc() {
		return associateCompanyDesc;
	}
	public void setAssociateCompanyDesc(String associateCompanyDesc) {
		this.associateCompanyDesc = associateCompanyDesc;
	}
	public Double getOtherDeduction() {
		return otherDeduction;
	}
	public void setOtherDeduction(Double otherDeduction) {
		this.otherDeduction = otherDeduction;
	}
	public Double getDisallowedAmount() {
		return disallowedAmount;
	}
	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}
	public Integer getPriorityId() {
		return priorityId;
	}
	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public Double getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}
	public Double getDueAmount() {
		return dueAmount;
	}
	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
	}
	public Double getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}
	public Integer getBillPayeeDetailsId() {
		return billPayeeDetailsId;
	}
	public void setBillPayeeDetailsId(Integer billPayeeDetailsId) {
		this.billPayeeDetailsId = billPayeeDetailsId;
	}
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}
	public String getCompanyDesc() {
		return companyDesc;
	}
	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}
	public Integer getBillId() {
		return billId;
	}
	public void setBillId(Integer billId) {
		this.billId = billId;
	}
	public char getIsInvoiceGenerated() {
		return isInvoiceGenerated;
	}
	public void setIsInvoiceGenerated(char isInvoiceGenerated) {
		this.isInvoiceGenerated = isInvoiceGenerated;
	}
	public synchronized Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}
	public synchronized void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}
	
}

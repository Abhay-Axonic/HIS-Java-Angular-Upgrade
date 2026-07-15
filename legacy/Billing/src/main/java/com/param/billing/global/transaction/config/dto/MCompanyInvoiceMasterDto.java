package com.param.billing.global.transaction.config.dto;

import javax.persistence.Column;

public class MCompanyInvoiceMasterDto {

	private Integer companyInvoiceId;

	private String invoiceNumber;

	private Integer paymentEntitlementId;

	private Integer payeeId;

	private Double totalAmount;

	private Double dueAmount;

	private Integer unitId;

	private Integer orgId;

	private Integer createdBy;

	private Integer updatedBy;

	private String invoiceDate;

	private String createdDate;

	private Long updatedDate;

	private Double creditBillAmount;
	
	private Integer companyId;
	
	private Integer companyTypeId;
	
	private Integer invoiceId;
	
	private String remark;
	
	private String reminderDate;

	private String response;
	
	private Integer companyInvoiceReminderId;
	
	private Integer associateCompanyId;
	
	private Double crNetAmount;
	
	private Character invoiceCreationInsurance;
	
	private Character invoiceCreationCorporate;
	
	private String billNumber;
	
	private Character invoiceNumberSystemGen;
	
	public Double getCrNetAmount() {
		return crNetAmount;
	}

	public void setCrNetAmount(Double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}
	
	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}
	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
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
	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}
	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
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
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Long getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Double getCreditBillAmount() {
		return creditBillAmount;
	}
	public void setCreditBillAmount(Double creditBillAmount) {
		this.creditBillAmount = creditBillAmount;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getCompanyTypeId() {
		return companyTypeId;
	}
	public void setCompanyTypeId(Integer companyTypeId) {
		this.companyTypeId = companyTypeId;
	}
	public Integer getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getReminderDate() {
		return reminderDate;
	}
	public void setReminderDate(String reminderDate) {
		this.reminderDate = reminderDate;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public Integer getCompanyInvoiceReminderId() {
		return companyInvoiceReminderId;
	}
	public void setCompanyInvoiceReminderId(Integer companyInvoiceReminderId) {
		this.companyInvoiceReminderId = companyInvoiceReminderId;
	}

	public Character getInvoiceCreationInsurance() {
		return invoiceCreationInsurance;
	}

	public void setInvoiceCreationInsurance(Character invoiceCreationInsurance) {
		this.invoiceCreationInsurance = invoiceCreationInsurance;
	}

	public Character getInvoiceCreationCorporate() {
		return invoiceCreationCorporate;
	}

	public void setInvoiceCreationCorporate(Character invoiceCreationCorporate) {
		this.invoiceCreationCorporate = invoiceCreationCorporate;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Character getInvoiceNumberSystemGen() {
		return invoiceNumberSystemGen;
	}

	public void setInvoiceNumberSystemGen(Character invoiceNumberSystemGen) {
		this.invoiceNumberSystemGen = invoiceNumberSystemGen;
	}

	

}

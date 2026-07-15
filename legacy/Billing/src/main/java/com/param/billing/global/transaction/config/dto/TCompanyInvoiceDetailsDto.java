package com.param.billing.global.transaction.config.dto;


public class TCompanyInvoiceDetailsDto {

	private Integer companyInvoiceDetailId;
	
	private Integer companyInvoiceId;
	
	private Integer billPayeeId;
	
	private Integer billId;
	
	private Integer tSaleId;
	
	private Double payableAmount;
	
	private Double dueAmount;
	
	private Integer unitId;
	
	private Integer orgId;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private Long createdDate;
	
	private Long updatedDate;
	
	private Double billAmount;

	private Double crNetAmount;
	
	private Integer billPayeeDetailsId;
	
	private Integer associateCompanyId;
	
	private Integer payeeDetailsId;
	
	public Integer getBillPayeeDetailsId() {
		return billPayeeDetailsId;
	}

	public void setBillPayeeDetailsId(Integer billPayeeDetailsId) {
		this.billPayeeDetailsId = billPayeeDetailsId;
	}

	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}

	public void setCompanyInvoiceDetailId(Integer companyInvoiceDetailId) {
		this.companyInvoiceDetailId = companyInvoiceDetailId;
	}

	public Double getCrNetAmount() {
		return crNetAmount;
	}

	public void setCrNetAmount(Double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}
	
	public Integer getCompanyInvoiceId() {
		return companyInvoiceId;
	}

	public void setCompanyInvoiceId(Integer companyInvoiceId) {
		this.companyInvoiceId = companyInvoiceId;
	}

	public Integer getBillPayeeId() {
		return billPayeeId;
	}

	public void setBillPayeeId(Integer billPayeeId) {
		this.billPayeeId = billPayeeId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(Double payableAmount) {
		this.payableAmount = payableAmount;
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

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public Integer gettSaleId() {
		return tSaleId;
	}

	public void settSaleId(Integer tSaleId) {
		this.tSaleId = tSaleId;
	}

	public Integer getPayeeDetailsId() {
		return payeeDetailsId;
	}

	public void setPayeeDetailsId(Integer payeeDetailsId) {
		this.payeeDetailsId = payeeDetailsId;
	}

	
}

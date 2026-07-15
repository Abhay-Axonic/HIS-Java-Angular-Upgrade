package com.param.billing.global.transaction.dto;

public class CompanyInvoiceSettlementSearchReqDto {
	private Integer companyId;
	private String fromDate;
	private String toDate;
	private Integer orgId;
	private Integer unitId;
	private Character isPendingOrSettled;
	private Integer companyInvoiceId;
	private Integer associateCompanyId;
	private String uhidNumber;
	private Integer visitTypeId;
	private Integer companyTypeId;
	private Double crNetAmount;
	
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
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public Character getIsPendingOrSettled() {
		return isPendingOrSettled;
	}
	public void setIsPendingOrSettled(Character isPendingOrSettled) {
		this.isPendingOrSettled = isPendingOrSettled;
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
	public Integer getCompanyTypeId() {
		return companyTypeId;
	}
	public void setCompanyTypeId(Integer companyTypeId) {
		this.companyTypeId = companyTypeId;
	}

	public Double getCrNetAmount() {
		return crNetAmount;
	}

	public void setCrNetAmount(Double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}
}

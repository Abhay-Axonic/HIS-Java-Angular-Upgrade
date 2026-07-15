package com.param.billing.unit.dto;

import java.math.BigDecimal;

public class DepositMatrixConfigMasterDto {
	
	private Integer mDepositMatrixId;

	private Integer patientTypeId;

	private Integer paymentEntitlementId;

	private Integer bedCategoryId;
	
	private String patientType;
	
	private String patientEntitleMent;
	
	private String billingCategory;

	private Integer againstDeposit;
	
	private BigDecimal surgical;

	private BigDecimal nonSurgical;

	private char status;

	private Integer organisationId;

	private Integer unitId;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;

	public Integer getmDepositMatrixId() {
		return mDepositMatrixId;
	}

	public void setmDepositMatrixId(Integer mDepositMatrixId) {
		this.mDepositMatrixId = mDepositMatrixId;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public Integer getAgainstDeposit() {
		return againstDeposit;
	}

	public void setAgainstDeposit(Integer againstDeposit) {
		this.againstDeposit = againstDeposit;
	}

	public BigDecimal getSurgical() {
		return surgical;
	}

	public void setSurgical(BigDecimal surgical) {
		this.surgical = surgical;
	}

	public BigDecimal getNonSurgical() {
		return nonSurgical;
	}

	public void setNonSurgical(BigDecimal nonSurgical) {
		this.nonSurgical = nonSurgical;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public String getPatientType() {
		return patientType;
	}

	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}

	public String getPatientEntitleMent() {
		return patientEntitleMent;
	}

	public void setPatientEntitleMent(String patientEntitleMent) {
		this.patientEntitleMent = patientEntitleMent;
	}

	public String getBillingCategory() {
		return billingCategory;
	}

	public void setBillingCategory(String billingCategory) {
		this.billingCategory = billingCategory;
	}
	
	

}

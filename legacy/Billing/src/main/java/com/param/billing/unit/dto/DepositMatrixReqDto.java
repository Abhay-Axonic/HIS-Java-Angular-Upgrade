package com.param.billing.unit.dto;

public class DepositMatrixReqDto {
	
	private Integer patientTypeId;

	private Integer paymentEntitlementId;

	private Integer bedCategoryId;
	
	private Integer organsationId;
	
	private Integer unitId;
	
	private int sergicalOrMedical;
	
	private Integer againstDeposit;

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

	public Integer getOrgansationId() {
		return organsationId;
	}

	public void setOrgansationId(Integer organsationId) {
		this.organsationId = organsationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public int getSergicalOrMedical() {
		return sergicalOrMedical;
	}

	public void setSergicalOrMedical(int sergicalOrMedical) {
		this.sergicalOrMedical = sergicalOrMedical;
	}

	public Integer getAgainstDeposit() {
		return againstDeposit;
	}

	public void setAgainstDeposit(Integer againstDeposit) {
		this.againstDeposit = againstDeposit;
	}
	
	
	

}

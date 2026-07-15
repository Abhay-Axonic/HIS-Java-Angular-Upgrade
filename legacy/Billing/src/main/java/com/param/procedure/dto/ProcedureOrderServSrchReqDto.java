package com.param.procedure.dto;

public class ProcedureOrderServSrchReqDto {
	private Integer orgId;
	private Integer unitId;
	private String serviceName;
	private Integer specialityId;
	private Integer tariffId;
	private Integer paymentEntitlementId;
	private Integer visitTypeId;
	private Integer patientTypeId;
	private Integer billingBedCategoryId;
	private Integer procedureTypeId;
	private char isProcedure;
	public char getIsProcedure() {
		return isProcedure;
	}
	public void setIsProcedure(char isProcedure) {
		this.isProcedure = (isProcedure != '\u0000' ? isProcedure : 'N');
	}
	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}
	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
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
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public Integer getTariffId() {
		return tariffId;
	}
	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}
	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}
	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public Integer getPatientTypeId() {
		return patientTypeId;
	}
	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}
	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}
	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}
	
}

package com.param.global.dto;

import java.util.List;

public class PatientSponsorForCostEstimationDto {

	 private Integer visitTypeId;
	 
	 private Integer billingBedCategoryId;
	 
	 private Integer patientId;
	 
	 private Integer patietCategoryId;
	 
	 private Integer organizationId;
	 
	 private Integer unitId;
	 
	 private String doa;
	 
	 private String pdd;
	 
	 private Integer contractId;
	 
	 private List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList;

	 private Integer paymentEntitlementId;
	 
	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
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

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getPatietCategoryId() {
		return patietCategoryId;
	}

	public void setPatietCategoryId(Integer patietCategoryId) {
		this.patietCategoryId = patietCategoryId;
	}

	public String getDoa() {
		return doa;
	}

	public void setDoa(String doa) {
		this.doa = doa;
	}

	public String getPdd() {
		return pdd;
	}

	public void setPdd(String pdd) {
		this.pdd = pdd;
	}

	public List<PatientSponsorDetailsDto> getPatientSponsorDetailsDtosList() {
		return patientSponsorDetailsDtosList;
	}

	public void setPatientSponsorDetailsDtosList(List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList) {
		this.patientSponsorDetailsDtosList = patientSponsorDetailsDtosList;
	}
	 
}

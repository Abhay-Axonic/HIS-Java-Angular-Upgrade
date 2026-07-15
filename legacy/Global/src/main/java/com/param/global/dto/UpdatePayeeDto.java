package com.param.global.dto;

import java.util.List;

public class UpdatePayeeDto 
{

	List<PatientSponsorDetailsDto> patientSponsorDetailsDtoList;
	
	private Integer billingBedCategoryId;
	
	private Integer patientCategoryId;
	
	private Integer defaultSelfTariffId;
	
	private Integer selfPayeeId;
	
	private Integer visitTypeId;
	
	private Integer paymentEntitlementId;
	
	private Integer contractId;
	
	private Integer admissionId;
	
	private Integer encounterId;
	
	private Integer unitId;
	
	private Integer orgId;
	
	private Character isPreBilling;
	
	private Integer patientId;
	
	private Integer referralId;
	
	private Integer createdBy;
	
	private Integer patientClassId;
	
	private Integer pharmacyServiceId;
	
	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public Character getIsPreBilling() {
		return isPreBilling;
	}

	public void setIsPreBilling(Character isPreBilling) {
		this.isPreBilling = isPreBilling;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public List<PatientSponsorDetailsDto> getPatientSponsorDetailsDtoList() {
		return patientSponsorDetailsDtoList;
	}

	public void setPatientSponsorDetailsDtoList(List<PatientSponsorDetailsDto> patientSponsorDetailsDtoList) {
		this.patientSponsorDetailsDtoList = patientSponsorDetailsDtoList;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Integer getDefaultSelfTariffId() {
		return defaultSelfTariffId;
	}

	public void setDefaultSelfTariffId(Integer defaultSelfTariffId) {
		this.defaultSelfTariffId = defaultSelfTariffId;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public Integer getPharmacyServiceId() {
		return pharmacyServiceId;
	}

	public void setPharmacyServiceId(Integer pharmacyServiceId) {
		this.pharmacyServiceId = pharmacyServiceId;
	}
	
}

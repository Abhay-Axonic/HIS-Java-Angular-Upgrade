package com.param.billing.unit.dto;

import java.util.Date;

public class TIndividualDocShareServiceWiseDto {

	private Integer individualServiceWiseId;
	
	private Integer specialityId;
	
	private Integer orgId;
	private Integer unitId;
	
	private Integer paymentEntitlementId;
	
	private Integer serviceId;
	
	private Double docShare;
	
	private Character status;
	
	private Long effectiveDate;
	
	private Integer doctorId;
	
	private Integer billingBedCategoryId;
	
	private Integer visitTypeId;
	
	private Integer patientTypeId;

	private Integer createdBy;
	
	private Integer updatedBy;
	
	private Date updatedDate;
	
	private Date createdDate;
	
   private String specialityName; 
	
	private String doctorName;
	
    private String billingBedDesc;
	
	private String paymentEntitlementDesc;
	
	private String visitTypeName;
	
	private String globalSharEffectiveDate;
	
	private String patientCategory;
	
	private String serviceName;
	
    private Integer tariffId;
	
	private Double amount;
	
	
	
	
	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getIndividualServiceWiseId() {
		return individualServiceWiseId;
	}

	public void setIndividualServiceWiseId(Integer individualServiceWiseId) {
		this.individualServiceWiseId = individualServiceWiseId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
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

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Double getDocShare() {
		return docShare;
	}

	public void setDocShare(Double docShare) {
		this.docShare = docShare;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Long getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Long effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getBillingBedDesc() {
		return billingBedDesc;
	}

	public void setBillingBedDesc(String billingBedDesc) {
		this.billingBedDesc = billingBedDesc;
	}

	public String getPaymentEntitlementDesc() {
		return paymentEntitlementDesc;
	}

	public void setPaymentEntitlementDesc(String paymentEntitlementDesc) {
		this.paymentEntitlementDesc = paymentEntitlementDesc;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public String getGlobalSharEffectiveDate() {
		return globalSharEffectiveDate;
	}

	public void setGlobalSharEffectiveDate(String globalSharEffectiveDate) {
		this.globalSharEffectiveDate = globalSharEffectiveDate;
	}

	public String getPatientCategory() {
		return patientCategory;
	}

	public void setPatientCategory(String patientCategory) {
		this.patientCategory = patientCategory;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}

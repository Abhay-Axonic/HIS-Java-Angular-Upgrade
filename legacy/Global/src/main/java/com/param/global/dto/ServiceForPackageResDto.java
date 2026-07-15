package com.param.global.dto;

import java.util.List;

public class ServiceForPackageResDto {
	private Integer serviceMasterId;
	private Integer specialityId;
	private String groupDesc;
	private Integer subSpecialityMasterId;
	private String subGroup;
	private String serviceCode;
	private String serviceDescription;
	private Double rate;
	private String effectiveDate;

	private Integer tariffId;
	private Integer paymentEntitlementId;
	private Integer patientTypeId;
	private Integer serviceId;
	private Character isDoctorRequired;
	private Integer visitTypeId;
	private Integer billingBedCategoryId; // package change

	private String paymentEntitlement;
	private String patientType;
	private String visitTypeName;
	private String billingBedCategory;

	private Integer itemId;
	private String itemName;
	private String itemCode;
	private Integer productCategoryId;
	private List<ServiceMasterDto> serviceList;
	private Character isDoctorReferralRequired;

	public Character getIsDoctorReferralRequired() {
		return isDoctorReferralRequired;
	}

	public void setIsDoctorReferralRequired(Character isDoctorReferralRequired) {
		this.isDoctorReferralRequired = isDoctorReferralRequired;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getGroupDesc() {
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public Integer getSubSpecialityMasterId() {
		return subSpecialityMasterId;
	}

	public void setSubSpecialityMasterId(Integer subSpecialityMasterId) {
		this.subSpecialityMasterId = subSpecialityMasterId;
	}

	public String getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(String subGroup) {
		this.subGroup = subGroup;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public List<ServiceMasterDto> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<ServiceMasterDto> serviceList) {
		this.serviceList = serviceList;
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

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Character getIsDoctorRequired() {
		return isDoctorRequired;
	}

	public void setIsDoctorRequired(Character isDoctorRequired) {
		this.isDoctorRequired = isDoctorRequired;
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

	public String getPaymentEntitlement() {
		return paymentEntitlement;
	}

	public void setPaymentEntitlement(String paymentEntitlement) {
		this.paymentEntitlement = paymentEntitlement;
	}

	public String getPatientType() {
		return patientType;
	}

	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public String getBillingBedCategory() {
		return billingBedCategory;
	}

	public void setBillingBedCategory(String billingBedCategory) {
		this.billingBedCategory = billingBedCategory;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	

}

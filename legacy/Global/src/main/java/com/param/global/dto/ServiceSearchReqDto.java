package com.param.global.dto;

public class ServiceSearchReqDto {

	private String serviceName;
	private String serviceCode;
	private Integer organisationId;
	private Integer unitId;
	private Integer groupId;
	private Integer subGroupId;
	private Integer packageTypeId;
	private Integer visitTypeId;
	private Integer bedBillingCategoryId;
	private Integer genderId;
	private Integer paymentEntitlementId;
	private Integer patientTypeId;
	private Integer parentPackageId;

	private Integer age;
	
	public Integer getGenderId() {
		return genderId;
	}
	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public Integer getBedBillingCategoryId() {
		return bedBillingCategoryId;
	}
	public void setBedBillingCategoryId(Integer bedBillingCategoryId) {
		this.bedBillingCategoryId = bedBillingCategoryId;
	}
	public Integer getPackageTypeId() {
		return packageTypeId;
	}
	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
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
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getSubGroupId() {
		return subGroupId;
	}
	public void setSubGroupId(Integer subGroupId) {
		this.subGroupId = subGroupId;
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
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Integer getParentPackageId() {
		return parentPackageId;
	}
	public void setParentPackageId(Integer parentPackageId) {
		this.parentPackageId = parentPackageId;
	}
	
}

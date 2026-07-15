package com.param.global.dto;

import java.util.List;

public class ParentPackageMasterDto {

	private Integer parentPackageId;

	private Integer packageTypeId;

	private String packageName;

	private Integer sexId;

	private String validityFromDate;

	private String validityToDate;

	private char status;

	private int validityPeriodIndays;

	private Integer noOfEncounters;

	private Integer minAge;

	private Integer maxAge;

	private Integer serviceId;

	private Integer tariffId;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;

	private Integer unitId;

	private Integer orgId;

	private Integer versionId;

	List<MPackageMasterDto> packageMasterDtoList;

	List<CommonDto>  paymentEntitlement;
	List<CommonDto> patientCategoryList;
	List<BBedCategoryEPDto> billingBedCategoryList;
	List<ServiceForPackageReqDto> serviceForPackageReqList;


	public Integer getParentPackageId() {
		return parentPackageId;
	}

	public void setParentPackageId(Integer parentPackageId) {
		this.parentPackageId = parentPackageId;
	}

	public Integer getSexId() {
		return sexId;
	}

	public void setSexId(Integer sexId) {
		this.sexId = sexId;
	}

	public String getValidityFromDate() {
		return validityFromDate;
	}

	public void setValidityFromDate(String validityFromDate) {
		this.validityFromDate = validityFromDate;
	}

	public String getValidityToDate() {
		return validityToDate;
	}

	public void setValidityToDate(String validityToDate) {
		this.validityToDate = validityToDate;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getValidityPeriodIndays() {
		return validityPeriodIndays;
	}

	public void setValidityPeriodIndays(int validityPeriodIndays) {
		this.validityPeriodIndays = validityPeriodIndays;
	}

	public Integer getNoOfEncounters() {
		return noOfEncounters;
	}

	public void setNoOfEncounters(Integer noOfEncounters) {
		this.noOfEncounters = noOfEncounters;
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
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

	public List<MPackageMasterDto> getPackageMasterDtoList() {
		return packageMasterDtoList;
	}

	public void setPackageMasterDtoList(List<MPackageMasterDto> packageMasterDtoList) {
		this.packageMasterDtoList = packageMasterDtoList;
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

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}


	public List<CommonDto> getPaymentEntitlement() {
		return paymentEntitlement;
	}

	public void setPaymentEntitlement(List<CommonDto> paymentEntitlement) {
		this.paymentEntitlement = paymentEntitlement;
	}

	public List<CommonDto> getPatientCategoryList() {
		return patientCategoryList;
	}

	public void setPatientCategoryList(List<CommonDto> patientCategoryList) {
		this.patientCategoryList = patientCategoryList;
	}

	public List<BBedCategoryEPDto> getBillingBedCategoryList() {
		return billingBedCategoryList;
	}

	public void setBillingBedCategoryList(List<BBedCategoryEPDto> billingBedCategoryList) {
		this.billingBedCategoryList = billingBedCategoryList;
	}

	public List<ServiceForPackageReqDto> getServiceForPackageReqList() {
		return serviceForPackageReqList;
	}

	public void setServiceForPackageReqList(List<ServiceForPackageReqDto> serviceForPackageReqList) {
		this.serviceForPackageReqList = serviceForPackageReqList;
	}
}

package com.param.global.dto;

import java.util.List;

public class MPackageMasterDto {
	private Integer packageMasterId;
	private Integer parentPackageId;
	private Integer visitTypeId;
	private Integer patientTypeId;
	private Integer packageTypeId;
	private Integer billingBedCategoryId;
	private Integer paymentEntitlementTypeId;
	private Integer sexId;
	private String validityFromDate;
	private String validityToDate;
	private char status;
	private double packageCost;
	private double markupDownInPercentage;
	private double packagePrice;
	private char isManualRoundingIsAllow;
	private double manualRoundoffAmount;
	private String createdDate;
	private Integer createdBy;
	private String updatedDate;
	private Integer updatedBy;
	private int validityPeriodIndays;
	private Integer minAge;
	private Integer maxAge;
	private Integer serviceId;
	private Integer orgId;
	private Integer unitId;
	private Integer tariffId;
	private Integer isMarkupDown;
	private Integer roundOffPositNeg;
	private Integer noOfEncounters;
	private String packageName;
	private String packageType;
	private String packageCode;

	private String paymentEntitlement;
	private String patientType;
	private String visitTypeName;
	private String billingBedCategory;
	private Integer versionId;
	private List<TPackageServicesDetailsDto> listTPackageServicesDetailsDto;// ehc
	private List<TPackageServicesDetailsDto> listTPackageItemDetailsDto;// ehc
	private List<TPackageIncExcDetailsDto> listTPackageIncExcServiceDetailsDto;// allInclusive
	private List<TPackageIncExcDetailsDto> listTPackageIncExcItemDetailsDto;// allInclusive

	// with cap
	private List<TPackageBedCategoryDetailDto> listTPackageBedCategoryDetailDto;
	private List<TPackageCapDetailsDto> listTPackageGroupWiseCapDetailsDto;
	private List<TPackageCapDetailsDto> listTPackageConsumableWiseCapDetailsDto;

	private List<TPackageCategoryWiseConsumableDetailsDto> listTPackageCategoryWiseConsumableDetailsDto;

	private List<TPackageIncExcDetailsDto> listGroupWiseIncExc;
	/* private List<TPackageIncExcDetailsDto> listCategoryWiseExc; */
	private List<TPackageIncExcDetailsDto> listConsumableWiseIncExc;
	private List<TPackageServicesDetailsDto> listTPackageConsumableDetailsDto;

	private List<TPackageEitherorGroupDetailsDto> listTPackageEitherorGroupDetailsDto;

	// PackageCalculation
	private Double grandTotal;
	private Double serviceWiseSummary;
	private Double groupWiseSummary;
	private Double bedCatSummary;
	private Double consumDtlsSummary;
	private Double catWiseConsumSummary;
	private Double consumWiseSummary;

	private Double packageGroupAmt;
	private Double serviceGroupAmt;

	private Integer subSpecialityId;

	private Integer specialityId;

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Integer getNoOfEncounters() {
		return noOfEncounters;
	}

	public void setNoOfEncounters(Integer noOfEncounters) {
		this.noOfEncounters = noOfEncounters;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getIsMarkupDown() {
		return isMarkupDown!=null ? isMarkupDown : 0;
	}

	public void setIsMarkupDown(Integer isMarkupDown) {
		this.isMarkupDown = isMarkupDown;
	}

	public Integer getRoundOffPositNeg() {
		return roundOffPositNeg!=null ? roundOffPositNeg : 0;
	}

	public void setRoundOffPositNeg(Integer roundOffPositNeg) {
		this.roundOffPositNeg = roundOffPositNeg;
	}

	public Integer getPackageMasterId() {
		return packageMasterId;
	}

	public void setPackageMasterId(Integer packageMasterId) {
		this.packageMasterId = packageMasterId;
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

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getPaymentEntitlementTypeId() {
		return paymentEntitlementTypeId;
	}

	public void setPaymentEntitlementTypeId(Integer paymentEntitlementTypeId) {
		this.paymentEntitlementTypeId = paymentEntitlementTypeId;
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
		this.status = (status != '\u0000' ? status : 'A');
	}

	public double getPackageCost() {
		return packageCost;
	}

	public void setPackageCost(double packageCost) {
		this.packageCost = packageCost;
	}

	public double getMarkupDownInPercentage() {
		return markupDownInPercentage;
	}

	public void setMarkupDownInPercentage(double markupDownInPercentage) {
		this.markupDownInPercentage = markupDownInPercentage;
	}

	public double getPackagePrice() {
		return packagePrice;
	}

	public void setPackagePrice(double packagePrice) {
		this.packagePrice = packagePrice;
	}

	public char getIsManualRoundingIsAllow() {
		return isManualRoundingIsAllow;
	}

	public void setIsManualRoundingIsAllow(char isManualRoundingIsAllow) {
		this.isManualRoundingIsAllow = (isManualRoundingIsAllow != '\u0000' ? isManualRoundingIsAllow : 'N');
	}

	public double getManualRoundoffAmount() {
		return manualRoundoffAmount;
	}

	public void setManualRoundoffAmount(double manualRoundoffAmount) {
		this.manualRoundoffAmount = manualRoundoffAmount;
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

	public int getValidityPeriodIndays() {
		return validityPeriodIndays;
	}

	public void setValidityPeriodIndays(int validityPeriodIndays) {
		this.validityPeriodIndays = validityPeriodIndays;
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

	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public List<TPackageIncExcDetailsDto> getListTPackageIncExcServiceDetailsDto() {
		return listTPackageIncExcServiceDetailsDto;
	}

	public void setListTPackageIncExcServiceDetailsDto(
			List<TPackageIncExcDetailsDto> listTPackageIncExcServiceDetailsDto) {
		this.listTPackageIncExcServiceDetailsDto = listTPackageIncExcServiceDetailsDto;
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

	public String getBillingBedCategory() {
		return billingBedCategory;
	}

	public void setBillingBedCategory(String billingBedCategory) {
		this.billingBedCategory = billingBedCategory;
	}

	public List<TPackageServicesDetailsDto> getListTPackageServicesDetailsDto() {
		return listTPackageServicesDetailsDto;
	}

	public void setListTPackageServicesDetailsDto(List<TPackageServicesDetailsDto> listTPackageServicesDetailsDto) {
		this.listTPackageServicesDetailsDto = listTPackageServicesDetailsDto;
	}

	public Integer getParentPackageId() {
		return parentPackageId;
	}

	public void setParentPackageId(Integer parentPackageId) {
		this.parentPackageId = parentPackageId;
	}

	public List<TPackageIncExcDetailsDto> getListTPackageIncExcItemDetailsDto() {
		return listTPackageIncExcItemDetailsDto;
	}

	public void setListTPackageIncExcItemDetailsDto(List<TPackageIncExcDetailsDto> listTPackageIncExcItemDetailsDto) {
		this.listTPackageIncExcItemDetailsDto = listTPackageIncExcItemDetailsDto;
	}

	public List<TPackageServicesDetailsDto> getListTPackageItemDetailsDto() {
		return listTPackageItemDetailsDto;
	}

	public void setListTPackageItemDetailsDto(List<TPackageServicesDetailsDto> listTPackageItemDetailsDto) {
		this.listTPackageItemDetailsDto = listTPackageItemDetailsDto;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Double getServiceWiseSummary() {
		return serviceWiseSummary;
	}

	public void setServiceWiseSummary(Double serviceWiseSummary) {
		this.serviceWiseSummary = serviceWiseSummary;
	}

	public List<TPackageBedCategoryDetailDto> getListTPackageBedCategoryDetailDto() {
		return listTPackageBedCategoryDetailDto;
	}

	public void setListTPackageBedCategoryDetailDto(
			List<TPackageBedCategoryDetailDto> listTPackageBedCategoryDetailDto) {
		this.listTPackageBedCategoryDetailDto = listTPackageBedCategoryDetailDto;
	}

	public List<TPackageCapDetailsDto> getListTPackageGroupWiseCapDetailsDto() {
		return listTPackageGroupWiseCapDetailsDto;
	}

	public void setListTPackageGroupWiseCapDetailsDto(List<TPackageCapDetailsDto> listTPackageGroupWiseCapDetailsDto) {
		this.listTPackageGroupWiseCapDetailsDto = listTPackageGroupWiseCapDetailsDto;
	}

	public List<TPackageCategoryWiseConsumableDetailsDto> getListTPackageCategoryWiseConsumableDetailsDto() {
		return listTPackageCategoryWiseConsumableDetailsDto;
	}

	public void setListTPackageCategoryWiseConsumableDetailsDto(
			List<TPackageCategoryWiseConsumableDetailsDto> listTPackageCategoryWiseConsumableDetailsDto) {
		this.listTPackageCategoryWiseConsumableDetailsDto = listTPackageCategoryWiseConsumableDetailsDto;
	}

	public List<TPackageIncExcDetailsDto> getListGroupWiseIncExc() {
		return listGroupWiseIncExc;
	}

	public void setListGroupWiseIncExc(List<TPackageIncExcDetailsDto> listGroupWiseIncExc) {
		this.listGroupWiseIncExc = listGroupWiseIncExc;
	}

	/*
	 * public List<TPackageIncExcDetailsDto> getListCategoryWiseExc() { return
	 * listCategoryWiseExc; }
	 * 
	 * public void setListCategoryWiseExc(List<TPackageIncExcDetailsDto>
	 * listCategoryWiseExc) { this.listCategoryWiseExc = listCategoryWiseExc; }
	 */

	public List<TPackageIncExcDetailsDto> getListConsumableWiseIncExc() {
		return listConsumableWiseIncExc;
	}

	public void setListConsumableWiseIncExc(List<TPackageIncExcDetailsDto> listConsumableWiseIncExc) {
		this.listConsumableWiseIncExc = listConsumableWiseIncExc;
	}

	public List<TPackageCapDetailsDto> getListTPackageConsumableWiseCapDetailsDto() {
		return listTPackageConsumableWiseCapDetailsDto;
	}

	public void setListTPackageConsumableWiseCapDetailsDto(
			List<TPackageCapDetailsDto> listTPackageConsumableWiseCapDetailsDto) {
		this.listTPackageConsumableWiseCapDetailsDto = listTPackageConsumableWiseCapDetailsDto;
	}

	public List<TPackageServicesDetailsDto> getListTPackageConsumableDetailsDto() {
		return listTPackageConsumableDetailsDto;
	}

	public void setListTPackageConsumableDetailsDto(List<TPackageServicesDetailsDto> listTPackageConsumableDetailsDto) {
		this.listTPackageConsumableDetailsDto = listTPackageConsumableDetailsDto;
	}

	public Double getGroupWiseSummary() {
		return groupWiseSummary;
	}

	public void setGroupWiseSummary(Double groupWiseSummary) {
		this.groupWiseSummary = groupWiseSummary;
	}

	public Double getBedCatSummary() {
		return bedCatSummary;
	}

	public void setBedCatSummary(Double bedCatSummary) {
		this.bedCatSummary = bedCatSummary;
	}

	public Double getConsumDtlsSummary() {
		return consumDtlsSummary;
	}

	public void setConsumDtlsSummary(Double consumDtlsSummary) {
		this.consumDtlsSummary = consumDtlsSummary;
	}

	public Double getCatWiseConsumSummary() {
		return catWiseConsumSummary;
	}

	public void setCatWiseConsumSummary(Double catWiseConsumSummary) {
		this.catWiseConsumSummary = catWiseConsumSummary;
	}

	public Double getConsumWiseSummary() {
		return consumWiseSummary;
	}

	public void setConsumWiseSummary(Double consumWiseSummary) {
		this.consumWiseSummary = consumWiseSummary;
	}

	public List<TPackageEitherorGroupDetailsDto> getListTPackageEitherorGroupDetailsDto() {
		return listTPackageEitherorGroupDetailsDto;
	}

	public void setListTPackageEitherorGroupDetailsDto(
			List<TPackageEitherorGroupDetailsDto> listTPackageEitherorGroupDetailsDto) {
		this.listTPackageEitherorGroupDetailsDto = listTPackageEitherorGroupDetailsDto;
	}

	public Double getPackageGroupAmt() {
		return packageGroupAmt;
	}

	public void setPackageGroupAmt(Double packageGroupAmt) {
		this.packageGroupAmt = packageGroupAmt;
	}

	public Double getServiceGroupAmt() {
		return serviceGroupAmt;
	}

	public void setServiceGroupAmt(Double serviceGroupAmt) {
		this.serviceGroupAmt = serviceGroupAmt;
	}

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

}

package com.param.global.dto;

import java.math.BigDecimal;
import java.util.List;

public class ServiceSearchResDto {

	private Integer serviceMasterId;
	private Integer organizationId;
	private String serviceAliasName;
	private String serviceStandardName;
	private String serviceStandardCode;
	private Integer specialityId;
	private Integer subSpecialityId;
	private Character isAvailableForPackage;
	private Character isOutsourceService;
	private Character isOtProcedure;
	private Character isOrderSet;
	private Character isRateEditable;
	private BigDecimal minRateEditable;
	private BigDecimal maxRateEditable;
	private Character isDiscountApplicable;
	private Character isAllowMultipleQuantity;
	private Character isDoctorRequired;
	private Character isTaxApplicable;
	private Integer renderingDepartmentId;
	private Character isAutorendered;
	private Character status;
	private Integer unitId;
	private String specialityName;
	private String subSpecialityName;
	private BigDecimal basePrice;
	private Double concession;
	private BigDecimal discount;
	private Integer taxId;
	private String taxName;
	private Double taxPercentage;
	private Character isQuantityEditable;
	private Integer procedureDuration;
	private Double basePriceForContract;
	private Character isRenderingSpeciality;
	private List<OrderDetailsDiscountDto> discountDetailsList;
	private Integer packageMasterId;
	private Integer parentPackageId;
	private String packageName;
	private Double packagePrice;
	private Integer quantity;
	private String packageType;
	private Integer packageTypeId;
	private Character isRefDoctorShare;
	private String orderDate;
	private Integer contractId;
	private Integer encounterId;
	private Integer admissionId;
	private Integer selfPayeeId;
	private Integer visitTypeId;
	private Integer billingBedCategoryId;
	private Integer patientCategoryId;
	private Integer defaultSelfTariffId;
	private Integer patientClassId;
	private Integer minAge;
	private Integer maxAge;
	private Integer sexId;
	private Character isAppointable;
	private String cptCode;
	private Double changedPrice;
	private Double ordDiscAmt;
	private Character isFromEncounter;
	private Character updateCap;
	private Double patientClassPer;
	private Integer procedureTypeId;
	private String effectiveDate;
	private Integer priorityId;
	private Integer procedureConfigurationId;
	private boolean applyAuth;
	private Double ordDiscPer;
	private Integer medallServiceId;
    private Character preRequisite;
	private String preRequisiteDetails;
	
	public Double getOrdDiscPer() {
		return ordDiscPer;
	}

	public void setOrdDiscPer(Double ordDiscPer) {
		this.ordDiscPer = ordDiscPer;
	}

	public boolean isApplyAuth() {
		return applyAuth;
	}

	public void setApplyAuth(boolean applyAuth) {
		this.applyAuth = applyAuth;
	}
	
	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Double getPatientClassPer() {
		return patientClassPer;
	}

	public void setPatientClassPer(Double patientClassPer) {
		this.patientClassPer = patientClassPer;
	}

	public Character getUpdateCap() {
		return updateCap;
	}

	public void setUpdateCap(Character updateCap) {
		this.updateCap = updateCap;
	}

	public Character getIsFromEncounter() {
		return isFromEncounter;
	}

	public void setIsFromEncounter(Character isFromEncounter) {
		this.isFromEncounter = isFromEncounter;
	}

	public Double getOrdDiscAmt() {
		return ordDiscAmt;
	}

	public void setOrdDiscAmt(Double ordDiscAmt) {
		this.ordDiscAmt = ordDiscAmt;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public Double getChangedPrice() {
		return changedPrice;
	}

	public void setChangedPrice(Double changedPrice) {
		this.changedPrice = changedPrice;
	}

	public Double getBasePriceForContract() {
		return basePriceForContract;
	}

	public void setBasePriceForContract(Double basePriceForContract) {
		this.basePriceForContract = basePriceForContract;
	}

	public Integer getDefaultSelfTariffId() {
		return defaultSelfTariffId;
	}

	public void setDefaultSelfTariffId(Integer defaultSelfTariffId) {
		this.defaultSelfTariffId = defaultSelfTariffId;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
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

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Character getIsRefDoctorShare() {
		return isRefDoctorShare;
	}

	public void setIsRefDoctorShare(Character isRefDoctorShare) {
		this.isRefDoctorShare = isRefDoctorShare;
	}

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

	public Double getPackagePrice() {
		return packagePrice;
	}

	public void setPackagePrice(Double packagePrice) {
		this.packagePrice = packagePrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getPackageMasterId() {
		return packageMasterId;
	}

	public void setPackageMasterId(Integer packageMasterId) {
		this.packageMasterId = packageMasterId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<OrderDetailsDiscountDto> getDiscountDetailsList() {
		return discountDetailsList;
	}

	public void setDiscountDetailsList(List<OrderDetailsDiscountDto> discountDetailsList) {
		this.discountDetailsList = discountDetailsList;
	}

	public Character getIsQuantityEditable() {
		return isQuantityEditable;
	}

	public void setIsQuantityEditable(Character isQuantityEditable) {
		this.isQuantityEditable = isQuantityEditable;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getServiceAliasName() {
		return serviceAliasName;
	}

	public void setServiceAliasName(String serviceAliasName) {
		this.serviceAliasName = serviceAliasName;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Character getIsAvailableForPackage() {
		return isAvailableForPackage;
	}

	public void setIsAvailableForPackage(Character isAvailableForPackage) {
		this.isAvailableForPackage = isAvailableForPackage;
	}

	public Character getIsOutsourceService() {
		return isOutsourceService;
	}

	public void setIsOutsourceService(Character isOutsourceService) {
		this.isOutsourceService = isOutsourceService;
	}

	public Character getIsOtProcedure() {
		return isOtProcedure;
	}

	public void setIsOtProcedure(Character isOtProcedure) {
		this.isOtProcedure = isOtProcedure;
	}

	public Character getIsOrderSet() {
		return isOrderSet;
	}

	public void setIsOrderSet(Character isOrderSet) {
		this.isOrderSet = isOrderSet;
	}

	public Character getIsRateEditable() {
		return isRateEditable;
	}

	public void setIsRateEditable(Character isRateEditable) {
		this.isRateEditable = isRateEditable;
	}

	public BigDecimal getMinRateEditable() {
		return minRateEditable;
	}

	public void setMinRateEditable(BigDecimal minRateEditable) {
		this.minRateEditable = minRateEditable;
	}

	public BigDecimal getMaxRateEditable() {
		return maxRateEditable;
	}

	public void setMaxRateEditable(BigDecimal maxRateEditable) {
		this.maxRateEditable = maxRateEditable;
	}

	public Character getIsDiscountApplicable() {
		return isDiscountApplicable;
	}

	public void setIsDiscountApplicable(Character isDiscountApplicable) {
		this.isDiscountApplicable = isDiscountApplicable;
	}

	public Character getIsAllowMultipleQuantity() {
		return isAllowMultipleQuantity;
	}

	public void setIsAllowMultipleQuantity(Character isAllowMultipleQuantity) {
		this.isAllowMultipleQuantity = isAllowMultipleQuantity;
	}

	public Character getIsDoctorRequired() {
		return isDoctorRequired;
	}

	public void setIsDoctorRequired(Character isDoctorRequired) {
		this.isDoctorRequired = isDoctorRequired;
	}

	public Character getIsTaxApplicable() {
		return isTaxApplicable;
	}

	public void setIsTaxApplicable(Character isTaxApplicable) {
		this.isTaxApplicable = isTaxApplicable;
	}

	public Integer getRenderingDepartmentId() {
		return renderingDepartmentId;
	}

	public void setRenderingDepartmentId(Integer renderingDepartmentId) {
		this.renderingDepartmentId = renderingDepartmentId;
	}

	public Character getIsAutorendered() {
		return isAutorendered;
	}

	public void setIsAutorendered(Character isAutorendered) {
		this.isAutorendered = isAutorendered;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getSubSpecialityName() {
		return subSpecialityName;
	}

	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public Double getConcession() {
		return concession;
	}

	public void setConcession(Double concession) {
		this.concession = concession;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Integer getProcedureDuration() {
		return procedureDuration;
	}

	public void setProcedureDuration(Integer procedureDuration) {
		this.procedureDuration = procedureDuration;
	}

	public Character getIsAppointable() {
		return isAppointable;
	}

	public void setIsAppointable(Character isAppointable) {
		this.isAppointable = isAppointable;
	}

	public String getCptCode() {
		return cptCode;
	}

	public void setCptCode(String cptCode) {
		this.cptCode = cptCode;
	}

	public Character getIsRenderingSpeciality() {
		return isRenderingSpeciality;
	}

	public void setIsRenderingSpeciality(Character isRenderingSpeciality) {
		this.isRenderingSpeciality = isRenderingSpeciality;
	}

	public Integer getParentPackageId() {
		return parentPackageId;
	}

	public void setParentPackageId(Integer parentPackageId) {
		this.parentPackageId = parentPackageId;
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

	public Integer getSexId() {
		return sexId;
	}

	public void setSexId(Integer sexId) {
		this.sexId = sexId;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}

	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}

	public Integer getProcedureConfigurationId() {
		return procedureConfigurationId;
	}

	public void setProcedureConfigurationId(Integer procedureConfigurationId) {
		this.procedureConfigurationId = procedureConfigurationId;
	}

	public Integer getMedallServiceId() {
		return medallServiceId;
	}

	public void setMedallServiceId(Integer medallServiceId) {
		this.medallServiceId = medallServiceId;
	}

	public Character getPreRequisite() {
		return preRequisite;
	}

	public void setPreRequisite(Character preRequisite) {
		this.preRequisite = preRequisite;
	}

	public String getPreRequisiteDetails() {
		return preRequisiteDetails;
	}

	public void setPreRequisiteDetails(String preRequisiteDetails) {
		this.preRequisiteDetails = preRequisiteDetails;
	}

	
}

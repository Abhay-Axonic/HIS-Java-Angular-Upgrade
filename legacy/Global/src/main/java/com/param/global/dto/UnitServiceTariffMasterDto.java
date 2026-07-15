package com.param.global.dto;

import java.math.BigInteger;
import java.util.List;

public class UnitServiceTariffMasterDto 
{
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer tariffId;
	
	private String tariffDesc;
	
	private Integer paymentEntitlementId;
	
	private String paymentEntitlementDesc;
	
	private Integer patientTypeId;
	
	private String patientTypeDesc;
	
	private Integer visitTypeId;
	
	private String visitTypeDesc;
	
	private Integer billingBedCatId;
	
	private String billingBedCatDesc;
	
	private Integer specialityId;
	
	private String specialityDesc;
	
	private Integer subSpecialityId;
	
	private String subSpecialityDesc;
	
	private Integer serviceId;
	
	private String serviceDesc;
	
	private Double rate;
	
	private Double serviceRate;
	
	private Integer taxId;
	
	private Double taxPercentage;
	
	private Double profit;
	
	private String statPer;
	
	private String effectiveDate;
	
	private Integer unitServiceTriffId;
	
	private Integer billingBedCategoryId;
	
	private String createdDate;
	
	private Integer createdBy;
	
	private String updatedDate;
	
	private Integer updatedBy;
	
	private Character status;
	
	private Double serviceCost;
	
	private String suSpecialityDesc;
	
	private List<MultiSelectDto> tariffMasterList;
	
	private List<MultiSelectDto> paymentEntitlementList;
	
	private List<MultiSelectDto> visitTypeList;
	
	private List<MultiSelectDto> patientTypeList;
	
	private List<MultiSelectDto> selectedPatientTypeList;
	
	private List<MultiSelectDto> bedCategoryList;
	
	private List<Integer> specialityList;
	
	private List<Integer> subSpecialityList;
	
	private List<Integer> serviceList;
	
	private List<Integer> duplicateIdsList;
	
	private Integer alreadyExist;
	
	private String orderDate;
	
	private List<UnitServiceTariffMasterDto> unitServiceTariffMasterDtosList;
	
	private List<Integer> listServiceId;
	
	private List<Integer> tariffMasterIdList;
	
	private List<Integer> paymentEntitlementIdList;
	
	private List<Integer> visitTypeIdList;
	
	private List<Integer> patientTypeIdList;
	
	private List<Integer> bedCategoryIdList;
	
	private String serviceCode;
	
	private Integer patientClassId;
	
	private Double patientClassPercentage;
	
	private Boolean markupStatus;
	
	private Integer insurancePatientCategory;
	
	private Integer companyPatientCategory;
	
	private BigInteger unitServiceTriffIdCount;

	private Integer selfPayeeId;
	
	private Double minRateEditable;//Ganesh
	
	private Double maxRateEditable;//Ganesh
	
	private Character isRateEditableService;
	
	private Character isRateEditable;
	
	private Character isQuantityEditable;
	
	private Double finalRate;
	
	private Character isDocReq;
	
	private Character isRefReq;
	
	private Character isDiscount;
	
	private Character isSchemeDiscount;
	
	private Character isAutoRender;
	
    private Integer contractId;
	private Integer contractGradeMasterId;
	private Integer ambulanceChargesServiceId;
	
	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	
	public Character getIsAutoRender() {
		return isAutoRender;
	}

	public void setIsAutoRender(Character isAutoRender) {
		this.isAutoRender = isAutoRender;
	}

	public Character getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(Character isDiscount) {
		this.isDiscount = isDiscount;
	}

	public Character getIsSchemeDiscount() {
		return isSchemeDiscount;
	}

	public void setIsSchemeDiscount(Character isSchemeDiscount) {
		this.isSchemeDiscount = isSchemeDiscount;
	}

	public Character getIsDocReq() {
		return isDocReq;
	}

	public void setIsDocReq(Character isDocReq) {
		this.isDocReq = isDocReq;
	}

	public Character getIsRefReq() {
		return isRefReq;
	}

	public void setIsRefReq(Character isRefReq) {
		this.isRefReq = isRefReq;
	}
	
	public List<Integer> getDuplicateIdsList() {
		return duplicateIdsList;
	}

	public void setDuplicateIdsList(List<Integer> duplicateIdsList) {
		this.duplicateIdsList = duplicateIdsList;
	}

	public Double getFinalRate() {
		return finalRate;
	}

	public void setFinalRate(Double finalRate) {
		this.finalRate = finalRate;
	}

	public Character getIsRateEditableService() {
		return isRateEditableService;
	}

	public void setIsRateEditableService(Character isRateEditableService) {
		this.isRateEditableService = isRateEditableService;
	}

	public Character getIsQuantityEditable() {
		return isQuantityEditable;
	}

	public void setIsQuantityEditable(Character isQuantityEditable) {
		this.isQuantityEditable = isQuantityEditable;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public Double getServiceRate() {
		return serviceRate;
	}

	public void setServiceRate(Double serviceRate) {
		this.serviceRate = serviceRate;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Double getTaxPercentage() {
		return taxPercentage!=null ? taxPercentage : 0.00;
	}

	public void setTaxPercentage(Double taxPercentage) {
		 this.taxPercentage= taxPercentage!=null ? taxPercentage : 0.00;
	}

	public BigInteger getUnitServiceTriffIdCount() {
		return unitServiceTriffIdCount;
	}

	public void setUnitServiceTriffIdCount(BigInteger unitServiceTriffIdCount) {
		this.unitServiceTriffIdCount = unitServiceTriffIdCount;
	}

	public List<MultiSelectDto> getSelectedPatientTypeList() {
		return selectedPatientTypeList;
	}

	public void setSelectedPatientTypeList(List<MultiSelectDto> selectedPatientTypeList) {
		this.selectedPatientTypeList = selectedPatientTypeList;
	}

	public Integer getInsurancePatientCategory() {
		return insurancePatientCategory;
	}

	public void setInsurancePatientCategory(Integer insurancePatientCategory) {
		this.insurancePatientCategory = insurancePatientCategory;
	}

	public Integer getCompanyPatientCategory() {
		return companyPatientCategory;
	}

	public void setCompanyPatientCategory(Integer companyPatientCategory) {
		this.companyPatientCategory = companyPatientCategory;
	}

	public Boolean getMarkupStatus() {
		return (markupStatus==null)?false:markupStatus;
	}

	public void setMarkupStatus(Boolean markupStatus) {
		this.markupStatus = (markupStatus==null)?false:markupStatus;;
	}

	public Double getPatientClassPercentage() {
		return (patientClassPercentage==null)?0:patientClassPercentage;
	}

	public void setPatientClassPercentage(Double patientClassPercentage) {
		this.patientClassPercentage = (patientClassPercentage==null)?0:patientClassPercentage;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public List<Integer> getTariffMasterIdList() {
		return tariffMasterIdList;
	}

	public void setTariffMasterIdList(List<Integer> tariffMasterIdList) {
		this.tariffMasterIdList = tariffMasterIdList;
	}
	
	

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public List<Integer> getPaymentEntitlementIdList() {
		return paymentEntitlementIdList;
	}

	public void setPaymentEntitlementIdList(List<Integer> paymentEntitlementIdList) {
		this.paymentEntitlementIdList = paymentEntitlementIdList;
	}

	public List<Integer> getVisitTypeIdList() {
		return visitTypeIdList;
	}

	public void setVisitTypeIdList(List<Integer> visitTypeIdList) {
		this.visitTypeIdList = visitTypeIdList;
	}

	public List<Integer> getPatientTypeIdList() {
		return patientTypeIdList;
	}

	public void setPatientTypeIdList(List<Integer> patientTypeIdList) {
		this.patientTypeIdList = patientTypeIdList;
	}

	public List<Integer> getBedCategoryIdList() {
		return bedCategoryIdList;
	}

	public void setBedCategoryIdList(List<Integer> bedCategoryIdList) {
		this.bedCategoryIdList = bedCategoryIdList;
	}

	public List<Integer> getListServiceId() {
		return listServiceId;
	}

	public void setListServiceId(List<Integer> listServiceId) {
		this.listServiceId = listServiceId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getAlreadyExist() {
		return alreadyExist;
	}

	public void setAlreadyExist(Integer alreadyExist) {
		this.alreadyExist = alreadyExist;
	}

	public Integer getUnitServiceTriffId() {
		return unitServiceTriffId;
	}

	public void setUnitServiceTriffId(Integer unitServiceTriffId) {
		this.unitServiceTriffId = unitServiceTriffId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public List<UnitServiceTariffMasterDto> getUnitServiceTariffMasterDtosList() {
		return unitServiceTariffMasterDtosList;
	}

	public void setUnitServiceTariffMasterDtosList(List<UnitServiceTariffMasterDto> unitServiceTariffMasterDtosList) {
		this.unitServiceTariffMasterDtosList = unitServiceTariffMasterDtosList;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public List<MultiSelectDto> getTariffMasterList() {
		return tariffMasterList;
	}

	public void setTariffMasterList(List<MultiSelectDto> tariffMasterList) {
		this.tariffMasterList = tariffMasterList;
	}

	public List<MultiSelectDto> getPaymentEntitlementList() {
		return paymentEntitlementList;
	}

	public void setPaymentEntitlementList(List<MultiSelectDto> paymentEntitlementList) {
		this.paymentEntitlementList = paymentEntitlementList;
	}

	public List<MultiSelectDto> getVisitTypeList() {
		return visitTypeList;
	}

	public void setVisitTypeList(List<MultiSelectDto> visitTypeList) {
		this.visitTypeList = visitTypeList;
	}

	public List<MultiSelectDto> getPatientTypeList() {
		return patientTypeList;
	}

	public void setPatientTypeList(List<MultiSelectDto> patientTypeList) {
		this.patientTypeList = patientTypeList;
	}

	public List<MultiSelectDto> getBedCategoryList() {
		return bedCategoryList;
	}

	public void setBedCategoryList(List<MultiSelectDto> bedCategoryList) {
		this.bedCategoryList = bedCategoryList;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public String getTariffDesc() {
		return tariffDesc;
	}

	public void setTariffDesc(String tariffDesc) {
		this.tariffDesc = tariffDesc;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public String getPaymentEntitlementDesc() {
		return paymentEntitlementDesc;
	}

	public void setPaymentEntitlementDesc(String paymentEntitlementDesc) {
		this.paymentEntitlementDesc = paymentEntitlementDesc;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public String getPatientTypeDesc() {
		return patientTypeDesc;
	}

	public void setPatientTypeDesc(String patientTypeDesc) {
		this.patientTypeDesc = patientTypeDesc;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getVisitTypeDesc() {
		return visitTypeDesc;
	}

	public void setVisitTypeDesc(String visitTypeDesc) {
		this.visitTypeDesc = visitTypeDesc;
	}

	public Integer getBillingBedCatId() {
		return billingBedCatId;
	}

	public void setBillingBedCatId(Integer billingBedCatId) {
		this.billingBedCatId = billingBedCatId;
	}

	public String getBillingBedCatDesc() {
		return billingBedCatDesc;
	}

	public void setBillingBedCatDesc(String billingBedCatDesc) {
		this.billingBedCatDesc = billingBedCatDesc;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getSpecialityDesc() {
		return specialityDesc;
	}

	public void setSpecialityDesc(String specialityDesc) {
		this.specialityDesc = specialityDesc;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public String getSubSpecialityDesc() {
		return subSpecialityDesc;
	}

	public void setSubSpecialityDesc(String subSpecialityDesc) {
		this.subSpecialityDesc = subSpecialityDesc;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getStatPer() {
		return statPer;
	}

	public void setStatPer(String statPer) {
		this.statPer = statPer;
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

	public List<Integer> getSpecialityList() {
		return specialityList;
	}

	public void setSpecialityList(List<Integer> specialityList) {
		this.specialityList = specialityList;
	}

	public List<Integer> getSubSpecialityList() {
		return subSpecialityList;
	}

	public void setSubSpecialityList(List<Integer> subSpecialityList) {
		this.subSpecialityList = subSpecialityList;
	}

	public List<Integer> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<Integer> serviceList) {
		this.serviceList = serviceList;
	}

	public Double getServiceCost() {
		return serviceCost;
	}

	public void setServiceCost(Double serviceCost) {
		this.serviceCost = serviceCost;
	}

	public String getSuSpecialityDesc() {
		return suSpecialityDesc;
	}

	public void setSuSpecialityDesc(String suSpecialityDesc) {
		this.suSpecialityDesc = suSpecialityDesc;
	}

	public Double getMinRateEditable() {
		return minRateEditable;
	}

	public void setMinRateEditable(Double minRateEditable) {
		this.minRateEditable = minRateEditable;
	}

	public Double getMaxRateEditable() {
		return maxRateEditable;
	}

	public void setMaxRateEditable(Double maxRateEditable) {
		this.maxRateEditable = maxRateEditable;
	}

	public Character getIsRateEditable() {
		return isRateEditable;
	}

	public void setIsRateEditable(Character isRateEditable) {
		this.isRateEditable = isRateEditable;
	}

	public Integer getContractGradeMasterId() {
		return contractGradeMasterId;
	}

	public void setContractGradeMasterId(Integer contractGradeMasterId) {
		this.contractGradeMasterId = contractGradeMasterId;
	}

	public Integer getAmbulanceChargesServiceId() {
		return ambulanceChargesServiceId;
	}

	public void setAmbulanceChargesServiceId(Integer ambulanceChargesServiceId) {
		this.ambulanceChargesServiceId = ambulanceChargesServiceId;
	}

	
}

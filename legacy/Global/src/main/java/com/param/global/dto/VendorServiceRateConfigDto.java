package com.param.global.dto;

import java.util.List;

public class VendorServiceRateConfigDto {

	private Integer vendorServiceRateConfigId;
	
	private Integer vendorId;
	
	private String vendorName;
	
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
	
	private Integer packageId;
	
	private String packageDesc;
	
	private String serviceDesc;
	
	private Double rate;
	
	private Integer taxId;
	
	private Double taxPercentage;
	
	private String effectiveDate;
	
	private Integer unitServiceTriffId;
	
	private Integer billingBedCategoryId;
	
	private String createdDate;
	
	private Integer createdBy;
	
	private String updatedDate;
	
	private Integer updatedBy;
	
	private Character status;
	
	private List<MultiSelectDto> tariffMasterList;
	
	private List<MultiSelectDto> paymentEntitlementList;
	
	private List<MultiSelectDto> visitTypeList;
	
	private List<MultiSelectDto> patientTypeList;
	
	private List<MultiSelectDto> selectedPatientTypeList;
	
	private List<MultiSelectDto> bedCategoryList;
	
	private List<MultiSelectDto> vendorsList;
	
	private List<MultiSelectDto> serviceList;
	
	private List<MultiSelectDto> packageList;
	
	private List<VendorServiceRateConfigDto> vendorServiceRateConfigDtosList;
	
	private List<Integer> listServiceId;
	
	private List<Integer> tariffMasterIdList;
	
	private List<Integer> paymentEntitlementIdList;
	
	private List<Integer> visitTypeIdList;
	
	private List<Integer> patientTypeIdList;
	
	private List<Integer> bedCategoryIdList;
	
	private List<Integer> venderIdList;
	
	private List<Integer> packageIdList;
	
	private String serviceCode;

	private List<Integer> duplicateIdsList;
	
	private Integer alreadyExist;
	
	private Integer packageServiceDetailsId;
	
	
	
	public Integer getPackageServiceDetailsId() {
		return packageServiceDetailsId;
	}

	public void setPackageServiceDetailsId(Integer packageServiceDetailsId) {
		this.packageServiceDetailsId = packageServiceDetailsId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public String getPackageDesc() {
		return packageDesc;
	}

	public void setPackageDesc(String packageDesc) {
		this.packageDesc = packageDesc;
	}

	public List<MultiSelectDto> getPackageList() {
		return packageList;
	}

	public void setPackageList(List<MultiSelectDto> packageList) {
		this.packageList = packageList;
	}

	public List<Integer> getPackageIdList() {
		return packageIdList;
	}

	public void setPackageIdList(List<Integer> packageIdList) {
		this.packageIdList = packageIdList;
	}

	public List<Integer> getVenderIdList() {
		return venderIdList;
	}

	public void setVenderIdList(List<Integer> venderIdList) {
		this.venderIdList = venderIdList;
	}

	public List<MultiSelectDto> getVendorsList() {
		return vendorsList;
	}

	public void setVendorsList(List<MultiSelectDto> vendorsList) {
		this.vendorsList = vendorsList;
	}

	public Integer getAlreadyExist() {
		return alreadyExist;
	}

	public void setAlreadyExist(Integer alreadyExist) {
		this.alreadyExist = alreadyExist;
	}

	public List<Integer> getDuplicateIdsList() {
		return duplicateIdsList;
	}

	public void setDuplicateIdsList(List<Integer> duplicateIdsList) {
		this.duplicateIdsList = duplicateIdsList;
	}

	public Integer getVendorServiceRateConfigId() {
		return vendorServiceRateConfigId;
	}

	public void setVendorServiceRateConfigId(Integer vendorServiceRateConfigId) {
		this.vendorServiceRateConfigId = vendorServiceRateConfigId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
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

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
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

	public List<MultiSelectDto> getSelectedPatientTypeList() {
		return selectedPatientTypeList;
	}

	public void setSelectedPatientTypeList(List<MultiSelectDto> selectedPatientTypeList) {
		this.selectedPatientTypeList = selectedPatientTypeList;
	}

	public List<MultiSelectDto> getBedCategoryList() {
		return bedCategoryList;
	}

	public void setBedCategoryList(List<MultiSelectDto> bedCategoryList) {
		this.bedCategoryList = bedCategoryList;
	}

	public List<MultiSelectDto> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<MultiSelectDto> serviceList) {
		this.serviceList = serviceList;
	}

	public List<VendorServiceRateConfigDto> getVendorServiceRateConfigDtosList() {
		return vendorServiceRateConfigDtosList;
	}

	public void setVendorServiceRateConfigDtosList(List<VendorServiceRateConfigDto> vendorServiceRateConfigDtosList) {
		this.vendorServiceRateConfigDtosList = vendorServiceRateConfigDtosList;
	}

	public List<Integer> getListServiceId() {
		return listServiceId;
	}

	public void setListServiceId(List<Integer> listServiceId) {
		this.listServiceId = listServiceId;
	}

	public List<Integer> getTariffMasterIdList() {
		return tariffMasterIdList;
	}

	public void setTariffMasterIdList(List<Integer> tariffMasterIdList) {
		this.tariffMasterIdList = tariffMasterIdList;
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

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
}

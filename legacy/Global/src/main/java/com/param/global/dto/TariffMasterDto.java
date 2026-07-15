package com.param.global.dto;

import java.util.List;

public class TariffMasterDto {
	
	private Integer tariffId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private String tariffCode;
	
	private String tariffDesc;
	
	private String validFrom;
	
	private String validTo;
	
	private Integer applicablePaymentEntitlementId;
	
	private String createdDate;
	
	private Integer createdBy;
	
	private String updatedDate;
	
	private Integer updatedBy;
	
	private Character status;
	
	private String paymentEntitlementDesc;
	
	private Integer tariffPaymentEntitlementMapperId;
	
	private List<Integer> applicablePaymentEntitlementIdList;
	
	private List<Integer> applicablePaymentEntitlementIdListOld;
	
	private List<TariffMasterDto> applicablePaymentEntitlementIdList2;
	
	private List<Integer> tariffIdList;
	
	private Integer id;
	private Integer tariffPatientTypeMapperId;
	private String patientCategory;
	private Integer patientTypeId;
	
	private Integer tariffVisitTypeMapperId;
	private String visitTypeName;
	private Integer visitTypeId;
	
	private Integer tariffBillingBedCategoryMapperId;
	private String billingBedDesc;
	private Integer billingBedCategoryId;
	
	private List<TariffMasterDto> paymentEntitlementIdList;
	private List<TariffMasterDto> visitTypeList;
	private List<TariffMasterDto> patientTypeList;
	private List<TariffMasterDto> billingBedList;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;
	
	private String lable;
	
	public List<Integer> getTariffIdList() {
		return tariffIdList;
	}

	public void setTariffIdList(List<Integer> tariffIdList) {
		this.tariffIdList = tariffIdList;
	}

	public Integer getTariffPaymentEntitlementMapperId() {
		return tariffPaymentEntitlementMapperId;
	}

	public void setTariffPaymentEntitlementMapperId(Integer tariffPaymentEntitlementMapperId) {
		this.tariffPaymentEntitlementMapperId = tariffPaymentEntitlementMapperId;
	}

	public List<Integer> getApplicablePaymentEntitlementIdListOld() {
		return applicablePaymentEntitlementIdListOld;
	}

	public void setApplicablePaymentEntitlementIdListOld(List<Integer> applicablePaymentEntitlementIdListOld) {
		this.applicablePaymentEntitlementIdListOld = applicablePaymentEntitlementIdListOld;
	}
	

	public String getPaymentEntitlementDesc() {
		return paymentEntitlementDesc;
	}

	public void setPaymentEntitlementDesc(String paymentEntitlementDesc) {
		this.paymentEntitlementDesc = paymentEntitlementDesc;
	}



	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
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

	public String getTariffCode() {
		return tariffCode;
	}

	public void setTariffCode(String tariffCode) {
		this.tariffCode = tariffCode;
	}

	public String getTariffDesc() {
		return tariffDesc;
	}

	public void setTariffDesc(String tariffDesc) {
		this.tariffDesc = tariffDesc;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}

	public Integer getApplicablePaymentEntitlementId() {
		return applicablePaymentEntitlementId;
	}

	public void setApplicablePaymentEntitlementId(Integer applicablePaymentEntitlementId) {
		this.applicablePaymentEntitlementId = applicablePaymentEntitlementId;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<TariffMasterDto> getVisitTypeList() {
		return visitTypeList;
	}

	public void setVisitTypeList(List<TariffMasterDto> visitTypeList) {
		this.visitTypeList = visitTypeList;
	}

	public List<TariffMasterDto> getPatientTypeList() {
		return patientTypeList;
	}

	public void setPatientTypeList(List<TariffMasterDto> patientTypeList) {
		this.patientTypeList = patientTypeList;
	}

	public List<TariffMasterDto> getBillingBedList() {
		return billingBedList;
	}

	public void setBillingBedList(List<TariffMasterDto> billingBedList) {
		this.billingBedList = billingBedList;
	}

	public List<TariffMasterDto> getPaymentEntitlementIdList() {
		return paymentEntitlementIdList;
	}

	public void setPaymentEntitlementIdList(List<TariffMasterDto> paymentEntitlementIdList) {
		this.paymentEntitlementIdList = paymentEntitlementIdList;
	}

	public List<Integer> getApplicablePaymentEntitlementIdList() {
		return applicablePaymentEntitlementIdList;
	}

	public void setApplicablePaymentEntitlementIdList(List<Integer> applicablePaymentEntitlementIdList) {
		this.applicablePaymentEntitlementIdList = applicablePaymentEntitlementIdList;
	}

	public List<TariffMasterDto> getApplicablePaymentEntitlementIdList2() {
		return applicablePaymentEntitlementIdList2;
	}

	public void setApplicablePaymentEntitlementIdList2(List<TariffMasterDto> applicablePaymentEntitlementIdList2) {
		this.applicablePaymentEntitlementIdList2 = applicablePaymentEntitlementIdList2;
	}

	public Integer getTariffVisitTypeMapperId() {
		return tariffVisitTypeMapperId;
	}

	public void setTariffVisitTypeMapperId(Integer tariffVisitTypeMapperId) {
		this.tariffVisitTypeMapperId = tariffVisitTypeMapperId;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getTariffPatientTypeMapperId() {
		return tariffPatientTypeMapperId;
	}

	public void setTariffPatientTypeMapperId(Integer tariffPatientTypeMapperId) {
		this.tariffPatientTypeMapperId = tariffPatientTypeMapperId;
	}

	public String getPatientCategory() {
		return patientCategory;
	}

	public void setPatientCategory(String patientCategory) {
		this.patientCategory = patientCategory;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Integer getTariffBillingBedCategoryMapperId() {
		return tariffBillingBedCategoryMapperId;
	}

	public void setTariffBillingBedCategoryMapperId(Integer tariffBillingBedCategoryMapperId) {
		this.tariffBillingBedCategoryMapperId = tariffBillingBedCategoryMapperId;
	}

	public String getBillingBedDesc() {
		return billingBedDesc;
	}

	public void setBillingBedDesc(String billingBedDesc) {
		this.billingBedDesc = billingBedDesc;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}
	
	
}

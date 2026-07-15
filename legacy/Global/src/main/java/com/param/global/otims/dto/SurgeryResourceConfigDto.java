package com.param.global.otims.dto;

import java.util.List;

import javax.persistence.Column;

import com.param.global.dto.UnitServiceMapperDto;

public class SurgeryResourceConfigDto {
	
	private Integer surgeryResourceConfigId;
	
	private Integer specialityId;
	
	private Integer surgeryGradeId;
	
	private Integer doctorGradeId;
	
	private Integer patientCategoryId;
	
	private Integer paymentEntitlementId;
	
	private Integer bedCategoryId;
	
	private Double surgeonPercentage;
	
	private Double asstSurgeonPercentage;
	
	private Double anesthesiologistPercentage;
	
	private Double otPercentage;
	
	private Character status;
	
	private Integer orgId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String specialityName;
	
	private String surgeryGrade;
	
	private String docGradeName;
	
	private String patientCategory;
	
	private String paymentEntitlementDesc;
	
	private String bedCategoryDesc;
	
	private Integer surgeonDependentOn;
	
	private Integer asstSurgeonDependentOn;
	
	private Integer anesthesiologistDependentOn;
	
	private Integer otDependentOn;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;
	
	private Integer tariffId;
	
	private Integer visitTypeId;
	
	private String procedureName;
	
	private Integer componenetServicesId;
	
	private String visitTypeName;
	
	private String tariffDesc;
	
	private Integer serviceId;
	
	private String billingBedDesc;
	
	private List<ResourceBillOfServicesMapperDto> listSelectedResourceBillOfServicesMapperDto;

	public Integer getSurgeryResourceConfigId() {
		return surgeryResourceConfigId;
	}

	public void setSurgeryResourceConfigId(Integer surgeryResourceConfigId) {
		this.surgeryResourceConfigId = surgeryResourceConfigId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSurgeryGradeId() {
		return surgeryGradeId;
	}

	public void setSurgeryGradeId(Integer surgeryGradeId) {
		this.surgeryGradeId = surgeryGradeId;
	}

	public Integer getDoctorGradeId() {
		return doctorGradeId;
	}

	public void setDoctorGradeId(Integer doctorGradeId) {
		this.doctorGradeId = doctorGradeId;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public Double getSurgeonPercentage() {
		return surgeonPercentage;
	}

	public void setSurgeonPercentage(Double surgeonPercentage) {
		this.surgeonPercentage = surgeonPercentage;
	}

	public Double getAsstSurgeonPercentage() {
		return asstSurgeonPercentage;
	}

	public void setAsstSurgeonPercentage(Double asstSurgeonPercentage) {
		this.asstSurgeonPercentage = asstSurgeonPercentage;
	}

	public Double getAnesthesiologistPercentage() {
		return anesthesiologistPercentage;
	}

	public void setAnesthesiologistPercentage(Double anesthesiologistPercentage) {
		this.anesthesiologistPercentage = anesthesiologistPercentage;
	}

	public Double getOtPercentage() {
		return otPercentage;
	}

	public void setOtPercentage(Double otPercentage) {
		this.otPercentage = otPercentage;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getSurgeryGrade() {
		return surgeryGrade;
	}

	public void setSurgeryGrade(String surgeryGrade) {
		this.surgeryGrade = surgeryGrade;
	}

	public String getDocGradeName() {
		return docGradeName;
	}

	public void setDocGradeName(String docGradeName) {
		this.docGradeName = docGradeName;
	}

	public String getPatientCategory() {
		return patientCategory;
	}

	public void setPatientCategory(String patientCategory) {
		this.patientCategory = patientCategory;
	}

	public String getPaymentEntitlementDesc() {
		return paymentEntitlementDesc;
	}

	public void setPaymentEntitlementDesc(String paymentEntitlementDesc) {
		this.paymentEntitlementDesc = paymentEntitlementDesc;
	}

	public String getBedCategoryDesc() {
		return bedCategoryDesc;
	}

	public void setBedCategoryDesc(String bedCategoryDesc) {
		this.bedCategoryDesc = bedCategoryDesc;
	}

	public Integer getSurgeonDependentOn() {
		return surgeonDependentOn;
	}

	public void setSurgeonDependentOn(Integer surgeonDependentOn) {
		this.surgeonDependentOn = surgeonDependentOn;
	}

	public Integer getAsstSurgeonDependentOn() {
		return asstSurgeonDependentOn;
	}

	public void setAsstSurgeonDependentOn(Integer asstSurgeonDependentOn) {
		this.asstSurgeonDependentOn = asstSurgeonDependentOn;
	}

	public Integer getAnesthesiologistDependentOn() {
		return anesthesiologistDependentOn;
	}

	public void setAnesthesiologistDependentOn(Integer anesthesiologistDependentOn) {
		this.anesthesiologistDependentOn = anesthesiologistDependentOn;
	}

	public Integer getOtDependentOn() {
		return otDependentOn;
	}

	public void setOtDependentOn(Integer otDependentOn) {
		this.otDependentOn = otDependentOn;
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

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public Integer getComponenetServicesId() {
		return componenetServicesId;
	}

	public void setComponenetServicesId(Integer componenetServicesId) {
		this.componenetServicesId = componenetServicesId;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public String getTariffDesc() {
		return tariffDesc;
	}

	public void setTariffDesc(String tariffDesc) {
		this.tariffDesc = tariffDesc;
	}

	public List<ResourceBillOfServicesMapperDto> getListSelectedResourceBillOfServicesMapperDto() {
		return listSelectedResourceBillOfServicesMapperDto;
	}

	public void setListSelectedResourceBillOfServicesMapperDto(
			List<ResourceBillOfServicesMapperDto> listSelectedResourceBillOfServicesMapperDto) {
		this.listSelectedResourceBillOfServicesMapperDto = listSelectedResourceBillOfServicesMapperDto;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getBillingBedDesc() {
		return billingBedDesc;
	}

	public void setBillingBedDesc(String billingBedDesc) {
		this.billingBedDesc = billingBedDesc;
	}
	
	
	
}

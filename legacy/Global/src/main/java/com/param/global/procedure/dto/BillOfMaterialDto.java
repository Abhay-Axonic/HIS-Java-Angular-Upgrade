package com.param.global.procedure.dto;

import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.param.global.dto.SpecialityMasterDto;

public class BillOfMaterialDto {

	private Integer otCompletionStaffId;

	private Integer specialityId;

	private Integer procedureOrderId;
	
	private Integer billOfMaterialId;
	
	private Integer procedureId;
	
	private Integer serviceId;
	
	private Integer billingBedCatId;
	
	private Double amount;
	
	private char status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String serviceName;
	
	private String procedure;
	
	private Integer specilityId;
	
	private Integer doctorId;
	
	private String doctorName;
	
	private String specilityName;
	
	private Integer  totalAmt;
	
	private Integer tariffId;
	
	private Integer procReqId;
	
	private List<BillOfMaterialDto> billOfMaterialList;
	
	private List<BillOfMaterialDto> totalAmount;
	
	private List<SpecialityMasterDto> specilityList;
	
	private List<SpecialityMasterDto> doctorList;
	
	private String firstName;
	
	private String lastName;

	private String specialityName;
	
	private Boolean isEditable;
	
	private char isRefDoctorShare;
	
	private Double originalAmount;
	private boolean isGrade;
	private Integer surgeryGradeId;
	
	private Integer visitTypeId;
	private Integer paymentEntitlementId;
	private Integer patientCategoryId;
	private String serviceCode;
	private String surgeryGrade;
	private String surgeryGradeCode;
	private String tariffDesc;
	private String patientCategory;
	private String visitTypeName;
	private String paymentEntitlementDesc;
	
	public Integer getBillOfMaterialId() {
		return billOfMaterialId;
	}

	public void setBillOfMaterialId(Integer billOfMaterialId) {
		this.billOfMaterialId = billOfMaterialId;
	}

	public Integer getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(Integer procedureId) {
		this.procedureId = procedureId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getBillingBedCatId() {
		return billingBedCatId;
	}

	public void setBillingBedCatId(Integer billingBedCatId) {
		this.billingBedCatId = billingBedCatId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getProcedure() {
		return procedure;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	public Integer getSpecilityId() {
		return specilityId;
	}

	public void setSpecilityId(Integer specilityId) {
		this.specilityId = specilityId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getSpecilityName() {
		return specilityName;
	}

	public void setSpecilityName(String specilityName) {
		this.specilityName = specilityName;
	}

	public List<BillOfMaterialDto> getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(List<BillOfMaterialDto> totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<BillOfMaterialDto> getBillOfMaterialList() {
		return billOfMaterialList;
	}

	public void setBillOfMaterialList(List<BillOfMaterialDto> billOfMaterialList) {
		this.billOfMaterialList = billOfMaterialList;
	}

	public Integer getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Integer totalAmt) {
		this.totalAmt = totalAmt;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getOtCompletionStaffId() {
		return otCompletionStaffId;
	}

	public void setOtCompletionStaffId(Integer otCompletionStaffId) {
		this.otCompletionStaffId = otCompletionStaffId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public Integer getProcReqId() {
		return procReqId;
	}

	public void setProcReqId(Integer procReqId) {
		this.procReqId = procReqId;
	}

	public List<SpecialityMasterDto> getSpecilityList() {
		return specilityList;
	}

	public void setSpecilityList(List<SpecialityMasterDto> specilityList) {
		this.specilityList = specilityList;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public List<SpecialityMasterDto> getDoctorList() {
		return doctorList;
	}

	public void setDoctorList(List<SpecialityMasterDto> doctorList) {
		this.doctorList = doctorList;
	}

	public char getIsRefDoctorShare() {
		return isRefDoctorShare;
	}

	public void setIsRefDoctorShare(char isRefDoctorShare) {
		this.isRefDoctorShare = isRefDoctorShare;
	}

	public Double getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(Double originalAmount) {
		this.originalAmount = originalAmount;
	}

	public boolean getIsGrade() {
		return isGrade;
	}

	public void setIsGrade(boolean isGrade) {
		this.isGrade = isGrade;
	}

	public Integer getSurgeryGradeId() {
		return surgeryGradeId;
	}

	public void setSurgeryGradeId(Integer surgeryGradeId) {
		this.surgeryGradeId = surgeryGradeId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getSurgeryGrade() {
		return surgeryGrade;
	}

	public void setSurgeryGrade(String surgeryGrade) {
		this.surgeryGrade = surgeryGrade;
	}

	public String getSurgeryGradeCode() {
		return surgeryGradeCode;
	}

	public void setSurgeryGradeCode(String surgeryGradeCode) {
		this.surgeryGradeCode = surgeryGradeCode;
	}

	public String getTariffDesc() {
		return tariffDesc;
	}

	public void setTariffDesc(String tariffDesc) {
		this.tariffDesc = tariffDesc;
	}

	public String getPatientCategory() {
		return patientCategory;
	}

	public void setPatientCategory(String patientCategory) {
		this.patientCategory = patientCategory;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public String getPaymentEntitlementDesc() {
		return paymentEntitlementDesc;
	}

	public void setPaymentEntitlementDesc(String paymentEntitlementDesc) {
		this.paymentEntitlementDesc = paymentEntitlementDesc;
	}

	

	
	
}

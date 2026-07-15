package com.param.global.dto;

import java.util.Date;

import javax.persistence.Column;

public class CustomEMRConfigurationDetailsDto {
private Integer customEmrConfigDetailsId;
	
	private Integer cusEmrCategoryId;
	
	private Integer cusEmrSubCategoryId;
	
	private Integer patientId;
	
	private Integer visitTypeId;
	
	private Integer encounterId;
	
	private Integer admissionId;
	private Integer doctorId;
	
	private String remark;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	private Integer customEmrConfigId;
	private String cusEmrSubCategoryDesc;
	private String cusEmrCategoryDesc;

	public Integer getCustomEmrConfigDetailsId() {
		return customEmrConfigDetailsId;
	}

	public void setCustomEmrConfigDetailsId(Integer customEmrConfigDetailsId) {
		this.customEmrConfigDetailsId = customEmrConfigDetailsId;
	}

	public Integer getCusEmrCategoryId() {
		return cusEmrCategoryId;
	}

	public void setCusEmrCategoryId(Integer cusEmrCategoryId) {
		this.cusEmrCategoryId = cusEmrCategoryId;
	}

	public Integer getCusEmrSubCategoryId() {
		return cusEmrSubCategoryId;
	}

	public void setCusEmrSubCategoryId(Integer cusEmrSubCategoryId) {
		this.cusEmrSubCategoryId = cusEmrSubCategoryId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getCustomEmrConfigId() {
		return customEmrConfigId;
	}

	public void setCustomEmrConfigId(Integer customEmrConfigId) {
		this.customEmrConfigId = customEmrConfigId;
	}

	public String getCusEmrSubCategoryDesc() {
		return cusEmrSubCategoryDesc;
	}

	public void setCusEmrSubCategoryDesc(String cusEmrSubCategoryDesc) {
		this.cusEmrSubCategoryDesc = cusEmrSubCategoryDesc;
	}

	public String getCusEmrCategoryDesc() {
		return cusEmrCategoryDesc;
	}

	public void setCusEmrCategoryDesc(String cusEmrCategoryDesc) {
		this.cusEmrCategoryDesc = cusEmrCategoryDesc;
	}
	
	
}

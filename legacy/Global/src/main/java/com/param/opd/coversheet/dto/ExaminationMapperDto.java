package com.param.opd.coversheet.dto;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;

public class ExaminationMapperDto {
	
	private Integer examinationMapperId;
	
	private Integer patientId;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private Integer doctorId;
	
	private Integer examinationMasterId;
	
	private String remark;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	private BigInteger count;
	private String examinationDesc;

	public Integer getExaminationMapperId() {
		return examinationMapperId;
	}

	public void setExaminationMapperId(Integer examinationMapperId) {
		this.examinationMapperId = examinationMapperId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getExaminationMasterId() {
		return examinationMasterId;
	}

	public void setExaminationMasterId(Integer examinationMasterId) {
		this.examinationMasterId = examinationMasterId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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

	public BigInteger getCount() {
		return count;
	}

	public void setCount(BigInteger count) {
		this.count = count;
	}

	public String getExaminationDesc() {
		return examinationDesc;
	}

	public void setExaminationDesc(String examinationDesc) {
		this.examinationDesc = examinationDesc;
	}
	
	
}

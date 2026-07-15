package com.param.billing.patientType.dto;

import java.util.List;

public class PatientTypeDto {
	
	private Integer patientTypeId;
	
	private String code;
	
	private String description;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private String createdDate;
	
	private String updatedDate;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private Character status;
	


	private List<PatientTypeServiceWiseDetailsDto> patientTypeServiceWiseDetails;
	
	private List<PatientTypeGroupWiseDetailsDto> patientTypeGroupWiseDetails;
	
	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	

	public List<PatientTypeServiceWiseDetailsDto> getPatientTypeServiceWiseDetails() {
		return patientTypeServiceWiseDetails;
	}

	public void setPatientTypeServiceWiseDetails(List<PatientTypeServiceWiseDetailsDto> patientTypeServiceWiseDetails) {
		this.patientTypeServiceWiseDetails = patientTypeServiceWiseDetails;
	}

	public List<PatientTypeGroupWiseDetailsDto> getPatientTypeGroupWiseDetails() {
		return patientTypeGroupWiseDetails;
	}

	public void setPatientTypeGroupWiseDetails(List<PatientTypeGroupWiseDetailsDto> patientTypeGroupWiseDetails) {
		this.patientTypeGroupWiseDetails = patientTypeGroupWiseDetails;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}


}

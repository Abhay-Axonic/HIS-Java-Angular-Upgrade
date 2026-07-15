package com.param.global.dto;

import java.util.List;

public class TVitalDto {

	
    private Integer tVitalId;
	
	private Integer vitalId;
	
	private String vitalTime;
	
	private Integer patientId;
	
	private Integer visitTypeId;
	
	private Integer admissionId;
	
	private Integer encounterId;
	
	private Integer organisationId;
	
	private Integer unitId;
	
	private char status;

	private String createdDate;
	
	private int createdBy;

	private int updatedBy;

	private String updatedDate;
	
	private String vitalName;
	
	private String vitalUnit;
	
	private List<TVitalDetailsDto> vitalList;

	private Integer hospitalId;

	public Integer gettVitalId() {
		return tVitalId;
	}

	public void settVitalId(Integer tVitalId) {
		this.tVitalId = tVitalId;
	}

	public Integer getVitalId() {
		return vitalId;
	}

	public void setVitalId(Integer vitalId) {
		this.vitalId = vitalId;
	}

	public String getVitalTime() {
		return vitalTime;
	}

	public void setVitalTime(String vitalTime) {
		this.vitalTime = vitalTime;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}


	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getVitalName() {
		return vitalName;
	}

	public void setVitalName(String vitalName) {
		this.vitalName = vitalName;
	}

	public String getVitalUnit() {
		return vitalUnit;
	}

	public void setVitalUnit(String vitalUnit) {
		this.vitalUnit = vitalUnit;
	}

	public List<TVitalDetailsDto> getVitalList() {
		return vitalList;
	}

	public void setVitalList(List<TVitalDetailsDto> vitalList) {
		this.vitalList = vitalList;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
}

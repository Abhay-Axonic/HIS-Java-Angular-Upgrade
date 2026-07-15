package com.param.opd.coversheet.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

public class VitalAppointmentQualifierMapperDto {
private Integer vitalAppoQualifierMapperId;
	
	private Integer vitalId;
	
	private Integer vitalQualifierId;
	
	private Integer patientId;

	private char status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private Integer encounterId;
	
	private Integer admissionId;
	private List<VitalAppointmentQualifierMapperDto> listOfSelectedQualifiers;
	public Integer getVitalAppoQualifierMapperId() {
		return vitalAppoQualifierMapperId;
	}

	public void setVitalAppoQualifierMapperId(Integer vitalAppoQualifierMapperId) {
		this.vitalAppoQualifierMapperId = vitalAppoQualifierMapperId;
	}

	public Integer getVitalId() {
		return vitalId;
	}

	public void setVitalId(Integer vitalId) {
		this.vitalId = vitalId;
	}

	public Integer getVitalQualifierId() {
		return vitalQualifierId;
	}

	public void setVitalQualifierId(Integer vitalQualifierId) {
		this.vitalQualifierId = vitalQualifierId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
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

	public List<VitalAppointmentQualifierMapperDto> getListOfSelectedQualifiers() {
		return listOfSelectedQualifiers;
	}

	public void setListOfSelectedQualifiers(List<VitalAppointmentQualifierMapperDto> listOfSelectedQualifiers) {
		this.listOfSelectedQualifiers = listOfSelectedQualifiers;
	}
	
	
}

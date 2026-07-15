package com.param.bloodBank.dto;

import java.util.Date;

public class IntraTransfusionMonitoringMapper {
	
	private Integer intraTransfusionMonitoringMapperId;
	private Integer transfusionDetailId;
	private Integer patientId;
	private String incubationStartTime;
	private Double pulse;
	private Double temperature;
	private Double bpDiastolic;
	private Double bpSystolic;
	private Integer respirationRate;
	private Integer unitId;
	private Integer organizationId;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private String updatedDate;
	private Character status;
	public Integer getIntraTransfusionMonitoringMapperId() {
		return intraTransfusionMonitoringMapperId;
	}
	public void setIntraTransfusionMonitoringMapperId(Integer intraTransfusionMonitoringMapperId) {
		this.intraTransfusionMonitoringMapperId = intraTransfusionMonitoringMapperId;
	}
	public Integer getTransfusionDetailId() {
		return transfusionDetailId;
	}
	public void setTransfusionDetailId(Integer transfusionDetailId) {
		this.transfusionDetailId = transfusionDetailId;
	}
	
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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
	public String getIncubationStartTime() {
		return incubationStartTime;
	}
	public void setIncubationStartTime(String incubationStartTime) {
		this.incubationStartTime = incubationStartTime;
	}
	public Double getPulse() {
		return pulse;
	}
	public void setPulse(Double pulse) {
		this.pulse = pulse;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getBpDiastolic() {
		return bpDiastolic;
	}
	public void setBpDiastolic(Double bpDiastolic) {
		this.bpDiastolic = bpDiastolic;
	}
	public Double getBpSystolic() {
		return bpSystolic;
	}
	public void setBpSystolic(Double bpSystolic) {
		this.bpSystolic = bpSystolic;
	}
	public Integer getRespirationRate() {
		return respirationRate;
	}
	public void setRespirationRate(Integer respirationRate) {
		this.respirationRate = respirationRate;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}

}

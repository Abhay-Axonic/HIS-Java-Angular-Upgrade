package com.param.opd.coversheet.dto;


public class PendingNursingOrderAdministrationDto {

	private Integer pendingNurOrdAdministrationId;
	
	private String pendingNurOrdAdministrationDate;
	
	private String pendingNurOrdAdministrationTime;
	
	private Integer reasonId;
	
	private Integer patientId;
	
	private Integer admissionId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String remark;
	private String reasonDesc;

	private Integer nursingOrderMapperId;
	private Integer frequencyId;

	
	private String administrationTime;
	
	public Integer getPendingNurOrdAdministrationId() {
		return pendingNurOrdAdministrationId;
	}

	public void setPendingNurOrdAdministrationId(Integer pendingNurOrdAdministrationId) {
		this.pendingNurOrdAdministrationId = pendingNurOrdAdministrationId;
	}


	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getNursingOrderMapperId() {
		return nursingOrderMapperId;
	}

	public void setNursingOrderMapperId(Integer nursingOrderMapperId) {
		this.nursingOrderMapperId = nursingOrderMapperId;
	}

	public String getPendingNurOrdAdministrationDate() {
		return pendingNurOrdAdministrationDate;
	}

	public void setPendingNurOrdAdministrationDate(String pendingNurOrdAdministrationDate) {
		this.pendingNurOrdAdministrationDate = pendingNurOrdAdministrationDate;
	}

	public String getPendingNurOrdAdministrationTime() {
		return pendingNurOrdAdministrationTime;
	}

	public void setPendingNurOrdAdministrationTime(String pendingNurOrdAdministrationTime) {
		this.pendingNurOrdAdministrationTime = pendingNurOrdAdministrationTime;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}

	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	public String getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(String administrationTime) {
		this.administrationTime = administrationTime;
	}
	
	
	
}

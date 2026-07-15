package com.param.pharmacy.qms.dto;

public class QMSTokenCallLogDto {
	
	private Integer qmsTokenCallLogId;
	
	private Integer qmsTokenMasterId;

	private Integer qmsKioskMasterId;

	private Integer qmsCounterMasterId;

	private Integer qmsDeptMasterId;

	private Integer callLogStatus;

	private Character status;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private Integer organisationId;

	private Integer unitId;

	public Integer getQmsTokenCallLogId() {
		return qmsTokenCallLogId;
	}

	public void setQmsTokenCallLogId(Integer qmsTokenCallLogId) {
		this.qmsTokenCallLogId = qmsTokenCallLogId;
	}

	public Integer getQmsKioskMasterId() {
		return qmsKioskMasterId;
	}

	public void setQmsKioskMasterId(Integer qmsKioskMasterId) {
		this.qmsKioskMasterId = qmsKioskMasterId;
	}

	public Integer getQmsCounterMasterId() {
		return qmsCounterMasterId;
	}

	public void setQmsCounterMasterId(Integer qmsCounterMasterId) {
		this.qmsCounterMasterId = qmsCounterMasterId;
	}

	public Integer getQmsDeptMasterId() {
		return qmsDeptMasterId;
	}

	public void setQmsDeptMasterId(Integer qmsDeptMasterId) {
		this.qmsDeptMasterId = qmsDeptMasterId;
	}

	public Integer getCallLogStatus() {
		return callLogStatus;
	}

	public void setCallLogStatus(Integer callLogStatus) {
		this.callLogStatus = callLogStatus;
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

	public Integer getQmsTokenMasterId() {
		return qmsTokenMasterId;
	}

	public void setQmsTokenMasterId(Integer qmsTokenMasterId) {
		this.qmsTokenMasterId = qmsTokenMasterId;
	}	
	
}

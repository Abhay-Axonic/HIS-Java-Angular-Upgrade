package com.param.qms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_qms_token_call_log",schema="pharmacy")
@SequenceGenerator(name="t_qms_token_call_log_seq",sequenceName="pharmacy.t_qms_token_call_log_seq",allocationSize=1)
public class QMSTokenCallLog {
	
	@Id
	@Column(name="qms_token_call_log_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="t_qms_token_call_log_seq")
	private Integer qmsTokenCallLogId;
	
	@Column(name="qms_kiosk_master_id")
	private Integer qmsKioskMasterId;
	
	@Column(name="qms_token_master_id")
	private Integer qmsTokenMasterId;
	
	@Column(name="qms_counter_master_id")
	private Integer qmsCounterMasterId;
	
	@Column(name="qms_dept_master_id")
	private Integer qmsDeptMasterId;
	
	@Column(name="call_log_status")
	private Integer callLogStatus;
	//1=>next to call
	//2=>served i.e called
	//3=>cancel
	
	@Column(name = "status")
	private Character status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name="organisation_id")
	private Integer organisationId;
	
	@Column(name="unit_id")
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
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

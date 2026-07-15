package com.param.opd.coversheet.model;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@Entity
@Table(name = "t_pending_nursing_order_administration", schema = "adt")
@SequenceGenerator(name = "t_pending_nursing_order_administration_seq", sequenceName = "adt.t_pending_nursing_order_administration_seq", allocationSize = 1)

public class PendingNursingOrderAdministrationMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_pending_nursing_order_administration_seq")
	
	@Column(name = "pending_nur_ord_administration_id")
	private Integer pendingNurOrdAdministrationId;
	
	@Column(name = "pending_nur_ord_administration_date")
	private Date pendingNurOrdAdministrationDate;
	
	@Column(name = "pending_nur_ord_administration_time")
	@Convert(converter = InstantConverter.class)
	private Instant pendingNurOrdAdministrationTime;
	
	@Column(name = "nursing_order_mapper_id")
	private Integer nursingOrderMapperId;
	
	@Column(name = "reason_id")
	private Integer reasonId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
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
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "frequency_id")
	private Integer frequencyId;
	
	
	@Column(name = "administrationTime")
	@Convert(converter = InstantConverter.class)
	private Instant administrationTime;
	

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

	public Date getPendingNurOrdAdministrationDate() {
		return pendingNurOrdAdministrationDate;
	}

	public void setPendingNurOrdAdministrationDate(Date pendingNurOrdAdministrationDate) {
		this.pendingNurOrdAdministrationDate = pendingNurOrdAdministrationDate;
	}

	public Instant getPendingNurOrdAdministrationTime() {
		return pendingNurOrdAdministrationTime;
	}

	public void setPendingNurOrdAdministrationTime(Instant pendingNurOrdAdministrationTime) {
		this.pendingNurOrdAdministrationTime = pendingNurOrdAdministrationTime;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}

	public Instant getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(Instant administrationTime) {
		this.administrationTime = administrationTime;
	}
	
	
	
	
	
}

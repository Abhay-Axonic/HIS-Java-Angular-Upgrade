package com.param.global.model;

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

import com.param.global.common.InstantConverter;
import com.param.global.common.LocalTimeConverter;

@Entity
@Table(name="t_vital",schema="public")
@SequenceGenerator(name="seq_t_vital",sequenceName="public.seq_t_vital",allocationSize=1)
public class TVital {

	
	@Id
	@Column(name="t_vital_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_t_vital")
	private int tVitalId;
	
	@Column(name="vital_time")
	@Convert(converter = InstantConverter.class)
	private Instant vitalTime;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="visit_type_id")
	private Integer visitTypeId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="organisation_id")
	private Integer organisationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="status")
	private char status;

	@Column(name="created_date")
	private Date createdDate;

	@Column(name="created_by")
	private int createdBy;

	@Column(name="updated_by")
	private int updatedBy;

	@Column(name="updated_date")
	private Date updatedDate;

	public int gettVitalId() {
		return tVitalId;
	}

	public void settVitalId(int tVitalId) {
		this.tVitalId = tVitalId;
	}


	public Instant getVitalTime() {
		return vitalTime;
	}

	public void setVitalTime(Instant vitalTime) {
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
	


}

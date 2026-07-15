package com.param.opd.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_new_cov_doctor_notes", schema = "patient")
@SequenceGenerator(name = "t_new_cov_doctor_notes_seq", sequenceName = "patient.t_new_cov_doctor_notes_seq", allocationSize = 1)
public class NewCovDoctorNotes {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_new_cov_doctor_notes_seq")
	
	@Column(name = "new_cov_doctor_notes_id")
	private Integer newCovDoctorNotesId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "admission_id")
	private Integer admissionId;
		
	@Column(name = "status")
	private Character status;
	
	@Column(name = "doctor_notes")
	private String doctorNotes;

	public Integer getNewCovDoctorNotesId() {
		return newCovDoctorNotesId;
	}

	public void setNewCovDoctorNotesId(Integer newCovDoctorNotesId) {
		this.newCovDoctorNotesId = newCovDoctorNotesId;
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

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getDoctorNotes() {
		return doctorNotes;
	}

	public void setDoctorNotes(String doctorNotes) {
		this.doctorNotes = doctorNotes;
	}
	
	
}

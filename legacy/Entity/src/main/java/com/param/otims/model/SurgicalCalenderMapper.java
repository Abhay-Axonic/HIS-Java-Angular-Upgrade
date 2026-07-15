package com.param.otims.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_surgical_calender_mppr", schema = "public")
@SequenceGenerator(name = "t_seq_surgical_calender_mppr", sequenceName = "public.t_seq_surgical_calender_mppr", allocationSize = 1)
public class SurgicalCalenderMapper {
	
	@Id
	@Column(name = "surgical_calender_mppr_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_surgical_calender_mppr")
	private Integer surgicalCalenderMpprId;
	
	@Column(name = "speciality_id")
	private Integer specialityId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "procedure_order_id")
	private Integer procedureOrderId;
	
	@Column(name = "surgical_calender_id")
	private Integer surgicalCalenderId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
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
	
	@Column(name = "organisation_id")
	private Integer organisationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "is_deleted")
	private Character isDeleted;

	public Integer getSurgicalCalenderMpprId() {
		return surgicalCalenderMpprId;
	}

	public void setSurgicalCalenderMpprId(Integer surgicalCalenderMpprId) {
		this.surgicalCalenderMpprId = surgicalCalenderMpprId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public Integer getSurgicalCalenderId() {
		return surgicalCalenderId;
	}

	public void setSurgicalCalenderId(Integer surgicalCalenderId) {
		this.surgicalCalenderId = surgicalCalenderId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = (status == '\u0000') ? 'A' : status;
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

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = (isDeleted == '\u0000') ? 'N' : isDeleted;
	}
	

}

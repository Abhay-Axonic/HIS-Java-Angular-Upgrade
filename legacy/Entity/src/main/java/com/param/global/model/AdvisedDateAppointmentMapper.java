package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_advised_date_appointment_mapper",schema="public")
@SequenceGenerator(name = "t_advised_date_appointment_mapper_seq", sequenceName = "public.t_advised_date_appointment_mapper_seq", allocationSize = 1)
public class AdvisedDateAppointmentMapper {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_advised_date_appointment_mapper_seq")
	@Column(name = "advised_date_appo_id")
	private Integer advisedDateAppoId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="doctor_id")
	private Integer doctorId;
	
	@Column(name="advised_date")
	private Date advisedDate;
	
	@Column(name="status")
	private char status;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;

	public Integer getAdvisedDateAppoId() {
		return advisedDateAppoId;
	}

	public void setAdvisedDateAppoId(Integer advisedDateAppoId) {
		this.advisedDateAppoId = advisedDateAppoId;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Date getAdvisedDate() {
		return advisedDate;
	}

	public void setAdvisedDate(Date advisedDate) {
		this.advisedDate = advisedDate;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
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
	
	
}

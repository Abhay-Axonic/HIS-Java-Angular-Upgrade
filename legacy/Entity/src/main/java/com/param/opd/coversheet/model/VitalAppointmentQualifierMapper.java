package com.param.opd.coversheet.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "t_vital_appo_qualifier_mapper", schema = "emr")
@SequenceGenerator(name = "t_vital_appo_qualifier_mapper_seq", sequenceName = "emr.t_vital_appo_qualifier_mapper_seq", allocationSize = 1)

public class VitalAppointmentQualifierMapper {
	@Id
	@Column(name = "vital_appo_qualifier_mapper_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_vital_appo_qualifier_mapper_seq")
	private Integer vitalAppoQualifierMapperId;
	
	@Column(name = "vital_id")
	private Integer vitalId;
	
	@Column(name = "vital_qualifier_id")
	private Integer vitalQualifireId;
	
	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "status")
	private char status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "createdDate")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="appointment_vital_mapper_id")
	private Integer appointmentVitalMapperId;

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

	public Integer getVitalQualifireId() {
		return vitalQualifireId;
	}

	public void setVitalQualifireId(Integer vitalQualifireId) {
		this.vitalQualifireId = vitalQualifireId;
	}

	public Integer getAppointmentVitalMapperId() {
		return appointmentVitalMapperId;
	}

	public void setAppointmentVitalMapperId(Integer appointmentVitalMapperId) {
		this.appointmentVitalMapperId = appointmentVitalMapperId;
	}
	
}

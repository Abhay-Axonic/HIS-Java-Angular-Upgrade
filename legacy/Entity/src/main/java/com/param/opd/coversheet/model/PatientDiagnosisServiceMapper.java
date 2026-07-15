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
@Table(name = "t_patient_diagnosis_service_mapper", schema = "public")
@SequenceGenerator(name = "t_patient_diagnosis_service_mapper_seq", sequenceName = "public.t_patient_diagnosis_service_mapper_seq", allocationSize = 1)
public class PatientDiagnosisServiceMapper {

	
	@Id
	@Column(name = "patient_diagnosis_service_mapper_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_patient_diagnosis_service_mapper_seq")
	private Integer patientDiagnosisServiceMapperId;

	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "diagnosis_type")
	private Integer diagnosisType;

	@Column(name = "diagnosis_id")
	private Integer diagnosisId;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "type")
	private Integer type;

	@Column(name = "remark")
	private String remark;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "status")
	private char status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	public Integer getPatientDiagnosisServiceMapperId() {
		return patientDiagnosisServiceMapperId;
	}

	public void setPatientDiagnosisServiceMapperId(Integer patientDiagnosisServiceMapperId) {
		this.patientDiagnosisServiceMapperId = patientDiagnosisServiceMapperId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getDiagnosisType() {
		return diagnosisType;
	}

	public void setDiagnosisType(Integer diagnosisType) {
		this.diagnosisType = diagnosisType;
	}

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	
	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
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
	
	

}

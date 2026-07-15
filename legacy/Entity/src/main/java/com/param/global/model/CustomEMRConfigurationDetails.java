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
@Table(name = "t_custom_emr_configuration_details", schema = "opd")
@SequenceGenerator(name = "t_custom_emr_configuration_details_seq", sequenceName = "opd.t_custom_emr_configuration_details_seq", allocationSize = 1)
public class CustomEMRConfigurationDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_custom_emr_configuration_details_seq")
	
	@Column(name = "custom_emr_config_details_id")
	private Integer customEmrConfigDetailsId;
	
	@Column(name = "cus_emr_category_id")
	private Integer cusEmrCategoryId;
	
	@Column(name = "cus_emr_sub_category_id")
	private Integer cusEmrSubCategoryId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "remark")
	private String remark;
	
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

	public Integer getCustomEmrConfigDetailsId() {
		return customEmrConfigDetailsId;
	}

	public void setCustomEmrConfigDetailsId(Integer customEmrConfigDetailsId) {
		this.customEmrConfigDetailsId = customEmrConfigDetailsId;
	}

	public Integer getCusEmrCategoryId() {
		return cusEmrCategoryId;
	}

	public void setCusEmrCategoryId(Integer cusEmrCategoryId) {
		this.cusEmrCategoryId = cusEmrCategoryId;
	}

	public Integer getCusEmrSubCategoryId() {
		return cusEmrSubCategoryId;
	}

	public void setCusEmrSubCategoryId(Integer cusEmrSubCategoryId) {
		this.cusEmrSubCategoryId = cusEmrSubCategoryId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	
	
}

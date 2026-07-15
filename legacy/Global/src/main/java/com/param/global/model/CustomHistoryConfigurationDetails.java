package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({

@NamedNativeQuery(name = "GET_ACTIVE_CUSTOM_HISTORY_CONFIG_LIST",
query = " select mchc.id as \"id\",mchc.speciality_id  as \"specialityId\" ," 
		  + " mchc.category  as \"category\" , mchc.created_by  as \"createdBy\" , " 
		  + " to_char(mchc.created_date,'yyyy-MM-dd HH:mm:ss')  as \"createdDate\", " 
		  + " mchc.status as \"status\" ,msm.speciality_name as \"specialityName\" " 
		  + " from emr.m_custom_history_configuration mchc " 
		  + " inner join doctor.m_speciality_master msm on msm.speciality_id = mchc.speciality_id " 
		  + " where mchc.speciality_id  in (:specialityIdList) and mchc.status='A' "),


@NamedNativeQuery(name = "GET_PATIENT_CUSTOM_HISTORY_DETAILS",
query = " SELECT dtls.custom_history_config_details_id AS \"customHistoryConfigDetailsId\", dtls.speciality_id  AS\"specialityId\", "
		+ " dtls.category AS \"category\", dtls.remark AS \"remark\", dtls.patient_id AS \"patientId\", "
		+ " dtls.visit_type_id AS \"visitTypeId\", dtls.encounter_id AS \"encounterId\","
		+ " dtls.admission_id AS \"admissionId\", dtls.custom_history_configuration_mstr_id AS \"customHistoryConfigurationMstrId\", "
		+ " to_char(dtls.created_date,'DD-MM-YYYY HH:mm:ss') AS \"createdDate\", dtls.created_by AS \"createdBy\", " 
		+ " dtls.status AS \"status\", dtls.updated_by AS \"updatedBy\", to_char(dtls.updated_date,'DD-MM-YYYY HH:mm:ss') AS \"updatedDate\", "
		+ " dtls.unit_id AS \"unitId\", dtls.organization_id AS \"organizationId\",msm.speciality_name AS \"specialityName\" "  
		+ " FROM public.t_custom_history_configuration_details dtls " 
		+ " inner join doctor.m_speciality_master msm on msm.speciality_id  = dtls.speciality_id " 
		+ " where dtls.organization_id = :organizationId " 
		+ " and dtls.unit_id = :unitId " 
		+ " and dtls.status='A' "),

})
@Entity
@Table(name = "t_custom_history_configuration_details", schema = "public")
@SequenceGenerator(name = "t_custom_history_configuration_details_seq", sequenceName = "public.t_custom_history_configuration_details_seq", allocationSize = 1)
public class CustomHistoryConfigurationDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_custom_history_configuration_details_seq")
	@Column(name = "custom_history_config_details_id")
	private Integer customHistoryConfigDetailsId;

	@Column(name = "specialityId")
	private Integer specialityId;

	@Column(name = "category")
	private String category;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "custom_history_configuration_mstr_id")
	private Integer customHistoryConfigurationMstrId;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	public Integer getCustomHistoryConfigDetailsId() {
		return customHistoryConfigDetailsId;
	}

	public void setCustomHistoryConfigDetailsId(Integer customHistoryConfigDetailsId) {
		this.customHistoryConfigDetailsId = customHistoryConfigDetailsId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getCustomHistoryConfigurationMstrId() {
		return customHistoryConfigurationMstrId;
	}

	public void setCustomHistoryConfigurationMstrId(Integer customHistoryConfigurationMstrId) {
		this.customHistoryConfigurationMstrId = customHistoryConfigurationMstrId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

package com.param.otims.model;

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

	  @NamedNativeQuery(name="GET_PREOPERATIVE_DETAILS_BY_PRO_REQ_ID",
			query=" SELECT pre_operative_id AS \"preOperativeId\", " 
					+" pro_req_id AS \"proReqId\" , indication_reason AS \"indicationReason\", " 
					+" patient_identification_status AS \"patientIdentificationStatus\", " 
					+" patient_consent_status AS \"patientConsentStatus\"," 
					+" who_checklist_status AS \"whoChecklistStatus\", " 
					+" blood_status AS \"bloodStatus\" , icu_status AS \"icuStatus\", anaesthesia_status AS \"anaesthesiaStatus\"," 
					+" created_by AS \"createdBy\" , status AS \"status\"," 
					+ "to_char(created_date,'YYYY-MM-DD') as \"createdDate\", "
					+ "to_char(updated_date,'YYYY-MM-DD') as \"updatedDate\", "
					+" updated_by AS \"updatedBy\", " 
					+" unit_id AS \"unitId\", organization_id AS \"organizationId\" " 
					+" FROM otims.t_pre_operative_details " 
					+" WHERE pro_req_id = :proReqId " ),
	  
	  @NamedNativeQuery(name="GET_ACTIVE_DOCTOR_LIST_WITH_EMAILID",
		query=" select " + 
				" 	doc.doctor_id as \"doctorId\", " + 
				" 	concat(coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"doctorName\", " + 
				" 	coalesce(doc.first_name) as \"firstName\", " + 
				" 	coalesce(doc.middle_name) as \"middleName\", " + 
				" 	coalesce(doc.last_name) as \"lastName\"," + 
				" 	doc.email  as \"email\"," + 
				" 	doc.mobile_number  as \"mobileNumber\" " + 
				" from " + 
				" 	doctor.m_doctor_master doc " + 
				" inner join t_user_unit_mapper uum on " + 
				" 	uum.user_id = doc.user_id " + 
				" where " + 
				" 	doc.organization_id = :orgId" + 
				" 	and uum.unit_id = :unitId" + 
				" 	and doc.status = 'A' " + 
				" 	and uum.status = 'A' " + 
				" order by " + 
				" 	doc.first_name " + 
				"" ),
	  
	  @NamedNativeQuery(name="GET_ANAESTHESIA_DOCTORS",
			query=" select " + 
					" 	doc.doctor_id as \"doctorId\", " + 
					" 	concat(coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"doctorName\", " + 
					" 	coalesce(doc.first_name) as \"firstName\", " + 
					" 	coalesce(doc.middle_name) as \"middleName\", " + 
					" 	coalesce(doc.last_name) as \"lastName\"," + 
					" 	doc.email  as \"email\"," + 
					" 	doc.mobile_number  as \"mobileNumber\" " + 
					" from " + 
					" 	doctor.m_doctor_master doc "
					+ " inner join public.t_user_role_mapper turm  on doc.user_id  = turm.user_id " + 
					" inner join public.m_role_master mrm on mrm.role_id  = turm.role_id  " + 
					" inner join t_user_unit_mapper uum on " + 
					" 	uum.user_id = doc.user_id " + 
					" where " + 
					" 	doc.organization_id = :orgId" + 
					" 	and uum.unit_id = :unitId" + 
					" 	and doc.status = 'A' " + 
					" 	and uum.status = 'A'"
					+ " and mrm.role_type_id = 9 " + 
					" order by " + 
					" 	doc.first_name " + 
					"" )
})


@Entity
@Table(name = "t_pre_operative_details" , schema = "otims")
@SequenceGenerator(name = "pre_operative_details_seq" , sequenceName = "otims.pre_operative_details_seq" , allocationSize = 1)
public class PreOperativeDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "pre_operative_details_seq")
	@Column(name = "pre_operative_id")
	private Integer preOperativeId;
	
	@Column(name = "pro_req_id")
	private Integer proReqId;
	
	@Column(name = "indication_reason")
	private String indicationReason;
	
	@Column(name = "patient_identification_status")
	private Integer patientIdentificationStatus;
	
	@Column(name = "patient_consent_status")
	private Integer patientConsentStatus;
	
	@Column(name = "who_checklist_status")
	private Integer whoChecklistStatus;
	
	@Column(name = "blood_status")
	private Integer bloodStatus;
	
	@Column(name = "icu_status")
	private Integer icuStatus;
	
	@Column(name = "anaesthesia_status")
	private Integer anaesthesiaStatus;
	
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

	public Integer getPreOperativeId() {
		return preOperativeId;
	}

	public void setPreOperativeId(Integer preOperativeId) {
		this.preOperativeId = preOperativeId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public String getIndicationReason() {
		return indicationReason;
	}

	public void setIndicationReason(String indicationReason) {
		this.indicationReason = indicationReason;
	}

	public Integer getPatientIdentificationStatus() {
		return patientIdentificationStatus;
	}

	public void setPatientIdentificationStatus(Integer patientIdentificationStatus) {
		this.patientIdentificationStatus = patientIdentificationStatus;
	}

	public Integer getPatientConsentStatus() {
		return patientConsentStatus;
	}

	public void setPatientConsentStatus(Integer patientConsentStatus) {
		this.patientConsentStatus = patientConsentStatus;
	}

	public Integer getWhoChecklistStatus() {
		return whoChecklistStatus;
	}

	public void setWhoChecklistStatus(Integer whoChecklistStatus) {
		this.whoChecklistStatus = whoChecklistStatus;
	}

	public Integer getBloodStatus() {
		return bloodStatus;
	}

	public void setBloodStatus(Integer bloodStatus) {
		this.bloodStatus = bloodStatus;
	}

	public Integer getIcuStatus() {
		return icuStatus;
	}

	public void setIcuStatus(Integer icuStatus) {
		this.icuStatus = icuStatus;
	}

	public Integer getAnaesthesiaStatus() {
		return anaesthesiaStatus;
	}

	public void setAnaesthesiaStatus(Integer anaesthesiaStatus) {
		this.anaesthesiaStatus = anaesthesiaStatus;
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

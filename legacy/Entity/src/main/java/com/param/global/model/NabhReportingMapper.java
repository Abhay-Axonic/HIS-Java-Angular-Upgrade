package com.param.global.model;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.InstantConverter;
import com.param.global.common.LocalTimeConverter;

@NamedQueries({


	
	@NamedQuery(name = "GET_NABH_REPORTING_MAPPER_LIST", query = "select"
			+ " incReporting.nabhReportingMapperId as nabhReportingMapperId,"
			+ " to_char(incReporting.nabhReportingDate, 'DD/MM/YYYY') as nabhReportingDate,"    
			+ " incReporting.patientId as patientId," +
			// " incReporting.incidentReportingTime as incidentReportingTime, " +    
			" incReporting.nabhReportingNumber as nabhReportingNumber, "
			+ " incReporting.nabhRemark as nabhRemark, " + " incReporting.specialityId as specialityId, "
			+ " incReporting.nabhTypeId as nabhTypeId, "
			+ " incReporting.incidentCategoryId as incidentCategoryId, "
			+ " incReporting.nabhProcessId as nabhProcessId, "
			+ " incReporting.reportingTemplateId as reportingTemplateId, "
			+ " incReporting.reportingTemplateObj as reportingTemplateObj, "
			+ " incReporting.conversationId as conversationId, "
			+ " incReporting.encounterId as encounterId, "
			+ " incReporting.admissionId as admissionId, "
			+ " templateMaster.templateFormatId as templateFormatId, "
			+ " specialityMaster.specialityName as specialityName , "
			+ " incidentCategoryMaster.incidentCategoryDesc as incidentCategoryDesc , "
			+ " nabhProcessMaster.nabhProcessDesc as nabhProcessDesc , "
			+ " patient.uhidNumber as uhidNumber, "
			+ " concat(coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) as patientName, "
			+ " incReporting.status as status " + " from" + " NabhReportingMapper incReporting "
			+ " Left join incReporting.patientRegistration patient "
			+ " Inner join incReporting.specialityMaster specialityMaster "
			+ " Inner join incReporting.incidentCategoryMaster incidentCategoryMaster "
			+ " Inner join incReporting.nabhProcessMaster nabhProcessMaster "
			+ " Inner join incReporting.templateMaster templateMaster with templateMaster.status='A' " + " where"
			+ " incReporting.organizationId =:organizationId" + " and incReporting.unitId =:unitId "
			+ " AND to_char(incReporting.createdDate, 'YYYY-MM-DD') BETWEEN :fromDate AND :toDate "
			+ " and (:specialityId = 0 OR incReporting.specialityId =:specialityId )"
			+ " and (:nabhTypeId = 0 OR incReporting.nabhTypeId =:nabhTypeId )"
			+ " and (:userId = 0 OR incReporting.userId =:userId )"),
	
	
	@NamedQuery(name = "GET_NABH_REPORTING_MAPPER_BY_ID", query = "select"
			+ " incReporting.nabhReportingMapperId as nabhReportingMapperId,"
			+ " to_char(incReporting.nabhReportingDate, 'DD/MM/YYYY') as nabhReportingDate,"
			+ " incReporting.patientId as patientId,"
			+ " to_char( incReporting.nabhReportingTime, 'HH24:MI') AS nabhReportingTime," 
			+" incReporting.nabhReportingNumber as nabhReportingNumber, "
			+ " incReporting.nabhRemark as nabhRemark, " + " incReporting.specialityId as specialityId, "
			+ " incReporting.nabhTypeId as nabhTypeId, " + " incReporting.createdBy as createdBy, "
			+ " incReporting.roleId as roleId, " + " incReporting.userId as userId, "
			+ " incReporting.incidentCategoryId as incidentCategoryId, "
			+ " incReporting.nabhProcessId as nabhProcessId, "
			+ " incReporting.reportingTemplateId as reportingTemplateId, "
			+ " incReporting.reportingTemplateObj as reportingTemplateObj, "
			+ " incReporting.conversationId as conversationId, "
			+ " incReporting.encounterId as encounterId, "
			+ " incReporting.admissionId as admissionId, "
			+ " nabhProcessMaster.nabhProcessDesc as nabhProcessDesc , "
			+ " templateMaster.templateFormatId as templateFormatId, "
			+ " specialityMaster.specialityName as specialityName ," + " patient.uhidNumber as uhidNumber, "+ " genderMaster.code as genderCode, "
			+ " incidentCategoryMaster.incidentCategoryDesc as incidentCategoryDesc , "
			+ " concat(coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) as patientName, "
			+ " incReporting.status as status " + " from" + " NabhReportingMapper incReporting "
			+ " left join incReporting.patientRegistration patient "
			+ " Inner join incReporting.specialityMaster specialityMaster "
			+ " Inner join incReporting.incidentCategoryMaster incidentCategoryMaster "
			+ " Inner join incReporting.templateMaster templateMaster with templateMaster.status='A' "
			+ " INNER JOIN patient.genderMaster genderMaster "
			+ " Inner join incReporting.nabhProcessMaster nabhProcessMaster " + " where"
			+ " incReporting.organizationId =:organizationId " + " and incReporting.unitId =:unitId "
			+ " and incReporting.nabhReportingMapperId =:nabhReportingMapperId "),
	
	@NamedQuery(name = "GET_NABH_REPORTING_MAPPER_BY_ID_FOR_DEPRTMENT", query = "select"
			+ " incReporting.nabhReportingMapperId as nabhReportingMapperId,"
			+ " to_char(incReporting.nabhReportingDate, 'DD/MM/YYYY') as nabhReportingDate,"
			+ " incReporting.patientId as patientId,"
			+ " to_char( incReporting.nabhReportingTime, 'HH24:MI') AS nabhReportingTime," 
			+" incReporting.nabhReportingNumber as nabhReportingNumber, "
			+ " incReporting.nabhRemark as nabhRemark, " + " incReporting.specialityId as specialityId, "
			+ " incReporting.nabhTypeId as nabhTypeId, " + " incReporting.createdBy as createdBy, "
			+ " incReporting.roleId as roleId, " + " incReporting.userId as userId, "
			+ " incReporting.incidentCategoryId as incidentCategoryId, "
			+ " incReporting.nabhProcessId as nabhProcessId, "
			+ " incReporting.reportingTemplateId as reportingTemplateId, "
			+ " incReporting.reportingTemplateObj as reportingTemplateObj, "
			+ " incReporting.conversationId as conversationId, "
			+ " incReporting.encounterId as encounterId, "
			+ " incReporting.admissionId as admissionId, "
			+ " nabhProcessMaster.nabhProcessDesc as nabhProcessDesc , "
			+ " templateMaster.templateFormatId as templateFormatId, "
			+ " specialityMaster.specialityName as specialityName ," 
			+ " incidentCategoryMaster.incidentCategoryDesc as incidentCategoryDesc , "
			+ " incReporting.status as status " + " from" + " NabhReportingMapper incReporting "
			+ " Inner join incReporting.specialityMaster specialityMaster "
			+ " Inner join incReporting.incidentCategoryMaster incidentCategoryMaster "
			+ " Inner join incReporting.templateMaster templateMaster with templateMaster.status='A' "
			+ " Inner join incReporting.nabhProcessMaster nabhProcessMaster " + " where"
			+ " incReporting.organizationId =:organizationId " + " and incReporting.unitId =:unitId "
			+ " and incReporting.nabhReportingMapperId =:nabhReportingMapperId "),

})
@Entity
@Table(name = "t_nabh_reporting_mapper", schema = "public")
@SequenceGenerator(name = "t_nabh_reporting_mapper_seq", sequenceName = "public.t_nabh_reporting_mapper_seq", allocationSize = 1)

public class NabhReportingMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_nabh_reporting_mapper_seq")
	@Column(name = "nabh_reporting_mapper_id")
	private Integer nabhReportingMapperId;

	@Column(name = "nabh_reporting_date")
	private Date nabhReportingDate;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "nabh_reporting_time")
	@Convert(converter = InstantConverter.class)
	private Instant nabhReportingTime;

	@Column(name = "nabh_reporting_number")
	private String nabhReportingNumber;

	@Column(name = "nabh_remark")
	private String nabhRemark;

	@Column(name = "speciality_id")
	private Integer specialityId;

	@Column(name = "nabh_type_id")
	private Integer nabhTypeId;

	@Column(name = "incident_category_id")
	private Integer incidentCategoryId;

	@Column(name = "nabh_process_id")
	private Integer nabhProcessId;

	@Column(name = "reporting_template_id")
	private Integer reportingTemplateId;

	@Column(name = "reporting_template_obj")
	private String reportingTemplateObj;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "status")
	private Character status;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "role_id")
	private Integer roleId;
	
	@Column(name = "conversation_id")
	private String conversationId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "admission_id")
	private Integer admissionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private PatientRegistration patientRegistration;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "speciality_id", insertable = false, updatable = false)
	private SpecialityMaster specialityMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "incident_category_id", insertable = false, updatable = false)
	private IncidentCategoryMaster incidentCategoryMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nabh_process_id", insertable = false, updatable = false)
	private NabhProcessMaster nabhProcessMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reporting_template_id", insertable = false, updatable = false)
	private TemplateMaster templateMaster;
	

	public Integer getNabhReportingMapperId() {
		return nabhReportingMapperId;
	}

	public void setNabhReportingMapperId(Integer nabhReportingMapperId) {
		this.nabhReportingMapperId = nabhReportingMapperId;
	}

	public Date getNabhReportingDate() {
		return nabhReportingDate;
	}

	public void setNabhReportingDate(Date nabhReportingDate) {
		this.nabhReportingDate = nabhReportingDate;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Instant getNabhReportingTime() {
		return nabhReportingTime;
	}

	public void setNabhReportingTime(Instant nabhReportingTime) {
		this.nabhReportingTime = nabhReportingTime;
	}

	public String getNabhReportingNumber() {
		return nabhReportingNumber;
	}

	public void setNabhReportingNumber(String nabhReportingNumber) {
		this.nabhReportingNumber = nabhReportingNumber;
	}

	public String getNabhRemark() {
		return nabhRemark;
	}

	public void setNabhRemark(String nabhRemark) {
		this.nabhRemark = nabhRemark;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getNabhTypeId() {
		return nabhTypeId;
	}

	public void setNabhTypeId(Integer nabhTypeId) {
		this.nabhTypeId = nabhTypeId;
	}

	public Integer getIncidentCategoryId() {
		return incidentCategoryId;
	}

	public void setIncidentCategoryId(Integer incidentCategoryId) {
		this.incidentCategoryId = incidentCategoryId;
	}

	public Integer getNabhProcessId() {
		return nabhProcessId;
	}

	public void setNabhProcessId(Integer nabhProcessId) {
		this.nabhProcessId = nabhProcessId;
	}

	public Integer getReportingTemplateId() {
		return reportingTemplateId;
	}

	public void setReportingTemplateId(Integer reportingTemplateId) {
		this.reportingTemplateId = reportingTemplateId;
	}

	public String getReportingTemplateObj() {
		return reportingTemplateObj;
	}

	public void setReportingTemplateObj(String reportingTemplateObj) {
		this.reportingTemplateObj = reportingTemplateObj;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
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

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public IncidentCategoryMaster getIncidentCategoryMaster() {
		return incidentCategoryMaster;
	}

	public void setIncidentCategoryMaster(IncidentCategoryMaster incidentCategoryMaster) {
		this.incidentCategoryMaster = incidentCategoryMaster;
	}

	public NabhProcessMaster getNabhProcessMaster() {
		return nabhProcessMaster;
	}

	public void setNabhProcessMaster(NabhProcessMaster nabhProcessMaster) {
		this.nabhProcessMaster = nabhProcessMaster;
	}

	public TemplateMaster getTemplateMaster() {
		return templateMaster;
	}

	public void setTemplateMaster(TemplateMaster templateMaster) {
		this.templateMaster = templateMaster;
	}

	
}

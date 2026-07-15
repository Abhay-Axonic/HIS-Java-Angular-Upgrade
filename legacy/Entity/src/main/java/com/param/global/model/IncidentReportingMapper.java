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
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@NamedQueries({


	
		@NamedQuery(name = "GET_INCIDENT_REPORTING_MAPPER_LIST", query = "select"
				+ " incReporting.incidentReportingMapperId as incidentReportingMapperId,"
				+ " to_char(incReporting.incidentReportingDate, 'DD/MM/YYYY') as incidentReportingDate,"
				+ " incReporting.patientId as patientId," +
				// " incReporting.incidentReportingTime as incidentReportingTime, " +
				" incReporting.incidentReportingNumber as incidentReportingNumber, "
				+ " incReporting.incidentRemark as incidentRemark, " + " incReporting.specialityId as specialityId, "
				+ " incReporting.incidentTypeId as incidentTypeId, "
				+ " incReporting.incidentCategoryId as incidentCategoryId, "
				+ " incReporting.incidentReportId as incidentReportId, "
				+ " incReporting.reportingTemplateId as reportingTemplateId, "
				+ " incReporting.reportingTemplateObj as reportingTemplateObj, "
				+ " incReporting.conversationId as conversationId, "
				+ " incReporting.encounterId as encounterId, "
				+ " incReporting.admissionId as admissionId, "
				+ " incidentReportMaster.incidentReportDesc as incidentReportDesc, "
				+ " templateMaster.templateFormatId as templateFormatId, "
				+ " specialityMaster.specialityName as specialityName , "
				+ " incidentCategoryMaster.incidentCategoryDesc as incidentCategoryDesc , "
				+ " patient.uhidNumber as uhidNumber, "
				+ " concat(coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) as patientName, "
				+ " incReporting.status as status " + " from" + " IncidentReportingMapper incReporting "
				+ " Left join incReporting.patientRegistration patient "
				+ " Inner join incReporting.specialityMaster specialityMaster "
				+ " Inner join incReporting.incidentCategoryMaster incidentCategoryMaster "
				+ " Inner join incReporting.incidentReportMaster incidentReportMaster "
				+ " Inner join incReporting.templateMaster templateMaster with templateMaster.status='A' " + " where"
				+ " incReporting.organizationId =:organizationId" + " and incReporting.unitId =:unitId "
				+ " AND to_char(incReporting.createdDate, 'YYYY-MM-DD') BETWEEN :fromDate AND :toDate "
				+ " and (:specialityId = 0 OR incReporting.specialityId =:specialityId )"
				+ " and (:incidentTypeId = 0 OR incReporting.incidentTypeId =:incidentTypeId )"
				+ " and (:userId = 0 OR incReporting.userId =:userId )"),

		@NamedQuery(name = "GET_ACTIVE_INCIDENT_REPORTING_MAPPER_LIST", query = "select"
				+ " incReporting.incidentReportingMapperId as incidentReportingMapperId,"
				+ " to_char(incReporting.incidentReportingDate, 'DD/MM/YYYY') as incidentReportingDate,"
				+ " incReporting.patientId as patientId," +
				// " incReporting.incidentReportingTime as incidentReportingTime, " +
				" incReporting.incidentReportingNumber as incidentReportingNumber, "
				+ " incReporting.incidentRemark as incidentRemark, " + " incReporting.specialityId as specialityId, "
				+ " incReporting.incidentTypeId as incidentTypeId, "
				+ " incReporting.incidentCategoryId as incidentCategoryId, "
				+ " incReporting.incidentReportId as incidentReportId, "
				+ " incReporting.reportingTemplateId as reportingTemplateId, "
				+ " incReporting.reportingTemplateObj as reportingTemplateObj, "
				+ " incReporting.conversationId as conversationId, "
				+ " incReporting.encounterId as encounterId, "
				+ " incReporting.admissionId as admissionId, "
				+ " incidentReportMaster.incidentReportDesc as incidentReportDesc, "
				+ " specialityMaster.specialityName as specialityName , "
				+ " incidentCategoryMaster.incidentCategoryDesc as incidentCategoryDesc , "
				+ " concat(coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) as patientName, "
				+ " incReporting.status as status " + " from" + " IncidentReportingMapper incReporting "
				+ " left join incReporting.patientRegistration patient "
				+ " Inner join incReporting.specialityMaster specialityMaster "
				+ " Inner join incReporting.incidentCategoryMaster incidentCategoryMaster "
				+ " Inner join incReporting.incidentReportMaster incidentReportMaster " + " where "
				+ " incReporting.organizationId =:organizationId " + " and incReporting.unitId =:unitId "
				+ " and incReporting.status ='A' "),

		@NamedQuery(name = "GET_INCIDENT_REPORTING_MAPPER_BY_ID", query = "select"
				+ " incReporting.incidentReportingMapperId as incidentReportingMapperId,"
				+ " to_char(incReporting.incidentReportingDate, 'DD/MM/YYYY') as incidentReportingDate,"
				+ " incReporting.patientId as patientId,"
				+ " to_char( incReporting.incidentReportingTime, 'HH24:MI') AS incidentReportingTime," +
				// " incReporting.incidentReportingTime as incidentReportingTime, " +
				" incReporting.incidentReportingNumber as incidentReportingNumber, "
				+ " incReporting.incidentRemark as incidentRemark, " + " incReporting.specialityId as specialityId, "
				+ " incReporting.incidentTypeId as incidentTypeId, " + " incReporting.createdBy as createdBy, "
				+ " incReporting.roleId as roleId, " + " incReporting.userId as userId, "
				+ " incReporting.incidentCategoryId as incidentCategoryId, "
				+ " incReporting.incidentReportId as incidentReportId, "
				+ " incReporting.reportingTemplateId as reportingTemplateId, "
				+ " incReporting.reportingTemplateObj as reportingTemplateObj, "
				+ " incReporting.conversationId as conversationId, "
				+ " incReporting.encounterId as encounterId, "
				+ " incReporting.admissionId as admissionId, "
				+ " incidentReportMaster.incidentReportDesc as incidentReportDesc, "
				+ " templateMaster.templateFormatId as templateFormatId, "
				+ " specialityMaster.specialityName as specialityName ," + " patient.uhidNumber as uhidNumber, "
				+ " incidentCategoryMaster.incidentCategoryDesc as incidentCategoryDesc , "
				+ " concat(coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) as patientName, "
				+ " incReporting.status as status " + " from" + " IncidentReportingMapper incReporting "
				+ " left join incReporting.patientRegistration patient "
				+ " Inner join incReporting.specialityMaster specialityMaster "
				+ " Inner join incReporting.incidentCategoryMaster incidentCategoryMaster "
				+ " Inner join incReporting.templateMaster templateMaster with templateMaster.status='A' "
				+ " Inner join incReporting.incidentReportMaster incidentReportMaster " + " where"
				+ " incReporting.organizationId =:organizationId " + " and incReporting.unitId =:unitId "
				+ " and incReporting.incidentReportingMapperId =:incidentReportingMapperId "),

})
@NamedNativeQuery(name = "GET_ROLE_WISE_USER_LIST", query = " select " +
					" distinct(um.user_id) as \"userId\", " +
					" concat( coalesce( em.first_name ), ' ', coalesce( em.middle_name ), ' ', coalesce( em.last_name )) as \"userName\" " +
					"from " +
					" public.t_user_role_mapper umppr " +
					"inner join public.m_user_master um on " +
					" um.user_id = umppr.user_id " +
					"inner join public.m_employee_master em on " +
					" em.user_id = um.user_id " +
					"where " +
					" umppr.role_id in (:roleIdList) " +
					" and um.organization_id = :organizationId " +
					" and um.unit_id =:unitId " +
					" and um.status = 'A' " +
					"union select " +
					" distinct(um.user_id) as \"userId\", " +
					" concat( coalesce( doc.first_name ), ' ', coalesce( doc.middle_name ), ' ', coalesce( doc.last_name )) as \"userName\" " +
					"from " +
					" public.t_user_role_mapper umppr " +
					"inner join public.m_user_master um on " +
					" um.user_id = umppr.user_id " +
					"inner join doctor.m_doctor_master doc on " +
					" doc.user_id = um.user_id " +
					"where " +
					" umppr.role_id in (:roleIdList) " +
					" and um.organization_id = :organizationId " +
					" and um.unit_id =:unitId " +
					" and um.status = 'A'"
					+ " ORDER BY \"userName\" ASC ")
@Entity
@Table(name = "t_incident_reporting_mapper", schema = "public")
@SequenceGenerator(name = "t_incident_reporting_mapper_seq", sequenceName = "public.t_incident_reporting_mapper_seq", allocationSize = 1)

public class IncidentReportingMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_incident_reporting_mapper_seq")
	@Column(name = "incident_reporting_mapper_id")
	private Integer incidentReportingMapperId;

	@Column(name = "incident_reporting_date")
	private Date incidentReportingDate;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "incident_reporting_time")
	@Convert(converter = InstantConverter.class)
	private Instant incidentReportingTime;

	@Column(name = "incident_reporting_number")
	private String incidentReportingNumber;

	@Column(name = "incident_remark")
	private String incidentRemark;

	@Column(name = "speciality_id")
	private Integer specialityId;

	@Column(name = "incident_type_id")
	private Integer incidentTypeId;

	@Column(name = "incident_category_id")
	private Integer incidentCategoryId;

	@Column(name = "incident_report_id")
	private Integer incidentReportId;

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
	@JoinColumn(name = "incident_report_id", insertable = false, updatable = false)
	private IncidentReportMaster incidentReportMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reporting_template_id", insertable = false, updatable = false)
	private TemplateMaster templateMaster;

	public Integer getIncidentReportingMapperId() {
		return incidentReportingMapperId;
	}

	public void setIncidentReportingMapperId(Integer incidentReportingMapperId) {
		this.incidentReportingMapperId = incidentReportingMapperId;
	}

	public Date getIncidentReportingDate() {
		return incidentReportingDate;
	}

	public void setIncidentReportingDate(Date incidentReportingDate) {
		this.incidentReportingDate = incidentReportingDate;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getIncidentTypeId() {
		return incidentTypeId;
	}

	public void setIncidentTypeId(Integer incidentTypeId) {
		this.incidentTypeId = incidentTypeId;
	}

	public Integer getIncidentCategoryId() {
		return incidentCategoryId;
	}

	public void setIncidentCategoryId(Integer incidentCategoryId) {
		this.incidentCategoryId = incidentCategoryId;
	}

	public Integer getIncidentReportId() {
		return incidentReportId;
	}

	public void setIncidentReportId(Integer incidentReportId) {
		this.incidentReportId = incidentReportId;
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

	public Instant getIncidentReportingTime() {
		return incidentReportingTime;
	}

	public void setIncidentReportingTime(Instant incidentReportingTime) {
		this.incidentReportingTime = incidentReportingTime;
	}

	public String getIncidentRemark() {
		return incidentRemark;
	}

	public void setIncidentRemark(String incidentRemark) {
		this.incidentRemark = incidentRemark;
	}

	public String getIncidentReportingNumber() {
		return incidentReportingNumber;
	}

	public void setIncidentReportingNumber(String incidentReportingNumber) {
		this.incidentReportingNumber = incidentReportingNumber;
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

	public   Integer getEncounterId() {
		return encounterId;
	}

	public  void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public  Integer getAdmissionId() {
		return admissionId;
	}

	public  void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

}
package com.param.opd.coversheet.model;

import java.util.Date;

import javax.persistence.Column;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UserMaster;
import com.param.global.model.PatientRegistration;
import com.param.global.model.SurgeryMaster;
import com.param.opd.appointment.model.AppointmentMaster;

@NamedQueries({
	
	@NamedQuery(name="GET_PATIENT_SURGICAL_HISTORY",
			query=" SELECT 		patientSurgicalHist.surgicalHistoryDetailsId as surgicalHistoryDetailsId, patientSurgicalHist.remark as remark,"
					+ "				patientSurgicalHist.isNoSignificantStatus as isNoSignificantStatus, patientSurgicalHist.month as month, "
					+ " 			patientSurgicalHist.year as year, patientSurgicalHist.surgeryId as surgeryId, patientSurgicalHist.surgenName as surgenName , patientSurgicalHist.surgeryName as surgeryNameFormulary,"
					+ "				patientSurgicalHist.status as status, patientSurgicalHist.encounterId as encounterId, patientSurgicalHist.patientId as patientId,"
					+ "				patientSurgicalHist.roleId as roleId, patientSurgicalHist.isReviewedFlag as isReviewedFlag, "
					+ "				patientSurgicalHist.isEnterInErrorStatus as isEnterInErrorStatus, "
					+ "				patientSurgicalHist.organizationId as organizationId, patientSurgicalHist.unitId as unitId, patientSurgicalHist.createdBy as createdBy,"
					+ "             patientSurgicalHist.updatedBy as updatedBy,"
					+ "				to_char(patientSurgicalHist.createdDate,'DD/MM/YYYY') as createdDate,patientSurgicalHist.createdDate as createdDateNew, to_char(patientSurgicalHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
					+ " (case "
					+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
					+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
					+ " else 'Admin' end) as userName,"
					+ "		  		surgeryMst.surgeryName as surgeryName "
					+ " FROM 		PatientSurgicalHistory patientSurgicalHist "
					+ " LEFT JOIN 	patientSurgicalHist.surgeryMaster surgeryMst "
					+ " LEFT JOIN patientSurgicalHist.userMaster um "
					+ " LEFT JOIN um.employeeMastersList empmst "
					+ " LEFT JOIN um.doctorMastersList docmst "
					+ " WHERE 		patientSurgicalHist.patientId=:patientId "
					+ " AND			patientSurgicalHist.status = 'A'"
//					+ " AND			patientSurgicalHist.unitId=:unitId"
//					+ " AND 		patientSurgicalHist.encounterId!=:encounterId "
					+ " AND 		patientSurgicalHist.organizationId=:organizationId "
					+ " AND			to_char(patientSurgicalHist.createdDate, 'DD-MM-YYYY') != :created_date"
					+ " ORDER BY 	patientSurgicalHist.surgicalHistoryDetailsId asc"
			),
			
	@NamedQuery(name="GET_PATIENT_SURGICAL_HISTORY_BY_ROLE_ID",
			query=" SELECT 		patientSurgicalHist.surgicalHistoryDetailsId as surgicalHistoryDetailsId, patientSurgicalHist.remark as remark,"
				+ "				patientSurgicalHist.isNoSignificantStatus as isNoSignificantStatus, patientSurgicalHist.month as month, "
				+ " 			patientSurgicalHist.year as year, patientSurgicalHist.surgeryId as surgeryId, patientSurgicalHist.surgenName as surgenName,"
				+ "				patientSurgicalHist.status as status, patientSurgicalHist.encounterId as encounterId, patientSurgicalHist.patientId as patientId,"
				+ "				patientSurgicalHist.roleId as roleId, patientSurgicalHist.isReviewedFlag as isReviewedFlag, "
				+ "				patientSurgicalHist.isEnterInErrorStatus as isEnterInErrorStatus, "
				+ "				patientSurgicalHist.organizationId as organizationId, patientSurgicalHist.unitId as unitId, patientSurgicalHist.createdBy as createdBy,"
				+ "             patientSurgicalHist.updatedBy as updatedBy,"
				+ "				to_char(patientSurgicalHist.createdDate,'DD/MM/YYYY') as createdDate, to_char(patientSurgicalHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
				+ "		  		surgeryMst.surgeryName as surgeryName "
				+ " FROM 		PatientSurgicalHistory patientSurgicalHist "
				+ " LEFT JOIN 	patientSurgicalHist.surgeryMaster surgeryMst"
				+ " WHERE 		patientSurgicalHist.patientId=:patientId "
				+ " AND			patientSurgicalHist.status = 'A'"
				+ " AND			patientSurgicalHist.unitId=:unitId"
				+ " AND 		patientSurgicalHist.roleId =:roleId "
//				+ " AND 		patientSurgicalHist.encounterId!=:encounterId "
				+ " AND 		patientSurgicalHist.organizationId=:organizationId"
		),
	
	@NamedQuery(name="GET_CURRENT_PATIENT_SURGICAL_HISTORY",
	query=" SELECT 		patientSurgicalHist.surgicalHistoryDetailsId as surgicalHistoryDetailsId, patientSurgicalHist.remark as remark,"
		+ "				patientSurgicalHist.isNoSignificantStatus as isNoSignificantStatus, patientSurgicalHist.month as month,patientSurgicalHist.surgeryName as surgeryNameFormulary,"
		+ " 			patientSurgicalHist.year as year, patientSurgicalHist.surgeryId as surgeryId, patientSurgicalHist.surgenName as surgenName,"
		+ "				patientSurgicalHist.status as status, patientSurgicalHist.encounterId as encounterId, patientSurgicalHist.patientId as patientId,"
		+ "				patientSurgicalHist.roleId as roleId, patientSurgicalHist.isReviewedFlag as isReviewedFlag, "
		+ "				patientSurgicalHist.isEnterInErrorStatus as isEnterInErrorStatus, "
		+ "				patientSurgicalHist.organizationId as organizationId, patientSurgicalHist.unitId as unitId, patientSurgicalHist.createdBy as createdBy,"
		+ "             patientSurgicalHist.updatedBy as updatedBy,"
		+ "				to_char(patientSurgicalHist.createdDate,'DD/MM/YYYY') as createdDate, to_char(patientSurgicalHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
		+ "		  		surgeryMst.surgeryName as surgeryName "
		+ " FROM 		PatientSurgicalHistory patientSurgicalHist "
		+ " LEFT JOIN 	patientSurgicalHist.surgeryMaster surgeryMst"
		+ " WHERE 		patientSurgicalHist.patientId=:patientId "
		+ " AND			patientSurgicalHist.status = 'A'"
		+ " AND			patientSurgicalHist.unitId=:unitId"
//		+ " AND 		patientSurgicalHist.encounterId=:encounterId "
		+ " AND 		patientSurgicalHist.organizationId=:organizationId"
		),
		
		@NamedQuery(name="GET_CURRENT_PATIENT_SURGICAL_HISTORY_BY_ROLE_ID",
		query=" SELECT 		patientSurgicalHist.surgicalHistoryDetailsId as surgicalHistoryDetailsId, patientSurgicalHist.remark as remark,"
			+ "				patientSurgicalHist.isNoSignificantStatus as isNoSignificantStatus, patientSurgicalHist.month as month,patientSurgicalHist.surgeryName as surgeryNameFormulary, "
			+ " 			patientSurgicalHist.year as year, patientSurgicalHist.surgeryId as surgeryId, patientSurgicalHist.surgenName as surgenName,"
			+ "				patientSurgicalHist.status as status, patientSurgicalHist.encounterId as encounterId, patientSurgicalHist.patientId as patientId,"
			+ "				patientSurgicalHist.roleId as roleId, patientSurgicalHist.isReviewedFlag as isReviewedFlag, "
			+ "				patientSurgicalHist.isEnterInErrorStatus as isEnterInErrorStatus, "
			+ "				patientSurgicalHist.organizationId as organizationId, patientSurgicalHist.unitId as unitId, patientSurgicalHist.createdBy as createdBy,"
			+ "             patientSurgicalHist.updatedBy as updatedBy,"
			+ "				to_char(patientSurgicalHist.createdDate,'DD/MM/YYYY') as createdDate, to_char(patientSurgicalHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
			+ "		  		surgeryMst.surgeryName as surgeryName "
			+ " FROM 		PatientSurgicalHistory patientSurgicalHist "
			+ " LEFT JOIN 	patientSurgicalHist.surgeryMaster surgeryMst"
			+ " WHERE 		patientSurgicalHist.patientId=:patientId "
			+ " AND			patientSurgicalHist.status = 'A'"
			+ " AND			patientSurgicalHist.unitId=:unitId"
			+ " AND 		patientSurgicalHist.roleId =:roleId "
//			+ " AND 		patientSurgicalHist.encounterId=:encounterId "
			+ " AND 		patientSurgicalHist.organizationId=:organizationId"
		),
		
		@NamedQuery(name="GET_CURRENT_PATIENT_SURGICAL_HISTORY_FOR_COVERSHEET",
		query=" SELECT 		patientSurgicalHist.surgicalHistoryDetailsId as surgicalHistoryDetailsId, patientSurgicalHist.remark as remark,"
			+ "				patientSurgicalHist.isNoSignificantStatus as isNoSignificantStatus, patientSurgicalHist.month as month,patientSurgicalHist.surgeryName as surgeryNameFormulary,"
			+ " 			patientSurgicalHist.year as year, patientSurgicalHist.surgeryId as surgeryId, patientSurgicalHist.surgenName as surgenName,"
			+ "				patientSurgicalHist.status as status, patientSurgicalHist.encounterId as encounterId, patientSurgicalHist.patientId as patientId,"
			+ "				patientSurgicalHist.roleId as roleId, patientSurgicalHist.isReviewedFlag as isReviewedFlag, "
			+ "				patientSurgicalHist.isEnterInErrorStatus as isEnterInErrorStatus, patientSurgicalHist.covTemplateId as covTemplateId,"
			+ "				patientSurgicalHist.organizationId as organizationId, patientSurgicalHist.unitId as unitId, patientSurgicalHist.createdBy as createdBy,"
			+ "  (case "
			+ "   when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
			+ "   when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
			+ "   else 'Admin' end) as userName,"
			+ "             patientSurgicalHist.updatedBy as updatedBy,"
			+ "				to_char(patientSurgicalHist.createdDate,'DD/MM/YYYY') as createdDate,patientSurgicalHist.createdDate as createdDateNew, to_char(patientSurgicalHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
			+ "		  		surgeryMst.surgeryName as surgeryName "
			+ " FROM 		PatientSurgicalHistory patientSurgicalHist "
			+ " LEFT JOIN 	patientSurgicalHist.surgeryMaster surgeryMst "
			+ " LEFT JOIN patientSurgicalHist.userMaster um "
			+ " LEFT JOIN um.employeeMastersList empmst "
			+ " LEFT JOIN um.doctorMastersList docmst "
			+ " WHERE 		patientSurgicalHist.patientId=:patientId "
			+ " AND			patientSurgicalHist.status = 'A'"
//			+ " AND			patientSurgicalHist.unitId=:unitId"
//			+ " AND 		patientSurgicalHist.encounterId=:encounterId "
			+ " AND 		patientSurgicalHist.organizationId=:organizationId "
			+ " AND			to_char(patientSurgicalHist.createdDate, 'DD-MM-YYYY') = :created_date"
			+ " ORDER BY 	patientSurgicalHist.surgicalHistoryDetailsId asc"
			),
		
		@NamedQuery(name="GET_CURRENT_PATIENT_SURGICAL_HISTORY_FOR_COV_TEMPLATE",
		query=" SELECT 		patientSurgicalHist.surgicalHistoryDetailsId as surgicalHistoryDetailsId, patientSurgicalHist.remark as remark,"
			+ "				patientSurgicalHist.isNoSignificantStatus as isNoSignificantStatus, patientSurgicalHist.month as month,patientSurgicalHist.surgeryName as surgeryNameFormulary,"
			+ " 			patientSurgicalHist.year as year, patientSurgicalHist.surgeryId as surgeryId, patientSurgicalHist.surgenName as surgenName,"
			+ "				patientSurgicalHist.status as status, patientSurgicalHist.encounterId as encounterId, patientSurgicalHist.patientId as patientId,"
			+ "				patientSurgicalHist.roleId as roleId, patientSurgicalHist.isReviewedFlag as isReviewedFlag, "
			+ "				patientSurgicalHist.isEnterInErrorStatus as isEnterInErrorStatus, patientSurgicalHist.covTemplateId as covTemplateId,"
			+ "				patientSurgicalHist.organizationId as organizationId, patientSurgicalHist.unitId as unitId, patientSurgicalHist.createdBy as createdBy,"
			+ "  (case "
			+ "   when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
			+ "   when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
			+ "   else 'Admin' end) as userName,"
			+ "             patientSurgicalHist.updatedBy as updatedBy,"
			+ "				to_char(patientSurgicalHist.createdDate,'DD/MM/YYYY') as createdDate, to_char(patientSurgicalHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
			+ "		  		surgeryMst.surgeryName as surgeryName "
			+ " FROM 		PatientSurgicalHistory patientSurgicalHist "
			+ " LEFT JOIN 	patientSurgicalHist.surgeryMaster surgeryMst "
			+ " LEFT JOIN patientSurgicalHist.userMaster um "
			+ " LEFT JOIN um.employeeMastersList empmst "
			+ " LEFT JOIN um.doctorMastersList docmst "
			+ " WHERE 		patientSurgicalHist.covTemplateId=:covTemplateId "
			+ " AND			patientSurgicalHist.status = 'A'"
			+ " AND			patientSurgicalHist.unitId=:unitId"
			+ " AND 		patientSurgicalHist.organizationId=:organizationId "
			+ " ORDER BY 	patientSurgicalHist.surgicalHistoryDetailsId asc"
			),
		
		@NamedQuery(name="GET_PATIENT_SURGICAL_HISTORY_FOR_DOCTOR_APP",
		query=" SELECT 		patientSurgicalHist.surgicalHistoryDetailsId as surgicalHistoryDetailsId, patientSurgicalHist.remark as remark,"
			+ "				patientSurgicalHist.isNoSignificantStatus as isNoSignificantStatus, patientSurgicalHist.month as month,patientSurgicalHist.surgeryName as surgeryNameFormulary,"
			+ " 			patientSurgicalHist.year as year, patientSurgicalHist.surgeryId as surgeryId, patientSurgicalHist.surgenName as surgenName,"
			+ "				patientSurgicalHist.status as status, patientSurgicalHist.encounterId as encounterId, patientSurgicalHist.patientId as patientId,"
			+ "				patientSurgicalHist.roleId as roleId, patientSurgicalHist.isReviewedFlag as isReviewedFlag, "
			+ "				patientSurgicalHist.isEnterInErrorStatus as isEnterInErrorStatus, "
			+ "				patientSurgicalHist.organizationId as organizationId, patientSurgicalHist.unitId as unitId, patientSurgicalHist.createdBy as createdBy,"
			+ "  (case "
			+ "   when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
			+ "   when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
			+ "   else 'Admin' end) as userName,"
			+ "             patientSurgicalHist.updatedBy as updatedBy,"
			+ "				to_char(patientSurgicalHist.createdDate,'DD/MM/YYYY') as createdDate, to_char(patientSurgicalHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
			+ "		  		surgeryMst.surgeryName as surgeryName "
			+ " FROM 		PatientSurgicalHistory patientSurgicalHist "
			+ " LEFT JOIN 	patientSurgicalHist.surgeryMaster surgeryMst "
			+ " LEFT JOIN patientSurgicalHist.userMaster um "
			+ " LEFT JOIN um.employeeMastersList empmst "
			+ " LEFT JOIN um.doctorMastersList docmst "
			+ " WHERE 		patientSurgicalHist.patientId=:patientId "
			+ " AND			patientSurgicalHist.status = 'A'"
//			+ " AND			patientSurgicalHist.unitId=:unitId"
//			+ " AND 		patientSurgicalHist.encounterId=:encounterId "
			+ " AND 		patientSurgicalHist.organizationId=:organizationId "
			+ " ORDER BY 	patientSurgicalHist.surgicalHistoryDetailsId asc"
			),
		
		@NamedQuery(name="GET_PATIENT_SURGICAL_HISTORY_FOR_BOOK_VIEW",
		query=" SELECT 		patientSurgicalHist.surgicalHistoryDetailsId as surgicalHistoryDetailsId, patientSurgicalHist.remark as remark,"
			+ "				patientSurgicalHist.isNoSignificantStatus as isNoSignificantStatus, patientSurgicalHist.month as month,patientSurgicalHist.surgeryName as surgeryNameFormulary,"
			+ " 			patientSurgicalHist.year as year, patientSurgicalHist.surgeryId as surgeryId, patientSurgicalHist.surgenName as surgenName,"
			+ "				patientSurgicalHist.status as status, patientSurgicalHist.encounterId as encounterId, patientSurgicalHist.patientId as patientId,"
			+ "				patientSurgicalHist.roleId as roleId, patientSurgicalHist.isReviewedFlag as isReviewedFlag, "
			+ "				patientSurgicalHist.isEnterInErrorStatus as isEnterInErrorStatus, "
			+ "				patientSurgicalHist.organizationId as organizationId, patientSurgicalHist.unitId as unitId, patientSurgicalHist.createdBy as createdBy,"
			+ "             patientSurgicalHist.updatedBy as updatedBy,"
			+ "				to_char(patientSurgicalHist.createdDate,'DD/MM/YYYY') as createdDate, to_char(patientSurgicalHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
			+ "		  		surgeryMst.surgeryName as surgeryName "
			+ " FROM 		PatientSurgicalHistory patientSurgicalHist "
			+ " LEFT JOIN 	patientSurgicalHist.surgeryMaster surgeryMst"
			+ " WHERE 		patientSurgicalHist.patientId=:patientId "
			+ " AND			patientSurgicalHist.status = 'A'"
			+ " AND 		patientSurgicalHist.organizationId=:organizationId "
			+ " AND to_char(patientSurgicalHist.createdDate , 'YYYY-MM-DD')  BETWEEN :fromDate " 
			+ " AND :toDate "
			+ " ORDER BY 	patientSurgicalHist.surgicalHistoryDetailsId asc"
			),
})


@NamedNativeQuery(name="GET_COMPLETED_PROCEDURE_LIST_FOR_SURGICAL_HISTORY" ,
					query="select " + 
							"	service.service_standard_name as \"surgeryName\"," + 
							"	to_char(pom.prefered_date_time,'month') as \"monthProc\"," + 
							"	to_char(pom.prefered_date_time,'yyyy') as \"yearProc\"," + 
							"	to_char(prm.prefered_date_time,'dd/MM/yyyy') as \"createdDate\"," + 
							"	prm.prefered_date_time as \"createdDateNew\"," + 
							"	ocd.remark as \"remark\"," + 
//							"	prm.procedure_status_id as \"procedureStatusId\"" + 
							" 	concat('Dr.',' ',coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"surgenName\"" + 
							" 	from otims.t_procedure_req_mst prm" + 
							"	inner join otims.t_procedure_order_master pom on prm.pro_req_id=pom.pro_req_id " + 
							"	inner join service.m_service_master service on" + 
							"	prm.primary_procedure_id = service.service_master_id" + 
							"	inner join doctor.m_speciality_master speciality on" + 
							"	speciality.speciality_id = service.speciality_id" + 
							"	inner join doctor.m_sub_speciality_master subspe on" + 
							"	subspe.sub_speciality_id = service.sub_speciality_id" + 
							"	inner join public.m_user_master use on use.user_id=prm.created_by" + 
							"	inner join doctor.m_doctor_master doc on doc.user_id=use.user_id" + 
							"	inner join otims.t_ot_completion_details ocd on ocd.pro_req_id=prm.pro_req_id" + 
							"	where prm.procedure_status_id=2" + 
							"	and prm.patient_id=:patientId" + 
							"	and prm.org_id=:organizationId")

@Entity
@Table(name="t_surgical_history_details",schema="patient")
@SequenceGenerator(name="t_surgical_history_details_seq" , sequenceName="patient.t_surgical_history_details_seq", allocationSize=1)
public class PatientSurgicalHistory {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "t_surgical_history_details_seq")
	@Column(name="surgical_history_details_id")
	private Integer surgicalHistoryDetailsId;

	@Column(name="remark")
	private String remark;
	
	@Column(name="is_no_significant_status")
	private Character isNoSignificantStatus;
	
	@Column(name="month")
	private Integer month;
	
	@Column(name="year")
	private Integer year;
	
	@Column(name="surgery_id")
	private Integer surgeryId;
	
	@Column(name="surgen_name")
	private String surgenName;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="role_id")
	private Integer roleId;
	
	@Column(name="is_reviewed_flag")
	private Character isReviewedFlag;
	
	@Column(name="is_enter_in_error")
	private Character isEnterInErrorStatus;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name="surgery_name")
	private String surgeryName;
	
	@Column(name="cov_template_id")
	private Integer covTemplateId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "surgery_id", insertable = false, nullable = false, updatable = false)
	private SurgeryMaster surgeryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appointment_id", insertable = false, nullable = false, updatable = false)
	private AppointmentMaster appointmentMaster;*/

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="created_by", insertable=false, updatable=false)
	private UserMaster userMaster;
	
	
	public Character getIsEnterInErrorStatus() {
		return isEnterInErrorStatus;
	}

	public void setIsEnterInErrorStatus(Character isEnterInErrorStatus) {
		this.isEnterInErrorStatus = isEnterInErrorStatus;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Character getIsReviewedFlag() {
		return isReviewedFlag;
	}

	public void setIsReviewedFlag(Character isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getSurgicalHistoryDetailsId() {
		return surgicalHistoryDetailsId;
	}

	public void setSurgicalHistoryDetailsId(Integer surgicalHistoryDetailsId) {
		this.surgicalHistoryDetailsId = surgicalHistoryDetailsId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Character getIsNoSignificantStatus() {
		return isNoSignificantStatus;
	}

	public void setIsNoSignificantStatus(Character isNoSignificantStatus) {
		this.isNoSignificantStatus = isNoSignificantStatus;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getSurgeryId() {
		return surgeryId;
	}

	public void setSurgeryId(Integer surgeryId) {
		this.surgeryId = surgeryId;
	}

	public String getSurgenName() {
		return surgenName;
	}

	public void setSurgenName(String surgenName) {
		this.surgenName = surgenName;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public SurgeryMaster getSurgeryMaster() {
		return surgeryMaster;
	}

	public void setSurgeryMaster(SurgeryMaster surgeryMaster) {
		this.surgeryMaster = surgeryMaster;
	}

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getSurgeryName() {
		return surgeryName;
	}

	public void setSurgeryName(String surgeryName) {
		this.surgeryName = surgeryName;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	/*public AppointmentMaster getAppointmentMaster() {
		return appointmentMaster;
	}

	public void setAppointmentMaster(AppointmentMaster appointmentMaster) {
		this.appointmentMaster = appointmentMaster;
	}
	
	*/
	
}

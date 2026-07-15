package com.param.opd.coversheet.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UserMaster;
import com.param.global.model.DoctorMaster;
import com.param.global.model.PatientRegistration;
import com.param.global.model.UserRoleMapper;
import com.param.opd.appointment.model.AppointmentMaster;

@NamedQueries({

		@NamedQuery(name = "GET_PATIENT_DIAGNOSIS_BY_PATIENT_ID",
				 query = "SELECT "
					 	+ " diagMapper.patientDiagnosisId AS patientDiagnosisId, "
					 	+ " diagMapper.admissionId AS admissionId, "
					 	+ " diagMapper.encounterId AS encounterId, "
					 	+ " diagMapper.isDeleted AS isDeleted, "
					 	+ "(select max(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId "
						+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as templateTransactionId, "
					 	+ "(select count(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId and tempObj.status = 'A'"
						+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as diagnosisTempCount, "
					 	+ " diagMapper.patientId AS patientId, "
					 	+ " diagMapper.diagnosisText AS diagnosisText, "
				 		+ " diagMapper.diagnosisId AS diagnosisId, "
				 		+ " diagMapper.type AS type, "
				 		+ " diagMapper.diagnosisType AS diagnosisType, "
				 		+ " (case " 
						+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
						+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
						+ " else 'Admin' end) as userName,"
				 		+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
				 		+ " diagMapper.created_date as created_date_new, "
				 		+ " to_char(diagMapper.created_date,'HH24:MI:SS') as createdTime, "
				 		+ " diagMapper.remark AS remark, "
				 		+ " diagMaster.isNotifiable AS isNotifiable , "
				 		+ " diagMaster.isComorbidity AS isComorbidity,"
				 		+ " diagMaster.code AS code,"
				 		+ " diagMaster.diagnosisName AS diagnosisName "
				+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
				+ " left JOIN diagMapper.diagnosisMaster diagMaster "
				+ " LEFT JOIN diagMapper.userMaster um " 
				+ " LEFT JOIN um.employeeMastersList empmst " 
				+ " LEFT JOIN um.doctorMastersList  docmst "
				+ " WHERE  diagMapper.patientId=:patientId "
				+ " and diagMapper.status='A'"
				+ " AND diagMapper.organizationId=:orgId"
				+ " AND diagMapper.isDiagnosisCancel='N'"
				+ " AND diagMapper.isDeleted='N'"
				+ " AND diagMapper.isResolved='N'"
				+ " order by diagMapper.patientDiagnosisId ASC") ,
		
		@NamedQuery(name = "GET_PATIENT_DIAGNOSIS_COV_TEMPALTE",
		 query = "SELECT "
			 	+ " diagMapper.patientDiagnosisId AS patientDiagnosisId, "
			 	+ " diagMapper.admissionId AS admissionId, "
			 	+ " diagMapper.isDeleted AS isDeleted, "
			 	+ "(select max(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId "
				+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as templateTransactionId, "
			 	+ "(select count(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId and tempObj.status = 'A'"
				+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as diagnosisTempCount, "
			 	+ " diagMapper.patientId AS patientId, "
		 		+ " diagMapper.diagnosisId AS diagnosisId, "
		 		+ " diagMapper.type AS type, "
		 		+ " diagMapper.diagnosisType AS diagnosisType, "
		 		+ " (case " 
				+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
				+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
				+ " else 'Admin' end) as userName,"
		 		+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
		 		+ " to_char(diagMapper.created_date,'HH24:MI') as createdTime, "
		 		+ " diagMapper.remark AS remark, "
		 		+ " diagMaster.isNotifiable AS isNotifiable , "
		 		+ " diagMaster.isComorbidity AS isComorbidity,"
		 		+ " diagMaster.code AS code,"
		 		+ " diagMaster.diagnosisName AS diagnosisName "
				+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
				+ " INNER JOIN diagMapper.diagnosisMaster diagMaster "
				+ " LEFT JOIN diagMapper.userMaster um " 
				+ " LEFT JOIN um.employeeMastersList empmst " 
				+ " LEFT JOIN um.doctorMastersList  docmst "
				+ " WHERE  diagMapper.covTemplateId=:covTemplateId "
				+ " and diagMapper.status='A'"
				+ " AND diagMapper.organizationId=:organizationId"
				+ " AND diagMapper.unitId=:unitId"
				+ " AND diagMapper.isDiagnosisCancel='N'"
				+ " AND diagMapper.isDeleted='N'"
				+ " AND diagMapper.isResolved='N'"
				+ " order by diagMapper.patientDiagnosisId desc") ,
		
		@NamedQuery(name = "GET_PATIENT_DIAGNOSIS_BY_ECNCOUNTER_ID",
		 query = "SELECT "
			 	+ " diagMapper.patientDiagnosisId AS patientDiagnosisId, "
			 	+ " diagMapper.admissionId AS admissionId, "
			 	+ " diagMapper.isDeleted AS isDeleted, "
			 	+ "(select max(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId "
				+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as templateTransactionId, "
			 	+ "(select count(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId and tempObj.status = 'A'"
				+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as diagnosisTempCount, "
			 	+ " diagMapper.patientId AS patientId, "
		 		+ " diagMapper.diagnosisId AS diagnosisId, "
		 		+ " diagMapper.type AS type, "
		 		+ " diagMapper.diagnosisType AS diagnosisType, "
		 		+ " (case " 
//				+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
				+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
				+ " else 'Admin' end) as userName,"
		 		+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
		 		+ " to_char(diagMapper.created_date,'HH24:MI') as createdTime, "
		 		+ " diagMapper.remark AS remark, "
		 		+ " diagMaster.isNotifiable AS isNotifiable , "
		 		+ " diagMaster.isComorbidity AS isComorbidity,"
		 		+ " diagMaster.code AS code,"
		 		+ " diagMaster.diagnosisName AS diagnosisName "
		+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
		+ " INNER JOIN diagMapper.diagnosisMaster diagMaster "
		+ " LEFT JOIN diagMapper.userMaster um " 
		+ " LEFT JOIN um.employeeMastersList empmst " 
		+ " LEFT JOIN um.doctorMastersList  docmst "
		+ " WHERE  diagMapper.patientId=:patientId "
		+ " and diagMapper.status='A'"
		+ " AND diagMapper.organizationId=:orgId"
		+ " AND diagMapper.encounterId=:encounterId"
		+ " AND diagMapper.isDiagnosisCancel='N'"
		+ " AND diagMapper.isDeleted='N'"
		+ " AND diagMapper.isResolved='N'"
		+ " order by diagMapper.patientDiagnosisId desc") ,
		
		@NamedQuery(name = "GET_PATIENT_DIAGNOSIS_BY_ADMISSION_ID",
		 query = "SELECT "
			 	+ " diagMapper.patientDiagnosisId AS patientDiagnosisId, "
			 	+ " diagMapper.admissionId AS admissionId, "
			 	+ " diagMapper.isDeleted AS isDeleted, "
			 	+ "(select max(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId "
				+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as templateTransactionId, "
			 	+ "(select count(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId and tempObj.status = 'A'"
				+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as diagnosisTempCount, "
			 	+ " diagMapper.patientId AS patientId, "
		 		+ " diagMapper.diagnosisId AS diagnosisId, "
		 		+ " diagMapper.type AS type, "
		 		+ " diagMapper.diagnosisType AS diagnosisType, "
		 		+ " (case " 
				+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
				+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
				+ " else 'Admin' end) as userName,"
		 		+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
		 		+ " to_char(diagMapper.created_date,'HH24:MI') as createdTime, "
		 		+ " diagMapper.remark AS remark, "
		 		+ " diagMaster.isNotifiable AS isNotifiable , "
		 		+ " diagMaster.isComorbidity AS isComorbidity,"
		 		+ " diagMaster.code AS code,"
		 		+ " diagMaster.diagnosisName AS diagnosisName "
				+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
				+ " INNER JOIN diagMapper.diagnosisMaster diagMaster "
				+ " LEFT JOIN diagMapper.userMaster um " 
				+ " LEFT JOIN um.employeeMastersList empmst " 
				+ " LEFT JOIN um.doctorMastersList  docmst "
				+ " WHERE  diagMapper.patientId=:patientId "
				+ " and diagMapper.status='A'"
				+ " AND diagMapper.organizationId=:orgId"
				+ " AND diagMapper.admissionId=:admissionId"
				+ " AND diagMapper.isDiagnosisCancel='N'"
				+ " AND diagMapper.isDeleted='N'"
				+ " AND diagMapper.isResolved='N'"
				+ " order by diagMapper.patientDiagnosisId desc") ,
		
		@NamedQuery(name = "GET_PATIENT_DIAGNOSIS_BY_PATIENT_ID_FOR_BOOK_VIEW",
		 query = "SELECT "
				 	+ " diagMapper.patientDiagnosisId AS patientDiagnosisId, "
				 	+ " diagMapper.encounterId AS encounterId, "
				 	+ " diagMapper.admissionId AS admissionId, "
						/*
						 * +
						 * "(select max(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId "
						 * +
						 * " and diagMapper.patientId=tempObj.patientId group by tempObj.templateTransactionId ) as templateTransactionId, "
						 */
			 	+ "(select max(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId "
				+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as templateTransactionId, "
			 	+ "(select count(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId and tempObj.status = 'A'"
				+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as diagnosisTempCount, "
			 	+ " diagMapper.patientId AS patientId, "
		 		+ " diagMapper.diagnosisId AS diagnosisId, "
		 		+ " diagMapper.type AS type, "
		 		+ " diagMapper.diagnosisType AS diagnosisType, "
		 		+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
		 		+ " to_char(diagMapper.created_date,'HH24:MI') as createdTime, "
		 		+ " diagMapper.remark AS remark, "
		 		+ " diagMaster.isNotifiable AS isNotifiable , "
		 		+ " diagMaster.isComorbidity AS isComorbidity,"
		 		+ " diagMaster.code AS code,"
		 		+ " diagMaster.diagnosisName AS diagnosisName "
		+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
		+ " INNER JOIN diagMapper.diagnosisMaster diagMaster "
		+ " WHERE  diagMapper.patientId=:patientId "
		+ " AND "
		+ " diagMapper.organizationId=:orgId"
		+ " AND diagMapper.isDiagnosisCancel='N'"
		+ " AND diagMapper.isDeleted='N'"
		+ " AND diagMapper.isResolved='N'"
		+ " AND to_char(diagMapper.created_date , 'YYYY-MM-DD') BETWEEN :fromDate " 
		+ " AND :toDate ") ,
		
		@NamedQuery(name = "GET_PATIENT_DIAGNOSIS_BY_PATIENT_ID_FOR_ICON",
		 query = "SELECT "
			 	+ " distinct(diagMapper.diagnosisId) AS diagnosisId, "
			 	+ " diagMapper.patientId AS patientId, "
		 		+ " diagMaster.isNotifiable AS isNotifiable , "
		 		+ " diagMaster.isComorbidity AS isComorbidity,"
		 		+ " diagMaster.code AS code,"
		 		+ " diagMaster.diagnosisName AS diagnosisName "
		+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
		+ " INNER JOIN diagMapper.diagnosisMaster diagMaster "
		+ " WHERE  diagMapper.patientId=:patientId "
//		+ "AND "
//		+ " diagMapper.unitId=:unitId "
		+ " AND diagMapper.organizationId=:orgId"
		+ " AND diagMapper.isDiagnosisCancel='N'"
		+ " AND diagMapper.isResolved='N'"
		+ " AND diagMapper.isDeleted='N'")
		,
		
		
		/*@NamedQuery(name = "GET_PATIENT_DIAGNOSIS_BY_PATIENT_ID_FOR_MEDICAL_HISTORY",
		 query = "SELECT diagMapper.patientId AS patientId, "
		 		+ " diagMapper.diagnosisId AS diagnosisId, "
		 		+ " diagMapper.type AS type, "
		 		+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
		 		+ " to_char(diagMapper.resolvedDate,'DD/MM/YYYY') as resolvedDate, "
		 		+ " diagMapper.remark AS remark, "
		 		+ " diagMaster.isNotifiable AS isNotifiable , "
		 		+ " diagMaster.isComorbidity AS isComorbidity,"
		 		+ " diagMaster.code AS code,"
		 		+ " diagMaster.diagnosisName AS diagnosisName ,"
		 		+ " diagMapper.isResolved as isResolved "
				+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
				+ " INNER JOIN diagMapper.diagnosisMaster diagMaster "
				+ " WHERE  diagMapper.patientId=:patientId "
//				+ "AND diagMapper.unitId=:unitId "
				+ " AND diagMapper.organizationId=:orgId"
//				+ " AND  to_char(diagMapper.created_date,'DD/MM/YYYY') =:createdDate "
				+ " AND diagMapper.isResolved='Y'"
				+ " AND diagMapper.isDeleted='N'"
				+ " AND diagMapper.isDiagnosisCancel='N'") ,
*/
		@NamedQuery(name = "GET_PATIENT_DIAGNOSIS_BY_PATIENT_VISIT",
		query = "SELECT diagMapper.patientId AS patientId, "
				+ " diagMapper.diagnosisId AS diagnosisId, "
				+ " diagMapper.type AS type, "
				+ " diagMapper.remark AS remark, "
				+ " diagMaster.isNotifiable AS isNotifiable , "
				+ " diagMaster.isComorbidity AS isComorbidity,"
				+ " diagMaster.code AS code,"
				+ " diagMaster.diagnosisName AS diagnosisName "
		+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
		+ " INNER JOIN diagMapper.diagnosisMaster diagMaster "
		+ " WHERE  diagMapper.patientId=:patientId "
		+ " AND diagMapper.isResolved='N'"
		+ " AND diagMapper.isDeleted='N'"
		+ " AND diagMapper.isDiagnosisCancel='N'"),
		
		@NamedQuery(name = "GET_LIST_PATIENT_CANCELLED_DIAGNOSIS",
		query = "SELECT diagMapper.patientId AS patientId, "
				+ " diagMapper.diagnosisId AS diagnosisId, "
				+ " diagMapper.justificationCancel AS justificationCancel, "
				+ " diagMapper.isDiagnosisCancel AS isDiagnosisCancel, "
				+ " diagMapper.type AS type, "
		 		+ " diagMapper.diagnosisType AS diagnosisType, "
		 		+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
				+ " to_char(diagMapper.created_date,'HH24:MI') as createdTime, "
				+ " to_char(diagMapper.updated_date,'DD/MM/YYYY') as updated_date, "
				+ " to_char(diagMapper.updated_date,'HH24:MI') as updatedTime, "
				+ " diagMapper.remark AS remark, "
				+ " diagMaster.isNotifiable AS isNotifiable , "
				+ " diagMaster.isComorbidity AS isComorbidity,"
				+ " diagMaster.code AS code,"
				+ " diagMaster.diagnosisName AS diagnosisName, "
				+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
				+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
				+ " INNER JOIN diagMapper.diagnosisMaster diagMaster "
				+ " LEFT JOIN 	diagMapper.userMaster userMaster"
//				+ " LEFT JOIN 	userMaster.employeeMastersList employee"
				+ " LEFT JOIN 	userMaster.doctorMastersList doc"
				+ " WHERE  diagMapper.patientId=:patientId AND "
				+ " diagMapper.unitId=:unitId "
				+ " AND "
				+ " diagMapper.organizationId=:orgId"
				+ " AND diagMapper.isDiagnosisCancel='Y'"
				+ " AND diagMapper.isResolved='N'"
				+ " AND diagMapper.isDeleted='N'"
				) ,
		
		@NamedQuery(name = "UPDATE_PATIENT_DIAGNOSIS_TYPE",
		query = "UPDATE DiagnosisPatientAppointmentMapper diagMapper "
				+ "  SET diagMapper.isDeleted = :isDeleted  "
				+ " WHERE diagMapper.organizationId= :organisationId "
//				+ " AND diagMapper.updated_date=:updatedDate"
				+ " AND diagMapper.unitId = :unitId"
				+ " AND diagMapper.patientDiagnosisId = :patientDiagnosisId"
				+ " AND diagMapper.patientId = :patientId"
				),
		
		@NamedQuery(name="CHECK_PRIMARY_DAIGNOSIS_AVAILABEL",
		query="SELECT diagMapper.patientId AS patientId, diagMapper.patientDiagnosisId as patientDiagnosisId"
					+ " FROM DiagnosisPatientAppointmentMapper diagMapper"
					+ " WHERE diagMapper.diagnosisType= :diagnosisType"
					+ " AND diagMapper.patientId = :patientId"
					+ " AND diagMapper.isDeleted = 'N'"
					),
		})
@NamedNativeQueries({
	@NamedNativeQuery(name="GET_PATIENT_DIAGNOSIS_BY_PATIENT_ID_FOR_MEDICAL_HISTORY", 
			  query="select" + 
			  		"	diagnosisp0_.patient_id as \"patientId\"," + 
			  		"	diagnosisp0_.diagnosis_id as \"diagnosisId\"," + 
			  		"	diagnosisp0_.type as \"type\"," + 
			  		"	to_char(diagnosisp0_.created_date,'DD/MM/YYYY') as \"created_date\"," + 
			  		"	(SELECT CAST(DATE_PART('day', :created_date::timestamp - :resolveDate::timestamp) AS int))as resolvedDate, " + 
			  		"	diagnosisp0_.remark as \"remark\"," + 
			  		"	diagnosism1_.is_notifiable as \"isNotifiable\"," + 
			  		"	diagnosism1_.is_comorbidity as \"isComorbidity\"," + 
			  		"	diagnosism1_.code as \"code\"," + 
			  		"	diagnosism1_.diagnosis_name as \"diagnosisName\"," + 
			  		"	diagnosisp0_.is_resolved as \"isResolved\"" + 
			  		" from" + 
			  		"	patient.t_patient_diagnosis_appo_mapper diagnosisp0_" + 
			  		" inner join emr.m_diagnosis_master diagnosism1_ on" + 
			  		"	diagnosisp0_.diagnosis_id = diagnosism1_.diagnosis_id" + 
			  		" where" + 
			  		"	diagnosisp0_.patient_id =:patientId " + 
			  		"	and diagnosisp0_.organization_id =:orgId" + 
			  		"	and diagnosisp0_.is_resolved = 'Y'" + 
			  		"	and diagnosisp0_.is_deleted = 'N'" + 
			  		"	and diagnosisp0_.is_diagnosis_cancel = 'N'" ),
	
	@NamedNativeQuery(name="GET_FREQUENTLY_USED_DIAGNOSIS_LIST", 
	  query=/*"select  " + 
	  		"	diagnosisp0_.diagnosis_id as \"diagnosisId\",  " + 
	  		"	diagnosism1_.diagnosis_name as \"diagnosisName\",  " + 
	  		"	count(*)  " + 
	  		"from  " + 
	  		"	patient.t_patient_diagnosis_appo_mapper diagnosisp0_  " + 
	  		"inner join emr.m_diagnosis_master diagnosism1_ on  " + 
	  		"	diagnosisp0_.diagnosis_id = diagnosism1_.diagnosis_id  " + 
	  		"left outer join public.m_user_master usermaster2_ on  " + 
	  		"	diagnosisp0_.created_by = usermaster2_.user_id  " + 
	  		"left outer join public.m_employee_master employeema3_ on  " + 
	  		"	usermaster2_.user_id = employeema3_.user_id  " + 
	  		"left outer join doctor.m_doctor_master doctormast4_ on  " + 
	  		"	usermaster2_.user_id = doctormast4_.user_id  " + 
	  		"where  " + 
	  		"	 diagnosisp0_.status = 'A'  " + 
	  		"	and diagnosisp0_.organization_id =:orgId  " + 
	  		"	and diagnosisp0_.is_diagnosis_cancel = 'N'  " + 
	  		"	and diagnosisp0_.is_deleted = 'N'  " + 
	  		"	and diagnosisp0_.is_resolved = 'N'  " + 
	  		"group  by   " + 
	  		"diagnosisp0_.diagnosis_id,  " + 
	  		"diagnosism1_.diagnosis_name  " + 
	  		"order by  " + 
	  		"	count(*) desc  " + 
	  		"limit 10;"*/
						"  select   " 
						+ "	diagnosisp0_.diagnosis_id as \"diagnosisId\",   "
						+ "	diagnosism1_.diagnosis_name as \"diagnosisName\",   "
						+ "	diagnosisp0_.diagnosis_text as \"diagnosisText\", " 
						+ "	count(*)   " + "from   "
						+ "	patient.t_patient_diagnosis_appo_mapper diagnosisp0_   "
						+ "left join emr.m_diagnosis_master diagnosism1_ on   "
						+ "	diagnosisp0_.diagnosis_id = diagnosism1_.diagnosis_id   "
						+ "left outer join public.m_user_master usermaster2_ on   "
						+ "	diagnosisp0_.created_by = usermaster2_.user_id   "
						+ "left outer join public.m_employee_master employeema3_ on   "
						+ "	usermaster2_.user_id = employeema3_.user_id   "
						+ "left outer join doctor.m_doctor_master doctormast4_ on   "
						+ "	usermaster2_.user_id = doctormast4_.user_id   " 
						+ "where   "
						+ "	 diagnosisp0_.status = 'A'   " 
						+ "	and diagnosisp0_.organization_id =:orgId   "
						+ "	and diagnosisp0_.is_diagnosis_cancel = 'N'   " 
						+ "	and diagnosisp0_.is_deleted = 'N'   "
						+ "	and (diagnosisp0_.diagnosis_id is not null or diagnosisp0_.diagnosis_text is not null) "
						+ "	and diagnosisp0_.is_resolved = 'N'   " + "group  by    " 
						+ " diagnosisp0_.diagnosis_id,   "
						+ " diagnosism1_.diagnosis_name  , " 
						+ " diagnosisp0_.diagnosis_text " + " order by   "
						+ "	count(*) desc   " 
						+ " limit 10;"),
	
})




@Entity 

@Table(name = "t_patient_diagnosis_appo_mapper", schema = "patient")
@SequenceGenerator(name = "patient_diagnosis_appo_mapper_seq", sequenceName = "patient.patient_diagnosis_appo_mapper_seq", allocationSize = 1)
public class DiagnosisPatientAppointmentMapper {

	@Id
	@Column(name = "patient_diagnosis_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_diagnosis_appo_mapper_seq")
	private int patientDiagnosisId;

	@Column(name = "encounter_id")
	private Integer encounterId;

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
	private Integer created_by;

	@Column(name = "created_date")
	private Date created_date;

	@Column(name = "updated_by")
	private Integer updated_by;

	@Column(name = "updated_date")
	private Date updated_date;

	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "diagnosis_type")
	private Integer diagnosisType;
	
	@Column(name = "is_deleted")
	private Character isDeleted;

	@Column(name = "is_resolved")
	private Character isResolved;
	
	@Column(name = "resolved_date")
	private Date resolvedDate;
	
	@Column(name = "resolved_remark")
	private String resolvedRemark;
	
	@Column(name = "diagnosis_text")
	private String diagnosisText;
	
	@Column(name="cov_template_id")
	private Integer covTemplateId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id", insertable = false, nullable = false, updatable = false)
	private EncounterMaster encounterMaster;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diagnosis_id", insertable = false, nullable = false, updatable = false)
	private DiagnosisMaster diagnosisMaster;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, nullable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, nullable = false, updatable = false)
	private UserMaster userMaster;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRoleMapper> userMasterList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DoctorMaster> doctorMastersList;

	@Column(name = "justification_cancel")
	private String justificationCancel;

	@Column(name = "is_diagnosis_cancel")
	private Character isDiagnosisCancel;
	
	@Column(name = "is_while_admission")
	private Character isWhileAdmission;

	public Character getIsWhileAdmission() {
		return isWhileAdmission;
	}

	public void setIsWhileAdmission(Character isWhileAdmission) {
		this.isWhileAdmission = isWhileAdmission;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
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

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}

	public Date getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}

	public EncounterMaster getEncounterMaster() {
		return encounterMaster;
	}

	public void setEncounterMaster(EncounterMaster encounterMaster) {
		this.encounterMaster = encounterMaster;
	}

	public DiagnosisMaster getDiagnosisMaster() {
		return diagnosisMaster;
	}

	public void setDiagnosisMaster(DiagnosisMaster diagnosisMaster) {
		this.diagnosisMaster = diagnosisMaster;
	}

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public int getPatientDiagnosisId() {
		return patientDiagnosisId;
	}

	public void setPatientDiagnosisId(int patientDiagnosisId) {
		this.patientDiagnosisId = patientDiagnosisId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getJustificationCancel() {
		return justificationCancel;
	}

	public void setJustificationCancel(String justificationCancel) {
		this.justificationCancel = justificationCancel;
	}

	public Character getIsDiagnosisCancel() {
		return isDiagnosisCancel;
	}

	public void setIsDiagnosisCancel(Character isDiagnosisCancel) {
		this.isDiagnosisCancel = isDiagnosisCancel;
	}

	public Integer getDiagnosisType() {
		return diagnosisType;
	}

	public void setDiagnosisType(Integer diagnosisType) {
		this.diagnosisType = diagnosisType;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Character getIsResolved() {
		return isResolved;
	}

	public void setIsResolved(Character isResolved) {
		this.isResolved = isResolved;
	}

	public Date getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(Date resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	public String getResolvedRemark() {
		return resolvedRemark;
	}

	public void setResolvedRemark(String resolvedRemark) {
		this.resolvedRemark = resolvedRemark;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	public String getDiagnosisText() {
		return diagnosisText;
	}

	public void setDiagnosisText(String diagnosisText) {
		this.diagnosisText = diagnosisText;
	}
	
	

}

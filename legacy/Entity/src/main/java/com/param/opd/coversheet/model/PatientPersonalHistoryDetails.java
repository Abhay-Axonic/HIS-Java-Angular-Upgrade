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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.PatientRegistration;
import com.param.opd.appointment.model.AppointmentMaster;

@NamedQueries({
	/*@NamedQuery(name="GET_PATIENT_PERSONAL_HISTORY_DEATILS",
				query=" SELECT 		patientPersonalHist.personalHistoryDetailsId as personalHistoryDetailsId, "
					+ " 			patientPersonalHist.description as description, patientPersonalHist.remark as remark,"
					+ " 			patientPersonalHist.encounterId as encounterId, patientPersonalHist.patientId as patientId, "
					+ " 			patientPersonalHist.isNoSignificantHistoryStatus as isNoSignificantHistoryStatus, patientPersonalHist.organizationId as organizationId,"
					+ " 			patientPersonalHist.unitId as unitId, patientPersonalHist.createdBy as createdBy, patientPersonalHist.updatedBy as updatedBy,"
					+ " 			to_char(patientPersonalHist.createdDate,'DD/MM/YYYY') as createdDate,  to_char(patientPersonalHist.updatedDate,'DD/MM/YYYY') as updatedDate, "
					+ " 			patientPersonalHist.status as status, patientPersonalHist.roleId as roleId,"
					+ "				patientPersonalHist.isReviewedFlag as isReviewedFlag, "
					+"				patientPersonalHist.isEnterInErrorStatus as isEnterInErrorStatus "	
					+ " FROM 		PatientPersonalHistoryDetails patientPersonalHist "
					+ " INNER JOIN 	patientPersonalHist.patientRegistration patientReg "
					+ " WHERE 		patientPersonalHist.patientId =:patientId "
					+ " AND 		patientPersonalHist.status = 'A'"
//					+ " AND 		patientPersonalHist.unitId=:unitId "
					+ " AND 		patientPersonalHist.organizationId=:organizationId "
					+ " AND 		patientPersonalHist.resolvedRemark is null "
					+ " AND			to_char(patientPersonalHist.createdDate, 'DD-MM-YYYY') != :created_date"
					+ " ORDER BY 	patientPersonalHist.personalHistoryDetailsId asc"
			),*/
			
			@NamedQuery(name="GET_PATIENT_PERSONAL_HISTORY_DEATILS_BY_ROLE_ID",
			query=" SELECT 		patientPersonalHist.personalHistoryDetailsId as personalHistoryDetailsId, "
				+ " 			patientPersonalHist.description as description, patientPersonalHist.remark as remark,"
				+ " 			patientPersonalHist.encounterId as encounterId, patientPersonalHist.patientId as patientId, "
				+ " 			patientPersonalHist.isNoSignificantHistoryStatus as isNoSignificantHistoryStatus, patientPersonalHist.organizationId as organizationId,"
				+ " 			patientPersonalHist.unitId as unitId, patientPersonalHist.createdBy as createdBy, patientPersonalHist.updatedBy as updatedBy,"
				+ " 			to_char(patientPersonalHist.createdDate,'DD/MM/YYYY') as createdDate,  to_char(patientPersonalHist.updatedDate,'DD/MM/YYYY') as updatedDate, "
				+ " 			patientPersonalHist.status as status, patientPersonalHist.roleId as roleId, "
				+ "				patientPersonalHist.isReviewedFlag as isReviewedFlag, "
				+"				patientPersonalHist.isEnterInErrorStatus as isEnterInErrorStatus "	
				+ " FROM 		PatientPersonalHistoryDetails patientPersonalHist "
				+ " INNER JOIN 	patientPersonalHist.patientRegistration patientReg "
				+ " WHERE 		patientPersonalHist.patientId =:patientId AND patientPersonalHist.status = 'A'"
				+ " AND 		patientPersonalHist.unitId=:unitId "
				+ " AND 		patientPersonalHist.organizationId=:organizationId "
				+ " AND 		patientPersonalHist.roleId =:roleId "
				+ " AND 		patientPersonalHist.encounterId NOT IN (:encounterId) "
				+ " ORDER BY 	patientPersonalHist.personalHistoryDetailsId desc"
		),
		
		@NamedQuery(name="GET_CURRENT_PATIENT_PERSONAL_HISTORY_DEATILS",
		query=" SELECT 		patientPersonalHist.personalHistoryDetailsId as personalHistoryDetailsId, "
			+ " 			patientPersonalHist.description as description, patientPersonalHist.remark as remark,"
			+ " 			patientPersonalHist.encounterId as encounterId, patientPersonalHist.patientId as patientId, "
			+ " 			patientPersonalHist.isNoSignificantHistoryStatus as isNoSignificantHistoryStatus, patientPersonalHist.organizationId as organizationId,"
			+ " 			patientPersonalHist.unitId as unitId, patientPersonalHist.createdBy as createdBy, patientPersonalHist.updatedBy as updatedBy,"
			+ " 			to_char(patientPersonalHist.createdDate,'DD/MM/YYYY') as createdDate,  to_char(patientPersonalHist.updatedDate,'DD/MM/YYYY') as updatedDate, "
			+ " 			patientPersonalHist.status as status, patientPersonalHist.roleId as roleId,"
			+ "				patientPersonalHist.isReviewedFlag as isReviewedFlag, "
			+"				patientPersonalHist.isEnterInErrorStatus as isEnterInErrorStatus "	
			+ " FROM 		PatientPersonalHistoryDetails patientPersonalHist "
			+ " INNER JOIN 	patientPersonalHist.patientRegistration patientReg "
			+ " WHERE 		patientPersonalHist.patientId =:patientId AND patientPersonalHist.status = 'A'"
			+ " AND 		patientPersonalHist.unitId=:unitId "
			+ " AND 		patientPersonalHist.organizationId=:organizationId "
//			+ " AND 		patientPersonalHist.encounterId =:encounterId "
//			+ " AND 		patientPersonalHist.admissionId =:admissionId "
//			+ " ORDER BY 	patientPersonalHist.personalHistoryDetailsId desc"
	),
	
	@NamedQuery(name="GET_CURRENT_PATIENT_PERSONAL_HISTORY_DEATILS_BY_ROLE_ID",
	query=" SELECT 		patientPersonalHist.personalHistoryDetailsId as personalHistoryDetailsId, "
		+ " 			patientPersonalHist.description as description, patientPersonalHist.remark as remark,"
		+ " 			patientPersonalHist.encounterId as encounterId, patientPersonalHist.patientId as patientId, "
		+ " 			patientPersonalHist.isNoSignificantHistoryStatus as isNoSignificantHistoryStatus, patientPersonalHist.organizationId as organizationId,"
		+ " 			patientPersonalHist.unitId as unitId, patientPersonalHist.createdBy as createdBy, patientPersonalHist.updatedBy as updatedBy,"
		+ " 			to_char(patientPersonalHist.createdDate,'DD/MM/YYYY') as createdDate,  to_char(patientPersonalHist.updatedDate,'DD/MM/YYYY') as updatedDate, "
		+ " 			patientPersonalHist.status as status, patientPersonalHist.roleId as roleId, "
		+ "				patientPersonalHist.isReviewedFlag as isReviewedFlag, "
		+"				patientPersonalHist.isEnterInErrorStatus as isEnterInErrorStatus "						
		+ " FROM 		PatientPersonalHistoryDetails patientPersonalHist "
		+ " INNER JOIN 	patientPersonalHist.patientRegistration patientReg "
		+ " WHERE 		patientPersonalHist.patientId =:patientId AND patientPersonalHist.status = 'A'"
		+ " AND 		patientPersonalHist.unitId=:unitId "
		+ " AND 		patientPersonalHist.organizationId=:organizationId "
		+ " AND 		patientPersonalHist.roleId =:roleId "
//		+ " AND 		patientPersonalHist.encounterId=:encounterId "
//		+ " ORDER BY 	patientPersonalHist.personalHistoryDetailsId desc"
),

@NamedQuery(name="GET_PATIENT_PERSONAL_HISTORY_DEATILS_BY_ID",
query=" SELECT 		patientPersonalHist.personalHistoryDetailsId as personalHistoryDetailsId, "
	+ " 			patientPersonalHist.description as description, patientPersonalHist.remark as remark,"
	+ " 			patientPersonalHist.encounterId as encounterId, patientPersonalHist.patientId as patientId, "
	+ " 			patientPersonalHist.isNoSignificantHistoryStatus as isNoSignificantHistoryStatus, patientPersonalHist.organizationId as organizationId,"
	+ " 			patientPersonalHist.unitId as unitId, patientPersonalHist.createdBy as createdBy, patientPersonalHist.updatedBy as updatedBy,"
	+ " 			to_char(patientPersonalHist.createdDate,'DD/MM/YYYY') as createdDate,  to_char(patientPersonalHist.updatedDate,'DD/MM/YYYY') as updatedDate, "
	+ " 			patientPersonalHist.status as status, patientPersonalHist.roleId as roleId,"
	+ "				patientPersonalHist.isReviewedFlag as isReviewedFlag "
	+ " FROM 		PatientPersonalHistoryDetails patientPersonalHist "
	+ " WHERE 		patientPersonalHist.personalHistoryDetailsId=:personalHistoryId "
	
),

/*@NamedQuery(name="GET_CURRENT_PATIENT_PERSONAL_HISTORY_DEATILS_FOR_COVERSHEET",
query=" SELECT 		patientPersonalHist.personalHistoryDetailsId as personalHistoryDetailsId, "
	+ " 			patientPersonalHist.description as description, patientPersonalHist.remark as remark,"
	+ " 			patientPersonalHist.encounterId as encounterId, patientPersonalHist.patientId as patientId, "
	+ " 			patientPersonalHist.isNoSignificantHistoryStatus as isNoSignificantHistoryStatus, patientPersonalHist.organizationId as organizationId,"
	+ " 			patientPersonalHist.unitId as unitId, patientPersonalHist.createdBy as createdBy, patientPersonalHist.updatedBy as updatedBy,"
	+ " 			to_char(patientPersonalHist.createdDate,'DD/MM/YYYY') as createdDate,  to_char(patientPersonalHist.updatedDate,'DD/MM/YYYY') as updatedDate, "
	+ " 			patientPersonalHist.status as status, patientPersonalHist.roleId as roleId,"
	+ "				patientPersonalHist.isReviewedFlag as isReviewedFlag, "
	+"				patientPersonalHist.isEnterInErrorStatus as isEnterInErrorStatus "	
	+ " FROM 		PatientPersonalHistoryDetails patientPersonalHist "
	+ " INNER JOIN 	patientPersonalHist.patientRegistration patientReg "
	+ " WHERE 		patientPersonalHist.patientId =:patientId AND patientPersonalHist.status = 'A'"
//	+ " AND 		patientPersonalHist.unitId=:unitId "
	+ " AND 		patientPersonalHist.organizationId=:organizationId "
	+ " AND 		patientPersonalHist.resolvedRemark is null "
	+ " AND			to_char(patientPersonalHist.createdDate, 'DD-MM-YYYY') = :created_date"
	+ " ORDER BY 	patientPersonalHist.personalHistoryDetailsId asc "
),*/

@NamedQuery(name="GET_PATIENT_PERSONAL_HISTORY_FOR_BOOK_VIEW",
query=" SELECT 		patientPersonalHist.personalHistoryDetailsId as personalHistoryDetailsId, "
	+ " 			patientPersonalHist.description as description, patientPersonalHist.remark as remark,"
	+ " 			patientPersonalHist.encounterId as encounterId, patientPersonalHist.patientId as patientId, "
	+ " 			patientPersonalHist.isNoSignificantHistoryStatus as isNoSignificantHistoryStatus, patientPersonalHist.organizationId as organizationId,"
	+ " 			patientPersonalHist.unitId as unitId, patientPersonalHist.createdBy as createdBy, patientPersonalHist.updatedBy as updatedBy,"
	+ " 			to_char(patientPersonalHist.createdDate,'DD/MM/YYYY') as createdDate,  to_char(patientPersonalHist.updatedDate,'DD/MM/YYYY') as updatedDate, "
	+ " 			patientPersonalHist.status as status, patientPersonalHist.roleId as roleId,"
	+ "				patientPersonalHist.isReviewedFlag as isReviewedFlag, "
	+"				patientPersonalHist.isEnterInErrorStatus as isEnterInErrorStatus "	
	+ " FROM 		PatientPersonalHistoryDetails patientPersonalHist "
	+ " INNER JOIN 	patientPersonalHist.patientRegistration patientReg "
	+ " WHERE 		patientPersonalHist.patientId =:patientId "
	+ " AND patientPersonalHist.status = 'A'"
	+ " AND 		patientPersonalHist.organizationId=:organizationId "
	+ " AND 		patientPersonalHist.resolvedRemark is null "
	+ " AND to_char(patientPersonalHist.createdDate , 'YYYY-MM-DD') BETWEEN :fromDate " 
	+ " AND :toDate "
	+ " ORDER BY 	patientPersonalHist.personalHistoryDetailsId asc "
),

@NamedQuery(name="GET_RESOLVED_PATIENT_PERSONAL_HISTORY_DEATILS_FOR_COVERSHEET",
query=" SELECT 		patientPersonalHist.personalHistoryDetailsId as personalHistoryDetailsId, "
	+ " 			patientPersonalHist.description as description, patientPersonalHist.remark as remark,"
	+ " 			patientPersonalHist.encounterId as encounterId, patientPersonalHist.patientId as patientId, "
	+ " 			to_char(patientPersonalHist.resolvedDate,'DD/MM/YYYY') as resolvedDate, patientPersonalHist.resolvedRemark as resolvedRemark, "
	+ " 			patientPersonalHist.isNoSignificantHistoryStatus as isNoSignificantHistoryStatus, patientPersonalHist.organizationId as organizationId,"
	+ " 			patientPersonalHist.unitId as unitId, patientPersonalHist.createdBy as createdBy, patientPersonalHist.updatedBy as updatedBy,"
	+ " 			to_char(patientPersonalHist.createdDate,'DD/MM/YYYY') as createdDate,  to_char(patientPersonalHist.updatedDate,'DD/MM/YYYY') as updatedDate, "
	+ " 			patientPersonalHist.status as status, patientPersonalHist.roleId as roleId,"
	+ "				patientPersonalHist.isReviewedFlag as isReviewedFlag, "
	+"				patientPersonalHist.isEnterInErrorStatus as isEnterInErrorStatus "	
	+ " FROM 		PatientPersonalHistoryDetails patientPersonalHist "
	+ " INNER JOIN 	patientPersonalHist.patientRegistration patientReg "
	+ " WHERE 		patientPersonalHist.patientId =:patientId AND patientPersonalHist.status = 'A'"
//	+ " AND 		patientPersonalHist.unitId=:unitId "
	+ " AND 		patientPersonalHist.organizationId=:organizationId "
	+ " AND 		patientPersonalHist.resolvedRemark is not null "
	+ " ORDER BY 	patientPersonalHist.personalHistoryDetailsId asc "
),

})

@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_CURRENT_PATIENT_PERSONAL_HISTORY_DEATILS_FOR_COVERSHEET",
			query=  "select" + 
					"	patientPersonalHist.personal_history_details_id as \"personalHistoryDetailsId\"," + 
					"	patientPersonalHist.description as \"description\"," + 
					"	patientPersonalHist.remark as \"remark\"," + 
					"	patientPersonalHist.cov_template_id as \"covTemplateId\"," + 
					"	patientPersonalHist.encounter_id as \"encounterId\"," + 
					"	patientPersonalHist.patient_id as \"patientId\"," + 
					"	patientPersonalHist.is_no_significant_history_status as \"isNoSignificantHistoryStatus\"," + 
					"	patientPersonalHist.organization_id as \"organizationId\"," + 
					"	patientPersonalHist.unit_id as \"unitId\"," + 
					"	patientPersonalHist.created_by as \"createdBy\"," + 
					"	patientPersonalHist.updated_by as \"updatedBy\"," + 
					"	to_char(" + 
					"		patientPersonalHist.created_date," + 
					"		'DD/MM/YYYY'" + 
					"	) as \"createdDate\"," + 
					"	patientPersonalHist.created_date as \"createdDateNew\"," + 
					"	to_char(" + 
					"		patientPersonalHist.updated_date," + 
					"		'DD/MM/YYYY'" + 
					"	) as \"updatedDate\"," + 
					"	patientPersonalHist.status as \"status\"," + 
					"	patientPersonalHist.role_id as \"roleId\"," + 
					"	patientPersonalHist.is_reviewed_flag as \"isReviewedFlag\"," + 
					"	 (case " + 
					"  when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
					"  when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
					"  else 'Admin' end) as \"userName\", " + 
					"	patientPersonalHist.is_enter_in_error as \"isEnterInErrorStatus\" " + 
					" from patient.t_patient_personal_history_details patientPersonalHist" + 
					"  inner join patient.t_patient_registration patientReg on" + 
					"	patientPersonalHist.patient_id = patientReg.patient_id" + 
					"	left  join public.m_user_master um on" + 
					"	patientPersonalHist.created_by = um.user_id" + 
					" left  join public.m_employee_master empmst on" + 
					"	um.user_id = empmst.user_id" + 
					" left  join doctor.m_doctor_master docmst on" + 
					"	um.user_id = docmst.user_id " + 
					" where" + 
					"	patientPersonalHist.patient_id =:patientId" + 
					"	and patientPersonalHist.status = 'A'" +  
					"	and patientPersonalHist.organization_id =:organizationId" + 
					"	and(" + 
					"		patientPersonalHist.resolved_remark is null" + 
					"	)" + 
					"	and to_char(patientPersonalHist.created_date,'DD-MM-YYYY') =:created_date" + 
					" order by" + 
					"	patientPersonalHist.personal_history_details_id asc" 
					),
	
	@NamedNativeQuery(name="GET_CURRENT_PATIENT_PERSONAL_HISTORY_DEATILS_FOR_COV_TEMPLATE",
	query=  "select" + 
			"	patientPersonalHist.personal_history_details_id as \"personalHistoryDetailsId\"," + 
			"	patientPersonalHist.description as \"description\"," + 
			"	patientPersonalHist.remark as \"remark\"," + 
			"	patientPersonalHist.cov_template_id as \"covTemplateId\"," + 
			"	patientPersonalHist.encounter_id as \"encounterId\"," + 
			"	patientPersonalHist.patient_id as \"patientId\"," + 
			"	patientPersonalHist.is_no_significant_history_status as \"isNoSignificantHistoryStatus\"," + 
			"	patientPersonalHist.organization_id as \"organizationId\"," + 
			"	patientPersonalHist.unit_id as \"unitId\"," + 
			"	patientPersonalHist.created_by as \"createdBy\"," + 
			"	patientPersonalHist.updated_by as \"updatedBy\"," + 
			"	to_char(" + 
			"		patientPersonalHist.created_date," + 
			"		'DD/MM/YYYY'" + 
			"	) as \"createdDate\"," + 
			"	to_char(" + 
			"		patientPersonalHist.updated_date," + 
			"		'DD/MM/YYYY'" + 
			"	) as \"updatedDate\"," + 
			"	patientPersonalHist.status as \"status\"," + 
			"	patientPersonalHist.role_id as \"roleId\"," + 
			"	patientPersonalHist.is_reviewed_flag as \"isReviewedFlag\"," + 
			"	 (case " + 
			"  when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
			"  when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
			"  else 'Admin' end) as \"userName\", " + 
			"	patientPersonalHist.is_enter_in_error as \"isEnterInErrorStatus\" " + 
			" from patient.t_patient_personal_history_details patientPersonalHist" + 
			"  inner join patient.t_patient_registration patientReg on" + 
			"	patientPersonalHist.patient_id = patientReg.patient_id" + 
			"	left  join public.m_user_master um on" + 
			"	patientPersonalHist.created_by = um.user_id" + 
			" left  join public.m_employee_master empmst on" + 
			"	um.user_id = empmst.user_id" + 
			" left  join doctor.m_doctor_master docmst on" + 
			"	um.user_id = docmst.user_id " + 
			" where" + 
			"	patientPersonalHist.cov_template_id =:covTemplateId" + 
			"	and patientPersonalHist.status = 'A'" +  
			"	and patientPersonalHist.organization_id =:organizationId" + 
			"	and patientPersonalHist.unit_id =:unitId" + 
			"	and(" + 
			"		patientPersonalHist.resolved_remark is null" + 
			"	)" + 
			" order by" + 
			"	patientPersonalHist.personal_history_details_id asc" 
			),
	
	@NamedNativeQuery(name="GET_PATIENT_PERSONAL_HISTORY_DEATILS",
	query=  "select" + 
			"	patientPersonalHist.personal_history_details_id as \"personalHistoryDetailsId\"," + 
			"	patientPersonalHist.description as \"description\"," + 
			"	patientPersonalHist.remark as \"remark\"," + 
			"	patientPersonalHist.encounter_id as \"encounterId\"," + 
			"	patientPersonalHist.patient_id as \"patientId\"," + 
			"	patientPersonalHist.is_no_significant_history_status as \"isNoSignificantHistoryStatus\"," + 
			"	patientPersonalHist.organization_id as \"organizationId\"," + 
			"	patientPersonalHist.unit_id as \"unitId\"," + 
			"	patientPersonalHist.created_by as \"createdBy\"," + 
			"	patientPersonalHist.updated_by as \"updatedBy\"," + 
			"	to_char(" + 
			"		patientPersonalHist.created_date," + 
			"		'DD/MM/YYYY'" + 
			"	) as \"createdDate\"," + 
			"			patientPersonalHist.created_date as \"createdDateNew\"," + 
			"	to_char(" + 
			"		patientPersonalHist.updated_date," + 
			"		'DD/MM/YYYY'" + 
			"	) as \"updatedDate\"," + 
			"	patientPersonalHist.status as \"status\"," + 
			"	patientPersonalHist.role_id as \"roleId\"," + 
			"	patientPersonalHist.is_reviewed_flag as \"isReviewedFlag\"," + 
			"	 (case " + 
			"  when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
			"  when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
			"  else 'Admin' end) as \"userName\", " + 
			"	patientPersonalHist.is_enter_in_error as \"isEnterInErrorStatus\" " + 
			" from patient.t_patient_personal_history_details patientPersonalHist" + 
			"  inner join patient.t_patient_registration patientReg on" + 
			"	patientPersonalHist.patient_id = patientReg.patient_id" + 
			"	left  join public.m_user_master um on" + 
			"	patientPersonalHist.created_by = um.user_id" + 
			" left  join public.m_employee_master empmst on" + 
			"	um.user_id = empmst.user_id" + 
			" left  join doctor.m_doctor_master docmst on" + 
			"	um.user_id = docmst.user_id " + 
			" where" + 
			"	patientPersonalHist.patient_id =:patientId" + 
			"	and patientPersonalHist.status = 'A'" +  
			"	and patientPersonalHist.organization_id =:organizationId" + 
			"	and(" + 
			"		patientPersonalHist.resolved_remark is null" + 
			"	)" + 
			"	and to_char(patientPersonalHist.created_date,'DD-MM-YYYY') !=:created_date" + 
			" order by" + 
			"	patientPersonalHist.personal_history_details_id asc" 
			),
	@NamedNativeQuery(name="GET_PATIENT_PERSONAL_HISTORY_FOR_DOCTOR_APP",
	query=  "select" + 
			"	patientPersonalHist.personal_history_details_id as \"personalHistoryDetailsId\"," + 
			"	patientPersonalHist.description as \"description\"," + 
			"	patientPersonalHist.remark as \"remark\"," + 
			"	patientPersonalHist.encounter_id as \"encounterId\"," + 
			"	patientPersonalHist.patient_id as \"patientId\"," + 
			"	patientPersonalHist.is_no_significant_history_status as \"isNoSignificantHistoryStatus\"," + 
			"	patientPersonalHist.organization_id as \"organizationId\"," + 
			"	patientPersonalHist.unit_id as \"unitId\"," + 
			"	patientPersonalHist.created_by as \"createdBy\"," + 
			"	patientPersonalHist.updated_by as \"updatedBy\"," + 
			"	to_char(" + 
			"		patientPersonalHist.created_date," + 
			"		'DD/MM/YYYY'" + 
			"	) as \"createdDate\"," + 
			"	to_char(" + 
			"		patientPersonalHist.updated_date," + 
			"		'DD/MM/YYYY'" + 
			"	) as \"updatedDate\"," + 
			"	patientPersonalHist.status as \"status\"," + 
			"	patientPersonalHist.role_id as \"roleId\"," + 
			"	patientPersonalHist.is_reviewed_flag as \"isReviewedFlag\"," + 
			"	 (case " + 
			"  when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
			"  when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
			"  else 'Admin' end) as \"userName\", " + 
			"	patientPersonalHist.is_enter_in_error as \"isEnterInErrorStatus\" " + 
			" from patient.t_patient_personal_history_details patientPersonalHist" + 
			"  inner join patient.t_patient_registration patientReg on" + 
			"	patientPersonalHist.patient_id = patientReg.patient_id" + 
			"	left  join public.m_user_master um on" + 
			"	patientPersonalHist.created_by = um.user_id" + 
			" left  join public.m_employee_master empmst on" + 
			"	um.user_id = empmst.user_id" + 
			" left  join doctor.m_doctor_master docmst on" + 
			"	um.user_id = docmst.user_id " + 
			" where" + 
			"	patientPersonalHist.patient_id =:patientId" + 
			"	and patientPersonalHist.status = 'A'" +  
			"	and patientPersonalHist.organization_id =:organizationId" + 
			"	and(" + 
			"		patientPersonalHist.resolved_remark is null" + 
			"	)" + 
			" order by" + 
			"	patientPersonalHist.personal_history_details_id asc" 
			)
})
@Entity
@Table(name = "t_patient_personal_history_details", schema = "patient")
@SequenceGenerator(name="t_patient_personal_history_details_seq", sequenceName="patient.t_patient_personal_history_details_seq", allocationSize=1)
public class PatientPersonalHistoryDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_patient_personal_history_details_seq")
	@Column(name="personal_history_details_id")
	private Integer personalHistoryDetailsId;
	
	@Column(name="description")
	private String description;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="role_id")
	private Integer roleId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="is_no_significant_history_status")
	private char isNoSignificantHistoryStatus;
	
	@Column(name="is_reviewed_flag")
	private char isReviewedFlag;
	
	@Column(name="is_enter_in_error")
	private char isEnterInErrorStatus;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private char status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name = "resolved_Date")
	private Date resolvedDate;

	@Column(name="cov_template_id")
	private Integer covTemplateId;
	
	@Column(name="resolved_Remark")
	private String resolvedRemark;
	
	
	public String getResolvedRemark() {
		return resolvedRemark;
	}

	public void setResolvedRemark(String resolvedRemark) {
		this.resolvedRemark = resolvedRemark;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appointment_id", insertable = false, nullable = false, updatable = false)
	private AppointmentMaster appointmentMaster;*/
	
	
	
	
	
	
	public char getIsReviewedFlag() {
		return isReviewedFlag;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public char getIsEnterInErrorStatus() {
		return isEnterInErrorStatus;
	}

	public void setIsEnterInErrorStatus(char isEnterInErrorStatus) {
		this.isEnterInErrorStatus = isEnterInErrorStatus;
	}

	public void setIsReviewedFlag(char isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getPersonalHistoryDetailsId() {
		return personalHistoryDetailsId;
	}

	public void setPersonalHistoryDetailsId(Integer personalHistoryDetailsId) {
		this.personalHistoryDetailsId = personalHistoryDetailsId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	
	public char getIsNoSignificantHistoryStatus() {
		return isNoSignificantHistoryStatus;
	}

	public void setIsNoSignificantHistoryStatus(char isNoSignificantHistoryStatus) {
		this.isNoSignificantHistoryStatus = isNoSignificantHistoryStatus;
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

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public Date getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(Date resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}
	
	
}

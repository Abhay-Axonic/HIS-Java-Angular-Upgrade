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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UserMaster;
import com.param.global.model.DoctorMaster;
import com.param.global.model.EmployeeMaster;

@NamedQueries({
	@NamedQuery(name="GET_COMPLAINT_APPOINTMENT_MAPPER",
				query=" SELECT comApp.complaintAppoId as complaintAppoId, comApp.encounterId as encounterId, comApp.diagnosisId as diagnosisId,"
						+ " comApp.resolvedRemark as resolvedRemark, to_char(comApp.resolvedDate, 'DD/MM/YYYY') as resolvedDate,"
					+ "(select max(tempObj.mIcpcId) from TemplateTransaction tempObj where tempObj.mIcpcId=comApp.diagnosisId "
					+ " and comApp.encounterId=tempObj.encounterId group by tempObj.mIcpcId ) as templateIcpcId,"
					+ "(select count(tempObj.mIcpcId) from TemplateTransaction tempObj where tempObj.mIcpcId=comApp.diagnosisId and tempObj.status = 'A'"
					+ " and comApp.encounterId=tempObj.encounterId group by tempObj.mIcpcId ) as complaintTempCount,"
					+ " (case "
					+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
					+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
					+ " else 'Admin' end) as userName,"
					+ " digMast.icpcDesc as diagnosisName, digMast.icpcCode as icpcCode, comApp.since as since, comApp.duration as duration, comApp.remark as remark,"
					+ " to_char(comApp.cerated_date, 'dd/mm/yyyy') as ceratedDate ,comApp.status as status, comApp.roleId as roleId, comApp.icpcCodeText AS icpcCodeText, "
					+ " digMast.icpcSystemId as icpcSystemId, comApp.covTemplateId as covTemplateId,"
					+ " comApp.cerated_date as ceratedDateNew ,"
					+ " comApp.isReviewedFlag as isReviewedFlag ,icpcSystemMaster.icpcSystemDesc as icpcSystemDesc"
					+ " FROM ComplaintAppointmentMapper comApp "
					+ " INNER JOIN comApp.diagnosisMaster digMast "
					+ " LEFT JOIN comApp.icpcSystemMaster icpcSystemMaster "
					+ " LEFT JOIN comApp.userMaster um "
					+ " LEFT JOIN um.employeeMastersList empmst "
					+ " LEFT JOIN um.doctorMastersList docmst "
					+ " WHERE comApp.encounterId =:encounterId AND comApp.status = 'A'"
					+ " AND comApp.unitId =:unitId "
					+ " AND comApp.organizationId =:organizationId "
					+ " AND comApp.isResolved ='N' "
					+ " AND (comApp.isComplaintStop ='N' OR  comApp.isComplaintStop IS NULL) "
					+ " ORDER BY comApp.complaintAppoId asc"
			),
	
	@NamedQuery(name="GET_COMPLAINT_APPOINTMENT_MAPPER_COV_TEMPLATE",
	query=" SELECT comApp.complaintAppoId as complaintAppoId, comApp.encounterId as encounterId, comApp.diagnosisId as diagnosisId,"
			+ " comApp.resolvedRemark as resolvedRemark, to_char(comApp.resolvedDate, 'DD/MM/YYYY') as resolvedDate,"
		+ "(select max(tempObj.mIcpcId) from TemplateTransaction tempObj where tempObj.mIcpcId=comApp.diagnosisId "
		+ " and comApp.encounterId=tempObj.encounterId group by tempObj.mIcpcId ) as templateIcpcId,"
		+ "(select count(tempObj.mIcpcId) from TemplateTransaction tempObj where tempObj.mIcpcId=comApp.diagnosisId and tempObj.status = 'A'"
		+ " and comApp.encounterId=tempObj.encounterId group by tempObj.mIcpcId ) as complaintTempCount,"
		+ " (case "
		+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
		+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
		+ " else 'Admin' end) as userName,"
		+ " digMast.icpcDesc as diagnosisName, digMast.icpcCode as icpcCode, comApp.since as since, comApp.duration as duration, comApp.remark as remark,"
		+ " to_char(comApp.cerated_date, 'dd/mm/yyyy') as ceratedDate ,comApp.status as status, comApp.roleId as roleId, comApp.icpcCodeText AS icpcCodeText, "
		+ " digMast.icpcSystemId as icpcSystemId, comApp.covTemplateId as covTemplateId,"
		+ " comApp.isReviewedFlag as isReviewedFlag ,icpcSystemMaster.icpcSystemDesc as icpcSystemDesc"
		+ " FROM ComplaintAppointmentMapper comApp "
		+ " INNER JOIN comApp.diagnosisMaster digMast "
		+ " LEFT JOIN comApp.icpcSystemMaster icpcSystemMaster "
		+ " LEFT JOIN comApp.userMaster um "
		+ " LEFT JOIN um.employeeMastersList empmst "
		+ " LEFT JOIN um.doctorMastersList docmst "
		+ " WHERE comApp.covTemplateId =:covTemplateId"
		+ " AND comApp.status = 'A'"
		+ " AND comApp.unitId =:unitId "
		+ " AND comApp.organizationId =:organizationId "
		+ " AND comApp.isResolved ='N' "
		+ " AND (comApp.isComplaintStop ='N' OR  comApp.isComplaintStop IS NULL) "
		+ " ORDER BY comApp.complaintAppoId asc"
),
			
			@NamedQuery(name="GET_COMPLAINT_APPOINTMENT_MAPPER_BY_ROLE_ID",
			query=" SELECT comApp.complaintAppoId as complaintAppoId,  comApp.encounterId as encounterId, comApp.diagnosisId as diagnosisId,"
				+ " digMast.icpcDesc as diagnosisName, digMast.icpcCode as icpcCode, comApp.since as since, "
				+ " to_char(comApp.cerated_date, 'dd/mm/yyyy') as ceratedDate, comApp.duration as duration, comApp.remark as remark,"
				+ " comApp.status as status, comApp.roleId as roleId, comApp.icpcCodeText AS icpcCodeText, "
				+ " digMast.icpcSystemId as icpcSystemId, "
				+ " comApp.isReviewedFlag as isReviewedFlag ,icpcSystemMaster.icpcSystemDesc as icpcSystemDesc"
				+ " FROM ComplaintAppointmentMapper comApp "
				+ " INNER JOIN comApp.diagnosisMaster digMast "
				+ " LEFT JOIN comApp.icpcSystemMaster icpcSystemMaster "
				+ " WHERE comApp.encounterId =:encounterId AND comApp.status = 'A'"
				+ " AND comApp.unitId =:unitId "
				+ " AND comApp.organizationId =:organizationId "
				+ " AND comApp.roleId =:roleId "
				+ " ORDER BY comApp.complaintAppoId asc"
		),
		
		/*@NamedQuery(name="GET_COMPLAINT_APPOINTMENT_MAPPER_BY_COMPLAINT_ID",
		query=" SELECT comApp.complaintAppoId as complaintAppoId,  comApp.encounterId as encounterId, comApp.diagnosisId as diagnosisId,"
			+ " comApp.since as since, comApp.duration as duration, comApp.remark as remark,"
			+ " comApp.status as status, comApp.roleId as roleId, "
			+ " diagnosisMaster.icpcDesc as  diagnosisName, "
			+ " comApp.isReviewedFlag as isReviewedFlag "
			+ " FROM ComplaintAppointmentMapper comApp "
			+ " INNER JOIN comApp.diagnosisMaster diagnosisMaster "
			+ " WHERE comApp.complaintAppoId =:complaintAppoId "
	)*/
})

@NamedNativeQueries({
	

	
	@NamedNativeQuery(name="GET_COMPLAINT_APPOINTMENT_BY_PATIENT_ID",
			query="select" + 
					"	complaintAdmission.complaint_admission_id as \"complaintAdmissionId\"," + 
					"	complaintAdmission.admission_id as \"admissionId\"," + 
					"	complaintAdmission.diagnosis_id as \"diagnosisId\"," + 
					"	(" + 
					"		select" + 
					"			max( templateTransaction.m_icpc_id )" + 
					"		from" + 
					"			public.t_template_transaction templateTransaction" + 
					"		where" + 
					"			templateTransaction.m_icpc_id = complaintAdmission.diagnosis_id" + 
					"			and complaintAdmission.admission_id = templateTransaction.admission_id" + 
					"		group by" + 
					"			templateTransaction.m_icpc_id" + 
					"	) as \"mIcpcId1\"," + 
					"	(" + 
					"		select" + 
					"			count( templateTransaction1.m_icpc_id )" + 
					"		from" + 
					"			public.t_template_transaction templateTransaction1" + 
					"		where" + 
					"			templateTransaction1.m_icpc_id = complaintAdmission.diagnosis_id" + 
					"			and complaintAdmission.admission_id = templateTransaction1.admission_id" + 
					"		group by" + 
					"			templateTransaction1.m_icpc_id" + 
					"	) as \"mIcpcId\"," + 
					"	icpc1_.icpc_desc as \"diagnosisName\"," + 
					"	icpc1_.icpc_code as \"icpcCode\"," + 
					"	complaintAdmission.since as \"since\"," + 
					"	complaintAdmission.duration as \"duration\"," + 
					"	complaintAdmission.remark as \"remark\"," + 
					"		to_char(" + 
					"		complaintAdmission.created_date," + 
					"		'dd/mm/yyyy'" + 
					"	) as \"createdDate\"," + 
					"	complaintAdmission.status as \"status\"," + 
					"	complaintAdmission.role_id as \"roleId\"," + 
					"	complaintAdmission.icpc_code_text as \"icpcCodeText\"," + 
					"	icpc1_.icpc_system_id as \"icpcSystemId\"," + 
					"		complaintAdmission.is_reviewed_flag as \"isReviewedFlag\"," + 
					"	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\", "
					+ " adm.visit_type_id as \"visitTypeId\", " + 
					"		(" + 
					"		coalesce(employee.first_name)|| ' ' || coalesce(employee.middle_name)|| ' ' || coalesce(employee.last_name)" + 
					"	) as \"employeeName\"," + 
					"	(" + 
					"		'Dr. ' || coalesce(doctor.first_name)|| ' ' || coalesce(doctor.middle_name)|| ' ' || coalesce(doctor.last_name)" + 
					"	) as \"doctorName\"" + 
					" from" + 
					"	adt.t_complaint_admission complaintAdmission" + 
					" inner join emr.m_icpc icpc1_ on" + 
					"	complaintAdmission.diagnosis_id = icpc1_.icpc_id" + 
					" left  join emr.m_icpc_system icpcsystem2_ on" + 
					"	complaintAdmission.icpc_system_id = icpcsystem2_.icpc_system_id" + 
					" left  join public.m_user_master um on" + 
					"	complaintAdmission.created_by = um.user_id" + 
					" left  join public.m_employee_master employee on" + 
					"	um.user_id = employee.user_id" + 
					" left  join doctor.m_doctor_master doctor on" + 
					"	um.user_id = doctor.user_id "
					+ "  inner join adt.t_admission adm on" + 
					"	complaintAdmission.admission_id = adm.admission_id"
					+ " left  join public.m_visit_type_master vm on" + 
					"	vm.visit_type_id = adm.visit_type_id" + 
					" where" + 
					"	complaintAdmission.patient_id =:patientId" + 
					"	and " + 
					"	complaintAdmission.status = 'A'" + 
					"	and complaintAdmission.is_complaint_stop = 'N'" + 
					"	and complaintAdmission.organization_id =:organizationId" +
					"	and complaintAdmission.is_resolved ='N'" + 
					" union" + 
					" select" + 
					"	complaintAppointment.complaint_appo_id as \"complaintAppoId\"," + 
					"	complaintAppointment.encounter_id as \"encounterId\"," + 
					"	complaintAppointment.diagnosis_id as \"diagnosisId\"," + 
					"	(" + 
					"		select" + 
					"			max( templateTransaction.m_icpc_id )" + 
					"		from" + 
					"			public.t_template_transaction templateTransaction" + 
					"		where" + 
					"			templateTransaction.m_icpc_id = complaintAppointment.diagnosis_id" + 
					"			and complaintAppointment.encounter_id = templateTransaction.encounter_id" + 
					"		group by" + 
					"			templateTransaction.m_icpc_id" + 
					"	) as \"mIcpcId1\"," + 
					"	(" + 
					"		select" + 
					"			count( templateTransaction1.m_icpc_id )" + 
					"		from" + 
					"			public.t_template_transaction templateTransaction1" + 
					"		where" + 
					"			templateTransaction1.m_icpc_id = complaintAppointment.diagnosis_id" + 
					"			and complaintAppointment.encounter_id = templateTransaction1.encounter_id" + 
					"		group by" + 
					"			templateTransaction1.m_icpc_id" + 
					"	) as \"mIcpcId\"," + 
					"	icpc1_.icpc_desc as \"diagnosisName\"," + 
					"	icpc1_.icpc_code as \"icpcCode\"," + 
					"	complaintAppointment.since as \"since\"," + 
					"	complaintAppointment.duration as \"duration\"," + 
					"	complaintAppointment.remark as \"remark\"," + 
					"	to_char(" + 
					"		complaintAppointment.cerated_date," + 
					"		'dd/mm/yyyy'" + 
					"	) as \"createdDate\"," + 
					"	complaintAppointment.status as \"status\"," + 
					"	complaintAppointment.role_id as \"roleId\"," + 
					"	complaintAppointment.icpc_code_text as \"icpcCodeText\"," + 
					"	icpc1_.icpc_system_id as \"icpcSystemId\"," + 
					"	complaintAppointment.is_reviewed_flag as \"isReviewedFlag\"," + 
					"	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\", "
					+ " entMst.visit_type_id as \"visitTypeId\", " + 
					"	(" + 
					"		coalesce(employee.first_name)|| ' ' || coalesce(employee.middle_name)|| ' ' || coalesce(employee.last_name)" + 
					"	) as \"employeeName\"," + 
					"	(" + 
					"		'Dr. ' || coalesce(doctor.first_name)|| ' ' || coalesce(doctor.middle_name)|| ' ' || coalesce(doctor.last_name)" + 
					"	) as \"doctorName\"" + 
					" from" + 
					"	patient.t_complaint_appo_mapper complaintAppointment" + 
					" inner join emr.m_icpc icpc1_ on" + 
					"	complaintAppointment.diagnosis_id = icpc1_.icpc_id" + 
					" left  join emr.m_icpc_system icpcsystem2_ on" + 
					"	complaintAppointment.icpc_system_id = icpcsystem2_.icpc_system_id" + 
					"	" + 
					" left  join public.m_user_master um on" + 
					"	complaintAppointment.created_by = um.user_id" + 
					" left  join public.m_employee_master employee on" + 
					"	um.user_id = employee.user_id" + 
					" left  join doctor.m_doctor_master doctor on" + 
					"	um.user_id = doctor.user_id "
					+ " inner join opd.t_encounter_master entMst on" + 
					"	complaintAppointment.encounter_id = entMst.encounter_id "
					+ " left  join public.m_visit_type_master vm on entMst.visit_type_id = vm.visit_type_id" + 
					" where" + 
					"	complaintAppointment.patient_id =:patientId" + 
					"	and " + 
					"	complaintAppointment.status = 'A'" + 
					"	and complaintAppointment.is_resolved ='N'"+
					"	and complaintAppointment.organization_id =:organizationId" + 
					"	and(complaintAppointment.is_complaint_stop = 'N'" +
					"	)"
				 
			),
	
	
	@NamedNativeQuery(name="GET_COMPLAINT_APPOINTMENT_MAPPER_BY_COMPLAINT_ID",
	query="SELECT comApp.complaint_appo_id as \"complaintAppoId\",  comApp.encounter_id as \"encounterId\", comApp.diagnosis_id as \"diagnosisId\" ,"
			+ " comApp.since as \"since\", comApp.duration as \"duration\", comApp.remark as \"remark\","
			+ "comApp.status as \"status\", comApp.role_id as \"roleId\", "
//			+ " diagnosisMaster.icpc_desc as  \"diagnosisName\", "
			+ " comApp.is_reviewed_flag as \"isReviewedFlag\"  "
			+ " FROM patient.t_complaint_appo_mapper comApp "
			+ " INNER JOIN emr.m_icpc diagnosisMaster on comApp.diagnosis_id=diagnosisMaster.icpc_id "
			+ " WHERE comApp.complaint_appo_id  =:complaintAppoId "
	),
	@NamedNativeQuery(name="GET_PAST_COMPLAINT_APPOINTMENT_BY_PATIENT_ID",
	query="select" + 
			"	complaintAdmission.complaint_admission_id as \"complaintAdmissionId\"," + 
			"	complaintAdmission.admission_id as \"admissionId\"," + 
			"	complaintAdmission.diagnosis_id as \"diagnosisId\"," + 
			"	complaintAdmission.resolved_remark as \"resolvedRemark\"," + 
			"	(" + 
			"		select" + 
			"			max( templateTransaction.m_icpc_id )" + 
			"		from" + 
			"			public.t_template_transaction templateTransaction" + 
			"		where" + 
			"			templateTransaction.m_icpc_id = complaintAdmission.diagnosis_id" + 
			"			and complaintAdmission.admission_id = templateTransaction.admission_id" + 
			"		group by" + 
			"			templateTransaction.m_icpc_id" + 
			"	) as \"mIcpcId1\"," + 
			"	(" + 
			"		select" + 
			"			count( templateTransaction1.m_icpc_id )" + 
			"		from" + 
			"			public.t_template_transaction templateTransaction1" + 
			"		where" + 
			"			templateTransaction1.m_icpc_id = complaintAdmission.diagnosis_id" + 
			"			and complaintAdmission.admission_id = templateTransaction1.admission_id" + 
			"		group by" + 
			"			templateTransaction1.m_icpc_id" + 
			"	) as \"mIcpcId\"," + 
			"	icpc1_.icpc_desc as \"diagnosisName\"," + 
			"	icpc1_.icpc_code as \"icpcCode\"," + 
			"	complaintAdmission.since as \"since\"," + 
			"	complaintAdmission.duration as \"duration\"," + 
			"	complaintAdmission.remark as \"remark\"," + 
			"		to_char(" + 
			"		complaintAdmission.created_date," + 
			"		'dd/mm/yyyy'" + 
			"	) as \"createdDate\"," + 
			"		to_char(" + 
			"		complaintAdmission.resolved_date," + 
			"		'dd/mm/yyyy'" + 
			"	) as \"resolvedDate\"," +
			"	complaintAdmission.status as \"status\"," + 
			"	complaintAdmission.role_id as \"roleId\"," + 
			"	complaintAdmission.icpc_code_text as \"icpcCodeText\"," + 
			"	icpc1_.icpc_system_id as \"icpcSystemId\"," + 
			"		complaintAdmission.is_reviewed_flag as \"isReviewedFlag\"," + 
			"	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\"," + 
			"		(" + 
			"		coalesce(employee.first_name)|| ' ' || coalesce(employee.middle_name)|| ' ' || coalesce(employee.last_name)" + 
			"	) as \"employeeName\"," + 
			"	(" + 
			"		'Dr. ' || coalesce(doctor.first_name)|| ' ' || coalesce(doctor.middle_name)|| ' ' || coalesce(doctor.last_name)" + 
			"	) as \"doctorName\"" + 
			" from" + 
			"	adt.t_complaint_admission complaintAdmission" + 
			" inner join emr.m_icpc icpc1_ on" + 
			"	complaintAdmission.diagnosis_id = icpc1_.icpc_id" + 
			" left  join emr.m_icpc_system icpcsystem2_ on" + 
			"	complaintAdmission.icpc_system_id = icpcsystem2_.icpc_system_id" + 
			" left  join public.m_user_master um on" + 
			"	complaintAdmission.created_by = um.user_id" + 
			" left  join public.m_employee_master employee on" + 
			"	um.user_id = employee.user_id" + 
			" left  join doctor.m_doctor_master doctor on" + 
			"	um.user_id = doctor.user_id" + 
			" where" + 
			"	complaintAdmission.patient_id =:patientId" + 
			"	and " + 
			"	complaintAdmission.status = 'A'" + 
			"	and complaintAdmission.is_resolved = 'Y'" + 
			"	and complaintAdmission.is_complaint_stop = 'N'" + 
			"	and complaintAdmission.organization_id =:organizationId" + 
			" union" + 
			" select" + 
			"	complaintAppointment.complaint_appo_id as \"complaintAppoId\"," + 
			"	complaintAppointment.encounter_id as \"encounterId\"," + 
			"	complaintAppointment.diagnosis_id as \"diagnosisId\"," + 
			"	complaintAppointment.resolved_remark as \"resolvedRemark\"," + 

			"	(" + 
			"		select" + 
			"			max( templateTransaction.m_icpc_id )" + 
			"		from" + 
			"			public.t_template_transaction templateTransaction" + 
			"		where" + 
			"			templateTransaction.m_icpc_id = complaintAppointment.diagnosis_id" + 
			"			and complaintAppointment.encounter_id = templateTransaction.encounter_id" + 
			"		group by" + 
			"			templateTransaction.m_icpc_id" + 
			"	) as \"mIcpcId1\"," + 
			"	(" + 
			"		select" + 
			"			count( templateTransaction1.m_icpc_id )" + 
			"		from" + 
			"			public.t_template_transaction templateTransaction1" + 
			"		where" + 
			"			templateTransaction1.m_icpc_id = complaintAppointment.diagnosis_id" + 
			"			and complaintAppointment.encounter_id = templateTransaction1.encounter_id" + 
			"		group by" + 
			"			templateTransaction1.m_icpc_id" + 
			"	) as \"mIcpcId\"," + 
			"	icpc1_.icpc_desc as \"diagnosisName\"," + 
			"	icpc1_.icpc_code as \"icpcCode\"," + 
			"	complaintAppointment.since as \"since\"," + 
			"	complaintAppointment.duration as \"duration\"," + 
			"	complaintAppointment.remark as \"remark\"," + 
			"	to_char(" + 
			"		complaintAppointment.cerated_date," + 
			"		'dd/mm/yyyy'" + 
			"	) as \"createdDate\"," + 
			"	to_char(" + 
			"		complaintAppointment.resolved_date," + 
			"		'dd/mm/yyyy'" + 
			"	) as \"resolvedDate\"," + 
			"	complaintAppointment.status as \"status\"," + 
			"	complaintAppointment.role_id as \"roleId\"," + 
			"	complaintAppointment.icpc_code_text as \"icpcCodeText\"," + 
			"	icpc1_.icpc_system_id as \"icpcSystemId\"," + 
			"	complaintAppointment.is_reviewed_flag as \"isReviewedFlag\"," + 
			"	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\"," + 
			"	(" + 
			"		coalesce(employee.first_name)|| ' ' || coalesce(employee.middle_name)|| ' ' || coalesce(employee.last_name)" + 
			"	) as \"employeeName\"," + 
			"	(" + 
			"		'Dr. ' || coalesce(doctor.first_name)|| ' ' || coalesce(doctor.middle_name)|| ' ' || coalesce(doctor.last_name)" + 
			"	) as \"doctorName\"" + 
			" from" + 
			"	patient.t_complaint_appo_mapper complaintAppointment" + 
			" inner join emr.m_icpc icpc1_ on" + 
			"	complaintAppointment.diagnosis_id = icpc1_.icpc_id" + 
			" left  join emr.m_icpc_system icpcsystem2_ on" + 
			"	complaintAppointment.icpc_system_id = icpcsystem2_.icpc_system_id" + 
			"	" + 
			" left  join public.m_user_master um on" + 
			"	complaintAppointment.created_by = um.user_id" + 
			" left  join public.m_employee_master employee on" + 
			"	um.user_id = employee.user_id" + 
			" left  join doctor.m_doctor_master doctor on" + 
			"	um.user_id = doctor.user_id" + 
			" where" + 
			"	complaintAppointment.patient_id =:patientId" + 
			"	and " + 
			"	complaintAppointment.status = 'A'" + 
			"	and complaintAppointment.is_resolved = 'Y'" + 
			"	and complaintAppointment.organization_id =:organizationId" + 
			"	and(complaintAppointment.is_complaint_stop = 'N'" + 
			"	)"  
	),
	
	@NamedNativeQuery(name="GET_COMPLAINTS_BY_PATIENT_ID_FOR_BOOK_VIEW",
	query="select" + 
			"	complaintAdmission.complaint_admission_id as \"complaintAdmissionId\"," + 
			"	complaintAdmission.admission_id as \"admissionId\"," + 
			"	complaintAdmission.diagnosis_id as \"diagnosisId\"," + 
			"	complaintAdmission.resolved_remark as \"resolvedRemark\"," + 
			"	(" + 
			"		select" + 
			"			max( templateTransaction.m_icpc_id )" + 
			"		from" + 
			"			public.t_template_transaction templateTransaction" + 
			"		where" + 
			"			templateTransaction.m_icpc_id = complaintAdmission.diagnosis_id" + 
			"			and complaintAdmission.admission_id = templateTransaction.admission_id" + 
			"		group by" + 
			"			templateTransaction.m_icpc_id" + 
			"	) as \"mIcpcId1\"," + 
			"	(" + 
			"		select" + 
			"			count( templateTransaction1.m_icpc_id )" + 
			"		from" + 
			"			public.t_template_transaction templateTransaction1" + 
			"		where" + 
			"			templateTransaction1.m_icpc_id = complaintAdmission.diagnosis_id"
			+ "         and templateTransaction1.status = 'A'" + 
			"			and complaintAdmission.admission_id = templateTransaction1.admission_id" + 
			"		group by" + 
			"			templateTransaction1.m_icpc_id" + 
			"	) as \"complaintTempCountBook\"," + 
			"	icpc1_.icpc_desc as \"diagnosisName\"," + 
			"	icpc1_.icpc_code as \"icpcCode\"," + 
			"	complaintAdmission.since as \"since\"," + 
			"	complaintAdmission.duration as \"duration\"," + 
			"	complaintAdmission.remark as \"remark\"," + 
			"		to_char(" + 
			"		complaintAdmission.created_date," + 
			"		'dd/mm/yyyy'" + 
			"	) as \"createdDate\"," + 
			"		to_char(" + 
			"		complaintAdmission.resolved_date," + 
			"		'dd/mm/yyyy'" + 
			"	) as \"resolvedDate\"," +
			"	complaintAdmission.status as \"status\"," + 
			"	complaintAdmission.role_id as \"roleId\"," + 
			"	complaintAdmission.icpc_code_text as \"icpcCodeText\"," + 
			"	icpc1_.icpc_system_id as \"icpcSystemId\"," + 
			"		complaintAdmission.is_reviewed_flag as \"isReviewedFlag\"," + 
			"	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\"," + 
			"		(" + 
			"		coalesce(employee.first_name)|| ' ' || coalesce(employee.middle_name)|| ' ' || coalesce(employee.last_name)" + 
			"	) as \"employeeName\"," + 
			"	(" + 
			"		'Dr. ' || coalesce(doctor.first_name)|| ' ' || coalesce(doctor.middle_name)|| ' ' || coalesce(doctor.last_name)" + 
			"	) as \"doctorName\"" + 
			" from" + 
			"	adt.t_complaint_admission complaintAdmission" + 
			" inner join emr.m_icpc icpc1_ on" + 
			"	complaintAdmission.diagnosis_id = icpc1_.icpc_id" + 
			" left  join emr.m_icpc_system icpcsystem2_ on" + 
			"	complaintAdmission.icpc_system_id = icpcsystem2_.icpc_system_id" + 
			" left  join public.m_user_master um on" + 
			"	complaintAdmission.created_by = um.user_id" + 
			" left  join public.m_employee_master employee on" + 
			"	um.user_id = employee.user_id" + 
			" left  join doctor.m_doctor_master doctor on" + 
			"	um.user_id = doctor.user_id" + 
			" where" + 
			"	complaintAdmission.patient_id =:patientId" + 
			"	and " + 
			"	complaintAdmission.status = 'A'" + 
			"	and complaintAdmission.is_complaint_stop = 'N'" + 
			" and complaintAdmission.is_resolved = 'N'"+
			"	and complaintAdmission.organization_id =:organizationId" 
			+ " AND to_char(complaintAdmission.created_date , 'YYYY-MM-DD') BETWEEN :fromDate " 
			+ " AND :toDate "+
			" union" + 
			" select" + 
			"	complaintAppointment.complaint_appo_id as \"complaintAppoId\"," + 
			"	complaintAppointment.encounter_id as \"encounterId\"," + 
			"	complaintAppointment.diagnosis_id as \"diagnosisId\"," + 
			"	complaintAppointment.resolved_remark as \"resolvedRemark\"," + 

			"	(" + 
			"		select" + 
			"			max( templateTransaction.m_icpc_id )" + 
			"		from" + 
			"			public.t_template_transaction templateTransaction" + 
			"		where" + 
			"			templateTransaction.m_icpc_id = complaintAppointment.diagnosis_id" + 
			"			and complaintAppointment.encounter_id = templateTransaction.encounter_id" + 
			"		group by" + 
			"			templateTransaction.m_icpc_id" + 
			"	) as \"mIcpcId1\"," + 
			"	(" + 
			"		select" + 
			"			count( templateTransaction1.m_icpc_id )" + 
			"		from" + 
			"			public.t_template_transaction templateTransaction1" + 
			"		where" + 
			"			templateTransaction1.m_icpc_id = complaintAppointment.diagnosis_id" + 
			"			and complaintAppointment.encounter_id = templateTransaction1.encounter_id "
			+ "         and templateTransaction1.status = 'A'" + 
			"		group by" + 
			"			templateTransaction1.m_icpc_id" + 
			"	) as \"complaintTempCountBook\"," + 
			"	icpc1_.icpc_desc as \"diagnosisName\"," + 
			"	icpc1_.icpc_code as \"icpcCode\"," + 
			"	complaintAppointment.since as \"since\"," + 
			"	complaintAppointment.duration as \"duration\"," + 
			"	complaintAppointment.remark as \"remark\"," + 
			"	to_char(" + 
			"		complaintAppointment.cerated_date," + 
			"		'dd/mm/yyyy'" + 
			"	) as \"createdDate\"," + 
			"	to_char(" + 
			"		complaintAppointment.resolved_date," + 
			"		'dd/mm/yyyy'" + 
			"	) as \"resolvedDate\"," + 
			"	complaintAppointment.status as \"status\"," + 
			"	complaintAppointment.role_id as \"roleId\"," + 
			"	complaintAppointment.icpc_code_text as \"icpcCodeText\"," + 
			"	icpc1_.icpc_system_id as \"icpcSystemId\"," + 
			"	complaintAppointment.is_reviewed_flag as \"isReviewedFlag\"," + 
			"	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\"," + 
			"	(" + 
			"		coalesce(employee.first_name)|| ' ' || coalesce(employee.middle_name)|| ' ' || coalesce(employee.last_name)" + 
			"	) as \"employeeName\"," + 
			"	(" + 
			"		'Dr. ' || coalesce(doctor.first_name)|| ' ' || coalesce(doctor.middle_name)|| ' ' || coalesce(doctor.last_name)" + 
			"	) as \"doctorName\"" + 
			" from" + 
			"	patient.t_complaint_appo_mapper complaintAppointment" + 
			" inner join emr.m_icpc icpc1_ on" + 
			"	complaintAppointment.diagnosis_id = icpc1_.icpc_id" + 
			" left  join emr.m_icpc_system icpcsystem2_ on" + 
			"	complaintAppointment.icpc_system_id = icpcsystem2_.icpc_system_id" + 
			"	" + 
			" left  join public.m_user_master um on" + 
			"	complaintAppointment.created_by = um.user_id" + 
			" left  join public.m_employee_master employee on" + 
			"	um.user_id = employee.user_id" + 
			" left  join doctor.m_doctor_master doctor on" + 
			"	um.user_id = doctor.user_id" + 
			" where" + 
			"	complaintAppointment.patient_id =:patientId" + 
			"	and " + 
			"	complaintAppointment.status = 'A'" + 
			"	and complaintAppointment.organization_id =:organizationId" + 
			"	and(complaintAppointment.is_complaint_stop = 'N'" +
			" and complaintAppointment.is_resolved = 'N'" 
			+ " AND to_char(complaintAppointment.cerated_date , 'YYYY-MM-DD') BETWEEN :fromDate " 
			+ " AND :toDate "+
			"	)"  ),
	@NamedNativeQuery(name="GET_PAST_COMPLAINT_APPOINTMENT_FOR_BOOK_VIEW",
	query="select" + 
			"	complaintAdmission.complaint_admission_id as \"complaintAdmissionId\"," + 
			"	complaintAdmission.admission_id as \"admissionId\"," + 
			"	complaintAdmission.diagnosis_id as \"diagnosisId\"," + 
			"	complaintAdmission.resolved_remark as \"resolvedRemark\"," + 
			"	(" + 
			"		select" + 
			"			max( templateTransaction.m_icpc_id )" + 
			"		from" + 
			"			public.t_template_transaction templateTransaction" + 
			"		where" + 
			"			templateTransaction.m_icpc_id = complaintAdmission.diagnosis_id" + 
			"			and complaintAdmission.admission_id = templateTransaction.admission_id" + 
			"		group by" + 
			"			templateTransaction.m_icpc_id" + 
			"	) as \"mIcpcId1\"," + 
			"	(" + 
			"		select" + 
			"			count( templateTransaction1.m_icpc_id )" + 
			"		from" + 
			"			public.t_template_transaction templateTransaction1" + 
			"		where" + 
			"			templateTransaction1.m_icpc_id = complaintAdmission.diagnosis_id "
			+ "         and templateTransaction1.status = 'A'" + 
			"			and complaintAdmission.admission_id = templateTransaction1.admission_id" + 
			"		group by" + 
			"			templateTransaction1.m_icpc_id" + 
			"	) as \"complaintTempCountBook\"," + 
			"	icpc1_.icpc_desc as \"diagnosisName\"," + 
			"	icpc1_.icpc_code as \"icpcCode\"," + 
			"	complaintAdmission.since as \"since\"," + 
			"	complaintAdmission.duration as \"duration\"," + 
			"	complaintAdmission.remark as \"remark\"," + 
			"		to_char(" + 
			"		complaintAdmission.created_date," + 
			"		'dd/mm/yyyy'" + 
			"	) as \"createdDate\"," + 
			"		to_char(" + 
			"		complaintAdmission.resolved_date," + 
			"		'dd/mm/yyyy'" + 
			"	) as \"resolvedDate\"," +
			"	complaintAdmission.status as \"status\"," + 
			"	complaintAdmission.role_id as \"roleId\"," + 
			"	complaintAdmission.icpc_code_text as \"icpcCodeText\"," + 
			"	icpc1_.icpc_system_id as \"icpcSystemId\"," + 
			"		complaintAdmission.is_reviewed_flag as \"isReviewedFlag\"," + 
			"	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\"," + 
			"		(" + 
			"		coalesce(employee.first_name)|| ' ' || coalesce(employee.middle_name)|| ' ' || coalesce(employee.last_name)" + 
			"	) as \"employeeName\"," + 
			"	(" + 
			"		'Dr. ' || coalesce(doctor.first_name)|| ' ' || coalesce(doctor.middle_name)|| ' ' || coalesce(doctor.last_name)" + 
			"	) as \"doctorName\"" + 
			" from" + 
			"	adt.t_complaint_admission complaintAdmission" + 
			" inner join emr.m_icpc icpc1_ on" + 
			"	complaintAdmission.diagnosis_id = icpc1_.icpc_id" + 
			" left  join emr.m_icpc_system icpcsystem2_ on" + 
			"	complaintAdmission.icpc_system_id = icpcsystem2_.icpc_system_id" + 
			" left  join public.m_user_master um on" + 
			"	complaintAdmission.created_by = um.user_id" + 
			" left  join public.m_employee_master employee on" + 
			"	um.user_id = employee.user_id" + 
			" left  join doctor.m_doctor_master doctor on" + 
			"	um.user_id = doctor.user_id" + 
			" where" + 
			"	complaintAdmission.patient_id =:patientId" + 
			"	and " + 
			"	complaintAdmission.status = 'A'" + 
			" and complaintAdmission.is_resolved = 'Y'" + 
			"	and complaintAdmission.is_complaint_stop = 'N'" + 
			"	and complaintAdmission.organization_id =:organizationId" 
			+ " AND to_char(complaintAdmission.created_date , 'YYYY-MM-DD') BETWEEN :fromDate " 
			+ " AND :toDate "+
			" union" + 
			" select" + 
			"	complaintAppointment.complaint_appo_id as \"complaintAppoId\"," + 
			"	complaintAppointment.encounter_id as \"encounterId\"," + 
			"	complaintAppointment.diagnosis_id as \"diagnosisId\"," + 
			"	complaintAppointment.resolved_remark as \"resolvedRemark\"," + 

			"	(" + 
			"		select" + 
			"			max( templateTransaction.m_icpc_id )" + 
			"		from" + 
			"			public.t_template_transaction templateTransaction" + 
			"		where" + 
			"			templateTransaction.m_icpc_id = complaintAppointment.diagnosis_id" + 
			"			and complaintAppointment.encounter_id = templateTransaction.encounter_id" + 
			"		group by" + 
			"			templateTransaction.m_icpc_id" + 
			"	) as \"mIcpcId1\"," + 
			"	(" + 
			"		select" + 
			"			count( templateTransaction1.m_icpc_id )" + 
			"		from" + 
			"			public.t_template_transaction templateTransaction1" + 
			"		where" + 
			"			templateTransaction1.m_icpc_id = complaintAppointment.diagnosis_id "
			+ "          and templateTransaction1.status = 'A'" + 
			"			and complaintAppointment.encounter_id = templateTransaction1.encounter_id" + 
			"		group by" + 
			"			templateTransaction1.m_icpc_id" + 
			"	) as \"complaintTempCountBook\"," + 
			"	icpc1_.icpc_desc as \"diagnosisName\"," + 
			"	icpc1_.icpc_code as \"icpcCode\"," + 
			"	complaintAppointment.since as \"since\"," + 
			"	complaintAppointment.duration as \"duration\"," + 
			"	complaintAppointment.remark as \"remark\"," + 
			"	to_char(" + 
			"		complaintAppointment.cerated_date," + 
			"		'dd/mm/yyyy'" + 
			"	) as \"createdDate\"," + 
			"	to_char(" + 
			"		complaintAppointment.resolved_date," + 
			"		'dd/mm/yyyy'" + 
			"	) as \"resolvedDate\"," + 
			"	complaintAppointment.status as \"status\"," + 
			"	complaintAppointment.role_id as \"roleId\"," + 
			"	complaintAppointment.icpc_code_text as \"icpcCodeText\"," + 
			"	icpc1_.icpc_system_id as \"icpcSystemId\"," + 
			"	complaintAppointment.is_reviewed_flag as \"isReviewedFlag\"," + 
			"	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\"," + 
			"	(" + 
			"		coalesce(employee.first_name)|| ' ' || coalesce(employee.middle_name)|| ' ' || coalesce(employee.last_name)" + 
			"	) as \"employeeName\"," + 
			"	(" + 
			"		'Dr. ' || coalesce(doctor.first_name)|| ' ' || coalesce(doctor.middle_name)|| ' ' || coalesce(doctor.last_name)" + 
			"	) as \"doctorName\"" + 
			" from" + 
			"	patient.t_complaint_appo_mapper complaintAppointment" + 
			" inner join emr.m_icpc icpc1_ on" + 
			"	complaintAppointment.diagnosis_id = icpc1_.icpc_id" + 
			" left  join emr.m_icpc_system icpcsystem2_ on" + 
			"	complaintAppointment.icpc_system_id = icpcsystem2_.icpc_system_id" + 
			"	" + 
			" left  join public.m_user_master um on" + 
			"	complaintAppointment.created_by = um.user_id" + 
			" left  join public.m_employee_master employee on" + 
			"	um.user_id = employee.user_id" + 
			" left  join doctor.m_doctor_master doctor on" + 
			"	um.user_id = doctor.user_id" + 
			" where" + 
			"	complaintAppointment.patient_id =:patientId" + 
			"	and " + 
			"	complaintAppointment.status = 'A'" + 
			"	and complaintAppointment.organization_id =:organizationId" + 
			"	and complaintAppointment.is_resolved = 'Y'" + 
			"	and complaintAppointment.is_complaint_stop = 'N'" 
			+ " AND to_char(complaintAppointment.cerated_date , 'YYYY-MM-DD') BETWEEN :fromDate " 
			+ " AND :toDate "
		  ),
	
	@NamedNativeQuery(name="GET_MOST_SAVED_COMPLAINT",
	query=/*"select" + 
			" mppr.diagnosis_id as \"icpcId\"," + 
			" mi.icpc_system_id as \"icpcSystemId\"," + 
			" mi.icpc_desc as \"icpcDesc\"," + 
			" mi.icpc_code as \"icpcCode\"," + 
			" mis.icpc_system_desc as \"icpcSystemDesc\"," + 
			" mis.icpc_system_code as \"icpcSystemCode\"," + 
			" count(*)" + 
			" from patient.t_complaint_appo_mapper mppr" + 
			" inner" + 
			" join emr.m_icpc mi on" + 
			" mi.icpc_id = mppr.diagnosis_id" + 
			" inner" + 
			" join emr.m_icpc_system mis on" + 
			" mis.icpc_system_id = mi.icpc_system_id" + 
			" where mppr.organization_id =:organizationId" + 
			" and mppr.unit_id =:unitId" + 
			" and mppr.status ='A'" + 
			" group by mppr.diagnosis_id ," + 
			" mi.icpc_desc," + 
			" mi.icpc_code," + 
			" mi.icpc_system_id," + 
			" mis.icpc_system_desc," + 
			" mis.icpc_system_code" + 
			" order by count(*) desc" + 
			" limit 10;"*/
			"select " + 
			"	distinct(mppr.diagnosis_id) as \"icpcId\", " + 
			"	mi.icpc_system_id as \"icpcSystemId\", " + 
			"	mi.icpc_desc as \"icpcDesc\", " + 
			"	mi.icpc_code as \"icpcCode\", " + 
			"	mis.icpc_system_desc as \"icpcSystemDesc\", " + 
			"	mis.icpc_system_code as \"icpcSystemCode\", " + 
			//"	mppr.icpc_code_text as \"icpcCodeText\", " + 
			"	count(*) " + 
			"from " + 
			"	patient.t_complaint_appo_mapper mppr " + 
			"inner join emr.m_icpc mi on " + 
			"	mi.icpc_id = mppr.diagnosis_id " + 
			"inner join emr.m_icpc_system mis on " + 
			"	mis.icpc_system_id = mi.icpc_system_id " + 
			" where mppr.organization_id =:organizationId" + 
			" and mppr.unit_id =:unitId" + 
			"	and mppr.status = 'A' "
			+ " and (mppr.diagnosis_id is not null or mppr.icpc_code_text is not null)" + 
			"group by " + 
			"	mppr.diagnosis_id , " + 
		//	"	mppr.icpc_code_text ," + 
			"	mi.icpc_desc, " + 
			"	mi.icpc_code, " + 
			"	mi.icpc_system_id, " + 
			"	mis.icpc_system_desc, " + 
			"	mis.icpc_system_code " + 
			
			" order by " + 
			"	count(*) desc " + 
			"limit 10; " + 
			" " + 
			""
		  )

	
})



@Entity
@Table(name="t_complaint_appo_mapper", schema="patient")
@SequenceGenerator(name = "t_complaint_appo_mapper_seq", sequenceName = "patient.t_complaint_appo_mapper_seq", allocationSize = 1)
public class ComplaintAppointmentMapper {

	
	@Id
	@Column(name="complaint_appo_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_complaint_appo_mapper_seq")
	private Integer complaintAppoId;
	
	/*@Column(name="complaint_id")
	private int complaintId;*/
	
	@Column(name="is_reviewed_flag")
	private char isReviewedFlag;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="role_id")
	private Integer roleId;
	
	@Column(name="diagnosis_id")
	private Integer diagnosisId;
	
	@Column(name="icpc_system_id")
	private Integer icpcSystemId;
	
	@Column(name="since")
	private Integer since;
	
	@Column(name="duration")
	private String duration;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="status")
	private char status;
	
	@Column(name="created_by")
	private Integer created_by;
	
	@Column(name="cerated_date")
	private Date cerated_date;
	
	@Column(name="updated_by")
	private Integer updated_by;
	
	@Column(name="updated_date")
	private Date updated_date;
	
	@Column(name="icpc_code_text")
	private String icpcCodeText;
	
	@Column(name="justification_complaint")
	private String justificationComplaint;
	
	@Column(name="is_complaint_stop")
	private char isComplaintStop;
	
	@Column(name="patient_id")
	private Integer patientId;

	@Column(name="resolved_remark")
	private String resolvedRemark;
	
	@Column(name="is_resolved")
	private Character isResolved;
	
	@Column(name="resolved_date")
	private Date resolvedDate;
	
	@Column(name="cov_template_id")
	private Integer covTemplateId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id", insertable = false, nullable = false, updatable = false)
	private EncounterMaster encounterMaster;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diagnosis_id", insertable = false, nullable = false, updatable = false)
	private Icpc diagnosisMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "icpc_system_id", insertable = false, nullable = false, updatable = false)
	private IcpcSystemMaster icpcSystemMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, nullable = false, updatable = false)
	private EmployeeMaster employeeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, nullable = false, updatable = false)
	private DoctorMaster doctorMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="created_by", insertable=false, updatable=false)
	private UserMaster userMaster;
	
	public Icpc getDiagnosisMaster() {
		return diagnosisMaster;
	}

	public void setDiagnosisMaster(Icpc diagnosisMaster) {
		this.diagnosisMaster = diagnosisMaster;
	}

	public char getIsReviewedFlag() {
		return isReviewedFlag;
	}

	public void setIsReviewedFlag(char isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
	}

	public void setComplaintAppoId(Integer complaintAppoId) {
		this.complaintAppoId = complaintAppoId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

/*	public int getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(int complaintId) {
		this.complaintId = complaintId;
	}
*/
	
	public EncounterMaster getEncounterMaster() {
		return encounterMaster;
	}

	public void setEncounterMaster(EncounterMaster encounterMaster) {
		this.encounterMaster = encounterMaster;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
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

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getSince() {
		return since;
	}

	public void setSince(Integer since) {
		this.since = since;
	}

	

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Date getCerated_date() {
		return cerated_date;
	}

	public void setCerated_date(Date cerated_date) {
		this.cerated_date = cerated_date;
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

	public int getComplaintAppoId() {
		return complaintAppoId;
	}

	public void setComplaintAppoId(int complaintAppoId) {
		this.complaintAppoId = complaintAppoId;
	}

	public Integer getIcpcSystemId() {
		return icpcSystemId;
	}

	public void setIcpcSystemId(Integer icpcSystemId) {
		this.icpcSystemId = icpcSystemId;
	}

	public IcpcSystemMaster getIcpcSystemMaster() {
		return icpcSystemMaster;
	}

	public void setIcpcSystemMaster(IcpcSystemMaster icpcSystemMaster) {
		this.icpcSystemMaster = icpcSystemMaster;
	}

	public String getIcpcCodeText() {
		return icpcCodeText;
	}

	public void setIcpcCodeText(String icpcCodeText) {
		this.icpcCodeText = icpcCodeText;
	}

	public String getJustificationComplaint() {
		return justificationComplaint;
	}

	public void setJustificationComplaint(String justificationComplaint) {
		this.justificationComplaint = justificationComplaint;
	}

	public char getIsComplaintStop() {
		return isComplaintStop;
	}

	public void setIsComplaintStop(char isComplaintStop) {
		this.isComplaintStop = isComplaintStop;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getResolvedRemark() {
		return resolvedRemark;
	}

	public void setResolvedRemark(String resolvedRemark) {
		this.resolvedRemark = resolvedRemark;
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

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	
	
	
	
}

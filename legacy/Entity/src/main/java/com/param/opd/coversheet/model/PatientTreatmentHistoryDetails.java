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

import com.param.adt.master.global.model.UserMaster;
@NamedQueries({
	
	@NamedQuery(name="GET_PATIENT_TREATMENT_HISTORY",
			query=" SELECT 		patientTreatmentHist.treatmentHistoryId as treatmentHistoryId, patientTreatmentHist.remark as remark,"
					+ "				patientTreatmentHist.isNoSignificantStatus as isNoSignificantStatus, patientTreatmentHist.month as month, "
					+ " 			patientTreatmentHist.year as year, patientTreatmentHist.surgenName as surgenName,"
					+ "				patientTreatmentHist.status as status, patientTreatmentHist.encounterId as encounterId, patientTreatmentHist.patientId as patientId,"
					+ "				patientTreatmentHist.roleId as roleId, patientTreatmentHist.isReviewedFlag as isReviewedFlag, "
					+ "				patientTreatmentHist.isEnterInErrorStatus as isEnterInErrorStatus, "
					+ "				patientTreatmentHist.organizationId as organizationId, patientTreatmentHist.unitId as unitId, patientTreatmentHist.createdBy as createdBy,"
					+ "             patientTreatmentHist.updatedBy as updatedBy, to_char(patientTreatmentHist.treatDate,'DD/MM/YYYY') as treatDate,patientTreatmentHist.treatDate as treatDateNew, "
					+ " (case "
					+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
					+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
					+ " else 'Admin' end) as userName,"
					+ "				to_char(patientTreatmentHist.createdDate,'DD/MM/YYYY') as createdDate,patientTreatmentHist.createdDate as createdDateNew, to_char(patientTreatmentHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
					+ "		  		patientTreatmentHist.treatementName as treatementName "
					+ " FROM 		PatientTreatmentHistoryDetails patientTreatmentHist "
					+ " LEFT JOIN patientTreatmentHist.userMaster um "
					+ " LEFT JOIN um.employeeMastersList empmst "
					+ " LEFT JOIN um.doctorMastersList docmst "
					+ " WHERE 		patientTreatmentHist.patientId=:patientId "
					+ " AND			patientTreatmentHist.status = 'A'"
//					+ " AND			patientTreatmentHist.unitId=:unitId"
					+ " AND 		patientTreatmentHist.organizationId=:organizationId"
					+ " AND			to_char(patientTreatmentHist.createdDate, 'DD-MM-YYYY') != :created_date"
					+ " ORDER BY 	patientTreatmentHist.treatmentHistoryId asc"
			
			),
			
	@NamedQuery(name="GET_PATIENT_TREATMENT_HISTORY_BY_ROLE_ID",
			query=" SELECT 		patientTreatmentHist.treatmentHistoryId as treatmentHistoryId, patientTreatmentHist.remark as remark,"
				+ "				patientTreatmentHist.isNoSignificantStatus as isNoSignificantStatus, patientTreatmentHist.month as month, "
				+ " 			patientTreatmentHist.year as year, patientTreatmentHist.surgenName as surgenName,"
				+ "				patientTreatmentHist.status as status, patientTreatmentHist.encounterId as encounterId, patientTreatmentHist.patientId as patientId,"
				+ "				patientTreatmentHist.roleId as roleId, patientTreatmentHist.isReviewedFlag as isReviewedFlag, "
				+ "				patientTreatmentHist.isEnterInErrorStatus as isEnterInErrorStatus, "
				+ "				patientTreatmentHist.organizationId as organizationId, patientTreatmentHist.unitId as unitId, patientTreatmentHist.createdBy as createdBy,"
				+ "             patientTreatmentHist.updatedBy as updatedBy, to_char(patientTreatmentHist.treatDate,'DD/MM/YYYY') as treatDate, "
				+ "				to_char(patientTreatmentHist.createdDate,'DD/MM/YYYY') as createdDate, to_char(patientTreatmentHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
				+ "		  		patientTreatmentHist.treatementName as treatementName "
				+ " FROM 		PatientTreatmentHistoryDetails patientTreatmentHist "
				+ " WHERE 		patientTreatmentHist.patientId=:patientId "
				+ " AND			patientTreatmentHist.status = 'A'"
				+ " AND			patientTreatmentHist.unitId=:unitId"
				+ " AND 		patientTreatmentHist.roleId =:roleId "
				+ " AND 		patientTreatmentHist.organizationId=:organizationId"
		),
	
	@NamedQuery(name="GET_CURRENT_PATIENT_TREATMENT_HISTORY",
	query=" SELECT 		patientTreatmentHist.treatmentHistoryId as treatmentHistoryId, patientTreatmentHist.remark as remark,"
		+ "				patientTreatmentHist.isNoSignificantStatus as isNoSignificantStatus, patientTreatmentHist.month as month,"
		+ " 			patientTreatmentHist.year as year, patientTreatmentHist.surgenName as surgenName,"
		+ "				patientTreatmentHist.status as status, patientTreatmentHist.encounterId as encounterId, patientTreatmentHist.patientId as patientId,"
		+ "				patientTreatmentHist.roleId as roleId, patientTreatmentHist.isReviewedFlag as isReviewedFlag, "
		+ "				patientTreatmentHist.isEnterInErrorStatus as isEnterInErrorStatus, "
		+ "				patientTreatmentHist.organizationId as organizationId, patientTreatmentHist.unitId as unitId, patientTreatmentHist.createdBy as createdBy,"
		+ "             patientTreatmentHist.updatedBy as updatedBy, to_char(patientTreatmentHist.treatDate,'DD/MM/YYYY') as treatDate, "
		+ "				to_char(patientTreatmentHist.createdDate,'DD/MM/YYYY') as createdDate, to_char(patientTreatmentHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
		+ "		  		patientTreatmentHist.treatementName as treatementName "
		+ " FROM 		PatientTreatmentHistoryDetails patientTreatmentHist "
		+ " WHERE 		patientTreatmentHist.patientId=:patientId "
		+ " AND			patientTreatmentHist.status = 'A'"
		+ " AND			patientTreatmentHist.unitId=:unitId"
		+ " AND 		patientTreatmentHist.organizationId=:organizationId "
		+ " ORDER BY 	patientTreatmentHist.treatmentHistoryId asc"
		),
		
		@NamedQuery(name="GET_CURRENT_PATIENT_TREATMENT_HISTORY_BY_ROLE_ID",
		query=" SELECT 		patientTreatmentHist.treatmentHistoryId as treatmentHistoryId, patientTreatmentHist.remark as remark,"
			+ "				patientTreatmentHist.isNoSignificantStatus as isNoSignificantStatus, patientTreatmentHist.month as month, "
			+ " 			patientTreatmentHist.year as year, patientTreatmentHist.surgenName as surgenName,"
			+ "				patientTreatmentHist.status as status, patientTreatmentHist.encounterId as encounterId, patientTreatmentHist.patientId as patientId,"
			+ "				patientTreatmentHist.roleId as roleId, patientTreatmentHist.isReviewedFlag as isReviewedFlag, "
			+ "				patientTreatmentHist.isEnterInErrorStatus as isEnterInErrorStatus, "
			+ "				patientTreatmentHist.organizationId as organizationId, patientTreatmentHist.unitId as unitId, patientTreatmentHist.createdBy as createdBy,"
			+ "             patientTreatmentHist.updatedBy as updatedBy , to_char(patientTreatmentHist.treatDate,'DD/MM/YYYY') as treatDate, "
			+ "				to_char(patientTreatmentHist.createdDate,'DD/MM/YYYY') as createdDate, to_char(patientTreatmentHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
			+ "		  		patientTreatmentHist.treatementName as treatementName "
			+ " FROM 		PatientTreatmentHistoryDetails patientTreatmentHist "
			+ " WHERE 		patientTreatmentHist.patientId=:patientId "
			+ " AND			patientTreatmentHist.status = 'A'"
			+ " AND			patientTreatmentHist.unitId=:unitId"
			+ " AND 		patientTreatmentHist.roleId =:roleId "
			+ " AND 		patientTreatmentHist.organizationId=:organizationId "
			+ " ORDER BY 	patientTreatmentHist.treatmentHistoryId asc"
		),
		
		@NamedQuery(name="GET_CURRENT_PATIENT_TREATMENT_HISTORY_FOR_COVERSHEET",
		query=" SELECT 		patientTreatmentHist.treatmentHistoryId as treatmentHistoryId, patientTreatmentHist.remark as remark,"
			+ "				patientTreatmentHist.isNoSignificantStatus as isNoSignificantStatus, patientTreatmentHist.month as month,"
			+ " 			patientTreatmentHist.year as year, patientTreatmentHist.surgenName as surgenName,"
			+ "				patientTreatmentHist.status as status, patientTreatmentHist.encounterId as encounterId, patientTreatmentHist.patientId as patientId,"
			+ "				patientTreatmentHist.roleId as roleId, patientTreatmentHist.isReviewedFlag as isReviewedFlag, "
			+ "				patientTreatmentHist.isEnterInErrorStatus as isEnterInErrorStatus, patientTreatmentHist.covTemplateId as covTemplateId, "
			+ "				patientTreatmentHist.organizationId as organizationId, patientTreatmentHist.unitId as unitId, patientTreatmentHist.createdBy as createdBy,"
			+ "             patientTreatmentHist.updatedBy as updatedBy , to_char(patientTreatmentHist.treatDate,'DD/MM/YYYY') as treatDate,patientTreatmentHist.treatDate as treatDateNew, "
			+ " (case "
			+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
			+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
			+ " else 'Admin' end) as userName,"
			+ "				to_char(patientTreatmentHist.createdDate,'DD/MM/YYYY') as createdDate,patientTreatmentHist.createdDate as createdDateNew, to_char(patientTreatmentHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
			+ "		  		patientTreatmentHist.treatementName as treatementName "
			+ " FROM 		PatientTreatmentHistoryDetails patientTreatmentHist "
			+ " LEFT JOIN patientTreatmentHist.userMaster um "
			+ " LEFT JOIN um.employeeMastersList empmst "
			+ " LEFT JOIN um.doctorMastersList docmst "
			+ " WHERE 		patientTreatmentHist.patientId=:patientId "
			+ " AND			patientTreatmentHist.status = 'A'"
//			+ " AND			patientTreatmentHist.unitId=:unitId"
			+ " AND 		patientTreatmentHist.organizationId=:organizationId "
			+ " AND			to_char(patientTreatmentHist.createdDate, 'DD-MM-YYYY') = :created_date"
			+ " ORDER BY 	patientTreatmentHist.treatmentHistoryId asc"
			),
		
		@NamedQuery(name="GET_CURRENT_PATIENT_TREATMENT_HISTORY_FOR_COV_TEMPLATE",
		query=" SELECT 		patientTreatmentHist.treatmentHistoryId as treatmentHistoryId, patientTreatmentHist.remark as remark,"
			+ "				patientTreatmentHist.isNoSignificantStatus as isNoSignificantStatus, patientTreatmentHist.month as month,"
			+ " 			patientTreatmentHist.year as year, patientTreatmentHist.surgenName as surgenName,"
			+ "				patientTreatmentHist.status as status, patientTreatmentHist.encounterId as encounterId, patientTreatmentHist.patientId as patientId,"
			+ "				patientTreatmentHist.roleId as roleId, patientTreatmentHist.isReviewedFlag as isReviewedFlag, "
			+ "				patientTreatmentHist.isEnterInErrorStatus as isEnterInErrorStatus, patientTreatmentHist.covTemplateId as covTemplateId, "
			+ "				patientTreatmentHist.organizationId as organizationId, patientTreatmentHist.unitId as unitId, patientTreatmentHist.createdBy as createdBy,"
			+ "             patientTreatmentHist.updatedBy as updatedBy , to_char(patientTreatmentHist.treatDate,'DD/MM/YYYY') as treatDate, "
			+ " (case "
			+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
			+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
			+ " else 'Admin' end) as userName,"
			+ "				to_char(patientTreatmentHist.createdDate,'DD/MM/YYYY') as createdDate, to_char(patientTreatmentHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
			+ "		  		patientTreatmentHist.treatementName as treatementName "
			+ " FROM 		PatientTreatmentHistoryDetails patientTreatmentHist "
			+ " LEFT JOIN patientTreatmentHist.userMaster um "
			+ " LEFT JOIN um.employeeMastersList empmst "
			+ " LEFT JOIN um.doctorMastersList docmst "
			+ " WHERE 		patientTreatmentHist.covTemplateId=:covTemplateId "
			+ " AND			patientTreatmentHist.status = 'A'"
			+ " AND			patientTreatmentHist.unitId=:unitId"
			+ " AND 		patientTreatmentHist.organizationId=:organizationId "
			+ " ORDER BY 	patientTreatmentHist.treatmentHistoryId asc"
			),
		
		
		@NamedQuery(name="GET_PATIENT_TREATMENT_HISTORY_FOR_DISCHARGE_SUMMARY",
		query=" SELECT 		patientTreatmentHist.treatmentHistoryId as treatmentHistoryId, patientTreatmentHist.remark as remark,"
			+ "				patientTreatmentHist.isNoSignificantStatus as isNoSignificantStatus, patientTreatmentHist.month as month,"
			+ " 			patientTreatmentHist.year as year, patientTreatmentHist.surgenName as surgenName,"
			+ "				patientTreatmentHist.status as status, patientTreatmentHist.encounterId as encounterId, patientTreatmentHist.patientId as patientId,"
			+ "				patientTreatmentHist.roleId as roleId, patientTreatmentHist.isReviewedFlag as isReviewedFlag, "
			+ "				patientTreatmentHist.isEnterInErrorStatus as isEnterInErrorStatus, "
			+ "				patientTreatmentHist.organizationId as organizationId, patientTreatmentHist.unitId as unitId, patientTreatmentHist.createdBy as createdBy,"
			+ "             patientTreatmentHist.updatedBy as updatedBy , to_char(patientTreatmentHist.treatDate,'DD/MM/YYYY') as treatDate, "
			+ " (case "
			+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
			+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
			+ " else 'Admin' end) as userName,"
			+ "				to_char(patientTreatmentHist.createdDate,'DD/MM/YYYY') as createdDate, to_char(patientTreatmentHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
			+ "		  		patientTreatmentHist.treatementName as treatementName "
			+ " FROM 		PatientTreatmentHistoryDetails patientTreatmentHist "
			+ " LEFT JOIN patientTreatmentHist.userMaster um "
			+ " LEFT JOIN um.employeeMastersList empmst "
			+ " LEFT JOIN um.doctorMastersList docmst "
			+ " WHERE 		patientTreatmentHist.patientId=:patientId "
			+ " AND			patientTreatmentHist.status = 'A'"
//			+ " AND			patientTreatmentHist.unitId=:unitId"
			+ " AND 		patientTreatmentHist.organizationId=:organizationId "
			+ " ORDER BY 	patientTreatmentHist.treatmentHistoryId asc"
			),
		
		@NamedQuery(name="GET_PATIENT_TREATMENT_HISTORY_FOR_BOOK_VIEW",
		query=" SELECT 		patientTreatmentHist.treatmentHistoryId as treatmentHistoryId, patientTreatmentHist.remark as remark,"
			+ "				patientTreatmentHist.isNoSignificantStatus as isNoSignificantStatus, patientTreatmentHist.month as month,"
			+ " 			patientTreatmentHist.year as year, patientTreatmentHist.surgenName as surgenName,"
			+ "				patientTreatmentHist.status as status, patientTreatmentHist.encounterId as encounterId, patientTreatmentHist.patientId as patientId,"
			+ "				patientTreatmentHist.roleId as roleId, patientTreatmentHist.isReviewedFlag as isReviewedFlag, "
			+ "				patientTreatmentHist.isEnterInErrorStatus as isEnterInErrorStatus, "
			+ "				patientTreatmentHist.organizationId as organizationId, patientTreatmentHist.unitId as unitId, patientTreatmentHist.createdBy as createdBy,"
			+ "             patientTreatmentHist.updatedBy as updatedBy , to_char(patientTreatmentHist.treatDate,'DD/MM/YYYY') as treatDate, "
			+ "				to_char(patientTreatmentHist.createdDate,'DD/MM/YYYY') as createdDate, to_char(patientTreatmentHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
			+ "		  		patientTreatmentHist.treatementName as treatementName "
			+ " FROM 		PatientTreatmentHistoryDetails patientTreatmentHist "
			+ " WHERE 		patientTreatmentHist.patientId=:patientId "
			+ " AND			patientTreatmentHist.status = 'A'"
			+ " AND 		patientTreatmentHist.organizationId=:organizationId "
			+ " AND to_char(patientTreatmentHist.createdDate , 'YYYY-MM-DD')  BETWEEN :fromDate " 
			+ " AND :toDate "
			+ " ORDER BY 	patientTreatmentHist.treatmentHistoryId asc"
			),
		@NamedQuery(name="GET_PATIENT_TREATMENT_HISTORY_FOR_DOCTOR_APP",
		query=" SELECT 		patientTreatmentHist.treatmentHistoryId as treatmentHistoryId, patientTreatmentHist.remark as remark,"
			+ "				patientTreatmentHist.isNoSignificantStatus as isNoSignificantStatus, patientTreatmentHist.month as month,"
			+ " 			patientTreatmentHist.year as year, patientTreatmentHist.surgenName as surgenName,"
			+ "				patientTreatmentHist.status as status, patientTreatmentHist.encounterId as encounterId, patientTreatmentHist.patientId as patientId,"
			+ "				patientTreatmentHist.roleId as roleId, patientTreatmentHist.isReviewedFlag as isReviewedFlag, "
			+ "				patientTreatmentHist.isEnterInErrorStatus as isEnterInErrorStatus, "
			+ "				patientTreatmentHist.organizationId as organizationId, patientTreatmentHist.unitId as unitId, patientTreatmentHist.createdBy as createdBy,"
			+ "             patientTreatmentHist.updatedBy as updatedBy, to_char(patientTreatmentHist.treatDate,'DD/MM/YYYY') as treatDate, "
			+ "				to_char(patientTreatmentHist.createdDate,'DD/MM/YYYY') as createdDate, to_char(patientTreatmentHist.updatedDate,'DD/MM/YYYY') as updatedDate,"
			+ "		  		patientTreatmentHist.treatementName as treatementName "
			+ " FROM 		PatientTreatmentHistoryDetails patientTreatmentHist "
			+ " WHERE 		patientTreatmentHist.patientId=:patientId "
			+ " AND			patientTreatmentHist.status = 'A'"
			//+ " AND			patientTreatmentHist.unitId=:unitId"
			+ " AND 		patientTreatmentHist.organizationId=:organizationId "
			+ " ORDER BY 	patientTreatmentHist.treatmentHistoryId asc"
			)
})
@NamedNativeQueries(
		{
			@NamedNativeQuery(name="GET_COMPLETED_DIAGNOSTICS_PROCEDURES",
					query=" 	select" + 
							" distinct (orderdetails.order_details_id) as \"orderDetailsId\"," + 
							" patientreg.patient_id as \"patientId\"," + 
							" ('Dr. ' || coalesce(doctormstr.first_name)|| ' ' || coalesce(doctormstr.middle_name)|| ' ' || coalesce(doctormstr.last_name)) as \"surgenName\"," + 
							" servicemstr.service_standard_name as \"treatementName\"," + 
							"   to_char(templatetxn.report_release_datetime ,'DD/MM/YYYY') as \"createdDate\"," + 
							"   templatetxn.report_release_datetime  as \"createdDateNew\"," +
							"  to_char(templatetxn.report_release_datetime ,'month') as \"monthProc\"," + 
							"	to_char(templatetxn.report_release_datetime ,'yyyy') as \"yearProc\" " + 
//							"	templatetxn.template_transaction_id as \"template_trCansaction_id\"" + 
							" from" + 
							" public.t_order_details orderdetails inner join public.t_order_master ordermaster on orderdetails.order_id = ordermaster.order_id" + 
							" inner join patient.t_patient_registration patientreg on ordermaster.patient_id = patientreg.patient_id" + 
							" left join billing.t_billing_details billingdtls on orderdetails.order_details_id = billingdtls.order_details_id" + 
							" left join billing.m_billing_master billingmstr on billingdtls.billing_master_id = billingmstr.billing_master_id and billingmstr.bill_cancelled ='N'" + 
							" inner join doctor.m_doctor_master doctormstr on ordermaster.ord_doctor_id = doctormstr.doctor_id" + 
							" left join service.m_service_master servicemstr on orderdetails.service_id = servicemstr.service_master_id" + 
							" left join service.t_unit_service_master_details unitDlts on unitDlts.service_master_id = servicemstr.service_master_id and unitDlts.procedure_type_id = 1" + 
							" and unitDlts.is_procedure = 'Y'" + 
							" left join public.t_diagnostic_procedure_mapper dmppr on dmppr.order_details_id = orderdetails.order_details_id" + 
							" left outer join public.t_template_transaction templatetxn on orderdetails.order_details_id = templatetxn.order_details_id and templatetxn.status = 'A' and templatetxn.is_questionary='N'" + 
							" left outer join public.t_template_master templatemstr on templatetxn.template_id = templatemstr.template_id and (templatemstr.template_type_id = 20)" + 
							" where" + 
							" ordermaster.org_id =:organizationId" + 
							" AND orderdetails.service_rendered = 1" + 
							" and( templatetxn.template_transaction_id is not null and templatetxn.is_final_release='Y')" + 
							" and patientreg.patient_id=:patientId"),	
			
			@NamedNativeQuery(name="GET_COMPLETED_MEDICAL_PROCEDURES",
			query="select" + 
					" (select to_char(pdr.created_date,'DD/MM/YYYY') from patient.t_patient_dialysis_record pdr where pdr.appointment_id=tham.appointment_id" + 
					" order by pdr.patient_dialysis_rec_id desc limit 1) as \"treatDate\"," + 
					" (select pdr.created_date from patient.t_patient_dialysis_record pdr where pdr.appointment_id=tham.appointment_id" + 
					" order by pdr.patient_dialysis_rec_id desc limit 1) as \"treatDateNew\"," + 
					" (select to_char(pdr.created_date,'month') from patient.t_patient_dialysis_record pdr where pdr.appointment_id=tham.appointment_id" + 
					" order by pdr.patient_dialysis_rec_id desc limit 1) as \"monthProc\"," + 
					" (select to_char(pdr.created_date,'yyyy') from patient.t_patient_dialysis_record pdr where pdr.appointment_id=tham.appointment_id" + 
					" order by pdr.patient_dialysis_rec_id desc limit 1)as \"yearProc\"," + 
					" ser.service_standard_name as \"treatementName\"," + 
					" concat('Dr.',' ',coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"surgenName\"" + 
					" from opd.t_hemodialysis_appointment_mapper tham" + 
					" inner join opd.m_appointment_master am on am.appointment_id=tham.appointment_id" + 
					" inner join doctor.m_doctor_master doc on doc.doctor_id=tham.doctor_id" + 
					" inner join public.t_order_details ord on ord.order_details_id=tham.order_details_id" + 
					" inner join service.m_service_master ser on ser.service_master_id=ord.service_id" + 
					" where am.patient_id=:patientId "
					+ " and am.organization_id=:organizationId "
					+ " and tham.dialysis_status_id in (2,4) "),
		})
@Entity
@Table(name="t_treatment_history_details",schema="patient")
@SequenceGenerator(name="t_treatment_history_details_seq" , sequenceName="patient.t_treatment_history_details_seq", allocationSize=1)
public class PatientTreatmentHistoryDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "t_treatment_history_details_seq")
	@Column(name="treatment_history_id")
	private Integer treatmentHistoryId;

	@Column(name="remark")
	private String remark;
	
	@Column(name="is_no_significant_status")
	private Character isNoSignificantStatus;
	
	@Column(name="month")
	private Integer month;
	
	@Column(name="year")
	private Integer year;
	
	@Column(name="treatement_name")
	private String treatementName;
	
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
	
	@Column(name="surgen_name")
	private String surgenName;
	
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

	@Column(name="cov_template_id")
	private Integer covTemplateId;
	
	@Column(name = "treat_date")
	private Date treatDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="created_by", insertable=false, updatable=false)
	private UserMaster userMaster;
	
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

	public String getTreatementName() {
		return treatementName;
	}

	public void setTreatementName(String treatementName) {
		this.treatementName = treatementName;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Character getIsEnterInErrorStatus() {
		return isEnterInErrorStatus;
	}

	public void setIsEnterInErrorStatus(Character isEnterInErrorStatus) {
		this.isEnterInErrorStatus = isEnterInErrorStatus;
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

	public Integer getTreatmentHistoryId() {
		return treatmentHistoryId;
	}

	public void setTreatmentHistoryId(Integer treatmentHistoryId) {
		this.treatmentHistoryId = treatmentHistoryId;
	}

	public String getSurgenName() {
		return surgenName;
	}

	public void setSurgenName(String surgenName) {
		this.surgenName = surgenName;
	}

	public Date getTreatDate() {
		return treatDate;
	}

	public void setTreatDate(Date treatDate) {
		this.treatDate = treatDate;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}
	
	
	
}

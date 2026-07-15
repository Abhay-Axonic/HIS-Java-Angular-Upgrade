package com.param.billing.global.transaction.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.ICommonConstants;
import com.param.billing.global.dto.BillListReqDto;
import com.param.billing.global.dto.BillListResDto;
import com.param.billing.global.transaction.dto.DoctorAdvanceDto;
import com.param.billing.global.transaction.dto.DoctorShareCrNoteDetailsDto;
import com.param.billing.global.transaction.dto.DoctorShareCrNoteDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.model.DoctorAdvance;
import com.param.billing.global.transaction.model.DoctorAdvanceDetails;
import com.param.billing.global.transaction.model.DoctorShareCrNote;
import com.param.billing.global.transaction.model.DoctorShareCrNoteDetails;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"unchecked","rawtypes"})
public class DoctorShareCRNoteDaoImpl extends GenericDao<DoctorShareCrNoteDetails, Integer> implements IDoctorShareCRNoteDao,ICommonConstants{

	public DoctorShareCRNoteDaoImpl() {
		super(DoctorShareCrNoteDetails.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getPendingBills(BillListReqDto reqDto) {
		try{
			if(reqDto.getStatus()==1)
			{
				String queryString = "select " 
						 + " 	bill.billing_master_id as \"billingMasterId\", " 
						 + " 	to_char(bill.bill_date_time, 'DD/MM/YYYY') as \"billDate\", " 
						 + " 	bill.bill_number as \"billNumber\", " 
						 + " 	pat.uhid_number as \"uhidNumber\", " 
						 + " 	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\", " 
						 + " 	bill.visit_type_id as \"visitTypeId\", " 
						 + " 	concat(empmst.first_name, ' ', empmst.last_name) as \"userName\", " 
						 + " 	pat.is_otc_reg as \"isOtcReg\", " 
						 + " 	bill.organisation_id as \"orgId\", " 
						 + " 	bill.unit_id as \"unitId\", " 
						 + " 	bill.patient_id as \"patientId\", " 
						 + " 	bill.is_dr_note as \"isDrNote\", " 
						 + " 	billagainst.bill_number as \"againstbillNumber\", " 
						 + " 	spl.is_nursing_assessment_required as \"isNursingAssessmentRequired\", " 
						 + " 	bill.disallowed_amount as \"disallowedAmount\", " 
						 + " 	bill.disallowed_against_invoice_dtl_id as \"disallowedAgainstInvoiceDtlsId\", " 
						/* + " 	case " 
						 + " 		when bill.is_interim_or_final_bill = 2 then ( cast(coalesce((select sum(due_amount) from billing.t_bill_payee_details where payee_id = ( select self_payee_id from t_unit_application_billing_configuration where unit_id = :unitId ) and bill_id in ( select billing_master_id from billing.m_billing_master where admission_id = bill.admission_id and is_interim_or_final_bill = 1)), 0) as double precision)) " 
						 + " end as \"intermediateSelfDue\", " */
						 
		                 + " cast(coalesce(temp.intermediateSelfDue,0)as double precision)  as \"intermediateSelfDue\", "
						 + " 	coalesce( case when bill.visit_type_id = 1 then ( select referral_id from t_referal_details where encounter_id = bill.encounter_id and referral_type_id = 1 ) end, case when bill.visit_type_id != 1 then ( select referral_id from t_referal_details where admission_id = bill.admission_id and referral_type_id = 1 ) end ) as \"referalDocId\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then enc.doctor_id when bill.visit_type_id != 1 then adm.doctor_id end ) as \"doctorId\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then enc.encounter_number when bill.visit_type_id != 1 then adm.admission_number end ) as \"visitNo\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then to_char(enc.encounter_date, 'DD/MM/YYYY') when bill.visit_type_id != 1 then to_char(adm.created_date, 'DD/MM/YYYY') end ) as \"visitDate\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then enc.encounter_id when bill.visit_type_id != 1 then adm.admission_id end ) as \"visitId\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then (select enc.appointment_id from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id and enc.status = 'A') end ) as \"appointmentId\", " 
						 + " 	coalesce(bill.total_bill_amount, 0) as \"totalBillAmount\", " 
						 + " 	coalesce(bill.net_amount, 0) as \"netAmount\", " 
						 + " 	coalesce((bill.net_amount - bill.outstanding), 0) as \"amountPaid\", " 
						 + " 	coalesce(bill.outstanding, 0) as \"outstanding\", " 
						 + " 	bill.bill_status_id as \"billStatusId\", " 
						 + " 	bill.is_interim_or_final_bill as \"isInterimOrFinalBill\", " 
						 + " 	coalesce(bill.total_self_payable, 0) as \"totalSelfPayable\", " 
						 + " 	coalesce(bill.total_credit_payable, 0) as \"totalCreditPayable\", " 
						 + " 	coalesce(billPayee.due_amount, 0) as \"patientPayable\" , " 
						 + " 	coalesce(crNote.total_credit, 0) as \"totalCredit\", " 
						 + " 	coalesce((crNote.total_credit-crNote.balance_credit), 0) as \"amountReceived\", " 
						 + " 	crNote.doctor_cr_id as \"doctorCrId\" " 
						 + " from " 
						 + " 	billing.m_billing_master bill " 
						 + " left join billing.m_billing_master billagainst on " 
						 + " 	bill.dr_note_against_billid = billagainst.billing_master_id " 
						 + " inner join patient.t_patient_registration pat on " 
						 + " 	pat.patient_id = bill.patient_id " 
						 + " left join m_prefix_master pre on "
						 + " 	pre.prefix_id = pat.prefix_id "
						 + " left join PUBLIC.m_employee_master empmst on " 
						 + " 	bill.created_by = empmst.user_id " 
						 + " left join opd.t_encounter_master enc on " 
						 + " 	enc.encounter_id = bill.encounter_id " 
						 + " left join adt.t_admission adm on " 
						 + " 	adm.admission_id = bill.admission_id " 
						 + " left join t_unit_speciality_mapper spl on "
						 + " 	spl.speciality_id = enc.dept_id "
						 + " 	and spl.unit_id =enc.unit_id  "
						 + " left join billing.t_doctor_cr_note crNote on " 
						 + " 	crNote.bill_id = bill.billing_master_id " 
						 + " inner join billing.t_bill_payee_details billPayee on " 
						 + " 	billPayee.bill_id = bill.billing_master_id " 
						 + " left join ("
						 + "			select sum(due_amount) as intermediateSelfDue ,tbpd.payee_id,mbm.is_interim_or_final_bill,mbm.admission_id,mbm.billing_master_id  "  
						 + " 		from billing.t_bill_payee_details tbpd "  
						 + " 		inner join t_unit_application_billing_configuration tubc on tubc.self_payee_id = tbpd.payee_id "  
						 + " 		inner join billing.m_billing_master mbm on mbm.billing_master_id = tbpd.bill_id AND mbm.is_interim_or_final_bill = 1 and mbm.status='A' " 
						 + " 		group by "
						 + " 		tbpd.payee_id,tbpd.bill_id ,mbm.is_interim_or_final_bill ,	mbm.billing_master_id ,"  
						 + " 		mbm.admission_id"
						 + ") temp on temp.billing_master_id = bill.billing_master_id "  
						 + " and temp.admission_id = bill.admission_id  and temp.is_interim_or_final_bill = bill.is_interim_or_final_bill"
						 + " AND  bill.is_interim_or_final_bill = 2  "
						 + " where " 
						 + " 	bill.bill_cancelled = 'N' " 
						 + " 	and billPayee.payee_id =( " 
						 + " 	select " 
						 + " 		self_payee_id " 
						 + " 	from " 
						 + " 		t_unit_application_billing_configuration " 
						 + " 	where " 
						 + " 		unit_id =:unitId " 
						 + " 		and status = 'A') " 
						 + " 	and billPayee.due_amount > 0 " 
						 + " 	and bill.organisation_id = :orgId " 
						 + " 	and bill.unit_id = :unitId " 
						 + " 	and bill.bill_status_id != 3 ";
				
				Date fromDate = reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty()	? GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-MM-yyyy"): null;
				Date toDate = reqDto.getToDate() != null && !reqDto.getToDate().isEmpty()? GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-MM-yyyy"): null;
				StringBuilder query = new StringBuilder(queryString);
				query.append(" and to_char(bill.bill_date_time,'YYYY-MM-DD') between :fromDate and :toDate ");

				if(reqDto.getVisitTypeId() != null)
					query.append(" and bill.visit_type_id =:visitTypeId ");

				if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
					query.append(" and trim(lower(bill.bill_number))like :billNo ");

				if (reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
					query.append(" and (trim(lower(enc.encounter_number)) like :visitNo or trim(lower(adm.admission_number)) like :visitNo )");

				if(reqDto.getBillStatusId() != null)
					query.append(" and bill.bill_status_id =:billStatusId" + reqDto.getBillStatusId());

				if (reqDto.getPatientId() != null)
					query.append(" and bill.patient_id =:patientId ");

				query.append(" order by bill.bill_date_time desc ");
				List<BillListResDto> listBills = null;
				Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("orgId", reqDto.getOrgId()).setInteger("unitId", reqDto.getUnitId())
							.setParameter("fromDate" , GlobalCommonDateUtils.getStringDate(fromDate, "yyyy-MM-dd"))
							.setParameter("toDate" , GlobalCommonDateUtils.getStringDate(toDate, "yyyy-MM-dd"));

				if(reqDto.getVisitTypeId() != null)
					sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

				if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
					sqlQuery.setParameter("billNo", "%" + reqDto.getBillNo().toLowerCase().trim() + "%");

				if (reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
					sqlQuery.setParameter("visitNo", "%" + reqDto.getVisitNo().toLowerCase().trim() + "%");

				if(reqDto.getBillStatusId() != null)
					sqlQuery.setParameter("billStatusId", reqDto.getBillStatusId());

				if (reqDto.getPatientId() != null)
					sqlQuery.setParameter("patientId", reqDto.getPatientId());

				listBills = sqlQuery
							.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();
				
				
				
				return new Response<>(SUCCESS, null, null, listBills, null);
			}
			if(reqDto.getStatus()==2)
			{
				String queryString = "select " 
						 + " 	bill.billing_master_id as \"billingMasterId\", " 
						 + " 	to_char(bill.bill_date_time, 'DD/MM/YYYY') as \"billDate\", " 
						 + " 	bill.bill_number as \"billNumber\", " 
						 + " 	pat.uhid_number as \"uhidNumber\", " 
						 + " 	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\", " 
						 + " 	bill.visit_type_id as \"visitTypeId\", " 
						 + " 	concat(empmst.first_name, ' ', empmst.last_name) as \"userName\", " 
						 + " 	pat.is_otc_reg as \"isOtcReg\", " 
						 + " 	bill.organisation_id as \"orgId\", " 
						 + " 	bill.unit_id as \"unitId\", " 
						 + " 	bill.patient_id as \"patientId\", " 
						 + " 	bill.is_dr_note as \"isDrNote\", " 
						 + " 	billagainst.bill_number as \"againstbillNumber\", " 
						 + " 	spl.is_nursing_assessment_required as \"isNursingAssessmentRequired\", " 
						 + " 	bill.disallowed_amount as \"disallowedAmount\", " 
						 + " 	bill.disallowed_against_invoice_dtl_id as \"disallowedAgainstInvoiceDtlsId\", " 
						/* + " 	case " 
						 + " 		when bill.is_interim_or_final_bill = 2 then ( cast(coalesce((select sum(due_amount) from billing.t_bill_payee_details where payee_id = ( select self_payee_id from t_unit_application_billing_configuration where unit_id = :unitId ) and bill_id in ( select billing_master_id from billing.m_billing_master where admission_id = bill.admission_id and is_interim_or_final_bill = 1)), 0) as double precision)) " 
						 + " end as \"intermediateSelfDue\", " */
						 + " cast(coalesce(temp.intermediateSelfDue,0)as double precision)  as \"intermediateSelfDue\", "
						 + " 	coalesce( case when bill.visit_type_id = 1 then ( select referral_id from t_referal_details where encounter_id = bill.encounter_id and referral_type_id = 1 ) end, case when bill.visit_type_id != 1 then ( select referral_id from t_referal_details where admission_id = bill.admission_id and referral_type_id = 1 ) end ) as \"referalDocId\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then enc.doctor_id when bill.visit_type_id != 1 then adm.doctor_id end ) as \"doctorId\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then enc.encounter_number when bill.visit_type_id != 1 then adm.admission_number end ) as \"visitNo\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then to_char(enc.encounter_date, 'DD/MM/YYYY') when bill.visit_type_id != 1 then to_char(adm.created_date, 'DD/MM/YYYY') end ) as \"visitDate\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then enc.encounter_id when bill.visit_type_id != 1 then adm.admission_id end ) as \"visitId\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then (select enc.appointment_id from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id and enc.status = 'A') end ) as \"appointmentId\", " 
						 + " 	coalesce(bill.total_bill_amount, 0) as \"totalBillAmount\", " 
						 + " 	coalesce(bill.net_amount, 0) as \"netAmount\", " 
						 + " 	coalesce((bill.net_amount - bill.outstanding), 0) as \"amountPaid\", " 
						 + " 	coalesce(bill.outstanding, 0) as \"outstanding\", " 
						 + " 	bill.bill_status_id as \"billStatusId\", " 
						 + " 	bill.is_interim_or_final_bill as \"isInterimOrFinalBill\", " 
						 + " 	coalesce(bill.total_self_payable, 0) as \"totalSelfPayable\", " 
						 + " 	coalesce(bill.total_credit_payable, 0) as \"totalCreditPayable\", " 
						 + " 	coalesce(billPayee.due_amount, 0) as \"patientPayable\" , " 
						 + " 	coalesce(crNote.total_credit, 0) as \"totalCredit\", " 
						 + " 	coalesce((crNote.total_credit-crNote.balance_credit), 0) as \"amountReceived\", " 
						 + " 	crNote.doctor_cr_id as \"doctorCrId\" " 
						 + " from " 
						 + " 	billing.m_billing_master bill " 
						 + " left join billing.m_billing_master billagainst on " 
						 + " 	bill.dr_note_against_billid = billagainst.billing_master_id " 
						 + " inner join patient.t_patient_registration pat on " 
						 + " 	pat.patient_id = bill.patient_id " 
						 + " left join m_prefix_master pre on "
						 + " 	pre.prefix_id = pat.prefix_id "
						 + " left join PUBLIC.m_employee_master empmst on " 
						 + " 	bill.created_by = empmst.user_id " 
						 + " left join opd.t_encounter_master enc on " 
						 + " 	enc.encounter_id = bill.encounter_id " 
						 + " left join adt.t_admission adm on " 
						 + " 	adm.admission_id = bill.admission_id " 
						 + " left join t_unit_speciality_mapper spl on "
						 + " 	spl.speciality_id = enc.dept_id "
						 + " 	and spl.unit_id =enc.unit_id  "
						 + " left join billing.t_doctor_cr_note crNote on " 
						 + " 	crNote.bill_id = bill.billing_master_id " 
						 + " inner join billing.t_bill_payee_details billPayee on " 
						 + " 	billPayee.bill_id = bill.billing_master_id " 
						 + " left join ("
						 + "			select sum(due_amount) as intermediateSelfDue ,tbpd.payee_id,mbm.is_interim_or_final_bill,mbm.admission_id,mbm.billing_master_id  "  
						 + " 		from billing.t_bill_payee_details tbpd "  
						 + " 		inner join t_unit_application_billing_configuration tubc on tubc.self_payee_id = tbpd.payee_id "  
						 + " 		inner join billing.m_billing_master mbm on mbm.billing_master_id = tbpd.bill_id AND mbm.is_interim_or_final_bill = 1 and mbm.status='A' " 
						 + " 		group by "
						 + " 		tbpd.payee_id,tbpd.bill_id ,mbm.is_interim_or_final_bill ,	mbm.billing_master_id ,"  
						 + " 		mbm.admission_id"
						 + ") temp on temp.billing_master_id = bill.billing_master_id "  
						 + " and temp.admission_id = bill.admission_id  and temp.is_interim_or_final_bill = bill.is_interim_or_final_bill"
						 + " AND  bill.is_interim_or_final_bill = 2  "
						 + " where " 
						 + " 	bill.bill_cancelled = 'N' " 
						 + " 	and billPayee.payee_id =( " 
						 + " 	select " 
						 + " 		self_payee_id " 
						 + " 	from " 
						 + " 		t_unit_application_billing_configuration " 
						 + " 	where " 
						 + " 		unit_id =:unitId " 
						 + " 		and status = 'A') " 
						 + " 	and bill.organisation_id = :orgId " 
						 + " 	and bill.unit_id = :unitId "
						 + "	and bill.billing_master_id IN (select distinct(bill_id) from billing.t_doctor_cr_note_details) " ;
				
				boolean dateFlag = false;
				Date fromDate = reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty()	? GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-MM-yyyy"): null;
				Date toDate = reqDto.getToDate() != null && !reqDto.getToDate().isEmpty()? GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-MM-yyyy"): null;
				StringBuilder query = new StringBuilder(queryString);
				dateFlag = (fromDate != null && toDate != null ? true : false);

				query.append(" and to_char(bill.bill_date_time,'YYYY-MM-DD') between :fromDate and :toDate ");

				if(reqDto.getVisitTypeId() != null)
					query.append(" and bill.visit_type_id =:visitTypeId ");

				if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
					query.append(" and trim(lower(bill.bill_number))like :billNo ");

				if (reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
					query.append(" and (trim(lower(enc.encounter_number)) like :visitNo or trim(lower(adm.admission_number)) like :visitNo)");

				if(reqDto.getBillStatusId() != null)
					query.append(" and bill.bill_status_id =:billStatusId ");

				if (reqDto.getPatientId() != null)
					query.append(" and bill.patient_id =:patientId ");

				query.append(" order by bill.bill_date_time desc ");
				List<BillListResDto> listBills = null;

				Query sqlQuery  = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter( "fromDate", GlobalCommonDateUtils.getStringDate(fromDate, "yyyy-MM-dd"))
						.setParameter( "toDate", GlobalCommonDateUtils.getStringDate(toDate, "yyyy-MM-dd"));

				if(reqDto.getVisitTypeId() != null)
					sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

				if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
					sqlQuery.setParameter("billNo", "%" + reqDto.getBillNo().toLowerCase().trim() + "%");

				if (reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
					sqlQuery.setParameter("visitNo", "%" + reqDto.getVisitNo().toLowerCase().trim() + "%");

				if(reqDto.getBillStatusId() != null)
					sqlQuery.setParameter("billStatusId", reqDto.getBillStatusId());

				if (reqDto.getPatientId() != null)
					sqlQuery.setParameter("patientId", reqDto.getPatientId());

				if (dateFlag) {
					listBills = sqlQuery.setParameter("orgId", reqDto.getOrgId())
							.setParameter("unitId", reqDto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();
				}
				else {
					listBills = sqlQuery.setParameter("orgId", reqDto.getOrgId())
							.setParameter("unitId", reqDto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();
				}



				
				
				return new Response<>(SUCCESS, null, null, listBills, null);
			}
			
		}catch(HibernateException e){
			
			throw e;
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getPendingBillsSearch(BillListReqDto reqDto) {
		try{
			
			if(reqDto.getStatus()==1)
			{
				String queryString = "select " 
						 + " 	bill.billing_master_id as \"billingMasterId\", " 
						 + " 	to_char(bill.bill_date_time, 'DD/MM/YYYY') as \"billDate\", " 
						 + " 	bill.bill_number as \"billNumber\", " 
						 + " 	pat.uhid_number as \"uhidNumber\", " 
						 + " 	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\", " 
						 + " 	bill.visit_type_id as \"visitTypeId\", " 
						 + " 	concat(empmst.first_name, ' ', empmst.last_name) as \"userName\", " 
						 + " 	pat.is_otc_reg as \"isOtcReg\", " 
						 + " 	bill.organisation_id as \"orgId\", " 
						 + " 	bill.unit_id as \"unitId\", " 
						 + " 	bill.patient_id as \"patientId\", " 
						 + " 	bill.is_dr_note as \"isDrNote\", " 
						 + " 	billagainst.bill_number as \"againstbillNumber\", " 
						 + " 	spl.is_nursing_assessment_required as \"isNursingAssessmentRequired\", " 
						 + " 	bill.disallowed_amount as \"disallowedAmount\", " 
						 + " 	bill.disallowed_against_invoice_dtl_id as \"disallowedAgainstInvoiceDtlsId\", " 
						/* + " 	case " 
						 + " 		when bill.is_interim_or_final_bill = 2 then ( cast(coalesce((select sum(due_amount) from billing.t_bill_payee_details where payee_id = ( select self_payee_id from t_unit_application_billing_configuration where unit_id = :unitId ) and bill_id in ( select billing_master_id from billing.m_billing_master where admission_id = bill.admission_id and is_interim_or_final_bill = 1)), 0) as double precision)) " 
						 + " end as \"intermediateSelfDue\", " */
						 + " cast(coalesce(temp.intermediateSelfDue,0)as double precision)  as \"intermediateSelfDue\", "
						 + " 	coalesce( case when bill.visit_type_id = 1 then ( select referral_id from t_referal_details where encounter_id = bill.encounter_id and referral_type_id = 1 ) end, case when bill.visit_type_id != 1 then ( select referral_id from t_referal_details where admission_id = bill.admission_id and referral_type_id = 1 ) end ) as \"referalDocId\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then enc.doctor_id when bill.visit_type_id != 1 then adm.doctor_id end ) as \"doctorId\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then enc.encounter_number when bill.visit_type_id != 1 then adm.admission_number end ) as \"visitNo\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then to_char(enc.encounter_date, 'DD/MM/YYYY') when bill.visit_type_id != 1 then to_char(adm.created_date, 'DD/MM/YYYY') end ) as \"visitDate\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then enc.encounter_id when bill.visit_type_id != 1 then adm.admission_id end ) as \"visitId\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then (select enc.appointment_id from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id and enc.status = 'A') end ) as \"appointmentId\", " 
						 + " 	coalesce(bill.total_bill_amount, 0) as \"totalBillAmount\", " 
						 + " 	coalesce(bill.net_amount, 0) as \"netAmount\", " 
						 + " 	coalesce((bill.net_amount - bill.outstanding), 0) as \"amountPaid\", " 
						 + " 	coalesce(bill.outstanding, 0) as \"outstanding\", " 
						 + " 	bill.bill_status_id as \"billStatusId\", " 
						 + " 	bill.is_interim_or_final_bill as \"isInterimOrFinalBill\", " 
						 + " 	coalesce(bill.total_self_payable, 0) as \"totalSelfPayable\", " 
						 + " 	coalesce(bill.total_credit_payable, 0) as \"totalCreditPayable\", " 
						 + " 	coalesce(billPayee.due_amount, 0) as \"patientPayable\" , " 
						 + " 	coalesce(crNote.total_credit, 0) as \"totalCredit\", " 
						 + " 	coalesce((crNote.total_credit-crNote.balance_credit), 0) as \"amountReceived\", " 
						 + " 	crNote.doctor_cr_id as \"doctorCrId\" " 
						 + " from " 
						 + " 	billing.m_billing_master bill " 
						 + " left join billing.m_billing_master billagainst on " 
						 + " 	bill.dr_note_against_billid = billagainst.billing_master_id " 
						 + " inner join patient.t_patient_registration pat on " 
						 + " 	pat.patient_id = bill.patient_id " 
						 + " left join m_prefix_master pre on "
						 + " 	pre.prefix_id = pat.prefix_id "
						 + " left join PUBLIC.m_employee_master empmst on " 
						 + " 	bill.created_by = empmst.user_id " 
						 + " left join opd.t_encounter_master enc on " 
						 + " 	enc.encounter_id = bill.encounter_id " 
						 + " left join adt.t_admission adm on " 
						 + " 	adm.admission_id = bill.admission_id " 
						 + " left join t_unit_speciality_mapper spl on "
						 + " 	spl.speciality_id = enc.dept_id "
						 + " 	and spl.unit_id =enc.unit_id  " 
						 + " left join billing.t_doctor_cr_note crNote on " 
						 + " 	crNote.bill_id = bill.billing_master_id " 
						 + " inner join billing.t_bill_payee_details billPayee on " 
						 + " 	billPayee.bill_id = bill.billing_master_id " 
						 + " left join ("
						 + "			select sum(due_amount) as intermediateSelfDue ,tbpd.payee_id,mbm.is_interim_or_final_bill,mbm.admission_id,mbm.billing_master_id  "  
						 + " 		from billing.t_bill_payee_details tbpd "  
						 + " 		inner join t_unit_application_billing_configuration tubc on tubc.self_payee_id = tbpd.payee_id "  
						 + " 		inner join billing.m_billing_master mbm on mbm.billing_master_id = tbpd.bill_id AND mbm.is_interim_or_final_bill = 1 and mbm.status='A' " 
						 + " 		group by "
						 + " 		tbpd.payee_id,tbpd.bill_id ,mbm.is_interim_or_final_bill ,	mbm.billing_master_id ,"  
						 + " 		mbm.admission_id"
						 + ") temp on temp.billing_master_id = bill.billing_master_id "  
						 + " and temp.admission_id = bill.admission_id  and temp.is_interim_or_final_bill = bill.is_interim_or_final_bill"
						 + " AND  bill.is_interim_or_final_bill = 2  "
						 + " where " 
						 + " 	bill.bill_cancelled = 'N' " 
						 + " 	and billPayee.payee_id =( " 
						 + " 	select " 
						 + " 		self_payee_id " 
						 + " 	from " 
						 + " 		t_unit_application_billing_configuration " 
						 + " 	where " 
						 + " 		unit_id =:unitId " 
						 + " 		and status = 'A') " 
						 + " 	and billPayee.due_amount > 0 " 
						 + " 	and bill.organisation_id = :orgId " 
						 + " 	and bill.unit_id = :unitId " 
						 + "    and  (trim(lower(bill.bill_number))like '%"+reqDto.getSearchText().trim().toLowerCase()+"%'"
						 + "    or  trim(lower(pat.uhid_number))like '%"+reqDto.getSearchText().trim().toLowerCase()+"%'"
						 + "    or  trim(lower(pat.first_name))like '%"+reqDto.getSearchText().trim().toLowerCase()+"%'"
						 + "    or  trim(lower(pat.middle_name))like '%"+reqDto.getSearchText().trim().toLowerCase()+"%'"
						 + "    or  trim(lower(pat.last_name))like '%"+reqDto.getSearchText().trim().toLowerCase()+"%')"
						 + " 	and bill.bill_status_id != 3 ";
				
				Date fromDate = reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty()	? GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-MM-yyyy"): null;
				Date toDate = reqDto.getToDate() != null && !reqDto.getToDate().isEmpty()? GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-MM-yyyy"): null;
				StringBuilder query = new StringBuilder(queryString);

				query.append(" and to_char(bill.bill_date_time,'YYYY-MM-DD') between :fromDate and :toDate ");

				if(reqDto.getVisitTypeId() != null)
					query.append(" and bill.visit_type_id =:visitTypeId ");

				if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
					query.append(" and trim(lower(bill.bill_number))like :billNo ");

				if (reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
					query.append(" and (trim(lower(enc.encounter_number)) like :visitNo or trim(lower(adm.admission_number)) like :visitNo ");

				if(reqDto.getBillStatusId() != null)
					query.append(" and bill.bill_status_id =:billStatusId ");

				if (reqDto.getPatientId() != null)
					query.append(" and bill.patient_id =:patientId ");

				query.append(" order by bill.bill_date_time desc ");
				List<BillListResDto> listBills = null;
				Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("orgId", reqDto.getOrgId())
							.setParameter("unitId", reqDto.getUnitId())
							.setParameter( "fromDate", GlobalCommonDateUtils.getStringDate(fromDate, "yyyy-MM-dd"))
							.setParameter( "toDate", GlobalCommonDateUtils.getStringDate(toDate, "yyyy-MM-dd"));

				if(reqDto.getVisitTypeId() != null)
					sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

				if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
					sqlQuery.setParameter("billNo", "%" + reqDto.getBillNo().toLowerCase().trim() + "%");

				if (reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
					sqlQuery.setParameter("visitNo", "%" + reqDto.getVisitNo().toLowerCase().trim() + "%");

				if(reqDto.getBillStatusId() != null)
					sqlQuery.setParameter("billStatusId", reqDto.getBillStatusId());

				if (reqDto.getPatientId() != null)
					sqlQuery.setParameter("patientId", reqDto.getPatientId());

				listBills = sqlQuery
							.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();
				
				
				
				return new Response<>(SUCCESS, null, null, listBills, null);
			}
			if(reqDto.getStatus()==2)
			{
				String queryString = "select " 
						 + " 	bill.billing_master_id as \"billingMasterId\", " 
						 + " 	to_char(bill.bill_date_time, 'DD/MM/YYYY') as \"billDate\", " 
						 + " 	bill.bill_number as \"billNumber\", " 
						 + " 	pat.uhid_number as \"uhidNumber\", " 
						 + " 	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\", " 
						 + " 	bill.visit_type_id as \"visitTypeId\", " 
						 + " 	concat(empmst.first_name, ' ', empmst.last_name) as \"userName\", " 
						 + " 	pat.is_otc_reg as \"isOtcReg\", " 
						 + " 	bill.organisation_id as \"orgId\", " 
						 + " 	bill.unit_id as \"unitId\", " 
						 + " 	bill.patient_id as \"patientId\", " 
						 + " 	bill.is_dr_note as \"isDrNote\", " 
						 + " 	billagainst.bill_number as \"againstbillNumber\", " 
						 + " 	spl.is_nursing_assessment_required as \"isNursingAssessmentRequired\", " 
						 + " 	bill.disallowed_amount as \"disallowedAmount\", " 
						 + " 	bill.disallowed_against_invoice_dtl_id as \"disallowedAgainstInvoiceDtlsId\", " 
						/* + " 	case " 
						 + " 		when bill.is_interim_or_final_bill = 2 then ( cast(coalesce((select sum(due_amount) from billing.t_bill_payee_details where payee_id = ( select self_payee_id from t_unit_application_billing_configuration where unit_id = :unitId ) and bill_id in ( select billing_master_id from billing.m_billing_master where admission_id = bill.admission_id and is_interim_or_final_bill = 1)), 0) as double precision)) " 
						 + " end as \"intermediateSelfDue\", " */
						 + " cast(coalesce(temp.intermediateSelfDue,0)as double precision)  as \"intermediateSelfDue\", "
						 + " 	coalesce( case when bill.visit_type_id = 1 then ( select referral_id from t_referal_details where encounter_id = bill.encounter_id and referral_type_id = 1 ) end, case when bill.visit_type_id != 1 then ( select referral_id from t_referal_details where admission_id = bill.admission_id and referral_type_id = 1 ) end ) as \"referalDocId\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then enc.doctor_id when bill.visit_type_id != 1 then adm.doctor_id end ) as \"doctorId\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then enc.encounter_number when bill.visit_type_id != 1 then adm.admission_number end ) as \"visitNo\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then to_char(enc.encounter_date, 'DD/MM/YYYY') when bill.visit_type_id != 1 then to_char(adm.created_date, 'DD/MM/YYYY') end ) as \"visitDate\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then enc.encounter_id when bill.visit_type_id != 1 then adm.admission_id end ) as \"visitId\", " 
						 + " 	coalesce( case when bill.visit_type_id = 1 then (select enc.appointment_id from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id and enc.status = 'A') end ) as \"appointmentId\", " 
						 + " 	coalesce(bill.total_bill_amount, 0) as \"totalBillAmount\", " 
						 + " 	coalesce(bill.net_amount, 0) as \"netAmount\", " 
						 + " 	coalesce((bill.net_amount - bill.outstanding), 0) as \"amountPaid\", " 
						 + " 	coalesce(bill.outstanding, 0) as \"outstanding\", " 
						 + " 	bill.bill_status_id as \"billStatusId\", " 
						 + " 	bill.is_interim_or_final_bill as \"isInterimOrFinalBill\", " 
						 + " 	coalesce(bill.total_self_payable, 0) as \"totalSelfPayable\", " 
						 + " 	coalesce(bill.total_credit_payable, 0) as \"totalCreditPayable\", " 
						 + " 	coalesce(billPayee.due_amount, 0) as \"patientPayable\" , " 
						 + " 	coalesce(crNote.total_credit, 0) as \"totalCredit\", " 
						 + " 	coalesce((crNote.total_credit-crNote.balance_credit), 0) as \"amountReceived\", " 
						 + " 	crNote.doctor_cr_id as \"doctorCrId\" " 
						 + " from " 
						 + " 	billing.m_billing_master bill " 
						 + " left join billing.m_billing_master billagainst on " 
						 + " 	bill.dr_note_against_billid = billagainst.billing_master_id " 
						 + " inner join patient.t_patient_registration pat on " 
						 + " 	pat.patient_id = bill.patient_id "
						 + " left join m_prefix_master pre on "
						 + " 	pre.prefix_id = pat.prefix_id "
						 + " left join PUBLIC.m_employee_master empmst on " 
						 + " 	bill.created_by = empmst.user_id " 
						 + " left join opd.t_encounter_master enc on " 
						 + " 	enc.encounter_id = bill.encounter_id " 
						 + " left join adt.t_admission adm on " 
						 + " 	adm.admission_id = bill.admission_id " 
						 + " left join t_unit_speciality_mapper spl on "
						 + " 	spl.speciality_id = enc.dept_id "
						 + " 	and spl.unit_id =enc.unit_id  "
						 + " left join billing.t_doctor_cr_note crNote on " 
						 + " 	crNote.bill_id = bill.billing_master_id " 
						 + " inner join billing.t_bill_payee_details billPayee on " 
						 + " 	billPayee.bill_id = bill.billing_master_id " 
						 + " left join ("
						 + "			select sum(due_amount) as intermediateSelfDue ,tbpd.payee_id,mbm.is_interim_or_final_bill,mbm.admission_id,mbm.billing_master_id  "  
						 + " 		from billing.t_bill_payee_details tbpd "  
						 + " 		inner join t_unit_application_billing_configuration tubc on tubc.self_payee_id = tbpd.payee_id "  
						 + " 		inner join billing.m_billing_master mbm on mbm.billing_master_id = tbpd.bill_id AND mbm.is_interim_or_final_bill = 1 and mbm.status='A' " 
						 + " 		group by "
						 + " 		tbpd.payee_id,tbpd.bill_id ,mbm.is_interim_or_final_bill ,	mbm.billing_master_id ,"  
						 + " 		mbm.admission_id"
						 + ") temp on temp.billing_master_id = bill.billing_master_id "  
						 + " and temp.admission_id = bill.admission_id  and temp.is_interim_or_final_bill = bill.is_interim_or_final_bill"
						 + " AND  bill.is_interim_or_final_bill = 2  "
						 + " where " 
						 + " 	bill.bill_cancelled = 'N' " 
						 + " 	and billPayee.payee_id =( " 
						 + " 	select " 
						 + " 		self_payee_id " 
						 + " 	from " 
						 + " 		t_unit_application_billing_configuration " 
						 + " 	where " 
						 + " 		unit_id =:unitId " 
						 + " 		and status = 'A') " 
						 + " 	and bill.organisation_id = :orgId " 
						 + " 	and bill.unit_id = :unitId "
						 + "    and  (trim(lower(bill.bill_number))like '%"+reqDto.getSearchText().trim().toLowerCase()+"%'"
						 + "    or  trim(lower(pat.uhid_number))like '%"+reqDto.getSearchText().trim().toLowerCase()+"%'"
						 + "    or  trim(lower(pat.first_name))like '%"+reqDto.getSearchText().trim().toLowerCase()+"%'"
						 + "    or  trim(lower(pat.middle_name))like '%"+reqDto.getSearchText().trim().toLowerCase()+"%'"
						 + "    or  trim(lower(pat.last_name))like '%"+reqDto.getSearchText().trim().toLowerCase()+"%')"
						 + "	and bill.billing_master_id IN (select distinct(bill_id) from billing.t_doctor_cr_note_details) " ;
				
				boolean dateFlag = false;
				Date fromDate = reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty()	? GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-MM-yyyy"): null;
				Date toDate = reqDto.getToDate() != null && !reqDto.getToDate().isEmpty()? GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-MM-yyyy"): null;
				StringBuilder query = new StringBuilder(queryString);
				dateFlag = (fromDate != null && toDate != null ? true : false);
				query.append(" and to_char(bill.bill_date_time,'YYYY-MM-DD') between '"+GlobalCommonDateUtils.getStringDate(fromDate, "yyyy-MM-dd")  +"' and '"+GlobalCommonDateUtils.getStringDate(toDate, "yyyy-MM-dd")+"' ");

				if(reqDto.getVisitTypeId() != null)
					query.append(" and bill.visit_type_id =:visitTypeId ");

				if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
					query.append(" and trim(lower(bill.bill_number))like :billNo ");

				if (reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
					query.append(" and (trim(lower(enc.encounter_number)) like :visitNo or trim(lower(adm.admission_number)) like :visitNo )");

				if(reqDto.getBillStatusId() != null)
					query.append(" and bill.bill_status_id =:billStatusId ");

				if (reqDto.getPatientId() != null)
					query.append(" and bill.patient_id =:patientId ");

				query.append(" order by bill.bill_date_time desc ");
				List<BillListResDto> listBills = null;

				Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter( "fromDate", GlobalCommonDateUtils.getStringDate(fromDate, "yyyy-MM-dd"))
						.setParameter( "toDate", GlobalCommonDateUtils.getStringDate(toDate, "yyyy-MM-dd"));

				if(reqDto.getVisitTypeId() != null)
					sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

				if(reqDto.getBillNo() != null && !reqDto.getBillNo().isEmpty())
					sqlQuery.setParameter("billNo", "%" + reqDto.getBillNo().toLowerCase().trim() + "%");

				if (reqDto.getVisitNo() != null && !reqDto.getVisitNo().isEmpty())
					sqlQuery.setParameter("visitNo", "%" + reqDto.getVisitNo().toLowerCase().trim() + "%");

				if(reqDto.getBillStatusId() != null)
					sqlQuery.setParameter("billStatusId", reqDto.getBillStatusId());

				if (reqDto.getPatientId() != null)
					sqlQuery.setParameter("patientId", reqDto.getPatientId());

				if (dateFlag)
					listBills = sqlQuery.setParameter("orgId", reqDto.getOrgId())
							.setParameter("unitId", reqDto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();
				else
					listBills = sqlQuery.setParameter("orgId", reqDto.getOrgId())
							.setParameter("unitId", reqDto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(BillListResDto.class)).list();
				
				
				
				return new Response<>(SUCCESS, null, null, listBills, null);
			}
			
		}catch(HibernateException e){
			
			throw e;
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response requestForDoctorCr(DoctorShareCrNoteDto reqDto)
	{
		try{
			DoctorShareCrNote crNote = new DoctorShareCrNote();
				crNote.setBillId(reqDto.getBillId());
				crNote.setCreatedBy(reqDto.getCreatedBy());
				crNote.setCreatedDate(TimeZoneComponent.getDateByzone(reqDto.getOrgUnitId()));
				crNote.setOrgId(reqDto.getOrgId());
				crNote.setOrgUnitId(reqDto.getOrgUnitId());
				crNote.setStatus('A');
				crNote.setTotalCredit(reqDto.getTotalCredit());
				crNote.setBalanceCredit(reqDto.getTotalCredit());
				crNote.setUpdatedBy(reqDto.getCreatedBy());
				crNote.setUpdatedDate(TimeZoneComponent.getDateByzone(reqDto.getOrgUnitId()));
			Integer docCrNoteId = (Integer) sessionFactory.getCurrentSession().save(crNote);
			
			return new Response<>(SUCCESS, null, null, null, docCrNoteId);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateRequestForDoctorCr(DoctorShareCrNoteDto reqDto) {
		try{
			
			sessionFactory.getCurrentSession().createSQLQuery("update billing.t_doctor_cr_note set "
					+ "total_credit=:totalCredit, "
					+ "balance_credit=:totalCredit, "
					+ "updated_by=:updatedBy, "
					+ "updated_date=:updatedDate "
					+ "where doctor_cr_id = :doctorCrId ")
				.setParameter("doctorCrId",reqDto.getDoctorCrId())
				.setParameter("totalCredit",reqDto.getTotalCredit())
				.setParameter("updatedBy",reqDto.getUpdatedBy())
				.setParameter("updatedDate", TimeZoneComponent.getDateByzone(0))
			.executeUpdate();
			
			
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response requestForDoctorCrDetails(DoctorShareCrNoteDetailsDto reqDto) {
		try{
			
			DoctorShareCrNoteDetails crNoteDetails = new DoctorShareCrNoteDetails();
				crNoteDetails.setDoctorCrId(reqDto.getDoctorCrId());
				crNoteDetails.setBillId(reqDto.getBillId());
				crNoteDetails.setDoctorId(reqDto.getDoctorId());
				crNoteDetails.setDoctorCredit(reqDto.getDoctorCredit());
				crNoteDetails.setBalanceCredit(reqDto.getDoctorCredit());
				crNoteDetails.setOrgId(reqDto.getOrgId());
				crNoteDetails.setOrgUnitId(reqDto.getOrgUnitId());
				crNoteDetails.setRequestStatus(1);//pending
				crNoteDetails.setStatus('A');
				crNoteDetails.setUpdatedBy(reqDto.getCreatedBy());
				crNoteDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(reqDto.getOrgUnitId()));
				crNoteDetails.setCreatedBy(reqDto.getCreatedBy());
				crNoteDetails.setCreatedDate(TimeZoneComponent.getDateByzone(reqDto.getOrgUnitId()));
				crNoteDetails.setReferralId(reqDto.getReferralId());
			Integer docCrNoteDtlsId = (Integer) sessionFactory.getCurrentSession().save(crNoteDetails);
			
			return new Response<>(SUCCESS, null, null, null, docCrNoteDtlsId);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateRequestForDoctorCrDetails(DoctorShareCrNoteDetailsDto dto) {
		try{
			
			sessionFactory.getCurrentSession().createSQLQuery("update billing.t_doctor_cr_note_details set "
					+ "doctor_credit=:doctorCredit, "
					+ "updated_by=:updatedBy, "
					+ "updated_date=:updatedDate "
					+ "where doctor_cr_details_id = :doctorCrDetailsId ")
				.setParameter("doctorCrDetailsId",dto.getDoctorCrDetailsId())
				.setParameter("doctorCredit",dto.getDoctorCredit())
				.setParameter("updatedBy",dto.getUpdatedBy())
				.setParameter("updatedDate", TimeZoneComponent.getDateByzone(0))
			.executeUpdate();
			
			
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorCrRequests(BillListReqDto reqDto) {
		try{
			
			String query = " select "
					 + " 	docCrDtls.doctor_cr_id as \"doctorCrId\", "
					 + "	docCrDtls.doctor_cr_details_id as \"doctorCrDetailsId\", "
					 + " 	docCrDtls.bill_id as \"billId\", "
					 + " 	docCrDtls.doctor_credit as \"doctorCredit\", "
					 + " 	note as \"note\", "
					 + " 	to_char(bill.bill_date_time, 'DD-MM-YYYY') as \"billDate\", "
					 + " 	bill.bill_number as \"billNumber\", "
					 + " 	pat.uhid_number as \"uhidNumber\", "
					 + " 	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\", "
					 + " 	coalesce(bill.total_bill_amount, 0) as \"totalBillAmount\", "
					 + " 	coalesce(bill.net_amount, 0) as \"netAmount\", "
					 + " 	coalesce((bill.net_amount - bill.outstanding), 0) as \"amountPaid\","
					 + "	coalesce(bill.outstanding, 0) as \"outstanding\", "
					 + " 	coalesce(crNote.total_credit, 0) as \"totalCredit\", "
					 + "	docCrDtls.request_status as \"requestStatus\" "
					 + " from "
					 + " 	billing.t_doctor_cr_note_details docCrDtls "
					 + " inner join billing.t_doctor_cr_note crNote on "
					 + " 	crNote.doctor_cr_id = docCrDtls.doctor_cr_id "
					 + " inner join billing.m_billing_master bill on "
					 + " 	bill.billing_master_id = docCrDtls.bill_id and bill.bill_cancelled = 'N' "
					 + " inner join patient.t_patient_registration pat on "
					 + " 	pat.patient_id = bill.patient_id "
					 + " left join m_prefix_master pre on "
					 + " 	pre.prefix_id = pat.prefix_id "
					 + " where "
					 + "	docCrDtls.request_status=1 "
					 + " 	and docCrDtls.doctor_id =:doctorId "
					 + "	and to_char(bill.bill_date_time, 'DD-MM-YYYY') between :fromDate and :toDate ";
			
			List<DoctorShareCrNoteDetailsDto> detailsList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("doctorId", reqDto.getDoctorId())
					.setParameter("fromDate", reqDto.getFromDate().substring(0,10))
					.setParameter("toDate", reqDto.getToDate().substring(0,10))
					.setResultTransformer(Transformers.aliasToBean(DoctorShareCrNoteDetailsDto.class)).list();
			
			
			return new Response<>(SUCCESS, null, null, detailsList, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getCrRequestDetails(Integer doctorCrId) {
		try{
			
			String query = " select "
					 + " 	docCrDtls.doctor_cr_id as \"doctorCrId\", "
					 + " 	docCrDtls.doctor_cr_details_id as \"doctorCrDetailsId\", "
					 + " 	docCrDtls.doctor_credit as \"doctorCredit\", "
					 + " 	note as \"note\", "
					 + " 	(case when docCrDtls.doctor_id is not null then "
					 + " 		concat('Dr. ', coalesce(dctMst.first_name, ''), ' ', coalesce(dctMst.last_name, ''))  "
					 + " 	else "
					 + "		mrm.referral_name end) as \"doctorName\", "
					 + "	docCrDtls.request_status as \"requestStatus\" "
					 + " from "
					 + " 		billing.t_doctor_cr_note_details docCrDtls "
					 + "	left join doctor.m_doctor_master dctMst on "
					 + "		dctMst.doctor_id=docCrDtls.doctor_id "
					 + "	left join adt.m_referral_master mrm on "
					 + "		mrm.referral_id = docCrDtls.referral_id "
					 + " where "
					 + " 	docCrDtls.doctor_cr_id =:doctorCrId "
					 + "	and docCrDtls.request_status != 3"; 
			
			List<DoctorShareCrNoteDetailsDto> detailsList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("doctorCrId", doctorCrId)
					.setResultTransformer(Transformers.aliasToBean(DoctorShareCrNoteDetailsDto.class)).list();
			
			
			return new Response<>(SUCCESS, null, null, detailsList, null);
		}catch(HibernateException e){
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response doctorsListForDoctorCr(Integer billId) {
		try{
			
			String query =" select " 
					 + " 	doc.doctor_id as \"doctorId\", " 
					 + " 	concat('Dr. ', coalesce(doc.first_name, ''), ' ', coalesce(doc.last_name, '')) as \"doctorName\" " 
					 + " from " 
					 + " 	doctor.m_doctor_master doc " 
					 + " where " 
					 + " 	doc.status = 'A' " 
					 + " 	and doc.doctor_id in ( " 
					 + " 	select " 
					 + " 		distinct(od.ord_doc_id) " 
					 + " 	from " 
					 + " 		billing.t_billing_details bd " 
					 + " 	inner join t_order_details od on " 
					 + " 		bd.order_details_id = od.order_details_id " 
					 + " 	where " 
					 + " 		bd.billing_master_id =:billId ) "
					 + " order by " 
					 + " 	doc.first_name "; 
			
			List<DoctorShareCrNoteDetailsDto> doctorsList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("billId", billId)
					.setResultTransformer(Transformers.aliasToBean(DoctorShareCrNoteDetailsDto.class)).list();

			String queryRef =" select "
					+ " 	mrm.referral_name as \"referralName\", "
					+ "		mrm.referral_id as \"referralId\" "
					+ "	from "
					+ " 	billing.m_billing_master mbm "
					+ " 	inner join t_referal_details trd on "
					+ " 	(case "
					+ " 		when mbm.encounter_id is not null then  "
					+ " 			trd.encounter_id = mbm.encounter_id "
					+ " 		else  "
					+ " 			trd.admission_id = mbm.admission_id "
					+ " 	end) "
					+ " 	inner join adt.m_referral_master mrm on  "
					+ " 		mrm.referral_id = trd.referral_id  "
					+ " 	where  "
					+ " 		mbm.billing_master_id =:billId "
					+ " 	order by "
					+ " 		mrm.referral_name  ";
			
			List<DoctorShareCrNoteDetailsDto> referralList = sessionFactory.getCurrentSession().createSQLQuery(queryRef)
					.setParameter("billId", billId)
					.setResultTransformer(Transformers.aliasToBean(DoctorShareCrNoteDetailsDto.class)).list();
			
			
			return new Response<>(SUCCESS, null, null, doctorsList, referralList);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response acceptDoctorCrRequests(Integer doctorCrDetailsId,Double amountPaid) {
		try{
			
			sessionFactory.getCurrentSession().createSQLQuery("update billing.t_doctor_cr_note_details set "
					+ "request_status = 2 , "
					//+ "balance_credit=balance_credit-:amountPaid, "
					+ "updated_date=:updatedDate "
					+ "where doctor_cr_details_id = :doctorCrDetailsId ")
				.setParameter("doctorCrDetailsId",doctorCrDetailsId)
				//.setDouble("amountPaid",amountPaid)
				.setParameter("updatedDate", TimeZoneComponent.getDateByzone(0))
			.executeUpdate();
			
			
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response rejectDoctorCrRequests(DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto) {
		try{
			
			sessionFactory.getCurrentSession().createSQLQuery("update billing.t_doctor_cr_note_details set "
					+ "request_status = 3 , "
					+ "balance_credit=0, "
					+ "reject_reason_id =:rejectResonId , "
					+ "reject_note =:rejectNote, "
					+ "updated_date=:updatedDate "
					+ "where doctor_cr_details_id = :doctorCrDetailsId ")
			     .setParameter("rejectResonId",doctorShareCrNoteDetailsDto.getRejectReasonId())
			     .setParameter("rejectNote",doctorShareCrNoteDetailsDto.getRejectionNote())
				 .setParameter("doctorCrDetailsId",doctorShareCrNoteDetailsDto.getDoctorCrDetailsId())
				 .setParameter("updatedDate", TimeZoneComponent.getDateByzone(0))
			.executeUpdate();
			
			sessionFactory.getCurrentSession().createSQLQuery("update billing.t_doctor_cr_note set "
					+ "total_credit=total_credit-:doctorCredit, "
					+ "balance_credit=balance_credit-:doctorCredit, "
					+ "updated_date=:updatedDate "
					+ "where doctor_cr_id = :doctorCrId ")
			    .setParameter("doctorCredit",doctorShareCrNoteDetailsDto.getDoctorCredit())
				.setParameter("doctorCrId",doctorShareCrNoteDetailsDto.getDoctorCrId())
				.setParameter("updatedDate", TimeZoneComponent.getDateByzone(0))
			.executeUpdate();
			
			
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getInactiveCredit(DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto) {
           try{
			
			sessionFactory.getCurrentSession().createSQLQuery("update billing.t_doctor_cr_note_details set "
					+ "request_status = 3 , "
					+ "updated_date=:updatedDate "
					+ "where doctor_cr_details_id = :doctorCrDetailsId ")
				.setParameter("doctorCrDetailsId",doctorShareCrNoteDetailsDto.getDoctorCrDetailsId())
				.setParameter("updatedDate", TimeZoneComponent.getDateByzone(0))
			.executeUpdate();
			
			sessionFactory.getCurrentSession().createSQLQuery("update billing.t_doctor_cr_note set "
					+ "total_credit=:totalCredit, "
					+ "balance_credit=:totalCredit, "
					+ "updated_date=:updatedDate "
					+ "where doctor_cr_id = :doctorCrId ")
			    .setParameter("totalCredit",doctorShareCrNoteDetailsDto.getTotalCredit())
				.setParameter("doctorCrId",doctorShareCrNoteDetailsDto.getDoctorCrId())
				.setParameter("updatedDate", TimeZoneComponent.getDateByzone(0))
			.executeUpdate();
			
			
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(HibernateException e){
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorCrDetailsForDoctorSharePaymentVoucher(
			DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto) {
		try{
			String query ="select " 
				 + " 		crdetails.doctor_credit as \"doctorCredit\", "
				// + " 		0.00 as \"amountPaid\", "
				 + " 		crdetails.balance_credit as \"balanceCredit\", "
				 + " 		crdetails.balance_credit as \"availableCredit\", "
				 + " 		(crdetails.doctor_credit - crdetails.balance_credit) as \"consumedCredit\", "
				 + " 		crdetails.bill_id as \"billId\", " 
				 + " 		bm.bill_number as \"billNumber\", " 
				 + " 		crdetails.doctor_cr_details_id as \"doctorCrDetailsId\", "
				 + "		concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\" "
				 + " 	from " 
				 + " 		billing.t_doctor_cr_note_details crdetails " 
				 + " 	inner join billing.m_billing_master bm on " 
				 + " 			bm.billing_master_id = crdetails.bill_id and bm.bill_cancelled = 'N' " 
				 + " 	inner join patient.t_patient_registration pat on "
				 + " 			pat.patient_id = bm.patient_id	"
				 + " 	left join m_prefix_master pre on "
				 + " 			pre.prefix_id = pat.prefix_id "
				 + " 	where " 
				 + " 		 crdetails.doctor_id =:doctorId " 
				 + " 		and crdetails.request_status = 2 "
				 + "		and crdetails.balance_credit > 0 "
				 + "		order by crdetails.doctor_cr_details_id " ; 
			
			List<DoctorShareCrNoteDetailsDto> detailsList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("doctorId", doctorShareCrNoteDetailsDto.getDoctorId())
					//.setInteger("docShareVoucheId", doctorShareCrNoteDetailsDto.getDocShareVoucheId())
					.setResultTransformer(Transformers.aliasToBean(DoctorShareCrNoteDetailsDto.class)).list();
			
			
			String query2 =
					   "    SELECT "
					 + "		coalesce(adv.balance_advance,0) as \"doctorAdvance\", " 
					 + "        adv.doctor_advance_id as \"doctorAdvanceId\" "
					 + " 	from " 
					 + " 		billing.t_doctor_advance adv " 
					 + " 	where " 
					 + " 		adv.doctor_id =:doctorId "
					 + "		and adv.balance_advance > 0 "
					 + "		order by 1 desc limit 1 " ; 
				
			DoctorShareCrNoteDetailsDto doctorAdvanceDto = (DoctorShareCrNoteDetailsDto)sessionFactory.getCurrentSession().createSQLQuery(query2)
						.setParameter("doctorId", doctorShareCrNoteDetailsDto.getDoctorId())
						//.setInteger("docShareVoucheId", doctorShareCrNoteDetailsDto.getDocShareVoucheId())
						.setResultTransformer(Transformers.aliasToBean(DoctorShareCrNoteDetailsDto.class)).uniqueResult();
			
			
			return new Response<>(SUCCESS, null, null, detailsList, doctorAdvanceDto);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveDoctorAdvance(DoctorAdvanceDto doctorAdvanceDto) {
		try{
			
			DoctorAdvanceDetails dotorAdvanceDetails = new DoctorAdvanceDetails();
				dotorAdvanceDetails.setDoctorId(doctorAdvanceDto.getDoctorId());
				dotorAdvanceDetails.setSpecialityId(doctorAdvanceDto.getSpecialityId());
				dotorAdvanceDetails.setRequestedAdvance(doctorAdvanceDto.getTotalAdvance());
				dotorAdvanceDetails.setApprovedAdvance(0.00);
				dotorAdvanceDetails.setOrgId(doctorAdvanceDto.getOrgId());
				dotorAdvanceDetails.setOrgUnitId(doctorAdvanceDto.getOrgUnitId());
				dotorAdvanceDetails.setRequestStatus(1);//pending
				dotorAdvanceDetails.setStatus('A');
				dotorAdvanceDetails.setUpdatedBy(doctorAdvanceDto.getCreatedBy());
				dotorAdvanceDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(doctorAdvanceDto.getOrgUnitId()));
				dotorAdvanceDetails.setCreatedBy(doctorAdvanceDto.getCreatedBy());
				dotorAdvanceDetails.setCreatedDate(TimeZoneComponent.getDateByzone(doctorAdvanceDto.getOrgUnitId()));
				dotorAdvanceDetails.setRemark(doctorAdvanceDto.getRemark());
			sessionFactory.getCurrentSession().saveOrUpdate(dotorAdvanceDetails);
			
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response acceptDoctorAdvance(DoctorAdvanceDto doctorAdvanceDto) {
		try{
			sessionFactory.getCurrentSession().createSQLQuery("update billing.t_doctor_advance_details set "
					+ "request_status = 2 , "
					+ "approved_advance = :approvedAdv , "
					+ "updated_date=:updatedDate "
					+ "where doctor_advance_details_id = :doctorAdvanceDeatailsId ")
				.setParameter("doctorAdvanceDeatailsId",doctorAdvanceDto.getDoctorAdvanceDetailsId())
				.setParameter("approvedAdv", doctorAdvanceDto.getApprovedAdvance())
				.setParameter("updatedDate", TimeZoneComponent.getDateByzone(doctorAdvanceDto.getOrgUnitId()))
				.executeUpdate();
			
			if(doctorAdvanceDto.getDoctorAdvanceId()!=null){
				sessionFactory.getCurrentSession().createSQLQuery("update billing.t_doctor_advance set "
						+ "updated_date=:updatedDate, "
						+ "total_advance=total_advance + :approvedAdv, "
						+ "balance_advance=balance_advance + :approvedAdv "
						+ "where doctor_advance_id = :doctorAdvanceId ")
					.setParameter("doctorAdvanceId",doctorAdvanceDto.getDoctorAdvanceId())
					.setParameter("approvedAdv", doctorAdvanceDto.getApprovedAdvance())
					.setParameter("updatedDate", TimeZoneComponent.getDateByzone(doctorAdvanceDto.getOrgUnitId()))
					.executeUpdate();
			}else{
				DoctorAdvance dotorAdvance = new DoctorAdvance();
					dotorAdvance.setDoctorId(doctorAdvanceDto.getDoctorId());
					dotorAdvance.setTotalAdvance(doctorAdvanceDto.getApprovedAdvance());
					dotorAdvance.setBalanceAdvance(doctorAdvanceDto.getApprovedAdvance());
					dotorAdvance.setOrgId(doctorAdvanceDto.getOrgId());
					dotorAdvance.setOrgUnitId(doctorAdvanceDto.getOrgUnitId());
					dotorAdvance.setStatus('A');
					dotorAdvance.setUpdatedBy(doctorAdvanceDto.getCreatedBy());
					dotorAdvance.setUpdatedDate(TimeZoneComponent.getDateByzone(doctorAdvanceDto.getOrgUnitId()));
					dotorAdvance.setCreatedBy(doctorAdvanceDto.getCreatedBy());
					dotorAdvance.setCreatedDate(TimeZoneComponent.getDateByzone(doctorAdvanceDto.getOrgUnitId()));
				sessionFactory.getCurrentSession().saveOrUpdate(dotorAdvance);
			}
			
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	public Response updateAcceptedDoctorCredit(DoctorShareCrNoteDetailsDto detailsDto,Integer voucherId) {
		try{
			
			sessionFactory.getCurrentSession().createSQLQuery("update billing.t_doctor_cr_note_details set "
					+ "balance_credit=:availableCredit, "
					+ "updated_date=:updatedDate "
					+ "where doctor_cr_details_id = :doctorCrDetailsId ")
				 .setParameter("doctorCrDetailsId",detailsDto.getDoctorCrDetailsId())
				 .setParameter("availableCredit",detailsDto.getAvailableCredit())
				 .setParameter("updatedDate", TimeZoneComponent.getDateByzone(detailsDto.getOrgUnitId()))
			.executeUpdate();
			
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response rejectDoctorAdvance(DoctorAdvanceDto doctorAdvanceDto) {
		try{
			sessionFactory.getCurrentSession().createSQLQuery("update billing.t_doctor_advance_details set "
					+ "request_status = 3 , "
					+ "updated_date=:updatedDate "
					+ "where doctor_advance_details_id = :doctorAdvanceDetailsId ")
				.setParameter("doctorAdvanceDetailsId",doctorAdvanceDto.getDoctorAdvanceDetailsId())
				.setParameter("updatedDate", TimeZoneComponent.getDateByzone(doctorAdvanceDto.getOrgUnitId()))
			.executeUpdate();
			
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateDoctorAdvanceDetails(DoctorAdvanceDto doctorAdvanceDto) {
		try{
			sessionFactory.getCurrentSession().createSQLQuery("update billing.t_doctor_advance_details set "
					+ "requested_advance =:requestedAdvance , "
					+ "remark =:remark , "
					+ "updated_date=:updatedDate "
					+ "where doctor_advance_details_id = :doctorAdvanceDetailsId ")
				.setParameter("doctorAdvanceDetailsId",doctorAdvanceDto.getDoctorAdvanceDetailsId())
				.setParameter("updatedDate", TimeZoneComponent.getDateByzone(doctorAdvanceDto.getOrgUnitId()))
				.setParameter("requestedAdvance",doctorAdvanceDto.getRequestedAdvance())
				.setParameter("remark", doctorAdvanceDto.getRemark())
			.executeUpdate();
			
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response updateStatusDoctorAdvanceDetails(DoctorAdvanceDto doctorAdvanceDto) {
		try{
			sessionFactory.getCurrentSession().createSQLQuery("update billing.t_doctor_advance_details set "
					+ "status =:status , "
					+ "updated_date=:updatedDate "
					+ "where doctor_advance_details_id = :doctorAdvanceDetailsId ")
				.setParameter("doctorAdvanceDetailsId",doctorAdvanceDto.getDoctorAdvanceDetailsId())
				.setParameter("updatedDate", TimeZoneComponent.getDateByzone(doctorAdvanceDto.getOrgUnitId()))
				.setParameter("status",doctorAdvanceDto.getStatus())
			.executeUpdate();
			
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(HibernateException e){
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getDoctorAdvanceData(DoctorAdvanceDto doctorAdvanceDto) {
		try{
			
			String query = " select " 
			 + " 	docAdv.doctor_advance_details_id as \"doctorAdvanceDetailsId\", " 
			 + " 	docAdv.doctor_id as \"doctorId\", " 
			 + " 	concat('Dr. ', coalesce(doc.first_name, ''), ' ', coalesce(doc.last_name, '')) as \"doctorName\", " 
			 + " 	docAdv.requested_advance as \"requestedAdvance\", " 
			 + " 	docAdv.approved_advance as \"approvedAdvance\", " 
			 + " 	docAdv.request_status as \"requestStatus\", " 
			 + " 	(select doctor_advance_id from billing.t_doctor_advance adv where adv.doctor_id = docAdv.doctor_id order by 1 desc limit 1) as \"doctorAdvanceId\", "
			 + "    spl.speciality_name as \"specialityName\", "
			 + "	docAdv.speciality_id as \"specialityId\", "
			 + "	docAdv.status as \"status\", "
			 + "	docAdv.remark as \"remark\", "
			 + "	to_char(docAdv.created_date,'DD/MM/YYYY') as \"createdDate\", "
			 + "	to_char(docAdv.updated_date,'DD/MM/YYYY') as \"updatedDate\" " 
			 + " from " 
			 + " 	billing.t_doctor_advance_details docAdv " 
			 + " inner join doctor.m_doctor_master doc on " 
			 + " 	docAdv.doctor_id = doc.doctor_id " 
			 + " inner join doctor.m_speciality_master spl on "
			 + " 		docAdv.speciality_id = spl.speciality_id "
			 + " where " 
			 + " 	docAdv.request_status != 3 " 
			 + " 	and docAdv.unit_id =:unitId " 
			 + " 	and docAdv.organization_id =:orgId "
			 + "	and docAdv.status = 'A' " 
			 + "	and to_char(docAdv.created_date, 'DD-MM-YYYY') between :fromDate and :toDate "
			 + " order by " 
			 + " 	docAdv.doctor_advance_details_id desc " ;
			
			List<DoctorAdvanceDto> detailsList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId",doctorAdvanceDto.getOrgUnitId())
					.setParameter("orgId",doctorAdvanceDto.getOrgId())
					.setParameter("fromDate",doctorAdvanceDto.getFromDate().substring(0,10))
					.setParameter("toDate",doctorAdvanceDto.getFromDate().substring(0,10))
					.setResultTransformer(Transformers.aliasToBean(DoctorAdvanceDto.class)).list();

			return new Response<>(SUCCESS, null, null, detailsList, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAcceptedDoctorAdvance(Double dummyConsumedDocAdvance, Integer doctorAdvanceId) {
		try{
			if(doctorAdvanceId != null && doctorAdvanceId > 0) {
			sessionFactory.getCurrentSession().createSQLQuery("update billing.t_doctor_advance set balance_advance=balance_advance-:consmuedAdance, "
					+ "updated_date=:updatedDate "
					+ "where doctor_advance_id = :doctorAdvanceId ")
				.setParameter("doctorAdvanceId",doctorAdvanceId)
				.setParameter("updatedDate", TimeZoneComponent.getDateByzone(0))
				.setParameter("consmuedAdance",dummyConsumedDocAdvance)
			.executeUpdate();
			}
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(HibernateException e){
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public void checkOrderDocId(Integer billingMasterId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Response getReferralCrRequests(BillListReqDto reqDto) {
		try{
			
			String query = " select "
					 + " 	docCrDtls.doctor_cr_id as \"doctorCrId\", "
					 + "	docCrDtls.doctor_cr_details_id as \"doctorCrDetailsId\", "
					 + " 	docCrDtls.bill_id as \"billId\", "
					 + " 	docCrDtls.doctor_credit as \"doctorCredit\", "
					 + " 	note as \"note\", "
					 + " 	to_char(bill.bill_date_time, 'DD-MM-YYYY') as \"billDate\", "
					 + " 	bill.bill_number as \"billNumber\", "
					 + " 	pat.uhid_number as \"uhidNumber\", "
					 + " 	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\", "
					 + " 	coalesce(bill.total_bill_amount, 0) as \"totalBillAmount\", "
					 + " 	coalesce(bill.net_amount, 0) as \"netAmount\", "
					 + " 	coalesce((bill.net_amount - bill.outstanding), 0) as \"amountPaid\","
					 + "	coalesce(bill.outstanding, 0) as \"outstanding\", "
					 + " 	coalesce(crNote.total_credit, 0) as \"totalCredit\", "
					 + "	docCrDtls.request_status as \"requestStatus\", "
					 + "	mrm.referral_id as \"referralId\", "
					 + "	mrm.referral_name as \"referralName\" "
					 + " from "
					 + " 	billing.t_doctor_cr_note_details docCrDtls "
					 + " inner join billing.t_doctor_cr_note crNote on "
					 + " 	crNote.doctor_cr_id = docCrDtls.doctor_cr_id "
					 + " inner join billing.m_billing_master bill on "
					 + " 	bill.billing_master_id = docCrDtls.bill_id "
					 + "	and bill.bill_cancelled = 'N' "
					 + " inner join patient.t_patient_registration pat on "
					 + " 	pat.patient_id = bill.patient_id "
					 + " left join m_prefix_master pre on "
					 + " 	pre.prefix_id = pat.prefix_id "
					 + " inner join adt.m_referral_master mrm on "
					 + " 	mrm.referral_id = docCrDtls.referral_id "
					 + " where "
					 + "	docCrDtls.request_status=1 "
					 + "	and to_char(bill.bill_date_time, 'DD-MM-YYYY') between :fromDate and :toDate ";
			
			List<DoctorShareCrNoteDetailsDto> detailsList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("fromDate",reqDto.getFromDate().substring(0,10))
					.setParameter("toDate",reqDto.getToDate().substring(0,10))
					.setResultTransformer(Transformers.aliasToBean(DoctorShareCrNoteDetailsDto.class)).list();
			
			
			return new Response<>(SUCCESS, null, null, detailsList, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response<TDoctorShareRespDto, Integer> getReferralCrDetailsForReferralSharePaymentVoucher(
			DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto) {
		try{
			String query =" select " 
					 + " 	crdetails.doctor_credit as \"doctorCredit\", " 
					 + " 	crdetails.balance_credit as \"balanceCredit\", " 
					 + " 	crdetails.balance_credit as \"availableCredit\", " 
					 + " 	(crdetails.doctor_credit - crdetails.balance_credit) as \"consumedCredit\", " 
					 + " 	crdetails.bill_id as \"billId\", " 
					 + " 	bm.bill_number as \"billNumber\", " 
					 + " 	crdetails.doctor_cr_details_id as \"doctorCrDetailsId\", " 
					 + " 	concat(coalesce(pre.prefix_code, ''), ' ', pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\" " 
					 + " from " 
					 + " 	billing.t_doctor_cr_note_details crdetails " 
					 + " inner join billing.m_billing_master bm on " 
					 + " 	bm.billing_master_id = crdetails.bill_id " 
					 + " 	and bm.bill_cancelled = 'N' " 
					 + " inner join patient.t_patient_registration pat on " 
					 + " 	pat.patient_id = bm.patient_id " 
					 + " left join m_prefix_master pre on " 
					 + " 	pre.prefix_id = pat.prefix_id " 
					 + " where " 
					 + " 	crdetails.referral_id =:referralId " 
					 + " 	and crdetails.request_status = 2 " 
					 + " 	and crdetails.balance_credit > 0 " 
					 + " order by " 
					 + " 	crdetails.doctor_cr_details_id " ; 
			
			List<DoctorShareCrNoteDetailsDto> detailsList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("referralId", doctorShareCrNoteDetailsDto.getReferralId())
					//.setInteger("docShareVoucheId", doctorShareCrNoteDetailsDto.getDocShareVoucheId())
					.setResultTransformer(Transformers.aliasToBean(DoctorShareCrNoteDetailsDto.class)).list();
			
			
			return new Response(SUCCESS, null, null, detailsList, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}

package com.param.billing.global.model;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.admission.model.AdmissionNote;
import com.param.billing.global.transaction.model.ActiveDepositeCreditNoteMapper;
import com.param.global.model.CompanyMaster;
import com.param.global.model.PatientRegistration;
import com.param.global.model.VisitTypeMaster;

@NamedNativeQueries({
		@NamedNativeQuery(name = "GET_CURRNET_DEPOSIT_ID", query = "select setval('billing.deposit_master_seq',nextval('billing.deposit_master_seq')-1)"),

		@NamedNativeQuery(name = "DEPOSIT_DETAILS_SEARCH", query = "select "
				+ "	dept.deposit_no as \"depositNo\", "
				+ "	dept.deposit_master_id as \"depositMasterId\", "
				+ "	to_char(dept.deposit_date, "
				+ "	'DD-MM-YYYY HH24:MI:SS') as \"depositDate\", "
				+ "	dept.deposit_date as \"depositDateNew\", "
				+ "	pat.uhid_number as \"uhidNumber\", "
				+ "	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\", "
				+ "	dept.deposit_type_id as \"depositTypeId\", "
				+ "	dept.visit_type_id as \"visitTypeId\", "
				+ " cast(coalesce(dept.deposit_amount,0) as numeric(10,2)) as \"depositAmount\",  "
				+ " cast(coalesce(dept.consumed_deposit,0) as numeric(10,2)) as \"consumedDeposit\" ,"
				+ " cast(coalesce(dept.available_deposit,0) as numeric(10,2)) as \"availableDeposit\",  "
				+ "	dept.refund_amount as \"refundAmount\", "
				+ "	coalesce( "
				+ "	case "
				+ "		when dept.visit_type_id = 1 then ( "
				+ "		select "
				+ "			enc.encounter_number "
				+ "		from "
				+ "			opd.t_encounter_master enc "
				+ "		where "
				+ "			enc.encounter_id = dept.encounter_id) "
				+ "	end , "
				+ "	case "
				+ "		when dept.visit_type_id = 2 "
				+ "		or dept.visit_type_id = 3 or dept.visit_type_id = 4 then ( "
				+ "		select "
				+ "			adm.admission_number "
				+ "		from "
				+ "			adt.t_admission adm "
				+ "		where "
				+ "			adm.admission_id = dept.admission_id) "
				+ "	end, "
				+ "	'-') as \"visitNo\", "
				+ "	 empmst.first_name ||' '||empmst.last_name as \"createdBy\", "
				+ "	dept.status as \"status\" "
				+ "from "
				+ "	billing.m_deposit_master dept "
				+ "inner join patient.t_patient_registration pat on "
				+ "	pat.patient_id = dept.patient_id "
				+ " left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
				+ "left join public.m_employee_master empmst on "
				+ "empmst.user_id = dept.created_by "
				+ "where "
//				+ "	dept.status = 'A' "
				+ " dept.org_id =:orgId "
				+ "	and dept.unit_id =:unitId "),
			//	+ "	and dept.available_deposit > 0"

		@NamedNativeQuery(name = "GET_AVAILABLE_DEPOSIT_BY_PATIENT", query = "select  "
				+" 	dept.deposit_master_id as \"depositMasterId\",  "
				+" 	dept.deposit_no as \"depositNo\",  "
				+" 	to_char(dept.deposit_date,  "
				+" 	'DD-MM-YYYY HH24:MI:SS') as \"depositDate\",  "
				+" 	pat.uhid_number as \"uhidNumber\",  "
				+" 	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\",  "
				+" 	dept.deposit_type_id as \"depositTypeId\",  "
				+" 	dept.visit_type_id as \"visitTypeId\",  "
				+" dept.encounter_id as \"encounterId\", "
				+" 	cast(coalesce(dept.deposit_amount,0) as numeric(10,2)) as \"depositAmount\",  "
				+"  cast(coalesce(dept.consumed_deposit,0) as numeric(10,2)) as \"consumedDeposit\" ,"
				+" 	cast(coalesce(dept.available_deposit,0) as numeric(10,2)) as \"availableDeposit\",  "
				+" 	coalesce(  "
				+" 	case  "
				+" 		when dept.visit_type_id = 1 then (  "
				+" 			select enc.encounter_number  "
				+" 		from  "
				+" 			opd.t_encounter_master enc  "
				+" 		where  "
				+" 			enc.encounter_id = dept.encounter_id)  "
				+" 	end ,  "
				+" 	case  "
				+" 		when dept.visit_type_id = 2  "
				+" 		or dept.visit_type_id = 3 or dept.visit_type_id = 4 then (  "
				+" 			select adm.admission_number  "
				+" 		from  "
				+" 			adt.t_admission adm  "
				+" 		where  "
				+" 			adm.admission_id = dept.admission_id)  "
				+" 	end,  "
				+" 	'-') as \"visitNo\"  "
				+" from  "
				+" 	billing.m_deposit_master dept  "
				+" inner join patient.t_patient_registration pat on  "
				+" 	pat.patient_id = dept.patient_id  "
				+ "left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
				+" where  "
				+" 	dept.status = 'A'  "
				+" 	and dept.org_id =:orgId  "
				+" 	and dept.unit_id =:unitId  "
				+" 	and cast(coalesce(dept.available_deposit,0) as numeric(10,2)) > 0  "
				+" 	and dept.patient_id =:patientId  "
				+" 	and deposit_type_id = 1  "
				+" union "
				+"select  "
				+" 	dept.deposit_master_id as \"depositMasterId\",  "
				+" 	dept.deposit_no as \"depositNo\",  "
				+" 	to_char(dept.deposit_date, 'DD-MM-YYYY HH24:MI:SS') as \"depositDate\",  "
				+" 	pat.uhid_number as \"uhidNumber\",  "
				+" 	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\",  "
				+" 	dept.deposit_type_id as \"depositTypeId\",  "
				+" 	dept.visit_type_id as \"visitTypeId\",  "
				+" dept.encounter_id as \"encounterId\", "
				+" 	cast(coalesce(dept.deposit_amount,0) as numeric(10,2)) as \"depositAmount\",  "
				+"  cast(coalesce(dept.consumed_deposit,0) as numeric(10,2)) as \"consumedDeposit\" ,"
				+" 	cast(coalesce(dept.available_deposit,0) as numeric(10,2)) as \"availableDeposit\",  "
				+" 	coalesce(  "
				+" 	case  "
				+" 		when dept.visit_type_id = 1 then (  "
				+" 			select enc.encounter_number  "
				+" 		from  "
				+" 			opd.t_encounter_master enc  "
				+" 		where  "
				+" 			enc.encounter_id = dept.encounter_id)  "
				+" 	end ,  "
				+" 	case  "
				+" 		when dept.visit_type_id = 2  "
				+" 		or dept.visit_type_id = 3 or dept.visit_type_id = 4 then (  "
				+" 			select adm.admission_number  "
				+" 		from  "
				+" 			adt.t_admission adm  "
				+" 		where  "
				+" 			adm.admission_id = dept.admission_id)  "
				+" 	end,  "
				+" 	'-') as visitNo  "
				+" from  "
				+" 	billing.m_deposit_master dept  "
				+" inner join patient.t_patient_registration pat on  "
				+" 	pat.patient_id = dept.patient_id  "
				+ " left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
				+" where  "
				+" 	dept.status = 'A'  "
				+" 	and dept.org_id =:orgId  "
				+" 	and dept.unit_id = :unitId  "
				+" and cast(coalesce(dept.available_deposit,0) as numeric(10,2)) > 0  "
				+" and dept.patient_id =:patientId  "
			 	+" and deposit_type_id in(1,2)  "
			 	//+" and ((dept.visit_type_id=:vistTypeId and dept.encounter_id =:encounterId ) or (dept.visit_type_id=:vistTypeId and dept.admission_id =:encounterId)) "
		// ---->

		),
		
		@NamedNativeQuery(name = "GET_AVAILABLE_DEPOSIT_BY_PATIENT_BY_OT_RESERVATION", query = "select  "
				+"	dept.deposit_master_id as \"depositMasterId\",  "
				+"	dept.deposit_no as \"depositNo\",  "
				+" 	to_char(dept.deposit_date,'DD-MM-YYYY HH24:MI:SS') as \"depositDate\",  "
				+" 	pat.uhid_number as \"uhidNumber\",  "
				+" 	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\",  "
				+" 	dept.deposit_type_id as \"depositTypeId\",  "
				+" 	dept.visit_type_id as \"visitTypeId\", "
				+" dept.encounter_id as \"encounterId\", "
				+" 	cast(coalesce(dept.deposit_amount,0) as numeric(10,2)) as \"depositAmount\",  "
				+"  cast(coalesce(dept.consumed_deposit,0) as numeric(10,2)) as \"consumedDeposit\" ,"
				+" 	cast(coalesce(dept.available_deposit,0) as numeric(10,2)) as \"availableDeposit\",  "
				+" 	coalesce(  "
				+" 	case  "
				+" 		when dept.visit_type_id = 1 then (  "
				+" 			select enc.encounter_number  "
				+" 		from  "
				+" 			opd.t_encounter_master enc  "
				+" 		where  "
				+" 			enc.encounter_id = dept.encounter_id)  "
				+" 	end ,  "
				+" 	case  "
				+" 		when dept.visit_type_id = 2  "
				+" 		or dept.visit_type_id = 3 or dept.visit_type_id = 4 then (  "
				+" 			select adm.admission_number  "
				+" 		from  "
				+" 			adt.t_admission adm  "
				+" 		where  "
				+" 			adm.admission_id = dept.admission_id)  "
				+" 	end,  "
				+" 	'-') as \"visitNo\"  "
				+" from  "
				+" 		billing.m_deposit_master dept  "
				+" 	inner join patient.t_patient_registration pat on  "
				+" 		pat.patient_id = dept.patient_id  "
				+ " left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
				+" where  "
				+" 		dept.status = 'A'  "
				+" 		and dept.org_id =:orgId  "
				+" 		and dept.unit_id =:unitId  "
				+" 		and cast(coalesce(dept.available_deposit,0) as numeric(10,2)) > 0  "
				+" 		and dept.patient_id =:patientId  "
				+" 		and deposit_type_id in (1,2,5) "),
		
		@NamedNativeQuery(name = "GET_DEPT_WISE_DEPOSIT_BY_PATIENT", query = "select  "
				+"	dept.deposit_master_id as \"depositMasterId\",  "
				+"	dept.deposit_no as \"depositNo\",  "
				+" 	to_char(dept.deposit_date,'DD-MM-YYYY HH24:MI:SS') as \"depositDate\",  "
				+" 	pat.uhid_number as \"uhidNumber\",  "
				+" 	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\",  "
				+" 	dept.deposit_type_id as \"depositTypeId\",  "
				+" 	dept.visit_type_id as \"visitTypeId\",  "
				+" dept.encounter_id as \"encounterId\", "
				+" 	cast(coalesce(dept.deposit_amount,0) as numeric(10,2)) as \"depositAmount\",  "
				+"  cast(coalesce(dept.consumed_deposit,0) as numeric(10,2)) as \"consumedDeposit\" ,"
				+" 	cast(coalesce(dept.available_deposit,0) as numeric(10,2)) as \"availableDeposit\",  "
				+" 	coalesce(  "
				+" 	case  "
				+" 		when dept.visit_type_id = 1 then (  "
				+" 			select enc.encounter_number  "
				+" 		from  "
				+" 			opd.t_encounter_master enc  "
				+" 		where  "
				+" 			enc.encounter_id = dept.encounter_id)  "
				+" 	end ,  "
				+" 	case  "
				+" 		when dept.visit_type_id = 2  "
				+" 		or dept.visit_type_id = 3 or dept.visit_type_id = 4 then (  "
				+" 			select adm.admission_number  "
				+" 		from  "
				+" 			adt.t_admission adm  "
				+" 		where  "
				+" 			adm.admission_id = dept.admission_id)  "
				+" 	end,  "
				+" 	'-') as \"visitNo\"  "
				+" from  "
				+" 		billing.m_deposit_master dept  "
				+" 	inner join patient.t_patient_registration pat on  "
				+" 		pat.patient_id = dept.patient_id  "
				+ " left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
				+" where  "
				+" 		dept.status = 'A'  "
				+" 		and dept.org_id =:orgId  "
				+" 		and dept.unit_id =:unitId  "
				+" 		and cast(coalesce(dept.available_deposit,0) as numeric(10,2)) > 0  "
				+" 		and dept.patient_id =:patientId  "
				+" 		and deposit_type_id in (6) "),
		
		@NamedNativeQuery(name="GET_AVAILABLE_DEPOSITE_BY_PATIENT_ID",
		query="SELECT dpmst.deposit_master_id as \"depositMasterId\", "
				+ " dpmst.deposit_no as \"depositNo\", "
				+ " to_char(dpmst.deposit_date, 'dd-MM-YYYY') as \"depositDate\", "
				+ " dpmst.deposit_date as \"depositDateNew\", "
				+ " dpmst.deposit_type_id as \"depositTypeId\", "
				+ " dptype.deposite_type_desc as \"depositType\", "
				+ " vmst.visit_type_code as \"visitTypeName\",  "
				+ " dpmst.visit_type_id as \"visitTypeId\", "
				+ " enc.encounter_number as \"encounterNumber\", "
				+ " adm.admission_number as \"admissionNumber\", "
				+" 	cast(coalesce(dpmst.deposit_amount,0) as numeric(10,2)) as \"depositAmount\",  "
				+" 	cast(coalesce(dpmst.available_deposit,0) as numeric(10,2)) as \"availableDeposit\"  "
				+ " FROM billing.m_deposit_master dpmst"
				+ " LEFT JOIN billing.m_deposite_type_master dptype ON dptype.deposite_type_id = dpmst.deposit_type_id"
				+ " LEFT JOIN public.m_visit_type_master vmst on vmst.visit_type_id = dpmst.visit_type_id"
				+ " LEFT JOIN adt.t_admission adm on adm.admission_id = dpmst.admission_id"
				+ " LEFT JOIN opd.t_encounter_master enc on enc.encounter_id = dpmst.encounter_id"
				+ " WHERE cast(coalesce(dpmst.available_deposit,0) as numeric(10,2)) > 0 and dpmst.patient_id =:patientId and dpmst.unit_id=:unitId and dpmst.status='A' "),
		

		@NamedNativeQuery(name = "GET_DEPOSIT_LIST_BY_DEPOSIT_TYPE", query = " select "
				+ "	dept.deposit_master_id as \"depositMasterId\", " 
				+ "	dept.deposit_no as \"depositNo\", "
				+ "	to_char(dept.deposit_date,'DD-MM-YYYY HH24:MI:SS') as \"depositDate\", "
				+ "	pat.uhid_number as \"uhidNumber\", "
				+ "	concat(coalesce(pre.prefix_code,''),' ', pat.first_name, ' ', pat.middle_name, ' ', pat.last_name ) as \"patientName\", "
				+ "	dept.payee_id as \"payeeId\", "
				+ "	dept.deposit_type_id as \"depositTypeId\", "
				+ "	dept.visit_type_id as \"visitTypeId\", "
				+ " cast(coalesce(dept.deposit_amount,0) as numeric(10,2)) as \"depositAmount\",  "
				+ " cast(coalesce(dept.consumed_deposit,0) as numeric(10,2)) as \"consumedDeposit\" ,"
				+ " cast(coalesce(dept.available_deposit,0) as numeric(10,2)) as \"availableDeposit\",  " 
				+ "	dept.refund_amount as \"refundAmount\", " 
				+ "	dept.admission_id as \"admissionId\", "
				+ "	dept.encounter_id as \"encounterId\", " 
				+ "	payee_master.company_desc as \"companyDesc\", "
				+ "	concat( emp_mst.first_name,' ', emp_mst.last_name )   as \"userName\", " + "	coalesce( "
				+ "		case " 
				+ "			when dept.visit_type_id = 1 then( " 
				+ "				select "
				+ "					enc.encounter_number " 
				+ "				from "
				+ "					opd.t_encounter_master enc " 
				+ "				where "
				+ "					enc.encounter_id = dept.encounter_id " 
				+ "			) " 
				+ "		end, "
				+ "		case " 
				+ "			when dept.visit_type_id = 2 "
				+ "			or dept.visit_type_id = 3 or dept.visit_type_id = 4 then( " 
				+ "				select "
				+ "					adm.admission_number " 
				+ "				from "
				+ "					adt.t_admission adm " 
				+ "				where "
				+ "					adm.admission_id = dept.admission_id " 
				+ "			) " 
				+ "		end, "
				+ "		'-' " 
				+ "	) as \"visitNo\" " 
				+ "from " 
				+ "	billing.m_deposit_master dept "
				+ "inner join patient.t_patient_registration pat on " 
				+ "	pat.patient_id = dept.patient_id "
				+ " left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
				+ "left join public.m_employee_master emp_mst on " 
				+ "	emp_mst.user_id = dept.created_by "
				+ "left join public.t_company_master payee_master on " 
				+ "	payee_master.company_id = dept.payee_id "
				+ "where " + "	dept.status = 'A' " 
				+ "	and dept.org_id =:orgId " 
				+ "	and dept.unit_id =:unitId "
				+ "	and dept.patient_id=:patientId " 
				+ "	and cast(coalesce(dept.available_deposit,0) as numeric(10,2)) > 0 "
				+ "	and dept.deposit_type_id in (:depositTypeIds)"),
		
		@NamedNativeQuery(name = "GET_DEPOSIT_CONSUMPTION_BY_DEPOSIT", query = "select "
				+ "	bill.bill_number as \"billNo\", "
				+ "	bill.visit_type_id as \"visitTypeId\", "
				+ "	to_char(bill.bill_date_time, 'DD-MM-YYYY') as \"billDate\", "
				+ "	cast(coalesce(consumption.consumed_amount,0) as numeric(10,2)) as \"amount\", "
				+ "	coalesce( case when bill.visit_type_id = 1 then ( select enc.encounter_number from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id) end , case when bill.visit_type_id = 2 or bill.visit_type_id = 3  or bill.visit_type_id = 4 then ( select adm.admission_number from adt.t_admission adm where adm.admission_id = bill.admission_id) end, '-') as \"visitNo\", "
				+ "	coalesce(case when bill.visit_type_id = 1 then ( select to_char(enc.encounter_date, 'DD-MM-YYYY') from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id) end ,	case when bill.visit_type_id = 2 or bill.visit_type_id = 3 or bill.visit_type_id = 4  then ( select to_char(adm.created_date, 'DD-MM-YYYY') from adt.t_admission adm where adm.admission_id = bill.admission_id) end, '-' ) as \"visitDate\" "
				+ "from "
				+ "	billing.m_deposit_master deposit "
				+ "inner join billing.t_payment_deposite_consumption_details consumption on "
				+ "	deposit.deposit_master_id = consumption.deposit_id "
				+ "inner join billing.t_payment_deposite_consumption master on "
				+ "	master.dep_consumption_id = consumption.dep_consumption_id "
				+ "inner join billing.m_billing_master bill on "
				+ "	bill.billing_master_id = master.bill_id and bill.bill_cancelled = 'N' "
				+ "where "
				+ "	 consumption.status ='A' and deposit.deposit_master_id = :depositId "
				+ " and  master.status = 'A' "
				+ " union "
				+ "select "
				+ "	sale.bill_no as \"billNo\", "
				+ "	sale .visit_type_id as \"visitTypeId\", "
				+ "	to_char(sale.added_date , 'DD-MM-YYYY') as \"billDate\", "
				+ "	cast(coalesce(consumption.consumed_amount,0) as numeric(10,2)) as \"amount\", "
				+ "	coalesce( case when sale.visit_type_id = 1 then ( select enc.encounter_number from opd.t_encounter_master enc where enc.encounter_id = sale.encounter_id) end , case when sale.visit_type_id = 2 or sale.visit_type_id = 3  or sale.visit_type_id = 4 then ( select adm.admission_number from adt.t_admission adm where adm.admission_id = sale.admission_id) end, '-') as \"visitNo\", "
				+ "	coalesce(case when sale.visit_type_id = 1 then ( select to_char(enc.encounter_date, 'DD-MM-YYYY') from opd.t_encounter_master enc where enc.encounter_id = sale.encounter_id) end ,	case when sale.visit_type_id = 2 or sale.visit_type_id = 3 or sale.visit_type_id = 4  then ( select to_char(adm.created_date, 'DD-MM-YYYY') from adt.t_admission adm where adm.admission_id = sale.admission_id) end, '-' ) as \"visitDate\" "
				+ "from "
				+ "	billing.m_deposit_master deposit "
				+ "inner join billing.t_payment_deposite_consumption_details consumption on "
				+ "	deposit.deposit_master_id = consumption.deposit_id "
				+ "inner join billing.t_payment_deposite_consumption master on "
				+ "	master.dep_consumption_id = consumption.dep_consumption_id "
				+ "inner join pharmacy.t_sale sale on "
				+ "	sale.id = master.sale_id "
				+ "where "
				+ "	 consumption.status ='A' and deposit.deposit_master_id = :depositId"
				+ " and  master.status = 'A' "),
		
		@NamedNativeQuery(name = "GET_DEPOSIT_DETAILS_BY_DEPOSIT", query = " select " 
				+ " 	paymentmode.payment_mode_desc as \"paymentMode\", " 
				+ " 	bankmaster.bank_name as \"bankName\", " 
				+ " 	paymentdetails.cheque_number as \"chequeNumber\", " 
				+ " 	paymentdetails.card_no as \"cardNo\", " 
				+ " 	cast(coalesce(paymentdetails.amount,0) as numeric(10,2)) as \"totalAmount\" " 
				+ " from " 
				+ " 	billing.t_payment_deposit_details paymentdetails " 
				+ " inner join public.m_payment_mode_master paymentmode on " 
				+ " 	paymentdetails.payment_mode_id = paymentmode.payment_mode_id " 
				+ " left outer join billing.m_bank_master bankmaster on " 
				+ " 	paymentdetails.bank_id = bankmaster.bank_id " 
				+ " where " 
				+ " 	paymentdetails.deposit_id =:depositMasterId "
				+ "	ORDER BY paymentdetails.deposit_details_id ASC 	"),
		
		@NamedNativeQuery(name = "GET_GRN_DETAILS_BY_GRN", query = " select "  + 
				"paymentmode.payment_mode_desc as \"paymentMode\", "  + 
				"bankmaster.bank_name as \"bankName\", "  + 
				"tgsrd.cheque_number as \"chequeNumber\", "  + 
				"tgsrd.transaction_no as \"cardNo\", "  + 
				"cast(coalesce(tgsrd.amount, 0) as numeric(10, 2)) as \"totalAmount\" "  + 
				"from "  + 
				"procurement.t_grn_settlement_receipt_detail tgsrd "  + 
				"inner join public.m_payment_mode_master paymentmode on "  + 
				"tgsrd.payment_mode_id = paymentmode.payment_mode_id "  + 
				"left outer join billing.m_bank_master bankmaster on "  + 
				"tgsrd.bank_id = bankmaster.bank_id "  + 
				"where "  + 
				"tgsrd.grn_id =:gRNId "  + 
				"order by "  + 
				"tgsrd.id asc")

			})


@Entity
@Table(name = "m_deposit_master", schema = "billing")
@SequenceGenerator(name = "deposit_master_seq", sequenceName = "billing.deposit_master_seq", allocationSize = 1)
public class DepositMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deposit_master_seq")
	@Column(name = "deposit_master_id")
	private int depositMasterId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "deposit_no")
	private String depositNo;

	@Column(name = "deposit_date")
	private Date depositDate;

	@Column(name = "payee_id")
	private Integer payeeId;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "visit_type_id")
	private Integer visitTypeId;

	@Column(name = "encounter_id")
	private Integer encounterId;

	@Column(name = "admission_id")
	private Integer admissionId;

	@Column(name = "adm_note_id")
	private Integer admNoteId;

	@Column(name = "deposit_type_id")
	private Integer depositTypeId;

	@Column(name = "deposit_amount")
	private double depositAmount;

	@Column(name = "available_deposit")
	private double availableDeposit;

	@Column(name = "consumed_deposit")
	private double consumedDeposit;

	@Column(name = "refund_amount")
	private double refundAmount;

	@Column(name = "received_from")
	private String receivedFrom;

	@Column(name = "identification_no")
	private String identificationNo;

	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "remarks")
	private String remarks;
	
	@Column(name="counter_id")
	private Integer counterId;
	
	@Column(name = "is_addendum")
	private Character isAddendum;
	
	@Column(name = "bill_id")
	private Integer billId;
	
	@Column(name = "signeture_image")
	private String signetureImage;
	
	@Column(name = "signature")
	private byte[] signature;
	
	@Column(name = "ot_reservation_id")
	private Integer otReservationId;
	
	@Column(name = "against_payment_reciept_id")
	private Integer againstPaymentRecieptId;
	
	@Column(name = "pharmacy_sale_mode_id")
	private Integer pharmacySaleModeId;

	@Column(name = "cancel_reason_id")
	private Integer cancelReasonId;

	@Column(name = "cancel_reason_note")
	private String cancelReasonNote;

	@Column(name = "cancelled_by")
	private Integer cancelledBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payee_id", insertable = false, updatable = false, nullable = false)
	private CompanyMaster companyMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false, nullable = false)
	private PatientRegistration patientRegistration;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_type_id", insertable = false, updatable = false, nullable = false)
	private VisitTypeMaster visitTypeMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "adm_note_id", insertable = false, updatable = false, nullable = false)
	private AdmissionNote admissionNote;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "depositMaster")
	private List<PaymentDepositDetails> listPaymentDepositDetails;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "depositMaster")
	private List<ActiveDepositeCreditNoteMapper> listActiveDepositeCreditNoteMapper;

	public Integer getAgainstPaymentRecieptId() {
		return againstPaymentRecieptId;
	}

	public void setAgainstPaymentRecieptId(Integer againstPaymentRecieptId) {
		this.againstPaymentRecieptId = againstPaymentRecieptId;
	}

	public String getSignetureImage() {
		return signetureImage;
	}

	public void setSignetureImage(String signetureImage) {
		this.signetureImage = signetureImage;
	}

	public Character getIsAddendum() {
		return isAddendum;
	}

	public void setIsAddendum(Character isAddendum) {
		this.isAddendum = (isAddendum == null) ? 'N' : isAddendum;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public List<ActiveDepositeCreditNoteMapper> getListActiveDepositeCreditNoteMapper() {
		return listActiveDepositeCreditNoteMapper;
	}

	public void setListActiveDepositeCreditNoteMapper(
			List<ActiveDepositeCreditNoteMapper> listActiveDepositeCreditNoteMapper) {
		this.listActiveDepositeCreditNoteMapper = listActiveDepositeCreditNoteMapper;
	}

	public int getDepositMasterId() {
		return depositMasterId;
	}

	public void setDepositMasterId(int depositMasterId) {
		this.depositMasterId = depositMasterId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getDepositNo() {
		return depositNo;
	}

	public void setDepositNo(String depositNo) {
		this.depositNo = depositNo;
	}

	public Date getDepositDate() {
		return depositDate;
	}

	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
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

	public Integer getAdmNoteId() {
		return admNoteId;
	}

	public void setAdmNoteId(Integer admNoteId) {
		this.admNoteId = admNoteId;
	}

	public Integer getDepositTypeId() {
		return depositTypeId;
	}

	public void setDepositTypeId(Integer depositTypeId) {
		this.depositTypeId = depositTypeId;
	}

	public double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public double getAvailableDeposit() {
		return availableDeposit;
	}

	public void setAvailableDeposit(double availableDeposit) {
		this.availableDeposit = availableDeposit;
	}

	public double getConsumedDeposit() {
		return consumedDeposit;
	}

	public void setConsumedDeposit(double consumedDeposit) {
		this.consumedDeposit = consumedDeposit;
	}

	public String getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public String getIdentificationNo() {
		return identificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public CompanyMaster getCompanyMaster() {
		return companyMaster;
	}

	public void setCompanyMaster(CompanyMaster companyMaster) {
		this.companyMaster = companyMaster;
	}

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public VisitTypeMaster getVisitTypeMaster() {
		return visitTypeMaster;
	}

	public void setVisitTypeMaster(VisitTypeMaster visitTypeMaster) {
		this.visitTypeMaster = visitTypeMaster;
	}

	public AdmissionNote getAdmissionNote() {
		return admissionNote;
	}

	public void setAdmissionNote(AdmissionNote admissionNote) {
		this.admissionNote = admissionNote;
	}

	public List<PaymentDepositDetails> getListPaymentDepositDetails() {
		return listPaymentDepositDetails;
	}

	public void setListPaymentDepositDetails(List<PaymentDepositDetails> listPaymentDepositDetails) {
		this.listPaymentDepositDetails = listPaymentDepositDetails;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public Integer getOtReservationId() {
		return otReservationId;
	}

	public void setOtReservationId(Integer otReservationId) {
		this.otReservationId = otReservationId;
	}

	public Integer getPharmacySaleModeId() {
		return pharmacySaleModeId;
	}

	public void setPharmacySaleModeId(Integer pharmacySaleModeId) {
		this.pharmacySaleModeId = pharmacySaleModeId;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelReasonNote() {
		return cancelReasonNote;
	}

	public void setCancelReasonNote(String cancelReasonNote) {
		this.cancelReasonNote = cancelReasonNote;
	}

	public Integer getCancelledBy() {
		return cancelledBy;
	}

	public void setCancelledBy(Integer cancelledBy) {
		this.cancelledBy = cancelledBy;
	}
}
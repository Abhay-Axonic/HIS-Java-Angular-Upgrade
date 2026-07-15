package com.param.billing.global.transaction.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;

@NamedNativeQueries({
		/*
		 * @NamedNativeQuery(name = "GET_INVOICE_DETAILS_FOR_SETTLEMENT_BY_INVOICE_ID",
		 * query = "select " +" 	inv.company_invoice_id as \"companyInvoiceId\", "
		 * +" 	invdtl.company_invoice_detail_id as \"companyInvoiceDetailId\", "
		 * +" 	inv.invoice_number as \"invoiceNumber\", "
		 * +" 	to_char(bill.bill_date_time, 'DD-MM-YYYY HH24:MI:SS') as \"billDateTime\", "
		 * +" 	bill.bill_number as \"billNumber\", "
		 * +" 	pat.uhid_number as \"uhidNumber\", "
		 * +" 	invdtl.bill_id as \"billId\", " +" 	bill_payee_id as \"billPayeeId\", "
		 * +" 	invdtl.bill_amount as \"totalBillAmount\", "
		 * +" 	invdtl.payable_amount as \"netAmount\", "
		 * +" 	invdtl.due_amount as \"outstanding\", "
		 * +" 	bill.outstanding as \"billOutStanging\", "
		 * +" 	concat(pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\" "
		 * +" from " +" 	billing.t_company_invoice_details invdtl "
		 * +" inner join billing.m_company_invoice_master inv on "
		 * +" 	inv.company_invoice_id = invdtl.company_invoice_id "
		 * +" inner join billing.m_billing_master bill on "
		 * +" 	bill.billing_master_id = invdtl.bill_id "
		 * +" inner join patient.t_patient_registration pat on "
		 * +" 	pat.patient_id = bill.patient_id " +" where "
		 * +" 	inv.company_invoice_id =:invoiceId " +" 	and invdtl.due_amount > 0 "
		 * +" order by " +" 	inv.company_invoice_id "),
		 */
	
	
	@NamedNativeQuery(name = "GET_INVOICE_DETAILS_FOR_SETTLEMENT_BY_INVOICE_ID",
	 query = " select " 
			 + " 	inv.company_invoice_id as \"companyInvoiceId\", " 
			 + " 	invdtl.company_invoice_detail_id as \"companyInvoiceDetailId\", " 
			 + " 	invdtl.organisation_id as \"organisationId\", " 
			 + " 	invdtl.unit_id as \"unitId\", " 
			 + " 	(case " 
			 + " 		when tem.doctor_id is not null then concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) " 
			 + " 		when tem.preferred_doc_id is not null then concat( coalesce(encDoc.first_name), ' ', coalesce( encDoc.middle_name, '' ), ' ', coalesce(encDoc.last_name)) " 
			 + " 		else concat(coalesce(adtDoc.first_name, ''), ' ', coalesce(adtDoc.middle_name, ''), ' ', coalesce(adtDoc.last_name, '')) " 
			 + " 	end) as \"doctorName\" , " 
			 + " 	inv.invoice_number as \"invoiceNumber\", " 
			 + " 	to_char(bill.bill_date_time, 'DD/MM/YYYY HH24:MI:SS') as \"billDateTime\", " 
			 + " 	bill.bill_number as \"billNumber\", " 
			 + " 	pat.uhid_number as \"uhidNumber\", " 
			 + " 	invdtl.bill_id as \"billId\", " 
			 + " 	0 as \"saleId\", " 
			 + " 	bill_payee_id as \"billPayeeId\", " 
			 + " 	invdtl.bill_amount as \"totalBillAmount\", " 
			 + " 	bill.patient_id as \"patientId\", " 
			 + " 	invdtl.payable_amount as \"netAmount\", " 
			 + " 	invdtl.due_amount as \"outstanding\", " 
			 + " 	coalesce(invdtl.cr_net_amount,0) as \"crNetAmount\", " 
			 + " 	bill.outstanding as \"billOutStanging\", " 
//			 + " 	bill.visit_type_id as \"visitTypeId\", " 
			 + " 	(case " 
			 + " 		when  ( tem.encounter_id is not null and tem.encounter_id > 0) then 1 " 
			 + " 		else ta.visit_type_id " 
			 + " 	end ) as \"visitTypeId\", " 
			 + " 	pat.middle_name as \"fatherName\", " 
			 + " 	pdtl.address as \"address\", " 
			 + " 	to_char(adtl.pdd, 'DD/MM/YYYY') as \"pdd\", " 
			 + " 	tem.encounter_id as \"encounterId\", " 
			 + " 	ta.admission_id as \"admissionId\", " 
			 + " 	coalesce( case when tem.encounter_id > 0 then ( select to_char(enc.encounter_date, 'DD/MM/YYYY') from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id) end , case when ta.admission_id > 0 then ( select to_char(adm.created_date, 'DD/MM/YYYY') from adt.t_admission adm where adm.admission_id = bill.admission_id) end, '-' ) as \"visitDate\", " 
			 + " 	coalesce( case when tem.encounter_id > 0 then ( select enc.encounter_id from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id) end , case when ta.admission_id > 0 then ( select adm.admission_id from adt.t_admission adm where adm.admission_id = bill.admission_id) end) as \"id\", " 
			 + " 	concat(coalesce(pre.prefix_code, ''), ' ', pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\", " 
			 + " 	(case " 
			 + " 		when bill.encounter_id is not null then tem.encounter_number " 
			 + " 		else ta.admission_number " 
			 + " 	end ) as \"visitNumber\" " 
			 + " from " 
			 + " 	billing.t_company_invoice_details invdtl " 
			 + " inner join billing.m_company_invoice_master inv on " 
			 + " 	inv.company_invoice_id = invdtl.company_invoice_id " 
			 + " inner join billing.m_billing_master bill on " 
			 + " 	bill.billing_master_id = invdtl.bill_id " 
			 + " 	and bill.bill_cancelled = 'N' " 
			 + " inner join patient.t_patient_registration pat on " 
			 + " 	pat.patient_id = bill.patient_id " 
			 + " left join patient.t_patient_details pdtl on " 
			 + " 	pdtl.patient_id = bill.patient_id " 
			 + " left join m_prefix_master pre on " 
			 + " 	pre.prefix_id = pat.prefix_id " 
			 + " left join opd.t_encounter_master tem on " 
			 + " 	tem.encounter_id = bill.encounter_id " 
			 + " left join adt.t_admission ta on " 
			 + " 	ta.admission_id = bill.admission_id " 
			 + " left join adt.t_admission_details adtl on " 
			 + " 	adtl.admission_id = bill.admission_id " 
			 + " 	and adtl.status = 'A' " 
			 + " left outer join doctor.m_doctor_master doc on " 
			 + " 	tem.doctor_id = doc.doctor_id " 
			 + " left join doctor.m_doctor_master encDoc on " 
			 + " 	encDoc.doctor_id = tem.preferred_doc_id " 
			 + " left join doctor.m_doctor_master adtDoc on " 
			 + " 	adtDoc.doctor_id = ta.doctor_id " 
			 + " where " 
			 + " 	inv.company_invoice_id =:invoiceId " 
			 + " 	and invdtl.due_amount > 0 " 
			 + " union all " 
			 + " select " 
			 + " 	inv.company_invoice_id as \"companyInvoiceId\", " 
			 + " 	invdtl.company_invoice_detail_id as \"companyInvoiceDetailId\", " 
			 + " 	invdtl.organisation_id as \"organisationId\", " 
			 + " 	invdtl.unit_id as \"unitId\", " 
			 + " 	(case " 
			 + " 		when tem.doctor_id is not null then concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) " 
			 + " 		when tem.preferred_doc_id is not null then concat( coalesce(encDoc.first_name), ' ', coalesce( encDoc.middle_name, '' ), ' ', coalesce(encDoc.last_name)) " 
			 + " 		else concat(coalesce(adtDoc.first_name, ''), ' ', coalesce(adtDoc.middle_name, ''), ' ', coalesce(adtDoc.last_name, '')) " 
			 + " 	end) as \"doctorName\" , " 
			 + " 	inv.invoice_number as \"invoiceNumber\", " 
			 + " 	to_char(sale.added_date, 'DD/MM/YYYY HH24:MI:SS') as \"billDateTime\", " 
			 + " 	sale.bill_no as \"billNumber\", " 
			 + " 	pat.uhid_number as \"uhidNumber\", " 
			 + " 	0 as \"billId\", " 
			 + " 	invdtl.t_sale_id as \"saleId\", " 
			 + " 	bill_payee_id as \"billPayeeId\", " 
			 + " 	invdtl.bill_amount as \"totalBillAmount\", " 
			 + " 	sale.patient_id as \"patientId\", " 
			 + " 	invdtl.payable_amount as \"netAmount\", " 
			 + " 	invdtl.due_amount as \"outstanding\", " 
			 + " 	coalesce(invdtl.cr_net_amount,0) as \"crNetAmount\", " 
			 + " 	sale.outstanding_amount as \"billOutStanging\", " 
			// + " 	1 as \"visitTypeId\",
			 + "	( "  
			 + "		case "  
			 + "			when ( tem.encounter_id is not null and tem.encounter_id > 0) then 1 " 
			 + "			else ta.visit_type_id " 
			 + "		end " 
			 + "	) as \"visitTypeId\", " 
			 + " 	pat.middle_name as \"fatherName\", " 
			 + " 	pdtl.address as \"address\", " 
			 + " 	to_char(adtl.pdd, 'DD/MM/YYYY') as \"pdd\", " 
			 + " 	tem.encounter_id as \"encounterId\", " 
			 + " 	ta.admission_id as \"admissionId\", " 
			 + " 	coalesce(case when tem.encounter_id > 0 then ( select to_char(enc.encounter_date, 'DD/MM/YYYY') from opd.t_encounter_master enc where enc.encounter_id = sale.encounter_id) end , case when ta.admission_id > 0 then ( select to_char(adm.created_date, 'DD-MM-YYYY') from adt.t_admission adm where adm.admission_id = sale.admission_id) end, '-' ) as \"visitDate\", " 
			 + " 	coalesce( case when tem.encounter_id > 0 then( select enc.encounter_id from opd.t_encounter_master enc where enc.encounter_id = sale.encounter_id ) end, case when ta.admission_id > 0 then( select adm.admission_id from adt.t_admission adm where adm.admission_id = sale.admission_id ) end) as \"id\", " 
			 + " 	(case " 
			 + " 		when sale.patient_id >0 then concat(coalesce(pre.prefix_code, ''), ' ', pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) " 
			 + " 		else tpatient.patient_name " 
			 + " 	end) as \"patientName\", " 
			 + " 	(case " 
			 + " 		when sale.encounter_id is not null then tem.encounter_number " 
			 + " 		else ta.admission_number " 
			 + " 	end ) as \"visitNumber\" " 
			 + " from " 
			 + " 	billing.t_company_invoice_details invdtl " 
			 + " inner join billing.m_company_invoice_master inv on " 
			 + " 	inv.company_invoice_id = invdtl.company_invoice_id " 
			 + " inner join pharmacy.t_sale sale on " 
			 + " 	sale.id = invdtl.t_sale_id " 
			 + " left join patient.t_patient_registration pat on " 
			 + " 	pat.patient_id = sale.patient_id " 
			 + " left join patient.t_patient_details pdtl on " 
			 + " 	pdtl.patient_id = sale.patient_id " 
			 + " left join m_prefix_master pre on " 
			 + " 	pre.prefix_id = pat.prefix_id " 
			 + " left join pharmacy.t_temp_patient_registration tpatient on " 
			 + " 	sale.temp_patient_id = tpatient.id " 
			 + " left join opd.t_encounter_master tem on " 
			 + " 	tem.encounter_id = sale.encounter_id " 
			 + " left join adt.t_admission ta on " 
			 + " 	ta.admission_id = sale.admission_id " 
			 + " left join adt.t_admission_details adtl on " 
			 + " 	adtl.admission_id = sale.admission_id " 
			 + " 	and adtl.status = 'A' " 
			 + " left outer join doctor.m_doctor_master doc on " 
			 + " 	tem.doctor_id = doc.doctor_id " 
			 + " left join doctor.m_doctor_master encDoc on " 
			 + " 	encDoc.doctor_id = tem.preferred_doc_id " 
			 + " left join doctor.m_doctor_master adtDoc on " 
			 + " 	adtDoc.doctor_id = ta.doctor_id " 
			 + " where " 
			 + " 	inv.company_invoice_id =:invoiceId " 
			 + " 	and invdtl.due_amount > 0 " 
			 + " order by " 
			 + " 	\"companyInvoiceId\" " ),
	
	@NamedNativeQuery(name = "UPDATE_COMPANY_INVOICE_DETAILS_DUE_AMOUNT",
					 query = "update billing.t_company_invoice_details set due_amount = (due_amount - :amount) "
					 		+ "where company_invoice_detail_id = :id ")	,
/*	@NamedNativeQuery(name="GET_COMPANY_INVOICE_SETTLEMENT_DETAILS_BY_COMPANY_INVOICE_ID",
	 query="select to_char(mCmpnyInvMstr.invoice_date,'DD-MM-YYYY') as \"invoiceDate\",mCmpnyInvMstr.invoice_number as \"invoiceNumber\", " 
 +" 	to_char(mBillMstr.bill_date_time,'DD-MM-YYYY') as \"billDate\", mBillMstr.bill_number as \"billNumber\",pat.uhid_number as \"uhidNumber\", " 
 +" 	coalesce(mPrefixMstr.prefix_desc ||'  ' ||pat.first_name ||' '|| pat.middle_name ||' '|| pat.last_name ) as \"patientName\", " 
 +" 	mBillMstr.total_bill_amount as \"totalAmount\",mBillMstr.net_amount as \"netAmount\",mBillMstr.outstanding as \"outstanding\", " 
 +"    (select sum(coalesce(tds_amount,0)) from billing.t_company_invoice_service_details where invoice_id=:invoiceId) as \"tdsAmount\" " 
 +" 	from billing.m_company_invoice_master mCmpnyInvMstr inner join billing.t_company_invoice_details tCmpnyInvDtls " 
 +" 	on mCmpnyInvMstr.company_invoice_id=tCmpnyInvDtls.company_invoice_id " 
 +" 	inner join billing.m_billing_master mBillMstr on mBillMstr.billing_master_id=tCmpnyInvDtls.bill_id " 
 +" 	inner join patient.registration pat on pat.patient_id=mBillMstr.patient_id " 
 +" 	inner join public.m_prefix_master mPrefixMstr on mPrefixMstr.prefix_id=pat.prefix_id " 
 +" 	where mCmpnyInvMstr.company_invoice_id=:invoiceId ")*/
	
	
	
	
	
	@NamedNativeQuery(name="GET_COMPANY_INVOICE_SETTLEMENT_DETAILS_BY_COMPANY_INVOICE_ID",
	 query=" select " 
			 + " 	to_char(cim.invoice_date, 'DD/MM/YYYY') as \"invoiceDate\", " 
			 + " 	cim.invoice_number as \"invoiceNumber\", "
			 + " 	to_char(mBillMstr.bill_date_time, 'DD/MM/YYYY') as \"billDate\", " 
			 + " 	mBillMstr.bill_number as \"billNumber\", "
			 + " 	pat.uhid_number as \"uhidNumber\", " 
			 + " 	coalesce(pm.prefix_desc || '  ' || pat.first_name || ' ' || pat.middle_name || ' ' || pat.last_name ) as \"patientName\", " 
			 + " 	mBillMstr.total_bill_amount as \"totalAmount\", " 
			 + " 	mBillMstr.net_amount as \"netAmount\", "
			 + "    cim.cr_net_amount as \"crNetAmount\", "
			 + " 	mBillMstr.outstanding as \"outstanding\", "
			 + " 	( " 
			 + " 	select " 
			 + " 		sum( coalesce( received_amount, 0 )) " 
			 + " 	from " 
			 + " 		billing.t_company_invoice_service_details " 
			 + " 	where " 
			 + " 		invoice_detail_id = cid.company_invoice_detail_id ) as \"receivedAmt\", " 
		/*	 + " 	( " 
			 + " 	select " 
			 + " 		sum(coalesce(tds_amount, 0)) " 
			 + " 	from " 
			 + " 		billing.t_company_invoice_service_details " 
			 + " 	where " 
			 + " 		invoice_detail_id = cid.company_invoice_detail_id ) "
			 + ""*/
			 + " tcrd.tds_amount as \"tdsAmount\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		sum(coalesce(disallowed_amount, 0)) " 
			 + " 	from " 
			 + " 		billing.t_company_invoice_service_details " 
			 + " 	where " 
			 + " 		invoice_detail_id = cid.company_invoice_detail_id ) as \"disallowedAmount\" , " 
			 + " 	( " 
			 + " 	select " 
			 + " 		sum(coalesce(other_deduction , 0)) " 
			 + " 	from " 
			 + " 		billing.t_company_invoice_service_details " 
			 + " 	where " 
			 + " 		invoice_detail_id = cid.company_invoice_detail_id ) as \"otherDeduction\", "
			 + "	mBillMstr.billing_master_id as \"billId\", "
			 + " 	mBillMstr.patient_id as \"patientId\", "
			 + " 	mBillMstr.visit_type_id as \"visitTypeId\", "
			 + " 	0 as \"billType\" "
			 + " from " 
			 + " 	billing.m_company_invoice_master cim " 
			 + " inner join billing.t_company_invoice_details cid on " 
			 + " 	cim.company_invoice_id = cid.company_invoice_id "
			 + " inner join 	billing.t_company_receipt_detail tcrd  on tcrd.company_invoice_detail_id =cid.company_invoice_detail_id  " 
			 + " inner join billing.m_billing_master mBillMstr on " 
			 + " 	mBillMstr.billing_master_id = cid.bill_id " 
			 + " 	and mBillMstr.bill_cancelled = 'N' " 
			 + " inner join patient.registration pat on " 
			 + " 	pat.patient_id = mBillMstr.patient_id " 
			 + " inner join public.m_prefix_master pm on " 
			 + " 	pm.prefix_id = pat.prefix_id " 
			 + " where " 
			 + " 	cim.company_invoice_id =:invoiceId " 
			 + " union all " 
			 + " select " 
			 + " 	to_char(cim.invoice_date, 'DD/MM/YYYY') as \"invoiceDate\", " 
			 + " 	cim.invoice_number as \"invoiceNumber\", " 
			 + " 	to_char(sale.added_date, 'DD/MM/YYYY') as \"billDate\", " 
			 + " 	sale.bill_no as \"billNumber\", "
			 + " 	pat.uhid_number as \"uhidNumber\", " 
			 + " 	(case " 
			 + " 		when sale.patient_id >0 then coalesce(pm.prefix_desc || '  ' || pat.first_name || ' ' || pat.middle_name || ' ' || pat.last_name ) " 
			 + " 		else coalesce(tpm.prefix_desc || '  ' || tpatient.patient_name) end) as \"patientName\", " 
			 + " 	sale.total_amount as \"totalAmount\", " 
			 + " 	sale.net_amount as \"netAmount\", "
			 + "    cim.cr_net_amount as \"crNetAmount\", "
			 + " 	sale.outstanding_amount as \"outstanding\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		sum( coalesce( received_amount, 0 )) " 
			 + " 	from " 
			 + " 		billing.t_company_invoice_service_details " 
			 + " 	where " 
			 + " 		invoice_detail_id = cid.company_invoice_detail_id ) as \"receivedAmt\", " 
		/*	 + " 	( " 
			 + " 	select " 
			 + " 		sum(coalesce(tds_amount, 0)) " 
			 + " 	from " 
			 + " 		billing.t_company_invoice_service_details " 
			 + " 	where " 
			 + " 		invoice_detail_id = cid.company_invoice_detail_id ) as \"tdsAmount\", " */
			 + " tcrd.tds_amount as \"tdsAmount\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		sum(coalesce(disallowed_amount, 0)) " 
			 + " 	from " 
			 + " 		billing.t_company_invoice_service_details " 
			 + " 	where " 
			 + " 		invoice_detail_id = cid.company_invoice_detail_id ) as \"disallowedAmount\" , " 
			 + " 	( " 
			 + " 	select " 
			 + " 		sum(coalesce(other_deduction , 0)) " 
			 + " 	from " 
			 + " 		billing.t_company_invoice_service_details " 
			 + " 	where " 
			 + " 		invoice_detail_id = cid.company_invoice_detail_id ) as \"otherDeduction\", "
			 + "	cast(sale.id as int4) as \"billId\", "
			 + "	cast(sale.patient_id as int4) as \"patientId\", "
			 + "	0 as \"visitTypeId\", "
			 + "	1 as \"billType\" " 
			 + " from " 
			 + " 	billing.m_company_invoice_master cim " 
			 + " inner join billing.t_company_invoice_details cid on " 
			 + " 	cim.company_invoice_id = cid.company_invoice_id "
			 + " inner join 	billing.t_company_receipt_detail tcrd  on tcrd.company_invoice_detail_id =cid.company_invoice_detail_id  " 
			 + " inner join pharmacy.t_sale sale on " 
			 + " 	sale.id = cid.t_sale_id " 
			 + " left join patient.registration pat on " 
			 + " 	pat.patient_id = sale.patient_id " 
			 + " left join public.m_prefix_master pm on " 
			 + " 	pm.prefix_id = pat.prefix_id " 
			 + " left join pharmacy.t_temp_patient_registration tpatient on " 
			 + " 	sale.temp_patient_id = tpatient.id " 
			 + " left join public.m_prefix_master tpm on " 
			 + " 	tpatient.prefix_id = tpm.prefix_id " 
			 + " where " 
			 + " 	cim.company_invoice_id =:invoiceId " )
})
@Entity
@Table(name="t_company_invoice_details",schema="billing")
@SequenceGenerator(name = "t_seq_company_invoice_details", sequenceName = "billing.t_seq_company_invoice_details", allocationSize = 1)
public class TCompanyInvoiceDetails {

	@Id
	@Column(name="company_invoice_detail_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_company_invoice_details")
	private int companyInvoiceDetailId;
	
	@Column(name="company_invoice_id")
	private Integer companyInvoiceId;
	
	@Column(name="bill_payee_id")
	private Integer billPayeeId;
	
	@Column(name="bill_id")
	private Integer billId;
	
	@Column(name="t_sale_id")
	private Integer tSaleId;
	
	@Column(name="payable_amount")
	private Double payableAmount;
	
	@Column(name="due_amount")
	private Double dueAmount;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organisation_id")
	private Integer orgId;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_date")
	@Convert(converter=DateConverter.class)
	private Long createdDate;
	
	@Column(name="updated_date")
	@Convert(converter=DateConverter.class)
	private Long updatedDate;
	
	@Column(name = "bill_amount")
	private double billAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_invoice_id" , insertable = false , updatable = false , nullable = false)
	private MCompanyInvoiceMaster mCompanyInvoiceMaster;

	@Column(name="cr_net_amount")
	private Double crNetAmount;
	
	@Column(name="associate_company_id")
	private Integer associateCompanyId;
	
	@Column(name="bill_payee_details_id")
	private Integer billPayeeDetailsId;
	
	public MCompanyInvoiceMaster getmCompanyInvoiceMaster() {
		return mCompanyInvoiceMaster;
	}

	public void setmCompanyInvoiceMaster(MCompanyInvoiceMaster mCompanyInvoiceMaster) {
		this.mCompanyInvoiceMaster = mCompanyInvoiceMaster;
	}

	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}

	public Integer getBillPayeeDetailsId() {
		return billPayeeDetailsId;
	}

	public void setBillPayeeDetailsId(Integer billPayeeDetailsId) {
		this.billPayeeDetailsId = billPayeeDetailsId;
	}

	public Double getCrNetAmount() {
		return crNetAmount;
	}

	public void setCrNetAmount(Double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}

	public int getCompanyInvoiceDetailId() {
		return companyInvoiceDetailId;
	}

	public void setCompanyInvoiceDetailId(int companyInvoiceDetailId) {
		this.companyInvoiceDetailId = companyInvoiceDetailId;
	}

	public Integer getCompanyInvoiceId() {
		return companyInvoiceId;
	}

	public void setCompanyInvoiceId(Integer companyInvoiceId) {
		this.companyInvoiceId = companyInvoiceId;
	}

	public Integer getBillPayeeId() {
		return billPayeeId;
	}

	public void setBillPayeeId(Integer billPayeeId) {
		this.billPayeeId = billPayeeId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(Double payableAmount) {
		this.payableAmount = payableAmount;
	}

	public Double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(double billAmount) {
		this.billAmount = billAmount;
	}

	public Integer gettSaleId() {
		return tSaleId;
	}

	public void settSaleId(Integer tSaleId) {
		this.tSaleId = tSaleId;
	}

	
}

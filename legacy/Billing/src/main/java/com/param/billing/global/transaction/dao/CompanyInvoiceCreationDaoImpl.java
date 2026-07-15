package com.param.billing.global.transaction.dao;

import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.IError;
import com.param.billing.global.transaction.config.dto.CompanyInvoiceCreationReqDto;
import com.param.billing.global.transaction.dto.CompanyInvoiceSettlementListResDto;
import com.param.billing.global.transaction.dto.CompanyInvoiceSettlementSearchReqDto;
import com.param.billing.global.transaction.model.BillingMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CompanyInvoiceCreationDaoImpl extends GenericDao<BillingMaster, Integer> implements ICompanyInvoiceCreationDao,com.param.billing.common.ICommonConstants,IError{

	public CompanyInvoiceCreationDaoImpl() {
		super(BillingMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;
	
	
	@Override
	public Response getCompanyInvoiceCreationList(CompanyInvoiceCreationReqDto reqDto)
			throws BillingException {
		try {
			
			//StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_BILL_MST_BY_PAYEE_ID").getQueryString());
			
			StringBuilder q1 = new StringBuilder("select "
			 + "	distinct payee.payee_id as \"payeeId\", "
			 + "	payee.associate_company_id as \"associateCompanyId\", "
			 + "	false as \"isPharamcy\", "
			 + "	bm.billing_master_id as \"billId\", "
			 + "	payee.bill_payee_details_id as \"billPayeeDetailsId\", "
			 + "	to_char(bm.bill_date_time, 'DD-MM-YYYY HH24:MI:SS') as \"billDate\", "
			 + "	bm.bill_number as \"billNumber\", "
			 + "	pat.uhid_number as \"uhidNumber\", "
			 + "	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.last_name) as \"patientName\", "
			 + "	payee.due_amount as \"billAmount\", "
			 + "	coalesce(payee.cr_net_amt,0) as \"crNetAmount\", "
			 + "	payee.bill_amount as \"creditBillAmount\", "
			 + "	bm.net_amount as \"netAmount\", "
			 + "	bm.total_bill_amount as \"totalBillAmount\", "
			 + "	bm.visit_type_id as \"visitTypeId\", "
			 + "	coalesce( case when enc.encounter_id > 0 then ( select enc.encounter_number from opd.t_encounter_master enc where enc.encounter_id = bm.encounter_id) end , case when adm.admission_id > 0 then ( select adm.admission_number from adt.t_admission adm where adm.admission_id = bm.admission_id) end, '-') as \"visitNo\", "
			 + "	coalesce(case when enc.encounter_id > 0 then ( select to_char(enc.encounter_date, 'DD/MM/YYYY') from opd.t_encounter_master enc where enc.encounter_id = bm.encounter_id) end , case when adm.admission_id > 0 then ( select to_char(adm.created_date, 'DD-MM-YYYY') from adt.t_admission adm where adm.admission_id = bm.admission_id) end, '-' ) as \"visitDate\" "
			 + "from "
			 + "	billing.t_bill_payee_details payee "
			 + "inner join billing.m_billing_master bm on "
			 + "	bm.billing_master_id = payee.bill_id "
			 + "inner join billing.t_billing_details bd on "
			 + "	bm.billing_master_id = bd.billing_master_id "
			 + "inner join public.t_order_details od on "
			 + "	bd.order_details_id = od.order_details_id "
			 + "inner join patient.t_patient_registration pat on "
			 + "	pat.patient_id = bm.patient_id "
			 + " left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
			 + "left join opd.t_encounter_master enc on "
			 + "	enc.encounter_id = bm.encounter_id "
			 + "left join adt.t_admission adm on "
			 + "	adm.admission_id = bm.admission_id "
			 + "inner join public.t_company_master copmst on "
			 + "	copmst.company_id = payee.payee_id "
			 + "where "
			 + "	payee.is_invoice_generated = 'N' "
			 + "	and bm.bill_cancelled = 'N' "
			 + "	and od.status = 'A' "
			 + "	and payee.payee_id != ( "
			 + "	select "
			 + "		self_payee_id "
			 + "	from "
			 + "		t_unit_application_billing_configuration "
			 + "	where "
			 + "		unit_id = payee.unit_id ) "
			 + "	and to_timestamp(to_char(bm.bill_date_time, 'YYYY-MM-DD'), 'YYYY-MM-DD') between :fromDate and :toDate "
			 + "	and payee.due_amount > 0 "
			 + "	and payee.payee_id =:payeeId ");
			
			 if( reqDto.getVisitTypeId()!=null) {
				  q1.append( " and bm.visit_type_id =:visitTypeId ");
				}
			
			String q2 = " union all ";
			
			 StringBuilder q3 = new StringBuilder ("select "
			 + "	distinct payee.payee_id as \"payeeId\", "
			 + "	payee.associate_company_id as \"associateCompanyId\", "
			 + "	true as \"isPharamcy\", "
			 + "	cast(sm.id as integer) as \"billId\", "
			 + "	payee.payee_details_id as \"billPayeeDetailsId\", "
			 + "	to_char(sm.added_date, 'DD-MM-YYYY HH24:MI:SS') as \"billDate\", "
			 + "	sm.bill_no as \"billNumber\", "
			 + "	pat.uhid_number as \"uhidNumber\", "
			 + "	(case "
			 + "		when sm.patient_id >0 then concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.last_name) "
			 + "		else tpatient.patient_name "
			 + "	end) as \"patientName\", "
			 + "	payee.due_amount as \"billAmount\", "
			 + "	0 as \"crNetAmount\", "
			 + "	payee.bill_amount as \"creditBillAmount\", "
			 + "	sm.net_amount as \"netAmount\", "
			 + "	sm.total_amount as \"totalBillAmount\", "
			 + "	sm.visit_type_id as \"visitTypeId\", "
			 + "	coalesce( case when enc.encounter_id > 0 then ( select enc.encounter_number from opd.t_encounter_master enc where enc.encounter_id = sm.encounter_id) end , case when adm.admission_id > 0 then ( select adm.admission_number from adt.t_admission adm where adm.admission_id = sm.admission_id) end, '-') as \"visitNo\", "
			 + "	coalesce(case when enc.encounter_id > 0 then ( select to_char(enc.encounter_date, 'DD/MM/YYYY') from opd.t_encounter_master enc where enc.encounter_id = sm.encounter_id) end , case when adm.admission_id > 0 then ( select to_char(adm.created_date, 'DD-MM-YYYY') from adt.t_admission adm where adm.admission_id = sm.admission_id) end, '-' ) as \"visitDate\" "
			 + "from "
			 + "	pharmacy.t_pharmacy_bill_payee_details payee "
			 + "inner join pharmacy.t_sale sm on "
			 + "	sm.id = payee.t_sale_id "
			 + "inner join pharmacy.t_sale_detail sd on "
			 + "	sm.id = sd.sale_id "
			 + "left join patient.t_patient_registration pat on "
			 + "	pat.patient_id = sm.patient_id "
			 + " left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
			 + "left join opd.t_encounter_master enc on "
			 + "	enc.encounter_id = sm.encounter_id "
			 + "left join adt.t_admission adm on "
			 + "	adm.admission_id = sm.admission_id "
			 + "left join pharmacy.t_temp_patient_registration tpatient on "
			 + "	sm.temp_patient_id = tpatient.id "
			 + "inner join public.t_company_master copmst on "
			 + "	copmst.company_id = payee.payee_id "
			 + "where "
			 + "	payee.is_invoice_generated = 'N' "
			 + "	and sm.is_active = true "
			 + "	and sm.status_id in (2,18) "
			 + "	and sd.is_active = true "
			 + "	and sm.is_deleted = false "
			 + "	and sm.is_consolidated_bill = false "
			 + "	and (case "
			 + "			when sm.admission_id is not null then "
			 + "				(sm.admission_id is not null "
			 + "					and not sm.is_from_billable_consumption "
			 + "					and not sm.is_from_crash_cart "
			 + "					and not sm.is_from_narcotics "
			 + "					and not sm.is_from_patient_indent "
			 + "					and not sm.is_from_pending_issue) "
			 + "				else true "
			 + "		end) "
			 + "	and payee.payee_id != ( "
			 + "	select "
			 + "		self_payee_id "
			 + "	from "
			 + "		t_unit_application_billing_configuration "
			 + "	where "
			 + "		unit_id = payee.unit_id ) "
			 + "	and to_timestamp(to_char(sm.added_date, 'YYYY-MM-DD'), 'YYYY-MM-DD') between :fromDate and :toDate "
			 + "	and payee.due_amount > 0 "
			 + "	and copmst.company_id =:payeeId"
			 + "  and (sm.is_virtual_store  is null  or  sm.is_virtual_store =false )");
			
			
			 if(reqDto.getAssociateCompanyId()!=null)
			 {
				q1.append("	and payee.associate_company_id =:associateCompanyId ");
				//q3.append("	and payee.associate_company_id ="+reqDto.getAssociateCompanyId());
			 }
			
			 if( reqDto.getVisitTypeId()!=null) {
				 q3.append( " and sm.visit_type_id =:visitTypeId ");
			 }

			 Date fromDate =(reqDto.getFromDate()!= null && !reqDto.getFromDate().isEmpty())?GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-M-yyyy"):TimeZoneComponent.getDateByzone(reqDto.getOrgUnitId());
			 Date toDate =(reqDto.getToDate()!= null && !reqDto.getToDate().isEmpty())?GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-M-yyyy"):TimeZoneComponent.getDateByzone(reqDto.getOrgUnitId());
				  Query sqlQuery = sessionFactory.getCurrentSession()
						   .createSQLQuery((q1.append(q2).append(q3)).toString())
						   .setParameter("fromDate", fromDate)
						   .setParameter("toDate", toDate)
						   .setParameter("payeeId", reqDto.getPayeeId());

			if( reqDto.getVisitTypeId()!=null)
				sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

			if(reqDto.getAssociateCompanyId()!=null)
				sqlQuery.setParameter("associateCompanyId", reqDto.getAssociateCompanyId());


			List<CompanyInvoiceCreationReqDto> listCompanyInvoiceCreationList = sqlQuery.setResultTransformer(Transformers.aliasToBean(CompanyInvoiceCreationReqDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listCompanyInvoiceCreationList, null);
			
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getDetailsCompanyInvoiceCreationList(CompanyInvoiceCreationReqDto companyInvoiceCreationReqDto)
			throws BillingException {
		try {
			
			StringBuilder query = new StringBuilder("select "
					 + "	payee.bill_payee_details_id as \"billPayeeDetailsId\", "
					 + "	payee.bill_id as \"billId\", "
					 + "	false as \"isPharamcy\", "
					 + "	payee.due_amount as \"dueAmount\", "
					 + "	om.order_no as \"orderNo\", "
					 + "	concat(doc.first_name, ' ', doc.last_name) as \"doctorName\", "
					 + "	bd.quantity as \"quantity\", "
					 + "	od.service_amt as \"unitRate\", "
					 + "	bd.tax_amount as \"taxAmount\", "
					 + "	bd.net_amt as \"netAmount\", "
					 + "	bd.concession as \"concession\", "
					 + "	bd.gross_amount as \"totalAmount\", "
					 + "	bd.discount as \"discount\", "
					 + "	bd.tax_per as \"taxPer\", "
					 + "	service.service_standard_name as \"serviceStandardName\", "
					 + "	mpr.payee_amount as \"credtPayable\", "
					 + "	bd.self_payable as \"selfPayable\", "
					 + "	coalesce((select sum(coalesce(tcnd.cr_net_amount,0)+coalesce(tcnd.cr_tax_amount,0)) from billing.t_credit_note_detail tcnd "
					 + "	inner join billing.m_credit_note_master mcnm on mcnm.credit_note_id = tcnd.credit_note_id "
					 + "	where tcnd.order_detail_id = bd.order_details_id and mcnm.bill_payee_id = mpr.payee_id and tcnd.status = 'A'), 0) as \"crNetAmount\" "
					 + "from "
					 + "	billing.t_bill_details_payee_mapper mpr "
					 + "inner join billing.t_billing_details bd on "
					 + "	mpr.bill_details_id = bd.billing_details_id "
					 + "inner join billing.m_billing_master bm on "
					 + "	bm.billing_master_id = bd.billing_master_id "
					 + "inner join public.t_order_details od on "
					 + "	od.order_details_id = bd.order_details_id "
					 + "inner join public.t_order_details_package_mapper odpm on "
					 + "	od.order_details_id = odpm.order_details_id and odpm.status  = 'A' and odpm.is_package_service ='N' "
					 + "inner join public.t_order_master om on "
					 + "	om.order_id = od.order_id "
					 + "inner join service.m_service_master service on "
					 + "	service.service_master_id = bd.service_id "
					 + "left join doctor.m_doctor_master doc on "
					 + "	doc.doctor_id = bd.ord_doc_id "
					 + "inner join billing.t_bill_payee_details payee on "
					 + "	payee.bill_id = bm.billing_master_id "
					 + "where "
					 + "	bm.billing_master_id =:billId "
					 + "	and bm.bill_cancelled = 'N' "
					 + "	and payee.payee_id = :payeeId "
					 + "	and mpr.payee_id = :payeeId "
					 + "	and od.status = 'A' ");
			if(companyInvoiceCreationReqDto.getAssociateCompanyId()!=null)
			{
				query.append("	and payee.associate_company_id =:associateCompanyId ");
				query.append( "	and mpr.associate_company_id =:associateCompanyId ");
			}
			
			String query2 = " union all "
					 + "select "
					 + "	payee.payee_details_id as \"billPayeeDetailsId\", "
					 + "	payee.t_sale_id as \"billId\", "
					 + "	true as \"isPharamcy\", "
					 + "	payee.due_amount as \"dueAmount\", "
					 + "	'-' as \"orderNo\", "
					 + "	concat(doc.first_name, ' ', doc.last_name) as \"doctorName\", "
					 + "	sd.quantity as \"quantity\", "
					 + "	cast(sd.rate as numeric(15, 2)) as \"unitRate\", "
					 + "	sd.tax_amount as \"taxAmount\", "
					 + "	sd.net_amount as \"netAmount\", "
					 + "	sd.consession_amount as \"concession\", "
					 + "	sd.total_amount as \"totalAmount\", "
					 + "	sd.discount_amount as \"discount\", "
					 + "	sd.tax_percentage as \"taxPer\", "
					 + "	item.\"name\" as \"serviceStandardName\", "
					 + "	sum(cast(sdpm.payee_amount as double precision)) as \"credtPayable\", "
					 + "	sd.self_payable as \"selfPayable\", "
					 + "	0 as \"crNetAmount\" "
					 + "from "
					 + "	pharmacy.t_sale_detail_payee_mapper sdpm "
					 + " inner join pharmacy.t_sale_detail sd on "
					 + "	sdpm.t_sale_details_id = sd.id "
//					 + " inner join public.t_sale_detail_package_mapper sdpkgm  on "
//					 + "	sdpkgm.t_sale_details_id = sd.id and sdpkgm.status  = 'A' and sdpkgm.is_package_item ='N'"
					 + " inner join pharmacy.t_sale sm on "
					 + "	sd.sale_id = sm.id "
					 + " inner join inventory.t_batch batch on "
					 + "	sd.batch_id = batch.id "
					 + " inner join public.m_item item on "
					 + "	batch.item_id = item.id "
					 + "	and batch.is_active = true "
					 + "	and batch.is_deleted = false "
					 + "inner join pharmacy.t_pharmacy_bill_payee_details payee on "
					 + "	payee.t_sale_id = sm.id "
					 + "left join doctor.m_doctor_master doc on "
					 + "	doc.doctor_id = sm.doctor_id "
					 + "where "
					 + "	sm.id =:billId "
					 + "	and sm.is_active = true "
					 + "	and sm.is_deleted = false "
					 + "	and payee.payee_id =:payeeId "
					 + "	and sdpm.payee_id =:payeeId and sd.is_active = true "
					 + "    and coalesce(sd.package_consumption_amt)<sd.net_amount  "
					 + "group by "
					 + "	payee.payee_details_id , "
					 + "	payee.t_sale_id , "
					 + "	\"isPharamcy\", "
					 + "	payee.due_amount, "
					 + "	\"orderNo\", "
					 + "	\"doctorName\", "
					 + "	sd.quantity, "
					 + "	sd.rate, "
					 + "	sd.tax_amount , "
					 + "	sd.net_amount , "
					 + "	sd.total_amount, "
					 + "	sd.discount_amount, "
					 + "	sd.tax_percentage , "
					 + "	item.\"name\" , "
					 + "	sd.self_payable, "
					 + "	sd.consession_amount ";
			
			query.append(query2);
			
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("billId", companyInvoiceCreationReqDto.getBillId())
					.setParameter("payeeId", companyInvoiceCreationReqDto.getPayeeId());

			if(companyInvoiceCreationReqDto.getAssociateCompanyId()!=null)
				sqlQuery.setParameter("associateCompanyId", companyInvoiceCreationReqDto.getAssociateCompanyId());

			List<CompanyInvoiceCreationReqDto> listDetailsCompanyInvoiceCreationList = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(CompanyInvoiceCreationReqDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listDetailsCompanyInvoiceCreationList, null);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getCompanyInvoiceSettlementDetailsByCompanyInvoiceId(CompanyInvoiceSettlementSearchReqDto reqDto)
			throws BillingException {
		try{
			List<CompanyInvoiceCreationReqDto> listDetailsCompanyInvoiceCreationList = sessionFactory.getCurrentSession().getNamedQuery("GET_COMPANY_INVOICE_SETTLEMENT_DETAILS_BY_COMPANY_INVOICE_ID")
					.setParameter("invoiceId", reqDto.getCompanyInvoiceId())
					.setResultTransformer(Transformers.aliasToBean(CompanyInvoiceCreationReqDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listDetailsCompanyInvoiceCreationList, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getCompInvoiceForSettlementDetails(CompanyInvoiceSettlementSearchReqDto reqDto)
			throws BillingException {
		try{
			
			StringBuilder query1 = new StringBuilder("select "
					+ "	distinct(inv.company_invoice_id) as \"companyInvoiceId\", "
					+ "	inv.invoice_number as \"invoiceNumber\", "
					+ "	comp.company_desc as \"companyName\", "
					+ "	inv.total_amount as \"totalAmount\", "
					+ "	inv.due_amount as \"dueAmount\", "
					+ "	to_char(inv.invoice_date, 'DD-MM-YYYY') as \"invoiceDate\", "
					+ "	inv.payment_entitlement_id as \"paymentEntitlementId\", "
					+ " inv.cr_net_amount as \"crNetAmount\", "
					+ "	inv.payee_id as \"payeeId\" "
					+ "from "
					+ "	billing.m_company_invoice_master inv "
					+ "inner join public.t_company_master comp on "
					+ "	comp.company_id = inv.payee_id "
					+ "where "
					+ "	inv.organisation_id = :orgId "
					+ "	and inv.unit_id = :unitId "
					+ "	and inv.due_amount = 0 ");
			
			
			if(reqDto.getCompanyId() != null)
			{
				query1.append(" and inv.payee_id =:companyId ");
			}
			if(reqDto.getAssociateCompanyId() != null)
			{
				query1.append(" and inv.associate_company_id =:associateCompanyId ");
			}
			
			if(reqDto.getCompanyTypeId() != null)
			{
				query1.append(" and comp.company_type_id =:companyTypeId ");
			}
			
			/*if(reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty() && reqDto.getToDate() != null && !reqDto.getToDate().isEmpty())
			{
				query1.append(" and to_char(inv.invoice_date, 'YYYY-MM-DD') between '"+reqDto.getFromDate()+"' and '"+reqDto.getToDate()+"' ");
			}*/
			
			if(reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty() && reqDto.getToDate() != null && !reqDto.getToDate().isEmpty())
			{
				query1.append(" and to_char(inv.updated_date, 'YYYY-MM-DD') between :fromDate and :toDate ");
			}
				
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query1.toString())
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId());

			if(reqDto.getCompanyId() != null)
				sqlQuery.setParameter("companyId", reqDto.getCompanyId());

			if(reqDto.getAssociateCompanyId() != null)
				sqlQuery.setParameter("associateCompanyId", reqDto.getAssociateCompanyId());

			if(reqDto.getCompanyTypeId() != null)
				sqlQuery.setParameter("companyTypeId", reqDto.getCompanyTypeId());

			if(reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty() && reqDto.getToDate() != null && !reqDto.getToDate().isEmpty())
			{
				sqlQuery.setParameter("fromDate", reqDto.getFromDate());
				sqlQuery.setParameter("toDate", reqDto.getToDate());
			}

			List<CompanyInvoiceSettlementListResDto> result = sqlQuery
				.setResultTransformer(Transformers.aliasToBean(CompanyInvoiceSettlementListResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, result, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response companyInvoiceCreationListByBillId(CompanyInvoiceCreationReqDto reqDto)
			throws BillingException {
		try {
			
			
			StringBuilder q1 = new StringBuilder("select "
			 + "	distinct payee.payee_id as \"payeeId\", "
			 + "	payee.associate_company_id as \"associateCompanyId\", "
			 + "	false as \"isPharamcy\", "
			 + "	bm.billing_master_id as \"billId\", "
			 + "	payee.bill_payee_details_id as \"billPayeeDetailsId\", "
			 + "	to_char(bm.bill_date_time, 'DD-MM-YYYY HH24:MI:SS') as \"billDate\", "
			 + "	bm.bill_number as \"billNumber\", "
			 + "	pat.uhid_number as \"uhidNumber\", "
			 + "	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.last_name) as \"patientName\", "
			 + "	payee.due_amount as \"billAmount\", "
			 + "	coalesce(payee.cr_net_amt,0) as \"crNetAmount\", "
			 + "	payee.bill_amount as \"creditBillAmount\", "
			 + "	bm.net_amount as \"netAmount\", "
			 + "	bm.total_bill_amount as \"totalBillAmount\", "
			 + "	bm.visit_type_id as \"visitTypeId\", "
			 + "	coalesce( case when enc.encounter_id > 0 then ( select enc.encounter_number from opd.t_encounter_master enc where enc.encounter_id = bm.encounter_id) end , case when adm.admission_id > 0 then ( select adm.admission_number from adt.t_admission adm where adm.admission_id = bm.admission_id) end, '-') as \"visitNo\", "
			 + "	coalesce(case when enc.encounter_id > 0 then ( select to_char(enc.encounter_date, 'DD/MM/YYYY') from opd.t_encounter_master enc where enc.encounter_id = bm.encounter_id) end , case when adm.admission_id > 0 then ( select to_char(adm.created_date, 'DD-MM-YYYY') from adt.t_admission adm where adm.admission_id = bm.admission_id) end, '-' ) as \"visitDate\" "
			 + "from "
			 + "	billing.t_bill_payee_details payee "
			 + "inner join billing.m_billing_master bm on "
			 + "	bm.billing_master_id = payee.bill_id "
			 + "inner join billing.t_billing_details bd on "
			 + "	bm.billing_master_id = bd.billing_master_id "
			 + "inner join public.t_order_details od on "
			 + "	bd.order_details_id = od.order_details_id "
			 + "inner join patient.t_patient_registration pat on "
			 + "	pat.patient_id = bm.patient_id "
			 + " left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
			 + "left join opd.t_encounter_master enc on "
			 + "	enc.encounter_id = bm.encounter_id "
			 + "left join adt.t_admission adm on "
			 + "	adm.admission_id = bm.admission_id "
			 + "inner join public.t_company_master copmst on "
			 + "	copmst.company_id = payee.payee_id "
			 + "where "
			 + "	payee.is_invoice_generated = 'N' "
			 + "	and bm.bill_cancelled = 'N' "
			 + "	and od.status = 'A' "
			 + "	and payee.payee_id != ( "
			 + "	select "
			 + "		self_payee_id "
			 + "	from "
			 + "		t_unit_application_billing_configuration "
			 + "	where "
			 + "		unit_id = payee.unit_id ) "
			 + "	and payee.due_amount > 0 "
			 + "	and payee.payee_id =:payeeId ");
			
			 if( reqDto.getVisitTypeId()!=null) {
				  q1.append( " and bm.visit_type_id =:visitTypeId ");
				}
			 
			 if( reqDto.getBillId()!=null) {
				  q1.append( " and bm.billing_master_id =:billId ");
				}
			
			String q2 = " union all ";
			
			 StringBuilder q3 = new StringBuilder ("select "
			 + "	distinct payee.payee_id as \"payeeId\", "
			 + "	payee.associate_company_id as \"associateCompanyId\", "
			 + "	true as \"isPharamcy\", "
			 + "	cast(sm.id as integer) as \"billId\", "
			 + "	payee.payee_details_id as \"billPayeeDetailsId\", "
			 + "	to_char(sm.added_date, 'DD-MM-YYYY HH24:MI:SS') as \"billDate\", "
			 + "	sm.bill_no as \"billNumber\", "
			 + "	pat.uhid_number as \"uhidNumber\", "
			 + "	(case "
			 + "		when sm.patient_id >0 then concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.last_name) "
			 + "		else tpatient.patient_name "
			 + "	end) as \"patientName\", "
			 + "	payee.due_amount as \"billAmount\", "
			 + "	0 as \"crNetAmount\", "
			 + "	payee.bill_amount as \"creditBillAmount\", "
			 + "	sm.net_amount as \"netAmount\", "
			 + "	sm.total_amount as \"totalBillAmount\", "
			 + "	sm.visit_type_id as \"visitTypeId\", "
			 + "	coalesce( case when enc.encounter_id > 0 then ( select enc.encounter_number from opd.t_encounter_master enc where enc.encounter_id = sm.encounter_id) end , case when adm.admission_id > 0 then ( select adm.admission_number from adt.t_admission adm where adm.admission_id = sm.admission_id) end, '-') as \"visitNo\", "
			 + "	coalesce(case when enc.encounter_id > 0 then ( select to_char(enc.encounter_date, 'DD/MM/YYYY') from opd.t_encounter_master enc where enc.encounter_id = sm.encounter_id) end , case when adm.admission_id > 0 then ( select to_char(adm.created_date, 'DD-MM-YYYY') from adt.t_admission adm where adm.admission_id = sm.admission_id) end, '-' ) as \"visitDate\" "
			 + "from "
			 + "	pharmacy.t_pharmacy_bill_payee_details payee "
			 + "inner join pharmacy.t_sale sm on "
			 + "	sm.id = payee.t_sale_id "
			 + "inner join pharmacy.t_sale_detail sd on "
			 + "	sm.id = sd.sale_id "
			 + "left join patient.t_patient_registration pat on "
			 + "	pat.patient_id = sm.patient_id "
			 + " left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
			 + "left join opd.t_encounter_master enc on "
			 + "	enc.encounter_id = sm.encounter_id "
			 + "left join adt.t_admission adm on "
			 + "	adm.admission_id = sm.admission_id "
			 + "left join pharmacy.t_temp_patient_registration tpatient on "
			 + "	sm.temp_patient_id = tpatient.id "
			 + "inner join public.t_company_master copmst on "
			 + "	copmst.company_id = payee.payee_id "
			 + "where "
			 + "	payee.is_invoice_generated = 'N' "
			 + "	and sm.is_active = true "
			 + "	and sm.status_id in (2,18) "
			 + "	and sd.is_active = true "
			 + "	and sm.is_deleted = false "
			 + "	and sm.is_consolidated_bill = false "
			 + "	and (case "
			 + "			when sm.admission_id is not null then "
			 + "				(sm.admission_id is not null "
			 + "					and not sm.is_from_billable_consumption "
			 + "					and not sm.is_from_crash_cart "
			 + "					and not sm.is_from_narcotics "
			 + "					and not sm.is_from_patient_indent "
			 + "					and not sm.is_from_pending_issue) "
			 + "				else true "
			 + "		end) "
			 + "	and payee.payee_id != ( "
			 + "	select "
			 + "		self_payee_id "
			 + "	from "
			 + "		t_unit_application_billing_configuration "
			 + "	where "
			 + "		unit_id = payee.unit_id ) "
			 + "	and payee.due_amount > 0 "
			 + "	and copmst.company_id =:payeeId"
			 + "  and (sm.is_virtual_store  is null  or  sm.is_virtual_store =false )");
			
			
			 if(reqDto.getAssociateCompanyId()!=null)
			 {
				 q1.append(" and payee.associate_company_id =:associateCompanyId ");
			 }
			
			 if( reqDto.getVisitTypeId()!=null) {
				 q3.append(" and sm.visit_type_id =:visitTypeId ");
			 }
			 if( reqDto.getBillId()!=null) {
				 q3.append(" and cast(sm.id as integer)=:billId ");
			 }

		    Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery((q1.append(q2).append(q3)).toString())
				   .setParameter("payeeId", reqDto.getPayeeId());

			if( reqDto.getVisitTypeId()!=null)
				sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

			if( reqDto.getBillId()!=null)
				sqlQuery.setParameter("billId", reqDto.getBillId());

			if(reqDto.getAssociateCompanyId()!=null)
				sqlQuery.setParameter("associateCompanyId", reqDto.getAssociateCompanyId());


			List<CompanyInvoiceCreationReqDto> listCompanyInvoiceCreationList = sqlQuery
						   .setResultTransformer(Transformers.aliasToBean(CompanyInvoiceCreationReqDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listCompanyInvoiceCreationList, null);
			
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

}

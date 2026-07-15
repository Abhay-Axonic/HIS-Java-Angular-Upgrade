package com.param.billing.global.transaction.dao;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.IError;
import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.BillDetailsByBillIdDto;
import com.param.billing.global.transaction.dto.TDocShareVoucherDtlsDto;
import com.param.billing.global.transaction.dto.TDocShareVoucherDto;
import com.param.billing.global.transaction.dto.TDoctorReferralShareDto;
import com.param.billing.global.transaction.dto.TDoctorShareProcessReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.model.DoctorSlabVoucherDetails;
import com.param.billing.global.transaction.model.TDocShareVoucher;
import com.param.billing.global.transaction.model.TDoctorShare;
import com.param.billing.unit.dto.DoctorSlabVoucherDetailsDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TDoctorShareDaoImpl extends GenericDao<TDoctorShare, Integer> implements IDoctorShareDao, ICommonConstants, IError{

	public TDoctorShareDaoImpl() {
		super(TDoctorShare.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private IDocShareVoucherDao iDocShareVoucherDao;
	
	@Autowired
	private IDocShareVoucherDtlsDao  iDocShareVoucherDtlsDao;
	
	
	@Override
	public Response getDoctorShareList(TDoctorShareReqDto reqDto) throws ApplicationException {
		try{
			
			StringBuilder query = new StringBuilder("select " 
					 + " 	docshare.bill_dtl_id as \"billDtlId\", " 
					 + " 	docshare.ord_dtl_id as \"ordDtlId\", " 
					 + " 	docshare.visit_id as \"visitId\", " 
					 + " 	docshare.service_id as \"serviceId\", " 
					 + " 	docshare.doc_id as \"docId\", " 
					 + " 	docshare.doc_spec_id as \"docSpecId\", " 
					 + " 	docshare.org_id as \"orgId\", " 
					 + " 	docshare.org_unit_id as \"orgUnitId\", " 
					 + " 	docshare.is_bill_cr_dr as \"isBillCrDr\", " 
					 + " 	docshare.is_on_bill_payment as \"isOnBillPayment\", " 
					 + " 	docshare.is_active as \"isActive\", " 
					 + " 	docshare.doctor_share_id as \"doctorShareId\", " 
					 + " 	docshare.unit_rate as \"unitRate\", " 
					 + " 	docshare.total_amount as \"totalAmount\", " 
					 + " 	docshare.concession as \"concession\", " 
					 + " 	docshare.discount as \"discount\", " 
					 + " 	docshare.tax_amt as \"taxAmt\", " 
					 + " 	docshare.net_amount as \"netAmount\", " 
					 + " 	coalesce( case when enc.encounter_id >0 then to_char(enc.encounter_date, 'DD/MM/YYYY HH24:MI:SS') end, case when adm.admission_id > 0 then  to_char(adm.created_date, 'DD/MM/YYYY HH24:MI:SS')end )as \"encounterDate\", " 
					 + " 	coalesce( case when enc.encounter_id >0 then (enc.encounter_number) end, case when adm.admission_id > 0 then (adm.admission_number) end ) as \"encounterNumber\", " 
					 + " 	to_char(bm.bill_date_time,'DD/MM/YYYY HH24:MI:SS') as \"billDateTime\", " 
					 + " 	bm.billing_master_id as \"billId\", " 
					 + " 	bm.bill_number as \"billNumber\", " 
					 + " 	pat.uhid_number as \"uhidNumber\", " 
					 + " 	bm.visit_type_id as \"visitTypeId\", " 
					 + " 	bm.patient_id as \"patientId\", " 
					 + " 	bm.billing_master_id as \"billingMasterId\", " 
					 + " 	visitmst.visit_type_code as \"visitTypeCode\", " 
					 + " 	concat(doc.first_name, ' ', doc.last_name) as \"doctorName\", " 
					 + " 	bd.ord_doc_spl_id as \"ordDocSplId\", " 
					 + " 	bd.quantity as \"quantity\", " 
					 + " 	spl.speciality_name as \"specialityName\", " 
					 + " 	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.last_name) as \"patientName\", " 
					 + " 	om.order_no as \"orderNo\", " 
					 + " 	docshare.share_per as \"sharePer\", " 
					 + " 	docshare.share_per as \"oldSharePer\", " 
					 + " 	docshare.due_share_amt as \"dueShareAmt\", " 
					 + " 	docshare.doc_share as \"docShare\", " 
					 + " 	docshare.total_doc_share as \"totalDocShare\", " 
					 + " 	coalesce(docshare.processed_share_amt, 0) as \"processedShareAmt\", " 
					 + " 	service.service_standard_code as \"serviceStandardCode\", " 
					 + " 	service.service_standard_name as \"serviceStandardName\", " 
					 + " 	coalesce(dslogs.doctor_share_split_id, 0) as \"doctorShareSplitId\", " 
					 + " 	docshare.tax_id as \"taxId\", " 
					 + " 	docshare.hosp_share as \"hospShare\" , " 
					 + " 	docshare.hosp_share as \"oldHospShare\" , " 
					 + " 	docshare.is_share_split as \"isShareSplit\","
					 + " 	( " 
					 + " 	select " 
					 + " 		coalesce(sum(cisd.disallowed_amount), 0) " 
					 + " 	from " 
					 + " 		billing.t_company_invoice_service_details cisd " 
					 + " 	inner join billing.t_company_invoice_details cid on " 
					 + " 		cid.company_invoice_detail_id = cisd.invoice_detail_id " 
					 + " 	where " 
					 + " 		cid.bill_id = bm.billing_master_id) as \"disallowedAmount\", " 
					 + " 	docshare.parent_doc_share_id as \"parentDocShareId\", " 
					 + " 	crnm.credit_note_no as \"creditNoteNo\", "
					 + "	od.service_rendered as \"serviceRendered\", "
					 + "	coalesce(docshare.cr_net_amount,0) as \"crNetAmount\" " 
					 + " from " 
					 + " 	billing.t_doctor_share docshare " 
					 + " inner join billing.t_billing_details bd on " 
					 + " 	bd.billing_details_id = docshare.bill_dtl_id " 
					 + " inner join billing.m_billing_master bm on " 
					 + " 	bm.billing_master_id = bd.billing_master_id and bm.bill_cancelled = 'N' " 
					 + " inner join public.t_order_details od on " 
					 + " 	od.order_details_id = docshare.ord_dtl_id " 
					 + " inner join public.t_order_master om on " 
					 + " 	om.order_id = od.order_id " 
					 + " inner join service.m_service_master service on " 
					 + " 	service.service_master_id = docshare.service_id " 
					 + " left join public.m_tax tax on " 
					 + " 	tax.id = docshare.tax_id " 
					 + " left join opd.t_encounter_master enc on " 
					 + " 	enc.encounter_id = bm.encounter_id " 
					 + " left join adt.t_admission adm on " 
					 + " 	adm.admission_id = bm.admission_id " 
					 + " inner join doctor.m_doctor_master doc on " 
					 + " 	doc.doctor_id = docshare.doc_id " 
					 + " inner join doctor.m_speciality_master spl on " 
					 + " 	spl.speciality_id = bd.ord_doc_spl_id " 
					 + " inner join patient.t_patient_registration pat on " 
					 + " 	pat.patient_id = bm.patient_id " 
					 + " left join m_prefix_master pre on "
					 + " 	pre.prefix_id = pat.prefix_id "
					 + " inner join public.m_visit_type_master visitmst on " 
					 + " 	visitmst.visit_type_id = bm.visit_type_id " 
					 + " left join billing.t_doctor_share_split_logs dslogs on " 
					 + " 	dslogs.doctor_share_id = docshare.doctor_share_id " 
					 + " 	and dslogs.doc_id = docshare.doc_id " 
					 + " left join billing.m_credit_note_master crnm on " 
					 + " 	crnm.credit_note_id = docshare.against_cr_id "
					 + " LEFT JOIN LATERAL ( "
					 + "    SELECT "
					 + "        payment_reciept_id, "
					 + "        date_of_reciept "
					 + "    FROM billing.t_payment_reciept_master tprm "
					 + "    WHERE tprm.billing_master_id = bm.billing_master_id "
					 + "      AND tprm.status = 'A' "
					 + "      AND tprm.reciept_cancelled = 'N' "
					 + "    ORDER BY date_of_reciept DESC "
					 + "    LIMIT 1 "
					 + ") reci ON TRUE " );
			
			if(reqDto.getCompanyId()!=null || reqDto.getAssociateCompanyId()!=null)
			{
				 query.append("inner join billing.t_bill_details_payee_mapper payee " 
						 	+ "on payee.bill_details_id = bd.billing_details_id " ); 
			}
			
			String conditions =	 
				  " where " 	
				 + " 	docshare.org_id =:orgId " 
				 + " 	and docshare.status = 'A' "
				 + " and docshare.referral_id is null " 
				 + " 	and docshare.org_unit_id =:orgUnitId "
				 + "	and od.is_dummy_service = false "
				 + "  and docshare.due_share_amt >0  "
				 + "	and docshare.doc_share<>coalesce(docshare.processed_share_amt, 0) ";
			query.append(conditions);

			if(reqDto.getVisitTypeId() != null)
				query.append(" AND docshare.visit_type_id =:visitTypeId ");

			if(reqDto.getDoctorIds() != null)
				query.append(" AND docshare.doc_id IN (:docIds)  ");

			if(reqDto.getServiceRendered()<2)
				query.append(" and od.service_rendered =:serviceRendered ");

			if(reqDto.getPatientId() != null)
				query.append(" AND bm.patient_id =:patientId ");

			if(reqDto.getCompanyId() != null)
				query.append(" AND payee.payee_id =:payeeId ");

			if(reqDto.getAssociateCompanyId() != null)
				query.append(" AND payee.associate_company_id =:associateCompanyId ");

			if(reqDto.getOrderServiceId() != null)
				query.append(" AND service.service_master_id =:serviceId ");

			if(reqDto.getBillNumber() != null && !reqDto.getBillNumber().isEmpty())
				query.append(" AND lower(bm.bill_number) like lower(:billNumber)");

			if(reqDto.getPatientName() != null && !reqDto.getPatientName().isEmpty())
				query.append( " AND (lower(pat.first_name) like lower(:patientName ) OR lower(pat.last_name) like lower(:patientName) OR lower(pat.uhid_number) like lower(:patientName)) ");
			
			if(reqDto.getSettlementOrInvoice().equals(1)) {
				
				query.append(" and bm.bill_status_id = 3 ");
//				query.append(" and  (case when bm.net_amount > 0  and reci.payment_reciept_id is not null then to_char(reci.date_of_reciept, 'YYYY-MM-DD') between :fromDate and :toDate else to_char(bm.bill_date_time , 'YYYY-MM-DD') between :fromDate and :toDate end )  order by bm.bill_number,doc.first_name");
				query.append(" AND ( ( bm.net_amount > 0 AND reci.payment_reciept_id IS NOT NULL AND reci.date_of_reciept >= CAST(:fromDate AS DATE) AND reci.date_of_reciept < CAST(:toDate AS DATE) + INTERVAL '1 day' ) " +
						" OR ( (bm.net_amount <= 0 OR reci.payment_reciept_id IS NULL) AND bm.bill_date_time >= CAST(:fromDate AS DATE) AND bm.bill_date_time < CAST(:toDate AS DATE) + INTERVAL '1 day')) " +
						" order by bm.bill_number,doc.first_name");

			}else {
				
				if(reqDto.getBillStatusId()==3)
				{
					query.append(" and bm.bill_status_id = 3 ");
				}else{
					query.append(reqDto.getBillStatusId()==1 ? " and bm.bill_status_id IN (1,2) " : " ");
				} 
				
//				query.append(" AND to_char(bm.bill_date_time,'YYYY-MM-DD') BETWEEN :fromDate AND :toDate order by bm.bill_number,doc.first_name ");
				query.append(" AND bm.bill_date_time >= CAST(:fromDate AS DATE) AND bm.bill_date_time < CAST(:toDate AS DATE) + INTERVAL '1 day' order by bm.bill_number,doc.first_name ");
			}
			
			 Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
				   .setParameter("orgId", reqDto.getOrgId())
				   .setParameter("orgUnitId", reqDto.getOrgUnitId());


			 if(reqDto.getVisitTypeId() != null)
				sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

			if(reqDto.getDoctorIds() != null)
				sqlQuery.setParameterList("docIds", reqDto.getDoctorIds());

			if(reqDto.getServiceRendered()<2)
				sqlQuery.setParameter("serviceRendered", reqDto.getServiceRendered());

			if(reqDto.getPatientId() != null)
				sqlQuery.setParameter("patientId", reqDto.getPatientId());

			if(reqDto.getCompanyId() != null)
				sqlQuery.setParameter("payeeId", reqDto.getCompanyId());

			if(reqDto.getAssociateCompanyId() != null)
				sqlQuery.setParameter("associateCompanyId", reqDto.getAssociateCompanyId());

			if(reqDto.getOrderServiceId() != null)
				sqlQuery.setParameter("serviceId", reqDto.getOrderServiceId());

			if(reqDto.getBillNumber() != null && !reqDto.getBillNumber().isEmpty())
				sqlQuery.setParameter("billNumber", "%" + reqDto.getBillNumber().toLowerCase() + "%");

			if(reqDto.getPatientName() != null && !reqDto.getPatientName().isEmpty())
				sqlQuery.setParameter("patientName", "%" + reqDto.getPatientName().toLowerCase() + "%");

			if(reqDto.getSettlementOrInvoice().equals(1)) {
				sqlQuery.setParameter("fromDate", reqDto.getFromDate().substring(0,10));
				sqlQuery.setParameter("toDate", reqDto.getToDate().substring(0,10));
			}
			else{
				sqlQuery.setParameter("fromDate", reqDto.getFromDate().substring(0,10));
				sqlQuery.setParameter("toDate", reqDto.getToDate().substring(0,10));
			}


			List<TDoctorShareRespDto> listTDoctorShareRespDto = sqlQuery
				   .setFirstResult(reqDto.getOffset() != null ? reqDto.getOffset() : 0)
				   .setMaxResults(reqDto.getRecordPerPage() != null ? reqDto.getRecordPerPage() : 10)
				   .setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();
			 
			 if(listTDoctorShareRespDto !=null && listTDoctorShareRespDto.size()>0){
				 List<Integer> orderDetailsIds = listTDoctorShareRespDto.stream().map(TDoctorShareRespDto::getOrdDtlId).collect(Collectors.toList());
				 
					List<TDoctorShareRespDto> listr =  sessionFactory.getCurrentSession()
							.createSQLQuery(
									" select sum(docref.doc_share) as \"refDocShare\","
									+ " docref.doctor_share_id as \"refDocShareId\", "
									+ " docref.referral_id  as \"referralId\", "
									+ " sum(docref.processed_share_amt) as \"refProcessedShareAmt\","
									+ " docref.ord_dtl_id as \"ordDtlId\" " 
									+ " from"
									+ " billing.t_doctor_share docref"
									+ " where"
									+ " docref.doc_share > 0"
									+ " and docref.referral_id is not null"
									+ " and docref.status = 'A'"
									+ " and docref.org_id =:orgId "
									+ " and docref.ord_dtl_id in(:orderDetailsIds) "
									+ " group by"
									+ " docref.doctor_share_id ,"
									+ " docref.referral_id ,"
									+ " docref.ord_dtl_id  ")
							.setParameter("orgId", reqDto.getOrgId())
							.setParameterList("orderDetailsIds", orderDetailsIds)
							.setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();
					
					if(listr != null && listr.size()>0){
						
						for (TDoctorShareRespDto dto : listTDoctorShareRespDto) {
							
							TDoctorShareRespDto detailsDto = listr.stream()
									  .filter(order -> dto.getOrdDtlId().equals(order.getOrdDtlId()))
									  .findAny()
									  .orElse(null);
							
							if(detailsDto != null ){
								
								dto.setRefDocShare(detailsDto.getRefDocShare());
								dto.setRefDocShareId(detailsDto.getRefDocShareId());
								dto.setReferralId(detailsDto.getReferralId());
								dto.setRefProcessedShareAmt(detailsDto.getRefProcessedShareAmt());
								
							}
							
						}
						
					}
					
			 }
			 
			 
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTDoctorShareRespDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDoctorShareListCount(TDoctorShareReqDto reqDto) throws ApplicationException {
		try{

			StringBuilder query = new StringBuilder("select count(*) "
					 + " from "
					 + " 	billing.t_doctor_share docshare "
					 + " inner join billing.t_billing_details bd on "
					 + " 	bd.billing_details_id = docshare.bill_dtl_id "
					 + " inner join billing.m_billing_master bm on "
					 + " 	bm.billing_master_id = bd.billing_master_id and bm.bill_cancelled = 'N' "
					 + " inner join public.t_order_details od on "
					 + " 	od.order_details_id = docshare.ord_dtl_id "
					 + " inner join public.t_order_master om on "
					 + " 	om.order_id = od.order_id "
					 + " inner join service.m_service_master service on "
					 + " 	service.service_master_id = docshare.service_id "
					 + " left join public.m_tax tax on "
					 + " 	tax.id = docshare.tax_id "
					 + " left join opd.t_encounter_master enc on "
					 + " 	enc.encounter_id = bm.encounter_id "
					 + " left join adt.t_admission adm on "
					 + " 	adm.admission_id = bm.admission_id "
					 + " inner join doctor.m_doctor_master doc on "
					 + " 	doc.doctor_id = docshare.doc_id "
					 + " inner join doctor.m_speciality_master spl on "
					 + " 	spl.speciality_id = bd.ord_doc_spl_id "
					 + " inner join patient.t_patient_registration pat on "
					 + " 	pat.patient_id = bm.patient_id "
					 + " left join m_prefix_master pre on "
					 + " 	pre.prefix_id = pat.prefix_id "
					 + " inner join public.m_visit_type_master visitmst on "
					 + " 	visitmst.visit_type_id = bm.visit_type_id "
					 + " left join billing.t_doctor_share_split_logs dslogs on "
					 + " 	dslogs.doctor_share_id = docshare.doctor_share_id "
					 + " 	and dslogs.doc_id = docshare.doc_id "
					 + " left join billing.m_credit_note_master crnm on "
					 + " 	crnm.credit_note_id = docshare.against_cr_id "
					 + " left join " + 
					 "( select max(tprm.payment_reciept_id) id ,tprm.billing_master_id , tprm.payment_reciept_id,tprm.date_of_reciept " + 
					 "from billing.t_payment_reciept_master tprm " + 
					 "where tprm.status = 'A' and tprm.reciept_cancelled = 'N' " + 
					 "group by tprm.billing_master_id ,tprm.payment_reciept_id,tprm.date_of_reciept " + 
					 ")reci  on reci.billing_master_id = bm.billing_master_id "
					  );

			if(reqDto.getCompanyId()!=null || reqDto.getAssociateCompanyId()!=null)
			{
				 query.append("inner join billing.t_bill_details_payee_mapper payee "
						 	+ "on payee.bill_details_id = bd.billing_details_id " );
			}

			String conditions =
				  " where "
				 + " 	docshare.org_id =:orgId "
				 + " 	and docshare.status = 'A' "
				 + " and docshare.referral_id is null "
				 + " 	and docshare.org_unit_id =:orgUnitId "
				 + "	and od.is_dummy_service = false "
				 + "	and docshare.doc_share<>coalesce(docshare.processed_share_amt, 0) ";
			query.append(conditions);

			if(reqDto.getVisitTypeId() != null)
				query.append(" AND docshare.visit_type_id =:visitTypeId ");

			if(reqDto.getDoctorIds() != null)
				query.append(" AND docshare.doc_id IN (:docIds)  ");

			if(reqDto.getServiceRendered()<2)
				query.append(" and od.service_rendered =:serviceRendered ");

			if(reqDto.getPatientId() != null)
				query.append(" AND bm.patient_id =:patientId ");

			if(reqDto.getCompanyId() != null)
				query.append(" AND payee.payee_id =:payeeId ");

			if(reqDto.getAssociateCompanyId() != null)
				query.append(" AND payee.associate_company_id =:associateCompanyId ");

			if(reqDto.getOrderServiceId() != null)
				query.append(" AND service.service_master_id =:serviceId ");

			if(reqDto.getBillNumber() != null && !reqDto.getBillNumber().isEmpty())
				query.append(" AND lower(bm.bill_number) like lower(:billNumber)");


			if(reqDto.getPatientName() != null && !reqDto.getPatientName().isEmpty())
				query.append(" AND (lower(pat.first_name) like lower(:patientName) OR lower(pat.last_name) like lower(:patientName) OR lower(pat.uhid_number) like lower(:patientName)) ");

			if(reqDto.getSettlementOrInvoice().equals(1)) {

				query.append(" and bm.bill_status_id = 3 ");
				query.append(" and  (case when bm.net_amount > 0  and reci.payment_reciept_id is not null then to_char(reci.date_of_reciept, 'YYYY-MM-DD') between :fromDate and :toDate else to_char(bm.bill_date_time , 'YYYY-MM-DD') between :fromDate and :toDate end )  ");

			}else {

				if(reqDto.getBillStatusId()==3)
				{
					query.append(" and bm.bill_status_id = 3 ");
				}else{
					query.append(reqDto.getBillStatusId()==1 ? " and bm.bill_status_id IN (1,2) " : " ");
				}

				query.append(" AND to_char(bm.bill_date_time,'YYYY-MM-DD') BETWEEN :fromDate AND :toDate " );
			}


			 Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
				   .setParameter("orgId", reqDto.getOrgId())
				   .setParameter("orgUnitId", reqDto.getOrgUnitId());

			if(reqDto.getVisitTypeId() != null)
				sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

			if(reqDto.getDoctorIds() != null)
				sqlQuery.setParameterList("docIds", reqDto.getDoctorIds());

			if(reqDto.getServiceRendered()<2)
				sqlQuery.setParameter("serviceRendered", reqDto.getServiceRendered());

			if(reqDto.getPatientId() != null)
				sqlQuery.setParameter("patientId", reqDto.getPatientId());

			if(reqDto.getCompanyId() != null)
				sqlQuery.setParameter("payeeId", reqDto.getCompanyId());

			if(reqDto.getAssociateCompanyId() != null)
				sqlQuery.setParameter("associateCompanyId", reqDto.getAssociateCompanyId());

			if(reqDto.getOrderServiceId() != null)
				sqlQuery.setParameter("serviceId", reqDto.getOrderServiceId());

			if(reqDto.getBillNumber() != null && !reqDto.getBillNumber().isEmpty())
				sqlQuery.setParameter("billNumber", "%" + reqDto.getBillNumber().toLowerCase() + "%");

			if(reqDto.getPatientName() != null && !reqDto.getPatientName().isEmpty())
				sqlQuery.setParameter("patientName", "%" + reqDto.getPatientName().toLowerCase() + "%");

			if(reqDto.getSettlementOrInvoice().equals(1)) {
				sqlQuery.setParameter("fromDate", reqDto.getFromDate().substring(0,10));
				sqlQuery.setParameter("toDate", reqDto.getToDate().substring(0,10));
			}
			else{
				sqlQuery.setParameter("fromDate", reqDto.getFromDate().substring(0,10));
				sqlQuery.setParameter("toDate", reqDto.getToDate().substring(0,10));
			}

			Object ListCount = sqlQuery.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, ListCount);
		}catch(Exception e){

			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getDoctorReferralList(TDoctorShareReqDto reqDto) throws ApplicationException {
		try{
			
			StringBuilder query = new StringBuilder(" select " 
					 + " 	docshare.bill_dtl_id as \"billDtlId\", " 
					 + " 	docshare.ord_dtl_id as \"ordDtlId\", " 
					 + " 	docshare.visit_id as \"visitId\", " 
					 + " 	docshare.service_id as \"serviceId\", " 
					 + " 	referral.referral_id as \"referralId\", " 
					 + " 	docshare.org_id as \"orgId\", " 
					 + " 	docshare.org_unit_id as \"orgUnitId\", " 
					 + " 	docshare.is_bill_cr_dr as \"isBillCrDr\", " 
					 + " 	docshare.is_on_bill_payment as \"isOnBillPayment\", " 
					 + " 	docshare.is_active as \"isActive\", " 
					 + " 	docshare.doctor_share_id as \"doctorShareId\", " 
					 + " 	docshare.unit_rate as \"unitRate\", " 
					 + " 	docshare.total_amount as \"totalAmount\", " 
					 + " 	docshare.concession as \"concession\", " 
					 + " 	docshare.discount as \"discount\", " 
					 + " 	docshare.tax_amt as \"taxAmt\", " 
					 + " 	docshare.net_amount as \"netAmount\", " 
					 + " 	coalesce( case when enc.encounter_id >0 then to_char(enc.encounter_date, 'DD/MM/YYYY HH24:MI:SS') end, case when adm.admission_id > 0 then  to_char(adm.created_date, 'DD/MM/YYYY HH24:MI:SS')end )as \"encounterDate\", " 
					 + " 	coalesce( case when enc.encounter_id >0 then (enc.encounter_number) end, case when adm.admission_id > 0 then (adm.admission_number) end ) as \"encounterNumber\", " 
					 + " 	to_char(bm.bill_date_time,'DD/MM/YYYY HH24:MI:SS') as \"billDateTime\", "  
					 + " 	bm.billing_master_id as \"billId\", " 
					 + " 	bm.bill_number as \"billNumber\", " 
					 + " 	pat.uhid_number as \"uhidNumber\", " 
					 + " 	bm.visit_type_id as \"visitTypeId\", " 
					 + " 	bm.patient_id as \"patientId\", " 
					 + " 	bm.billing_master_id as \"billingMasterId\", " 
					 + " 	visitmst.visit_type_code as \"visitTypeCode\", " 
					 + " 	referral.referral_name as \"doctorName\", "
					 + " 	referral.referral_type_id as \"referralTypeId\", "
					 + " 	bd.quantity as \"quantity\", " 
					 + " 	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.last_name) as \"patientName\", " 
					 + " 	om.order_no as \"orderNo\", " 
					 + " 	docshare.share_per as \"sharePer\", " 
					 + " 	docshare.share_per as \"oldSharePer\", " 
					 + " 	docshare.due_share_amt as \"dueShareAmt\", " 
					 + " 	docshare.due_share_amt as \"oldDueShareAmt\", " 
					 + " 	docshare.doc_share as \"docShare\", "
					 + " 	docshare.doc_share as \"oldDocShare\", "
					 + " 	docshare.total_doc_share as \"totalDocShare\", " 
					 + " 	coalesce(docshare.processed_share_amt, 0) as \"processedShareAmt\", " 
					 + " 	service.service_standard_code as \"serviceStandardCode\", " 
					 + " 	service.service_standard_name as \"serviceStandardName\", " 
					 + " 	coalesce(dslogs.doctor_share_split_id, 0) as \"doctorShareSplitId\", " 
					 + " 	docshare.tax_id as \"taxId\", " 
					 + " 	docshare.hosp_share as \"hospShare\" , " 
					 + " 	docshare.hosp_share as \"oldHospShare\" , " 
					 + " 	docshare.is_share_split as \"isShareSplit\", " 
					 + "    docshare.doctor_share_id as \"docDocShareId\", "
					 + " 	(select " 
					 + " 		coalesce(sum(cisd.disallowed_amount), 0) " 
					 + " 	from " 
					 + " 		billing.t_company_invoice_service_details cisd " 
					 + " 	inner join billing.t_company_invoice_details cid on " 
					 + " 		cid.company_invoice_detail_id = cisd.invoice_detail_id " 
					 + " 	where " 
					 + " 		cid.bill_id = bm.billing_master_id) as \"disallowedAmount\", " 
					 + " 	docshare.parent_doc_share_id as \"parentDocShareId\", "
					 + " 	crnm.credit_note_no as \"creditNoteNo\","
					 + "	od.service_rendered as \"serviceRendered\", "
					 + "	coalesce(docshare.cr_net_amount,0) as \"crNetAmount\" " 
					 + " from " 
					 + " 	billing.t_doctor_share docshare " 
					 + " inner join billing.t_billing_details bd on " 
					 + " 	bd.billing_details_id = docshare.bill_dtl_id " 
					 + " inner join billing.m_billing_master bm on " 
					 + " 	bm.billing_master_id = bd.billing_master_id and bm.bill_cancelled = 'N' " 
					 + " inner join public.t_order_details od on " 
					 + " 	od.order_details_id = docshare.ord_dtl_id " 
					 + " inner join public.t_order_master om on " 
					 + " 	om.order_id = od.order_id " 
					 + " inner join service.m_service_master service on " 
					 + " 	service.service_master_id = docshare.service_id " 
					 + " left join public.m_tax tax on " 
					 + " 	tax.id = docshare.tax_id " 
					 + " left join opd.t_encounter_master enc on " 
					 + " 	enc.encounter_id = bm.encounter_id " 
					 + " left join adt.t_admission adm on " 
					 + " 	adm.admission_id = bm.admission_id " 
					 + " inner join adt.m_referral_master referral on " 
					 + " 	referral.referral_id = docshare.referral_id  " 
					 + " inner join patient.t_patient_registration pat on " 
					 + " 	pat.patient_id = bm.patient_id " 
					 + " inner join public.m_visit_type_master visitmst on " 
					 + " 	visitmst.visit_type_id = bm.visit_type_id " 
					 + " left join billing.t_doctor_share_split_logs dslogs on " 
					 + " 	dslogs.doctor_share_id = docshare.doctor_share_id " 
					 + " 	and dslogs.doc_id = docshare.doc_id " 
					 + " left join billing.m_credit_note_master crnm on " 
					 + " 	crnm.credit_note_id = docshare.against_cr_id "
					 + " left join m_prefix_master pre on "
					 + " 	pre.prefix_id = pat.prefix_id " );
					 
		 			if(reqDto.getCompanyId()!=null || reqDto.getAssociateCompanyId()!=null)
					{
						 query.append("inner join billing.t_bill_details_payee_mapper payee " 
								 	+ "on payee.bill_details_id = bd.billing_details_id " ); 
					}
					
					query.append(" where " 
							 + " 	docshare.org_id =:orgId " 
							 + " 	and docshare.status = 'A' " 
							 + " 	and docshare.org_unit_id =:orgUnitId "
							 + "	and od.is_dummy_service = false "
							 + "	and docshare.doc_share<>coalesce(docshare.processed_share_amt, 0) ");


			if(reqDto.getVisitTypeId() != null)
				query.append(" AND docshare.visit_type_id =:visitTypeId ");

			if(reqDto.getReferralIds() != null)
				query.append(" AND docshare.referral_id IN (:referralIds) ");


			if(reqDto.getBillStatusId()==3)
			{
				query.append(" and bm.bill_status_id = 3 ");
			}else{
				query.append(reqDto.getBillStatusId()==1 ? " and bm.bill_status_id IN (1,2) " : " ");
			}

			if(reqDto.getServiceRendered()<2)
				query.append(" and od.service_rendered =:serviceRendered ");

			if(reqDto.getPatientId() != null)
				query.append(" AND bm.patient_id =:patientId ");

			if(reqDto.getCompanyId() != null)
				query.append(" AND payee.payee_id =:payeeId ");

			if(reqDto.getAssociateCompanyId() != null)
				query.append(" AND payee.associate_company_id =:associateCompanyId ");

			if(reqDto.getOrderServiceId() != null)
				query.append(" AND service.service_master_id =:serviceId ");

			if(reqDto.getBillNumber() != null && !reqDto.getBillNumber().isEmpty())
				query.append(" AND lower(bm.bill_number) like lower(:billNumber)");

			query.append(" AND to_char(bm.bill_date_time,'YYYY-MM-DD') BETWEEN :fromDate AND :toDate  order by bm.bill_number,referral.referral_name ");
		   
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
				   .setParameter("orgId", reqDto.getOrgId())
				   .setParameter("orgUnitId", reqDto.getOrgUnitId())
				   .setParameter("fromDate", reqDto.getFromDate().substring(0,10))
				   .setParameter("toDate", reqDto.getToDate().substring(0,10));

			if(reqDto.getVisitTypeId() != null)
				sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

			if(reqDto.getReferralIds() != null)
				sqlQuery.setParameterList("referralIds", reqDto.getReferralIds());

			if(reqDto.getServiceRendered()<2)
				sqlQuery.setParameter("serviceRendered", reqDto.getServiceRendered());

			if(reqDto.getPatientId() != null)
				sqlQuery.setParameter("patientId", reqDto.getPatientId());

			if(reqDto.getCompanyId() != null)
				sqlQuery.setParameter("payeeId", reqDto.getCompanyId());

			if(reqDto.getAssociateCompanyId() != null)
				sqlQuery.setParameter("associateCompanyId", reqDto.getAssociateCompanyId());

			if(reqDto.getOrderServiceId() != null)
				sqlQuery.setParameter("serviceId", reqDto.getOrderServiceId());

			if(reqDto.getBillNumber() != null && !reqDto.getBillNumber().isEmpty())
				sqlQuery.setParameter("billNumber", "%" + reqDto.getBillNumber().toLowerCase() + "%");


			List<TDoctorShareRespDto> listTDoctorShareRespDto =	sqlQuery.setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTDoctorShareRespDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response saveDoctorShareList(TDoctorShareProcessReqDto reqDto) throws ApplicationException {
		try{
			if(reqDto.getListtDoctorShareRespDto() !=null && !reqDto.getListtDoctorShareRespDto().isEmpty()) {
				for (TDoctorShareRespDto tDoctorShareRespDto : reqDto.getListtDoctorShareRespDto()) {
					sessionFactory.getCurrentSession().createSQLQuery("update "
									+ "    billing.t_doctor_share set "
									+ " 	processed_share_amt = coalesce(processed_share_amt, 0)  + :processedShareAmt, "
									+ "		due_share_amt =:dueShareAmt ,"
									+ " 	updated_by =:updatedBy, "
									+ " 	updated_date = now(), "
									+ " 	processed_share_date = now(), "
									+ " 	due_share_date = now(), "
									+ " 	remark =:remark "
									+ " where "
									+ " 	doctor_share_id =:doctorShareId ;")
							.setParameter("processedShareAmt", tDoctorShareRespDto.getProcessedShareAmt())
							.setParameter("dueShareAmt", tDoctorShareRespDto.getDueShareAmt())
							.setParameter("updatedBy", reqDto.getUserId())
							.setParameter("remark", tDoctorShareRespDto.getRemark())
							.setParameter("doctorShareId", tDoctorShareRespDto.getDoctorShareId())
							.executeUpdate();
				}

				TDocShareVoucherDto docShareVoucherDto = reqDto.gettDocShareVoucherDto();
				if (docShareVoucherDto != null && (reqDto.getListTDocShareVoucherDtlsDto() != null && reqDto.getListTDocShareVoucherDtlsDto().size() > 0)) {
					docShareVoucherDto.setOrgId(reqDto.getOrgId());
					docShareVoucherDto.setOrgUnitId(reqDto.getUnitId());
					docShareVoucherDto.setType(1);
					Response docShareVoucherRes = iDocShareVoucherDao.saveDocShareVoucher(docShareVoucherDto);
					if (docShareVoucherRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && docShareVoucherRes.getObject() != null) {
						TDocShareVoucher tDocShareVoucher = (TDocShareVoucher) docShareVoucherRes.getObject();
						Integer docShareVoucheId = tDocShareVoucher.getDocShareVoucheId();
						ListIterator<TDocShareVoucherDtlsDto> servicDtlsItr = reqDto.getListTDocShareVoucherDtlsDto().listIterator();
						TDocShareVoucherDtlsDto tDocShareVoucherDtlsDto = null;
						while (servicDtlsItr.hasNext()) {
							tDocShareVoucherDtlsDto = servicDtlsItr.next();
							tDocShareVoucherDtlsDto.setDocShareVoucheId(docShareVoucheId);
							tDocShareVoucherDtlsDto.setOrgId(reqDto.getOrgId());
							tDocShareVoucherDtlsDto.setOrgUnitId(reqDto.getUnitId());
							iDocShareVoucherDtlsDao.saveDocShareVoucherDtls(tDocShareVoucherDtlsDto);
						}
					}
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, ERROR_MESSAGE, null, null);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	
	@Override
	public Response getDoctorSharePaymentVoucherList(TDoctorShareReqDto reqDto) throws ApplicationException {
		try{
			
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_SHARE_PAYMENT_VOUCHER_LIST").getQueryString());
			  //query.append(reqDto.getDoctorId() != null ? " AND docMst.doctor_id ='"+reqDto.getDoctorId()+"'"  : " ");
			 // query.append(" AND to_timestamp(to_char(docShareVoucher.created_date,'YYYY-MM-DD'),'YYYY-MM-DD') BETWEEN '"+reqDto.getFromDate() +"' AND '"+reqDto.getToDate() +"'");
		   List<TDoctorShareRespDto> listTDoctorShareRespDto= sessionFactory.getCurrentSession().createSQLQuery(query.toString())
				   .setParameter("orgId", reqDto.getOrgId())
				   .setParameter("orgUnitId", reqDto.getOrgUnitId())
				   .setParameter("doctorId", reqDto.getDoctorId())
				   .setParameter("fromDate", GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-MM-yyyy"))
				   .setParameter("toDate", GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-MM-yyyy"))
				   .setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTDoctorShareRespDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	@Override
	public Response getDoctorRefferalPaymentVoucherList(TDoctorShareReqDto reqDto) throws ApplicationException {
		try{
			
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_REFERRAL_PAYMENT_VOUCHER_LIST").getQueryString());
			  //query.append(reqDto.getDoctorId() != null ? " AND docMst.doctor_id ='"+reqDto.getDoctorId()+"'"  : " ");
			 // query.append(" AND to_timestamp(to_char(docShareVoucher.created_date,'YYYY-MM-DD'),'YYYY-MM-DD') BETWEEN '"+reqDto.getFromDate() +"' AND '"+reqDto.getToDate() +"'");
		   List<TDoctorShareRespDto> listTDoctorShareRespDto= sessionFactory.getCurrentSession().createSQLQuery(query.toString())
				   .setParameter("orgId", reqDto.getOrgId())
				   .setParameter("orgUnitId", reqDto.getOrgUnitId())
				   .setParameter("referralId", reqDto.getReferralId())
				   .setParameter("fromDate", GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-MM-yyyy"))
				   .setParameter("toDate", GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-MM-yyyy"))
				   .setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTDoctorShareRespDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
		
	}

	@Override
	public Response getDoctorSharePaymentVoucherDtlsByDoctorShareId(TDoctorShareReqDto reqDto)
			throws ApplicationException {
		try{
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_SHARE_PAYMENT_VOUCHER_DTLS_BY_DOC_SHARE_ID").getQueryString());
		   List<TDoctorShareRespDto> listTDoctorShareRespDto= sessionFactory.getCurrentSession().createSQLQuery(query.toString())
				   .setParameter("orgId", reqDto.getOrgId())
				   .setParameter("orgUnitId", reqDto.getOrgUnitId())
				   .setParameter("docShareVoucherId", reqDto.getDocShareVoucherId())
				   .setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTDoctorShareRespDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response saveDoShareObject(TDoctorShare tDoctorShareRespDto)
			throws ApplicationException {
		try{
				save(tDoctorShareRespDto);
				
				sessionFactory.getCurrentSession().createSQLQuery("update "
						+"  billing.t_doctor_share_split_logs set "
						+" 	share_amount =:shareAmount "
						+"  where "
						+" 	doctor_share_id =:doctorShareId ")
						.setParameter("shareAmount", tDoctorShareRespDto.getDocShare())
						.setParameter("doctorShareId", tDoctorShareRespDto.getDoctorShareId())
				.executeUpdate();
				
				
				
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
	}

	//Save doctor share data though cron job
	@Override
	public com.param.global.common.Response saveDoctorShareData(TDoctorShareRespDto tDoctorShareRespDto) throws ApplicationException {
		try {
				Integer docShareId = null;
			    if(tDoctorShareRespDto.getDocId()!=null ) 
			    {
			    	TDoctorShare doctorShare = new TDoctorShare();
			    	doctorShare.setAdmissionId(tDoctorShareRespDto.getAdmissionId());
					doctorShare.setOrdDtlId(tDoctorShareRespDto.getOrdDtlId());
					doctorShare.setCreatedBy(tDoctorShareRespDto.getCreatedBy());
					doctorShare.setUpdatedDate(TimeZoneComponent.getDateByzone(tDoctorShareRespDto.getOrgUnitId()).getTime());
					doctorShare.setDocId(tDoctorShareRespDto.getDocId());
					doctorShare.setDocShare(tDoctorShareRespDto.getDocShare());
					doctorShare.setDocSpecId(tDoctorShareRespDto.getDocSpecId());
					doctorShare.setConcession(tDoctorShareRespDto.getConcession());
					doctorShare.setHospShare(tDoctorShareRespDto.getHospShare());
					doctorShare.setBillDtlId(tDoctorShareRespDto.getBillDtlId());
					doctorShare.setVisitId(tDoctorShareRespDto.getVisitId());
					doctorShare.setAdmissionId(tDoctorShareRespDto.getAdmissionId());
					doctorShare.setVisitTypeId(tDoctorShareRespDto.getVisitTypeId());
					doctorShare.setServiceId(tDoctorShareRespDto.getServiceId());
					doctorShare.setUnitRate(tDoctorShareRespDto.getUnitRate());
					doctorShare.setDueShareAmt(tDoctorShareRespDto.getDueShareAmt());
					doctorShare.setDueShareDate(TimeZoneComponent.getDateByzone(tDoctorShareRespDto.getOrgUnitId()).getTime());
					doctorShare.setTotalAmount(tDoctorShareRespDto.getTotalAmount());
					doctorShare.setDiscount(tDoctorShareRespDto.getDiscount());
					doctorShare.setTaxAmt(tDoctorShareRespDto.getTaxAmt());
					doctorShare.setStatus(ACTIVE);
					doctorShare.setNetAmount(tDoctorShareRespDto.getNetAmount());
					doctorShare.setTaxId(tDoctorShareRespDto.getTaxId());
					doctorShare.setTaxType(tDoctorShareRespDto.getTaxType());
					doctorShare.setProcessedShareAmt(tDoctorShareRespDto.getProcessedShareAmt());
					doctorShare.setIsActive(tDoctorShareRespDto.getIsActive());
					doctorShare.setIsBillCrDr(tDoctorShareRespDto.getIsBillCrDr());
					doctorShare.setIsOnBillPayment(tDoctorShareRespDto.getIsOnBillPayment());
					doctorShare.setOrgId(tDoctorShareRespDto.getOrgId());
					doctorShare.setOrgUnitId(tDoctorShareRespDto.getOrgUnitId());
					doctorShare.setProcessedShareDate(TimeZoneComponent.getDateByzone(tDoctorShareRespDto.getOrgUnitId()).getTime());
					doctorShare.setSharePer(tDoctorShareRespDto.getSharePer());
					doctorShare.setIsShareSplit(tDoctorShareRespDto.getIsShareSplit());
					doctorShare.setTotalDocShare(tDoctorShareRespDto.getTotalDocShare());
					doctorShare.setAgainstCrId(tDoctorShareRespDto.getAgainstCrId());
					doctorShare.setCrNetAmount(tDoctorShareRespDto.getCrNetAmount());
					doctorShare = save(doctorShare);
					docShareId = doctorShare.getDoctorShareId();
			    }if(tDoctorShareRespDto.getReferralId()!=null ) 
			    {
			    	TDoctorShare doctorShare = new TDoctorShare();
			    	doctorShare.setAdmissionId(tDoctorShareRespDto.getAdmissionId());
					doctorShare.setOrdDtlId(tDoctorShareRespDto.getOrdDtlId());
					doctorShare.setCreatedBy(tDoctorShareRespDto.getCreatedBy());
					doctorShare.setUpdatedDate(TimeZoneComponent.getDateByzone(tDoctorShareRespDto.getOrgUnitId()).getTime());
					doctorShare.setReferralId(tDoctorShareRespDto.getReferralId());
					doctorShare.setDocShare(tDoctorShareRespDto.getDocShare());
					doctorShare.setConcession(tDoctorShareRespDto.getConcession());
					doctorShare.setHospShare(tDoctorShareRespDto.getHospShare());
					doctorShare.setBillDtlId(tDoctorShareRespDto.getBillDtlId());
					doctorShare.setVisitId(tDoctorShareRespDto.getVisitId());
					doctorShare.setAdmissionId(tDoctorShareRespDto.getAdmissionId());
					doctorShare.setVisitTypeId(tDoctorShareRespDto.getVisitTypeId());
					doctorShare.setServiceId(tDoctorShareRespDto.getServiceId());
					doctorShare.setUnitRate(tDoctorShareRespDto.getUnitRate());
					doctorShare.setDueShareAmt(tDoctorShareRespDto.getDueShareAmt());
					doctorShare.setDueShareDate(TimeZoneComponent.getDateByzone(tDoctorShareRespDto.getOrgUnitId()).getTime());
					doctorShare.setTotalAmount(tDoctorShareRespDto.getTotalAmount());
					doctorShare.setDiscount(tDoctorShareRespDto.getDiscount());
					doctorShare.setTaxAmt(tDoctorShareRespDto.getTaxAmt());
					doctorShare.setStatus(ACTIVE);
					doctorShare.setNetAmount(tDoctorShareRespDto.getNetAmount());
					doctorShare.setTaxId(tDoctorShareRespDto.getTaxId());
					doctorShare.setTaxType(tDoctorShareRespDto.getTaxType());
					doctorShare.setProcessedShareAmt(tDoctorShareRespDto.getProcessedShareAmt());
					doctorShare.setIsActive(tDoctorShareRespDto.getIsActive());
					doctorShare.setIsBillCrDr(tDoctorShareRespDto.getIsBillCrDr());
					doctorShare.setIsOnBillPayment(tDoctorShareRespDto.getIsOnBillPayment());
					doctorShare.setOrgId(tDoctorShareRespDto.getOrgId());
					doctorShare.setOrgUnitId(tDoctorShareRespDto.getOrgUnitId());
					doctorShare.setProcessedShareDate(TimeZoneComponent.getDateByzone(tDoctorShareRespDto.getOrgUnitId()).getTime());
					doctorShare.setSharePer(tDoctorShareRespDto.getSharePer());
					doctorShare.setIsShareSplit(tDoctorShareRespDto.getIsShareSplit());
					doctorShare.setTotalDocShare(tDoctorShareRespDto.getTotalDocShare());
					doctorShare.setAgainstCrId(tDoctorShareRespDto.getAgainstCrId());
					doctorShare.setCrNetAmount(tDoctorShareRespDto.getCrNetAmount());
					doctorShare = save(doctorShare);
					docShareId = doctorShare.getDoctorShareId();
			    }
			return new com.param.global.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, docShareId);
			
		}catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public com.param.global.common.Response getDataFromBillingMst(int startPoint, int maxResults) throws ApplicationException {
		
		StatelessSession session = null;
		LocalDate date = LocalDate.now().minusDays(1);
		Date todayDate = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		try{
			session = sessionFactory.openStatelessSession();
			 StringBuilder query = new StringBuilder(session.getNamedQuery("GET_DATA_FROM_BILLING_DTLS").getQueryString());
					query.append(" WHERE to_char(bilmst.bill_date_time,'DD-MM-YYYY') =:todayDate ");
					query.append(" AND bilmst.is_doctor_referral_posted='N'"
							  +" GROUP BY bilmst.billing_master_id,"
							  + " trefd.referral_id");
					 List<TDoctorShareRespDto> listTDoctorShareRespDto= session.createSQLQuery(query.toString())
					.setParameter("todayDate", GlobalCommonDateUtils.getStringDate(todayDate, "dd-MM-yyyy"))
					.setFirstResult(startPoint).setMaxResults(maxResults)
					.setResultTransformer(Transformers.aliasToBean(TDoctorReferralShareDto.class)).list();
			return new com.param.global.common.Response<>(SUCCESS, SUCCESS_CODE, null, listTDoctorShareRespDto, null);
		}catch(Exception e){
			
			throw e;
		}finally{
			if(session != null)
				session.close();
		}
	}

	@Override
	public com.param.global.common.Response getDataFromBillingDtls(int startPoint, int maxResults,int unitId)
			throws ApplicationException {
		
		StatelessSession session = null;

		try{
			
			String query = " select  " 
					 + " 	bdtls.billing_details_id as \"billDtlId\", " 
					 + " 	bdtls.order_details_id as \"ordDtlId\", " 
					 + " 	bdtls.net_amt as \"totalAmount\", "
					 + "    bdtls.package_consumption_amt as \"packageConsumptionAmt\" ," 
					 + " 	bdtls.rate as \"unitRate\", " 
					 + " 	bdtls.quantity as \"quantity\", " 
					 + " 	bdtls.concession as \"concession\", " 
					 + " 	bdtls.service_id as \"serviceId\", " 
					 + " 	bdtls.unit_id as \"orgUnitId\", " 
					 + " 	bdtls.organization_id as \"orgId\", " 
					 + " 	bdtls.discount as \"discount\", " 
					 + " 	bdtls.tax_id as \"taxId\", " 
					 + " 	coalesce(mt.tax_percentage,0) as \"taxPer\", "
					 + " 	bdtls.net_amt as \"netAmount\", " 
					 + " 	bm.encounter_id as \"visitId\", " 
					 + " 	bm.admission_id as \"admissionId\", " 
					 + " 	bdtls.tax_amount as \"taxAmt\", " 
					 + " 	bm.visit_type_id as \"visitTypeId\", " 
					 + " 	od.ord_doc_id as \"docId\", " 
					 + " 	od.ord_doc_spl_id as \"docSpecId\", " 
					 + " 	coalesce(cast(od.ref_doc_share_per as double precision),0) as \"refSharePer\", " 
					 + " 	coalesce(cast(od.doc_share_per as double precision),0) as \"sharePer\", " 
					 + " 	0 as \"isDocShare\" "
					 + " FROM  " 
					 + " 	billing.t_billing_details bdtls  " 
					 + " INNER JOIN billing.m_billing_mASter bm on  " 
					 + " 	bm.billing_mASter_id = bdtls.billing_mASter_id  " 
					 + " INNER JOIN public.t_order_details od on  " 
					 + " 	od.order_details_id = bdtls.order_details_id  "
					 + " LEFT JOIN public.m_tax mt on "
					 + " 	mt.id = bdtls.tax_id "
					 + "  WHERE bdtls.is_doctor_share_posted = 'N'  " 
					 + " and od.doc_share_per > 0 "
					 + " 	and od.ord_doc_id is not null and od.ord_doc_id > 0 " 
					 + " 	and od.ord_doc_spl_id is not null  " 
//					 + " 	and bm.admission_id is null  "
					 + " 	AND ( "
					 + "       bm.admission_id IS NULL "
					 + "    OR bm.encounter_id IS NULL "
					 + " )  "
					 + " 	and bdtls.unit_id=:unitId  "
//					 + "	and to_char(od.order_sch_date,'YYYY-MM-DD') <= :orderSchDate "
					 + "	and od.order_sch_date < CAST(:orderSchDate AS DATE) + INTERVAL '1 day' "
//					 + "  UNION  "
//					 + "  select  "
//					 + " 	bdtls.billing_details_id as \"billDtlId\", "
//					 + " 	bdtls.order_details_id as \"ordDtlId\", "
//					 + " 	bdtls.net_amt as \"totalAmount\", "
//					 + "    bdtls.package_consumption_amt as \"packageConsumptionAmt\" ,"
//					 + " 	bdtls.rate as \"unitRate\", "
//					 + " 	bdtls.quantity as \"quantity\", "
//					 + " 	bdtls.concession as \"concession\", "
//					 + " 	bdtls.service_id as \"serviceId\", "
//					 + " 	bdtls.unit_id as \"orgUnitId\", "
//					 + " 	bdtls.organization_id as \"orgId\", "
//					 + " 	bdtls.discount as \"discount\", "
//					 + " 	bdtls.tax_id as \"taxId\", "
//					 + " 	coalesce(mt.tax_percentage,0) as \"taxPer\", "
//					 + " 	bdtls.net_amt as \"netAmount\", "
//					 + " 	bm.encounter_id as \"visitId\", "
//					 + " 	bm.admission_id as \"admissionId\", "
//					 + " 	bdtls.tax_amount as \"taxAmt\", "
//					 + " 	bm.visit_type_id as \"visitTypeId\", "
//					 + " 	od.ord_doc_id as \"docId\", "
//					 + " 	od.ord_doc_spl_id as \"docSpecId\", "
//					 + " 	coalesce(cast(od.ref_doc_share_per as double precision),0) as \"refSharePer\", "
//					 + " 	coalesce(cast(od.doc_share_per as double precision),0) as \"sharePer\" , "
//					 + " 	0 as \"isDocShare\" "
//					 + "  FROM  "
//					 + " 	billing.t_billing_details bdtls  "
//					 + " INNER JOIN billing.m_billing_mASter bm on  "
//					 + " 	bm.billing_mASter_id = bdtls.billing_mASter_id  "
//					 + " INNER JOIN public.t_order_details od on  "
//					 + " 	od.order_details_id = bdtls.order_details_id  "
//					 + " LEFT JOIN public.m_tax mt on "
//					 + " 	mt.id = bdtls.tax_id "
//					 + " WHERE bdtls.is_doctor_share_posted = 'N'  "
//					 + " and od.doc_share_per > 0 "
//					 + " 	and od.ord_doc_id is not null and od.ord_doc_id > 0 "
//					 + " 	and od.ord_doc_spl_id is not null  "
//					 + " 	and bm.encounter_id is null  "
//					 + "	and to_char(od.order_sch_date,'YYYY-MM-DD') <= :orderSchDate "
//					 + " 	and bdtls.unit_id=:unitId  "
					 + " UNION ALL "
					 + " select  " 
					 + " 	bdtls.billing_details_id as \"billDtlId\", " 
					 + " 	bdtls.order_details_id as \"ordDtlId\", " 
					 + " 	bdtls.net_amt as \"totalAmount\", " 
					 + "    bdtls.package_consumption_amt as \"packageConsumptionAmt\" ," 
					 + " 	bdtls.rate as \"unitRate\", " 
					 + " 	bdtls.quantity as \"quantity\", " 
					 + " 	bdtls.concession as \"concession\", " 
					 + " 	bdtls.service_id as \"serviceId\", " 
					 + " 	bdtls.unit_id as \"orgUnitId\", " 
					 + " 	bdtls.organization_id as \"orgId\", " 
					 + " 	bdtls.discount as \"discount\", " 
					 + " 	bdtls.tax_id as \"taxId\", " 
					 + " 	coalesce(mt.tax_percentage,0) as \"taxPer\", "
					 + " 	bdtls.net_amt as \"netAmount\", " 
					 + " 	bm.encounter_id as \"visitId\", " 
					 + " 	bm.admission_id as \"admissionId\", " 
					 + " 	bdtls.tax_amount as \"taxAmt\", " 
					 + " 	bm.visit_type_id as \"visitTypeId\", " 
//					 + " 	(select referral_id from t_referal_details where encounter_id = bm.encounter_id and status = 'A' limit 1) as \"docId\", "
					+ " 	trd.referral_id as \"docId\", "
					 + " 	0 as \"docSpecId\", "
					 + " 	coalesce(cast(od.ref_doc_share_per as double precision),0) as \"refSharePer\", " 
					 + " 	coalesce(cast(od.doc_share_per as double precision),0) as \"sharePer\", " 
					 + " 	1 as \"isDocShare\" "
					 + " FROM  " 
					 + " 	billing.t_billing_details bdtls  " 
					 + " INNER JOIN billing.m_billing_mASter bm on  " 
					 + " 	bm.billing_mASter_id = bdtls.billing_mASter_id  " 
					 + " INNER JOIN public.t_order_details od on  " 
					 + " 	od.order_details_id = bdtls.order_details_id  " 
					 + " LEFT JOIN public.m_tax mt on "
					 + " 	mt.id = bdtls.tax_id "
					 + " LEFT JOIN LATERAL ( "
					 + " 	SELECT referral_id "
					 + " 	FROM t_referal_details trd "
					 + " 	WHERE trd.encounter_id = bm.encounter_id "
					 + " 	AND trd.status = 'A' "
					 + " 	LIMIT 1 "
					 + " 	) trd ON TRUE "
					 + "  WHERE bdtls.is_doctor_share_posted = 'N'  "
					 + " and od.ref_doc_share_per > 0 "
					 + " 	and bm.admission_id is null  " 
					 + " 	and bdtls.unit_id=:unitId  "
//					 + "	and to_char(od.order_sch_date,'YYYY-MM-DD') <= :orderSchDate "
					 + "	and od.order_sch_date < CAST(:orderSchDate AS DATE) + INTERVAL '1 day' "
					 + "  UNION ALL  "
					 + "  select  " 
					 + " 	bdtls.billing_details_id as \"billDtlId\", " 
					 + " 	bdtls.order_details_id as \"ordDtlId\", " 
					 + " 	bdtls.net_amt as \"totalAmount\", " 
					 + "    bdtls.package_consumption_amt as \"packageConsumptionAmt\" ," 
					 + " 	bdtls.rate as \"unitRate\", " 
					 + " 	bdtls.quantity as \"quantity\", " 
					 + " 	bdtls.concession as \"concession\", " 
					 + " 	bdtls.service_id as \"serviceId\", " 
					 + " 	bdtls.unit_id as \"orgUnitId\", " 
					 + " 	bdtls.organization_id as \"orgId\", " 
					 + " 	bdtls.discount as \"discount\", " 
					 + " 	bdtls.tax_id as \"taxId\", " 
					 + " 	coalesce(mt.tax_percentage,0) as \"taxPer\", "
					 + " 	bdtls.net_amt as \"netAmount\", " 
					 + " 	bm.encounter_id as \"visitId\", " 
					 + " 	bm.admission_id as \"admissionId\", " 
					 + " 	bdtls.tax_amount as \"taxAmt\", " 
					 + " 	bm.visit_type_id as \"visitTypeId\", " 
//					 + " 	(select referral_id from t_referal_details where admission_id = bm.admission_id and status = 'A' limit 1) as \"docId\", "
					 + " 	trd.referral_id as \"docId\", "
					 + " 	0 as \"docSpecId\", "
					 + " 	coalesce(cast(od.ref_doc_share_per as double precision),0) as \"refSharePer\", " 
					 + " 	coalesce(cast(od.doc_share_per as double precision),0) as \"sharePer\", " 
					 + " 	1 as \"isDocShare\" "
					 + "  FROM  " 
					 + " 	billing.t_billing_details bdtls  " 
					 + " INNER JOIN billing.m_billing_mASter bm on  " 
					 + " 	bm.billing_mASter_id = bdtls.billing_mASter_id  " 
					 + " INNER JOIN public.t_order_details od on  " 
					 + " 	od.order_details_id = bdtls.order_details_id  " 
					 + " LEFT JOIN public.m_tax mt on "
					 + " 	mt.id = bdtls.tax_id "
				 	 + " LEFT JOIN LATERAL ( "
					 + "    SELECT referral_id "
					 + "    FROM t_referal_details trd "
					 + "    WHERE trd.admission_id = bm.admission_id "
					 + "      AND trd.status = 'A' "
					 + "    LIMIT 1 "
					 + " ) trd ON TRUE "
					 + " WHERE bdtls.is_doctor_share_posted = 'N'  "
					 + " and od.ref_doc_share_per > 0 "
					 + " 	and bm.encounter_id is null  "
//					 + "	and to_char(od.order_sch_date,'YYYY-MM-DD') <= :orderSchDate "
					 + "	and od.order_sch_date < CAST(:orderSchDate AS DATE) + INTERVAL '1 day' "
					 + " 	and bdtls.unit_id=:unitId  " ;
			
			session = sessionFactory.openStatelessSession(); 
					 List<TDoctorShareRespDto> listTDoctorShareRespDto= session.createSQLQuery(query)
					.setParameter("unitId", unitId)
					.setParameter("orderSchDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "yyyy-MM-dd"))
					.setFirstResult(startPoint).setMaxResults(maxResults)
					.setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();

			if(listTDoctorShareRespDto != null && !listTDoctorShareRespDto.isEmpty()){
				List<Integer> orderDetiIds = listTDoctorShareRespDto.stream().map(TDoctorShareRespDto :: getOrdDtlId).collect(Collectors.toList());

				String q = " select  " +
						"  coalesce(sum(cr_net_amount),0) as \"crNetAmount\",order_detail_id as \"ordDtlId\"  " +
						"  from  " +
						"  billing.t_credit_note_detail tcnd  " +
						"  where  " +
						"  order_detail_id in(:orderDeIds) " +
						" and status = 'A' group  by order_detail_id ";

				StatelessSession session1  = sessionFactory.openStatelessSession();
				List<TDoctorShareRespDto> creditAmountList= session1.createSQLQuery(q)
						.setParameterList("orderDeIds", orderDetiIds)
						.setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();


				for(TDoctorShareRespDto dto :listTDoctorShareRespDto){
					TDoctorShareRespDto dummyservic= creditAmountList.stream()
							.filter(order -> dto.getOrdDtlId().equals(order.getOrdDtlId()))
							.findAny()
							.orElse(null);
					if(dummyservic!=null){
						dto.setCrNetAmount(dummyservic.getCrNetAmount());
					}else{
						dto.setCrNetAmount(0d);
					}
				}


			}



			return new com.param.global.common.Response<>(SUCCESS, SUCCESS_CODE, null, listTDoctorShareRespDto, null);
		}catch(Exception e){
			
			throw e;
		}finally{
			if(session != null)
				session.close();
		}
	}
	
	
	@Override
	public Response getDocShareDataByDoctorShareId(Integer docShareId) throws ServiceException {
		
		try{
			TDoctorShare tDoctorShare = findById(docShareId);
			if (tDoctorShare.getDoctorShareId() != 0)
			{
		     	return new Response<>(SUCCESS, SUCCESS_CODE, null, null, tDoctorShare);
			}else {
		     	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, tDoctorShare);
			}
			
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveDoctorShareMaster(TDoctorShare tDoctorShare) throws ApplicationException {
		try{
			if (tDoctorShare !=null)
			{
				tDoctorShare.setStatus('A');
				tDoctorShare = save(tDoctorShare);
		     	return new Response<>(SUCCESS, SUCCESS_CODE, null, null, tDoctorShare.getDoctorShareId());
			}else {
		     	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, tDoctorShare);
			}
			
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDoctorShareMaster(TDoctorShareRespDto tDoctorShare) throws ApplicationException {
		try{
			sessionFactory.getCurrentSession().createSQLQuery("UPDATE billing.t_doctor_share set "
					+ "share_per=:sharePer, "
					+ "doc_share=:docShare, "
					+ "hosp_share=:hospShare, "
					+ "due_share_amt=:dueShareAmt "
					+ "WHERE doctor_share_id=:doctorShareId ")
				.setParameter("sharePer", tDoctorShare.getSharePer())
				.setParameter("docShare", tDoctorShare.getDocShare())
				.setParameter("hospShare", tDoctorShare.getHospShare())
				.setParameter("dueShareAmt", tDoctorShare.getDueShareAmt())
				.setParameter("doctorShareId", tDoctorShare.getDoctorShareId())
			.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getDocShareSplitDetails(Integer docShareId) throws ApplicationException {
			try{
			String quary ="select "  
						+"	docShare.net_amount as \"netAmount\",  "
						+"	splitLog.doc_id as \"docId\",  "
						+"	concat('Dr. ',coalesce(doc.first_name), ' ', coalesce(doc.last_name)) as \"doctorName\",   "
						+"	splitLog.new_share_percentage as \"oldSharePercentage\",  "
						+"	splitLog.share_amount as \"oldsharAmt\",  "
						+"	splitLog.share_amount as \"shareAmount\",  "
						+"	splitLog.priority as \"priority\",  "
						+"	splitLog.doctor_share_split_id as \"doctorShareSplitId\",  "
						+"	splitLog.doctor_share_id as \"doctorShareId\",  "
						+"	docShare.doc_spec_id as \"docSpecId\",  "
						+"	splitLog.new_share_percentage as \"newSharePercentage\",  "
						+ " (select voucher_dtls_id from billing.t_doc_share_voucher_dtls where doctor_share_id=splitLog.doctor_share_id order by 1 desc limit 1) as \"voucherDtlsId\" "
						+"FROM  "
						+"	billing.t_doctor_share_split_logs splitLog  "
						+"INNER JOIN billing.t_doctor_share docShare on  "
						+"	docShare.doctor_share_id =  splitLog.parent_doc_share_id  "
						+"INNER JOIN doctor.m_doctor_master doc on doc.doctor_id=splitLog.doc_id  "
						+"WHERE splitLog.parent_doc_share_id = :doctorShareId  "
						+ "AND splitLog.status='A' "
						+ "order by splitLog.doctor_share_split_id asc";
		
					List<TDoctorShareRespDto> listTDoctorShareRespDto = sessionFactory.getCurrentSession().createSQLQuery(quary)
					.setParameter("doctorShareId",docShareId)
					.setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTDoctorShareRespDto, null);
			}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public com.param.global.common.Response getDataFromBillingMst(int startPoint, int maxResults,List encounterAdmissionIds)
			throws ApplicationException {
		StatelessSession session = null;
		try{
			session = sessionFactory.openStatelessSession();
			 StringBuilder query = new StringBuilder(session.getNamedQuery("GET_DATA_FROM_BILLING_DTLS_FROM_GIVEN_DURATION").getQueryString());
//					query.append(" WHERE to_char(bilmst.bill_date_time,'DD-MM-YYYY') ='"+GlobalCommonDateUtils.getStringDate(todayDate, "dd-MM-yyyy")+"'");
//			        query.append(" WHERE to_timestamp(to_char(bilmst.bill_date_time,'DD-MM-YYYY'),'DD-MM-YYYY') BETWEEN '"+firstDate +"' AND '"+todayDate +"'");
					query.append(" WHERE bilmst.is_doctor_referral_posted='N'"
							+ " AND (bilmst.encounter_id IN (:encounterAdmissionIds) OR bilmst.admission_id IN (:encounterAdmissionIds) )");
//							  +" GROUP BY bilmst.billing_master_id,"
//							  + " trefd.referral_id");
					 List<TDoctorShareRespDto> listTDoctorShareRespDto= session.createSQLQuery(query.toString())
							 .setParameterList("encounterAdmissionIds", encounterAdmissionIds)
					.setFirstResult(startPoint).setMaxResults(maxResults)
					.setResultTransformer(Transformers.aliasToBean(TDoctorReferralShareDto.class)).list();
			return new com.param.global.common.Response<>(SUCCESS, SUCCESS_CODE, null, listTDoctorShareRespDto, null);
		}catch(Exception e){
			
			throw e;
		}finally{
			if(session != null)
				session.close();
		}
	}

	@Override
	public com.param.global.common.Response updateDoctorShareData(TDoctorShare tDoctorShare) {
		try{
			if (tDoctorShare !=null)
			{
				tDoctorShare.setStatus('A');
				tDoctorShare = save(tDoctorShare);
		     	return new com.param.global.common.Response<>(SUCCESS, SUCCESS_CODE, null, null, tDoctorShare);
			}else {
		     	return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, tDoctorShare);
			}
			
		}catch(Exception e){
			
			throw e;
			//throw new ServiceException(e.getMessage());
		}
	//	return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, tDoctorShare);
	}

	@Override
	public Response updateDoctor(TDoctorShareRespDto masterDto) 
	{
		try{
			int i = sessionFactory.getCurrentSession().createSQLQuery("update "
				+"  billing.t_doctor_share set "
				+" 	doc_id =:docId, "
				+" 	doc_spec_id =:docSplId "
				+"  where "
				+" 	doctor_share_id =:doctorShareId ")
				.setParameter("docSplId", masterDto.getDocSpecId())
				.setParameter("docId", masterDto.getDocId())
				.setParameter("doctorShareId", masterDto.getDoctorShareId())
			.executeUpdate();
			
			int j = sessionFactory.getCurrentSession().createSQLQuery("update "
					+"  billing.t_doctor_share_split_logs set "
					+" 	doc_id =:docId "
					+"  where "
					+" 	doctor_share_id =:doctorShareId "
					+ " and doctor_share_split_id=:doctorShareSplitId ")
					.setParameter("doctorShareSplitId", masterDto.getDoctorShareSplitId())
					.setParameter("docId", masterDto.getDocId())
					.setParameter("doctorShareId", masterDto.getDoctorShareId())
			.executeUpdate();
			if(i==1 && j==1)
				return new Response<>(SUCCESS, SUCCESS_CODE, "Doctor Updated Successfully...!!", null, null);
			else
				return new Response<>(ERROR, null, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateStatus(TDoctorShareRespDto masterDto) 
	{
		try{
			int i = sessionFactory.getCurrentSession().createSQLQuery("update "
					+"  billing.t_doctor_share set "
					+" 	status ='I' "
					+"  where "
					+" 	doctor_share_id =:doctorShareId ")
					.setParameter("doctorShareId", masterDto.getDoctorShareId())
				.executeUpdate();
				
				int j = sessionFactory.getCurrentSession().createSQLQuery("update "
						+"  billing.t_doctor_share_split_logs set "
						+" 	status = 'I' "
						+"  where "
						+ " doctor_share_split_id=:doctorShareSplitId ")
						.setParameter("doctorShareSplitId", masterDto.getDoctorShareSplitId())
				.executeUpdate();
				
				int k = sessionFactory.getCurrentSession().createSQLQuery("update "
						+"  billing.t_doctor_share set "
						+" 	hosp_share =:hospShre, total_doc_share=:totalDocShare "
						+"  where "
						+ " doctor_share_id=:doctorShareId ")
						.setParameter("hospShre", masterDto.getHospShare())
						.setParameter("totalDocShare", masterDto.getTotalDocShare())
						.setParameter("doctorShareId", masterDto.getParentDocShareId())
				.executeUpdate();
				
			if(i==1 && j==1 && k==1)
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			else
				return new Response<>(ERROR, null, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getPendingServicesForDoctorShareByBillId(TDoctorShareReqDto tDoctorShareDto) {
		try {
			
			StringBuilder query = new StringBuilder ("select " 
				+ " 	billingmas0_.billing_master_id as \"billingMasterId\", " 
				+ " 	concat(billingmas0_.bill_number,' | ',to_char(billingmas0_.bill_date_time,'DD/MM/YYYY HH24:MI:SS')) as \"billNumber\", " 
				+ " 	billingdet1_.billing_details_id as \"billingDetailsId\", " 
				+ " 	ordermaste5_.order_no as \"orderNo\", " 
				+ " 	billingdet1_.service_id as \"serviceId\", " 
				+ " 	servicemas2_.service_standard_name as \"serviceStandardName\", " 
				+ " 	billingdet1_.ord_doc_id as \"doctorId\", " 
				+ " 	('Dr. ' || coalesce(doctormast6_.first_name)|| ' ' || coalesce(doctormast6_.middle_name)|| ' ' || coalesce(doctormast6_.last_name)) as \"doctorName\", " 
				+ " 	billingdet1_.rate as \"unitPrice\", " 
				+ " 	billingdet1_.gross_amount as \"grossAmount\", " 
				+ " 	billingdet1_.self_payable as \"selfPayable\", " 
				+ " 	billingdet1_.credit_payable as \"creditPayable\", " 
				+ " 	billingdet1_.payee_id as \"payeeId\", " 
				+ " 	billingdet1_.concession as \"concession\", " 
				+ " 	billingdet1_.discount as \"discount\", " 
				+ " 	billingdet1_.tax_id as \"taxId\", " 
				+ " 	tax7_.name as \"taxName\", " 
				+ " 	tax7_.tax_percentage as \"taxPercentage\", " 
				+ " 	billingdet1_.tax_amount as \"taxAmount\", " 
				+ " 	billingdet1_.net_amt as \"netAmt\", " 
				+ " 	speciality3_.speciality_id as \"specialityId\", " 
				+ " 	speciality3_.speciality_name as \"specialityName\", " 
				+ " 	billingmas0_.round_off as \"roundOff\", " 
				+ " 	to_char(orderdetai4_.order_date, 'dd/mm/yyyy') as \"orderDateString\", " 
				+ " 	orderdetai4_.order_details_id as \"orderDetailsId\", "
				+ " 	pat.uhid_number as \"uhidNumber\", " 
				+ "		concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.last_name) as \"patientName\" " 
				+ " from " 
				+ " 	billing.m_billing_master billingmas0_ " 
				+ " inner join billing.t_billing_details billingdet1_ on " 
				+ " 	billingmas0_.billing_master_id = billingdet1_.billing_master_id " 
				+ " inner join service.m_service_master servicemas2_ on " 
				+ " 	billingdet1_.service_id = servicemas2_.service_master_id " 
				+ " inner join service.t_unit_service_master_details serviceDtls on " 
				+ " 	billingdet1_.service_id = serviceDtls.service_master_id	 " 
				+ " inner join doctor.m_speciality_master speciality3_ on " 
				+ " 	servicemas2_.speciality_id = speciality3_.speciality_id " 
				+ " inner join public.t_order_details orderdetai4_ on " 
				+ " 	billingdet1_.order_details_id = orderdetai4_.order_details_id " 
				+ " inner join public.t_order_master ordermaste5_ on " 
				+ " 	orderdetai4_.order_id = ordermaste5_.order_id " 
				+ " left outer join public.m_tax tax7_ on " 
				+ " 	orderdetai4_.tax_id = tax7_.id " 
				+ " left outer join doctor.m_doctor_master doctormast6_ on " 
				+ " 	billingdet1_.ord_doc_id = doctormast6_.doctor_id "
				+ " inner join patient.t_patient_registration pat on " 
				+ " 	pat.patient_id = billingmas0_.patient_id "
				+ "  left join m_prefix_master pre on "
				+ " 	pre.prefix_id = pat.prefix_id "
				+ " where " 
				+ " 	serviceDtls.unit_id =:unitId "
				+ "		and billingmas0_.unit_id=:unitId "
				+ " 	and serviceDtls.is_doc_req = 'Y' "
				+ "		and (orderdetai4_.ord_doc_id is null or orderdetai4_.ord_doc_id = 0 ) "
				+ "		and orderdetai4_.status  = 'A' "
				+ "     and billingmas0_.status ='A' "
				+ " 	and billingdet1_.status = 'A' "
				+ " 	and orderdetai4_.doc_share_per is null "
				+ "		and to_char(orderdetai4_.order_sch_date,'YYYY-MM-DD') <= '"+GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(tDoctorShareDto.getOrgUnitId()), "yyyy-MM-dd")+"'" 
				);

			if(tDoctorShareDto.getServiceRendered() != null && tDoctorShareDto.getServiceRendered()<2)
				query.append(" and orderdetai4_.service_rendered =:serviceRendered ");

			if(tDoctorShareDto.getVisitTypeId() != null)
				query.append(" AND billingmas0_.visit_type_id =:visitTypeId ");

			if(tDoctorShareDto.getPatientId() != null)
				query.append(" AND billingmas0_.patient_id =:patientId ");

			if(tDoctorShareDto.getBillNumber() != null && !tDoctorShareDto.getBillNumber().isEmpty())
				query.append(" AND lower(billingmas0_.bill_number) like lower(:billNumber)");


			query.append(" AND to_char(orderdetai4_.order_sch_date,'YYYY-MM-DD') BETWEEN :fromDate AND :toDate ");
			query.append(" 	order by billingdet1_.billing_details_id desc " );
			System.out.println("query"+query);
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("unitId", tDoctorShareDto.getOrgUnitId())
					.setParameter("fromDate", tDoctorShareDto.getFromDate().substring(0,10))
					.setParameter("toDate", tDoctorShareDto.getToDate().substring(0,10));

			if(tDoctorShareDto.getServiceRendered()<2)
				sqlQuery.setParameter("serviceRendered", tDoctorShareDto.getServiceRendered());

			if(tDoctorShareDto.getVisitTypeId() != null)
				sqlQuery.setParameter("visitTypeId", tDoctorShareDto.getVisitTypeId());

			if(tDoctorShareDto.getPatientId() != null)
				sqlQuery.setParameter("patientId", tDoctorShareDto.getPatientId());

			if(tDoctorShareDto.getBillNumber() != null && !tDoctorShareDto.getBillNumber().isEmpty())
				sqlQuery.setParameter("billNumber", "%" + tDoctorShareDto.getBillNumber().toLowerCase() + "%");

			List<BillDetailsByBillIdDto> detailsByBillIdDtosList = sqlQuery.setResultTransformer(Transformers.aliasToBean(BillDetailsByBillIdDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, detailsByBillIdDtosList, null);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response changeDoctorRefeeral(TDoctorShareRespDto masterDto) {
		{
			try{
				   sessionFactory.getCurrentSession().createSQLQuery("update "
					+"  billing.t_doctor_share set "
					+" 	referral_id =:referrlId "
					+"  where "
					+" 	doctor_share_id =:doctorShareId ")
					.setParameter("referrlId", masterDto.getReferralId())
					.setParameter("doctorShareId", masterDto.getDoctorShareId())
				.executeUpdate();
				
					return new Response<>(SUCCESS, SUCCESS_CODE, "Referral Updated Successfully...!!", null, null);
			}catch(Exception e){
				
				throw e;
			}
		}
	}
	
	@Override
	public Response saveDoctorReferralList(TDoctorShareProcessReqDto reqDto) throws ApplicationException {
	try{
				for(TDoctorShareRespDto tDoctorShareRespDto: reqDto.getListtDoctorShareRespDto())
				{ 
					sessionFactory.getCurrentSession().createSQLQuery("update "
					+ "     billing.t_doctor_share set "
					+ " 	processed_share_amt = coalesce(processed_share_amt, 0)  + :processedShareAmt, "
					+ " 	doc_share =:docShare, "
					+ " 	total_doc_share =:totalDocShare, "
					+ " 	hosp_share =:hospShare, "
					+ "		due_share_amt =:dueShareAmt ,"
					+ " 	updated_by =:updatedBy, "
					+ " 	updated_date = now(), "
					+ " 	processed_share_date = now(), "
					+ " 	due_share_date = now(), "
					+ " 	remark =:remark "
					+ " where "
					+ " 	doctor_share_id =:doctorShareId ;")
					.setParameter("docShare", tDoctorShareRespDto.getDocShare())
					.setParameter("totalDocShare", tDoctorShareRespDto.getDocShare())
					.setParameter("hospShare", tDoctorShareRespDto.getHospShare())
					.setParameter("processedShareAmt", tDoctorShareRespDto.getProcessedShareAmt())
					.setParameter("dueShareAmt", tDoctorShareRespDto.getDueShareAmt())
					.setParameter("updatedBy", reqDto.getUserId())
					.setParameter("remark", tDoctorShareRespDto.getRemark())
					.setParameter("doctorShareId", tDoctorShareRespDto.getDoctorShareId())
					.executeUpdate();
					
					if(tDoctorShareRespDto.getDocDocShareId() != null && tDoctorShareRespDto.getDocDocShareId() > 0) 
					{
						sessionFactory.getCurrentSession().createSQLQuery("update "
								+ "     billing.t_doctor_share set "
								+ " 	hosp_share =:hospShare "
								+ " where "
								+ " 	doctor_share_id =:doctorShareId ;")
								.setParameter("hospShare", tDoctorShareRespDto.getHospShare())
								.setParameter("doctorShareId", tDoctorShareRespDto.getDocDocShareId())
								.executeUpdate();
					}
			    }  
			
				TDocShareVoucherDto docShareVoucherDto = reqDto.gettDocShareVoucherDto();
				if(docShareVoucherDto != null)
				{
					docShareVoucherDto.setOrgId(reqDto.getOrgId());
					docShareVoucherDto.setOrgUnitId(reqDto.getUnitId());
					docShareVoucherDto.setType(2);
					Response docShareVoucherRes = iDocShareVoucherDao.saveDocShareVoucher(docShareVoucherDto);
					if(docShareVoucherRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && docShareVoucherRes.getObject() != null)
					{
						TDocShareVoucher tDocShareVoucher = (TDocShareVoucher)docShareVoucherRes.getObject();
						Integer docShareVoucheId = tDocShareVoucher.getDocShareVoucheId();
						ListIterator<TDocShareVoucherDtlsDto> servicDtlsItr = reqDto.getListTDocShareVoucherDtlsDto().listIterator();
						TDocShareVoucherDtlsDto tDocShareVoucherDtlsDto = null;
						while(servicDtlsItr.hasNext())
						{
							tDocShareVoucherDtlsDto = servicDtlsItr.next();
							tDocShareVoucherDtlsDto.setDocShareVoucheId(docShareVoucheId);
							tDocShareVoucherDtlsDto.setOrgId(reqDto.getOrgId());
							tDocShareVoucherDtlsDto.setOrgUnitId(reqDto.getUnitId());
							iDocShareVoucherDtlsDao.saveDocShareVoucherDtls(tDocShareVoucherDtlsDto);
						}
						
					}
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, ERROR_MESSAGE, null, null);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response saveDoctorSlabVoucher(TDoctorShareProcessReqDto reqDto) throws ApplicationException {
		try{
			
				TDocShareVoucherDto docShareVoucherDto = reqDto.gettDocShareVoucherDto();
				if(docShareVoucherDto != null)
				{
					docShareVoucherDto.setOrgId(reqDto.getOrgId());
					docShareVoucherDto.setOrgUnitId(reqDto.getUnitId());
					Response docShareVoucherRes = iDocShareVoucherDao.saveDocShareVoucher(docShareVoucherDto);
					if(docShareVoucherRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && docShareVoucherRes.getObject() != null)
					{
						TDocShareVoucher tDocShareVoucher = (TDocShareVoucher)docShareVoucherRes.getObject();
						Integer docShareVoucheId = tDocShareVoucher.getDocShareVoucheId();
						ListIterator<TDocShareVoucherDtlsDto> servicDtlsItr = reqDto.getListTDocShareVoucherDtlsDto().listIterator();
						TDocShareVoucherDtlsDto tDocShareVoucherDtlsDto = null;
						while(servicDtlsItr.hasNext())
						{
							tDocShareVoucherDtlsDto = servicDtlsItr.next();
								tDocShareVoucherDtlsDto.setDocShareVoucheId(docShareVoucheId);
								tDocShareVoucherDtlsDto.setOrgId(reqDto.getOrgId());
								tDocShareVoucherDtlsDto.setOrgUnitId(reqDto.getUnitId());
							iDocShareVoucherDtlsDao.saveDocShareVoucherDtls(tDocShareVoucherDtlsDto);
							
							
							sessionFactory.getCurrentSession().createSQLQuery("update billing.t_doctor_slab_transaction "
									+ "	set process_amount = doctor_revenue "
									+ "	where id="+tDocShareVoucherDtlsDto.getDoctorSlabTransactionId())
							.executeUpdate();
						}
						
						if(reqDto.getDoctorSlabVoucherDetailsDtosList()!=null && reqDto.getDoctorSlabVoucherDetailsDtosList().size()>0)
						{
							for(DoctorSlabVoucherDetailsDto doctorSlabVoucherDetailsDto : reqDto.getDoctorSlabVoucherDetailsDtosList())
							{
								DoctorSlabVoucherDetails doctorSlabVoucherDetails = new DoctorSlabVoucherDetails();
									doctorSlabVoucherDetails.setVoucherId(docShareVoucheId);
									doctorSlabVoucherDetails.setDoctorId(doctorSlabVoucherDetailsDto.getDoctorId());
									doctorSlabVoucherDetails.setDoctorSlabMasterId(doctorSlabVoucherDetailsDto.getDoctorSlabMasterId());
									doctorSlabVoucherDetails.setRevenueProcessed(doctorSlabVoucherDetailsDto.getRevenueProcessed());
									doctorSlabVoucherDetails.setRevenueGeneratedAgainst(doctorSlabVoucherDetailsDto.getRevenueGeneratedAgainst());
									doctorSlabVoucherDetails.setStatus('A');
								sessionFactory.getCurrentSession().save(doctorSlabVoucherDetails);
							}
						}
						
						
					}
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, ERROR_MESSAGE, null, null);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getDoctorReferralPaymentVoucherDtlsByReferralId(TDoctorShareReqDto reqDto)
			throws ApplicationException {
		try{
			
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_REFERRAL_PAYMENT_VOUCHER_LIST").getQueryString());
			  //query.append(reqDto.getDoctorId() != null ? " AND docMst.doctor_id ='"+reqDto.getDoctorId()+"'"  : " ");
			 // query.append(" AND to_timestamp(to_char(docShareVoucher.created_date,'YYYY-MM-DD'),'YYYY-MM-DD') BETWEEN '"+reqDto.getFromDate() +"' AND '"+reqDto.getToDate() +"'");
		   List<TDoctorShareRespDto> listTDoctorShareRespDto= sessionFactory.getCurrentSession().createSQLQuery(query.toString())
				   .setParameter("orgId", reqDto.getOrgId())
				   .setParameter("orgUnitId", reqDto.getOrgUnitId())
				   .setParameter("referralId", reqDto.getReferralId())
				   .setParameter("fromDate", GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-MM-yyyy"))
				   .setParameter("toDate", GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-MM-yyyy"))
				   .setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTDoctorShareRespDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response docSharefindById(Integer DocShareId)
			throws ApplicationException {
		try{
			
			TDoctorShare tDoctorShareRespDto = findById(DocShareId);
			
			
			TDoctorShareRespDto doctorShare = new TDoctorShareRespDto();
				doctorShare.setAdmissionId(tDoctorShareRespDto.getAdmissionId());
				doctorShare.setOrdDtlId(tDoctorShareRespDto.getOrdDtlId());
				doctorShare.setCreatedBy(tDoctorShareRespDto.getCreatedBy());
				doctorShare.setUpdatedDate(TimeZoneComponent.getDateByzone(tDoctorShareRespDto.getOrgUnitId()).getTime());
				doctorShare.setDocId(tDoctorShareRespDto.getDocId());
				doctorShare.setDocShare(tDoctorShareRespDto.getDocShare());
				doctorShare.setDocSpecId(tDoctorShareRespDto.getDocSpecId());
				doctorShare.setConcession(tDoctorShareRespDto.getConcession());
				doctorShare.setHospShare(tDoctorShareRespDto.getHospShare());
				doctorShare.setBillDtlId(tDoctorShareRespDto.getBillDtlId());
				doctorShare.setVisitId(tDoctorShareRespDto.getVisitId());
				doctorShare.setAdmissionId(tDoctorShareRespDto.getAdmissionId());
				doctorShare.setVisitTypeId(tDoctorShareRespDto.getVisitTypeId());
				doctorShare.setServiceId(tDoctorShareRespDto.getServiceId());
				doctorShare.setUnitRate(tDoctorShareRespDto.getUnitRate());
				doctorShare.setDueShareAmt(tDoctorShareRespDto.getDueShareAmt());
				doctorShare.setDueShareDate(TimeZoneComponent.getDateByzone(tDoctorShareRespDto.getOrgUnitId()).getTime());
				doctorShare.setTotalAmount(tDoctorShareRespDto.getTotalAmount());
				doctorShare.setDiscount(tDoctorShareRespDto.getDiscount());
				doctorShare.setTaxAmt(tDoctorShareRespDto.getTaxAmt());
				doctorShare.setStatus(ACTIVE);
				doctorShare.setNetAmount(tDoctorShareRespDto.getNetAmount());
				doctorShare.setTaxId(tDoctorShareRespDto.getTaxId());
				doctorShare.setTaxType(tDoctorShareRespDto.getTaxType());
				doctorShare.setProcessedShareAmt(tDoctorShareRespDto.getProcessedShareAmt());
				doctorShare.setIsActive(tDoctorShareRespDto.getIsActive());
				doctorShare.setIsBillCrDr(tDoctorShareRespDto.getIsBillCrDr());
				doctorShare.setIsOnBillPayment(tDoctorShareRespDto.getIsOnBillPayment());
				doctorShare.setOrgId(tDoctorShareRespDto.getOrgId());
				doctorShare.setOrgUnitId(tDoctorShareRespDto.getOrgUnitId());
				doctorShare.setProcessedShareDate(TimeZoneComponent.getDateByzone(tDoctorShareRespDto.getOrgUnitId()).getTime());
				doctorShare.setSharePer(tDoctorShareRespDto.getSharePer());
				doctorShare.setIsShareSplit(tDoctorShareRespDto.getIsShareSplit());
				doctorShare.setTotalDocShare(tDoctorShareRespDto.getTotalDocShare());
				doctorShare.setReferralId(tDoctorShareRespDto.getReferralId());
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, doctorShare,tDoctorShareRespDto);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getDoctorShareDataByOrderDetailsId(Integer ordDtlsId)throws ApplicationException {
		try{
			String query =  " select " 
					 + " 	docShare.doctor_share_id as \"doctorShareId\", " 
					 + " 	docShare.total_amount as \"totalAmount\", " 
					 + " 	docShare.doc_id as \"docId\", " 
					 + " 	docShare.due_share_amt as \"dueShareAmt\", " 
					 + " 	docShare.share_per as \"sharePer\", " 
					 + " 	voudtls.processed_amt as \"voucherProcessedAmt\", " 
					 + " 	(voudtls.processed_amt/docShare.total_amount)*100 as \"voucherSharePer\" " 
					 + " from " 
					 + " 	billing.t_doctor_share docShare  " 
					 + " left join billing.t_doc_share_voucher_dtls voudtls on " 
					 + " 	voudtls.doctor_share_id = docShare.doctor_share_id	 " 
					 + " where "
					 + "	docShare.ord_dtl_id =:ordDetailsId ";
			
			 List<TDoctorShareRespDto> listTDoctorShareRespDto= sessionFactory.getCurrentSession().createSQLQuery(query)
					   .setParameter("ordDetailsId", ordDtlsId)
					   .setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTDoctorShareRespDto, null);
			
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDoctorReferralData(Integer refDocShareId, Double refDocShare,Double hospShare) {
		try {
			sessionFactory.getCurrentSession().createSQLQuery(" update " 
					 + " 	billing.t_doctor_share " 
					 + " set " 
					 + " 	doc_share =:docShare, " 
					 + " 	due_share_amt =:docShare, " 
					 + " 	total_doc_share =:docShare, " 
					 + " 	updated_date = now(), " 
					 + " 	share_per = (select (:docShare/ ((total_amount - discount))*100) from billing.t_doctor_share where doctor_share_id =:doctorShareId) " 
					 + " where " 
					 + " 	doctor_share_id =:doctorShareId ")
					.setParameter("docShare", refDocShare)
					.setParameter("doctorShareId", refDocShareId)
					.executeUpdate();
			
			
			sessionFactory.getCurrentSession().createSQLQuery(" update " 
					 + " 	billing.t_doctor_share " 
					 + " set " 
					 + " 	hosp_share =:hospShare " 
					 + " where " 
					 + " 	doctor_share_id =:doctorShareId ")
					.setParameter("hospShare", hospShare)
					.setParameter("doctorShareId", refDocShareId)
					.executeUpdate();
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response inactiveDocShare(Integer docShareId) {
		try {
			sessionFactory.getCurrentSession().createSQLQuery(" update " 
					 + " 	billing.t_doctor_share " 
					 + " set " 
					 + " 	status='I' " 
					 + " where " 
					 + " 	doctor_share_id =:doctorShareId ")
					.setParameter("doctorShareId", docShareId)
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDoctorShareProcessedVoucherList(TDoctorShareReqDto tDoctorShareDto) throws ApplicationException {
	try{
	
		String query = sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_SHARE_PROCESSED_VOUCHER_LIST").getQueryString();

		StringBuffer subQuery = new StringBuffer();
		if(tDoctorShareDto.getDoctorId()!=null){
			subQuery.append(" AND vou.doctor_id =:docId ");
		}
		if(tDoctorShareDto.getReferralId()!=null){
			subQuery.append(" AND vou.referral_id =:refId ");
		}

		query = query.concat(subQuery.toString());

		Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query)
			   .setInteger("organizationId", tDoctorShareDto.getOrgId())
			   .setInteger("unitId", tDoctorShareDto.getOrgUnitId())
			   .setString("fromDate",tDoctorShareDto.getFromDate())
			   .setString("toDate", tDoctorShareDto.getToDate());

		if(tDoctorShareDto.getDoctorId()!=null)
			sqlQuery.setParameter("docId", tDoctorShareDto.getDoctorId());

		if(tDoctorShareDto.getReferralId()!=null)
			sqlQuery.setParameter("refId", tDoctorShareDto.getReferralId());

		List<TDoctorShareRespDto> listTDoctorShareRespDto = sqlQuery
						.setFirstResult(tDoctorShareDto.getOffset() != null ? tDoctorShareDto.getOffset() : 0)
						.setMaxResults(tDoctorShareDto.getRecordPerPage() != null ? tDoctorShareDto.getRecordPerPage() : 10)
						.setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();

		int count = 0;
		if(listTDoctorShareRespDto!=null && listTDoctorShareRespDto.size()>0) {
			Query sqlQueryForCnt = sessionFactory.getCurrentSession().createSQLQuery(query)
				   .setParameter("organizationId", tDoctorShareDto.getOrgId())
				   .setParameter("unitId", tDoctorShareDto.getOrgUnitId())
				   .setParameter("fromDate",tDoctorShareDto.getFromDate())
				   .setParameter("toDate", tDoctorShareDto.getToDate());

			List<TDoctorShareRespDto> listTDoctorShareRespDtoForCnt = sqlQueryForCnt
				   .setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();
			count = listTDoctorShareRespDtoForCnt.size();
		}

		return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTDoctorShareRespDto, count);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response deactivateDocShareAndSplit(Integer docDocShareId,Integer billDtlsId) {
		{
			try{
				   sessionFactory.getCurrentSession().createSQLQuery("update "
						+ "  billing.t_doctor_share set "
						+ " 	status='I' "
						+ "  where "
						+ " 	doctor_share_id =:doctorShareId ")
					.setParameter("doctorShareId", docDocShareId)
					.executeUpdate();
				   
				   sessionFactory.getCurrentSession().createSQLQuery("update "
							+ "  billing.t_doctor_share_split_logs set "
							+ " 	status='I' "
							+ "  where "
							+ " 	parent_doc_share_id =:doctorShareId ")
					.setParameter("doctorShareId", docDocShareId)
					.executeUpdate();
				
				   if(billDtlsId!=null && billDtlsId>0)
				   {
					   sessionFactory.getCurrentSession().createSQLQuery("update billing.t_billing_details "
								+ " set is_doctor_share_posted='N' "
								+ " where billing_details_id =:billDtlsId ")
						.setParameter("billDtlsId", billDtlsId)
						.executeUpdate();
				   }
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}catch(Exception e){
				
				throw e;
			}
		}
	}

	@Override
	public void allowBillDtailToReacalulateDocShare(Integer orderDetailsId) {
		try{
			   if(orderDetailsId!=null && orderDetailsId>0)
			   {
				   sessionFactory.getCurrentSession().createSQLQuery("update billing.t_billing_details "
							+ " set is_doctor_share_posted='N' "
							+ " where order_details_id =:orderDetailsId and status='A' ")
					.setParameter("orderDetailsId", orderDetailsId)
					.executeUpdate();
			   }
		}catch(Exception e){
			
			throw e;
		}
	}
	

}

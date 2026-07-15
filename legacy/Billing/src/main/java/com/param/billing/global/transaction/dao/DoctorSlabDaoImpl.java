package com.param.billing.global.transaction.dao;

import java.sql.SQLData;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.DoctorSlabTransaction;
import com.param.billing.global.transaction.dto.DoctorSlabTransactionDto;
import com.param.billing.global.transaction.dto.TDoctorShareReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.dto.TDoctorShareSplitLogsReqDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DoctorSlabDaoImpl extends GenericDao<DoctorSlabTransaction, Integer> implements IDoctorSlabDao,ICommonConstants
{

	private static final Logger log = LoggerFactory.getLogger(DoctorSlabDaoImpl.class);

	public DoctorSlabDaoImpl() {
		super(DoctorSlabTransaction.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveDoctorSlabTransaction(DoctorSlabTransactionDto doctorSlabTransactionDto) {
		try {
			
			DoctorSlabTransaction doctorSlabTransaction = new DoctorSlabTransaction(); 
				doctorSlabTransaction.setBillDetailId(doctorSlabTransactionDto.getBillDetailId());
				doctorSlabTransaction.setDoctorId(doctorSlabTransactionDto.getDoctorId());
				doctorSlabTransaction.setDoctorRevenue(doctorSlabTransactionDto.getDoctorRevenue());
				doctorSlabTransaction.setRevenuePercentage(doctorSlabTransactionDto.getRevenuePercentage());
				doctorSlabTransaction.setOrganizationId(doctorSlabTransactionDto.getOrganizationId());
				doctorSlabTransaction.setUnitId(doctorSlabTransactionDto.getUnitId());
				doctorSlabTransaction.setStatus('A');
				doctorSlabTransaction.setCreatedDate(TimeZoneComponent.getDateByzone(doctorSlabTransactionDto.getUnitId()));
				doctorSlabTransaction.setUpdatedDate(TimeZoneComponent.getDateByzone(doctorSlabTransactionDto.getUnitId()));
				doctorSlabTransaction.setProcessAmount(doctorSlabTransactionDto.getProcessAmount());
				doctorSlabTransaction.setSplitRevenueAgainst(doctorSlabTransactionDto.getSplitRevenueAgainst());
			sessionFactory.getCurrentSession().save(doctorSlabTransaction);
			
		return new com.param.global.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		}catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getlistForDoctorSlab(TDoctorShareReqDto reqDto) {
		try{
			
			StringBuilder query = new StringBuilder(" select " 
					 + " 	slabTran.bill_detail_id as \"billDtlId\", " 
					 + " 	bd.order_details_id as \"ordDtlId\", " 
					 + " 	bd.service_id as \"serviceId\", " 
					 + " 	slabTran.doctor_id as \"docId\", " 
					 + " 	bd.organization_id as \"orgId\", " 
					 + " 	bd.unit_id as \"orgUnitId\", " 
					 + " 	slabTran.id as \"doctorSlabTransactionId\", " 
					 + " 	bd.rate as \"unitRate\", " 
					 + " 	bd.gross_amount as \"totalAmount\", " 
					 + " 	bd.concession as \"concession\", " 
					 + " 	bd.discount as \"discount\", " 
					 + " 	bd.tax_amount as \"taxAmt\", " 
					 + " 	bd.net_amt as \"netAmount\", " 
					 + " 	coalesce( case when enc.encounter_id >0 then to_char(enc.encounter_date, 'DD/MM/YYYY HH24:MI:SS') end, case when adm.admission_id > 0 then to_char(adm.created_date, 'DD/MM/YYYY HH24:MI:SS')end )as \"encounterDate\", " 
					 + " 	coalesce( case when enc.encounter_id >0 then (enc.encounter_number) end, case when adm.admission_id > 0 then (adm.admission_number) end ) as \"encounterNumber\", " 
					 + " 	to_char(bm.bill_date_time, 'DD/MM/YYYY HH24:MI:SS') as \"billDateTime\", " 
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
					 + " 	concat(coalesce(pre.prefix_code, ''), ' ', pat.first_name, ' ', pat.last_name) as \"patientName\", " 
					 + " 	om.order_no as \"orderNo\", " 
					 + " 	service.service_standard_code as \"serviceStandardCode\", " 
					 + " 	service.service_standard_name as \"serviceStandardName\", " 
					 + " 	bd.tax_id as \"taxId\", " 
					 + " 	(case when docref.id > 0 then cast('Y' as char) else cast('N' as char) end) as \"isShareSplit\", " 
					 + " 	docref.doctor_id as \"splitDocId\", " 
					 + " 	slabTran.doctor_revenue as \"doctorRevenue\", " 
					 + " 	docref.id as \"parentRevenueId\" " 
					 + " from " 
					 + " 	billing.t_doctor_slab_transaction slabTran " 
					 + " inner join billing.t_billing_details bd on " 
					 + " 	bd.billing_details_id = slabTran.bill_detail_id " 
					 + " inner join billing.m_billing_master bm on " 
					 + " 	bm.billing_master_id = bd.billing_master_id " 
					 + " 	and bm.bill_cancelled = 'N' " 
					 + " inner join public.t_order_details od on " 
					 + " 	od.order_details_id = bd.order_details_id " 
					 + " inner join public.t_order_master om on " 
					 + " 	om.order_id = od.order_id " 
					 + " inner join service.m_service_master service on " 
					 + " 	service.service_master_id = bd.service_id " 
					 + " left join public.m_tax tax on " 
					 + " 	tax.id = bd.tax_id " 
					 + " left join opd.t_encounter_master enc on " 
					 + " 	enc.encounter_id = bm.encounter_id " 
					 + " left join adt.t_admission adm on " 
					 + " 	adm.admission_id = bm.admission_id " 
					 + " inner join doctor.m_doctor_master doc on " 
					 + " 	doc.doctor_id = slabTran.doctor_id " 
					 + " inner join patient.t_patient_registration pat on " 
					 + " 	pat.patient_id = bm.patient_id " 
					 + " left join m_prefix_master pre on " 
					 + " 	pre.prefix_id = pat.prefix_id " 
					 + " inner join public.m_visit_type_master visitmst on " 
					 + " 	visitmst.visit_type_id = bm.visit_type_id " 
					 + " left join billing.t_doctor_slab_transaction docref on " 
					 + " 	docref.id = slabTran.split_revenue_against " );
			
			if(reqDto.getCompanyId()!=null || reqDto.getAssociateCompanyId()!=null)
			{
				 query.append("inner join billing.t_bill_details_payee_mapper payee " 
						 	+ "on payee.bill_details_id = bd.billing_details_id " ); 
			}
			
			String conditions =	 
				  " where " 
				 + " 	slabTran.organization_id =:orgId "
				 + "	and slabTran.process_amount = 0 " 
				 + " 	and slabTran.status = 'A' " 
				 + " 	and slabTran.unit_id =:orgUnitId "
				 + "	and od.is_dummy_service = false "
				 + "	and od.service_rendered = 1 ";
			query.append(conditions);

			if(reqDto.getVisitTypeId() != null)
				query.append(" AND bm.visit_type_id =:visitTypeId ");

			if(reqDto.getDoctorIds() != null)
				query.append(" AND slabTran.doctor_id IN (:docIds)  ");

			if(reqDto.getBillStatusId()==3)
			{
				query.append(" and bm.bill_status_id = 3 ");

			}else if(reqDto.getBillStatusId()==1 ){
					query.append(" and bm.bill_status_id IN (1,2) ");
			}

			if(reqDto.getPatientId() != null)
				query.append(" AND bm.patient_id =:patientId ");

			if(reqDto.getCompanyId() != null)
				query.append(" AND payee.payee_id =");

			if(reqDto.getAssociateCompanyId() != null)
				query.append(" AND payee.associate_company_id =:associateCompanyId ");

			if(reqDto.getOrderServiceId() != null)
				query.append(" AND service.service_master_id =:orderServiceId ");

			if(reqDto.getBillNumber() != null && reqDto.getBillNumber().length()>0 )
				query.append(" AND lower(bm.bill_number) like lower(:billNo)");

			if(reqDto.getPatientName() != null && reqDto.getPatientName().length()>0)
				query.append(" AND (lower(pat.first_name) like lower(:patientName) OR lower(pat.last_name) like lower(:patientName) OR lower(pat.uhid_number) like lower(:patientName)) ");
			
			query.append(" AND to_char(bm.bill_date_time,'YYYY-MM-DD') BETWEEN :fromDate AND :toDate order by doc.first_name ");

		   Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
				   .setParameter("orgId", reqDto.getOrgId())
				   .setParameter("orgUnitId", reqDto.getOrgUnitId())
				   .setParameter("fromDate", reqDto.getFromDate().substring(0,10))
				   .setParameter("toDate", reqDto.getToDate().substring(0,10))
				   .setParameter("orgUnitId", reqDto.getOrgUnitId())
				   .setParameterList("docIds", reqDto.getDoctorIds());

			if(reqDto.getVisitTypeId() != null)
				sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

			if(reqDto.getDoctorIds() != null)
				sqlQuery.setParameterList("docIds", reqDto.getDoctorIds());

			if(reqDto.getPatientId() != null)
				sqlQuery.setParameter("patientId", reqDto.getPatientId());

			if(reqDto.getCompanyId() != null)
				sqlQuery.setParameter("payeeId", reqDto.getCompanyId());

			if(reqDto.getAssociateCompanyId() != null)
				sqlQuery.setParameter("associateCompanyId", reqDto.getAssociateCompanyId());

			if(reqDto.getOrderServiceId() != null)
				sqlQuery.setParameter("orderServiceId", reqDto.getOrderServiceId());

			if(reqDto.getBillNumber() != null && reqDto.getBillNumber().length()>0 )
				sqlQuery.setParameter("billNo", "%" + reqDto.getBillNumber().toLowerCase() + "%");

			if(reqDto.getPatientName() != null && reqDto.getPatientName().length()>0 )
				sqlQuery.setParameter("patientName", "%" + reqDto.getPatientName().toLowerCase() + "%");


			List<TDoctorShareRespDto> listTDoctorShareRespDto = sqlQuery
				   .setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTDoctorShareRespDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response splitRevenue(TDoctorShareSplitLogsReqDto masterDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getRevenueTransactionById(Integer doctorSlabTransactionId) {
		DoctorSlabTransaction doctorSlabTransaction = findById(doctorSlabTransactionId);
		if (doctorSlabTransaction.getId() != 0)
		{
	     	return new Response<>(SUCCESS, SUCCESS_CODE, null, null, doctorSlabTransaction);
		}else {
	     	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	public Response updateDoctorSlabTransaction(DoctorSlabTransaction doctorSlabTransaction) {
		try{
			if (doctorSlabTransaction !=null)
			{
				doctorSlabTransaction = save(doctorSlabTransaction);
		     	return new com.param.global.common.Response<>(SUCCESS, SUCCESS_CODE, null, null, doctorSlabTransaction);
			}else {
		     	return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, doctorSlabTransaction);
			}
			
		}catch(Exception e){
			
			throw e;
			
			//throw new ServiceException(e.getMessage());
		}
		//return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, doctorSlabTransaction);
	}

	@Override
	public Response getDoctorSlabSplitDetails(Integer id) {
		try{
				String query =    " select " 
						 + " 	tbd.net_amt as \"netAmount\", " 
						 + " 	splitLog.doctor_id as \"docId\", " 
						 + " 	concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.last_name)) as \"doctorName\", " 
						 + " 	splitLog.revenue_percentage as \"oldRevenuePercentage\", " 
						 + " 	splitLog.doctor_revenue as \"oldDoctorRevenue\", " 
						 + " 	splitLog.doctor_revenue as \"newDoctorRevenue\", " 
						 + "  	splitLog.id as \"doctorShareSplitId\", " 
						 + " 	splitLog.bill_detail_id as \"billDtlId\", " 
						 + " 	splitLog.revenue_percentage as \"newRevenuePercentage\", " 
						  + " 	( " 
						 + " 	select " 
						 + " 		voucher_dtls_id " 
						 + " 	from " 
						 + " 		billing.t_doc_share_voucher_dtls " 
						 + " 	where " 
						 + " 		doctor_slab_transaction_id = splitLog.id " 
						 + " 	order by " 
						 + " 		1 desc " 
						 + " 	limit 1) as \"voucherDtlsId\" "
						 + " from " 
						 + " 	billing.t_doctor_slab_transaction splitLog " 
						 + " inner join billing.t_billing_details tbd on " 
						 + " 	tbd.billing_details_id = splitLog.bill_detail_id " 
						 + " inner join doctor.m_doctor_master doc on " 
						 + " 	doc.doctor_id = splitLog.doctor_id  " 
						 + " where " 
						 + " 	(splitLog.id = :id OR splitLog.split_revenue_against =:id) " 
						 + " 	and splitLog.status = 'A' " 
						 + " order by " 
						 + " 	splitLog.id asc " ;
				
				List<TDoctorShareRespDto> listTDoctorShareRespDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("id",id)
						.setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();
				
		     	return new com.param.global.common.Response<>(SUCCESS, SUCCESS_CODE, null, null, listTDoctorShareRespDto);
			
		}catch(Exception e){
			
			throw e;
		}
		//return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response changeDoctor(TDoctorShareRespDto masterDto) {
		try{
			
			int j = sessionFactory.getCurrentSession().createSQLQuery("update "
					+"  billing.t_doctor_slab_transaction set "
					+" 		doctor_id =:docId "
					+"  where "
					+" 		id =:doctorSlabTransactionId ")
					.setParameter("doctorSlabTransactionId", masterDto.getDoctorSlabTransactionId())
					.setParameter("docId", masterDto.getDocId())
			.executeUpdate();
			if(j==1)
				return new Response<>(SUCCESS, SUCCESS_CODE, "Doctor Updated Successfully...!!", null, null);
			else
				return new Response<>(ERROR, null, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response inactiveSplitData(TDoctorShareRespDto masterDto) {
		try{
			int i = sessionFactory.getCurrentSession().createSQLQuery("update "
						+"  billing.t_doctor_slab_transaction set "
						+" 		status ='I' "
						+"  where "
						+" 		id =:doctorRevenueSplitId ")
					.setParameter("doctorRevenueSplitId", masterDto.getDoctorShareSplitId())
				.executeUpdate();
				
				int k = sessionFactory.getCurrentSession().createSQLQuery("update "
						+"  billing.t_doctor_slab_transaction set "
						+" 		doctor_revenue =:doctorRevenue "
						+"  where "
						+ " 	id=:parentId ")
					.setParameter("doctorRevenue", masterDto.getDoctorRevenue().doubleValue())
					.setParameter("parentId", masterDto.getDoctorSlabTransactionId())
				.executeUpdate();
				
			if(i==1 && k==1)
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			else
				return new Response<>(ERROR, null, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getVoucherDetails(TDoctorShareReqDto tDoctorShareDto) {
		try{
			
			String query = "select " 
					 + " 	to_char(bm.bill_date_time, 'DD/MM/YYYY HH24:MI:SS') as \"billDateTime\", " 
					 + " 	bm.bill_number as \"billNumber\", " 
					 + " 	concat(coalesce(pre.prefix_code, ''), ' ', pat.first_name, ' ', pat.last_name) as \"patientName\", " 
					 + " 	pat.uhid_number as \"uhidNumber\", " 
					 + " 	bm.visit_type_id as \"visitTypeId\", " 
					 + " 	visitmst.visit_type_code as \"visitTypeCode\", " 
					 + " 	to_char(enc.encounter_date, 'DD/MM/YYYY') as \"encounterDate\", " 
					 + " 	enc.encounter_number as \"encounterNumber\", " 
					 + " 	to_char(adm.created_date, 'DD/MM/YYYY') as \"admissionDate\", " 
					 + " 	adm.admission_number as \"admissionNumber\", " 
					 + " 	concat(doc.first_name, ' ', doc.last_name) as \"doctorName\", " 
					 + " 	doc.doctor_id as \"docId\", " 
					 + " 	spl.speciality_name as \"specialityName\", " 
					 + " 	service.service_standard_code as \"serviceStandardCode\", " 
					 + " 	service.service_standard_name as \"serviceStandardName\", " 
					 + " 	bdtl.rate as \"unitRate\", " 
					 + " 	bdtl.gross_amount as \"totalAmount\", " 
					 + " 	bdtl.quantity as \"quantity\", " 
					 + " 	bdtl.discount as \"discount\", " 
					 + " 	bdtl.tax_amount as \"taxAmt\", " 
					 + " 	tdst.doctor_revenue as \"doctorRevenue\", " 
					 + " 	bdtl.net_amt as \"netAmount\", " 
					 + " 	dsvd.processed_amt as \"processedShareAmt\" " 
					 + " from " 
					 + " 	billing.t_doc_share_voucher dsv " 
					 + " inner join billing.t_doc_share_voucher_dtls dsvd on " 
					 + " 	dsvd.doc_share_vouche_id = dsv.doc_share_vouche_id " 
					 + " inner join billing.t_doctor_slab_transaction tdst on " 
					 + " 	tdst.id = dsvd.doctor_slab_transaction_id  " 
					 + " inner join billing.t_billing_details bdtl on " 
					 + " 	bdtl.billing_details_id = tdst.bill_detail_id  " 
					 + " inner join billing.m_billing_master bm on " 
					 + " 	bm.billing_master_id = bdtl.billing_master_id " 
					 + " inner join patient.t_patient_registration pat on " 
					 + " 	pat.patient_id = bm.patient_id " 
					 + " inner join public.m_prefix_master pre on " 
					 + " 	pre.prefix_id = pat.prefix_id " 
					 + " inner join public.m_visit_type_master visitmst on " 
					 + " 	visitmst.visit_type_id = bm.visit_type_id " 
					 + " left join opd.t_encounter_master enc on " 
					 + " 	enc.encounter_id = bm.encounter_id " 
					 + " 	and bm.visit_type_id = 1 " 
					 + " left join adt.t_admission adm on " 
					 + " 	adm.admission_id = bm.admission_id " 
					 + " 	and bm.visit_type_id != 1 " 
					 + " left join doctor.m_doctor_master doc on " 
					 + " 	doc.doctor_id = bdtl.ord_doc_id " 
					 + " left join doctor.m_speciality_master spl on " 
					 + " 	spl.speciality_id = bdtl.ord_doc_spl_id " 
					 + " inner join service.m_service_master service on " 
					 + " 	service.service_master_id = bdtl.service_id  " 
					 + " where " 
					 + " 	dsv.org_id =:orgId " 
					 + " 	and dsv.org_unit_id =:orgUnitId " 
					 + " 	and dsv.doc_share_vouche_id =:docShareVoucherId " ;
			
			
			List<TDoctorShareRespDto> listTDoctorShareRespDto = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("orgId", tDoctorShareDto.getOrgId())
					.setParameter("orgUnitId", tDoctorShareDto.getOrgUnitId())
					.setParameter("docShareVoucherId", tDoctorShareDto.getDocShareVoucherId())
					.setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTDoctorShareRespDto, null);
			
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response voucherSlabConsumptionDetails(Integer docShareVoucherId) {
		try{
			
			String query = " select " 
					 + " 	mdsm.slab_from as \"slabFrom\", " 
					 + " 	mdsm.slab_to as \"slabTo\", " 
					 + " 	docshare.total_doc_share_amout as \"totalDocRevenue\", " 
					 + " 	voucherDtls.revenue_processed as \"processAmount\", "
					 + " 	mdsm.percentage as \"slabPercentage\", "
					 + " 	voucherDtls.revenue_generated_against as \"revenueGeneratedAgainst\" "
					 + " from " 
					 + " 	billing.t_doctor_slab_voucher_details voucherDtls " 
					 + " inner join billing.t_doc_share_voucher docshare on " 
					 + " 	voucherDtls.voucher_id = docshare.doc_share_vouche_id " 
					 + " inner join billing.m_doctor_slab_master mdsm on " 
					 + " 	mdsm.doctor_slab_master_id = voucherDtls.doctor_slab_master_id " 
					 + " where " 
					 + " 	voucherDtls.voucher_id =:voucherId " 
					 + " 	and voucherDtls.status = 'A' " 
					 + " order by " 
					 + " 	mdsm.slab_from ";
			
			
			List<DoctorSlabTransactionDto> listTDoctorShareRespDto = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("voucherId", docShareVoucherId)
					.setResultTransformer(Transformers.aliasToBean(DoctorSlabTransactionDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTDoctorShareRespDto, null);
			
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
}

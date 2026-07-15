package com.param.billing.global.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.param.billing.global.dto.RefundCollectionReqDto;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.global.dto.RefundCnSearchDto;
import com.param.billing.global.transaction.dto.BankDetailsDto;
import com.param.billing.global.transaction.dto.CreditNoteDetailsDto;
import com.param.billing.global.transaction.dto.PaymentRefundDetailsDto;
import com.param.billing.global.transaction.dto.PaymentRefundMasterDto;
import com.param.billing.global.transaction.dto.RefundSearchDto;
import com.param.billing.global.transaction.model.PaymentRefundMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class PaymentRefundMasterDaoImpl extends GenericDao<PaymentRefundMaster, Integer> implements IPaymentRefundMasterDao, ICommonConstants {

	public PaymentRefundMasterDaoImpl() {
		super(PaymentRefundMaster.class);
	}

	@Override
	public Response savePaymentRefundMasterDto(PaymentRefundMasterDto paymentRefundMasterDto) throws BillingException {
		try{
			PaymentRefundMaster master = new PaymentRefundMaster();
				master.setPaymentRefundVoucherNo(paymentRefundMasterDto.getPaymentRefundVoucherNo());
				master.setDateOfRefund(paymentRefundMasterDto.getDateOfRefund() != null && !paymentRefundMasterDto.getDateOfRefund().isEmpty() ? GlobalCommonDateUtils.getDate(paymentRefundMasterDto.getDateOfRefund(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(paymentRefundMasterDto.getUnitId()));
				master.setIsCnOrDeposit(paymentRefundMasterDto.getIsCnOrDeposit());
				master.settRefundCnId(paymentRefundMasterDto.gettRefundCnId());
				master.setCreditNoteId(paymentRefundMasterDto.getCreditNoteId());
				master.settPharmacyReturnId(paymentRefundMasterDto.gettPharmacyReturnId());
				master.setAmountRefunded(paymentRefundMasterDto.getAmountRefunded());
				master.setPatientId(paymentRefundMasterDto.getPatientId());
				master.setReceivedFrom(paymentRefundMasterDto.getReceivedFrom());
				master.setIdentificationNo(paymentRefundMasterDto.getIdentificationNo());
				master.setNameOfPerson(paymentRefundMasterDto.getNameOfPerson());
				master.setStatus(paymentRefundMasterDto.getStatus());
				master.setOrgnisationId(paymentRefundMasterDto.getOrgnisationId());
				master.setSaleReturnId(paymentRefundMasterDto.getSaleReturnId());
				master.setUnitId(paymentRefundMasterDto.getUnitId());
				master.setCreatedBy(paymentRefundMasterDto.getCreatedBy());
				master.setCreatedDate(paymentRefundMasterDto.getCreatedDate() != null && !paymentRefundMasterDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(paymentRefundMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(paymentRefundMasterDto.getUnitId()));
				master.setUpdatedBy(paymentRefundMasterDto.getUpdatedBy());
				master.setUpdatedDate(paymentRefundMasterDto.getUpdatedDate() != null && !paymentRefundMasterDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(paymentRefundMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(paymentRefundMasterDto.getUnitId()));
				master.setCounterId(paymentRefundMasterDto.getCounterId());
			master = save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@Override
	public BigInteger generateRefundVoucherNo() throws BillingException {
		try{
			BigInteger id = (BigInteger)sessionFactory.getCurrentSession().getNamedQuery("GENERATE_REFUND_VOUCHER_NO").uniqueResult();
			return id;
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response searchRefundMaster(RefundCnSearchDto reqDto) throws BillingException {
		try{
			
			
			if(reqDto.getFromDate() == null || reqDto.getToDate() == null || reqDto.getFromDate().isEmpty() || reqDto.getToDate().isEmpty())
			{
				reqDto.setFromDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()), "yyyy-MM-dd"));
				reqDto.setToDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()), "yyyy-MM-dd"));
			}
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_REFUND_MASTER_LIST").getQueryString());

			if(reqDto.getPatientName() != null && !reqDto.getPatientName().isEmpty())
				query.append(" and ( trim(lower(pat.first_name)) like :patientName or trim(lower(pat.middle_name)) like :patientName or trim(lower(pat.last_name)) like :patientName) ");

			if(reqDto.getUhidNumber() != null && !reqDto.getUhidNumber().isEmpty())
				query.append( " and trim(lower(pat.uhid_number)) like :uhidNumber ");

			if(reqDto.getRefundCnNo()!= null  && !reqDto.getRefundCnNo().isEmpty())
				query.append(" and lower(refund_cn.refund_cn_no) like :refundCnNo ");

			if(reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty())
				query.append(" and to_char(paymnt_refnd_mast.date_of_refund,'YYYY-MM-DD') >=:fromDate ");

			if(reqDto.getToDate() != null && !reqDto.getToDate().isEmpty())
				query.append(" and to_char(paymnt_refnd_mast.date_of_refund,'YYYY-MM-DD') <=:toDate ");

			if(reqDto.getPaymentRefundVoucherNo()!= null  && !reqDto.getPaymentRefundVoucherNo().isEmpty())
				query.append(" and lower(paymnt_refnd_mast.payment_refund_voucher_no) like :paymentRefundVoucherNo ");

			query.append(" order by paymnt_refnd_mast.date_of_refund desc ");

			Query sqlQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId());

			/*Integer count=0;
			List<RefundSearchDto> listRefundSearchDto1= sqlQuery
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(RefundSearchDto.class)).list();*/

			if(reqDto.getPatientName() != null && !reqDto.getPatientName().isEmpty())
				sqlQuery.setParameter("patientName", "%" + reqDto.getPatientName().toLowerCase() + "%");

			if(reqDto.getUhidNumber() != null && !reqDto.getUhidNumber().isEmpty())
				sqlQuery.setParameter("uhidNumber", "%" + reqDto.getUhidNumber().toLowerCase() + "%");

			if(reqDto.getRefundCnNo() != null && !reqDto.getRefundCnNo().isEmpty())
				sqlQuery.setParameter("refundCnNo", "%" + reqDto.getRefundCnNo().toLowerCase() + "%");

			if(reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty())
				sqlQuery.setParameter("fromDate", reqDto.getFromDate());

			if(reqDto.getToDate() != null && !reqDto.getToDate().isEmpty())
				sqlQuery.setParameter("toDate", reqDto.getToDate());

			if(reqDto.getPaymentRefundVoucherNo() != null && !reqDto.getPaymentRefundVoucherNo().isEmpty())
				sqlQuery.setParameter("paymentRefundVoucherNo", "%" + reqDto.getPaymentRefundVoucherNo().toLowerCase() + "%");

			List<RefundSearchDto> listRefundSearchDto1 = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(RefundSearchDto.class)).list();

			List<RefundSearchDto> listRefundSearchDto = sqlQuery
					.setFirstResult(reqDto.getOffset() != null ? reqDto.getOffset() : 0)
					.setMaxResults(reqDto.getNoOfRecordsPerPage() != null ? reqDto.getNoOfRecordsPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(RefundSearchDto.class)).list();

			int count = (listRefundSearchDto1 != null) ? listRefundSearchDto1.size() : 0;

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listRefundSearchDto, count);

		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getRefundDetailsByRefundId(int orgId, int unitId, int paymentRefundVoucherId)
			throws BillingException {
		try{
			List<PaymentRefundDetailsDto> listPaymentRefundDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_REFUND_DETAILS_BY_REFUND_ID")
					.setParameter("paymentRefundId", paymentRefundVoucherId)
					.setResultTransformer(Transformers.aliasToBean(PaymentRefundDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPaymentRefundDetailsDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getRefundBankDetails(int orgId, int unitId, int tRefundCnId) throws BillingException {
		try{
			List<BankDetailsDto> listBankDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_BANK_DETAILS_BY_VOUCHER_ID")
					.setParameter("tRefundCnId", tRefundCnId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BankDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listBankDetailsDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response searchRefundedCreditNote(RefundCnSearchDto reqDto) throws BillingException {
	try{
			
		if(reqDto.getFromDate() == null || reqDto.getToDate() == null || reqDto.getFromDate().isEmpty() || reqDto.getToDate().isEmpty())
		{
			reqDto.setFromDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()), "yyyy-MM-dd"));
			reqDto.setToDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()), "yyyy-MM-dd"));
		}
			
		StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_REFUND_MASTER_CR_LIST").getQueryString());

		if(reqDto.getPatientName() != null && !reqDto.getPatientName().isEmpty())
			query.append(" and ( trim(lower(pat.first_name)) like :patientName or trim(lower(pat.middle_name)) like :patientName or trim(lower(pat.last_name)) like :patientName ) ");

		if(reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty())
			query.append(" and to_char(paymnt_refnd_mast.date_of_refund,'YYYY-MM-DD') >=:fromDate ");

		if(reqDto.getToDate() != null && !reqDto.getToDate().isEmpty())
			query.append(" and to_char(paymnt_refnd_mast.date_of_refund,'YYYY-MM-DD') <=:toDate ");

		if(reqDto.getUhidNumber() != null && !reqDto.getUhidNumber().isEmpty())
			query.append(" and trim(lower(pat.uhid_number)) like :uhidNumber ");

		if(reqDto.getRefundCrNo() != null && !reqDto.getRefundCrNo().isEmpty())
			query.append(" and lower(crnotemst.credit_note_no) like :refundCrNo ");

		if(reqDto.getPaymentRefundVoucherNo() != null && !reqDto.getPaymentRefundVoucherNo().isEmpty())
			query.append(" and lower(paymnt_refnd_mast.payment_refund_voucher_no) like :paymentRefundVoucherNo ");

		query.append(" order by paymnt_refnd_mast.date_of_refund desc ");

		Query sqlQuery = sessionFactory.getCurrentSession()
				.createSQLQuery(query.toString())
				.setInteger("orgId", reqDto.getOrgId())
				.setInteger("unitId", reqDto.getUnitId());

		if(reqDto.getPatientName() != null && !reqDto.getPatientName().isEmpty())
			sqlQuery.setParameter("patientName", "%" + reqDto.getPatientName().toLowerCase() + "%");

		if(reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty())
			sqlQuery.setParameter("fromDate", reqDto.getFromDate());

		if(reqDto.getToDate() != null && !reqDto.getToDate().isEmpty())
			sqlQuery.setParameter("toDate", reqDto.getToDate());

		if(reqDto.getUhidNumber() != null && !reqDto.getUhidNumber().isEmpty())
			sqlQuery.setParameter("uhidNumber", "%" + reqDto.getUhidNumber().toLowerCase() + "%");

		if(reqDto.getRefundCrNo() != null && !reqDto.getRefundCrNo().isEmpty())
			sqlQuery.setParameter("refundCrNo", "%" + reqDto.getRefundCrNo().toLowerCase() + "%");

		if(reqDto.getPaymentRefundVoucherNo() != null && !reqDto.getPaymentRefundVoucherNo().isEmpty())
			sqlQuery.setParameter("paymentRefundVoucherNo", "%" + reqDto.getPaymentRefundVoucherNo().toLowerCase() + "%");


	List<RefundSearchDto> listRefundSearchDto = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(RefundSearchDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listRefundSearchDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response searchRefundedCreditNoteDetails(int orgId, int unitId, int paymentRefundVoucherId)
			throws BillingException {
		try{
			List<PaymentRefundDetailsDto> listPaymentRefundDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_REFUND_DETAILS_BY_REFUND_ID")
					.setParameter("paymentRefundId", paymentRefundVoucherId)
					.setResultTransformer(Transformers.aliasToBean(PaymentRefundDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPaymentRefundDetailsDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response checkIsDoctroSharePosted(Integer creditNoteId) {
		try{
			
			String query = " select " 
					 + " 	cnd.order_detail_id as \"orderDetailId\", " 
					 + " 	cnd.cr_discount_amount as \"crDiscountAmount\", " 
					 + " 	cnd.cr_service_rate_amount as \"crServiceRateAmount\", " 
					 + " 	cnd.cr_additional_discount as \"crAdditionalDiscount\", " 
					 + " 	cnd.cr_tax_amount as \"crTaxAmount\", " 
					 + " 	cnd.cr_net_amount as \"crNetAmount\", "
					 + "	cnm.cancel_or_discount as \"cancelOrDiscount\", "
					 + "	bds.gross_amount - cnd.cr_additional_discount as \"serviceNewNetAmount\" " 
					 + " from " 
					 + " 	billing.t_credit_note_detail cnd " 
					 + " inner join billing.m_credit_note_master cnm on " 
					 + " 	cnd.credit_note_id = cnm.credit_note_id " 
					 + " inner join billing.t_billing_details bds on " 
					 + " 	bds.billing_details_id = cnd.billing_details_id " 
					 + " where " 
					 + " 	bds.is_doctor_share_posted = 'Y' " 
					 + " 	and cnm.credit_note_id =:creditNoteId ";
			
			List<CreditNoteDetailsDto> listPaymentRefundDetailsDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("creditNoteId", creditNoteId)
					.setResultTransformer(Transformers.aliasToBean(CreditNoteDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPaymentRefundDetailsDto, null);
			
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	public Response checkExistingRefundApproval(RefundCollectionReqDto refundCollectionReqDto, int depositId, int tRefundCnId) {
		try{
			List<Integer> paymentRefundIdsList = new ArrayList<>();
			String query = " select "
					+ " 	refMst.payment_refund_id as \"paymentRefundId\" "
					+ " from "
					+ " 	billing.t_payment_refund_master refMst "
					+ " where "
					+ " 	refMst.status = 'A' "
					+ " 	and refMst.t_refund_cn_id = :tRefundCnId "
					+ " 	and refMst.amount_refunded = :refundAmount "
					+ " 	and refMst.patient_id =:patientId "
					+ " 	and refMst.unit_id =:unitId "
					+ " 	and refMst.orgnisation_id =:organizationId ";

			paymentRefundIdsList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("tRefundCnId", tRefundCnId)
					.setParameter("refundAmount", refundCollectionReqDto.getPaymentRefundMasterDto().getAmountRefunded())
					.setParameter("patientId", refundCollectionReqDto.getPaymentRefundMasterDto().getPatientId() != null ? refundCollectionReqDto.getPaymentRefundMasterDto().getPatientId() : 0)
					.setParameter("unitId", refundCollectionReqDto.getPaymentRefundMasterDto().getUnitId() != null ? refundCollectionReqDto.getPaymentRefundMasterDto().getUnitId() : 0)
					.setParameter("organizationId", refundCollectionReqDto.getPaymentRefundMasterDto().getOrgnisationId() != null ? refundCollectionReqDto.getPaymentRefundMasterDto().getOrgnisationId() : 0)
					.list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, paymentRefundIdsList, null);

		}catch(Exception e){

			throw e;
		}
	}
}

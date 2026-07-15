package com.param.billing.global.transaction.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.ICommonConstants;
import com.param.billing.global.transaction.dto.PaymentInvoicesDetailsDto;
import com.param.billing.global.transaction.model.PaymentReceiptDetails;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class PaymentRecieptDetailsDaoImpl extends GenericDao<PaymentReceiptDetails, Integer> implements IPaymentRecieptDetailsDao,ICommonConstants{

	public PaymentRecieptDetailsDaoImpl() {
		super(PaymentReceiptDetails.class);
	}

	@Autowired
	private DozerBeanMapper mapper;
	
	@Override
	public Response savePaymentRecieptDetails(PaymentReceiptDetailsDto paymentReceiptDetailsDto)
			throws ApplicationException {
		try {
			PaymentReceiptDetails paymentRecieptDetails = mapper.map(paymentReceiptDetailsDto, PaymentReceiptDetails.class,"PaymentReceiptDetailsDto_to_PaymentRecieptDetails");
				paymentRecieptDetails.setCreatedDate(TimeZoneComponent.getDateByzone(paymentReceiptDetailsDto.getUnitId()));
				paymentRecieptDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(paymentReceiptDetailsDto.getUnitId()));
				paymentRecieptDetails.setDateOfTransaction(TimeZoneComponent.getDateByzone(paymentReceiptDetailsDto.getUnitId()));
			paymentRecieptDetails=save(paymentRecieptDetails);
			paymentReceiptDetailsDto.setPaymentRecieptDetailsId(paymentRecieptDetails.getPaymentRecieptDetailsId());
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, paymentReceiptDetailsDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response paymentRecieptByBillId(PaymentInvoicesDetailsDto paymentInvoicesDetailsDto)
			throws ApplicationException {
   try{
	    StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_PAYMENT_INVOICES_BY_BILL_ID").getQueryString());
		List<PaymentInvoicesDetailsDto> listPaymentInvoicesDetailsDto= sessionFactory.getCurrentSession().createSQLQuery(query.toString()).setParameter("orgId",  paymentInvoicesDetailsDto.getOrgId()).setParameter("orgUnitId", paymentInvoicesDetailsDto.getOrgUnitId()).setParameter("billId", paymentInvoicesDetailsDto.getBillId())
				   .setResultTransformer(Transformers.aliasToBean(PaymentInvoicesDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS, listPaymentInvoicesDetailsDto, null);
		}catch(Exception e){
			
			throw e;
			//throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymentReceieptDetailsForAddendumByBill(int billId) throws ApplicationException {
		try{
			List<PaymentReceiptDetailsDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_PAYMENT_RECIEPT_DETAILS_FOR_ADDENDUM_BY_BILL_ID").setParameter("billId", billId).setResultTransformer(Transformers.aliasToBean(PaymentReceiptDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	
	@Override
	public List<PaymentReceiptDetails> getPaymentReceieptDetailsForBillCancellation(int payemntRecieptId) throws ApplicationException {
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(PaymentReceiptDetails.class);
			c.add(Restrictions.eq("paymentReceiptMasterId", payemntRecieptId));
			c.add(Restrictions.eq("status", 'A'));
			List<PaymentReceiptDetails> paymentReceiptDetailsList = c.list();

			return paymentReceiptDetailsList;
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}

	@Override
	public Response inactiveRecieptDetails(PaymentReceiptDetails receiptDetails) {
		try {

				sessionFactory.getCurrentSession().update(receiptDetails);

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	
	
	
}

package com.param.billing.global.transaction.dao;

import java.math.BigInteger;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.ICommonConstants;
import com.param.billing.global.transaction.model.PaymentRecieptMaster;
import com.param.global.billing.dto.PaymentRecieptMasterDto;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class PaymentRecieptMasterDaoImpl extends GenericDao<PaymentRecieptMaster, Integer> implements IPaymentRecieptMasterDao,ICommonConstants{

	public PaymentRecieptMasterDaoImpl() {
		super(PaymentRecieptMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;
	
	@Override
	public Response savePaymentRecieptMaster(PaymentRecieptMasterDto paymentRecieptMasterDto)
			throws ApplicationException {
		try {
			PaymentRecieptMaster paymentRecieptMaster = mapper.map(paymentRecieptMasterDto, PaymentRecieptMaster.class, "PaymentRecieptMasterDto_to_PaymentRecieptMaster"); 
				paymentRecieptMaster.setCreatedDate(TimeZoneComponent.getDateByzone(paymentRecieptMasterDto.getUnitId()));
				paymentRecieptMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(paymentRecieptMasterDto.getUnitId()));
				paymentRecieptMaster.setStatus('A');
				paymentRecieptMaster.setRecieptCancelled('N');
			paymentRecieptMaster = save(paymentRecieptMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, paymentRecieptMaster);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPaymentRecieptsByBillId(int billId) throws ApplicationException {
		try {
			List<PaymentRecieptMasterDto> listPaymentRecieptMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_PAYMENT_RECEIPTS_FOR_ADDENDUM_BY_BILL").setParameter("billId", billId).setResultTransformer(Transformers.aliasToBean(PaymentRecieptMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPaymentRecieptMasterDto, null);
		}catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response updatePaymentReceiptAsCancelById(PaymentRecieptMasterDto dto) throws ApplicationException {
		try {
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_PAYMENT_RECEIPT_AS_CANCEL_FOR_ADDENDUM_BILL").setParameter("cancelledBy", dto.getCancelledBy()).setParameter("remarks", dto.getCancelRemarks())
							.setParameter("receiptId", dto.getPaymentRecieptId()).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		}catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Long getNextValRecieptMasterId() throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			SQLQuery query = session.createSQLQuery("select nextval('billing.payment_reciept_master_seq')");
			Long id = ((BigInteger) query.uniqueResult()).longValue();

			SQLQuery query2 = session.createSQLQuery("select setval('billing.payment_reciept_master_seq'," + (id-1) + ")");
			query2.uniqueResult();

			return id;
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}

	
}


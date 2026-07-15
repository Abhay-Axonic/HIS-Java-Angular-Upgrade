package com.param.global.billing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.global.model.PaymentModeMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.PaymentEntitlementMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })

public class PaymentModeMasterDaoImpl extends GenericDao<PaymentModeMaster, Integer>
		implements IPaymentModeMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(PaymentModeMasterDaoImpl.class);

	public PaymentModeMasterDaoImpl() {
		super(PaymentModeMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response savePaymentModeMaster(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			PaymentModeMaster paymentModeMaster = new PaymentModeMaster();
			paymentModeMaster.setPaymentModeCode(paymentEntitlementMasterDto.getPaymentEntitlementCode());
			paymentModeMaster.setPaymentModeDesc(paymentEntitlementMasterDto.getPaymentEntitlementDesc());
			paymentModeMaster.setCreatedBy(paymentEntitlementMasterDto.getCreatedBy());
			paymentModeMaster.setUpdatedBy(paymentEntitlementMasterDto.getUpdatedBy());
			paymentModeMaster.setCreatedDate(
					GlobalCommonDateUtils.getDate(paymentEntitlementMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			paymentModeMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(paymentEntitlementMasterDto.getUpdateDate(), "dd-M-yyyy HH:mm:ss"));
			paymentModeMaster.setStatus(paymentEntitlementMasterDto.getStatus());
			paymentModeMaster.setOrganizationId(paymentEntitlementMasterDto.getOrganizationId());
			save(paymentModeMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("savePaymentModeMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("savePaymentModeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymentModeMasterById(int payModeId, int orgId) throws ApplicationException {
		try {
			List<PaymentEntitlementMasterDto> paymentEntitlementMasterDtoList=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_PAYMENT_MODE_MASTER_BY_ID")
				.setParameter("payModeId", payModeId)
				.setParameter("orgId", orgId)
				.setResultTransformer(Transformers.aliasToBean(PaymentEntitlementMasterDto.class))
				.list();		
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, paymentEntitlementMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getPaymentModeMasterById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPaymentModeMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymentModeMasterList(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			List<PaymentEntitlementMasterDto> paymentEntitlementMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAYMENT_MODE_MASTER_LIST")
					.setParameter("orgId", paymentEntitlementMasterDto.getOrganizationId())
					.setFirstResult(paymentEntitlementMasterDto.getOffset() != null ? paymentEntitlementMasterDto.getOffset() : 0)
					.setMaxResults(paymentEntitlementMasterDto.getNoOfRecordsPerPage() != null ? paymentEntitlementMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(PaymentEntitlementMasterDto.class))
					.list();		
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, paymentEntitlementMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getPaymentModeMasterList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPaymentModeMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePaymentModeMaster(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {

			PaymentModeMaster paymentModeMaster = findById(paymentEntitlementMasterDto.getPaymentEntitlementId());
			paymentModeMaster.setPaymentModeCode(paymentEntitlementMasterDto.getPaymentEntitlementCode());
			paymentModeMaster.setPaymentModeDesc(paymentEntitlementMasterDto.getPaymentEntitlementDesc());
			paymentModeMaster.setUpdatedBy(paymentEntitlementMasterDto.getUpdatedBy());
			paymentModeMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(paymentEntitlementMasterDto.getUpdateDate(), "dd-M-yyyy HH:mm:ss"));
			update(paymentModeMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updatePaymentModeMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updatePaymentModeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePaymentModeMasterStatus(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			PaymentModeMaster paymentModeMaster = findById(paymentEntitlementMasterDto.getPaymentEntitlementId());
			paymentModeMaster.setStatus(paymentEntitlementMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			paymentModeMaster.setUpdatedBy(paymentEntitlementMasterDto.getUpdatedBy());
			paymentModeMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(paymentEntitlementMasterDto.getUpdateDate(), "dd-M-yyyy HH:mm:ss"));
			update(paymentModeMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updatePaymentModeMasterStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updatePaymentModeMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getPaymentModeMasterCount(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(PaymentModeMaster.class);
			c.add(Restrictions.eq("organizationId", paymentEntitlementMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			logger.error("getPaymentModeMasterCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPaymentModeMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActivePaymentModeMasterList(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			List<PaymentEntitlementMasterDto> paymentEntitlementMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_PAYMENT_MODE_MASTER_LIST")
					.setParameter("orgId", paymentEntitlementMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PaymentEntitlementMasterDto.class))
					.list();		
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, paymentEntitlementMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getActivePaymentModeMasterList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getActivePaymentModeMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymentModeMasterListByName(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			List<PaymentEntitlementMasterDto> paymentEntitlementMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAYMENT_MODE_MASTER_LIST_BY_NAME")
					.setParameter("name", paymentEntitlementMasterDto.getPaymentEntitlementDesc().toLowerCase())
					.setParameter("orgId", paymentEntitlementMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PaymentEntitlementMasterDto.class))
					.list();		
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, paymentEntitlementMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getPaymentModeMasterListByName failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPaymentModeMasterListByName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymentModeMasterListByNameNotById(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			List<PaymentEntitlementMasterDto> paymentEntitlementMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAYMENT_MODE_MASTER_LIST_BY_NAME_NOT_BY_ID")
					.setParameter("name", paymentEntitlementMasterDto.getPaymentEntitlementDesc().toLowerCase())
					.setParameter("payModeId", paymentEntitlementMasterDto.getPaymentEntitlementId())
					.setParameter("orgId", paymentEntitlementMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PaymentEntitlementMasterDto.class))
					.list();		
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, paymentEntitlementMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getPaymentModeMasterListByNameNotById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPaymentModeMasterListByNameNotById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}

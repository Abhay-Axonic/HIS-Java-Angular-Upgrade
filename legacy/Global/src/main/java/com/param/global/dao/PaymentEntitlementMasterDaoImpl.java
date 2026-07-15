package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CommonDto;
import com.param.global.dto.PaymentEntitlementMasterDto;
import com.param.global.model.PaymentEntitlementMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PaymentEntitlementMasterDaoImpl extends GenericDao<PaymentEntitlementMaster, Integer>
		implements IPaymentEntitlementMasterDao, ICommonConstants, IError {

	final static Logger logger = Logger.getLogger(PaymentEntitlementMasterDaoImpl.class);

	public PaymentEntitlementMasterDaoImpl() {
		super(PaymentEntitlementMaster.class);
	}

	@Override
	public Response getPaymentEntitlementMaster(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			List<PaymentEntitlementMasterDto> listPaymentEntitlementMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_PAYMENT_ENTITLEMENT_LIST")
					.setResultTransformer(Transformers.aliasToBean(PaymentEntitlementMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, "", listPaymentEntitlementMasterDto, null);
		} catch (HibernateException he) {
			logger.error("getPaymentEntitlementMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPaymentEntitlementMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymentEntitlementListByOrgId(int orgId) throws ApplicationException {
		try {
			List<CommonDto> listCommonDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAYMENT_ENTITLEMENT_LIST_BY_ORG")
					//.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listCommonDto, null);
		} catch (HibernateException he) {
			logger.error("getPaymentEntitlementListByOrgId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPaymentEntitlementListByOrgId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymententitlementById(int orgId, int paymentEntId) throws ApplicationException {
		try {
			List<PaymentEntitlementMasterDto> paymentEntitlementMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAYMENT_ENTITLEMENT_BY_ID")
					//.setParameter("orgId", orgId)
					.setParameter("paymentEntId", paymentEntId)
					.setResultTransformer(Transformers.aliasToBean(PaymentEntitlementMasterDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, paymentEntitlementMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getPaymententitlementById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPaymententitlementById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymententitlementList(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			List<PaymentEntitlementMasterDto> paymentEntitlementMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAYMENT_ENTITLEMENT_LIST")
					//.setParameter("orgId", paymentEntitlementMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PaymentEntitlementMasterDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, paymentEntitlementMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getPaymententitlementList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPaymententitlementList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response savePaymententitlement(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			PaymentEntitlementMaster paymentEntitlementMaster=new PaymentEntitlementMaster();
			paymentEntitlementMaster.setPaymentEntitlementCode(paymentEntitlementMasterDto.getPaymentEntitlementCode());
			paymentEntitlementMaster.setPaymentEntitlementDesc(paymentEntitlementMasterDto.getPaymentEntitlementDesc());
			paymentEntitlementMaster.setCreatedBy(paymentEntitlementMasterDto.getCreatedBy());
			paymentEntitlementMaster.setUpdatedBy(paymentEntitlementMasterDto.getUpdatedBy());
			paymentEntitlementMaster.setCreatedDate(
					GlobalCommonDateUtils.getDate(paymentEntitlementMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			paymentEntitlementMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(paymentEntitlementMasterDto.getUpdateDate(), "dd-M-yyyy HH:mm:ss"));
			paymentEntitlementMaster.setStatus(paymentEntitlementMasterDto.getStatus());
			paymentEntitlementMaster.setOrganizationId(paymentEntitlementMasterDto.getOrganizationId());
			save(paymentEntitlementMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("savePaymententitlement failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("savePaymententitlement failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePaymententitlement(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			PaymentEntitlementMaster paymentEntMaster = findById(paymentEntitlementMasterDto.getPaymentEntitlementId());
			paymentEntMaster.setPaymentEntitlementCode(paymentEntitlementMasterDto.getPaymentEntitlementCode());
			paymentEntMaster.setPaymentEntitlementDesc(paymentEntitlementMasterDto.getPaymentEntitlementDesc());
			paymentEntMaster.setUpdatedBy(paymentEntitlementMasterDto.getUpdatedBy());
			paymentEntMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(paymentEntitlementMasterDto.getUpdateDate(), "dd-M-yyyy HH:mm:ss"));
			update(paymentEntMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updatePaymententitlement failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updatePaymententitlement failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePaymententitlementStatus(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			PaymentEntitlementMaster paymentEntMaster = findById(paymentEntitlementMasterDto.getPaymentEntitlementId());
			paymentEntMaster.setStatus(paymentEntitlementMasterDto.getStatus()==ACTIVE?ACTIVE:INACTIVE);
			paymentEntMaster.setUpdatedBy(paymentEntitlementMasterDto.getUpdatedBy());
			paymentEntMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(paymentEntitlementMasterDto.getUpdateDate(), "dd-M-yyyy HH:mm:ss"));
			update(paymentEntMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updatePaymententitlementStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updatePaymententitlementStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymententitlementCount(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(PaymentEntitlementMaster.class);
			c.add(Restrictions.eq("organizationId", paymentEntitlementMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);

		} catch (HibernateException he) {
			logger.error("getPaymententitlementCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPaymententitlementCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymententitlementByName(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			List<PaymentEntitlementMasterDto> paymentEntitlementMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAYMENT_ENTITLEMENT_BY_NAME")
					//.setParameter("orgId", paymentEntitlementMasterDto.getOrganizationId())
					.setString("name", paymentEntitlementMasterDto.getPaymentEntitlementDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(PaymentEntitlementMasterDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, paymentEntitlementMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getPaymententitlementByName failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPaymententitlementByName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymententitlementByNameNotById(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			List<PaymentEntitlementMasterDto> paymentEntitlementMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAYMENT_ENTITLEMENT_BY_NAME_NOT_BY_ID")
					//.setParameter("orgId", paymentEntitlementMasterDto.getOrganizationId())
					.setParameter("paymentEntId", paymentEntitlementMasterDto.getPaymentEntitlementId())
					.setString("name", paymentEntitlementMasterDto.getPaymentEntitlementDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(PaymentEntitlementMasterDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, paymentEntitlementMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getPaymententitlementByNameNotById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPaymententitlementByNameNotById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}

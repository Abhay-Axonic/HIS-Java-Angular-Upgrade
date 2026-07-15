package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.model.ServiceTarrifPaymentEntitlementManager;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TarrifPaymentEntitlementMpprDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
public class TarrifPaymentCategoryDaoImpl extends GenericDao<ServiceTarrifPaymentEntitlementManager, Integer> implements ITarrifPaymentCategoryDao, ICommonConstants, IError{

	final static Logger logger = Logger.getLogger(TarrifPaymentCategoryDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private DozerBeanMapper mapper;
	
	public TarrifPaymentCategoryDaoImpl() {
		super(ServiceTarrifPaymentEntitlementManager.class);
	}

	@Override
	public Response savePaymentEntitlementFactor(TarrifPaymentEntitlementMpprDto paymentEntitlementMpprDto)
			throws ApplicationException {
		try {
			paymentEntitlementMpprDto.setServiceTarrifPaymentEntitlementManagerId(null);
			ServiceTarrifPaymentEntitlementManager paymentEntitlementManager = mapper.map(paymentEntitlementMpprDto, ServiceTarrifPaymentEntitlementManager.class,
					"TarrifPaymentEntitlementMpprDto_to_ServiceTarrifPaymentEntitlementManager");
			paymentEntitlementManager.setStatus('A');
			paymentEntitlementManager = save(paymentEntitlementManager);

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, paymentEntitlementManager);
		} catch (HibernateException he) {
			logger.error("savePaymentEntitlementFactor failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("savePaymentEntitlementFactor failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response getPaymentCategoryMultiplicationFactorByTarrifId(Integer tarrifMasterId) throws ApplicationException {
		try{
			List<TarrifPaymentEntitlementMpprDto> listPaymentCategoryMpprDto = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_PAYMENT_TYPE_FACTOR_BY_TARRIF_ID")
				.setParameter("serviceTarrifId", tarrifMasterId)
				.setResultTransformer(Transformers.aliasToBean(TarrifPaymentEntitlementMpprDto.class)).list();
		
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listPaymentCategoryMpprDto, null);
		
		}catch(HibernateException he){
			throw he;
		}catch (Exception e) {
			throw e;
		}
	}

}

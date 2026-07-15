package com.param.global.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.LoyaltyCardConfigurationDto;
import com.param.global.model.LoyaltyCardConfiguration;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class LoyaltyCardConfigurationDaoImpl extends GenericDao<LoyaltyCardConfiguration, Integer> implements ILoyaltyCardConfigurationDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(LoyaltyCardConfigurationDaoImpl.class);


	public LoyaltyCardConfigurationDaoImpl() {
		super(LoyaltyCardConfiguration.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveLoyaltyCardConfiguration(LoyaltyCardConfigurationDto loyaltyCardConfigurationDto) throws ApplicationException{
		try {
			LoyaltyCardConfiguration loyaltyCardConfiguration=new LoyaltyCardConfiguration();
			loyaltyCardConfiguration.setMinStartAmount(loyaltyCardConfigurationDto.getMinStartAmount());
			loyaltyCardConfiguration.setMinRechargeAmount(loyaltyCardConfigurationDto.getMinRechargeAmount());
			loyaltyCardConfiguration.setThresholdAmount(loyaltyCardConfigurationDto.getThresholdAmount());
			loyaltyCardConfiguration.setIsDeleted('N');
			loyaltyCardConfiguration.setOrganisationId(loyaltyCardConfigurationDto.getOrganisationId());
			loyaltyCardConfiguration.setUnitId(loyaltyCardConfigurationDto.getUnitId());
			loyaltyCardConfiguration.setStatus('A');
			loyaltyCardConfiguration.setCreatedBy(loyaltyCardConfigurationDto.getCreatedBy());
			loyaltyCardConfiguration.setCreatedDate(GlobalCommonDateUtils.getDate(loyaltyCardConfigurationDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			loyaltyCardConfiguration.setUpdatedBy(loyaltyCardConfigurationDto.getUpdatedBy());
			loyaltyCardConfiguration.setUpdatedDate(GlobalCommonDateUtils.getDate(loyaltyCardConfigurationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(loyaltyCardConfiguration);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveLoyaltyCardConfiguration failed", he);
		} catch (Exception e) {
		logger.error("saveLoyaltyCardConfiguration failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateLoyaltyCardConfigurationStatus(LoyaltyCardConfigurationDto loyaltyCardConfigurationDto) throws ApplicationException{
		try {
				LoyaltyCardConfiguration loyaltyCardConfiguration=findById(loyaltyCardConfigurationDto.getCardConfigurationId());
				
				loyaltyCardConfiguration.setStatus(loyaltyCardConfigurationDto.getStatus());
				loyaltyCardConfiguration.setIsDeleted(loyaltyCardConfigurationDto.getIsDeleted());
				loyaltyCardConfiguration.setUpdatedBy(loyaltyCardConfigurationDto.getUpdatedBy());
				loyaltyCardConfiguration.setUpdatedDate(GlobalCommonDateUtils.getDate(loyaltyCardConfigurationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				 
				update(loyaltyCardConfiguration);
		
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateLoyaltyCardConfigurationStatus failed", he);
		} catch (Exception e) {
		logger.error("updateLoyaltyCardConfigurationStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
	}
	
	@Override
	public Response getLoyaltyCardConfigurationById(Integer cardConfigurationId, Integer organisationId, Integer unitId) throws ApplicationException{
		try {
			List<LoyaltyCardConfigurationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LOYALTY_CARD_CONFIGURATION_BY_ID")
					.setParameter("cardConfigurationId", cardConfigurationId)
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(LoyaltyCardConfigurationDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getLoyaltyCardConfigurationById failed", he);
		} catch (Exception e) {
		logger.error("getLoyaltyCardConfigurationById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getLoyaltyCardConfigurationList(LoyaltyCardConfigurationDto loyaltyCardConfigurationDto) throws ApplicationException{
		try {
			List<LoyaltyCardConfigurationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LOYALTY_CARD_CONFIGURATION_LIST")
					.setParameter("organisationId", loyaltyCardConfigurationDto.getOrganisationId())
					.setParameter("unitId", loyaltyCardConfigurationDto.getUnitId())
					.setFirstResult(loyaltyCardConfigurationDto.getOffset() != null ? loyaltyCardConfigurationDto.getOffset()  : 0)
					.setMaxResults(loyaltyCardConfigurationDto.getNoOfRecordsPerPage() != null ? loyaltyCardConfigurationDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(LoyaltyCardConfigurationDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getLoyaltyCardConfigurationList failed", he);
		} catch (Exception e) {
		logger.error("getLoyaltyCardConfigurationList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getLoyaltyCardConfigurationCount(LoyaltyCardConfigurationDto loyaltyCardConfigurationDto) throws ApplicationException{
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(LoyaltyCardConfiguration.class);
			crit.add(Restrictions.eq("organisationId", loyaltyCardConfigurationDto.getOrganisationId()));
			crit.add(Restrictions.eq("unitId", loyaltyCardConfigurationDto.getUnitId()));
			crit.add(Restrictions.eq("isDeleted", 'N'));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getLoyaltyCardConfigurationCount failed", he);
		} catch (Exception e) {
		logger.error("getLoyaltyCardConfigurationCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkLoyaltyCardConfigurationAvailable(LoyaltyCardConfigurationDto loyaltyCardConfigurationDto)
			throws ApplicationException {
		try {
			List<LoyaltyCardConfigurationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_LOYALTY_CARD_CONFIGURATION_AVAILABLE")
					.setParameter("organisationId", loyaltyCardConfigurationDto.getOrganisationId())
					.setParameter("unitId", loyaltyCardConfigurationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(LoyaltyCardConfigurationDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("checkLoyaltyCardConfigurationAvailable failed", he);
		} catch (Exception e) {
		logger.error("checkLoyaltyCardConfigurationAvailable failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



}


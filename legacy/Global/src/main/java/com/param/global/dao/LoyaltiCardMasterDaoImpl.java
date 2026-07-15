package com.param.global.dao;

import java.math.BigInteger;
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

import com.param.entity.org.model.LoyaltiCardMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.LoyaltiCardMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class LoyaltiCardMasterDaoImpl extends GenericDao<LoyaltiCardMaster, Integer> implements ILoyaltiCardMasterDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(LoyaltiCardMasterDaoImpl.class);


	public LoyaltiCardMasterDaoImpl() {
		super(LoyaltiCardMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveLoyaltiCardMaster(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException{
		try {
			LoyaltiCardMaster loyaltiCardMaster=new LoyaltiCardMaster();
			loyaltiCardMaster.setCardNo(loyaltiCardMasterDto.getCardNo());
			loyaltiCardMaster.setIsDeleted('N');
			loyaltiCardMaster.setIsActive('N');
			loyaltiCardMaster.setOrganisationId(loyaltiCardMasterDto.getOrganisationId());
			loyaltiCardMaster.setUnitId(loyaltiCardMasterDto.getUnitId());
			loyaltiCardMaster.setStatus('A');
			loyaltiCardMaster.setIsUserDefined(loyaltiCardMasterDto.getIsUserDefined());
			loyaltiCardMaster.setCreatedBy(loyaltiCardMasterDto.getCreatedBy());
			loyaltiCardMaster.setCreatedDate(GlobalCommonDateUtils.getDate(loyaltiCardMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			loyaltiCardMaster.setUpdatedBy(loyaltiCardMasterDto.getUpdatedBy());
			loyaltiCardMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(loyaltiCardMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			loyaltiCardMaster = save(loyaltiCardMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, loyaltiCardMaster);
		
		} catch (HibernateException he) {
		logger.error("saveLoyaltiCardMaster failed", he);
		} catch (Exception e) {
		logger.error("saveLoyaltiCardMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateLoyaltiCardMasterStatus(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException{
		try {
				LoyaltiCardMaster loyaltiCardMaster=findById(loyaltiCardMasterDto.getLoyaltyCardId());
				
				loyaltiCardMaster.setStatus(loyaltiCardMasterDto.getStatus());
				loyaltiCardMaster.setIsDeleted(loyaltiCardMasterDto.getIsDeleted());
				loyaltiCardMaster.setUpdatedBy(loyaltiCardMasterDto.getUpdatedBy());
				loyaltiCardMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(loyaltiCardMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				 
				update(loyaltiCardMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, "Loyalty Card Remove Successfully", null, null);
		
		} catch (HibernateException he) {
		logger.error("updateLoyaltiCardMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateLoyaltiCardMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
	}

	@Override
	public Response getLoyaltiCardMasterList(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException{
		try {
			List<LoyaltiCardMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LOYALTI_CARD_MASTER_LIST")
					.setParameter("organisationId", loyaltiCardMasterDto.getOrganisationId())
					.setParameter("unitId", loyaltiCardMasterDto.getUnitId())
					.setFirstResult(loyaltiCardMasterDto.getOffset() != null ? loyaltiCardMasterDto.getOffset()  : 0)
					.setMaxResults(loyaltiCardMasterDto.getNoOfRecordsPerPage() != null ? loyaltiCardMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(LoyaltiCardMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getLoyaltiCardMasterList failed", he);
		} catch (Exception e) {
		logger.error("getLoyaltiCardMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getLoyaltiCardMasterCount(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException{
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(LoyaltiCardMaster.class);
			crit.add(Restrictions.eq("organisationId", loyaltiCardMasterDto.getOrganisationId()));
			crit.add(Restrictions.eq("unitId", loyaltiCardMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getLoyaltiCardMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getLoyaltiCardMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkLoyaltyCardNumberExistOrNot(int orgId, String cardNo) throws ApplicationException {
		try {
			BigInteger count=(BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_LOYALATI_CARD_NO")
					.setParameter("orgId", orgId)
					.setParameter("cardNo", cardNo)
					.uniqueResult();
		return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
		
		} catch (HibernateException he) {
		logger.error("checkLoyaltyCardNumberExistOrNot failed", he);
		} catch (Exception e) {
		logger.error("checkLoyaltyCardNumberExistOrNot failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}




}



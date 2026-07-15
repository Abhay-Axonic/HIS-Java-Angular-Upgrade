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
import com.param.global.dto.TubeFeedingProductsDto;
import com.param.global.model.TubeFeedingProductsMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class TubeFeedingProductsDaoImpl extends GenericDao<TubeFeedingProductsMaster, Integer> implements ITubeFeedingProductsDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(TubeFeedingProductsDaoImpl.class);

	
	@Autowired
	SessionFactory sessionFactory;
	
	public TubeFeedingProductsDaoImpl() {
		super(TubeFeedingProductsMaster.class);
	}

	@Override
	public Response saveTubeFeedingProducts(TubeFeedingProductsDto tubeFeedingProductsDto) throws ApplicationException {
		try {
			TubeFeedingProductsMaster tubeFeedingProductsMaster=new TubeFeedingProductsMaster();
			tubeFeedingProductsMaster.setProductsCode(tubeFeedingProductsDto.getProductsCode());
			tubeFeedingProductsMaster.setProductsDesc(tubeFeedingProductsDto.getProductsDesc());
			tubeFeedingProductsMaster.setOrganisationId(tubeFeedingProductsDto.getOrganisationId());
			tubeFeedingProductsMaster.setUnitId(tubeFeedingProductsDto.getUnitId());
			tubeFeedingProductsMaster.setStatus(tubeFeedingProductsDto.getStatus());
			tubeFeedingProductsMaster.setCreatedBy(tubeFeedingProductsDto.getCreatedBy());
			tubeFeedingProductsMaster.setCreatedDate(GlobalCommonDateUtils.getDate(tubeFeedingProductsDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			tubeFeedingProductsMaster.setUpdatedBy(tubeFeedingProductsDto.getUpdatedBy());
			tubeFeedingProductsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(tubeFeedingProductsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(tubeFeedingProductsMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveTubeFeedingProducts failed", he);
		} catch (Exception e) {
		logger.error("saveTubeFeedingProducts failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateTubeFeedingProducts(TubeFeedingProductsDto tubeFeedingProductsDto)
			throws ApplicationException {
		try {
			TubeFeedingProductsMaster tubeFeedingProductsMaster=findById(tubeFeedingProductsDto.getTubeFeedingProductsId());
			tubeFeedingProductsMaster.setProductsCode(tubeFeedingProductsDto.getProductsCode());
			tubeFeedingProductsMaster.setProductsDesc(tubeFeedingProductsDto.getProductsDesc());
			tubeFeedingProductsMaster.setOrganisationId(tubeFeedingProductsDto.getOrganisationId());
			tubeFeedingProductsMaster.setUnitId(tubeFeedingProductsDto.getUnitId());
			tubeFeedingProductsMaster.setUpdatedBy(tubeFeedingProductsDto.getUpdatedBy());
			tubeFeedingProductsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(tubeFeedingProductsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
		update(tubeFeedingProductsMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateTubeFeedingProducts failed", he);
	} catch (Exception e) {
	logger.error("updateTubeFeedingProducts failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response updateTubeFeedingProductsStatus(TubeFeedingProductsDto tubeFeedingProductsDto)
			throws ApplicationException {
		try {
			TubeFeedingProductsMaster tubeFeedingProductsMaster=findById(tubeFeedingProductsDto.getTubeFeedingProductsId());
			
			tubeFeedingProductsMaster.setStatus(tubeFeedingProductsDto.getStatus());
			tubeFeedingProductsMaster.setUpdatedBy(tubeFeedingProductsDto.getUpdatedBy());
			tubeFeedingProductsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(tubeFeedingProductsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(tubeFeedingProductsMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateTubeFeedingProductsStatus failed", he);
	} catch (Exception e) {
	logger.error("updateTubeFeedingProductsStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
}

	@Override
	public Response getTubeFeedingProductsList(TubeFeedingProductsDto tubeFeedingProductsDto)
			throws ApplicationException {
		try{
				List<TubeFeedingProductsDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_TUBE_FEEDING_PRODUCTS_LIST")
				.setParameter("organisationId", tubeFeedingProductsDto.getOrganisationId())//change by minesh from getOrg to getUnit
				.setFirstResult(tubeFeedingProductsDto.getOffset() != null ? tubeFeedingProductsDto.getOffset() : 0)
				.setMaxResults(tubeFeedingProductsDto.getNoOfRecordsPerPage() != null ? tubeFeedingProductsDto.getNoOfRecordsPerPage() : 10)
				.setResultTransformer(Transformers.aliasToBean(TubeFeedingProductsDto.class)).list();

	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	
	} catch (HibernateException he) {
	logger.error("getTubeFeedingProductsList failed", he);
	} catch (Exception e) {
	logger.error("getTubeFeedingProductsList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getTubeFeedingProductsById(Integer tubeFeedingProductsId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try {
			List<TubeFeedingProductsDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TUBE_FEEDING_PRODUCTS_BY_ID")
					.setParameter("tubeFeedingProductsId", tubeFeedingProductsId)
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(TubeFeedingProductsDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getTubeFeedingProductsById failed", he);
		} catch (Exception e) {
		logger.error("getTubeFeedingProductsById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response getActiveTubeFeedingProducts(TubeFeedingProductsDto tubeFeedingProductsDto)
			throws ApplicationException {
		try {
			List<TubeFeedingProductsDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_TUBE_FEEDING_PRODUCTS")
					.setParameter("organisationId", tubeFeedingProductsDto.getOrganisationId())
					.setResultTransformer(Transformers.aliasToBean(TubeFeedingProductsDto.class)).list();
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getActiveTubeFeedingProducts failed", he);
		} catch (Exception e) {
		logger.error("getActiveTubeFeedingProducts failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getTubeFeedingProductsCount(TubeFeedingProductsDto tubeFeedingProductsDto)
			throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(TubeFeedingProductsMaster.class);
			crit.add(Restrictions.eq("organisationId", tubeFeedingProductsDto.getOrganisationId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getTubeFeedingProductsCount failed", he);
		} catch (Exception e) {
		logger.error("getTubeFeedingProductsCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response checkTubeFeedingProductsAvailable(TubeFeedingProductsDto tubeFeedingProductsDto)
			throws ApplicationException {
		try{
			List<TubeFeedingProductsDto> list=sessionFactory.getCurrentSession()
			.getNamedQuery("GET_AVAILABLE_TUBE_FEEDING_PRODUCTS")
			.setParameter("organisationId", tubeFeedingProductsDto.getOrganisationId())//added by minesh
			.setParameter( "productsDesc", tubeFeedingProductsDto.getProductsDesc().toLowerCase())
			.setResultTransformer(Transformers.aliasToBean(TubeFeedingProductsDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("checkTubeFeedingProductsAvailable failed", he);
		} catch (Exception e) {
		logger.error("checkTubeFeedingProductsAvailable failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getTubeFeedingProductsByNameNotId(TubeFeedingProductsDto tubeFeedingProductsDto)
			throws ApplicationException {
		try{
			List<TubeFeedingProductsDto> list=sessionFactory.getCurrentSession()
			.getNamedQuery("GET_TUBE_FEEDING_PRODUCTS_BY_NAME_NOT_ID")
			.setParameter("organisationId", tubeFeedingProductsDto.getOrganisationId())//added by minesh
			.setParameter( "productsDesc", tubeFeedingProductsDto.getProductsDesc().toLowerCase())
			.setParameter( "tubeFeedingProductsId", tubeFeedingProductsDto.getTubeFeedingProductsId())
			.setResultTransformer(Transformers.aliasToBean(TubeFeedingProductsDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
			} catch (HibernateException he) {
			logger.error("getTubeFeedingProductsByNameNotId failed", he);
			} catch (Exception e) {
			logger.error("getTubeFeedingProductsByNameNotId failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
			
		}

package com.param.global.billing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.ServiceBasePriceMaster;
import com.param.global.billing.dto.ServiceBasePriceMasterDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Repository
public class ServiceBasePriceMasterDaoImpl extends GenericDao<ServiceBasePriceMaster, Integer>
		implements IServiceBasePriceMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(ServiceBasePriceMasterDaoImpl.class);

	public ServiceBasePriceMasterDaoImpl() {
		super(ServiceBasePriceMaster.class);
	}

	@Override
	public Response getServiceBasePriceList(ServiceBasePriceMasterDto serviceBasePriceMasterDto)
			throws ApplicationException {
		try {
			List<ServiceBasePriceMasterDto> serviceBasePriceMasterDtolist = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_BASE_PRICE_LIST")
					.setParameter("orgId", serviceBasePriceMasterDto.getOrgnisationId())
					.setParameter("unitId", serviceBasePriceMasterDto.getUnitId())
					.setFirstResult(serviceBasePriceMasterDto.getOffset() != null ? serviceBasePriceMasterDto.getOffset() : 0)
					.setMaxResults(serviceBasePriceMasterDto.getNoOfRecordsPerPage() != null ? serviceBasePriceMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ServiceBasePriceMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, serviceBasePriceMasterDtolist, null);

		} catch (HibernateException he) {
			logger.error("getServiceBasePriceList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getServiceBasePriceList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getServiceBasePriceListById(Integer serviceBasePriceId) throws ApplicationException {
		try {
			List<ServiceBasePriceMasterDto> serviceBasePriceMasterDtolist = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_BASE_PRICE_LIST_BY_ID")
					.setParameter("serviceBasePriceId", serviceBasePriceId)
					.setResultTransformer(Transformers.aliasToBean(ServiceBasePriceMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, serviceBasePriceMasterDtolist, null);
		} catch (HibernateException he) {
			logger.error("getServiceBasePriceListById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getServiceBasePriceListById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateServiceBasePriceMaster(ServiceBasePriceMasterDto serviceBasePriceMasterDto)
			throws ApplicationException {
		try {
			ServiceBasePriceMaster serviceBasePriceMaster = findById(serviceBasePriceMasterDto.getServiceBasePriceId());
				serviceBasePriceMaster.setUpdatedBy(serviceBasePriceMasterDto.getUpdatedBy());
				serviceBasePriceMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(serviceBasePriceMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				serviceBasePriceMaster.setBasePrice(serviceBasePriceMasterDto.getBasePrice());
				serviceBasePriceMaster.setCostOfService(serviceBasePriceMasterDto.getCostOfService());
			update(serviceBasePriceMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateServiceBasePriceMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateServiceBasePriceMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateServiceBasePriceMasterStatus(ServiceBasePriceMasterDto serviceBasePriceMasterDto)
			throws ApplicationException {
		try {
			ServiceBasePriceMaster serviceBasePriceMaster = findById(serviceBasePriceMasterDto.getServiceBasePriceId());
			serviceBasePriceMaster.setUpdatedBy(serviceBasePriceMasterDto.getUpdatedBy());
			serviceBasePriceMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(serviceBasePriceMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			serviceBasePriceMaster.setStatus(serviceBasePriceMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			update(serviceBasePriceMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateServiceBasePriceMasterStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateServiceBasePriceMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response countServiceBasePriceMaster(ServiceBasePriceMasterDto serviceBasePriceMasterDto)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(ServiceBasePriceMaster.class); 
			c.add(Restrictions.eq("orgnisationId", serviceBasePriceMasterDto.getOrgnisationId()));
			c.add(Restrictions.eq("status",'A'));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			logger.error("countServiceBasePriceMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("countServiceBasePriceMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getServiceBasePriceListSearch(ServiceBasePriceMasterDto serviceBasePriceMasterDto)
			throws ApplicationException {
		try {
			List<ServiceBasePriceMasterDto> serviceBasePriceMasterDtolist = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_BASE_PRICE_LIST_SEARCH")
					.setParameter("orgId", serviceBasePriceMasterDto.getOrgnisationId())
					.setParameter("unitId", serviceBasePriceMasterDto.getUnitId())
					.setParameter("serviceSearch", "%"+serviceBasePriceMasterDto.getServiceName().trim().toLowerCase()+"%")
					.setFirstResult(serviceBasePriceMasterDto.getOffset() != null ? serviceBasePriceMasterDto.getOffset() : 0)
					.setMaxResults(serviceBasePriceMasterDto.getNoOfRecordsPerPage() != null ? serviceBasePriceMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ServiceBasePriceMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, serviceBasePriceMasterDtolist, null);

		} catch (HibernateException he) {
			logger.error("getServiceBasePriceListSearch failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getServiceBasePriceListSearch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}

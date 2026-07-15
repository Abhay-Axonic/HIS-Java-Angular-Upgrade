package com.param.global.unit.dao;

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
import com.param.global.dto.CommonDto;
import com.param.global.model.CounterMaster;
import com.param.global.unit.dto.CounterMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class CounterMasterDaoImpl extends GenericDao<CounterMaster, Integer> implements ICounterMasterDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(CounterMasterDaoImpl.class);

	
	@Autowired
	SessionFactory sessionFactory;
	
	public CounterMasterDaoImpl() {
		super(CounterMaster.class);
	}

	@Override
	public Response saveCounterMaster(CounterMasterDto counterMasterDto) throws ApplicationException {
		try {
			CounterMaster counterMaster=new CounterMaster();
			counterMaster.setCounterCode(counterMasterDto.getCounterCode());
			counterMaster.setCounterDesc(counterMasterDto.getCounterDesc());
			counterMaster.setOrganizationId(counterMasterDto.getOrganizationId());
			counterMaster.setUnitId(counterMasterDto.getUnitId());
			counterMaster.setStatus(counterMasterDto.getStatus());
			counterMaster.setCreatedBy(counterMasterDto.getCreatedBy());
			counterMaster.setCreatedDate(GlobalCommonDateUtils.getDate(counterMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			counterMaster.setUpdatedBy(counterMasterDto.getUpdatedBy());
			counterMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(counterMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(counterMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveCounterMaster failed", he);
		} catch (Exception e) {
		logger.error("saveCounterMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateCounterMaster(CounterMasterDto counterMasterDto) throws ApplicationException {
		try {
			CounterMaster counterMaster=findById(counterMasterDto.getCounterId());
			counterMaster.setCounterCode(counterMasterDto.getCounterCode());
			counterMaster.setCounterDesc(counterMasterDto.getCounterDesc());
			counterMaster.setOrganizationId(counterMasterDto.getOrganizationId());
			counterMaster.setUnitId(counterMasterDto.getUnitId());
			counterMaster.setUpdatedBy(counterMasterDto.getUpdatedBy());
			counterMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(counterMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
		update(counterMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateCounterMaster failed", he);
	} catch (Exception e) {
	logger.error("updateCounterMaster failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response updateCounterMasterStatus(CounterMasterDto counterMasterDto) throws ApplicationException {
		try {
			CounterMaster counterMaster=findById(counterMasterDto.getCounterId());
			
			counterMaster.setStatus(counterMasterDto.getStatus());
			counterMaster.setUpdatedBy(counterMasterDto.getUpdatedBy());
			counterMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(counterMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(counterMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateCounterMasterStatus failed", he);
	} catch (Exception e) {
	logger.error("updateCounterMasterStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
}
	@Override
	public Response getCounterMasterById(Integer counterId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<CounterMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COUNTER_MASTER_BY_ID")
					.setParameter("counterId", counterId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(CounterMasterDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getCounterMasterById failed", he);
		} catch (Exception e) {
		logger.error("getCounterMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response getCounterMasterList(CounterMasterDto counterMasterDto) throws ApplicationException {
		try {
			List<CounterMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COUNTER_MASTER_LIST")
					.setParameter("organizationId", counterMasterDto.getOrganizationId())
					.setParameter("unitId", counterMasterDto.getUnitId())
					.setFirstResult(counterMasterDto.getOffset() != null ? counterMasterDto.getOffset() : 0)
					.setMaxResults(counterMasterDto.getNoOfRecordsPerPage() != null ? counterMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(CounterMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getCounterMasterList failed", he);
		} catch (Exception e) {
		logger.error("getCounterMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCounterMasterCount(CounterMasterDto counterMasterDto) throws ApplicationException {
try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(CounterMaster.class);
			crit.add(Restrictions.eq("organizationId", counterMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", counterMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getCounterMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getCounterMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveCounterMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<CounterMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_COUNTER_MASTER")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(CounterMasterDto.class)).list();	
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getActiveCounterMaster failed", he);
		} catch (Exception e) {
		logger.error("getActiveCounterMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getCounterMasterByName(CounterMasterDto counterMasterDto) throws ApplicationException {
		try{
			List<CounterMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_COUNTER_MASTER_BY_NAME")
				.setParameter("counterDesc", counterMasterDto.getCounterDesc().toLowerCase())
				.setParameter("organizationId", counterMasterDto.getOrganizationId())
				.setParameter("unitId", counterMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(CounterMasterDto.class)).list();
	
	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	
	} catch (HibernateException he) {
	logger.error("getCounterMasterByName failed", he);
	} catch (Exception e) {
	logger.error("getCounterMasterByName failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}


	@Override
	public Response getCounterMasterByNameNotId(CounterMasterDto counterMasterDto) throws ApplicationException {
		try{
			List<CounterMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_COUNTER_MASTER_BY_NAME_NOT_ID")
				.setParameter("counterId", counterMasterDto.getCounterId())
				.setParameter("counterDesc", counterMasterDto.getCounterDesc().toLowerCase())
				.setParameter("organizationId", counterMasterDto.getOrganizationId())
				.setParameter("unitId", counterMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(CounterMasterDto.class)).list();
		 
	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	
	} catch (HibernateException he) {
	logger.error("getCounterMasterByNameNotId failed", he);
	} catch (Exception e) {
	logger.error("getCounterMasterByNameNotId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getCountersList(Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			List<CommonDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_COUNTERS")
				.setParameter("orgId", organizationId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();
		 
	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	
	} catch (HibernateException he) {
	logger.error("getCountersList failed", he);
	} catch (Exception e) {
	logger.error("getCountersList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}

package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
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
import com.param.global.common.ServiceException;
import com.param.global.dto.StateMasterDto;
import com.param.global.model.StateMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class StateMasterDaoImpl extends GenericDao<StateMaster, Integer> implements IStateMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(StateMasterDaoImpl.class);

	public StateMasterDaoImpl() {
		super(StateMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Response getStateByName(StateMasterDto stateMasterDto) throws ApplicationException {
		try {
			List<StateMasterDto> stateMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STATE_LIST_BY_NAME")
					.setParameter("stateName", stateMasterDto.getStateName().toLowerCase())
					.setParameter("orgId", stateMasterDto.getOrganizationId())
					.setParameter("countryId", stateMasterDto.getCountryId())
					.setResultTransformer(Transformers.aliasToBean(StateMasterDto.class)).list();
			return new Response(SUCCESS, null, null, stateMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getStateByName failed", he);
		} catch (Exception e) {
			logger.error("getStateByName failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addStateMaster(StateMasterDto stateMasterDto) throws ApplicationException {
		try {
			StateMaster stateMaster = mapper.map(stateMasterDto, StateMaster.class, "stateMasterDto_to_stateMaster");
			stateMaster.setStatus('A');
			stateMaster = save(stateMaster);
			return new Response(SUCCESS, null, null, null, stateMaster);
		} catch (HibernateException he) {
			logger.error("addStateMaster failed", he);
		} catch (Exception e) {
			logger.error("addStateMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getStateMasterList(StateMasterDto stateMasterDto) throws ApplicationException {
		try {
			List<StateMasterDto> stateMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STATE_LIST")
					.setParameter("orgId", stateMasterDto.getOrganizationId())
					.setFirstResult(stateMasterDto.getOffset() != null ? stateMasterDto.getOffset() : 0)
					.setMaxResults(stateMasterDto.getNoOfRecordsPerPage() != null ? stateMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(StateMasterDto.class)).list();
			return new Response(SUCCESS, null, null, stateMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getStateMasterList failed", he);
		} catch (Exception e) {
			logger.error("getStateMasterList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getStateByNameNotId(StateMasterDto stateMasterDto) throws ApplicationException {
		try {
			List<StateMasterDto> stateMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STATE_LIST_BY_NAME_NOT_ID").setParameter("stateId", stateMasterDto.getStateId())
					.setParameter("stateName", stateMasterDto.getStateName().toLowerCase())
					.setParameter("orgId", stateMasterDto.getOrganizationId())
					.setParameter("countryId", stateMasterDto.getCountryId())
					//.setParameter("orgId", stateMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(StateMasterDto.class)).list();
			return new Response(SUCCESS, null, null, stateMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getStateByNameNotId failed", he);
		} catch (Exception e) {
			logger.error("getStateByNameNotId failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getStateById(StateMasterDto stateMasterDto) throws ApplicationException {
		try {
			List<StateMasterDto> stateMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STATE_LIST_BY_ID")
					.setParameter("stateId", stateMasterDto.getStateId())
					.setParameter("orgId", stateMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(StateMasterDto.class)).list();
			return new Response(SUCCESS, null, null, stateMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getStateById failed", he);
		} catch (Exception e) {
			logger.error("getStateById failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStateStatus(StateMasterDto stateMasterDto) throws ApplicationException {
		try {

			StateMaster stateMaster = findById(stateMasterDto.getStateId());
			stateMaster.setStatus(stateMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			stateMaster.setUpdatedBy(stateMasterDto.getUpdatedBy());
			stateMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(stateMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(stateMaster);
			return new Response<StateMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			logger.error("updateStateStatus failed", he);
			return new Response<StateMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateStateStatus failed", e);
			return new Response<StateMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response getActiveStateList(Integer organizationId) throws ApplicationException {
		try {
			List<StateMasterDto> stateMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_STATE_LIST")
					.setParameter("orgId", organizationId)
					.setResultTransformer(Transformers.aliasToBean(StateMasterDto.class)).list();
			return new Response(SUCCESS, null, null, stateMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getActiveStateList failed", he);
		} catch (Exception e) {
			logger.error("getActiveStateList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStateMaster(StateMasterDto stateMasterDto) throws ApplicationException {
		try {
			StateMaster stateMaster = findById(stateMasterDto.getStateId());
			stateMaster.setStateName(stateMasterDto.getStateName());
			stateMaster.setStateCode(stateMasterDto.getStateCode());
			stateMaster.setCountryId(stateMasterDto.getCountryId());
			stateMaster.setUpdatedBy(stateMasterDto.getUpdatedBy());
			stateMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(stateMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(stateMaster);
			return new Response<StateMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, stateMaster);

		} catch (HibernateException he) {
			logger.error("updateStateMaster failed", he);
			return new Response<StateMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateStateMaster failed", e);
			return new Response<StateMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}


	public Response getCount(StateMasterDto stateMasterDto)
	{
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(StateMaster.class); 
//			c.add(Restrictions.eq("organizationId", stateMasterDto.getOrganizationId()));
			c.add(Restrictions.disjunction()
					.add(Restrictions.eq("organizationId",stateMasterDto.getOrganizationId()))
					.add(Restrictions.isNull("organizationId")));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			logger.error("getCount failed", e);
			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getStateByCountryId(StateMasterDto stateMasterDto)throws ApplicationException {
		try {
			List<StateMasterDto> stateMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STATE_BY_COUNTRY_ID").setParameter("countryId", stateMasterDto.getCountryId())
					.setResultTransformer(Transformers.aliasToBean(StateMasterDto.class)).list();
			return new Response(SUCCESS, null, null, stateMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getStateByCountryId failed", he);
		} catch (Exception e) {
			logger.error("getStateByCountryId failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

}

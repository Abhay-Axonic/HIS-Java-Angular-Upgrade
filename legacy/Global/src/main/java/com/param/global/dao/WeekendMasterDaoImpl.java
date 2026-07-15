package com.param.global.dao;

import java.util.List;


import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import com.param.global.dto.WeekendMasterDto;
import com.param.global.model.WeekendMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WeekendMasterDaoImpl extends GenericDao<WeekendMaster, Integer>implements IWeekendMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(WeekendMasterDaoImpl.class);

	@Autowired
	DozerBeanMapper mapper;

	public WeekendMasterDaoImpl() {
		super(WeekendMaster.class);
	}

	@Override
	public Response saveWeekendMaster(WeekendMasterDto weekendMasterDto) throws ApplicationException {
		try {
			WeekendMaster weekendMaster = new WeekendMaster();
				weekendMaster.setOrganizationId(weekendMasterDto.getOrganizationId());
				weekendMaster.setUnitId(weekendMasterDto.getUnitId());
				weekendMaster.setWeekendDate(GlobalCommonDateUtils.getDate(weekendMasterDto.getWeekendDate(),"dd-M-yyyy HH:mm:ss"));
				weekendMaster.setDayId(weekendMasterDto.getDayId());
				weekendMaster.setIsHalfDay(weekendMasterDto.getIsHalfDay());
				if(weekendMasterDto.getIsHalfDay()=='Y'){
					
					weekendMaster.setFromTime(weekendMasterDto.getFromTime());
					weekendMaster.setToTime(weekendMasterDto.getToTime());
					
				}
				weekendMaster.setCreatedBy(weekendMasterDto.getCreatedBy());
				weekendMaster.setUpdatedBy(weekendMasterDto.getUpdatedBy());
				weekendMaster.setCreatedDate(GlobalCommonDateUtils.getDate(weekendMasterDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
				weekendMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(weekendMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
				weekendMaster.setStatus('A');
			save(weekendMaster);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
			logger.error("saveWeekendMaster failed: " + he.getMessage(), he);
		} catch (Exception e) {
			logger.error("saveWeekendMaster failed: " + e.getMessage(), e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getWeekendMasterList(WeekendMasterDto weekendMasterDto) throws ApplicationException {
		try {
			List<WeekendMasterDto> getWeekendMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_WEEKENDS_LIST")
					.setParameter("organizationId", weekendMasterDto.getOrganizationId())
					.setParameter("unitId",weekendMasterDto.getUnitId() )
					.setResultTransformer(Transformers.aliasToBean(WeekendMasterDto.class)).list();
			return new Response (SUCCESS, SUCCESS_CODE, null, getWeekendMasterDtosList, null);
		}catch (HibernateException he) {
			logger.error("getWeekendMasterList failed: " + he.getMessage(), he);
		} catch (Exception e) {
			logger.error("getWeekendMasterList failed: " + e.getMessage(), e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateWeekendMaster(WeekendMasterDto weekendMasterDto) throws ApplicationException {
	try{
		WeekendMaster weekendMaster = findById(weekendMasterDto.getWeekendId());
		weekendMaster.setOrganizationId(weekendMasterDto.getOrganizationId());
		weekendMaster.setUnitId(weekendMasterDto.getUnitId());
		weekendMaster.setWeekendDate(GlobalCommonDateUtils.getDate(weekendMasterDto.getWeekendDate(),"dd-M-yyyy HH:mm:ss"));
		weekendMaster.setDayId(weekendMasterDto.getDayId());
		weekendMaster.setIsHalfDay(weekendMasterDto.getIsHalfDay());
		weekendMaster.setFromTime(weekendMasterDto.getFromTime());
		weekendMaster.setToTime(weekendMasterDto.getToTime());
		weekendMaster.setUpdatedBy(weekendMasterDto.getUpdatedBy());
		weekendMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(weekendMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
	
		
		update(weekendMaster);
		
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

	} catch (HibernateException he) {
		logger.error("updateWeekendMaster failed: " + he.getMessage(), he);
	} catch (Exception e) {
		logger.error("updateWeekendMaster failed: " + e.getMessage(), e);
	}
	return new Response(ERROR, null, null, null, null);
}

	@Override
	public Response updateWeekendMasterStatus(WeekendMasterDto weekendMasterDto) throws ApplicationException {
		try{
			WeekendMaster weekendMaster = findById(weekendMasterDto.getWeekendId());
			weekendMaster.setUpdatedBy(weekendMasterDto.getUpdatedBy());
			weekendMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(weekendMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
			weekendMaster.setStatus(weekendMasterDto.getStatus());
			
			update(weekendMaster);
			
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
			logger.error("updateWeekendMasterStatus failed: " + he.getMessage(), he);
		} catch (Exception e) {
			logger.error("updateWeekendMasterStatus failed: " + e.getMessage(), e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getListOfWeekendMaster(WeekendMasterDto weekendMasterDto) throws ApplicationException {
		try{
			List<WeekendMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_WEEKEND_MASTER")
					.setParameter("organizationId", weekendMasterDto.getOrganizationId())
					.setParameter("unitId", weekendMasterDto.getUnitId())
					.setFirstResult(weekendMasterDto.getOffset() != null ? weekendMasterDto.getOffset() : 0)
					.setMaxResults(weekendMasterDto.getNoOfRecordsPerPage() != null ? weekendMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(WeekendMasterDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getListOfWeekendMaster failed: " + he.getMessage(), he);
		} catch (Exception e) {
			logger.error("getListOfWeekendMaster failed: " + e.getMessage(), e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getWeekendMasterCount(WeekendMasterDto weekendMasterDto) throws ApplicationException {
		try{
			Session session= sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria((WeekendMaster.class));
			c.add(Restrictions.eq("organizationId", weekendMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", weekendMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			
			return new Response(SUCCESS, null, null, null, count);

			} catch (HibernateException he) {
				logger.error("getWeekendMasterCount failed: " + he.getMessage(), he);
			} catch (Exception e) {
				logger.error("getWeekendMasterCount failed: " + e.getMessage(), e);
			}
			return new Response(ERROR, null, null, null, null);
		}

	@Override
	public Response getWeekendMasterByName(WeekendMasterDto weekendMasterDto) throws ApplicationException {
		try{
			
			List<WeekendMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WEEKEND_MASTER_BY_NAME")
					.setParameter("weekendDate", GlobalCommonDateUtils.getDate(weekendMasterDto.getWeekendDate(), "dd-M-yyyy"))
					.setResultTransformer(Transformers.aliasToBean(WeekendMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
			} catch (HibernateException he) {
			logger.error("getWeekendMasterByName failed", he);
			} catch (Exception e) {
			logger.error("getWeekendMasterByName failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
	
	@Override
	public Response getWeekendMasterByNameNotId(WeekendMasterDto weekendMasterDto) throws ApplicationException {
		try{
			List<WeekendMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WEEKEND_MASTER_BY_NAME_NOT_ID")
					.setParameter("weekendId", weekendMasterDto.getWeekendId())
					.setParameter("weekendDate", GlobalCommonDateUtils.getDate(weekendMasterDto.getWeekendDate(), "dd-M-yyyy"))
					.setResultTransformer(Transformers.aliasToBean(WeekendMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
			} catch (HibernateException he) {
			logger.error("getWeekendMasterByNameNotId failed", he);
			} catch (Exception e) {
			logger.error("getWeekendMasterByNameNotId failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}

	@Override
	public Response getWeekendMasterById(Integer weekendId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try{
			
			List<WeekendMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WEEKEND_MASTER_BY_ID")
					.setParameter("weekendId", weekendId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(WeekendMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
			} catch (HibernateException he) {
			logger.error("getWeekendMasterById failed", he);
			} catch (Exception e) {
			logger.error("getWeekendMasterById failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}

	

}

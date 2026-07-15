package com.param.global.master.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dto.DietTypeMasterDto;
import com.param.global.master.dto.MealMasterDto;
import com.param.global.master.model.MealMaster;
import com.param.opd.coversheet.model.DietTypeMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MealMasterDaoImpl extends GenericDao<MealMaster, Integer> implements IMealMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(MealMasterDaoImpl.class);

	public MealMasterDaoImpl() {
		super(MealMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;

	public Response getMealMasterByName(MealMasterDto mealMasterDto) throws ApplicationException {
		try {
			List<MealMasterDto> mealMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("MEAL_MASTER.GET_MEAL_MASTER_LIST_BY_NAME")
					.setParameter("organizationId", mealMasterDto.getOrganizationId()) //added by minesh for save
					.setParameter("code", mealMasterDto.getCode().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(MealMasterDto.class)).list();
			return new Response(SUCCESS, null, null, mealMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	//added by minesh for update
	public Response getMealMasterByNameNotById(MealMasterDto mealMasterDto) throws ApplicationException {
		try {
			List<MealMasterDto> mealMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("MEAL_MASTER.GET_MEAL_MASTER_LIST_BY_NAME_Not_BY_ID")
					.setParameter("organizationId", mealMasterDto.getOrganizationId()) 
					.setParameter("mealId", mealMasterDto.getMealId())
					.setParameter("code", mealMasterDto.getCode().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(MealMasterDto.class)).list();
			return new Response(SUCCESS, null, null, mealMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	public Response getMealMasterByTime(MealMasterDto mealMasterDto) throws ApplicationException {
		try {
			List<MealMasterDto> mealMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("MEAL_MASTER.GET_MEAL_MASTER_BY_SERVING_TIME")
					.setParameter("organizationId", mealMasterDto.getOrganizationId()) //added by minesh for save
					.setParameter("servingTime", mealMasterDto.getServingTime())
					.setResultTransformer(Transformers.aliasToBean(MealMasterDto.class)).list();
			return new Response(SUCCESS, null, null, mealMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	//added by minesh for update
	public Response getMealMasterByTimeNotById(MealMasterDto mealMasterDto) throws ApplicationException {
		try {
			List<MealMasterDto> mealMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("MEAL_MASTER.GET_MEAL_MASTER_BY_SERVING_TIME_NOT_BY_ID")
					.setParameter("organizationId", mealMasterDto.getOrganizationId())
					.setParameter("mealId", mealMasterDto.getMealId())
					.setParameter("servingTime", mealMasterDto.getServingTime())
					.setResultTransformer(Transformers.aliasToBean(MealMasterDto.class)).list();
			return new Response(SUCCESS, null, null, mealMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveMealMaster(MealMasterDto mealMasterDto) throws ApplicationException {
		try {
			MealMaster mealMaster = mapper.map(mealMasterDto, MealMaster.class, "MealMasterDto_to_MealMaster");

			mealMaster.setCreatedDate(TimeZoneComponent.getDateByzone(0));
			mealMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(0));

			mealMaster = save(mealMaster);
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllMealMasterList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			List<MealMasterDto> mealMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("MEAL_MASTER.GET_ALL_MEAL_MASTER_LIST")
					.setParameter("organizationId",organizationId)
					//.setParameter("unitId", unitId) //coment by minesh bcoz its only on org level.
					.setResultTransformer(Transformers.aliasToBean(MealMasterDto.class)).list();
			return new Response(SUCCESS, null, null, mealMasterList, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveMealMasterList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			List<MealMasterDto> mealMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("MEAL_MASTER.GET_ACTIVE_MEAL_MASTER_LIST")
					.setParameter("organizationId",organizationId)
					//.setParameter("unitId", unitId) //coment by minesh bcoz its only on org level.
					.setResultTransformer(Transformers.aliasToBean(MealMasterDto.class)).list();
			return new Response(SUCCESS, null, null, mealMasterList, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getMealMasterDropDown(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			List<MealMasterDto> mealMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("MEAL_MASTER.GET_MEAL_MASTER_DROP_DOWN")
					.setParameter("organizationId",organizationId)
					//.setParameter("unitId", unitId)  //coment by minesh bcoz its only on org level.
					.setResultTransformer(Transformers.aliasToBean(MealMasterDto.class)).list();
			return new Response(SUCCESS, null, null, mealMasterList, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStatus(MealMasterDto mealMasterDto) {
		try {
			Session session = sessionFactory.getCurrentSession();

			MealMaster mealMaster = (MealMaster) session.get(MealMaster.class, mealMasterDto.getMealId());
			mealMaster.setStatus(mealMasterDto.getStatus());
			mealMaster.setUpdatedBy(mealMasterDto.getUpdatedBy());
			mealMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(0));

			mealMaster = update(mealMaster);
			if (mealMaster.getStatus() == 'A') {
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			} else {
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_DELETE, null, null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response update(MealMasterDto mealMasterDto) {
		try {
			Session session = sessionFactory.getCurrentSession();

			MealMaster mealMaster = (MealMaster) session.get(MealMaster.class, mealMasterDto.getMealId());

			mealMaster.setCode(mealMasterDto.getCode());
			mealMaster.setName(mealMasterDto.getName());
			//mealMaster.setServingTime(mealMasterDto.getServingTime());
			mealMaster.setServingTime(new SimpleDateFormat("HH:mm").parse(mealMasterDto.getServingTime()));
			mealMaster.setUpdatedBy(mealMasterDto.getUpdatedBy());
			mealMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(0));

			mealMaster = update(mealMaster);

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public Response getAllMealMasterListPaginationList(MealMasterDto mealMasterDto) throws ApplicationException {
		try {
			List<MealMasterDto> MealMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("MEAL_MASTER.GET_ACTIVE_MEAL_MASTER_LIST")
					.setParameter("organizationId",mealMasterDto.getOrganizationId())
					.setFirstResult(mealMasterDto.getOffset() != null ? mealMasterDto.getOffset()  : 0)
					.setMaxResults(mealMasterDto.getNoOfRecordsPerPage() != null ? mealMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(MealMasterDto.class)).list();
			return new Response(SUCCESS, null, null, MealMasterDtosList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public Response getAllMealMasterListPaginationListCount(MealMasterDto mealMasterDto) throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(MealMaster.class);
			crit.add(Restrictions.eq("organizationId", mealMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", mealMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}

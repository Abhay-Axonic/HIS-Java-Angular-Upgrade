package com.param.adt.master.unit.dao;

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

import com.param.adt.master.unit.dto.MealPreferenceMasterDto;
import com.param.adt.master.unit.model.MealPreferenceMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MealPreferenceMasterDaoImpl extends GenericDao<MealPreferenceMaster, Integer> implements IMealPreferenceMasterDao, ICommonConstants {

	public MealPreferenceMasterDaoImpl() {
		super(MealPreferenceMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	@Override
	public Response saveMealPreferenceMaster(MealPreferenceMasterDto mealPreferenceMasterDto)
			throws ApplicationException {
		try {
			MealPreferenceMaster mealPreferenceMaster=new MealPreferenceMaster();
			mealPreferenceMaster.setMealPreferenceCode(mealPreferenceMasterDto.getMealPreferenceCode());
			mealPreferenceMaster.setMealPreferenceDesc(mealPreferenceMasterDto.getMealPreferenceDesc());
			mealPreferenceMaster.setStatus(mealPreferenceMasterDto.getStatus());
			mealPreferenceMaster.setOrganizationId(mealPreferenceMasterDto.getOrganizationId());
			mealPreferenceMaster.setUnitId(mealPreferenceMasterDto.getUnitId());
			mealPreferenceMaster.setCreatedBy(mealPreferenceMasterDto.getCreatedBy());
			mealPreferenceMaster.setCreatedDate(GlobalCommonDateUtils.getDate(mealPreferenceMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			mealPreferenceMaster.setUpdatedBy(mealPreferenceMasterDto.getUpdatedBy());
			mealPreferenceMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(mealPreferenceMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			save(mealPreferenceMaster);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mealPreferenceMaster);
		} catch (HibernateException he) {
		} catch (Exception e) {
				
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}



	@Override
	public Response getMealPreferenceMasterById(int mealPrefernceId, int orgId, int unitId)
			throws ApplicationException {
		try {
			List<MealPreferenceMasterDto> listMealPreferenceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MEAL_PREFERENCE_MASTER_BY_ID")
					.setParameter("mealPrefernceId", mealPrefernceId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(MealPreferenceMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMealPreferenceMasterDto, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getMealPreferenceMasterList(MealPreferenceMasterDto mealPreferenceMasterDto)
			throws ApplicationException {
		try {
			List<MealPreferenceMasterDto> listMealPreferenceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MEAL_PREFERENCE_MASTER_LIST")
					.setParameter("orgId", mealPreferenceMasterDto.getOrganizationId())
					.setParameter("unitId", mealPreferenceMasterDto.getUnitId())
					.setFirstResult(mealPreferenceMasterDto.getOffset() != null ? mealPreferenceMasterDto.getOffset() : 0)
					.setMaxResults(mealPreferenceMasterDto.getNoOfRecordsPerPage() != null ? mealPreferenceMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(MealPreferenceMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMealPreferenceMasterDto, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response updateNursingStationMaster(MealPreferenceMasterDto mealPreferenceMasterDto)
			throws ApplicationException {
		try {
			MealPreferenceMaster mealPreferenceMaster=findById(mealPreferenceMasterDto.getMealPreferenceId());
			mealPreferenceMaster.setMealPreferenceCode(mealPreferenceMasterDto.getMealPreferenceCode());
			mealPreferenceMaster.setMealPreferenceDesc(mealPreferenceMasterDto.getMealPreferenceDesc());
			mealPreferenceMaster.setUpdatedBy(mealPreferenceMasterDto.getUpdatedBy());
			mealPreferenceMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(mealPreferenceMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(mealPreferenceMaster);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mealPreferenceMaster);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response updateMealPreferenceMasterStatus(MealPreferenceMasterDto mealPreferenceMasterDto)
			throws ApplicationException {
		try {
			MealPreferenceMaster mealPreferenceMaster=findById(mealPreferenceMasterDto.getMealPreferenceId());
			mealPreferenceMaster.setStatus(mealPreferenceMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			mealPreferenceMaster.setUpdatedBy(mealPreferenceMasterDto.getUpdatedBy());
			mealPreferenceMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(mealPreferenceMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(mealPreferenceMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mealPreferenceMaster.getUnitId());

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getMealPreferenceMasterCount(MealPreferenceMasterDto mealPreferenceMasterDto)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(MealPreferenceMaster.class); 
			c.add(Restrictions.eq("organizationId", mealPreferenceMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", mealPreferenceMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getMealPreferenceMasterByName(MealPreferenceMasterDto mealPreferenceMasterDto)
			throws ApplicationException {
		try {
			List<MealPreferenceMasterDto> listMealPreferenceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MEAL_PREFERENCE_MASTER_BY_NAME")
					.setParameter("orgId", mealPreferenceMasterDto.getOrganizationId())
					.setParameter("unitId", mealPreferenceMasterDto.getUnitId())
					.setParameter("mealDesc", mealPreferenceMasterDto.getMealPreferenceDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(MealPreferenceMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMealPreferenceMasterDto, null);

		} catch (HibernateException he) {
		} catch (Exception e) {

		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getMealPreferenceMasterByNameNotById(MealPreferenceMasterDto mealPreferenceMasterDto)
			throws ApplicationException {
		try {
			List<MealPreferenceMasterDto> listMealPreferenceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MEAL_PREFERENCE_MASTER_BYNAME_NOTBYID")					
					.setParameter("mealPrefernceId", mealPreferenceMasterDto.getMealPreferenceId())
					.setParameter("orgId", mealPreferenceMasterDto.getOrganizationId())
					.setParameter("unitId", mealPreferenceMasterDto.getUnitId())
					.setParameter("mealDesc", mealPreferenceMasterDto.getMealPreferenceDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(MealPreferenceMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMealPreferenceMasterDto, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getActiveMealPreferenceMasterList(MealPreferenceMasterDto mealPreferenceMasterDto)
			throws ApplicationException {
		try {
			List<MealPreferenceMasterDto> listMealPreferenceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_MEAL_PREFERENCE_MASTER_LIST")
					.setParameter("orgId", mealPreferenceMasterDto.getOrganizationId())
					.setParameter("unitId", mealPreferenceMasterDto.getUnitId())
					.setFirstResult(mealPreferenceMasterDto.getOffset() != null ? mealPreferenceMasterDto.getOffset() : 0)
					.setMaxResults(mealPreferenceMasterDto.getNoOfRecordsPerPage() != null ? mealPreferenceMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(MealPreferenceMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMealPreferenceMasterDto, null);
		} catch (HibernateException he) {
		} catch (Exception e) {

		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}
}

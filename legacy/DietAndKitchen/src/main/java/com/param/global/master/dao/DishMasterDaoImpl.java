package com.param.global.master.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dto.DishDto;
import com.param.global.master.dto.DishMasterDietPrefMapperDto;
import com.param.global.master.dto.DishMasterDto;
import com.param.global.master.dto.KeyIngredientsMasterDto;
import com.param.global.master.dto.MealPreferenceMasterDto;
import com.param.global.master.model.DishMaster;
import com.param.global.master.model.DishMasterDietPrefMapper;
import com.param.global.master.model.DishMasterMapper;
import com.param.global.master.model.MealMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DishMasterDaoImpl extends GenericDao<DishMaster, Integer> implements IDishMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(DishMasterDaoImpl.class);

	public DishMasterDaoImpl() {
		super(DishMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;


	public Response getDishMasterByName(DishMasterDto dishMasterDto) throws ApplicationException {
		try {
			List<DishMasterDto> dishMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISH_MASTER_LIST_BY_NAME")
					.setParameter("dishName", dishMasterDto.getDishCode().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(DishMasterDto.class)).list();

			for (DishMasterDto dishMasterDto1 : dishMasterDtoList) {

				List<DishMasterDietPrefMapperDto> dishMasterDietPrefMapperDtoList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_DIET_PREF_DETAILS_BY_DISH_ID")
						.setParameter("dishId", dishMasterDto1.getDishId())
						.setResultTransformer(Transformers.aliasToBean(DishMasterDietPrefMapperDto.class)).list();

				dishMasterDto1.setDietPreferenceList(dishMasterDietPrefMapperDtoList);

			}

			return new Response(SUCCESS, null, null, dishMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveDishMaster(DishMasterDto dishMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();

			DishMaster dishMaster = mapper.map(dishMasterDto, DishMaster.class, "DishMasterDto_to_DishMaster");

			dishMaster.setStatus('A');
			dishMaster.setCreatedDate(TimeZoneComponent.getDateByzone(dishMasterDto.getUnitId()));
			dishMaster.setCreatedBy(dishMasterDto.getCreatedBy());
			dishMaster.setUpdatedBy(dishMasterDto.getUpdatedBy());
			dishMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(dishMasterDto.getUnitId()));

			Integer dishId = (Integer) session.save(dishMaster);

			List<KeyIngredientsMasterDto> ingredientsList = dishMasterDto.getIngredientsList();

			for (KeyIngredientsMasterDto keyIngredientsMasterDto : ingredientsList) {

				DishMasterMapper dishMasterMapper = new DishMasterMapper();

				dishMasterMapper.setDishId(dishId);
				dishMasterMapper.setKeyIngredientsId(keyIngredientsMasterDto.getKeyIngredientId());
				dishMasterMapper.setUomId(keyIngredientsMasterDto.getUomId());

				dishMasterMapper.setValue(keyIngredientsMasterDto.getValue());
				dishMasterMapper.setUpdatedBy(dishMasterDto.getUpdatedBy());
				dishMasterMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(dishMasterDto.getUnitId()));

				session.save(dishMasterMapper);

			}


			dishMasterDto.getDietPreferenceList().forEach(dishMasterDietPref -> {
				dishMasterDto.getDietMealTypeList().forEach(dietMealType -> {
					dishMasterDto.getDietTypeList().forEach(dietType -> {
						DishMasterDietPrefMapper dietPrefMapper = new DishMasterDietPrefMapper();
						dietPrefMapper.setDishId(dishId);
						dietPrefMapper.setDietPreferenceId(dishMasterDietPref.getDietPrefId());
						dietPrefMapper.setMealTypeId(dietMealType.getMealTypeId());
						dietPrefMapper.setDietTypeId(dietType.getDietTypeId());
						session.save(dietPrefMapper);
					});
				});
			});


			return new Response(SUCCESS, SUCCESS_CODE, "Added Successfully", null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDishMasterList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			List<DishMasterDto> dishMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISH_MASTER_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DishMasterDto.class)).list();

			for (DishMasterDto dishMasterDto : dishMasterList) {

				List<DishMasterDietPrefMapperDto> dishMasterDietPrefMapperDtoList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_DIET_PREF_DETAILS_BY_DISH_ID")
						.setParameter("dishId", dishMasterDto.getDishId())
						.setResultTransformer(Transformers.aliasToBean(DishMasterDietPrefMapperDto.class)).list();

				dishMasterDto.setDietPreferenceList(dishMasterDietPrefMapperDtoList);

				List<DishMasterDietPrefMapperDto> dishMasterDietPrefMapperDtoList1 = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_MEAL_PREF_DETAILS_BY_DISH_ID")
						.setParameter("dishId", dishMasterDto.getDishId())
						.setResultTransformer(Transformers.aliasToBean(DishMasterDietPrefMapperDto.class)).list();

				dishMasterDto.setDietMealTypeList(dishMasterDietPrefMapperDtoList1);

				List<DishMasterDietPrefMapperDto> dishMasterDietPrefMapperDtoList2 = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_DIET_TYPE_DETAILS_BY_DISH_ID")
						.setParameter("dishId", dishMasterDto.getDishId())
						.setResultTransformer(Transformers.aliasToBean(DishMasterDietPrefMapperDto.class)).list();

				dishMasterDto.setDietTypeList(dishMasterDietPrefMapperDtoList2);

			}
			return new Response(SUCCESS, null, null, dishMasterList, null);
		} catch (Exception e) {
			throw e;

		}
	//	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDishMasterListWithoutOrgId() throws ApplicationException {
		try {
			List<DishMasterDto> dishMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISH_MASTER_LIST_WITHOUT_ORG_ID")
					.setResultTransformer(Transformers.aliasToBean(DishMasterDto.class)).list();

			for (DishMasterDto dishMasterDto : dishMasterList) {

				List<DishMasterDietPrefMapperDto> dishMasterDietPrefMapperDtoList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_DIET_PREF_DETAILS_BY_DISH_ID")
						.setParameter("dishId", dishMasterDto.getDishId())
						.setResultTransformer(Transformers.aliasToBean(DishMasterDietPrefMapperDto.class)).list();

				dishMasterDto.setDietPreferenceList(dishMasterDietPrefMapperDtoList);

			}
			return new Response(SUCCESS, null, null, dishMasterList, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStatus(DishMasterDto dishMasterDto) {
		try {
			Session session = sessionFactory.getCurrentSession();
			DishMaster dishMaster = (DishMaster) session.get(DishMaster.class, dishMasterDto.getDishId());
			dishMaster.setStatus(dishMasterDto.getStatus());
			if (dishMasterDto.getStatus() == 'A') {
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
	public Response updateMaster(DishMasterDto dishMasterDto) {
		try {
			Session session = sessionFactory.getCurrentSession();

			DishMaster dishMaster = (DishMaster) session.get(DishMaster.class, dishMasterDto.getDishId());

			dishMaster.setDietPreferenceId(dishMasterDto.getDietPreferenceId());
			dishMaster.setDishCode(dishMasterDto.getDishCode());
			dishMaster.setDishDescription(dishMasterDto.getDishDescription());
			dishMaster.setDishValue(dishMasterDto.getDishValue());
			dishMaster.setUomId(dishMasterDto.getUomId());

			dishMaster.setUpdatedBy(dishMasterDto.getUpdatedBy());
			dishMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(dishMasterDto.getUnitId()!= null ? dishMasterDto.getUnitId():0));

			dishMaster = update(dishMaster);

			String deleteQuery = "DELETE FROM DishMasterMapper WHERE dishId=:dish_id";
			session.createQuery(deleteQuery).setParameter("dish_id", dishMasterDto.getDishId()).executeUpdate();

			List<KeyIngredientsMasterDto> ingredientsList = dishMasterDto.getIngredientsList();

			for (KeyIngredientsMasterDto keyIngredientsMasterDto : ingredientsList) {

				DishMasterMapper dishMasterMapper = new DishMasterMapper();

				dishMasterMapper.setDishId(dishMasterDto.getDishId());
				dishMasterMapper.setKeyIngredientsId(keyIngredientsMasterDto.getKeyIngredientId());
				dishMasterMapper.setUomId(keyIngredientsMasterDto.getUomId());

				dishMasterMapper.setValue(keyIngredientsMasterDto.getValue());
				dishMasterMapper.setUpdatedBy(dishMasterDto.getUpdatedBy());
				dishMasterMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(dishMasterDto.getUnitId()!= null ? dishMasterDto.getUnitId():0));

				session.save(dishMasterMapper);

			}

			String dietDeleteQuery = "DELETE FROM DishMasterDietPrefMapper WHERE dishId=:dish_id";
			session.createQuery(dietDeleteQuery).setParameter("dish_id", dishMasterDto.getDishId()).executeUpdate();


			dishMasterDto.getDietPreferenceList().forEach(dishMasterDietPref -> {
				dishMasterDto.getDietMealTypeList().forEach(dietMealType -> {
					dishMasterDto.getDietTypeList().forEach(dietType -> {
						DishMasterDietPrefMapper dietPrefMapper = new DishMasterDietPrefMapper();
						dietPrefMapper.setDishId(dishMasterDto.getDishId());
						dietPrefMapper.setDietPreferenceId(dishMasterDietPref.getDietPrefId());
						dietPrefMapper.setMealTypeId(dietMealType.getMealTypeId());
						dietPrefMapper.setDietTypeId(dietType.getDietTypeId());
						session.save(dietPrefMapper);
					});
				});
			});


			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveDishMasterList() throws ApplicationException {
		try {
			List<DishMasterDto> dishMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISH_ACTIVE_MASTER_LIST")
					.setResultTransformer(Transformers.aliasToBean(DishMasterDto.class)).list();


			for (DishMasterDto dishMasterDto : dishMasterList) {

				List<DishMasterDietPrefMapperDto> dishMasterDietPrefMapperDtoList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_DIET_PREF_DETAILS_BY_DISH_ID")
						.setParameter("dishId", dishMasterDto.getDishId())
						.setResultTransformer(Transformers.aliasToBean(DishMasterDietPrefMapperDto.class)).list();

				dishMasterDto.setDietPreferenceList(dishMasterDietPrefMapperDtoList);

			}
			return new Response(SUCCESS, null, null, dishMasterList, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override

	public Response getDishListByDietPrefId(List<MealPreferenceMasterDto> mealPrefDtos,Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			List<Integer> dietPrefIds = new ArrayList<>();
			List<Integer> mealPrefIds = new ArrayList<>();
			List<Integer> dietTypeIds = new ArrayList<>();
			for (MealPreferenceMasterDto mealPreferenceMasterDto : mealPrefDtos) {
				dietPrefIds.add(mealPreferenceMasterDto.getDietPrefId());
				mealPrefIds.add(mealPreferenceMasterDto.getMealPreferenceId());
				dietTypeIds.add(mealPreferenceMasterDto.getDietTypeId());
			}
			List<DishMasterDto> dishMasterList=new ArrayList();

			if(!dietPrefIds.isEmpty())
			{
				dishMasterList= sessionFactory.getCurrentSession()
						.getNamedQuery("GET_DISH_LIST_BY_DIET_PREF_ID")
						.setParameterList("dietPrefIds",dietPrefIds)
						.setParameterList("mealPrefIds",mealPrefIds)
						.setParameterList("dietTypeIds",dietTypeIds)
						.setParameter("organizationId",organizationId)
						.setParameter("unitId",unitId)
						.setResultTransformer(Transformers.aliasToBean(DishMasterDto.class)).list();
				for (DishMasterDto dishMasterDto : dishMasterList) {

					List<DishMasterDietPrefMapperDto> dishMasterDietPrefMapperDtoList = sessionFactory.getCurrentSession()
							.getNamedQuery("GET_DIET_PREF_DETAILS_BY_DISH_ID")
							.setParameter("dishId", dishMasterDto.getDishId())
							.setResultTransformer(Transformers.aliasToBean(DishMasterDietPrefMapperDto.class)).list();

					dishMasterDto.setDietPreferenceList(dishMasterDietPrefMapperDtoList);

					Response resp2 = this.getIngredientsByDishId(dishMasterDto.getDishId());
					List<KeyIngredientsMasterDto> ingredientsList = resp2.getListObject();
					dishMasterDto.setIngredientsList(ingredientsList);

				}
			}
			else
			{
				dishMasterList= sessionFactory.getCurrentSession()
						.getNamedQuery("GET_DISH_ACTIVE_MASTER_DROP_DOWN")
						.setParameter("organizationId",organizationId)
						.setParameter("unitId",unitId)
						.setResultTransformer(Transformers.aliasToBean(DishMasterDto.class)).list();
				for (DishMasterDto dishMasterDto : dishMasterList) {

					List<DishMasterDietPrefMapperDto> dishMasterDietPrefMapperDtoList = sessionFactory.getCurrentSession()
							.getNamedQuery("GET_DIET_PREF_DETAILS_BY_DISH_ID")
							.setParameter("dishId", dishMasterDto.getDishId())
							.setResultTransformer(Transformers.aliasToBean(DishMasterDietPrefMapperDto.class)).list();

					dishMasterDto.setDietPreferenceList(dishMasterDietPrefMapperDtoList);

				}
			}

			return new Response(SUCCESS, null, null, dishMasterList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	public Response getDishMasterDropDown(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<DishMasterDto> distMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISH_ACTIVE_MASTER_DROP_DOWN")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(DishMasterDto.class)).list();

			return new Response(SUCCESS, null, null, distMasterList, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getIngredientsByDishId(Integer dishId) throws ApplicationException {
		try {
			List<KeyIngredientsMasterDto> ingredientsList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INGREDIENTS_BY_DISH_ID").setParameter("dishId", dishId)
					.setResultTransformer(Transformers.aliasToBean(KeyIngredientsMasterDto.class)).list();
			return new Response(SUCCESS, null, null, ingredientsList, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}

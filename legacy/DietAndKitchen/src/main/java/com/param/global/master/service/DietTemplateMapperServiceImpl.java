package com.param.global.master.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dao.IDietTemplateMapperDao;
import com.param.global.master.dto.DayWiseDishListDto;
import com.param.global.master.dto.DayWiseNutritionValuesDto;
import com.param.global.master.dto.DietTemplateDietPreferenceMapperDto;
import com.param.global.master.dto.DietTemplateDto;
import com.param.global.master.dto.DietTemplateMapperDto;
import com.param.global.master.dto.DishDto;
import com.param.global.master.dto.MealDetailsDto;
import com.param.global.master.dto.MealsDto;
import com.param.global.master.dto.NutritionalValuesDto;
import com.param.global.master.model.DietTemplateDietPreferenceMapper;
import com.param.global.master.model.MealDetails;
import com.param.global.master.model.SortByDayId;
import com.param.global.master.model.SortByMealId;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DietTemplateMapperServiceImpl implements IDietTemplateMapperService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietTemplateMapperServiceImpl.class);

	@Autowired
	IDietTemplateMapperDao iDietTemplateMapperDao;

	@Autowired
	IDishMasterService iDishMasterService;

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveDietTemplateMapper(DietTemplateDto dietTemplateDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();

			Response resp = iDietTemplateMapperDao.saveDietTemplateMapper(dietTemplateDto);
			Integer dietTemplateId = (Integer) resp.getObject();

			List<DietTemplateDietPreferenceMapperDto> dietPrefList = dietTemplateDto.getDietPreferenceList();

			for (DietTemplateDietPreferenceMapperDto dietPrefId : dietPrefList) {
				DietTemplateDietPreferenceMapper dietPrefMapper = new DietTemplateDietPreferenceMapper();
				dietPrefMapper.setDietTemplateId(dietTemplateId);
				dietPrefMapper.setDietPrefId(dietPrefId.getDietPrefId());
				session.save(dietPrefMapper);
			}

			List<MealDetailsDto> mealDetailsList = dietTemplateDto.getMealDetailsList();

			for (MealDetailsDto mealDetailsDto : mealDetailsList) {

				for (MealsDto mealsDto : mealDetailsDto.getMealsList()) {

					for (DishDto dishDto : mealsDto.getDishList()) {

						if (dishDto!= null) {
							MealDetails mealDetails = new MealDetails();

							mealDetails.setMealId(mealDetailsDto.getMealId());
							mealDetails.setDietTemplateId(dietTemplateId);
							mealDetails.setDayId(mealsDto.getDayId());
							mealDetails.setDishId(dishDto.getDishId());
							mealDetails.setQuantity(dishDto.getQuantity());

							session.save(mealDetails);
						}

					}

				}

			}

			return new Response(SUCCESS, null, "Added Successfully.", null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllDietTemplateMapper(Integer organizationId,Integer unitId) throws ApplicationException {
		try {

			List<DietTemplateDto> dietTemplateList = sessionFactory.getCurrentSession()
					.createQuery(
							"select dietTemplate.dietTemplateId as dietTemplateId,dietTemplate.dietTypeId as dietTypeId from DietTemplate dietTemplate "
							+ "where dietTemplate.organizationId=:organizationId and dietTemplate.unitId=:unitId ")
					.setInteger("organizationId",organizationId)
					.setInteger("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DietTemplateDto.class)).list();

			for (DietTemplateDto dietTemplateDto : dietTemplateList) {
				List<DietTemplateDietPreferenceMapperDto> dietPreferenceList = sessionFactory.getCurrentSession()
						.createQuery(
								"select distinct dietPref.dietPrefId as dietPrefId,dietPref.dietTemplateDietPrefId as dietTemplateDietPrefId,"
										+ " mealPreferenceMaster.mealPreferenceDesc as dietPreferenceDesc "
										+ "from DietTemplateDietPreferenceMapper dietPref "
										+ "INNER JOIN dietPref.mealPreferenceMaster as mealPreferenceMaster "
										+ "where dietPref.dietTemplateId=:dietTemplateId")
						.setInteger("dietTemplateId", dietTemplateDto.getDietTemplateId())
						.setResultTransformer(Transformers.aliasToBean(DietTemplateDietPreferenceMapperDto.class))
						.list();
				dietTemplateDto.setDietPreferenceList(dietPreferenceList);
			}

			for (DietTemplateDto dietTemplateDto : dietTemplateList) {

				List<MealDetailsDto> mealDetailsList = sessionFactory.getCurrentSession()
						.createQuery("select distinct mealDetails.mealId as mealId,"
								+ "mealMaster.name as mealName from MealDetails mealDetails "
								+ "INNER JOIN mealDetails.mealMaster as mealMaster "
								+ "where mealDetails.dietTemplateId=:dietTemplateId")
						.setInteger("dietTemplateId", dietTemplateDto.getDietTemplateId())
						.setResultTransformer(Transformers.aliasToBean(MealDetailsDto.class)).list();

				for (MealDetailsDto mealDetailsDto : mealDetailsList) {

					List<MealsDto> mealsList = new ArrayList();
					for (int i = 0; i < 7; i++) {
						MealsDto mealsDto = new MealsDto();
						mealsDto.setDayId(i);
						mealsList.add(mealsDto);
					}

					for (MealsDto mealsDto : mealsList) {

						List<DishDto> dishList = sessionFactory.getCurrentSession()
								.createQuery("select mealDetails.dishId as dishId,"
										+ "mealDetails.mealDetailsId as mealDetailsId,"
										+ "dishMaster.dishDescription as dishName,"
										+ "mealDetails.quantity as quantity " + "from MealDetails mealDetails "
										+ "INNER JOIN mealDetails.dishMaster as dishMaster "
										+ "where mealDetails.dietTemplateId=:dietTemplateId "
										+ "AND mealDetails.dayId=:dayId " + "AND mealDetails.mealId=:mealId ")
								.setInteger("dietTemplateId", dietTemplateDto.getDietTemplateId())
								.setInteger("dayId", mealsDto.getDayId())
								.setInteger("mealId", mealDetailsDto.getMealId())
								.setResultTransformer(Transformers.aliasToBean(DishDto.class)).list();

						if (!dishList.isEmpty()) {
							mealsDto.setDishList(dishList);
						} else {
							DishDto dishDto = new DishDto();
							dishList.add(dishDto);
							mealsDto.setDishList(dishList);
						}

					}

					Collections.sort(mealsList, new SortByDayId());

					mealDetailsDto.setMealsList(mealsList);
				}
				Collections.sort(mealDetailsList, new SortByMealId());

				dietTemplateDto.setMealDetailsList(mealDetailsList);
			}

			return new Response(SUCCESS, null, null, dietTemplateList, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDietTemplateById(Integer dietTemplateId,Integer organizationId,Integer unitId) {
		try {

			List<DietTemplateDto> dietTemplateList = sessionFactory.getCurrentSession().createQuery(
					"select dietTemplate.dietTemplateId as dietTemplateId,dietTemplate.dietTypeId as dietTypeId "
							+ "from DietTemplate dietTemplate " + "where dietTemplate.dietTemplateId=:dietTemplateId "
							+ "and dietTemplate.organizationId=:organizationId and dietTemplate.unitId=:unitId  ")
					.setInteger("dietTemplateId", dietTemplateId)
					.setInteger("organizationId", organizationId)
					.setInteger("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DietTemplateDto.class)).list();

			for (DietTemplateDto dietTemplateDto : dietTemplateList) {
				List<DietTemplateDietPreferenceMapperDto> dietPreferenceList = sessionFactory.getCurrentSession()
						.createQuery(
								"select distinct dietPref.dietPrefId as dietPrefId,dietPref.dietTemplateDietPrefId as dietTemplateDietPrefId,"
										+ " mealPreferenceMaster.mealPreferenceDesc as dietPreferenceDesc "
										+ "from DietTemplateDietPreferenceMapper dietPref "
										+ "INNER JOIN dietPref.mealPreferenceMaster as mealPreferenceMaster "
										+ "where dietPref.dietTemplateId=:dietTemplateId")
						.setInteger("dietTemplateId", dietTemplateDto.getDietTemplateId())
						.setResultTransformer(Transformers.aliasToBean(DietTemplateDietPreferenceMapperDto.class))
						.list();
				dietTemplateDto.setDietPreferenceList(dietPreferenceList);
			}

			for (DietTemplateDto dietTemplateDto : dietTemplateList) {

				List<MealDetailsDto> mealDetailsList = sessionFactory.getCurrentSession()
						.createQuery("select distinct mealMaster.id as mealId,"
								+ "mealMaster.name as mealName from MealMaster mealMaster "
								+ "where mealMaster.organizationId=:organizationId and status='A' "
								//+ "and mealMaster.unitId=:unitId "  //coment by minesh bcoz its only on org level.
								+ "")
						.setInteger("organizationId", organizationId)
						//.setInteger("unitId", unitId)  //coment by minesh bcoz its only on org level.
						.setResultTransformer(Transformers.aliasToBean(MealDetailsDto.class)).list();

				for (MealDetailsDto mealDetailsDto : mealDetailsList) {

					List<MealsDto> mealsList = new ArrayList();
					for (int i = 0; i < 7; i++) {
						MealsDto mealsDto = new MealsDto();
						mealsDto.setDayId(i);
						mealsList.add(mealsDto);
					}

					for (MealsDto mealsDto : mealsList) {

						List<DishDto> dishList = sessionFactory.getCurrentSession()
								.createQuery("select mealDetails.dishId as dishId,"
										+ "mealDetails.mealDetailsId as mealDetailsId,"
										+ "dishMaster.dishDescription as dishDescription,"
										+ "mealDetails.quantity as quantity " + "from MealDetails mealDetails "
										+ "INNER JOIN mealDetails.dishMaster as dishMaster "
										+ "where mealDetails.dietTemplateId=:dietTemplateId "
										+ "AND mealDetails.dayId=:dayId " + "AND mealDetails.mealId=:mealId ")
								.setInteger("dietTemplateId", dietTemplateDto.getDietTemplateId())
								.setInteger("dayId", mealsDto.getDayId())
								.setInteger("mealId", mealDetailsDto.getMealId())
								.setResultTransformer(Transformers.aliasToBean(DishDto.class)).list();

						if (!dishList.isEmpty()) {
							mealsDto.setDishList(dishList);
						} else {
							DishDto dishDto = new DishDto();
							dishList.add(dishDto);
							mealsDto.setDishList(dishList);
						}

					}

					Collections.sort(mealsList, new SortByDayId());

					mealDetailsDto.setMealsList(mealsList);
				}

				Collections.sort(mealDetailsList, new SortByMealId());

				dietTemplateDto.setMealDetailsList(mealDetailsList);
			}

			return new Response(SUCCESS, null, null, null, dietTemplateList.get(0));

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	public Response getDietTemplateByDietPrefIdsAndDietTypeId(Integer dietPrefId,Integer dietTypeId,Integer organizationId,Integer unitId) {
		try {

			List<DietTemplateDto> dietTemplateList = sessionFactory.getCurrentSession().createQuery(
					"select dietTemplate.dietTemplateId as dietTemplateId,dietTemplate.dietTypeId as dietTypeId "
							+ "from DietTemplate dietTemplate " + "where dietTemplate.dietTypeId=:dietTypeId "
							+ " AND dietTemplate.organizationId=:organizationId AND dietTemplate.unitId=:unitId ")
					.setInteger("organizationId", organizationId)
					.setInteger("unitId", unitId)
					.setInteger("dietTypeId", dietTypeId)
					.setResultTransformer(Transformers.aliasToBean(DietTemplateDto.class)).list();

			if(!dietTemplateList.isEmpty())
			{
			for (DietTemplateDto dietTemplateDto : dietTemplateList) {
				List<DietTemplateDietPreferenceMapperDto> dietPreferenceList = sessionFactory.getCurrentSession()
						.createQuery(
								"select distinct dietPref.dietPrefId as dietPrefId,dietPref.dietTemplateDietPrefId as dietTemplateDietPrefId,"
										+ " mealPreferenceMaster.mealPreferenceDesc as dietPreferenceDesc "
										+ "from DietTemplateDietPreferenceMapper dietPref "
										+ "INNER JOIN dietPref.mealPreferenceMaster as mealPreferenceMaster "
										+ "where dietPref.dietTemplateId=:dietTemplateId AND dietPref.dietPrefId=:dietPrefId ")
						.setInteger("dietTemplateId", dietTemplateDto.getDietTemplateId())
						.setInteger("dietPrefId",dietPrefId )
						.setResultTransformer(Transformers.aliasToBean(DietTemplateDietPreferenceMapperDto.class))
						.list();
				dietTemplateDto.setDietPreferenceList(dietPreferenceList);
			}

			for (DietTemplateDto dietTemplateDto : dietTemplateList) {

				List<MealDetailsDto> mealDetailsList = sessionFactory.getCurrentSession()
						.createQuery("select distinct mealMaster.id as mealId,"
								+ "mealMaster.name as mealName, "
								+ "to_char(mealMaster.servingTime,'HH24:MI') as servingTime "
								+ "from MealMaster mealMaster "
								+ "where mealMaster.organizationId=:organizationId "
								+ "and mealMaster.status='A' "
								//+ "mealMaster.unitId=:unitId "  //coment by minesh bcoz its only on org level.
								+ "")
						.setInteger("organizationId", organizationId)
						//.setInteger("unitId", unitId)  //coment by minesh bcoz its only on org level.
						.setResultTransformer(Transformers.aliasToBean(MealDetailsDto.class)).list();

				for (MealDetailsDto mealDetailsDto : mealDetailsList) {

					List<MealsDto> mealsList = new ArrayList();
					for (int i = 0; i < 7; i++) {
						MealsDto mealsDto = new MealsDto();
						mealsDto.setDayId(i);
						mealsList.add(mealsDto);
					}

					for (MealsDto mealsDto : mealsList) {

						List<DishDto> dishList = sessionFactory.getCurrentSession()
								.createQuery("select mealDetails.dishId as dishId,"
										+ "mealDetails.mealDetailsId as mealDetailsId,"
										+ "dishMaster.dishDescription as dishDescription,"
										+ "mealDetails.quantity as quantity " + "from MealDetails mealDetails "
										+ "INNER JOIN mealDetails.dishMaster as dishMaster "
										+ "where mealDetails.dietTemplateId=:dietTemplateId "
										+ "AND mealDetails.dayId=:dayId " + "AND mealDetails.mealId=:mealId ")
								.setInteger("dietTemplateId", dietTemplateDto.getDietTemplateId())
								.setInteger("dayId", mealsDto.getDayId())
								.setInteger("mealId", mealDetailsDto.getMealId())
								.setResultTransformer(Transformers.aliasToBean(DishDto.class)).list();

						if (!dishList.isEmpty()) {
							mealsDto.setDishList(dishList);
						} else {
							DishDto dishDto = new DishDto();
							dishList.add(dishDto);
							mealsDto.setDishList(dishList);
						}

					}

					Collections.sort(mealsList, new SortByDayId());

					mealDetailsDto.setMealsList(mealsList);
				}

				Collections.sort(mealDetailsList, new SortByMealId());

				dietTemplateDto.setMealDetailsList(mealDetailsList);
			}

			return new Response(SUCCESS, null, null, null, dietTemplateList.get(0));

			}
			else
			{
				return new Response(ERROR, null, "No matching Diet Template is Available.", null, null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	

	@Override
	public Response getDietTemplateList(Integer organizationId,Integer unitId) {
		try {
			Session session = sessionFactory.getCurrentSession();

			List<DietTemplateDto> dietTemplateList = session
					.createQuery("select dietTemplate.dietTemplateId as dietTemplateId,"
							+ "dietTemplate.dietTypeId as dietTypeId,dietTypeMaster.dietTypeDescription as dietTypeDesc,"
							+ "to_char(dietTemplate.createdDate,'dd-MM-yyyy') as createdDate "
							+ "from DietTemplate dietTemplate INNER JOIN dietTemplate.dietTypeMaster as dietTypeMaster "
							+ "where dietTemplate.organizationId=:organizationId and dietTemplate.unitId=:unitId")
					.setInteger("organizationId", organizationId)
					.setInteger("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DietTemplateDto.class)).list();

			for (DietTemplateDto dietTemplateDto : dietTemplateList) {
				List<DietTemplateDietPreferenceMapperDto> dietPreferenceList = sessionFactory.getCurrentSession()
						.createQuery("select distinct dietPref.dietPrefId as dietPrefId,"
								+ "dietPref.dietTemplateDietPrefId as dietTemplateDietPrefId,"
								+ "mealPreferenceMaster.mealPreferenceDesc as dietPreferenceDesc "
								+ "from DietTemplateDietPreferenceMapper dietPref "
								+ "INNER JOIN dietPref.mealPreferenceMaster as mealPreferenceMaster "
								+ "where dietPref.dietTemplateId=:dietTemplateId")
						.setInteger("dietTemplateId", dietTemplateDto.getDietTemplateId())
						.setResultTransformer(Transformers.aliasToBean(DietTemplateDietPreferenceMapperDto.class))
						.list();
				dietTemplateDto.setDietPreferenceList(dietPreferenceList);
			}

			return new Response(SUCCESS, null, null, dietTemplateList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDietTemplateMapper(DietTemplateDto dietTemplateDto) throws ApplicationException {
		try {

			dietTemplateDto = (DietTemplateDto) iDietTemplateMapperDao.updateDietTemplateMapper(dietTemplateDto)
					.getObject();
			this.saveDietTemplateMapper(dietTemplateDto);

			return new Response(SUCCESS, null, "Updated Successfully.", null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDayWiseNutritionList(Integer dietTemplateId) throws ApplicationException {
		try {
			List<DayWiseDishListDto> dayWiseDishList = new ArrayList();
			for (int dayId = 0; dayId < 7; dayId++) {
				List<DishDto> dishList = sessionFactory.getCurrentSession()
						.createQuery("select mealDetails.dishId as dishId,"
								+ "mealDetails.mealDetailsId as mealDetailsId,"
								+ "dishMaster.dishDescription as dishName," + "mealDetails.quantity as quantity "
								+ "from MealDetails mealDetails " + "INNER JOIN mealDetails.dishMaster as dishMaster "
								+ "where mealDetails.dietTemplateId=:dietTemplateId " + "AND mealDetails.dayId=:dayId ")
						.setInteger("dietTemplateId", dietTemplateId).setInteger("dayId", dayId)
						.setResultTransformer(Transformers.aliasToBean(DishDto.class)).list();
				DayWiseDishListDto dayWiseDishListDto = new DayWiseDishListDto();
				dayWiseDishListDto.setDayId(dayId);
				dayWiseDishListDto.setDishList(dishList);
				dayWiseDishList.add(dayWiseDishListDto);
			}
			List<DayWiseNutritionValuesDto> dayWiseNutritionValuesList = new ArrayList();

			for (DayWiseDishListDto dayWiseDishListDto : dayWiseDishList) {

				Map<Integer, NutritionalValuesDto> dayWiseMap = new HashMap();

				List<DishDto> dishList = dayWiseDishListDto.getDishList();
				if (!dishList.isEmpty()) {

					for (DishDto dishDto : dishList) {

						List<NutritionalValuesDto> nutritionalValuesList = iDishMasterService
								.getNutritionalValues(dishDto.getDishId()).getListObject();

						for (NutritionalValuesDto nutritionalValuesDto : nutritionalValuesList) {

							if (dayWiseMap.containsKey(nutritionalValuesDto.getId())) {
								Double value = dayWiseMap.get(nutritionalValuesDto.getId()).getValue()
										+ (nutritionalValuesDto.getValue() * dishDto.getQuantity());
								dayWiseMap.get(nutritionalValuesDto.getId()).setValue(value);
							} else {
								nutritionalValuesDto.setValue(nutritionalValuesDto.getValue() * dishDto.getQuantity());
								dayWiseMap.put(nutritionalValuesDto.getId(), nutritionalValuesDto);
							}

						}

					}

				}

				List<NutritionalValuesDto> list = new ArrayList<>(dayWiseMap.values());

				DayWiseNutritionValuesDto dayWiseNutritionValuesDto = new DayWiseNutritionValuesDto();

				dayWiseNutritionValuesDto.setDayId(dayWiseDishListDto.getDayId());
				dayWiseNutritionValuesDto.setNutritionList(list);
				dayWiseNutritionValuesList.add(dayWiseNutritionValuesDto);

			}

			return new Response(SUCCESS, null, null, dayWiseNutritionValuesList, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/*
	 * @Override public Response updateDietTemplateMapper(DietTemplateDto
	 * dietTemplateDto) throws ApplicationException { try {
	 * 
	 * Session session = sessionFactory.getCurrentSession();
	 * 
	 * iDietTemplateMapperDao.updateDietTemplateMapper(dietTemplateDto);
	 * 
	 * List<DietTemplateDietPreferenceMapperDto> dietPrefList =
	 * dietTemplateDto.getDietPreferenceList();
	 * 
	 * for (DietTemplateDietPreferenceMapperDto dietPrefDto : dietPrefList) {
	 * 
	 * DietTemplateDietPreferenceMapper dietPrefMapper =
	 * (DietTemplateDietPreferenceMapper)session
	 * .get(DietTemplateDietPreferenceMapper.class,dietPrefDto.
	 * getDietTemplateDietPrefId());
	 * dietPrefMapper.setDietTemplateId(dietTemplateDto.getDietTemplateId());
	 * dietPrefMapper.setDietPrefId(dietPrefDto.getDietPrefId());
	 * session.update(dietPrefMapper);
	 * 
	 * }
	 * 
	 * List<MealDetailsDto> mealDetailsList =
	 * dietTemplateDto.getMealDetailsList();
	 * 
	 * for (MealDetailsDto mealDetailsDto : mealDetailsList) {
	 * 
	 * for (MealsDto mealsDto : mealDetailsDto.getMealsList()) {
	 * 
	 * for (DishDto dishDto : mealsDto.getDishList()) {
	 * 
	 * if(dishDto.getMealDetailsId()!=null) { MealDetails mealDetails =
	 * (MealDetails)session.get(MealDetails.class, dishDto.getMealDetailsId());
	 * 
	 * mealDetails.setMealId(mealDetailsDto.getMealId());
	 * mealDetails.setDietTemplateId(dietTemplateDto.getDietTemplateId());
	 * mealDetails.setDayId(mealsDto.getDayId());
	 * mealDetails.setDishId(dishDto.getDishId());
	 * mealDetails.setQuantity(dishDto.getQuantity());
	 * 
	 * session.update(mealDetails); }
	 * 
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * return new Response(SUCCESS, null, "Updated Successfully.", null, null);
	 * } catch (Exception e) {  } return new Response(ERROR,
	 * null, null, null, null); }
	 */

	/*----------------------------Older APIs-----------------------------------------------*/

	@Override
	public Response getDietTemplateMapperByDishAndFoodType(DietTemplateMapperDto dietTemplateMapperDto)
			throws ApplicationException {
		try {
			return iDietTemplateMapperDao.getDietTemplateMapperByDishAndFoodType(dietTemplateMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveDietTemplateMapper() throws ApplicationException {
		try {
			return iDietTemplateMapperDao.getActiveDietTemplateMapper();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/*
	 * @Override public Response getAllDietTemplateMapper() throws
	 * ApplicationException { try { return
	 * iDietTemplateMapperDao.getAllDietTemplateMapper(); } catch (Exception e)
	 * {  } return new Response(ERROR, COMMON_ERROR_CODE,
	 * null, null, null); }
	 */

	/*
	 * @Override public Response saveDietTemplateMapper(DietTemplateMapperDto
	 * dietTemplateMapperDto) throws ApplicationException { try { return
	 * iDietTemplateMapperDao.saveDietTemplateMapper(dietTemplateMapperDto); }
	 * catch (Exception e) {  } return new Response(ERROR,
	 * null, null, null, null); }
	 */

	@Override
	public Response updateStatus(DietTemplateMapperDto dietTemplateMapperDto) {
		try {
			return iDietTemplateMapperDao.updateStatus(dietTemplateMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}

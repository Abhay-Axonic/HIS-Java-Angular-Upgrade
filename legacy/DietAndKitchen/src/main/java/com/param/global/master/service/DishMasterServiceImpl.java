package com.param.global.master.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Conventions;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dao.IDishMasterDao;
import com.param.global.master.dto.DishMasterDto;
import com.param.global.master.dto.KeyIngredientsMasterDto;
import com.param.global.master.dto.MealPreferenceMasterDto;
import com.param.global.master.dto.NutritionalMasterDto;
import com.param.global.master.dto.NutritionalValuesDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DishMasterServiceImpl implements IDishMasterService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(DishMasterServiceImpl.class);

	@Autowired
	IDishMasterDao iDishMasterDao;

	@Autowired
	IKeyIngredientsMasterService iKeyIngredientsMasterService;

	@Override
	public Response saveDishMaster(DishMasterDto dishMasterDto) throws ApplicationException {
		Response deptResponse = iDishMasterDao.getDishMasterByName(dishMasterDto);
		try {
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				return iDishMasterDao.saveDishMaster(dishMasterDto);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDishMasterList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			Response resp = iDishMasterDao.getDishMasterList(organizationId,unitId);
			List<DishMasterDto> distMasterList = resp.getListObject();
			if (distMasterList != null) {
				for (DishMasterDto dishMasterDto : distMasterList) {
					Response resp2 = iDishMasterDao.getIngredientsByDishId(dishMasterDto.getDishId());
					List<KeyIngredientsMasterDto> ingredientsList = resp2.getListObject();
					dishMasterDto.setIngredientsList(ingredientsList);
				}

			}

			return new Response(SUCCESS, null, null, distMasterList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getDishMasterListWithoutOrgId() throws ApplicationException {
		try {
			Response resp = iDishMasterDao.getDishMasterListWithoutOrgId();
			List<DishMasterDto> distMasterList = resp.getListObject();
			if (distMasterList != null) {
				for (DishMasterDto dishMasterDto : distMasterList) {
					Response resp2 = iDishMasterDao.getIngredientsByDishId(dishMasterDto.getDishId());
					List<KeyIngredientsMasterDto> ingredientsList = resp2.getListObject();
					dishMasterDto.setIngredientsList(ingredientsList);
				}

			}

			return new Response(SUCCESS, null, null, distMasterList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStatus(DishMasterDto dishMasterDto) {
		try {
			return iDishMasterDao.updateStatus(dishMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateMaster(DishMasterDto dishMasterDto) {
		try {
			return iDishMasterDao.updateMaster(dishMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveDishMasterList(Integer organizationId,Integer unitId) {
		try {
			Response resp = iDishMasterDao.getActiveDishMasterList();
			List<DishMasterDto> distMasterList = resp.getListObject();
			if (distMasterList != null) {
				for (DishMasterDto dishMasterDto : distMasterList) {
					Response resp2 = iDishMasterDao.getIngredientsByDishId(dishMasterDto.getDishId());
					List<KeyIngredientsMasterDto> ingredientsList = resp2.getListObject();
					dishMasterDto.setIngredientsList(ingredientsList);
				}

			}

			return new Response(SUCCESS, null, null, distMasterList, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDishListByDietPrefId(List<MealPreferenceMasterDto> mealPrefDtos,Integer organizationId,Integer unitId) {
		try {
			return iDishMasterDao.getDishListByDietPrefId(mealPrefDtos,organizationId,unitId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	public Response getDishMasterDropDown(Integer organizationId,Integer unitId) {
		try {
			return iDishMasterDao.getDishMasterDropDown(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getNutritionalValues(Integer dishId) throws ApplicationException {
		try {
			List<NutritionalValuesDto> nutritionalValuesList = new ArrayList<>();
			Response resp = getDishMasterListWithoutOrgId();
			DishMasterDto dishMasterDto = null;
			List<DishMasterDto> dihsList = resp.getListObject();
			for (DishMasterDto dishMasterDtoTemp : dihsList) {
				if (dishMasterDtoTemp.getDishId().intValue() == dishId) {
					dishMasterDto = dishMasterDtoTemp;
					break;
				}
			}

			Response resp2 = iKeyIngredientsMasterService.getAllKeyIngredientsMasterWithoutOrgId();
			List<KeyIngredientsMasterDto> ingredientsList = resp2.getListObject();
			
			Response resp3 = iDishMasterDao.getIngredientsByDishId(dishMasterDto.getDishId());
			List<KeyIngredientsMasterDto> ingredientsList1 = resp3.getListObject();
			

			if (dishMasterDto != null) {
				List<KeyIngredientsMasterDto> keyIngredientsList = dishMasterDto.getIngredientsList();
				for (KeyIngredientsMasterDto keyIngredientsMasterDto : keyIngredientsList) {
					for (KeyIngredientsMasterDto ingredientsMasterDto : ingredientsList) {
						if (keyIngredientsMasterDto.getKeyIngredientId() == ingredientsMasterDto.getKeyIngredientId()) {
							List<NutritionalMasterDto> nutritionList = ingredientsMasterDto.getNutriciansList();
							for (NutritionalMasterDto nutritionalMasterDto : nutritionList) {
								if(nutritionalMasterDto.getIsCalories()!=null && nutritionalMasterDto.getIsCalories().equalsIgnoreCase("Y")) {
									NutritionalValuesDto nValue = new NutritionalValuesDto();
									nValue.setId(nutritionalMasterDto.getNutritionId());
									nValue.setName(nutritionalMasterDto.getNutritionName());
									nValue.setUomName(nutritionalMasterDto.getUomName());
									nValue.setValue(
											(keyIngredientsMasterDto.getValue() / ingredientsMasterDto.getIngredientValue())
													* nutritionalMasterDto.getValue());
									nutritionalValuesList.add(nValue);
								}
							}
						}
					}
				}

			}

			Map<Integer, NutritionalValuesDto> mapOfDtos = new HashMap<>();

			for (int i = 0; i < nutritionalValuesList.size(); i++) {
				if (mapOfDtos.containsKey(nutritionalValuesList.get(i).getId())) {
					Double value = mapOfDtos.get(nutritionalValuesList.get(i).getId()).getValue()
							+ nutritionalValuesList.get(i).getValue();
					mapOfDtos.get(nutritionalValuesList.get(i).getId()).setValue(value);
				} else {
					mapOfDtos.put(nutritionalValuesList.get(i).getId(), nutritionalValuesList.get(i));
				}
			}

			List<NutritionalValuesDto> list = new ArrayList<NutritionalValuesDto>(mapOfDtos.values());

			return new Response(SUCCESS, null, null, list, null,null,ingredientsList1);

		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getNutritionalValuesByDishList(List<DishMasterDto> dishList) throws ApplicationException {
		try {

			Map<Integer, NutritionalValuesDto> mapOfDtos = new HashMap();

			if (!dishList.isEmpty()) {

				for (DishMasterDto dishDto : dishList) {

					List<NutritionalValuesDto> nutritionalValuesList = this.getNutritionalValues(dishDto.getDishId())
							.getListObject();

					for (NutritionalValuesDto nutritionalValuesDto : nutritionalValuesList) {

						if (mapOfDtos.containsKey(nutritionalValuesDto.getId())) {
							Double value = mapOfDtos.get(nutritionalValuesDto.getId()).getValue()
									+ (nutritionalValuesDto.getValue() * dishDto.getQuantity());
							mapOfDtos.get(nutritionalValuesDto.getId()).setValue(value);
						} else {
							nutritionalValuesDto.setValue(nutritionalValuesDto.getValue() * dishDto.getQuantity());
							mapOfDtos.put(nutritionalValuesDto.getId(), nutritionalValuesDto);
						}

					}

				}

			}

			List<NutritionalValuesDto> list = new ArrayList<>(mapOfDtos.values());

			return new Response(SUCCESS, null, null, list, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}

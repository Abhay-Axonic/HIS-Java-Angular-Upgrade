package com.param.global.master.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.master.dto.DishMasterDto;
import com.param.global.master.dto.MealPreferenceMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDishMasterService {

	@Transactional
	Response saveDishMaster(DishMasterDto dishMasterDto) throws ApplicationException;

	@Transactional
	Response getDishMasterList(Integer organizationId,Integer unitId) throws ApplicationException;
	
	@Transactional
	Response getDishMasterListWithoutOrgId() throws ApplicationException;

	@Transactional
	Response updateStatus(DishMasterDto dishMasterDto);

	@Transactional
	Response updateMaster(DishMasterDto dishMasterDto);

	@Transactional
	Response getActiveDishMasterList(Integer organizationId,Integer unitId);

	@Transactional
	Response getDishMasterDropDown(Integer organizationId,Integer unitId);

	@Transactional
	Response getNutritionalValues(Integer dishId);

	@Transactional
	Response getNutritionalValuesByDishList(List<DishMasterDto> dishList);

	@Transactional
	Response getDishListByDietPrefId(List<MealPreferenceMasterDto> mealPrefDtos,Integer organizationId,Integer unitId);

}

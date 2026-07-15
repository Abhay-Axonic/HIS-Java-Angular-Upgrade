package com.param.global.master.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.master.dto.DishMasterDto;
import com.param.global.master.dto.MealPreferenceMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDishMasterDao {

	Response saveDishMaster(DishMasterDto dishMasterDto) throws ApplicationException;

	Response getDishMasterByName(DishMasterDto dishMasterDto) throws ApplicationException;

	Response getDishMasterList(Integer organizationId,Integer unitId) throws ApplicationException;
	
	Response getDishMasterListWithoutOrgId() throws ApplicationException;

	Response updateStatus(DishMasterDto dishMasterDto);

	Response updateMaster(DishMasterDto dishMasterDto);

	Response getActiveDishMasterList() throws ApplicationException;

	Response getDishListByDietPrefId(List<MealPreferenceMasterDto> mealPrefDtos,Integer organizationId,Integer unitId) throws ApplicationException;

	Response getDishMasterDropDown(Integer organizationId,Integer unitId) throws ApplicationException;

	Response getIngredientsByDishId(Integer dishId) throws ApplicationException;

}

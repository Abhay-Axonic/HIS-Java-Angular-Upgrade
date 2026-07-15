package com.param.global.master.dao;

import com.param.global.common.Response;
import com.param.global.master.dto.MealMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IMealMasterDao {

	Response saveMealMaster(MealMasterDto mealMasterDto) throws ApplicationException;

	Response getMealMasterByName(MealMasterDto mealMasterDto) throws ApplicationException;

	//added by minesh for update
	Response getMealMasterByNameNotById(MealMasterDto mealMasterDto) throws ApplicationException;
	
	Response getMealMasterByTime(MealMasterDto mealMasterDto) throws ApplicationException;
	
	//added by minesh for update
	Response getMealMasterByTimeNotById(MealMasterDto mealMasterDto) throws ApplicationException;

	Response getAllMealMasterList(Integer organizationId,Integer unitId) throws ApplicationException;

	Response getActiveMealMasterList(Integer organizationId,Integer unitId) throws ApplicationException;

	Response updateStatus(MealMasterDto mealMasterDto);

	Response update(MealMasterDto mealMasterDto);

	Response getMealMasterDropDown(Integer organizationId,Integer unitId) throws ApplicationException;

	Response getAllMealMasterListPaginationList(MealMasterDto mealMasterDto) throws ApplicationException;

	Response getAllMealMasterListPaginationListCount(MealMasterDto mealMasterDto) throws ApplicationException;

}

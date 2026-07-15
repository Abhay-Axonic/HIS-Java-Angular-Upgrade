package com.param.global.master.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.master.dto.MealMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IMealMasterService {

	@Transactional
	Response saveMealMaster(MealMasterDto mealMasterDto) throws ApplicationException;

	@Transactional
	Response getAllMealMasterList(Integer organizationId,Integer unitId) throws ApplicationException;

	@Transactional
	Response getActiveMealMasterList(Integer organizationId,Integer unitId) throws ApplicationException;

	@Transactional
	Response updateStatus(MealMasterDto mealMasterDto);

	@Transactional
	Response update(MealMasterDto mealMasterDto);

	@Transactional
	Response getMealMasterDropDown(Integer organizationId,Integer unitId) throws ApplicationException;

	@Transactional
	Response getAllMealMasterListPaginationList(MealMasterDto mealMasterDto) throws ApplicationException;

	@Transactional
	Response getAllMealMasterListPaginationListCount(MealMasterDto mealMasterDto) throws ApplicationException;

}

package com.param.adt.master.unit.service;

import javax.transaction.Transactional;

import com.param.adt.master.unit.dto.MealPreferenceMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IMealPreferenceMasterService {
	

	Response saveMealPreferenceMaster(MealPreferenceMasterDto mealPreferenceMasterDto) throws ApplicationException;

	Response getMealPreferenceMasterById(int mealPrefernceId, int orgId, int unitId) throws ApplicationException;

	Response getMealPreferenceMasterList(MealPreferenceMasterDto mealPreferenceMasterDto) throws ApplicationException;

	Response updateNursingStationMaster(MealPreferenceMasterDto mealPreferenceMasterDto) throws ApplicationException;

	Response updateMealPreferenceMasterStatus(MealPreferenceMasterDto mealPreferenceMasterDto) throws ApplicationException;

	Response getMealPreferenceMasterCount(MealPreferenceMasterDto mealPreferenceMasterDto) throws ApplicationException;

	Response getActiveMealPreferenceMasterList(MealPreferenceMasterDto mealPreferenceMasterDto) throws ApplicationException;


}

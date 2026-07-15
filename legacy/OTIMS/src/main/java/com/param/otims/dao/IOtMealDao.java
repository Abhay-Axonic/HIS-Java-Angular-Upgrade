package com.param.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.OtMealDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IOtMealDao {

	Response saveOtMeal(OtMealDto mealDto)throws ApplicationException;

	Response getOtMeals(Integer postOperativeCareId)throws ApplicationException;

}

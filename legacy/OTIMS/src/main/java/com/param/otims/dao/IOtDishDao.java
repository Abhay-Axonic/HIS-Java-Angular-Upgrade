package com.param.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.OtDishDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IOtDishDao {

	Response saveOtDish(OtDishDto dishdto)throws ApplicationException;

	Response getOtDishData(Integer otMealId)throws ApplicationException;

}

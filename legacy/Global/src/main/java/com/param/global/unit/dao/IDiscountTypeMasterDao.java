package com.param.global.unit.dao;

import com.param.global.unit.dto.DiscountCategoryTypeMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDiscountTypeMasterDao {

	
	Response saveDiscountCategory(DiscountCategoryTypeMasterDto dto) throws ApplicationException;

	Response updateDiscountCategory(DiscountCategoryTypeMasterDto dto) throws ApplicationException;

	Response updateDiscountCategoryStatus(DiscountCategoryTypeMasterDto dto) throws ApplicationException;

	Response getDiscountCategoryList(DiscountCategoryTypeMasterDto dto) throws ApplicationException;

	Response getDiscountCategoryCount(DiscountCategoryTypeMasterDto dto) throws ApplicationException;

	Response getDiscountCategoryById(DiscountCategoryTypeMasterDto dto) throws ApplicationException;

	Response getDiscountCategoryExistOrNotByName(DiscountCategoryTypeMasterDto dto)throws ApplicationException;

	Response getDiscountCategoryExistOrNotByNameNotById(DiscountCategoryTypeMasterDto dto)throws ApplicationException;

}

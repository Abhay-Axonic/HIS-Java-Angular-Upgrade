package com.param.global.unit.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.global.unit.dto.DiscountCategoryTypeMasterDto;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface IDiscountTypeMasterService {

	
	Response saveDiscountCategory(DiscountCategoryTypeMasterDto dto) throws ApplicationException;

	Response updateDiscountCategory(DiscountCategoryTypeMasterDto dto)throws ApplicationException;

	Response updateDiscountCategoryStatus(DiscountCategoryTypeMasterDto dto)throws ApplicationException;

	Response getDiscountCategoryList(DiscountCategoryTypeMasterDto dto)throws ApplicationException;

	Response getDiscountCategoryCount(DiscountCategoryTypeMasterDto dto)throws ApplicationException;

	Response getDiscountCategoryById(DiscountCategoryTypeMasterDto dto)throws ApplicationException;

}

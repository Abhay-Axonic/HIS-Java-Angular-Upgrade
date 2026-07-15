package com.param.global.master.dao;

import com.param.global.common.Response;
import com.param.global.master.dto.DietTemplateDto;
import com.param.global.master.dto.DietTemplateMapperDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDietTemplateMapperDao {

	Response saveDietTemplateMapper(DietTemplateDto dietTemplateDto) throws ApplicationException;

	Response getAllDietTemplateMapper() throws ApplicationException;

	Response updateDietTemplateMapper(DietTemplateDto dietTemplateDto) throws ApplicationException;

	/*
	 * Response saveDietTemplateMapper(DietTemplateMapperDto
	 * dietTemplateMapperDto) throws ApplicationException;
	 */

	Response getDietTemplateMapperByDishAndFoodType(DietTemplateMapperDto dietTemplateMapperDto)
			throws ApplicationException;

	Response getActiveDietTemplateMapper() throws ApplicationException;

	Response updateStatus(DietTemplateMapperDto dietTemplateMapperDto);

}

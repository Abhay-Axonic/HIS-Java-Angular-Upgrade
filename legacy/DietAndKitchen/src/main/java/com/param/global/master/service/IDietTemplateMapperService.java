package com.param.global.master.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.master.dto.DietTemplateDto;
import com.param.global.master.dto.DietTemplateMapperDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDietTemplateMapperService {

	/**
	 * This method creates new Diet Template.
	 * 
	 * @param dietTemplateDto
	 * @return Response
	 */
	@Transactional
	Response saveDietTemplateMapper(DietTemplateDto dietTemplateDto) throws ApplicationException;

	/**
	 * This method updates the particular Diet Template by Id.
	 * 
	 * @param dietTemplateDto
	 * @return Response
	 */
	@Transactional
	Response updateDietTemplateMapper(DietTemplateDto dietTemplateDto) throws ApplicationException;

	/**
	 * This method returns brief Diet Template List.
	 * 
	 * @return Response
	 */

	@Transactional
	Response getDietTemplateList(Integer organizationId,Integer unitId) throws ApplicationException;

	/**
	 * This method returns particular Diet Template by Id.
	 * 
	 * @param dietTemplateId
	 * @return Response
	 */
	@Transactional
	Response getDietTemplateById(Integer dietTemplateId,Integer organizationId,Integer unitId) throws ApplicationException;

	/**
	 * This method returns list of All Diet Templates.
	 * 
	 * @return Response
	 */
	@Transactional
	Response getAllDietTemplateMapper(Integer organizationId,Integer unitId) throws ApplicationException;

	/**
	 * This method returns day wise nutrition value list by Diet Template Id.
	 * 
	 * @param dietTemplateId
	 * @return Response
	 */
	@Transactional
	Response getDayWiseNutritionList(Integer dietTemplateId) throws ApplicationException;
	
	@Transactional
	Response getDietTemplateByDietPrefIdsAndDietTypeId(Integer dietPrefId,Integer dietTypeId,Integer organizationId,Integer unitId) throws ApplicationException;
	
	

	/*------------------------Older APIs------------------------------------------------*/

	/*
	 * @Transactional Response saveDietTemplateMapper(DietTemplateMapperDto
	 * dietTemplateMapperDto) throws ApplicationException;
	 */

	@Transactional
	Response getDietTemplateMapperByDishAndFoodType(DietTemplateMapperDto dietTemplateMapperDto)
			throws ApplicationException;

	@Transactional
	Response getActiveDietTemplateMapper() throws ApplicationException;

	@Transactional
	Response updateStatus(DietTemplateMapperDto dietTemplateMapperDto);

}

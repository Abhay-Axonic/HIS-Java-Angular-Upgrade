package com.param.global.master.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.master.dto.NutritionalMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface INutritionalMasterService {

	@Transactional
	Response saveNutritionalMaster(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException;

	@Transactional
	Response getNutritionalMasterList(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException;

	@Transactional
	Response updateNutritionalMaster(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException;

	@Transactional
	Response getNutritionalMasterById(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException;

	@Transactional
	Response getAllNutritionalMasterList(Integer organizationId,Integer unitId, Integer offset, Integer noOfRecordsPerPage) throws ApplicationException;

	@Transactional
	Response getActiveNutritionalMasterList(Integer organizationId,Integer unitId) throws ApplicationException;

	@Transactional
	Response updateNutritionMasterStatus(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException;
	
	@Transactional
	Response getNutritionMasterCount(NutritionalMasterDto nutritionalMasterDto ) throws ApplicationException;

}

package com.param.global.master.dao;

import com.param.global.common.Response;
import com.param.global.master.dto.NutritionalMasterDto;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface INutritionalMasterDao {

	Response saveNutritionalMaster(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException;

	Response getNutritionByName(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException;

	Response getNutritionalMasterList(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException;

	Response getAllNutritionalMasterList(Integer organizationId,Integer unitId, Integer offset, Integer noOfRecordsPerPage) throws ApplicationException;

	Response getActiveNutritionalMasterList(Integer organizationId,Integer unitId) throws ApplicationException;

	Response updateNutritionalMaster(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException;

	Response getNutritionalMasterById(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException;

	Response updateNutritionMasterStatus(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException;

	Response checkNutritionByIdNotByName(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException;

	Response getNutritionMasterCount(NutritionalMasterDto nutritionalMasterDto)throws ApplicationException;

}

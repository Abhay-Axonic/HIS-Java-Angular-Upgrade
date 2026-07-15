package com.param.global.master.dao;

import com.param.global.common.Response;
import com.param.global.master.dto.DietTypeMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDietTypeMasterDao {

	Response saveDietTypeMaster(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException;

	Response getDietTypeByName(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException;

	Response getDietTypeMasterById(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException;

	Response updateDietTypeMaster(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException;

	Response updateDietTypeMasterStatus(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException;

	Response getDietTypeMasterList(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException;

	Response getAllDietTypeMasterList(Integer organizationId,Integer unitId) throws ApplicationException;

	Response getActiveDietTypeMasterList(Integer organizationId,Integer unitId) throws ApplicationException;

	Response getDietTypeMasterPaginationList(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException;

	Response getDietTypeMasterPaginationListCount(DietTypeMasterDto dietTypeMasterDto)throws ApplicationException;

}

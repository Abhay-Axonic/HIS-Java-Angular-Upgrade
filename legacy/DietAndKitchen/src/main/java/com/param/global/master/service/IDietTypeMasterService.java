package com.param.global.master.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.master.dto.DietTypeMasterDto;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDietTypeMasterService {

	@Transactional
	Response saveDietTypeMaster(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException;

	@Transactional
	Response getDietTypeMasterById(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException;

	@Transactional
	Response updateDietTypeMaster(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException;

	@Transactional
	Response updateDietTypeMasterStatus(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException;

	@Transactional
	Response getDietTypeMasterList(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException;

	@Transactional
	Response getAllDietTypeMasterList(Integer organizationId,Integer unitId) throws ApplicationException;

	@Transactional
	Response getActiveDietTypeMasterList(Integer organizationId,Integer unitId) throws ApplicationException;

	@Transactional
	Response getDietTypeMasterPaginationList(DietTypeMasterDto dietTypeMasterDto)throws ApplicationException;

	@Transactional
	Response getDietTypeMasterPaginationListCount(DietTypeMasterDto dietTypeMasterDto)throws ApplicationException;

}

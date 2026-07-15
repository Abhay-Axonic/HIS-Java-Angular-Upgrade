package com.param.global.master.service;

import javax.transaction.Transactional;



import com.param.global.common.Response;
import com.param.global.master.dto.KeyIngredientsMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IKeyIngredientsMasterService {

	@Transactional
	Response saveKeyIngredientsMaster(KeyIngredientsMasterDto keyIngredientsMasterDto) throws ApplicationException;

	@Transactional
	Response getUnitMasterDetails(Integer organizationId,Integer unitId) throws ApplicationException;

	@Transactional
	Response getKeyIngredientsMasterById(KeyIngredientsMasterDto keyIngredientsMasterDto) throws ApplicationException;

	@Transactional
	Response getKeyIngredientsDetailsById(KeyIngredientsMasterDto keyIngredientsMasterDto) throws ApplicationException;

	@Transactional
	Response getAllKeyIngredientsMaster(Integer organizationId,Integer unitId ) throws ApplicationException;

	@Transactional
	Response getAllKeyIngredientsMasterWithoutOrgId() throws ApplicationException;

	
	@Transactional
	Response getActiveKeyIngredientsMaster(Integer organizationId,Integer unitId ) throws ApplicationException;

	@Transactional
	Response updateKeyIngredientsMasterStatus(KeyIngredientsMasterDto keyIngredientsMasterDto)
			throws ApplicationException;

	@Transactional
	Response updateKeyIngredientsMaster(KeyIngredientsMasterDto keyIngredientsMasterDto) throws ApplicationException;

}

package com.param.global.master.dao;



import com.param.global.common.Response;
import com.param.global.master.dto.KeyIngredientsMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IKeyIngredientsMasterDao {

	Response getKeyIngredientsByName(KeyIngredientsMasterDto keyIngredientsMasterDto) throws ApplicationException;

	Response saveKeyIngredientsMaster(KeyIngredientsMasterDto keyIngredientsMasterDto) throws ApplicationException;

	Response getUnitMasterDetails(Integer organizationId,Integer unitId) throws ApplicationException;

	Response getKeyIngredientsMasterById(KeyIngredientsMasterDto keyIngredientsMasterDto) throws ApplicationException;

	Response getKeyIngredientsDetailsById(KeyIngredientsMasterDto keyIngredientsMasterDto) throws ApplicationException;

	Response getAllKeyIngredientsMaster(Integer organizationId,Integer unitId) throws ApplicationException;
	
	Response getAllKeyIngredientsMasterWithoutOrgId() throws ApplicationException;

	Response getActiveKeyIngredientsMaster(Integer organizationId,Integer unitId) throws ApplicationException;

	Response updateKeyIngredientsMasterStatus(KeyIngredientsMasterDto keyIngredientsMasterDto)
			throws ApplicationException;

	Response updateKeyIngredientsMaster(KeyIngredientsMasterDto keyIngredientsMasterDto) throws ApplicationException;

	/**
	 * BR-121: dedicated ownership check for keyIngredientId, used before
	 * returning its nutrition sub-list -- that endpoint's frontend never sends
	 * organizationId/unitId at all, so there is no existing scoped query to
	 * reuse (GET_KEY_INGREDIENTS_MASTER_BY_ID is close, but also requires
	 * status='A', wrong for a pure ownership check on a possibly-inactive row).
	 */
	boolean isKeyIngredientOwnedByOrgAndUnit(Integer keyIngredientId, Integer organizationId, Integer unitId)
			throws ApplicationException;

}

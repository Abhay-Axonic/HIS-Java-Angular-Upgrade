package com.param.CSSDMaster.dao;

import com.param.CSSDMaster.dto.SterilizationTypeMasterDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface ISterilizationTypeMasterDao {

	Response updateSterilizationType(SterilizationTypeMasterDto masterDto);
	
	Response updateSterilizationTypeStatus(SterilizationTypeMasterDto masterDto);

	Response getSterilizationTypeList(Integer organizationId,Integer unitId,Integer offset,Integer noOfRecordsPerPage);
	
	Response getActiveSterilizationTypeList(Integer organizationId,Integer unitId);

	Response createSterilizationType(SterilizationTypeMasterDto masterDto);

	Response getSterilizationTypesListCount(SterilizationTypeMasterDto sterilizationTypeMasterDto);

}

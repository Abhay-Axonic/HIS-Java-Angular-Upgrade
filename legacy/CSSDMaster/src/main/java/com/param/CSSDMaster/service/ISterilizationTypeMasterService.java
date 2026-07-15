package com.param.CSSDMaster.service;

import com.param.CSSDMaster.dto.SterilizationTypeMasterDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface ISterilizationTypeMasterService {

	Response updateMaster(SterilizationTypeMasterDto masterDto);
	
	Response updateMasterStatus(SterilizationTypeMasterDto masterDto);

	Response createMaster(SterilizationTypeMasterDto masterDto);

	Response getMasterList(Integer organizationId,Integer unitId,Integer offset,Integer noOfRecordsPerPage);
	
	Response getActiveMasterList(Integer organizationId,Integer unitId);

	Response getSterilizationTypesListCount(SterilizationTypeMasterDto sterilizationTypeMasterDto);

}

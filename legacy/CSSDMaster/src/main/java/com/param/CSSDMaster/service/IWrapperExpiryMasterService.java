package com.param.CSSDMaster.service;

import com.param.CSSDMaster.dto.WrapperExpiryMasterDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IWrapperExpiryMasterService {

	Response updateMaster(WrapperExpiryMasterDto masterDto);
	
	Response updateMasterStatus(WrapperExpiryMasterDto masterDto);

	Response createMaster(WrapperExpiryMasterDto masterDto);

	Response getMasterList(Integer organizationId,Integer unitId,Integer offset,Integer noOfRecordsPerPage);
	
	Response getItemList(Integer organizationId,Integer unitId);
	
	Response getActiveMasterList(Integer organizationId,Integer unitId);
	
	Response getActiveWrapperExpiryByCode(Integer organizationId,Integer unitId,String code);

	Response getWrapperListcount(WrapperExpiryMasterDto wrapperExpiryMasterDto);
}

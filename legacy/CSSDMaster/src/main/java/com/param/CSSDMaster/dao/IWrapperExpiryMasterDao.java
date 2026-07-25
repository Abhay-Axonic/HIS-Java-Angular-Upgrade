package com.param.CSSDMaster.dao;

import com.param.CSSDMaster.dto.WrapperExpiryMasterDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IWrapperExpiryMasterDao {

	Response updateWrapperExpiry(WrapperExpiryMasterDto masterDto);
	
	Response updateWrapperExpiryStatus(WrapperExpiryMasterDto masterDto);

	Response getWrapperExpiryList(Integer organizationId,Integer unitId,Integer offset,Integer noOfRecordsPerPage);
	
	Response getItemList(Integer organizationId,Integer unitId);
	
	Response getActiveWrapperExpiryList(Integer organizationId,Integer unitId);

	Response createWrapperExpiry(WrapperExpiryMasterDto masterDto);

	Response getActiveWrapperExpiryByCode(Integer organizationId,Integer unitId,String code);

	Response getWrapperListcount(WrapperExpiryMasterDto wrapperExpiryMasterDto);

	Response getWrapperExpiryByNameOrCode(WrapperExpiryMasterDto masterDto);

	Response getWrapperExpiryByNameOrCodeNotById(WrapperExpiryMasterDto masterDto);

}

package com.param.global.billing.dao;

import com.param.global.billing.dto.ServiceBasePriceMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IServiceBasePriceMasterDao {
	
	
	Response getServiceBasePriceList(ServiceBasePriceMasterDto serviceBasePriceMasterDto) throws ApplicationException;

	Response getServiceBasePriceListById(Integer serviceBasePriceId) throws ApplicationException;

	Response updateServiceBasePriceMaster(ServiceBasePriceMasterDto serviceBasePriceMasterDto) throws ApplicationException;

	Response updateServiceBasePriceMasterStatus(ServiceBasePriceMasterDto serviceBasePriceMasterDto) throws ApplicationException;

	Response countServiceBasePriceMaster(ServiceBasePriceMasterDto serviceBasePriceMasterDto) throws ApplicationException;

	Response getServiceBasePriceListSearch(ServiceBasePriceMasterDto serviceBasePriceMasterDto) throws ApplicationException;
}

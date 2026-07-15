package com.param.global.service;

import com.param.global.common.Response;
import com.param.global.dto.ServiceChargesReqDto;
import com.param.global.dto.VariablePricingDto;

import in.param.exception.ApplicationException;

public interface IServiceTarrifMasterService {

	Response saveServiceTarrifMaster(VariablePricingDto variablePricingDto) throws ApplicationException;
	
	Response getServiceTarrifMaster(VariablePricingDto variablePricingDto) throws ApplicationException;
	
	/*METHOD TO GET SERVICE PRICES BY VISIT TYPE & VARIABLE FACTOR*/
	Response listServiceMasterWithBasePriceByVisitType(Integer orgId, Integer unitId, Integer visitTypeId, Integer offset, Integer recordPerPage) throws ApplicationException;
	
	/*GET PRICE DETAILS BY SERVICE ID's*/
	Response getServiceCharges(ServiceChargesReqDto serviceChargesReqDto) throws ApplicationException;
}

package com.param.billing.unit.service;


import com.param.global.common.Response;
import com.param.service.dto.ChargeConversionReqDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")

public interface IChargeConversionService {

/*	@Transactional
	public  Response saveChargeConversionDetailsList(ChargeConversionDetailsDto chargeConversionMasterDto)throws ApplicationException;
*/	
	public  Response saveChargeConversionMasterList(ChargeConversionReqDto chargeConversionReqDto)throws ApplicationException;
	
	

}

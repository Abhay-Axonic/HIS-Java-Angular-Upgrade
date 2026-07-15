package com.param.billing.global.transaction.config.service;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.config.dto.UpdatePayeeInformationReqDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IUpdatePayeeInformationService {
	
	public Response getPayeeInfoByVisitTypeId(UpdatePayeeInformationReqDto reqDto)throws ApplicationException;
	
}

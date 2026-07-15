package com.param.global.otims.service;

import com.param.global.common.Response;
import com.param.global.otims.dto.AdditionalOtTimeDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAdditionalOtTimeService {

	Response saveAdditionalOtTime(AdditionalOtTimeDto additionalOtTimeDto)throws ApplicationException;

	Response updateAdditionalOtTime(AdditionalOtTimeDto additionalOtTimeDto)throws ApplicationException;

	Response getAdditionalOtTimeList(Integer orgId, Integer unitId)throws ApplicationException;

	Response updateStatusAdditionalOtTime(AdditionalOtTimeDto additionalOtTimeDto)throws ApplicationException;

	Response getAddtionalRate(AdditionalOtTimeDto additionalOtTimeDto)throws ApplicationException;

}

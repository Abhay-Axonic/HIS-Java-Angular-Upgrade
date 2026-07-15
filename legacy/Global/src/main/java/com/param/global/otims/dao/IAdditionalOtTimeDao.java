package com.param.global.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.AdditionalOtTimeDto;
import com.param.otims.model.AdditionalOtTime;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAdditionalOtTimeDao extends IGenericDao<AdditionalOtTime, Integer>{

	Character isAdditionalOtTimeExist(AdditionalOtTimeDto additionalOtTimeDto)throws ApplicationException;

	Response saveAdditionalOtTime(AdditionalOtTimeDto additionalOtTimeDto)throws ApplicationException;

	Character isAdditionalOtTimeExistNotById(AdditionalOtTimeDto additionalOtTimeDto)throws ApplicationException;

	Response updateAdditionalOtTime(AdditionalOtTimeDto additionalOtTimeDto)throws ApplicationException;

	Response getAdditionalOtTimeList(Integer orgId, Integer unitId)throws ApplicationException;

	Response updateStatusAdditionalOtTime(AdditionalOtTimeDto additionalOtTimeDto);

	Response getAddtionalRate(AdditionalOtTimeDto additionalOtTimeDto);

}

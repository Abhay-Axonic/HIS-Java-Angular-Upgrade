package com.param.bloodBank.dao;

import com.param.bloodBank.dto.BloodComponentSeparationDetailsDto;
import com.param.bloodBank.dto.BloodComponentSeparationDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IBloodComponentSeparationDao {

	Response saveBloodComponentSeparation(BloodComponentSeparationDto dto)throws ApplicationException;

	Response getComponentSeparationListForStockUpdation(BloodComponentSeparationDetailsDto dto)throws ApplicationException;

}

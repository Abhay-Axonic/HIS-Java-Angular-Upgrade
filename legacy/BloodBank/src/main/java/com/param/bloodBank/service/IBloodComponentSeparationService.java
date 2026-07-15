package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodComponentSeparationDetailsDto;
import com.param.bloodBank.dto.BloodComponentSeparationDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IBloodComponentSeparationService {

	Response saveBloodComponentSeparation(BloodComponentSeparationDto dto)throws ApplicationException;

	Response getComponentSeparationListForStockUpdation(BloodComponentSeparationDetailsDto dto)throws ApplicationException;

}

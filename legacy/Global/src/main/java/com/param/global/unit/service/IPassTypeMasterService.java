package com.param.global.unit.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.global.unit.dto.PassTypeMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IPassTypeMasterService {

	
	Response savePassTypeMaster(PassTypeMasterDto dto) throws ApplicationException;

	Response updateDiscountCategory(PassTypeMasterDto dto)throws ApplicationException;

	Response updatePassTypeMasterStatus(PassTypeMasterDto dto)throws ApplicationException;

	Response getPassTypeMasterList(PassTypeMasterDto dto)throws ApplicationException;

	Response getPassTypeMasterCount(PassTypeMasterDto dto)throws ApplicationException;

	Response getPassTypeMasterById(PassTypeMasterDto dto)throws ApplicationException;

	Response getActivePassTypeMasterList(PassTypeMasterDto dto)throws ApplicationException;

}

package com.param.global.unit.dao;

import com.param.global.unit.dto.PassTypeMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IPassTypeMasterDao {


	Response savePassTypeMaster(PassTypeMasterDto dto) throws ApplicationException;

	Response updateDiscountCategory(PassTypeMasterDto dto) throws ApplicationException;

	Response updatePassTypeMasterStatus(PassTypeMasterDto dto) throws ApplicationException;

	Response getPassTypeMasterList(PassTypeMasterDto dto) throws ApplicationException;

	Response getPassTypeMasterCount(PassTypeMasterDto dto) throws ApplicationException;

	Response getPassTypeMasterById(PassTypeMasterDto dto) throws ApplicationException;

	Response checkPassTypeMasterAlreadyExistOrNot(PassTypeMasterDto dto) throws ApplicationException;

	Response checkPassTypeNotById(PassTypeMasterDto dto)throws ApplicationException;

	Response getActivePassTypeMasterList(PassTypeMasterDto dto)throws ApplicationException;

}

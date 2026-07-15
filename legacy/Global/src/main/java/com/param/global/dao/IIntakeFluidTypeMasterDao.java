package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.IntakeFluidTypeMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IIntakeFluidTypeMasterDao {

	Response getIntakeFluidTypeMasterListById(IntakeFluidTypeMasterDto intakeFluidTypeMasterDto) throws ApplicationException;

}

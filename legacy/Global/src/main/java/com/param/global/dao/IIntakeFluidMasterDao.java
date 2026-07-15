package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.IntakeFluidMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})

public interface IIntakeFluidMasterDao {

	Response getIntakeFluidMasterList(IntakeFluidMasterDto intakeFluidMasterDto) throws ApplicationException;

}

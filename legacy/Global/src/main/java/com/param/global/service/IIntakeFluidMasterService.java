package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.unit.dto.IntakeFluidMasterDto;
import com.param.global.unit.dto.IntakeFluidTypeMasterDto;
import com.param.global.unit.dto.OutputTypeMasterDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IIntakeFluidMasterService {

	public Response getIntakeFluidMasterList(IntakeFluidMasterDto intakeFluidMasterDto) throws ApplicationException;

	public Response getIntakeFluidTypeMasterListById(IntakeFluidTypeMasterDto intakeFluidTypeMasterDto) throws ApplicationException;

	public Response getOutputFluidMasterList(OutputTypeMasterDto outputTypeMasterDto)throws ApplicationException;
	
	}



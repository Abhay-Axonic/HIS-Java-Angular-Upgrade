package com.param.global.dao;


import com.param.global.common.Response;
import com.param.global.unit.dto.OutputTypeMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IOutputFluidTypeMasterDao {

	public Response getOutputFluidMasterList(OutputTypeMasterDto outputTypeMasterDto) throws ApplicationException;
}

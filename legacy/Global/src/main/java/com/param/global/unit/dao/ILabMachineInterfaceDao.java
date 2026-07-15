package com.param.global.unit.dao;

import com.param.global.unit.dto.MedallRequestResponseDto;

import in.param.exception.ApplicationException;
import com.param.global.common.Response;

public interface ILabMachineInterfaceDao {

	public Response saveLabMachineInterface(MedallRequestResponseDto med)throws ApplicationException;

}

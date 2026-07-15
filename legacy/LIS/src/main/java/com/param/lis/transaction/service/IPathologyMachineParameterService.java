package com.param.lis.transaction.service;

import java.util.List;

import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.MachineParameterMpperDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IPathologyMachineParameterService {


	public Response saveMachineParameterMapping(MachineParameterMpperDto machineParameterMpperDto)
			throws ApplicationException;

	public Response getPathalogyMachineParameterList(
			MachineParameterMpperDto machineParameterMpperDto);

	public Response getPathalogyMachineParameterListByMachineId(MachineParameterMpperDto machineParameterMpperDto)throws ApplicationException;

	public Response updateMachineParaMapping(MachineParameterMpperDto machineParameterMpperDto)throws ApplicationException;

	public Response updatePathalogyMachineParameterStatus(MachineParameterMpperDto machineParameterMpperDto);

	
}

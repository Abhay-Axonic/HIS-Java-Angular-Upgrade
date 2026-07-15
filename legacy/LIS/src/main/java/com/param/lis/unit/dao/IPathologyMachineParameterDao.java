package com.param.lis.unit.dao;

import java.util.List;

import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.MachineParameterMpperDto;

import in.param.exception.ApplicationException;

public interface IPathologyMachineParameterDao {

	public Response saveMachineParameterMapping(MachineParameterMpperDto machineParameterMpperDto)
	throws ApplicationException;

	public Response getPathalogyMachineParameterList(MachineParameterMpperDto machineParameterMpperDto)throws ApplicationException;

	public Response getPathalogyMachineParameterListByMachineId(MachineParameterMpperDto machineParameterMpperDto);

	public Response updateMachineParaMapping(MachineParameterMpperDto machineParameterMpperDto);

	public Response updatePathalogyMachineParameterStatus(MachineParameterMpperDto machineParameterMpperDto);
	


	

}

package com.param.lis.unit.service;

import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.MachineMasterDto;
import com.param.lis.unit.dto.MachineParameterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IMachineParameterService {


	public Response saveMachineParameterMaster(MachineParameterDto machineParameterDto)
			throws ApplicationException;

	public Response getMachineParameterMasterById(Integer organisationId, Integer unitId,
			Integer machinParameterId) throws ApplicationException;

	public Response updateMachineParaMaster(MachineParameterDto machineParameterDto)
	throws ApplicationException;

	public Response activateInactivateMachineParaMaster(Integer organisationId,
			Integer machinParameterId, Character machineStatus) throws ApplicationException;

	public Response listMachineParaMaster(Integer organisationId, Integer unitId,
			Integer offset, Integer recordPerPage) throws ApplicationException;

	public Response getTotalMachineParaMasterRecord(Integer organisationId, Integer unitId)
			throws ApplicationException;

	public Response getActiveMachineParaList(MachineParameterDto machineParameterDto)
			throws ApplicationException;

	public Response getActiveMachineList(MachineParameterDto machineParameterDto)
			throws ApplicationException;

	public Response getActiveParaMachineList(MachineParameterDto machineParameterDto)
			throws ApplicationException;



}

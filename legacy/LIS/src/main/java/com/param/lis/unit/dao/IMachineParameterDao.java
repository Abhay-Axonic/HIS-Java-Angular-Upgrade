package com.param.lis.unit.dao;

import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.MachineParameterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IMachineParameterDao {

	
	Response saveMachineParameterMaster(MachineParameterDto machinePatraMstrDto)
			throws ApplicationException;

	Response getMachineParameterMasterById(Integer organisationId, Integer unitId, Integer machinParameterId)
	throws ApplicationException;

	Response updateMachineParaMaster(MachineParameterDto machineParameterDto)
			throws ApplicationException;

	Response activateInactivateMachineParaMaster(Integer organisationId, Integer machinParameterId,
			Character machineStatus) throws ApplicationException;

	Response listMachineParaMaster(Integer organisationId, Integer unitId, Integer offset,
			Integer recordPerPage) throws ApplicationException;

	Response getTotalMachineParaMasterRecord(Integer organisationId, Integer unitId)
			throws ApplicationException;

	Response getActiveMachineParaList(MachineParameterDto machineParameterDto)
			throws ApplicationException;

	Response getActiveMachineList(MachineParameterDto machineParameterDto)
			throws ApplicationException;

	Response getActiveParaMachineList(MachineParameterDto machineParameterDto)
			throws ApplicationException;

}

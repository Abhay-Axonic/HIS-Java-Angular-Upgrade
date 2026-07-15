package com.param.lis.unit.service;

import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.MachineMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IMachineMasterService {

public Response saveMachineMaster(MachineMasterDto machineMasterDto)
		throws ApplicationException;

public Response getMachineMasterById(Integer organisationId, Integer unitId,
		Integer machineId)throws ApplicationException;

public Response updateMachineMaster(MachineMasterDto machineMasterDto)
		throws ApplicationException;

public Response activateInactivateMachineMaster(Integer organisationId,
		Integer machineId, Character machineStatus)throws ApplicationException;

public Response listMachineMaster(Integer organisationId, Integer unitId,
		Integer offset, Integer recordPerPage)throws ApplicationException;

public Response getTotalMachineMasterRecord(Integer organisationId, Integer unitId)
		throws ApplicationException;

public Response getActiveMachineList(MachineMasterDto machineMasterDto)
		throws ApplicationException;

}

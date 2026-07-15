package com.param.adt.master.unit.dao;

import com.param.adt.master.unit.dto.NurseAllocationDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface INurseAllocationDao {

	Response getNurseAllocationByUserId(NurseAllocationDto nurseAllocationDto)throws ApplicationException;

	Response saveNurseAllocationMaster(NurseAllocationDto nurseAllocationDto)throws ApplicationException;

	Response getNurseAllocationMasterList(NurseAllocationDto nurseAllocationDto)throws ApplicationException;

	Response getNurseAllocationMasterByNameNotId(NurseAllocationDto nurseAllocationDto)throws ApplicationException;

	Response updateNurseAllocationMaster(NurseAllocationDto nurseAllocationDto)throws ApplicationException;

	Response updateNurseAllocationMasterStatus(NurseAllocationDto nurseAllocationDto)throws ApplicationException;

	Response getNurseAllocationMasterCount(NurseAllocationDto nurseAllocationDto)throws ApplicationException;

	Response getNurseAllocationMasterById(Integer nurseAllocationId, Integer organizationId, Integer unitId)throws ApplicationException;

	Response getActiveNurseAllocationMasterByWardId(Integer wardId, Integer organizationId, Integer unitId)throws ApplicationException;

	Response getActiveNurseAllocationMasterByNurseId(Integer nurseId, Integer organizationId, Integer unitId);

}

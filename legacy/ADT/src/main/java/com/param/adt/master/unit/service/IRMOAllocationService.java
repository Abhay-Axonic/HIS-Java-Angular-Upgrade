package com.param.adt.master.unit.service;

import javax.transaction.Transactional;

import com.param.adt.master.unit.dto.NurseAllocationDto;
import com.param.adt.master.unit.dto.RmoAllocationDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IRMOAllocationService {

	Response saveRmoAllocationMaster(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response getRmoAllocationMasterList(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response updateRmoAllocationMaster(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response updateRmoAllocationMasterStatus(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response getRmoAllocationMasterCount(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response getRmoAllocationMasterById(Integer rmoAllocationId, Integer organizationId, Integer unitId)throws ApplicationException;

	Response getActiveRmoAllocationMasterByWardId(Integer wardId, Integer organizationId, Integer unitId)throws ApplicationException;

	Response getActiveRmoAllocationMasterByDoctorId(Integer doctorId, Integer organizationId, Integer unitId)throws ApplicationException;

	Response getDoctorListByRmoallocationId(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response getActiveRmoAllocationMasterByRMODoctorId(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response getListOfDoctorMappedToRmo(RmoAllocationDto rmoAllocationDto)throws ApplicationException;


}

package com.param.adt.master.unit.dao;

import com.param.adt.master.unit.dto.RmoAllocationDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IRMOAllocationDao {

	Response getRmoAllocationByRmoDoctorId(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response saveRmoAllocationMaster(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response getRmoAllocationMasterList(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response getRmoAllocationMasterByNameNotId(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response updateRmoAllocationMaster(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response updateRmoAllocationMasterStatus(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response getRmoAllocationMasterCount(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response getRmoAllocationMasterById(Integer rmoAllocationId, Integer organizationId, Integer unitId)throws ApplicationException;

	Response getActiveRmoAllocationMasterByWardId(Integer wardId, Integer organizationId, Integer unitId)throws ApplicationException;

	Response getActiveRmoAllocationMasterByDoctorId(Integer doctorId, Integer organizationId, Integer unitId);

	Response getDoctorListByRmoallocationId(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response getActiveRmoAllocationMasterByRMODoctorId(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

	Response getListOfDoctorMappedToRmo(RmoAllocationDto rmoAllocationDto)throws ApplicationException;

}

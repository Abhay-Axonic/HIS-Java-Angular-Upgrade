package com.param.bloodBank.dao;

import com.param.bloodBank.dto.BloodComponentMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})

public interface IBloodComponentMasterDao {
	public Response saveBloodComponentMaster(BloodComponentMasterDto bloodComponentMasterDto) throws ApplicationException;
	
	public Response updateBloodComponentMaster(BloodComponentMasterDto bloodComponentMasterDto) throws ApplicationException;
	
	public Response updateBloodComponentMasterStatus(BloodComponentMasterDto bloodComponentMasterDto) throws ApplicationException;
	
	public Response getBloodComponentMasterList(BloodComponentMasterDto bloodComponentMasterDto) throws ApplicationException;
	
	public Response getBloodComponentMasterById(Integer bloodComponentId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveBloodComponentMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getBloodComponentMasterCount(BloodComponentMasterDto bloodComponentMasterDto) throws ApplicationException;
	
	public Response getBloodComponentMasterByName(BloodComponentMasterDto bloodComponentMasterDto) throws ApplicationException;
	
	public Response getBloodComponentMasterByNameNotId(BloodComponentMasterDto bloodComponentMasterDto) throws ApplicationException;
}

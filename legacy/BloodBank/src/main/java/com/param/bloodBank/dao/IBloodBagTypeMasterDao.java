package com.param.bloodBank.dao;

import com.param.bloodBank.dto.BloodBagTypeMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})

public interface IBloodBagTypeMasterDao {
	
	public Response saveBloodBagTypeMaster(BloodBagTypeMasterDto bloodBagTypeMasterDto) throws ApplicationException;
	
	public Response updateBloodBagTypeMaster(BloodBagTypeMasterDto bloodBagTypeMasterDto) throws ApplicationException;
	
	public Response updateBloodBagTypeMasterStatus(BloodBagTypeMasterDto bloodBagTypeMasterDto) throws ApplicationException;
	
	public Response getBloodBagTypeMasterList(BloodBagTypeMasterDto bloodBagTypeMasterDto) throws ApplicationException;
	
	public Response getBloodBagTypeMasterById(Integer bloodBagTypeId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveBloodBagTypeMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getBloodBagTypeMasterCount(BloodBagTypeMasterDto bloodBagTypeMasterDto) throws ApplicationException;
	
	public Response getBloodBagTypeMasterByName(BloodBagTypeMasterDto bloodBagTypeMasterDto) throws ApplicationException;
	
	public Response getBloodBagTypeMasterByNameNotId(BloodBagTypeMasterDto bloodBagTypeMasterDto) throws ApplicationException;
}
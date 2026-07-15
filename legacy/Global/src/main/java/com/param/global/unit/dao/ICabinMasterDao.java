package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.CoversheetAccessMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface ICabinMasterDao {
	
	public Response saveCabinMaster(CabinMasterDto cabinMasterDto) throws ApplicationException;
	
	public Response updateCabinMaster(CabinMasterDto cabinMasterDto) throws ApplicationException;
	
	public Response updateCabinMasterStatus(CabinMasterDto cabinMasterDto) throws ApplicationException;
	
	public Response getCabinMasterById(Integer cabinId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getCabinMasterList(CabinMasterDto cabinMasterDto) throws ApplicationException;
	
	public Response getCabinMasterCount(CabinMasterDto cabinMasterDto) throws ApplicationException;
	
	public Response getActiveCabinMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getCabinMasterByName(CabinMasterDto cabinMasterDto) throws ApplicationException;
	
	public Response getCabinMasterByNameNotId(CabinMasterDto cabinMasterDto) throws ApplicationException;

	public Response getCoversheetAccessMaster(CoversheetAccessMasterDto coversheetAccessMasterDto) throws ApplicationException;
}

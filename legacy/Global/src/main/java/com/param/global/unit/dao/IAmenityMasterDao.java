package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.AmenityMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IAmenityMasterDao {
	
	
	Response saveAmenityMaster(AmenityMasterDto amenityMasterDto) throws ApplicationException;

	Response updateAmenityMaster(AmenityMasterDto amenityMasterDto) throws ApplicationException;

	Response updateAmenityMasterStatus(AmenityMasterDto amenityMasterDto) throws ApplicationException;

	Response getAmenityMasterList(AmenityMasterDto amenityMasterDto) throws ApplicationException;

	Response getActiveAmenityMasterList(AmenityMasterDto amenityMasterDto) throws ApplicationException;

	Response getAmenityMasterListCount(AmenityMasterDto amenityMasterDto) throws ApplicationException;

	Response getAmenityMasterListById(Integer amenityId) throws ApplicationException; 
	
	Response getAmenityMasterListByName(AmenityMasterDto amenityMasterDto) throws ApplicationException;
	
	Response getAmenityMasterListByNameNotById(AmenityMasterDto amenityMasterDto) throws ApplicationException;
}

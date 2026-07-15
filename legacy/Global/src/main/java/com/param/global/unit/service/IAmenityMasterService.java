package com.param.global.unit.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.unit.dto.AmenityMasterDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings("rawtypes")
public interface IAmenityMasterService {

	
	Response saveAmenityMaster(AmenityMasterDto amenityMasterDto) throws ApplicationException;

	Response updateAmenityMaster(AmenityMasterDto amenityMasterDto) throws ApplicationException;

	Response updateAmenityMasterStatus(AmenityMasterDto amenityMasterDto) throws ApplicationException;

	Response getAmenityMasterList(AmenityMasterDto amenityMasterDto) throws ApplicationException;

	Response getActiveAmenityMasterList(AmenityMasterDto amenityMasterDto) throws ApplicationException;

	Response getAmenityMasterListCount(AmenityMasterDto amenityMasterDto) throws ApplicationException;

	Response getAmenityMasterListById(Integer amenityId) throws ApplicationException; 

}

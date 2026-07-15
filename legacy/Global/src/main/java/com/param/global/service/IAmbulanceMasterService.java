package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.AmbulanceMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IAmbulanceMasterService {
	
	@Transactional
	Response saveAmbulanceMasterData(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException;

	@Transactional
	Response getAmbulanceMasterDataList(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException;

	@Transactional
	Response getAmbulanceMasterDataActiveList(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException;

	@Transactional
	Response getSingleAmbulanceMasterData(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException;

	@Transactional
	Response updateAmbulanceMasterData(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException;

	@Transactional
	Response changeAmbulanceMasterDataStatus(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException;

	
	



}


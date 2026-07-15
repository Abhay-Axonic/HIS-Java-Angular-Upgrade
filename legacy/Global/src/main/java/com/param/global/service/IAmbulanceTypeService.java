package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.AmbulanceTypeDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IAmbulanceTypeService {
	@Transactional
	Response addAmbulanceType(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException;

	@Transactional
	Response getAmbulanceTypeList(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException;

	@Transactional
	Response getAmbulanceTypeActiveList(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException;
	
	@Transactional
	Response getSingleAmbulanceType(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException;

	@Transactional
	Response updateAmbulanceType(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException;

	@Transactional
	Response changeAmbulanceStatus(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException;




}


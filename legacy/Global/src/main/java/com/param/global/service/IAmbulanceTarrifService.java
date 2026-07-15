package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.AmbulanceTarrifDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IAmbulanceTarrifService {
	@Transactional
	Response saveAmbulanceTarrif(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;

	@Transactional
	Response getAmbulanceTarrifList(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;

	@Transactional
	Response getAmbulanceTarrifActiveList(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;

	@Transactional
	Response getSingleAmbulanceTarrif(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;

	@Transactional
	Response updateAmbulanceTarrif(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;

	@Transactional
	Response changeAmbulanceTarrifStatus(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;

	



}


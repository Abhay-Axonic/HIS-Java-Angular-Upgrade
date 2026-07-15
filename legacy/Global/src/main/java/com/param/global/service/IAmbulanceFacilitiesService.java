package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.AmbulanceFacilitiesDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IAmbulanceFacilitiesService {
	@Transactional
	Response saveAmbulanceFacilities(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException;

	@Transactional
	Response getAmbulanceFacilitiesList(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException;

	@Transactional
	Response getAmbulanceFacilitiesActiveList(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException;

	@Transactional
	Response getSingleAmbulanceFacilities(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException;

	@Transactional
	Response updateAmbulanceFacilities(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException;

	@Transactional
	Response changeAmbulanceFacilitiesStatus(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException;


	



}


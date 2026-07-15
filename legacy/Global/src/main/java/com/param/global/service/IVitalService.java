package com.param.global.service;

import com.param.global.common.Response;
import com.param.global.dto.TVitalDto;
import com.param.global.dto.VitalSearchDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IVitalService {
	
	public Response saveVitals(TVitalDto tVitalDto)throws ApplicationException;
	public Response getVitalsByPatient(VitalSearchDto vitalSearchDto)throws ApplicationException;

}

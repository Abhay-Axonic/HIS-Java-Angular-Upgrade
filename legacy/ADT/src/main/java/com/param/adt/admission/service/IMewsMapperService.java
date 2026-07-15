package com.param.adt.admission.service;

import javax.transaction.Transactional;

import com.param.adt.admission.dto.MewsMapperDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})


	
public interface IMewsMapperService {
	
	public Response saveMewsMapper(MewsMapperDto mewsMapperDto) throws ApplicationException;
	
	public Response getMewsMapperList(Integer patientId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getCountOnMewsMapper(Integer patientId, Integer organizationId, Integer unitId) throws ApplicationException;

	public Response getListOfAllergies(Integer patientId, Integer organizationId, Integer unitId) throws ApplicationException;

	public Response getListOfOrderedProcedure(Integer patientId, Integer organizationId, Integer unitId) throws ApplicationException;

}

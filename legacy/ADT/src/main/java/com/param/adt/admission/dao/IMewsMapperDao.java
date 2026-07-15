package com.param.adt.admission.dao;

import com.param.adt.admission.dto.MewsMapperDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
public interface IMewsMapperDao {
	public Response saveMewsMapper(MewsMapperDto mewsMapperDto) throws ApplicationException;
	public Response getMewsMapperList(Integer patientId, Integer organizationId, Integer unitId) throws ApplicationException;
	public Response getCountOnMewsMapper(Integer patientId, Integer organizationId, Integer unitId) throws ApplicationException;
	public Response getListOfAllergies(Integer patientId, Integer organizationId, Integer unitId) throws ApplicationException;
	public Response getListOfOrderedProcedure(Integer patientId, Integer organizationId, Integer unitId) throws ApplicationException;

}

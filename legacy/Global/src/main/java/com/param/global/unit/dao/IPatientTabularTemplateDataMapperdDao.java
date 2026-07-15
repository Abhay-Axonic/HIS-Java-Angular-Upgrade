package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.PatientTabularTemplateDataMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
public interface IPatientTabularTemplateDataMapperdDao {

	public Response savePatientTabularTemplateDataMapper(PatientTabularTemplateDataMapperDto patientTabularTemplateDataMapperDto)throws ApplicationException;

	public Response getPatientTabularTemplateDataMapperById(Integer tabularTemplateId, Integer organizationId,Integer unitId , Integer patientId ,Integer templateTypeId)throws ApplicationException;

}

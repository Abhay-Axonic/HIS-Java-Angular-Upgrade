package com.param.global.unit.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.unit.dto.PatientTabularTemplateDataMapperDto;
import com.param.global.unit.dto.TabularTemplateDataMapperDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings({"rawtypes"})
public interface ITabularTemplateDataMapperService {

	public Response saveTabularTemplateDataMapper(TabularTemplateDataMapperDto tabularTemplateDataMapperDto)throws ApplicationException;

	public Response updateTabularTemplateDataMapper(TabularTemplateDataMapperDto tabularTemplateDataMapperDto)throws ApplicationException;

	public Response getTabularTemplateDataMapperById(Integer tabularTemplateId, Integer organizationId, Integer unitId)throws ApplicationException;

	public Response savePatientTabularTemplateDataMapper(PatientTabularTemplateDataMapperDto patientTabularTemplateDataMapperDto)throws ApplicationException;

	public Response getPatientTabularTemplateDataMapperById(Integer tabularTemplateId, Integer organizationId, Integer unitId , Integer patientId ,Integer templateTypeId)throws ApplicationException;

}

package com.param.global.unit.service;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;
import com.param.global.common.Response;
import com.param.global.unit.dto.TemplateDiagnosisMapperDto;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface ITemplateDiagnosisMapperService {

	public Response saveTemplateDiagnosisMapper(TemplateDiagnosisMapperDto templateDiagnosisMapperDto) throws ApplicationException;
	
	public Response updateTemplateDiagnosisMapper(TemplateDiagnosisMapperDto templateDiagnosisMapperDto) throws ApplicationException;
	
	public Response updateTemplateDiagnosisMapperStatus(Integer templateId, Integer organisationId, Integer unitId, Character status) throws ApplicationException;
	
	public Response getTemplateDiagnosisMapperlist(Integer offset, Integer noOfRecordsPerPage) throws ApplicationException;
	
	public Response getTemplateDiagnosisMapperById(Integer templateId, Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveTemplateDiagnosisMapper(TemplateDiagnosisMapperDto templateDiagnosisMapperDto) throws ApplicationException;
	
	public Response checkActiveTemplateDiagnosisMapperAvaiable(TemplateDiagnosisMapperDto templateDiagnosisMapperDto) throws ApplicationException;
	
	public Response getTemplateDiagnosisMapperDatailedList(Integer templateId, Integer organisationId, Integer unitId) throws ApplicationException;

	public Response getTemplateDiagnosisMapperCount() throws ApplicationException;
	
	public Response getTemplateList(Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response getListOfDiagnosis(Integer organisationId, Integer unitId) throws ApplicationException;

}

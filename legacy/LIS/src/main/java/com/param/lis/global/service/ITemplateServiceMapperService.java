package com.param.lis.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.lis.global.dto.TemplateServiceMapperDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings({"rawtypes"})
public interface ITemplateServiceMapperService {

	public Response saveTemplateServiceMapper(TemplateServiceMapperDto templateServiceMapperDto) throws ApplicationException;
	
	public Response updateTemplateServiceMapper(TemplateServiceMapperDto templateServiceMapperDto) throws ApplicationException;
	
	public Response updateTemplateServiceMapperStatus(Integer serviceId, Integer organizationId, Integer unitId, Character status) throws ApplicationException;
	
	public Response getTemplateServiceMapperlist(TemplateServiceMapperDto templateServiceMapperDto) throws ApplicationException;
	
	public Response getTemplateServiceMapperCount(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveTemplateServiceMapper(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getTemplateServiceMapperById(Integer serviceId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response checkTemplateServiceMapperAvailable(TemplateServiceMapperDto templateServiceMapperDto) throws ApplicationException;
	
	public Response getDetailedServiceList(Integer serviceId, Integer organizationId, Integer unitId) throws ApplicationException;

}

package com.param.lis.global.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.lis.global.dto.TemplateServiceMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
public interface ITemplateServiceMapperDao {
public Response saveTemplateServiceMapper(TemplateServiceMapperDto templateServiceMapperDto) throws ApplicationException;
	
	public Response updateTemplateServiceMapper(List<TemplateServiceMapperDto> listTemplateServiceMapperDto) throws ApplicationException;
	
	public Response updateTemplateServiceMapperStatus(Integer templateId, Integer organizationId, Integer unitId, Character status) throws ApplicationException;
	
	public Response getTemplateServiceMapperlist(TemplateServiceMapperDto templateServiceMapperDto) throws ApplicationException;
	
	public Response getTemplateServiceMapperCount(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveTemplateServiceMapper(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getTemplateServiceMapperById(Integer templateId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response checkTemplateServiceMapperAvailable(TemplateServiceMapperDto templateServiceMapperDto) throws ApplicationException;
	
	public Response getDetailedServiceList(Integer templateId, Integer organizationId, Integer unitId) throws ApplicationException;


}

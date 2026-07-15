package com.param.global.unit.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.unit.dto.ICPCTemplateMappingDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings({"rawtypes"})

public interface IICPCTemplateMappingService {

	public Response saveICPCTemplateMapping(ICPCTemplateMappingDto iCPCTemplateMappingDto) throws ApplicationException;

	public Response updateICPCTemplateMapping(ICPCTemplateMappingDto iCPCTemplateMappingDto) throws ApplicationException;
	
	public Response getICPCTemplateMappingById(ICPCTemplateMappingDto iCPCTemplateMappingDto) throws ApplicationException;
	
	public Response updateICPCTemplateMappingStatus(ICPCTemplateMappingDto iCPCTemplateMappingDto) throws ApplicationException;

	public Response getICPCTemplateMappingList(ICPCTemplateMappingDto iCPCTemplateMappingDto) throws ApplicationException;

	public Response getListOfICPCSystems() throws ApplicationException;

	public Response getDetailedListOfICPCSystems(ICPCTemplateMappingDto iCPCTemplateMappingDto) throws ApplicationException;

	public Response getDetailedListOfICPCSystemsDescription(ICPCTemplateMappingDto iCPCTemplateMappingDto) throws ApplicationException;

	public Response getICPCTemplateMappingCount(ICPCTemplateMappingDto iCPCTemplateMappingDto) throws ApplicationException;
	
	public Response getICPCSystemsCodesBySystemId(ICPCTemplateMappingDto iCPCTemplateMappingDto) throws ApplicationException;

	public Response getTemplateListForICPC(ICPCTemplateMappingDto iCPCTemplateMappingDto) throws ApplicationException;

	

}

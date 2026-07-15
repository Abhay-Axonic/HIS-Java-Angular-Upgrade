package com.param.adt.discharge.service;

import java.util.List;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;

import com.param.adt.discharge.dto.DischargeSummaryTemplateDto;
import com.param.global.common.Response;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IDischargeSummaryTemplateService {

	public Response saveDischargeSummaryTemplate(DischargeSummaryTemplateDto dischargeSummaryTemplateDto) throws ApplicationException;
	
	public Response updateDischargeSummaryTemplate(DischargeSummaryTemplateDto dischargeSummaryTemplateDto) throws ApplicationException;
	
	public Response updateDischargeSummaryTemplateStatus(DischargeSummaryTemplateDto dischargeSummaryTemplateDto) throws ApplicationException;
	
	public Response getDischargeSummaryTemplateById(Integer dischargeSummaryTempId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getDischargeSummaryTemplateMasterList(DischargeSummaryTemplateDto dischargeSummaryTemplateDto) throws ApplicationException;
	
	public Response getDischargeSummaryTemplateCount(Integer organizationId, Integer unitId, String searchKeyword) throws ApplicationException;
	
	public Response getActiveDischargeSummaryTemplate(Integer organizationId, Integer unitId) throws ApplicationException;

	public Response saveListDischargeSummaryTemplate(List<DischargeSummaryTemplateDto> dischargeSummaryTemplateDto);

	public Response getDischargeTempDeptListById(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)throws ApplicationException;


}

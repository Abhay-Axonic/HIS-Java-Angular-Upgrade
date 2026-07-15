package com.param.adt.discharge.dao;

import java.util.List;

import in.param.exception.ApplicationException;

import com.param.adt.discharge.dto.DischargeSummaryTemplateDto;
import com.param.global.common.Response;

@SuppressWarnings({"rawtypes"})
public interface IDischargeSummaryTemplateDao {

	public Response saveDischargeSummaryTemplate(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)throws ApplicationException;
	
	public Response updateDischargeSummaryTemplate(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)throws ApplicationException;
	
	public Response updateDischargeSummaryTemplateStatus(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)throws ApplicationException;
	
	public Response getDischargeSummaryTemplateById(Integer dischargeSummaryTempId, Integer organizationId, Integer unitId)throws ApplicationException;
	
	public Response getDischargeSummaryTemplateMasterList(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)throws ApplicationException;
	
	public Response getDischargeSummaryTemplateCount(Integer organizationId, Integer unitId, String searchKeyword)throws ApplicationException;
	
	public Response getActiveDischargeSummaryTemplate(Integer organizationId, Integer unitId)throws ApplicationException;
	
	public Response getDischargeSummaryTemplateByName(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)throws ApplicationException;
	
	public Response getDischargeSummaryTemplateByNameNotId(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)throws ApplicationException;

	public Response getDischargeSummaryTemplateByName(List<DischargeSummaryTemplateDto> dischargeSummaryTemplateDto);

	public Response saveListDischargeSummaryTemplate(List<DischargeSummaryTemplateDto> dischargeSummaryTemplateDto);
	

}

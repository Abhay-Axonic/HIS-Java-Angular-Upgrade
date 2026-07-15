package com.param.adt.discharge.dao;

import in.param.exception.ApplicationException;

import com.param.adt.discharge.dto.DischargeSummaryTemplateMapperDto;
import com.param.global.common.Response;
@SuppressWarnings({"rawtypes"})
public interface IDischargeSummaryTemplateMapperDao {

	public Response saveDischargeSummaryFieldDetails(DischargeSummaryTemplateMapperDto dischargeSummaryTemplateMapperDto)throws ApplicationException;
	
	public Response getDetailedDischargeSummaryTemplateList(Integer dischargeSummaryTempId, Integer organizationId, Integer unitId)throws ApplicationException;

	public Response updateDischargeSummaryFieldDetails(DischargeSummaryTemplateMapperDto dto);

}

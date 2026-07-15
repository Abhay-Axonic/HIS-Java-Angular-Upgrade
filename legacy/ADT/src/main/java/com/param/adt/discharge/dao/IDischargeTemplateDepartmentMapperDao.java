package com.param.adt.discharge.dao;

import com.param.adt.discharge.dto.DischargeSummaryTemplateDto;
import com.param.adt.discharge.dto.DischargeTemplateDepartmentMapperDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IDischargeTemplateDepartmentMapperDao {
	
	public Response saveDischargeTemplateDepartmentMapper(DischargeTemplateDepartmentMapperDto deptDto)throws ApplicationException;

	public Response getDischargeTempDeptListById(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)throws ApplicationException;

	public Response updateDischargeTemplateDepartmentMapper(DischargeTemplateDepartmentMapperDto deptDto)throws ApplicationException;


}

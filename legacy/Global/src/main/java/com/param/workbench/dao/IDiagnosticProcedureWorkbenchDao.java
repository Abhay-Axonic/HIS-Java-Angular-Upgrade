package com.param.workbench.dao;

import com.param.global.common.Response;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.TemplateReqDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDiagnosticProcedureWorkbenchDao {
	
	
	Response getProcedureworkbench(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException;

	Response saveDiagnosticProcedureMapper(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException;

	Response getProcedureResultEntry(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException;

	Response getTemplateByTemplateTypeId(TemplateReqDto templateReqDto)throws ApplicationException;

	Response getProcedureResultValidation(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	Response getProcedureWorkListForPrinting(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	Response printDiagnosisReport(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

}

package com.param.workbench.dao;

import java.io.ByteArrayOutputStream;

import com.param.global.common.Response;
import com.param.global.dto.RadiologyOrdersListDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IRISWorkbenchDao{

	
	Response getRadiologyWorkbenchWorklist(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

//	Response acceptRadiologyRequest(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	Response saveRadiologyMapper(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	Response getRadiologyResultEntrylist(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	Response getradiologyReportReleaseWorkList(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	Response getradiologyWorkListForPrinting(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;
	
	ByteArrayOutputStream printRadiologyReport(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	ByteArrayOutputStream labPrintReport(RadiologyOrdersListDto dto)throws ApplicationException;

	Response printReportTest(RadiologyOrdersListDto dto)throws ApplicationException;

	Response printReportTestLab(RadiologyOrdersListDto dto)throws ApplicationException;

	Response getRadiologyWorkbenchWorklistPagination(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	Response updateTemplateTransForRadiologyReportUrl(RadiologyOrdersListDto radiologyOrdersListDto, String str)throws ApplicationException;

    Response getPayee(RadiologyOrdersListDto orderDto)throws ApplicationException;

	Response checkOutStandingAmount(Integer encounterId)throws ApplicationException;
}

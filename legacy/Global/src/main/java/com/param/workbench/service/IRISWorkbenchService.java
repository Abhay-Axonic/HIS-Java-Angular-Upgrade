package com.param.workbench.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.TemplateTransactionDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IRISWorkbenchService {
	
	Response getRadiologyWorkbenchWorklist(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException;

	/*Response acceptRadiologyRequest(RadiologyOrdersListDto radiologyOrdersListDto)  throws ApplicationException;

	Response rejectRadiologyRequest(RadiologyOrdersListDto radiologyOrdersListDto)  throws ApplicationException;
*/
	Response getRadiologyResultEntrylist(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException;

	Response getradiologyReportReleaseWorkList(RadiologyOrdersListDto radiologyOrdersListDto)  throws ApplicationException;

	Response getradiologyWorkListForPrinting(RadiologyOrdersListDto radiologyOrdersListDto)  throws ApplicationException;

	ByteArrayOutputStream printRadiologyReport(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	ByteArrayOutputStream labPrintReport(RadiologyOrdersListDto dto)throws ApplicationException;

	Response printReportTest(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	Response printReportTestLab(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	Response getRadiologyWorkbenchWorklistPagination(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	Response sendRadiologyReportOnMail(List<RadiologyOrdersListDto> list, String str)throws ApplicationException, Exception;

	Response updateTemplateTransForRadiologyReportUrl(RadiologyOrdersListDto radiologyOrdersListDto, String str)throws ApplicationException, Exception;

	Response getOrganiazationFolderPath(Integer orgId)throws ApplicationException, Exception;

	Response updateRISPdfPathByTemplateTransactionId(String docPath, TemplateTransactionDto dto)throws ApplicationException, Exception;

	Response getConnectionStringForMyLife()throws ApplicationException, Exception;

	Response getMyLifeDetail(Integer id)throws ApplicationException, Exception;

	Response checkDueAmount(RadiologyOrdersListDto orderDto)throws ApplicationException, Exception;
}

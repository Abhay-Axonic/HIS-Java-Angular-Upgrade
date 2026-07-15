package com.param.lis.reports.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.reports.service.IBiochemReportService;
import com.param.lis.transaction.dto.LabResultMasterDto;

@RestController
@SuppressWarnings(
{ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/Reports")
public class BioChemLabReportController implements ICommonConstants, IError
{
	@Autowired
	private IBiochemReportService iBiochemReportService;
	@Autowired
	private ServletContext servletContext;
	
	final static Logger logger = Logger.getLogger(BioChemLabReportController.class);
	
	@RequestMapping(value = "/Print", method = RequestMethod.POST)
	public Response printBiochemistryReports(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception
	{
		Response<Document, Integer> response = null;
		try
		{
			if (servletRequest.getParameter("nameOfReport") != null)
			{
				String contextPath = servletContext.getRealPath("/");
				servletResponse.setContentType("application/pdf");
				ObjectMapper mapper = new ObjectMapper();
				String labTestResultObj = servletRequest.getParameter("nameOfReport");
				//System.out.println("labTestResultObj"+labTestResultObj);
				List<LabResultMasterDto> listLabResultMasterDto = mapper.readValue(labTestResultObj, new TypeReference<List<LabResultMasterDto>>(){});
				response = iBiochemReportService.printBiochemistryReports(listLabResultMasterDto,servletRequest,servletResponse, contextPath);
				 return response;
			}
	
		 return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	@RequestMapping(value = "/coverSheetPrint", method = RequestMethod.POST)
	public Response printCoverSheetReports(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
	{
		Response<Document, Integer> response = null;
		try
		{
			if (servletRequest.getParameter("nameOfReport") != null)
			{
				String contextPath = servletContext.getRealPath("/");
				servletResponse.setContentType("application/pdf");
				Integer labResultId = Integer.parseInt(servletRequest.getParameter("labResultId"));
				//System.out.println("labTestResultObj"+labResultId);
				response = iBiochemReportService.printCoverSheetReports(labResultId,servletRequest,servletResponse, contextPath);
				 return response;
			}
	
		 return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
}

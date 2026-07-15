package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.ReportDetailsDto;
import com.param.global.unit.service.IReportDetailsService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global/unit")
@SuppressWarnings({"rawtypes"})
public class ReportDetailsController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(ReportDetailsController.class);

	@Autowired
	private IReportDetailsService iReportDetailsService;
	
	@RequestMapping(value = "/getReportDetailsList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getReportDetailsList(@RequestBody ReportDetailsDto reportDetailsDto){
		
		try {
			return iReportDetailsService.getReportDetailsList(reportDetailsDto);
			
		} catch (Exception e) {
		logger.error("getReportDetailsList failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getReportDetailsByReportId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getReportDetailsByReportId(@RequestBody ReportDetailsDto reportDetailsDto){
		
		try {
			return iReportDetailsService.getReportDetailsByReportId(reportDetailsDto);
			
		} catch (Exception e) {
		logger.error("getReportDetailsByReportId failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getReportGroupHeaderMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getReportGroupHeaderMaster(@RequestBody ReportDetailsDto reportDetailsDto){
		
		try {
			return iReportDetailsService.getReportGroupHeaderMaster(reportDetailsDto);
			
		} catch (Exception e) {
		logger.error("getReportGroupHeaderMaster failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	
	@RequestMapping(value = "/getReportGroupHeaderForUserRoleManagement", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getReportGroupHeaderForUserRoleManagement(@RequestBody ReportDetailsDto reportDetailsDto){
		
		try {
			return iReportDetailsService.getReportGroupHeaderForUserRoleManagement(reportDetailsDto);
			
		} catch (Exception e) {
		logger.error("getReportGroupHeaderForUserRoleManagement failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/searchMISReport", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchMISReport(@RequestBody ReportDetailsDto reportDetailsDto){
		
		try {
			return iReportDetailsService.searchMISReport(reportDetailsDto);
			
		} catch (Exception e) {
		logger.error("searchMISReport failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getMISReportFormattesMasterforShowReports", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getMISReportFormattesMasterforShowReports(@RequestBody ReportDetailsDto reportDetailsDto){
	
		
		try {
			return iReportDetailsService.getMISReportFormattesMasterforShowReports(reportDetailsDto);
			
		} catch (Exception e) {
		logger.error("getMISReportFormattesMasterforShowReports failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

}

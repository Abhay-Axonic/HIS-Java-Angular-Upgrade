package com.param.global.nabhprocess.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.nabhprocess.dto.ReportedNabhMapperDto;
import com.param.global.nabhprocess.service.IReportedNabhMapperService;
import com.param.global.unit.dto.ReportedIncidentMapperDto;
import com.param.global.unit.service.IReportedIncidentMapperService;

@RestController
@SuppressWarnings({ "rawtypes" })
@RequestMapping("/api/unit")
public class ReportedNabhMapperController  implements ICommonConstants{
	private static final Logger logger = Logger.getLogger(ReportedNabhMapperController.class);

	@Autowired
	private IReportedNabhMapperService iReportedIncidentMapperService;

	@RequestMapping(value = "/saveReportedNabhMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveReportedNabhMapper(@RequestBody ReportedNabhMapperDto reportedNabhMapperDto) {
		try {
			return iReportedIncidentMapperService.saveReportedNabhMapper(reportedNabhMapperDto);

		} catch (Exception e) {
			logger.error("saveReportedNabhMapper failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getReportedNabhMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getReportedNabhMapperList(@RequestBody ReportedNabhMapperDto reportedNabhMapperDto) {
		try {
			return iReportedIncidentMapperService.getReportedNabhMapperList(reportedNabhMapperDto);

		} catch (Exception e) {
			logger.error("getReportedNabhMapperList failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}

//	@RequestMapping(value = "/getReportedIncidentMapperById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)  
//	public Response getReportedIncidentMapperById(@RequestBody ReportedIncidentMapperDto reportedIncidentMapperDto) {
//		try {
//			return iReportedIncidentMapperService.getReportedIncidentMapperById(reportedIncidentMapperDto);
//
//		} catch (Exception e) {
//			
//			return new Response<>(ERROR, null, null, null, null);
//		}
//	}
//	
//	@RequestMapping(value = "/getTemplateByReportedIncidentId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public Response getTemplateByReportedIncidentId(@RequestBody ReportedIncidentMapperDto reportedIncidentMapperDto) {
//		try {
//			return iReportedIncidentMapperService.getTemplateByReportedIncidentId(reportedIncidentMapperDto);
//		} catch (Exception e) {
//			
//			return new Response<>(ERROR, null, null, null, null);
//		}
//	}
//	
	@RequestMapping(value = "/submitReportedNabhMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response submitReportedNabhMapper(@RequestBody ReportedNabhMapperDto reportedNabhMapperDto) {
		try {
			return iReportedIncidentMapperService.submitReportedNabhMapper(reportedNabhMapperDto);

		} catch (Exception e) {
			logger.error("submitReportedNabhMapper failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}

}

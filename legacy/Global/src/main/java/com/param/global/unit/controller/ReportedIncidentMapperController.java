package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.nabhprocess.dto.ReportedNabhMapperDto;
import com.param.global.unit.dto.ReportedIncidentMapperDto;
import com.param.global.unit.service.IReportedIncidentMapperService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({ "rawtypes" })
@RequestMapping("/api/unit")
public class ReportedIncidentMapperController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(ReportedIncidentMapperController.class);

	
	@Autowired
	private IReportedIncidentMapperService iReportedIncidentMapperService;

	@RequestMapping(value = "/saveReportedIncidentMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveReportedIncidentMapper(@RequestBody ReportedIncidentMapperDto reportedIncidentMapperDto) {
		try {
			return iReportedIncidentMapperService.saveReportedIncidentMapper(reportedIncidentMapperDto);

		} catch (Exception e) {
		logger.error("saveReportedIncidentMapper failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getReportedIncidentMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getReportedIncidentMapperList(@RequestBody ReportedIncidentMapperDto reportedIncidentMapperDto) {
		try {
			return iReportedIncidentMapperService.getReportedIncidentMapperList(reportedIncidentMapperDto);

		} catch (Exception e) {
		logger.error("getReportedIncidentMapperList failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}

	@RequestMapping(value = "/getReportedIncidentMapperById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getReportedIncidentMapperById(@RequestBody ReportedIncidentMapperDto reportedIncidentMapperDto) {
		try {
			return iReportedIncidentMapperService.getReportedIncidentMapperById(reportedIncidentMapperDto);

		} catch (Exception e) {
		logger.error("getReportedIncidentMapperById failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getTemplateByReportedIncidentId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTemplateByReportedIncidentId(@RequestBody ReportedIncidentMapperDto reportedIncidentMapperDto) {
		try {
			return iReportedIncidentMapperService.getTemplateByReportedIncidentId(reportedIncidentMapperDto);
		} catch (Exception e) {
		logger.error("getTemplateByReportedIncidentId failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/submitReportedIncidentMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response submitReportedIncidentMapper(@RequestBody ReportedIncidentMapperDto reportedIncidentMapperDto) {
		try {
			return iReportedIncidentMapperService.submitReportedIncidentMapper(reportedIncidentMapperDto);

		} catch (Exception e) {
		logger.error("submitReportedIncidentMapper failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getTemplateByReportedNabhId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTemplateByReportedNabhId(@RequestBody ReportedNabhMapperDto reportedNabhMapperDto) {
		try {
			return iReportedIncidentMapperService.getTemplateByReportedNabhId(reportedNabhMapperDto);
		} catch (Exception e) {
		logger.error("getTemplateByReportedNabhId failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getReportedNabhMapperById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getReportedNabhMapperById(@RequestBody ReportedNabhMapperDto reportedNabhMapperDto) {
		try {
			return iReportedIncidentMapperService.getReportedNabhMapperById(reportedNabhMapperDto);

		} catch (Exception e) {
		logger.error("getReportedNabhMapperById failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
}

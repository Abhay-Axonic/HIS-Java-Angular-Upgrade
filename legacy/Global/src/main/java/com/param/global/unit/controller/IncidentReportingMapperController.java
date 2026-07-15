package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.IncidentReportingMapperDto;
import com.param.global.unit.service.IIncidentReportingMapperService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({ "rawtypes" })
@RequestMapping("/api/unit")
public class IncidentReportingMapperController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(IncidentReportingMapperController.class);

	@Autowired
	private IIncidentReportingMapperService iIncidentReportingMapperService;

	@RequestMapping(value = "/saveIncidentReportingMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveIncidentReportingMapper(@RequestBody IncidentReportingMapperDto incidentReportingMapperDto) {
		try {
			return iIncidentReportingMapperService.saveIncidentReportingMapper(incidentReportingMapperDto);

		} catch (Exception e) {
		logger.error("saveIncidentReportingMapper failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}

	@RequestMapping(value = "/updateIncidentReportingMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateIncidentReportingMapper(@RequestBody IncidentReportingMapperDto incidentReportingMapperDto) {
		try {
			return iIncidentReportingMapperService.updateIncidentReportingMapper(incidentReportingMapperDto);

		} catch (Exception e) {
		logger.error("updateIncidentReportingMapper failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}

	@RequestMapping(value = "/getIncidentReportingMapperById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIncidentReportingMapperById(@RequestBody IncidentReportingMapperDto incidentReportingMapperDto) {
		try {
			return iIncidentReportingMapperService.getIncidentReportingMapperById(incidentReportingMapperDto);

		} catch (Exception e) {
		logger.error("getIncidentReportingMapperById failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}

	@RequestMapping(value = "/getIncidentReportingMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIncidentReportingMapperList(@RequestBody IncidentReportingMapperDto incidentReportingMapperDto) {
		try {
			return iIncidentReportingMapperService.getIncidentReportingMapperList(incidentReportingMapperDto);

		} catch (Exception e) {
		logger.error("getIncidentReportingMapperList failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}

	@RequestMapping(value = "/updateIncidentReportingMapperStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateIncidentReportingMapperStatus(
			@RequestBody IncidentReportingMapperDto incidentReportingMapperDto) {
		try {
			return iIncidentReportingMapperService.updateIncidentReportingMapperStatus(incidentReportingMapperDto);

		} catch (Exception e) {
		logger.error("updateIncidentReportingMapperStatus failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}

	@RequestMapping(value = "/getActiveIncidentReportingMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveIncidentReportingMapperList(
			@RequestBody IncidentReportingMapperDto incidentReportingMapperDto) {
		try {
			return iIncidentReportingMapperService.getActiveIncidentReportingMapperList(incidentReportingMapperDto);

		} catch (Exception e) {
		logger.error("getActiveIncidentReportingMapperList failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}

	@RequestMapping(value = "/getIncidentReportingMapperCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIncidentReportingMapperCount(
			@RequestBody IncidentReportingMapperDto incidentReportingMapperDto) {
		try {
			return iIncidentReportingMapperService.getIncidentReportingMapperCount(incidentReportingMapperDto);

		} catch (Exception e) {
		logger.error("getIncidentReportingMapperCount failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}

	@RequestMapping(value = "/getRoleWiseUserList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getRoleWiseUserList(@RequestBody IncidentReportingMapperDto incidentReportingMapperDto) {
		try {
			return iIncidentReportingMapperService.getRoleWiseUserList(incidentReportingMapperDto);

		} catch (Exception e) {
		logger.error("getRoleWiseUserList failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
}

package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.ObservationDto;
import com.param.opd.coversheet.service.IObservationService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ObservationController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(ObservationController.class);

	@Autowired 
	private IObservationService iObservationService;
	
	@RequestMapping(value="/saveObservation", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveObservation(@RequestBody ObservationDto observationDto) {
		try {
			return iObservationService.saveObservation(observationDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateObservation", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateObservation(@RequestBody ObservationDto observationDto) {
		try {
			return iObservationService.updateObservation(observationDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateObservationStatus", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateObservationStatus(@RequestBody ObservationDto observationDto) {
		try {
			return iObservationService.updateObservationStatus(observationDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getObservationList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getObservationList(@RequestBody ObservationDto observationDto) {
		try {
			return iObservationService.getObservationList(observationDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getObservationById/{observationId}/{organizationId}/{unitId}", method=RequestMethod.GET)
	public @ResponseBody Response getObservationById(@PathVariable ("observationId") Integer observationId,  @PathVariable ("organizationId") Integer organizationId, @PathVariable ("unitId") Integer unitId) {
		try {
			return iObservationService.getObservationById(observationId, organizationId, unitId);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getObservationCount", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getObservationCount(@RequestBody ObservationDto observationDto) {
		try {
			return iObservationService.getObservationCount(observationDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}

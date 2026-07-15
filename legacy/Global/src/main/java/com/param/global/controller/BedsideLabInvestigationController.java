package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.BedsideLabInvestigationDto;
import com.param.global.service.IBedsideLabInvestigationService;

@RestController
@RequestMapping(value="/api/global/")
@SuppressWarnings({"rawtypes"})
public class BedsideLabInvestigationController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(BedsideLabInvestigationController.class);

	@Autowired
	private IBedsideLabInvestigationService iBedsideLabInvestigationService;

	@RequestMapping(value = "/saveBedsideLabInvestigation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveCabinMaster(@RequestBody BedsideLabInvestigationDto  bedsideLabInvestigationDto){

		try {
			return iBedsideLabInvestigationService.saveBedsideLabInvestigation(bedsideLabInvestigationDto);

		} catch (Exception e) {
			logger.error("Error while saving the bedside lab investigation", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/updateBedsideLabInvestigation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBedsideLabInvestigation(@RequestBody BedsideLabInvestigationDto bedsideLabInvestigationDto){

		try {
			return iBedsideLabInvestigationService.updateBedsideLabInvestigation(bedsideLabInvestigationDto);

		} catch (Exception e) {
			logger.error("Error while updating the bedside lab investigation", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/updateBedsideLabInvestigationStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBedsideLabInvestigationStatus(@RequestBody BedsideLabInvestigationDto bedsideLabInvestigationDto){

		try {
			return iBedsideLabInvestigationService.updateBedsideLabInvestigationStatus(bedsideLabInvestigationDto);

		} catch (Exception e) {
			logger.error("Error while updating the bedside lab investigation status", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/getBedsideLabInvestigationById/{bedsideLabId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getBedsideLabInvestigationById(@PathVariable ("bedsideLabId") Integer bedsideLabId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){

		try {
			return iBedsideLabInvestigationService.getBedsideLabInvestigationById(bedsideLabId, organizationId, unitId);

		} catch (Exception e) {
			logger.error("Error while fetching the bedside lab investigation by id", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}


	@RequestMapping(value = "/getBedsideLabInvestigationList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBedsideLabInvestigationList(@RequestBody BedsideLabInvestigationDto bedsideLabInvestigationDto){

		try {
			return iBedsideLabInvestigationService.getBedsideLabInvestigationList(bedsideLabInvestigationDto);

		} catch (Exception e) {
			logger.error("Error while fetching the bedside lab investigation list", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/getBedsideLabInvestigationCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBedsideLabInvestigationCount(@RequestBody BedsideLabInvestigationDto bedsideLabInvestigationDto){

		try {
			return iBedsideLabInvestigationService.getBedsideLabInvestigationCount(bedsideLabInvestigationDto);

		} catch (Exception e) {
			logger.error("Error while fetching the bedside lab investigation count", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/getActiveBedsideLabInvestigation/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveBedsideLabInvestigation(@PathVariable ("organizationId") Integer organizationId,
										 @PathVariable ("unitId") Integer unitId){

		try {
			return iBedsideLabInvestigationService.getActiveBedsideLabInvestigation(organizationId, unitId);

		} catch (Exception e) {
			logger.error("Error while fetching the active bedside lab investigation", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

}

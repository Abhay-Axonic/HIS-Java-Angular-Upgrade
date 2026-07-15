package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.BedsideImagingMasterDto;
import com.param.global.service.IBedsideImagingMasterService;

@RestController
@RequestMapping(value="/api/global/")
@SuppressWarnings({"rawtypes"})
public class BedsideImagingMasterController implements ICommonConstants{
	private static final Logger logger = Logger.getLogger(BedsideImagingMasterController.class);

	@Autowired
	private IBedsideImagingMasterService iBedsideImagingMasterService;

	@RequestMapping(value = "/saveBedsideImagingMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBedsideImagingMaster(@RequestBody BedsideImagingMasterDto bedsideImagingMasterDto){

		try {
			return iBedsideImagingMasterService.saveBedsideImagingMaster(bedsideImagingMasterDto);

		} catch (Exception e) {
			logger.error("Error while saving the bedside imaging master", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/updateBedsideImagingMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBedsideImagingMaster(@RequestBody BedsideImagingMasterDto bedsideImagingMasterDto){

		try {
			return iBedsideImagingMasterService.updateBedsideImagingMaster(bedsideImagingMasterDto);

		} catch (Exception e) {
			logger.error("Error while updating the bedside imaging master", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/updateBedsideImagingMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBedsideImagingMasterStatus(@RequestBody BedsideImagingMasterDto bedsideImagingMasterDto){

		try {
			return iBedsideImagingMasterService.updateBedsideImagingMasterStatus(bedsideImagingMasterDto);

		} catch (Exception e) {
			logger.error("Error while updating the bedside imaging master status", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/getBedsideImagingMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBedsideImagingMasterList(@RequestBody BedsideImagingMasterDto bedsideImagingMasterDto){

		try {
			return iBedsideImagingMasterService.getBedsideImagingMasterList(bedsideImagingMasterDto);

		} catch (Exception e) {
			logger.error("Error while fetching the bedside imaging master list", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/getBedsideImagingMasterActive", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBedsideImagingMasterActive(@RequestBody BedsideImagingMasterDto bedsideImagingMasterDto){

		try {
			return iBedsideImagingMasterService.getActiveBedsideImagingMasterList(bedsideImagingMasterDto);

		} catch (Exception e) {
			logger.error("Error while fetching the active bedside imaging master list", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

}

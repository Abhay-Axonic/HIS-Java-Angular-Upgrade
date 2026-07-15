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
import com.param.global.dto.BedsideProcedureMasterDto;
import com.param.global.service.IBedsideProcedureMasterService;

@RestController
@RequestMapping(value="/api/global/")
@SuppressWarnings({"rawtypes"})
public class BedsideProcedureMasterController implements ICommonConstants{
	private static final Logger logger = Logger.getLogger(BedsideProcedureMasterController.class);

	@Autowired
	private IBedsideProcedureMasterService iBedsideProcedureMasterService;

	@RequestMapping(value = "/saveBedsideProcedureMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBedsideProcedureMaster(@RequestBody BedsideProcedureMasterDto bedsideProcedureMasterDto){

		try {
			return iBedsideProcedureMasterService.saveBedsideProcedureMaster(bedsideProcedureMasterDto);

		} catch (Exception e) {
			logger.error("Error while saving the bedside procedure master", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/updateBedsideProcedureMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBedsideProcedureMaster(@RequestBody BedsideProcedureMasterDto bedsideProcedureMasterDto){

		try {
			return iBedsideProcedureMasterService.updateBedsideProcedureMaster(bedsideProcedureMasterDto);

		} catch (Exception e) {
			logger.error("Error while updating the bedside procedure master", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/updateBedsideProcedureMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBedsideProcedureMasterStatus(@RequestBody BedsideProcedureMasterDto bedsideProcedureMasterDto){

		try {
			return iBedsideProcedureMasterService.updateBedsideProcedureMasterStatus(bedsideProcedureMasterDto);

		} catch (Exception e) {
			logger.error("Error while updating the bedside procedure master status", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/getBedsideProcedureMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBedsideProcedureMasterList(@RequestBody BedsideProcedureMasterDto bedsideProcedureMasterDto){

		try {
			return iBedsideProcedureMasterService.getBedsideProcedureMasterList(bedsideProcedureMasterDto);

		} catch (Exception e) {
			logger.error("Error while fetching the bedside procedure master list", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/getBedsideProcedureMasterActive", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBedsideProcedureMasterActive(@RequestBody BedsideProcedureMasterDto bedsideProcedureMasterDto){

		try {
			return iBedsideProcedureMasterService.getActiveBedsideProcedureMasterList(bedsideProcedureMasterDto);

		} catch (Exception e) {
			logger.error("Error while fetching the active bedside procedure master list", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

}


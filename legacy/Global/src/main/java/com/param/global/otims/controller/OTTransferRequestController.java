package com.param.global.otims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OTTransferRequestDto;
import com.param.global.otims.service.IOTTransferRequestService;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping(value = "/api/otims/OTTransfer")
public class OTTransferRequestController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(OTTransferRequestController.class);


	
	@Autowired
	IOTTransferRequestService iOTTransferRequestService;
	

	@RequestMapping(value="/saveOTTransferRequest", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveOTTransferRequest(@RequestBody OTTransferRequestDto oTTransferRequestDto){
		try{
			return iOTTransferRequestService.saveOTTransferRequest(oTTransferRequestDto);
		}catch (Exception e) {
		logger.error("saveOTTransferRequest failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getOTTransferRequest", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getOTTransferRequest(@RequestBody OTTransferRequestDto oTTransferRequestDto){
		try{
			return iOTTransferRequestService.getOTTransferRequest(oTTransferRequestDto);
		}catch (Exception e) {
		logger.error("getOTTransferRequest failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value="/updateOTTransferRequest", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateOTTransferRequest(@RequestBody OTTransferRequestDto oTTransferRequestDto){
		try{
			return iOTTransferRequestService.updateOTTransferRequest(oTTransferRequestDto);
		}catch (Exception e) {
		logger.error("updateOTTransferRequest failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getOTTransferByAdmissionId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getOTTransferByAdmissionId(@RequestBody OTTransferRequestDto oTTransferRequestDto){
		try{
			return iOTTransferRequestService.getOTTransferByAdmissionId(oTTransferRequestDto);
		}catch (Exception e) {
		logger.error("getOTTransferByAdmissionId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/initOTTransferRequest", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response initOTTransferRequest(@RequestBody OTTransferRequestDto oTTransferRequestDto){
		try{
			return iOTTransferRequestService.initOTTransferRequest(oTTransferRequestDto);
		}catch (Exception e) {
		logger.error("initOTTransferRequest failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}

package com.param.global.otims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OtcCompletionInvImplantsDto;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;
import com.param.global.otims.service.IOtcCompletionInvImplantsService;
	
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value = "/api/otims")
@SuppressWarnings("rawtypes")
public class OtcCompletionInvImplantsController implements  ICommonConstants{
	final static Logger logger = Logger.getLogger(OtcCompletionInvImplantsController.class);


	@Autowired
	private IOtcCompletionInvImplantsService iOtcCompletionInvImplantsService;
	
	@RequestMapping(value = "/saveInvImplants" ,method=RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveInvImplants(@RequestBody OtcCompletionInvImplantsDto otcCompletionInvImplantsDto){
		try{
			return iOtcCompletionInvImplantsService.saveInvImplants(otcCompletionInvImplantsDto);
		}catch(Exception e){
		logger.error("saveInvImplants failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "getImplantIssueAcceptanceData", method = RequestMethod.POST)
	public Response getImplantIssueAcceptanceData(@RequestBody OtcCompletionInvImplantsDto otcCompletionInvImplantsDto){
	try {
	return iOtcCompletionInvImplantsService.getImplantIssueAcceptanceData(otcCompletionInvImplantsDto);

	} catch (Exception e) {
	logger.error("getImplantIssueAcceptanceData failed", e);

	return new Response<>("error", "500", "error", null, null);
	}
	}
}

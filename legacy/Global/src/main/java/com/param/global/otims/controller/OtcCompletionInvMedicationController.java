package com.param.global.otims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;
import com.param.global.otims.service.IOtcCompletionInvMedicationService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value = "/api/otims")
@SuppressWarnings("rawtypes")
public class OtcCompletionInvMedicationController implements  ICommonConstants{
	final static Logger logger = Logger.getLogger(OtcCompletionInvMedicationController.class);


	@Autowired
	private IOtcCompletionInvMedicationService iOtcCompletionInvMedicationService;
	
	@RequestMapping(value = "/saveInvMedications" ,method=RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveInvMedications(@RequestBody OtcCompletionInvMedicationDto otcCompletionInvMedicationDto){
		try{
			return iOtcCompletionInvMedicationService.saveInvMedications(otcCompletionInvMedicationDto);
		}catch(Exception e){
		logger.error("saveInvMedications failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
		

}

package com.param.opd.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.NursingInitialAssessmentDto;
import com.param.opd.unit.service.INursingInitialAssessmentService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NursingInitialAssessmentController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(NursingInitialAssessmentController.class);


	@Autowired
	private INursingInitialAssessmentService iNursingInitialAssessmentService;
	
	@RequestMapping(value="/saveNursingInitialAssessment", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveNursingInitialAssessment(@RequestBody NursingInitialAssessmentDto nursingInitialAssessmentDto) {
		try
		{
			return iNursingInitialAssessmentService.saveNursingInitialAssessment(nursingInitialAssessmentDto);

		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	/*@RequestMapping(value="/saveNursingInitialAssessment", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveNursingInitialAssessment(@RequestBody String nursingInitialAssessmentDto) {
		
			Response<NursingInitialAssessmentDto, Integer> response = null;
			try {
				
				JsonCheck.mapJsonToObject(nursingInitialAssessmentDto, NursingInitialAssessmentDto.class);
				
				//  response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
				 // response;
				 
				return null;

		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/
	
	@RequestMapping(value="/getNursingInitialAssessmentList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNursingInitialAssessmentList(@RequestBody NursingInitialAssessmentDto nursingInitialAssessmentDto) {
		try
		{
			return iNursingInitialAssessmentService.getNursingInitialAssessmentList(nursingInitialAssessmentDto);

		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}

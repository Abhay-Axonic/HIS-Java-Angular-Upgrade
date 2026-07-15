package com.param.global.org.common.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.org.common.dto.EmployeeSurveyDto;
import com.param.global.org.common.service.IEmployeeSurveyService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@SuppressWarnings({ "unchecked", "rawtypes" })
@RequestMapping(value="/api/common/empSurvey")
@RestController
public class EmployeeSurveyController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(EmployeeSurveyController.class);

	@Autowired
	private IEmployeeSurveyService iEmployeeSurveyService;
	
	@RequestMapping(value = "/saveEmployeeSurvey", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveEmployeeSurvey(@RequestBody EmployeeSurveyDto employeeSurveyDto){
		
		try {
			return iEmployeeSurveyService.saveEmployeeSurvey(employeeSurveyDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getEmployeeSurveyById/{userId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getCabinMasterById(@PathVariable ("userId") Integer userId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		
		try {
			return iEmployeeSurveyService.getEmployeeSurveyById(userId, organizationId, unitId);
			
		} catch (Exception e) {
			logger.error("getCabinMasterById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	


}

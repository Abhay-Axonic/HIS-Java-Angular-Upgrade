package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.TVitalDto;
import com.param.global.dto.VitalSearchDto;
import com.param.global.service.IVitalService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value = "/global")
@SuppressWarnings({"rawtypes"})
public class VitalsController  implements ICommonConstants{
	final static Logger logger = Logger.getLogger(VitalsController.class);

	
	
	@Autowired
	private IVitalService iVitalService;
	
	
	@RequestMapping(value = "/vital" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveVitals(@RequestBody TVitalDto tVitalDto){
		try{
			
			return iVitalService.saveVitals(tVitalDto);
		}catch(Exception e){
		logger.error("saveVitals failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/vital/details" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVitalsByPatient(@RequestBody VitalSearchDto vitalSearchDto){
		try{
			return iVitalService.getVitalsByPatient(vitalSearchDto);
		}catch(Exception e){
		logger.error("getVitalsByPatient failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	


}

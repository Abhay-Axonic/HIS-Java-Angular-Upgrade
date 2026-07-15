package com.param.otims.controller;

import com.param.global.dto.RadiologyOrdersListDto;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.PreOperativeDetailsDto;
import com.param.otims.service.IPreOperativeDetailsService;

@RestController
@RequestMapping(value = "/api/otims/PreOperativeDetails")
@SuppressWarnings({"rawtypes","unused"})
public class PreOperativeDetailsController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(PreOperativeDetailsController.class);

	@Autowired
	IPreOperativeDetailsService iPreOperativeDetailsService;
	
	@RequestMapping(value = "/savePreOperativeDetails" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePreOperativeDetails(@RequestBody PreOperativeDetailsDto preOperativeDetailsDto){
		try{
			Response res =  iPreOperativeDetailsService.savePreOperativeDetails(preOperativeDetailsDto);
			if(preOperativeDetailsDto.getPreOperativeId()!=null) {
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null,res.getObject());
			}else {
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null,res.getObject());
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/getPreOperativeDetailsByProReqId" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPreOperativeDetailsByProReqId(@RequestBody PreOperativeDetailsDto preOperativeDetailsDto){
		try{
			return  iPreOperativeDetailsService.getPreOperativeDetailsByProReqId(preOperativeDetailsDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/getActiveDoctorList" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveDoctorList(@RequestBody PreOperativeDetailsDto preOperativeDetailsDto){
		try{
			return  iPreOperativeDetailsService.getActiveDoctorList(preOperativeDetailsDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/sendNotificationToDoctor" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response sendNotificationToDoctor(@RequestBody PreOperativeDetailsDto preOperativeDetailsDto){
		try{
			return  iPreOperativeDetailsService.sendNotificationToDoctor(preOperativeDetailsDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	
	@RequestMapping(value = "/getAnaesthesiaDoctorList" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAnaesthesiaDoctorList(@RequestBody PreOperativeDetailsDto preOperativeDetailsDto){
		try{
			return  iPreOperativeDetailsService.getAnaesthesiaDoctorList(preOperativeDetailsDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/getProcedureOrderIdByProReqId" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getProcedureOrderIdByProReqId(@RequestBody PreOperativeDetailsDto preOperativeDetailsDto){
		try{
			return  iPreOperativeDetailsService.getProcedureOrderIdByProReqId(preOperativeDetailsDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	@RequestMapping(value="/getAiTranscriptionCreds", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response processTranscribedText(@RequestBody PreOperativeDetailsDto preOperativeDetailsDto) {
		try {

			return iPreOperativeDetailsService.getAiTranscriptionCreds(preOperativeDetailsDto);

		}catch (HibernateException e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}

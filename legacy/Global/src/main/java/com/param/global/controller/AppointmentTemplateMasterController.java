package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.AppointmentTemplateMasterDto;
import com.param.global.service.IAppointmentTemplateMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api")
@SuppressWarnings({"rawtypes"})
public class AppointmentTemplateMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(AppointmentTemplateMasterController.class);

	
	@Autowired
	IAppointmentTemplateMasterService iAppointmentTemplateMasterService;
	
	@RequestMapping(value = "/saveAppointmentTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response save(@RequestBody AppointmentTemplateMasterDto dto){
		
		try {
			return iAppointmentTemplateMasterService.saveAppointmentTemplate(dto);
			
		} catch (Exception e) {
		logger.error("save failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getAppointmentTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAppointmentTemplate(@RequestBody AppointmentTemplateMasterDto dto){
		
		try {
			return iAppointmentTemplateMasterService.getAppointmentTemplate(dto);
			
		} catch (Exception e) {
		logger.error("getAppointmentTemplate failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getAppointmentTemplateMapperData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAppointmentTemplateMapperData(@RequestBody AppointmentTemplateMasterDto dto){
		
		try {
			return iAppointmentTemplateMasterService.getAppointmentTemplateMapperData(dto);
			
		} catch (Exception e) {
		logger.error("getAppointmentTemplateMapperData failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateStatus(@RequestBody AppointmentTemplateMasterDto dto){
		
		try {
			return iAppointmentTemplateMasterService.updateStatus(dto);
			
		} catch (Exception e) {
		logger.error("updateStatus failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
}

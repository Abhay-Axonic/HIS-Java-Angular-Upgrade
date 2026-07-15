package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.DoctorIPConsultationServiceMapperDto;
import com.param.global.service.IDoctorIPConsultationServiceMapperService;

@RestController
@RequestMapping(value="api/global")
@SuppressWarnings({"rawtypes"})

public class DoctorIPConsultationServiceMapperController implements ICommonConstants {
	private static final Logger logger = Logger.getLogger(DoctorIPConsultationServiceMapperController.class);

	@Autowired
	IDoctorIPConsultationServiceMapperService iDoctorIPConsultationServiceMapperService;
	
	@RequestMapping(value="/saveDoctorIPConsultationServiceMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  Response saveDoctorIPConsultationServiceMapper(@RequestBody DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto){
		try{
			return iDoctorIPConsultationServiceMapperService.saveDoctorIPConsultationServiceMapper(doctorIPConsultationServiceMapperDto);
		}catch(Exception e){
			logger.error("Error while saving the doctor IP consultation service mapper", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value="/updateDoctorIPConsultationServiceMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  Response updateDoctorIPConsultationServiceMapper(@RequestBody DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto){
		try{
			return iDoctorIPConsultationServiceMapperService.updateDoctorIPConsultationServiceMapper(doctorIPConsultationServiceMapperDto);
		}catch(Exception e){
			logger.error("Error while updating the doctor IP consultation service mapper", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
		
	@RequestMapping(value="/getDoctorIPConsultationServiceMapperList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  Response getDoctorIPConsultationServiceMapperList(@RequestBody DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto){
		try{
			return iDoctorIPConsultationServiceMapperService.getDoctorIPConsultationServiceMapperList(doctorIPConsultationServiceMapperDto);
		}catch(Exception e){
			logger.error("Error while fetching the doctor IP consultation service mapper list", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value="/getDoctorIPConsultationServiceMapperById/{doctorIPConsultationServiceMapperId}/{organizationId}/{unitId}" , method = RequestMethod.GET)
	public  Response getDoctorIPConsultationServiceMapperById(@PathVariable ("doctorIPConsultationServiceMapperId") Integer doctorIPConsultationServiceMapperId,
															  @PathVariable ("organizationId") Integer organizationId,
															  @PathVariable ("unitId") Integer unitId){
		try{
			return iDoctorIPConsultationServiceMapperService.getDoctorIPConsultationServiceMapperById(doctorIPConsultationServiceMapperId, organizationId, unitId);
		}catch(Exception e){
			logger.error("Error while fetching the doctor IP consultation service mapper by id", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value="/getDoctorIPConsultationServiceMapperListCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  Response getDoctorIPConsultationServiceMapperListCount(@RequestBody DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto){
		try{
			return iDoctorIPConsultationServiceMapperService.getDoctorIPConsultationServiceMapperListCount(doctorIPConsultationServiceMapperDto);
		}catch(Exception e){
			logger.error("Error while fetching the doctor IP consultation service mapper list count", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

}

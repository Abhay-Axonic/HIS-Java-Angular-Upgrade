package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionDto;
import com.param.opd.coversheet.dto.AdmissionOpdDTO;
import com.param.opd.coversheet.service.IAdmissionService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
/*FUNCTIONALITY : Admission
DATE:16/04/2018
CREATED BY: TUSHAR*/

@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdmissionController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(AdmissionController.class);

	
	@Autowired 
	private IAdmissionService iAdmissionService;
	
	@RequestMapping(value="/saveAdmission", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAdmission(@RequestBody AdmissionOpdDTO admissionOpdDTO) {
		try
		{
			return iAdmissionService.saveAdmission(admissionOpdDTO);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateAdmission", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAdmission(@RequestBody AdmissionOpdDTO admissionOpdDTO) {
		try
		{
			return iAdmissionService.updateAdmission(admissionOpdDTO);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getAdmission", method=RequestMethod.GET)
	public @ResponseBody Response getAdmission(@RequestParam("admissionId") Integer admissionId,@RequestParam("instruction_post_id") Integer instruction_post_id ) {
		try
		{
			return iAdmissionService.getAdmission(admissionId,instruction_post_id);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getReasonMasters", method=RequestMethod.GET)
	public @ResponseBody Response getReasonMasters(){
		try
		{
			return iAdmissionService.getReasonMasters();
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getBedMasters", method=RequestMethod.GET)
	public @ResponseBody Response getBedMasters(){
		try
		{
			return iAdmissionService.getReasonMasters();
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getPatientReturnDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientReturnDetails(@RequestBody AdmissionOpdDTO admissionOpdDTO) {
		try
		{
			return iAdmissionService.getPatientReturnDetails(admissionOpdDTO);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}

package com.param.global.controller;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.SurgeryGradeMasterDto;
import com.param.global.service.ISurgeryGradeMasterService;
import com.param.service.global.SessionService;
import com.param.service.global.model.CookieObjectDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
/*FUNCTIONALITY : Surgery Grade MASTER
DATE:27/04/2018
CREATED BY: Vivek */
@RestController
@RequestMapping("api/global")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SurgeryGradeMasterController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(SurgeryGradeMasterController.class);

	
	@Autowired
	ISurgeryGradeMasterService iSurgeryGradeMasterService;
	
	@Autowired
	SessionService sessionService;
	
	@RequestMapping(value="/saveSurgeryGradeMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveSurgeryGradeMaster(@RequestBody SurgeryGradeMasterDto surgeryGradeMasterDto){
		try{
			
			return iSurgeryGradeMasterService.addSurgeryGradeMaster(surgeryGradeMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSurgeryGradeMasterList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSurgeryGradeMasterList(@RequestBody SurgeryGradeMasterDto surgeryGradeMasterDto){
		try{
			return iSurgeryGradeMasterService.getSurgeryGradeMasterList(surgeryGradeMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateSurgeryGradeMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateSurgeryGradeMaster(@RequestBody SurgeryGradeMasterDto surgeryGradeMasterDto){
		try{
			return iSurgeryGradeMasterService.updateSurgeryGradeMaster(surgeryGradeMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSurgeryGradeById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSurgeryGradeById(@RequestBody SurgeryGradeMasterDto surgeryGradeMasterDto){
		try{
			
			return iSurgeryGradeMasterService.getSurgeryGradeById(surgeryGradeMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateStatusForSurgeryGrade" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForSurgeryGrade(@RequestBody SurgeryGradeMasterDto surgeryGradeMasterDto){
		try{
			
			return iSurgeryGradeMasterService.updateStatusForSurgeryGrade(surgeryGradeMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getActiveSurgeryGradeList", method = RequestMethod.GET)
	public @ResponseBody Response getActiveSurgeryGradeList(HttpServletRequest request){
		try{
			if(request.getCookies()!=null)
        	{
        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
        		 if (cookies != null && cookies.size() > 0) {
                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.get(0).getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
                 }
        	}
			return iSurgeryGradeMasterService.getActiveSurgeryGradeList(sessionService.getOrganizationId());
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	

}

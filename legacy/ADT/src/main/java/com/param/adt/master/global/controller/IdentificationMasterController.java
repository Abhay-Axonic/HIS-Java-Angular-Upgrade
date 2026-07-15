package com.param.adt.master.global.controller;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.IdentificationMasterDto;
import com.param.adt.master.global.service.IIdentificationMasterService;
import com.param.service.global.SessionService;
import com.param.service.global.model.CookieObjectDto;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class IdentificationMasterController implements ICommonConstants
{
	@Autowired
	private IIdentificationMasterService iIdentificationMasterService;
	
	@Autowired
	SessionService sessionService;
	
	@RequestMapping(value="/saveIdentificationMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveIdentificationMaster(@RequestBody IdentificationMasterDto identificationMasterDto){
		try{
			
			return iIdentificationMasterService.addIdentificationMaster(identificationMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getIdentificationMasterList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getIdentificationMasterList(@RequestBody IdentificationMasterDto identificationMasterDto){
		try{
			return iIdentificationMasterService.getIdentificationMasterList(identificationMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	 
	@RequestMapping(value="/updateIdentificationMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateIdentificationMaster(@RequestBody IdentificationMasterDto identificationMasterDto){
		try{
			return iIdentificationMasterService.updateIdentificationMaster(identificationMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getIdentificationById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getIdentificationById(@RequestBody IdentificationMasterDto identificationMasterDto){
		try{
			
			return iIdentificationMasterService.getIdentificationById(identificationMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateStatusForIdentification" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForIdentification(@RequestBody IdentificationMasterDto identificationMasterDto){
		try{
			
			return iIdentificationMasterService.updateStatusForIdentification(identificationMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getActiveIdentificationList", method = RequestMethod.GET)
	public @ResponseBody Response getActiveIdentificationList(HttpServletRequest request){
		try{
			
			if(request.getCookies()!=null)
        	{
        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
        		 if (cookies != null && cookies.size() > 0) {
                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.get(0).getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
                 }
        	}

			return iIdentificationMasterService.getActiveIdentificationList(sessionService.getOrganizationId());


		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getIdentificationCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getIdentificationCount(@RequestBody IdentificationMasterDto identificationMasterDto){
		try{
			
			return iIdentificationMasterService.getIdentificationCount(identificationMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getActiveIdentificationMasterListByOrgId", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveIdentificationMasterListByOrgId(@RequestBody IdentificationMasterDto identificationMasterDto){
		try{
			return iIdentificationMasterService.getActiveIdentificationMasterListByOrgId(identificationMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
}

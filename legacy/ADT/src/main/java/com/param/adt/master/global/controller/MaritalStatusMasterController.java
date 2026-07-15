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
import com.param.adt.master.global.dto.MaritalStatusDto;
import com.param.adt.master.global.service.IMaritalStatusMasterService;
import com.param.service.global.SessionService;
import com.param.service.global.model.CookieObjectDto;


@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class MaritalStatusMasterController implements ICommonConstants {
	
	 @Autowired
	  private IMaritalStatusMasterService iMaritalStatusMasterService;
	 
	 @Autowired
	 SessionService sessionService;
	 
	 @RequestMapping(value="/getMaritalStatusMasterList", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getMaritalStatusMasterList(@RequestBody MaritalStatusDto maritalStatusDto){
			try{
				return iMaritalStatusMasterService.getMaritalStatusMasterList(maritalStatusDto);
			}catch(Exception e){
				
			}
			return new Response(ERROR, null, null, null,null);
		}
	 
	 @RequestMapping(value="/saveMaritalStatusMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response saveMaritalStatusMaster(@RequestBody MaritalStatusDto maritalStatusDto){
			try{
				
				return iMaritalStatusMasterService.addMaritalStatusMaster(maritalStatusDto);
			}catch(Exception e){
				
			}
			return new Response(ERROR, null, null, null,null);
		}
		
	 @RequestMapping(value="/getMaritalById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getMaritalById(@RequestBody MaritalStatusDto maritalStatusDto){
			try{
				
				return iMaritalStatusMasterService.getMaritalById(maritalStatusDto);
			}catch(Exception e){
				
			}
			return new Response(ERROR, null, null, null,null);
		}
	 @RequestMapping(value="/updateMaritalStatusMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updateMaritalMaster(@RequestBody MaritalStatusDto maritalStatusDto){
			try{
				return iMaritalStatusMasterService.updateMaritalStatusMaster(maritalStatusDto);
			}catch(Exception e){
				
			}
			return new Response(ERROR, null, null, null,null);
		}
		@RequestMapping(value="/updateStatusForMaritalStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updateStatusForMarital(@RequestBody MaritalStatusDto maritalStatusDto){
			try{
				
				return iMaritalStatusMasterService.updateStatusForMarital(maritalStatusDto);
			}catch(Exception e){
				
			}
			return new Response(ERROR, null, null, null,null);
		}
		@RequestMapping(value="/getActiveMaritalStatusList", method = RequestMethod.GET)
		public @ResponseBody Response getActiveMaritalStatusList(HttpServletRequest request){
			try{
				
				if(request.getCookies()!=null)
	        	{
	        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
	                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
	        		 if (cookies != null && cookies.size() > 0) {
	                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.get(0).getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
	                 }
	        	}

				return iMaritalStatusMasterService.getActiveMaritalStatusList(sessionService.getOrganizationId());

			}catch(Exception e){
				
			}
			return new Response(ERROR, null, null, null,null);
		}
		
		@RequestMapping(value="/getMaritalStatusCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getMaritalStatusCount(@RequestBody MaritalStatusDto maritalStatusDto){
			try{
				
				return iMaritalStatusMasterService.getMaritalStatusCount(maritalStatusDto);
			}catch(Exception e){
				
			}
			return new Response(ERROR, null, null, null,null);
		}
}

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
import com.param.adt.master.global.dto.OccupationMasterDto;
import com.param.adt.master.global.service.IOccupationMasterService;
import com.param.service.global.SessionService;
import com.param.service.global.model.CookieObjectDto;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class OccupationMasterController implements ICommonConstants{
	
	@Autowired
	  private IOccupationMasterService iOccupationMasterService;
	
	@Autowired
	SessionService sessionService;
	
	 
	 @RequestMapping(value="/getOccupationMasterList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getOccupationMasterList(@RequestBody OccupationMasterDto occupationMasterDto){
			try{
				return iOccupationMasterService.getOccupationMasterList(occupationMasterDto);
			}catch(Exception e){
				
			}
			return new Response(ERROR, null, null, null,null);
		}
	 
	 @RequestMapping(value="/saveOccupationMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response saveOccuaprtionMaster(@RequestBody OccupationMasterDto occupationMasterDto){
			try{
				
				return iOccupationMasterService.addOccupationMaster(occupationMasterDto);
			}catch(Exception e){
				
			}
			return new Response(ERROR, null, null, null,null);
		}
		
	 @RequestMapping(value="/getOccupationMasterById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getOccupationMasterById(@RequestBody OccupationMasterDto occupationMasterDto){
			try{
				
				return iOccupationMasterService.getOccupationById(occupationMasterDto);
			}catch(Exception e){
				
			}
			return new Response(ERROR, null, null, null,null);
		}
	 @RequestMapping(value="/updateOccupationMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updateOccupationMaster(@RequestBody OccupationMasterDto occupationMasterDto){
			try{
				return iOccupationMasterService.updateOccupationMaster(occupationMasterDto);
			}catch(Exception e){
				
			}
			return new Response(ERROR, null, null, null,null);
		}
		@RequestMapping(value="/updateStatusForOccupation" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updateStatusForOccupation(@RequestBody OccupationMasterDto occupationMasterDto){
			try{
				
				return iOccupationMasterService.updateStatusForOccupation(occupationMasterDto);
			}catch(Exception e){
				
			 
			}
			return new Response(ERROR, null, null, null,null);
		}
		@RequestMapping(value="/getActiveOccupationList", method = RequestMethod.GET)
		public @ResponseBody Response getActiveOccupationList(HttpServletRequest request){
			try{
				
				if(request.getCookies()!=null)
	        	{
	        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
	                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
	        		 if (cookies != null && cookies.size() > 0) {
	                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.get(0).getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
	                 }
	        	}
				
				return iOccupationMasterService.getActiveMaritalStatusList(sessionService.getOrganizationId());
			}catch(Exception e){
				
			}
			return new Response(ERROR, null, null, null,null);
		}
		@RequestMapping(value="/getOccupationCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getOccupationCount(@RequestBody OccupationMasterDto occupationMasterDto){
			try{
				
				return iOccupationMasterService.getOccupationCount(occupationMasterDto);
			}catch(Exception e){
				
			}
			return new Response(ERROR, null, null, null,null);
		}

}

package com.param.global.controller;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.param.global.dto.GenderMasterDto;
import com.param.global.dto.PrefixMasterDto;
import com.param.global.utility.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.RelationMasterDto;
import com.param.global.service.IRelationMasterService;
import com.param.service.global.SessionService;
import com.param.service.global.model.CookieObjectDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class RelationMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(RelationMasterController.class);

	 
	@Autowired
	  private IRelationMasterService iRelationService;
	
	@Autowired
	SessionService sessionService;
	 
	 @RequestMapping(value="/getRelationMasterList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getRelationMasterList(@RequestBody RelationMasterDto relationMasterDto){
			try{
				return iRelationService.getRelationMasterList(relationMasterDto);
			}catch(Exception e){
			logger.error("method failed", e);
			}
			return new Response(ERROR, null, null, null,null);
		}
	 
	 @RequestMapping(value="/saveRelationMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response saveRelationMaster(@RequestBody RelationMasterDto relationMasterDto){
			try{
				
				return iRelationService.addRelationMaster(relationMasterDto);
			}catch(Exception e){
			logger.error("method failed", e);
			}
			return new Response(ERROR, null, null, null,null);
		}
		
	 @RequestMapping(value="/getRelationById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getRelationById(@RequestBody RelationMasterDto relationMasterDto){
			try{
				
				return iRelationService.getRelationById(relationMasterDto);
			}catch(Exception e){
			logger.error("method failed", e);
			}
			return new Response(ERROR, null, null, null,null);
		}
	 @RequestMapping(value="/updateRelationMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updateGenderMaster(@RequestBody RelationMasterDto relationMasterDto){
			try{
				return iRelationService.updateRelationMaster(relationMasterDto);
			}catch(Exception e){
			logger.error("method failed", e);
			}
			return new Response(ERROR, null, null, null,null);
		}
		@RequestMapping(value="/updateStatusForRelation" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updateStatusForRelation(@RequestBody RelationMasterDto relationMasterDto){
			try{
				
				return iRelationService.updateStatusForRelation(relationMasterDto);
			}catch(Exception e){
			logger.error("method failed", e);
			}
			return new Response(ERROR, null, null, null,null);
		}
		@RequestMapping(value="/getActiveRelationList", method = RequestMethod.GET)
		public @ResponseBody Response getActiveRelationList(HttpServletRequest request){
			try{
				if(request.getCookies()!=null)
	        	{
	        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
	                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
	        		 if (cookies != null && cookies.size() > 0) {
	                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.getFirst().getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
	                 }
	        	}

				return  iRelationService.getActiveRelationList(sessionService.getOrganizationId());

			}catch(Exception e){
			logger.error("method failed", e);
			}
			return new Response(ERROR, null, null, null,null);
		}
		
		@RequestMapping(value="/getActiveRelationListForWebPage",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getActiveRelationListForWebPage(@RequestBody RelationMasterDto relationMasterDto){
			try{
				return iRelationService.getActiveRelationListForWebPage(relationMasterDto);
			}catch(Exception e){
			logger.error("method failed", e);
			}
			return new Response(ERROR, null, null, null,null);
		}
		
		
		@RequestMapping(value="/getRelationCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getRelationCount(@RequestBody RelationMasterDto relationMasterDto){
			try{
				
				return iRelationService.getRelationCount(relationMasterDto);
			}catch(Exception e){
			logger.error("method failed", e);
			}
			return new Response(ERROR, null, null, null,null);
		}
}

package com.param.global.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.param.global.dto.sync.hl7ConfigListDto;
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
import com.param.global.dto.PrefixMasterDto;
import com.param.global.service.IPrefixMasterService;
import com.param.service.global.SessionService;
import com.param.service.global.model.CookieObjectDto;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Controller
@RequestMapping("/adt")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PrefixMasterController implements ICommonConstants 
{
	final static Logger logger = Logger.getLogger(PrefixMasterController.class);


		@Autowired
		private IPrefixMasterService iPrefixMasterService;
		
		 @Autowired
		 SessionService sessionService;
		
		@RequestMapping(value = "/savePrefixMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response savePrefixMaster(@RequestBody PrefixMasterDto prefixMasterDto) {
			try {

				return iPrefixMasterService.addPrefixMaster(prefixMasterDto);
			} catch (Exception e) {
			logger.error("method failed", e);
			return new Response(ERROR, null, null, null, null);
			}
		}

		@RequestMapping(value = "/getPrefixMasterList",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getPrefixMasterList(@RequestBody PrefixMasterDto prefixMasterDto) {
			try {
				return iPrefixMasterService.getPrefixMasterList(prefixMasterDto);
			} catch (Exception e) {
			logger.error("method failed", e);
			return new Response(ERROR, null, null, null, null);
			}
		}

		@RequestMapping(value = "/updatePrefixMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updatePrefixMaster(@RequestBody PrefixMasterDto prefixMasterDto) {
			try {
				return iPrefixMasterService.updatePrefixMaster(prefixMasterDto);
			} catch (Exception e) {
			logger.error("method failed", e);
			return new Response(ERROR, null, null, null, null);
			}
		}

		@RequestMapping(value = "/getPrefixById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getPrefixById(@RequestBody PrefixMasterDto prefixMasterDto) {
			try {

				return iPrefixMasterService.getPrefixById(prefixMasterDto);
			} catch (Exception e) {
			logger.error("method failed", e);
			return new Response(ERROR, null, null, null, null);
			}
		}

		@RequestMapping(value = "/updateStatusForPrefix", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updateStatusForPrefix(@RequestBody PrefixMasterDto prefixMasterDto) {
			try {

				return iPrefixMasterService.updateStatusForPrefix(prefixMasterDto);
			} catch (Exception e) {
			logger.error("method failed", e);
			return new Response(ERROR, null, null, null, null);
			}
		}

		@RequestMapping(value = "/getActivePrefixList", method = RequestMethod.GET)
		public @ResponseBody Response getActivePrefixList(HttpServletRequest request) {
			try {
				
				if(request.getCookies()!=null)
	        	{
	        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
	                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
	        		 if (cookies != null && cookies.size() > 0) {
	                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.get(0).getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
	                 }
	        	}

				return  iPrefixMasterService.getActivePrefixList(sessionService.getOrganizationId());

			} catch (Exception e) {
			logger.error("method failed", e);
			return new Response(ERROR, null, null, null, null);
			}
		}
		
		@RequestMapping(value = "/getPrefixCount",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getPrefixCount(@RequestBody PrefixMasterDto prefixMasterDto) {
			try {
				return iPrefixMasterService.getPrefixCount(prefixMasterDto);
			} catch (Exception e) {
			logger.error("method failed", e);
			return new Response(ERROR, null, null, null, null);
			}
		}
		
		@RequestMapping(value = "/getActivePrefixListForWebPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getActivePrefixListForWebPage(@RequestBody PrefixMasterDto prefixMasterDto) {
			try {
				
				return iPrefixMasterService.getActivePrefixListForWebPage(prefixMasterDto);
			} catch (Exception e) {
			logger.error("method failed", e);
			return new Response(ERROR, null, null, null, null);
			}
		}
		
		@RequestMapping(value = "/getPrefixMasterAndMedallPrefixMaster",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getPrefixMasterAndMedallPrefixMaster(@RequestBody PrefixMasterDto prefixMasterDto) {
			try {
				return iPrefixMasterService.getPrefixMasterAndMedallPrefixMaster(prefixMasterDto);
			} catch (Exception e) {
			logger.error("method failed", e);
			return new Response(ERROR, null, null, null, null);
			}
		}
		
		
}

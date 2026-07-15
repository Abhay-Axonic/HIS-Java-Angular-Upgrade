package com.param.global.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.UserSSORequestDto;
import com.param.global.service.ISSOService;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes","unchecked"})
@Controller
@RequestMapping("/sso")
public class SSOController implements ICommonConstants 
{
	final static Logger logger = Logger.getLogger(SSOController.class);

	@Autowired
	ISSOService iSSOService;
	
	
	
	@RequestMapping(value= "/validateToken" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody  Response validateToken(@RequestBody UserSSORequestDto userSSORequestDto){
		try{
			return iSSOService.validateToken(userSSORequestDto);
		}catch(Exception e){
			logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}


	@RequestMapping(value= "/clearSSODetails", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody  Response clearSSODetails(@RequestBody UserSSORequestDto userSSORequestDto){
		try{
			return iSSOService.clearSSODetails(userSSORequestDto);
		}catch(Exception e){
			logger.error("method failed", e);
		 }
		return new Response(ERROR, null, null, null,null);
	    }

	}

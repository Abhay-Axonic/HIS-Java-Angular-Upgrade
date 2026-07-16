package com.param.global.controller;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.ICPCMapperDto;
import com.param.global.service.IICPCMapperService;
import com.param.service.global.SessionService;
import com.param.service.global.model.CookieObjectDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping("/api/opd")
public class ICPCMapperController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(ICPCMapperController.class);

	@Autowired
	IICPCMapperService iICPCMapperService;
	
	@Autowired
	 SessionService sessionService;

	@RequestMapping(value = "/saveICPCMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveICPCMapper(@RequestBody ICPCMapperDto ICPCMapperDto) {
		try {
			return iICPCMapperService.saveICPCMapper(ICPCMapperDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateICPCMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateICPCMapper(@RequestBody ICPCMapperDto ICPCMapperDto) {
		try {
			return iICPCMapperService.updateICPCMapper(ICPCMapperDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateICPCMapperStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateICPCMapperStatus(@RequestBody ICPCMapperDto ICPCMapperDto) {
		try {
			return iICPCMapperService.updateICPCMapperStatus(ICPCMapperDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getICPCMapperById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getICPCMapperById(@RequestBody ICPCMapperDto ICPCMapperDto) {
		try {
			return iICPCMapperService.getICPCMapperById(ICPCMapperDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getICPCMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getICPCMapperList(@RequestBody ICPCMapperDto ICPCMapperDto) {
		try {
			return iICPCMapperService.getICPCMapperList(ICPCMapperDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getICPCMapperCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getICPCMapperCount(@RequestBody ICPCMapperDto ICPCMapperDto) {
		try {
			return iICPCMapperService.getICPCMapperCount(ICPCMapperDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getActiveICPCMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveICPCMapperList(@RequestBody ICPCMapperDto ICPCMapperDto) {
		try {
			return iICPCMapperService.getActiveICPCMapperList(ICPCMapperDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getGenderListByICPCSystemId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getGenderListByICPCSystemId(@RequestBody ICPCMapperDto ICPCMapperDto) {
		try {
			return iICPCMapperService.getGenderListByICPCSystemId(ICPCMapperDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getSystemListForComplaintsOnCoversheet", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSystemListForComplaintsOnCoverheet(@RequestBody ICPCMapperDto ICPCMapperDto) {
		try {
			return iICPCMapperService.getSystemListForComplaintsOnCoversheet(ICPCMapperDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getActiveGenderListForICPCMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveGenderListForICPCMapper(HttpServletRequest request) {
		try {
			if(request.getCookies()!=null)
        	{
        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
        		 if (cookies != null && cookies.size() > 0) {
                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.getFirst().getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
                 }
        	}
			return iICPCMapperService.getActiveGenderListForICPCMapper(request);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getICPCIdByCode", method = RequestMethod.GET)
	public Response getICPCIdByCode(@RequestParam("icpcCode") String icpcCode) {
		try {
			return iICPCMapperService.getICPCByCode(icpcCode);
		} catch (Exception e) {
		logger.error("getICPCIdByCode failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
}

package com.param.adt.master.unit.controller;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.param.adt.master.unit.dto.NursingStationWardMapperDto;
import com.param.adt.master.unit.service.INursingStationWardMapperService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.service.global.SessionService;
import com.param.service.global.model.CookieObjectDto;

@RestController
@RequestMapping("/api/adt/unit")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NursingStationWardMapperController implements ICommonConstants{
	@Autowired
	INursingStationWardMapperService iNursingStationWardMapperService;
	@Autowired
	SessionService sessionService;
	
	@RequestMapping(value="/saveNursingStationWardMapper", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveNursingStationWardMapper(@RequestBody NursingStationWardMapperDto nursingStationWardMapperDto) {
		try {
			return iNursingStationWardMapperService.saveNursingStationWardMapper(nursingStationWardMapperDto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getNursingStationWardMapperList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNursingStationWardMapperList(@RequestBody NursingStationWardMapperDto nursingStationWardMapperDto) {
		try {
			return iNursingStationWardMapperService.getNursingStationWardMapperList(nursingStationWardMapperDto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/updateNursingStationWardMapperStatus", method = RequestMethod.PUT , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateNursingStationWardMapperStatus(@RequestBody NursingStationWardMapperDto nursingStationWardMapperDto) {
		try {
			//System.out.println(nursingStationWardMapperDto.toString());
			return iNursingStationWardMapperService.updateNursingStationWardMapperStatus(nursingStationWardMapperDto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/updateNursingStationWardMapper", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateNursingStationWardMapper(@RequestBody NursingStationWardMapperDto nursingStationWardMapperDto) {
		try {
			return iNursingStationWardMapperService.updateNursingStationWardMapper(nursingStationWardMapperDto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/getNursingStationWardMapperById/id/{id}/orgId/{orgId}/unitId/{unitId}", method = RequestMethod.GET)
	public Response getNursingStationWardMapperById(@PathVariable("id") Integer id,@PathVariable("orgId") Integer orgId,@PathVariable("unitId") Integer unitId) {
		try {
			return iNursingStationWardMapperService.getNursingStationWardMapperById(id,orgId,unitId);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/getNursingStationWardMapperCount", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNursingStationWardMapperCount(@RequestBody NursingStationWardMapperDto nursingStationWardMapperDto) {
		try {
			return iNursingStationWardMapperService.getNursingStationWardMapperCount(nursingStationWardMapperDto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@RequestMapping(value="/getWardList/userId/{userId}", method = RequestMethod.GET)
	public Response getWardListByUserId(@PathVariable("userId") Integer userId,HttpServletRequest request) {
		try {
				if(request.getCookies()!=null)
	        	{
	        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
	                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
	        		 if (cookies != null && cookies.size() > 0) {
	                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.get(0).getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
	                 }
	        	}
			return iNursingStationWardMapperService.getWardListByUserId(userId,sessionService.getOrganizationId(),sessionService.getUnitId());
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/getWardListByNursingStationId/{nursingStationId}/orgId/{orgId}/unitId/{unitId}", method = RequestMethod.GET)
	public Response getWardListByNursingStationId(@PathVariable("nursingStationId") Integer nursingStationId,@PathVariable("orgId") Integer orgId,@PathVariable("unitId") Integer unitId) {
		try {
			return iNursingStationWardMapperService.getWardListByNursingStationId(nursingStationId,orgId,unitId);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getNursingStationListByWardId/{wardId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public Response getNursingStationListByWardId(@PathVariable("wardId") Integer wardId,@PathVariable("orgId") Integer orgId,@PathVariable("unitId") Integer unitId) {
		try {
			return iNursingStationWardMapperService.getNursingStationListByWardId(wardId,orgId,unitId);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
}

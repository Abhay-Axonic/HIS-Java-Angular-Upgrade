package com.param.global.unit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.UserCounterMapperDto;
import com.param.global.service.IUserCounterMapperService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping("/api/global")
public class UserCounterMapperController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(UserCounterMapperController.class);


	
	@Autowired
	private IUserCounterMapperService iUserCounterMapperService;
	
	
	@RequestMapping(value = "/userCounterMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response addUserCounterMapper(@RequestBody UserCounterMapperDto userCounterMapperDto){
		try{
			
			return iUserCounterMapperService.addUserCounterMapper(userCounterMapperDto);
		}catch(Exception e){
		logger.error("addUserCounterMapper failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/userCounterMapper" , method = RequestMethod.PUT , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateUserCounterMapper(@RequestBody UserCounterMapperDto userCounterMapperDto){
		try{
			return iUserCounterMapperService.updateUserCounterMapper(userCounterMapperDto);
		}catch(Exception e){
		logger.error("updateUserCounterMapper failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/userCounterMapper/{orgId}/{unitId}/{offset}/{recordPerPage}" , method = RequestMethod.GET )
	public Response listUserCounterMapper(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int offset = Integer.parseInt(params.get("offset").trim());
			int recordPerPage = Integer.parseInt(params.get("recordPerPage").trim());
			return iUserCounterMapperService.listUserCounterMapper(orgId, unitId, offset, recordPerPage);
		}catch(Exception e){
		logger.error("listUserCounterMapper failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	
	@RequestMapping(value = "/userCounterMapper/size/{orgId}/{unitId}" , method = RequestMethod.GET )
	public Response getTotalUserCounterMapperCount(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			return iUserCounterMapperService.getTotalUserCounterMapperCount(orgId, unitId);
		}catch(Exception e){
		logger.error("getTotalUserCounterMapperCount failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/userCounterMapper/status/{userId}/{status}" , method = RequestMethod.GET )
	public Response activateInactivateUserCounterMapper(@PathVariable Map<String,String> params){
		try{
			int userId = Integer.parseInt(params.get("userId").trim());
			char status = params.get("status").trim().charAt(0);
			return iUserCounterMapperService.activateInactivateUserCounterMapper(userId, status);
		}catch(Exception e){
		logger.error("activateInactivateUserCounterMapper failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/userCounterMapper/user/{orgId}/{unitId}/{userId}" , method = RequestMethod.GET )
	public Response getUserCounterMapperById(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int userId = Integer.parseInt(params.get("userId").trim());
			return iUserCounterMapperService.getUserCounterMapperById(orgId, unitId, userId);
		}catch(Exception e){
		logger.error("getUserCounterMapperById failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/userCounterMapper/userView/{orgId}/{unitId}/{userId}" , method = RequestMethod.GET )
	public Response getUserCounterMapperViewById(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int userId = Integer.parseInt(params.get("userId").trim());
			return iUserCounterMapperService.getUserCounterMapperViewById(orgId, unitId, userId);
		}catch(Exception e){
		logger.error("getUserCounterMapperViewById failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	
}

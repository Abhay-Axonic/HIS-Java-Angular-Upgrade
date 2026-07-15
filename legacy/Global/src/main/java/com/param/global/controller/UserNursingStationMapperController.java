package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.UserNursingStationMapperDto;
import com.param.global.service.IUserNursingStationMapperService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping("/api/global")
public class UserNursingStationMapperController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(UserNursingStationMapperController.class);


	
	@Autowired
	private IUserNursingStationMapperService iUserNursingStationMapperService;
	
	
	@RequestMapping(value = "/saveUserNursingStationMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveUserNursingStationMapper(@RequestBody UserNursingStationMapperDto userNursingStationMapperDto){
		try{
			return iUserNursingStationMapperService.saveUserNursingStationMapper(userNursingStationMapperDto);
			
		}catch(Exception e){
		logger.error("saveUserNursingStationMapper failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/updateUserNursingStationMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateUserNursingStationMapper(@RequestBody UserNursingStationMapperDto userNursingStationMapperDto){
		try{
			
			return iUserNursingStationMapperService.updateUserNursingStationMapper(userNursingStationMapperDto);
			
		}catch(Exception e){
		logger.error("updateUserNursingStationMapper failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/updateUserNursingStationMapperStatus/{userId}/{organisationId}/{unitId}/{status}" , method = RequestMethod.GET)
	public Response updateUserNursingStationMapperStatus(@PathVariable ("userId") Integer userId,
														@PathVariable ("organisationId") Integer organisationId,
														@PathVariable ("unitId") Integer unitId,
														@PathVariable ("status") Character status){
		try{
			return iUserNursingStationMapperService.updateUserNursingStationMapperStatus(userId, organisationId, unitId, status);
		}catch(Exception e){
		logger.error("updateUserNursingStationMapperStatus failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getUserNursingStationMapperlist" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE )
	public Response getUserNursingStationMapperlist(@RequestBody UserNursingStationMapperDto userNursingStationMapperDto){
		try{
			return iUserNursingStationMapperService.getUserNursingStationMapperlist(userNursingStationMapperDto);
		}catch(Exception e){
		logger.error("getUserNursingStationMapperlist failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getUserNursingStationMapperCount/{organisationId}/{unitId}" , method = RequestMethod.GET )
	public Response getUserNursingStationMapperCount(@PathVariable ("organisationId") Integer organisationId,
											  @PathVariable ("unitId") Integer unitId){
		try{
			return iUserNursingStationMapperService.getUserNursingStationMapperCount(organisationId, unitId);
		}catch(Exception e){
		logger.error("getUserNursingStationMapperCount failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveUserNursingStationMapper/{organisationId}/{unitId}" , method = RequestMethod.GET )
	public Response getActiveUserNursingStationMapper(@PathVariable ("organisationId") Integer organisationId,
												@PathVariable ("unitId") Integer unitId){
		try{
				return iUserNursingStationMapperService.getActiveUserNursingStationMapper(organisationId, unitId);
		}catch(Exception e){
		logger.error("getActiveUserNursingStationMapper failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getUserNursingStationMapperById/{userId}/{organisationId}/{unitId}" , method = RequestMethod.GET)
	public Response getUserNursingStationMapperById(@PathVariable ("userId") Integer userId,
														@PathVariable ("organisationId") Integer organisationId,
														@PathVariable ("unitId") Integer unitId){
		try{
			return iUserNursingStationMapperService.getUserNursingStationMapperById(userId, organisationId, unitId);
		}catch(Exception e){
		logger.error("getUserNursingStationMapperById failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getUserList/{organisationId}/{unitId}" , method = RequestMethod.GET)
	public Response getUserList(@PathVariable ("organisationId") Integer organisationId,
									@PathVariable ("unitId")Integer unitId){
		try{
			return iUserNursingStationMapperService.getUserList(organisationId, unitId);
			
		}catch(Exception e){
		logger.error("getUserList failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getDetailedNursingStationList/{userId}/{organisationId}/{unitId}", method = RequestMethod.GET)
	public Response getDetailedNursingStationList(@PathVariable ("userId") Integer userId,
														@PathVariable ("organisationId") Integer organisationId,
														@PathVariable ("unitId") Integer unitId){
		try{
			return iUserNursingStationMapperService.getDetailedNursingStationList(userId, organisationId, unitId);
		}catch(Exception e){
		logger.error("getDetailedNursingStationList failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);

	}
	
	@RequestMapping(value = "/getNursingStationList/{organisationId}/{unitId}", method = RequestMethod.GET)
	public Response getNursingStationList(@PathVariable ("organisationId") Integer organisationId,
												  @PathVariable ("unitId") Integer unitId){
		try{
			return iUserNursingStationMapperService.getNursingStationList(organisationId, unitId);
		}catch(Exception e){
		logger.error("getNursingStationList failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);

	}
	
	@RequestMapping(value = "/getUserListForStore/{organisationId}/{unitId}" , method = RequestMethod.GET)
	public Response getUserListForStore(@PathVariable ("organisationId") Integer organisationId,
									@PathVariable ("unitId")Integer unitId){
		try{
			return iUserNursingStationMapperService.getUserListForStore(organisationId, unitId);
			
		}catch(Exception e){
		logger.error("getUserListForStore failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	
}
	
	


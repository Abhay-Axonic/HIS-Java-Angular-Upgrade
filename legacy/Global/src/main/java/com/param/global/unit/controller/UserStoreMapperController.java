package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.UserStoreMapperDto;
import com.param.global.unit.service.IUserStoreMapperService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping("/api/global")
public class UserStoreMapperController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(UserStoreMapperController.class);

	@Autowired
	private IUserStoreMapperService iUserStoreMapperService;
	
	
	@RequestMapping(value = "/saveUserStoreMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveUserStoreMapper(@RequestBody UserStoreMapperDto userStoreMapperDto){
		try{
			return iUserStoreMapperService.saveUserStoreMapper(userStoreMapperDto);
			
		}catch(Exception e){
		logger.error("saveUserStoreMapper failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/updateUserStoreMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateUserStoreMapper(@RequestBody UserStoreMapperDto userStoreMapperDto){
		try{
			return iUserStoreMapperService.updateUserStoreMapper(userStoreMapperDto);
			
		}catch(Exception e){
		logger.error("updateUserStoreMapper failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/updateUserStoreMapperStatus/{userId}/{organizationId}/{unitId}/{isActive}" , method = RequestMethod.GET)
	public Response updateUserStoreMapperStatus(@PathVariable ("userId") Integer userId,
														@PathVariable ("organizationId") Integer organizationId,
														@PathVariable ("unitId") Integer unitId,
														@PathVariable ("isActive") Boolean isActive){
		try{
			return iUserStoreMapperService.updateUserStoreMapperStatus(userId, organizationId, unitId, isActive);
		}catch(Exception e){
		logger.error("updateUserStoreMapperStatus failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getUserStoreMapperlist" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE )
	public Response getUserStoreMapperlist(@RequestBody UserStoreMapperDto userStoreMapperDto){
		try{
			return iUserStoreMapperService.getUserStoreMapperlist(userStoreMapperDto);
		}catch(Exception e){
		logger.error("getUserStoreMapperlist failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	@RequestMapping(value = "/getUserStoreMapperlist/userNameWise" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE )
	public Response getUserStoreMapperlistUserNameWise(@RequestBody UserStoreMapperDto userStoreMapperDto){
		try{
			return iUserStoreMapperService.getUserStoreMapperlistUserNameWise(userStoreMapperDto);
		}catch(Exception e){
		logger.error("getUserStoreMapperlistUserNameWise failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getUserStoreMapperCount/{organizationId}/{unitId}" , method = RequestMethod.GET )
	public Response getUserStoreMapperCount(@PathVariable ("organizationId") Integer organizationId,
											  @PathVariable ("unitId") Integer unitId){
		try{
			return iUserStoreMapperService.getUserStoreMapperCount(organizationId, unitId);
		}catch(Exception e){
		logger.error("getUserStoreMapperCount failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveUserStoreMapper/{organizationId}/{unitId}" , method = RequestMethod.GET )
	public Response getActiveUserStoreMapper(@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId){
		try{
				return iUserStoreMapperService.getActiveUserStoreMapper(organizationId, unitId);
		}catch(Exception e){
		logger.error("getActiveUserStoreMapper failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getUserStoreMapperById/{userId}/{organizationId}/{unitId}" , method = RequestMethod.GET)
	public Response getUserStoreMapperById(@PathVariable ("userId") Integer userId,
														@PathVariable ("organizationId") Integer organizationId,
														@PathVariable ("unitId") Integer unitId){
		try{
			return iUserStoreMapperService.getUserStoreMapperById(userId, organizationId, unitId);
		}catch(Exception e){
		logger.error("getUserStoreMapperById failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	/*@RequestMapping(value = "/getUserList/{organizationId}/{unitId}" , method = RequestMethod.GET)
	public Response getUserList(@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId")Integer unitId){
		try{
			return iUserStoreMapperService.getUserList(organizationId, unitId);
			
		}catch(Exception e){
			
			return new Response<>(ERROR, null, null, null, null);
		}
	}*/
	
	@RequestMapping(value = "/getDetailedStoreList/{userId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getDetailedStoreList(@PathVariable ("userId") Integer userId,
														@PathVariable ("organizationId") Integer organizationId,
														@PathVariable ("unitId") Integer unitId){
		try{
			return iUserStoreMapperService.getDetailedStoreList(userId, organizationId, unitId);
		}catch(Exception e){
		logger.error("getDetailedStoreList failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);

	}
	
	@RequestMapping(value = "/getStoreList/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getStoreList(@PathVariable ("organizationId") Integer organizationId,
												  @PathVariable ("unitId") Integer unitId){
		try{
			return iUserStoreMapperService.getStoreList(organizationId, unitId);
		}catch(Exception e){
		logger.error("getStoreList failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);

	}
}

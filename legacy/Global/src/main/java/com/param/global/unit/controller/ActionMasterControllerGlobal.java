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
import com.param.global.common.ServiceException;
import com.param.global.unit.dto.ActionMasterDto;
import com.param.global.unit.service.IActionMasterServiceGlobal;

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value="/api/global")
@SuppressWarnings({"rawtypes"})
public class ActionMasterControllerGlobal implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(ActionMasterControllerGlobal.class);

	@Autowired
	private IActionMasterServiceGlobal iActionMasterService;
	
	@RequestMapping(value = "/saveActionMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveActionMaster(@RequestBody ActionMasterDto actionMasterDto){
		
		try {
			return iActionMasterService.saveActionMaster(actionMasterDto);

		} catch (Exception e) {
			logger.error("saveActionMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/updateActionMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateActionMaster(@RequestBody ActionMasterDto actionMasterDto){
		
		try {
			return iActionMasterService.updateActionMaster(actionMasterDto);

		} catch (Exception e) {
			logger.error("updateActionMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/updateActionMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateActionMasterStatus(@RequestBody ActionMasterDto actionMasterDto){
		
		try {
			return iActionMasterService.updateActionMasterStatus(actionMasterDto);

		} catch (Exception e) {
			logger.error("updateActionMasterStatus failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getActionMasterList/{offset}/{noOfRecordsPerPage}", method = RequestMethod.GET)
	public Response getActionMasterList(@PathVariable ("offset") Integer offset,
			                            @PathVariable ("noOfRecordsPerPage") Integer noOfRecordsPerPage){
		
		try {
			return iActionMasterService.getActionMasterList(offset, noOfRecordsPerPage);

		} catch (Exception e) {
			logger.error("getActionMasterList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getActionMasterById/{actionId}", method = RequestMethod.GET)
	public Response getActionMasterById(@PathVariable ("actionId") Integer actionId){
		
		try {
			return iActionMasterService.getActionMasterById(actionId);

		} catch (Exception e) {
			logger.error("getActionMasterById failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getActionMasterCount", method = RequestMethod.GET)
	public Response getActionMasterCount(){
		
		try {
			return iActionMasterService.getActionMasterCount();

		} catch (Exception e) {
			logger.error("getActionMasterCount failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getActiveActionMaster", method = RequestMethod.GET)
	public Response getActiveActionMaster(){
		
		try {
			return iActionMasterService.getActiveActionMaster();

		} catch (Exception e) {
			logger.error("getActiveActionMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value = "/searchActionMasterByParentActionName", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchActionMasterByParentActionName(@RequestBody ActionMasterDto actionMasterDto){
		
		try {
			return iActionMasterService.searchActionMasterByParentActionName(actionMasterDto);

		} catch (Exception e) {
			logger.error("searchActionMasterByParentActionName failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
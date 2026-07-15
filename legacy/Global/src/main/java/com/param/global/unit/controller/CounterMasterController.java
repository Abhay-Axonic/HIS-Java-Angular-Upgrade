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
import com.param.global.unit.dto.CounterMasterDto;
import com.param.global.unit.service.ICounterMasterService;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global/unit")
@SuppressWarnings({"rawtypes"})
public class CounterMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(CounterMasterController.class);

	@Autowired
	private ICounterMasterService iCounterMasterService;
	
	@RequestMapping(value = "/saveCounterMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveCounterMaster(@RequestBody CounterMasterDto counterMasterDto){
		
		try {
			return iCounterMasterService.saveCounterMaster(counterMasterDto);
			
		} catch (Exception e) {
		logger.error("saveCounterMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateCounterMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateCounterMaster(@RequestBody CounterMasterDto counterMasterDto){
		
		try {
			return iCounterMasterService.updateCounterMaster(counterMasterDto);
			
		} catch (Exception e) {
		logger.error("updateCounterMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateCounterMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateCounterMasterStatus(@RequestBody CounterMasterDto counterMasterDto){
		
		try {
			return iCounterMasterService.updateCounterMasterStatus(counterMasterDto);
			
		} catch (Exception e) {
		logger.error("updateCounterMasterStatus failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getCounterMasterById/{counterId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getCounterMasterById(@PathVariable ("counterId") Integer counterId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		
		try {
			return iCounterMasterService.getCounterMasterById(counterId, organizationId, unitId);
			
		} catch (Exception e) {
		logger.error("getCounterMasterById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getCounterMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCounterMasterList(@RequestBody CounterMasterDto counterMasterDto){

		try {
			return iCounterMasterService.getCounterMasterList(counterMasterDto);

		} catch (Exception e) {
		logger.error("getCounterMasterList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getCounterMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCounterMasterCount(@RequestBody CounterMasterDto counterMasterDto){
		
		try {
			return iCounterMasterService.getCounterMasterCount(counterMasterDto);
			
		} catch (Exception e) {
		logger.error("getCounterMasterCount failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveCounterMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveCabinMaster(@PathVariable ("organizationId") Integer organizationId,
										 @PathVariable ("unitId") Integer unitId){
		
		try {
			return iCounterMasterService.getActiveCounterMaster(organizationId, unitId);
			
		} catch (Exception e) {
		logger.error("getActiveCabinMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/counter/organization/{organizationId}/unit/{unitId}", method = RequestMethod.GET)
	public Response getCountersList(@PathVariable ("organizationId") Integer organizationId,
			@PathVariable ("unitId") Integer unitId){
		
		try {
			return iCounterMasterService.getCountersList(organizationId,unitId);
			
		} catch (Exception e) {
		logger.error("getCountersList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
}

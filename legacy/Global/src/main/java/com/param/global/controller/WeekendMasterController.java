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
import com.param.global.dto.WeekendMasterDto;
import com.param.global.service.IWeekendMasterService;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/api/global/unit")
@SuppressWarnings({"rawtypes","unchecked"})
public class WeekendMasterController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(WeekendMasterController.class);


	@Autowired
	IWeekendMasterService iWeekendMasterService; 
	
	@RequestMapping(value="/saveWeekendMaster", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveWeekendMaster(@RequestBody WeekendMasterDto weekendMasterDto) {
		try {
				return iWeekendMasterService.saveWeekendMaster(weekendMasterDto);
			
		}catch (Exception e) {
		logger.error("saveWeekendMaster failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getWeekendMasterList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getWeekendMasterList(@RequestBody WeekendMasterDto weekendMasterDto) {
		try {
				return iWeekendMasterService.getWeekendMasterList(weekendMasterDto);
			
		}catch (Exception e) {
		logger.error("getWeekendMasterList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/updateWeekendMaster", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateWeekendMaster(@RequestBody WeekendMasterDto weekendMasterDto) {
		try {
				return iWeekendMasterService.updateWeekendMaster(weekendMasterDto);
			
		}catch (Exception e) {
		logger.error("updateWeekendMaster failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
		
	@RequestMapping(value="/updateWeekendMasterStatus", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateWeekendMasterStatus(@RequestBody WeekendMasterDto weekendMasterDto) {
		try {
				return iWeekendMasterService.updateWeekendMasterStatus(weekendMasterDto);
			
		}catch (Exception e) {
		logger.error("updateWeekendMasterStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getWeekendMasterById/{weekendId}/{organizationId}/{unitId}", method=RequestMethod.GET)
	public Response getWeekendMasterById(@PathVariable ("weekendId") Integer weekendId,
													   @PathVariable ("organizationId") Integer organizationId,
													   @PathVariable ("unitId") Integer unitId) {
		try {
				return iWeekendMasterService.getWeekendMasterById(weekendId, organizationId, unitId);
			
		}catch (Exception e) {
		logger.error("getWeekendMasterById failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getListOfWeekendMaster", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getListOfWeekendMaster(@RequestBody WeekendMasterDto weekendMasterDto) {
		try {
				return iWeekendMasterService.getListOfWeekendMaster(weekendMasterDto);
			
		}catch (Exception e) {
		logger.error("getListOfWeekendMaster failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getWeekendMasterCount", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getWeekendMasterCount(@RequestBody WeekendMasterDto weekendMasterDto) {
		try {
				return iWeekendMasterService.getWeekendMasterCount(weekendMasterDto);
			
		}catch (Exception e) {
		logger.error("getWeekendMasterCount failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}

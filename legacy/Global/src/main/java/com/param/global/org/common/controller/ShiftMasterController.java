package com.param.global.org.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dto.DoseMasterDto;
import com.param.global.org.common.dto.ShiftMasterDto;
import com.param.global.org.common.service.IShiftMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "unchecked", "rawtypes" })
@RequestMapping(value="/api/common")
@RestController
public class ShiftMasterController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(ShiftMasterController.class);


	@Autowired
	private IShiftMasterService iShiftMasterService;
	
	@RequestMapping(value = "/saveShiftMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveShiftMaster(@RequestBody ShiftMasterDto shiftMasterDto) {
		try {
			return iShiftMasterService.saveShiftMaster(shiftMasterDto);
		}catch(Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getShiftMasterList", method = RequestMethod.POST)
	public @ResponseBody Response getShiftMasterList(@RequestBody ShiftMasterDto shiftMasterDto) {
		try {
			return iShiftMasterService.getShiftMasterList(shiftMasterDto);
		}catch(Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateShiftMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateShiftMaster(@RequestBody ShiftMasterDto shiftMasterDto) {
		try {
			return iShiftMasterService.updateShiftMaster(shiftMasterDto);
		}catch(Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateShiftMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateShiftMasterStatus(@RequestBody ShiftMasterDto shiftMasterDto) {
		try {
			return iShiftMasterService.updateShiftMasterStatus(shiftMasterDto);
		}catch(Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getShiftMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getShiftMasterCount(@RequestBody ShiftMasterDto shiftMasterDto) {
		try {
			return iShiftMasterService.getShiftMasterCount(shiftMasterDto);
		}catch(Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getShiftMasterById/{shiftId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getShiftMasterById(@PathVariable ("shiftId") Integer shiftId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		try {
			return iShiftMasterService.getShiftMasterById(shiftId,organizationId,unitId);
		}catch(Exception e) {
		logger.error("getShiftMasterById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getActiveShiftMasterList/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveShiftMasterList(@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		try {
			return iShiftMasterService.getActiveShiftMasterList(organizationId,unitId);
		}catch(Exception e) {
		logger.error("getActiveShiftMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getShiftlistByTime", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getShiftlistByTime(@RequestBody ShiftMasterDto shiftMasterDto) {
		try {
			return iShiftMasterService.getShiftlistByTime(shiftMasterDto);
		}catch(Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}

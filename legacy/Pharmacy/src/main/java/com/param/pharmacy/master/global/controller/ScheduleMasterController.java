package com.param.pharmacy.master.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import com.param.pharmacy.master.global.dto.equipment.ScheduleMasterDto;
import com.param.pharmacy.master.global.service.equipment.IScheduleMasterService;

@RestController
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping(value = "/Pharmacy/global")
public class ScheduleMasterController implements ICommonConstants, IError {

	@Autowired
	private IScheduleMasterService iScheduleMasterService;

	private static final Logger logger = Logger.getLogger(ScheduleMasterController.class);

	@RequestMapping(value = "/saveScheduleMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveScheduleMaster(@RequestBody ScheduleMasterDto scheduleMasterDto) {
		Response<ScheduleMasterDto, Integer> response = null;
		try {
			response = iScheduleMasterService.saveScheduleMaster(scheduleMasterDto);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

   @RequestMapping(value = "/getSchedulebyId/{orgId}/{unitId}/{scheduleId}", method = RequestMethod.GET)
	public @ResponseBody Response getScheduleMasterById(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId, @PathVariable(value = "scheduleId") Integer scheduleId) {
		Response<ScheduleMasterDto, Integer> response = null;
		try {
			response = iScheduleMasterService.getScheduleMasterById(orgId, unitId, scheduleId);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateScheduleTest", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateScheduleMaster(@RequestBody ScheduleMasterDto scheduleMasterDto) {
		Response<ScheduleMasterDto, Integer> response = null;
		try {
			response = iScheduleMasterService.updateScheduleMaster(scheduleMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/activeInactiveSchedule/{orgId}/{scheduleId}/{scheduleStatus}", method = RequestMethod.GET)
	public @ResponseBody Response activeInactiveSchedule(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "scheduleId") Integer scheduleId,
			@PathVariable(value = "scheduleStatus") Character scheduleStatus) {

		Response<ScheduleMasterDto, Integer> response = null;
		try {
			response = iScheduleMasterService.ActivateInactivateScheduleMaster(orgId, scheduleId,
					scheduleStatus);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/listScheduleMaster/{orgId}/{unitId}/{offset}/{recordPerPage}", method = RequestMethod.GET)
	public @ResponseBody Response listScheduleMaster(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId, 
			@PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage) {
		Response<ScheduleMasterDto, Integer> response = null;
		try {
			response = iScheduleMasterService.listScheduleMaster(orgId, unitId, offset, recordPerPage);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getTotalScheduleMasterRecord/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getTotalScheduleMasterRecord(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId) {
		Response<ScheduleMasterDto, Integer> response = null;
		try {
			response = iScheduleMasterService.getToTalScheduleMasterRecord(orgId, unitId);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}

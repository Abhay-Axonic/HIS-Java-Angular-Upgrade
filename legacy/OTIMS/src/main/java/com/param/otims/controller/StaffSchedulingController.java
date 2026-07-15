package com.param.otims.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.StaffSchedulingDto;
import com.param.otims.service.IStaffSchedulingService;

@RestController
@RequestMapping("/global/otims")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StaffSchedulingController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(StaffSchedulingController.class);

	@Autowired
	IStaffSchedulingService iStaffSchedulingService;

	@RequestMapping(value = "/saveStaffScheduling", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveStaffScheduling(@RequestBody StaffSchedulingDto staffSchedulingDto) {
		try {
			return iStaffSchedulingService.saveStaffScheduling(staffSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@RequestMapping(value = "/getStaffScheduling", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getStaffScheduling(@RequestBody StaffSchedulingDto staffSchedulingDto) {
		try {
			return iStaffSchedulingService.getStaffScheduling(staffSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/deactivateStaffSchedule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response deactivateStaffSchedule(@RequestBody StaffSchedulingDto staffSchedulingDto) {
		try {
			return iStaffSchedulingService.deactivateStaffSchedule(staffSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getStaffListByRoleId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getStaffListByRoleId(@RequestBody StaffSchedulingDto staffSchedulingDto) {
		try {
			return iStaffSchedulingService.getStaffListByRoleId(staffSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/checkStaffAvailableOrNot", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response checkStaffAvailableOrNot(@RequestBody StaffSchedulingDto staffSchedulingDto) {
		try {
			return iStaffSchedulingService.checkStaffAvailableOrNot(staffSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getOTIMSStaffList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOTIMSStaffList(@RequestBody StaffSchedulingDto staffSchedulingDto) {
		try {
			return iStaffSchedulingService.getOTIMSStaffList(staffSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@RequestMapping(value = "/checkStaffListAvailableOrNot", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response checkStaffListAvailableOrNot(@RequestBody List<StaffSchedulingDto> staffSchedulingDto) {
		try {
			return iStaffSchedulingService.checkStaffListAvailableOrNot(staffSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}

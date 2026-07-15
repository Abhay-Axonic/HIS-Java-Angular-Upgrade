package com.param.otims.controller;

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
import com.param.global.otims.dto.OtSchedulingDto;
import com.param.otims.service.IOtSchedulingService;

@RestController
@RequestMapping("/global/otims")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OtSchedulingController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(OtSchedulingController.class);

	@Autowired
	IOtSchedulingService iOtSchedulingService;

	@RequestMapping(value = "/saveOtScheduling", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveOtScheduling(@RequestBody OtSchedulingDto otSchedulingDto) {
		try {
			return iOtSchedulingService.saveOtScheduling(otSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@RequestMapping(value = "/getOtScheduling", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOtScheduling(@RequestBody OtSchedulingDto otSchedulingDto) {
		try {
			return iOtSchedulingService.getOtScheduling(otSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/deactivateOtSchedule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response deactivateOtSchedule(@RequestBody OtSchedulingDto otSchedulingDto) {
		try {
			return iOtSchedulingService.deactivateOtSchedule(otSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/toCheckSlotAvailability", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response toCheckSlotAvailability(@RequestBody OtSchedulingDto otSchedulingDto) {
		try {
			return iOtSchedulingService.toCheckSlotAvailability(otSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getOtSchedulingForOTcalender", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOtSchedulingForOTcalender(@RequestBody OtSchedulingDto otSchedulingDto) {
		try {
			return iOtSchedulingService.getOtSchedulingForOTcalender(otSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}

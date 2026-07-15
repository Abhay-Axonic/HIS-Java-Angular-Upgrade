package com.param.global.master.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.service.IWeekDaysMasterService;

@RestController
@RequestMapping("/api/kitchen/master/weekDaysMaster")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WeekDaysMasterController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(WeekDaysMasterController.class);

	@Autowired
	IWeekDaysMasterService iWeekDaysMasterService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getWeekDaysMasterList() {
		try {
			return iWeekDaysMasterService.getAllWeekDaysMaster();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}

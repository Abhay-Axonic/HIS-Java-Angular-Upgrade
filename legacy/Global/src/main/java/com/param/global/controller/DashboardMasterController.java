package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.DashboardMasterDto;
import com.param.global.service.IDashboardMasterService;

@RestController
@RequestMapping("api/")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DashboardMasterController implements ICommonConstants{
	private static final Logger logger = Logger.getLogger(DashboardMasterController.class);

	@Autowired
	private IDashboardMasterService iDashboardMasterService;

	@RequestMapping(value="/getDashboardMasterList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getDashboardMasterList(@RequestBody DashboardMasterDto dashboardMasterDto) {
		try {
			return iDashboardMasterService.getDashboardMasterList(dashboardMasterDto);
		} catch (Exception e) {
			logger.error("Error while fetching the dashboard master list", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/getDashboardMasterListByRoleId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getDashboardMasterListByRoleId(@RequestBody DashboardMasterDto dashboardMasterDto) {
		try {
			return iDashboardMasterService.getDashboardMasterListByRoleId(dashboardMasterDto);
		} catch (Exception e) {
			logger.error("Error while fetching the dashboard master list by role id", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}

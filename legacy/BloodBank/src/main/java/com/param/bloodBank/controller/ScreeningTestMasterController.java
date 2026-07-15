package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.ScreeningTestMasterDto;
import com.param.bloodBank.service.IScreeningTestMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ScreeningTestMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(ScreeningTestMasterController.class);

	@Autowired
	IScreeningTestMasterService iScreeningTestMasterService;

	@RequestMapping(value = "/saveScreeningTestMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveScreeningTestMaster(@RequestBody ScreeningTestMasterDto screeningTestMasterDto) {
		try {
			return iScreeningTestMasterService.saveScreeningTestMaster(screeningTestMasterDto);
		} catch (Exception e) {
			logger.error("saveScreeningTestMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateScreeningTestMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateScreeningTestMaster(@RequestBody ScreeningTestMasterDto screeningTestMasterDto) {
		try {
			return iScreeningTestMasterService.updateScreeningTestMaster(screeningTestMasterDto);
		} catch (Exception e) {
			logger.error("updateScreeningTestMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateScreeningTestMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateScreeningTestMasterStatus(@RequestBody ScreeningTestMasterDto screeningTestMasterDto) {
		try {
			return iScreeningTestMasterService.updateScreeningTestMasterStatus(screeningTestMasterDto);
		} catch (Exception e) {
			logger.error("updateScreeningTestMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getScreeningTestMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getScreeningTestMasterList(@RequestBody ScreeningTestMasterDto screeningTestMasterDto) {
		try {
			return iScreeningTestMasterService.getScreeningTestMasterList(screeningTestMasterDto);
		} catch (Exception e) {
			logger.error("getScreeningTestMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getScreeningTestMasterById/{screeningTestId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getScreeningTestMasterById(@PathVariable ("screeningTestId") Integer screeningTestId,
												@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId) {
		try {
			return iScreeningTestMasterService.getScreeningTestMasterById(screeningTestId, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getScreeningTestMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveScreeningTestMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveScreeningTestMaster(@PathVariable ("organizationId") Integer organizationId,
												 @PathVariable ("unitId") Integer unitId) {
		try {
			return iScreeningTestMasterService.getActiveScreeningTestMaster(organizationId, unitId);
		} catch (Exception e) {
			logger.error("getActiveScreeningTestMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getScreeningTestMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getScreeningTestMasterCount(@RequestBody ScreeningTestMasterDto screeningTestMasterDto) {
		try {
			return iScreeningTestMasterService.getScreeningTestMasterCount(screeningTestMasterDto);
		} catch (Exception e) {
			logger.error("getScreeningTestMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}


package com.param.CSSDMaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.CSSDMaster.dto.SterilizationTypeMasterDto;
import com.param.CSSDMaster.service.ISterilizationTypeMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/cssd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SterilizationTypeMasterController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(SterilizationTypeMasterController.class);


	@Autowired
	private ISterilizationTypeMasterService iSterilizationTypeMasterService;

	@RequestMapping(value = "/updateSterilizationType", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateResponseBody(@RequestBody SterilizationTypeMasterDto MasterDto) {
		try {
			return iSterilizationTypeMasterService.updateMaster(MasterDto);
		} catch (Exception e) {
			logger.error("updateResponseBody failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateSterilizationTypeStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateSterilizationTypeStatus(@RequestBody SterilizationTypeMasterDto MasterDto) {
		try {
			return iSterilizationTypeMasterService.updateMasterStatus(MasterDto);
		} catch (Exception e) {
			logger.error("updateSterilizationTypeStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/createSterilizationType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response createSterilizationType(@RequestBody SterilizationTypeMasterDto MasterDto) {
		try {
			return iSterilizationTypeMasterService.createMaster(MasterDto);
		} catch (Exception e) {
			logger.error("createSterilizationType failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getSterilizationTypesList", method = RequestMethod.GET)
	public @ResponseBody Response getSterilizationTypesList(@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId ,@RequestParam("offset")Integer offset ,
			@RequestParam("noOfRecordsPerPage")Integer noOfRecordsPerPage) {
		try {
			return iSterilizationTypeMasterService.getMasterList(organizationId,unitId,offset,noOfRecordsPerPage);
		} catch (Exception e) {
			logger.error("getSterilizationTypesList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveSterilizationTypesList", method = RequestMethod.GET)
	public @ResponseBody Response getActiveSterilizationTypesList(@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId) {
		try {
			return iSterilizationTypeMasterService.getActiveMasterList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getActiveSterilizationTypesList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getsterilizationtypeslistcount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSterilizationTypesListCount(
			@RequestBody SterilizationTypeMasterDto sterilizationTypeMasterDto) {
		try {
			return iSterilizationTypeMasterService.getSterilizationTypesListCount(sterilizationTypeMasterDto);
		} catch (Exception e) {
			logger.error("getSterilizationTypesListCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}

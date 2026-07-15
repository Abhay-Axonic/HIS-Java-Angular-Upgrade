package com.param.CSSDMaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.CSSDMaster.dto.AutoclaveMasterDto;
import com.param.CSSDMaster.dto.SterilizationTypeMasterDto;
import com.param.CSSDMaster.service.IAutoclaveMasterService;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/cssd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AutoclaveMasterController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(AutoclaveMasterController.class);


	@Autowired
	private IAutoclaveMasterService iAutoclaveMasterService;

	@RequestMapping(value = "/updateAutoclave", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateResponseBody(@RequestBody AutoclaveMasterDto MasterDto) {
		try {
			return iAutoclaveMasterService.updateMaster(MasterDto);
		} catch (Exception e) {
			logger.error("updateResponseBody failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateAutoclaveStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAutoclaveStatus(@RequestBody AutoclaveMasterDto MasterDto) {
		try {
			return iAutoclaveMasterService.updateAutoclaveStatus(MasterDto);
		} catch (Exception e) {
			logger.error("updateAutoclaveStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/createAutoclave", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response createAutoclave(@RequestBody AutoclaveMasterDto MasterDto) {
		try {
			return iAutoclaveMasterService.createMaster(MasterDto);
		} catch (Exception e) {
			logger.error("createAutoclave failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getAllAutoclavesList", method = RequestMethod.GET)
	public @ResponseBody Response getAllAutoclavesList(@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId ,@RequestParam("offset")Integer offset ,
			@RequestParam("noOfRecordsPerPage")Integer noOfRecordsPerPage) {
		try {
			return iAutoclaveMasterService.getAllMasterList(organizationId,unitId,offset,noOfRecordsPerPage);
		} catch (Exception e) {
			logger.error("getAllAutoclavesList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveAutoclavesList", method = RequestMethod.GET)
	public @ResponseBody Response getActiveAutoclavesList(@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId) {
		try {
			return iAutoclaveMasterService.getActiveMasterList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getActiveAutoclavesList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getAutoclavesListcount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAutoclavesListcount(
			@RequestBody AutoclaveMasterDto autoclaveMasterDto) {
		try {
			return iAutoclaveMasterService.getAutoclavesListcount(autoclaveMasterDto);
		} catch (Exception e) {
			logger.error("getAutoclavesListcount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveSterilizationColorList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveSterilizationColorList(
			@RequestBody AutoclaveMasterDto autoclaveMasterDto) {
		try {
			return iAutoclaveMasterService.getActiveSterilizationColorList(autoclaveMasterDto);
		} catch (Exception e) {
			logger.error("getActiveSterilizationColorList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
}

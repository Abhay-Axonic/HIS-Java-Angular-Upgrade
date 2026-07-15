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
import com.param.CSSDMaster.dto.WrapperExpiryMasterDto;
import com.param.CSSDMaster.service.IWrapperExpiryMasterService;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/cssd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WrapperExpiryMasterController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(WrapperExpiryMasterController.class);


	@Autowired
	private IWrapperExpiryMasterService iWrapperExpiryMasterService;

	@RequestMapping(value = "/updateWrapperExpiry", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateResponseBody(@RequestBody WrapperExpiryMasterDto MasterDto) {
		try {
			return iWrapperExpiryMasterService.updateMaster(MasterDto);
		} catch (Exception e) {
			logger.error("updateResponseBody failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateWrapperExpiryStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateWrapperExpiryStatus(@RequestBody WrapperExpiryMasterDto MasterDto) {
		try {
			return iWrapperExpiryMasterService.updateMasterStatus(MasterDto);
		} catch (Exception e) {
			logger.error("updateWrapperExpiryStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/createWrapperExpiry", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response createWrapperExpiry(@RequestBody WrapperExpiryMasterDto MasterDto) {
		try {
			return iWrapperExpiryMasterService.createMaster(MasterDto);
		} catch (Exception e) {
			logger.error("createWrapperExpiry failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getWrapperExpiryList", method = RequestMethod.GET)
	public @ResponseBody Response getWrapperExpiryList(@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId,
			@RequestParam(value="offset")Integer offset,
			@RequestParam(value="noOfRecordsPerPage")Integer noOfRecordsPerPage
			) {
		try {
			return iWrapperExpiryMasterService.getMasterList(organizationId,unitId,offset,noOfRecordsPerPage);
		} catch (Exception e) {
			logger.error("getWrapperExpiryList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveWrapperExpiryList", method = RequestMethod.GET)
	public @ResponseBody Response getActiveWrapperExpiryList(@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId ) {
		try {
			return iWrapperExpiryMasterService.getActiveMasterList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getActiveWrapperExpiryList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveWrapperExpiryByCode", method = RequestMethod.GET)
	public @ResponseBody Response getActiveWrapperExpiryByCode(@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId,@RequestParam(value="code")String code ) {
		try {
			return iWrapperExpiryMasterService.getActiveWrapperExpiryByCode(organizationId,unitId,code);
		} catch (Exception e) {
			logger.error("getActiveWrapperExpiryByCode failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getItemList/{organizationId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getItemList(@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId) {
		try {
			return iWrapperExpiryMasterService.getItemList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getItemList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getWrapperListcount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getWrapperListcount(
			@RequestBody WrapperExpiryMasterDto wrapperExpiryMasterDto) {
		try {
			return iWrapperExpiryMasterService.getWrapperListcount(wrapperExpiryMasterDto);
		} catch (Exception e) {
			logger.error("getWrapperListcount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}

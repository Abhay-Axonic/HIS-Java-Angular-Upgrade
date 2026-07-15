package com.param.global.otims.controller;

import org.apache.log4j.Logger;
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
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ResourceChargeConfigurationDto;
import com.param.global.otims.service.IResourceChargeConfigurationService;

@RestController
@RequestMapping("/api/otims")
@SuppressWarnings("rawtypes")
public class ResourceChargeConfigurationController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(ResourceChargeConfigurationController.class);

	@Autowired
	IResourceChargeConfigurationService iResourceChargeConfigurationService; 
	
	@RequestMapping(value = "/resourceChargeConfiguration", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveResourceChargeConfiguration(@RequestBody ResourceChargeConfigurationDto resourceChargeConfigurationDto){
		try {
			return iResourceChargeConfigurationService.saveResourceChargeConfiguration(resourceChargeConfigurationDto);
		} catch (Exception e) {
			logger.error("saveResourceChargeConfiguration failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateResourceChargeConfiguration", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateResourceChargeConfiguration(@RequestBody ResourceChargeConfigurationDto resourceChargeConfigurationDto){
		try {
			return iResourceChargeConfigurationService.updateResourceChargeConfiguration(resourceChargeConfigurationDto);

		} catch (Exception e) {
			logger.error("updateResourceChargeConfiguration failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/resourceChargeConfiguration/{resourceChargeConfigurationId}", method = RequestMethod.GET)
	public @ResponseBody Response getResourceChargeConfigurationById(@PathVariable("resourceChargeConfigurationId") Integer resourceChargeConfigurationId){
		try {
			return iResourceChargeConfigurationService.getResourceChargeConfigurationById(resourceChargeConfigurationId);

		} catch (Exception e) {
			logger.error("getResourceChargeConfigurationById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/resourceChargeConfiguration/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getResourceChargeConfigurationList(@PathVariable("orgId") Integer orgId,@PathVariable("unitId") Integer unitId){
		try {
			return iResourceChargeConfigurationService.getResourceChargeConfigurationList(orgId,unitId);

		} catch (Exception e) {
			logger.error("getResourceChargeConfigurationList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateResourceChargeConfigurationStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateResourceChargeConfigurationStatus(@RequestBody ResourceChargeConfigurationDto resourceChargeConfigurationDto){
		try {
			return iResourceChargeConfigurationService.updateResourceChargeConfigurationStatus(resourceChargeConfigurationDto);

		} catch (Exception e) {
			logger.error("updateResourceChargeConfigurationStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getResourceChareges", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getResourceChareges(@RequestBody ResourceChargeConfigurationDto resourceChargeConfigurationDto){
		try {
			return iResourceChargeConfigurationService.getResourceChareges(resourceChargeConfigurationDto);

		} catch (Exception e) {
			logger.error("getResourceChareges failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}

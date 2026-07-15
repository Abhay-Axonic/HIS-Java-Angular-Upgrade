package com.param.global.billing.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.billing.dto.AutoPostingServicesConfigDto;
import com.param.global.billing.service.IAutoPostingConfigService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("config/billing")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AutoPostingConfigController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(AutoPostingConfigController.class);

	@Autowired
	IAutoPostingConfigService iAutoPostingConfigService;
	
	@RequestMapping(value = "/master", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveAutoPostingConfig(@RequestBody AutoPostingServicesConfigDto postingServicesConfigDto) {
		try {
			return iAutoPostingConfigService.saveAutoPostingConfig(postingServicesConfigDto);
		} catch (Exception e) {
			logger.error("saveAutoPostingConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/list/{orgId}/{unitId}", method = RequestMethod.GET)
	public Response getAutoPostingConfigList(@PathVariable("orgId") Integer orgId,
			@PathVariable("unitId") Integer unitId) {
		try {
			return iAutoPostingConfigService.getAutoPostingConfigList(orgId,unitId);
		} catch (Exception e) {
			logger.error("getAutoPostingConfigList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateServiceAutoPostingStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateServiceAutoPostingStatus(@RequestBody AutoPostingServicesConfigDto postingServicesConfigDto) {
		try {
			return iAutoPostingConfigService.updateServiceAutoPostingStatus(postingServicesConfigDto);
		} catch (Exception e) {
			logger.error("updateServiceAutoPostingStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}

package com.param.global.unit.controller;

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
import com.param.global.unit.dto.MpesaApplicationConfigurationDto;
import com.param.global.unit.service.IMpesaAppConfigService;

@RestController
@RequestMapping(value="/api/global/unit")
@SuppressWarnings({"rawtypes"})
public class MpesaAppConfigController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(MpesaAppConfigController.class);

	@Autowired
	IMpesaAppConfigService iMpesaAppConfigService;

	@RequestMapping(value = "/saveMpesaAppConfig", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveMpesaAppConfig(@RequestBody MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto){

		try {
			return iMpesaAppConfigService.saveMpesaAppConfig(mpesaApplicationConfigurationDto);

		} catch (Exception e) {
			logger.error("saveMpesaAppConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateMpesaAppConfigStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateMpesaAppConfigStatus(@RequestBody MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto){

		try {
			return iMpesaAppConfigService.updateMpesaAppConfigStatus(mpesaApplicationConfigurationDto);

		} catch (Exception e) {
			logger.error("updateMpesaAppConfigStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getMpesaAppConfig", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getMpesaAppConfig(@RequestBody MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto){

		try {
			return iMpesaAppConfigService.getMpesaAppConfig(mpesaApplicationConfigurationDto);

		} catch (Exception e) {
			logger.error("getMpesaAppConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getMpesaAppConfigList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getMpesaAppConfigList(@RequestBody MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto){

		try {
			return iMpesaAppConfigService.getMpesaAppConfigList(mpesaApplicationConfigurationDto);

		} catch (Exception e) {
			logger.error("getMpesaAppConfigList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}

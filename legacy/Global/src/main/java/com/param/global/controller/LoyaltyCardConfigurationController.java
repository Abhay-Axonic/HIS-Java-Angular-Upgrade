package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.LoyaltyCardConfigurationDto;
import com.param.global.service.ILoyaltyCardConfigurationService;

@RestController
@RequestMapping(value="/api/unit")
@SuppressWarnings({"rawtypes"})
public class LoyaltyCardConfigurationController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(LoyaltyCardConfigurationController.class);

	@Autowired
	private ILoyaltyCardConfigurationService iLoyaltyCardConfigurationService;
	
	@RequestMapping(value = "/saveLoyaltyCardConfiguration", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveLoyaltyCardConfiguration(@RequestBody LoyaltyCardConfigurationDto loyaltyCardConfigurationDto){
		
		try {
			return iLoyaltyCardConfigurationService.saveLoyaltyCardConfiguration(loyaltyCardConfigurationDto);

		} catch (Exception e) {
			logger.error("Error while saving loyalty card configuration", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/updateLoyaltyCardConfigurationStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateLoyaltyCardConfigurationStatus(@RequestBody LoyaltyCardConfigurationDto loyaltyCardConfigurationDto){
		
		try {
			return iLoyaltyCardConfigurationService.updateLoyaltyCardConfigurationStatus(loyaltyCardConfigurationDto);

		} catch (Exception e) {
			logger.error("Error while updating loyalty card configuration status", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getLoyaltyCardConfigurationById/{cardConfigurationId}/{organisationId}/{unitId}", method = RequestMethod.GET)
	public Response getLoyaltyCardConfigurationById(@PathVariable ("cardConfigurationId") Integer cardConfigurationId,
									@PathVariable ("organisationId") Integer organisationId,
									@PathVariable ("unitId") Integer unitId){
		
		try {
			return iLoyaltyCardConfigurationService.getLoyaltyCardConfigurationById(cardConfigurationId, organisationId, unitId);

		} catch (Exception e) {
			logger.error("Error while fetching loyalty card configuration by id", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getLoyaltyCardConfigurationList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getLoyaltyCardConfigurationList(@RequestBody LoyaltyCardConfigurationDto loyaltyCardConfigurationDto){

		try {
			return iLoyaltyCardConfigurationService.getLoyaltyCardConfigurationList(loyaltyCardConfigurationDto);

		} catch (Exception e) {
			logger.error("Error while fetching loyalty card configuration list", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getLoyaltyCardConfigurationCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getLoyaltyCardConfigurationCount(@RequestBody LoyaltyCardConfigurationDto loyaltyCardConfigurationDto){
		
		try {
			return iLoyaltyCardConfigurationService.getLoyaltyCardConfigurationCount(loyaltyCardConfigurationDto);

		} catch (Exception e) {
			logger.error("Error while fetching loyalty card configuration count", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
}

package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.TubeFeedingProductsDto;
import com.param.global.service.ITubeFeedingProductsService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global")
@SuppressWarnings({"rawtypes"})
public class TubeFeedingProductsController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(TubeFeedingProductsController.class);

	@Autowired
	private ITubeFeedingProductsService iTubeFeedingProductsService;
	
	@RequestMapping(value = "/saveTubeFeedingProducts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveTubeFeedingProducts(@RequestBody TubeFeedingProductsDto tubeFeedingProductsDto){
		
		try {
			return iTubeFeedingProductsService.saveTubeFeedingProducts(tubeFeedingProductsDto);
			
		} catch (Exception e) {
		logger.error("saveTubeFeedingProducts failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateTubeFeedingProducts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateTubeFeedingProducts(@RequestBody TubeFeedingProductsDto tubeFeedingProductsDto){
		
		try {
			return iTubeFeedingProductsService.updateTubeFeedingProducts(tubeFeedingProductsDto);
			
		} catch (Exception e) {
		logger.error("updateTubeFeedingProducts failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateTubeFeedingProductsStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateTubeFeedingProductsStatus(@RequestBody TubeFeedingProductsDto tubeFeedingProductsDto){
		
		try {
			return iTubeFeedingProductsService.updateTubeFeedingProductsStatus(tubeFeedingProductsDto);
			
		} catch (Exception e) {
		logger.error("updateTubeFeedingProductsStatus failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getTubeFeedingProductsList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTubeFeedingProductsList(@RequestBody TubeFeedingProductsDto tubeFeedingProductsDto){
		
		try {
			return iTubeFeedingProductsService.getTubeFeedingProductsList(tubeFeedingProductsDto);
			
		} catch (Exception e) {
		logger.error("getTubeFeedingProductsList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getTubeFeedingProductsById/{tubeFeedingProductsId}/{organisationId}/{unitId}", method = RequestMethod.GET)
	public Response getTubeFeedingProductsById(@PathVariable ("tubeFeedingProductsId") Integer  tubeFeedingProductsId,
																				@PathVariable ("organisationId") Integer organisationId, @PathVariable ("unitId") Integer unitId){
		
		try {
			return iTubeFeedingProductsService.getTubeFeedingProductsById(tubeFeedingProductsId, organisationId,  unitId);
			
		} catch (Exception e) {
		logger.error("getTubeFeedingProductsById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveTubeFeedingProducts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveTubeFeedingProducts(@RequestBody TubeFeedingProductsDto tubeFeedingProductsDto){
		
		try {
			return iTubeFeedingProductsService.getActiveTubeFeedingProducts(tubeFeedingProductsDto);
			
		} catch (Exception e) {
		logger.error("getActiveTubeFeedingProducts failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getTubeFeedingProductsCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTubeFeedingProductsCount(@RequestBody TubeFeedingProductsDto tubeFeedingProductsDto){
		
		try {
			return iTubeFeedingProductsService.getTubeFeedingProductsCount(tubeFeedingProductsDto);
			
		} catch (Exception e) {
		logger.error("getTubeFeedingProductsCount failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
}

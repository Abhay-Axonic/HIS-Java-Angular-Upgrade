package com.param.global.procedure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.procedure.dto.AnesthesiaTypeMasterDto;
import com.param.global.procedure.service.IAnesthesiaTypeService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/otims")
@SuppressWarnings({"rawtypes"})
public class AnesthesiaTypeMasterController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(AnesthesiaTypeMasterController.class);

	@Autowired
	private IAnesthesiaTypeService iAnesthesiaTypeService;
	
	@RequestMapping(value = "/saveAnesthesiaTypeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveAnesthesiaTypeMaster(@RequestBody AnesthesiaTypeMasterDto anesthesiaTypeMasterDto){
		
		try {
			return iAnesthesiaTypeService.saveAnesthesiaTypeMaster(anesthesiaTypeMasterDto);
			
		} catch (Exception e) {
		logger.error("saveAnesthesiaTypeMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateAnesthesiaTypeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateAnesthesiaTypeMaster(@RequestBody AnesthesiaTypeMasterDto anesthesiaTypeMasterDto){
		
		try {
			return iAnesthesiaTypeService.updateAnesthesiaTypeMaster(anesthesiaTypeMasterDto);
			
		} catch (Exception e) {
		logger.error("updateAnesthesiaTypeMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/updateAnesthesiaTypeMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateAnesthesiaTypeMasterStatus(@RequestBody AnesthesiaTypeMasterDto anesthesiaTypeMasterDto){
		
		try {
			return iAnesthesiaTypeService.updateAnesthesiaTypeMasterStatus(anesthesiaTypeMasterDto);
			
		} catch (Exception e) {
		logger.error("updateAnesthesiaTypeMasterStatus failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getListAnesthesiaTypeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getListAnesthesiaTypeMaster(@RequestBody AnesthesiaTypeMasterDto anesthesiaTypeMasterDto){
		
		try {
			return iAnesthesiaTypeService.getListAnesthesiaTypeMaster(anesthesiaTypeMasterDto);
			
		} catch (Exception e) {
		logger.error("getListAnesthesiaTypeMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getAnesthesiaTypeMasterById/{anesthesiaTypeId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public Response getAnesthesiaTypeMasterById(@PathVariable ("anesthesiaTypeId") Integer anesthesiaTypeId,
												@PathVariable ("orgId") Integer orgId,
												@PathVariable ("unitId") Integer unitId){
											
		try {
			return iAnesthesiaTypeService.getAnesthesiaTypeMasterById(anesthesiaTypeId, orgId, unitId);
			
		} catch (Exception e) {
		logger.error("getAnesthesiaTypeMasterById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getAnesthesiaTypeMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAnesthesiaTypeMasterCount(@RequestBody AnesthesiaTypeMasterDto anesthesiaTypeMasterDto){
		
		try {
			return iAnesthesiaTypeService.getAnesthesiaTypeMasterCount(anesthesiaTypeMasterDto);
			
		} catch (Exception e) {
		logger.error("getAnesthesiaTypeMasterCount failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
}

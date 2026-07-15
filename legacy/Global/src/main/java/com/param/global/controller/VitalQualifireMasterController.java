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
import com.param.global.dto.VitalQualifireMasterDto;
import com.param.global.service.IVitalQualifireMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global")
@SuppressWarnings({"rawtypes"})
public class VitalQualifireMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(VitalQualifireMasterController.class);

	@Autowired
	private IVitalQualifireMasterService iVitalQualifireMasterService;
	
	@RequestMapping(value = "/saveVitalQualifireMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveVitalQualifireMaster(@RequestBody VitalQualifireMasterDto vitalQualifireMasterDto){
		
		try {
			return iVitalQualifireMasterService.saveVitalQualifireMaster(vitalQualifireMasterDto);
			
		} catch (Exception e) {
		logger.error("saveVitalQualifireMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateVitalQualifireMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateVitalQualifireMaster(@RequestBody VitalQualifireMasterDto vitalQualifireMasterDto){
		
		try {
			return iVitalQualifireMasterService.updateVitalQualifireMaster(vitalQualifireMasterDto);
			
		} catch (Exception e) {
		logger.error("updateVitalQualifireMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateVitalQualifireMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateVitalQualifireMasterStatus(@RequestBody VitalQualifireMasterDto vitalQualifireMasterDto){
		
		try {
			return iVitalQualifireMasterService.updateVitalQualifireMasterStatus(vitalQualifireMasterDto);
			
		} catch (Exception e) {
		logger.error("updateVitalQualifireMasterStatus failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getVitalQualifireMasterById/{vitalQualifireId}/{organizationId}", method = RequestMethod.GET)
	public Response getVitalQualifireMasterById(@PathVariable ("vitalQualifireId") Integer vitalQualifireId,
									@PathVariable ("organizationId") Integer organizationId){
		
		try {
			return iVitalQualifireMasterService.getVitalQualifireMasterById(vitalQualifireId, organizationId);
			
		} catch (Exception e) {
		logger.error("getVitalQualifireMasterById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getVitalQualifireMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVitalQualifireMasterList(@RequestBody VitalQualifireMasterDto vitalQualifireMasterDto){

		try {
			return iVitalQualifireMasterService.getVitalQualifireMasterList(vitalQualifireMasterDto);

		} catch (Exception e) {
		logger.error("getVitalQualifireMasterList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getVitalQualifireMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVitalQualifireMasterCount(@RequestBody VitalQualifireMasterDto vitalQualifireMasterDto){
		
		try {
			return iVitalQualifireMasterService.getVitalQualifireMasterCount(vitalQualifireMasterDto);
			
		} catch (Exception e) {
		logger.error("getVitalQualifireMasterCount failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveVitalQualifireMaster/{organizationId}", method = RequestMethod.GET)
	public Response getActiveVitalQualifireMaster(@PathVariable ("organizationId") Integer organizationId){
		
		try {
			return iVitalQualifireMasterService.getActiveVitalQualifireMaster(organizationId);
			
		} catch (Exception e) {
		logger.error("getActiveVitalQualifireMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getVitalCategoryList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVitalCategoryList(@RequestBody VitalQualifireMasterDto vitalQualifireMasterDto){
		
		try {
			return iVitalQualifireMasterService.getVitalCategoryList(vitalQualifireMasterDto);
			
		} catch (Exception e) {
		logger.error("getVitalCategoryList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}

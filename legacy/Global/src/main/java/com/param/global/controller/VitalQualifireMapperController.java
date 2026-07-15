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
import com.param.global.dto.VitalQualifireMapperDto;
import com.param.global.service.IVitalQualifireMapperService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global")
@SuppressWarnings({"rawtypes"})
public class VitalQualifireMapperController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(VitalQualifireMapperController.class);

	@Autowired
	private IVitalQualifireMapperService iVitalQualifireMapperService;
	
	@RequestMapping(value = "/saveVitalQualifireMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveVitalQualifireMapper(@RequestBody VitalQualifireMapperDto vitalQualifireMapperDto){
		
		try {
			return iVitalQualifireMapperService.saveVitalQualifireMapper(vitalQualifireMapperDto);
			
		} catch (Exception e) {
		logger.error("saveVitalQualifireMapper failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateVitalQualifireMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateVitalQualifireMapper(@RequestBody VitalQualifireMapperDto vitalQualifireMapperDto){
		
		try {
			return iVitalQualifireMapperService.updateVitalQualifireMapper(vitalQualifireMapperDto);
			
		} catch (Exception e) {
		logger.error("updateVitalQualifireMapper failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateVitalQualifireMapperStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateVitalQualifireMapperStatus(@RequestBody VitalQualifireMapperDto vitalQualifireMapperDto){
		
		try {
			return iVitalQualifireMapperService.updateVitalQualifireMapperStatus(vitalQualifireMapperDto);
			
		} catch (Exception e) {
		logger.error("updateVitalQualifireMapperStatus failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
//	@RequestMapping(value = "/getVitalQualifireMapperById/{vitalQualifireId}/{organizationId}", method = RequestMethod.GET)
//	public Response getVitalQualifireMapperById(@PathVariable ("vitalQualifireId") Integer vitalQualifireId,
//									@PathVariable ("organizationId") Integer organizationId){
//		
//		try {
//			return iVitalQualifireMapperService.getVitalQualifireMapperById(vitalQualifireId, organizationId);
//			
//		} catch (Exception e) {
//			
//			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
//		}
//	}
	
	
	@RequestMapping(value = "/getVitalQualifireMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVitalQualifireMapperList(@RequestBody VitalQualifireMapperDto vitalQualifireMapperDto){

		try {
			return iVitalQualifireMapperService.getVitalQualifireMapperList(vitalQualifireMapperDto);

		} catch (Exception e) {
		logger.error("getVitalQualifireMapperList failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getVitalQualifireMapperCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVitalQualifireMapperCount(@RequestBody VitalQualifireMapperDto vitalQualifireMapperDto){
		
		try {
			return iVitalQualifireMapperService.getVitalQualifireMapperCount(vitalQualifireMapperDto);
			
		} catch (Exception e) {
		logger.error("getVitalQualifireMapperCount failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveVitalQualifireMapper/{organizationId}", method = RequestMethod.GET)
	public Response getActiveVitalQualifireMapper(@PathVariable ("organizationId") Integer organizationId){
		
		try {
			return iVitalQualifireMapperService.getActiveVitalQualifireMapper(organizationId);
			
		} catch (Exception e) {
		logger.error("getActiveVitalQualifireMapper failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getVitalQualifireListByCategoryId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVitalQualifireListByCategoryId(@RequestBody VitalQualifireMapperDto vitalQualifireMapperDto){

		try {
			return iVitalQualifireMapperService.getVitalQualifireListByCategoryId(vitalQualifireMapperDto);

		} catch (Exception e) {
		logger.error("getVitalQualifireListByCategoryId failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getVitalQualifireListByVitalId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVitalQualifireListByVitalId(@RequestBody VitalQualifireMapperDto vitalQualifireMapperDto){

		try {
			return iVitalQualifireMapperService.getVitalQualifireListByVitalId(vitalQualifireMapperDto);

		} catch (Exception e) {
		logger.error("getVitalQualifireListByVitalId failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getVitalQualifireMapperListById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVitalQualifireMapperListById(@RequestBody VitalQualifireMapperDto vitalQualifireMapperDto){

		try {
			return iVitalQualifireMapperService.getVitalQualifireMapperListById(vitalQualifireMapperDto);

		} catch (Exception e) {
		logger.error("getVitalQualifireMapperListById failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}

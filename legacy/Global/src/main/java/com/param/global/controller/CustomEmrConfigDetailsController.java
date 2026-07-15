package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CustomEMRConfigurationDetailsDto;
import com.param.global.service.ICustomEmrConfigDetailsService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global/unit")
@SuppressWarnings({"rawtypes"})
public class CustomEmrConfigDetailsController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(CustomEmrConfigDetailsController.class);

	@Autowired
	private ICustomEmrConfigDetailsService iCustomEmrConfigDetailsService;
	
	@RequestMapping(value = "/getCustomEMRCategorySubcategoryList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCustomEMRCategorySubcategoryList(@RequestBody CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto){
		
		try {
			return iCustomEmrConfigDetailsService.getCustomEMRCategorySubcategoryList(customEMRConfigurationDetailsDto);
			
		} catch (Exception e) {
		logger.error("getCustomEMRCategorySubcategoryList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateCustomEMRCategorySubcategory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateCustomEMRCategorySubcategory(@RequestBody CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto){
		
		try {
			return iCustomEmrConfigDetailsService.updateCustomEMRCategorySubcategory(customEMRConfigurationDetailsDto);
			
		} catch (Exception e) {
		logger.error("updateCustomEMRCategorySubcategory failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getAllListoCustomEMRByPatientId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllListoCustomEMRByPatientId(@RequestBody CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto){
		
		try {
			return iCustomEmrConfigDetailsService.getAllListoCustomEMRByPatientId(customEMRConfigurationDetailsDto);
			
		} catch (Exception e) {
		logger.error("getAllListoCustomEMRByPatientId failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
}

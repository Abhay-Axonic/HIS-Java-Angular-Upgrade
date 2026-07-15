package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.PatientTabularTemplateDataMapperDto;
import com.param.global.unit.dto.TabularTemplateDataMapperDto;
import com.param.global.unit.dto.TabularTemplateMasterDto;
import com.param.global.unit.service.ITabularTemplateDataMapperService;
import com.param.global.unit.service.ITabularTemplateMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global/unit/template/tabular/mapper")
@SuppressWarnings({"rawtypes"})
public class TabularTemplateDataMapperController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(TabularTemplateDataMapperController.class);

	@Autowired
	private ITabularTemplateDataMapperService iTabularTemplateDataMapperService;
	

	@RequestMapping(value = "/saveTabularTemplateDataMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveTabularTemplateDataMapper(@RequestBody TabularTemplateDataMapperDto tabularTemplateDataMapperDto){
		
		try {
			return iTabularTemplateDataMapperService.saveTabularTemplateDataMapper(tabularTemplateDataMapperDto);
			
		} catch (Exception e) {
		logger.error("saveTabularTemplateDataMapper failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateTabularTemplateDataMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateTabularTemplateDataMapper(@RequestBody TabularTemplateDataMapperDto TabularTemplateDataMapperDto){
		
		try {
			return iTabularTemplateDataMapperService.updateTabularTemplateDataMapper(TabularTemplateDataMapperDto);
			
		} catch (Exception e) {
		logger.error("updateTabularTemplateDataMapper failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getTabularTemplateDataMapperById/{tabularTemplateId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getTabularTemplateDataMapperById(@PathVariable ("tabularTemplateId") Integer tabularTemplateId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		
		try {
			return iTabularTemplateDataMapperService.getTabularTemplateDataMapperById(tabularTemplateId, organizationId, unitId);
			
		} catch (Exception e) {
		logger.error("getTabularTemplateDataMapperById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/savePatientTabularTemplateDataMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePatientTabularTemplateDataMapper(@RequestBody PatientTabularTemplateDataMapperDto patientTabularTemplateDataMapperDto){
		
		try {
			return iTabularTemplateDataMapperService.savePatientTabularTemplateDataMapper(patientTabularTemplateDataMapperDto);
			
		} catch (Exception e) {
		logger.error("savePatientTabularTemplateDataMapper failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getPatientTabularTemplateDataMapperById/{tabularTemplateId}/{organizationId}/{unitId}/{patientId}/{templateTypeId}", method = RequestMethod.GET)
	public Response getPatientTabularTemplateDataMapperById(@PathVariable ("tabularTemplateId") Integer tabularTemplateId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId,
									@PathVariable ("patientId") Integer patientId,
									@PathVariable ("templateTypeId") Integer templateTypeId){
		
		try {
			return iTabularTemplateDataMapperService.getPatientTabularTemplateDataMapperById(tabularTemplateId, organizationId, unitId ,patientId,templateTypeId);
			
		} catch (Exception e) {
		logger.error("getPatientTabularTemplateDataMapperById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

}

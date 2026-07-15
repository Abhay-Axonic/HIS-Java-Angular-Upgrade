package com.param.global.unit.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dto.TabularTemplateMasterDto;
import com.param.global.unit.dto.TabularTemplateMasterDto;
import com.param.global.unit.service.ITabularTemplateMasterService;

@RestController
@RequestMapping(value="/api/global/unit/template/tabular")
@SuppressWarnings({"rawtypes"})
public class TabularTemplateMasterController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(TabularTemplateMasterController.class);

	@Autowired
	private ITabularTemplateMasterService iTabularTemplateMasterService;

	@RequestMapping(value = "/saveTabularTemplateMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveTabularTemplateMaster(@RequestBody TabularTemplateMasterDto tabularTemplateMasterDto){
		
		try {
			return iTabularTemplateMasterService.saveTabularTemplateMaster(tabularTemplateMasterDto);

		} catch (Exception e) {
			logger.error("saveTabularTemplateMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateTabularTemplateMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateTabularTemplateMaster(@RequestBody TabularTemplateMasterDto tabularTemplateMasterDto){
		
		try {
			return iTabularTemplateMasterService.updateTabularTemplateMaster(tabularTemplateMasterDto);

		} catch (Exception e) {
			logger.error("updateTabularTemplateMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateTabularTemplateMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateTabularTemplateMasterStatus(@RequestBody TabularTemplateMasterDto tabularTemplateMasterDto){
		
		try {
			return iTabularTemplateMasterService.updateTabularTemplateMasterStatus(tabularTemplateMasterDto);

		} catch (Exception e) {
			logger.error("updateTabularTemplateMasterStatus failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getTabularTemplateMasterById/{tabularTemplateId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getTabularTemplateMasterById(@PathVariable ("tabularTemplateId") Integer tabularTemplateId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		
		try {
			return iTabularTemplateMasterService.getTabularTemplateMasterById(tabularTemplateId, organizationId, unitId);
			
		} catch (Exception e) {
		logger.error("getTabularTemplateMasterById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getTabularTemplateMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTabularTemplateMasterList(@RequestBody TabularTemplateMasterDto tabularTemplateMasterDto){

		try {
			return iTabularTemplateMasterService.getTabularTemplateMasterList(tabularTemplateMasterDto);

		} catch (Exception e) {
		logger.error("getTabularTemplateMasterList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getTabularTemplateMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTabularTemplateMasterCount(@RequestBody TabularTemplateMasterDto tabularTemplateMasterDto){
		
		try {
			return iTabularTemplateMasterService.getTabularTemplateMasterCount(tabularTemplateMasterDto);
			
		} catch (Exception e) {
		logger.error("getTabularTemplateMasterCount failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveTabularTemplateMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveTabularTemplateMaster(@PathVariable ("organizationId") Integer organizationId,
										 @PathVariable ("unitId") Integer unitId){
		
		try {
			return iTabularTemplateMasterService.getActiveTabularTemplateMaster(organizationId, unitId);
			
		} catch (Exception e) {
		logger.error("getActiveTabularTemplateMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}


}

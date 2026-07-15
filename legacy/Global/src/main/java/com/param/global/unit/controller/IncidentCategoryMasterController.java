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
import com.param.global.unit.dto.IncidentCategoryMasterDto;
import com.param.global.unit.service.IIncidentCategoryMasterServiceImpl;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global/unit")
@SuppressWarnings({"rawtypes"})

public class IncidentCategoryMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(IncidentCategoryMasterController.class);

	
	@Autowired
	private IIncidentCategoryMasterServiceImpl iIncidentCategoryMasterServiceImpl;
	
	
	@RequestMapping(value = "/saveIncidentCategoryMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveIncidentCategoryMaster(@RequestBody IncidentCategoryMasterDto incidentCategoryMasterDto){
		
		try {
			return iIncidentCategoryMasterServiceImpl.saveIncidentCategoryMaster(incidentCategoryMasterDto);
			
		} catch (Exception e) {
		logger.error("saveIncidentCategoryMaster failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateIncidentCategoryMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateIncidentCategoryMaster(@RequestBody IncidentCategoryMasterDto incidentCategoryMasterDto){
		
		try {
			return iIncidentCategoryMasterServiceImpl.updateIncidentCategoryMaster(incidentCategoryMasterDto);
			
		} catch (Exception e) {
		logger.error("updateIncidentCategoryMaster failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateIncidentCategoryMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateIncidentCategoryMasterStatus(@RequestBody IncidentCategoryMasterDto incidentCategoryMasterDto){
		
		try {
			return iIncidentCategoryMasterServiceImpl.updateIncidentCategoryMasterStatus(incidentCategoryMasterDto);
			
		} catch (Exception e) {
		logger.error("updateIncidentCategoryMasterStatus failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getIncidentCategoryMasterById/{incidentCategoryId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getIncidentCategoryMasterById(@PathVariable ("incidentCategoryId") Integer incidentCategoryId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		
		try {
			return iIncidentCategoryMasterServiceImpl.getIncidentCategoryMasterById(incidentCategoryId, organizationId, unitId);
			
		} catch (Exception e) {
		logger.error("getIncidentCategoryMasterById failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getIncidentCategoryMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIncidentCategoryMasterList(@RequestBody IncidentCategoryMasterDto incidentCategoryMasterDto){

		try {
			return iIncidentCategoryMasterServiceImpl.getIncidentCategoryMasterList(incidentCategoryMasterDto);

		} catch (Exception e) {
		logger.error("getIncidentCategoryMasterList failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getIncidentCategoryMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIncidentCategoryMasterCount(@RequestBody IncidentCategoryMasterDto incidentCategoryMasterDto){
		
		try {
			return iIncidentCategoryMasterServiceImpl.getIncidentCategoryMasterCount(incidentCategoryMasterDto);
			
		} catch (Exception e) {
		logger.error("getIncidentCategoryMasterCount failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveIncidentCategoryMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveIncidentCategoryMaster(@PathVariable ("organizationId") Integer organizationId,
										 @PathVariable ("unitId") Integer unitId){
		
		try {
			return iIncidentCategoryMasterServiceImpl.getActiveIncidentCategoryMaster(organizationId, unitId);
			
		} catch (Exception e) {
		logger.error("getActiveIncidentCategoryMaster failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	

}

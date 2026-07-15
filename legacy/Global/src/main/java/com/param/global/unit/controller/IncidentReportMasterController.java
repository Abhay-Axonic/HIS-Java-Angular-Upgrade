package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.IncidentReportMasterDto;
import com.param.global.unit.service.IIncidentReportMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping("/api/unit")
public class IncidentReportMasterController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(IncidentReportMasterController.class);

	@Autowired
	private IIncidentReportMasterService iIncidentReportMasterService;
	
	
	@RequestMapping(value = "/saveIncidentReportMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveIncidentReportMaster(@RequestBody IncidentReportMasterDto incidentReportingMapperDto){
		try{
			return iIncidentReportMasterService.saveIncidentReportMaster(incidentReportingMapperDto);
			
		}catch(Exception e){
		logger.error("saveIncidentReportMaster failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/updateIncidentReportMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateIncidentReportMaster(@RequestBody IncidentReportMasterDto incidentReportingMapperDto){
		try{
			return iIncidentReportMasterService.updateIncidentReportMaster(incidentReportingMapperDto);
			
		}catch(Exception e){
		logger.error("updateIncidentReportMaster failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getIncidentReportMasterById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIncidentReportMasterById(@RequestBody IncidentReportMasterDto incidentReportingMapperDto){
		try{
			return iIncidentReportMasterService.getIncidentReportMasterById(incidentReportingMapperDto);
			
		}catch(Exception e){
		logger.error("getIncidentReportMasterById failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getIncidentReportMasterList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIncidentReportMasterList(@RequestBody IncidentReportMasterDto incidentReportingMapperDto){
		try{
			return iIncidentReportMasterService.getIncidentReportMasterList(incidentReportingMapperDto);
			
		}catch(Exception e){
		logger.error("getIncidentReportMasterList failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getTemplateByIncidentReportId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTemplateByIncidentReportId(@RequestBody IncidentReportMasterDto incidentReportingMapperDto){
		try{
			return iIncidentReportMasterService.getTemplateByIncidentReportId(incidentReportingMapperDto);
			
		}catch(Exception e){
		logger.error("getTemplateByIncidentReportId failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getRoleByIncidentReportId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getRoleByIncidentReportId(@RequestBody IncidentReportMasterDto incidentReportingMapperDto){
		try{
			return iIncidentReportMasterService.getRoleByIncidentReportId(incidentReportingMapperDto);
			
		}catch(Exception e){
		logger.error("getRoleByIncidentReportId failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	@RequestMapping(value = "/updateIncidentReportMasterStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateIncidentReportMasterStatus(@RequestBody IncidentReportMasterDto incidentReportingMapperDto){
		try{
			return iIncidentReportMasterService.updateIncidentReportMasterStatus(incidentReportingMapperDto);
			
		}catch(Exception e){
		logger.error("updateIncidentReportMasterStatus failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveIncidentReportMasterList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveIncidentReportMasterList(@RequestBody IncidentReportMasterDto incidentReportingMapperDto){
		try{
		return iIncidentReportMasterService.getActiveIncidentReportMasterList(incidentReportingMapperDto);
	
		}catch(Exception e){
		logger.error("getActiveIncidentReportMasterList failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getIncidentReportMasterCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIncidentReportMasterCount(@RequestBody IncidentReportMasterDto incidentReportingMapperDto){
			try{
			return iIncidentReportMasterService.getIncidentReportMasterCount(incidentReportingMapperDto);
		
			}catch(Exception e){
			logger.error("getIncidentReportMasterCount failed", e);
				return new Response<>(ERROR, null, null, null, null);
			}
	}
}

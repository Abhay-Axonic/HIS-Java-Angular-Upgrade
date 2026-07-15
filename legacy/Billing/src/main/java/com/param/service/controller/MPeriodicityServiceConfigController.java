package com.param.service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.Response;
import com.param.service.dto.MPeriodicityServiceConfigDto;
import com.param.service.services.IMPeriodicityServiceConfigService;

@RestController
@RequestMapping(value = "/api/periodicity")
@SuppressWarnings({"rawtypes"})
public class MPeriodicityServiceConfigController {

	@Autowired
	private IMPeriodicityServiceConfigService iMPeriodicityServiceConfigService;
	
	@RequestMapping(value = "/config" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePeriodicityConfig(@RequestBody MPeriodicityServiceConfigDto dto){
		try{
			return iMPeriodicityServiceConfigService.saveMPeriodicityServiceConfig(dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/configs/{orgId}/{unitId}" , method = RequestMethod.GET)
	public Response getPeriodicityConfigList(@PathVariable("orgId")int orgId, @PathVariable("unitId")int unitId){
		try{
			return iMPeriodicityServiceConfigService.getMPeriodicityList(orgId, unitId);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/config/status/{id}/{status}" , method = RequestMethod.GET)
	public Response updatePeriodicityConfigStatus(@PathVariable("id")int id, @PathVariable("status")char status){
		try{
			return iMPeriodicityServiceConfigService.updateMPeriodicityStatusById(id, status);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/config" , method = RequestMethod.PUT , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updatePeriodicityConfig(@RequestBody MPeriodicityServiceConfigDto dto){
		try{
			return iMPeriodicityServiceConfigService.updateMPeriodicityServiceConfig(dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/patients/{orgId}/{unitId}/{visitTypeId}/{name}" , method = RequestMethod.GET)
	public Response searchPatientForPeriodicity(@PathVariable Map<String, String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId"));
			int unitId = Integer.parseInt(params.get("unitId"));
			int visitTypeId = Integer.parseInt(params.get("visitTypeId"));
			String name = params.get("name");
			return iMPeriodicityServiceConfigService.searchPatientForPServiceOrder(visitTypeId, orgId, unitId, name);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/config/avail/{serviceId}/{orgId}/{unitId}/{visitTypeId}" , method = RequestMethod.GET)
	public Response validatePServiceConfigAvail(@PathVariable Map<String, String> params){
		try{
			int visitTypeId = Integer.parseInt(params.get("visitTypeId"));;
			int serviceId = Integer.parseInt(params.get("serviceId"));
			int orgId = Integer.parseInt(params.get("orgId"));
			int unitId = Integer.parseInt(params.get("unitId"));
			return iMPeriodicityServiceConfigService.validatePServiceConfigAvail(serviceId, orgId, unitId,visitTypeId);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/config/getRate/{serviceId}/{orgId}/{unitId}/{visitTypeId}/{periodicityServiceConfigId}" , method = RequestMethod.GET)
	public Response getRatePServiceConfig(@PathVariable Map<String, String> params){
		try{
			int visitTypeId = Integer.parseInt(params.get("visitTypeId"));;
			int serviceId = Integer.parseInt(params.get("serviceId"));
			int orgId = Integer.parseInt(params.get("orgId"));
			int unitId = Integer.parseInt(params.get("unitId"));
			int periodicityServiceConfigId = Integer.parseInt(params.get("periodicityServiceConfigId"));
			return iMPeriodicityServiceConfigService.getRatePServiceConfig(serviceId, orgId, unitId,visitTypeId,periodicityServiceConfigId);
		}catch(Exception e){
			
			throw e;
		}
	}
}

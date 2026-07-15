package com.param.lis.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.lis.global.dto.TemplateServiceMapperDto;
import com.param.lis.global.service.ITemplateServiceMapperService;

@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping("/api/lis/global")
public class TemplateServiceMapperController implements ICommonConstants {

	
	@Autowired
	private ITemplateServiceMapperService iTemplateServiceMapperService;
	
	
	@RequestMapping(value = "/saveTemplateServiceMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveTemplateServiceMapper(@RequestBody TemplateServiceMapperDto templateServiceMapperDto){
		try{
			return iTemplateServiceMapperService.saveTemplateServiceMapper(templateServiceMapperDto);
			
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/updateTemplateServiceMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateTemplateServiceMapper(@RequestBody TemplateServiceMapperDto templateServiceMapperDto){
		try{
			
			return iTemplateServiceMapperService.updateTemplateServiceMapper(templateServiceMapperDto);
			
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/updateTemplateServiceMapperStatus/{serviceId}/{organizationId}/{unitId}/{status}" , method = RequestMethod.GET)
	public Response updateTemplateServiceMapperStatus(@PathVariable ("serviceId") Integer serviceId,
														@PathVariable ("organizationId") Integer organizationId,
														@PathVariable ("unitId") Integer unitId,
														@PathVariable ("status") Character status){
		try{
			return iTemplateServiceMapperService.updateTemplateServiceMapperStatus(serviceId, organizationId, unitId, status);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/getTemplateServiceMapperlist" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE )
	public Response getTemplateServiceMapperlist(@RequestBody TemplateServiceMapperDto templateServiceMapperDto){
		try{
			return iTemplateServiceMapperService.getTemplateServiceMapperlist(templateServiceMapperDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/getTemplateServiceMapperCount/{organizationId}/{unitId}" , method = RequestMethod.GET )
	public Response getTemplateServiceMapperCount(@PathVariable ("organizationId") Integer organizationId,
											  @PathVariable ("unitId") Integer unitId){
		try{
			return iTemplateServiceMapperService.getTemplateServiceMapperCount(organizationId, unitId);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getActiveTemplateServiceMapper/{organizationId}/{unitId}" , method = RequestMethod.GET )
	public Response getActiveTemplateServiceMapper(@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId){
		try{
				return iTemplateServiceMapperService.getActiveTemplateServiceMapper(organizationId, unitId);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getTemplateServiceMapperById/{serviceId}/{organizationId}/{unitId}" , method = RequestMethod.GET)
	public Response getTemplateServiceMapperById(@PathVariable ("serviceId") Integer serviceId,
														@PathVariable ("organizationId") Integer organizationId,
														@PathVariable ("unitId") Integer unitId){
		try{
			return iTemplateServiceMapperService.getTemplateServiceMapperById(serviceId, organizationId, unitId);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/getDetailedServiceList/{serviceId}/{organizationId}/{unitId}" , method = RequestMethod.GET)
	public Response getDetailedServiceList(@PathVariable ("serviceId") Integer serviceId,
																					@PathVariable ("organizationId") Integer organizationId,
																					@PathVariable ("unitId") Integer unitId){
		try{
			return iTemplateServiceMapperService.getDetailedServiceList(serviceId, organizationId,unitId);
		}catch(Exception e){
			
			throw e;
		}
	}

}

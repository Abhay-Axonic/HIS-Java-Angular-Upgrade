package com.param.adt.master.global.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.adt.master.global.dto.ICPCCodesConfigurationMapperDto;
import com.param.adt.master.global.service.IICPCCodesConfigurationMapperService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping("api/adt")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ICPCCodesConfigurationMapperController implements ICommonConstants {

	@Autowired
	private IICPCCodesConfigurationMapperService iICPCCodesConfigurationMapperService;

	@RequestMapping(value = "/saveICPCCodesConfigurationMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveICPCCodesConfigurationMapper(@RequestBody  ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto) {
		try {
			return iICPCCodesConfigurationMapperService.saveICPCCodesConfigurationMapper(iCPCCodesConfigurationMapperDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getICPCCodesConfigurationMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getICPCCodesConfigurationMapperList(@RequestBody  ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto) {
		try {
			return iICPCCodesConfigurationMapperService.getICPCCodesConfigurationMapperList(iCPCCodesConfigurationMapperDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getICPCSystemList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getICPCSystemList(@RequestBody  ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto) {
		try {
			return iICPCCodesConfigurationMapperService.getICPCSystemList(iCPCCodesConfigurationMapperDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateICPCCodesConfigurationStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateICPCCodesConfigurationStatus(@RequestBody  ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto) {
		try {
			return iICPCCodesConfigurationMapperService.updateICPCCodesConfigurationStatus(iCPCCodesConfigurationMapperDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getSpecialityListByIcpcSystemId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getSpecialityListByIcpcSystemId(@RequestBody  ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto) {
		try {
			return iICPCCodesConfigurationMapperService.getSpecialityListByIcpcSystemId(iCPCCodesConfigurationMapperDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
}
